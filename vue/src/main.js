import { createApp } from "vue";
import { createPinia } from "pinia"; // ✅ Pinia 인스턴스 생성
import App from "./App.vue";
import store from "./store";
import router from "./router";
import myPlugin from "./assets/js/commPlugin";
import VTooltip from "v-tooltip";

import LightBootstrap from "./light-bootstrap-main";

import PrimeVue from 'primevue/config';
import 'primevue/resources/themes/lara-light-blue/theme.css'
import 'primevue/resources/primevue.min.css'
import 'primeicons/primeicons.css'

import "./assets/css/nucleo-icons.css";
import "./assets/css/nucleo-svg.css";

const appInstance = createApp(App);
const pinia = createPinia(); // Pinia 인스턴스 생성

appInstance.use(PrimeVue)
appInstance.use(store);
appInstance.use(router);
appInstance.use(LightBootstrap);
appInstance.use(pinia); // pinia 사용
appInstance.use(myPlugin); // 커스텀 플러그인 사용 등록
appInstance.use(VTooltip);
appInstance.mount("#app");
