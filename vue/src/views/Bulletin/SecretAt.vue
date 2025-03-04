<template>
  <div class="container">
    <h2>게시글 비밀번호 확인</h2>

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
  width: 90%;
  max-width: 1200px; /* 게시판 크기에 맞게 조정 */
  margin: 50px auto;
  padding: 20px;
}

/* 비밀번호 입력 박스 */
.password-box {
  background: #eef2f7; /* 연한 회색 배경 */
  border-radius: 8px;
  border: 1px solid #dcdcdc;
  box-shadow: 0 4px 10px rgba(0, 0, 0, 0.1);
  padding: 30px;
  text-align: center;
}

h2 {
  font-size: 22px;
  font-weight: bold;
  margin-bottom: 20px;
}

h3 {
  font-size: 20px;
  font-weight: bold;
  margin-bottom: 15px;
}

p {
  font-size: 14px;
  color: #666;
  margin-bottom: 15px;
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
  margin-bottom: 15px;
}

.input-group label {
  font-weight: bold;
  margin-right: 10px;
}

input {
  width: 180px;
  padding: 8px;
  border: 1px solid #ccc;
  border-radius: 5px;
  text-align: center;
  font-size: 16px;
}

/* 버튼 그룹 */
.button-group {
  display: flex;
  justify-content: center;
  gap: 15px;
}

.btn {
  padding: 10px 20px;
  border: none;
  border-radius: 5px;
  cursor: pointer;
  font-size: 14px;
  font-weight: bold;
  width: 120px;
}

.btn.primary {
  background: #555;
  color: #fff;
}

.btn.secondary {
  background: #ccc;
  color: #333;
}

.error {
  color: red;
  font-size: 14px;
  margin-top: 10px;
}

.success {
  color: green;
  font-size: 14px;
  margin-top: 10px;
}
</style>
