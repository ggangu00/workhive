<template>
    <div class="content">
      <ApprovalRegister
        :headButtons="headButtons"
        :ApprovalButtons="true"
        :showFile="true"
        @button-click="buttonClick"
        ref="approvalRegisterRef"
      />
      <!--  이벤트추가-->
    </div>
  </template> 
  
  <script setup>
  import ApprovalRegister from '@/components/PaymentLayout/ApprovalRegister.vue';
  import {ref} from 'vue';

  const headButtons = ref([ //빈값넣으면 버튼안나옴
          { label: "양식선택", class: "btn-primary" },
          { label: "초기화", class: "btn-warning" },
          { label: "상신", class: "btn-success" }
        ])

  const buttonClick = async (buttonName)=>{
    switch(buttonName){
      case '양식선택' :
        await formAdd();
        break;

      case '초기화' :
        await resetBtn();
        break;

      // case '결재문서첨부':
      //   await deleteDocument();
      //   break;
  
      case '상신':
        await approvalBtn();
        break;
    }
  }
  const approvalRegisterRef = ref(null);
  const formAdd = () =>{
      if (approvalRegisterRef.value) {
      approvalRegisterRef.value.modalOpen(); 
    }
  }

  const resetBtn = () =>{
      if (approvalRegisterRef.value) {
      approvalRegisterRef.value.conditionReset(); 
    }
  }

  const approvalBtn = () =>{
    if (approvalRegisterRef.value) {
      approvalRegisterRef.value.approvalInfo(); 
    }
  }
  </script>