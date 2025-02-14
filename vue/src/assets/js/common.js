// 날짜포맷 (yyyy-mm-dd 형식)
const dateFormat = (value) => {
    let date = value == null ? new Date() : new Date(value);

    let year = date.getFullYear();
    let month = ('0' + (date.getMonth() + 1)).slice(-2);
    let day = ('0' + date.getDate()).slice(-2);

    let result = year + '-' + month + '-' + day;
    return result;
};

// 숫자포맷 (천단위 콤마)
const numberFormat = (value) => {
    if (!value) return '0';
    return Number(value).toLocaleString();
};

console.log(dateFormat("2025-02-11 00:00:00"));

export {
    dateFormat,
    numberFormat
}