<template>
    <ApprovalList :buttons="buttons" :columnDefs="columnDefs" :rowData="rowData" />
  </template>
  
  <script>
  import ApprovalList from '@/components/SkhComponents/ApprovalList.vue'
  
  export default {
    components: { ApprovalList },
    data() {
      return {
        buttons: [
          { label: '기안', class: 'btn-primary' },
          { label: '공람발송', class: 'btn-warning' },
          { label: '인쇄/다운로드', class: 'btn-excel' }
        ],
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
          { 문서번호: '총무-2025', 현재결재상태: '승인', 종류: '일반결재', 양식: '구입신청서', 제목: 'test20250208', 기안부서: '총무팀', 기안자: '홍길동', 기안일시: '2025-02-03' }
        ]
      }
    }
  }
  </script>
  