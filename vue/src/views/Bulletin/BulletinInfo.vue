<template>
  <div class="content">
    <div class="container-fluid">
      <!-- 상단 버튼 영역 -->
      <div class="card">
        <div class="card-body">
          <h4 class="card-title float-left mt-1">게시글 상세조회</h4>
          <button class="btn btn-danger btn-fill float-right" @click="deleteBulletin">삭제</button>
          <button class="btn btn-success btn-fill float-right" @click="editBulletin">수정</button>
        </div>
      </div>

      <!-- 상세조회 내용 영역 -->
      <div class="card" v-if="bulletinInfo">
        <div class="card-body">
          <form>
            <!-- 제목 및 작성정보 영역 -->
            <div class="mb-3" style="text-align: center;">
              <label style="font-size: 30px;">{{ bulletinInfo.nttSj || '게시글 제목' }}</label>
            </div>

            <div style="background-color: #f4f4f4; padding: 10px; text-align: center; font-size: 14px; color: #555; margin-bottom: 20px;">
              <span style="font-weight: bold;">작성자:</span>
              <input type="text" v-model="bulletinInfo.ntcrNm" style="font-weight: bold; text-align: center; width: 150px; margin-left: 5px;"  v-bind:readonly="isUpdate">

              <span style="margin: 0 15px;">|</span>

              <span style="font-weight: bold;">등록일:</span>
              <input type="date" v-model="bulletinInfo.frstRegistPnttm" style="text-align: center; width: 150px; margin-left: 5px;">

              <span style="margin: 0 15px;">|</span>

              <span style="font-weight: bold;">조회수:</span>
              <input type="number" v-model="bulletinInfo.inqireCo" style="text-align: center; width: 80px; margin-left: 5px;">
            </div>

            <!-- 게시글 내용 -->
            <div class="mb-3">
              <textarea
                class="form-control w100"
                v-model="bulletinInfo.nttCn"                
                rows="10"
                style="margin-top: 20px;">
              </textarea>
            </div>

            <!-- 게시기간 -->
            <div class="mb-3">
              <label class="form-label">게시기간 <em class="point-red">*</em></label>
              <div class="row">
                <div class="col-auto">
                  <input type="date" v-model="bulletinInfo.ntceBgnde" class="form-control">
                </div>
                <div class="col-auto">~</div>
                <div class="col-auto">
                  <input type="date" v-model="bulletinInfo.ntceEndde" class="form-control">
                </div>
              </div>
            </div>

            <!-- 파일첨부 -->
            <div class="mb-3" v-if="bulletinInfo.attachFileName">
              <label>파일첨부</label>
              <div>
                <a :href="bulletinInfo.attachFileUrl" target="_blank">{{ bulletinInfo.attachFileName }}</a>
              </div>
            </div>

            <!-- 댓글 입력 -->
            <div class="mb-3">
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
            </div>

            <!-- 댓글 목록 -->
            <div class="mb-3" v-for="(commentItem, index) in comments" :key="index">
              <div class="input-group mb-3" style="background-color: #f8f9fa; padding: 10px; border-radius: 5px;">
                <input
                  type="text"
                  class="form-control"
                  v-model="commentItem.commentCn"
                  placeholder="내용을 입력해주세요"
                  :readonly="!commentItem.isEditing"
                  style="border-radius: 5px; margin-right: 10px;">
                <input
                  type="text"
                  class="form-control"
                  v-model="commentItem.wrterNm"
                  placeholder="작성자"
                  :readonly="!commentItem.isEditing"
                  style="border-radius: 5px; margin-right: 10px;">
                
                <template v-if="!commentItem.isEditing">
                  <button class="btn btn-success btn-fill" @click="enterEdit(index)" style="border-radius: 5px; margin-right: 10px;">수정</button>
                  <button class="btn btn-danger btn-fill" @click="deleteComment(index)" style="border-radius: 5px;">삭제</button>
                </template>
                <template v-else>
                  <button class="btn btn-success btn-fill" @click="saveEdit(index)" style="border-radius: 5px; margin-right: 10px;">저장</button>
                  <button class="btn btn-secondary btn-fill" @click="cancelEdit(index)" style="border-radius: 5px;">취소</button>
                </template>
              </div>
            </div>

          </form>
        </div>
      </div>

    </div>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue';
import axios from 'axios';
import { useRoute, useRouter } from 'vue-router';


const isUpdate = ref(true);
const route = useRoute();
const router = useRouter();
const nttId = route.params.nttId
const bbsId = route.params.bbsId
console.log("아이디:",nttId);




// 게시글 상세 데이터
const bulletinInfo = ref({ 
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

// 댓글 목록
const comments = ref([]);

// 새 댓글 등록 데이터
const newComment = ref({
  commentCn: '',
  wrterNm: ''
});

// 게시글 상세 조회 API 호출

const fetchBulletinInfo = async () => {
  const route = useRoute(); // 현재 라우트 정보 가져오기
  const bulletinId = route.params.bulletinId; // URL에서 bulletinId 추출

  try {
    const response = await axios.get(`/api/bulletin/bulletinInfo?bulletinId=${bulletinId}&bbsId=${bbsId}`);
    bulletinInfo.value = response.data;
  } catch (error) {
    console.error('게시글 상세 조회 오류:', error.response || error);
   
  }
};


// 댓글 목록 조회
const fetchComments = async () => {
  try {
    const response = await axios.get('/api/comment/commentList', {
      params: { nttId },
    });
    comments.value = response.data.map(comment => ({ ...comment, isEditing: false }));
  } catch (error) {
    console.error('댓글 조회 오류:', error.response || error);
   
  }
};

// 댓글 등록
const addComment = async () => {
  if (!newComment.value.commentCn.trim() || !newComment.value.wrterNm.trim()) {
    alert('댓글 내용과 작성자를 입력해주세요.');
    return;
  }

  try {
    const response = await axios.post('/api/comment/commentAdd', {
      commentCn: newComment.value.commentCn,
      wrterNm: newComment.value.wrterNm,
      nttId,
    });

    comments.value.push({
      commentCn: response.data.commentCn,
      wrterNm: response.data.wrterNm,
      isEditing: false,
    });

    newComment.value.commentCn = '';
    newComment.value.wrterNm = '';
  } catch (error) {
    console.error('댓글 등록 오류:', error.response || error);
    alert(`댓글 등록 실패: ${error.response?.statusText || '서버 오류'}`);
  }
};

// 댓글 수정
const enterEdit = (index) => {
  comments.value[index].isEditing = true;
};

const saveEdit = async (index) => {
  const { commentCn, wrterNm } = comments.value[index];
  try {
    await axios.put('/api/comment/commentUpdate', { commentCn, wrterNm, nttId });
    comments.value[index].isEditing = false;
    alert('댓글이 수정되었습니다.');
  } catch (error) {
    console.error('댓글 수정 오류:', error.response || error);
    alert(`댓글 수정 실패: ${error.response?.statusText || '서버 오류'}`);
  }
};

const cancelEdit = (index) => {
  comments.value[index].isEditing = false;
};

// 댓글 삭제
const deleteComment = async (index) => {
  if (!confirm('정말 삭제하시겠습니까?')) return;

  try {
    await axios.delete('/api/comment/commentDelete', {
      data: { nttId, commentId: comments.value[index].commentId },
    });
    comments.value.splice(index, 1);
    alert('댓글이 삭제되었습니다.');
  } catch (error) {
    console.error('댓글 삭제 오류:', error.response || error);
    alert(`댓글 삭제 실패: ${error.response?.statusText || '서버 오류'}`);
  }
};

// 게시글 삭제
const deleteBulletin = async () => {
  if (!confirm('게시글을 삭제하시겠습니까?')) return;

  try {
    await axios.delete('/api/bulletin/bulletinDelete', { data: { nttId } });
    alert('게시글이 삭제되었습니다.');
    router.push('/bulletin-list'); // 삭제 후 목록으로 이동
  } catch (error) {
    console.error('게시글 삭제 오류:', error.response || error);
    alert(`게시글 삭제 실패: ${error.response?.statusText || '서버 오류'}`);
  }
};

// 게시글 수정 (예시: 수정 페이지로 이동)
const editBulletin = () => {
  router.push({ name: 'BulletinEdit', params: { nttId } });
};

// 페이지 로드 시 데이터 가져오기
onMounted(() => {
 
  fetchBulletinInfo();
  fetchComments();
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
</style>
