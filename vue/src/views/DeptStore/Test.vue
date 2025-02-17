<template>

  <div class="content">
    <div class="container-fluid">
      <!-- 페이지 헤더 -->
      <div class="card">
        <div class="card-body">
          <h4 class="card-title float-left mt-1">출퇴근 정정 요청 관리</h4>
        </div>
      </div>
  
      <!-- 바디 -->
      <div class="card">
        <div class="card-body">

          <div class="row mt-5">
            <div class="col">
              
              <!-- 테이블 -->
              <div class="table-responsive">
                <table class="table table-hover" style="table-layout: fixed;">
                  <colgroup>
                    <col style="width: 16.6%;">
                    <col style="width: 16.6%;">
                    <col style="width: 16.6%;">
                    <col style="width: 16.6%;">
                    <col style="width: 16.6%;">
                    <col style="width: 16.6%;">
                  </colgroup>
                  <tbody>
                    <tr>
                      <th>휴가 시작일</th>
                      <td colspan="2"><input type="date" id="startDate" class="form-control"></td>
                      <th>휴가 종료일</th>
                      <td colspan="2"><input type="date" id="startDate" class="form-control"></td>
                    </tr>
                    <tr>
                      <th>휴가 종류</th>
                      <td>
                        <select class="form-select" aria-label="Default select example">
                          <option selected>휴가종류</option>
                          <option value="">연차</option>
                          <option value="">오전반차</option>
                          <option value="">오후반차</option>
                          <option value="">공가</option>
                        </select>
                      </td>
                      <th>사용 일수</th>
                      <td><input type="text" class="form-control"></td>
                      <th>예상 잔여 일수</th>
                      <td><input type="text" class="form-control"></td>
                    </tr>
                    <tr>
                      <th>휴가 사유</th>
                      <td colspan="5"><textarea name="" id=""  class="form-control"></textarea></td>
                    </tr>
                    <tr>
                      <th>파일 첨부</th>
                      <td colspan="5"><input type="file" class="form-control"></td>
                    </tr>
                    <tr>
                      <th>결재자</th>
                      <td colspan="5"><input type="text" class="form-control"></td>
                    </tr>
  
                  </tbody>
                </table>
              </div>
        
              <div class="row justify-content-center">
                <button class="btn btn-secondary btn-fill">취소</button>
                <button class="btn btn-success btn-fill">저장</button>
              </div>
            </div>
          </div>
  
        </div>
      </div>
      
  
    </div>
  </div>
  
</template>

<script>

</script>


<style scoped>
.header > hr {
  height: 2px;
  background-color: black;
  border: none;
}
.vc-card hr {
  border: 1px dashed gray;
  margin: 3px 0;
}
.vc-header {
  font-weight: bolder;
  font-size: 18px;
  margin-bottom: 15px;
}

th {
  background-color: #d2d6da !important;
  color: black !important;
  font-size: 16px;
}

button {
  width: 80px;
  padding: 5px 10px !important;
}
</style>
  
    