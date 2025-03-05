<template>
  <div class="content">
    <ApprovalInfo
      :headButtons="headButtons"
      :ApprovalButtons="false"
      :showFile="false"
      :title="title"
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
import { useRouter } from 'vue-router';
import Swal from 'sweetalert2';
import { generatePDF } from '@/assets/js/pdfDownload.js'; 

const route = useRoute();
const docCd = ref('');
const docKind = ref('');
const formType = ref('');
const formCd = ref('');
const deptNm = ref('');
const docTitle = ref('');
const docCnEditor = ref('');
const atchFileId =ref('');

//넘긴 문서코드
onMounted(()=>{
  docCd.value = route.query.docCd || "";
  docKind.value = route.query.docKind || "";
  formType.value = route.query.formType || "";
  formCd.value = route.query.formCd || "";
  deptNm.value = route.query.deptNm || "";
  docTitle.value = route.query.docTitle || "";
  docCnEditor.value = route.query.docCnEditor || "";
  atchFileId.value=route.query.atchFileId || "";

  window.history.replaceState({}, '', route.path);
})
//버튼명
const headButtons = ref([
  { label: '수신기안', class: 'btn-warning btn-fill' },
  { label: '수신확인', class: 'btn-primary btn-fill' },
  { label: '인쇄/다운로드', class: 'btn-success btn-fill' }
]);

//버튼별 기능
const buttonClick = async (buttonName)=>{
  switch(buttonName){
    case '수신확인' :
      await btnSelectChange();
      break;

    case '수신기안':
      await restartDraft();
      break;

    case '인쇄/다운로드' :
      await downloadBtn();
      break;
  }
}

//회수코드
//수신확인 버튼 기능(다중)
const btnSelectChange = () => {
  Swal.fire({
    title: "수신 진행",
    text: "수신접수 하시겠습니까?",
    showDenyButton: false,
    showCancelButton: true,
    cancelButtonText:"닫기",
    reverseButtons:true,
    confirmButtonText: "수신접수",
  }).then(async (result) => {
    let modeText = '';
    let newReceptYn = '';
    if (result.isConfirmed) {
      modeText = "수신접수";
      newReceptYn = "A01";
    } else {
      return; // 취소 시 함수 종료
    }

    try {
      const response = await axios.put(`/api/document/receivedState`, {
        receptlArr: [docCd.value],
        receptYn: newReceptYn
      });

      if (response.statusText == "OK") {
        Swal.fire({
          icon: "success",
          title: modeText + " 완료",
          text: "선택한 문서를 " + modeText + "하였습니다",
        });
      }
    } catch (err) {
      Swal.fire({
        icon: "error",
        title: modeText + " 실패",
        text: "Error : " + err,
      });
    }
  });
}

//재기안코드
const router = useRouter();
const restartDraft = () => {
  if (!docCd.value) {
    Swal.fire({
      icon: "error",
      title: "등록실패",
      text: "문서가 없습니다.",
    })
    return;
  }
  router.push(`/approval/restartDraft?docCd=${docCd.value}`);
  router.push({
  path: '/approval/restartDraft',
  query: {
    docCd: docCd.value,
    docKind: '수신결재',
    formType: formType.value,
    formCd: formCd.value,
    deptNm: deptNm.value,
    docTitle: docTitle.value,
    docCnEditor: docCnEditor.value
  }
});
};
const downloadBtn = async () => {
  await generatePDF(docCnEditor.value, docTitle.value || "document");
};
const title="수신문서"
</script>