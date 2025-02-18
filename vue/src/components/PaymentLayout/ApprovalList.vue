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
                <select class="form-select w10" name="doc_kind">
                  <option disabled selected>전체</option>
                  <option value="">일반결재</option>
                  <option value="">수신결재</option>
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
import { ref, onMounted } from "vue";
import { useRouter } from "vue-router";
import "tui-grid/dist/tui-grid.css";

// Props 정의
const props = defineProps({
  buttons: { type: Array, required: true },
  columnDefs: { type: Array, required: true }, // Column 정의
  status: { type: String, required: true }, // 현재 진행 상태
});

// Vue Router 사용
const router = useRouter();

// Grid 및 필터 설정
const grid = ref(null);
const filters = ref({
  dept_nm: "",
  form_cd: "",
});

// API 요청 파라미터
const getParams = ({
  status: props.status,
  deptNm: filters.value.dept_nm,
  formCd: filters.value.form_cd,
  startDate: filters.value.startDate,
  endDate: filters.value.endDate,
  page: 1,
});

const dataSource = {
  api: {
    readData: {
      url: "/api/document/list",
      method: "GET",
      initParams: getParams.value, // 페이지, 상태코드(미결, 반려, 진행완료)
    },
  },
  
}

// Toast Grid 초기화
const initializeGrid = () => {
  console.log("서버로 보낼 데이터:", getParams.value);

  grid.value = new window.tui.Grid({
    el: document.getElementById("tableGrid"),
    scrollX: true,
    scrollY: true,
    columns: props.columnDefs,
    rowHeaders: ["checkbox"],
    pageOptions: {
      useClient: false, // 서버 사이드 페이지네이션 사용
      perPage: 1,
    },
    
    data: dataSource,
  });

  // 서버 응답 데이터를 콘솔에 출력
  grid.value.on("response", (ev) => {
    console.log("서버 응답 데이터:", ev.responseData);
  });

  // 행 클릭 이벤트 추가
  grid.value.on("click", handleRowClick);
};

// 행 클릭 이벤트 핸들러
const handleRowClick = (ev) => {
  console.log("click ! ", ev)
  if (!grid.value) return;
  const dataRow = grid.value.getRow(ev.rowKey);
  console.log("선택된 행 데이터:", dataRow);

  // 특정 조건일 때 페이지 이동
  if (dataRow?.crntSignStat === "반려") {
    router.push({ path: "/approval/rejectedInfo" });
  }
};

onMounted(() => {
  initializeGrid(); // Grid 초기화 실행
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
