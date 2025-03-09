<template>

  <div class="content">
    <div class="container-fluid">
      <!-- 페이지 헤더 -->
      <div class="card">
        <div class="card-body">
          <h4 class="card-title float-left">휴가 신청 결재</h4>
  
          <button class="btn btn-danger btn-sm btn-fill float-right" value="D04" @click="btnVcSign">반려</button>
          <button class="btn btn-warning btn-sm btn-fill float-right" value="D03" @click="btnVcSign">보완</button>
          <button class="btn btn-primary btn-sm btn-fill float-right" value="D02" @click="btnVcSign">승인</button>
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
                  <input type="date" class="form-control w50" v-model="vcSrchData.startDate">
                </div>
                <span class="fw-bold">~</span>
                <div class="input-group">
                  <span class="input-group-text fw-bold">신청일(종료)</span>
                  <input type="date" class="form-control w50" v-model="vcSrchData.endDate">
                </div>
              </div>
              
              <button class="btn btn-secondary btn-fill" @click="vcResetBtn" style="margin: 6px 0;">초기화</button>
            </div>
          </div>

          <div class="row">
            <div class="col">
              <!-- grid -->
              <div id="vcGrid"></div>
  
            </div>
          </div>
  
        </div>
      </div>
  
  
      <!-- 페이지 헤더 -->
      <div class="card">
        <div class="card-body">
          <h4 class="card-title float-left">휴가 신청 결재 내역</h4>
          <button class="btn btn-secondary btn-sm btn-fill float-right" value="D01" @click="btnVcSign">결재취소</button>
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
import { dateTimeFormat } from '../../assets/js/common';
import * as vcFormat from '../../assets/js/formatter.js';
import Swal from 'sweetalert2';
import { swalCheck } from '../../assets/js/common.js';

const token = localStorage.getItem("token");

// 조회 조건
const vcSrchData = ref({
  startDate: '',
  endDate: '',
})
const signSrchData = ref({
  startDate: '',
  endDate: '',
})

const vcResetBtn = () => {
  vcSrchData.value = {
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
let vcGridInstance = ref();
let signGridInstance = ref();

// 그리드 로우 데이터
const vcGetList = () => {
  vcGridInstance.value.readData(1,  vcSrchData.value);
}
let vcList = {
  api: {
    readData: { 
      url: '/api/vacation/signerList', 
      method: 'GET', 
      initParams: vcSrchData.value,
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
      url: '/api/vacation/signedList', 
      method: 'GET', 
      initParams: signSrchData.value,
      headers: {'Authorization': `Bearer ${token}`},
    }
  }
};

// 그리드 컬럼 데이터
let vcCol = [
  { header: '시작일', name: 'vcStartDt', align: 'center', formatter: vcFormat.dateFormatter },
  { header: '종료일', name: 'vcEndDt', align: 'center', formatter: vcFormat.dateFormatter },
  { header: '휴가종류', name: 'vcType', align: 'center', formatter: vcFormat.vcTypeFormatter, width: 150 },
  { header: '휴가일수', name: 'useDays', align: 'center',
    formatter: ({ value }) => { // 소숫점 숫자 표시
      const num = Number(value);
      return num % 1 === 0 ? num : num.toFixed(1);
    }, 
    width: 120
  },
  { header: '신청일', name: 'createDt', align: 'center', formatter: vcFormat.dateFormatter },
  { header: '신청자', name: 'createNm', align: 'center', width: 120},
  { header: '결재상태', name: 'signState', align: 'center', formatter: vcFormat.signFormatter, width: 120 },
];
let signCol = [
  { header: '시작일', name: 'vcStartDt', align: 'center', formatter: vcFormat.dateFormatter },
  { header: '종료일', name: 'vcEndDt', align: 'center', formatter: vcFormat.dateFormatter },
  { header: '휴가종류', name: 'vcType', align: 'center', formatter: vcFormat.vcTypeFormatter, width: 150 },
  { header: '휴가일수', name: 'useDays', align: 'center',
    formatter: ({ value }) => { // 소숫점 숫자 표시
      const num = Number(value);
      return num % 1 === 0 ? num : num.toFixed(1);
    }, 
    width: 120
  },
  { header: '신청일', name: 'createDt', align: 'center', formatter: vcFormat.dateFormatter },
  { header: '신청자', name: 'createNm', align: 'center', width: 120},
  { header: '결재일', name: 'signDt', align: 'center', formatter: vcFormat.dateFormatter },
  { header: '결재상태', name: 'signState', align: 'center', formatter: vcFormat.signFormatter, width: 120 },
];

// 실시간 조회 조건
watch(() => vcSrchData, () => {
  vcGetList();
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
onMounted(async () => {
  initGrid(vcGridInstance, 'vcGrid', vcList, vcCol);
  initGrid(signGridInstance, 'signGrid', signList, signCol);
  
  // vcGetList();
  // signGetList();
});

// 컴포넌트가 파괴될 때 기존 Grid 삭제
onBeforeUnmount(() => {
  if (vcGridInstance.value) vcGridInstance.value.destroy();
  if (signGridInstance.value) signGridInstance.value.destroy();
});

// 결재 - 휴가 신청자의 연차 정보, 신청일자의 대상 연도, -> 연차정보 없을시 생성, 
const btnVcSign = async (e) => {
  let selectedRows = e.target.value === 'D01' ? signGridInstance.value.getCheckedRows() : vcGridInstance.value.getCheckedRows();

  let signDataArray = [];

  for (const row of selectedRows) {
    let signData = {
      vcCd: row.vcCd,
      signState: e.target.value
    };

    let yearVcData = {
      mberId: row.createId,
      targetYear: dateTimeFormat(row.vcStartDt, 'yyyy'),
      useDays: e.target.value === 'D02' ? row.useDays : 
                e.target.value === 'D01' && row.signState === 'D02' ? -row.useDays : 0
    }

    signDataArray.push({ signData, yearVcData });
  }

  let check = await swalCheck('결재');
  if(check.isConfirmed) {
    // 해당 데이터들을 서버에 보내도록 수정
    if (signDataArray.length) {
      try {
        if(e.target.value == 'D01' || e.target.value == 'D02')
          await axios.post('/api/yearVc/yearVcModify', signDataArray.map(data => data.yearVcData)); 
        
        await axios.post('/api/vacation/signModify', signDataArray.map(data => data.signData)); 
      } catch (err) {
        Swal.fire({ icon: "error", title: "휴가 신청 결재 실패", text: "Error : " + err });
      }
    }
  }
  else {
    return;
  }

  // 리스트 새로 고침
  vcGetList();
  signGetList();
};

</script>


<style scoped>
</style>
  
    