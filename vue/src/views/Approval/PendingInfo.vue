<template>
    <div class="content">
      <ApprovalInfo
        :headButtons="headButtons"
        :ApprovalButtons="false"
        :showFile="false"
        @button-click="buttonClick"
      />
      <!--  이벤트추가-->
    </div>
  </template>
  
  <script setup>
  import {onMounted, ref} from 'vue';
  import ApprovalInfo from '@/components/PaymentLayout/ApprovalInfo.vue';
  //import axios from 'axios';
  import { useRoute } from 'vue-router';

  const route = useRoute();
  const docCd = ref('');
  const docKind = ref('');
  const formType = ref('');
  const deptNm = ref('');
  const docTitle = ref('');
  const docCnEditor = ref('');

  //넘긴 문서코드
  onMounted(()=>{
    docCd.value = route.query.docCd || "";
    docKind.value = route.query.docKind || "";
    formType.value = route.query.formType || "";
    deptNm.value = route.query.deptNm || "";
    docTitle.value = route.query.docTitle || "";
    docCnEditor.value = route.query.docCnEditor || "";

    console.log('dsadsa',route.query.formType)
    window.history.replaceState({}, '', route.path);
  })
  //버튼명
  const headButtons = ref([
    { label: '인쇄/다운로드', class: 'btn-success btn-fill' }, 
    { label: '결재', class: 'btn-primary' },
  ]);

  //버튼별 기능
  const buttonClick = async (buttonName)=>{
    switch(buttonName){
      case '결재' :
        await retrieveBtn();
        break;
    }
  }

  //회수코드
  const retrieveBtn = async () => {

    };


  </script>