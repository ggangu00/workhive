import { dateTimeFormat } from '../../assets/js/common';

// 월/일 시:분
export const timeFormatter = ({ value }) => dateTimeFormat(value, 'MM/dd hh:mm');

// 년-월-일
export const dateFormatter = ({ value }) => dateTimeFormat(value, 'yyyy-MM-dd');

// 결재 상태
export const signFormatter = ({ value }) => {
  let signState = '';
  switch(value) {
    case "D01":
      signState = "미결재";
      break;
    case "D02":
      signState = "승인";
      break;
    case "D03":
      signState = "보완";
      break;
    case "D04":
      signState = `<a>반려</a>`;
      break;
  }
  return signState;
}

// 출근 상태
export const goFormatter = ({ value }) => {
  let goState = '';
  switch(value) {
    case "F01":
      goState = "정상";
      break;
    case "F02":
      goState = "지각";
      break;
  }
  return goState;
}

// 퇴근 상태
export const leaveFormatter = ({ value }) => {
  let leaveState = '';
  switch(value) {
    case "G01":
      leaveState = "정상";
      break;
    case "G02":
      leaveState = "연장";
      break;
    case "G03":
      leaveState = "주말";
      break;
    case "G04":
      leaveState = `야간`;
      break;
    case "G05":
      leaveState = `조퇴`;
      break;
  }
  return leaveState;
}

// 휴가 상태
export const vcTypeFormatter = ({ value }) => {
  let vcType = '';
  switch(value) {
    case "E01":
      vcType = "연차";
      break;
    case "E02":
      vcType = "오전반차";
      break;
    case "E03":
      vcType = "오후반차";
      break;
    case "E04":
      vcType = `공가`;
      break;
  }
  return vcType;
}

