<template>

<div class="content">
  <div class="container-fluid">
    <!-- 페이지 헤더 -->
    <div class="row">
      <div class="col">
        <h3>부서 업무 관리</h3>
      </div>
    </div>

    <!-- 페이지 바디 시작 -->
    <div class="row">
      <!-- 업무함 사이드바 -->
      <div class="col-3">
        <div class="jobbx">
          <DeptJobBx></DeptJobBx>
        </div>
      </div>

      <div class="col-9">
        <!-- 업무 관리 기능 -->
        <div class="row justify-content-between align-items-center">
          <div class="col-auto">
            <button class="btn btn-danger btn-fill me-2">선택 삭제</button>
            <button class="btn btn-primary btn-fill" @click="openJobModal">업무 등록</button>

            <!-- 업무 등록 모달 -->
            <JobManage
              :isShowJobModal="isShowJobModal"
              @closeJobModal="closeJobModal"
              @confirmJobModal="confirmJobModal"
            />
          </div>
          <div class="col d-flex justify-content-end align-items-center">
            <label for="title-search" class="m-0 me-2">제목 검색</label>
            <input class="form-control w-auto me-2" id="title-search">
            <button class="btn btn-info">검색</button>
          </div>
        </div>
        
        <!-- 업무 목록 -->
        <div class="row mt-2">
          <div class="col" style="height: 550px;">
            <div id="employeeGrid"></div>

          </div>
        </div>
      </div>

    </div>
    <!-- 페이지 바디 끝 -->

  </div>
</div>

</template>

<script setup>
import DeptJobBx from "./DeptJobBx.vue";
import { ref, onMounted } from 'vue';
import JobManage from "./JobManage.vue";

let gridInstance = ref();

// Toast UI Grid 초기화
onMounted(() => {
  gridInstance.value = new window.tui.Grid({
    el: document.getElementById('employeeGrid'),
    data: [],
    scrollX: false,
    scrollY: true,
    columns: [
      { header: '이름', name: 'name', sortable: true, editor: 'text', align: 'center' },
      { header: '직책', name: 'title', sortable: true, align: 'center' },
      { header: '부서', name: 'dept', sortable: true, align: 'center' },
      { header: '선택', name: 'select', align: 'center', renderer: { type: 'checkbox' } }
    ]
  })
})

const isShowJobModal = ref(false);
const openJobModal = () => {
  isShowJobModal.value = true;
}

const closeJobModal = () => {
  isShowJobModal.value = false;
}
const confirmJobModal = () => {
  console.log('job modal confirm click');
  isShowJobModal.value = false;
}



/*
import { AgGridVue } from "ag-grid-vue3";
import DeptJobBx from "./DeptJobBx.vue";

export default {
  name: "DeptJobInfo",
  components: {
    AgGridVue,
    DeptJobBx,
  },
  data() {
    return {
      columnDefs: [
        { 
          headerCheckboxSelection: true,
          checkboxSelection: true,
          headerName: "",
        },
        { headerName: "번호", field: "job_num" },
        { headerName: "제목", field: "dept_job_nm" },
        { headerName: "담당자", field: "charger_id" },
        { headerName: "작성일", field: "create_dt" },
        { 
          headerName: "",
          field: "",
          cellRenderer: this.btnRenderer,
          cellStyle: { textAlign: "center" },
        }
      ],
      rowData: [
        { job_num: 1, dept_job_nm: "업무1", charger_id: "이름1", create_dt: "2025-01-12" },
        { job_num: 2, dept_job_nm: "업무2", charger_id: "이름2", create_dt: "2025-01-13" },
        { job_num: 3, dept_job_nm: "업무3", charger_id: "이름3", create_dt: "2025-01-14" },
        { job_num: 4, dept_job_nm: "업무4", charger_id: "이름4", create_dt: "2025-01-15" },
      ],
      defaultColDef: {
        flex: 1, resizable: true, minWidth: 100
      },
    };
  },
  methods: {
    btnRenderer() {
      let btnTag = `<button class="btn btn-success btn-fill cell-btn-custom">
                      수정
                    </button>
                    <button class="btn btn-danger btn-fill cell-btn-custom">
                      삭제
                    </button>`;
      return btnTag;
    },
    
    // 업무함 선택, 업무 목록 가져오기
    selectJobBx() {

    }
  }
};
*/
</script>


<style scoped>
.content {
  padding: 0 !important;
}
.container-fluid > .row {
  background-color: white;
  border-radius: 7px;
  margin: 10px 0 10px 0;
  padding: 5px 0 5px 0;
}
h3, h4 {
  margin: 5px 0;
  font-weight: 900;
}

.jobbx {
  background-color: gray;
  border-radius: 5px;
  color: white;
  height: 600px;
  margin: 5px 0;
  /* padding: 5px; */
}

div::v-deep .cell-btn-custom {
  width: 30px;
  height: 25px;
  padding: 0;
}

div::v-deep .nav-link {
  display: flex !important;
}
</style>
  
  