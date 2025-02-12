<script>
import FullCalendar from '@fullcalendar/vue3'
import dayGridPlugin from '@fullcalendar/daygrid'
import timeGridPlugin from '@fullcalendar/timegrid'
import interactionPlugin from '@fullcalendar/interaction'

export default {
  components: {
    FullCalendar
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
      }
    }
  }
}
</script>

<template>    
<div class="content">
    <div class="container-fluid">
      <div class="card" style="padding: 10px;">
        <div class='calendar'>
          <FullCalendar :options="calendarOptions" />
        </div>
    </div>
  </div>
</div>
</template>
<style>
.calendar{
  width: 70%;
  height: 60%;
  margin: 0 auto;
}
</style>
