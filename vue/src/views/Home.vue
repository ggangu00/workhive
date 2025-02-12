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
              <div class="status-content">2건</div>
            </div>

            <div class="status-box projects">
              <div class="status-title status-green">진행중인 프로젝트</div>
              <div class="status-content">2건</div>
            </div>

            <div class="status-box projects">
              <div class="status-title status-blue">진행중인 프로젝트</div>
              <div class="status-content">2건</div>
            </div>

          </div>
        </div>
      </div>


      <div class="row" style="background-color: transparent;">
        <div class="col-5" style=" padding-left: 0;">
          <div class="card">
            <div class="home-container">
              <div class="home-container-header">참여예정 회의</div>

              <div class="meeting-item">
                <div class="meeting-category">프로젝트</div>
                <div class="meeting-title">빵둘2 빵순2 킥오프 회의 <span class="badge badge-warning">D-day</span></div>
                <div class="meeting-date">2025-02-05(수) 10:00</div>
              </div>

              <div class="meeting-item">
                <div class="meeting-category">일정</div>
                <div class="meeting-title">세미나 일정 회의 <span class="badge badge-warning">D-day</span></div>
                <div class="meeting-date">2025-02-10(월) 10:30</div>
              </div>

              <div class="meeting-item">
                <div class="meeting-category">외부</div>
                <div class="meeting-title">업체 과업 조율 회의 <span class="badge badge-warning">D-day</span></div>
                <div class="meeting-date">2025-02-12(수) 16:00</div>
              </div>
            </div>
          </div>

        </div>

        <div class="col-7" style="padding-right: 0;">
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