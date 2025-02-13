<template>

  <div class="modal fade" :class="{ show: isShowModal }" id="exampleModal" tabindex="-1" aria-labelledby="organizationModalLabel"
    aria-hidden="true">
    <div class="modal-dialog modal-lg modal-dialog-centered modal-dialog-scrollable">
      <div class="modal-content">
        <!-- 모달 헤더 -->
        <div class="modal-header">
          <h5 class="modal-title" id="organizationModalLabel">{{ modalTitle }}</h5>
          <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
        </div>

        <!-- 모달 바디 -->
        <div class="modal-body">
          <div class="card">
            <div class="card-body">
              <slot name="body">slot</slot>
            </div>
          </div>
        </div>

        <!-- 모달 푸터 -->
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
  background: rgba(0, 0, 0, 0.4);
}

.modal-body{
  background-color: #f0f2f5;
}
.show {
  display: block;
}
</style>