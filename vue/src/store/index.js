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

      // ksy
      commuteList: [],
      startDate: "",
      endDate: "",

      jobBxSelected: {
         searchDeptCd: '',
         searchDeptNm: '',
         searchDeptjobBxId: '',
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
      commuteSetList(state, data) {
         state.commuteList = data;
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
      async commuteGetList({ commit, state }) {
         const params = {
         mberId: "user01",
         startDate: state.startDate,
         endDate: state.endDate
         };
         let result = await axios.get(`/api/commute/cmtList`, { params });
         commit("commuteSetList", result.data);
      },

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
