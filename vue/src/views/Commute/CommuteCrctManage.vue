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

          <!-- 테이블 -->
          <div class="table">
            <table class="table" style="table-layout: fixed; margin: 0;">
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
                  <th>근무 일자 <em class="point-red">*</em></th>
                  <td colspan="5">
                    <input type="date" id="wordDate" class="form-control" style="width: 18.6%;" 
                           v-model="crctData.commuteDt" :readonly="isUpdate || isDetail">
                  </td>
                </tr>
                <tr>
                  <th>출퇴근 시간</th>
                  <td colspan="5">
                    <div class="row align-items-center">
                      <div class="col-auto">
                        <input type="datetime-local" class="form-control" 
                               v-model="crctData.goTime" readonly>
                      </div>
                      <div class="col-auto p-none"> ~ </div>
                      <div class="col-auto">
                        <input type="datetime-local" class="form-control" 
                               v-model="crctData.leaveTime" readonly>
                      </div>
                    </div>
                  </td>
                </tr>
                <tr>
                  <th>정정 출퇴근 시간 <em class="point-red">*</em></th>
                  <td colspan="5">
                    <div class="row align-items-center">
                      <div class="col-auto">
                        <input type="datetime-local" class="form-control" @change="timeCheck" 
                               v-model="crctData.crctGoTime" :max="crctData.crctLeaveTime" :readonly="isDetail">
                      </div>
                      <div class="col-auto p-none"> ~ </div>
                      <div class="col-auto">
                        <input type="datetime-local" class="form-control" @change="timeCheck" 
                               v-model="crctData.crctLeaveTime" :min="crctData.crctGoTime" :readonly="isDetail">
                      </div>
                    </div>
                  </td>
                </tr>
                <tr>
                  <th>정정 사유 <em class="point-red">*</em></th>
                  <td colspan="5">
                    <textarea id="crctReason" class="form-control" 
                              v-model="crctData.crctReason" :readonly="isDetail"
                              style="height: 135px;">
                    </textarea>
                  </td>
                </tr>
                <tr>
                  <th>파일 첨부</th>
                  <td colspan="5">
                    <div class="form-control custom-file-div">
                      <div v-if="!isDetail">
                        <label class="btn btn-sm btn-fill cell-btn-custom" for="inputFile">파일선택</label>
                        <a>{{ (fileList.length == 0) ? "선택된 파일 없음" : `파일 ${fileList.length}개` }}</a>
                        <p class="file-info">개별 파일 기준 최대 30MB까지 첨부할 수 있습니다.</p>
                        <input type="file" id="inputFile" style="display: none;" @change="addFileList($event)" multiple>
                        <hr>
                      </div>

                      <div class="row file-list" v-if="!isDetail">
                        <div class="col-4" v-for="(file, index) in fileList" :key="index">
                          {{ file.name }}
                          <button class="btn btn-sm btn-danger cell-btn-custom" @click="removeFile(index)">삭제</button>
                        </div>
                      </div>
                      <div class="row file-list" v-if="isDetail">
                        <div class="col-4" v-for="(file, index) in fileList" :key="index" @click="downloadFile(file)">
                          {{ file.name }}
                        </div>
                        <div class="col" v-if="fileList.length == 0">첨부된 파일이 없습니다.</div>
                      </div>

                    </div>
                  </td>
                </tr>
                <tr>
                  <th>결재자 <em class="point-red">*</em></th>
                  <td colspan="5">
                    <div class="row align-items-center">
                      <div class="col-auto" style="padding-right: 0;">
                        <input type="text" class="form-control" v-model="crctData.signNm" @click="modalOpen" readonly>
                      </div>
                      <div class="col-auto" style="padding-left: 5px;">
                        <i class="fa-solid fa-magnifying-glass" @click="modalOpen"></i>
                      </div>
                    </div>
                  </td>
                </tr>
              </tbody>
            </table>
          </div>
          <SignerModal
            :isShowModal="isShowModal"
            @modalClose="modalClose"
            @modalConfirm="modalConfirm"
          />
          <div class="text-center">
              <button class="btn btn-secondary btn-fill mx-2" @click="btnCrctCancle">취소</button>
              <button class="btn btn-success btn-fill" @click="btnCrctManage" v-if="!isDetail">저장</button>
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
import { dateTimeFormat, swalCheck } from '../../assets/js/common.js';
import SignerModal from '../components/Modal/SignerModal.vue';
import Swal from 'sweetalert2';
import FileSaver from 'file-saver';

const route = useRoute();
let isUpdate = ref(route.query.isUpdate === 'true');
let isDetail = ref(route.query.isDetail === 'true');
let cmtCd;
let crctCd;

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
      Swal.fire({ icon: "error", title: "근무 일자 조회 실패", text: "Error : " + err });
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
    Swal.fire({ icon: "error", title: "출퇴근 정보 조회 실패", text: "Error : " + err });
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
    Swal.fire({ icon: "error", title: "출퇴근 정보 조회 실패", text: "Error : " + err });
  }
  crctData.value = result.data;
  crctData.value.commuteDt = dateTimeFormat(result.data.commuteDt, 'yyyy-MM-dd');

  files();
}

///////////////////// 첨부파일 /////////////////////
const fileList = ref([]);
// 파일 저장
let updateFileCheck = 0;
const addFileList = (event) => {
  if(updateFileCheck == 0 && isUpdate.value) {
    fileList.value = [];
    updateFileCheck++;
  }

  // 이벤트가 없거나 files가 없으면 return
  if (!event || !event.target || !event.target.files) {
    console.error("파일 선택 오류: event 또는 files가 존재하지 않습니다.");
    return;
  }

  const newFiles = Array.from(event.target.files); //배열로

  newFiles.forEach((newFile) => { //중복체크
    const isDuplicate = fileList.value.some(
      (file) => file.name == newFile.name && file.size == newFile.size
    );
    if (!isDuplicate) {
      fileList.value.push(newFile);
    }
  });

  // input 필드 초기화 (같은 파일 다시 추가 가능)
  event.target.value = "";
};

// 파일 삭제
const removeFile = (index) => {
  fileList.value.splice(index, 1);
};
// 파일 가져오기
const files = async () => {
  if (!crctData.value.atchFileId) return;

  try{
    const response = await axios.get("/api/cmm/fms/selectFileInfs.do",{
      params: {
        param_atchFileId: crctData.value.atchFileId
      },
    });

    fileList.value = response.data; // 결과 저장
    fileList.value.forEach(i => {
      i.name = i.orignlFileNm;
    })
  } catch (err) {
    Swal.fire({ icon: "error", title: "파일 목록 조회 실패", text: "Error : " + err });
  }
}
// 파일 다운로드
const downloadFile = async (file) => {
  try {
    // 파일 다운로드 API 호출
    const response = await axios.get(`/api/cmm/fms/FileDown.do`, {
      params: {
        atchFileId: file.atchFileId,  // 파일 ID
        fileSn: file.fileSn,          // 파일 시리얼 넘버
      },
      responseType: "blob",  // 바이너리 데이터 형식으로 받기
    });
    
    // 받은 데이터를 Blob으로 변환하고, FileSaver를 사용하여 다운로드
    const blob = new Blob([response.data]);
    FileSaver.saveAs(blob, file.name);  // 파일명으로 다운로드
  } catch (err) {
    Swal.fire({ icon: "error", title: "파일 다운로드 실패", text: "Error : " + err });
  }
};

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
      Swal.fire({icon:"error", title: "출퇴근 정정 요청 등록 실패", text: "Error : " + err});
    }
  }
  else {
    let  check = await swalCheck('수정');
    if(check.isConfirmed) {
      try {
        await axios.post('/api/commute/crctModify', formData);
      } catch (err) {
        Swal.fire({ icon: "error", title: "출퇴근 정정 요청 수정 실패", text: "Error : " + err });
      }
    }
    else {
      return;
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
  if(!isDetail.value) isShowModal.value = true;
}
const modalClose = () => {
  isShowModal.value = false;
}
const modalConfirm = (row) => {
  isShowModal.value = false;
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
    const focusTag = document.querySelector('#crctReason');
    focusTag?.focus();
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
const timeCheck = () => {
  const goTime = new Date(crctData.value.crctGoTime);
  const leaveTime = new Date(crctData.value.crctLeaveTime);

  // 출근 시간이 퇴근 시간보다 늦지 않도록 체크
  if (goTime >= leaveTime) {
    Swal.fire({
      icon: "info",
      title: "출근 시간은 퇴근 시간보다 빠른 시간이어야 합니다.",
    });
    crctData.value.crctGoTime = ''; // 잘못된 값은 초기화
  }

  // 퇴근 시간이 출근 시간보다 일찍 선택되지 않도록 체크
  if (leaveTime <= goTime) {
    Swal.fire({
      icon: "info",
      title: "퇴근 시간은 출근 시간보다 늦은 시간이어야 합니다.",
    });
    crctData.value.crctLeaveTime = ''; // 잘못된 값은 초기화
  }
}

</script>


<style scoped>
th {
  background-color: #d2d6da !important;
  color: black !important;
  font-size: 16px;
}

i {
  cursor: pointer;
}
</style>

