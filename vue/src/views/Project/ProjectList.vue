<template>
  <div class="content" @keydown.esc="modalClose">
    <div class="container-fluid">
      <card>
        <h4 class="card-title float-left">프로젝트 조회</h4>
        <button class="btn btn-primary btn-sm btn-fill float-right"
          onclick="location.href ='/project/add?menuCd=15'">프로젝트
          등록</button>
      </card>
      <form @submit.prevent="projectGetList">
        <card>
          <ul class="nav nav-pills">
            <li class="nav-item">
              <a class="nav-link" :class="searchData.searchState == '' ? 'active' : ''"
                @click="searchData.searchState = ''">전체</a>
            </li>
            <li class="nav-item">
              <a class="nav-link" :class="searchData.searchState == '1' ? 'active' : ''"
                @click="searchData.searchState = '1'">진행전</a>
            </li>
            <li class="nav-item">
              <a class="nav-link" :class="searchData.searchState == '2' ? 'active' : ''"
                @click="searchData.searchState = '2'">진행중</a>
            </li>
            <li class="nav-item">
              <a class="nav-link" :class="searchData.searchState == '3' ? 'active' : ''"
                @click="searchData.searchState = '3'">진행완료</a>
            </li>
          </ul>
        </card>
        <card>
          <div class="mb-3 row">
            <label for="staticEmail" class="col-sm-2 col-form-label">검색</label>
            <div class="col-auto">
              <select class="form-select" v-model="searchData.searchCondition">
                <option value="0">프로젝트명</option>
                <option value="1">거래처</option>
                <option value="2">담당자</option>
              </select>
            </div>

            <div class="col-auto">
              <input type="text" class="form-control" placeholder="검색어를 입력해주세요" v-model="searchData.searchKeyword">
            </div>
          </div>
          <div class="mb-3 row">
            <label for="inputPassword" class="col-sm-2 col-form-label">프로젝트 기간</label>
            <div class="col-auto">
              <input type="date" class="form-control" v-model="searchData.searchStartDt">
            </div>
            <div class="col-auto">~</div>
            <div class="col-auto">
              <input type="date" class="form-control" v-model="searchData.searchEndDt">
            </div>
          </div>
          <div class="mb-3 row">
            <label for="inputPassword" class="col-sm-2 col-form-label">프로젝트 금액</label>
            <div class="col-auto">
              <input type="number" class="form-control" v-model="searchData.searchStartPrice">
            </div>
            <div class="col-auto">~</div>
            <div class="col-auto">
              <input type="number" class="form-control" v-model="searchData.searchEndPrice">
            </div>
          </div>
          <div class="text-center">
            <button class="btn btn-secondary btn-fill">
              초기화
            </button>
            <button type="submit" class="btn btn-info btn-fill">
              검색
            </button>
          </div>
        </card>
      </form>
      <card>
        <div class="d-flex">
          <div class="p-2 w90">
            <button class="btn btn-danger btn-fill btn-sm" @click="projectListRemove">다중삭제</button>
            <button class="btn btn-excel btn-sm" @click="exportToExcel"><img class="me-1"
                src="../../assets/img/icon/excel.svg" alt="xls">
              엑셀다운로드</button>
          </div>
        </div>
        <div id="tableGrid" class="toastui project"></div>
      </card>

      <!--프로젝트 상세보기 모달[s]-->
      <Modal :isShowModal="isShowModal" :modalTitle="'프로젝트 상세보기'" @click.self="modalClose">
        <template v-slot:body>
          <card>
            <p class="card-title mb-2">프로젝트 정보</p>

            <div class="table-responsive">
              <table class="table table-hover project">
                <colgroup><col width="10%"><col><col width="10%"><col></colgroup>
                <tbody>
                  <tr>
                    <th class="table-secondary">프로젝트명</th>
                    <td class="text-start">{{ projectInfo.prNm }}</td>
                    <th class="table-secondary">거래처명</th>
                    <td class="text-start">{{ projectInfo.comNm }}</td>
                  </tr>
                  <tr>
                    <th class="table-secondary">프로젝트 기간</th>

                    <td class="text-start">{{ dateFormat(projectInfo.startDt) }} ~ {{ dateFormat(projectInfo.endDt) }}
                    </td>
                    <th class="table-secondary">금액</th>
                    <td class="text-start">{{ projectInfo.price ? projectInfo.price : "-" }}</td>
                  </tr>
                  <tr>
                    <th class="table-secondary">참여자</th>
                    <td colspan="3" class="text-start">김민진, 박주현, 신강현</td>
                  </tr>
                  <tr>
                    <th class="table-secondary">진행상태</th>
                    <td colspan="3" class="text-start">{{ projectInfo.state == 'A03' ? '진행중' : '완료' }}</td>
                  </tr>
                </tbody>
              </table>
            </div>
          </card>

          <card>
            <p class="card-title mb-2">프로젝트 과업</p>
            <div class="progress-bar ">
              <div class="progress-bar-fill " :style="'width:' + progress + '%'">{{ Math.floor(workProgress) }}%</div>
            </div>
            <div class="table-responsive" :style="workCount > 0 ? 'max-height: 200px; overflow-y: auto;' : ''">
              <table class="table table-hover project">
                <colgroup><col width="10%"><col width="40%"><col><col></colgroup>
                <thead class="table-secondary">
                  <tr>
                    <th>순서</th>
                    <th>과업명</th>
                    <th>진행률</th>
                    <th>상태</th>
                  </tr>
                </thead>
                <tbody>
                  <template v-if="workCount > 0">
                    <tr :key="i" v-for="(work, i) in workList">
                      <td>{{ i + 1 }}</td>
                      <td>{{ work.prWorkNm }}</td>
                      <td>{{ work.progress }}%</td>
                      <td :class="work.state == 'A02' ? 'point-red' : 'point-blue'">{{ work.state == 'A02' ? '미완료' :
                        '완료' }}</td>
                    </tr>
                  </template>
                  <tr v-else class="list-nodata">
                    <td colspan="4">
                      <div>등록된 일정이 없습니다.</div>
                    </td>
                  </tr>
                </tbody>
              </table>
            </div>
          </card>
        </template>

        <template v-slot:footer>
          <button type="button" class="btn btn-secondary btn-fill" @click="modalClose">닫기</button>
        </template>
      </Modal>
      <!--프로젝트 상세보기 모달[e]-->

    </div>
  </div>
</template>

<script setup>
import { useStore } from "vuex";
import axios from "../../assets/js/customAxios";
import { useRouter } from "vue-router";
import { onMounted, ref, computed, watch, onBeforeUnmount } from 'vue';

//========================== 컴포넌트 ==========================
import Grid from 'tui-grid';
import Swal from 'sweetalert2';
import Modal from '../../components/Modal.vue';
import Card from '../../components/Cards/Card.vue'

//============================= js =============================
import { dateFormat } from '../../assets/js/common'
import { dateTermCalc } from '../../assets/js/project'

//========================= 공통함수 =========================

const store = useStore();
const isCmt = computed(() => store.state.isCmt);
watch(isCmt, () => {
  projectGetList();
})

// 진행률 애니메이션 실행 함수 (requestAnimationFrame 사용)
const progress = ref(0);
const animateProgress = (endValue) => {
  let startTime = null;
  const duration = 2000; // 애니메이션 지속 시간 (1초)

  const step = (timestamp) => {
    if (!startTime) startTime = timestamp;
    const elapsed = timestamp - startTime;
    const progressValue = Math.min((elapsed / duration) * endValue, endValue);

    progress.value = progressValue.toFixed(2); // 소수점 제거

    if (elapsed < duration) {
      requestAnimationFrame(step);
    }
  };

  requestAnimationFrame(step);
};

//========================= Toast grid =========================

//프로젝트 전체조회
let grid = ref();
const projectGetList = () => {
  grid.value.readData(1, searchData.value);
}

//검색조건
const searchData = ref({
  searchState: '',
  searchCondition: '0',
  searchKeyword: '',
  searchStartDt: '',
  searchEndDt: '',
  searchStartPrice: '',
  searchEndPrice: ''
});

//검색조건이 변경되면 리스트가 새로 로드됨
watch(searchData, () => {
  projectGetList();
}, { deep: true });

const token = localStorage.getItem("token");
const dataSource = {
  api: {
    readData: {
      url: '/api/project/list',
      method: 'GET',
      initParams: searchData,
      headers: { 'Authorization': `Bearer ${token}` }
    }
  }
};

//리스트 초기화
onMounted(() => {

  if (grid.value) {
    grid.value.destroy(); // 기존 Grid 제거
  }

  grid.value = new Grid({
    el: document.getElementById("tableGrid"),
    data: dataSource,
    scrollX: true,
    scrollY: true,
    columns: [
      { header: "진행상태", name: "state", align: "center", width: 80, formatter: ({ row }) => `${row.state == 'A03' ? '진행중' : '완료'}` },
      { header: "프로젝트명", name: "prNm", align: "center", renderer: subjectRenderer },
      {
        header: "프로젝트 기간", name: "startDt", align: "center", width: 200,
        formatter: ({ row }) => dateFormat(row.startDt) + ' ~ ' + dateFormat(row.endDt)
      },
      { header: "금액", name: "price", align: "center", width: 120, },
      { header: "일정", name: "plan", align: "center", width: 100, renderer: BtnRendererPlan },
      { header: "완료처리", name: "end", align: "center", width: 100, renderer: BtnRendererEnd },
      { header: "등록일", name: "createDt", align: "center", width: 100, formatter: ({ row }) => dateFormat(row.createDt) },
      { header: "관리", name: "managementSetting", align: "center", width: 150, renderer: BtnRendererSetting }
    ],
    pageOptions: {
      useClient: false,
      perPage: 15,
    },
    rowHeight: 50,
    rowHeaders: ["checkbox"]
  });
});

// 페이지 이동 시 Grid 제거하여 중복 방지
onBeforeUnmount(() => {
  if (grid.value) {
    grid.value.destroy();
    grid.value = null;
  }
});

//리스트 엑셀다운로드
const exportToExcel = () => {
  if (grid.value) {
    grid.value.export("xlsx", {
      fileName: "프로젝트 내역 조회_" + dateFormat(),
      useFormattedValue: true,
      onlySelected: false,
      includeHiddenColumns: false,
      columnNames: ['state', 'prNm', 'startDt', 'price', 'createDt'] 
    });
  }
};

//===================== Toast Grid Rendere =====================

//프로젝트명
class subjectRenderer {
  constructor(props) {
    const termClass = ref('');
    const rowKey = props.row?.rowKey ?? props.grid.getRow(props.rowKey)?.rowKey;
    const rowData = props.grid.getRow(rowKey);

    const el = document.createElement("div");
    el.className = "mlp10";

    //남은 기간이 10일 이하인 경우 경고배지 적용
    let term = dateTermCalc(dateFormat(rowData.endDt), dateFormat()) * (-1);
    
    if(rowData.state == 'A04'){
      termClass.value = 'badge-secondary';
    }else{
      term < 10 ? termClass.value = 'badge-danger': termClass.value = 'badge-primary';
    }

    el.innerHTML = `
      <div class="category">${rowData.comNm}</div>
      <div class="subject">
        <a href="#" class="mrp5">${rowData.prNm}</a>
        <span class="badge ${termClass.value}">
          ${rowData.state == 'A04' ? '완료'
          : term > 0 ? "D-" + term
          : term < 0 ? "D+" + term * (-1)
          : "D-day"}</span>
      </div>
    `;

    el.addEventListener("click", () => {
      modalOpen(rowData.prCd); //제목 클릭 시 상세보기 모달 표출
    });

    this.el = el;
  }

  getElement() {
    return this.el;
  }
}

//프로젝트 수정/삭제 버튼
class BtnRendererSetting {
  constructor(props) {
    const rowKey = props.row?.rowKey ?? props.grid.getRow(props.rowKey)?.rowKey;
    const rowData = props.grid.getRow(rowKey);

    const el = document.createElement("div");

    el.innerHTML = `
      <button class="btn btn-success btn-fill btn-sm mr-1" data-type="edit">수정</button>
      <button class="btn btn-danger btn-fill btn-sm mr-1" data-type="del">삭제</button>
    `;

    el.addEventListener("click", (event) => {
      const type = event.target.dataset.type;

      if (type === "edit") { //수정버튼 클릭 시 수정페이지로 이동
        if (rowData.state == 'A03') {
          btnPageMove("add", rowData.prCd);
        } else {
          Swal.fire({
            icon: "error",
            title: "수정불가",
            text: "완료된 프로젝트는 수정할 수 없습니다."
          });
        }
      } else if (type === "del") { //삭제버튼 클릭 시 삭제처리
        btnProjectRemove(rowData.prCd);
      }
    });

    this.el = el;
  }

  getElement() {
    return this.el;
  }
}

//프로젝트 일정관리 버튼
class BtnRendererPlan {
  constructor(props) {
    const rowKey = props.row?.rowKey ?? props.grid.getRow(props.rowKey)?.rowKey;
    const rowData = props.grid.getRow(rowKey);
    const el = document.createElement("div");

    el.innerHTML = `
      <button class="btn btn-primary btn-sm">일정관리</button>
    `;

    el.addEventListener("click", () => {
      btnPageMove('plan', rowData.prCd); //일정관리 버튼 클릭 시 일정관리 페이지로 이동
    });

    this.el = el;
  }

  getElement() {
    return this.el;
  }
}

//프로젝트 완료처리관리 버튼
class BtnRendererEnd {
  constructor(props) {
    const rowKey = props.row?.rowKey ?? props.grid.getRow(props.rowKey)?.rowKey;
    const rowData = props.grid.getRow(rowKey);
    const el = document.createElement("div");

    if (rowData.state == 'A03') { //미완료 상태일 때 완료처리하기
      el.innerHTML = `
        <button class="btn btn-success btn-sm">완료처리</button>
      `;
    } else { //완료 상태일 때 완료취소 처리하기
      el.innerHTML = `
        <button class="btn btn-secondary btn-sm">완료처리됨</button>
      `;
    }

    el.addEventListener("click", () => {
      btnProjectEnd(rowData.prCd, rowData.state); //완료처리 버튼 클릭 시 완료처리 함수 수행
    });

    this.el = el;
  }

  getElement() {
    return this.el;
  }
}

//========================= 모달 =========================

const isShowModal = ref(false);

//프로젝트 정보 모달 열기
const modalOpen = (prCd) => {
  isShowModal.value = true;
  projectGetInfo(prCd);
}

//프로젝트 정보 모달 닫기
const modalClose = (e) => {
  if (e.key === "Escape") {
    if (isShowModal.value) {
      isShowModal.value = !isShowModal.value
    }
  } else {
    isShowModal.value = false;
  }
}

//======================= 버튼이벤트 =======================

const router = useRouter();

//수정/일정관리 페이지로 이동
const btnPageMove = (mode, code) => {
  router.push({ path: `/project/${mode}`, query: { prCd: code } });
}

// 프로젝트 삭제 버튼
const btnProjectRemove = (code) => {
  Swal.fire({
    title: "해당 프로젝트를 삭제 하시겠습니까?",
    icon: "question",
    showCancelButton: true,
    customClass: {
      confirmButton: "btn btn-secondary btn-fill",
      cancelButton: "btn btn-danger btn-fill"
    },
    confirmButtonText: "닫기",
    cancelButtonText: "삭제",
  }).then((result) => {
    if (result.dismiss == Swal.DismissReason.cancel) {
      projectRemove(code); //삭제처리 함수
    }
  });
}

const btnProjectEnd = (code, mode) => {
  if (mode == 'A03') { //미완료 상태일 때 완료처리하기
    Swal.fire({
      title: "프로젝트를 완료처리하시겠습니까?",
      text: "완료처리 시 프로젝트 정보 및 일정을 수정 할 수 없습니다.",
      icon: "question",
      showCancelButton: true,
      customClass: {
        confirmButton: "btn btn-secondary btn-fill",
        cancelButton: "btn btn-primary btn-fill"
      },
      confirmButtonText: "아니오",
      cancelButtonText: "확인",
    }).then((result) => {
      if (result.dismiss == Swal.DismissReason.cancel) {
        projectEnd(code, mode); //완료처리 함수
      }
    });
  } else { //완료 상태일 때 완료취소 처리하기
    Swal.fire({
      title: "완료된 상태를 취소하시겠습니까?",
      icon: "question",
      showCancelButton: true,
      customClass: {
        confirmButton: "btn btn-secondary btn-fill",
        cancelButton: "btn btn-primary btn-fill"
      },
      confirmButtonText: "아니오",
      cancelButtonText: "확인",
    }).then((result) => {
      if (result.dismiss == Swal.DismissReason.cancel) {
        projectEnd(code, mode); //완료처리 함수
      }
    });
  }
}

//======================= axios =======================

//프로젝트 전체조회
/*
const projectList = ref([]);
const projectCount = ref(0);

const projectGetList = async () => {
  try {
    const result = await axios.get(`/api/project/list`, { params: searchData.value });
    projectCount.value = result.data.length;
    projectList.value = result.data.map(item => ({
      ...item,
      startDt: dateFormat(item.startDt),
      endDt: dateFormat(item.endDt),
      term: dateTermCalc(dateFormat(item.endDt), dateFormat()) //프로젝트 종료일까지 남은기간
    }));

    grid.value.resetData(projectList.value); //toast grid로 데이터 전달

    await nextTick(); //toast grid 로드될때까지 기다림
    projectList.value.forEach((row, index) => { //toast grid 로드완료 후 상태별 rowClass 부여
      if (row.state == "A03") {
        grid.value.addRowClassName(index, "table-white");
      } else {
        grid.value.addRowClassName(index, "table-end");
      }
    });

  } catch (err) {
    projectList.value = [];

    Swal.fire({
      icon: "error",
      title: "API 조회 오류",
      text: "Error : " + err
    });
  }
}*/

//프로젝트 단건조회
const projectInfo = ref([]);
const projectGetInfo = async (prCd) => {
  try {
    const result = await axios.get(`/api/project/info/${prCd}`);
    projectInfo.value = result.data.info;
    projectWorkGetList(prCd); //프로젝트 과업리스트 조회
  } catch (err) {
    projectInfo.value = [];

    Swal.fire({
      icon: "error",
      title: "API 조회 오류",
      text: "Error : " + err
    });
  }
}

//프로젝트 단건삭제
const projectRemove = async (prCd) => {

  try {
    const response = await axios.delete(`/api/project/${prCd}`);

    if (response.data === true) {
      Swal.fire({
        icon: "success",
        title: "삭제완료",
        text: "선택한 프로젝트를 삭제하였습니다",
      })
      projectGetList(); //삭제완료 후 리스트 리로드*-+-
    }
  } catch (err) {
    Swal.fire({
      icon: "error",
      title: "삭제 실패",
      text: "Error : " + err
    });
  }
}

//프로젝트 다중삭제
const projectListRemove = async () => {
  const checkedData = grid.value.getCheckedRows();

  try {
    const response = ref([]);
    response.value = await axios.put(`/api/project/delete`, {
      projectArr: checkedData.map(row => row.prCd)
    });

    if (response.value.statusText == "OK") {
      Swal.fire({
        icon: "success",
        title: "삭제완료",
        text: "선택한 프로젝트를 삭제하였습니다",
      })
      projectGetList(); //삭제완료 후 리스트 리로드
    }
  } catch (err) {
    Swal.fire({
      icon: "error",
      title: "삭제 실패",
      text: "Error : " + err
    });
  }
}

//프로젝트 진행상태 변경
const projectEnd = async (code, mode) => {

  mode == 'A03' ? mode = 'A04' : mode = 'A03'; //미완료일때 완료로, 완료일때 미완료로 

  try {
    const requestData = {
      prCd: code,
      state: mode
    };

    const response = ref([]);
    response.value = await axios.put(`/api/project`, requestData);

    if (response.value.data === true) {
      Swal.fire({
        icon: "success",
        title: "상태변경 완료",
      });
      projectGetList();
    }

  } catch (err) {
    Swal.fire({
      icon: "error",
      title: "상태변경 실패",
    })
  }
}

//프로젝트 과업조회
const workProgress = ref('');
const workList = ref([]);
const workCount = ref(0);
const projectWorkGetList = async (prCd) => {
  try {
    const result = await axios.get(`/api/project/work/${prCd}`);

    workList.value = result.data;
    workCount.value = result.data.length;

    //프로젝트 진행률 퍼센트 계산
    if (workCount.value > 0) {
      workProgress.value = (((workList.value.reduce((sum, item) => sum + (item.progress || 0), 0)) / workCount.value) / 100) * 100;
    } else {
      workProgress.value = 0;
    }

    setTimeout(() => {
      animateProgress(workProgress.value); // 원하는 진행률 설정
    }, 100); // 0.5초 후 시작
  } catch (err) {
    workList.value = [];
  }
}
</script>