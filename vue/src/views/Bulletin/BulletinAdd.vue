<template>
  <div class="content">
    <div class="container-fluid">
      <!-- 등록폼 -->
      <div class="card">
        <div class="card-body">
          <h4 class="card-title float-left mt-1">게시글 등록</h4>
          <button @click="submitForm" class="btn btn-primary btn-sm btn-fill float-right">등록</button>
          <button @click="goToBulletinList" class="btn btn-secondary btn-sm btn-fill float-right">목록</button>
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
              <!-- 에디터의 HTML 내용을 내부적으로 관리 (DB 컬럼과 매핑) -->
              <input type="hidden" v-model="nttCn"/>
            </div>

            <div class="mb-3">
              <label>공지여부</label>
              <div class="form-check form-check-inline" style="margin-left: 10px;">
                <input
                  class="form-check-input"
                  type="checkbox"
                  v-model="noticeAt"
                  :disabled="isDisabled('notice')"
                  @change="handleCheckChange('notice')"
                />
              </div>
            </div>

            <div class="mb-3">
              <label>익명여부</label>
              <div class="form-check form-check-inline" style="margin-left: 10px;">
                <input
                  class="form-check-input"
                  type="checkbox"
                  v-model="anoAt"
                  :disabled="isDisabled('anonymous')"
                  @change="handleCheckChange('anonymous')"
                />
              </div>
            </div>

            <div class="mb-3">
              <label>비밀여부</label>
              <div class="form-check form-check-inline" style="margin-left: 10px;">
                <input
                  class="form-check-input"
                  type="checkbox"
                  v-model="secretAt"
                  :disabled="isDisabled('secret')"
                  @change="handleCheckChange('secret')"
                />
              </div>
            </div>

            <!-- 비밀번호 입력 필드 -->
            <div class="mb-3" v-show="secretAt">
              <label>비밀번호 4자리를 입력해주세요</label>
              <input
                type="password"
                class="form-control w30"
                placeholder="비밀번호 입력"
                maxlength="4"
                v-model="password"
              />
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

            <!-- 파일첨부 (선택 사항) -->
            <div class="mb-3">
              <label>파일첨부</label>
              <div class="input-group w30" style="border: 1px solid #ccc; border-radius: 3px; background-color: #fff;">
                <input type="file" class="form-control" ref="fileInput" />
              </div>
            </div>
          </form>
        </div>
      </div>

      <!-- 응답 메시지 -->
      <div
        v-if="responseMessage"
        class="alert"
        :class="isSuccess ? 'alert-success' : 'alert-danger'"
        style="margin-top: 15px;"
      >
        {{ responseMessage }}
      </div>
    </div>
  </div>
</template>

<script setup>
import '@toast-ui/editor/dist/toastui-editor.css';
import { Editor } from '@toast-ui/editor';
import { ref, onMounted } from 'vue';
import axios from 'axios';
import { useRoute, useRouter } from 'vue-router';

const route = useRoute();
const router = useRouter();

// 필드 데이터
const bbsId = ref(route.query.bbsId);              // 게시판id
const nttSj = ref('');             // 제목
const nttCn = ref('');             // 에디터의 HTML 내용 (게시글 내용)
const noticeAt = ref(false);       // 공지여부
const secretAt = ref(false);       // 비밀여부
const anoAt = ref(false);          // 익명여부
const password = ref('');          // 비밀번호 (비밀글일 경우)
const ntceBgnde = ref('');   // 게시 시작일
const ntceEndde = ref('');    // 게시 종료일
const showPasswordField = ref(false); // 비밀번호 입력 필드 노출 여부

// TOAST UI Editor 인스턴스
const editor = ref(null);
// 파일 첨부용 ref
const fileInput = ref(null);

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

  if (secretAt.value && password.value.trim().length !== 4) {
    responseMessage.value = "비밀글인 경우 4자리 비밀번호를 입력해주세요.";
    isSuccess.value = false;
    return false;
  }
  return true;
};

const submitForm = async () => {
  if (!validateForm()) return;

  const formData = new FormData();
  formData.append('nttSj', nttSj.value);
  formData.append('nttCn', nttCn.value);
  formData.append('noticeAt', noticeAt.value ? 'Y' : 'N');
  formData.append('secretAt', secretAt.value ? 'Y' : 'N');
  formData.append('anoAt', anoAt.value ? 'Y' : 'N');
  formData.append('password', password.value);
  formData.append('ntceBgnde', formatDate(ntceBgnde.value));
  formData.append('ntceEndde', formatDate(ntceEndde.value));
  formData.append('bbsId', bbsId.value);  // 게시판 ID 확인

  if (fileInput.value.files.length > 0) {
    formData.append('file_1', fileInput.value.files[0]);
  }

  try {
    const response = await axios.post("/api/bulletin/bulletinAdd", formData);

    responseMessage.value = response.data?.message ?? "게시글이 성공적으로 등록되었습니다!";
    isSuccess.value = true;

    setTimeout(() => {
      router.push({ path: '/bulletin/bulletinList/'+ bbsId.value });
    }, 1000); // 1초 후 페이지 이동

  } catch (error) {
    responseMessage.value = "게시글 등록에 실패했습니다. 다시 시도해주세요.";
    isSuccess.value = false;
  }
};

const goToBulletinList = () => {
  router.push({ path: '/bulletin/bulletinList/'+ bbsId.value });
};

// 체크박스를 클릭하면 나머지 두 개는 비활성화
const isDisabled = (target) => {
  if (target === 'notice' && (secretAt.value || anoAt.value)) return true;
  if (target === 'anonymous' && (secretAt.value || noticeAt.value)) return true;
  if (target === 'secret' && (noticeAt.value || anoAt.value)) return true;
  return false;
};

// 체크박스 클릭 시 나머지 체크박스를 비활성화하는 함수
const handleCheckChange = (target) => {
  if (target === 'notice' && (secretAt.value || anoAt.value)) {
    secretAt.value = false;
    anoAt.value = false;
  } else if (target === 'anonymous' && (secretAt.value || noticeAt.value)) {
    secretAt.value = false;
    noticeAt.value = false;
  } else if (target === 'secret' && (noticeAt.value || anoAt.value)) {
    noticeAt.value = false;
    anoAt.value = false;
  }

  // 비밀여부가 체크되면 비밀번호 필드 표시
  if (secretAt.value) {
    showPasswordField.value = true;
  } else {
    showPasswordField.value = false;
  }
};

// 컴포넌트 마운트 시 에디터 초기화
onMounted(() => {
  initEditor();
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
