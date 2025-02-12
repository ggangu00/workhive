<template>
    <div class="content">
      <div class="container-fluid">
        <!-- 페이지 헤더 -->
        <div class="row">
          <div class="col">
            <h3>게시판 목록</h3>
          </div>
        </div>
       
        <div class="white-background">
          <div class="controls">
            <select class="custom-select">
              <option value="">선택</option>
              <option value="공지사항">공지사항</option>
              <option value="사내게시판">사내게시판</option>
            </select>
            <input type="text" class="custom-input">
            <button class="btn btn-info btn-fill mr-1">조회</button>
            <button type="submit" class="btn btn-primary btn-fill">등록</button>
          </div>         
        </div>      
      </div>
    </div>
  </template>
  
  <script>
  export default {
    name: '',
    // 기타 옵션
  };
  </script>
  
  <style>
  button {
    padding: 5px 10px;
    margin-left: 5px;
    cursor: pointer;
  }
  .controls {
    display: flex;
    justify-content: flex-end;
    margin-bottom: 10px;
  }
  .custom-input,
  .custom-select {
    width: 200px; /* 너비 */
    height: 30px; /* 높이 */
    padding: 5px; /* 내부 여백  */
    margin-right: 10px; /* 오른쪽 여백  */
    font-size: 14px; /* 글꼴 크기  */
    border: 1px solid #ccc; /* 테두리  */
    border-radius: 5px; /* 모서리 둥글기  */
    background-color: #fff; /* 배경색  */
    color: #333; /* 글자색  */
    appearance: none; /* 브라우저 기본 스타일 제거 */
    -webkit-appearance: none;
    -moz-appearance: none;
  }
  .white-background {
    background-color: white;
    padding: 20px;
    border-radius: 10px;
    box-shadow: 0 4px 6px rgba(0, 0, 0, 0.1);
    margin-top: 10px;
  }
  </style>
  