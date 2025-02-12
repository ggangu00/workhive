<template>
  <div class="content">
    <div class="container-fluid">
    <div>
      <div class="card">
        <div class="card-body">
          <h4 class="card-title float-left mt-1">게시판 등록</h4>
          <button class="btn btn-primary btn-sm btn-fill float-right"
            onclick="location.href ='#/admin/project/add'">등록</button>
          <button class="btn btn-secondary btn-sm btn-fill float-right">초기화</button>
        </div>
      </div>
      </div>

      <!-- 입력 폼 -->
      <div class="card">            
          <div class="card-body">
            <form action="BoardAdd" method="post">
              <div class="mb-3">
                <label>제목 <em class="point-red">*</em></label>
                <input type="text" class="form-control" placeholder="제목을 입력해주세요">
              </div>
            
              <div class="mb-3">                 
                <label class="form-label">게시판 유형<em class="point-red">*</em></label>
                <select class="form-select w30" aria-label="Default select example">
                  <option selected>선택하세요</option>
                  <option value="1">공지사항</option>
                  <option value="2">사내게시판</option>                    
                </select>
              </div>

              <div class="mb-3">
                <label class="form-label">파일첨부 가능여부</label>
                <select class="form-select w30" aria-label="Default select example">
                  <option selected>선택하세요</option>
                  <option value="1">예</option>
                  <option value="2">아니오</option>                    
                </select>
              </div>

              <div class="mb-3">
                <label class="form-label">댓글 가능여부</label>
                <select class="form-select w30" aria-label="Default select example">                  
                  <option selected>선택하세요</option>
                  <option value="1">예</option>
                  <option value="2">아니오</option>                    
                </select>
              </div>
              </form>
            </div>
          </div>
        </div>
</div>




</template>

<script>
export default {
  name: '',
  data() {
    return {
      boardName: '',
      boardType: '',
      fileAttach: '',
      commentUsage: ''
    };
  },
  methods: {
    goToList() {
      this.$router.push({ name: 'bbsList' });
    },
  },
};
</script>

<style>


</style>
