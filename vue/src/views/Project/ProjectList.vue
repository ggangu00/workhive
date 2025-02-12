<template>
  <div class="content">
    <div class="container-fluid">
      <div class="card">
        <div class="card-body">
          <h4 class="card-title float-left mt-1">프로젝트 조회</h4>
          <button class="btn btn-primary btn-sm btn-fill float-right" onclick="location.href ='/project/add'">프로젝트
            등록</button>
        </div>
      </div>
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
            <button class="btn btn-excel btn-fill btn-sm">엑셀다운로드</button>
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
            <thead>
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
              <tr>
                <td>
                  <div class="form-check">
                    <input class="form-check-input" type="checkbox" id="inlineCheckbox1" value="option1">
                  </div>
                </td>
                <td>1</td>
                <td>진행중</td>
                <td>
                  <div class="category">(대구)사람이좋다협회</div>
                  <div class="subject">아동복지 홈페이지 구축 용역
                    <span class="badge badge-danger">D-10</span>
                  </div>
                </td>
                <td>2025-01-01 ~ 2025-03-31</td>
                <td>30,000,000</td>
                <td>김지환</td>
                <td><button class="btn btn-primary btn-sm" onclick="location.href ='/project/plan'">일정등록</button></td>
                <td>2024-12-01</td>
                <td>
                  <button class="btn btn-success btn-fill btn-sm mr-1">수정</button>
                  <button class="btn btn-danger btn-fill btn-sm mr-1">삭제</button>
                </td>
              </tr>
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
              </tr>
            </tbody>
          </table>
        </div>
      </card>
    </div>
  </div>
</template>

<script>
import Card from '@/components/Cards/Card.vue'

export default {
  components: {
    Card
  },
}

</script>
<style></style>
