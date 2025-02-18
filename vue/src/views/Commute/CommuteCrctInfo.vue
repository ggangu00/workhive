<template>

  <div class="content">
    <div class="container-fluid">
      <!-- 페이지 헤더 -->
      <div class="card">
        <div class="card-body">
          <h4 class="card-title float-left mt-1">출퇴근 정정 요청 조회</h4>
        </div>
      </div>
  
      <!-- 바디 -->
      <div class="card">
        <div class="card-body">

          <div class="row">
            <div class="col">
              <!-- 조회 조건 -->
              <div class="row search align-items-center justify-content-end">
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
      
              <!-- 목록 -->
              <div class="row mt-3">
                <div class="col">
                  <div id="crctGrid"></div>

                </div>
              </div>
      
            </div>
          </div>
        </div>
      </div>
      
    </div>
  </div>

</template>

<script setup>
// import axios from 'axios';
// import Grid from 'tui-grid';
// import { ref } from 'vue';

// let rowData = ref([]);

// const crctGetList = async () => {
//   const params = {
//     createId: "user01",
//     startDate: state.startDate,
//     endDate: state.endDate
//   };
//   const result = await axios.get('/api/commute/crctList', { params });
//   rowData.value = result.data;
// }

// // Toast UI Grid 초기화
// onMounted(() => {
//   gridInstance.value = new Grid({
//     el: document.getElementById('crctGrid'),
//     data: rowData.value,
//     scrollX: false,
//     scrollY: true,
//     columns: [ //
//       { header: '번호', name: 'rowNum', sortable: true, align: 'center' },
//     ]
//   })
  
// })

</script>


<style scoped>
.header > hr {
  height: 2px;
  background-color: black;
  border: none;
}
.vc-card hr {
  border: 1px dashed gray;
  margin: 3px 0;
}
.vc-header {
  font-weight: bolder;
  font-size: 18px;
  margin-bottom: 15px;
}

.search > * > input, select {
  border: 1px solid #d2d6da;
  border-radius: 5px;
  font-size: 13px;
  line-height: 30px;
  height: 30px;
  width: 100px;
}

div::v-deep .cell-btn-custom {
  width: 30px;
  height: 25px;
  padding: 0;
}
</style>

  