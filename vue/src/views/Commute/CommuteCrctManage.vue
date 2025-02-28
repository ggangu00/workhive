<template>

  <div class="content">
    <div class="container-fluid">
      <!-- 페이지 헤더 -->
      <div class="card">
        <div class="card-body">
          <h4 class="card-title float-left">출퇴근 정정 요청 관리</h4>
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
                      <td><input type="date" id="wordDate" class="form-control" v-model="crctData.commuteDt" :readonly="isUpdate"></td>
                      <th>출근 시간</th>
                      <td><input type="datetime-local" class="form-control" v-model="crctData.goTime" readonly></td>
                      <th>퇴근 시간</th>
                      <td><input type="datetime-local" class="form-control" v-model="crctData.leaveTime" readonly></td>
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
                      <td colspan="5">
                        <div class="form-control custom-file-div">
                          <label class="btn btn-fill cell-btn-custom" for="inputFile">파일선택</label>
                          <a>{{ (fileList.length == 0) ? "선택된 파일 없음" : `파일 ${fileList.length}개` }}</a>
                          <p class="file-info">개별 파일 기준 최대 30MB까지 첨부할 수 있습니다.</p>
                          <input type="file" id="inputFile" style="display: none;" @change="addFileList($event.target)" multiple>
                          <hr>
                          <div class="row file-list">
                            <div class="col-4" v-for="(file, index) in fileList" :key="index">
                            {{ file.name }}
                            <button class="btn btn-sm btn-danger cell-btn-custom" @click="removeFile(index)">삭제</button>
                            </div>
                            <div class="col" v-if="fileList.length == 0">첨부된 파일이 없습니다.</div>
                          </div>
                        </div>
                      </td>
                    </tr>
                    <tr>
                      <th>결재자</th>
                      <td colspan="5"><input type="text" class="form-control" v-model="crctData.signId"></td>
                    </tr>
                  </tbody>
                </table>
              </div>
        
              <div class="row justify-content-center">
                <div class="col-auto">
                  <button class="btn btn-secondary btn-fill mx-2" @click="btnCrctCancle">취소</button>
                  <button class="btn btn-success btn-fill" @click="btnCrctManage">저장</button>
                </div>
              </div>
            </div>
          </div>
  
        </div>
      </div>
      
  
    </div>
  </div>
  
</template>

<script setup>
import { useRoute, useRouter } from 'vue-router';
import axios from 'axios';
import { ref , onMounted, watch } from 'vue';
import { dateTimeFormat } from '../../assets/js/common.js';
import { useUserInfoStore } from '../../store/userStore.js';

const userInfoStore = useUserInfoStore();
let loginUser = userInfoStore.user.mberId;
console.log("로그인 정보 : ", loginUser);

const route = useRoute();
let isUpdate = ref(route.query.isUpdate === 'true');;
let cmtCd;
let crctCd;

// 첨부파일
const fileList = ref([]);
const addFileList = (target) => {
  const newFile = Array.from(target.files);
  fileList.value.push(...newFile);
}
const removeFile = (index) => {
  fileList.value.splice(index, 1);
};

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
  preGoTime: '',
  preLeaveTime: '',
});

// 날짜 변경 감지(등록용 데이터 가져오기)
watch (() => crctData.value.commuteDt, async () => {
  if(cmtCd == null) {
    let result = await axios.get(`/api/commute/dateCmtInfo`, {params: {commuteDt: crctData.value.commuteDt}});
    
    crctData.value.commuteCd = result.data.commuteCd;
    crctData.value.goTime = result.data.goTime;
    crctData.value.leaveTime = result.data.leaveTime;
    crctData.value.crctGoTime = result.data.goTime;
    crctData.value.crctLeaveTime = result.data.leaveTime;
  }
}, {deep:true});
// 등록 화면 데이터 가져오기
const cmtGetInfo = async () => {
  let result = await axios.get(`/api/commute/cmtInfo?commuteCd=${cmtCd}`);

  crctData.value.commuteCd = result.data.commuteCd;
  crctData.value.commuteDt = dateTimeFormat(result.data.commuteDt, 'yyyy-MM-dd');
  crctData.value.goTime = result.data.goTime;
  crctData.value.leaveTime = result.data.leaveTime;
  crctData.value.crctGoTime = result.data.goTime;
  crctData.value.crctLeaveTime = result.data.leaveTime;
}
// 수정 화면 데이터 가져오기
const crctGetInfo = async () => {
  let result = await axios.get(`/api/commute/crctInfo?crctCd=${crctCd}`);

  crctData.value = result.data;
  crctData.value.commuteDt = dateTimeFormat(result.data.commuteDt, 'yyyy-MM-dd');
}

onMounted(() => {
  if(route.query.cmtCd != undefined) {
    cmtCd = route.query.cmtCd;
    cmtGetInfo();
  } else if(route.query.crctCd != undefined) {
    crctCd = route.query.crctCd;
    crctGetInfo();
  }
});

// 저장/취소
const router = useRouter();
const btnCrctManage = async () => {
  let formData = new FormData();

  formData.append("crctCd", crctData.value.crctCd);
  formData.append("commuteCd", crctData.value.commuteCd);
  formData.append("crctGoTime", dateTimeFormat(crctData.value.crctGoTime, 'yyyy-MM-dd hh:mm:ss'));
  formData.append("crctLeaveTime", dateTimeFormat(crctData.value.crctLeaveTime, 'yyyy-MM-dd hh:mm:ss'));
  formData.append("crctReason", crctData.value.crctReason);
  formData.append("signId", crctData.value.signId);
  
  fileList.value.forEach((file) => {
    formData.append("files[]", file);
  });
  
  if(!isUpdate.value) {
    formData.append("createId", loginUser);
    formData.append("preGoTime", crctData.value.goTime);
    formData.append("preLeaveTime", crctData.value.leaveTime);
    await axios.post('/api/commute/crctAdd', formData);
  }
  else {
    formData.append("updateId", loginUser);
    await axios.post('/api/commute/crctModify', formData);
  }

  router.push({ name: 'CrctList' });
}
const btnCrctCancle = () => { // 뒤로가기
  router.go(-1);
}

</script>


<style scoped>
th {
  background-color: #d2d6da !important;
  color: black !important;
  font-size: 16px;
}

.custom-file-div{
  text-align: left;
}
</style>
  
    