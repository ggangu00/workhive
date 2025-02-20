<template>
  <div class="content">
    <div class="container-fluid">
      <div class="card">
        <div class="profile-section">
          <div class="profile-info">
            <div class="profile-icon"><i class="bi bi-person-fill"></i></div>
            <div>
              <small>개발팀</small>
              <h4>박주현 주임</h4>
            </div>
          </div>
          <div class="status-boxes">
            <div class="status-box attendance">
              <div class="status-title status-red">출/퇴근</div>
              <div class="status-content">
                출근 08:50<br>
                퇴근 --:--
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
              <template v-if="meetCount > 0">
                <div class="meeting-item" :key="i" v-for="(meet, i) in meetList">
                  <div class="meeting-category">{{ meet.typeCd }}</div>
                  <div class="meeting-title">{{ meet.mtgNm }} <span class="badge badge-warning">D{{
                    dateTermCalc(dateFormat(meet.mtgDe)) }}</span></div>
                  <div class="meeting-date">{{ dateFormat(meet.mtgDe) }}({{ dateGetDay(meet.mtgDe) }}) {{
                    meet.mtgBeginTm }} ~ {{ meet.mtgEndTm }}</div>
                </div>
              </template>
              <div v-else class="meeting-none">
                <div class="meeting-item" :key="i" v-for="i in 3">
                  <div class="meeting-category">-</div>
                  <div class="meeting-title">예정된 회의가 없습니다.</div>
                  <div class="meeting-date"></div>
                </div>
              </div>
            </div>
          </div>

        </div>

        <div class="col-7">
          <div class="card">
            <div class="calendar-container">
              <!-- Calendar Section -->
              <div class="flex-grow-1 cal" style="border-right:1px solid #b5b5b5">
                <div class="home-container-header">월간 일정</div>
                <table class="calendar-table">
                  <tr>
                    <th>일</th>
                    <th>월</th>
                    <th>화</th>
                    <th>수</th>
                    <th>목</th>
                    <th>금</th>
                    <th>토</th>
                  </tr>
                  <tr>
                    <td></td>
                    <td></td>
                    <td>1</td>
                    <td class="highlight">2</td>
                    <td>3</td>
                    <td class="highlight">4</td>
                    <td>5</td>
                  </tr>
                  <tr>
                    <td>6</td>
                    <td>7</td>
                    <td>8</td>
                    <td>9</td>
                    <td class="today">10</td>
                    <td>11</td>
                    <td>12</td>
                  </tr>
                  <tr>
                    <td>13</td>
                    <td>14</td>
                    <td>15</td>
                    <td>16</td>
                    <td>17</td>
                    <td>18</td>
                    <td>19</td>
                  </tr>
                  <tr>
                    <td>20</td>
                    <td>21</td>
                    <td>22</td>
                    <td>23</td>
                    <td>24</td>
                    <td>25</td>
                    <td>26</td>
                  </tr>
                  <tr>
                    <td>27</td>
                    <td>28</td>
                    <td>29</td>
                    <td>30</td>
                    <td>31</td>
                  </tr>
                </table>
              </div>
              <!-- Schedule Section -->
              <div class="home-container list">
                <div class="home-container-header">2025-01-10</div>

                <div class="meeting-item">
                  <div class="meeting-category">백업</div>
                  <div class="meeting-title">(전체) 사내 보안 문서 연간 정기백업</div>
                </div>

                <div class="meeting-item">
                  <div class="meeting-category">회의</div>
                  <div class="meeting-title">(개발팀) 개발팀 부서회의</div>
                </div>
              </div>
            </div>
          </div>
        </div>
      </div>

      <div class="card">

        <div class="home-container">
          <div class="home-container-header">게시판</div>
          <div class="board-item">
            <span class="badge badge-danger mrp10">공지</span>
            <span class="board-title">연말정산 제출 안내 및 출력 방법 안내</span>
            <span class="board-meta">관리자</span>
            <span class="board-meta">2025-02-01</span>
          </div>
          <div class="board-item">
            <span class="badge badge-danger mrp10">공지</span>
            <span class="board-title">★★ 2월 거래처별 변동사항 (필독) ★★</span>
            <span class="board-meta">관리자</span>
            <span class="board-meta">2025-02-01</span>
          </div>
          <div class="board-item">
            <span class="board-title">2025년 건강검진 미진료 대상자 안내</span>
            <span class="board-meta">관리자</span>
            <span class="board-meta">2025-02-01</span>
          </div>
          <div class="board-item">
            <span class="board-title">2025년 건강검진 미진료 대상자 안내</span>
            <span class="board-meta">관리자</span>
            <span class="board-meta">2025-02-01</span>
          </div>
          <div class="board-item">
            <span class="board-title">2025년 건강검진 미진료 대상자 안내</span>
            <span class="board-meta">관리자</span>
            <span class="board-meta">2025-02-01</span>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import axios from "axios";
import Swal from 'sweetalert2';
import { onBeforeMount, ref } from 'vue';
import { numberFormat, dateFormat, dateGetDay, dateTermCalc } from '../assets/js/common.js'

//---------------데이터-------------- 

onBeforeMount(() => {
  homeGetInfo();
  meetGetList();
});

//---------------axios--------------
const homeInfo = ref([]);
const homeGetInfo = async () => { // 대시보드 건수 조회
  try {
    const result = await axios.get(`/api/comm/homeInfo/2025001`);
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
</script>
