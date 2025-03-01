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
  import axios from '../../assets/js/customAxios';
  import { useRoute } from 'vue-router';

  const route = useRoute();
  const docCd = ref('');
  const docKind = ref('');
  const formType = ref('');
  const deptNm = ref('');
  const docTitle = ref('');
  const docCnEditor = ref('');
  const atchFileId =ref('');

  //넘긴 문서코드
  onMounted(()=>{
    docCd.value = route.query.docCd || "";
    docKind.value = route.query.docKind || "";
    formType.value = route.query.formType || "";
    deptNm.value = route.query.deptNm || "";
    docTitle.value = route.query.docTitle || "";
    docCnEditor.value = route.query.docCnEditor || "";
    atchFileId.value=route.query.atchFileId || "";

    window.history.replaceState({}, '', route.path);
  })
  //버튼명
  const headButtons = ref([
    { label: '인쇄/다운로드', class: 'btn-success btn-fill' },
    { label: '회수', class: 'btn-primary' },
  ]);

  //버튼별 기능
  const buttonClick = async (buttonName)=>{
    switch(buttonName){
      case '회수' :
        await retrieveBtn();
        break;
    }
  }

  //회수코드
  const retrieveBtn = async () => {
    if (!docCd.value) {
        alert("문서 코드 x");
        return;
      }
        const response = await axios.put(`/api/document/retrieve/${docCd.value}`, {});
        if (response.status === 200) {
          alert("회수 성공");
        } else {
          alert("회수 실패");
        }
    };


  </script>