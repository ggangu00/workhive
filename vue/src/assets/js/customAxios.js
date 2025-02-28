// 공통 axios
/**
 *
 */
// 공통 axios 인스턴스 생성
import axios from 'axios';

const token = localStorage.getItem("token");

const instance = axios.create({
   headers: {'Authorization': `Bearer ${token}`}
});

// 요청 인터셉터 - 매 요청마다 토큰만 추가
// instance.interceptors.request.use(
//    (config) => {
//
//

//       if (token) {
//          config.headers['Authorization'] = `Bearer ${token}`;
//       }

//       return config;
//    },
//    (error) => Promise.reject(error)
// );

export default instance;