<template>
    <div class="content">
      <div class="container-fluid">
  
        <!--등록폼 [S]-->
        <div class="card">
          <div class="card-body">
            <h4 class="card-title float-left mt-1">게시글 상세조회</h4>          
            <button class="btn btn-danger btn-fill float-right">삭제</button>
            <button class="btn btn-success btn-fill float-right">수정</button>             
          </div>
        </div>
      
        <div class="card">
          <div class="card-body">
            <form action="BoardAdd" method="post">
              <div class="mb-3" style="text-align: center;">
                <label style="font-size: 30px;">안녕하세요</label>             
              </div>
  
              <div style="background-color: #f4f4f4; padding: 10px; text-align: center; font-size: 14px; color: #555; margin-bottom: 20px;">
                <span style="font-weight: bold;">작성자 홍길동</span> 
                <span style="margin: 0 15px;">|</span>
                <span style="font-weight: bold;">등록일 2025.02.05</span> 
                <span style="margin: 0 15px;">|</span>
                <span style="font-weight: bold;">조회수 1</span>
              </div>
  
              <div class="mb-3">              
                <textarea class="form-control w100" placeholder="내용을 입력해주세요" rows="10" style="margin-top: 20px;"></textarea>
              </div>
              
              <div class="mb-3">
              <label class="form-label">게시기간 <em class="point-red">*</em></label>
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
            
        <!-- 댓글 입력 -->
        <div class="mb-3">
        <label>댓글</label>
        <div class="input-group mb-3" style="background-color: #f8f9fa; padding: 10px; border-radius: 5px;">
          <input type="text" class="form-control" placeholder="내용을 입력해주세요" style="border-radius: 5px; margin-right: 10px;">
          <input type="text" class="form-control" placeholder="작성자" style="border-radius: 5px; margin-right: 10px;">
          <button class="btn btn-success btn-fill" style="border-radius: 5px;">등록</button>
        </div>
      </div>

      <!-- 댓글 목록 -->
      <div class="mb-3">
        <div class="input-group mb-3" style="background-color: #f8f9fa; padding: 10px; border-radius: 5px;">
          <input type="text" class="form-control" placeholder="내용을 입력해주세요" style="border-radius: 5px; margin-right: 10px;">
          <input type="text" class="form-control" placeholder="작성자" style="border-radius: 5px; margin-right: 10px;">
          <button class="btn btn-success btn-fill" style="border-radius: 5px; margin-right: 10px;">수정</button>
          <button class="btn btn-danger btn-fill" style="border-radius: 5px;">삭제</button>
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
  
  <style scoped>
  .btn-fill {
    padding: 10px 20px;
    font-size: 14px;
  }

  .form-control {
    font-size: 14px;
  }

  .input-group .form-control {
    font-size: 14px;
  }

  .input-group {
    display: flex;
    align-items: center;
    justify-content: space-between;
  }
</style>
