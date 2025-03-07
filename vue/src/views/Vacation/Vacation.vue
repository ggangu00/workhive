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
                
              <!-- <div class="row">
                <div class="col header">
                  <h4>  [ 휴가 정보 ]</h4>
                  <hr>
                </div>
              </div> -->

              <div class="row profile-section justify-content-center">
                <div class="col-auto status-boxes">

                  <div class="status-box projects">
                    <div class="status-title" style="background-color: gray;">대상연도</div>
                    <div class="status-content">{{ vcInfo.targetYear }} 년</div>
                  </div>

                  <div class="status-box projects">
                    <div class="status-title" style="background-color: gray;">부여일수</div>
                    <div class="status-content">{{ vcInfo.giveDays }} 일</div>
                  </div>
                  
                  <div class="status-box projects">
                    <div class="status-title" style="background-color: gray;">잔여일수</div>
                    <div class="status-content">{{ vcInfo.remainDays }} 일</div>
                  </div>
                  
                  <div class="status-box projects">
                    <div class="status-title" style="background-color: gray;">신청가능일수</div>
                    <div class="status-content">{{ vcInfo.requestDays }} 일</div>
                  </div>
                  
                  <div class="status-box projects">
                    <div class="status-title" style="background-color: gray;">결재대기</div>
                    <div class="status-content">{{ vcInfo.signWait }} 건</div>
                  </div>
                  
                  <div class="status-box projects">
                    <div class="status-title" style="background-color: gray;">보완요청</div>
                    <div class="status-content">{{ vcInfo.signSup }} 건</div>
                  </div>

              <!--박주현 레이아웃 수정[S]-->
              <div class="vc-info-card">
                <div class="item dash">
                    <div class="label">대상연도</div>
                    <div class="value">{{ vcInfo.targetYear }}년</div>
                </div>
                <div class="item">
                    <div class="label">부여일수</div>
                    <div class="value">{{ vcInfo.giveDays }}일</div>
                </div>
                <div class="separator"><i class="fa-solid fa-minus"></i></div>
                <div class="item">
                    <div class="label">사용일수</div>
                    <div class="value">{{ vcInfo.useDays }}일</div>
                </div>
                <div class="separator"><i class="fa-solid fa-equals"></i></div>
                <div class="item dash">
                    <div class="label">잔여일수</div>
                    <div class="value">{{ vcInfo.remainDays }}일</div>
                </div>
                <div class="item dash">
                    <div class="label">신청가능일수</div>
                    <div class="value primary">{{ vcInfo.requestDays }}일</div>
                </div>
                <div class="item dash">
                    <div class="label">결재대기</div>
                    <div class="value danger">{{ vcInfo.signWait }}건</div>
                </div>
                <div class="item">
                    <div class="label">보완요청</div>
                    <div class="value warning">{{ vcInfo.signSup }}건</div>
                </div>
              </div>
              <!--박주현 레이아웃 수정[E]-->
              
              <!-- 
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
              -->

            </div>
          </div>

          <!-- 휴가 신청 리스트 / 휴가 신청 관리 -->
          <router-view :vcInfo="vcInfo" @manageClick="yearVcCheck"/>

        </div>
      </div>

    </div>
  </div>

</template>

<script setup>
import { ref, onMounted } from 'vue';
import { useRoute } from 'vue-router';
import { dateTimeFormat } from '../../assets/js/common';
import * as vacation from '../../assets/js/vacation';


const route = useRoute();

// 사용자 연차 정보 조회(있으면 사용, 없으면 생성) => 로그인 완성되면 로그인 이후 동작하게 변경
let searchData = {
  targetYear: dateTimeFormat(new Date(), 'yyyy'),
}

// 휴가 정보
let vcInfo = ref({
  yearVcCd: '',
  targetYear: '',
  giveDays: '',
  useDays: '',
  remainDays: '',
  signWait: '',
  signWaitDays: '',
  signSup: '',
  signSupDays: '',
  requestDays: '',
});

onMounted(() => {
  yearVcCheck();
});

const yearVcCheck = async () => {
  // 연차 정보 조회
  let yearVcData = await vacation.yearVcGetInfo(searchData.targetYear);

  // 연차 정보 없는 경우 - 연차 생성
  if(yearVcData == '') {
    // 새 연차 정보 생성
    await vacation.yearVcAdd(searchData.targetYear);
    // 연차 정보 재조회
    yearVcData = await vacation.yearVcGetInfo(searchData.targetYear);
  }

  // 사용 예정일 조회
  let requestData = await vacation.reqVcGetInfo(searchData.targetYear);

  // 휴가 정보 입력
  vcInfo.value = vacation.vcGetInfo(yearVcData, requestData);
}


</script>

<style scoped>
.vc-info-card {
  display: flex;
  justify-content: space-between;
  align-items: center;
  border: 1px solid #ccc;
  padding: 30px 7px;

  .item {
      text-align: center;
      flex: 1;
      position: relative;
  }
  .item.dash:not(:last-child)::after {
      content: "";
      position: absolute;
      right: 0;
      top: 10%;
      height: 80%;
      width: 1px;
      background-color: #ccc;
  }
  .label {
      font-size: 17px;
      color: #747474;
  }
  .value {
      font-size: 24px;
      font-weight: bold;
      color: #565656;
  }
  .separator i {
    color: #bfbfbf;
  }

  .primary {
    color: #81a1eb;
  }
  .warning {
    color: #ffc479;
  }
  .danger {
    color: #ff9097;
  }
}

</style>
