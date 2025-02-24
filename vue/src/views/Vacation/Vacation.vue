<template>

<div class="content">
    <div class="container-fluid">
      <!-- 페이지 헤더 -->
      <div class="card">
        <div class="card-body">
          <h4 v-if="route.name === 'VcList'" class="card-title float-left mt-1">휴가 조회</h4>
          <h4 v-if="route.name === 'VcManage'" class="card-title float-left mt-1">휴가 신청 관리</h4>
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
          <router-view />

        </div>
      </div>

      <!-- 임시 테스트 버튼 -->
      <button @click="btnAddTest">등록</button>
      <button @click="btnUpdateTest">수정</button>
    </div>
  </div>

</template>

<script setup>
import { ref } from 'vue';
import { useRoute, useRouter } from 'vue-router';

const route = useRoute();
const router = useRouter();

// 휴가 정보
let vcInfo = ref({
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

// 라우터 테스트용
const btnAddTest = () => {
  router.push({name:"VcManage"});
}
const btnUpdateTest = () => {
  router.push({name:"VcManage", params: {isUpdate: true}});
}
</script>

<style scoped>

</style>
