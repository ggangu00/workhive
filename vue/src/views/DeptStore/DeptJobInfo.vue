<template>

<div class="content"  @keydown.esc="modalCloseFunc">
  <div class="container-fluid">
    <!-- 페이지 헤더 -->
    <div class="card">
      <div class="card-body">
        <h4 class="card-title float-left mt-1">부서 업무 관리</h4>
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
            <button class="btn btn-primary btn-fill" @click="modalOpenJob">업무 등록</button>

            <!-- 업무 등록 모달 -->
            <JobManage
              :isShowJobModal="isShowJobModal"
              @modalCloseJob="modalCloseJob"
              @modalConfirmJob="modalConfirmJob"
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
            <div id="jobGrid"></div>

          </div>
        </div>
      </div>

    </div>
    <!-- 페이지 바디 끝 -->

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

// Toast UI Grid 초기화
onMounted(() => {
  gridInstance.value = new window.tui.Grid({
    el: document.getElementById('jobGrid'),
    data: rowData.value,
    scrollX: false,
    scrollY: true,
    columns: [ // 체크박스 / 번호 / 제목 / 담당자 / 작성일 / 버튼
      { header: '', name: ''},
      { header: '번호', name: '', sortable: true, align: 'center' },
      { header: '제목', name: 'deptJobNm', sortable: true},
      { header: '담당자', name: 'chargerNm', sortable: true},
      { header: '작성일', name: 'frstRegisterPnttm', sortable: true},
      { header: '', name: ''},
    ]
  })
  
  jobbxGetList();
  jobGetList("DX_012");
})

// 업무함 목록 조회
const jobbxGetList = async () => {
  await axios.get('/api/jobbx/jobbxlist')
  .then(response => {
    console.log("데이터:", response.data);
    
  })
  .catch(error => console.error("에러 :", error));
};
// 업무 목록 조회 업무함 아이디로 검색
const jobGetList = async (searchDeptJobBxId) => {
  let joblist = await axios.get('/api/jobbx/joblist', {
    params: {searchDeptJobBxId: searchDeptJobBxId}
  })
  .catch(error => console.error("에러 :", error));

  rowData.value = [...joblist.data.resultList];
  console.log(rowData.value);
  gridInstance.value.resetData(rowData.value);
};

const isShowJobModal = ref(false);
const modalOpenJob = () => {
  isShowJobModal.value = true;
}

const modalCloseJob = () => {
  isShowJobModal.value = false;
}
const modalConfirmJob = () => {
  console.log('job modal confirm click');
  isShowJobModal.value = false;
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
  
  