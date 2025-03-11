<template>
  <div class="content">
    <div class="container-fluid">
      <div>
        <div class="card">
          <div class="card-body">
            <h4 class="card-title float-left mt-1">게시판 수정</h4>
            <button class="btn btn-success btn-fill float-right" @click="BoardSave">수정</button>
            <button class="btn btn-secondary btn-fill float-right" @click="resetForm">초기화</button>
          </div>
        </div>
      </div>

      <!-- 입력 폼 -->
      <div class="card">
        <div class="card-body">
          <form>
            <div class="mb-3">
              <label>게시판명 </label>
              <input v-model="formValues.bbsNm" type="text" class="form-control" placeholder="게시판명을 입력해주세요">
            </div>

            <div class="mb-3">
              <label>게시판소개 </label>
              <textarea v-model="formValues.bbsIntrcn" class="form-control textarea-style" 
                placeholder="게시판 소개를 입력해주세요" rows="5"></textarea>
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
import { ref, onMounted, onBeforeMount } from 'vue';
import axios from '../../assets/js/customAxios';
import { useRouter, useRoute } from 'vue-router';
import Swal from 'sweetalert2';

const router = useRouter();
const route = useRoute(); // 쿼리 추출 시 useRoute 사용

// 게시판 데이터 변수
const formValues = ref({
  bbsId: '',
  bbsNm: '',
  bbsIntrcn: '',
});

const responseMessage = ref('');
const isSuccess = ref(false);

const setFormValuesFromQuery = () => {
  formValues.value.bbsId = route.query.bbsId ?? '';
  formValues.value.bbsNm = route.query.bbsNm ?? '';
  formValues.value.bbsIntrcn = route.query.bbsIntrcn ?? ''; 
};

// 게시판 수정 (FormData 방식)
const BoardSave = async () => {
  const addData = new FormData();
  Object.entries(formValues.value).forEach(([key, value]) => {
    addData.append(key, value);
  });

  try {
    await axios.post('/api/board/boardModify', addData);

    Swal.fire({
        icon: "success",
        title: "수정 완료",
        text: "게시판이 성공적으로 수정되었습니다!"
    }).then(() => {
        router.push('/board/boardList');
    });

  } catch (error) {
    Swal.fire({
        icon: "error",
        title: "게시판 수정 실패",
        text: "Error : " + (error.response?.data?.message || "게시판 수정에 실패했습니다. 다시 시도해주세요.")
    });
  }
};

// 폼 초기화
const resetForm = () => {
  formValues.value = {
    bbsId: '',
    bbsNm: '',
    bbsIntrcn: '',
  };
};

// ✅ 페이지 로드 시 초기화 및 쿼리 데이터 적용
onMounted(() => {
  setFormValuesFromQuery();
});

onBeforeMount(() => {});
</script>

<style scoped>
/* 🔹 게시판 소개 입력 칸 스타일 (Enter 적용 및 높이 증가) */
.textarea-style {
  min-height: 600px; /* 기본 높이를 100px로 설정 */
  max-height: 300px; /* 최대 높이를 300px로 제한 */
  resize: vertical; /* 사용자가 크기를 조절할 수 있도록 설정 */
  white-space: pre-wrap; /* 줄바꿈 유지 */
  word-wrap: break-word; /* 긴 단어 자동 줄바꿈 */
}
</style>
