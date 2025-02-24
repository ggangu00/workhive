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
              <button class="btn btn-primary btn-header" value="D02" @click="btnCrctSign">승인</button>
              <button class="btn btn-warning btn-header" value="D03" @click="btnCrctSign">보완</button>
              <button class="btn btn-danger btn-header" value="D04" @click="btnCrctSign">반려</button>
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
              <button class="btn btn-secondary btn-header" value="D01" @click="btnCrctSign">결재취소</button>
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
import { dateTimeFormat } from '../../assets/js/common';
import { cmtCheck } from '../../assets/js/commute';

// 그리드 인스턴스
let crctGridInstance = ref();
let signGridInstance = ref();

// 그리드 로우 데이터
let crctList = ref();
let signList = ref();

// 가져온 원본 목록 데이터
let originCrctList;
let originSignList;

// 그리드 컬럼 데이터
let crctCol = [
  { header: '근무일자', name: 'commuteDt'},
  { header: '출근시간', name: 'preGoTime'},
  { header: '퇴근시간', name: 'preLeaveTime'},
  { header: '정정출근시간', name: 'crctGoTime'},
  { header: '정정퇴근시간', name: 'crctLeaveTime'},
  { header: '신청일', name: 'createDt'},
  { header: '신청자', name: 'createId'},
  { header: '결재상태', name: 'signState'},
];
let signCol = [
  { header: '근무일자', name: 'commuteDt'},
  { header: '출근시간', name: 'preGoTime'},
  { header: '퇴근시간', name: 'preLeaveTime'},
  { header: '정정출근시간', name: 'crctGoTime'},
  { header: '정정퇴근시간', name: 'crctLeaveTime'},
  { header: '신청일', name: 'createDt'},
  { header: '신청자', name: 'createId'},
  { header: '결재일', name: 'signDt'},
  { header: '결재상태', name: 'signState'},
];

// 조회 조건
const crctSrchData = ref({
  signId: 'test',
  startDate: '',
  endDate: '',
})
const signSrchData = ref({
  signId: 'test',
  startDate: '',
  endDate: '',
})

// 그리드 데이터 조회 메소드
const crctGetList = async () => {
  const result = await axios.get('/api/commute/signerList', { params : crctSrchData.value });

  originCrctList = JSON.parse(JSON.stringify(result.data)); // 깊은 복사
  crctList.value = result.data;
  listFormat(crctList.value);
  
  crctGridInstance.value.resetData(crctList.value);
}
const signGetList = async () => {
  const result = await axios.get('/api/commute/signedList', { params : signSrchData.value });

  originSignList = JSON.parse(JSON.stringify(result.data)); // 깊은 복사
  signList.value = result.data;
  listFormat(signList.value);

  signGridInstance.value.resetData(signList.value);
}
// 그리드 데이터 형식 변경
const listFormat = (list) => {
  list.forEach(i => {
    i.commuteDt = dateTimeFormat(i.commuteDt, 'yyyy-MM-dd');
    i.preGoTime = dateTimeFormat(i.preGoTime, 'MM/dd hh:mm');
    i.preLeaveTime = dateTimeFormat(i.preLeaveTime, 'MM/dd hh:mm');
    i.crctGoTime = dateTimeFormat(i.crctGoTime, 'MM/dd hh:mm');
    i.crctLeaveTime = dateTimeFormat(i.crctLeaveTime, 'MM/dd hh:mm');
    i.createDt = dateTimeFormat(i.createDt, 'yyyy-MM-dd');
    i.signDt = dateTimeFormat(i.signDt, 'yyyy-MM-dd');
    switch(i.signState) {
      case "D01":
        i.signState = "미결재";
        break;
      case "D02":
        i.signState = "승인";
        break;
      case "D03":
        i.signState = "보완";
        break;
      case "D04":
        i.signState = `<a>반려</a>`;
        break;
    }
  });
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
    rowHeaders: ['checkbox'],
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

// // 결재 버튼
// const btnCrctSign = async (e) => {
//   let selectedRows;
//   let originList;

//   if (e.target.value == "D01") {
//     selectedRows = signGridInstance.value.getCheckedRows();
//     originList = originSignList;
//   } else {
//     selectedRows = crctGridInstance.value.getCheckedRows();
//     originList = originCrctList;
//   }

//   for (const row of selectedRows) {
//     let originalRow = originList.find(item => item.commuteCd === row.commuteCd);
//     if (originalRow) {
//       await signModify(originalRow, e.target.value);
//     }
//   }

//   crctGetList();
//   signGetList();
// }
// // 결재 처리 : 출퇴근 정정 수정
// const signModify = async (row, signState) => {
//   let signData = new FormData();
//   signData.append("crctCd", row.crctCd);
//   signData.append("signState", signState);

//   if(signState == "D01") { // 결재취소(대기상태로 변경) => 결재 이전의 출퇴근 시간을 기반으로 출퇴근 수정, 정정 상태 수정
//     let cmtData = new FormData();
//     cmtData.append('commuteCd', row.commuteCd);
//     cmtData.append('goTime', row.preGoTime);
//     cmtData.append('leaveTime', row.preLeaveTime);

//     let changeData = await cmtCheck(row.preGoTime, row.preLeaveTime);
//     cmtData.append('goState', changeData.goState);
//     cmtData.append('leaveState', changeData.leaveState);
//     cmtData.append('workTime', changeData.workTime);
//     cmtData.append('overWorkTime', changeData.overWorkTime);

//     await axios.post('/api/commute/crctSignModify', cmtData); // 기존 출퇴근 시간 및 상태로 출퇴근 정보 되돌리기

//   } else if(signState == "D02") { // 승인 => 현재 출퇴근 시간 정보를 출퇴근 정정에 추가(pre), 정정 시간을 출퇴근에 수정
//     let cmtData = new FormData();
//     cmtData.append('commuteCd', row.commuteCd);
//     cmtData.append('goTime', row.crctGoTime);
//     cmtData.append('leaveTime', row.crctLeaveTime);

//     let changeData = await cmtCheck(row.crctGoTime, row.crctLeaveTime);
//     cmtData.append('goState', changeData.goState);
//     cmtData.append('leaveState', changeData.leaveState);
//     cmtData.append('workTime', changeData.workTime);
//     cmtData.append('overWorkTime', changeData.overWorkTime);
    
//     await axios.post('/api/commute/crctSignModify', cmtData); // 기존 출퇴근 시간 및 상태로 출퇴근 정보 되돌리기
//   }

//   await axios.post('/api/commute/signModify', signData); // 출퇴근 정정 결재 상태 변경

// }

const btnCrctSign = async (e) => {
  let selectedRows = e.target.value === 'D01' ? signGridInstance.value.getCheckedRows() : crctGridInstance.value.getCheckedRows();
  let originList = e.target.value === 'D01' ? originSignList : originCrctList;

  let signDataArray = [];

  for (const row of selectedRows) {
    let originalRow = originList.find(item => item.commuteCd === row.commuteCd);
    if (originalRow) {
      let signData = {
        crctCd: originalRow.crctCd,
        signState: e.target.value
      };

      let cmtData = {
        commuteCd: originalRow.commuteCd,
        goTime: e.target.value === 'D01' ? originalRow.preGoTime : originalRow.crctGoTime,
        leaveTime: e.target.value === 'D01' ? originalRow.preLeaveTime : originalRow.crctLeaveTime
      };

      let changeData = await cmtCheck(cmtData.goTime, cmtData.leaveTime);
    //   Object.assign(cmtData, changeData);
      cmtData.goState = changeData.goState;
      cmtData.leaveState = changeData.leaveState;
      cmtData.workTime = changeData.workTime;
      cmtData.overWorkTime = changeData.overWorkTime;

      // signData에 cmtData를 포함
      signData.cmtData = cmtData;

      // signData와 cmtData를 모두 포함한 객체를 배열에 추가
      signDataArray.push({ signData, cmtData });
    }
  }

  // 해당 데이터들을 서버에 보내도록 수정
  if (signDataArray.length) {
    if(e.target.value == 'D01' || e.target.value == 'D02')
      await axios.post('/api/commute/crctSignModify', signDataArray.map(data => data.cmtData)); 
    await axios.post('/api/commute/signModify', signDataArray.map(data => data.signData)); 
  }

  // 리스트 새로 고침
  crctGetList();
  signGetList();
};



</script>


<style scoped>
.btn-header {
  height: 30px;
  width: 60px;
  padding: 0;
  margin: 2px !important;
}
</style>


