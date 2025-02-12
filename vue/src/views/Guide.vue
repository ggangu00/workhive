<template>
  <div class="content">
    <div class="container-fluid">

      <!--등록폼 [S]-->
      <div class="card">
        <div class="card-body">
          <h4 class="card-title float-left mt-1">뭐시기 등록</h4>
          <button class="btn btn-primary btn-sm btn-fill float-right"
            onclick="location.href ='#/admin/project/add'">등록</button>
          <button class="btn btn-secondary btn-sm btn-fill float-right">초기화</button>
        </div>
      </div>
      <div class="card">
        <div class="card-body">
          <form action="BoardAdd" method="post">
            <div class="mb-3">
              <label>제목 <em class="point-red">*</em></label>
              <input type="text" class="form-control" placeholder="제목을 입력해주세요">
            </div>
            <div class="mb-3">
              <label>검색</label>
              <div class="row">
                <div class="col-auto">
                  <input type="text" class="form-control" placeholder="제목을 입력해주세요">
                </div>
                <div class="col-auto">
                  <button class="btn btn-info btn-fill">검색</button>
                </div>
              </div>
            </div>
            <div class="mb-3">
              <label>셀렉트</label>
              <select class="form-select" aria-label="Default select example">
                <option selected>Open this select menu</option>
                <option value="1">One</option>
                <option value="2">Two</option>
                <option value="3">Three</option>
              </select>
            </div>

            <div class="mb-3">
              <label>체크박스</label>
              <div>
                <div class="form-check form-check-inline">
                  <input class="form-check-input" type="checkbox" id="inlineCheckbox1" value="option1">
                  <label class="form-check-label" for="inlineCheckbox1">1</label>
                </div>
                <div class="form-check form-check-inline">
                  <input class="form-check-input" type="checkbox" id="inlineCheckbox2" value="option2">
                  <label class="form-check-label" for="inlineCheckbox2">2</label>
                </div>
                <div class="form-check form-check-inline">
                  <input class="form-check-input" type="checkbox" id="inlineCheckbox3" value="option3" disabled>
                  <label class="form-check-label" for="inlineCheckbox3">3 (disabled)</label>
                </div>
              </div>
            </div>

            <div class="mb-3">
              <label>라디오</label>
              <div>
                <div class="form-check form-check-inline">
                  <input class="form-check-input" type="radio" name="inlineRadioOptions" id="inlineRadio1"
                    value="option1">
                  <label class="form-check-label" for="inlineRadio1">1</label>
                </div>
                <div class="form-check form-check-inline">
                  <input class="form-check-input" type="radio" name="inlineRadioOptions" id="inlineRadio2"
                    value="option2">
                  <label class="form-check-label" for="inlineRadio2">2</label>
                </div>
                <div class="form-check form-check-inline">
                  <input class="form-check-input" type="radio" name="inlineRadioOptions" id="inlineRadio3"
                    value="option3" disabled>
                  <label class="form-check-label" for="inlineRadio3">3 (disabled)</label>
                </div>
              </div>
            </div>
          </form>
        </div>
      </div>
      <!--등록폼 [E]-->



      <div class="col-12">
        <div class="card">
          <div class="card-header">
            <h4 class="card-title">안내문구</h4>
          </div>
          <div class="card-body">
            <div class="alert alert-primary">
              <button type="button" aria-hidden="true" class="close" data-dismiss="alert">
                <i class="bi bi-x-lg"></i>
              </button>
              <span><b> Primary - </b> This is a regular notification made with ".alert-primary"</span>
            </div>
            <div class="alert alert-info">
              <button type="button" aria-hidden="true" class="close" data-dismiss="alert">
                <i class="bi bi-x-lg"></i>
              </button>
              <span><b> Info - </b> This is a regular notification made with ".alert-info"</span>
            </div>
            <div class="alert alert-success">
              <button type="button" aria-hidden="true" class="close" data-dismiss="alert">
                <i class="bi bi-x-lg"></i>
              </button>
              <span><b> Success - </b> This is a regular notification made with ".alert-success"</span>
            </div>
            <div class="alert alert-warning">
              <button type="button" aria-hidden="true" class="close" data-dismiss="alert">
                <i class="bi bi-x-lg"></i>
              </button>
              <span><b> Warning - </b> This is a regular notification made with ".alert-warning"</span>
            </div>
            <div class="alert alert-danger">
              <button type="button" aria-hidden="true" class="close" data-dismiss="alert">
                <i class="bi bi-x-lg"></i>
              </button>
              <span><b> Danger - </b> This is a regular notification made with ".alert-danger"</span>
            </div>
          </div>

          <div class="card-header">
            <h4 class="card-title">버튼</h4>
          </div>
          <div class="card-body">
            <button class="btn btn-primary btn-fill">등록/확인/선택</button>
            <button class="btn btn-success btn-fill">저장/수정</button>
            <button class="btn btn-danger btn-fill">삭제</button>

            <button class="btn btn-warning btn-fill">정정요청/보완/수신</button>
            <button class="btn btn-info btn-fill">검색/조회</button>
            <button class="btn btn-info btn-fill"><i class="bi bi-search"></i></button>
            <button class="btn btn-secondary btn-fill">초기화/닫기</button>
            <button class="btn btn-excel">엑셀다운로드</button>

            <button class="btn btn-primary">승인</button>
            <button class="btn btn-danger">반려</button>
          </div>

          <div class="card-header">
            <h4 class="card-title">강조색상</h4>
          </div>
          <div class="card-body">
            <p class="text-primary">
              Text Primary - Light Bootstrap Table Heading and complex bootstrap dashboard you've ever seen on the
              internet.
            </p>
            <p class="text-info">
              Text Info - Light Bootstrap Table Heading and complex bootstrap dashboard you've ever seen on the
              internet.
            </p>
            <p class="text-success">
              Text Success - Light Bootstrap Table Heading and complex bootstrap dashboard you've ever seen on the
              internet.
            </p>
            <p class="text-warning">
              Text Warning - Light Bootstrap Table Heading and complex bootstrap dashboard you've ever seen on the
              internet.
            </p>
            <p class="text-danger">
              Text Danger - Light Bootstrap Table Heading and complex bootstrap dashboard you've ever seen on the
              internet.
            </p>
          </div>

          <div class="card-header">
            <h4 class="card-title">테이블</h4>
          </div>
          <div class="card-body">
            <div class="table-responsive">
              <table class="table table-hover">
                <thead>
                  <tr>
                    <th>Id</th>
                    <th>Name</th>
                    <th>Salary</th>
                    <th>Country</th>
                    <th>City</th>
                  </tr>
                </thead>
                <tbody>
                  <tr>
                    <td>1</td>
                    <td>정상</td>
                    <td>$36.738</td>
                    <td>Niger</td>
                    <td>Oud-Turnhout</td>
                  </tr>
                  <tr>
                    <td>2</td>
                    <td>정상</td>
                    <td>$23,789</td>
                    <td>Curaçao</td>
                    <td>Sinaai-Waas</td>
                  </tr>
                  <tr>
                    <td>3</td>
                    <td>정상</td>
                    <td>$56,142</td>
                    <td>Netherlands</td>
                    <td>Baileux</td>
                  </tr>
                  <tr>
                    <td>4</td>
                    <td>정상</td>
                    <td>$38,735</td>
                    <td>Korea, South</td>
                    <td>Overland Park</td>
                  </tr>
                  <tr class="table-secondary">
                    <td>5</td>
                    <td>완료</td>
                    <td>$63,542</td>
                    <td>Malawi</td>
                    <td>Feldkirchen in Kärnten</td>
                  </tr>
                </tbody>
              </table>
            </div>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>