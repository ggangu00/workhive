<template>
   <div class="login-container-fluid mx-auto" style="background-color: #fff;">

      <div class="d-flex justify-content-center align-items-center mb30">
         <img src="../../assets/img/vue-logo.png" alt="WorkHive Logo" class="me-3" style="width: 100px; height: auto;">
         <h1>WorkHive</h1>
      </div>

      <div class="mb20">
         <h5>비밀번호를 찾고자 하는 아이디를 입력하세요</h5>
      </div>

      <div class="mx-auto" @keydown.enter="keyEventHandler">
         <div class="login_box">
            <div class="input_box">
               <div class="input_item">
                  <!-- 아이디 입력 필드 -->
                  <input type="text" class="input_id" v-model="mberId">
                  <label class="text_label">아이디 또는 전화번호</label>

                  <!-- 아이디 입력 초기화 버튼 (한 글자 이상 입력 시 표시) -->
                  <button v-if="mberId.length > 0" type="button" class="btn_delete" @click="userIdReset">
                     <i class="fa-solid fa-xmark"></i>
                  </button>
               </div>
            </div>

            <div class="btn_login_wrap">
               <button type="button" class="btn_login btn_next" @click="btnFindPw">다음</button>
            </div>
         </div>
      </div>

   </div>
</template>

<script setup>
   import { onBeforeMount, onBeforeUnmount, ref } from "vue";
   import { useStore } from "vuex";
   import { useRouter } from "vue-router";
   import axios from 'axios';
   import Swal from 'sweetalert2';

// ================================================== side, header 숨기기 ==================================================
   const store = useStore(); // pinia 선언
   const router = useRouter();   // router 선언

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

// ============================================= Btn Event =============================================
   // 엔터 키 이벤트 핸들러 (로그인 실행)
   const keyEventHandler = () => {
      if (!mberId.value.trim()) {
         Swal.fire({
            icon: 'warning',
            title: '아이디를 입력하세요',
         });

         const userIdInput = document.querySelector('.input_id');
         if (userIdInput) userIdInput.focus();

         return false; // 유효성 실패
      }
      findPasswordGet();
   }

   const mberId = ref(""); // 아이디 입력값

   // 아이디 입력 초기화
   const userIdReset = () => {
      mberId.value = "";
   };

   // 다음으로 넘어가기 버튼
   const btnFindPw = () => {
      if (!mberId.value.trim()) {
         Swal.fire({
            icon: 'warning',
            title: '아이디를 입력하세요',
         });

         const userIdInput = document.querySelector('.input_id');
         if (userIdInput) userIdInput.focus();

         return false; // 유효성 실패
      }

      findPasswordGet();
   }
// ============================================= Axios Event =============================================
   const findPasswordGet = async () => {
      try {
         const result = await axios.post('/api/passwordProc', { mberId : mberId.value });

         if(result.data.code === 200) {
            Swal.fire({
               icon: "success",
               title: "등록된 메일함을 확인하세요.",
               text:  result.data.message
            }).then(() => {
               router.push('/login');
            });
         }

      } catch (err) {
         Swal.fire({
            icon: "error",
            title: "API 조회 오류",
            text:  "Error : " + err.response.data.error
         });
      }
   };

</script>

<style scoped>

   .input_keep {
      width: 15px;
      height: 15px;
   }

   /* 아이디 / 비밀번호 찾기 */
   .find_wrap {
      display: flex;
      justify-content: center;
      margin-top: 15px;
      font-size: 14px;
   }

   .find_wrap li {
      list-style: none;
      margin: 0 10px;
   }

   .find_wrap a {
      text-decoration: none;
      color: #666;
   }

   .find_wrap a:hover {
      text-decoration: underline;
   }
</style>
