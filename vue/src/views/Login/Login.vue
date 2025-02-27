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
               <input class="form-check-input me-1" type="checkbox">
               <label class="form-check-label">로그인 상태 유지</label>
            </div>

            <div class="btn_login_wrap">
               <button type="button" class="btn_login" @click="btnLogin">로그인</button>
            </div>
         </div>



         <ul class="find_wrap">
            <li><a href="/findPw" class="find_text" @click="goToFindPw">비밀번호 찾기</a></li>
            <li>|</li>
            <li><a href="#" class="find_text">아이디 찾기</a></li>
         </ul>
      </div>

   </div>
</template>

<script setup>
   import { onBeforeMount, onBeforeUnmount, ref } from "vue";
   import { useStore } from "vuex";
   import { useRouter } from "vue-router";
   import axios from "axios";
   import Swal from 'sweetalert2';

// ================================================== side, header 숨기기 ==================================================
   const store = useStore();

   const toggleEveryDisplay = () => store.commit("toggleEveryDisplay");
   const toggleHideConfig = () => store.commit("toggleHideConfig");

   onBeforeMount(() => {
      toggleEveryDisplay();
      toggleHideConfig();
   });

   onBeforeUnmount(() => {
      toggleEveryDisplay();
      toggleHideConfig();
   });

   const userId = ref(""); // 아이디 입력값
   const password = ref(""); // 비밀번호 입력값
   const isPasswordVisible = ref(false); // 비밀번호 입력 체크

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
   const router = useRouter();
   const goToFindPw = () => {
      router.push({ path : '/findPw' });
   }

// ============================================= Btn Event =============================================
   // 로그인 버튼 함수
   const btnLogin = () => {
      loginSelect();
   }

   const keyEventHandler = () => {
      btnLogin(); // 로그인 실행
   }

// ============================================= Axios Event =============================================
   // id, pass값 서버로 보내기
   const loginSelect = async () => {
      // const requestData = {
      //    username : userId.value,
      //    password : password.value,
      // }

      const options = {
         method: 'POST',
         headers: { 'content-type': 'application/x-www-form-urlencoded' },
         data: `username=${userId.value}&password=${password.value}`,
         url : '/api/login'
      };

      try {
         const result = await axios(options);

         console.log("로그인 성공 => ", result.data);

         if(result.data.result == "success") {
            Swal.fire({
               icon: "success",
               title: "Login 성공 !!!",
            });
            router.push({ path: `/home` });
         }

      } catch (err) {
         console.log("로그인 실패 => ", err)
         Swal.fire({
            icon: "error",
            title: "Login 실패",
            text:  "Error : " + err.response.data.error
         });
      }
   }

</script>
