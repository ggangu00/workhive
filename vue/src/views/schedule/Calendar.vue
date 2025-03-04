<template>
<div class="content">
  <div class="container-fluid">
    <div class="card card-header d-flex justify-content-between align-items-center flex-row">
    <h3 class="card-title m-0">일정관리</h3>
    <button class="btn btn-primary btn-fill" data-bs-toggle="modal" data-bs-target="#scheduleModal" id='openModalButton'>
      일정 등록
    </button>
  </div>

    <div class="card" style="padding: 10px;">
      <div class='calendar'>
        <FullCalendar
        :options="calendarOptions"
        />
      </div>
  </div>
</div>
</div>

<!-- 모달 시작 -->
<div class="modal fade" id="scheduleModal" tabindex="-1" aria-labelledby="scheduleModalLabel" aria-hidden="false">
  <div class="modal-dialog modal-lg modal-dialog-centered">
    <div class="modal-content">
      <!-- 모달 헤더 -->
      <div class="modal-header">
        <h5 class="modal-title" id="scheduleModalLabel">일정등록</h5>
        <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
      </div>

      <!-- 모달 바디 -->
      <div class="card">
          <div class="card-header">
            <h5 class="card-title">일정관리등록</h5>
          </div>
          <div class="card-body">
            <div class="mb-3">
              <label class="form-label">일정유형 <em class="point-red">*</em></label>
                <select class="form-select w30" aria-label="Default select example" v-model="schedule.type">
                  <option v-for="(data, idx) in selectedData"
                  :key="idx"
                  :value="data.commDtlCd">
                  {{ data.commDtlNm }}
                  </option>
                </select>
            </div>
            <div class="mb-3">
              <div class="form-group has-label">
                <label>일정명 <em class="point-red">*</em></label>
              </div>
              <input type="text" name="project_nm" class="form-control" v-model="schedule.title">
            </div>
            <div class="mb-3">
              <div class="form-group has-label">
                <label>일정내용 <em class="point-red">*</em></label>
              </div>
              <input type="textarea" name="project_nm" class="form-control" v-model="schedule.content">
            </div>
            <div class="mb-3">
              <div class="form-group has-label">
                <label>장소 <em class="point-red">*</em></label>
              </div>
              <input type="text" name="project_nm" class="form-control" v-model="schedule.place">
            </div>

            <div class="mb-3">
              <label class="form-label">날짜 <em class="point-red">*</em></label>
              <div class="row">
                <div class="col-auto">
                  <input type="date" name="start_dt" class="form-control" v-model="schedule.start">
                </div>
                <div class="col-auto">~</div>
                <div class="col-auto">
                  <input type="date" name="end_dt" class="form-control" v-model="schedule.end">
                </div>
                <div class="col-auto">
                  <input type="checkbox" id="allDayCheck" class="ms-3" v-model="isAllDay" @change="toggle">
                  <label for="allDayCheck" class="ms-2">종일</label>
                </div>
              </div>
            </div>
            <div class="mb-3" v-if="!isAllDay">
                <label class="form-label">시간<em class="point-red">*</em></label>
                <div class="d-flex align-items-center">
                  <input type="time" class="form-control w-auto" v-model="schedule.bgntm">
                  <span class="mx-3">~</span>
                  <input type="time" class="form-control w-auto" v-model="schedule.endtm">
                </div>
              </div>
            <div class="mb-3">
              <label class="form-label">부서</label>
              <div class="row">
                <div class="col-auto">
                  <select class="form-select" v-model="schedule.deptCd">
                    <option v-for="(dept, idx) in departments" :key="idx" :value="dept.deptCd">
                      {{ dept.deptNm }}
                    </option>
                  </select>
                </div>
              </div>
            </div>
            <div class="mb-3">
              <label class="form-label">담당자</label>
              <div class="row">
                <div class="col-auto">
                  <input type="text" class="form-control" v-model="schedule.name" readonly/>
                </div>
              </div>
            </div>
          </div>

        </div>
      <!-- 모달 푸터 -->
      <div class="modal-footer">
        <button type="button" class="btn btn-secondary btn-fill" data-bs-dismiss="modal">닫기</button>
        <button v-if="isNewSchedule" type="button" class="btn btn-primary btn-fill" data-bs-dismiss="modal" @click="scheduleAdd">저장</button>
        <template v-else>
          <button v-if="isScheduleName" type="button" class="btn btn-primary btn-fill" data-bs-dismiss="modal" @click="scheduleAdd">수정</button>
          <button v-if="isScheduleName && this.schedule.type!='I01'" type="button" class="btn btn-danger btn-fill" data-bs-dismiss="modal" @click="scheduleRemove">삭제</button>
        </template>
      </div>
    </div>
  </div>
</div>
<!-- 모달 끝 -->
</template>
<script>
import FullCalendar from '@fullcalendar/vue3'
import dayGridPlugin from '@fullcalendar/daygrid'
import timeGridPlugin from '@fullcalendar/timegrid'
import interactionPlugin from '@fullcalendar/interaction'
import axios from "../../assets/js/customAxios";
import { Modal } from "bootstrap";
import Swal from 'sweetalert2';
import { useUserInfoStore } from '../../store/userStore.js';

export default {
components: {
  FullCalendar
},
data() {
  //캘린더 옵션
  return {
    calendarOptions: {
      plugins: [dayGridPlugin, timeGridPlugin, interactionPlugin],
      displayEventTime: false,
      initialView: 'dayGridMonth',
      headerToolbar: {
        left: 'prev next today',
        center: 'title',
        right: 'dayGridMonth,timeGridWeek,timeGridDay'
      },
      events: [],
      eventDisplay: 'block',
      selectedData:[], //일정종류 
      dateClick: this.handleDateClick, //연결해줘야함 이벤트
      eventClick: this.handleEventClick,  
      datesSet: this.handleDateChange
    },
    isNewSchedule: true, //등록(true)인지 수정인지
    isAllDay: false,
    schedule:{
    type:"",
    title:"",
    content:"",
    place:"",
    start:"",
    end:"",
    bgntm: "",
    endtm: "",
    deptCd:"",
    deptNm:"",
    name: ""
  },
  loginUser: "" 
  }
},
computed: {
  isScheduleName() {
    return this.loginUser == this.schedule.name; // 로그인한 사용자와 일정 등록자가 동일한지 확인
  }
},
mounted() {
  const userInfoStore = useUserInfoStore();
  this.loginUser = userInfoStore.user ? userInfoStore.user.mberNm : "";

  this.departmentlist();
  this.dataReset(); //일정등록(안에 데이터 초기화)
  this.commonDtlList();
},
methods: {
getEventColor(asdf) {
  switch (asdf) {
    case 'L01 ': return '#ffc0cb'; //회의
    default: return '#3788d8'; // 기본 색상
  }
},

  async handleDateChange(info) {
  const year = info.view.currentStart.getFullYear();
  const month = (info.view.currentStart.getMonth() + 1).toString().padStart(2, '0');

  const response = await axios.get('/api/schedule/month', {
      params: { year: year,
                month: month,
                }
  });

  console.log(response.data)
  this.calendarOptions.events = response.data.map(event => {
      return {
          id: event.schdulId,
          title: event.schdulNm,
          start: this.formatDate(event.schdulBgnde),
          end: this.formatDate(event.schdulEndde),
          schdulCn: event.schdulCn,
          place: event.schdulPlace,
          charger: event.schdulChargerId,
          register: event.mberId,
          kind: event.schdulKndCode,
          name: event.mberNm,
          type: event.schdulSe,
          deptCd: event.deptCd,
          deptNm: event.deptNm,
          color: this.getEventColor(event.schdulSe)
      };
  });
}
,
  //종일버튼시 시간값 초기화(하루종일 => 0000)
  toggle(){
    if (this.isAllDay) {
            this.schedule.bgntm = "";
            this.schedule.endtm = "";
        }
  },

  //부서정보불러오기
  async departmentlist() {
    try {
      const response = await axios.get('/api/department'); // API 호출
      this.departments = [...response.data]; // 부서 목록 저장
      
    } catch (error) {
      console.error("부서 목록을 불러오는 중 오류 발생:", error);
    }
  },

  //일정등록 모달클릭시 데이터 초기화
  dataReset(){
    document.getElementById("openModalButton").addEventListener('click', ()=>{
      this.isNewSchedule = true;  // 등록 모드로 전환
      this.schedule = {
        type:"",
        title:"",
        content:"",
        place:"",
        start:"",
        end:"",
        bgntm: "",
        endtm: "",
        deptCd:"",
        deptNm:"",
        name:"",
        mberNm:"", 
      }
    })
  },

  // 공통코드 가져오기
  async commonDtlList(){
    const docKind = await axios.get(`/api/comm/codeList`, {
      params: {cd:'SK'}
    });

    this.selectedData = [...docKind.data]
  },

  //달력 클릭이벤트
  async handleEventClick(e){
    const modal = new Modal(document.getElementById("scheduleModal"));
    modal.show();

    this.isNewSchedule = false;  // 수정 모드로 전환

    // 날짜 변환
    const startDate = e.event.start ? e.event.start.toISOString().slice(0, 10) : "";
    const endDate = e.event.end ? e.event.end.toISOString().slice(0, 10) : "";

    // 시간 변환
    const startTime = e.event.start ? e.event.start.toISOString().slice(11, 16) : "";
    const endTime = e.event.end ? e.event.end.toISOString().slice(11, 16) : "";

    //모달에 정보담기
    this.schedule.title = e.event.title;
    this.schedule.content = e.event.extendedProps.schdulCn;
    this.schedule.place = e.event.extendedProps.place;
    this.schedule.name = e.event.extendedProps.name;
    this.schedule.start = startDate;
    this.schedule.end = endDate;
    this.schedule.bgntm = startTime;
    this.schedule.endtm = endTime;
    this.schedule.type = e.event.extendedProps.type;
    this.schedule.deptNm = e.event.extendedProps.deptNm;
    this.schedule.deptCd = e.event.extendedProps.deptCd;  // 부서 코드 추가
    
    this.selectedEventId = e.event.id;//수정용 스케쥴아이디
    console.log(e.event.extendedProps)
  },



  // //일정 정보 호출 메소드
  // async scheduleGetList() {
  //     const response = await axios.get('/api/schedule/month');
  //     this.calendarOptions.events = response.data.map(event => ({
  //       id: event.schdulId,
  //       title: event.schdulNm,
  //       start: this.formatDate(event.schdulBgnde),  // 받은 날짜형식변환
  //       end: this.formatDate(event.schdulEndde),
  //       schdulCn: event.schdulCn,
  //       place: event.schdulPlace,
  //       charger: event.schdulChargerId,
  //       register: event.mberId,
  //       kind: event.schdulKndCode,
  //       name: event.mberId,
  //       type: event.schdulSe,
  //       dept: event.deptNm
  //     }));
  // },

  //등록 메소드
  async scheduleAdd(){
    const bgndeTime = this.schedule.bgntm ? this.schedule.bgntm.replace(":", "") : "0000";
    const enddeTime = this.schedule.endtm ? this.schedule.endtm.replace(":", "") : "0000";

    const addList = new FormData();
    addList.append("cmd", "save");
    addList.append("schdulDeptId", this.schedule.deptCd);
    addList.append("schdulSe", this.schedule.type);
    addList.append("schdulNm", this.schedule.title);
    addList.append("schdulCn", this.schedule.content);
    addList.append("schdulPlace", this.schedule.place);
    addList.append("schdulBgnde", this.schedule.start.replace(/-/g, '')  + bgndeTime);
    addList.append("schdulEndde", this.schedule.end.replace(/-/g, '') + enddeTime);

    if(this.selectedEventId){
      addList.append("schdulId", this.selectedEventId);
      const response = await axios.post('/api/schedule/modify', addList );

      if(response.request.status==200){
        Swal.fire({
        icon: "success",
        title: "일정 수정완료",
      })
    }
    }else{
      const response = await axios.post('/api/schedule/register', addList );
      console.log(response)
      if(response.request.status==200){
        Swal.fire({
        icon: "success",
        title: "일정 등록완료",

      })
    }
  }

  //다시불러오기
  this.handleDateChange({
  view: {
    currentStart: new Date() // 현재 달력의 시작 날짜를 기준으로 API 요청
  }
});

this.selectedEventId = null;

  //필드초기화
  this.schedule = {
    type: "",
    title: "",
    content: "",
    place: "",
    start: "",
    end: "",
    bgntm: "",
    endtm: "",
    deptNm: "",
    name: "",
    deptCd:"",
  }
},

//삭제메소드(id받을 수있으면 id에따라 버튼 다르게 할예정)
async scheduleRemove(){
  let response = await axios.delete(`/api/schedule/delete/${this.selectedEventId}`);
  if(response.data == "success"){
    Swal.fire({
        title: "삭제 완료",
        icon: "success",
        buttons: {
          cancel : "취소",
          catch: {
              text: "확인",
              value: "catch"
          },
        }

    });
    this.handleDateChange({
    view: {
      currentStart: new Date() // 현재 달력의 시작 날짜를 기준으로 API 요청
    }
});
  }
},

//풀캘린더 데이터형식
  formatDate(date) {
    if (!date) return null;
    return `${date.substring(0, 4)}-${date.substring(4, 6)}-${date.substring(6, 8)}T${date.substring(8, 10)}:${date.substring(10, 12)}:00`;
  }
},

watch(){
  
}
}
</script>
<style>
.calendar{
width: 70%;
height: 60%;
margin: 0 auto;
}

</style>
