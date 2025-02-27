<template>
    <div class="container">
      <div class="modal">
        <h2>비밀글보기</h2>
        <p>이 글은 비밀글입니다. 비밀번호를 입력하여 주세요.</p>
        <p>관리자는 확인버튼만 누르시면 됩니다.</p>
  
        <div class="form-group">
          <label for="password">비밀번호</label>
          <input type="password" v-model="password" id="password" />
        </div>
  
        <div class="buttons">
          <button @click="checkPassword">확인</button>
          <button @click="goBack">목록</button>
        </div>
      </div>
    </div>
  </template>
  
  <script>
 
  </script>
  
  <style scoped>
  .container {
    display: flex;
    justify-content: center;
    align-items: center;
    height: 100vh;
    background-color: #f2f2f2;
  }
  
  .modal {
    background-color: white;
    padding: 20px;
    border-radius: 10px;
    box-shadow: 0 4px 8px rgba(0, 0, 0, 0.1);
    width: 300px;
    text-align: center;
  }
  
  h2 {
    font-size: 24px;
    margin-bottom: 10px;
  }
  
  p {
    font-size: 14px;
    margin-bottom: 20px;
  }
  
  .form-group {
    margin-bottom: 20px;
  }
  
  input {
    width: 80%;
    padding: 8px;
    margin: 10px 0;
    border: 1px solid #ccc;
    border-radius: 5px;
  }
  
  .buttons {
    display: flex;
    justify-content: space-between;
  }
  
  button {
    padding: 10px 20px;
    background-color: #007bff;
    color: white;
    border: none;
    border-radius: 5px;
    cursor: pointer;
  }
  
  button:hover {
    background-color: #0056b3;
  }
  </style>
  