import axios from "../../assets/js/customAxios";
import Swal from 'sweetalert2';

//공통함수
/**
 * @description 공통코드 상세 목록 조회
 * @param {string} code 공통코드 구분 값 (ex: 'DK', 'STATUS', 'GRADE' 등)
 * @returns {Promise<Array<{ commDtlCd: string, commDtlNm: string }>>} 공통코드 목록 배열
 */
export const getComm = async (code) => {
   try {
      const response = await axios.get('/api/comm/codeList', {
         params: { cd: code }
      });

      return response.data;
   } catch (err) {
      Swal.fire({
         icon: "error",
         title: "공통코드 조회 실패",
         text: `Error: ${err.response?.data?.error || err.message}`
      });
      return [];
   }
};

// export async function getComm(cd) { // comm_cd
//    let result = await axios.get(`/api/comm/codeList?cd=${cd}`)
//       .catch(err => console.log(err));
//    return result.data;
// };

// 날짜포맷 (년-월-일 형식)
export function dateFormat(value) {
   let date = value == null ? new Date() : new Date(value);

   let year = date.getFullYear();
   let month = ('0' + (date.getMonth() + 1)).slice(-2);
   let day = ('0' + date.getDate()).slice(-2);

   let result = year + '-' + month + '-' + day;
   return result;
};

// 날짜포맷 (년-월-일 시:분:초 형식)
export function dateTimeFormat(value, format) {
   let date = value == null ? null : new Date(value);

   if(date == null) return;

   let year = date.getFullYear();
   let month = ('0' + (date.getMonth() + 1)).slice(-2);
   let day = ('0' + date.getDate()).slice(-2);

   let hour = ('0' + date.getHours()).slice(-2);
   let min = ('0' + date.getMinutes()).slice(-2);
   let sec = ('0' + date.getSeconds()).slice(-2);

    let result = format.replace('yyyy', year)
                       .replace('MM', month)
                       .replace('dd', day)
                       .replace('hh', hour)
                       .replace('mm', min)
                       .replace('ss', sec);
    return result;
}

// 숫자포맷 (천단위 콤마)
export function numberFormat(num) {
   if (!num) return '0';
   return Number(num).toLocaleString();
};


// 수정 삭제 여부 체크
export const swalCheck = async (title) => {
   const result = Swal.fire({
      icon: 'question',
      title: '내역을 ' + title + ' 하시겠습니까?',
      showCancelButton: true,
      cancelButtonText: '취소',
      confirmButtonText: '확인',
      reverseButtons:true
   });
   return result;
}