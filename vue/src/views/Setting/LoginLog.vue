<template>
   <div class="content">
      <div class="container-fluid">
         <card>
            <form @submit.prevent="loginLogGetList">
               <div class="mb-3 row">
                  <label for="staticEmail" class="col-sm-2 col-form-label">검색</label>
                  <div class="col-auto">
                     <select class="form-select" v-model="searchData.searchCondition">
                        <option value="1">아이디</option>
                        <option value="2">아이피</option>
                     </select>
                  </div>
                  <div class="col-auto">
                     <input type="text" class="form-control" placeholder="검색어를 입력해주세요"
                        v-model="searchData.searchKeyword">
                  </div>
               </div>
               <div class="mb-3 row">
                  <label for="inputPassword" class="col-sm-2 col-form-label">조회 기간</label>
                  <div class="col-auto">
                     <input type="date" class="form-control" v-model="searchData.searchStartDt">
                  </div>
                  <div class="col-auto">~</div>
                  <div class="col-auto">
                     <input type="date" class="form-control" v-model="searchData.searchEndDt">
                  </div>
               </div>
               <div class="text-center">
                  <button type="reset" class="btn btn-secondary btn-fill">
                     초기화
                  </button>
                  <button type="submit" class="btn btn-info btn-fill">
                     검색
                  </button>
               </div>
            </form>
         </card>
         <card>
            <div id="logGrid" class="project"></div>
         </card>
      </div>
   </div>
</template>

<script setup>
import axios from "../../assets/js/customAxios.js";
import Swal from 'sweetalert2';
import { ref, onMounted, onBeforeMount, watch } from 'vue';
import Card from '../../components/Cards/Card.vue'
import Grid from 'tui-grid';
import { dateFormat, dateTimeFormat } from '../../assets/js/common.js'

//---------------데이터--------------

let rowData = ref([]);

onBeforeMount(() => {
   loginLogGetList();
});

//검색조건
const searchData = ref({
   searchCondition: '1',
   searchKeyword: '',
   searchStartDt: dateFormat(),
   searchEndDt: dateFormat(),
});

//검색조건이 변경되면 리스트가 새로 로드됨
watch(() => searchData, () => {
   loginLogGetList();
}, { deep: true });

// Toast UI Grid 초기화
let gridInstance = ref();
onMounted(() => {
   gridInstance.value = new Grid({
      el: document.getElementById('logGrid'),
      data: rowData.value,
      scrollX: false,
      scrollY: true,
      columns: [
         { header: '번호', name: 'rowNum', align: 'center', width: 80 },
         { header: '사용자', name: 'logId', renderer: logMemInfo },
         { header: 'IP 주소', name: 'logIp', align: 'center' },
         { header: '성공여부', name: 'state', align: 'center', formatter: ({ row }) => `${row.state == 'A01' ? '로그인 성공' : '로그인 실패'}` },
         { header: "일정", name: "plan", align: "center", renderer: BtnRenderer },
         { header: '날짜', name: 'createDt', align: 'center', sortable: true }
      ],
      pageOptions: {
         useClient: false,
         perPage: 5,
      },
      rowHeight: 50
   })
})

//===================== Toast Grid Rendere =====================

class logMemInfo {
   constructor(props) {
      const rowKey = props.row?.rowKey ?? props.grid.getRow(props.rowKey)?.rowKey;
      const rowData = props.grid.getRow(rowKey);

      const el = document.createElement("div");
      el.className = "mlp10";

      el.innerHTML = `
      <div class="subject">
         ${rowData.logId}
      </div>
      <div>
      [${rowData.deptNm}] ${rowData.mberNm} ${rowData.gradeNm != null ? rowData.gradeNm : ''}
      </div>
    `;

      this.el = el;
   }

   getElement() {
      return this.el;
   }
}

//로그인 잠금 해제
class BtnRenderer {
   constructor(props) {
      const rowKey = props.row?.rowKey ?? props.grid.getRow(props.rowKey)?.rowKey;
      const rowData = props.grid.getRow(rowKey);
      const el = document.createElement("div");
      el.className = "mlp10 mrp10";

      if (rowData.lockAt == 'A01') { //미완료 상태일 때 완료처리하기
         el.innerHTML = ` <button class="btn btn-danger btn-sm">계정잠금</button> `;
      } else { //완료 상태일 때 완료취소 처리하기
         el.innerHTML = ` <button class="btn btn-primary btn-sm">정상</button> `;
      }

      el.addEventListener("click", () => {
         btnLockChg(rowData.logId);
      });

      this.el = el;
   }

   getElement() {
      return this.el;
   }
}

//======================= 버튼이벤트 =======================
const btnLockChg = (logId) => {
   Swal.fire({
      title: "해당 계정을 잠금해제하시겠습니까?",
      icon: "question",
      showCancelButton: true,
      customClass: {
         confirmButton: "btn btn-secondary btn-fill",
         cancelButton: "btn btn-danger btn-fill"
      },
      confirmButtonText: "아니오",
      cancelButtonText: "예",
   }).then((result) => {
      if (result.dismiss == Swal.DismissReason.cancel) {
         lockChg(logId); //완료처리 함수
      }
   });
}

//---------------axios--------------

const logList = ref([]);
const loginLogGetList = async () => { //로그인 로그 전체조회
   try {
      const result = await axios.get('/api/comm/loginLog', { params: searchData.value });
      logList.value = result.data;

      rowData.value = [...logList.value];

      logList.value = result.data.map((item, index) => ({
         ...item,
         rowNum: index + 1,
         createDt: dateTimeFormat(item.createDt, 'yyyy-MM-dd hh:mm:ss') // 날짜 포맷 변환 적용
      }));

      if (gridInstance.value) {
         gridInstance.value.resetData(logList.value);
      }
   } catch (err) {
      logList.value = [];

      Swal.fire({
         icon: "error",
         title: "API 조회 오류",
         text: "Error : " + err
      });
   }
}

//계정 잠금해제
const lockChg = async (logId) => {

   try {
      await axios.put(`/api/comm/loginLog/${logId}`);

      Swal.fire({
         icon: "success",
         title: "상태변경 완료",
      });
      loginLogGetList();

   } catch (err) {
      Swal.fire({
         icon: "error",
         title: "상태변경 실패",
      })
   }
}

</script>
