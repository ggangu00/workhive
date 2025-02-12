import { createRouter, createWebHistory } from "vue-router";
import Dashboard from "../views/Dashboard.vue";
import Tables from "../views/Tables.vue";
import Billing from "../views/Billing.vue";
import Notifications from "../views/Notifications.vue";
import Profile from "../views/Profile.vue";
import SignIn from "../views/SignIn.vue";
import SignUp from "../views/SignUp.vue";
import Guide from "../views/Guide.vue";
import Layout from '../views/Layout.vue';

// kmj
import PersonalManage from '../views/Setting/PersonalManage.vue';
import MemberManage from '../views/Setting/MemberManage.vue'








//skh컴포넌트
import ApprovalList from "../components/SkhComponents/ApprovalList.vue";
import ApprovalLine from "../views/Approval/ApprovalLine.vue";
//skh
import Test from "../views/Approval/test.vue";
import Completed from "../views/Approval/Completed.vue";
import Register from "../views/Approval/Register.vue";
import Calendar from "../views/schedule/Calendar.vue";


// pjh
import Todo from '../views/Todo/TodoList.vue'
import ProjectAdd from '../views/Project/ProjectAdd.vue'
import ProjectList from '../views/Project/ProjectList.vue'
import ProjectPlan from '../views/Project/ProjectPlan.vue'
import MeetAdd from '../views/Meet/MeetAdd.vue'
import MeetList from '../views/Meet/MeetList.vue'
import LoginLog from '../views/Setting/LoginLog.vue'
import Home from '../views/Home.vue'



// ksy
import DeptjobInfo from '../views/DeptStore/DeptJobInfo.vue';
import KsyTest from '../views/DeptStore/Test.vue';
import VacationInfo from '../views/Vacation/VacationInfo.vue';
import VacationManage from '../views/Vacation/VacationManage.vue';
import CommuteInfo from '../views/Commute/CommuteInfo.vue';

// kjg
import BbsAdd from '../views/bbs/bbsadd.vue';
import BbsList from '../views/bbs/bbslist.vue';



const routes = [
  {
    path: "/",
    name: "/",
    redirect: "/dashboard",
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
  {
    path: "/sign-in",
    name: "SignIn",
    component: SignIn,
  },
  {
    path: "/sign-up",
    name: "SignUp",
    component: SignUp,
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
  { // 개인정보 설정
    path: '/setting/personalInfo',
    name: 'PersonalManage',
    component: PersonalManage
  },
  { // 
    path: '/setting/member/users',
    name: 'MemberManage',
    component: MemberManage
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
  //skh
  { // 
    path: '/schdule/test',
    name: 'Test',
    component: Test
  },
  { // 일정관리
    path: '/schdule/calendar',
    name: 'Calendar',
    component: Calendar
  },
  { // 완료함
    path: '/approval/completed',
    name: 'Completed',
    component : Completed
  },
  { // 문서 기안
    path: '/approval/register',
    name: 'Register',
    component : Register
  },

  //ksy
  {
    path: '/deptStore/deptJobInfo',
    name: 'DeptjobInfo',
    component: DeptjobInfo,
  },
  {
    path: '/deptStore/Test',
    name: 'KsyTest',
    component: KsyTest,
  },
  {
    path: '/vacation/vacationInfo',
    name: 'VacationInfo',
    component: VacationInfo,
  },
  {
    path: '/vacation/vacationManage',
    name: 'VacationManage',
    component: VacationManage,
  },
  {
    path: '/commute/commuteInfo',
    name: 'CommuteInfo',
    component: CommuteInfo,
  },

  //kjg
  {
    path:'/bbs/bbsList',
    name: 'BbsList',
    component : BbsList,
  },
  {
    path:'/bbs/bbsAdd',
    name: 'BbsAdd',
    component : BbsAdd,
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
    path: '/setting/LoginLog',
    name: 'LoginLog',
    component: LoginLog
  }, 
  {
    path: '/todo/list',
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
