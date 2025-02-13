<template>
  <div @keydown.esc="modalCloseFunc">
    <h1>Ksy Test Page</h1>

    <!-- 모달 열기-->
    <button @click="openModal">modal open</button>

    <!-- 모달 컴포넌트 표시 -->
    <ModalPageSample
      :isShowModal="isShowModal"
      @closeModal="closeModal"
      @confirm="confirm"
    />
    

  </div>

</template>

<script setup>
import ModalPageSample from './ModalPageSample.vue';
import { ref } from 'vue';

const isShowModal = ref(false);

const modalCloseFunc = (e) => {
  if (e.key === "Escape") {
      if(isShowModal.value) {
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
