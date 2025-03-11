<template>
  <div class="content">
    <div class="container-fluid">
      <!-- 수정폼 -->
      <div class="card">
        <div class="card-body">
          <h4 class="card-title float-left mt-1">게시글 수정</h4>
          <button class="btn btn-success btn-fill float-right" @click="submitForm">저장</button>
          <button @click="resetForm" class="btn btn-secondary btn-fill float-right">초기화</button>
        </div>
      </div>

      <!-- 입력폼 -->
      <div class="card" :class="{'sticky-top': noticeAt}">
        <div class="card-body">
          <form @submit.prevent="submitForm">
            <div class="mb-3">
              <label>제목 <em class="point-red">*</em></label>
              <input type="text" class="form-control" placeholder="제목을 입력해주세요" v-model="nttSj" />
            </div>

            <!-- 에디터 영역 -->
            <div class="col-12">
              <div id="editor"></div>
              <input type="hidden" v-model="nttCn" />
            </div>

            <div class="mb-3">
              <label>공지여부</label>
              <div class="form-check form-check-inline" style="margin-left: 10px;">
                <input class="form-check-input" type="checkbox" v-model="noticeAt" />
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

// 라우터 훅을 사용하여 파라미터를 가져옵니다
const route = useRoute();
const router = useRouter();

// URL에서 nttId와 bbsId를 받아옵니다
const nttId = ref(route.params.nttId);  // nttId는 URL 파라미터에서 받아옵니다.
const bbsId = ref(route.params.bbsId);   // bbsId는 쿼리 파라미터에서 받아옵니다.

// 필드 데이터
const nttSj = ref('');             // 제목
const nttCn = ref('');             // 에디터의 HTML 내용 (게시글 내용)
const noticeAt = ref(false);       // 공지여부
const ntceBgnde = ref('');         // 게시 시작일
const ntceEndde = ref('');         // 게시 종료일

// TOAST UI Editor 인스턴스
let editor = null;

// 응답 메시지 및 성공 여부
const responseMessage = ref('');
const isSuccess = ref(false);

// TOAST UI Editor 초기화
const initEditor = () => {
  editor = new Editor({
    el: document.querySelector("#editor"),
    height: "500px",
    initialEditType: "WYSIWYG",
    previewStyle: "vertical"
  });
};

// 게시글 데이터 불러오기
const fetchBulletinInfo = async () => {
  try {
    const response = await axios.get(`/api/bulletin/bulletinInfo?nttId=${nttId.value}&bbsId=${bbsId.value}`);
    
    if (response.data && response.data.result) {
      const postData = response.data.result;

      // 불러온 데이터를 폼에 채웁니다
      nttSj.value = postData.nttSj || '';
      nttCn.value = postData.nttCn || '';
      ntceBgnde.value = formatDate(postData.ntceBgnde);   // 게시 시작일
      ntceEndde.value = formatDate(postData.ntceEndde);   // 게시 종료일
      noticeAt.value = postData.noticeAt === 'Y';

      if (editor) {
        editor.setHTML(nttCn.value); // 에디터 내용 설정
      }

    } else {
      alert("게시글을 찾을 수 없습니다.");
      responseMessage.value = "게시글을 찾을 수 없습니다.";
      isSuccess.value = false;
    }
  } catch (error) {
    alert("게시글을 불러오는 데 오류가 발생했습니다. 다시 시도해주세요.");
    responseMessage.value = "게시글을 불러오는 데 오류가 발생했습니다.";
    isSuccess.value = false;
  }
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
    responseMessage.value = "제목을 입력해주세요.";
    isSuccess.value = false;
    return false;
  }
  nttCn.value = editor.getHTML().trim();
  if (!nttCn.value) {
    responseMessage.value = "내용을 입력해주세요.";
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

// 게시글 수정
const submitForm = async () => {
  if (!validateForm()) return;

  const formData = new FormData();
  formData.append('nttSj', nttSj.value);
  formData.append('nttCn', nttCn.value);
  formData.append('ntceBgnde', formatDate(ntceBgnde.value));
  formData.append('ntceEndde', formatDate(ntceEndde.value));
  formData.append('bbsId', bbsId.value);
  formData.append('nttId', nttId.value);
  formData.append('noticeAt', noticeAt.value ? 'Y' : 'N'); // 공지 여부 처리

  try {
    // 게시글 수정 요청
    await axios.post(`/api/bulletin/bulletinModify/${bbsId.value}/${nttId.value}`, formData);
    
    // 성공 시 SweetAlert2 알림 표시
    Swal.fire({
      icon: "success",
      title: "수정 완료",
      text: "게시글이 성공적으로 수정되었습니다."
    }).then(() => {
      // 팝업 확인 후 목록 페이지 이동
      router.push({ path: `/bulletin/bulletinList/${bbsId.value}` });
    });
  } catch (error) {
    // 오류 발생 시 SweetAlert2 알림 표시
    Swal.fire({
      icon: "error",
      title: "게시글 수정 실패",
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

  // Destroy and reinitialize editor to avoid transaction conflicts
  if (editor) {
    editor.destroy();
    editor = null;
  }
  initEditor(); // Reinitialize the editor

  responseMessage.value = '';
  isSuccess.value = false;
};

// 페이지 로드 시 기존 데이터 불러오기
onMounted(() => {
  fetchBulletinInfo();  // Fetch bulletin info on mount
  initEditor();         // Initialize the editor
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
