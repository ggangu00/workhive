<template>

  <div class="content">
    <div class="container-fluid">
      <!-- 페이지 헤더 -->
      <div class="card">
        <div class="card-body">
          <h4 class="card-title float-left">출퇴근 기록 조회</h4>
        </div>
      </div>
  
      <!-- 바디 -->
      <div class="card">
        <div class="card-body">

          <div class="row">
            <div class="col">
              <!-- 조회 조건 -->
              <div class="button-collection d-flex justify-content-end align-items-center flex-wrap" style="padding-bottom: 15px;">
                <div class="selectbox search d-flex">

                  <div class="d-flex align-items-center">
                    <div class="input-group">
                      <span class="input-group-text fw-bold">근무일(시작)</span>
                      <input type="date" class="form-control w50" v-model="startDate">
                    </div>
                    <span class="fw-bold">~</span>
                    <div class="input-group">
                      <span class="input-group-text fw-bold">근무일(종료)</span>
                      <input type="date" class="form-control w50" v-model="endDate">
                    </div>
                  </div>

                  <button class="btn btn-secondary btn-fill" @click="resetBtn" style="margin: 6px 0;">초기화</button>
                </div>
              </div>
      
              <!-- 목록 -->
              <div class="row">
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
import Grid from 'tui-grid';
import { useRouter } from 'vue-router';
import * as cmtFormat from '../../assets/js/formatter';

const token = localStorage.getItem("token");

const store = useStore();
const isCmt = computed(() => store.state.isCmt);
watch(isCmt, () => {
  cmtGetList();
})

let gridInstance = ref();

// 조회 조건
let params = {
  startDate: '',
  endDate: ''
};
const resetBtn = () => {
  params.value = {
    startDate: '',
    endDate: '',
  }
}

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
  params.startDate = startDate.value;
  params.endDate = endDate.value;

  cmtGetList();
});

// 출퇴근 기록
const cmtGetList = () => {
  gridInstance.value.readData(1, params);
}
const dataSource = {
  api: {
    readData: { 
      url: '/api/commute/cmtList', 
      method: 'GET', 
      initParams: params,
      headers: {'Authorization': `Bearer ${token}`},
    }
  }
};
// Grid 초기화
const initGrid = () => {
  if (gridInstance.value) {
    gridInstance.value.destroy(); // 기존 Grid 제거
  }

  gridInstance.value = new Grid({
    el: document.getElementById('commuteGrid'),
    data: dataSource,
    scrollX: false,
    scrollY: false,
    pageOptions: {
      useClient: false,
      perPage: 13,
    },
    columns: [
      { header: '근무일자', name: 'commuteDt', align: 'center', formatter: cmtFormat.dateFormatter },
      { header: '사원정보', name: 'mberNm', align: 'center', width: 100 },
      { header: '출근시간', name: 'goTime', align: 'center', formatter: cmtFormat.timeFormatter },
      { header: '출근상태', name: 'goState', align: 'center', formatter: cmtFormat.goFormatter, width: 100 },
      { header: '퇴근시간', name: 'leaveTime', align: 'center', formatter: cmtFormat.timeFormatter },
      { header: '퇴근상태', name: 'leaveState', align: 'center', formatter: cmtFormat.leaveFormatter, width: 100 },
      { header: '근무시간', name: 'workTime', align: 'center', width: 100 },
      { header: '초과근무시간', name: 'overWorkTime', align: 'center', width: 100 },
      { header: '관리', name: 'action', align: 'center', renderer: BtnRenderer, width: 120 }
    ]
  });
};

// Toast Grid 초기화
onMounted(() => {
  initGrid();
  // store.dispatch("commuteGetList");
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

  router.push({ name: 'CrctManage', query: { cmtCd: selectedRowData.commuteCd, isUpdate: 'false' } });
}

</script>


<style scoped>
</style>

  