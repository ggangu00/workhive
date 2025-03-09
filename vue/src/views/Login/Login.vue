<template>
   <div class="login-container-fluid mx-auto">

      <div class="d-flex justify-content-center align-items-center mb30">
         <img src="../../assets/img/vue-logo.png" alt="WorkHive Logo" class="me-3" style="width: 100px; height: auto;">
         <h1>WorkHive</h1>
      </div>

      <div class="mx-auto" @keydown.enter="keyEventHandler">
         <div class="login_box">
            <div class="input_box mb10">
               <div class="input_item">
                  <!-- 아이디 입력 필드 -->
                  <input type="text" class="input_id" v-model="userId">
                  <label class="text_label">아이디 또는 전화번호</label>

                  <!-- 아이디 입력 초기화 버튼 (한 글자 이상 입력 시 표시) -->
                  <button v-if="userId.length > 0" type="button" class="btn_delete" @click="userIdReset">
                     <i class="fa-solid fa-xmark"></i>
                  </button>
               </div>

               <div class="input_item">
                  <!-- 비밀번호 입력 필드 -->
                  <input :type="isPasswordVisible ? 'text' : 'password'" class="input_pw" v-model="password">
                  <label class="text_label">비밀번호</label>

                  <!-- 비밀번호 보기/숨기기 버튼 -->
                  <button v-if="password.length > 0" type="button" class="btn_view me-3" @click="togglePasswordVisibility">
                     <i :class="['font-14', isPasswordVisible ? 'fa-solid fa-lock-open' : 'fa-solid fa-lock']"></i>
                  </button>

                  <!-- 비밀번호 입력 초기화 버튼 (한 글자 이상 입력 시 표시) -->
                  <button v-if="password.length > 0" type="button" class="btn_delete" @click="passwordReset">
                     <i class="fa-solid fa-xmark"></i>
                  </button>
               </div>
            </div>

            <!-- 로그인 상태 유지 -->
            <div class="form-check text-start m-0">
               <input class="form-check-input me-1" type="checkbox" v-model="rememberMe">
               <label class="form-check-label">아이디 저장하기</label>
            </div>

            <div class="btn_login_wrap">
               <button type="button" class="btn_login" @click="btnLogin">로그인</button>
            </div>
         </div>

         <ul class="find_wrap ps-0">
            <li><a href="/findPw" class="find_text" @click="goToFindPw">비밀번호 찾기</a></li>
         </ul>
      </div>

   </div>
</template>

<script setup>
   import { onBeforeMount, onBeforeUnmount, ref, watch } from "vue";
   import { useStore } from "vuex";
   import { useRouter } from "vue-router";
   import axios from "axios";
   import Swal from 'sweetalert2';
   import { useUserInfoStore  } from '../../store/userStore';

// ================================================== Pinia 스토어 및 Vuex 설정 ==================================================
   const store = useStore();
   const userInfoStore = useUserInfoStore();
   const router = useRouter();

   // 새로고침 시 저장된 데이터 불러오기
   userInfoStore.loadStoredData();

// ================================================== side, header 숨기기 ==================================================
   const toggleEveryDisplay = () => store.commit("toggleEveryDisplay");
   const toggleHideConfig = () => store.commit("toggleHideConfig");

   // 페이지 진입 시 숨김 적용
   onBeforeMount(() => {
      toggleEveryDisplay();
      toggleHideConfig();
   });

   // 다른 페이지로 이동할 때 다시 원래대로 복구
   onBeforeUnmount(() => {
      toggleEveryDisplay();
      toggleHideConfig();
   });

// ================================================== id, password 입력창 ==================================================
   // 로그인 시도한 아이디는 항상 저장되도록 설정
   const userId = ref(userInfoStore.savedUserId ?? "");  // pinia에 저장된 아이디 불러오기
   const password = ref("");  // 비밀번호
   const isPasswordVisible = ref(false);  // 비밀번호 표시 여부
   const rememberMe = ref(userInfoStore.isRemembered); // 아이디 저장 체크박스 값

   // 체크박스 변경 시 자동 반영
   watch(rememberMe, (newValue) => {
      userInfoStore.isRemembered = newValue;

      if (!newValue) {
         userId.value = ""; // 체크 해제 시 아이디 입력 필드도 초기화
      }
   });

   // 비밀번호 가리기/보이기 토글
   const togglePasswordVisibility = () => {
      isPasswordVisible.value = !isPasswordVisible.value;
   };

   // 아이디 입력 초기화
   const userIdReset = () => {
      userId.value = "";
   };

   // 비밀번호 입력 초기화
   const passwordReset = () => {
      password.value = "";
   };

   // 비밀번호 찾기 페이지로 이동
   const goToFindPw = () => {
      router.push({ path : '/findPw' });
   }

// ============================================= Btn Event =============================================
   // 로그인 버튼 함수
   const btnLogin = () => {
      loginSelect();
   }

   // 엔터 키 이벤트 핸들러 (로그인 실행)
   const keyEventHandler = () => {
      btnLogin(); // 로그인 실행
   }

// ============================================= Axios Event =============================================
   // id, pass값 서버로 보내기
   const loginSelect = async () => {

      const isValid = await validateLoginInput();
      if (!isValid) return; // 유효성 실패 시 종료

      userInfoStore.saveUserId(userId.value, rememberMe.value); // 아이디 저장하기 체크 시 로그인 시도한 아이디 저장

      try {
         const response = await axios.post('/api/loginProc', {
            username: userId.value,
            password: password.value
         }, {
            headers: { 'Content-Type': 'application/json' },
            'Authorization': ''
         });

         // 성공 처리
         const { result, token, user } = response.data;

         if (result === "success") {
            localStorage.setItem("token", token);
            userInfoStore.setUser(user);

            Swal.fire({
               icon: "success",
               title: "Login 성공 !!!",
            }).then(() => {
               router.push('/home');
            });
         }

      } catch (err) {
         const { response } = err;

         if (response?.data) {
            const { code, message } = response.data;

            if (code === 423) {
               // 계정 잠금 (LOCKED)
               await Swal.fire({
                  icon: "warning",
                  title: "계정 잠김",
                  text: message || "비밀번호 5회 실패로 계정이 잠겼습니다. 관리자에게 문의하세요."
               });
               return;
            }

            if (code === 401) {
               // 로그인 실패 (UNAUTHORIZED)
               await Swal.fire({
                  icon: "error",
                  title: "로그인 실패",
                  text: message || "아이디 또는 비밀번호가 틀렸습니다."
               });
               return;
            }
         }

         // 그 외 오류
         await Swal.fire({
            icon: "error",
            title: "오류 발생",
            text: "서버와의 통신 중 문제가 발생했습니다."
         });
      }
   }

   // 유효성 체크 함수
   const validateLoginInput = async () => {
      if (!userId.value.trim()) {
         await Swal.fire({
            icon: 'warning',
            title: '아이디를 입력하세요',
         });

         const userIdInput = document.querySelector('.input_id');
         if (userIdInput) userIdInput.focus();

         return false; // 유효성 실패
      }

      if (!password.value.trim()) {
         await Swal.fire({
            icon: 'warning',
            title: '비밀번호를 입력하세요',
         });

         const passwordInput = document.querySelector('.input_pw');
         if (passwordInput) passwordInput.focus();

         return false; // 유효성 실패
      }

      return true; // 유효성 통과
   };

</script>
