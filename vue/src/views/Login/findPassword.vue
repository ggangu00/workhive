<template>
   <div class="login-container-fluid mx-auto" style="background-color: #fff;">

      <div class="d-flex justify-content-center align-items-center mb30">
         <img src="../../assets/img/vue-logo.png" alt="WorkHive Logo" class="me-3" style="width: 100px; height: auto;">
         <h1>WorkHive</h1>
      </div>

      <div class="mb20">
         <h5>비밀번호를 찾고자 하는 아이디를 입력하세요</h5>
      </div>

      <div class="mx-auto">
         <div class="login_box">
            <div class="input_box">
               <div class="input_item">
                  <!-- 아이디 입력 필드 -->
                  <input type="text" class="input_id" v-model="username">
                  <label class="text_label">아이디 또는 전화번호</label>

                  <!-- 아이디 입력 초기화 버튼 (한 글자 이상 입력 시 표시) -->
                  <button v-if="username.length > 0" type="button" class="btn_delete" @click="usernameReset">
                     <i class="fa-solid fa-xmark"></i>
                  </button>
               </div>
            </div>

         </div>

         <div class="btn_login_wrap">
            <button type="button" class="btn_login">다음</button>
         </div>
      </div>

   </div>
</template>

<script setup>
   import { onBeforeMount, onBeforeUnmount, ref } from "vue";
   import { useStore } from "vuex";
   //import { useRouter } from "vue-router";

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

   const username = ref(""); // 아이디 입력값

   // 아이디 입력 초기화
   const usernameReset = () => {
      username.value = "";
   };

   // 비밀번호 찾기 페이지로 이동
   // const router = useRouter();
   // const goToFindPw = () => {
   //    router.push({ path : '/findPw' });
   // }

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
