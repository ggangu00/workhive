<template>    
<div class="content">
    <div class="container-fluid">
      <div class="card card-header d-flex justify-content-between align-items-center flex-row">
      <h3 class="card-title m-0">일정관리</h3>
      <button class="btn btn-primary" data-bs-toggle="modal" data-bs-target="#scheduleModal">
        일정 등록
      </button>
    </div>

      <div class="card" style="padding: 10px;">
        <div class='calendar'>
          <FullCalendar :options="calendarOptions" />
        </div>
    </div>
  </div>
</div>

<!-- 모달 시작 -->
<div class="modal fade" id="scheduleModal" tabindex="-1" aria-labelledby="scheduleModalLabel"
    aria-hidden="true">
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
                <select class="form-select w30" aria-label="Default select example">
                  <option value="A03">회의</option>
                  <option value="A04">세미나</option>
                </select>
              </div>
              <div class="mb-3">
                <div class="form-group has-label">
                  <label>일정명 <em class="point-red">*</em></label>
                </div>
                <input type="text" name="project_nm" class="form-control">
              </div>
              <div class="mb-3">
                <div class="form-group has-label">
                  <label>일정내용 <em class="point-red">*</em></label>
                </div>
                <input type="text" name="project_nm" class="form-control">
              </div>
              <div class="mb-3">
                <div class="form-group has-label">
                  <label>장소 <em class="point-red">*</em></label>
                </div>
                <input type="text" name="project_nm" class="form-control">
              </div>

              <div class="mb-3">
                <label class="form-label">날짜 <em class="point-red">*</em></label>
                <div class="row">
                  <div class="col-auto">
                    <input type="date" name="start_dt" class="form-control">
                  </div>
                  <div class="col-auto">~</div>
                  <div class="col-auto">
                    <input type="date" name="end_dt" class="form-control">
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
                    <input type="text" class="form-control">
                  </div>
                </div>
              </div>
              <div class="mb-3">
                <label class="form-label">담당자</label>
                <div class="row">
                  <div class="col-auto">
                    <input type="text" class="form-control">
                  </div>
                </div>
              </div>
            </div>

          </div>
        <!-- 모달 푸터 -->
        <div class="modal-footer">
          <button type="button" class="btn btn-primary btn-fill" data-bs-dismiss="modal">등록</button>
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

export default {
  components: {
    FullCalendar
  },
  methods: {
    toggle() {
      this.isAllDay = !this.isAllDay;
    }
  },
  data() {
    return {
      calendarOptions: {
        plugins: [dayGridPlugin, timeGridPlugin, interactionPlugin],  
        initialView: 'dayGridMonth',  
        headerToolbar: { 
          left: 'prev,next today',
          center: 'title',
          right: 'dayGridMonth,timeGridWeek,timeGridDay'  
        },
      },
      isAllDay: false
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
