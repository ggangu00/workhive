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

          <div class="row">
            <div class="col">
              
              <!-- 테이블 -->
              <div class="table-responsive">
                <table class="table" style="table-layout: fixed;">
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
                      <th>근무 일자</th>
                      <td><input type="date" id="wordDate" class="form-control"></td>
                      <th>출근 시간</th>
                      <td><input type="time" class="form-control"></td>
                      <th>퇴근 시간</th>
                      <td><input type="time" class="form-control"></td>
                    </tr>
                    <tr>
                      <th>정정 출근 시간</th>
                      <td colspan="2"><input type="time" class="form-control" style="width: 47.5%;"></td>
                      <th>정정 퇴근 시간</th>
                      <td colspan="2"><input type="time" class="form-control" style="width: 47.5%;"></td>
                    </tr>
                    <tr>
                      <th>정정 사유</th>
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

<script setup>

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
  
    