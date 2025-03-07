<template>

  <div class="content">
    <div class="container-fluid">
      <!-- 페이지 헤더 -->
      <div class="card">
        <div class="card-body">
          <h4 class="card-title float-left">출퇴근 정정 요청 결재</h4>

          <button class="btn btn-danger btn-sm btn-fill float-right" value="D04" @click="btnCrctSign">반려</button>
          <button class="btn btn-warning btn-sm btn-fill float-right" value="D03" @click="btnCrctSign">보완</button>
          <button class="btn btn-primary btn-sm btn-fill float-right" value="D02" @click="btnCrctSign">승인</button>
        </div>
      </div>

      <!-- 결재 요청 목록 -->
      <div class="card">
        <div class="card-body">
          <!-- 조회 조건 -->
          <div class="button-collection d-flex justify-content-end align-items-center flex-wrap" style="padding-bottom: 15px;">
            <div class="selectbox search d-flex">

              <div class="d-flex align-items-center">
                <div class="input-group">
                  <span class="input-group-text fw-bold">신청일(시작)</span>
                  <input type="date" class="form-control w50" v-model="crctSrchData.startDate">
                </div>
                <span class="fw-bold">~</span>
                <div class="input-group">
                  <span class="input-group-text fw-bold">신청일(종료)</span>
                  <input type="date" class="form-control w50" v-model="crctSrchData.endDate">
                </div>
              </div>
              
              <button class="btn btn-secondary btn-fill" @click="crctResetBtn" style="margin: 6px 0;">초기화</button>
            </div>
          </div>

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
          <h4 class="card-title float-left">출퇴근 정정 요청 결재 내역</h4>
          <button class="btn btn-secondary btn-sm btn-fill float-right" value="D01" @click="btnCrctSign">결재취소</button>
        </div>
      </div>

      <!-- 결재 내역 -->
      <div class="card">
        <div class="card-body">
          <!-- 조회 조건 -->
          <div class="button-collection d-flex justify-content-end align-items-center flex-wrap" style="padding-bottom: 15px;">
            <div class="selectbox search d-flex">

              <div class="d-flex align-items-center">
                <div class="input-group">
                  <span class="input-group-text fw-bold">결재일(시작)</span>
                  <input type="date" class="form-control w50" v-model="signSrchData.startDate">
                </div>
                <span class="fw-bold">~</span>
                <div class="input-group">
                  <span class="input-group-text fw-bold">결재일(종료)</span>
                  <input type="date" class="form-control w50" v-model="signSrchData.endDate">
                </div>
              </div>
              
              <button class="btn btn-secondary btn-fill" @click="signResetBtn" style="margin: 6px 0;">초기화</button>
            </div>
          </div>

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
import axios from '../../assets/js/customAxios.js';
import Grid from 'tui-grid';
import { ref, onMounted, onBeforeUnmount, watch } from 'vue';
import { cmtCheck } from '../../assets/js/commute';
import * as crctFormat from '../../assets/js/formatter.js';
import Swal from 'sweetalert2';
import { swalCheck } from '../../assets/js/common.js';

const token = localStorage.getItem("token");

// 조회 조건
const crctSrchData = ref({
  startDate: '',
  endDate: '',
})
const signSrchData = ref({
  startDate: '',
  endDate: '',
})
const crctResetBtn = () => {
  crctSrchData.value = {
    startDate: '',
    endDate: '',
  }
}
const signResetBtn = () => {
  signSrchData.value = {
    startDate: '',
    endDate: '',
  }
}

// 그리드 인스턴스
let crctGridInstance = ref();
let signGridInstance = ref();

// 그리드 로우 데이터
const crctGetList = () => {
  crctGridInstance.value.readData(1,  crctSrchData.value);
}
const crctList = {
  api: {
    readData: { 
      url: '/api/commute/signerList', 
      method: 'GET', 
      initParams: crctSrchData.value,
      headers: {'Authorization': `Bearer ${token}`},
    }
  }
};
const signGetList = () => {
  signGridInstance.value.readData(1, signSrchData.value);
}
const signList = {
  api: {
    readData: { 
      url: '/api/commute/signedList', 
      method: 'GET', 
      initParams: signSrchData.value,
      headers: {'Authorization': `Bearer ${token}`},
    }
  }
};

// 가져온 원본 목록 데이터
// let originCrctList;
// let originSignList;


// 그리드 컬럼 데이터
let crctCol = [
  { header: '근무일자', name: 'commuteDt', align: 'center', formatter: crctFormat.dateFormatter },
  { header: '출근시간', name: 'preGoTime', align: 'center', formatter: crctFormat.timeFormatter },
  { header: '퇴근시간', name: 'preLeaveTime', align: 'center', formatter: crctFormat.timeFormatter },
  { header: '정정출근시간', name: 'crctGoTime', align: 'center', formatter: crctFormat.timeFormatter },
  { header: '정정퇴근시간', name: 'crctLeaveTime', align: 'center', formatter: crctFormat.timeFormatter },
  { header: '신청일', name: 'createDt', align: 'center', formatter: crctFormat.dateFormatter },
  { header: '신청자', name: 'createNm', align: 'center'},
  { header: '결재상태', name: 'signState', align: 'center', formatter: crctFormat.signFormatter },
];
let signCol = [
  { header: '근무일자', name: 'commuteDt', align: 'center', formatter: crctFormat.dateFormatter },
  { header: '출근시간', name: 'preGoTime', align: 'center', formatter: crctFormat.timeFormatter },
  { header: '퇴근시간', name: 'preLeaveTime', align: 'center', formatter: crctFormat.timeFormatter },
  { header: '정정출근시간', name: 'crctGoTime', align: 'center', formatter: crctFormat.timeFormatter },
  { header: '정정퇴근시간', name: 'crctLeaveTime', align: 'center', formatter: crctFormat.timeFormatter },
  { header: '신청일', name: 'createDt', align: 'center', formatter: crctFormat.dateFormatter },
  { header: '신청자', name: 'createNm', align: 'center'},
  { header: '결재일', name: 'signDt', align: 'center'},
  { header: '결재상태', name: 'signState', align: 'center', formatter: crctFormat.signFormatter },
];

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
    data: rowData,
    scrollX: false,
    scrollY: false,
    rowHeaders: ['checkbox'],
    pageOptions: {
      useClient: false,
      perPage: 5,
    },
    columns: colData,
  });
};

// Toast Grid 초기화
onMounted(() => {
  initGrid(crctGridInstance, 'crctGrid', crctList, crctCol);
  initGrid(signGridInstance, 'signGrid', signList, signCol);

  // crctGetList();
  // signGetList();
});

// 컴포넌트가 파괴될 때 기존 Grid 삭제
onBeforeUnmount(() => {
  if (crctGridInstance.value) crctGridInstance.value.destroy();
  if (signGridInstance.value) signGridInstance.value.destroy();
});

// 정정 요청 결재 기능
const btnCrctSign = async (e) => {
  let selectedRows = e.target.value === 'D01' ? signGridInstance.value.getCheckedRows() : crctGridInstance.value.getCheckedRows();

  let signDataArray = [];

  for (const row of selectedRows) {
    let signData = {
      crctCd: row.crctCd,
      signState: e.target.value
    };

    let cmtData = {
      commuteCd: row.commuteCd,
      goTime: e.target.value === 'D01' ? row.preGoTime : row.crctGoTime,
      leaveTime: e.target.value === 'D01' ? row.preLeaveTime : row.crctLeaveTime
    };

    let changeData = await cmtCheck(cmtData.goTime, cmtData.leaveTime);
    cmtData.goState = changeData.goState;
    cmtData.leaveState = changeData.leaveState;
    cmtData.workTime = changeData.workTime;
    cmtData.overWorkTime = changeData.overWorkTime;

    // signData와 cmtData를 모두 포함한 객체를 배열에 추가
    signDataArray.push({ signData, cmtData });
  }

  let  check = await swalCheck('결재');
  if(check.isConfirmed) {
    // 해당 데이터들을 서버에 보내도록 수정
    if (signDataArray.length) {
      try {
        if(e.target.value == 'D01' || e.target.value == 'D02')
          await axios.post('/api/commute/crctSignModify', signDataArray.map(data => data.cmtData));
        
        await axios.post('/api/commute/signModify', signDataArray.map(data => data.signData));
      } catch (err) {
        Swal.fire({ icon: "error", title: "출퇴근 정정 요청 결재 실패", text: "Error : " + err });
      }
    }
  }
  else {
    return;
  }

  // 리스트 새로 고침
  crctGetList();
  signGetList();
};



</script>


<style scoped>
</style>


