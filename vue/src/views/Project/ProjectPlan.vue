<template>
  <div class="content" @keydown.esc="modalClose">
    <div class="container-fluid schedule-container">
      <Card>
        <h4 class="card-title float-left mt-1">프로젝트 일정관리</h4>
      </Card>

      <div class="card border-primary">
        <div class="card-body">
          <p class="card-sub">{{ projectInfo.comNm }}</p>
          <h5 class="card-title mb-3">
            {{ projectInfo.prNm }}

            <span class="badge" :class="projectInfo.term * (-1) > 10 ? 'badge-primary' : 'badge-danger'" v-if="projectInfo.state=='A03'">D{{ term
            }}</span>
            <span v-else class="badge badge-secondary">완료</span>
          </h5>
          <p class="card-sub"><b>기간 : </b> {{ projectInfo.startDt }} ~ {{ projectInfo.endDt }}
          </p>
          <p class="card-sub"><b>참여자 : </b>  <span v-html="memArr.map(mem => mem.mgrSt == 'A01' 
                        ? `<i class='fa-solid fa-crown master'></i> ${mem.mberNm}` 
                        : mem.mberNm).join(', ') || '-'"></span></p>
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
          </table>
          <table v-if="planCount > 0">
            <tbody>
              <tr :key="plan" v-for="plan in planList"> <!--등록된 일정갯수만큼-->
                <td :key="date" v-for="date in dateTermArr"> <!--시작일 ~ 종료일 날짜 기간만큼-->
                  <div class="task" v-if="date == plan.startDt"
                    :style="'width: ' + (dateTermCalc(plan.startDt, plan.endDt) * 100 - 10) + '%; background-color:' + plan.color">
                    <span @click="btnProjectPlanUpdate(plan.prPlanCd)">{{ plan.planNm }}</span>
                    <button class="close" @click="btnProjectPlanRemove(plan.prPlanCd)"
                      v-show="projectInfo.state == 'A03'">×</button>
                  </div>
                </td>
              </tr>
            </tbody>
          </table>
          <table v-else>
            <tbody>
              <tr class="list-nodata">
                <td>
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
          <input type="color" v-model="color" class="form-control form-control-color"
            id="exampleColorInput" title="Choose your color">
        </div>
        <div class="mb-3">
          <label class="form-label">기간 <em class="point-red">*</em></label>
          <div class="row">
            <div class="col-auto">
              <input type="date" v-model="startDt" :min="dateFormat(projectInfo.startDt)"
                class="form-control">
            </div>
            <div class="col-auto p-none">~</div>
            <div class="col-auto">
              <input type="date" v-model="endDt" :min="startDt" :max="dateFormat(projectInfo.endDt)"
                class="form-control">
            </div>
          </div>
        </div>
        <div class="mb-3">
          <div class="form-group has-label">
            <label>일정명 <em class="point-red">*</em></label>
          </div>
          <input type="text" v-model="planNm" class="form-control" placeholder="일정내용을 입력해주세요">
        </div>
      </card>
    </template>

    <!-- 모달 푸터 -->
    <template v-slot:footer>
      <button type="button" class="btn btn-secondary btn-fill" @click="modalClose">닫기</button>
      <button type="button" class="btn btn-fill" :class="isUpdated ? 'btn-success' : 'btn-primary'"
        @click="btnProjectPlanAdd">{{ isUpdated ? '저장' : '등록' }}</button>
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

const isUpdated = ref(false);
const route = useRoute();
const prCd = ref('');            //해당 프로젝트 코드
const term = ref('');            //프로젝트 남은일수

const planNm = ref('');          //일정명
const color = ref('#fd9b9b');    //화면에 보여질 일정색상
const startDt = ref('');         //일정 시작일
const endDt = ref('');           //일정 종료일
const dateTermArr = ref([]);        //일정 시작일~종료일 모든 일자 출력

onBeforeMount(() => {
  prCd.value = route.query.prCd;
  projectGetInfo(prCd.value);     //해당 프로젝트 정보 호출 함수
});

//========================= 모달 =========================

const isShowModal = ref(false);
const modalOpen = () => { //일정 등록/수정 모달 열기
  if (projectInfo.value.state == 'A04') {
    Swal.fire({
      icon: "error",
      title: "등록불가",
      text: "완료된 프로젝트는 일정을 등록할 수 없습니다."
    });
  } else {
    isShowModal.value = true;
  }
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
  isUpdated.value = false;
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
  isUpdated.value = true;
}

//일정 삭제
const btnProjectPlanRemove = (code) => {
  Swal.fire({
    title: "해당 일정을 삭제 하시겠습니까?",
    icon: "question",
    showCancelButton: true,
    customClass: {
      confirmButton: "btn btn-secondary btn-fill",
      cancelButton: "btn btn-danger btn-fill"
    },
    cancelButtonText: "삭제",
    confirmButtonText: "닫기",
  }).then(async (result) => {
    if (result.dismiss == Swal.DismissReason.cancel) {
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
  color.value = '#fd9b9b';
  startDt.value = '';
  endDt.value = '';
}

//======================= axios =======================

//프로젝트 단건조회
const projectInfo = ref([]);
const memList = ref([]);
const memArr = ref([]);
const projectGetInfo = async (prCd) => {
  try {
    const result = await axios.get(`/api/project/info/${prCd}`);
    projectInfo.value = result.data.result[0];
    projectInfo.value.startDt = dateFormat(projectInfo.value.startDt);
    projectInfo.value.endDt = dateFormat(projectInfo.value.endDt);
    projectInfo.value.term = dateTermCalc(dateFormat(projectInfo.value.endDt), dateFormat());

    memList.value = result.data.list;
    memArr.value = [];

    memList.value.forEach((data) => {
      memArr.value.push(
        { mberNm: data.memNm, mgrSt: data.mgrSt }
      );
    });
    term.value = projectInfo.value.term;

    term.value > 0 ? term.value = "+" + term.value  //종료일 전일 때
      : term.value < 0 ? term.value                 //종료일을 초과했을 때
        : term.value = term.value = "-day";         //종료일 당일일 때

    projectPlanGetList(prCd);
    projectDateTerm(projectInfo.value.startDt, projectInfo.value.endDt); //프로젝트 시작일~종료일까지 모든 일자 배열에 담음
  } catch (err) {
    projectInfo.value = [];
    memList.value = [];

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
  } else if (!startDt.value) {
    Swal.fire({
      icon: "info",
      title: "시작일을 입력하세요"
    });
    return;
  } else if (!endDt.value) {
    Swal.fire({
      icon: "info",
      title: "종료일을 입력하세요"
    });
    return;
  }

  let txt = '';
  if(isUpdated.value) txt = "수정";
  else txt = "등록";
  
  const requestData = {
    prPlanCd: projectPlanInfo.value.prPlanCd,
    prCd: prCd.value,
    planNm: planNm.value,
    startDt: startDt.value,
    endDt: endDt.value,
    color: color.value
  };

  try {
    
    const response = ref([]);
    if (isUpdated.value) response.value = await axios.put(`/api/project/plan`, requestData); //수정
    else response.value = await axios.post("/api/project/plan", requestData); //등록
    
    if (response.value.data === true) {
        Swal.fire({
          icon: "success",
          title: txt + "완료",
          text: txt + "한 일정은 목록에서 확인할 수 있습니다",
        })
        formReset();
        projectPlanGetList(prCd.value);
      }
    } catch (err) {
      Swal.fire({
        icon: "error",
        title: txt + "실패",
        text: "일정 " + txt + " 실패",
      })
    }
}

//프로젝트 일정삭제
const projectPlanRemove = async (prPlanCd) => {

  try {
    const response = await axios.delete(`/api/project/plan/${prPlanCd}`);

    if (response.data === true) {
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