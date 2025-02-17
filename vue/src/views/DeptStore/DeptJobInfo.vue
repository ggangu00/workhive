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
                <button class="btn btn-primary btn-fill" @click="modalOpenJob">업무 등록</button>
    
                <!-- 업무 등록 모달 -->
                <JobManage
                  :isShowJobModal="isShowJobModal"
                  @modalCloseJob="modalCloseJob"
                  @modalConfirmJob="modalConfirmJob"
                  :jobbxSelected="jobbxSelected"
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

let jobbxSelected = {
  searchDeptId: '',
  searchDeptJobBxId: 'DX_012'
};

// 부서 정보 받아오기
const getDeptData = (result) => {
  jobbxSelected.searchDeptId = result.deptId;
  jobbxSelected.searchDeptJobBxId = result.jobbxId;

  console.log('부서 정보:', jobbxSelected.searchDeptId);
  console.log('업무함 정보:', jobbxSelected.searchDeptJobBxId);
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
        renderer: btnRenderer,
      }

    ]
  })
  
  jobGetList(jobbxSelected);
})

// 그리드 버튼 -> 적용 안됨, 렌더러 다시 공부
const btnRenderer = {
  renderer({ rowKey }) {
    const btnDiv = document.createElement("div");
    btnDiv.className = "btn-group";

    const udtBtn = document.createElement("button");
    udtBtn.className = "btn btn-success btn-fill me-2";
    udtBtn.textContent = "수정";
    udtBtn.addEventListener("click", () => udtEvent(rowKey));

    const delBtn = document.createElement("button");
    delBtn.className = "btn btn-danger btn-fill";
    delBtn.textContent = "삭제";
    delBtn.addEventListener("click", () => delEvent(rowKey));

    btnDiv.appendChild(udtBtn);
    btnDiv.appendChild(delBtn);

    return btnDiv;
  }
};
const udtEvent = (rowKey) => {
  console.log(`수정 버튼 클릭됨, 행 번호: ${rowKey}`);
};
const delEvent = (rowKey) => {
  console.log(`삭제 버튼 클릭됨, 행 번호: ${rowKey}`);
  // 삭제 로직 추가
};

/*
이벤트 위임 방식 생각해보기
const btnRenderer = {
  render({ rowKey }) {
    const container = document.createElement("div");
    container.className = "btn-group";

    container.innerHTML = `
      <button class="btn btn-success btn-fill me-2" data-type="edit">수정</button>
      <button class="btn btn-danger btn-fill" data-type="delete">삭제</button>
    `;

    container.addEventListener("click", (event) => {
      const type = event.target.getAttribute("data-type");
      if (type === "edit") udtEvent(rowKey);
      else if (type === "delete") delEvent(rowKey);
    });

    return container;
  }
};
*/

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
  console.log(rowData.value);

  gridInstance.value.resetData(rowData.value);
};

// 모달
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
  
  