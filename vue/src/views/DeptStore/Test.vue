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
            <!-- 추가된 부분: 사이드바 메뉴 -->
            <ul>
              <li v-for="dept in departments" :key="dept.id">
                <div @click="toggleDept(dept)" class="flex-container">
                  <i :class="dept.open ? 'fa-regular fa-folder-open' : 'fa-solid fa-folder'"></i>
                  <span>{{ dept.name }}</span>
                  <i class="fa-solid fa-plus" @click.stop="addDept(dept)"></i>
                </div>
                <ul v-if="dept.open">
                  <li v-for="sub in dept.subDepartments" :key="sub.id">
                    <div @click="toggleDept(sub)" class="flex-container">
                      <i :class="sub.open ? 'fa-regular fa-folder-open' : 'fa-solid fa-folder'"></i>
                      <span>{{ sub.name }}</span>
                      <i class="fa-solid fa-plus" @click.stop="addDept(sub)"></i>
                    </div>
                    <ul v-if="sub.open">
                      <li v-for="job in sub.jobs" :key="job.id" class="flex-container">
                        <i class="fa-solid fa-file"></i>
                        <span>{{ job.name }}</span>
                        <i class="fa-solid fa-pen" @click.stop="editJob(job)"></i>
                        <i class="fa-solid fa-x" @click.stop="removeJob(job)"></i>
                      </li>
                    </ul>
                  </li>
                </ul>
              </li>
            </ul>
            <!-- 추가된 부분 끝 -->
          </div>
        </div>

        <!-- 업무 목록 -->
        <div class="col-9">
          <!-- 업무 관리 기능 -->
          <div class="row justify-content-between align-items-center">
            <div class="col-auto">
              <button class="btn btn-danger btn-fill me-2">선택 삭제</button>
              <button class="btn btn-primary btn-fill">업무 등록</button>
            </div>
            <div class="col d-flex justify-content-end align-items-center">
              <label for="title-search" class="m-0 me-2">제목 검색</label>
              <input class="form-control w-auto me-2" id="title-search">
              <button class="btn btn-info">검색</button>
            </div>
          </div>

          <div class="row mt-2">
            <div class="col" style="height: 550px;">
              <!-- ag-grid -->
              <ag-grid-vue
                :columnDefs="columnDefs"
                :rowData="rowData"
                :defaultColDef="defaultColDef"
                class="ag-theme-alpine"
                style="width: 100%; height: 100%;"
                rowSelection="multiple"
              ></ag-grid-vue>
            </div>
          </div>
        </div>

      </div>
      <!-- 페이지 바디 끝 -->

    </div>
  </div>
</template>

<script>
import { AgGridVue } from "ag-grid-vue3";

export default {
  name: "DeptJobInfo",
  components: {
    AgGridVue,
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
      // 추가된 부분: 부서 및 업무 데이터
      departments: [
        {
          id: 1, name: "개발팀", open: false,
          subDepartments: [
            { id: 2, name: "서버팀", open: false, jobs: [{ id: 101, name: "서버 유지보수" }] }
          ]
        },
        {
          id: 3, name: "디자인팀", open: false,
          subDepartments: [
            { id: 4, name: "UI 디자인팀", open: false, jobs: [{ id: 102, name: "앱 UI 개선" }] }
          ]
        }
      ]
      // 추가된 부분 끝
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

    // 추가된 부분: 부서 및 업무 관리 메서드
    toggleDept(dept) {
      dept.open = !dept.open;
    },
    addDept(parent) {
      console.log("부서 추가", parent);
    },
    editJob(job) {
      console.log("업무 수정", job);
    },
    removeJob(job) {
      console.log("업무 삭제", job);
    }
    // 추가된 부분 끝
  }
};
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
  padding: 5px;
}

.flex-container {
  display: flex;
  align-items: center;
  gap: 5px;
}

.jobbx i {
  margin-right: 5px;
}

div::v-deep .cell-btn-custom {
  width: 30px;
  height: 25px;
  padding: 0;
}

</style>
