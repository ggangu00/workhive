<template>
    <div class="content">
      <ApprovalInfo
        :headButtons="headButtons"
        :ApprovalButtons="true"
        :showFile="true"
        @button-click="buttonClick"
      />
      <!--  이벤트추가-->
    </div>
  </template>
  
  <script setup>
  import {onMounted, ref} from 'vue';
  import ApprovalInfo from '@/components/PaymentLayout/ApprovalInfo.vue';
  import axios from 'axios';
  import { useRoute } from 'vue-router';
  import { useRouter } from 'vue-router';

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
  })
  //버튼명
  const headButtons = ref([
    { label: '회수', class: 'btn-primary' },
    { label: '재기안', class: 'btn-warning' },
    { label: '삭제', class: 'btn-success' }
  ]);

  //버튼별 기능
  const buttonClick = async (buttonName)=>{
    console.log(buttonName)
    switch(buttonName){
      case '회수' :
        await retrieveBtn();
        break;

      case '재기안':
        restartDraft();
        break;

      // case '삭제':
      //   await deleteDocument();
      //   break;
  
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

  //재기안코드
  const router = useRouter();
  const restartDraft = () => {
    if (!docCd.value) {
      alert("문서 코드 x");
      return;
    }
    router.push(`/approval/restartDraft?docCd=${docCd.value}`);
    router.push({
    path: '/approval/restartDraft',
    query: {
      docCd: docCd.value,
      docKind: docKind.value,
      formNm: formNm.value,
      deptNm: deptNm.value,
      docTitle: docTitle.value,
      docCnEditor: docCnEditor.value
    }
  });
  };
  </script>