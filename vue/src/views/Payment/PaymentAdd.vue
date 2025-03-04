<template>
 <div class="content">
    <div class="container-fluid">

      <!--등록폼 [S]-->
      <div class="card">
        <div class="card-body">
          <h4 class="card-title float-left mt-1">외부지출 등록</h4>
          <button class="btn btn-primary btn-sm btn-fill float-right" onclick="location.href ='#/admin/project/add'">등록</button>
          <button class="btn btn-secondary btn-sm btn-fill float-right">초기화</button>
        </div>
      </div>

      <!-- 입력 폼 -->
      <div class="card">            
          <div class="card-body">
            <form action="BoardAdd" method="post">
              <div class="mb-3">
                <label>작성자 <em class="point-red">*</em></label>
                <input type="text" class="form-control w50" placeholder="제목을 입력해주세요">
              </div>

              <div class="mb-3">
                <label>지출항목 <em class="point-red">*</em></label>
                <input type="text" class="form-control w50" placeholder="지출항목을 입력해주세요">
              </div>

              <div class="mb-3">
              <label class="form-label">사용일 <em class="point-red">*</em></label>
              <div class="row">
                <div class="col-auto">
                  <input type="date" name="use_dt" class="form-control">
                </div>                              
              </div>
            </div>

              <div class="mb-3">
                <label>금액 <em class="point-red">*</em></label>
                <input type="text" class="form-control w50" placeholder="금액을 입력해주세요">
              </div>

              <div class="mb-3">
              <label>파일첨부  <em class="point-red">*</em></label>
              <div class="input-group w30" style="border: 1px solid #ccc; border-radius: 3px; background-color: #fff;">
                <input type="file" class="form-control" ref="fileInput" />
              </div>
            </div>
            </form>
          </div>
        </div>
    </div>

</div>

</template>

<script>

</script>