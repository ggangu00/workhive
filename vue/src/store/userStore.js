import { defineStore } from 'pinia';
import { ref, computed, watch } from 'vue';

export const useUserInfoStore = defineStore('userInfo', () => {
   // 로그인된 사용자 정보 저장
   const user = ref(JSON.parse(localStorage.getItem("user")) || null);

   // 아이디 저장 기능을 위한 변수
   const savedUserId = ref(localStorage.getItem("savedUserId") ?? "");
   const isRemembered = ref(localStorage.getItem("isRemembered") === "true");

   // 로그인 여부 확인 (토큰과 사용자 정보가 둘 다 있어야 진짜 로그인 상태로 간주)
   const isAuthenticated = computed(() => !!user.value);

   // 로그인 성공 시 사용자 정보 & 토큰 저장
   function setUser(userData) {
      user.value = userData;

      localStorage.setItem("user", JSON.stringify(userData));
   }

   // 아이디 저장 여부에 따라 저장/삭제
   function saveUserId(userId, rememberMe) {
      savedUserId.value = userId;
      if (rememberMe) {
         localStorage.setItem("savedUserId", userId);
         localStorage.setItem("isRemembered", "true");
      } else {
         localStorage.removeItem("savedUserId");
         localStorage.setItem("isRemembered", "false");
      }
   }

   // 새로고침 시 `localStorage`의 데이터를 유지하는 함수
   function loadStoredData() {
      savedUserId.value = localStorage.getItem("savedUserId") || "";
      isRemembered.value = localStorage.getItem("isRemembered") === "true";

   }

   // 로그아웃 처리 (토큰과 사용자 정보 전부 날리기)
   function logout() {
      user.value = null;

      localStorage.removeItem("user");
      localStorage.removeItem("token");

      if (!isRemembered.value) {
         savedUserId.value = "";
         localStorage.removeItem("savedUserId");
      }
   }

   // 체크박스 변경 시 자동 저장
   watch(isRemembered, (newValue) => {
      localStorage.setItem("isRemembered", newValue.toString());
      if (!newValue) {
         savedUserId.value = "";
         localStorage.removeItem("savedUserId");
      }
   });

   return {
      user,
      savedUserId,
      isAuthenticated,
      isRemembered,
      saveUserId,
      setUser,
      logout,
      loadStoredData
   };
});
