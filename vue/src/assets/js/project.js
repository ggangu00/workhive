//월-일 요일 출력 (프로젝트 일정)
export function dateFormatDay(dateString) {
    const day = dateString.substr(5, 10) + " (" + dateGetDay(dateString) + ")";
    return day;
};

//날짜 요일계산
export function dateGetDay(dateString) {
    const days = ['일', '월', '화', '수', '목', '금', '토'];
    const date = new Date(dateString);
    return days[date.getDay()]; // 0: 일요일, 1: 월요일, ...
};

// 날짜차이 계산
export function dateTermCalc(startDt, endDt) {
    let endDate = new Date(endDt);
    let today = new Date();

    if (startDt != '') today = new Date(startDt);


    const diff = endDate - today;

    let diffDay = Math.floor(diff / (1000 * 60 * 60 * 24)) + 0;

    return diffDay;
}