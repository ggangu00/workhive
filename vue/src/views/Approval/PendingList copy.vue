<template>
  <PendingListComp
    :buttons="buttons" 
    :columnDefs="columnDefs" 
    :status="status"
    @button-click="buttonClick"
    ref="approvalRegisterRef"
  />
</template>

<script setup>
import PendingListComp from '@/components/PaymentLayout/PendingListComp.vue'
import {ref} from'vue';
import { useRouter } from 'vue-router'; 

const router = useRouter(); 

const buttons = ref([
        { label: '기안', class: 'btn-warning btn-fill'},
        { label: '결재', class: 'btn-primary btn-fill' },
      ]);

const buttonClick = async (buttonName)=>{
    switch(buttonName){
      case '기안' :       
      router.push('/approval/registerTest');
      break;
      
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
        { header: '문서번호', name: 'docCd',sortable: true },
        { header: '현재결재상태', name: 'crntSignStat',sortable: true },
        { header: '종류', name: 'docKind',sortable: true },
        { header: '양식', name: 'formCd',sortable: true },
        { header: '제목', name: 'docTitle' ,sortable: true},
        { header: '기안부서', name: 'deptNm',sortable: true },
        { header: '기안자', name: 'mberNm' ,sortable: true},
        { header: '기안일시', name: 'draftDt' ,sortable: true},
      ])
</script>
