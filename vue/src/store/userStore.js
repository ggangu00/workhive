// stores/userInfo.js
import { defineStore } from 'pinia';
import { ref, computed } from 'vue';

export const useUserInfoStore = defineStore('userInfo', () => { // 함수명 수정
   const user = ref(JSON.parse(localStorage.getItem("user"))); // 새로고침 시 유지

   const isAuthenticated = computed(() => !!user.value); // 로그인 여부 체크

   function setUser(userData) {
      user.value = userData;
      localStorage.setItem("user", JSON.stringify(userData)); // 로컬 스토리지에 저장
   }

   function logout() {
      user.value = null;
      localStorage.removeItem("user");
   }

   return { user, isAuthenticated, setUser, logout };
});
