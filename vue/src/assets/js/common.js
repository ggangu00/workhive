import axios from "axios";

//공통함수 
export async function getComm(cd){ // comm_cd
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

// 숫자포맷 (천단위 콤마)
export function numberFormat(value) {
    if (!value) return '0';
    return Number(value).toLocaleString();
};