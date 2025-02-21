<template>
  <div class="content">
    <div class="container-fluid">
      <div class="row">
        <div class='col-8'>
          <div class="card left-card">
            <!-- head -->
            <div class="d-flex justify-content-between align-items-center">
              <div>
                <h4 class="card-title me-auto">문서기안</h4>
              </div>
              <div>
                <button v-for="(btn, index) in headButtons" :key="index" :class="['btn', btn.class]"
                  @click="$emit('button-click', btn.label)">
                  <!-- 자식컴포넌트 클릭이벤트 -->
                  {{ btn.label }}
                </button>
              </div>
            </div>
            <!-- 제목구분선 -->
            <div class="divid_line"></div>
            <!-- body -->
            <div class="document-input-section">
              <div class=''>
                <div class="d-flex justify-content-between mb-1">
                  <div class="col-3"><span>결재종류</span></div>
                  <div class="col-3"><span>양식</span></div>
                  <div class="col-3"><span>기안부서</span></div>
                </div>
                <div class="d-flex justify-content-between">
                  <div class="col-3">
                    <input type="text" class="form-control w80" :value="$route.query.docKind" disabled>
                  </div>
                  <div class="col-3">
                    <input type="text" class="form-control w80" :value="$route.query.formNm" disabled>
                  </div>
                  <div class="col-3">
                    <input type="text" class="form-control w80" :value="$route.query.deptNm" disabled>
                  </div>
                </div>
              </div>
              <div class="d-flex justify-content-between mb-1 mt-3">
                <div class="col-12"><span>제목</span></div>
              </div>
              <div class="d-flex justify-content-between">
                <div title-box>
                  <div class="col-12">
                    <input type="text" class="form-control w80" :value="$route.query.docTitle" disabled>
                  </div>
                </div>
              </div>
              <!-- 에디터 -->
              <div class='col-12'>
                <div v-html="$route.query.docCnEditor" class="htmlBx"></div>
                <!-- <div id="editor"></div> -->
              </div>
              <div class="d-flex" style='margin-top: 20px;'>
                <!-- 왼쪽 -->
                <div class="col-5" v-if="showFile">
                  <div class="mb-1">
                    <span>첨부파일</span>
                  </div>
                  <div>
                    <input type="file" />
                    <p class="file-info">개별 파일 기준 최대 30MB까지 첨부할 수 있습니다.</p>
                  </div>
                </div>

                <!-- 구분선 -->
                <div class="col-1 d-flex justify-content-center align-items-center">
                  <div class=' file_line'></div>
                </div>

                <!-- 첨부된 파일 목록 -->
                <div class="col-5 file-upload-right">
                  <div class="mb-1">
                    <span>첨부된 파일 목록</span>
                  </div>
                  <ul class="file-list">
                    <li v-for="(file, index) in fileList" :key="index">
                      {{ file.name }} ({{ formatFileSize(file.size) }})
                      <button class="btn btn-sm btn-danger">삭제</button>
                    </li>
                    <li v-if="fileList.length == 0">첨부된 파일이 없습니다.</li>
                  </ul>
                </div>
              </div>
            </div>
          </div>
        </div>
        <!-- 우측 결재선 -->
        <div class="col-4">
          <div class='card right-card'>
            <div class="button-collection d-flex justify-content-between align-items-center flex-wrap"
              style='padding: 5px ;'>
              <div class='d-flex'>
                <h4 class="card-title">결재선</h4>
              </div>
              <div class='selectbox d-flex'>
                <button v-if="ApprovalButtons" class="btn btn-primary btn-fill float-right" data-bs-toggle="modal"
                  data-bs-target="#approvalRegiModal">결재선지정</button>
              </div>
            </div>

            <div class="divid_line"></div>
            <div class="d-flex flex-column flex-grow-1">
              <span class="mt-1 mb-1">결재</span>
              <div class="approval-box">
                <div class="approval-item"><span class='badge bg-info'>결정</span> [총무팀] 홍길동 팀장</div>
                <div class="approval-item"><span class='badge bg-info'>결정</span> [인사팀] 홍길순 과장</div>
                <div class="approval-item"><span class='badge bg-info'>기안</span> [영업팀] 홍길권 대리</div>
              </div>
              <span class="mt-3 mb-1">수신</span>
              <div class="approval-box">
                <div class="approval-item"><span class="badge bg-warning">수신</span> [총무팀]</div>
                <div class="approval-item"><span class="badge bg-warning">수신</span> [인사팀]</div>
                <div class="approval-item"><span class="badge bg-warning">수신</span> [영업팀]</div>
              </div>
            </div>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>
<script setup>
import { ref, onMounted, onUnmounted } from 'vue';
import ApprovalLine from '../../components/PaymentLayout/ApprovalLine.vue';
import { useRoute } from 'vue-router';

const route = useRoute();
//데이터받기
const docCnEditor = ref("");

defineProps({
  headButtons: { type: Array, required: true },
  ApprovalButtons: { type: Boolean, default: true },
  showFile: { type: Boolean, default: true }
});

//  ApprovalLine을 참조할 ref 생성
const approvalLineRef = ref(null);

//  모달이 열릴 때 Toast Grid를 초기화하는 함수
const handleModalOpen = () => {
  if (approvalLineRef.value) {
    approvalLineRef.value.onModalOpen();  // ApprovalLine의 onModalOpen() 실행
  }
};

//  Bootstrap 모달 이벤트 리스너 추가
onMounted(() => {
  const modalElement = document.getElementById('approvalRegiModal');
  if (modalElement) {
    modalElement.addEventListener('shown.bs.modal', handleModalOpen);
  }
});

//  컴포넌트 언마운트 시 이벤트 리스너 제거
onUnmounted(() => {
  console.log("전달받은 query params:", route.query);
  const modalElement = document.getElementById('approvalRegiModal');
  if (modalElement) {
    modalElement.removeEventListener('shown.bs.modal', handleModalOpen);
  }
  docCnEditor.value = route.query.docCnEditor || "asdf";
});

const fileList = ref([]);


</script>
<style>
.modal-xl {
  max-width: 70vw !important;
  /* 모달 가로 크기 확장 */
}

.modal-content {
  max-height: 80vh;
  /* 모달 세로 크기 제한 */
  overflow-y: auto;
  /* 내부 스크롤 적용 */
}

.button-collection button {
  margin-right: 10px;
}

.selectbox select {
  margin-right: 10px;
}

.divid_line {
  width: 99%;
  height: 2px;
  background-color: #e2e2e2;
  margin: 10px auto;
}

.input-box {
  background-color: #e2e2e2;
  padding: 0px;
}

.title-box {
  padding: 0px;
}

.file_line {
  width: 2px;
  height: 90%;
  background-color: #ccc;
  margin-top: 5px;
  margin-bottom: 5px;
}

.approval-box {
  border: 1px solid #ccc;
  padding: 10px;
  background-color: #fafafa;
}

.left-card,
.right-card {
  height: 100%;
  padding: 10px;
}

.htmlBx {
  margin-top: 30px;
  border: 2px solid #eee;
  padding: 30px;
}

.badge {
  font-weight: 400 !important;
}
</style>