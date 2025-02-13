<template>
  <Modal
    :isShowModal="props.isShowJobModal"
    :isUpdate="false"
    :modalTitle="'업무 관리'"
    :width="600"
    @click.self="closeJobModal"
  >
    <template v-slot:body>
      <div class="content">
        <div class="container-fluid">

          <!--등록폼 [S]-->
          <div class="card">
            <div class="card-body">
              <form action="BoardAdd" method="post">
                <div class="mb-3">
                  <label>부서명</label>
                  <div class="row">
                    <div class="col-5">
                      <input type="text" class="form-control" placeholder="부서명" readonly>
                    </div>
                  </div>
                </div>

                <div class="col mb-3">
                  <div class="row">
                    <div class="col">
                      <label>업무함명</label>
                    </div>
                    <div class="col">
                      <label>우선순위</label>
                    </div>
                  </div>
                  <div class="row align-items-center">
                    <div class="col-6">
                      <select class="form-select" aria-label="Default select example">
                        <option selected>Open this select menu</option>
                        <option value="1">One</option>
                        <option value="2">Two</option>
                        <option value="3">Three</option>
                      </select>
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
                  <textarea class="form-control" placeholder="업무내용을 입력해주세요" style="height: 85px;"></textarea>
                </div>


                
                <div class="mb-3">
                  <label>제목 <em class="point-red">*</em></label>
                  <input type="text" class="form-control" placeholder="제목을 입력해주세요">
                </div>
                <div class="mb-3">
                  <label>검색</label>
                  <div class="row">
                    <div class="col-auto">
                      <input type="text" class="form-control" placeholder="제목을 입력해주세요">
                    </div>
                    <div class="col-auto">
                      <button class="btn btn-info btn-fill">검색</button>
                    </div>
                  </div>
                </div>
                <div class="mb-3">
                  <label>셀렉트</label>
                  <select class="form-select" aria-label="Default select example">
                    <option selected>Open this select menu</option>
                    <option value="1">One</option>
                    <option value="2">Two</option>
                    <option value="3">Three</option>
                  </select>
                </div>

                <div class="mb-3">
                  <label>체크박스</label>
                  <div>
                    <div class="form-check form-check-inline">
                      <input class="form-check-input" type="checkbox" id="inlineCheckbox1" value="option1">
                      <label class="form-check-label" for="inlineCheckbox1">1</label>
                    </div>
                    <div class="form-check form-check-inline">
                      <input class="form-check-input" type="checkbox" id="inlineCheckbox2" value="option2">
                      <label class="form-check-label" for="inlineCheckbox2">2</label>
                    </div>
                    <div class="form-check form-check-inline">
                      <input class="form-check-input" type="checkbox" id="inlineCheckbox3" value="option3" disabled>
                      <label class="form-check-label" for="inlineCheckbox3">3 (disabled)</label>
                    </div>
                  </div>
                </div>

                <div class="mb-3">
                  <label>라디오</label>
                  <div>
                    <div class="form-check form-check-inline">
                      <input class="form-check-input" type="radio" name="inlineRadioOptions" id="inlineRadio1"
                        value="option1">
                      <label class="form-check-label" for="inlineRadio1">1</label>
                    </div>
                    <div class="form-check form-check-inline">
                      <input class="form-check-input" type="radio" name="inlineRadioOptions" id="inlineRadio2"
                        value="option2">
                      <label class="form-check-label" for="inlineRadio2">2</label>
                    </div>
                    <div class="form-check form-check-inline">
                      <input class="form-check-input" type="radio" name="inlineRadioOptions" id="inlineRadio3"
                        value="option3" disabled>
                      <label class="form-check-label" for="inlineRadio3">3 (disabled)</label>
                    </div>
                  </div>
                </div>
              </form>
            </div>
          </div>
          <!--등록폼 [E]-->
        </div>
      </div>
    </template>
    <template v-slot:footer>
      <button class="btn btn-secondary btn-fill mx-2" @click="closeJobModal">닫기</button>
      <button class="btn btn-success btn-fill mx-2" @click="confirmJobModal">저장</button>
    </template>
  </Modal>
</template>


<script setup>
import Modal from '../../components/Modal.vue';

const props = defineProps({
  isShowJobModal: Boolean,
});

const emit = defineEmits(['closeJobModal', 'confirmJobModal']);
const closeJobModal = () => {
  emit('closeJobModal');
}
const confirmJobModal = () => {
  emit('confirmJobModal');
}
</script>


<style>
.col-6 {
  display: flex;
  align-items: center;
  height: 100%; /* 높이 설정 */
}
</style>

