
// 공통 axios 인스턴스 생성
import axios from "axios";
import Swal from "sweetalert2";
import router from "../../router";

const instance = axios.create({
   withCredentials: true  // 쿠키 기반 세션이나 CORS 대응할 때 필요
});

// 요청 인터셉터 - 매 요청마다 토큰 자동 부착
instance.interceptors.request.use(config => {
   const token = localStorage.getItem("token");
   if (token) {
      config.headers.Authorization = `Bearer ${token}`;
   }
   return config;
}, error => {
   return Promise.reject(error);
});

instance.interceptors.response.use(
   response => {
      return response;  // 정상 응답은 그대로 반환
   }, error => {
      // 에러 응답 처리 (401, 403 감지)
      if (error.response) {
         const { status } = error.response;

         if (status === 401) {
            // 인증 실패 (로그인 필요, 세션 만료 등)
            Swal.fire({
               icon: "error",
               title: "세션 만료",
               text: "로그인이 필요합니다. 다시 로그인해주세요."
            }).then(() => {
               localStorage.removeItem("token");

               if (router.currentRoute.value.path !== "/login") {
                  router.push("/login");
               }
            });

         } else if (status === 403) {
            // 권한 없음
            Swal.fire({
               icon: "warning",
               title: "접근 권한 없음",
               text: "메뉴에 대한 접근 권한이 없습니다."
            });
         }
      } else {

         // 응답 자체가 없는 네트워크 에러 등
         Swal.fire({
            icon: "error",
            title: "네트워크 오류",
            text: "서버와의 통신에 실패했습니다. 네트워크 상태를 확인해주세요."
         });
      }
      return Promise.reject(error);  // 에러를 호출한 곳에서도 핸들링할 수 있게 넘겨줌
   }
);

export default instance;