<template>

  <div class="content">
    <div class="container-fluid">
      <!-- 페이지 헤더 -->
      <div class="row">
        <div class="col">
          <h3>휴가 조회</h3>
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
              <h4>  [ 휴가 신청 목록 ]</h4>
              <hr>
            </div>
          </div>
  
          <!-- 조회 조건 -->
          <div class="row search align-items-center justify-content-end">
            <div class="col-auto">
              <select class="select-custom">
                <option selected>휴가종류</option>
                <option value="">연차</option>
                <option value="">오전반차</option>
                <option value="">오후반차</option>
                <option value="">공가</option>
              </select>
            </div>
  
            <div class="col-auto">
              <select class="select-custom">
                <option selected>결재상태</option>
                <option value="">승인</option>
                <option value="">보완</option>
                <option value="">반려</option>
              </select>
            </div>
  
            <div class="col-auto">
              <input type="date" id="startDate" class="input-custom">
              <a class="align-middle"> ~ </a>
              <input type="date" id="endDate" class="input-custom">
            </div>
          </div>
  
          <!-- 휴가 신청 목록 -->
          <div class="row mt-3">
            <div class="col" style="height: 300px; width: 800px;">

            </div>
          </div>
  
        </div>
      </div>
    </div>
  </div>
  
  </template>
  
  <script>
  
  export default {
    components: {
    },
    data() {
      return {
        
      };
    },
    methods: {
      // statusRenderer(params) {
      //   let status = params.value;
  
      //   let btnTag;
      //   if (status === "보완") {
      //     btnTag = `<button class="btn btn-warning btn-fill cell-btn-custom">
      //                 ${status}
      //               </button>
      //               <button class="btn btn-danger btn-fill cell-btn-custom">
      //                 삭제
      //               </button>`;
      //   }
      //   else if (status === "대기") {
      //     btnTag = `<button class="btn btn-success btn-fill cell-btn-custom">
      //                 ${status}
      //               </button>
      //               <button class="btn btn-danger btn-fill cell-btn-custom">
      //                 삭제
      //               </button>`;
      //   }
      //   else if (status === "승인") {
      //     btnTag = `<a>${status}</a>`;
      //   }
  
      //   return btnTag;
      // }
    }
  };
  
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
  
  .search > * > input, select {
    border: 1px solid #d2d6da;
    border-radius: 5px;
    font-size: 13px;
    line-height: 30px;
    height: 30px;
    width: 100px;
  }
  
  div::v-deep .cell-btn-custom {
    width: 30px;
    height: 25px;
    padding: 0;
  }
  </style>
  
    