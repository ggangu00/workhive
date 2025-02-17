<template>

  <div class="content">
    <div class="container-fluid">
      <!-- 페이지 헤더 -->
      <div class="card">
        <div class="card-body">
          <h4 class="card-title float-left mt-1">출퇴근 기록 조회</h4>
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
                  <input type="date" id="startDate" class="input-custom">
                  <a class="align-middle"> ~ </a>
                  <input type="date" id="endDate" class="input-custom">
                </div>
              </div>
      
              <!-- 목록 -->
              <div class="row mt-3">
                <div class="col">
                  <div id="commuteGrid"></div>

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
import { ref, onMounted } from 'vue';
import axios from 'axios';

let gridInstance = ref();
let rowData = ref([]);

// Toast UI Grid 초기화
onMounted(() => {
  gridInstance.value = new window.tui.Grid({
    el: document.getElementById('commuteGrid'),
    data: rowData.value,
    scrollX: false,
    scrollY: true,
    columns: [ // 근무일자/사원정보/출근시간/출근상태/퇴근시간/퇴근상태/근무시간/초과근무시간/정정요청버튼
      { header: '근무일자', name: 'commuteDt'},
      { header: '사원정보', name: 'memCd'},
      { header: '출근시간', name: 'goTime'},
      { header: '출근상태', name: 'goState'},
      { header: '퇴근시간', name: 'leaveTime'},
      { header: '퇴근상태', name: 'leaveState'},
      { header: '근무시간', name: 'workTime'},
      { header: '초과근무시간', name: 'overWorkTime'},
      {
        header: '관리', name: 'action', align: 'center',
        renderer: '',
      }

    ]
  })
  
  cmtGetList();
})

// 출퇴근 목록 조회
const memCd = "user01";
const cmtGetList = async () => {
  let result = await axios.get(`/api/commute/cmtList?memCd=${memCd}`);
  console.log(result.data);
  rowData.value = result.data;
  
  gridInstance.value.resetData(rowData.value);
}


/*
statusRenderer(params) {
  let status = params.value;

  let btnTag;
  if (status === "보완") {
    btnTag = `<button class="btn btn-warning btn-fill cell-btn-custom">
                ${status}
              </button>
              <button class="btn btn-danger btn-fill cell-btn-custom">
                삭제
              </button>`;
  }
  else if (status === "대기") {
    btnTag = `<button class="btn btn-success btn-fill cell-btn-custom">
                ${status}
              </button>
              <button class="btn btn-danger btn-fill cell-btn-custom">
                삭제
              </button>`;
  }
  else if (status === "승인") {
    btnTag = `<a>${status}</a>`;
  }

  return btnTag;
}
*/

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

  