<template>
  <div class="content">
    <div class="container-fluid">
    <div>
      <div class="card">
        <div class="card-body">
          <h4 class="card-title float-left mt-1">게시판 등록</h4>
          <button class="btn btn-primary btn-sm btn-fill float-right"
              @click="BoardSave">등록</button>
          <button class="btn btn-secondary btn-sm btn-fill float-right">초기화</button>
         
        </div>
      </div>
      </div>

      <!-- 입력 폼 -->
      <div class="card">            
          <div class="card-body">
            <form action="BoardAdd" method="post">
              <div class="mb-3">
                <label>게시판명 <em class="point-red">*</em></label>
                <input v-model="BoardAdd.bbsNm"   type="text" class="form-control" placeholder="게시판명을 입력해주세요">
              </div>

                    <div class="mb-3">
                      <label class="form-label">게시판 유형</label>
                      <select v-model="BoardAdd.bbsTyCode" class="form-select w30" aria-label="Default select example">
                        <option selected>선택하세요</option>
                        <option value="A01">공지사항</option>
                        <option value="A02">사내게시판</option>                        
                      </select>
                    </div>

                    <div class="mb-3">
                      <label class="form-label">파일첨부 가능여부</label>
                      <select v-model="BoardAdd.fileAtchPosblAt" class="form-select w30" aria-label="Default select example">
                        <option selected>선택하세요</option>
                        <option value="Y">예</option>
                        <option value="N">아니오</option>
                      </select>
                    </div>

                    <div class="mb-3">
                      <label class="form-label">댓글 가능여부</label>
                      <select v-model="BoardAdd.answerAt" class="form-select w30" aria-label="Default select example">
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

<script setup>
import { ref, onMounted } from "vue";
import axios from "axios";

// 📌 게시판 데이터 저장 변수 (초기값 설정)
const BoardAdd = ref({
  bbsNm: "",
  bbsTyCode: "",
  fileAtchPosblAt: "",
  answerAt: ""
});

const BoardList = ref([]);
const gridInstance = ref(null); // Toast Grid 사용 시

// 게시글 불러오기
const BoardGetAdd = async () => {
  try {
    const result = await axios.post("/api/board/boardAdd");
    if (Array.isArray(result.data.resultList) && result.data.resultList.length > 0) {
      BoardList.value = result.data.resultList; // 최신 데이터로 업데이트
    }
  } catch (error) {
    console.error("게시글 목록 불러오는 중 오류 발생:", error);
  }
  console.log(BoardList.value);
};

// 게시판 등록 함수 (등록 후 목록 추가)
const BoardSave = async () => {
  const formData = {
    BBS_NM: BoardAdd.value.bbsNm,
    BBS_TY_CODE: BoardAdd.value.bbsTyCode,
    FILE_ATCH_POSBL_AT: BoardAdd.value.fileAtchPosblAt,
    REPLY_POSBL_AT: BoardAdd.value.answerAt
  };

  try {
    const response = await axios.post("/api/board/boardAdd", formData);
    console.log("게시판 등록 성공:", response.data);
    alert("게시판이 성공적으로 등록되었습니다.");

    if (response.data.result) {
      BoardList.value = [...BoardList.value, response.data.result];

     
      if (gridInstance.value) {
        gridInstance.value.resetData(BoardList.value);
      }    }

    await BoardGetAdd();

  } catch (error) {
    console.error("게시판 등록 중 오류 발생:", error);
    alert("게시판 등록에 실패했습니다.");
  }
};

const resetForm = () => {
  BoardAdd.value.bbsNm = "";
  BoardAdd.value.bbsTyCode = "";
  BoardAdd.value.fileAtchPosblAt = "";
  BoardAdd.value.answerAt = "";
};


// 컴포넌트가 마운트되었을 때 게시글 불러오기 실행
onMounted(() => {
  BoardGetAdd();
  resetForm();
});
</script>


<style>


</style>
       