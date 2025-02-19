import { createStore } from "vuex";
import axios from "axios";

export default createStore({
   state: {
      hideConfigButton: false,
      isPinned: true,
      showConfig: false,
      sidebarType: "bg-gradient-main",
      isRTL: false,
      color: "success",
      isNavFixed: false,
      isAbsolute: false,
      showNavs: true,
      showSidenav: true,
      showNavbar: true,
      showFooter: true,
      showMain: true,
      isDarkMode: false,
      navbarFixed:
         "position-sticky blur shadow-blur left-auto top-1 z-index-sticky px-0 mx-4",
      absolute: "position-absolute px-4 mx-0 w-100 z-index-2",

      // ksy 출퇴근 데이터
      commuteList: [],
      startDate: "",
      endDate: "",
   },
   mutations: {
      toggleConfigurator(state) {
         state.showConfig = !state.showConfig;
      },
      navbarMinimize(state) {
         const sidenav_show = document.querySelector(".g-sidenav-show");

         if (sidenav_show.classList.contains("g-sidenav-pinned")) {
         sidenav_show.classList.remove("g-sidenav-pinned");
         state.isPinned = true;
         } else {
         sidenav_show.classList.add("g-sidenav-pinned");
         state.isPinned = false;
         }
      },
      navbarFixed(state) {
         if (state.isNavFixed === false) {
         state.isNavFixed = true;
         } else {
         state.isNavFixed = false;
         }
      },
      toggleEveryDisplay(state) {
         state.showNavbar = !state.showNavbar;
         state.showSidenav = !state.showSidenav;
         state.showFooter = !state.showFooter;
      },
      toggleHideConfig(state) {
         state.hideConfigButton = !state.hideConfigButton;
      },
      color(state, payload) {
         state.color = payload;
      },

      // ksy 출퇴근 관리
      commuteSetList(state, data) {
         state.commuteList = data;
      },
      setStartDate(state, value) {
         state.startDate = value;
      },
      setEndDate(state, value) {
         state.endDate = value;
      },
   },
   actions: {
      setColor({ commit }, payload) {
         commit("color", payload);
      },

      async commuteGetList({ commit, state }) {
         const params = {
         memCd: "user01",
         startDate: state.startDate,
         endDate: state.endDate
         };
         let result = await axios.get(`/api/commute/cmtList`, { params });
         commit("commuteSetList", result.data);
      },
   },
   getters: {},
});
