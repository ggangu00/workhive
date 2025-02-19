<template>
   <div class="container-fluid mx-auto" style="background-color: #fff;">
      <div class="d-flex justify-content-center align-items-center">
         <img src="../../assets/img/vue-logo.png" alt="WorkHive Logo" class="me-3" style="width: 100px; height: auto;">
         <h1 class="">WorkHive</h1>
      </div>
      <div class="">
         <div class="login_box">
            <div class="input_item id on" id="input_item_id">
               <input type="text"  title="아이디" class="input_id" aria-label="아이디 또는 전화번호">
               <label class="text_label" id="id_label" aria-hidden="true">아이디 또는 전화번호</label>
               <button type="button" class="btn_delete" id="id_clear" style="display: block;">
                  <i class="fa-solid fa-xmark"></i>
               </button>
            </div>
            <div class="input_item pw on" id="input_item_pw">
               <input type="password" id="pw" name="pw" title="비밀번호" class="input_pw" maxlength="16" aria-label="비밀번호">
               <label for="pw" class="text_label" id="pw_label" aria-hidden="true">비밀번호</label>
               <button type="button" class="btn_view hide" id="pw_hide" aria-pressed="false" style="display: block;">
                  <i class="fa-solid fa-lock"></i><i class="fa-solid fa-lock-open"></i>
               </button>
               <button type="button" class="btn_delete" id="pw_clear" style="display: block;">
                  <i class="fa-solid fa-xmark"></i>
               </button>
            </div>
            <!-- 로그인 상태 유지 -->
            <div class="login_keep_wrap" id="login_keep_wrap">
               <div class="keep_check">
                  <input type="checkbox" id="keep" name="nvlong" class="input_keep" value="off">
                  <label for="keep" class="keep_text">로그인 상태 유지</label>
               </div>
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

<style scoped>
/* 전체 화면 배경 및 정렬 */
.container-fluid {
   display: flex;
   flex-direction: column;
   align-items: center;
   justify-content: center;
   height: 100vh;
   background-color: #fff;
}

/* 로고 및 타이틀 정렬 */
.d-flex {
   display: flex;
   align-items: center;
   justify-content: center;
   margin-bottom: 30px;
}

img {
   width: 100px;
   height: auto;
   margin-right: 10px;
}

h1 {
   font-size: 32px;
   font-weight: bold;
   color: #222222;
}

/* 로그인 박스 */
.login_box {
   background: #fff;
   padding: 30px;
   border-radius: 10px;
   box-shadow: 0 4px 10px rgba(0, 0, 0, 0.1);
   width: 400px;
   text-align: center;
}

/* 입력 필드 */
.input_item {
   position: relative;
   margin-bottom: 15px;
   display: flex;
   align-items: center;
   border: 1px solid #ddd;
   border-radius: 8px;
   padding: 12px;
   background: #fff;
}

.input_item input {
   width: 100%;
   border: none;
   outline: none;
   font-size: 16px;
   padding: 5px;
}

.input_item label {
   position: absolute;
   left: 12px;
   top: 50%;
   transform: translateY(-50%);
   font-size: 14px;
   color: #aaa;
   pointer-events: none;
   transition: 0.3s ease;
}

/* 입력 필드에 값이 입력되면 label 숨김 */
.input_item input:focus + label,
.input_item input:not(:placeholder-shown) + label {
   top: 5px;
   font-size: 12px;
   color: #777;
}

/* 삭제 버튼 */
.btn_delete {
   background: transparent;
   border: none;
   cursor: pointer;
   font-size: 16px;
   color: #aaa;
}

.btn_delete:hover {
   color: #666;
}

/* 로그인 유지 체크박스 */
.login_keep_wrap {
   display: flex;
   justify-content: start;
   align-items: center;
   margin-bottom: 15px;
   font-size: 14px;
   color: #666;
}

.keep_check {
   display: flex;
   align-items: center;
}

.input_keep {
   margin-right: 5px;
   width: 16px;
   height: 16px;
}

/* 로그인 버튼 */
.btn_login_wrap {
   margin-top: 10px;
}

.btn_login {
   width: 100%;
   background-color: #facc15;
   color: #fff;
   font-size: 18px;
   font-weight: bold;
   padding: 12px;
   border: none;
   border-radius: 8px;
   cursor: pointer;
   transition: background 0.3s;
}

.btn_login:hover {
   background-color: #eab308;
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
