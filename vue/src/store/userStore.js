import { defineStore } from 'pinia';
import { ref, computed, watch } from 'vue';

export const useUserInfoStore = defineStore('userInfo', () => {
   // ✅ 로그인된 사용자 정보 저장
   const user = ref(JSON.parse(localStorage.getItem("user")) || null);

   // ✅ 아이디 저장 기능을 위한 변수 (체크박스 상태 확인)
   const savedUserId = ref(localStorage.getItem("savedUserId") ?? ""); // 저장된 아이디 불러오기
   const isRemembered = ref(localStorage.getItem("isRemembered") === "true"); // 체크박스 상태 유지

   // ✅ 로그인 여부 확인 (사용자 정보가 존재하면 `true`, 아니면 `false`)
   const isAuthenticated = computed(() => !!user.value);

   // ✅ 로그인 성공 시 사용자 정보를 저장하는 함수
   function setUser(userData) {
      user.value = userData;
      localStorage.setItem("user", JSON.stringify(userData)); // 사용자 정보를 `localStorage`에 저장

      // 로그인 상태 업데이트
      isAuthenticated.value = !!userData;
   }

   // ✅ 아이디 저장 여부에 따라 로컬 스토리지에 저장/삭제
   function saveUserId(userId, rememberMe) {
      savedUserId.value = userId; // 로그인 시도한 아이디는 무조건 저장

      if (rememberMe) {
         localStorage.setItem("savedUserId", userId); // 체크박스가 활성화된 경우 아이디 저장
         localStorage.setItem("isRemembered", "true");
      } else {
         localStorage.removeItem("savedUserId"); // 체크 해제 시 저장된 아이디 삭제
         localStorage.setItem("isRemembered", "false");
      }
   }

   // ✅ 새로고침 시 `localStorage`의 데이터를 유지하는 함수
   function loadStoredData() {
      savedUserId.value = localStorage.getItem("savedUserId") || ""; // 저장된 아이디 로드
      isRemembered.value = localStorage.getItem("isRemembered") === "true"; // 체크박스 상태 유지
   }

   // ✅ 로그아웃 함수 (사용자 정보는 삭제하되, 아이디 저장 여부에 따라 유지)
   function logout() {
      user.value = null;
      localStorage.removeItem("user"); // 사용자 정보 삭제

      if (!isRemembered.value) {
         savedUserId.value = ""; // 체크 해제 시 아이디 삭제
         localStorage.removeItem("savedUserId");
      }
   }

   // ✅ 체크박스(`isRemembered`) 변경 시 `localStorage` 자동 업데이트
   watch(isRemembered, (newValue) => {
      localStorage.setItem("isRemembered", newValue.toString());

      if (!newValue) {
         savedUserId.value = ""; // 체크 해제 시 아이디 삭제
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
