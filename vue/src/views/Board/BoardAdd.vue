<script setup>
import { ref, onMounted } from 'vue';
import axios from 'axios';

// 게시판 데이터 변수
const formValues = ref({
  bbsId:'abc',
  bbsNm: '',   // 게시판명
  bbsAttrbCode:'abc',      
  bbsTyCode: '', // 게시판 유형
  fileAtchPosblAt: '', // 파일 첨부 가능 여부
  useAt:'abc',    
  frstRegisterId:'abbc',
  frstRegisterPnttm:'abc',
  answerAt: '',      // 댓글 가능 여부
});

// 게시판 등록 (FormData 방식)
const BoardSave = async () => {
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

    const result = await axios.post('/api/board/boardAdd', addData);
     console.log("서버 응답:", result.data)
    };

  

// 📌 입력 폼 초기화
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

// 📌 페이지 로드 시 초기화
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

    </div>
  </div>
</template>

<style>
/* 필요한 스타일을 여기에 추가하세요 */
</style>
