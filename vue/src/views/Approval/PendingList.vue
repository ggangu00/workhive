<template>
  <ApprovalList 
    :buttons="buttons" 
    :columnDefs="columnDefs" 
    :status="status"
    @button-click="buttonClick"
    ref="approvalRegisterRef"
  />
</template>

<script setup>
import ApprovalList from '@/components/PaymentLayout/ApprovalList.vue'
import {ref} from'vue';

const buttons = ref([
        { label: '기안', class: 'btn-warning btn-fill'},
        { label: '결재', class: 'btn-primary btn-fill' },
        { label: '인쇄/다운로드', class: 'btn-excel' }
      ]);

const buttonClick = async (buttonName)=>{
    switch(buttonName){
      case '기안' :       
        {
          const registerPage = 'http://localhost:8077/registerTest';
          window.location.href = registerPage;
          break;
        }
    
        case '결재' :
        await approvalBtn();
        break;

      // case '인쇄/다운로드' :
      //   await approvalBtn();
      //   break;
    }
  }  
  const approvalRegisterRef = ref(null);
  const approvalBtn = () =>{
    if(approvalRegisterRef.value){
      approvalRegisterRef.value.btnSelectChange();
    }
  }

      const columnDefs = ref([
        { header: '문서번호', name: 'docCd' },
        { header: '현재결재상태', name: 'crntSignStat' },
        { header: '종류', name: 'docKind' },
        { header: '양식', name: 'formCd' },
        { header: '제목', name: 'docTitle' },
        { header: '기안부서', name: 'deptNm' },
        { header: '기안자', name: 'mberId' },
        { header: '기안일시', name: 'draftDt' },
      ])

      const status = "H01";
</script>
