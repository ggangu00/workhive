<template>

  <!-- 휴가 신청 등록/수정 -->
  <div class="row mt-5">
    <div class="col">
        
      <div class="row">
        <div class="col header">
          <h4>  [ 휴가 신청 목록 ]</h4>
          <hr>
        </div>
      </div>

      <!-- 조회 조건 -->
      <div class="row search align-items-center justify-content-end">
        <div class="col-auto">
          <select class="select-custom">
            <option selected>휴가종류</option>
            <option value="">연차</option>
            <option value="">오전반차</option>
            <option value="">오후반차</option>
            <option value="">공가</option>
          </select>
        </div>

        <div class="col-auto">
          <select class="select-custom">
            <option selected>결재상태</option>
            <option value="">승인</option>
            <option value="">보완</option>
            <option value="">반려</option>
          </select>
        </div>

        <div class="col-auto">
          <input type="date" id="startDate" class="input-custom">
          <a class="align-middle"> ~ </a>
          <input type="date" id="endDate" class="input-custom">
        </div>
      </div>

      <!-- 휴가 신청 목록 -->
      <div class="row mt-3">
        <div class="col" style="height: 300px; width: 800px;">
          <div id="vcGrid"></div>
        </div>
      </div>

    </div>
  </div>
  
</template>

<script setup>
// import { ref } from 'vue';

// let gridInstance = ref();
// let rowData = ref([]);

// // 검색 데이터
// const searchData = ref({
//   startDate: '',
//   endDate: '',
// });
// const crctGetList = async () => {
//   const result = await axios.get('/api/vacation/vcList', { params : searchData.value });
//   rowData.value = result.data;

//   rowData.value.forEach((i) => {
//     i.commuteDt = dateTimeFormat(i.commuteDt, 'yyyy-MM-dd');
//     i.preGoTime = dateTimeFormat(i.preGoTime, 'MM/dd hh:mm');
//     i.preLeaveTime = dateTimeFormat(i.preLeaveTime, 'MM/dd hh:mm');
//     i.crctGoTime = dateTimeFormat(i.crctGoTime, 'MM/dd hh:mm');
//     i.crctLeaveTime = dateTimeFormat(i.crctLeaveTime, 'MM/dd hh:mm');
//     i.createDt = dateTimeFormat(i.createDt, 'yyyy-MM-dd');
//   })

//   gridInstance.value.resetData(rowData.value);
// }

// watch(() => searchData, () => {
//   crctGetList();
// }, {deep:true});

// // Toast UI Grid 초기화
// onMounted(() => {
//   gridInstance.value = new Grid({
//     el: document.getElementById('crctGrid'),
//     data: rowData.value,
//     scrollX: false,
//     scrollY: true,
//     columns: [ //근무일자 / 출근시간 / 퇴근시간 / 정정출근시간 / 정정퇴근시간 / 신청일 / 결재자
//       { header: '근무일자', name: 'commuteDt', align: 'center'},
//       { header: '출근시간', name: 'preGoTime', align: 'center'},
//       { header: '퇴근시간', name: 'preLeaveTime', align: 'center'},
//       { header: '정정출근시간', name: 'crctGoTime', align: 'center'},
//       { header: '정정퇴근시간', name: 'crctLeaveTime', align: 'center'},
//       { header: '신청일', name: 'createDt', align: 'center'},
//       { header: '결재자', name: 'signId', align: 'center'},
//       { header: '결재상태', name: 'signState', align: 'center', renderer: BtnRenderer }
//     ]
//   })
  
//   crctGetList();
// })

</script>

<style scoped>

</style>
