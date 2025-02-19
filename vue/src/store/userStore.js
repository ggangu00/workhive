// stores/counter.js
import { defineStore } from 'pinia';
import { ref } from 'vue';


export const useUserStore = defineStore('user', () => {
   const user = ref(null); // 사용자 정보
   const token = ref(null); // JWT 토큰(API 요청 시 사용)

   // 로그인 처리
   const login = (userData, authToken) => {
      // 로그인 성공시 사용자 정보, 토큰 저장
      user.value = userData; // 사용자 정보
      token.value = authToken; // 토큰
   };

   return { user, token, login };
}, {
   persist : true, // 새로고침시에도 데이터 유지 !!
})