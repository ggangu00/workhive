<template>

<div class="content"  @keydown.esc="modalCloseFunc">
  <div class="container-fluid">
    <!-- 페이지 헤더 -->
    <div class="card">
      <div class="card-body">
        <h4 class="card-title float-left mt-1">부서 업무 관리</h4>
      </div>
    </div>

    <div class="card">
      <div class="card-body">
        <!-- 페이지 바디 시작 -->
        <div class="row">
          <!-- 업무함 사이드바 -->
          <div class="col-3">
            <div class="jobbx">
              <DeptJobBx @datareturn="getDeptData"></DeptJobBx>
            </div>
          </div>
    
          <div class="col-9">
            <!-- 업무 관리 기능 -->
            <div class="row justify-content-between align-items-center">
              <div class="col-auto">
                <button class="btn btn-danger btn-fill me-2">선택 삭제</button>
                <button class="btn btn-primary btn-fill" @click="modalAddJob">업무 등록</button>
    
                <!-- 업무 등록 모달 -->
                <JobManage
                  :isShowJobModal="isShowJobModal"
                  :isUpdate="isUpdate"
                  @modalCloseJob="modalCloseJob"
                  @modalConfirmJob="modalConfirmJob"
                  :jobbxSelected="jobbxSelected"
                  :jobbxList="jobbxList"
                  :selectedRowData="selectedRowData"
                />
              </div>
              <div class="col d-flex justify-content-end align-items-center">
                <label for="title-search" class="m-0 me-2">제목 검색</label>
                
                <input type="text" class="form-control w-auto me-2" placeholder="제목을 입력해주세요" id="title-search">
                <button class="btn btn-info btn-fill">검색</button>
              </div>
            </div>
            
            <!-- 업무 목록 -->
            <div class="row mt-2">
              <div class="col" style="height: 550px;">
                <div id="jobGrid"></div>
    
              </div>
            </div>
          </div>
    
        </div>
        <!-- 페이지 바디 끝 -->
      </div>
    </div>

  </div>
</div>

</template>

<script setup>
import { ref, onMounted } from 'vue';
import axios from 'axios';

import DeptJobBx from "./DeptJobBx.vue";
import JobManage from "./JobManage.vue";

let gridInstance = ref();
let rowData = ref([]);

let jobbxList = [];
let jobbxSelected = {
  searchDeptId: 'ORGNZT_0000000000000',
  searchDeptJobBxId: 'DX_012'
};

// 부서 정보 받아오기
const getDeptData = (result) => {
  jobbxSelected.searchDeptId = result.deptId;
  jobbxSelected.searchDeptJobBxId = result.jobbxId;
  jobbxList = result.jobbxList;

  jobGetList(jobbxSelected);
}

// Toast UI Grid 초기화
onMounted(() => {
  gridInstance.value = new window.tui.Grid({
    el: document.getElementById('jobGrid'),
    data: rowData.value,
    scrollX: false,
    scrollY: true,
    rowHeaders: ['checkbox'],
    columns: [ // 체크박스 / 번호 / 제목 / 담당자 / 작성일 / 버튼
      { header: '번호', name: 'rowNum', sortable: true, align: 'center' },
      { header: '제목', name: 'deptJobNm', sortable: true},
      { header: '담당자', name: 'chargerNm', sortable: true},
      { header: '작성일', name: 'frstRegisterPnttm', sortable: true},
      {
        header: '관리', name: 'action', align: 'center',
        renderer: BtnRenderer,
      }

    ]
  })
  
  jobGetList(jobbxSelected);
})

// 그리드 버튼 -> 적용 안됨, 렌더러 다시 공부
class BtnRenderer {
  constructor(props) {
    const el = document.createElement("div");
    el.className = "btn-group";

    el.innerHTML = `
      <button class="btn btn-success btn-fill me-2" data-type="edit">수정</button>
      <button class="btn btn-danger btn-fill" data-type="delete">삭제</button>
    `;

    el.addEventListener("click", (event) => {
      const type = event.target.dataset.type;

      // ✅ props.row가 없을 경우 grid에서 데이터 가져오기
      const rowKey = props.row?.rowKey ?? props.grid.getRow(props.rowKey)?.rowKey;

      if (rowKey === undefined) {
        console.error("BtnRenderer: rowKey를 가져올 수 없습니다.", props);
        return;
      }

      if (type === "edit") {
        udtEvent(rowKey);
      } else if (type === "delete") {
        delEvent(rowKey);
      }
    });

    this.el = el;
  }

  getElement() {
    return this.el;
  }
}

const udtEvent = (rowKey) => {
  selectedRowData = gridInstance.value.getRow(rowKey);
  console.log(`수정 버튼 클릭됨, 행 번호: ${rowKey}`, selectedRowData);
  modalUpdateJob(); // 모달 열기
};

const delEvent = async (rowKey) => {
  selectedRowData = gridInstance.value.getRow(rowKey);
  console.log(`삭제 버튼 클릭됨, 행 번호: ${rowKey}`, selectedRowData.deptJobId);

  await axios.delete('/api/deptstore/jobremove', { params: { deptJobId: selectedRowData.deptJobId } });
  
  jobGetList(jobbxSelected);
};

// 업무 목록 조회 업무함 아이디로 검색
const jobGetList = async (jobbxSelected) => {
  let joblist = await axios.get('/api/deptstore/joblist', {
    params: jobbxSelected
  })
  .catch(error => console.error("에러 :", error));

  rowData.value = [...joblist.data.resultList];
  
  rowData.value = rowData.value.map((item, index) => ({
    rowNum: index + 1, // 1부터 시작하는 행번호
    ...item
  }));

  gridInstance.value.resetData(rowData.value);
};

// 모달
let isShowJobModal = ref(false);
let isUpdate = ref(false);

// 업무 등록
const modalAddJob = () => { 
  isUpdate.value = false;
  isShowJobModal.value = true;
}
const modalCloseJob = () => {
  isShowJobModal.value = false;
}
const modalConfirmJob = () => {
  console.log('job modal confirm click');
  isShowJobModal.value = false;
  jobGetList(jobbxSelected);
}

// 업무 수정
let selectedRowData = {};
const modalUpdateJob = () => {
  isUpdate.value = true;
  isShowJobModal.value = true;
}

const modalCloseFunc = (e) => {
  if (e.key === "Escape") {
      if(isShowJobModal.value) {
        isShowJobModal.value = !isShowJobModal.value;
      }
  }
}

</script>


<style scoped>
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
  
  