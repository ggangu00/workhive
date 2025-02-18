<template>

  <div class="content">
    <div class="container-fluid">
      <!-- 페이지 헤더 -->
      <div class="card">
        <div class="card-body">
          <h4 class="card-title float-left mt-1">출퇴근 정정 요청 관리</h4>
        </div>
      </div>
  
      <!-- 바디 -->
      <div class="card">
        <div class="card-body">

          <div class="row">
            <div class="col">
              
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
                      <th>근무 일자</th>
                      <td><input type="date" id="wordDate" class="form-control" v-model="crctData.commuteDt"></td>
                      <th>출근 시간</th>
                      <td><input type="datetime-local" class="form-control" v-model="crctData.goTime"></td>
                      <th>퇴근 시간</th>
                      <td><input type="datetime-local" class="form-control" v-model="crctData.leaveTime"></td>
                    </tr>
                    <tr>
                      <th>정정 출근 시간</th>
                      <td colspan="2"><input type="datetime-local" class="form-control" style="width: 47.5%;" v-model="crctData.crctGoTime"></td>
                      <th>정정 퇴근 시간</th>
                      <td colspan="2"><input type="datetime-local" class="form-control" style="width: 47.5%;" v-model="crctData.crctLeaveTime"></td>
                    </tr>
                    <tr>
                      <th>정정 사유</th>
                      <td colspan="5"><textarea class="form-control" v-model="crctData.crctReason"></textarea></td>
                    </tr>
                    <tr>
                      <th>파일 첨부</th>
                      <td colspan="5"><input type="file" class="form-control"></td>
                    </tr>
                    <tr>
                      <th>결재자</th>
                      <td colspan="5"><input type="text" class="form-control" v-model="crctData.signId"></td>
                    </tr>
                  </tbody>
                </table>
              </div>
        
              <div class="row justify-content-center">
                <button class="btn btn-secondary btn-fill" @click="btnCrctCancle">취소</button>
                <button class="btn btn-success btn-fill" @click="btnCrctManage">저장</button>
              </div>
            </div>
          </div>
  
        </div>
      </div>
      
  
    </div>
  </div>
  
</template>

<script setup>
import { useRoute } from 'vue-router';
import axios from 'axios';
import { ref, onMounted } from 'vue';
import { dateFormat, dateTimeFormat } from '../../assets/js/common.js';

const route = useRoute();
// 넘겨받은 출퇴근 코드
let cmtCd = route.query.cmtCd;

// 등록 화면 데이터
let crctData = ref({
  commuteCd: '',
  commuteDt: '',
  goTime: '',
  leaveTime: '',
  crctGoTime: '',
  crctLeaveTime: '',
  crctReason: '',
  atchFileId: '',
  signId: '',
});
const cmtGetInfo = async () => {
  let result = await axios.get(`/api/commute/cmtInfo?commuteCd=${cmtCd}`);

  console.log("cmtCd : ", result);
  crctData.value.commuteCd = result.data.commuteCd;
  crctData.value.commuteDt = dateFormat(result.data.commuteDt, 'yyyy-MM-dd hh:mm:ss');
  crctData.value.goTime = result.data.goTime;
  crctData.value.leaveTime = result.data.leaveTime;
  crctData.value.crctGoTime = result.data.goTime;
  crctData.value.crctLeaveTime = result.data.leaveTime;
}

onMounted(() => {
  cmtGetInfo();
});

// 저장/취소
const btnCrctManage = async () => {
  const addData = new FormData();
  addData.append("commuteCd", crctData.value.commuteDt);
  addData.append("crctGoTime", dateTimeFormat(crctData.value.crctGoTime, 'yyyy-MM-dd hh:mm:ss'));
  addData.append("crctLeaveTime", dateTimeFormat(crctData.value.crctLeaveTime, 'yyyy-MM-dd hh:mm:ss'));
  addData.append("crctReason", crctData.value.crctReason);
  // addData.append("atchFileId", );
  addData.append("createId", 'user01');
  addData.append("signId", crctData.value.signId);
  
  await axios.post('/api/commute/crctAdd', addData);

}
const btnCrctCancle = () => {

}

</script>


<style scoped>
.header > hr {
  height: 2px;
  background-color: black;
  border: none;
}
.vc-card hr {
  border: 1px dashed gray;
  margin: 3px 0;
}
.vc-header {
  font-weight: bolder;
  font-size: 18px;
  margin-bottom: 15px;
}

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
  
    