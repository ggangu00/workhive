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
         await Swal.fire({
            icon: "error",
            title: "세션 만료",
            text: "로그인이 필요합니다."
         });
         router.push("/login");
      }
      return Promise.reject(error);
   }
);

export default instance;
