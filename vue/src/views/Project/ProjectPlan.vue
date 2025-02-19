<template>
  <div class="content" @keydown.esc="modalClose">
    <div class="container-fluid schedule-container">
      <Card>
        <h4 class="card-title float-left">프로젝트 조회</h4>
      </Card>

      <div class="card border-primary">
        <div class="card-body">
          <p class="card-sub">대구사람이좋다협회</p>
          <h5 class="card-title mb-3">{{ projectInfo.prNm }} <span class="badge badge-danger">D-10</span></h5>
          <p class="card-sub"><b>기간 : </b> {{ dateFormat(projectInfo.startDt) }} ~ {{ dateFormat(projectInfo.endDt) }}
          </p>
          <p class="card-sub"><b>참여자 : </b> 박주현, 박지훈, 정수민, 박명식</p>
        </div>
      </div>

      <Card>
        <button class="btn btn-primary btn-fill float-right" @click="modalOpen">일정등록</button>
        <table class="table-responsive">
          <tr>
            <th>1주차</th>
            <th>2주차</th>
            <th>3주차</th>
            <th>4주차</th>
            <th>5주차</th>
            <th>6주차</th>
            <th>7주차</th>
            <th>8주차</th>
          </tr>
          <tr>
            <td>
              <div class="task red" style="width:190%">화면설계 및 데이터베이스 정리 <span class="close">×</span></div>
            </td>
            <td></td>
            <td></td>
            <td></td>
            <td></td>
            <td></td>
            <td></td>
            <td></td>
          </tr>
          <tr>
            <td></td>
            <td></td>
            <td>
              <div class="task blue" style="width:290%">화면설계 및 데이터베이스 정리 <span class="close">×</span></div>
            </td>
            <td></td>
            <td></td>
            <td></td>
            <td></td>
            <td></td>
          </tr>
        </table>
      </Card>
    </div>
  </div>

  <!--업무등록 모달[s]-->
  <Modal :isShowModal="isShowModal" :modalTitle="'일정등록'" @click.self="modalClose">
    <!-- 모달 바디 -->
    <template v-slot:body>
      <card>
        <div class="mb-3">
          <div class="form-group has-label">
            <label>일정색상</label>
          </div>
          <input type="color" class="form-control form-control-color" id="exampleColorInput" value="#563d7c"
            title="Choose your color">
        </div>
        <div class="mb-3">
          <label class="form-label">기간 <em class="point-red">*</em></label>
          <div class="row">
            <div class="col-auto">
              <select class="form-select" :name="startWeek" v-model="startWeek">
                <option value="1">1주차</option>
                <option value="2">2주차</option>
                <option value="3">3주차</option>
              </select>
            </div>
            <div class="col-auto">~</div>
            <div class="col-auto">
              <select class="form-select" :name="endWeek" v-model="endWeek">
                <option value="1">1주차</option>
                <option value="2">2주차</option>
                <option value="3">3주차</option>
              </select>
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
  <!--업무등록 모달[e]-->

</template>

<script setup>
import axios from "axios";
import Swal from 'sweetalert2';
import { onBeforeMount, ref } from 'vue';
import Card from '../../components/Cards/Card.vue'
import Modal from '../../components/Modal.vue';
import { dateFormat } from '../../assets/js/common.js'
import { useRoute } from 'vue-router';

//---------------데이터-------------- 

const route = useRoute();
const prCd = ref('');
const planNm = ref('');
const startWeek = ref('1');
const endWeek = ref('1');

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

//---------------axois--------------

const projectInfo = ref([]);
const projectGetInfo = async (prCd) => { //프로젝트 단건조회
  try {
    const result = await axios.get(`/api/project/info?prCd=${prCd}`);
    projectInfo.value = result.data.info;
  } catch (err) {
    projectInfo.value = [];

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
  } else if (startWeek.value >= endWeek.value) {
    Swal.fire({
      icon: "info",
      title: "일정 시작일이 종료일보다 작을 수 없습니다."
    });
    return;
  }

  const requestData = { // 요청 본문에 보낼 데이터
    prCd: prCd.value,
    planNm: planNm.value,
    startWeek: startWeek.value,
    endWeek: endWeek.value,
    createId: "admin"
  };

  console.log(requestData);

  try {
    const response = await axios.post('/api/project/plan', requestData);

    if (response.data.result === true) {
      Swal.fire({
        icon: "success",
        title: "등록완료",
        text: "등록한 일정은 목록에서 확인할 수 있습니다",
      })
    }
  } catch (err) {
    Swal.fire({
      icon: "error",
      title: "등록실패",
      text: "프로젝트 등록 실패",
    })
  }
}
</script>