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
                  <input type="date" id="startDate" class="input-custom" v-model="startDate">
                  <a class="align-middle"> ~ </a>
                  <input type="date" id="endDate" class="input-custom" v-model="endDate">
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
import { ref, onMounted, computed, watch, onBeforeUnmount } from 'vue';
import { useStore } from "vuex";
import axios from 'axios';
import Grid from 'tui-grid';
import { useRouter } from 'vue-router';
import { dateTimeFormat } from '../../assets/js/common.js';

const store = useStore();
const rowData = computed(() => store.state.commuteList);

let gridInstance = ref();

// 날짜 조회 조건
const startDate = computed({
  get() {
    return store.state.startDate;  // Vuex 상태에서 startDate 가져오기
  },
  set(value) {
    store.commit("setStartDate", value);  // Vuex 상태에서 startDate 업데이트
  }
});
const endDate = computed({
  get() {
    return store.state.endDate;  // Vuex 상태에서 endDate 가져오기
  },
  set(value) {
    store.commit("setEndDate", value);  // Vuex 상태에서 endDate 업데이트
  }
});

watch ([startDate, endDate], async () => {
  const params = {
    memCd: "user01",
    startDate: startDate.value,
    endDate: endDate.value
  };

  let result = await axios.get(`/api/commute/cmtList`, { params });
  
  store.commit('commuteSetList', result.data);
});

// Grid 초기화
const initGrid = () => {
  if (gridInstance.value) {
    gridInstance.value.destroy(); // 기존 Grid 제거
  }

  gridInstance.value = new Grid({
    el: document.getElementById('commuteGrid'),
    data: rowData.value,
    scrollX: false,
    scrollY: true,
    bodyHeight: 500,
    columns: [
      { header: '근무일자', name: 'commuteDt', align: 'center' },
      { header: '사원정보', name: 'memCd', align: 'center' },
      { header: '출근시간', name: 'goTime', align: 'center' },
      { header: '출근상태', name: 'goState', align: 'center' },
      { header: '퇴근시간', name: 'leaveTime', align: 'center' },
      { header: '퇴근상태', name: 'leaveState', align: 'center' },
      { header: '근무시간', name: 'workTime', align: 'center' },
      { header: '초과근무시간', name: 'overWorkTime', align: 'center' },
      { header: '관리', name: 'action', align: 'center', renderer: BtnRenderer }
    ]
  });
};

// Toast Grid 초기화
onMounted(() => {
  initGrid();
  store.dispatch("commuteGetList");
});

// rowData가 변경될 때 Toast Grid 업데이트
watch(rowData, (newData) => {
  rowData.value.forEach(i => {
    i.commuteDt = dateTimeFormat(i.commuteDt, 'yyyy-MM-dd');
    i.goTime = dateTimeFormat(i.goTime, 'MM/dd hh:mm');
    i.leaveTime = dateTimeFormat(i.leaveTime, 'MM/dd hh:mm');
  });
  if (gridInstance.value) {
    gridInstance.value.resetData(newData);
  }
});

// 페이지 이동 시 Grid 제거하여 중복 방지
onBeforeUnmount(() => {
  if (gridInstance.value) {
    gridInstance.value.destroy();
    gridInstance.value = null;
  }
});


// 그리드 버튼
class BtnRenderer {
  constructor(props) {
    const el = document.createElement("div");
    el.className = "btn-group";

    el.innerHTML = `
      <button class="btn btn-warning btn-fill cell-btn-custom">정정요청</button>
    `;

    el.addEventListener("click", () => {
      // props.row가 없을 경우 grid에서 데이터 가져오기
      const rowKey = props.row?.rowKey ?? props.grid.getRow(props.rowKey)?.rowKey;

      if (rowKey === undefined) {
        console.error("BtnRenderer: rowKey를 가져올 수 없습니다.", props);
        return;
      }

      btnCrctAdd(rowKey);
    });

    this.el = el;
  }

  getElement() {
    return this.el;
  }
}
// 정정 요청 페이지로 출퇴근 코드 전달
const router = useRouter();
const btnCrctAdd = (rowKey) => {
  let selectedRowData = gridInstance.value.getRow(rowKey);
  console.log("selectedRowData : ", selectedRowData.commuteCd);

  router.push({ name: 'CrctManage', query: { cmtCd: selectedRowData.commuteCd, isUpdate: false } });
}

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

</style>

  