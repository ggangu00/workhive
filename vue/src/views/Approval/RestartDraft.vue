<template>
    <div class="content">
      <ApprovalRegister
        :headButtons="headButtons"
        :ApprovalButtons="true"
        :showFile="true"
        @button-click="buttonClick"
      />
      <!--  이벤트추가-->
    </div>
  </template> 
  
  <script setup>
  import ApprovalRegister from '@/components/PaymentLayout/ApprovalRegister.vue';
  import {ref} from 'vue';
  
  //변수선언
  const docCd = ref('');
  const docKind = ref('');
  const formNm = ref('');
  const deptNm = ref('');
  const docTitle = ref('');
  //const docCnEditor = ref('');



  const headButtons = ref([ //빈값넣으면 버튼안나옴
          { label: "양식선택", class: "btn-primary" },
          { label: "초기화", class: "btn-warning" },
          { label: "결재문서첨부", class: "btn-success" },
          { label: "상신", class: "btn-success" }
        ])

  const buttonClick = async (buttonName)=>{
    switch(buttonName){
      case '양식선택' :
        await formSelectBtn();
        break;

      case '초기화' :
        await resetBtn();
        break;

      // case '결재문서첨부':
      //   await deleteDocument();
      //   break;
  
      // case '상신':
      //   await deleteDocument();
      //   break;
    }
  }

  const formSelectBtn = async () => {};

  const resetBtn = async () => {
    docCd.value = '';
    docKind.value = '';
    formNm.value = '';
    deptNm.value = '';
    docTitle.value = '';
    //docCnEditor.value = '';
  };
  </script>