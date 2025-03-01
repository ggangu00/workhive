<template>
  <div class="content">
    <div class="container-fluid">
      <card>
        <h4 class="card-title float-left">프로젝트 등록</h4>
        <button class="btn btn-sm btn-fill float-right" :class="isUpdated ? 'btn-success' : 'btn-primary'"
          @click="projectAdd">{{ txt }}</button>
        <button class="btn btn-secondary btn-sm btn-fill float-right" @click="formReset">초기화</button>
      </card>
      <input type="hidden" :name="createId" v-model="createId">
      <div class="card">
        <div class="card-header">
          <h5 class="card-title">1. 프로젝트 정보</h5>
        </div>
        <div class="card-body">
          <div class="mb-3">
            <div class="form-group has-label">
              <label>프로젝트명 <em class="point-red">*</em></label>
            </div>
            <input type="text" v-model="prNm" class="form-control" placeholder="프로젝트명을 입력해주세요">
          </div>
          <div class="mb-3">
            <label class="form-label">프로젝트 구분 <em class="point-red">*</em></label>
            <select class="form-select w30" v-model="typeCd" aria-label="Default select example">
              <option value="A03">내부프로젝트</option>
              <option value="A04">외부프로젝트</option>
            </select>
          </div>
          <div class="mb-3">
            <label class="form-label">프로젝트 기간 <em class="point-red">*</em></label>
            <div class="row">
              <div class="col-auto">
                <input type="date" v-model="startDt" :max="endDt" class="form-control">
              </div>
              <div class="col-auto p-none">~</div>
              <div class="col-auto">
                <input type="date" v-model="endDt" :min="startDt" class="form-control">
              </div>
            </div>
          </div>
          <div class="mb-3">
            <label class="form-label">프로젝트 금액</label>
            <div class="row">
              <div class="col-auto">
                <input type="text" v-model="price" class="form-control" placeholder="0" style="text-align: right;"
                  @keyup="numberAutoFormat()">
              </div>
              <div class="col-auto p-none">
                <span class="form-text">
                  원
                </span>
              </div>
            </div>
          </div>
          <div class="mb-3">
            <label class="form-label">내부완료일</label>
            <input type="date" v-model="aheadDt" class="form-control w30">
          </div>
          <div class="mb-3">
            <label class="form-label">거래처</label>
            <div class="row">
              <div class="col-auto">
                <input type="hidden" v-model="entrprsMberId">
                <input type="text" v-model="cmpnyNm" class="form-control" placeholder="거래처를 선택해주세요" readonly>
              </div>
              <div class="col-auto p-none">
                <button class="btn btn-info btn-fill" @click="modalOpen">검색</button>
              </div>
            </div>
          </div>
        </div>
      </div>

      <div class="card">
        <div class="card-body">
          <h5 class="card-title float-left mt-1">2. 프로젝트 과업</h5>
        </div>


        <div class="card-body">
          <button class="btn btn-danger btn-sm btn-fill float-right" @click="btnWorkRemove">선택삭제</button>
          <button class="btn btn-primary btn-sm btn-fill float-right" @click="btnWorkAdd">과업추가</button>

          <div id="jobGrid"></div>
        </div>
      </div>
    </div>
  </div>

  <!--프로젝트 상세보기 모달[s]-->
  <Modal :isShowModal="isShowModal" :modalTitle="'거래처 선택'" @click.self="modalClose">
    <template v-slot:body>
      <card>
        <p class="card-title mb-2">거래처 목록</p>
        <div class="table-responsive">
          <table class="table table-hover project">
            <thead class="table-light">
              <tr>
                <th>번호</th>
                <th>업체명</th>
                <th>사업자등록번호</th>
                <th>대표자</th>
                <th>연락처</th>
                <th>팩스</th>
                <th></th>
              </tr>
            </thead>
            <tbody>
              <template v-if="companyCount > 0">
                <tr :key="i" v-for="(company, i) in companyList">
                  <td>{{ i + 1 }}</td>
                  <td>{{ company.cmpnyNm }}</td>
                  <td>{{ company.bizrno ? company.bizrno : "-" }}</td>
                  <td>{{ company.cxfc ? company.cxfc : "-" }}</td>
                  <td>{{ company.areaNo + "-" + company.entrprsMiddleTelno + "-" + company.entrprsEndTelno }}</td>
                  <td>{{ company.fxnum ? company.fxnum : "-" }}</td>
                  <td>
                    <button class="btn btn-info btn-fill btn-sm mr-1" @click="comSelect(company)">선택</button>
                  </td>
                </tr>
              </template>
              <tr v-else>
                <td colspan="10">
                  <div class="list-nodata">등록된 거래처가 없습니다.</div>
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

</template>

<script setup>
import axios from "../../assets/js/customAxios";
import { useRouter, useRoute } from 'vue-router';
import { onBeforeMount, onMounted, ref } from 'vue';

//========================== 컴포넌트 ==========================
import Swal from 'sweetalert2';
import Modal from '../../components/Modal.vue';
import Card from '../../components/Cards/Card.vue'
import { dateFormat } from "../../assets/js/common";

//========================= 데이터 초기화 =========================

const router = useRouter()
const route = useRoute();

//프로젝트 정보
const prCd = ref('');
const prNm = ref('');
const typeCd = ref('A03');
const startDt = ref('');
const endDt = ref('');
const price = ref('');
const aheadDt = ref('');
const entrprsMberId = ref('');
const cmpnyNm = ref('');
const createId = ref('admin');
const isUpdated = ref(false);
let txt = '';

if (route.query.prCd) { //수정일 경우
  prCd.value = route.query.prCd;
  isUpdated.value = true;
  txt = '수정';
}else{
  txt = '등록';
}

//======================== 공통함수 ========================

onBeforeMount(() => {
  if (isUpdated.value == true) {
    projectGetInfo(prCd.value);
  }
});

//입력 시 콤마 자동입력
const numberAutoFormat = () => {
  if (!price.value) {
    price.value = "";
    return;
  }

  let strValue = price.value.replace(/[^0-9]/g, ""); // 숫자만 남기기 (문자 제거)
  price.value = strValue.replace(/(\d)(?=(?:\d{3})+(?!\d))/g, '$1,'); // 천 단위 콤마 추가하여 표시
}

//입력정보 초기화
const formReset = () => {
  Swal.fire({
    title: "작성내용을 초기화하시겠습니까?",
    icon: "question",
    showCancelButton: true,
    customClass: {
      confirmButton: "btn btn-secondary btn-fill",
      cancelButton: "btn btn-danger btn-fill"
    },
    confirmButtonText: "아니요",
    cancelButtonText: "네",
  }).then(result => {
    if (result.dismiss == Swal.DismissReason.cancel) {
      prNm.value = '';
      typeCd.value = 'A03';
      startDt.value = '';
      endDt.value = '';
      price.value = '';
      aheadDt.value = '';
      entrprsMberId.value = '';
    }
  })
}

//========================= Toast grid =========================

const gridInstance = ref();
const workList = ref([]);
onMounted(() => {
  gridInstance.value = new window.tui.Grid({
    el: document.getElementById('jobGrid'),
    data: workList.value,
    scrollX: false,
    scrollY: true,
    rowHeaders: ['checkbox'],
    columns: [
      { header: '과업내용', name: 'prWorkNm', editor: 'text', formatter: ({ value }) => value ? value : '과업을 입력하세요' },
      { header: '진행률', name: 'progress', editor: 'text', formatter: ({ value }) => value ? value : '진행률을 입력하세요' },
      {
        header: '완료상태', name: 'state',
        editor: {
          type: 'select',
          options: {
            listItems: [
              { text: '미완료', value: 'A02' },
              { text: '완료', value: 'A01' }
            ]
          }
        }
      }
    ],
    draggable: true,
    language: {
      emptyMessage: '등록된 과업이 없습니다'
    }
  })
})

//행 추가 시 컬럼값
const appendedData = {
  prWorkNm: '',
  progress: '',
  state: '',
};

//========================= 모달 =========================

//거래처 목록 모달 열기
const isShowModal = ref(false);
const modalOpen = () => {
  isShowModal.value = true;
  comGetList();
}

//거래처 목록 모달 닫기
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

//행 추가 버튼
const btnWorkAdd = () => {
  gridInstance.value.appendRow(appendedData);
};

//======================= axios =======================

//프로젝트 단건조회
const projectInfo = ref([]);
const projectGetInfo = async (prCd) => { // 수정 시 쓰임
  try {
    const result = await axios.get(`/api/project/info/${prCd}`);
    projectInfo.value = result.data.info;

    prNm.value = projectInfo.value.prNm;
    typeCd.value = projectInfo.value.typeCd;
    startDt.value = dateFormat(projectInfo.value.startDt);
    endDt.value = dateFormat(projectInfo.value.endDt);
    price.value = projectInfo.value.price;
    aheadDt.value = dateFormat(projectInfo.value.aheadDt);
    entrprsMberId.value = projectInfo.value.entrprsMberId;
    cmpnyNm.value = projectInfo.value.comNm;

  } catch (err) {
    projectInfo.value = [];

    Swal.fire({
      icon: "error",
      title: "API 조회 오류",
      text: "Error : " + err
    });
  }
}

//거래처 목록 조회
const companyList = ref([]);
const companyCount = ref(0);
const comGetList = async () => {
  try {
    const result = await axios.get('/api/comm/comList');

    companyList.value = result.data;
    companyCount.value = result.data.length;
  } catch (err) {
    companyList.value = [];
  }
}

//거래처 선택 시 v-model값 추가
const comSelect = (params) => {
  entrprsMberId.value = params.entrprsMberId;
  cmpnyNm.value = params.cmpnyNm;
  isShowModal.value = false;
}

//프로젝트 등록
const projectAdd = async () => {

  if (!prNm.value) {
    Swal.fire({
      icon: "info",
      title: "프로젝트명을 입력하세요"
    });
    return;
  } else if (!typeCd.value) {
    Swal.fire({
      icon: "info",
      title: "프로젝트 구분을 선택하세요"
    });
    return;
  }

  const modifiedRows = gridInstance.value.getModifiedRows(); // 변경된 데이터 가져오기
  const newData = modifiedRows.createdRows; // 새로 추가된 데이터만 추출

  const requestData = {
    workArr: newData.map(row => ({
      prWorkNm: row.prWorkNm,
      progress: row.progress,
      state: row.state,
      seq: row.rowKey
    })),
    prCd: prCd.value,
    prNm: prNm.value,
    typeCd: typeCd.value,
    startDt: startDt.value,
    endDt: endDt.value,
    price: price.value,
    aheadDt: aheadDt.value,
    entrprsMberId: entrprsMberId.value,
    createId: createId.value
  };

  try {
    const response = ref([]);
    if(isUpdated.value) response.value = await axios.put(`/api/project`, requestData); //수정
    else response.value = await axios.post("/api/project", requestData); //등록

    if (response.value.data === true) {
      Swal.fire({
        icon: "success",
        title: txt+"완료",
        text: txt+"한 프로젝트는 목록에서 확인할 수 있습니다",
      }).then(() => {
        router.replace({ name: 'ProjectList' }) //프로젝트 조회페이지로 이동
      });
    }
  } catch (err) {
    Swal.fire({
      icon: "error",
      title: txt+"실패",
      text: "프로젝트 "+txt+" 실패",
    })
  }
}
</script>