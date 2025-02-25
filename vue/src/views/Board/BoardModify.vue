<template>
    <div class="content">
      <div class="container-fluid">
        <div>
          <div class="card">
            <div class="card-body">
              <h4 class="card-title float-left mt-1">게시판 수정</h4>
              <button class="btn btn-success btn-fill float-right" @click="BoardSave">수정</button>
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
                  <option value="" disabled selected>선택하세요</option>
                  <option value="A01">공지사항</option>
                  <option value="A02">사내게시판</option>
                </select>
              </div>
  
              <div class="mb-3">
                <label class="form-label">파일첨부 가능여부</label>
                <select v-model="formValues.fileAtchPosblAt" class="form-select w30">
                  <option value="" disabled selected>선택하세요</option>
                  <option value="A01">예</option>
                  <option value="A02">아니오</option>
                </select>
              </div>
  
              <div class="mb-3">
                <label class="form-label">댓글 가능여부</label>
                <select v-model="formValues.answerAt" class="form-select w30">
                  <option value="" disabled selected>선택하세요</option>
                  <option value="A01">예</option>
                  <option value="A02">아니오</option>
                </select>
              </div>

              <div class="mb-3">
                <label class="form-label">사용여부</label>
                <select v-model="formValues.useAt" class="form-select w30">
                  <option value="" disabled selected>선택하세요</option>
                  <option value="A01">예</option>
                  <option value="A02">아니오</option>
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
  
  <script setup>
  import { ref, onMounted, onBeforeMount } from 'vue';
  import axios from 'axios';
  import { useRouter, useRoute } from 'vue-router';

  
  const router = useRouter();
  const route = useRoute(); // 쿼리 추출 시 useRoute 사용
  
  //  쿼리 파라미터에서 데이터 추출 (쿼리 파라미터 이름과 일치시킴)
  let boardData = ref({
    bbsNm: route.query.bbsNm || '',
    bbsTyCode: route.query.bbsTyCode || '',          //  게시판 유형
    fileAtchPosblAt: route.query.fileAtchPosblAt || '', //  파일 첨부 여부
    answerAt: route.query.answerAt || '' ,            //  댓글 여부
    useAt: route.query.useAt || ''             //  사용여부
  });
  console.log(' route.query:', route.query);
  console.log('받은 데이터:', boardData.value);
  
  // 게시판 데이터 변수
  const formValues = ref({
    bbsId: 'abc',
    bbsNm: '', 
    bbsAttrbCode: 'abc',
    bbsTyCode: '', 
    fileAtchPosblAt: '', 
    useAt: '',
    frstRegisterId: 'abbc',
    frstRegisterPnttm: 'abc',
    answerAt: '', 
  });
  
  // 응답 메시지와 성공 여부 변수
  const responseMessage = ref('');
  const isSuccess = ref(false);
  
  const setFormValuesFromQuery = () => {
    const query = route.query;
  console.log("응답:",query);
    formValues.value.bbsId = query.bbsId ?? '';
    formValues.value.bbsNm = query.bbsNm ?? '';
    formValues.value.bbsTyCode = query.bbsTyCode ?? '';
    formValues.value.fileAtchPosblAt = query.fileAtchPosblAt ?? '';
    formValues.value.answerAt = query.answerAt ?? '';
    formValues.value.useAt = query.useAt ?? '';
  
    console.log('✅ 적용된 formValues:', formValues.value);
  };
  
  
  // 폼 입력값 검증
  const validateForm = () => {
    if (!formValues.value.bbsNm || !formValues.value.bbsTyCode || !formValues.value.fileAtchPosblAt || !formValues.value.answerAt) {
      responseMessage.value = "모든 필수 항목을 입력해주세요.";
      isSuccess.value = false;
      return false;
    }
    return true;
  };
  
  // 게시판 수정 (FormData 방식)
  const BoardSave = async () => {
    if (!validateForm()) return; 
  
    const addData = new FormData();
    Object.entries(formValues.value).forEach(([key, value]) => {
      addData.append(key, value);
    });
  
    try {
      const result = await axios.post('/api/board/boardModify', addData);
      responseMessage.value = result.data.message || "게시판이 성공적으로 등록되었습니다!";
      isSuccess.value = true;
  
      setTimeout(() => {
        router.push('/board/boardList');
      }, 1000); 
    } catch (error) {
      responseMessage.value = "게시판 등록에 실패했습니다. 다시 시도해주세요.";
      isSuccess.value = false;
    }
  };
  
  // 폼 초기화
  const resetForm = () => {
    formValues.value = {
      bbsId: '',
      bbsNm: '',
      bbsAttrbCode: '',
      bbsTyCode: '',
      fileAtchPosblAt: '',
      useAt: '',
      frstRegisterId: '',
      frstRegisterPnttm: '',
      answerAt: '',
    };
  };
  
  // ✅ 페이지 로드 시 초기화 및 쿼리 데이터 적용
  onMounted(() => {
    resetForm();             
    setFormValuesFromQuery(); 
  });

  onBeforeMount(()=>{
    let cd = route.query;
    console.log("전체 테스트",cd);
  })
  </script>
  
  
  
  
  
  
  <style scoped>
  /* 필요한 스타일을 여기에 추가하세요 */
  </style>
  