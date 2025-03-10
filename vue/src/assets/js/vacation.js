import axios from "../../assets/js/customAxios";
import { dateTimeFormat } from "./common";
import Swal from 'sweetalert2';

// 연차 조회
export async function yearVcGetInfo(targetYear) {
  let searchData = {
    targetYear: targetYear,
  }

  let result;
  try {
    result = await axios.get('/api/yearVc/yearVcInfo', { params : searchData });
  } catch (err) {
    Swal.fire({ icon: "error", title: "연차 정보 조회 실패", text: "Error : " + err });
  }
  return result.data;
}

// 연차 생성
export async function yearVcAdd(targetYear) {
  let formData = new FormData();
  formData.append("targetYear", targetYear);
  formData.append("giveDays", 15);
  formData.append("useDays", 0);

  try {
    await axios.post('/api/yearVc/yearVcAdd', formData);
  } catch (err) {
    Swal.fire({ icon: "error", title: "연차 정보 등록 실패", text: "Error : " + err });
  }
}

// 사용 예정일 조회
export async function reqVcGetInfo(targetYear) {
  let searchData = {
    createDt: targetYear,
  }

  let result;
  try {
    result = await axios.get('/api/vacation/expectInfo', { params : searchData });
  } catch (err) {
    Swal.fire({ icon: "error", title: "사용 예정일 조회 실패", text: "Error : " + err });
  }
  return result.data;
}

// 개인 연차 정보 계산
export function vcGetInfo(yearVcData, requestData) {
  let result = {
    yearVcCd: yearVcData.yearVcCd,
    targetYear: yearVcData.targetYear,
    giveDays: yearVcData.giveDays,
    useDays: yearVcData.useDays,
    remainDays: yearVcData.giveDays - yearVcData.useDays,
    signWait: requestData.signWait,
    signWaitDays: requestData.signWaitDays,
    signSup: requestData.signSup,
    signSupDays: requestData.signSupDays,
    requestDays: yearVcData.giveDays - yearVcData.useDays - requestData.signWaitDays - requestData.signSupDays,
  };
  return result;
}

// 주말을 제외하고 날짜를 계산하는 함수
export function vcDateCalc(date, requestDays, type, isMax) {
  if (!date || !requestDays) return null; // 날짜 또는 requestDays가 없으면 null 반환

  let referenceDate = new Date(date); // 기준 날짜를 Date 객체로 변환

  // 'E01'인 경우, requestDays의 소수점 부분을 버린다.
  if (type === 'E01') {
    requestDays = Math.floor(requestDays); // 소수점 버림
  }

  let daysToCalculate = Math.floor(requestDays); // requestDays의 정수 부분 (전체 일수)
  let calculatedDate = new Date(referenceDate); // 계산된 날짜

  let daysCounted = 1; // 첫 번째 날짜는 항상 포함 (주말 제외)
  
  // 날짜를 더하거나 빼는 로직
  while (daysCounted < daysToCalculate) {
    calculatedDate.setDate(calculatedDate.getDate() + (isMax ? 1 : -1)); // max는 날짜를 더하고, min은 날짜를 뺀다
    if (calculatedDate.getDay() !== 0 && calculatedDate.getDay() !== 6) { // 주말 제외 (0=일요일, 6=토요일)
      daysCounted++;
    }
  }

  // minDateCalc일 경우, 오늘 날짜보다 이전이면 오늘 날짜로 설정
  if (!isMax) {
    const today = new Date();
    today.setHours(0, 0, 0, 0); // 오늘 날짜의 자정으로 설정
    if (calculatedDate < today) {
      calculatedDate = today;
    }
  }

  // dateTimeFormat을 사용하여 날짜 포맷팅
  return dateTimeFormat(calculatedDate, 'yyyy-MM-dd');
}
