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
import jsPDF from "jspdf";
import html2canvas from "html2canvas";

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
      alert("문서 코드 x");
      return;
    }
  };

  const downloadBtn = async () => {
  if (!docCnEditor.value || docCnEditor.value.trim() === "") {
    alert("다운로드할 내용이 없습니다.");
    return;
  }

  // HTML 내용을 동적으로 div에 삽입
  const content = document.createElement("div");
  content.innerHTML = docCnEditor.value;
  content.style.width = "100%"; // 화면 너비 100% 설정
  content.style.maxWidth = "800px"; // 가독성을 위해 최대 너비 지정 (조정 가능)
  content.style.padding = "20px";
  content.style.background = "white"; // 배경색 지정
  document.body.appendChild(content); // 임시 추가

  try {
    // html2canvas로 HTML을 이미지로 변환 (html캡쳐 => 이미지로 변환)
    const canvas = await html2canvas(content, {
      scale: 3, // 해상도 높이기 (선명하게)
      useCORS: true, // CORS 이슈 방지
    });

    const imgData = canvas.toDataURL("image/png");
    const pdf = new jsPDF("p", "mm", "a4"); //a4크기 pdf생성

    // PDF 크기를 HTML 캔버스 크기와 동일하게 조정
    const pdfWidth = pdf.internal.pageSize.getWidth(); // A4 너비
    const pdfHeight = (canvas.height * pdfWidth) / canvas.width; // 자동 비율 맞춤

    pdf.addImage(imgData, "PNG", 0, 0, pdfWidth, pdfHeight); // PDF 크기에 맞게 추가
    pdf.save(`${docTitle.value || "document"}.pdf`); // PDF 파일 저장
  } catch (error) {
    console.error("PDF 변환 오류:", error);
    alert("PDF 변환 중 오류가 발생했습니다.");
  } finally {
    document.body.removeChild(content); // 임시 추가한 div 제거 (div없으면 domc요소처럼 못만들어서 화면 이미지로 캡쳐하기 위해 생성)
  }
};

</script>