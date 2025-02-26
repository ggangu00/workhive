<template>
  <Modal
    :isShowModal="props.isShowJobModal"
    :modalTitle="'업무 관리'"
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
                    <input type="text" class="form-control" placeholder="부서명" v-model="localJobBxSelected.searchDeptCd" readonly />
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
                    <select class="form-select" v-model="formValues.deptJobBxId">
                      <option disabled value="">업무함을 선택하세요</option>
                      <option v-for="job in localJobBxList" :key="job.deptJobBxId" :value="job.deptJobBxId">
                        {{ job.deptJobBxNm }}
                      </option>
                    </select>
                  </div>
                  <div class="col-6">
                    <div class="form-check form-check-inline d-flex align-items-center">
                      <input class="form-check-input my-0" type="radio" name="inlineRadioOptions" id="inlineRadio1"
                        value="1" v-model="formValues.priort">
                      <!-- <input class="form-check-input my-0" type="radio" name="inlineRadioOptions" id="inlineRadio1"
                        value="option1"> -->
                      <label class="form-check-label my-0" for="inlineRadio1">높음</label>
                    </div>
                    <div class="form-check form-check-inline d-flex align-items-center">
                      <input class="form-check-input my-0" type="radio" name="inlineRadioOptions" id="inlineRadio2"
                        value="2" v-model="formValues.priort">
                      <label class="form-check-label my-0" for="inlineRadio2">보통</label>
                    </div>
                    <div class="form-check form-check-inline d-flex align-items-center">
                      <input class="form-check-input my-0" type="radio" name="inlineRadioOptions" id="inlineRadio3"
                        value="3" v-model="formValues.priort">
                      <label class="form-check-label my-0" for="inlineRadio3">낮음</label>
                    </div>
                  </div>
                </div>
              </div>

              <div class="mb-3">
                <label>업무명<em class="point-red">*</em></label>
                <input type="text" class="form-control" placeholder="업무명을 입력해주세요" v-model="formValues.deptJobNm">
              </div>

              <div class="mb-3">
                <label>업무내용<em class="point-red">*</em></label>
                <textarea class="form-control" placeholder="업무내용을 입력해주세요" style="height: 86px;" v-model="formValues.deptJobCn"></textarea>
              </div>

              <div class="mb-3">
                <label>업무담당자<em class="point-red">*</em></label>
                <input type="text" class="form-control" placeholder="담당자명을 입력해주세요" v-model="formValues.chargerId">
              </div>

              <div class="mb-3">
                <label>파일<em class="point-red">*</em></label>
                <input type="file" class="form-control" style="padding: 6px" multiple>
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
import axios from 'axios';
import { useStore } from 'vuex';

const props = defineProps({
  isShowJobModal: Boolean,
  isUpdate: Boolean,
  jobBxSelected: Object,
  selectedRowData: Object,
});

let localJobBxSelected = ref(props.jobBxSelected);
const store = useStore();
let localJobBxList = computed(() => store.state.jobBxList);
let test = computed(() => store.state.jobBxSelected);

// 입력값 저장
let formValues = ref({
  selectedData: localJobBxSelected,
  deptJobBxId: '',
  deptJobId: '',
  priort: '',
  deptJobNm: '',
  deptJobCn: '',
  chargerId: '',
});

watch(() => props.jobBxSelected, (newVal) => {
  console.log('local watch', newVal);
  formValues.value.deptJobBxId = newVal.searchDeptJobBxId;
})
watch(() => test, (newVal) => {
  console.log('test : ', newVal);
})

// 업무함 리스트 변경시 모달에서도 변경
// watch(() => props.jobBxList, (newVal) => {
//   localJobBxList.value = newVal;
// }, { immediate: true });

// 업무 수정으로 열린 경우
watch(() => props.selectedRowData, async (newVal) => {
  if (props.isUpdate) {
    // 업무 상세 정보 가져오기
    const result = await axios.get('/api/deptstore/jobInfo', { params: {deptJobId: newVal.deptJobId} });

    formValues.value.deptJobBxId = result.data.deptJobBxId;
    formValues.value.deptJobId = result.data.deptJobId;
    formValues.value.priort = result.data.priort;
    formValues.value.deptJobNm = result.data.deptJobNm;
    formValues.value.deptJobCn = result.data.deptJobCn;
    formValues.value.chargerId = result.data.chargerId;
  };
}, { immediate: true });

const emit = defineEmits(['modalCloseJob', 'modalConfirmJob']);
const modalCloseJob = () => {
  emit('modalCloseJob');
}
const modalConfirmJob = async () => {
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
  addData.append("deptCd", formValues.value.selectedData.searchDeptCd);
  addData.append("deptJobBxId", formValues.value.deptJobBxId);
  addData.append("priort", formValues.value.priort);
  addData.append("deptJobNm", formValues.value.deptJobNm);
  addData.append("deptJobCn", formValues.value.deptJobCn);
  addData.append("chargerId", formValues.value.chargerId);
  // addData.append("", formValues.); 파일

  await axios.post('/api/deptstore/jobAdd', addData);
}

// 수정
const jobUpdate = async () => {
  const modifyData = new FormData();
  modifyData.append("deptCd", formValues.value.selectedData.searchDeptCd);
  modifyData.append("deptJobBxId", formValues.value.deptJobBxId);
  modifyData.append("deptJobId", formValues.value.deptJobId);
  modifyData.append("priort", formValues.value.priort);
  modifyData.append("deptJobNm", formValues.value.deptJobNm);
  modifyData.append("deptJobCn", formValues.value.deptJobCn);
  modifyData.append("chargerId", formValues.value.chargerId);
  // modifyData.append("", formValues.); 파일

  await axios.post('/api/deptstore/jobModify', modifyData);
}
</script>

<style>
.col-6 {
  display: flex;
  align-items: center;
  height: 100%;
}
</style>
