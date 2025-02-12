<template>
    <div class="content">
      <div class="container-fluid">
        <div class="row">
            <div class='col-12'>
                <div class='card'>
                    <div class="button-collection d-flex justify-content-between align-items-center flex-wrap" style='padding: 15px ;'>
                        <div class='d-flex'>
                            <button class="btn btn-primary btn-fill">기안</button>
                            <button class="btn btn-warning btn-fill">공람발송</button>
                            <button class="btn btn-excel">인쇄/다운로드</button>
                        </div>
                        <div class='selectbox d-flex'>
                            <select class="form-select w10" aria-label="Default select example" name="doc_kind">
                                <option disabled selected>전체</option>
                                <option value="">일반결재</option>
                                <option value="">수신결재</option>
                            </select>
                            <select class="form-select w10" aria-label="Default select example" name="doc_kind">
                                <option disabled selected>부서</option>
                                <option value="">기획팀</option>
                                <option value="">영업팀</option>
                                <option value="">영업팀</option>
                            </select>
                            <select class="form-select w10" aria-label="Default select example" name="doc_kind">
                                <option disabled selected>양식</option>
                                <option value="">구입신청서</option>
                                <option value="">외부지출서</option>
                                <option value="">증명서</option>
                            </select>
                            <select class="form-select w10" aria-label="Default select example" name="doc_kind">
                                <option disabled selected>전체</option>
                                <option value="">기안승인된문서</option>
                                <option value="">결재완료된문서</option>
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
          <div class="col-12">
              <div class="ag-theme-alpine">
                <ag-grid-vue
                  class="ag-theme-alpine"
                  :columnDefs="columnDefs"
                  :rowData="rowData"
                  :gridOptions="gridOptions"
                  :pagination="true"
                  :paginationPageSize="5">
                </ag-grid-vue>
              </div>
          </div>  
        </div>
      </div>
    </div>
  </template>
  <script>
    import 'ag-grid-community/styles/ag-grid.css'
    import 'ag-grid-community/styles/ag-theme-alpine.css'
    import { AgGridVue } from 'ag-grid-vue3'
    
    export default {
      components: {
        AgGridVue 
      },
      data () {
        return {
          gridOptions: {
            rowSelection: 'multiple', // 🔹 다중 선택 가능
          },
          columnDefs: [
            { headerName: '문서번호', field: '문서번호', checkboxSelection: true },
            { headerName: '현재결재상태', field: '현재결재상태' },
            { headerName: '종류', field: '종류' },
            { headerName: '양식', field: '양식' },
            { headerName: '제목', field: '제목' },
            { headerName: '기안부서', field: '기안부서' },
            { headerName: '기안자', field: '기안자' },
            { headerName: '기안일시', field: '기안일시' }
          ],
          rowData: [
            { 문서번호: '총무-2025', 현재결재상태: '승인', 종류: '일반결재', 양식: '구입신청서', 제목: 'test20250208', 기안부서: '총무팀', 기안자: '홍길동', 기안일시: '2025-02-03' },
            { 문서번호: '영업-2025', 현재결재상태: '진행중', 종류: '일반결재', 양식: '외부지출서', 제목: 'test20250209', 기안부서: '영업팀', 기안자: '홍길순', 기안일시: '2025-02-04' },
            { 문서번호: '기획-2025', 현재결재상태: '대기', 종류: '일반결재', 양식: '증명서', 제목: 'test20250210', 기안부서: '기획팀', 기안자: '홍길담', 기안일시: '2025-02-05' }
          ]
        }
      }
    }
  </script>
  <style>
    .button-collection button{
        margin-right: 10px;
    }
    .selectbox select{
        margin-right: 10px;
    }
    .ag-theme-alpine {
      width: 100%;
      height: 500px;
    }
  </style>
  