<template>
   <div class="container-fluid mx-auto">
      <div class="d-flex justify-content-center align-items-center">
         <img src="../../assets/img/vue-logo.png" alt="WorkHive Logo" class="me-3" style="width: 100px; height: auto;">
         <h1 class="">WorkHive</h1>
      </div>
      <div class="">
         <div class="login_box">
            <div class="input_item id on" id="input_item_id">
               <input type="text" id="id" name="id" accesskey="L" maxlength="41" autocapitalize="none" value="" title="아이디" class="input_id" aria-label="아이디 또는 전화번호">
               <label for="id" class="text_label" id="id_label" aria-hidden="true">아이디 또는 전화번호</label>
               <button type="button" class="btn_delete" id="id_clear" style="display: block;">
                  <span class="icon_delete"><span class="blind">삭제</span></span>
               </button>
            </div>
            <div class="input_item pw on" id="input_item_pw">
               <input type="password" id="pw" name="pw" title="비밀번호" class="input_pw" maxlength="16" aria-label="비밀번호">
               <label for="pw" class="text_label" id="pw_label" aria-hidden="true">비밀번호</label>
               <button type="button" class="btn_view hide" id="pw_hide" aria-pressed="false" style="display: block;">
                  <span class="icon_view"><span class="blind" id="icon_view">비밀번호 표시</span></span>
               </button>
               <button type="button" class="btn_delete" id="pw_clear" style="display: block;">
                  <span class="icon_delete"><span class="blind">삭제</span></span>
               </button>
            </div>
         </div>

         <div class="login_keep_wrap" id="login_keep_wrap">
            <div class="keep_check">
               <input type="checkbox" id="keep" name="nvlong" class="input_keep" value="off">
               <label for="keep" class="keep_text">로그인 상태 유지</label>
            </div>
         </div>
         <div class="btn_login_wrap">
            <button type="submit" class="btn_login next_step nlog-click" id="log.login">로그인</button>
         </div>

         <ul class="find_wrap" id="find_wrap">
            <li><a target="_blank" href="#" id="idinquiry" class="find_text">비밀번호 찾기</a></li>
            <li><a target="_blank" href="#" id="pwinquiry" class="find_text">아이디 찾기</a></li>
         </ul>
      </div>

   </div>
</template>

<script setup>
   import { onBeforeMount, onBeforeUnmount } from "vue";
   import { useStore } from "vuex";

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
</script>

<style>
   body {
      background-color: white !important;
   }
</style>