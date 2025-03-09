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

              <div class="depth treeview jobBx">
                <ul class="list-unstyled">
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

                      <div class="card">
                        <div class="card-body">

                          <div class="row">
                            <div class="col">
                              <div class="mb-3">
                                <label>부서명</label>
                                <input type="text" class="form-control" v-model="jobBxData.deptNm" placeholder="부서명" readonly />
                              </div>
                            </div>
                            <div class="col">
                              <div class="mb-3">
                                <label for="indictOrdr">표시순서<em class="point-red">*</em></label>
                                <input id="indictOrdr" type="number" class="form-control" v-model="jobBxData.indictOrdr" />
                              </div>
                            </div>
                          </div>
                          <div class="row">
                            <div class="col">
                              <div class="mb-3">
                                <label for="jobBxNm">업무함명<em class="point-red">*</em></label>
                                <input id="jobBxNm" type="text" class="form-control" v-model="jobBxData.deptJobBxNm" placeholder="업무함명을 입력해주세요." />
                              </div>
                            </div>
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
                    :isDetail="isDetail"
                    @modalCloseJob="modalCloseJob"
                    @modalConfirmJob="modalConfirmJob"
                    :jobBxSelected="jobBxSelected"
                    :selectedRowData="selectedRowData"
                    :jobBoxes="jobBoxes"
                  />
                </div>
                <div class="col d-flex justify-content-end align-items-center">
                  <select class="form-select me-2" v-model="jobSearch.searchCnd" style="width: 100px;">
                    <option value="0">전체</option>
                    <option value="1">업무명</option>
                    <option value="2">업무내용</option>
                    <option value="3">담당자명</option>
                  </select>

                  <input type="text" class="form-control w-auto me-2" placeholder="" id="title-search" v-model="jobSearch.searchWrd">
                  <button class="btn btn-info btn-fill" @click="jobGetList" style="margin: 4 0 !important;">검색</button>
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
import { swalCheck } from '../../assets/js/common.js';
import { priortFormatter, dateFormatter } from '../../assets/js/formatter.js';

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
  jobSearch.value.searchCnd = 0;
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
    const result = await axios.get('/api/department/treeList');
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
  //부서 정보
  jobBxData.value.deptCd = data.deptCd;
  jobBxData.value.deptNm = data.deptNm.trim();
  switch(type) {
    case 'add':
      //초기화
      jobBxData.value.indictOrdr = 0;
      jobBxData.value.deptJobBxNm = '';
      jobBxData.value.deptJobBxId = '';
      modalOpen();
      break;
    case 'modify':
      jobBxData.value.indictOrdr = data.indictOrdr;
      jobBxData.value.deptJobBxId = data.deptJobBxId;
      jobBxData.value.deptJobBxNm = data.deptJobBxNm.trim();
      modalOpen();
      break;
    case 'remove':
      await jobBxRemove(data.deptJobBxId);

      deptGetList();
      await jobBxGetList();

      jobListReset();
      break;
  }

}
provide('jobBxCheck', jobBxCheck);

const jobListReset = () => {
  const relatedJobBoxes = jobBoxes.value.filter(j => j.deptCd === jobBxData.value.deptCd);

  store.dispatch('jobBxSelectedUpdate', { 
    searchDeptCd: jobBxData.value.deptCd, 
    searchDeptNm: jobBxData.value.deptNm,
    searchDeptJobBxId: '',
    searchDeptJobBxNm: '',
  });

  store.dispatch('jobBxListUpdate', relatedJobBoxes);
}

const jobBxRemove = async (deptJobBxId) => {
  let check = await Swal.fire({
    icon: 'question',
    title: '업무도 함께 삭제 하시겠습니까?',
    showCancelButton: true,
    cancelButtonText: '취소',
    confirmButtonText: '확인',
    reverseButtons:true
  });

  if(check.isConfirmed) {
    try {
      await axios.delete('/api/deptstore/jobBxRemove', { params: { deptJobBxId: deptJobBxId } });
    } catch (err) {
      Swal.fire({ icon: "error", title: "업무함 삭제 실패", text: "Error : " + err });
    }
  }
  else {
    return;
  }
}

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

  if(!jobBxValidCheck()) return;

  if(jobBxModalType == 'add') {
    try {
      await axios.post('/api/deptstore/jobBxAdd', formData);
    } catch (err) {
      Swal.fire({ icon: "error", title: "업무함 등록 실패", text: "Error : " + err });
    }

  } else if(jobBxModalType == 'modify') {
    formData.append("deptJobBxId", jobBxData.value.deptJobBxId);

    let check = await swalCheck('수정');
    if(check.isConfirmed) {
      try {
        await axios.post('/api/deptstore/jobBxModify', formData);
      } catch (err) {
        Swal.fire({ icon: "error", title: "업무함 수정 실패", text: "Error : " + err });
      }
    }
    else {
      return;
    }
  }
  deptGetList();
  await jobBxGetList();
  modalClose();
  
  jobListReset();
}

// 업무함 유효성 체크
const jobBxValidCheck = () => {
  if(jobBxData.value.indictOrdr === '') {
    const focusTag = document.querySelector('#indictOrdr');
    focusTag?.focus();
    Swal.fire({
      icon: "info",
      title: "표시순서를 입력하세요.",
    });
    return false;
  }
  if(!jobBxData.value.deptJobBxNm?.trim()) {
    const focusTag = document.querySelector('#jobBxNm');
    focusTag?.focus();
    Swal.fire({
      icon: "info",
      title: "업무함명을 입력하세요.",
    });
    return false;
  }
  return true;
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
    scrollY: false,
    rowHeaders: ['checkbox', 'rowNum'],
    pageOptions: {
      useClient: false,
      perPage: 13,
    },
    columns: [
      { header: '우선순위', name: 'priort', align: 'center', formatter: priortFormatter, width: 80 },
      { header: '업무명', name: 'deptJobNm', sortable: true },
      { header: '담당자', name: 'chargerNm', align: 'center', sortable: true, width: 120 },
      { header: '작성일', name: 'frstRegisterPnttm', align: 'center', sortable: true, formatter: dateFormatter, width: 120 },
      {
        header: '관리', name: 'action', align: 'center',
        renderer: BtnRenderer,
        width: 150 
      }
    ]
  })

  // 행 클릭 이벤트 추가
  gridInstance.value.on("click", handleRowClick);
})

// 행클릭
let isDetail = ref(false);
const handleRowClick = (e) => {
  if (!gridInstance.value || e.rowKey == null || e.rowKey == undefined ) return;
  if (e.nativeEvent.target.type == "checkbox") {
    return;
  }

  isDetail.value = true;
  isUpdate.value = false;
  selectedRowData = gridInstance.value.getRow(e.rowKey);
  isShowJobModal.value = true;
}

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
      event.stopPropagation(); // 부모 요소(행 클릭) 이벤트 전파 방지
      
      const type = event.target.dataset.type;

      // props.row가 없을 경우 grid에서 데이터 가져오기
      const rowKey = props.row?.rowKey ?? props.grid.getRow(props.rowKey)?.rowKey;

      if (rowKey === undefined) {
        return;
      }

      if (type === "edit") {
        modalUpdateJob(rowKey);
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

// 업무 삭제
const delEvent = async (rowKey) => {
  selectedRowData = gridInstance.value.getRow(rowKey);
  
  let check = await swalCheck('삭제');
  if(check.isConfirmed) {
    try {
      await axios.delete('/api/deptstore/jobRemove', { params: { deptJobId: selectedRowData.deptJobId } });
    } catch (err) {
      Swal.fire({ icon: "error", title: "업무 삭제 실패", text: "Error : " + err });
    }
  }
  else {
    return;
  }

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

  if(selectedRows.length == 0) {
    Swal.fire({ icon: "info", title: "삭제할 업무를 선택하세요." });
    return;
  }

  let check = await swalCheck('삭제');
  if(check.isConfirmed) {
    try {
      await axios.post('/api/deptstore/jobListRemove', jobList);
    } catch (err) {
      Swal.fire({ icon: "error", title: "업무 목록 삭제 실패", text: "Error : " + err });
    }
  }
  else {
    return;
  }

  jobGetList();
};

// 모달
let isShowJobModal = ref(false);
let isUpdate = ref(false);

// 업무 등록
const modalAddJob = async () => {
  isUpdate.value = false;
  isDetail.value = false;
  isShowJobModal.value = true;
}
const modalCloseJob = () => {
  isShowJobModal.value = false;
}
const modalConfirmJob = () => {
  isShowJobModal.value = false;
  isUpdate.value = false;
  isDetail.value = false;
  jobGetList();
}

// 업무 수정
let selectedRowData = {};
const modalUpdateJob = (rowKey) => {
  isUpdate.value = true;
  isDetail.value = false;
  selectedRowData = gridInstance.value.getRow(rowKey);
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
div::v-deep .nav-link {
  display: flex !important;
}
.jobBx {
  padding-left: 2px !important;
}

</style>

