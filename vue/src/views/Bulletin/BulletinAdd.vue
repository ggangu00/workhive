<template>
  <div class="content">
    <div class="container-fluid">
      <!-- 등록폼 -->
      <div class="card">
        <div class="card-body">
          <h4 class="card-title float-left mt-1">게시글 등록</h4>
          <!-- 등록 버튼 -->
          <button @click="submitForm" class="btn btn-primary btn-fill float-right">등록</button>        
          <!-- 초기화 버튼 -->
          <button @click="resetForm" class="btn btn-secondary btn-fill float-right">초기화</button>
        </div>
      </div>

      <div class="card">
        <div class="card-body">
          <form @submit.prevent="submitForm">
            <div class="mb-3">
              <label>제목 <em class="point-red">*</em></label>
              <input type="text" class="form-control" placeholder="제목을 입력해주세요" v-model="nttSj" />
            </div>

            <!-- 에디터 영역 -->
            <div class="col-12">
              <div id="editor"></div>
              <input type="hidden" v-model="nttCn"/>
            </div>

            <div class="mb-3">
              <label>공지여부</label>
              <div class="form-check form-check-inline" style="margin-left: 10px;">
                <input
                  class="form-check-input"
                  type="checkbox"
                  v-model="noticeAt"
                />
              </div>
            </div>

            <div class="mb-3">
              <label class="form-label">게시기간 <em class="point-red">*</em></label>
              <div class="row">
                <div class="col-auto">
                  <input type="date" v-model="ntceBgnde" class="form-control" required />
                </div>
                <div class="col-auto">~</div>
                <div class="col-auto">
                  <input type="date" v-model="ntceEndde" class="form-control" required />
                </div>
              </div>
            </div>
          </form>
        </div>
      </div>

      <!-- 응답 메시지 -->
      <div v-if="responseMessage" class="alert" :class="isSuccess ? 'alert-success' : 'alert-danger'" style="margin-top: 15px;">
        {{ responseMessage }}
      </div>
    </div>
  </div>
</template>

<script setup>
import '@toast-ui/editor/dist/toastui-editor.css';
import { Editor } from '@toast-ui/editor';
import { ref, onMounted } from 'vue';
import axios from '../../assets/js/customAxios';
import { useRoute, useRouter } from 'vue-router';
import Swal from 'sweetalert2';

const route = useRoute();
const router = useRouter();

// 필드 데이터
const mberId = ref();
const wrterNm = ref();
const bbsId = ref(route.query.bbsId);              // 게시판id
const nttSj = ref('');             // 제목
const nttCn = ref('');             // 에디터의 HTML 내용 (게시글 내용)
const noticeAt = ref(false);       // 공지여부
const ntceBgnde = ref('');   // 게시 시작일
const ntceEndde = ref('');    // 게시 종료일

// TOAST UI Editor 인스턴스
const editor = ref(null);

// 응답 메시지 및 성공 여부
const responseMessage = ref('');
const isSuccess = ref(false);

// TOAST UI Editor 초기화
const initEditor = () => {
  editor.value = new Editor({
    el: document.querySelector("#editor"),
    height: "500px",
    initialEditType: "WYSIWYG",
    previewStyle: "vertical"
  });
};

// 날짜 형식을 'yyyy-mm-dd'로 변환
const formatDate = (date) => {
  const d = new Date(date);
  const year = d.getFullYear();
  const month = String(d.getMonth() + 1).padStart(2, '0');
  const day = String(d.getDate()).padStart(2, '0');
  return `${year}-${month}-${day}`;
};

// 현재 날짜를 'yyyy-mm-dd' 형식으로 가져오는 함수
const getCurrentDate = () => {
  const today = new Date();
  const year = today.getFullYear();
  const month = String(today.getMonth() + 1).padStart(2, '0');
  const day = String(today.getDate()).padStart(2, '0');
  return `${year}-${month}-${day}`;
};

// 폼 입력값 검증
const validateForm = () => {
  if (!nttSj.value.trim()) {
    responseMessage.value = "제목은 필수 입력 항목입니다.";
    isSuccess.value = false;
    return false;
  }
  // 에디터의 HTML 내용을 가져와 nttCn에 저장
  nttCn.value = editor.value.getHTML().trim();
  if (!nttCn.value) {
    responseMessage.value = "게시글 내용을 입력해주세요.";
    isSuccess.value = false;
    return false;
  }

  // 게시 시작일과 종료일 유효성 검사
  const currentDate = getCurrentDate(); // 현재 날짜

  if (ntceBgnde.value < currentDate) {
    responseMessage.value = "게시 시작일은 오늘 이후로 설정해야 합니다.";
    isSuccess.value = false;
    return false;
  }

  if (ntceEndde.value < ntceBgnde.value) {
    responseMessage.value = "게시 종료일은 시작일 이후로 설정해야 합니다.";
    isSuccess.value = false;
    return false;
  }

  if (ntceEndde.value < currentDate) {
    responseMessage.value = "게시 종료일은 오늘 이후로 설정해야 합니다.";
    isSuccess.value = false;
    return false;
  }

  return true;
};

// 게시글 등록
const submitForm = async () => {
  if (!validateForm()) return;

  const formData = new FormData();
  formData.append('mberId', mberId.value);
  formData.append('wrterNm', wrterNm.value);
  formData.append('nttSj', nttSj.value);
  formData.append('nttCn', nttCn.value);
  formData.append('noticeAt', noticeAt.value ? 'Y' : 'N');  // 공지 여부 처리
  formData.append('ntceBgnde', formatDate(ntceBgnde.value));
  formData.append('ntceEndde', formatDate(ntceEndde.value));
  formData.append('bbsId', bbsId.value);  // 게시판 ID 확인

  try {
    // 게시글 등록 요청
    await axios.post("/api/bulletin/bulletinAdd", formData);
    
    Swal.fire({
        icon: "success",
        title: "등록 완료",
        text: "등록한 게시글은 목록에서 확인할 수 있습니다."
    }).then(() => {
        // 팝업 닫은 후 목록 페이지로 이동
        router.push({ path: '/bulletin/bulletinList/' + bbsId.value });
    });

  } catch (error) {   
    Swal.fire({
        icon: "error",
        title: "게시글 등록에 실패하였습니다.",
        text: "Error : " + (error.response?.data?.message || error.message)
    });
  }
};

// 초기화 기능
const resetForm = () => {
  // Reset form fields
  nttSj.value = '';
  nttCn.value = '';
  noticeAt.value = false;
  ntceBgnde.value = '';
  ntceEndde.value = '';

  // Destroy the editor instance safely and then reinitialize
  if (editor.value) {
    editor.value.destroy();  // Destroy the editor to avoid transaction mismatches
    editor.value = null;     // Clear the editor reference
  }

  // Reinitialize the editor
  initEditor();

  // Clear other states
  responseMessage.value = '';
  isSuccess.value = false;
};

// 컴포넌트 마운트 시 에디터 초기화
onMounted(() => {
  initEditor(); // Initialize the editor on component mount
});
</script>

<style scoped>
.sticky-top {
  position: sticky;
  top: 0;
  z-index: 1;
  background-color: #fff;
  box-shadow: 0px 2px 10px rgba(0, 0, 0, 0.1);
}
</style>
