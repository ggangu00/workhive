import { createRouter, createWebHistory } from "vue-router";
import Dashboard from "../views/Dashboard.vue";
import Tables from "../views/Tables.vue";
import Billing from "../views/Billing.vue";
import Notifications from "../views/Notifications.vue";
import Profile from "../views/Profile.vue";
import SignIn from "../views/SignIn.vue";
import SignUp from "../views/SignUp.vue";
import Guide from "../views/Guide.vue";

import PersonalInfo from '../views/Setting/PersonalInfo.vue'
import Layout from '../views/Layout.vue';

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
import BoardList from '../views/Board/BoardList.vue';
import BoardAdd from '../views/Board/BoardAdd.vue';
import BulletinList from "../views/Bulletin/BulletinList.vue";
import BulletinAdd from "../views/Bulletin/BulletinAdd.vue";


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
  {
    path: '/guide',
    name: 'Guide',
    component: Guide
  },
  {
    path: '/setting/personalInfo',
    name: 'PersonalInfo',
    component: PersonalInfo
  },
  {
    path: '/layout',
    name: 'Layout',
    component: Layout
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
  {
    path: '/schdule/test',
    name: 'Test',
    component: Test
  },
  {
    path: '/schdule/calendar',
    name: 'Calendar',
    component: Calendar
  },
  {
    path: '/approval/completed',
    name: 'Completed',
    component : Completed
  },
  {
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
    path:'/board/boardList',
    name: 'BoardList',
    component : BoardList,
  },
  {
    path:'/board/boardAdd',
    name: 'BoardAdd',
    component : BoardAdd,
  },
  {
    path:'/bulletin/bulletinList',
    name: 'BulletinList',
    component : BulletinList,
  },
  {
    path:'/bulletin/bulletinAdd',
    name: 'BulletinAdd',
    component : BulletinAdd,
  },


  //pjh
  {
    path: '/project/add',
    name: 'ProjectAdd',
    component: ProjectAdd
  }, {
    path: '/project/list',
    name: 'ProjectList',
    component: ProjectList
  }, {
    path: '/project/plan',
    name: 'ProjectPlan',
    component: ProjectPlan
  }, {
    path: '/meet/add',
    name: 'MeetAdd',
    component: MeetAdd
  }, {
    path: '/meet/list',
    name: 'MeetList',
    component: MeetList
  }, {
    path: '/setting/LoginLog',
    name: 'LoginLog',
    component: LoginLog
  }, {
    path: '/todo/list',
    name: 'Todo',
    component: Todo
  }, {
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
