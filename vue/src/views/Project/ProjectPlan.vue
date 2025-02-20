<template>
  <div class="content" @keydown.esc="modalClose">
    <div class="container-fluid schedule-container">
      <Card>
        <h4 class="card-title float-left">프로젝트 일정관리</h4>
      </Card>

      <div class="card border-primary">
        <div class="card-body">
          <p class="card-sub">{{ projectInfo.comNm }}</p>
          <h5 class="card-title mb-3">{{ projectInfo.prNm }} <span class="badge badge-danger">D{{ term > 0 ? "-" + term
            :
            "+" + term*(-1) }}</span></h5>
          <p class="card-sub"><b>기간 : </b> {{ projectInfo.startDt }} ~ {{ projectInfo.endDt }}
          </p>
          <p class="card-sub"><b>참여자 : </b> 박주현, 박지훈, 정수민, 박명식</p>
        </div>
      </div>

      <Card>
        <button class="btn btn-primary btn-fill float-right" @click="modalOpen">일정등록</button>
        <div style="width:100%; overflow:auto">
          <table class="table-responsive">
            <thead>
              <tr>
                <th :key="date" v-for="date in dateTerm">
                  <span v-if="dateGetDay(date) == '일'" class="point-red">{{ dateFormatDay(date)}}</span>
                  <span v-else-if="dateGetDay(date) == '토'" class="point-blue">{{ dateFormatDay(date) }}</span>                  
                  <span v-else>{{ dateFormatDay(date) }}</span>
                </th>
              </tr>
            </thead>
            <tbody>

              <template v-if="planCount > 0">
                <tr :key="plan" v-for="plan in planList">
                  <td :key="date" v-for="date in dateTerm">
                    <div class="task" v-if="date == plan.startDt"
                      :style="'width: ' + (dateTermCalc(plan.startDt, plan.endDt) * 100 - 10) + '%; background-color:' + plan.color">
                      <span @click="btnProjectPlanUpdate(plan.prPlanCd)">{{ plan.planNm }}</span>
                      <button class="close" @click="btnProjectPlanRemove(plan.prPlanCd)">×</button>
                    </div>
                  </td>
                </tr>
              </template>
              <tr v-else class="list-nodata">
                <td :colspan="dateTerm.length">
                  <div>등록된 일정이 없습니다.</div>
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
      <button type="button" class="btn btn-primary btn-fill" @click="confirm">등록</button>
      <button type="button" class="btn btn-secondary btn-fill" @click="modalClose">닫기</button>
    </template>
  </Modal>
  <!--일정등록 모달[e]-->

</template>

<script setup>
import axios from "axios";
import Swal from 'sweetalert2';
import { onBeforeMount, ref } from 'vue';
import Card from '../../components/Cards/Card.vue'
import Modal from '../../components/Modal.vue';
import { dateFormat, dateTermCalc, dateGetDay, dateFormatDay } from '../../assets/js/common.js'
import { useRoute } from 'vue-router';

//---------------데이터-------------- 

const route = useRoute();
const prCd = ref('');
const planNm = ref('');
const term = ref('');
const color = ref('#fd9b9b');
const startDt = ref('');
const endDt = ref('');
const dateTerm = ref([]);

onBeforeMount(() => {
  prCd.value = route.query.prCd;
  projectGetInfo(prCd.value);
});

//---------------모달--------------

const isShowModal = ref(false);
const modalOpen = () => { //프로젝트 정보 모달 열기
  isShowModal.value = true;
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

const confirm = () => {
  projectPlanAdd();
  isShowModal.value = false;
}

//-------------버튼이벤트------------

const btnProjectPlanUpdate = (code) => {
  modalOpen();
  projectPlanGetInfo(code);
}

// 프로젝트 일정 삭제 버튼
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

      // 프로젝트 일정삭제
      projectPlanRemove(code);
    }
  });
}

//-------------공통함수------------

const generateDateList = (startDate, endDate) => { // 시작일 ~ 종료일 사이의 날짜 배열로 담는 함수 
  let start = new Date(startDate);
  let end = new Date(endDate);

  while (start <= end) {
    let formattedDate = start.toISOString().split("T")[0]; // YYYY-MM-DD 형식으로 변환
    dateTerm.value.push(formattedDate);
    start.setDate(start.getDate() + 1); // 하루 증가
  }
};

//---------------axois--------------

const projectInfo = ref([]);
const projectGetInfo = async (prCd) => { //프로젝트 단건조회
  try {
    const result = await axios.get(`/api/project/info/${prCd}`);

    projectInfo.value = result.data.info;
    projectInfo.value.startDt = dateFormat(projectInfo.value.startDt);
    projectInfo.value.endDt = dateFormat(projectInfo.value.endDt);

    term.value = dateTermCalc('', dateFormat(projectInfo.value.endDt))

    projectPlanGetList(prCd);
    generateDateList(projectInfo.value.startDt, projectInfo.value.endDt);
  } catch (err) {
    projectInfo.value = [];

    Swal.fire({
      icon: "error",
      title: "API 조회 오류",
      text: "Error : " + err
    });
  }
}

const planList = ref([]);
const planCount = ref(0);
const projectPlanGetList = async (prCd) => { //프로젝트 일정조회
  try {
    const result = await axios.get(`/api/project/plan/${prCd}`);

    planList.value = result.data.map(item => ({
      ...item,
      startDt: dateFormat(item.startDt),
      endDt: dateFormat(item.endDt),
    }));

    planCount.value = result.data.length;

  } catch (err) {
    planList.value = [];
  }
}

const projectPlanInfo = ref([]);
const projectPlanGetInfo = async (prPlanCd) => { //프로젝트 일정 단건조회
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

const projectPlanAdd = async () => { //프로젝트 일정 등록

  if (!planNm.value) {
    Swal.fire({
      icon: "info",
      title: "일정명을 입력하세요"
    });
    return;
  } else if (startDt.value >= endDt.value) {
    Swal.fire({
      icon: "info",
      title: "일정 시작일이 종료일보다 작을 수 없습니다."
    });
    return;
  }

  const formData = new FormData();
  formData.append("prCd", prCd.value);
  formData.append("planNm", planNm.value);
  formData.append("startDt", startDt.value);
  formData.append("endDt", endDt.value);
  formData.append("color", color.value);
  formData.append("createId", 'admin');

  try {
    const response = await axios.post('/api/project/plan', formData);

    if (response.data.result === true) {
      Swal.fire({
        icon: "success",
        title: "등록완료",
        text: "등록한 일정은 목록에서 확인할 수 있습니다",
      })
      planNm.value = '';
      color.value = '';
      startDt.value = '';
      endDt.value = '';
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

const projectPlanRemove = async (prPlanCd) => { //프로젝트 일정삭제
  console.log(prPlanCd);

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