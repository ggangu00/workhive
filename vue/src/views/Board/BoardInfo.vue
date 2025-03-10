<template>
  <div class="content">
    <div class="container-fluid">
      <div>
        <div class="card">
          <div class="card-body">
            <h4 class="card-title float-left mt-1">게시판 상세화면</h4>
            <button class="btn btn-secondary btn-fill float-right" @click="resetForm">초기화</button>
          </div>
        </div>
      </div>

      <!-- 조회 화면 -->
      <div class="card">
        <div class="card-body">
          <form>
            <div class="mb-3">
              <label class="form-label">게시판명</label>
              <div class="form-control-plaintext">{{ formValues.bbsNm || '제목이 없습니다.' }}</div>
            </div>

            <div class="mb-3">
              <label class="form-label">게시판소개</label>
              <div class="form-control-plaintext textarea-style">
                {{ formValues.bbsIntrcn || '내용이 없습니다.' }}
              </div>
            </div>
          </form>
        </div>
      </div> 

      <!-- 응답 메시지 -->
      <div v-if="responseMessage" class="alert" :class="isSuccess ? 'alert-success' : 'alert-danger'">
        {{ responseMessage }}
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue';
import axios from '../../assets/js/customAxios';
import { useRoute } from 'vue-router';

const route = useRoute();

// 📌 게시판 데이터 변수
const formValues = ref({
bbsId: '',
bbsNm: '',
bbsIntrcn: '',
});

// 📌 응답 메시지 변수
const responseMessage = ref('');
const isSuccess = ref(false);

// 📌 쿼리에서 데이터 적용
const setFormValuesFromQuery = () => {
formValues.value.bbsId = route.query.bbsId ?? '';
formValues.value.bbsNm = route.query.bbsNm && route.query.bbsNm.trim() !== '' ? route.query.bbsNm : '';
formValues.value.bbsIntrcn = route.query.bbsIntrcn && route.query.bbsIntrcn.trim() !== '' ? route.query.bbsIntrcn : '';

if (!formValues.value.bbsNm || !formValues.value.bbsIntrcn) {
  fetchBoardDetail();
}
};

// 📌 게시판 상세 API 요청
const fetchBoardDetail = async () => {
if (!formValues.value.bbsId) {
  return;
}

try {
  const { data } = await axios.get(`/api/board/boardDetail`, {
    params: { bbsId: formValues.value.bbsId },
  });

  if (data) {
    formValues.value.bbsNm = data.bbsNm ?? '제목 없음';
    formValues.value.bbsIntrcn = data.bbsIntrcn ?? '내용 없음';
  }
} catch (error) {
  console.error("❌ 게시판 상세 불러오기 실패:", error);
}
};

// 📌 폼 초기화
const resetForm = () => {
formValues.value = {
  bbsId: '',
  bbsNm: '',
  bbsIntrcn: '',
};
};

// ✅ 페이지 로드 시 쿼리 데이터 적용 후 API 요청
onMounted(() => {
setFormValuesFromQuery();
fetchBoardDetail();
});
</script>

<style scoped>
/* 🔹 조회 화면 스타일 */
.form-control-plaintext {
padding: 10px;
border: 1px solid #ddd;
border-radius: 5px;
background-color: #f8f9fa;
min-height: 40px;
}

/* 🔹 게시판 소개를 위한 스타일 (높이 조정) */
.textarea-style {
min-height: 600px; /* 기본 높이 */
max-height: 300px; /* 최대 높이 제한 */
overflow-y: auto; /* 스크롤 가능 */
white-space: pre-wrap; /* 줄바꿈 유지 */
word-wrap: break-word; /* 긴 단어 자동 줄바꿈 */
}
</style>
