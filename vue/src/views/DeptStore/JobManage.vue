<template>
  <Modal
    :isShowModal="props.isShowJobModal"
    :modalTitle="modalTitle"
    @click.self="modalCloseJob"
  >
    <template v-slot:body>
      <div class="content">
        <div class="container-fluid">
          <!-- 등록폼 [S] -->
          <div class="card">
            <div class="card-body">
              <div class="mb-3">
                <label>부서명</label>
                <div class="row">
                  <div class="col-6">
                    <input type="text" class="form-control" placeholder="부서명" v-model="formValues.deptNm" readonly />
                  </div>
                </div>
              </div>

              <div class="col mb-3">
                <div class="row">
                  <div class="col">
                    <label>업무함명<em class="point-red">*</em></label>
                  </div>
                  <div class="col">
                    <label>우선순위<em class="point-red">*</em></label>
                  </div>
                </div>
                <div class="row align-items-center">
                  <div class="col-6">
                    <select class="form-select" v-model="formValues.deptJobBxId" :disabled="isDetail">
                      <option disabled value="">업무함을 선택하세요</option>
                      <option v-for="job in localJobBxList" :key="job.deptJobBxId" :value="job.deptJobBxId">
                        {{ job.deptJobBxNm }}
                      </option>
                    </select>
                  </div>
                  <div class="col-6">
                    <div class="form-check form-check-inline d-flex align-items-center">
                      <input class="form-check-input my-0" type="radio" name="inlineRadioOptions" id="inlineRadio1"
                        value="1" v-model="formValues.priort" :disabled="isDetail">
                      <label class="form-check-label my-0" for="inlineRadio1">높음</label>
                    </div>
                    <div class="form-check form-check-inline d-flex align-items-center">
                      <input class="form-check-input my-0" type="radio" name="inlineRadioOptions" id="inlineRadio2"
                        value="2" v-model="formValues.priort" :disabled="isDetail">
                      <label class="form-check-label my-0" for="inlineRadio2">보통</label>
                    </div>
                    <div class="form-check form-check-inline d-flex align-items-center">
                      <input class="form-check-input my-0" type="radio" name="inlineRadioOptions" id="inlineRadio3"
                        value="3" v-model="formValues.priort" :disabled="isDetail">
                      <label class="form-check-label my-0" for="inlineRadio3">낮음</label>
                    </div>
                  </div>
                </div>
              </div>

              <div class="mb-3">
                <label>업무명<em class="point-red">*</em></label>
                <input type="text" class="form-control" placeholder="업무명을 입력해주세요" v-model="formValues.deptJobNm" :readonly="isDetail">
              </div>

              <div class="mb-3">
                <label>업무내용<em class="point-red">*</em></label>
                <textarea class="form-control" placeholder="업무내용을 입력해주세요" style="height: 86px;" v-model="formValues.deptJobCn" :readonly="isDetail"></textarea>
              </div>

              <div class="mb-3">
                <label>업무담당자<em class="point-red">*</em></label>
                <input type="text" class="form-control" placeholder="담당자명을 입력해주세요" v-model="formValues.chargerNm" @click="modalOpen" readonly>
                
                <SignerModal
                  :isShowModal="isShowModal"
                  @modalClose="modalClose"
                  @modalConfirm="modalConfirm"
                />
              </div>

              <div class="mb-3">
                <label>파일첨부</label>
                <div class="form-control">
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
              </div>

            </div>
          </div>
          <!-- 등록폼 [E] -->
        </div>
      </div>
    </template>
    <template v-slot:footer>
      <button class="btn btn-secondary btn-fill mx-2" @click="modalCloseJob">닫기</button>
      <button class="btn btn-success btn-fill mx-2" @click="modalConfirmJob">저장</button>
    </template>
  </Modal>
</template>

<script setup>
import { computed, ref, watch } from 'vue';
import Modal from '../../components/Modal.vue';
import axios from '../../assets/js/customAxios.js';
import { useStore } from 'vuex';
import SignerModal from '../components/Modal/SignerModal.vue';
import Swal from 'sweetalert2';

const props = defineProps({
  isShowJobModal: Boolean,
  isUpdate: Boolean,
  isDetail: Boolean,
  jobBxSelected: Object,
  selectedRowData: Object,
});

const store = useStore();
// let localJobBxSelected = ref(props.jobBxSelected);
let localJobBxList = computed(() => store.state.jobBxList);

let modalTitle = computed(() => {
  return props.isDetail ? '업무 상세 조회' : props.isUpdate ? '업무 수정' : '업무 등록';
});

// 입력값 저장
let formValues = ref({
  deptCd: '',
  deptNm: '',
  deptJobBxId: '',
  deptJobId: '',
  priort: '',
  deptJobNm: '',
  deptJobCn: '',
  chargerId: '',
  chargerNm: '',
});
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

watch(() => props.isShowJobModal, () => {
  if(props.isShowJobModal && !props.isUpdate) {
    formValues.value.priort = '';
    formValues.value.deptJobNm = '';
    formValues.value.deptJobCn = '';
    formValues.value.chargerId = '';
    fileList.value = [];
  }
})

// 업무함 선택 변경(부서 및 업무함 정보 변경)
watch(() => props.jobBxSelected, (newVal) => {
  formValues.value.deptCd = newVal.searchDeptCd;
  formValues.value.deptNm = newVal.searchDeptNm;
  formValues.value.deptJobBxId = newVal.searchDeptJobBxId;
  formValues.value.deptJobBxNm = newVal.searchDeptJobBxNm;
})

watch(() => props.isUpdate, async () => {
  if(!props.isUpdate) {
    formValues.value.priort = '';
    formValues.value.deptJobNm = '';
    formValues.value.deptJobCn = '';
    formValues.value.chargerId = '';
    formValues.value.chargerNm = '';
  }
});

watch(() => props.selectedRowData, async (newVal) => {
  // 업무 수정 또는 상세로 열린 경우
  if(props.isUpdate || props.isDetail) {
    // 업무 상세 정보 가져오기
    let result;
    try {
      result = await axios.get('/api/deptstore/jobInfo', { params: {deptJobId: newVal.deptJobId} });
    } catch (err) {
      Swal.fire({ icon: "error", title: "업무 상세 정보 조회에 실패하였습니다.", text: "Error : " + err });
    }
    console.log("업무 상세 정보 : ", result.data);

    formValues.value.deptCd = result.data.deptCd;
    formValues.value.deptNm = result.data.deptNm;
    formValues.value.deptJobBxId = result.data.deptJobBxId;
    formValues.value.deptJobId = result.data.deptJobId;
    formValues.value.priort = result.data.priort;
    formValues.value.deptJobNm = result.data.deptJobNm;
    formValues.value.deptJobCn = result.data.deptJobCn;
    formValues.value.chargerId = result.data.chargerId;
    formValues.value.chargerNm = result.data.chargerNm;
    formValues.value.atchFileId = result.data.atchFileId;

    files();
  };
}, { immediate: true });

/////////////////////첨부파일 가져오기/////////////////////
const files = async () => {
  if (!formValues.value.atchFileId) return;

  try{
    const response = await axios.get("/api/cmm/fms/selectFileInfs.do",{
      params: {
        param_atchFileId: formValues.value.atchFileId
      },
    });

    fileList.value = response.data; // 결과 저장
    fileList.value.forEach(i => {
      i.name = i.orignlFileNm;
    })
  } catch (error) {
    console.error("파일 목록 불러오기 실패:", error);
  }
}

const emit = defineEmits(['modalCloseJob', 'modalConfirmJob']);
const modalCloseJob = () => {
  emit('modalCloseJob');
}
const modalConfirmJob = async () => {
  if(!validCheck()) return;

  if(!props.isUpdate) {
    await jobAdd();
  } else {
    await jobUpdate();
  }
  emit('modalConfirmJob');
}

// 등록
const jobAdd = async () => {
  const addData = new FormData();
  addData.append("deptCd", formValues.value.deptCd);
  addData.append("deptJobBxId", formValues.value.deptJobBxId);
  addData.append("priort", formValues.value.priort);
  addData.append("deptJobNm", formValues.value.deptJobNm);
  addData.append("deptJobCn", formValues.value.deptJobCn);
  addData.append("chargerId", formValues.value.chargerId);
  fileList.value.forEach((file) => {
    addData.append("files[]", file);
  });

  try {
    await axios.post('/api/deptstore/jobAdd', addData);
  } catch (err) {
    Swal.fire({ icon: "error", title: "업무 등록에 실패하였습니다.", text: "Error : " + err });
  }
}

// 수정
const jobUpdate = async () => {
  const modifyData = new FormData();
  modifyData.append("deptCd", formValues.value.deptCd);
  modifyData.append("deptJobBxId", formValues.value.deptJobBxId);
  modifyData.append("deptJobId", formValues.value.deptJobId);
  modifyData.append("priort", formValues.value.priort);
  modifyData.append("deptJobNm", formValues.value.deptJobNm);
  modifyData.append("deptJobCn", formValues.value.deptJobCn);
  modifyData.append("chargerId", formValues.value.chargerId);
  fileList.value.forEach((file) => {
    modifyData.append("files[]", file);
  });

  try {
    await axios.post('/api/deptstore/jobModify', modifyData);
  } catch (err) {
    Swal.fire({ icon: "error", title: "업무 수정에 실패하였습니다.", text: "Error : " + err });
  }
}


// 유효성 체크
const validCheck = () => {
  if(!formValues.value.priort?.trim()) {
    Swal.fire({
      icon: "info",
      title: "우선순위를 선택하세요.",
    });
    return false;
  }
  if(!formValues.value.deptJobNm?.trim()) {
    Swal.fire({
      icon: "info",
      title: "업무명을 입력하세요.",
    });
    return false;
  }
  if(!formValues.value.deptJobCn?.trim()) {
    Swal.fire({
      icon: "info",
      title: "업무 내용을 입력하세요.",
    });
    return false;
  }
  if(!formValues.value.chargerNm?.trim()) {
    Swal.fire({
      icon: "info",
      title: "업무 담당자를 선택하세요.",
    });
    return false;
  }

  return true;
}

// 결재자 모달
let isShowModal = ref(false);
const modalOpen = () => {
  if(!props.isDetail) isShowModal.value = true;
}
const modalClose = () => {
  isShowModal.value = false;
}
const modalConfirm = (row) => {
  isShowModal.value = false;
  console.log("결재자 : ", row);
  formValues.value.chargerId = row.mberId;
  formValues.value.chargerNm = row.mberNm;
}

</script>

<style>
.col-6 {
  display: flex;
  align-items: center;
  height: 100%;
}
</style>
