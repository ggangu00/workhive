<template>
  <!-- 모달 컴포넌트 -->
  <Modal
    :isShowModal="props.isShowModal"
    :modalTitle="'모달 타이틀'"
    @click.self="closeModal"
  >
    <template v-slot:body>
      내용부
    </template>
    <template v-slot:footer>
      <button class="btn btn-primary btn-fill" @click="confirm">등록</button>
      <button class="btn btn-secondary btn-fill" @click="closeModal">닫기</button>      
    </template>
  </Modal>
</template>


<script setup>
import Modal from '../../components/Modal.vue';

const props = defineProps({
  isShowModal: Boolean,
});

const emit = defineEmits(['closeModal', 'confirm']);
const closeModal = () => {
  emit('closeModal');
}
const confirm = () => {
  emit('confirm');
}

</script>


<style>

</style>

