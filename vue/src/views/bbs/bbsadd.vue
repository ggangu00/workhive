<template>
    <div class="content">
      <div class="container-fluid">
        <!-- 페이지 헤더 -->
        <div class="header-container">
          <h3 class="title">게시판 등록</h3>
          <hr>
          <!-- 버튼 영역 -->
          <div class="button-container">
            <button type="submit" class="btn btn-primary btn-fill">등록</button>
            <button @click="goToList" class="btn btn-secondary btn-fill">목록</button>
          </div>
        </div>
        
        <!-- 입력 폼 -->
        <div class="form-container">
          <label for="boardName">게시판명</label>
          <input type="text" id="boardName" v-model="boardName" class="form-control" placeholder="게시판명을 입력하세요" />
          
          <label for="boardType">게시판 유형</label>
          <select id="boardType" v-model="boardType" class="form-control">
            <option value="">-- 선택하세요 --</option>
            <option value="공지사항">공지사항</option>
            <option value="자유게시판">자유게시판</option>
            <option value="질문게시판">질문게시판</option>
          </select>
          
          <label for="fileAttach">파일첨부 가능 여부</label>
          <select id="fileAttach" v-model="fileAttach" class="form-control">
            <option value="">-- 선택하세요 --</option>
            <option value="예">예</option>
            <option value="아니오">아니오</option>
          </select>
          
          <label for="commentUsage">댓글 사용 여부</label>
          <select id="commentUsage" v-model="commentUsage" class="form-control">
            <option value="">-- 선택하세요 --</option>
            <option value="예">예</option>
            <option value="아니오">아니오</option>
          </select>
        </div>
      </div>
    </div>
  </template>
  
  <script>
  export default {
    name: '',
    data() {
      return {
        boardName: '',
        boardType: '',
        fileAttach: '',
        commentUsage: ''
      };
    },
    methods: {
      goToList() {
        this.$router.push({ name: 'bbsList' });
      },
    },
  };
  </script>
  
  <style scoped>
  /* 전체 컨테이너 */
  .container-fluid {
    background-color: white;
    padding: 20px;
    border-bottom: 2px solid blue;
  }
  
  /* 제목과 버튼을 한 줄로 정렬 */
  .header-container {
    display: flex;
    align-items: center;
    justify-content: space-between;
  }
  
  /* 버튼 스타일 */
  .button-container {
    display: flex;
    gap: 10px;
  }
  
  .btn {
    padding: 5px 15px;
    font-size: 14px;
    border-radius: 5px;
  }
  
  .btn-primary {
    background-color: #4CAF50;
    color: white;
    border: none;
  }
  
  .btn-secondary {
    background-color: #B0B0B0;
    color: white;
    border: none;
  }
  
  /* 폼 스타일 */
  .form-container {
    margin-top: 20px;
    display: flex;
    flex-direction: column;
    gap: 10px;
  }
  
  label {
    font-weight: bold;
  }
  
  .form-control {
    width: 100%;
    padding: 8px;
    border: 1px solid #ccc;
    border-radius: 5px;
  }

  </style>
  