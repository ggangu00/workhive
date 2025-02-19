import axios from "axios";

//공통함수
export async function getComm(cd) { // comm_cd
   let result = await axios.get(`/api/comm/codeList?cd=${cd}`)
      .catch(err => console.log(err));
   return result.data;
};

// 날짜포맷 (yyyy-mm-dd 형식)
export function dateFormat(value) {
   let date = value == null ? new Date() : new Date(value);

   let year = date.getFullYear();
   let month = ('0' + (date.getMonth() + 1)).slice(-2);
   let day = ('0' + date.getDate()).slice(-2);

   let result = year + '-' + month + '-' + day;
   return result;
};

export function dateTimeFormat(value) {
   let date = value == null ? new Date() : new Date(value);

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

// 날짜차이 계산
export function dateTermCalc(date) {
    const endDate = new Date(date);
    const today = new Date();

    const diff = endDate - today;

    let diffDay = Math.floor(diff / (1000 * 60 * 60 * 24)) + 1;

    if (diffDay == 0) {
        diffDay = "-day";
    } else if (diffDay < 0) {
        diffDay = "+" + (diffDay * (-1));
    } else {
        diffDay = "-" + diffDay;
    }

    return diffDay;
}

//날짜 요일계산
export function dateGetDay(dateString){
    const days = ['일', '월', '화', '수', '목', '금', '토'];
    const date = new Date(dateString);
    return days[date.getDay()]; // 0: 일요일, 1: 월요일, ...
};
