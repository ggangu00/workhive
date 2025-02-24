<template>
  <!-- 휴가 신청 등록/수정 -->
  <div class="row mt-5">
    <div class="col">
        
      <div class="row">
        <div class="col header">
          <h4 v-if="!isUpdate">  [ 휴가 신청 등록 ]</h4>
          <h4 v-else>  [ 휴가 신청 수정 ]</h4>
          <hr>
        </div>
      </div>

      <!-- 테이블 -->
      <div class="table-responsive">
        <table class="table" style="table-layout: fixed;">
          <colgroup>
            <col style="width: 16.6%;">
            <col style="width: 16.6%;">
            <col style="width: 16.6%;">
            <col style="width: 16.6%;">
            <col style="width: 16.6%;">
            <col style="width: 16.6%;">
          </colgroup>
          <tbody>
            <tr>
              <th>휴가 시작일</th>
              <td colspan="2"><input type="date" id="startDate" class="form-control" v-model="vcData.vcStartDt"></td>
              <th>휴가 종료일</th>
              <td colspan="2">
                <input type="date" id="startDate" class="form-control"
                       v-model="vcData.vcEndDt" :readonly="vcData.vcType === 'E02' || vcData.vcType === 'E03'">
              </td>
            </tr>
            <tr>
              <th>휴가 종류</th>
              <td>
                <select class="form-select" aria-label="Default select example" v-model="vcData.vcType">
                  <option value="E01" selected>연차</option>
                  <option value="E02">오전반차</option>
                  <option value="E03">오후반차</option>
                  <option value="E04">공가</option>
                </select>
              </td>
              <th>사용 일수</th>
              <td><input type="text" class="form-control" v-model="vcData.useDays"></td>
              <th>예상 잔여 일수</th>
              <td><input type="text" class="form-control" v-model="vcData.remainDays"></td>
            </tr>
            <tr>
              <th>휴가 사유</th>
              <td colspan="5"><textarea name="" id=""  class="form-control" v-model="vcData.vcReason"></textarea></td>
            </tr>
            <tr>
              <th>파일 첨부</th>
              <td colspan="5"><input type="file" class="form-control"></td>
            </tr>
            <tr>
              <th>결재자</th>
              <td colspan="5"><input type="text" class="form-control" v-model="vcData.signId"></td>
            </tr>

          </tbody>
        </table>
      </div>

      <div class="row justify-content-center">
        <button class="btn btn-secondary btn-fill" @click="btnVcManageCancle">취소</button>
        <button class="btn btn-success btn-fill" @click="btnVcManage">저장</button>
      </div>
    </div>
  </div>

</template>

<script setup>
import { ref, watch } from 'vue';
import { useRoute, useRouter } from 'vue-router';
import axios from 'axios';

// params에서 isUpdate 받아오기
const route = useRoute();
const router = useRouter();
const isUpdate = ref(route.params.isUpdate === 'true');

// 라우터 파라미터가 변경 감지
watch(() => route.params.isUpdate, (newVal) => {
  isUpdate.value = newVal === 'true';
});


// 입력 데이터
let vcData = ref({
  vcStartDt: '',
  vcEndDt: '',
  vcType: 'E01',
  useDays: '',
  vcReason: '',
  atchFileId: '',
  signId: '',
  
  remainDays: '',
  createId: '',
})
// 날짜 입력 감지 - 사용일수 계산
watch(() => [vcData.value.vcStartDt, vcData.value.vcEndDt, vcData.value.vcType], (newVal) => {
  switch(newVal[2]) { // 연차 종류에 따른 동작
    case 'E01': // 연차
      vcData.value.useDays = vcDateCalc(newVal[0], newVal[1]);
      break;
    case 'E04': // 공가
      vcData.value.useDays = 0;
      break;
    default:
      vcData.value.useDays = 0.5;
      vcData.value.vcEndDt = vcData.value.vcStartDt;
  }
})

// 휴가 일수 계산
const vcDateCalc = (startDt, endDt) => {
  let startDate = new Date(startDt);
  let endDate = new Date(endDt);

  const diff = endDate - startDate;
  let diffDay = Math.floor(diff / (1000 * 60 * 60 * 24)) + 1;

  let weekendDays = 0;

  // 시작 날짜부터 종료 날짜까지 반복
  for (let date = new Date(startDate); date <= endDate; date.setDate(date.getDate() + 1)) {
    // 토요일(6) 또는 일요일(0)인 경우 weekendDays 증가
    if (date.getDay() === 6 || date.getDay() === 0) {
      weekendDays++;
    }
  }

  // 주말 제외한 일수 반환
  return diffDay - weekendDays;
}

// 등록 / 수정
const btnVcManage = async () => {
  let formData = new FormData();
  formData.append("vcStartDt", vcData.value.vcStartDt);
  formData.append("vcEndDt", vcData.value.vcEndDt);
  formData.append("vcType", vcData.value.vcType);
  formData.append("useDays", vcData.value.useDays);
  formData.append("vcReason", vcData.value.vcReason);
  // formData.append("atchFileId", vcData.value.atchFileId);
  formData.append("createId", 'user01');
  formData.append("signId", 'admin');

  let result = axios.post('/api/vacation/vcAdd', formData);

  console.log(result.data);

}
// 취소
const btnVcManageCancle = () => {
  router.push({name:"VcList"});

}


</script>


<style scoped>
th {
  background-color: #d2d6da !important;
  color: black !important;
  font-size: 16px;
}

button {
  width: 80px;
  padding: 5px 10px !important;
}
</style>
  
  