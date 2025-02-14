<template>    
<div class="content">
    <div class="container-fluid">
      <div class="card card-header d-flex justify-content-between align-items-center flex-row">
      <h3 class="card-title m-0">일정관리</h3>
      <button class="btn btn-primary" data-bs-toggle="modal" data-bs-target="#scheduleModal" id='openModalButton'>
        일정 등록
      </button>
    </div>

      <div class="card" style="padding: 10px;">
        <div class='calendar'>
          <FullCalendar @dateClick="handleDateClick" :options="calendarOptions"/>
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
                  <option value="1">회의</option>
                  <option value="2">세미나</option>
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
                <input type="text" name="project_nm" class="form-control" v-model="schedule.content">
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
                  <input type="checkbox" id="allDayCheck" class="ms-3" v-model="isAllDay" @change="toggle">
                  <label for="allDayCheck" class="ms-2">종일</label>
                </div>
              </div>
              <div class="mb-3" v-if="!isAllDay">
                  <label class="form-label">시간</label>
                  <div class="d-flex align-items-center">
                    <input type="time" class="form-control w-auto">
                    <span class="mx-3">~</span>
                    <input type="time" class="form-control w-auto">
                  </div>
                </div>
              <div class="mb-3">
                <label class="form-label">부서</label>
                <div class="row">
                  <div class="col-auto">
                    <input type="text" class="form-control" v-model="dept">
                  </div>
                </div>
              </div>
              <div class="mb-3">
                <label class="form-label">담당자</label>
                <div class="row">
                  <div class="col-auto">
                    <input type="text" class="form-control" v-model="name">
                  </div>
                </div>
              </div>
            </div>

          </div>
        <!-- 모달 푸터 -->
        <div class="modal-footer">
          <button type="button" class="btn btn-primary btn-fill" data-bs-dismiss="modal" @click='scheduleAdd'>등록</button>
          <button type="button" class="btn btn-secondary btn-fill" data-bs-dismiss="modal">닫기</button>
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
import axios from "axios";


export default {
  components: {
    FullCalendar
  },
  data() {
    //캘린더 옵션
    return {
      calendarOptions: {
        plugins: [dayGridPlugin, timeGridPlugin, interactionPlugin],  
        initialView: 'dayGridMonth',  
        headerToolbar: { 
          left: 'prev, next today',
          center: 'title',
          right: 'dayGridMonth,timeGridWeek,timeGridDay'  
        },
        events: []
      },
      isAllDay: false,
      
      schedule:{
      type:"",
      title:"",
      content:"",
      place:"",
      start:"",
      end:"",
      dept:"",
      name:""
    }

    }
  },
  mounted() {
    this.scheduleGetList();

  },
  methods: {
    //달력 클릭이벤트
    handleDateClick(arg) {
      alert(arg.date)
  	},

    //일정 정보 호출 메소드
    async scheduleGetList() {
        const response = await axios.get('/api/schedule/month'); 
        this.calendarOptions.events = response.data.map(event => ({
          id: event.schdulId,
          title: event.schdulNm,
          start: this.formatDate(event.schdulBgnde),  // 받은 날짜형식변환
          end: this.formatDate(event.schdulEndde),
          schdulNm: event.schdulNm,
          schdulCn: event.schdulCn,
          place: event.schdulPlace,
          charger: event.schdulChargerId,
          register: event.memCd,
          kind: event.schdulKndCode
        }));
    },
    //등록 메소드
    async scheduleAdd(){
      const addList = new FormData();
      addList.append("schdulId", "");
      addList.append("cmd", "save");
      addList.append("schdulDeptId", this.schedule.dept);
      addList.append("schdulSe", this.schedule.type);
      addList.append("schdulNm", this.schedule.title);
      addList.append("schdulCn", this.schedule.content);
      addList.append("schdulPlace", this.schedule.place);
      addList.append("schdulBgnde", this.schedule.start.replace(/-/g, ''));
      addList.append("schdulEndde", this.schedule.end.replace(/-/g, ''));

      //입력테이터 찍어보기
      for (let pair of addList.entries()) {
        console.log(`${pair[0]}: ${pair[1]}`);
      }

        const result = await axios.post('/api/schedule/register', addList, {
          headers: { "Content-Type": "multipart/form-data" }
        });
        //서버응답 찍어보기
        console.log("서버 응답:", result.data);

    //다시불러오기
    this.scheduleGetList();

    //필드초기화
    this.schedule = {
      type:"",
      title:"",
      content:"",
      place:"",
      start:"",
      end:"",
      dept:"",
      name:""
    }
  },
    formatDate(date) { //풀캘린더 데이터 형식으로 변환
      if (!date) return null;
      return `${date.substring(0, 4)}-${date.substring(4, 6)}-${date.substring(6, 8)}T${date.substring(8, 10)}:${date.substring(10, 12)}:00`;
    }
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
