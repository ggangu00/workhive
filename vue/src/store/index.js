import { createStore } from "vuex";

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

      // ksy
      isCmt: false,
      startDate: "",
      endDate: "",

      jobBxSelected: {
         searchDeptCd: '',
         searchDeptNm: '',
         searchDeptJobBxId: '',
         searchDeptJobBxNm: '',
      },
      jobBxList: []
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

      // ksy
      isCmtSet(state, value) {
         state.isCmt = value;
      },
      setStartDate(state, value) {
         state.startDate = value;
      },
      setEndDate(state, value) {
         state.endDate = value;
      },

      setJobBxSelected(state, payload) {
         state.jobBxSelected = payload;
      },
      setJobBxList(state, payload) {
         state.jobBxList = payload;
      }
   },
   actions: {
      setColor({ commit }, payload) {
         commit("color", payload);
      },

      // ksy
      jobBxSelectedUpdate({ commit }, jobBxSelected) {
         commit('setJobBxSelected', jobBxSelected);
      },
      jobBxListUpdate({ commit }, jobBxList) {
         commit('setJobBxList', jobBxList);
      }
   },
   getters: {
      jobBxSelected: state => state.jobBxSelected,
      jobBxList: state => state.jobBxList
   },
});
