<template>
  <div class="content">
    <div class="container-fluid">
      <div>
        <div class="card">
          <div class="card-body">
            <h4 class="card-title float-left mt-1">게시판 등록</h4>
            <button class="btn btn-primary  btn-fill float-right" @click="BoardSave">등록</button>
            <button class="btn btn-secondary  btn-fill float-right" @click="resetForm">초기화</button>
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
<!-- 
            <div class="mb-3">
              <label class="form-label">게시판 유형</label>
              <select v-model="formValues.bbsTyCode" class="form-select w30">
                <option value="" disabled selected>선택하세요</option>
                <option value="A01">공지사항</option>
                <option value="A02">사내게시판</option>
              </select>
            </div> -->

            <!-- <div class="mb-3">
              <label class="form-label">파일첨부 가능여부</label>
              <select v-model="formValues.fileAtchPosblAt" class="form-select w30">
                <option value="" disabled selected>선택하세요</option>
                <option value="A01">예</option>
                <option value="A02">아니오</option>
              </select>
            </div> -->

            <!-- <div class="mb-3">
              <label class="form-label">댓글 가능여부</label>
              <select v-model="formValues.answerAt" class="form-select w30">
                <option value="" disabled selected>선택하세요</option>
                <option value="A01">예</option>
                <option value="A02">아니오</option>
              </select>
            </div> -->

            <!-- <div class="mb-3">
              <label class="form-label">사용여부</label>
              <select v-model="formValues.useAt" class="form-select w30">
                <option value="" disabled selected>선택하세요</option>
                <option value="A01">예</option>
                <option value="A02">아니오</option>
              </select>
            </div> -->
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
import { useRoute, useRouter } from 'vue-router';
import Swal from 'sweetalert2';

const route = useRoute();
const router = useRouter();

// 게시판 데이터 변수 (boardData 제거)
const formValues = ref({
  bbsId: 'abc',
  bbsNm: '',
  bbsAttrbCode: 'abc',
  bbsTyCode: '',
  fileAtchPosblAt: '',
  useAt: 'abc',
  frstRegisterId: 'abbc',
  frstRegisterPnttm: 'abc',
  answerAt: '',
});

// 응답 메시지와 성공 여부 변수
const responseMessage = ref('');
const isSuccess = ref(false);

// 쿼리 파라미터에서 값을 받아와 formValues에 적용
const setFormValuesFromQuery = () => {
  const query = route.query;

  formValues.value.bbsNm = query.bbsNm ?? '';
  formValues.value.bbsTyCode = query.bbsTyCode ?? '';
  formValues.value.fileAtchPosblAt = query.fileAtchPosblAt ?? '';
  formValues.value.answerAt = query.answerAt ?? '';
  formValues.value.useAt = query.useAt ?? '';
};

 // 폼 입력값 검증
// const validateForm = () => {
//   if (!formValues.value.bbsNm || !formValues.value.bbsTyCode || !formValues.value.fileAtchPosblAt || !formValues.value.answerAt) {
//     responseMessage.value = "모든 필수 항목을 입력해주세요.";
//     isSuccess.value = false;
//     return false;
//   }
//   return true;
// };

 // 게시판 등록 (FormData 방식)
   const BoardSave = async () => {
   const addData = new FormData();
   Object.entries(formValues.value).forEach(([key, value]) => {
     addData.append(key, value);
   });

  try {
    // 게시판 추가 요청 (POST)
    await axios.post('/api/board/boardAdd', addData);

    // 성공 시 SweetAlert2 알림 표시
    Swal.fire({
        icon: "success",
        title: "등록 완료",
        text: "게시판이 성공적으로 등록되었습니다!"
    }).then(() => {
        // 팝업 닫은 후 게시판 목록 페이지로 이동
        router.push('/board/boardList');
    });

  } catch (error) {
    // 오류 발생 시 SweetAlert2 알림 표시
    Swal.fire({
        icon: "error",
        title: "게시판 등록 실패",
        text: "Error : " + (error.response?.data?.message || "게시판 등록에 실패했습니다. 다시 시도해주세요.")
    });
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
</script>

<style scoped>
/* 필요한 스타일을 여기에 추가하세요 */
</style>
