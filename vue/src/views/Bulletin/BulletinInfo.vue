<template>
  <div class="content">
    <div class="container-fluid">
      <!-- 상단 버튼 영역 -->
      <div class="card">
        <div class="card-body">
          <h4 v-if="bbsNm" class="card-title float-left mt-1 font-weight-bold">{{ bbsNm }}</h4>   
          <!-- <button class="btn btn-danger btn-fill float-right" @click="deleteBulletin">삭제</button> -->
          <button class="btn btn-success btn-fill float-right" @click="goToBulletinModify">수정</button>
          <button @click="goToBulletinList" class="btn btn-secondary btn-fill float-right">목록</button>
        </div>
      </div>

     <!-- 상세조회 내용 영역 -->
     <div class="card" v-if="bulletinInfo">
        <div class="card-body">
          <form>
            <!-- 제목 및 작성정보 영역 -->
            <div class="mb-3" style="text-align: center;">
              <label style="font-size: 30px;">{{ bulletinInfo.nttSj || '-' }}</label>
            </div>

            <div style="background-color: #f4f4f4; padding: 10px; text-align: center; font-size: 14px; color: #555; margin-bottom: 20px;">
              <span style="font-weight: bold;">작성자:</span>
              <span style="font-weight: bold; text-align: center; width: 150px; display: inline-block; margin-left: 5px;">
              {{ bulletinInfo.wrterNm || '-' }}
              </span>

              <span style="margin: 0 15px;">|</span>

              <span style="font-weight: bold;">등록일:</span>
              <span style="text-align: center; width: 150px; display: inline-block; margin-left: 5px;">
              {{ bulletinInfo.frstRegistPnttm || '-' }}
              </span>

              <span style="margin: 0 15px;">|</span>

              <span style="font-weight: bold;">조회수:</span>
              <span style="text-align: center; width: 80px; display: inline-block; margin-left: 5px;">
                {{ bulletinInfo.inqireCo ?? 0 }}
              </span>
            </div>


           <!-- 게시글 내용 -->
              <div class="mb-3">
                <div class="content-box">
                  <span v-html="bulletinInfo.nttCn || '내용이 없습니다.'"></span>
                </div>
              </div>

          <!-- 게시기간 -->
              <div class="mb-3">
                <label class="form-label">게시기간 </label><br>
                <div class="row">
                  <div class="col-auto">
                    <span class="info-box">{{ formatDate(bulletinInfo.ntceBgnde) }}</span>
                  </div>
                  <div class="col-auto">~</div>
                  <div class="col-auto">
                    <span class="info-box">{{ formatDate(bulletinInfo.ntceEndde) }}</span>
                  </div>
                </div>
              </div>


            <!-- 파일첨부 -->
            <!-- <div class="mb-3" v-if="bulletinInfo.attachFileName">
              <label>파일첨부</label>
              <div>
                <a :href="bulletinInfo.attachFileUrl" target="_blank">{{ bulletinInfo.attachFileName }}</a>
              </div>
            </div> -->

            <!-- 댓글 입력 -->
        <!-- <div class="mb-3">
          <label>댓글 등록</label>
          <div class="input-group mb-3" style="background-color: #f8f9fa; padding: 10px; border-radius: 5px;">
            <input
              type="text"
              class="form-control"
              placeholder="내용을 입력해주세요"
              v-model="newComment.commentCn"
              style="border-radius: 5px; margin-right: 10px;">
            <input
              type="text"
              class="form-control"
              placeholder="작성자"
              v-model="newComment.wrterNm"
              style="border-radius: 5px; margin-right: 10px;">
            <button class="btn btn-success btn-fill" @click.prevent="addComment" style="border-radius: 5px; margin-right: 10px;">등록</button>
          </div>
        </div> -->

<!-- 댓글 목록 -->
<!-- <div class="mb-3">
  <label>댓글 목록</label>
  <div class="table-responsive">
    <table class="table table-bordered">
      <thead>
        <tr style="background-color: #f8f9fa;">
          <th style="width: 80px; text-align: center;">댓글번호</th>
          <th style="text-align: center;">댓글내용</th>
          <th style="width: 150px; text-align: center;">작성자</th>
          <th style="width: 180px; text-align: center;">작성시간</th>
          <th style="width: 160px; text-align: center;">관리</th>
        </tr>
      </thead>
      <tbody>
        <tr v-for="(commentItem, index) in comments" :key="index">
        
          <td style="text-align: center;">
            <input type="text" class="form-control text-center" v-model="commentItem.commentNo" readonly>
          </td>
          
          <td>
            <input type="text" class="form-control" v-model="commentItem.commentCn" :readonly="!commentItem.isEditing">
          </td>
        
          <td>
            <input type="text" class="form-control" v-model="commentItem.wrterNm" :readonly="!commentItem.isEditing">
          </td>
          
          <td style="text-align: center;">
            <input type="text" class="form-control text-center" v-model="commentItem.frstRegisterPnttm" readonly>
          </td>
     
          <td style="text-align: center;">
            <template v-if="!commentItem.isEditing">
              <button class="btn btn-success " @click="enterEdit(index)">수정</button>
              <button class="btn btn-danger " @click="deleteComment(index)">삭제</button>
            </template>
            <template v-else>
              <button class="btn btn-primary " @click="saveEdit(index)">저장</button>
              <button class="btn btn-secondary " @click="cancelEdit(index)">취소</button>
            </template>
          </td>
        </tr>
      </tbody>
    </table>
  </div>
</div>  -->


          </form>
        </div>
      </div>

    </div>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue';
import axios from '../../assets/js/customAxios';
import { useRoute, useRouter } from 'vue-router';



const route = useRoute();
const router = useRouter();
const nttId = route.params.nttId
const bbsId = route.params.bbsId

const bbsNm = ref(''); //  bbsNm을 ref로 선언

const formatDate = (date) => {
  if (!date) return '-'; // 날짜가 없으면 대체 텍스트 출력
  const d = new Date(date);
  return d.toISOString().split('T')[0]; // YYYY-MM-DD 형식
};



// 게시글 상세 데이터
const bulletinInfo = ref({ 
  wrterNm:'',
  nttSj: '',
  ntcrNm: '',
  frstRegistPnttm: '',
  nttCn: '',
  inqireCo: 0,
  ntceBgnde: '',
  ntceEndde: '',
  attachFileName: '',
  attachFileUrl: ''
});



// 새 댓글 등록 데이터
// const newComment = ref({ 
//   commentCn: '',
//   wrterNm: ''
// });

// 게시글 상세 조회 API 호출

const fetchBulletinInfo = async () => {
  try {
    const response = await axios.get(`/api/bulletin/bulletinInfo?nttId=${nttId}&bbsId=${bbsId}`);

    // 게시글 정보 저장
    if (response.data.result) {
      bulletinInfo.value = response.data.result;
    }

    //  boardMasterVO 객체에서 bbsNm 값 가져오기
    if (response.data.boardMasterVO && response.data.boardMasterVO.bbsNm) {
      bbsNm.value = response.data.boardMasterVO.bbsNm; // ref로 선언된 bbsNm에 값 저장
    } else {
      alert("게시판 이름을 찾을 수 없습니다.");
    }

  } catch (error) {
    alert("게시글 정보를 불러오는 데 실패했습니다. 다시 시도해주세요.");
  }
};





// 댓글 조회
// const fetchComments = async () => {
//   try {
//     const response = await axios.get(`/api/comment/commentList?bbsId=${bbsId}&nttId=${nttId}`);

//     // `resultList`가 존재하고 배열인지 확인
//     if (response.data && Array.isArray(response.data.resultList)) {
//       comments.value = response.data.resultList;  // 댓글 목록을 배열로 저장
//     } else {
//       alert("댓글 목록을 불러오는 데 문제가 발생했습니다.");
//       comments.value = [];  // 만약 배열이 아니면 빈 배열로 초기화
//     }
//   } catch (error) {
//     alert("댓글을 불러오는 데 실패했습니다. 다시 시도해주세요.");
//   }
// };



// 댓글 목록
// const comments = ref([]);


// 댓글 추가
// const addComment = async () => {
//   console.log(" 댓글 등록 요청 - bbsId:", bbsId, "nttId:", nttId);

//   if (!newComment.value.commentCn.trim() || !newComment.value.wrterNm.trim()) {
//     alert("댓글 내용과 작성자를 입력해주세요.");
//     return;
//   }
//   try {
//     const response = await axios.post(`/api/comment/commentAdd?bbsId=${bbsId}&nttId=${nttId}`, {
      
//       commentCn: newComment.value.commentCn,
//       wrterNm: newComment.value.wrterNm,
//     });

//     console.log(" 댓글 추가 응답:", response.data);

//     if (response.data.status === "success") {
//       // 댓글 목록에 최신 댓글을 맨 앞에 추가
//       comments.value.unshift({
//         commentCn: response.data.comment.commentCn,
//         wrterNm: response.data.comment.wrterNm,
//         isEditing: false,
//       });

//       // 댓글 목록 새로고침 (Vue의 반응성 문제 해결)
//       comments.value = [...comments.value];

//       // 입력 필드 초기화
//       newComment.value.commentCn = "";
//       newComment.value.wrterNm = "";
//     } else {
//       alert("댓글 등록 실패");
//     }
//   } catch (error) {
//     console.error(" 댓글 등록 오류:", error.response || error);
//     alert("댓글 등록에 실패했습니다.");
//   }
// };





// 댓글 수정
// const enterEdit = (index) => {
//   comments.value[index].isEditing = true;
// };

// const saveEdit = async (index) => {
//   const { commentCn, wrterNm } = comments.value[index];
//   try {
//     await axios.put('/api/comment/commentUpdate', { commentCn, wrterNm, nttId });
//     comments.value[index].isEditing = false;
//     alert('댓글이 수정되었습니다.');
//   } catch (error) {
//     console.error('댓글 수정 오류:', error.response || error);
//     alert(`댓글 수정 실패: ${error.response?.statusText || '서버 오류'}`);
//   }
// };

// const cancelEdit = (index) => {
//   comments.value[index].isEditing = false;
// };

// 댓글 삭제
// const deleteComment = async (index) => {
//   if (!confirm('정말 삭제하시겠습니까?')) return;

//   try {
//     await axios.delete('/api/comment/commentDelete', {
//       data: { nttId, commentId: comments.value[index].commentId },
//     });
//     comments.value.splice(index, 1);
//     alert('댓글이 삭제되었습니다.');
//   } catch (error) {
//     console.error('댓글 삭제 오류:', error.response || error);
//     alert(`댓글 삭제 실패: ${error.response?.statusText || '서버 오류'}`);
//   }
// };

//  게시글 삭제
// const deleteBulletin = async () => {
//   if (!confirm('게시글을 삭제하시겠습니까?')) return;

//   try {
//     await axios.delete(`/api/bulletin/bulletinRemove/${nttId}`);

//     alert('게시글이 삭제되었습니다.');

//     //  삭제 후, 해당 게시판 목록으로 이동
//     router.push({ path: `/bulletin/bulletinList/${bbsId}` });
//   } catch (error) {
//     console.error('게시글 삭제 오류:', error.response || error);
//     alert(`게시글 삭제 실패: ${error.response?.data?.message || '서버 오류'}`);
//   }
// };


//게시글 수정으로 이동
const goToBulletinModify = () => {
  router.push({
    name: "BulletinModify",
    params: { bbsId: route.params.bbsId, nttId: route.params.nttId },
  });
};


//게시글 목록으로 이동
const goToBulletinList = () => {
  router.push({ path: `/bulletin/bulletinList/${bbsId}` });
};

// 페이지 로드 시 데이터 가져오기
onMounted(() => {
  fetchBulletinInfo();
  //fetchComments();
});
</script>

<style scoped>
.btn-fill {
  padding: 10px 20px;
  font-size: 14px;
}

.form-control {
  font-size: 14px;
}

.input-group .form-control {
  font-size: 14px;
}

.input-group {
  display: flex;
  align-items: center;
  justify-content: space-between;
}

.info-box {
  font-weight: bold;
  text-align: center;
  width: 150px;
  padding: 5px;
  background-color: #fff;
  border: 1px solid #ddd;
  border-radius: 5px;
  display: inline-block;
}
.content-box {
  width: 100%;
  min-height: 200px;
  padding: 10px;
  background-color: #fff;
  border: 1px solid #ddd;
  border-radius: 5px;
  white-space: pre-wrap; /* 개행 유지 */
  word-wrap: break-word; /* 긴 단어 줄바꿈 */
}
</style>
