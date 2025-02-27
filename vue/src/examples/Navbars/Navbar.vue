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
               <li class="nav-item dropdown ps-3 d-flex align-items-center me-3">
                  <!-- 로그인 유저 정보 -->
                  <a
                     href="#"
                     class="p-0 nav-link lh-1 font-16"
                     :class="[color ? color : 'text-body', showProfile ? 'show' : '']"
                     id="dropdownMenuButton"
                     data-bs-toggle="dropdown"
                     aria-expanded="false"
                     @click="showProfile = !showProfile"
                  >
                     <i class="fa-solid fa-user font-16"></i>
                     <span v-if="userInfoStore.isAuthenticated" class="ms-2 font-16">
                        {{ userInfoStore.user.gradeNm }} {{ userInfoStore.user.mberNm }}
                     </span>
                  </a>

                  <ul
                     class="px-2 py-3 dropdown-menu dropdown-menu-end me-sm-n4"
                     :class="showProfile ? 'show' : ''"
                     aria-labelledby="dropdownMenuButton"
                  >
                     <li class="mb-2">
                        <a class="dropdown-item border-radius-md" @click="btnLogout">
                        <div class="py-1 d-flex">
                           <div class="d-flex flex-column justify-content-center">
                              <h5 class="mb-1 text-sm font-weight-normal">
                                 <i class="fa-solid fa-right-to-bracket"></i>
                                 <span class="font-weight-bold"> 로그아웃</span>
                              </h5>
                           </div>
                        </div>
                        </a>
                     </li>

                     <li class="mb-2">
                        <a class="dropdown-item border-radius-md" href="/personal/manage">

                        <div class="py-1 d-flex">
                           <div class="d-flex flex-column justify-content-center">
                              <h6 class="mb-1 text-sm font-weight-normal">
                                 <i class="fa-solid fa-user-pen"></i>
                                 <span class="font-weight-bold"> 개인정보수정</span>
                              </h6>
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
                  <button class="btn btn-primary" @click="btnCommuteAdd" v-if="!lastCmt">출근</button>
                  <button class="btn btn-danger" @click="btnCommuteModify" v-else>퇴근</button>
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
import { ref, onBeforeMount } from "vue";
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

      const btnLogout = () => {
         userInfoStore.logout();
         router.push('/login');
      };
      //let loginUser = "user01";

      // 출근
      const btnCommuteAdd = async () => {
         const result = await cmtCheck(null, null); // 현재 시간 기준 출근 체크

         console.log(result);

         const addData = new FormData();
         addData.append("mberId", loginUser); // 로그인 유저 정보로 변경 예정
         addData.append("goTime", dateTimeFormat(result.goTime, 'yyyy-MM-dd hh:mm:ss'));
         addData.append("goState", result.goState); // 버튼 동작 시간 체크 후 지각여부 체크 후 입력

         await axios.post('/api/commute/cmtAdd', addData);
         await store.dispatch("commuteGetList"); // Vuex에서 출퇴근 목록 갱신

         // 출퇴근 버튼 클릭 후 날짜 초기화
         store.commit("setStartDate", "");
         store.commit("setEndDate", "");

         lastCmtGetInfo(); // 출근 후 마지막 기록 갱신
      }

      // 퇴근
      const btnCommuteModify = async () => {
         let result = await cmtCheck(lastCmt.value.goTime, null);

         const modifyData = new FormData();
         modifyData.append("commuteCd", lastCmt.value.commuteCd);
         modifyData.append("leaveTime", dateTimeFormat(result.leaveTime, 'yyyy-MM-dd hh:mm:ss'));
         modifyData.append("leaveState", result.leaveState);
         modifyData.append("workTime", result.workTime);
         modifyData.append("overWorkTime", result.overWorkTime);
         await axios.post(`/api/commute/cmtModify`, modifyData);
         await store.dispatch("commuteGetList"); // Vuex에서 출퇴근 목록 갱신

         // 출퇴근 버튼 클릭 후 날짜 초기화
         store.commit("setStartDate", "");
         store.commit("setEndDate", "");

         lastCmtGetInfo(); // 출근 후 마지막 기록 갱신
      }

      // 마지막 출퇴근 기록
      const lastCmt = ref(null);
      const lastCmtGetInfo = async () => {
         console.log("출퇴근 기록 ")
         // const result = await axios.get('/api/commute/lastCmtInfo?mberId=user01');
         // lastCmt.value = result.data ? result.data : null;
      }
      onBeforeMount(() => {
         lastCmtGetInfo();
      })

      return {
         btnCommuteAdd,
         btnCommuteModify,
         lastCmtGetInfo,
         lastCmt,

         userInfoStore, // 유저정보 저장소
         loginUser, // 현재 로그인되어있는 유저 id
         btnLogout,
      }
   },
};
</script>
