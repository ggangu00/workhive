<template>

  <div class="content">
    <div class="container-fluid">
      <!-- 페이지 헤더 -->
      <div class="row">
        <div class="col">
          <h3>휴가 신청 관리</h3>
        </div>
      </div>
  
      <!-- 휴가 정보 -->
      <div class="row">
        <div class="col">
            
          <div class="row">
            <div class="col header">
              <h4>  [ 휴가 정보 ]</h4>
              <hr>
            </div>
          </div>
  
          <div class="row">
            <div class="col vc-card">
              <div class="vc-header">대상연도</div>
              <div class="vc-body">2025</div>
              <hr>
            </div>
            
            <div class="col vc-card">
              <div class="vc-header">부여일수</div>
              <div class="vc-body">15</div>
              <hr>
            </div>
            
            <div class="col vc-card">
              <div class="vc-header">잔여일수</div>
              <div class="vc-body">13</div>
              <hr>
            </div>
            
            <div class="col vc-card">
              <div class="vc-header">신청가능일수</div>
              <div class="vc-body">11</div>
              <hr>
            </div>
            
            <div class="col vc-card">
              <div class="vc-header">결재대기</div>
              <div class="vc-body">1</div>
              <hr>
            </div>
            
            <div class="col vc-card">
              <div class="vc-header">보완요청</div>
              <div class="vc-body">1</div>
              <hr>
            </div>
          </div>
  
        </div>
      </div>
  
      <!-- 휴가 신청 등록/수정 -->
      <div class="row">
        <div class="col">
            
          <div class="row">
            <div class="col header">
              <h4>  [ 휴가 신청 등록/수정 ]</h4>
              <hr>
            </div>
          </div>
  
          <!-- 휴가 관리 -->
          <div class="row">
            <div class="col input-table align-itmes-center">
  
              <div class="row">
                <div class="col-2 table-header">휴가 시작일</div>
                <div class="col">
                  <input type="date" id="startDate" class="form-control w-90">
                </div>
                <div class="col-2 table-header">휴가 종료일</div>
                <div class="col">
                  <input type="date" id="endDate" class="form-control w-90">
                </div>
              </div>
              
              <div class="row">
                <div class="col-2 table-header">휴가 종류</div>
                <div class="col">
                  <select class="select-custom">
                    <option selected>휴가종류</option>
                    <option value="">연차</option>
                    <option value="">오전반차</option>
                    <option value="">오후반차</option>
                    <option value="">공가</option>
                  </select>
                </div>
                <div class="col-2 table-header">사용일수</div>
                <div class="col">
                  <input type="text" class="form-control w-90">
                </div>
                <div class="col-2 table-header">예상잔여일수</div>
                <div class="col">
                  <input type="text" class="form-control w-90">
                </div>
              </div>
              
              <div class="row">
                <div class="col-2 table-header">휴가 사유</div>
                <div class="col">
                  <textarea name="" id="" class="w-95"></textarea>
                </div>
              </div>
  
              <div class="row">
                <div class="col-2 table-header">파일 첨부</div>
                <div class="col"></div>
              </div>
              
              <div class="row">
                <div class="col-2 table-header">결재자</div>
                <div class="col">
                  <input type="text" class="form-control w-95">
                </div>
              </div>
  
              <div class="row justify-content-center">
                <button class="btn btn-secondary btn-fill">취소</button>
                <button class="btn btn-success btn-fill">저장</button>
              </div>
  
            </div>
          </div>
          <!-- 휴가 관리 끝-->
  
  
        </div>
      </div>
  
    </div>
  </div>
  
  </template>
  
  <script>
  
  </script>
  
  
  <style scoped>
  .content {
    padding: 0 !important;
  }
  .container-fluid > .row {
    background-color: white;
    border-radius: 7px;
    margin: 10px 0 10px 0;
    padding: 5px 0 5px 0;
  }
  h3, h4 {
    margin: 5px 0;
    font-weight: 900;
  }
  
  
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
  
  .input-custom {
    border: 1px solid #d2d6da;
    border-radius: 5px;
  }
  .input-table {
    margin: 5px 10px;
  }
  .input-table > * > * {
    border: 1px solid;
    padding: 5px 0 5px 10px;
    font-size: 17px;
  }
  .table-header {
    background-color: #d2d6da;
    color: black;
    font-weight: bold;
  }
  
  button {
    width: 80px;
    padding: 5px 10px !important;
  }
  </style>
    
    