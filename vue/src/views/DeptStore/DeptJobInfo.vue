<template>

  <div class="content" @keydown.esc="modalCloseFunc">
    <div class="container-fluid">
      <!-- 페이지 헤더 -->
      <div class="card">
        <div class="card-body">
          <h4 class="card-title float-left">부서 업무 관리</h4>
        </div>
      </div>

      <div class="card">
        <div class="card-body">
          <!-- 페이지 바디 시작 -->
          <div class="row">
            <!-- 업무함 사이드바 -->
            <div class="col-3">
              <div class="jobBx">
                <ul>
                  <DeptJobBx
                    v-for="dept in rootDepartments"
                    :key="dept.deptCd"
                    :dept="dept"
                    :departments="departments"
                    :jobBoxes="jobBoxes"
                  />
                </ul>

              </div>

              <!-- 업무함 관리(등록/수정) -->
              <Modal
                :isShowModal="isShowModal"
                :modalTitle="'업무함 관리'"
                @click.self="modalClose"
              >
                <template v-slot:body>
                  <div class="content">
                    <div class="container-fluid">

                      <div class="row">
                        <div class="col">
                          <div class="mb-3">
                            <label>부서명</label>
                            <input type="text" class="form-control" v-model="jobBxData.deptNm" placeholder="부서명" readonly />
                          </div>
                        </div>
                        <div class="col">
                          <div class="mb-3">
                            <label>표시순서</label>
                            <input type="number" class="form-control" v-model="jobBxData.indictOrdr" />
                          </div>
                        </div>
                      </div>
                      <div class="row">
                        <div class="col">
                          <div class="mb-3">
                            <label>업무함명</label>
                            <input type="text" class="form-control" v-model="jobBxData.deptJobBxNm" placeholder="업무함명을 입력해주세요." />
                          </div>
                        </div>
                      </div>

                    </div>
                  </div>
                </template>
                <template v-slot:footer>
                  <button class="btn btn-secondary btn-fill mx-2" @click="modalClose">닫기</button>
                  <button class="btn btn-success btn-fill mx-2" @click="modalConfirm">저장</button>
                </template>
              </Modal>

            </div>

            <div class="col-9">
              <!-- 업무 관리 기능 -->
              <div class="row justify-content-between align-items-center">
                <div class="col-auto">
                  <button class="btn btn-danger btn-fill me-2" @click="btnJobListRemove">선택 삭제</button>
                  <button class="btn btn-primary btn-fill" @click="modalAddJob">업무 등록</button>

                  <!-- 업무 등록 모달 -->
                  <JobManage
                    :isShowJobModal="isShowJobModal"
                    :isUpdate="isUpdate"
                    @modalCloseJob="modalCloseJob"
                    @modalConfirmJob="modalConfirmJob"
                    :jobBxSelected="jobBxSelected"
                    :selectedRowData="selectedRowData"
                  />
                </div>
                <div class="col d-flex justify-content-end align-items-center">
                  <label for="title-search" class="m-0 me-2">제목 검색</label>

                  <input type="text" class="form-control w-auto me-2" placeholder="제목을 입력해주세요" id="title-search" v-model="jobSearch.searchWrd">
                  <button class="btn btn-info btn-fill" @click="jobGetList">검색</button>
                </div>
              </div>

              <!-- 업무 목록 -->
              <div class="row mt-2">
                <div class="col" style="height: 550px;">
                  <div id="jobGrid"></div>
                  <div id="pagination" class="tui-pagination"></div>

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
import { ref, computed, onMounted, onBeforeUnmount, watch } from 'vue';
import axios from '../../assets/js/customAxios.js';
import Grid from 'tui-grid';
import "tui-grid/dist/tui-grid.css";
import Swal from 'sweetalert2';
import { useStore } from 'vuex';
import { provide } from 'vue'; // 자식에게 전달
import DeptJobBx from "./DeptJobBx.vue";
import JobManage from "./JobManage.vue";
import Modal from '../../components/Modal.vue';

const token = localStorage.getItem("token");

let gridInstance = ref();

// 부서 및 업무함 시작
// vuex 감지
const store = useStore();
const jobBxSelected = computed(() => store.state.jobBxSelected);

// 업무 목록 조회 업무함 아이디로 검색
let jobSearch = ref({
  searchWrd: '',
  searchCnd: 0, // 업무명 검색 조건
});

// 선택한 업무함 변경시 업무 목록 갱신
watch(jobBxSelected, () => {
  jobSearch.value.searchWrd = '';
  jobSearch.value = Object.assign(jobSearch.value, jobBxSelected.value);

  jobGetList();
});
const jobGetList = () => {
  gridInstance.value.readData(1, jobSearch.value);
}

// 부서 및 업무함 데이터
const departments = ref([]);
const jobBoxes = ref([]);

// 부서 정보 조회
const deptGetList = async () => {
  try {
    const result = await axios.get('/api/department');
    departments.value = result.data;

  } catch (err) {
    Swal.fire({ icon: "error", title: "부서 조회 실패", text: "Error : " + err });
  }
};

// 업무함 정보 조회
const jobBxGetList  = async () => {
  try {
    const result = await axios.get('/api/deptstore/jobBxList');
    jobBoxes.value = result.data;

  } catch (err) {
    Swal.fire({ icon: "error", title: "업무함 조회 실패", text: "Error : " + err });
  }
};

// 최상위 부서 찾기 (depth === 0)
const rootDepartments = computed(() =>
  departments.value.filter(dept => dept.depth === 0)
);

// 업무함 모달 데이터
const jobBxData = ref({
  deptCd: '',
  deptNm: '',
  indictOrdr: 0,
  deptJobBxNm: '',
  deptJobBxId: '',
});
let jobBxModalType = '';
// 업무함 관리 동작
const jobBxCheck = async (type, data) => {
  jobBxModalType = type;
  switch(type) {
    case 'add':
      //부서 정보
      jobBxData.value.deptCd = data.deptCd;
      jobBxData.value.deptNm = data.deptNm.trim();

      //초기화
      jobBxData.value.indictOrdr = 0;
      jobBxData.value.deptJobBxNm = '';
      jobBxData.value.deptJobBxId = '';
      modalOpen();
      break;
    case 'modify':
      jobBxData.value.deptCd = data.deptCd;
      jobBxData.value.deptNm = data.deptNm.trim();
      jobBxData.value.indictOrdr = data.indictOrdr;
      jobBxData.value.deptJobBxId = data.deptJobBxId;
      jobBxData.value.deptJobBxNm = data.deptJobBxNm.trim();
      modalOpen();
      break;
    case 'remove':
      await axios.delete('/api/deptstore/jobBxRemove', { params: { deptJobBxId: data.deptJobBxId } });
      deptGetList();
      jobBxGetList();
      break;
  }

}
provide('jobBxCheck', jobBxCheck);

// 업무함 관리 모달
let isShowModal = ref(false);
const modalOpen = () => {
  isShowModal.value = true;
}
const modalClose = () => {
  isShowModal.value = false;
}
const modalConfirm = async () => {
  const formData = new FormData();
  formData.append("deptCd", jobBxData.value.deptCd);
  formData.append("deptJobBxNm", jobBxData.value.deptJobBxNm);
  formData.append("indictOrdr", jobBxData.value.indictOrdr);

  if(jobBxModalType == 'add') {
    await axios.post('/api/deptstore/jobBxAdd', formData);

  } else if(jobBxModalType == 'modify') {
    formData.append("deptJobBxId", jobBxData.value.deptJobBxId);
    await axios.post('/api/deptstore/jobBxModify', formData);

  }
  deptGetList();
  jobBxGetList();
  modalClose();
}


// 부서 및 업무함 종료
// Toast UI Grid 초기화
onMounted(() => {
  // 사이드 메뉴 데이터 호출
  deptGetList();
  jobBxGetList();

  gridInstance.value = new Grid({
    el: document.getElementById('jobGrid'),
    data: dataSource,
    scrollX: false,
    scrollY: true,
    rowHeaders: ['checkbox', 'rowNum'],
    pageOptions: {
      useClient: false,
      perPage: 5,
    },
    columns: [ // 체크박스 / 번호 / 제목 / 담당자 / 작성일 / 버튼
      { header: '제목', name: 'deptJobNm', sortable: true},
      { header: '담당자', name: 'chargerNm', sortable: true},
      { header: '작성일', name: 'frstRegisterPnttm', sortable: true},
      {
        header: '관리', name: 'action', align: 'center',
        renderer: BtnRenderer,
      }
    ]
  })

  // jobGetList();
})

const dataSource = {
  api: {
    readData: { 
      url: '/api/deptstore/jobList', 
      method: 'GET',
      headers: {'Authorization': `Bearer ${token}`},
    }
  }
};

// 그리드 버튼
class BtnRenderer {
  constructor(props) {
    const el = document.createElement("div");
    el.className = "btn-group";

    el.innerHTML = `
      <button class="btn btn-success btn-fill cell-btn-custom me-2" data-type="edit">수정</button>
      <button class="btn btn-danger btn-fill cell-btn-custom" data-type="delete">삭제</button>
    `;

    el.addEventListener("click", (event) => {
      const type = event.target.dataset.type;

      // props.row가 없을 경우 grid에서 데이터 가져오기
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
  modalUpdateJob(); // 모달 열기
};

// 업무 삭제
const delEvent = async (rowKey) => {
  selectedRowData = gridInstance.value.getRow(rowKey);

  await axios.delete('/api/deptstore/jobRemove', { params: { deptJobId: selectedRowData.deptJobId } });

  jobGetList();
};
// 업무 목록 삭제
const btnJobListRemove = async () => {
  let selectedRows = gridInstance.value.getCheckedRows();

  // 필요한 필드만 추출
  const jobList = selectedRows.map(row => ({
    deptJobId: row.deptJobId,
    atchFileId: row.atchFileId,
  }));

  await axios.post('/api/deptstore/jobListRemove', jobList);

  jobGetList();
};

// const jobGetList = async (page = 1) => {

//   // console.log(jobBxSelected);

//   // 그리드 페이지 정보 입력
//   // let gridPage = gridInstance.value.paginationManager.getPagination();
//   // console.log("grid page : ", gridPage);
//   // let newPagination = {
//   //   pageIndex: gridPage._currentPage,
//   //   pageUnit: gridPage._options.perPage,
//   //   pageSize: 5, // 페이지 표시 갯수
//   // };
//   // jobSearch.value = {...jobSearch.value, ...newPagination};

//   jobSearch.value.page = page;

//   let jobList = await axios.get('/api/deptstore/jobList', {
//     params: jobSearch.value
//   })
//   .catch(error => console.error("에러 :", error));

//   rowData.value = [...jobList.data.resultList];

//   rowData.value = rowData.value.map((item, index) => ({
//     rowNum: index + 1, // 1부터 시작하는 행번호
//     ...item
//   }));

//   let paginationInfo = jobList.data.paginationInfo;
//   console.log(jobList.data);
//   /*
//   {
//     "result": true,
//     "data": {
//       "contents": [],
//       "pagination": {
//         "page": 1,
//         "totalCount": 100
//       }
//     }
//   }
//   */

//   gridInstance.value.resetData(rowData.value);
//   gridInstance.value.setPaginationTotalCount(paginationInfo.totalRecordCount);
// };

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
  isShowJobModal.value = false;
  isUpdate.value = '';
  jobGetList();
}

// 업무 수정
let selectedRowData = {};
const modalUpdateJob = () => {
  isUpdate.value = true;
  isShowJobModal.value = true;
}

const modalCloseFunc = (e) => {
  if (e.key === "Escape") {
    if (isShowJobModal.value) {
      isShowJobModal.value = false;
    }
    if (isShowModal.value) {
      isShowModal.value = false;
    }
  }
}

// esc 키 포커스
onMounted(() => {
  document.addEventListener("keydown", modalCloseFunc);
});
onBeforeUnmount(() => {
  document.removeEventListener("keydown", modalCloseFunc);
})
</script>


<style scoped>
.jobBx {
  background-color: gray;
  border-radius: 5px;
  color: white;
  height: 600px;
  margin: 5px 0;
  /* padding: 5px; */
}
ul {
  list-style: none;
  padding-left: 15px;
}
li {
  margin: 5px 0;
}
span {
  font-weight: bold;
  cursor: pointer;
}
.job-box {
  color: blue;
  font-style: italic;
  margin-left: 15px;
}
/* 아이콘 스타일 */
i {
  margin-right: 8px;
}

div::v-deep .nav-link {
  display: flex !important;
}

</style>

