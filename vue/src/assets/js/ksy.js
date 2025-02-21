import axios from "axios";

// 휴식 시간 계산
const restTime = (go, leave, time) => {
  // 점심시간 제외 (12~13시 근무 포함 시 1시간 차감)
  if (go <= 12 && leave >= 13) {
    time--;
  }
  // 저녁시간 제외
  if (go <= 18 && leave >= 19) {
    time--;
  }

  return time;
}

// 출퇴근 체크(출근 상태 / 퇴근 상태 / 근무 시간)
export async function cmtCheck(goTimeInput, leaveTimeInput) {
  const goTime = goTimeInput == null ? new Date() : new Date(goTimeInput);
  const leaveTime = leaveTimeInput == null ? new Date() : new Date(leaveTimeInput);

  let result = {
    goTime: goTime,
    goState: '', // 근무 상태 = F01 : 정상, F02 : 지각
    leaveTime: leaveTime,
    leaveState: '', // 퇴근 상태 = G01 : 정상, G02 : 연장, G03 : 주말, G04 : 야간, G05: 조퇴
    workTime: 0,
    overWorkTime: 0
  };

  // 회사 출퇴근 시간 정보 가져오기
  let compData = await axios('/api/commute/cmtTimeInfo');
  let startTime = new Date(compData.data.startTime).getHours(); // 출근 기준 시간
  let endTime = new Date(compData.data.endTime).getHours(); // 퇴근 기준 시간

  // 근무 시간 단순 계산 (밀리초 -> 시간 변환)
  result.workTime = Math.round((leaveTime - goTime) / (60 * 60 * 1000));

  // 출퇴근 시간
  const goHour = goTime.getHours();
  const leaveHour = leaveTime.getHours();

  // 출근 상태 체크
  result.goState = goHour <= startTime ? "F01" : "F02"; // 정상 출근 / 지각

  // 퇴근 상태 체크
  let isWeekend = leaveTime.getDay() === 0 || leaveTime.getDay() === 6; // 주말 여부
  let isNightWork = leaveHour >= 22 || leaveHour < 6; // 야간 근무 여부
  let isNextDayWork = goTime.getDate() < leaveTime.getDate(); // 출근일과 퇴근일이 다르면 익일 근무

  if (isWeekend) { // G03: 주말 근무
    result.leaveState = "G03";
    result.overWorkTime = result.workTime;
    result.workTime = 0;
    
    result.overWorkTime = restTime(goHour, leaveHour, result.overWorkTime);
    
    return result;

  } else if (isNightWork || isNextDayWork) { // G04: 야간 근무
    result.leaveState = "G04";
    
    // 야간 근무 시간 계산 (22시 ~ 익일 6시)
    let nightWorkHours = 0;
    if (leaveHour >= 22) {
      nightWorkHours = leaveHour - 22;
    } else if (leaveHour < 6) {
      nightWorkHours = leaveHour + 2; // 0시~6시는 전날 22시부터 이어지는 근무
    }

    result.overWorkTime = nightWorkHours;
    result.workTime -= nightWorkHours;

    result.workTime = restTime(goHour, leaveHour, result.workTime);

    if(result.workTime > 8) {
      result.overWorkTime += result.workTime - 8;
      result.workTime = 8;
    }

  } else { // 연장, 조퇴, 정상 퇴근인 경우(점심 저녁 시간 제외 후 업무 시간 계산)
    result.workTime = restTime(goHour, leaveHour, result.workTime);

    if (result.workTime > 8) { // G02: 연장 근무
      result.leaveState = "G02";
      result.overWorkTime = result.workTime - 8;
      result.workTime = 8;
      
    } else if (leaveHour < endTime) { // G05: 조퇴
      result.leaveState = "G05";
  
    } else if(leaveHour >= endTime) { // G01: 정상 퇴근
      result.leaveState = "G01";
    }

  }
  
  return result;
}
