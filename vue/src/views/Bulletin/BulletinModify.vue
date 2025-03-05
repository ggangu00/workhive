<template>
  <div class="content">
    <div class="container-fluid">
      <!-- 수정폼 -->
      <div class="card">
        <div class="card-body">
          <h4 class="card-title float-left mt-1">게시글 수정</h4>
          <button class="btn btn-success btn-fill float-right" @click="submitForm">저장</button>
          <button @click="goToBulletinList" class="btn btn-secondary btn-sm btn-fill float-right">목록</button>
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
                <input class="form-check-input" type="checkbox" v-model="noticeAt" @change="handleNoticeChange" />
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
                <input
                  class="form-check-input"
                  type="checkbox"
                  v-model="secretAt"
                  @click="togglePasswordField"
                />
              </div>
            </div>

            <div class="mb-3" v-show="showPasswordField">
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
import axios from '../../assets/js/customAxios';
import { useRoute, useRouter } from 'vue-router';
import Swal from 'sweetalert2';

// 라우터 훅을 사용하여 파라미터를 가져옵니다
const route = useRoute();
const router = useRouter();

// URL에서 nttId와 bbsId를 받아옵니다
const nttId = ref(route.params.nttId);  // nttId는 URL 파라미터에서 받아옵니다.
const bbsId = ref(route.params.bbsId);   // bbsId는 쿼리 파라미터에서 받아옵니다.
//const bulletinInfo = ref({});





// 필드 데이터
const nttSj = ref('');             // 제목
const nttCn = ref('');             // 에디터의 HTML 내용 (게시글 내용)
const noticeAt = ref(false);       // 공지여부
const secretAt = ref(false);       // 비밀여부
const anoAt = ref(false);          // 익명여부
const password = ref('');          // 비밀번호 (비밀글일 경우)
const ntceBgnde = ref('');         // 게시 시작일
const ntceEndde = ref('');         // 게시 종료일
const showPasswordField = ref(false); // 비밀번호 입력 필드 노출 여부

// TOAST UI Editor 인스턴스
let editor =null;

// 파일 첨부용 ref
const fileInput = ref(null);

// 응답 메시지 및 성공 여부
const responseMessage = ref('');
const isSuccess = ref(false);

// 비밀번호 필드 토글 함수
const togglePasswordField = () => {
  showPasswordField.value = !showPasswordField.value;
};

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
      secretAt.value = postData.secretAt === 'Y';
      anoAt.value = postData.anoAt === 'Y';

      if (editor) {
        editor.setHTML(nttCn.value);
      }

    } else {
      responseMessage.value = "게시글을 찾을 수 없습니다.";
      isSuccess.value = false;
    }
  } catch (error) {
    console.error('게시글 데이터 불러오기 오류:', error);
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

  if (secretAt.value && password.value.trim().length !== 4) {
    responseMessage.value = "비밀글인 경우 4자리 비밀번호를 입력해주세요.";
    isSuccess.value = false;
    return false;
  }
  return true;
};

// 게시글 수정
const submitForm = async () => {
  if (!validateForm()) return;

  // 폼 데이터를 준비합니다
  const formData = new FormData();
  formData.append('nttSj', nttSj.value);
  formData.append('nttCn', nttCn.value);
  formData.append('ntceBgnde', formatDate(ntceBgnde.value));
  formData.append('ntceEndde', formatDate(ntceEndde.value));
  formData.append('bbsId', bbsId.value);
  formData.append('nttId', nttId.value);

  // 파일 첨부 처리
  const fileInput = document.querySelector('input[type="file"]');
  if (fileInput && fileInput.files.length > 0) {
    formData.append('file_1', fileInput.files[0]);
  }

  // 공지, 비밀 여부 등 처리
  formData.append('noticeAt', noticeAt.value ? 'Y' : 'N');
  formData.append('secretAt', secretAt.value ? 'Y' : 'N');
  formData.append('anoAt', anoAt.value ? 'Y' : 'N');

  try {
    if (bbsId.value && nttId.value) {
        // 기존 게시글 수정 (POST 요청)
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

    } else {
        // 새 게시글 추가 (POST 요청)
        await axios.post("/api/bulletin/bulletinAdd", formData);

        // 성공 시 SweetAlert2 알림 표시
        Swal.fire({
            icon: "success",
            title: "등록 완료",
            text: "등록한 게시글은 목록에서 확인할 수 있습니다."
        }).then(() => {
            // 팝업 확인 후 목록 페이지 이동
            router.push({ path: `/bulletin/bulletinList/${bbsId.value}` });
        });
    }

} catch (error) {
    // 오류 발생 시 SweetAlert2 알림 표시
    Swal.fire({
        icon: "error",
        title: "게시글 처리 실패",
        text: "Error : " + (error.response?.data?.message || error.message)
    });
}


};

// 게시글 목록으로 이동
const goToBulletinList = () => {
  router.push({ name: 'BulletinList', params: { bbsId: bbsId.value } });
};

// 페이지 로드 시 기존 데이터 불러오기
onMounted(() => {
  fetchBulletinInfo();
  initEditor();  // 에디터 초기화
 
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
