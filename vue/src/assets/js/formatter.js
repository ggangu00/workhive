import { dateTimeFormat } from '../../assets/js/common';

// 월/일 시:분
export const timeFormatter = ({ value }) => dateTimeFormat(value, 'MM/dd hh:mm');

// 년-월-일
export const dateFormatter = ({ value }) => dateTimeFormat(value, 'yyyy-MM-dd');

// 우선순위
export const priortFormatter = ({ value }) => {
  let priort = '';
  switch(value) {
    case '1':
      priort = "높음";
      break;
    case '2':
      priort = "보통";
      break;
    case '3':
      priort = "낮음";
      break;
  }
  return priort;
};

// 결재 상태
export const signFormatter = ({ value }) => {
  let signState = '';
  switch(value) {
    case "D01":
      signState = `<span class="text-secondary fw-bold">미결재</span>`;
      break;
    case "D02":
      signState = `<span class="text-success fw-bold">승인</span>`;
      break;
    case "D03":
      signState = `<span class="text-warning fw-bold">보완</span>`;
      break;
    case "D04":
      signState = `<span class="text-danger fw-bold">반려</span>`;
      break;
  }
  return signState;
}

// 출근 상태
export const goFormatter = ({ value }) => {
  let goState = '';
  switch(value) {
    case "F01":
      goState = `<span class="text-success fw-bold">정상</span>`;
      break;
    case "F02":
      goState = `<span class="text-danger fw-bold">지각</span>`;
      break;
  }
  return goState;
}

// 퇴근 상태
export const leaveFormatter = ({ value }) => {
  let leaveState = '';
  switch(value) {
    case "G01":
      leaveState = `<span class="text-success fw-bold">정상</span>`;
      break;
    case "G02":
      leaveState = `<span class="text-warning fw-bold">연장</span>`;
      break;
    case "G03":
      leaveState = `<span class="text-warning fw-bold">주말</span>`;
      break;
    case "G04":
      leaveState = `<span class="text-warning fw-bold">야간</span>`;
      break;
    case "G05":
      leaveState = `<span class="text-danger fw-bold">조퇴</span>`;
      break;
  }
  return leaveState;
}

// 휴가 상태
export const vcTypeFormatter = ({ value }) => {
  let vcType = '';
  switch(value) {
    case "E01":
      vcType = `<span class="text-success fw-bold">연차</span>`;
      break;
    case "E02":
      vcType = `<span class="text-warning fw-bold">오전반차</span>`;
      break;
    case "E03":
      vcType = `<span class="text-warning fw-bold">오후반차</span>`;
      break;
    case "E04":
      vcType = `<span class="text-primary fw-bold">공가</span>`;
      break;
  }
  return vcType;
}

