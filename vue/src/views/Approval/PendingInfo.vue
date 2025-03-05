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
  import axios from '../../assets/js/customAxios.js';
  import { useRoute } from 'vue-router';
  import { useUserInfoStore } from '../../store/userStore.js';
  import Swal from 'sweetalert2';
  import { generatePDF } from '@/assets/js/pdfDownload.js'; // 다운로드

const userInfoStore = useUserInfoStore();
let loginUser = userInfoStore.user ? userInfoStore.user.mberId : ""; // 로그인한 사용자 정보 가져오기


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
    { label: '결재', class: 'btn-primary' },
  ]);

  //버튼별 기능
  const buttonClick = async (buttonName)=>{
    switch(buttonName){
      case '결재' :
        await approvalBtn();
        break;

      case '인쇄/다운로드' :
        await downloadBtn();
        break;
    }
  }

//결재 버튼 기능(다중)
const approvalBtn = () => {
  Swal.fire({
    title: "결재 진행",
    input: 'textarea',
    showDenyButton: true,
    showCancelButton: true,
    confirmButtonText: "승인",
    denyButtonText: "반려"
  }).then(async (result) => {
    let modeText = '';
    let newSignStat = '';
    if (result.isConfirmed) {
      modeText = "승인";
      newSignStat = "D02";
    } else if (result.isDenied) {
      modeText = "반려";
      newSignStat = "D04";
    } else {
      return; // 취소 시 함수 종료
    }

    try {
      const response = await axios.put(`/api/document/state`, {
        approvalArr: [docCd.value],
        mberId: loginUser , // 실제 로그인 아이디로 변경
        signStat: newSignStat
      });

      if (response.statusText == "OK") {
        Swal.fire({
          icon: "success",
          title: modeText + " 완료",
          text: "선택한 문서를 " + modeText + "하였습니다",
        });
        await axios.put(`/api/document/approvalOpn`,{
          signOpn: result.value,
          docCd : docCd.value,
          mberId : loginUser

        })
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

const downloadBtn = async () => {
  await generatePDF(docCnEditor.value, docTitle.value || "document");
};

const title="미결문서"
  </script>