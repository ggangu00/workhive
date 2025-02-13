<template>
  <div class="modal fade" :class="{ show: isShowModal }" id="exampleModal" tabindex="-1">
    <div class="modal-dialog modal-dialog-centered modal-dialog-scrollable" :style="{ maxWidth: width + 'px' }">
      <div class="modal-content">
        <div class="modal-header">
          <h1 class="modal-title fs-2">{{ modalTitle }}</h1>
        </div>
        <div class="modal-body">
          <slot name="body">slot</slot>
        </div>
        <div class="modal-footer justify-content-center">
          <slot name="footer">slot</slot>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
export default {
  name: "Modal",
  props: {
    isShowModal: Boolean,
    modalTitle: String,
    width: Number
  },

  data() {
    return {
      modalState: this.isShowModal,
    }
  },

}
</script>


<style scoped>
.modal {
  position: fixed;
  left: 0;
  top: 0;
  width: 100%;
  height: 100%;
  background: rgba(0, 0, 0, 0.4);
  .modal-body {
    max-height: 500px;
  }
}
.show {
  display: block;
}

.modal-header, .modal-footer {
  background-color: #5C5C5C;
}
h1 {
  color: white;
}
</style>