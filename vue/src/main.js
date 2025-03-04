import { createApp } from "vue";
import { createPinia } from "pinia"; // ✅ Pinia 인스턴스 생성
import App from "./App.vue";
import store from "./store";
import router from "./router";
import "./assets/css/nucleo-icons.css";
import "./assets/css/nucleo-svg.css";
import myPlugin from "./assets/js/commPlugin";
//import pinia from "./store/userStore"
import VTooltip from "v-tooltip"

import LightBootstrap from "./light-bootstrap-main";

const appInstance = createApp(App);
const pinia = createPinia(); // ✅ Pinia 인스턴스 생성

appInstance.use(store);
appInstance.use(router);
appInstance.use(LightBootstrap);
appInstance.use(pinia); // pinia 사용
appInstance.use(myPlugin); // 커스텀 플러그인 사용 등록
appInstance.use(VTooltip);
appInstance.mount("#app");
