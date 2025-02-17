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
        <div class="mb-3 row">
          <label for="staticEmail" class="col-sm-2 col-form-label">검색</label>
          <div class="col-auto">
            <select class="form-select" aria-label="Default select example">
              <option value="1">프로젝트명</option>
              <option value="2">거래처</option>
              <option value="3">담당자</option>
            </select>
          </div>
          <div class="col-auto">
            <input type="text" class="form-control" placeholder="검색어를 입력해주세요">
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
      </card>
      <card>
        <div class="d-flex">
          <div class="p-2 w90">
            <button class="btn btn-danger btn-fill btn-sm">다중삭제</button>
            <button class="btn btn-excel btn-sm"><img class="me-1" src="../../assets/img/icon/excel.svg" alt="xls">
              엑셀다운로드</button>
          </div>
          <div class="p-2 flex-shrink-1">
            <select class="form-select form-select-sm">
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
                <tr :key="i" v-for="(project, i) in projectList">
                  <td>
                    <div class="form-check">
                      <input class="form-check-input" type="checkbox" id="inlineCheckbox1" value="option1">
                    </div>
                  </td>
                  <td>{{ i + 1 }}</td>
                  <td>진행중</td>
                  <td>
                    <div class="category">{{ project.entrprsMberId }}</div>
                    <div class="subject"><a href="#" @click="modalOpen(project.prCd)" class="mrp5">{{ project.prNm
                        }}</a>
                      <span class="badge badge-danger">D-10</span>
                    </div>
                  </td>
                  <td>{{ dateFormat(project.startDt) }} ~ {{ dateFormat(project.endDt) }}</td>
                  <td>{{ Number(project.price).toLocaleString() }}</td>
                  <td>김지환</td>
                  <td><button class="btn btn-primary btn-sm" onclick="location.href ='/project/plan'">일정등록</button></td>
                  <td>{{ dateFormat(project.createDt) }}</td>
                  <td>
                    <button class="btn btn-success btn-fill btn-sm mr-1">수정</button>
                    <button class="btn btn-danger btn-fill btn-sm mr-1">삭제</button>
                  </td>
                </tr>
              </template>
              <tr v-else>
                <td colspan="10">
                  <div class="list-nodata">등록된 프로젝트가 없습니다.</div>
                </td>
              </tr>
              <!--
              <tr>
                <td>
                  <div class="form-check">
                    <input class="form-check-input" type="checkbox" id="inlineCheckbox1" value="option1">
                  </div>
                </td>
                <td>2</td>
                <td>진행중</td>
                <td>
                  <div class="category">경북자연생태원</div>
                  <div class="subject">프로그램 예약 및 결제 시스템 구축
                    <span class="badge badge-primary">D-20</span>
                  </div>
                </td>
                <td>2024-12-01 ~ 2025-03-31</td>
                <td>30,000,000</td>
                <td>김지환</td>
                <td><button class="btn btn-primary btn-sm">일정등록</button></td>
                <td>2024-12-01</td>
                <td>
                  <button class="btn btn-success btn-fill btn-sm mr-1">수정</button>
                  <button class="btn btn-danger btn-fill btn-sm mr-1">삭제</button>
                </td>
              </tr>
              <tr class="table-secondary">
                <td>
                  <div class="form-check">
                    <input class="form-check-input" type="checkbox" id="inlineCheckbox1" value="option1">
                  </div>
                </td>
                <td>3</td>
                <td>완료</td>
                <td>
                  <div class="category">예담대학교</div>
                  <div class="subject">예담대학교 빌더 관리자페이지 고도화 용역</div>
                </td>
                <td>2024-12-01 ~ 2025-03-31</td>
                <td>30,000,000</td>
                <td>김지환</td>
                <td><button class="btn btn-primary btn-sm">일정등록</button></td>
                <td>2024-12-01</td>
                <td>
                  <button class="btn btn-success btn-fill btn-sm mr-1">수정</button>
                  <button class="btn btn-danger btn-fill btn-sm mr-1">삭제</button>
                </td>
              </tr>-->
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
                    <td class="text-start">{{ projectInfo.entrprs_mber_id }}</td>
                  </tr>
                  <tr>
                    <th class="table-secondary">프로젝트 기간</th>
                    <td class="text-start">{{ dateFormat(projectInfo.startDt) }} ~ {{ dateFormat(projectInfo.endDt) }}
                    </td>
                    <th class="table-secondary">금액</th>
                    <td class="text-start">{{ Number(projectInfo.price).toLocaleString() }}</td>
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
                  <template v-if="projectCount > 0">
                    <tr>
                      <th>1</th>
                      <td>교내 SSO 연동</td>
                      <th>100%</th>
                      <td>완료</td>
                    </tr>
                  </template>
                  <tr v-else>
                    <td colspan="4">
                      <div class="list-nodata">등록된 일정이 없습니다.</div>
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
import Card from '../../components/Cards/Card.vue'
import Modal from '../../components/Modal.vue';

const projectList = ref([]);
const projectInfo = ref([]);
const isShowModal = ref(false);
const projectCount = ref(0);

onBeforeMount(() => {
  projectGetList();
});

//---------------공통함수--------------

const dateFormat = (value) => { //날짜포맷 (yyyy-mm-dd)
  let date = value == null ? new Date() : new Date(value);

  let year = date.getFullYear();
  let month = ('0' + (date.getMonth() + 1)).slice(-2);
  let day = ('0' + date.getDate()).slice(-2);

  let result = year + '-' + month + '-' + day;
  return result;
};

//---------------모달--------------

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

//---------------axios--------------

const projectGetList = async () => { //프로젝트 전체조회
  try {
    const result = await axios.get('/api/project/list');

    projectList.value = result.data;
    projectCount.value = result.data.length;
  } catch (err) {
    projectList.value = [];
  }
}

const projectGetInfo = async (prCd) => { //프로젝트 단건조회
  try {
    const result = await axios.get(`/api/project/info?pr=${prCd}`);

    projectInfo.value = result.data;
  } catch (err) {
    projectInfo.value = [];
  }
}

</script>
