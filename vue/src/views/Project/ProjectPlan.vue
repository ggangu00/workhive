<template>
  <div class="content" @keydown.esc="modalCloseFunc">
    <div class="container-fluid schedule-container">
      <div class="card">
        <div class="card-body">
          <h4 class="card-title float-left">프로젝트 조회</h4>
        </div>
      </div>

      <div class="card border-primary">
        <div class="card-body">
          <p class="card-sub">대구사람이좋다협회</p>
          <h5 class="card-title mb-3">2025 대구 아동복지 홈페이지 구축 용역 <span class="badge badge-danger">D-10</span></h5>
          <p class="card-sub"><b>기간 : </b> 2025-01-01 ~ 2025-03-31</p>
          <p class="card-sub"><b>참여자 : </b> 박주현, 박지훈, 정수민, 박명식</p>
        </div>
      </div>

      <div class="card">
        <div class="card-body">
          <button class="btn btn-primary btn-fill float-right" @click="openModal">일정등록</button>
          <table class="table-responsive">
            <tr>
              <th>1주차</th>
              <th>2주차</th>
              <th>3주차</th>
              <th>4주차</th>
              <th>5주차</th>
              <th>6주차</th>
              <th>7주차</th>
              <th>8주차</th>
            </tr>
            <tr>
              <td>
                <div class="task red" style="width:190%">화면설계 및 데이터베이스 정리 <span class="close">×</span></div>
              </td>
              <td></td>
              <td></td>
              <td></td>
              <td></td>
              <td></td>
              <td></td>
              <td></td>
            </tr>
            <tr>
              <td></td>
              <td></td>
              <td>
                <div class="task blue" style="width:290%">화면설계 및 데이터베이스 정리 <span class="close">×</span></div>
              </td>
              <td></td>
              <td></td>
              <td></td>
              <td></td>
              <td></td>
            </tr>
          </table>
        </div>
      </div>
    </div>
  </div>

  <!--업무등록 모달[s]-->
  <Modal :isShowModal="isShowModal" :modalTitle="'일정등록'" @click.self="closeModal">
    <!-- 모달 바디 -->
    <template v-slot:body>
      <div class="mb-3">
        <div class="form-group has-label">
          <label>일정색상</label>
        </div>
        <input type="color" class="form-control form-control-color" id="exampleColorInput" value="#563d7c" title="Choose your color">
      </div>
      <div class="mb-3">
        <label class="form-label">기간 <em class="point-red">*</em></label>
        <div class="row">
          <div class="col-auto">
            <select class="form-select">
              <option value="1">1주차</option>
              <option value="2">2주차</option>
              <option value="3">3주차</option>
            </select>
          </div>
          <div class="col-auto">~</div>
          <div class="col-auto">
            <select class="form-select">
              <option value="1">1주차</option>
              <option value="2">2주차</option>
              <option value="3">3주차</option>
            </select>
          </div>
        </div>
      </div>
      <div class="mb-3">
        <div class="form-group has-label">
          <label>일정명 <em class="point-red">*</em></label>
        </div>
        <input type="text" name="project_nm" class="form-control" placeholder="일정내용을 입력해주세요">
      </div>
    </template>

    <!-- 모달 푸터 -->
    <template v-slot:footer>
      <button type="button" class="btn btn-primary btn-fill" @click="confirm">등록</button>
      <button type="button" class="btn btn-secondary btn-fill" @click="closeModal">닫기</button>
    </template>
  </Modal>
  <!--업무등록 모달[e]-->

</template>

<script setup>
import Modal from '../../components/Modal.vue';
import { ref } from 'vue';

const isShowModal = ref(false);

const modalCloseFunc = (e) => {
  if (e.key === "Escape") {
    if (isShowModal.value) {
      isShowModal.value = !isShowModal.value
    }
  }
}
const openModal = () => {
  isShowModal.value = true;
}
const closeModal = () => {
  isShowModal.value = false;
}
const confirm = () => {
  isShowModal.value = false;
}
</script>