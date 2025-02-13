<template>
  <div class="content">
    <div class="container-fluid">

      <!--등록폼 [S]-->
      <div class="card">
        <div class="card-body">
          <h4 class="card-title float-left mt-1">게시글 등록</h4>
          <button class="btn btn-primary btn-sm btn-fill float-right" onclick="location.href ='#/admin/project/add'">등록</button>
          <button class="btn btn-secondary btn-sm btn-fill float-right">초기화</button>
        </div>
      </div>

      <div class="card">
        <div class="card-body">
          <form action="BoardAdd" method="post">
            <div class="mb-3">
              <label>제목 <em class="point-red ">*</em></label>
              <input type="text" class="form-control" placeholder="제목을 입력해주세요">
            </div>

            <div class="mb-3">
              <label>내용 <em class="point-red">*</em></label>
              <textarea class="form-control w50" placeholder="내용을 입력해주세요" rows="10"></textarea>
            </div>

            <div class="mb-3">
              <label>공지여부</label>
              <div class="form-check form-check-inline" style="margin-left: 10px;">
                <input class="form-check-input" type="checkbox" id="inlineCheckbox1" value="option1">
              </div>
            </div>

            <div class="mb-3">
              <label>익명여부</label>
              <div class="form-check form-check-inline" style="margin-left: 10px;">
                <input class="form-check-input" type="checkbox" id="inlineCheckbox2" value="option2">
              </div>
            </div>

            <div class="mb-3">
              <label>비밀여부</label>
              <div class="form-check form-check-inline" style="margin-left: 10px;">
                <input class="form-check-input" type="checkbox" id="inlineCheckbox3" value="option3" @click="togglePasswordField">
              </div>
            </div>

            <div class="mb-3" v-show="showPasswordField">
              <label>비밀번호 4자리를 입력해주세요</label>
              <input type="password" class="form-control w30" placeholder="비밀번호 입력" maxlength="4">
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

            <div class="mb-3">
            <label>파일첨부 <em class="point-red ">*</em></label>
              <div class="input-group w30" style="border: 1px solid #ccc; border-radius: 3px; background-color: #fff;">
               <button class="btn btn-success btn-fill" type="button" style="border-top-left-radius: 5px; border-bottom-left-radius: 5px;">파일선택</button>  
              </div>
            </div>
          </form>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
export default {
  name: "billing-card",
  data() {
    return {
      showPasswordField: false, // 비밀번호 필드 표시 여부
    };
  },
  methods: {
    togglePasswordField() {
      this.showPasswordField = !this.showPasswordField; // 체크박스 클릭 시 필드 표시/숨기기
    }
  }
};
</script>
