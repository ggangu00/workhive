import { useUserInfoStore } from "../store/userStore"; // Pinia Store 가져오기
import axios from "../assets/js/customAxios.js"
import Swal from 'sweetalert2';

import { createRouter, createWebHistory } from "vue-router";
import Dashboard from "../views/Dashboard.vue";
import Tables from "../views/Tables.vue";
import Billing from "../views/Billing.vue";
import Notifications from "../views/Notifications.vue";
import Profile from "../views/Profile.vue";
import Guide from "../views/Guide.vue";
import Layout from '../views/Layout.vue';

// kmj
import Login from "../views/Login/Login.vue";
import FindPw from '../views/Login/findPassword.vue';
import PersonalManage from '../views/Setting/PersonalManage.vue';
import MemberManage from '../views/Setting/MemberManage.vue'
import DepartmentManage from "../views/Setting/DepartmentManage.vue";
import AuthorityManage from "../views/Setting/AuthorityManage.vue";





//skh컴포넌트
import ApprovalList from "../components/PaymentLayout/ApprovalList.vue";
import ApprovalLine from "../components/PaymentLayout/ApprovalLine.vue";
import ApprovalRegister from "../components/PaymentLayout/ApprovalRegister.vue";
import ApprovalInfo from "../components/PaymentLayout/ApprovalInfo.vue";
import PendingListComp from "../components/PaymentLayout/PendingListComp.vue";
//skh
import RegisterTest from "../views/Approval/RegisterTest.vue";
import CompletedList from "../views/Approval/CompletedList.vue";
import Calendar from "../views/schedule/Calendar.vue";
import PendingList from "../views/Approval/PendingList copy.vue";
import ProceedList from "../views/Approval/ProceedList.vue";
import RejectedList from "../views/Approval/RejectedList.vue";
import RejectedInfo from "../views/Approval/RejectedInfo.vue";
import RestartDraft from "../views/Approval/RestartDraft.vue";
import CompletedInfo from "../views/Approval/CompletedInfo.vue";
import PendingInfo from "../views/Approval/PendingInfo.vue";
import ProceedInfo from "../views/Approval/ProceedInfo.vue";
import RetrieveList from "../views/Approval/RetrieveList.vue";
import ReceivedList from "../views/Approval/ReceivedList.vue";

// pjh
import Todo from '../views/Todo/TodoManage.vue'
import ProjectAdd from '../views/Project/ProjectAdd.vue'
import ProjectList from '../views/Project/ProjectList.vue'
import ProjectPlan from '../views/Project/ProjectPlan.vue'
import ProjectMem from '../views/Project/ProjectMem.vue'
import MeetAdd from '../views/Meet/MeetAdd.vue'
import MeetList from '../views/Meet/MeetList.vue'
import LoginLog from '../views/Setting/LoginLog.vue'
import Home from '../views/Home.vue'



// ksy
import KsyTest from '../views/DeptStore/Test.vue';
import DeptjobInfo from '../views/DeptStore/DeptJobInfo.vue';
import Vacation from '../views/Vacation/Vacation.vue';
import VcList from "../views/Vacation/VacationList.vue";
import VcManage from "../views/Vacation/VacationManage.vue";
import VcSignManage from '../views/Vacation/VacationSign.vue';
import CmtList from '../views/Commute/CommuteList.vue';
import CrctManage from '../views/Commute/CommuteCrctManage.vue';
import CrctList from '../views/Commute/CommuteCrctList.vue';
import CrctSignManage from '../views/Commute/CommuteCrctSign.vue';

// kjg
import PaymentList from '../views/Payment/PaymentList.vue';
import PaymentAdd from '../views/Payment/PaymentAdd.vue';
import PaymentManage from '../views/Payment/PaymentManage.vue';
import BoardList from '../views/Board/BoardList.vue';
import BoardAdd from '../views/Board/BoardAdd.vue';
import BoardModify from "../views/Board/BoardModify.vue";
import BulletinList from "../views/Bulletin/BulletinList.vue";
import BulletinAdd from "../views/Bulletin/BulletinAdd.vue";
import BulletinInfo from "../views/Bulletin/BulletinInfo.vue";
import BulletinModify from "../views/Bulletin/BulletinModify.vue";
import SecretAt from "../views/Bulletin/SecretAt.vue"




const routes = [
   {
      path: "/",
      name: "/",
      redirect: "/home",
   },
   {
      path: "/dashboard",
      name: "Dashboard",
      component: Dashboard,
   },
   {
      path: "/tables",
      name: "Tables",
      component: Tables,
   },
   {
      path: "/billing",
      name: "Billing",
      component: Billing,
   },
   {
      path: "/notifications",
      name: "Notifications",
      component: Notifications,
   },
   {
      path: "/profile",
      name: "Profile",
      component: Profile,
   },
   { // ui가이드
      path: '/guide',
      name: 'Guide',
      component: Guide
   },
   { // layout틀
      path: '/layout',
      name: 'Layout',
      component: Layout
   },

   // kmj
   {  // 로그인
      path: '/login',
      name: 'Login',
      component: Login,
   },
   {  // 패스워드 찾기
      path: '/findPw',
      name: 'FindPw',
      component: FindPw,
   },
   { // 개인정보 설정
      path: '/personal/manage',
      name: 'PersonalManage',
      component: PersonalManage
   },
   { // 부서 구성원 관리
      path: '/setting/organization/member',
      name: 'MemberManage',
      component: MemberManage
   },
   { // 부서 관리
      path: '/setting/organization/department',
      name: 'DepartmentManage',
      component: DepartmentManage
   },
   { // 권한 관리
      path: '/setting/organization/authority',
      name: 'AuthorityManage',
      component: AuthorityManage
   },






   //skh컴포넌트
   {
      path: '/approvalList',
      name: 'ApprovalList',
      component: ApprovalList
   },
   {
      path: '/approvalLine',
      name: 'ApprovalLine',
      component: ApprovalLine
   },
   {
      path: '/approvalRegister',
      name: 'ApprovalRegister',
      component: ApprovalRegister
   },
   {
      path: '/approvalInfo',
      name: 'ApprovalInfo',
      component: ApprovalInfo
   },
   {
      path: '/pendingListComp',
      name: 'PendingListComp',
      component: PendingListComp
   },
   //skh

   { //
      path: '/approval/restartDraft',
      name: 'RestartDraft',
      component: RestartDraft
   },
   {
      path: '/approval/registerTest',
      name: 'RegisterTest',
      component: RegisterTest
   },
   { //일정
      path: '/schdule/manage',
      name: 'Calendar',
      component: Calendar
   },
   { // 미결함
      path: '/approval/pendingList',
      name: 'PendingList',
      component : PendingList
   },
   { //미결문서 상세보기
      path: '/approval/pendingInfo',
      name: 'PendingInfo',
      component : PendingInfo
   },
   { // 완료함
      path: '/approval/completedList',
      name: 'CompletedList',
      component : CompletedList
   },
   {//완료문서 상세보기
      path: '/approval/completedInfo',
      name: 'CompletedInfo',
      component : CompletedInfo
   },
   { // 진행함
      path: '/approval/proceedList',
      name: 'ProceedList',
      component : ProceedList
   },
   { // 진행문서 상세보기
      path: '/approval/proceedInfo',
      name: 'ProceedInfo',
      component : ProceedInfo
   },
   { // 반려함
      path: '/approval/rejectedList',
      name: 'RejectedList',
      component : RejectedList
   },
   {//반려문서 상세보기
      path: '/approval/rejectedInfo',
      name: 'RejectedInfo',
      component:RejectedInfo
   },
   {//임시함
      path: '/approval/retrieveList',
      name: 'RetrieveList',
      component : RetrieveList
   },
   {//수신함
      path: '/approval/receivedList',
      name: 'ReceivedList',
      component : ReceivedList
   },


   //ksy
   { // 테스트
      path: '/deptstore/test',
      name: 'KsyTest',
      component: KsyTest,
   },
   { // 부서 업무 관리
      path: '/deptstore/list',
      name: 'DeptjobInfo',
      component: DeptjobInfo,
   },
   { // 출퇴근 기록 조회
      path: '/commute/cmtList',
      name: 'CmtList',
      component: CmtList,
   },
   { // 출퇴근 정정 요청 관리
      path: '/commute/crctManage',
      name: 'CrctManage',
      component: CrctManage,
   },
   { // 출퇴근 정정 요청 조회
      path: '/commute/crctList',
      name: 'CrctList',
      component: CrctList,
   },
   { // 출퇴근 정정 요청 결재
      path: '/commute/crctSignManage',
      name: 'CrctSignManage',
      component: CrctSignManage,
   },
   { // 휴가 조회
      path: '/vacation',
      name: 'Vacation',
      component: Vacation,
      children: [
         {
            path: 'vcList', // 기본 페이지로 'VacationList'를 표시
            name: 'VcList',
            component: VcList,
         },
         {
            path: 'vcManage', // 휴가 관리 페이지
            name: 'VcManage',
            component: VcManage,
         },
      ],
   },
   { // 휴가 신청 결재
      path: '/vacation/vcSignManage',
      name: 'VcSignManage',
      component: VcSignManage,
   },

   //kjg
      {//외부지출 조회
      path:'/payment/paymentList',
      name: 'PaymentList',
      component : PaymentList,
   },
   {//외부지출 등록
      path:'/payment/paymentAdd',
      name: 'PaymentAdd',
      component : PaymentAdd,
   },
      {//외부지출 승인/반려 관리
         path:'/payment/paymentManage',
         name: 'PaymentManage',
         component : PaymentManage,
      },

   {//게시판 목록
      path:'/setting/board/boardList',
      name: 'BoardList',
      component : BoardList,
   },
   {//게시판 등록
      path:'/setting/board/boardAdd',
      name: 'BoardAdd',
      component : BoardAdd,
   },
   {//게시판 수정
      path:'/setting/board/boardModify',
      name: 'BoardModify',
      component : BoardModify,
   },
   {//게시글 목록
      path:'/bulletin/bulletinList/:bbsId',
      name: 'BulletinList',
      component : BulletinList,
   },
   {//게시글 등록
      path:'/bulletin/bulletinAdd',
      name: 'BulletinAdd',
      component : BulletinAdd,
   },
   {//게시글 상세조회
      path:'/bulletin/bulletinInfo/:bbsId/:nttId',
      name: 'BulletinInfo',
      component : BulletinInfo,
   },
    {//게시글 수정
      path:'/bulletin/bulletinModify/:bbsId/:nttId',
      name: 'BulletinModify',
      component : BulletinModify,
   },
      {//게시글 수정
         path:'/bulletin/secretAt',
         name: 'SecretAt',
         component : SecretAt,
      },







   //pjh
   { // 프로젝트 등록
      path: '/project/add',
      name: 'ProjectAdd',
      component: ProjectAdd
   },
   {
      path: '/project/list',
      name: 'ProjectList',
      component: ProjectList
   },
   {
      path: '/project/plan',
      name: 'ProjectPlan',
      component: ProjectPlan
   },
   {
      path: '/project/mem',
      name: 'ProjectMem',
      component: ProjectMem
   },
   {
      path: '/meet/add',
      name: 'MeetAdd',
      component: MeetAdd
   },
   {
      path: '/meet/list',
      name: 'MeetList',
      component: MeetList
   },
   {
      path: '/setting/loginLog',
      name: 'LoginLog',
      component: LoginLog
   },
   {
      path: '/todo/manage',
      name: 'Todo',
      component: Todo
   },
   {
      path: '/home',
      name: 'Home',
      component: Home
   },
];

const router = createRouter({
   history: createWebHistory(process.env.BASE_URL),
   routes,
   linkActiveClass: "active",
});

// const authorityGetList = async () => {
//    try {
//       const result = await axios.get('/api/authority');
//       console.log("접근 권한 체크 => ", result.data);

//    } catch (err) {
//       Swal.fire({
//          icon: "error",
//          title: "API 조회 오류",
//          text:  "Error : " + err.response.data.error
//       });
//    }
// };

// **전역 네비게이션 가드 설정**
// router.beforeEach((to, from, next) => {
//    //authorityGetList();

//    const store = useUserInfoStore(); // pinia 정보

//    // 로그인 없이 접근 가능한 페이지 목록
//    const publicPages = ["/login", "/findPw"];

//    // 현재 이동하려는 페이지가 로그인 필요한 페이지인지 확인
//    const authRequired = !publicPages.includes(to.path);

//    if (authRequired && !store.isAuthenticated) {
//       next("/login"); // 로그인 안 된 경우 로그인 페이지로 이동
//    } else {
//       // 이 부분에 권한체크가 들어와야해
//    }
// });

router.beforeEach(async (to, from, next) => {
   const store = useUserInfoStore();

   // 로그인 없이 접근 가능한 페이지 목록
   const publicPages = ["/login", "/findPw", "/home"];

   // 현재 이동하려는 페이지가 로그인 필요한 페이지인지 확인
   const authRequired = !publicPages.includes(to.path);

   // 로그인 안 되어있으면 무조건 로그인 페이지로
   if (authRequired && !store.isAuthenticated) {
      return next("/login");
   }

   // ✅ 로그인한 경우, 권한 체크 수행 (publicPages가 아닌 경우)
   if (authRequired) {
      const menuCd = to.query.menuCd;

      if (!menuCd) {
         console.warn("메뉴코드가 없습니다. 권한체크 없이 이동합니다.");
         return next();   // 메뉴코드 없으면 권한체크 패스하고 그냥 이동
      }

      try {
         // 메뉴코드 있는 경우만 권한 체크
         const response = await axios.get(`/api/access/${menuCd}`, {
            withCredentials: true
         });

         if (response.status === 200 && response.data === true) {
            return next(); // ✅ 권한 있음 → 정상 이동
         } else {
            Swal.fire({
               icon: "warning",
               title: "접근 불가",
               text: "해당 메뉴에 접근할 권한이 없습니다."
            });
            return next(false); // 🚫 권한 없으면 이동 차단
         }
      } catch (error) {
         if (error.response?.status === 401) {
            return next("/login");
         } else {
            Swal.fire({
               icon: "error",
               title: "오류 발생",
               text: "서버와의 통신 중 문제가 발생했습니다."
            });
            return next(false);
         }
      }
   } else {
      next(); // publicPages는 그냥 통과
   }
});



export default router;
