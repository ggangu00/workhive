<template>
  <div class="container"> <div class="card">
        <div class="card-body">
          <h4 class="card-title float-left mt-1">게시글 비밀번호 확인</h4>
        
        </div>
      </div>
    <div class="password-box">
      <h3>비밀글보기</h3>
      <p>
        이 글은 비밀글입니다. <span class="highlight">비밀번호를 입력하여 주세요.</span><br />
        관리자는 확인 버튼만 누르시면 됩니다.
      </p>

      <!-- 비밀번호 입력 필드 -->
      <div class="input-group">
        <label>🔹 비밀번호</label>
        <input type="password" v-model="password" maxlength="4" placeholder="비밀번호 입력" />
      </div>

      <!-- 버튼 -->
      <div class="button-group">
        <button class="btn secondary" @click="goToList">목록</button>
        <button class="btn primary" @click="checkPassword">확인</button>
      </div>

      <!-- 응답 메시지 -->
      <p v-if="message" :class="isError ? 'error' : 'success'">{{ message }}</p>
    </div>
  </div>
</template>

<script setup>
import { ref } from 'vue';
import axios from '../../assets/js/customAxios';
import { useRouter } from 'vue-router';

const password = ref('');
const message = ref('');
const isError = ref(false);
const router = useRouter();

// 비밀번호 확인 함수
const checkPassword = async () => {
  if (password.value.trim().length !== 4) {
    message.value = "비밀번호는 4자리 숫자로 입력해주세요.";
    isError.value = true;
    return;
  }

  try {
    const response = await axios.post('/api/checkPassword', { password: password.value });

    if (response.data.success) {
      message.value = "비밀번호가 일치합니다! 비밀글을 확인할 수 있습니다.";
      isError.value = false;
    } else {
      message.value = "비밀번호가 올바르지 않습니다.";
      isError.value = true;
    }
  } catch (error) {
    message.value = "서버 오류가 발생했습니다. 다시 시도해주세요.";
    isError.value = true;
  }
};

// 목록으로 이동
const goToList = () => {
  router.push('/list'); // 목록 페이지로 이동
};
</script>

 
<style scoped>
/* 전체 컨테이너 */
.container {
  width: 95%; /* 화면의 95%로 더 넓게 설정 */
  max-width: 1000px; /* 최대 너비를 설정 */
  margin: 50px auto;
  padding: 40px;
  font-family: 'Arial', sans-serif;
  color: #333;
  background-color: #f7f7f7;
  border-radius: 8px;
}

/* 비밀번호 입력 박스 */
.password-box {
  background: #fff;
  border-radius: 8px;
  border: 1px solid #dcdcdc;
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.1);
  padding: 50px;
  width: 80%;  /* 흰색 배경을 더 넓게 설정 */
  max-width: 900px; /* 최대 너비 설정 */
  margin: 0 auto; /* 가운데 정렬 */
  text-align: center;
}

h2 {
  font-size: 26px;
  font-weight: bold;
  margin-bottom: 25px;
  color: #444;
}

h3 {
  font-size: 22px;
  font-weight: 600;
  margin-bottom: 20px;
  color: #555;
}

p {
  font-size: 16px;
  color: #777;
  margin-bottom: 30px;
}

.highlight {
  color: #007bff;
  font-weight: bold;
}

/* 비밀번호 입력 영역 */
.input-group {
  display: flex;
  justify-content: center;
  align-items: center;
  margin-bottom: 30px;
}

.input-group label {
  font-weight: bold;
  margin-right: 12px;
  font-size: 18px;
}

input {
  width: 240px;
  padding: 12px;
  border: 1px solid #ccc;
  border-radius: 5px;
  text-align: center;
  font-size: 18px;
  outline: none;
}

input:focus {
  border-color: #007bff;
}

/* 버튼 그룹 */
.button-group {
  display: flex;
  justify-content: center;
  gap: 20px;
  margin-top: 30px;
}

.btn {
  padding: 15px 25px;
  border: none;
  border-radius: 5px;
  cursor: pointer;
  font-size: 16px;
  font-weight: bold;
  width: 160px;
  transition: background-color 0.3s, transform 0.2s ease;
}

.btn.primary {
  background: #007bff;
  color: #fff;
}

.btn.primary:hover {
  background: #0056b3;
  transform: scale(1.05);
}

.btn.secondary {
  background: #f0f0f0;
  color: #555;
}

.btn.secondary:hover {
  background: #dcdcdc;
  transform: scale(1.05);
}

.error {
  color: red;
  font-size: 16px;
  margin-top: 20px;
  font-weight: bold;
}

.success {
  color: green;
  font-size: 16px;
  margin-top: 20px;
  font-weight: bold;
}
</style>
