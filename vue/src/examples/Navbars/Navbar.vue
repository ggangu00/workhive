<template>
   <nav
      class="shadow-none navbar navbar-main navbar-expand-lg border-radius-xl"
      v-bind="$attrs"
      id="navbarBlur"
      data-scroll="true"
      :class="isAbsolute ? 'mt-4' : 'mt-0'"
   >
      <div class="px-3 py-1 container-fluid">
         <breadcrumbs :currentPage="currentRouteName" :color="color" />
         <div
            class="mt-2 collapse navbar-collapse mt-sm-0 me-md-0 me-sm-4"
            :class="isRTL ? 'px-0' : 'me-sm-4'"
            id="navbar"
         >
            <div
               class="pe-md-3 d-flex align-items-center"
               :class="isRTL ? 'me-md-auto' : 'ms-md-auto'"
            >
            </div>

            <ul class="navbar-nav justify-content-end">
               <li class="nav-item dropdown ps-3 d-flex align-items-center me-3" @click="showProfile = !showProfile">
                  <!-- 로그인 유저 정보 -->
                  <a
                     href="#"
                     class="p-0 nav-link lh-1 font-16"
                     :class="[color ? color : 'text-body', showProfile ? 'show' : '']"
                     id="dropdownMenuButton"
                     data-bs-toggle="dropdown"
                     aria-expanded="false"
                     
                  >
                     <i class="fa-solid fa-user font-16"></i>
                     <span v-if="userInfoStore.isAuthenticated" class="ms-2 font-16">
                        {{ userInfoStore.user.gradeNm }} {{ userInfoStore.user.mberNm }}
                     </span>
                  </a>

                  <ul
                     class="dropdown-menu dropdown-menu-end me-sm-n4 p-none"
                     :class="showProfile ? 'show' : ''"
                     aria-labelledby="dropdownMenuButton"
                  >
                     <li>
                        <a class="dropdown-item border-radius-md" href="/personal/manage">

                        <div class="py-1 d-flex">
                           <div class="d-flex flex-column justify-content-center">
                              <h5 class="mb-0 text-sm font-weight-normal">
                                 <i class="fa-solid fa-user-pen"></i>
                                 개인정보수정
                              </h5>
                           </div>
                        </div>
                        </a>
                     </li>
                     <li>
                        <a class="dropdown-item border-radius-md" @click="btnLogout">
                        <div class="py-1 d-flex">
                           <div class="d-flex flex-column justify-content-center">
                              <h5 class="mb-0 text-sm font-weight-normal">
                                 <i class="fa-solid fa-right-to-bracket"></i>
                                 로그아웃
                              </h5>
                           </div>
                        </div>
                        </a>
                     </li>
                  </ul>
               </li>

               <!-- 조직도 버튼 -->
               <li class="nav-item d-flex align-items-center me-2">
                  <button class="btn btn-info btn-fill">
                     <i class="fa-solid fa-user-group"></i>
                     조직도
                  </button>
               </li>

               <!-- 출퇴근 버튼 추가 - 토글식으로 변경 예정 -->
               <li class="nav-item d-flex align-items-center me-2">
                  <!-- <button class="btn btn-primary" @click="btnCommuteAdd" v-if="isLogin && !isCmt">출근</button>
                  <button class="btn btn-danger" @click="btnCommuteModify" v-if="isLogin && isCmt">퇴근</button> -->
                  <button class="btn btn-primary" @click="btnCommuteAdd" :disabled="isLoadingAdd" v-if="isLogin && !isCmt">
                     {{ isLoadingAdd ? '⏳ 출근 중...' : '출근' }}
                  </button>
                  <button class="btn btn-danger" @click="btnCommuteModify" :disabled="isLoadingModify" v-if="isLogin && isCmt">
                     {{ isLoadingModify ? '⏳ 퇴근 중...' : '퇴근' }}
                  </button>
               </li>


               <li class="nav-item d-xl-none ps-3 d-flex align-items-center">
                  <a
                     href="#"
                     @click="toggleSidebar"
                     class="p-0 nav-link text-body lh-1"
                     id="iconNavbarSidenav"
                  >
                     <div class="sidenav-toggler-inner">
                        <i class="sidenav-toggler-line"></i>
                        <i class="sidenav-toggler-line"></i>
                        <i class="sidenav-toggler-line"></i>
                     </div>
                  </a>
               </li>

            </ul>
         </div>
      </div>
   </nav>
</template>
<script>
import Breadcrumbs from "../Breadcrumbs.vue";
import { mapMutations, mapState } from "vuex";

// ksy 추가
import axios from 'axios';
import { useStore } from "vuex";
import { ref, onMounted, watch } from "vue";
import { useRouter } from "vue-router";
import { cmtCheck } from "../../assets/js/commute";
import { dateTimeFormat } from "../../assets/js/common";
import { useUserInfoStore } from '../../store/userStore.js';

export default {

   name: "navbar",
   data() {
      return {
         showMenu: false,
         showProfile: false
      };
   },
   props: ["minNav", "color"],
   created() {
      this.minNav;
   },
   methods: {
      ...mapMutations(["navbarMinimize", "toggleConfigurator"]),

      toggleSidebar() {
         this.navbarMinimize();
      },
   },
   components: {
      Breadcrumbs,
   },
   computed: {
      ...mapState(["isRTL", "isAbsolute"]),

      currentRouteName() {
         return this.$route.name;
      },
   },

   // 출퇴근 기능 추가
   setup() {
      const userInfoStore = useUserInfoStore();
      let loginUser = userInfoStore.user ? userInfoStore.user.mberId : ""; // 로그인한 사용자 정보 사용

      const store = useStore();  //vuex
      const router = useRouter();// router

      // 로그인 체크
      let lastCmt = ref(null);
      let isLogin = ref(loginUser != "");
      let isCmt = ref(false);
      watch(() => lastCmt.value, () => {
         console.log("lastCmt watch");
         isCmt.value = lastCmt.value != null;
      });
      watch(() => isCmt.value, () => {
         console.log("isCmt watch");
         store.commit('isCmtSet', isCmt.value);
      })

      // 출퇴근 로딩
      let isLoadingAdd = ref(false);
      let isLoadingModify = ref(false);

      const btnLogout = () => {
         userInfoStore.logout();
         router.push('/login');
      };

      // 출근
      const btnCommuteAdd = async () => {
         if (isLoadingAdd.value) return; // 중복 방지
         isLoadingAdd.value = true;

         try {
            const result = await cmtCheck(null, null); // 현재 시간 기준 출근 체크
            const addData = new FormData();
            addData.append("mberId", loginUser);
            addData.append("goTime", dateTimeFormat(result.goTime, 'yyyy-MM-dd hh:mm:ss'));
            addData.append("goState", result.goState);

            await axios.post('/api/commute/cmtAdd', addData);

            store.commit("setStartDate", "");
            store.commit("setEndDate", "");

            await lastCmtGetInfo(); // 출근 후 마지막 기록 갱신
         } catch (error) {
            console.error('출근 처리 중 오류:', error);
         } finally {
            isLoadingAdd.value = false;
         }
      }


      // 퇴근
      const btnCommuteModify = async () => {
         if (isLoadingModify.value) return; // 중복 방지
         isLoadingModify.value = true;

         try {
            let result = await cmtCheck(lastCmt.value.goTime, null);
            const modifyData = new FormData();
            modifyData.append("commuteCd", lastCmt.value.commuteCd);
            modifyData.append("leaveTime", dateTimeFormat(result.leaveTime, 'yyyy-MM-dd hh:mm:ss'));
            modifyData.append("leaveState", result.leaveState);
            modifyData.append("workTime", result.workTime);
            modifyData.append("overWorkTime", result.overWorkTime);

            await axios.post(`/api/commute/cmtModify`, modifyData);

            store.commit("setStartDate", "");
            store.commit("setEndDate", "");

            await lastCmtGetInfo(); // 퇴근 후 마지막 기록 갱신
         } catch (error) {
            console.error('퇴근 처리 중 오류:', error);
         } finally {
            isLoadingModify.value = false;
         }
      }

      // 마지막 출퇴근 기록 조회
      const lastCmtGetInfo = async () => {
         const result = await axios.get(`/api/commute/lastCmtInfo?mberId=${loginUser}`);
         lastCmt.value = result.data ? result.data : null;
      }
      onMounted(async () => {
         if(isLogin.value) await lastCmtGetInfo();
         console.log("로그인 체크 : ", isLogin.value);
         console.log("출퇴근 체크 : ", isCmt.value);
      })

      return {
         btnCommuteAdd,
         btnCommuteModify,
         lastCmtGetInfo,
         isLogin,
         isCmt,

         userInfoStore, // 유저정보 저장소
         loginUser, // 현재 로그인되어있는 유저 id
         btnLogout,
      }
   },
};
</script>
<style>
.dropdown-menu { 
   border: 1px solid #eee !important;
   h5{
      color: #7b809a;
   }
   li:first-child {
      border-bottom: 1px solid #eee !important;
   }   
   i {
      width: 20px;
      color: #7b809a;
   }
}
</style>