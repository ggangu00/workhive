<template>
  <div class="content">
    <div class="container-fluid">
      <div class="row">
        <div class="col-12">
          <div class="card">
            <!-- 버튼 & 필터 -->
            <div class="button-collection d-flex justify-content-between align-items-center flex-wrap" style="padding: 15px;">
              <div class="d-flex">
                <button 
                  v-for="(btn, index) in buttons" 
                  :key="index"
                  :class="['btn', btn.class]"
                  @click="$emit('button-click', btn.action)">
                  {{ btn.label }}
                </button>
              </div>

              <div class="selectbox d-flex">
                <select class="form-select w10" name="doc_kind" v-model="docKind">
                  <option v-for="(data, idx) in selectedData" 
                  :key="idx"
                  :value="data.commDtlCd">
                  {{ data.commDtlNm }}
                  </option>

                </select>
                <select class="form-select w10" name="dept_nm">
                  <option disabled selected>부서</option>
                  <option value="">기획팀</option>
                  <option value="">영업팀</option>
                </select>
                <select class="form-select w10" name="form_cd">
                  <option disabled selected>양식</option>
                  <option value="">구입신청서</option>
                  <option value="">외부지출서</option>
                  <option value="">증명서</option>
                </select>
                <div class="d-flex">
                  <div class="input-group">
                    <span class="input-group-text fw-bold">기안일(시작)</span>
                    <input type="date" class="form-control">
                  </div>
                  <span class="fw-bold">~</span>
                  <div class="input-group">
                    <span class="input-group-text fw-bold">기안일(종료)</span>
                    <input type="date" class="form-control">
                  </div>
                </div>
                
                <button class="btn btn-secondary btn-fill">초기화</button>
              </div>
            </div>
          </div>
        </div>

        <!-- Toast UI Grid 영역 -->
        <div class="col-12">
          <div id="tableGrid" class="toastui"></div>
          <div id="pagination" class="tui-pagination"></div>
        </div>

      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted, watch } from "vue";
import { useRouter } from "vue-router";
import "tui-grid/dist/tui-grid.css";
import axios from "axios";

// Props 정의
const props = defineProps({
  buttons: { type: Array, required: true },
  columnDefs: { type: Array, required: true }, // Column 정의
  status: { type: String, required: true }, // 현재 진행 상태
});

// Vue Router 사용
const router = useRouter();
const page = ref(1);

// Grid 및 필터 설정
const grid = ref(null);
const docKind = ref('');
const rowData = ref([]);
//셀렉트박스
const selectedData = ref([]);

//공통코드 가져오기
const commonDtlList = async () =>{
  const docKind = await axios.get(`/api/comm/codeList`, {
    params: {cd:'DK'}
  });
  selectedData.value = [...docKind.data]
}
// API 요청 파라미터
const getParams = ({
  status: props.status,
  deptNm: '',
  docKind: '',
  formCd: '',
  startDate: '',
  endDate: '',
});

const dataSource = {
  api: {
    readData: {
      url: "/api/document/list",
      method: "GET",
      initParams: getParams, // 페이지, 상태코드(미결, 반려, 진행완료)
    },
  },
  
}

// Toast Grid 초기화
const TueGrid = () => {

  grid.value = new window.tui.Grid({
    el: document.getElementById("tableGrid"),
    scrollX: true,
    scrollY: true,
    columns: props.columnDefs,
    rowHeaders: ["checkbox"],
    pageOptions: {
      useClient: false, // 서버 사이드 페이지네이션 사용
      perPage: 5,
    },
    data: dataSource,
  });

  // 행 클릭 이벤트 추가
  grid.value.on("click", handleRowClick);
};

// 행 클릭 이벤트 핸들러
const handleRowClick = (ev) => {
  if (!grid.value) return;
  const dataRow = grid.value.getRow(ev.rowKey);

  // 특정 조건일 때 페이지 이동
  if (dataRow?.crntSignStat == "반려") {
    router.push({ path: "/approval/rejectedInfo" });
  }
};

onMounted(() => {
  TueGrid(); // Grid 초기화 실행
  commonDtlList();
});

//셀렉트박스 변경시 필터 감지하여 재로딩
watch(docKind, async (newVal) => {
    const response = await axios.get("/api/document/list", { params: {
      docKind : newVal,
      perPage: 5,
      page: page.value
    } });

    console.log(response)
    rowData.value = [...response.data.data.contents];
    if (grid.value) {
      grid.value.resetData(rowData.value);
    }
});




</script>

<style scoped>
.button-collection button {
  margin-right: 10px;
}
.selectbox select {
  margin-right: 10px;
}
.toastui {
  width: 100%;
  height: 500px;
}
</style>
