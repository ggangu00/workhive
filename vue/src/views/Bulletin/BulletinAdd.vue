<template>
  <div class="content">
    <div class="container-fluid">
      <!-- 등록폼 [S] -->
      <div class="card">
        <div class="card-body">
          <h4 class="card-title float-left mt-1">게시글 등록</h4>
          <button @click="submitForm" class="btn btn-primary btn-sm btn-fill float-right">등록</button>
          <button @click="resetForm" class="btn btn-secondary btn-sm btn-fill float-right">초기화</button>
        </div>
      </div>

      <div class="card" :class="{'sticky-top': noticeAt}">
        <div class="card-body">
          <form @submit.prevent="submitForm">
            <div class="mb-3">
              <label>제목 <em class="point-red ">*</em></label>
              <input type="text" class="form-control" placeholder="제목을 입력해주세요" v-model="nttSj" />
            </div>

            <!-- 에디터 생성 -->
            <div class="col-12">
              <div id="editor"></div>
            </div>

            <div class="mb-3">
              <label>공지여부</label>
              <div class="form-check form-check-inline" style="margin-left: 10px;">
                <input class="form-check-input" type="checkbox" v-model="noticeAt" />
              </div>
            </div>

            <div class="mb-3">
              <label>익명여부</label>
              <div class="form-check form-check-inline" style="margin-left: 10px;">
                <input class="form-check-input" type="checkbox" v-model="anoAt" />
              </div>
            </div>

            <div class="mb-3">
              <label>비밀여부</label>
              <div class="form-check form-check-inline" style="margin-left: 10px;">
                <input class="form-check-input" type="checkbox" v-model="secretAt" @click="togglePasswordField" />
              </div>
            </div>

            <div class="mb-3" v-show="showPasswordField">
              <label>비밀번호 4자리를 입력해주세요</label>
              <input type="password" class="form-control w30" placeholder="비밀번호 입력" maxlength="4" v-model="password" />
            </div>

            <div class="mb-3">
              <label class="form-label">게시기간 <em class="point-red">*</em></label>
              <div class="row">
                <div class="col-auto">
                  <input type="date" v-model="frstRegistPnttm" class="form-control" required />
                </div>
                <div class="col-auto">~</div>
                <div class="col-auto">
                  <input type="date" v-model="lastUpdttPnttm" class="form-control" required />
                </div>
              </div>
            </div>

            <div class="mb-3">
              <label>파일첨부 <em class="point-red ">*</em></label>
              <div class="input-group w30" style="border: 1px solid #ccc; border-radius: 3px; background-color: #fff;">
                <input type="file" class="form-control" ref="fileInput" />
              </div>
            </div>
          </form>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import '@toast-ui/editor/dist/toastui-editor.css';
import { Editor } from '@toast-ui/editor';
import { ref, onMounted } from 'vue';
import axios from 'axios';

// 필드 데이터 설정
const nttSj = ref(''); // 제목
const noticeAt = ref(false); // 공지여부
const secretAt = ref(false); // 비밀여부
const anoAt = ref(false); // 익명여부
const password = ref(''); // 비밀번호
const frstRegistPnttm = ref(''); // 게시 시작일
const lastUpdttPnttm = ref(''); // 게시 종료일
const showPasswordField = ref(false); // 비밀번호 필드 표시 여부
const editor = ref(null); // TOAST UI Editor 인스턴스 저장

// 파일 첨부용 ref
const fileInput = ref(null);

// 비밀번호 필드 토글
const togglePasswordField = () => {
  showPasswordField.value = !showPasswordField.value;
};

// TOAST UI Editor 초기화
const initEditor = () => {
  editor.value = new Editor({
    el: document.querySelector("#editor"),
    height: "500px",
    initialEditType: "WYSIWYG",
    previewStyle: "vertical"
  });
};

// 날짜 형식을 'yyyy-mm-dd'로 변환하는 함수
const formatDate = (date) => {
  const d = new Date(date);
  const year = d.getFullYear();
  const month = String(d.getMonth() + 1).padStart(2, '0'); // 월은 0부터 시작
  const day = String(d.getDate()).padStart(2, '0');
  return `${year}-${month}-${day}`;
};

// 폼 제출
const submitForm = async () => {
  const editorContent = editor.value.getHTML(); // Get editor content

  // 날짜 형식을 서버가 요구하는 형식으로 변환
  const formattedFrstRegistPnttm = formatDate(frstRegistPnttm.value);
  const formattedLastUpdttPnttm = formatDate(lastUpdttPnttm.value);

  // FormData 사용해서 전송 (파일 첨부도 가능)
  const formData = new FormData();
  formData.append('nttSj', nttSj.value);
  formData.append('content', editorContent);
  formData.append('noticeAt', noticeAt.value);
  formData.append('secretAt', secretAt.value);
  formData.append('anoAt', anoAt.value);
  formData.append('password', password.value);
  formData.append('frstRegistPnttm', formattedFrstRegistPnttm);
  formData.append('lastUpdttPnttm', formattedLastUpdttPnttm);

  // 파일 첨부
  if (fileInput.value.files.length > 0) {
    formData.append('file', fileInput.value.files[0]);
  }

  try {
    const response = await axios.post("/api/bulletin/bulletinAdd", formData, {
      headers: {
        "Content-Type": "multipart/form-data",  // Correct content type for form data
      },
    });
    console.log("게시글 등록 성공", response);
  } catch (error) {
    console.error("게시글 등록 실패", error);
  }
};

// 초기화 함수
const resetForm = () => {
  nttSj.value = '';
  noticeAt.value = false;
  secretAt.value = false;
  anoAt.value = false;
  password.value = '';
  frstRegistPnttm.value = '';
  lastUpdttPnttm.value = '';
  showPasswordField.value = false;
  editor.value.setHTML(''); // 에디터 초기화
};

// 컴포넌트가 마운트될 때 에디터 초기화
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
