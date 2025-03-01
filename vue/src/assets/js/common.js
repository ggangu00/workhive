import axios from "../../assets/js/customAxios";


//공통함수
export async function getComm(cd) { // comm_cd
   let result = await axios.get(`/api/comm/codeList?cd=${cd}`)
      .catch(err => console.log(err));
   return result.data;
};

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


