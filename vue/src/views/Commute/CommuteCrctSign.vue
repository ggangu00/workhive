<template>

  <div class="content">
    <div class="container-fluid">
      <!-- 페이지 헤더 -->
      <div class="card">
        <div class="card-body">
          <div class="row">
            <div class="col">
              <h4 class="card-title float-left mt-1">출퇴근 정정 요청 결재</h4>
            </div>
            <div class="col-auto d-flex align-items-center">
              <button class="btn btn-primary btn-header">승인</button>
              <button class="btn btn-warning btn-header">보완</button>
              <button class="btn btn-danger btn-header">반려</button>
            </div>
          </div>
        </div>
      </div>

      <!-- 결재 요청 목록 -->
      <div class="card">
        <div class="card-body">

          <div class="row">
            <div class="col">
              <!-- grid -->
              <div id="crctGrid"></div>

            </div>
          </div>

        </div>
      </div>


      <!-- 페이지 헤더 -->
      <div class="card">
        <div class="card-body">
          <div class="row">
            <div class="col">
              <h4 class="card-title float-left mt-1">출퇴근 정정 요청 결재 내역</h4>
            </div>
            <div class="col-auto d-flex align-items-center">
              <button class="btn btn-secondary btn-header">결재취소</button>
            </div>
          </div>
        </div>
      </div>

      <!-- 결재 내역 -->
      <div class="card">
        <div class="card-body">

          <div class="row">
            <div class="col">
              <!-- grid -->
              <div id="signGrid"></div>


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
import { ref, onMounted, onBeforeUnmount, watch } from 'vue';

// 그리드 인스턴스
let crctGridInstance = ref();
let signGridInstance = ref();

// 그리드 로우 데이터
let crctList = ref();
let signList = ref();

// 그리드 컬럼 데이터
let crctCol = [
  { header: '체크박스', name: ''},
  { header: '근무일자', name: ''},
  { header: '출근시간', name: ''},
  { header: '퇴근시간', name: ''},
  { header: '정정출근시간', name: ''},
  { header: '정정퇴근시간', name: ''},
  { header: '신청일', name: ''},
  { header: '신청자', name: ''},
];
let signCol = [
  { header: '체크박스', name: ''},
  { header: '근무일자', name: ''},
  { header: '출근시간', name: ''},
  { header: '퇴근시간', name: ''},
  { header: '정정출근시간', name: ''},
  { header: '정정퇴근시간', name: ''},
  { header: '신청일', name: ''},
  { header: '신청자', name: ''},
  { header: '결재일', name: ''},
  { header: '결재상태', name: ''},
];

// 조회 조건
const crctSrchData = ref({
  signId: 'admin01',
  startDate: '',
  endDate: '',
})
const signSrchData = ref({
  signId: 'admin01',
  startDate: '',
  endDate: '',
})

// 그리드 데이터 조회 메소드
const crctGetList = async () => {
  const result = await axios.get('/api/commute/signerList', { params : crctSrchData.value });
  crctList.value = result.data;

  crctGridInstance.value.resetData(crctList.value);
}
const signGetList = async () => {
  const result = await axios.get('/api/commute/signedList', { params : signSrchData.value });
  signList.value = result.data;

  signGridInstance.value.resetData(signList.value);
}

// 실시간 조회 조건
watch(() => crctSrchData, () => {
  crctGetList();
}, {deep:true});
watch(() => signSrchData, () => {
  signGetList();
}, {deep:true});

// Grid 초기화
const initGrid = (gridInstance, gridDiv, rowData, colData) => {
  gridInstance.value = new Grid({
    el: document.getElementById(gridDiv),
    data: rowData.value,
    scrollX: false,
    scrollY: true,
    columns: colData,
  });
};


// Toast Grid 초기화
onMounted(() => {
  initGrid(crctGridInstance, 'crctGrid', crctList, crctCol);
  initGrid(signGridInstance, 'signGrid', signList, signCol);
  
  crctGetList();
  signGetList();
});


// 컴포넌트가 파괴될 때 기존 Grid 삭제
onBeforeUnmount(() => {
  if (crctGridInstance.value) crctGridInstance.value.destroy();
  if (signGridInstance.value) signGridInstance.value.destroy();
});
</script>


<style scoped>
.btn-header {
  height: 30px;
  width: 60px;
  padding: 0;
  margin: 2px !important;
}
</style>
  
    