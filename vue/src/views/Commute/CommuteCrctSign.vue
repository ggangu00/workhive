<template>

<div class="content">
  <div class="container-fluid">
    <!-- 페이지 헤더 -->
    <div class="row">
      <div class="col">
        <h3>출퇴근 정정 요청 결재</h3>
      </div>
      <div class="col-auto d-flex align-items-center">
        <button class="btn btn-primary btn-header">승인</button>
        <button class="btn btn-warning btn-fill btn-header">보완</button>
        <button class="btn btn-danger btn-header">반려</button>
      </div>
    </div>

    <!-- 페이지 바디 -->
    <div class="row">
      <div class="col">
          <!-- grid -->


      </div>
    </div>

    <!-- 페이지 헤더 -->
    <div class="row">
      <div class="col">
        <h3>출퇴근 정정 요청 결재 내역</h3>
      </div>
      <div class="col-auto d-flex align-items-center">
        <button class="btn btn-secondary btn-fill btn-header">결재취소</button>
      </div>
    </div>

    <!-- 페이지 바디 -->
    <div class="row">
      <div class="col">
          <!-- grid -->


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

.btn-header {
  height: 30px;
  width: 60px;
  padding: 0;
  margin: 2px !important;
}
</style>

  