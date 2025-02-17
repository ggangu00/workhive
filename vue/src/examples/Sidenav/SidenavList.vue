<template>
   <div class="w-auto h-auto collapse navbar-collapse max-height-vh-100 h-100"
      id="sidenav-collapse-main"
   >
      <ul class="navbar-nav">
         <li class="nav-item">
         <sidenav-collapse
            url="#"
            :aria-controls="''"
            v-bind:collapse="false"
            collapseRef="/guide"
            navText="레이아웃"
         >
            <template v-slot:icon>
               <i class="material-icons-round opacity-10 fs-5">dashboard</i>
            </template>
         </sidenav-collapse>
         </li>
         <li class="nav-item">
         <sidenav-collapse
            url="#"
            :aria-controls="''"
            v-bind:collapse="false"
            collapseRef="/layout"
            navText="레이아웃틀"
         >
            <template v-slot:icon>
               <i class="material-icons-round opacity-10 fs-5">dashboard</i>
            </template>
         </sidenav-collapse>
         </li>
         <li class="nav-item">
         <sidenav-collapse
               url="#"
               :aria-controls="''"
               v-bind:collapse="false"
               collapseRef="/todo/manage"
               navText="업무일지 관리"
         >
            <template v-slot:icon>
               <i class="fa-solid fa-list-ol"></i>
            </template>
            <template v-slot:list>
               <li class="sub-item" @click="movePage('Todo')">업무일지 관리</li>
            </template>
         </sidenav-collapse>
         </li>
         <li class="nav-item">
         <sidenav-collapse
               url="#"
               :aria-controls="''"
               v-bind:collapse="false"
               collapseRef="/project/list"
               navText="프로젝트 관리"
         >
            <template v-slot:icon>
               <i class="fa-solid fa-chart-line"></i>
            </template>
            <template v-slot:list>
               <li class="sub-item" @click="movePage('ProjectList')">프로젝트 조회</li>
               <li class="sub-item" @click="movePage('ProjectAdd')">프로젝트 등록</li>
               <li class="sub-item" @click="movePage('ProjectMem')">프로젝트 참여자관리</li>
            </template>
         </sidenav-collapse>
         </li>

         <li class="nav-item">
         <sidenav-collapse
               url="#"
               :aria-controls="''"
               v-bind:collapse="false"
               collapseRef="/meet/list"
               navText="회의 관리"
         >
            <template v-slot:icon>
               <i class="fa-solid fa-users-line"></i>
            </template>
            <template v-slot:list>
               <li class="sub-item" @click="movePage('MeetList')">회의 조회</li>
               <li class="sub-item" @click="movePage('MeetAdd')">회의 등록</li>
            </template>
         </sidenav-collapse>
         </li>

         <li class="nav-item">
         <sidenav-collapse
            url="#"
            :aria-controls="''"
            v-bind:collapse="false"
            collapseRef="/approval/register"
            navText="전자결재"
         >
         <template v-slot:icon>
            <i class="fa-solid fa-file"></i>
            </template>
            <template v-slot:list>
               <li class="sub-item" @click="movePage('Register')">문서기안</li>
               <li class="sub-item" @click="movePage('Completed')">완료함</li>
               <li class="sub-item" @click="movePage('Test')">컴포넌트테스트</li>
               <li class="sub-item" @click="movePage('RegisterTest')">등록테스트</li>
            </template>
         </sidenav-collapse>
         </li>
         <li class="nav-item">
         <sidenav-collapse
            url="#"
            :aria-controls="''"
            v-bind:collapse="false"
            collapseRef="/schedule/calendar"
            navText="일정관리"
         >
         <template v-slot:icon>
            <i class="fa-regular fa-calendar-check"></i>
            </template>
            <template v-slot:list>
               <li class="sub-item" @click="movePage('Calendar')">풀캘린더</li>
            </template>
         </sidenav-collapse>
         </li>



         <!-- KSY -->
         <li class="nav-item">
         <sidenav-collapse
            url="#"
            :aria-controls="''"
            v-bind:collapse="false"
            collapseRef="/deptstore/list"
            navText="부서 업무 관리"
         >
            <template v-slot:icon>
               <i class="fa-solid fa-box-archive"></i>
            </template>
         </sidenav-collapse>
         </li>
         <li class="nav-item">
         <sidenav-collapse
               url="#"
               :aria-controls="''"
               v-bind:collapse="false"
               collapseRef="/commute/cmtList"
               navText="출퇴근 관리"
         >
            <template v-slot:icon>
               <i class="material-icons-round opacity-10 fs-5">dashboard</i>
            </template>
            <template v-slot:list>
               <li class="sub-item" @click="movePage('CmtList')">출퇴근 기록 조회</li>
               <li class="sub-item" @click="movePage('CrctManage')">출퇴근 정정 요청 등록</li>
               <li class="sub-item" @click="movePage('CrctList')">출퇴근 정정 요청 조회</li>
               <li class="sub-item" @click="movePage('CrctSignManage')">출퇴근 정정 요청 결재</li>
            </template>
         </sidenav-collapse>
         </li>
         <li class="nav-item">
         <sidenav-collapse
               url="#"
               :aria-controls="''"
               v-bind:collapse="false"
               collapseRef="/vacation/vcList"
               navText="휴가 관리"
         >
            <template v-slot:icon>
               <i class="material-icons-round opacity-10 fs-5">dashboard</i>
            </template>
            <template v-slot:list>
               <li class="sub-item" @click="movePage('VcList')">휴가 조회</li>
               <li class="sub-item" @click="movePage('VcManage')">휴가 신청 등록</li>
               <li class="sub-item" @click="movePage('VcSignManage')">휴가 신청 결재</li>
            </template>
         </sidenav-collapse>
         </li>

         <!--kjg-->
         <li class="nav-item">
         <sidenav-collapse
            url="#"
            :aria-controls="''"
            v-bind:collapse="false"
            collapseRef="/PaymentList"
            navText="외부지출 관리"
         >
         <template v-slot:icon>
            <i class="material-icons-round opacity-10 fs-5">dashboard</i>
         </template>
         <template v-slot:list>
               <li class="sub-item" @click="movePage('PaymentList')">외부지출 조회</li>
               <li class="sub-item" @click="movePage('PaymentAdd')">외부지출 등록</li>
               <li class="sub-item" @click="movePage('PaymentManage')">외부지출 승인/반려 관리</li>
         </template>
         </sidenav-collapse>
         </li>

         <li class="nav-item">
         <sidenav-collapse
            url="#"
            :aria-controls="''"
            v-bind:collapse="false"
            collapseRef="/BoardList"
            navText="게시판 관리"
         >
            <template v-slot:icon>
               <i class="material-icons-round opacity-10 fs-5">dashboard</i>
            </template>
            <template v-slot:list>
               <li class="sub-item" @click="movePage('BoardList')">게시판 목록</li>
               <li class="sub-item" @click="movePage('BoardAdd')">게시판 등록</li>
            </template>
         </sidenav-collapse>
         </li>

         <li class="nav-item">
         <sidenav-collapse
            url="#"
            :aria-controls="''"
            v-bind:collapse="false"
            collapseRef="/BulletinList"
            navText="게시글 관리"
         >
            <template v-slot:icon>
               <i class="material-icons-round opacity-10 fs-5">dashboard</i>
            </template>
            <template v-slot:list>
               <li class="sub-item" @click="movePage('BulletinList')">게시글 목록</li>
               <li class="sub-item" @click="movePage('BulletinAdd')">게시글 등록</li>
               <li class="sub-item" @click="movePage('BulletinInfo')">게시글 상세조회</li>
            </template>
         </sidenav-collapse>
         </li>

         <li class="nav-item">
         <sidenav-collapse
               url="#"
               :aria-controls="''"
               v-bind:collapse="false"
               collapseRef="/setting/personalInfo"
               navText="관리자"
         >
            <template v-slot:icon>
               <i class="fa-solid fa-gears"></i>
            </template>
            <template v-slot:list>
               <li class="sub-item" @click="movePage('PersonalManage')">개인정보 설정</li>
               <li class="sub-item" @click="movePage('DepartmentManage')">부서 관리</li>
               <li class="sub-item" @click="movePage('MemberManage')">구성원</li>
               <li class="sub-item" @click="movePage('AuthorityManage')">권한관리</li>
               <li class="sub-item" @click="movePage('LoginLog')">접속기록 조회</li>
            </template>
         </sidenav-collapse>
         </li>

         <!-- <li class="nav-item">
         <sidenav-collapse
            url="#"
            :aria-controls="''"
            v-bind:collapse="false"
            collapseRef="dashboard"
            navText="Dashboard"
         >
            <template v-slot:icon>
               <i class="material-icons-round opacity-10 fs-5">dashboard</i>
            </template>
         </sidenav-collapse>
         </li> -->
         <!-- <li class="nav-item">
         <sidenav-collapse
            url="#"
            :aria-controls="''"
            v-bind:collapse="false"
            collapseRef="tables"
            navText="Tables"
         >
            <template v-slot:icon>
               <i class="material-icons-round opacity-10 fs-5">table_view</i>
            </template>
         </sidenav-collapse>
         </li> -->
         <!-- <li class="nav-item">
         <sidenav-collapse
            url="#"
            :aria-controls="''"
            v-bind:collapse="false"
            collapseRef="billing"
            navText="Billing"
         >
            <template v-slot:icon>
               <i class="material-icons-round opacity-10 fs-5">receipt_long</i>
            </template>
         </sidenav-collapse>
         </li> -->
         <!-- <li class="nav-item">
         <sidenav-collapse
            url="#"
            :aria-controls="''"
            v-bind:collapse="false"
            collapseRef="notifications"
            navText="Notifications"
         >
            <template v-slot:icon>
               <i class="material-icons-round opacity-10 fs-5">notifications</i>
            </template>
         </sidenav-collapse>
         </li> -->
         <!-- <li class="mt-3 nav-item">
         <h6
            class="text-xs ps-4 text-uppercase font-weight-bolder text-white"
            :class="$store.state.isRTL ? 'me-4' : 'ms-2'"
         >
            ACCOUNT PAGES
         </h6>
         </li> -->
         <!-- <li class="nav-item">
         <sidenav-collapse
            url="#"
            :aria-controls="''"
            v-bind:collapse="false"
            collapseRef="profile"
            navText="Profile"
         >
            <template v-slot:icon>
               <i class="material-icons-round opacity-10 fs-5">person</i>
            </template>
         </sidenav-collapse>
         </li> -->
         <!-- <li class="nav-item">
         <sidenav-collapse
            url="#"
            :aria-controls="''"
            v-bind:collapse="false"
            collapseRef="sign-in"
            navText="SignIn"
         >
            <template v-slot:icon>
               <i class="material-icons-round opacity-10 fs-5">login</i>
            </template>
         </sidenav-collapse>
         </li> -->
         <!-- <li class="nav-item">
         <sidenav-collapse
            url="#"
            :aria-controls="''"
            v-bind:collapse="false"
            collapseRef="sign-up"
            navText="SignUp"
         >
            <template v-slot:icon>
               <i class="material-icons-round opacity-10 fs-5">assignment</i>
            </template>
         </sidenav-collapse>
         </li>   -->
      </ul>
   </div>
</template>


<script>
   import SidenavCollapse from "./SidenavCollapse.vue";

   export default {
      name: "SidenavList",
      props: {
         cardBg: String,
      },
      data() {
         return {
            title: "Soft UI Dashboard PRO",
            controls: "dashboardsExamples",
            isActive: "active",
         };
      },
      components: {
         SidenavCollapse,
      },

      methods: {
         movePage(page) {
            this.$router.push({ name: page });
         }
      }
   };
</script>

<style scoped>
   .sub-item {
      color:#cfcfcf;
      font-size: 13px;
      border-left: 3px solid;
      padding: 2px 1px 2px 4px;
      margin: 7px 6px 11px 32px;
   }

</style>