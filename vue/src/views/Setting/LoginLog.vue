<template>
   <div class="content">
      <div class="container-fluid">
         <card>
            <h4 class="card-title float-left">접속기록 조회</h4>
         </card>
         <form @submit.prevent="loginLogGetList">
            <card>
               <ul class="nav nav-pills">
                  <li class="nav-item">
                     <a class="nav-link" :class="searchData.searchState == '' ? 'active' : ''"
                        @click="searchData.searchState = ''">전체</a>
                  </li>
                  <li class="nav-item">
                     <a class="nav-link" :class="searchData.searchState == 'A01' ? 'active' : ''"
                        @click="searchData.searchState = 'A01'">성공</a>
                  </li>
                  <li class="nav-item">
                     <a class="nav-link" :class="searchData.searchState == 'A02' ? 'active' : ''"
                        @click="searchData.searchState = 'A02'">실패</a>
                  </li>
               </ul>
            </card>
         </form>
         <card>
            <div class="row justify-content-between align-items-end mb-2">
               <div class="col-4"></div>
               <div class="selectbox col d-flex align-items-center">
               <select class="form-select w50" v-model="searchData.searchCondition">
                  <option value="0">아이디</option>
                  <option value="1">아이피</option>
               </select>

               <input type="text" class="form-control mlp10" placeholder="검색어를 입력해주세요" v-model="searchData.searchKeyword">
               <div class="input-group">
                  <span class="input-group-text fw-bold">시작일</span>
                  <input type="date" class="form-control w50" v-model="searchData.searchStartDt">
               </div>
               <span class="fw-bold">~</span>
               <div class="input-group">
                  <span class="input-group-text fw-bold">종료일</span>
                  <input type="date" class="form-control w50" v-model="searchData.searchEndDt">
               </div>
               <button class="btn btn-secondary btn-fill w30 mlp10">초기화</button>
               </div>
            </div>
            <div id="logGrid" class="project"></div>
         </card>
      </div>
   </div>
</template>

<script setup>
import { useStore } from "vuex";
import axios from "../../assets/js/customAxios.js";
import { ref, onMounted, computed, watch, onBeforeUnmount } from 'vue';

//========================== 컴포넌트 ==========================
import Grid from 'tui-grid';
import Swal from 'sweetalert2';
import Card from '../../components/Cards/Card.vue'

//============================= js =============================
import { dateFormat, dateTimeFormat } from '../../assets/js/common.js'

//========================= 데이터 =========================
const store = useStore();
const isCmt = computed(() => store.state.isCmt);
watch(isCmt, () => {
   loginLogGetList();
})

//========================= Toast grid =========================

//로그인 로그 전체조회
let gridInstance = ref();
const loginLogGetList = () => {
   gridInstance.value.readData(1, searchData.value);
}

//검색조건
const searchData = ref({
   searchState: '',
   searchCondition: '0',
   searchKeyword: '',
   searchStartDt: dateFormat(),
   searchEndDt: dateFormat()
});

//검색조건이 변경되면 리스트가 새로 로드됨
watch(searchData, () => {
   loginLogGetList();
}, { deep: true });



const token = localStorage.getItem("token");
const dataSource = {
   api: {
      readData: {
         url: '/api/comm/loginLog',
         method: 'GET',
         initParams: searchData,
         headers: { 'Authorization': `Bearer ${token}` }
      }
   }
};

// Toast UI Grid 초기화
onMounted(() => {

   if (gridInstance.value) {
      gridInstance.value.destroy(); // 기존 Grid 제거
   }

   gridInstance.value = new Grid({
      el: document.getElementById('logGrid'),
      data: dataSource,
      scrollX: false,
      scrollY: false,
      columns: [
         { header: '사용자', name: 'logId', renderer: logMemInfo },
         { header: 'IP 주소', name: 'logIp', align: 'center' },
         { header: '성공여부', name: 'state', align: 'center', 
           formatter: ({ row }) => {
               if (row.state == 'A01'){ return `<span class="point-blue">로그인 성공</span>`;}
               else {return `<span class="point-red">로그인 실패</span>`;}
               
            },
         },
         { header: "잠금상태", name: "plan", align: "center", renderer: BtnRenderer },
         { header: '날짜', name: 'createDt', align: 'center', sortable: true, formatter: ({ row }) => dateTimeFormat(row.createDt, 'yyyy-MM-dd hh:mm:ss') }
      ],
      pageOptions: {
         useClient: false,
         perPage: 15,
      },
      rowHeight: 50
   })
});

// 페이지 이동 시 Grid 제거하여 중복 방지
onBeforeUnmount(() => {
   if (gridInstance.value) {
      gridInstance.value.destroy();
      gridInstance.value = null;
   }
});

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
      ${rowData.deptNm != null ? '['+rowData.deptNm+']' : ''} 
      ${rowData.mberNm || ''} 
      ${rowData.gradeNm || ''}
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
         el.innerHTML = ` 정상 `;
      }

      el.addEventListener("click", () => {
         if (rowData.lockAt == 'A01') {
            btnLockChg(rowData.logId);
         }
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

/*
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
*/

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


<style scoped>
.input-group-text {
  background-color: #eee;
  color: #747474;
}
</style>