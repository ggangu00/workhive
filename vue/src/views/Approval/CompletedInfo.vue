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
  const formNm = ref('');
  const deptNm = ref('');
  const docTitle = ref('');
  const docCnEditor = ref('');

  //넘긴 문서코드
  onMounted(()=>{
    docCd.value = route.query.docCd || "";
    docKind.value = route.query.docKind || "";
    formNm.value = route.query.formNm || "";
    deptNm.value = route.query.deptNm || "";
    docTitle.value = route.query.docTitle || "";
    docCnEditor.value = route.query.docCnEditor || "";

    window.history.replaceState({}, '', route.path);
  })
  //버튼명
  const headButtons = ref([
    { label: '인쇄/다운로드', class: 'btn-success btn-fill' },  
    { label: '공람발송', class: 'btn-primary btn-fill' },
  ]);




  //버튼별 기능
  const buttonClick = async (buttonName)=>{
    switch(buttonName){
      case '공람발송' :
        await retrieveBtn();
        break;

      case '인쇄/다운로드' :
        await downloadBtn();
        break;  
    }


  }

  //회수코드
  const retrieveBtn = async () => {
      if (!docCd.value) {
        alert("문서 코드 x");
        return;
      }
    };

  //다운로드 코드

  const downloadBtn = () => {
    downloadClobFile(docCnEditor.value, "document.html", "text/html");
  };

  const downloadClobFile = (content, fileName, mimeType) => {
    const blob = new Blob([content], { type: mimeType });
    const link = document.createElement("a");
    link.href = URL.createObjectURL(blob);
    link.download = fileName;
    document.body.appendChild(link);
    link.click();
    document.body.removeChild(link);
    URL.revokeObjectURL(link.href);
  };
  // const downloadBtn = async () =>{
  //   alert('다운로드');
  // }

  </script>