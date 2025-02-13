<template>
  <div class="content">
    <div class="container-fluid">
      <div class="card">
        <div class="card-body">
          <h4 class="card-title float-left mt-1">업무일지 관리</h4>
        </div>
      </div>

      <div class="card todo-container">
        <div class="todo-header">
          <button>캘린더 펼침 ▼</button>
        </div>
        <div class="month">
          <button>&lt;</button>
          2025.01
          <button>&gt;</button>
        </div>
        <div class="days">
          <button class="day red">1</button>
          <button class="day">2</button>
          <button class="day">3</button>
          <button class="day blue">4</button>
          <button class="day red">5</button>
          <button class="day">6</button>
          <button class="day">7</button>
          <button class="day">8</button>
          <button class="day">9</button>
          <button class="day">10</button>
          <button class="day blue">11</button>
          <button class="day red">12</button>
          <button class="day">13</button>
          <button class="day">14</button>
          <button class="day">15</button>
          <button class="day">16</button>
          <button class="day">17</button>
          <button class="day blue">18</button>
          <button class="day red">19</button>
          <button class="day">20</button>
          <button class="day">21</button>
          <button class="day">22</button>
          <button class="day">23</button>
          <button class="day">24</button>
          <button class="day blue">25</button>
          <button class="day red">26</button>
          <button class="day">27</button>
          <button class="day">28</button>
          <button class="day">29</button>
          <button class="day">30</button>
          <button class="day">31</button>
        </div>
      </div>

      <div class="card">
        <div class="card-body">
          <div class="row">
            <div class="col-2">
              <select class="form-select float-right">
                <option value="1">내부업무</option>
                <option value="2">외부업무</option>
                <option value="3">프로젝트</option>
              </select>
            </div>
            <div class="col-8">
              <input type="text" class="form-control float-right" placeholder="업무내용을 입력해주세요">
            </div>
            <div class="col-2">
              <button class="btn btn-secondary btn-fill btn-sm float-left">등록</button>
              <button class="btn btn-primary btn-fill btn-sm float-left">상세등록</button>
            </div>
          </div>
          <div class="table-responsive">
            <table class="table table-hover project" style="margin-bottom: 0;">
              <thead>
                <tr>
                  <th width="50px">
                    <div class="form-check">
                      <input class="form-check-input" type="checkbox" id="inlineCheckbox1" value="option1">
                    </div>
                  </th>
                  <th>
                    <button class="btn btn-secondary btn-fill btn-sm float-left">선택미완료</button>
                    <button class="btn btn-secondary btn-fill btn-sm float-left">선택완료</button>
                    <button class="btn btn-danger btn-fill btn-sm float-left">선택삭제</button>
                  </th>
                  <th></th>
                  <th></th>
                  <th></th>
                </tr>
              </thead>
            </table>
            <table class="table table-hover project">
              <colgroup>
                <col width="50px">
                <col width="10%">
                <col width="10%">
                <col>
                <col width="13%">
              </colgroup>
              <tbody>
                <tr class="table-secondary">
                  <td>
                    <div class="form-check">
                      <input class="form-check-input" type="checkbox" id="inlineCheckbox1" value="option1">
                    </div>
                  </td>
                  <td>완료</td>
                  <td>내부업무</td>
                  <td align="left">비품 재고 정리</td>
                  <td>
                    <button class="btn btn-success btn-fill btn-sm mr-1">수정</button>
                    <button class="btn btn-danger btn-fill btn-sm mr-1">삭제</button>
                  </td>
                </tr>
                <tr class="table-secondary">
                  <td width="50px">
                    <div class="form-check">
                      <input class="form-check-input" type="checkbox" id="inlineCheckbox1" value="option1">
                    </div>
                  </td>
                  <td>완료</td>
                  <td>내부업무</td>
                  <td align="left">비품 재고 정리</td>
                  <td>
                    <button class="btn btn-success btn-fill btn-sm mr-1">수정</button>
                    <button class="btn btn-danger btn-fill btn-sm mr-1">삭제</button>
                  </td>
                </tr>
                <tr>
                  <td width="50px">
                    <div class="form-check">
                      <input class="form-check-input" type="checkbox" id="inlineCheckbox1" value="option1">
                    </div>
                  </td>
                  <td>완료</td>
                  <td>내부업무</td>
                  <td align="left">비품 재고 정리</td>
                  <td>
                    <button class="btn btn-success btn-fill btn-sm mr-1">수정</button>
                    <button class="btn btn-danger btn-fill btn-sm mr-1">삭제</button>
                  </td>
                </tr>
              </tbody>
            </table>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>