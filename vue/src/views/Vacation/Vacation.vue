<template>

<div class="content">
    <div class="container-fluid">
      <!-- 페이지 헤더 -->
      <div class="card">
        <div class="card-body">
          <h4 v-if="route.name === 'VcList'" class="card-title float-left">휴가 조회</h4>
          <h4 v-if="route.name === 'VcManage'" class="card-title float-left">휴가 신청 관리</h4>
        </div>
      </div>
  
      <!-- 휴가 정보 -->
      <div class="card">
        <div class="card-body">
          
          <div class="row">
            <div class="col">
                
              <div class="row">
                <div class="col header">
                  <h4>  [ 휴가 정보 ]</h4>
                  <hr>
                </div>
              </div>

              <div class="row">
                <div class="col vc-card">
                  <div class="vc-header">대상연도</div>
                  <div class="vc-body">{{ vcInfo.targetYear }}</div>
                  <hr>
                </div>
                
                <div class="col vc-card">
                  <div class="vc-header">부여일수</div>
                  <div class="vc-body">{{ vcInfo.giveDays }}</div>
                  <hr>
                </div>
                
                <div class="col vc-card">
                  <div class="vc-header">잔여일수</div>
                  <div class="vc-body">{{ vcInfo.remainDays }}</div>
                  <hr>
                </div>
                
                <div class="col vc-card">
                  <div class="vc-header">신청가능일수</div>
                  <div class="vc-body">{{ vcInfo.requestDays }}</div>
                  <hr>
                </div>
                
                <div class="col vc-card">
                  <div class="vc-header">결재대기</div>
                  <div class="vc-body">{{ vcInfo.signWait }}</div>
                  <hr>
                </div>
                
                <div class="col vc-card">
                  <div class="vc-header">보완요청</div>
                  <div class="vc-body">{{ vcInfo.signSup }}</div>
                  <hr>
                </div>
              </div>

            </div>
          </div>

          <!-- 휴가 신청 리스트 / 휴가 신청 관리 -->
          <router-view :vcInfo="vcInfo" @manageClick="yearVcGetInfo"/>

        </div>
      </div>

    </div>
  </div>

</template>

<script setup>
import { ref, onMounted } from 'vue';
import { useRoute } from 'vue-router';
import { dateTimeFormat } from '../../assets/js/common';
import axios from 'axios';


const route = useRoute();

// 사용자 연차 정보 조회(있으면 사용, 없으면 생성) => 로그인 완성되면 로그인 이후 동작하게 변경
let searchData = {
  mberId: 'user01',
  targetYear: dateTimeFormat(new Date(), 'yyyy'),
}
onMounted(async () => {
  yearVcGetInfo();
});

const yearVcGetInfo = async () => {
  // 연차 정보 조회
  let yearVcData = await axios.get('/api/yearVc/yearVcInfo', { params : searchData });
  console.log("연차 조회 결과 : ", yearVcData.data);

  // 연차 정보 없는 경우 - 연차 생성
  if(yearVcData.data == '') {
    let formData = new FormData();
    formData.append("mberId", 'user01');
    formData.append("targetYear", searchData.targetYear);
    formData.append("giveDays", 15);
    formData.append("useDays", 0);
    
    await axios.post('/api/yearVc/yearVcAdd', formData);

    // 생성 후 재호출
    yearVcData = await axios.get('/api/yearVc/yearVcInfo', { params : searchData });
  }

  // 사용 예정일 조회
  let requestData = await axios.get(`/api/vacation/expectInfo?createId=${searchData.mberId}`);

  // 휴가 정보 입력
  vcInfo.value.yearVcCd = yearVcData.data.yearVcCd;
  vcInfo.value.targetYear = yearVcData.data.targetYear;
  vcInfo.value.giveDays = yearVcData.data.giveDays;
  vcInfo.value.useDays = yearVcData.data.useDays;
  vcInfo.value.remainDays = yearVcData.data.giveDays - yearVcData.data.useDays;
  vcInfo.value.signWait = requestData.data.signWait;
  vcInfo.value.signWaitDays = requestData.data.signWaitDays;
  vcInfo.value.signSup = requestData.data.signSup;
  vcInfo.value.signSupDays = requestData.data.signSupDays;
  vcInfo.value.requestDays = vcInfo.value.remainDays - requestData.data.signWaitDays - requestData.data.signSupDays;
}


// 휴가 정보
let vcInfo = ref({
  yearVcCd: '',
  targetYear: 0, // 대상연도
  giveDays: 0, // 부여일수
  useDays: 0, // 사용일수
  remainDays: 0, // 잔여일수 = 부여일 - 사용일
  requestDays: 0, // 신청가능일수 = 부여일 - 사용일 - 결재대기일수 - 보완 요청 일수
  signWait: 0, // 결재 대기 숫자
  signWaitDays: 0, // 결재 대기 일수
  signSup: 0, // 보완 요청 숫자
  signSupDays: 0, // 보완 요청 일수
});

</script>

<style scoped>

</style>
