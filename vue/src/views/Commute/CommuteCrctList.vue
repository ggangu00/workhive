<template>

  <div class="content">
    <div class="container-fluid">
      <!-- 페이지 헤더 -->
      <div class="card">
        <div class="card-body">
          <h4 class="card-title float-left">출퇴근 정정 요청 조회</h4>
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
                  <select class="input-custom" v-model="searchData.searchState">
                    <option value="" selected>결재상태</option>
                    <option value="D01">결재대기</option>
                    <option value="D02">승인</option>
                    <option value="D03">보완</option>
                    <option value="D04">반려</option>
                  </select>
                </div>

                <div class="col-auto">
                  <input type="date" id="startDate" class="input-custom" v-model="searchData.startDate">
                  <a class="align-middle"> ~ </a>
                  <input type="date" id="endDate" class="input-custom" v-model="searchData.endDate">
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
import axios from '../../assets/js/customAxios.js';
import Grid from 'tui-grid';
import { ref, onBeforeUnmount, onMounted, watch } from 'vue';
import { useRouter } from 'vue-router';
import { timeFormatter, dateFormatter } from '../../assets/js/formatter.js';

const token = localStorage.getItem("token");

let gridInstance = ref();

// 검색 데이터
const searchData = ref({
  startDate: '',
  endDate: '',
  searchState: ''
});

const crctGetList = () => {
  gridInstance.value.readData(1, searchData.value);
}
const dataSource = {
  api: {
    readData: { 
      url: '/api/commute/crctList', 
      method: 'GET', 
      initParams: searchData.value,
      headers: {'Authorization': `Bearer ${token}`},
    }
  }
};

watch(() => searchData, () => {
  crctGetList();
}, {deep:true});

// Toast UI Grid 초기화
onMounted(() => {
  gridInstance.value = new Grid({
    el: document.getElementById('crctGrid'),
    data: dataSource,
    scrollX: false,
    scrollY: true,
    pageOptions: {
      useClient: false,
      perPage: 5,
    },
    columns: [ //근무일자 / 출근시간 / 퇴근시간 / 정정출근시간 / 정정퇴근시간 / 신청일 / 결재자
      { header: '근무일자', name: 'commuteDt', align: 'center', formatter: dateFormatter },
      { header: '출근시간', name: 'preGoTime', align: 'center', formatter: timeFormatter },
      { header: '퇴근시간', name: 'preLeaveTime', align: 'center', formatter: timeFormatter },
      { header: '정정출근시간', name: 'crctGoTime', align: 'center', formatter: timeFormatter },
      { header: '정정퇴근시간', name: 'crctLeaveTime', align: 'center', formatter: timeFormatter },
      { header: '신청일', name: 'createDt', align: 'center', formatter: dateFormatter },
      { header: '결재자', name: 'signId', align: 'center'},
      { header: '결재상태', name: 'signState', align: 'center', renderer: BtnRenderer }
    ]
  })

  // 행 클릭 이벤트 추가
  gridInstance.value.on("click", handleRowClick);
})

// 행클릭
let isDetail = ref(false);
const handleRowClick = (e) => {
  if (!gridInstance.value || e.rowKey == null || e.rowKey == undefined ) return;

  isDetail.value = true;
  let selectedRowData = gridInstance.value.getRow(e.rowKey);

  router.push({ name: 'CrctManage', query: { crctCd: selectedRowData.crctCd, isDetail: 'true' } });
}

// 그리드 버튼
class BtnRenderer {
  constructor(props) {
    const el = document.createElement("div");
    el.className = "btn-group";

    const signState = props.grid.getRow(props.rowKey).signState;

    if (signState === "D01") {
      el.innerHTML = `
        <button class="btn btn-success btn-fill cell-btn-custom me-2" data-type="wait">대기</button>
        <button class="btn btn-danger btn-fill cell-btn-custom" data-type="delete">삭제</button>
      `;
    } else if (signState === "D02") {
      el.innerHTML = `<span class="text-success fw-bold">승인</span>`;
    } else if (signState === "D03") {
      el.innerHTML = `
        <button class="btn btn-warning btn-fill cell-btn-custom me-2" data-type="edit">보완</button>
        <button class="btn btn-danger btn-fill cell-btn-custom" data-type="delete">삭제</button>
      `;
    } else if (signState === "D04") {
      el.innerHTML = `<span">반려</span>`;
    }

    el.addEventListener("click", (event) => {
      event.stopPropagation(); // 부모 요소(행 클릭) 이벤트 전파 방지

      const type = event.target.dataset.type;

      // props.row가 없을 경우 grid에서 데이터 가져오기
      const rowKey = props.row?.rowKey ?? props.grid.getRow(props.rowKey)?.rowKey;

      if (rowKey === undefined) {
        console.error("BtnRenderer: rowKey를 가져올 수 없습니다.", props);
        return;
      }

      if (type === "wait") {
        btnCrctUpdate(rowKey);
      } else if (type === "edit") {
        btnCrctUpdate(rowKey);
      } else if (type === "delete") {
        btnCrctDelete(rowKey);
      }
    });

    this.el = el;
  }

  getElement() {
    return this.el;
  }
}

const router = useRouter();
const btnCrctUpdate = (rowKey) => {
  let selectedRowData = gridInstance.value.getRow(rowKey);

  router.push({ name: 'CrctManage', query: { crctCd: selectedRowData.crctCd, isUpdate: 'true' } });
};
const btnCrctDelete = async (rowKey) => {
  let crctCd = gridInstance.value.getRow(rowKey).crctCd;

  await axios.post(`/api/commute/crctRemove?crctCd=${crctCd}`);
  crctGetList();
};

// 페이지 이동 시 Grid 제거하여 중복 방지
onBeforeUnmount(() => {
  if (gridInstance.value) {
    gridInstance.value.destroy();
    gridInstance.value = null;
  }
});
</script>


<style scoped>
</style>

