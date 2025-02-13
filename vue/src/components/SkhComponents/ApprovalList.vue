<template>
  <div class="content">
    <div class="container-fluid">
      <div class="row">
        <div class="col-12">
          <div class="card">
            <!-- 제목 -->
            <h3 class="card-title" style="padding: 15px;">{{ title }}</h3>

            <!-- 버튼 & 필터 -->
            <div class="button-collection d-flex justify-content-between align-items-center flex-wrap" style="padding: 15px;">
              <div class="d-flex">
                <button 
                  v-for="(btn, index) in buttons" 
                  :key="index"
                  :class="['btn', btn.class, 'btn-fill']">
                  {{ btn.label }}
                </button>
              </div>

              <div class="selectbox d-flex">
                <select class="form-select w10" name="doc_kind">
                  <option disabled selected>전체</option>
                  <option value="">일반결재</option>
                  <option value="">수신결재</option>
                </select>
                <select class="form-select w10" name="doc_kind">
                  <option disabled selected>부서</option>
                  <option value="">기획팀</option>
                  <option value="">영업팀</option>
                </select>
                <select class="form-select w10" name="doc_kind">
                  <option disabled selected>양식</option>
                  <option value="">구입신청서</option>
                  <option value="">외부지출서</option>
                  <option value="">증명서</option>
                </select>
                <div class="d-flex">
                  <div class="input-group">
                    <span class="input-group-text fw-bold">기안일(시작)</span>
                    <input type="date" class="form-control">
                  </div>
                  <span class="fw-bold">~</span>
                  <div class="input-group">
                    <span class="input-group-text fw-bold">기안일(종료)</span>
                    <input type="date" class="form-control">
                  </div>
                </div>
                <button class="btn btn-secondary btn-fill">초기화</button>
              </div>
            </div>
          </div>
        </div>

        <!-- 🔹 Toast UI Grid 영역 -->
        <div class="col-12">
          <div id="tableGrid" class="toastui-grid-container"></div>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
import "tui-grid/dist/tui-grid.css";
import Grid from "tui-grid";

export default {
  props: {
    title: { type: String, required: true },
    buttons: { type: Array, required: true },
    columnDefs: { type: Array, required: true }, // Column 정의
    rowData: { type: Array, required: true } // 데이터
  },
  mounted() {
    this.initToastGrid();
  },
  methods: {
    initToastGrid() {
      this.grid = new Grid({
        el: document.getElementById("tableGrid"),
        data: this.rowData, // 데이터 적용
        scrollX: true,
        scrollY: true,
        columns: this.columnDefs, // 컬럼 정의 적용
        
        pageOptions: {
          useClient: true, // 클라이언트 사이드 페이지네이션
          perPage: 5 // 한 페이지에 5개 항목 표시
        }
      });
    }
  }
};
</script>

<style scoped>
.button-collection button {
  margin-right: 10px;
}
.selectbox select {
  margin-right: 10px;
}
.toastui-grid-container {
  width: 100%;
  height: 500px;
}
</style>
