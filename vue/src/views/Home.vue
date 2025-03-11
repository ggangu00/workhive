<template>
  <div class="content">
    <div class="container-fluid">
      <div class="card">
        <div class="profile-section">
          <div class="profile-info">
            <div class="profile-icon"><i class="bi bi-person-fill"></i></div>
            <div>
              <small>{{ userData.deptNm || '' }}</small>
              <h4>{{ userData.mberNm || '' }} {{ userData.gradeNm || '' }}</h4>
            </div>
          </div>
          <div class="status-boxes">
            <div class="status-box attendance">
              <div class="status-title status-red">출/퇴근</div>
              <div class="status-content">
                출근 {{ homeInfo.goTime || '--:--' }}<br>
                퇴근 {{ homeInfo.leaveTime || '--:--' }}
              </div>
            </div>
            <div class="status-box projects">
              <div class="status-title status-orange">진행중인 프로젝트</div>
              <div class="status-content">{{ homeInfo.projectCnt ? numberFormat(homeInfo.projectCnt) : 0 }}건</div>
            </div>

            <div class="status-box projects">
              <div class="status-title status-green">금일 예정 일정</div>
              <div class="status-content">{{ homeInfo.schCnt ? numberFormat(homeInfo.schCnt) : 0 }}건</div>
            </div>

            <div class="status-box projects">
              <div class="status-title status-blue">미완료 일지</div>
              <div class="status-content">{{ homeInfo.todoCnt ? numberFormat(homeInfo.todoCnt) : 0 }}건</div>
            </div>

          </div>
        </div>
      </div>

      <div class="row" style="background-color: transparent;">
        <div class="col-5">
          <div class="card">
            <div class="home-container">
              <div class="home-container-header">참여예정 회의</div>
              <div class="meeting-item" :key="i" v-for="(meet, i) in meetList">
                <div class="meeting-category">{{ meet.typeNm }}</div>
                <div class="meeting-title">
                  {{ meet.mtgNm }}
                  <span class="badge badge-warning">
                    D{{ dateTermCalc(dateFormat(meet.mtgDe), dateFormat()) }}
                  </span>
                </div>
                <div class="meeting-date">
                  {{ dateFormat(meet.mtgDe) }}({{ dateGetDay(meet.mtgDe) }})
                  {{ meet.mtgBeginTm }} ~ {{ meet.mtgEndTm }}
                </div>
              </div>

              <!-- 등록된 회의가 3건 미만일 경우 부족한 개수만큼 빈 회의 행을 추가 -->
              <div class="meeting-none">
                <div class="meeting-item" :key="'empty-' + i" v-for="i in Math.max(0, 3 - meetList.length)">
                  <div class="meeting-category">-</div>
                  <div class="meeting-title">예정된 회의가 없습니다.</div>
                  <div class="meeting-date">0000-00-00</div>
                </div>
              </div>
            </div>
          </div>
        </div>

        <div class="col-7">
          <div class="card">
            <div class="calendar-container">
              <!-- Calendar Section -->
              <div class="flex-grow-1 cal"
                style="border-right: 1px solid rgb(217 217 217 / 50%); box-shadow: 2px 0px -2px 4px rgb(0 0 0 / 12%);">
                <div class="home-container-header">월간 일정</div>
                <div class="p-2">
                  <Calendar  @dateSelected="handleDateSelected" />
                </div>
              </div>
              <!-- Schedule Section -->
              <div class="home-container list">
                <div class="home-container-header">{{ selectedDate }}</div>

                <div class="meeting-item" :key="i" v-for="(cal, i) in calList">
                  <div class="meeting-category">{{cal.typeNm || '-'}}</div>
                  <div class="meeting-title">{{ cal.deptNm ? '['+cal.deptNm+']' : ''}} {{cal.schdulNm}}</div>
                </div>

                <!-- 등록된 일정이 3건 미만일 경우 부족한 개수만큼 빈 회의 행을 추가 -->
                <div class="meeting-none">
                  <div class="meeting-item" :key="'empty-' + i" v-for="i in Math.max(0, 3 - calList.length)">
                    <div class="meeting-category">-</div>
                    <div class="meeting-title">등록된 일정이 없습니다.</div>
                  </div>
                </div>
              </div>
            </div>
          </div>
        </div>
      </div>
      
      <div class="card">
        <div class="home-container">
          <div class="home-container-header">게시글</div>
          <div class="board-item" v-for="(bbs, i) in bbsList" :key="i" @click="goToBulletinInfo(bbs);">
            <span class="badge badge-danger mrp10" v-show="bbs.noticeAt == 'Y'">공지</span>
            <span class="board-title">{{ bbs.nttSj }}</span>
            <span class="board-meta">{{ bbs.mberNm }}</span>
            <span class="board-meta">{{ bbs.createDt }}</span>
          </div>

          <!-- 등록된 게시글이 3건 미만일 경우 부족한 개수만큼 빈 회의 행을 추가 -->
          <div class="board-none">
            <div class="board-item" :key="'empty-' + i" v-for="i in Math.max(0, 5 - bbsList.length)">
              <span class="board-title">등록된 게시글이 없습니다.</span>
              <span class="board-meta">0000-00-00</span>
            </div>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import axios from "../assets/js/customAxios.js";
import { useStore } from "vuex";
import { useRouter } from 'vue-router';
import { onBeforeMount, ref, computed, watch } from 'vue';
import { useUserInfoStore } from '../store/userStore';

//========================== 컴포넌트 ==========================
import Swal from 'sweetalert2';
import Calendar from './components/Calendar.vue';

//============================= js =============================
import { numberFormat, dateFormat } from '../assets/js/common.js'
import { dateGetDay, dateTermCalc } from '../assets/js/project.js'

//============================= 데이터 =============================

const store = useStore();
const isCmt = computed(() => store.state.isCmt);
watch(isCmt, () => {
  homeGetInfo();
})


const router = useRouter();
const userInfoStore = useUserInfoStore();
const userData = computed(() => userInfoStore?.user || {});

//========================= 공통함수 =========================
onBeforeMount(() => {
  homeGetInfo();
  meetGetList();
  calGetList();
  calGetCnt();
  bbsGetList();  
});

const selectedDate = ref(dateFormat());
const handleDateSelected = (date) => {
  selectedDate.value = date;
};

watch(selectedDate, () => {
  calGetList();
});

//=========================== axios ==========================
const homeInfo = ref([]);
const homeGetInfo = async () => { // 대시보드 건수 조회
  try {
    const result = await axios.get(`/api/comm/homeInfo`);
    homeInfo.value = result.data.info;
  } catch (err) {
    homeInfo.value = [];

    Swal.fire({
      icon: "error",
      title: "API 조회 오류",
      text: "Error : " + err
    });
  }
}

const meetList = ref([]);
const meetCount = ref(0);
const meetGetList = async () => { //회의 최신 3건 조회
  try {
    const result = await axios.get('/api/meet/list?rowCtn=3&state=ing');

    meetList.value = result.data;
    meetCount.value = result.data.length;

  } catch (err) {
    meetList.value = [];

    Swal.fire({
      icon: "error",
      title: "API 조회 오류",
      text: "Error : " + err
    });
  }
}

const calList = ref([]);
const calCount = ref(0);
const calGetList = async () => { //일정 최신 3건 조회
  try {
    const result = await axios.get(`/api/comm/calList/${selectedDate.value}`);
    
    calList.value = result.data;
    calCount.value = result.data.length;

  } catch (err) {
    meetList.value = [];

    Swal.fire({
      icon: "error",
      title: "API 조회 오류",
      text: "Error : " + err
    });
  }
}

//일자별 일정 건수조회
const calCntList = ref([]);
const calGetCnt = async () => {
  try {
    const result = await axios.get(`/api/comm/calList/cnt/${dateFormat().slice(0, 7)+'-01'}`);
    calCntList.value = result.data;

  } catch (err) {
    calCntList.value = [];

    Swal.fire({
      icon: "error",
      title: "API 조회 오류",
      text: "Error : " + err
    });
  }
};

const bbsList = ref([]);
const bbsCount = ref(0);
const bbsGetList = async () => { //게시글 5건 조회
  try {
    const result = await axios.get(`/api/comm/bbsList`);

    bbsCount.value = result.data.length;
    bbsList.value = result.data.map(item => ({
      ...item,
      createDt: dateFormat(item.createDt)
    }));

  } catch (err) {
    bbsList.value = [];

    Swal.fire({
      icon: "error",
      title: "API 조회 오류",
      text: "Error : " + err
    });
  }
}

// 게시글 상세 조회 이동
const goToBulletinInfo = (row) => {
  router.push({ path: `/bulletin/bulletinInfo/${row.bbsId}/${row.nttId}` });
};
</script>
