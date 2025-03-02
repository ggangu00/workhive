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
          <div class="row search align-items-center justify-content-end">
            <div class="col-auto">
              <input type="date" id="startDate" class="input-custom" v-model="vcSrchData.startDate">
              <a class="align-middle"> ~ </a>
              <input type="date" id="endDate" class="input-custom" v-model="vcSrchData.endDate">
            </div>
          </div>
  
          <div class="row mt-3">
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
          <div class="row search align-items-center justify-content-end">
            <div class="col-auto">
              <input type="date" id="startDate" class="input-custom" v-model="signSrchData.startDate">
              <a class="align-middle"> ~ </a>
              <input type="date" id="endDate" class="input-custom" v-model="signSrchData.endDate">
            </div>
          </div>
  
          <div class="row mt-3">
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
    { header: '휴가종류', name: 'vcType', align: 'center', formatter: vcFormat.vcTypeFormatter },
    { header: '휴가일수', name: 'useDays', align: 'center',
      formatter: ({ value }) => { // 소숫점 숫자 표시
        const num = Number(value);
        return num % 1 === 0 ? num : num.toFixed(1);
      }
    },
    { header: '신청일', name: 'createDt', align: 'center', formatter: vcFormat.dateFormatter },
    { header: '신청자', name: 'createId', align: 'center'},
    { header: '결재상태', name: 'signState', align: 'center', formatter: vcFormat.signFormatter },
  ];
  let signCol = [
    { header: '시작일', name: 'vcStartDt', align: 'center', formatter: vcFormat.dateFormatter },
    { header: '종료일', name: 'vcEndDt', align: 'center', formatter: vcFormat.dateFormatter },
    { header: '휴가종류', name: 'vcType', align: 'center', formatter: vcFormat.vcTypeFormatter },
    { header: '휴가일수', name: 'useDays', align: 'center',
      formatter: ({ value }) => { // 소숫점 숫자 표시
        const num = Number(value);
        return num % 1 === 0 ? num : num.toFixed(1);
      }
    },
    { header: '신청일', name: 'createDt', align: 'center', formatter: vcFormat.dateFormatter },
    { header: '신청자', name: 'createId', align: 'center'},
    { header: '결재일', name: 'signDt', align: 'center', formatter: vcFormat.dateFormatter },
    { header: '결재상태', name: 'signState', align: 'center', formatter: vcFormat.signFormatter },
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
      scrollY: true,
      rowHeaders: ['checkbox'],
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
    // let originList = e.target.value === 'D01' ? originSignList : originVcList;
  
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
  
      // let originalRow = originList.find(item => item.vcCd === row.vcCd);
      // if (originalRow) {
      //   let signData = {
      //     vcCd: originalRow.vcCd,
      //     signState: e.target.value
      //   };
  
      //   console.log('originalRow : ', originalRow);
      //   let yearVcData = {
      //     mberId: originalRow.createId,
      //     targetYear: dateTimeFormat(originalRow.vcStartDt, 'yyyy'),
      //     useDays: e.target.value === 'D02' ? originalRow.useDays : 
      //              e.target.value === 'D01' && originalRow.signState === 'D02' ? -originalRow.useDays : 0
      //   }
  
      //   signDataArray.push({ signData, yearVcData });
      // }
    }
  
    console.log("전체 데이터 : ", signDataArray);
  
    // 해당 데이터들을 서버에 보내도록 수정
    if (signDataArray.length) {
      if(e.target.value == 'D01' || e.target.value == 'D02')
        await axios.post('/api/yearVc/yearVcModify', signDataArray.map(data => data.yearVcData)); 
      await axios.post('/api/vacation/signModify', signDataArray.map(data => data.signData)); 
    }
  
    // 리스트 새로 고침
    vcGetList();
    signGetList();
  };
  
  </script>
  
  
  <style scoped>
  </style>
  
    