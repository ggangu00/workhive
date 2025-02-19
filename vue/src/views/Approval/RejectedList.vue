<template>
  <ApprovalList 
    :buttons="buttons" 
    :columnDefs="columnDefs" 
    :status="status"
    @button-click="linkButton"
  />
</template>

<script>
import ApprovalList from '@/components/PaymentLayout/ApprovalList.vue'
//import axios from 'axios';

export default {
  
  components: { ApprovalList },
  methods:{
      //문서기안버튼
      linkButton(action){
        window.location.href = action;
      },
  },
  
  data() { 
    return {
      buttons: [
        { label: '기안', class: 'btn-warning btn-fill', action: 'http://localhost:8077/registerTest'},
        { label: '재기안', class: 'btn-primary btn-fill' },
        { label: '인쇄/다운로드', class: 'btn-excel' }
      ],

      columnDefs: [
        { header: '문서번호', name: 'docCd' },
        { header: '현재결재상태', name: 'crntSignStat' },
        { header: '종류', name: 'docKind' },
        { header: '양식', name: 'formNm' },
        { header: '제목', name: 'docTitle' },
        { header: '기안부서', name: 'deptNm' },
        { header: '기안자', name: 'memCd' },
        { header: '기안일시', name: 'draftDt' },
      ],
      status: "H03",
    }
  },
}
</script>
