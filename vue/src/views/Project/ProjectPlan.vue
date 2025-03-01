<template>
  <div class="content" @keydown.esc="modalClose">
    <div class="container-fluid schedule-container">
      <Card>
        <h4 class="card-title float-left">프로젝트 일정관리</h4>
      </Card>

      <div class="card border-primary">
        <div class="card-body">
          <p class="card-sub">{{ projectInfo.comNm }}</p>
          <h5 class="card-title mb-3">
            {{ projectInfo.prNm }}
            <span class="badge" :class="projectInfo.term > 10 ? 'badge-primary' : 'badge-danger'">D{{ term }}</span>
          </h5>
          <p class="card-sub"><b>기간 : </b> {{ projectInfo.startDt }} ~ {{ projectInfo.endDt }}
          </p>
          <p class="card-sub"><b>참여자 : </b> 박주현, 박지훈, 정수민, 박명식</p>
        </div>
      </div>
      <Card>
        <button class="btn btn-primary btn-fill float-right" @click="modalOpen">일정등록</button>

        <div style="width:100%; overflow:auto">
          <table>
            <thead>
              <tr>
                <th :key="date" v-for="date in dateTermArr">
                  <span :class="dateGetDay(date) == '일' ? 'point-red' : dateGetDay(date) == '토' ? 'point-blue' : ''">{{
                    dateFormatDay(date) }}</span>
                </th>
              </tr>
            </thead>
            <tbody>
              <template v-if="planCount > 0">
                <tr :key="plan" v-for="plan in planList"> <!--등록된 일정갯수만큼-->
                  <td :key="date" v-for="date in dateTermArr"> <!--시작일 ~ 종료일 날짜 기간만큼-->
                    <div class="task" v-if="date == plan.startDt"
                      :style="'width: ' + (dateTermCalc(plan.startDt, plan.endDt) * 100 - 10) + '%; background-color:' + plan.color">
                      <span @click="btnProjectPlanUpdate(plan.prPlanCd)">{{ plan.planNm }}</span>
                      <button class="close" @click="btnProjectPlanRemove(plan.prPlanCd)">×</button>
                    </div>
                  </td>
                </tr>
              </template>
              <tr v-else class="list-nodata">
                <td :colspan="13">
                  <div>프로젝트 일정을 등록해주세요</div>
                </td>
              </tr>
            </tbody>
          </table>
        </div>
      </Card>
    </div>
  </div>

  <!--일정등록 모달[s]-->
  <Modal :isShowModal="isShowModal" :modalTitle="'일정등록'" @click.self="modalClose">
    <!-- 모달 바디 -->
    <template v-slot:body>
      <card>
        <div class="mb-3">
          <div class="form-group has-label">
            <label>일정색상</label>
          </div>
          <input type="color" :name="color" v-model="color" class="form-control form-control-color"
            id="exampleColorInput" title="Choose your color">
        </div>
        <div class="mb-3">
          <label class="form-label">기간 <em class="point-red">*</em></label>
          <div class="row">
            <div class="col-auto">
              <input type="date" :name="startDt" v-model="startDt" :min="dateFormat(projectInfo.startDt)"
                class="form-control">
            </div>
            <div class="col-auto p-none">~</div>
            <div class="col-auto">
              <input type="date" :name="endDt" v-model="endDt" :min="startDt" :max="dateFormat(projectInfo.endDt)"
                class="form-control">
            </div>
          </div>
        </div>
        <div class="mb-3">
          <div class="form-group has-label">
            <label>일정명 <em class="point-red">*</em></label>
          </div>
          <input type="text" :name="planNm" v-model="planNm" class="form-control" placeholder="일정내용을 입력해주세요">
        </div>
      </card>
    </template>

    <!-- 모달 푸터 -->
    <template v-slot:footer>
      <button type="button" class="btn btn-primary btn-fill" @click="btnProjectPlanAdd">등록</button>
      <button type="button" class="btn btn-secondary btn-fill" @click="modalClose">닫기</button>
    </template>
  </Modal>
  <!--일정등록 모달[e]-->

</template>

<script setup>
import axios from "../../assets/js/customAxios";
import { onBeforeMount, ref } from 'vue';
import { useRoute } from "vue-router";

//========================== 컴포넌트 ==========================
import Swal from 'sweetalert2';
import Card from '../../components/Cards/Card.vue'
import Modal from '../../components/Modal.vue';

//============================= js =============================
import { dateFormat } from '../../assets/js/common'
import { dateTermCalc, dateGetDay, dateFormatDay } from '../../assets/js/project'


//========================= 데이터 초기화 =========================

const route = useRoute();
const prCd = ref('');            //해당 프로젝트 코드
const term = ref('');            //프로젝트 남은일수

const planNm = ref('');          //일정명
const color = ref('#fd9b9b');    //화면에 보여질 일정색상
const startDt = ref('');         //일정 시작일
const endDt = ref('');           //일정 종료일
const dateTermArr = ref([]);        //일정 시작일~종료일 모든 일자 출력
const createId = ref('admin');

onBeforeMount(() => {
  prCd.value = route.query.prCd;
  projectGetInfo(prCd.value);     //해당 프로젝트 정보 호출 함수
});

//========================= 모달 =========================

const isShowModal = ref(false);
const modalOpen = () => { //일정 등록/수정 모달 열기
  isShowModal.value = true;
}

const modalClose = (e) => { //일정 등록/수정 모달 닫기
  formReset();
  if (e.key === "Escape") {
    if (isShowModal.value) {
      isShowModal.value = !isShowModal.value
    }
  } else {
    isShowModal.value = false;
  }
}

//======================= 버튼이벤트 =======================

//일정 등록
const btnProjectPlanAdd = () => {
  projectPlanAdd();
  isShowModal.value = false;
}

//일정 수정
const btnProjectPlanUpdate = (code) => {
  modalOpen();
  projectPlanGetInfo(code);
}

//일정 삭제
const btnProjectPlanRemove = (code) => {
  Swal.fire({
    title: "해당 일정을 삭제 하시겠습니까?",
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
      projectPlanRemove(code);
    }
  });
}

//======================== 공통함수 ========================

// 시작일 ~ 종료일 사이의 날짜 배열로 담는 함수
const projectDateTerm = (startDate, endDate) => {
  let start = new Date(startDate);
  let end = new Date(endDate);

  while (start <= end) {
    let formattedDate = start.toISOString().split("T")[0]; // YYYY-MM-DD 형식으로 변환
    dateTermArr.value.push(formattedDate);
    start.setDate(start.getDate() + 1); // 하루 증가
  }
};

//입력정보 초기화
const formReset = () => {
  planNm.value = '';
  color.value = '';
  startDt.value = '';
  endDt.value = '';
}

//======================= axios =======================

//프로젝트 단건조회
const projectInfo = ref([]);
const projectGetInfo = async (prCd) => {
  try {
    const result = await axios.get(`/api/project/info/${prCd}`);

    projectInfo.value = result.data.info;
    projectInfo.value.startDt = dateFormat(projectInfo.value.startDt);
    projectInfo.value.endDt = dateFormat(projectInfo.value.endDt);
    projectInfo.value.term = dateTermCalc(dateFormat(projectInfo.value.endDt), dateFormat());

    term.value = projectInfo.value.term;

    term.value > 0 ? term.value = "+" + term.value  //종료일 전일 때
      : term.value < 0 ? term.value                 //종료일을 초과했을 때
        : term.value = term.value = "-day";         //종료일 당일일 때

    projectPlanGetList(prCd);
    projectDateTerm(projectInfo.value.startDt, projectInfo.value.endDt); //프로젝트 시작일~종료일까지 모든 일자 배열에 담음
  } catch (err) {
    projectInfo.value = [];

    Swal.fire({
      icon: "error",
      title: "API 조회 오류",
      text: "Error : " + err
    });
  }
}

//프로젝트 일정 전체조회
const planList = ref([]);
const planCount = ref(0);
const projectPlanGetList = async (prCd) => {
  try {
    const result = await axios.get(`/api/project/plan/${prCd}`);

    planCount.value = result.data.length;
    planList.value = result.data.map(item => ({
      ...item,
      startDt: dateFormat(item.startDt),
      endDt: dateFormat(item.endDt),
    }));

  } catch (err) {
    planList.value = [];
  }
}

//프로젝트 일정 단건조회
const projectPlanInfo = ref([]);
const projectPlanGetInfo = async (prPlanCd) => {
  try {
    const result = await axios.get(`/api/project/plan/info/${prPlanCd}`);

    projectPlanInfo.value = result.data.info;
    planNm.value = projectPlanInfo.value.planNm;
    color.value = projectPlanInfo.value.color;
    startDt.value = dateFormat(projectPlanInfo.value.startDt);
    endDt.value = dateFormat(projectPlanInfo.value.endDt);

  } catch (err) {
    projectPlanInfo.value = [];

    Swal.fire({
      icon: "error",
      title: "API 조회 오류",
      text: "Error : " + err
    });
  }
}

//프로젝트 일정 등록
const projectPlanAdd = async () => {

  if (!planNm.value) {
    Swal.fire({
      icon: "info",
      title: "일정명을 입력하세요"
    });
    return;
  }else if (!startDt.value) {
    Swal.fire({
      icon: "info",
      title: "시작일을 입력하세요"
    });
    return;
  }else if (!endDt.value) {
    Swal.fire({
      icon: "info",
      title: "종료일을 입력하세요"
    });
    return;
  }

  const formData = new FormData();
  formData.append("prCd", prCd.value);
  formData.append("planNm", planNm.value);
  formData.append("startDt", startDt.value);
  formData.append("endDt", endDt.value);
  formData.append("color", color.value);
  formData.append("createId", createId.value);

  try {
    const response = await axios.post('/api/project/plan', formData);

    if (response.data.result === true) {
      Swal.fire({
        icon: "success",
        title: "등록완료",
        text: "등록한 일정은 목록에서 확인할 수 있습니다",
      })

      projectPlanGetList(prCd.value);
    }
  } catch (err) {
    Swal.fire({
      icon: "error",
      title: "등록실패",
      text: "프로젝트 등록 실패",
    })
  }
}

//프로젝트 일정삭제
const projectPlanRemove = async (prPlanCd) => {

  try {
    const response = await axios.delete(`/api/project/plan/${prPlanCd}`);

    if (response.data.result === true) {
      Swal.fire({
        icon: "success",
        title: "삭제완료",
        text: "선택한 일정을 삭제하였습니다",
      })

      projectPlanGetList(prCd.value);
    }
  } catch (err) {
    Swal.fire({
      icon: "error",
      title: "삭제 실패",
      text: "Error : " + err
    });
  }
}
</script>