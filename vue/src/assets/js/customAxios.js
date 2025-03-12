import axios from 'axios';
import Swal from 'sweetalert2';
import router from '../../router';

const instance = axios.create({
   withCredentials: true
});

instance.interceptors.request.use(config => {
   const token = localStorage.getItem('token');

   if (token) {
      config.headers.Authorization = `Bearer ${token}`;
   }
   return config;
}, error => Promise.reject(error));

instance.interceptors.response.use(
   response => response,
   async error => {
      if (error.response?.status === 401) {
         const message = error.response?.data?.message || '인증이 필요합니다. 다시 로그인해주세요.';

         // 만료된 Access Token 삭제
         localStorage.removeItem('token');

         await Swal.fire({
            icon: "error",
            title: "로그인 필요",
            text: message
         }).then(() => {
            router.push("/login");
         });

      }
      return Promise.reject(error);
   }
);

export default instance;
