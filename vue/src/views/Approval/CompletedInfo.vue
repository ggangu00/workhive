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
//import axios from '../../assets/js/customAxios';
import { useRoute } from 'vue-router';
//import jsPDF from "jspdf";
//import html2canvas from "html2canvas";
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
      Swal.fire({
      icon: "error",
      title: "등록실패",
      text: "문서가 없습니다.",
    })
      return;
    }
  };

const downloadBtn = async () => {
  await generatePDF(docCnEditor.value, docTitle.value || "document");
};

</script>