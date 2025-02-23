<template>
   <div class="w-auto h-auto collapse navbar-collapse max-height-vh-100 h-100" id="sidenav-collapse-main">
      <ul class="navbar-nav">
         <li class="nav-item" v-for="(item, i) in menus" :key="i">
            <sidenav-collapse
               url="#"
               :aria-controls="''"
               :collapse="false"
               :collapseRef="item.collapseRef"
               :navText="item.navText"
            >
               <template v-slot:icon>
                  <i :class="item.iconClass"></i>
               </template>

               <template v-if="item.subMenus" v-slot:list>
                  <li
                     v-for="sub in item.subMenus"
                     :key="sub.name"
                     class="sub-item"
                     @click="movePage(sub.route)"
                  >
                     {{ sub.label }}
                  </li>
               </template>
            </sidenav-collapse>
         </li>
      </ul>
   </div>
</template>

<script setup>
   import { ref, onMounted } from "vue";
   import { useRouter } from "vue-router";
   import SidenavCollapse from "./SidenavCollapse.vue";
   import axios from "axios";
   import Swal from 'sweetalert2';

   const router = useRouter();

// ============================================= Axios Event =============================================
   // id, pass값 서버로 보내기
   const menuGetList = async () => {
      try {
         const response = await axios.get('/api/menu');

         menuBuildTree(response.data);

      } catch (err) {
         Swal.fire({
            icon: "error",
            title: "API 조회 오류",
            text:  "Error : " + err
         });
      }
   }
   //const menus = ref([]);
   // 메뉴 구조 생성
   const menuBuildTree = (menuArr) => {
      let mainMenus = new Map();
      let subMenus = new Map();
      let subSubMenus = new Map();
      menuArr.forEach((item, idx) => {
         console.log("item => ", item, "idx => ", idx);
         if(item.depth === 1) {
            console.log(item , "은 최상위 메뉴")
         }
      });
   }

   let menus = ref([
      {
         collapseRef: "/login",
         navText: "Login",
         iconClass: "material-icons-round"
      },
      {
         collapseRef: "/guide",
         navText: "레이아웃",
         iconClass: "material-icons-round opacity-10 fs-5"
      },
      {
         collapseRef: "/layout",
         navText: "레이아웃틀",
         iconClass: "material-icons-round opacity-10 fs-5"
      },
      {
         collapseRef: "/home",
         navText: "Home",
         iconClass: "fa-solid fa-house"
      },
      {
         collapseRef: "/todo/manage",
         navText: "업무일지 관리",
         iconClass: "fa-solid fa-list-ol",
         subMenus: [
            { name: "Todo", route: "Todo", label: "업무일지 관리" }
         ],
      },
      {
         collapseRef: "/project/list",
         navText: "프로젝트 관리",
         iconClass: "fa-solid fa-chart-line",
         subMenus: [
            { name: "ProjectList", route: "ProjectList", label: "프로젝트 조회" },
            { name: "ProjectAdd", route: "ProjectAdd", label: "프로젝트 등록" },
            { name: "ProjectMem", route: "ProjectMem", label: "프로젝트 참여자관리" },
         ],
      },
      {
         collapseRef: "/meet/list",
         navText: "회의 관리",
         iconClass: "fa-solid fa-users-line",
         subMenus: [
            { name: "MeetList", route: "MeetList", label: "회의 조회" },
            { name: "MeetAdd", route: "MeetAdd", label: "회의 등록" },
         ],
      },
      {
         collapseRef: "/approval/",
         navText: "전자결재",
         iconClass: "fa-solid fa-file-signature",
         subMenus: [
            { name: "RegisterTest", route: "RegisterTest", label: "문서기안" },
            { name: "PendingList", route: "PendingList", label: "미결함" },
            { name: "ProceedList", route: "ProceedList", label: "진행함" },
            { name: "CompletedList", route: "CompletedList", label: "완료함" },
            { name: "RejectedList", route: "RejectedList", label: "반려함" },
            { name: "RetrieveList", route: "RetrieveList", label: "임시함" },
         ],
      },
      {
         collapseRef: "/schedule/calendar",
         navText: "일정관리",
         iconClass: "fa-regular fa-calendar-check",
         subMenus: [
            { name: "Calendar", route: "Calendar", label: "풀캘린더" }
         ],
      },
      {
         collapseRef: "/deptstore/list",
         navText: "부서 업무 관리",
         iconClass: "fa-solid fa-folder-closed",
      },
      {
         collapseRef: "/commute/cmtList",
         navText: "출퇴근 관리",
         iconClass: "fa-solid fa-person-walking",
         subMenus: [
            { name: "CmtList", route: "CmtList", label: "출퇴근 기록 조회" },
            { name: "CrctManage", route: "CrctManage", label: "출퇴근 정정 요청 등록" },
            { name: "CrctList", route: "CrctList", label: "출퇴근 정정 요청 조회" },
            { name: "CrctSignManage", route: "CrctSignManage", label: "출퇴근 정정 요청 결재" },
         ],
      },
      {
         collapseRef: "/vacation/vcList",
         navText: "휴가 관리",
         iconClass: "fa-solid fa-plane",
         subMenus: [
            { name: "VcList", route: "VcList", label: "휴가 조회" },
            { name: "VcManage", route: "VcManage", label: "휴가 신청 등록" },
            { name: "VcSignManage", route: "VcSignManage", label: "휴가 신청 결재" },
         ],
      },
      {
         collapseRef: "/PaymentList",
         navText: "외부지출 관리",
         iconClass: "fa-solid fa-file-invoice-dollar",
         subMenus: [
            { name: "PaymentList", route: "PaymentList", label: "외부지출 조회" },
            { name: "PaymentAdd", route: "PaymentAdd", label: "외부지출 등록" },
            { name: "PaymentManage", route: "PaymentManage", label: "외부지출 승인/반려 관리" },
         ],
      },
      {
         collapseRef: "/BoardList",
         navText: "게시판 관리",
         iconClass: "",
         subMenus: [
            { name: "BoardList", route: "BoardList", label: "게시판 목록" },
            { name: "BoardAdd", route: "BoardAdd", label: "게시판 등록" },
            { name: "BoardUpdate", route: "BoardUpdate", label: "게시판 수정" },
         ],
      },
      {
         collapseRef: "/BulletinList",
         navText: "게시글 관리",
         iconClass: "fa-solid fa-pen-to-square",
         subMenus: [
            { name: "BulletinList", route: "BulletinList", label: "게시글 목록" },
            { name: "BulletinAdd", route: "BulletinAdd", label: "게시글 등록" },
            { name: "BulletinInfo", route: "BulletinInfo", label: "게시글 상세조회" },
         ],
      },
      {
         collapseRef: "/personal/setting",
         navText: "개인정보 설정",
         iconClass: "fa-solid fa-user",
      },
      {
         collapseRef: "/setting/personalInfo",
         navText: "관리자",
         iconClass: "fa-solid fa-gears",
         subMenus: [
            { name: "PersonalManage", route: "PersonalManage", label: "개인정보 설정" },
            { name: "DepartmentManage", route: "DepartmentManage", label: "부서 관리" },
            { name: "MemberManage", route: "MemberManage", label: "구성원" },
            { name: "AuthorityManage", route: "AuthorityManage", label: "권한관리" },
            { name: "LoginLog", route: "LoginLog", label: "접속기록 조회" },
         ],
      },
   ]);

   const movePage = (page) => {
      router.push({ name: page });
   };

   onMounted(() => {
      menuGetList(); // 메뉴 목록 가져오기
   });
</script>

<style scoped>
   .sub-item {
      color: #cfcfcf;
      font-size: 12px;
      padding: 3px 1px 2px 4px;
      margin: 10px 5px 10px 35px;
   }

   li {
      cursor: pointer;
   }
</style>
