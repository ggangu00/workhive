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
  import Swal from 'sweetalert2';
  import { generatePDF } from '@/assets/js/pdfDownload.js'; 

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

      case '인쇄/다운로드' :
        await downloadBtn();
        break;  
    }
  }

  //회수코드
  const retrieveBtn = async () => {
    if (!docCd.value) {
      Swal.fire({
      icon: "error",
      title: "등록실패",
      text: "문서가 없습니다.",
    })
        return;
      }
        const response = await axios.put(`/api/document/retrieve/${docCd.value}`, {});
        if (response.status === 200) {
          Swal.fire({
               icon: "success",
               title: "회수 성공",
               text: "임시함에서 확인하세요"
            })
        } else {
          Swal.fire({
          icon: "errowr",
          title: "회수실패"
          })
        }
    };

const downloadBtn = async () => {
  await generatePDF(docCnEditor.value, docTitle.value || "document");
};

  </script>