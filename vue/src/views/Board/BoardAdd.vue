<template>
  <div class="content">
    <div class="container-fluid">
    <div>
      <div class="card">
        <div class="card-body">
          <h4 class="card-title float-left mt-1">게시판 등록</h4>
          <button class="btn btn-primary btn-sm btn-fill float-right"
            onclick="location.href ='#/admin/project/add'">등록</button>
          <button class="btn btn-secondary btn-sm btn-fill float-right">초기화</button>
        </div>
      </div>
      </div>

      <!-- 입력 폼 -->
      <div class="card">            
          <div class="card-body">
            <form action="BoardAdd" method="post">
              <div class="mb-3">
                <label>제목 <em class="point-red">*</em></label>
                <input   type="text" class="form-control" placeholder="제목을 입력해주세요">
              </div>

                    <div class="mb-3">
                      <label class="form-label">게시판 유형</label>
                      <select v-model="newBoard.bbsTyCode" class="form-select w30" aria-label="Default select example">
                        <option selected>선택하세요</option>
                        <option value="A01">공지사항</option>
                        <option value="A02">사내게시판</option>
                        <option value="A03">Q&A</option>
                        <option value="A04">자료실</option>
                      </select>
                    </div>

                    <div class="mb-3">
                      <label class="form-label">파일첨부 가능여부</label>
                      <select v-model="newBoard.fileAtchPosblAt" class="form-select w30" aria-label="Default select example">
                        <option selected>선택하세요</option>
                        <option value="Y">예</option>
                        <option value="N">아니오</option>
                      </select>
                    </div>

                    <div class="mb-3">
                      <label class="form-label">댓글 가능여부</label>
                      <select v-model="newBoard.answerAt" class="form-select w30" aria-label="Default select example">
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
// import { onMounted, ref } from 'vue';
// import axios from "axios";

// const BoardAdd = ref([]);  // 게시판 데이터 저장
// let gridInstance = ref();  // Toast Grid 인스턴스


// const newBoard = ref({
//   bbsNm: '', // 게시판명
//   bbsTyCode: '', // 게시판 유형 (드롭다운 선택 값)
//   fileAtchPosblAt: '' ,// 파일 첨부가능여부
//   answerAt:''
// });

// const boardTypes = ref([
//   { value: '공지사항', label: '공지사항' },
//   { value: '자유게시판', label: '자유게시판' },
//   { value: 'Q&A', label: 'Q&A' },
//   { value: '자료실', label: '자료실' }
// ]);

// const fileAttachOptions = ref([
//   { value: 'Y', label: '예' },
//   { value: 'N', label: '아니오' }
// ]);

// const replyOptions = ref([
//   { value: 'Y', label: '예' },
//   { value: 'N', label: '아니오' }
// ]);



// //===========axios===============
// const BoardList = ref([]); //초기값 배열

// // API 호출 (게시글 불러오기)
// const BoardGetAdd = async () => {
//   try {
//     const result = await axios.post('/api/board/boardAdd');
//     BoardAdd.value = result.data.resultList;    
//   } catch (error) {
//     console.error('게시글 목록 불러오는중 오류 발생:', error)
//    BoardAdd.value = [];   
//   } 
//   console.log(BoardAdd.value);
//   gridInstance.value.resetData(BoardAdd.value);
// }

// const formData = {
//       BBS_NM: newBoard.value.boardName,
//       BBS_TY_CODE: newBoard.value.boardType,
//       FILE_ATCH_POSBL_AT: newBoard.value.fileAttach,
//       REPLY_POSBL_AT: newBoard.value.allowReply
//     };

</script>

<style>


</style>
       