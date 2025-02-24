<template>
  <div class="content" @keydown.esc="modalClose">
    <div class="container-fluid">
      <card>
        <h4 class="card-title float-left">프로젝트 조회</h4>
        <button class="btn btn-primary btn-sm btn-fill float-right" onclick="location.href ='/project/add'">프로젝트
          등록</button>
      </card>
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
        <form name="searchForm" method="get" action="/project/list">
          <div class="mb-3 row">
            <label for="staticEmail" class="col-sm-2 col-form-label">검색</label>
            <div class="col-auto">
              <select class="form-select" name="searchCondition">
                <option value="0">프로젝트명</option>
                <option value="1">거래처</option>
                <option value="2">담당자</option>
              </select>
            </div>
            <div class="col-auto">
              <input type="text" class="form-control" placeholder="검색어를 입력해주세요" name="searchKeyword">
            </div>
          </div>
          <div class="mb-3 row">
            <label for="inputPassword" class="col-sm-2 col-form-label">프로젝트 기간</label>
            <div class="col-auto">
              <input type="date" name="START_DT" class="form-control">
            </div>
            <div class="col-auto">~</div>
            <div class="col-auto">
              <input type="date" name="END_DT" class="form-control">
            </div>
          </div>
          <div class="mb-3 row">
            <label for="inputPassword" class="col-sm-2 col-form-label">프로젝트 금액</label>
            <div class="col-auto">
              <input type="number" name="PRICE_ST" class="form-control">
            </div>
            <div class="col-auto">~</div>
            <div class="col-auto">
              <input type="number" name="PRICE_END" class="form-control">
            </div>
          </div>
          <div class="text-center">
            <button type="submit" class="btn btn-secondary btn-fill">
              초기화
            </button>
            <button class="btn btn-info btn-fill">
              검색
            </button>
          </div>
        </form>
      </card>
      <card>
        <div class="d-flex">
          <div class="p-2 w90">
            <button class="btn btn-danger btn-fill btn-sm">다중삭제</button>
            <button class="btn btn-excel btn-sm"><img class="me-1" src="../../assets/img/icon/excel.svg" alt="xls">
              엑셀다운로드</button>
          </div>
          <div class="p-2 flex-shrink-1">
            <select class="form-select form-select-sm mt-1">
              <option value="PA">프로젝트명 오름차순</option>
              <option value="PD">프로젝트명 내림차순</option>
              <option value="DA">프로젝트 기간 최신순</option>
              <option value="DD">프로젝트 기간 오래된순</option>
            </select>
          </div>
        </div>
        <div class="table-responsive">
          <table class="table table-hover project">
            <thead class="table-light">
              <tr>
                <th>
                  <div class="form-check">
                    <input class="form-check-input" type="checkbox" id="inlineCheckbox1" value="option1">
                  </div>
                </th>
                <th>번호</th>
                <th>진행상태</th>
                <th>프로젝트명</th>
                <th>프로젝트 기간</th>
                <th>금액</th>
                <th>담당자</th>
                <th>일정</th>
                <th>등록일</th>
                <th>관리</th>
              </tr>
            </thead>
            <tbody>
              <template v-if="projectCount > 0">
                <tr :key="i" v-for="(project, i) in projectList"
                  :class="projectInfo.state == 'A04' ? 'table-secondary' : ''">
                  <td>
                    <div class="form-check">
                      <input class="form-check-input" type="checkbox" id="inlineCheckbox1" value="option1">
                    </div>
                  </td>
                  <td>{{ i + 1 }}</td>
                  <td>{{ projectInfo.state == 'A04' ? '완료' : '진행중' }}</td>
                  <td>
                    <div class="category">{{ project.comNm }}</div>
                    <div class="subject"><a href="#" @click="modalOpen(project.prCd)" class="mrp5">{{ project.prNm
                        }}</a>
                      <span class="badge" :class="project.term > 10 ? 'badge-primary' : 'badge-danger'">
                        D{{ project.term > 0 ? "-" + project.term : "+" + project.term * (-1) }}</span>
                    </div>
                  </td>
                  <td>{{ project.startDt }} ~ {{ project.endDt }}</td>
                  <td>{{ project.price ? project.price : "-" }}</td>
                  <td>{{ project.entrprsMberId }}</td>
                  <td><button class="btn btn-primary btn-sm" @click="btnPageMove('plan', project.prCd)">일정관리</button>
                  </td>
                  <td>{{ dateFormat(project.createDt) }}</td>
                  <td>
                    <button class="btn btn-success btn-fill btn-sm mr-1"
                      @click="btnPageMove('add', project.prCd)">수정</button>
                    <button class="btn btn-danger btn-fill btn-sm mr-1"
                      @click="btnProjectRemove(project.prCd)">삭제</button>
                  </td>
                </tr>
              </template>
              <tr v-else class="list-nodata">
                <td colspan="10">
                  <div>등록된 프로젝트가 없습니다.</div>
                </td>
              </tr>
            </tbody>
          </table>
        </div>
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
import { onBeforeMount, ref } from 'vue';
import { useRouter } from "vue-router";

//---------------컴포넌트-------------- 
import Swal from 'sweetalert2';
import Card from '../../components/Cards/Card.vue'
import Modal from '../../components/Modal.vue';

//---------------js-------------- 
import { dateFormat } from '../../assets/js/common'
import { dateTermCalc } from '../../assets/js/project'



//---------------데이터-------------- 

onBeforeMount(() => {
  projectGetList();
});

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
  try {
    const result = await axios.get('/api/project/list');

    projectCount.value = result.data.length;
    projectList.value = result.data.map(item => ({
      ...item,
      startDt: dateFormat(item.startDt),
      endDt: dateFormat(item.endDt),
      term: dateTermCalc('', dateFormat(item.endDt))
    }));

  } catch (err) {
    projectList.value = [];

    Swal.fire({
      icon: "error",
      title: "API 조회 오류",
      text: "Error : " + err
    });
  }
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
