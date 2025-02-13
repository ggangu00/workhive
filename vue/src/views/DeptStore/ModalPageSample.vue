<template>
  <!-- 모달 컴포넌트 -->
  <Modal
    :isShowModal="props.isShowModal"
    :modalTitle="'modal title'"
    :width="600"
  >
    <template v-slot:body>
      <div>
        <h3>modal body content</h3>
      </div>
    </template>
    <template v-slot:footer>
      <button class="btn btn-secondary btn-fill mx-2" @click="closeModal">닫기</button>
      <button class="btn btn-success btn-fill mx-2" @click="confirm">저장</button>
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

