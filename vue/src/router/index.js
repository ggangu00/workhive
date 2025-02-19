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
import PersonalManage from '../views/Setting/PersonalManage.vue';
import MemberManage from '../views/Setting/MemberManage.vue'
import DepartmentManage from "../views/Setting/DepartmentManage.vue";
import AuthorityManage from "../views/Setting/AuthorityManage.vue";
import SignIn from "../views/Login/SignIn.vue";




//skh컴포넌트
import ApprovalList from "../components/PaymentLayout/ApprovalList.vue";
import ApprovalLine from "../components/PaymentLayout/ApprovalLine.vue";
import ApprovalRegister from "../components/PaymentLayout/ApprovalRegister.vue";
//skh
import Test from "../views/Approval/test.vue";
import RegisterTest from "../views/Approval/RegisterTest.vue";
import CompletedList from "../views/Approval/CompletedList.vue";
import Register from "../views/Approval/Register.vue";
import Calendar from "../views/schedule/Calendar.vue";
import PendingList from "../views/Approval/PendingList.vue";
import ProceedList from "../views/Approval/ProceedList.vue";
import RejectedList from "../views/Approval/RejectedList.vue";
import RejectedInfo from "../views/Approval/RejectedInfo.vue";


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
import VcList from '../views/Vacation/VacationInfo.vue';
import VcManage from '../views/Vacation/VacationManage.vue';
import VcSignManage from '../views/Vacation/VacationSign.vue';
import CmtList from '../views/Commute/CommuteInfo.vue';
import CrctManage from '../views/Commute/CommuteCrctManage.vue';
import CrctList from '../views/Commute/CommuteCrctInfo.vue';
import CrctSignManage from '../views/Commute/CommuteCrctSign.vue';

// kjg
import PaymentList from '../views/Payment/PaymentList.vue';
import PaymentAdd from '../views/Payment/PaymentAdd.vue';
import PaymentManage from '../views/Payment/PaymentManage.vue';
import BoardList from '../views/Board/BoardList.vue';
import BoardAdd from '../views/Board/BoardAdd.vue';
import BoardUpdate from "../views/Board/BoardUpdate.vue";
import BulletinList from "../views/Bulletin/BulletinList.vue";
import BulletinAdd from "../views/Bulletin/BulletinAdd.vue";
import BulletinInfo from "../views/Bulletin/BulletinInfo.vue";



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
      path: '/signIn',
      name: 'SignIn',
      component: SignIn,
   },
   {  // 로그인
      path: '/login',
      name: 'Login',
      component: Login,
   },
   { // 개인정보 설정
      path: '/setting/personalInfo',
      name: 'PersonalManage',
      component: PersonalManage
   },
   { // 부서 구성원 관리
      path: '/setting/member/users',
      name: 'MemberManage',
      component: MemberManage
   },
   { // 부서 관리
      path: '/setting/department',
      name: 'DepartmentManage',
      component: DepartmentManage
   },
   { // 부서 관리
      path: '/setting/authority',
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
   //skh

   { //
      path: '/schdule/test',
      name: 'Test',
      component: Test
   },
   {
      path: '/registerTest',
      name: 'RegisterTest',
      component: RegisterTest
   },
   {
      path: '/schdule/calendar',
      name: 'Calendar',
      component: Calendar
   },
   { // 미결함
      path: '/approval/pendingList',
      name: 'PendingList',
      component : PendingList
   },
   { // 완료함
      path: '/approval/completedList',
      name: 'CompletedList',
      component : CompletedList
   },
   { // 진행함
      path: '/approval/proceedList',
      name: 'ProceedList',
      component : ProceedList
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
   { // 문서 기안
      path: '/approval/register',
      name: 'Register',
      component : Register
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
      path: '/vacation/vcList',
      name: 'VcList',
      component: VcList,
   },
   { // 휴가 신청 관리(등록, 수정 등)
      path: '/vacation/vcManage',
      name: 'VcManage',
      component: VcManage,
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
      path:'/board/boardList',
      name: 'BoardList',
      component : BoardList,
   },
   {//게시판 등록
      path:'/board/boardAdd',
      name: 'BoardAdd',
      component : BoardAdd,
   },
    {//게시판 수정
      path:'/board/boardUpdate',
      name: 'BoardUpdate',
      component : BoardUpdate,
   },
   {//게시글 목록
      path:'/bulletin/bulletinList',
      name: 'BulletinList',
      component : BulletinList,
   },
   {//게시글 등록
      path:'/bulletin/bulletinAdd',
      name: 'BulletinAdd',
      component : BulletinAdd,
   },
   {//게시글 상세조회
      path:'/bulletin/bulletinInfo',
      name: 'BulletinInfo',
      component : BulletinInfo,
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

export default router;
