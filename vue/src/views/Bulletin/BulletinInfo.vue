<template>
  <div class="content">
    <div class="container-fluid">
      <!-- 상단 버튼 영역 -->
      <div class="card">
        <div class="card-body">
          <h4 class="card-title float-left mt-1">게시글 상세조회</h4>
          <button class="btn btn-danger btn-fill float-right">삭제</button>
          <button class="btn btn-success btn-fill float-right">수정</button>
        </div>
      </div>

      <!-- 상세조회 내용 영역 -->
      <div class="card">
        <div class="card-body">
          <form action="BoardAdd" method="post">
            <!-- 제목 및 작성정보 영역 -->
            <div class="mb-3" style="text-align: center;">
              <label style="font-size: 30px;">{{ bulletinInfo.nttSj || '게시글 제목' }}</label>
            </div>

            <div style="background-color: #f4f4f4; padding: 10px; text-align: center; font-size: 14px; color: #555; margin-bottom: 20px;">
              <span style="font-weight: bold;">작성자: {{ bulletinInfo.ntcrNm || '홍길동' }}</span>
              <span style="margin: 0 15px;">|</span>
              <span style="font-weight: bold;">등록일: {{ bulletinInfo.frstRegistPnttm || '2025.02.05' }}</span>
              <span style="margin: 0 15px;">|</span>
              <span style="font-weight: bold;">조회수: {{ bulletinInfo.inqireCo || 0 }}</span>
            </div>

            <!-- 게시글 내용 -->
            <div class="mb-3">
              <textarea
                class="form-control w100"
                v-model="bulletinInfo.nttCn"
                placeholder="내용을 입력해주세요"
                rows="10"
                style="margin-top: 20px;">
              </textarea>
            </div>

            <!-- 게시기간 -->
            <div class="mb-3">
              <label class="form-label">게시기간 <em class="point-red">*</em></label>
              <div class="row">
                <div class="col-auto">
                  <input type="date" name="start_dt" class="form-control">
                </div>
                <div class="col-auto">~</div>
                <div class="col-auto">
                  <input type="date" name="end_dt" class="form-control">
                </div>
              </div>
            </div>

            <!-- 파일첨부 -->
            <div class="mb-3">
              <label>파일첨부 <em class="point-red">*</em></label>
              <div class="input-group w30" style="border: 1px solid #ccc; border-radius: 3px; background-color: #fff;">
                <input type="file" class="form-control" ref="fileInput" />
              </div>
            </div>

            <!-- 댓글 입력 -->
            <div class="mb-3">
              <label>댓글 등록</label>
              <div class="input-group mb-3" style="background-color: #f8f9fa; padding: 10px; border-radius: 5px;">
                <!-- v-model 이름을 script와 일치시킵니다. -->
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
                <button class="btn btn-success btn-fill" 
                        @click.prevent="addComment" 
                        style="border-radius: 5px; margin-right: 10px;">
                  등록
                </button>
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
                  style="border-radius: 5px; margin-right: 10px;">
                <input
                  type="text"
                  class="form-control"
                  v-model="commentItem.wrterNm"
                  placeholder="작성자"
                  style="border-radius: 5px; margin-right: 10px;">
                  
                <template v-if="!commentItem.isEditing">
                  <button class="btn btn-success btn-fill"
                          @click="enterEdit(index)"
                          style="border-radius: 5px; margin-right: 10px;">
                    수정
                  </button>
                  <button class="btn btn-danger btn-fill"
                          @click="deleteComment(index)"
                          style="border-radius: 5px;">
                    삭제
                  </button>
                </template>
                <template v-else>
                  <button class="btn btn-success btn-fill" 
                          @click="saveEdit(index)"  
                          style="border-radius: 5px; margin-right: 10px;">
                    저장
                  </button>
                  <button class="btn btn-secondary btn-fill" 
                          @click="cancelEdit(index)" 
                          style="border-radius: 5px;">
                    취소
                  </button>
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
import { useRoute } from 'vue-router';

// 라우터에서 게시글 ID를 가져옵니다.
const route = useRoute();
const bulletinId = route.params.bulletinId || 'defaultId';

// 게시글 상세 데이터와 조회수를 저장할 변수
const bulletinInfo = ref({
  nttSj: '',
  ntcrNm: '',
  frstRegistPnttm: '',
  nttCn: '',
  inqireCo: 0
});


onMounted(() => {
  
});

// 새 댓글 등록을 위한 데이터 (서버와 통신 시 필드명에 주의)
const newComment = ref({
  commentCn: '',
  wrterNm: ''
});

// 댓글 목록 (실제 서비스라면 API 호출로 가져오겠지만 예제에서는 하드코딩)
const comments = ref([
  {
    commentCn: '댓글 내용 예시',
    wrterNm: '작성자 예시',
    isEditing: false
  }
]);

// 댓글 등록 함수 (axios POST 요청)
// 댓글 등록 버튼 클릭 시 addComment()가 호출되어 서버에 댓글 데이터를 전송합니다.
const addComment = async () => {
  const { commentCn, wrterNm } = newComment.value;

  if (!commentCn.trim() || !wrterNm.trim()) {
    alert('댓글 내용과 작성자를 입력해주세요.');
    return;
  }

  try {
    const response = await axios.post('/api/comment/commentAdd', {
      commentCn,
      wrterNm,
      nttId: bulletinId,
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


// 댓글 수정 모드 전환
const enterEdit = (index) => {
  comments.value[index].isEditing = true;
};

// 수정 취소 시 수정 모드 해제
const cancelEdit = (index) => {
  comments.value[index].isEditing = false;
};

// 댓글 수정 저장 (추후 서버 전송 기능 추가 가능)
const saveEdit = (index) => {
  console.log('수정된 댓글:', comments.value[index]);
  comments.value[index].isEditing = false;
};

// 댓글 삭제 함수
const deleteComment = (index) => {
  comments.value.splice(index, 1);
};
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
