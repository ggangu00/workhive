<template>
  <div class="content" @keydown.esc="modalClose">
    <div class="container-fluid">
      <card>
        <h4 class="card-title float-left">프로젝트 조회</h4>
        <button class="btn btn-primary btn-sm btn-fill float-right" onclick="location.href ='/project/add'">프로젝트
          등록</button>
      </card>
      <form @submit.prevent="projectGetList">
        <card>
          <ul class="nav nav-pills">
            <li class="nav-item">
              <a class="nav-link active" aria-current="page">전체(100)</a>
            </li>
            <li class="nav-item">
              <a class="nav-link">진행전(30)</a>
            </li>
            <li class="nav-item">
              <a class="nav-link">진행중(20)</a>
            </li>
            <li class="nav-item">
              <a class="nav-link">진행완료(50)</a>
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
              <input type="date" class="form-control" v-model="searchData.startDt">
            </div>
            <div class="col-auto">~</div>
            <div class="col-auto">
              <input type="date" class="form-control" v-model="searchData.endDt">
            </div>
          </div>
          <div class="mb-3 row">
            <label for="inputPassword" class="col-sm-2 col-form-label">프로젝트 금액</label>
            <div class="col-auto">
              <input type="number" class="form-control" v-model="searchData.priceSt">
            </div>
            <div class="col-auto">~</div>
            <div class="col-auto">
              <input type="number" class="form-control" v-model="searchDatapriceEnd">
            </div>
          </div>
          <div class="text-center">
            <button type="reset" class="btn btn-secondary btn-fill">
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
            <button class="btn btn-excel btn-sm"><img class="me-1" src="../../assets/img/icon/excel.svg" alt="xls">
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
                <tbody>
                  <tr>
                    <th class="table-secondary">프로젝트명</th>
                    <td class="text-start">{{ projectInfo.prNm }}</td>
                    <th class="table-secondary">거래처명</th>
                    <td class="text-start">{{ projectInfo.entrprsMberId }}</td>
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
                </tbody>
              </table>
            </div>
          </card>

          <card>
            <p class="card-title mb-2">프로젝트 과업</p>

            <div class="table-responsive">
              <table class="table table-hover project">
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
                      <th>{{ i + 1 }}</th>
                      <td>{{ work.prWorkNm }}</td>
                      <th>{{ work.progress }}%</th>
                      <td>{{ work.state == 'A02' ? '미완료' : '완료' }}</td>
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
import axios from "axios";
import { onMounted, ref, watch } from 'vue';
import { useRouter } from "vue-router";

//---------------컴포넌트-------------- 
import Swal from 'sweetalert2';
import Grid from 'tui-grid';
import Card from '../../components/Cards/Card.vue'
import Modal from '../../components/Modal.vue';

//---------------js-------------- 
import { dateFormat } from '../../assets/js/common'
import { dateTermCalc } from '../../assets/js/project'

//---------------데이터-------------- 

//검색조건
const searchData = ref({
  searchCondition: "",
  searchKeyword: '',
  startDt: '',
  endDt: '',
  priceSt: '',
  priceEnd: ''
});

const grid = ref([]);
watch(() => searchData, () => {
  projectGetList();
}, { deep: true });

onMounted(() => {
  projectGetList();

  grid.value = new Grid({
    el: document.getElementById("tableGrid"),
    scrollX: true,
    scrollY: true,
    columns: [
      { header: "진행상태", name: "state", align: "center", width: 80, formatter: ({ row }) => `${row.state == 'A04' ? '완료' : '진행중'}` },
      { header: "프로젝트명", name: "prNm", align: "center", renderer: subjectRenderer },
      { header: "프로젝트 기간", name: "startDt", align: "center", width: 200, formatter: ({ row }) => `${row.startDt} ~ ${row.endDt}` },
      { header: "금액", name: "price", align: "center", width: 120, },
      { header: "일정", name: "plan", align: "center", width: 100, renderer: BtnRendererPlan },
      { header: "등록일", name: "createDt", align: "center", width: 100, formatter: ({ row }) => dateFormat(row.createDt) },
      { header: "관리", name: "managementSetting", align: "center", width: 150, renderer: BtnRendererSetting }
    ],
    pageOptions: {
      useClient: false,
      perPage: 5,
    },
    rowHeaders: ["checkbox"],
    data: projectList.value,
    rowHeight: 50,
    rowClassName: (row) => {
      return row.state == 'A04' ? 'table-white' : 'table-end';
    }
  });
});


//--------------그리드 렌더링-------------

//프로젝트 제목 
class subjectRenderer {
  constructor(props) {
    const rowKey = props.row?.rowKey ?? props.grid.getRow(props.rowKey)?.rowKey;
    const rowData = props.grid.getRow(rowKey);

    const el = document.createElement("div");
    el.className = "mlp10";
    const termClass = ref('');
    rowData.term > 10 ? termClass.value = 'badge-primary' : termClass.value = 'badge-danger';

    el.innerHTML = `
      <div class="category">${rowData.comNm}</div>
      <div class="subject">
        <a href="#" class="mrp5">${rowData.prNm}</a>
        <span class="badge ${termClass.value}">
          D${rowData.term > 0 ? "+" + rowData.term : rowData.term}</span>
      </div>
    `;

    el.addEventListener("click", () => {
      modalOpen(rowData.prCd)
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

    const el = document.createElement("div");

    el.innerHTML = `
      <button class="btn btn-success btn-fill btn-sm mr-1" data-type="edit">수정</button>
      <button class="btn btn-danger btn-fill btn-sm mr-1" data-type="del">삭제</button>
    `;

    el.addEventListener("click", (event) => {
      const type = event.target.dataset.type;
      const rowKey = props.row?.rowKey ?? props.grid.getRow(props.rowKey)?.rowKey;

      const rowData = props.grid.getRow(rowKey);

      if (type === "edit") {
        btnPageMove("add", rowData.prCd);
      } else if (type === "del") {
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

    const el = document.createElement("div");

    el.innerHTML = `
      <button class="btn btn-primary btn-sm">일정관리</button>
    `;

    el.addEventListener("click", () => {
      const rowKey = props.row?.rowKey ?? props.grid.getRow(props.rowKey)?.rowKey;

      if (rowKey === undefined) {
        console.error("❌ BtnRenderer: rowKey를 가져올 수 없습니다.", props);
        return;
      }

      const rowData = props.grid.getRow(rowKey);

      btnPageMove('plan', rowData.prCd)
    });

    this.el = el;
  }

  getElement() {
    return this.el;
  }
}

//---------------모달--------------

const isShowModal = ref(false);
const modalOpen = (prCd) => { //프로젝트 정보 모달 열기
  isShowModal.value = true;
  projectGetInfo(prCd);
}

const modalClose = (e) => { //프로젝트 정보 모달 닫기
  if (e.key === "Escape") {
    if (isShowModal.value) {
      isShowModal.value = !isShowModal.value
    }
  } else {
    isShowModal.value = false;
  }
}

//-------------버튼이벤트------------
const router = useRouter();
const btnPageMove = (mode, code) => { //수정/일정관리 페이지로
  router.push({ path: `/project/${mode}`, query: { prCd: code } });
}

// 프로젝트 삭제 버튼
const btnProjectRemove = (code) => {
  Swal.fire({
    title: "해당 프로젝트를 삭제 하시겠습니까?",
    icon: "question",
    showCancelButton: true,
    customClass: {
      confirmButton: "btn btn-danger btn-fill",
      cancelButton: "btn btn-secondary btn-fill"
    },
    cancelButtonText: "닫기",
    confirmButtonText: "삭제",
  }).then((result) => {
    if (result.isConfirmed) {
      // 프로젝트 삭제
      projectRemove(code);
    }
  });
}

//---------------axios--------------

const projectList = ref([]);
const projectCount = ref(0);
const projectGetList = async () => { //프로젝트 전체조회

  //const params = new URLSearchParams({ searchKeyword: searchKeyword.value }).toString();

  try {
    const result = await axios.get(`/api/project/list`, { params: searchData.value });
    projectCount.value = result.data.length;
    projectList.value = result.data.map(item => ({
      ...item,
      startDt: dateFormat(item.startDt),
      endDt: dateFormat(item.endDt),
      term: dateTermCalc(dateFormat(item.endDt), dateFormat())
    }));

  } catch (err) {
    projectList.value = [];

    Swal.fire({
      icon: "error",
      title: "API 조회 오류",
      text: "Error : " + err
    });
  }

  grid.value.resetData(projectList.value);
}

const projectInfo = ref([]);
const projectGetInfo = async (prCd) => { //프로젝트 단건조회
  try {
    const result = await axios.get(`/api/project/info/${prCd}`);
    projectInfo.value = result.data.info;
    projectWorkGetList(prCd);
  } catch (err) {
    projectInfo.value = [];

    Swal.fire({
      icon: "error",
      title: "API 조회 오류",
      text: "Error : " + err
    });
  }
}

const projectRemove = async (prCd) => { //프로젝트 삭제

  try {
    const response = await axios.delete(`/api/project/${prCd}`);

    if (response.data.result === true) {
      Swal.fire({
        icon: "success",
        title: "삭제완료",
        text: "선택한 프로젝트를 삭제하였습니다",
      })
      projectList.value = response.data.list;
    }
  } catch (err) {
    Swal.fire({
      icon: "error",
      title: "삭제 실패",
      text: "Error : " + err
    });
  }
}

const projectListRemove = async () => { //프로젝트 삭제
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
    }
  } catch (err) {
    Swal.fire({
      icon: "error",
      title: "삭제 실패",
      text: "Error : " + err
    });
  }
}

const workList = ref([]);
const workCount = ref(0);
const projectWorkGetList = async (prCd) => { //프로젝트 과업조회
  try {
    const result = await axios.get(`/api/project/work/${prCd}`);

    workList.value = result.data;
    workCount.value = result.data.length;
  } catch (err) {
    workList.value = [];
  }
}
</script>
