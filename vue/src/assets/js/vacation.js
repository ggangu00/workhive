import axios from "axios";

// 연차 조회
export async function yearVcGetInfo(mberId, targetYear) {
  let searchData = {
    mberId: mberId,
    targetYear: targetYear,
  }
  let result = await axios.get('/api/yearVc/yearVcInfo', { params : searchData });
  return result.data;
}

// 연차 생성
export async function yearVcAdd(mberId, targetYear) {
  let formData = new FormData();
  formData.append("mberId", mberId);
  formData.append("targetYear", targetYear);
  formData.append("giveDays", 15);
  formData.append("useDays", 0);
  
  await axios.post('/api/yearVc/yearVcAdd', formData);
}

// 사용 예정일 조회
export async function reqVcGetInfo(mberId, targetYear) {
  let searchData = {
    createId: mberId,
    createDt: targetYear,
  }
  let result = await axios.get('/api/vacation/expectInfo', { params : searchData });
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


