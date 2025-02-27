<template>

  <div class="content">
    <div class="container-fluid">
      <!-- 페이지 헤더 -->
      <div class="card">
        <div class="card-body">
          <h4 class="card-title float-left">출퇴근 정정 요청 조회</h4>
        </div>
      </div>
  
      <!-- 바디 -->
      <div class="card">
        <div class="card-body">

          <div class="row">
            <div class="col">
              <!-- 조회 조건 -->
              <div class="row search align-items-center justify-content-end">
                <div class="col-auto">
                  <select class="input-custom" v-model="searchData.searchState">
                    <option value="" selected>결재상태</option>
                    <option value="D01">결재대기</option>
                    <option value="D02">승인</option>
                    <option value="D03">보완</option>
                    <option value="D04">반려</option>
                  </select>
                </div>
      
                <div class="col-auto">
                  <input type="date" id="startDate" class="input-custom" v-model="searchData.startDate">
                  <a class="align-middle"> ~ </a>
                  <input type="date" id="endDate" class="input-custom" v-model="searchData.endDate">
                </div>
              </div>
      
              <!-- 목록 -->
              <div class="row mt-3">
                <div class="col">
                  <div id="crctGrid"></div>

                </div>
              </div>
      
            </div>
          </div>
        </div>
      </div>
      
    </div>
  </div>

</template>

<script setup>
import axios from 'axios';
import Grid from 'tui-grid';
import { ref, onBeforeUnmount, onMounted, watch } from 'vue';
import { dateTimeFormat } from '../../assets/js/common.js';
import { useRouter } from 'vue-router';

let gridInstance = ref();
let rowData = ref([]);

// 검색 데이터
const searchData = ref({
  createId: "user01",
  startDate: '',
  endDate: '',
  searchState: ''
});
const crctGetList = async () => {
  const result = await axios.get('/api/commute/crctList', { params : searchData.value });
  rowData.value = result.data;

  rowData.value.forEach((i) => {
    i.commuteDt = dateTimeFormat(i.commuteDt, 'yyyy-MM-dd');
    i.preGoTime = dateTimeFormat(i.preGoTime, 'MM/dd hh:mm');
    i.preLeaveTime = dateTimeFormat(i.preLeaveTime, 'MM/dd hh:mm');
    i.crctGoTime = dateTimeFormat(i.crctGoTime, 'MM/dd hh:mm');
    i.crctLeaveTime = dateTimeFormat(i.crctLeaveTime, 'MM/dd hh:mm');
    i.createDt = dateTimeFormat(i.createDt, 'yyyy-MM-dd');
  })

  gridInstance.value.resetData(rowData.value);
}

watch(() => searchData, () => {
  crctGetList();
}, {deep:true});

// Toast UI Grid 초기화
onMounted(() => {
  gridInstance.value = new Grid({
    el: document.getElementById('crctGrid'),
    data: rowData.value,
    scrollX: false,
    scrollY: true,
    columns: [ //근무일자 / 출근시간 / 퇴근시간 / 정정출근시간 / 정정퇴근시간 / 신청일 / 결재자
      { header: '근무일자', name: 'commuteDt', align: 'center'},
      { header: '출근시간', name: 'preGoTime', align: 'center'},
      { header: '퇴근시간', name: 'preLeaveTime', align: 'center'},
      { header: '정정출근시간', name: 'crctGoTime', align: 'center'},
      { header: '정정퇴근시간', name: 'crctLeaveTime', align: 'center'},
      { header: '신청일', name: 'createDt', align: 'center'},
      { header: '결재자', name: 'signId', align: 'center'},
      { header: '결재상태', name: 'signState', align: 'center', renderer: BtnRenderer }
    ]
  })
  
  crctGetList();
})

// 그리드 버튼
class BtnRenderer {
  constructor(props) {
    const el = document.createElement("div");
    el.className = "btn-group";

    const signState = props.grid.getRow(props.rowKey).signState;

    if (signState === "D01") {
      el.innerHTML = `
        <button class="btn btn-success btn-fill cell-btn-custom me-2" data-type="wait">대기</button>
        <button class="btn btn-danger btn-fill cell-btn-custom" data-type="delete">삭제</button>
      `;
    } else if (signState === "D02") {
      el.innerHTML = `<span class="text-success fw-bold">승인</span>`;
    } else if (signState === "D03") {
      el.innerHTML = `
        <button class="btn btn-warning btn-fill cell-btn-custom me-2" data-type="edit">보완</button>
        <button class="btn btn-danger btn-fill cell-btn-custom" data-type="delete">삭제</button>
      `;
    } else if (signState === "D04") {
      el.innerHTML = `<span">반려</span>`;
    }

    el.addEventListener("click", (event) => {
      const type = event.target.dataset.type;

      // props.row가 없을 경우 grid에서 데이터 가져오기
      const rowKey = props.row?.rowKey ?? props.grid.getRow(props.rowKey)?.rowKey;

      if (rowKey === undefined) {
        console.error("BtnRenderer: rowKey를 가져올 수 없습니다.", props);
        return;
      }

      if (type === "wait") {
        btnCrctUpdate(rowKey);
      } else if (type === "edit") {
        btnCrctUpdate(rowKey);
      } else if (type === "delete") {
        btnCrctDelete(rowKey);
      }
    });

    this.el = el;
  }

  getElement() {
    return this.el;
  }
}

const router = useRouter();
const btnCrctUpdate = (rowKey) => {
  let selectedRowData = gridInstance.value.getRow(rowKey);
  
  router.push({ name: 'CrctManage', query: { crctCd: selectedRowData.crctCd, isUpdate: 'true' } });
};
const btnCrctDelete = async (rowKey) => {
  let crctCd = gridInstance.value.getRow(rowKey).crctCd;

  await axios.post(`/api/commute/crctRemove?crctCd=${crctCd}`);
  crctGetList();
};

// 페이지 이동 시 Grid 제거하여 중복 방지
onBeforeUnmount(() => {
  if (gridInstance.value) {
    gridInstance.value.destroy();
    gridInstance.value = null;
  }
});
</script>


<style scoped>
</style>

  