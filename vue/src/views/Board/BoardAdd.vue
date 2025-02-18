<script setup>
import { ref, onMounted } from 'vue';
import axios from 'axios';

// 게시판 데이터 변수
const formValues = ref({
  bbsId: 'abc',
  bbsNm: '', // 게시판명
  bbsAttrbCode: 'abc',
  bbsTyCode: '', // 게시판 유형
  fileAtchPosblAt: '', // 파일 첨부 가능 여부
  useAt: 'abc',
  frstRegisterId: 'abbc',
  frstRegisterPnttm: 'abc',
  answerAt: '', // 댓글 가능 여부
});

// 응답 메시지와 성공 여부 변수
const responseMessage = ref('');
const isSuccess = ref(false);

// 폼 입력값 검증
const validateForm = () => {
  if (!formValues.value.bbsNm || !formValues.value.bbsTyCode || !formValues.value.fileAtchPosblAt || !formValues.value.answerAt) {
    responseMessage.value = "모든 필수 항목을 입력해주세요.";
    isSuccess.value = false;
    return false;
  }
  return true;
};

// 게시판 등록 (FormData 방식)
const BoardSave = async () => {
  if (!validateForm()) return; // 폼 검증 실패 시 실행하지 않음

  const addData = new FormData();
  addData.append("bbsId", formValues.value.bbsId);
  addData.append("bbsNm", formValues.value.bbsNm);
  addData.append("bbsAttrbCode", formValues.value.bbsAttrbCode);
  addData.append("bbsTyCode", formValues.value.bbsTyCode);
  addData.append("fileAtchPosblAt", formValues.value.fileAtchPosblAt);
  addData.append("useAt", formValues.value.useAt);
  addData.append("frstRegisterId", formValues.value.frstRegisterId);
  addData.append("frstRegisterPnttm", formValues.value.frstRegisterPnttm);
  addData.append("answerAt", formValues.value.answerAt);

  try {
    const result = await axios.post('/api/board/boardAdd', addData);
    // 서버 응답을 이용해 메시지 설정
    responseMessage.value = result.data.message || "게시판이 성공적으로 등록되었습니다!";
    isSuccess.value = true;
  } catch (error) {
    responseMessage.value = "게시판 등록에 실패했습니다. 다시 시도해주세요.";
    isSuccess.value = false;
  }
};

// 폼 초기화
const resetForm = () => {
  formValues.value.bbsNm = '';
  formValues.value.bbsTyCode = '';
  formValues.value.fileAtchPosblAt = '';
  formValues.value.answerAt = '';
  formValues.value.bbsId = '';
  formValues.value.bbsAttrbCode = '';
  formValues.value.frstRegisterId = '';
  formValues.value.frstRegisterPnttm = '';
  formValues.value.useAt = '';
};

// 페이지 로드 시 초기화
onMounted(() => {
  resetForm();
});
</script>

<template>
  <div class="content">
    <div class="container-fluid">
      <div>
        <div class="card">
          <div class="card-body">
            <h4 class="card-title float-left mt-1">게시판 등록</h4>
            <button class="btn btn-primary btn-sm btn-fill float-right" @click="BoardSave">등록</button>
            <button class="btn btn-secondary btn-sm btn-fill float-right" @click="resetForm">초기화</button>
          </div>
        </div>
      </div>

      <!-- 입력 폼 -->
      <div class="card">
        <div class="card-body">
          <form>
            <div class="mb-3">
              <label>게시판명 <em class="point-red">*</em></label>
              <input v-model="formValues.bbsNm" type="text" class="form-control" placeholder="게시판명을 입력해주세요">
            </div>

            <div class="mb-3">
              <label class="form-label">게시판 유형</label>
              <select v-model="formValues.bbsTyCode" class="form-select w30">
                <option selected>선택하세요</option>
                <option value="A01">공지사항</option>
                <option value="A02">사내게시판</option>
              </select>
            </div>

            <div class="mb-3">
              <label class="form-label">파일첨부 가능여부</label>
              <select v-model="formValues.fileAtchPosblAt" class="form-select w30">
                <option selected>선택하세요</option>
                <option value="Y">예</option>
                <option value="N">아니오</option>
              </select>
            </div>

            <div class="mb-3">
              <label class="form-label">댓글 가능여부</label>
              <select v-model="formValues.answerAt" class="form-select w30">
                <option selected>선택하세요</option>
                <option value="Y">예</option>
                <option value="N">아니오</option>
              </select>
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

<style scoped>
/* 필요한 스타일을 여기에 추가하세요 */
</style>
