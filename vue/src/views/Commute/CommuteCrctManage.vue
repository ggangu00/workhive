<template>

  <div class="content" @keydown.esc="modalClose">
    <div class="container-fluid">
      <!-- 페이지 헤더 -->
      <div class="card">
        <div class="card-body">
          <h4 class="card-title float-left" v-if="isUpdate">출퇴근 정정 요청 수정</h4>
          <h4 class="card-title float-left" v-if="isDetail">출퇴근 정정 요청 상세 조회</h4>
          <h4 class="card-title float-left" v-if="!isUpdate && !isDetail">출퇴근 정정 요청 등록</h4>
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
                      <td colspan="5"><input type="text" class="form-control" v-model="crctData.signNm" @click="modalOpen" readonly></td>
                    </tr>
                  </tbody>
                </table>
              </div>
              <SignerModal
                :isShowModal="isShowModal"
                @modalClose="modalClose"
                @modalConfirm="modalConfirm"
              />
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
import axios from '../../assets/js/customAxios.js';
import { ref , onMounted, watch } from 'vue';
import { dateTimeFormat } from '../../assets/js/common.js';
import SignerModal from '../components/Modal/SignerModal.vue';
import Swal from 'sweetalert2';

const route = useRoute();
let isUpdate = ref(route.query.isUpdate === 'true');
let isDetail = ref(route.query.isDetail === 'true');
let cmtCd;
let crctCd;

// 같은 페이지로 push될 때 감지하고 새로고침
watch(
   () => route.fullPath,
   (newPath, oldPath) => {
      if (newPath === oldPath) {
        console.log("reloaded");
         window.location.reload(); // 강제 새로고침
      }
   }
);

// 첨부파일
const fileList = ref([]);
const addFileList = (target) => {
  fileList.value = [];
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
  signNm: '',
  preGoTime: '',
  preLeaveTime: '',
});

// 날짜 변경 감지(등록용 데이터 가져오기)
watch (() => crctData.value.commuteDt, async () => {
  if(cmtCd == null) {
    let result;
    try {
      result = await axios.get(`/api/commute/dateCmtInfo`, {params: {commuteDt: crctData.value.commuteDt}});
    } catch (err) {
      Swal.fire({ icon: "error", title: "근무 일자 조회에 실패하였습니다.", text: "Error : " + err });
    }
    crctData.value.commuteCd = result.data.commuteCd;
    crctData.value.goTime = result.data.goTime;
    crctData.value.leaveTime = result.data.leaveTime;
    crctData.value.crctGoTime = result.data.goTime;
    crctData.value.crctLeaveTime = result.data.leaveTime;
  }
}, {deep:true});
// 등록 화면 데이터 가져오기
const cmtGetInfo = async () => {
  let result;
  try {
    result = await axios.get(`/api/commute/cmtInfo?commuteCd=${cmtCd}`);
  } catch (err) {
    Swal.fire({ icon: "error", title: "출퇴근 정보를 가져오지 못했습니다.", text: "Error : " + err });
  }

  crctData.value.commuteCd = result.data.commuteCd;
  crctData.value.commuteDt = dateTimeFormat(result.data.commuteDt, 'yyyy-MM-dd');
  crctData.value.goTime = result.data.goTime;
  crctData.value.leaveTime = result.data.leaveTime;
  crctData.value.crctGoTime = result.data.goTime;
  crctData.value.crctLeaveTime = result.data.leaveTime;
}
// 수정 화면 데이터 가져오기
const crctGetInfo = async () => {
  let result;
  try {
    result = await axios.get(`/api/commute/crctInfo?crctCd=${crctCd}`);
  } catch (err) {
    Swal.fire({ icon: "error", title: "출퇴근 정보를 가져오지 못했습니다.", text: "Error : " + err });
  }
  crctData.value = result.data;
  crctData.value.commuteDt = dateTimeFormat(result.data.commuteDt, 'yyyy-MM-dd');
  files();
}

/////////////////////첨부파일 가져오기/////////////////////
const files = async () => {
  if (!crctData.value.atchFileId) return;

  try{
    const response = await axios.get("/api/cmm/fms/selectFileInfs.do",{
      params: {
        param_atchFileId: crctData.value.atchFileId
      },
    });

    console.log("파일내용=> ",response.data);
    fileList.value = response.data; // 결과 저장
    fileList.value.forEach(i => {
      i.name = i.orignlFileNm;
    })
  } catch (err) {
    Swal.fire({ icon: "error", title: "파일 목록을 불러오지 못했습니다.", text: "Error : " + err });
  }
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
  formData.append("commuteDt", dateTimeFormat(crctData.value.commuteDt, 'yyyy-MM-dd'));
  formData.append("crctGoTime", dateTimeFormat(crctData.value.crctGoTime, 'yyyy-MM-dd hh:mm:ss'));
  formData.append("crctLeaveTime", dateTimeFormat(crctData.value.crctLeaveTime, 'yyyy-MM-dd hh:mm:ss'));
  formData.append("crctReason", crctData.value.crctReason);
  formData.append("signId", crctData.value.signId);

  fileList.value.forEach((file) => {
    formData.append("files[]", file);
  });

  if(!validCheck()) return;

  if(!isUpdate.value) {
    formData.append("preGoTime", crctData.value.goTime);
    formData.append("preLeaveTime", crctData.value.leaveTime);

    try {
      await axios.post('/api/commute/crctAdd', formData);
    } catch(err) {
      Swal.fire({icon:"error", title: "출퇴근 정정 등록에 실패하였습니다.", text: "Error : " + err});
    }
  }
  else {
    try {
      await axios.post('/api/commute/crctModify', formData);
    } catch (err) {
      Swal.fire({ icon: "error", title: "출퇴근 정정 수정에 실패하였습니다.", text: "Error : " + err });
    }
  }

  router.push({ name: 'CrctList' });
}
const btnCrctCancle = () => { // 뒤로가기
  router.go(-1);
}

// 결재자 모달
let isShowModal = ref(false);
const modalOpen = () => {
  isShowModal.value = true;
}
const modalClose = () => {
  isShowModal.value = false;
}
const modalConfirm = (row) => {
  isShowModal.value = false;
  console.log("결재자 : ", row);
  crctData.value.signId = row.mberId;
  crctData.value.signNm = row.mberNm;
}

// 유효성 체크
const validCheck = () => {
  if(!crctData.value.commuteDt?.trim()) {
    Swal.fire({
      icon: "info",
      title: "근무일자를 입력하세요.",
    });
    return false;
  }
  if(!crctData.value.crctGoTime?.trim()) {
    Swal.fire({
      icon: "info",
      title: "정정 출근 시간을 입력하세요.",
    });
    return false;
  }
  if(!crctData.value.crctLeaveTime?.trim()) {
    Swal.fire({
      icon: "info",
      title: "정정 퇴근 시간을 입력하세요.",
    });
    return false;
  }
  if(!crctData.value.crctReason?.trim()) {
    Swal.fire({
      icon: "info",
      title: "정정 사유를 입력하세요.",
    });
    return false;
  }
  if(!crctData.value.signId?.trim()) {
    Swal.fire({
      icon: "info",
      title: "결재자를 선택하세요.",
    });
    return false;
  }

  return true;
}

</script>


<style scoped>
th {
  background-color: #d2d6da !important;
  color: black !important;
  font-size: 16px;
}

</style>

