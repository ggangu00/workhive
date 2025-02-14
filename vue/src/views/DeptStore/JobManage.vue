<template>
  <Modal
    :isShowModal="props.isShowJobModal"
    :modalTitle="'업무 관리'"
    @click.self="modalCloseJob"
  >
    <template v-slot:body>
      <div class="content">
        <div class="container-fluid">

          <!--등록폼 [S]-->
          <div class="card">
            <div class="card-body">

              <div class="mb-3">
                <label>부서명</label>
                <div class="row">
                  <div class="col-6">
                    <input type="text" class="form-control" placeholder="부서명" v-model="localJobbxSelected.searchDeptId" readonly />
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
                    <input type="text"  v-model="localJobbxSelected.searchDeptJobBxId" readonly>
                    <!-- <select class="form-select" aria-label="Default select example"  v-model="localJobbxSelected.searchDeptJobBxId">
                      <option selected>Open this select menu</option>
                      <option value="1">One</option>
                      <option value="2">Two</option>
                      <option value="3">Three</option>
                    </select> -->
                  </div>
                  <div class="col-6">
                    <div class="form-check form-check-inline d-flex align-items-center">
                      <input class="form-check-input my-0" type="radio" name="inlineRadioOptions" id="inlineRadio1"
                        value="option1">
                      <label class="form-check-label my-0" for="inlineRadio1">높음</label>
                    </div>
                    <div class="form-check form-check-inline d-flex align-items-center">
                      <input class="form-check-input my-0" type="radio" name="inlineRadioOptions" id="inlineRadio2"
                        value="option2">
                      <label class="form-check-label my-0" for="inlineRadio2">보통</label>
                    </div>
                    <div class="form-check form-check-inline d-flex align-items-center">
                      <input class="form-check-input my-0" type="radio" name="inlineRadioOptions" id="inlineRadio3"
                        value="option3">
                      <label class="form-check-label my-0" for="inlineRadio3">낮음</label>
                    </div>
                  </div>
                </div>
              </div>

              <div class="mb-3">
                <label>업무명<em class="point-red">*</em></label>
                <input type="text" class="form-control" placeholder="업무명을 입력해주세요">
              </div>

              <div class="mb-3">
                <label>업무내용<em class="point-red">*</em></label>
                <textarea class="form-control" placeholder="업무내용을 입력해주세요" style="height: 86px;"></textarea>
              </div>

              <div class="mb-3">
                <label>업무담당자<em class="point-red">*</em></label>
                <input type="text" class="form-control" placeholder="담당자명을 입력해주세요">
              </div>

              <div class="mb-3">
                <label>파일<em class="point-red">*</em></label>
                <input type="file" class="form-control" style="padding: 6px" multiple>
              </div>

            </div>
          </div>
          <!--등록폼 [E]-->
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
import { ref, defineProps, watch } from 'vue';
import Modal from '../../components/Modal.vue';

const props = defineProps({
  isShowJobModal: Boolean,
  jobbxSelected: Object,
});
console.log('props : ', props);
// jobbxSelected를 로컬 데이터로 복사
let localJobbxSelected = ref({ ...props.jobbxSelected });

watch(() => props.jobbxSelected, (newVal) => {
  console.log('props 변경 감지', newVal);
});


const emit = defineEmits(['modalCloseJob', 'modalConfirmJob']);
const modalCloseJob = () => {
  emit('modalCloseJob');
}
const modalConfirmJob = () => {
  emit('modalConfirmJob');
}
</script>


<style>
.col-6 {
  display: flex;
  align-items: center;
  height: 100%; /* 높이 설정 */
}
</style>

