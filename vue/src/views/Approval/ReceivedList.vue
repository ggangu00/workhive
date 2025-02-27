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
          { label: '수신접수', class: 'btn-warning btn-fill'},
          { label: '수신기안', class: 'btn-warning btn-fill'},
          { label: '인쇄/다운로드', class: 'btn-excel' }
        ]);
  
  const buttonClick = async (buttonName)=>{
      switch(buttonName){
        case '기안' :       
          {
            const registerPage = 'http://localhost:8077/approval/registerTest';
            window.location.href = registerPage;
            break;
          }
  
        // case '인쇄/다운로드' :
        //   await approvalBtn();
        //   break;
      }
    }  
    const approvalRegisterRef = ref(null);
  
        const columnDefs = ref([
          { header: '문서번호', name: 'docCd',sortable: true },
          { header: '현재결재상태', name: 'crntSignStat',sortable: true },
          { header: '종류', name: 'docKind',sortable: true },
          { header: '양식', name: 'formCd',sortable: true },
          { header: '제목', name: 'docTitle' ,sortable: true},
          { header: '기안부서', name: 'deptNm',sortable: true },
          { header: '기안자', name: 'mberId' ,sortable: true},
          { header: '기안일시', name: 'draftDt' ,sortable: true},
        ])
  
        const status = "H01";
  </script>
  