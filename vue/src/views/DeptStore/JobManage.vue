<template>
  <Modal
    :isShowModal="props.isShowJobModal"
    :isUpdate="false"
    :modalTitle="'modal title'"
    :width="600"
  >
    <template v-slot:body>
      <div>
        <h3>modal body content</h3>
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

</style>

