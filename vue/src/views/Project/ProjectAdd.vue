<template>
  <div class="content">
    <div class="container-fluid">
      <div>
        <div class="card">
          <div class="card-body">
            <h4 class="card-title float-left mt-1">프로젝트 등록</h4>
            <button class="btn btn-primary btn-sm btn-fill float-right"
              onclick="location.href ='#/admin/project/add'">등록</button>
            <button class="btn btn-secondary btn-sm btn-fill float-right">초기화</button>
          </div>
        </div>
        <form action="BoardAdd" method="post">
          <div class="card">
            <div class="card-header">
              <h5 class="card-title">1. 프로젝트 정보</h5>
            </div>
            <div class="card-body">
              <div class="mb-3">
                <div class="form-group has-label">
                  <label>프로젝트명 <em class="point-red">*</em></label>
                </div>
                <input type="text" name="project_nm" class="form-control" placeholder="프로젝트명을 입력해주세요">
              </div>
              <div class="mb-3">
                <label class="form-label">프로젝트 구분 <em class="point-red">*</em></label>
                <select class="form-select w30" aria-label="Default select example">
                  <option value="A03">내부프로젝트</option>
                  <option value="A04">외부프로젝트</option>
                </select>
              </div>
              <div class="mb-3">
                <label class="form-label">프로젝트 기간 <em class="point-red">*</em></label>
                <div class="row">
                  <div class="col-auto">
                    <input type="date" name="start_dt" class="form-control">
                  </div>
                  <div class="col-auto">~</div>
                  <div class="col-auto">
                    <input type="date" name="end_dt" class="form-control">
                  </div>
                </div>
              </div>
              <div class="mb-3">
                <label class="form-label">프로젝트 금액</label>
                <div class="row">
                  <div class="col-auto">
                    <input type="number" name="price" class="form-control" placeholder="0" style="text-align: right;">
                  </div>
                  <div class="col-auto">
                    <span class="form-text">
                      원
                    </span>
                  </div>
                </div>
              </div>
              <div class="mb-3">
                <label class="form-label">내부완료일</label>
                <input type="date" name="ahead_dt" class="form-control w30">
              </div>
              <div class="mb-3">
                <label class="form-label">거래처</label>
                <div class="row">
                  <div class="col-auto">
                    <input type="text" class="form-control" placeholder="제목을 입력해주세요">
                  </div>
                  <div class="col-auto">
                    <button class="btn btn-info btn-fill">검색</button>
                  </div>
                </div>
              </div>
            </div>

          </div>

          <div class="card">
            <div class="card-body">
            <h5 class="card-title float-left mt-1">2. 프로젝트 과업</h5>
          </div>
            <div class="card-body">
              <button class="btn btn-secondary btn-sm btn-fill float-right"
              onclick="location.href ='#/admin/project/add'">과업추가</button>
              <table class="table">
                <colgroup>
                  <col width="10%">
                  <col>
                  <col width="15%">
                  <col width="10%">
                  <col width="5%">

                </colgroup>
                <thead>
                  <tr>
                    <th></th>
                    <th>과업내용</th>
                    <th>진행률</th>
                    <th>완료상태</th>
                    <th></th>
                  </tr>
                </thead>
                <tbody>
                  <tr>
                    <td><i class="bi bi-arrows-vertical"></i></td>
                    <td><input type="text" name="PR_WORK_NM" class="form-control"></td>
                    <td><input type="number" name="PROGRESS" class="form-control"></td>
                    <td>
                      <select class="form-select" aria-label="Default select example">
                        <option value="A01">완료</option>
                        <option value="A02" selected>미완료</option>
                      </select>
                    </td>
                    <td><a><i class="bi bi-x-lg"></i></a></td>
                  </tr>
                </tbody>
              </table>
            </div>
          </div>
          <div class="text-center">
            <button type="submit" class="btn btn-primary btn-fill">
              등록
            </button>
            <button class="btn btn-secondary btn-fill">
              초기화
            </button>
          </div>
        </form>
      </div>
    </div>
  </div>
</template>
