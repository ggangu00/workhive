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
import { useRouter } from 'vue-router';
import Swal from 'sweetalert2';

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

  console.log(docKind.value)
  window.history.replaceState({}, '', route.path);
})
//버튼명
const headButtons = ref([
  { label: '수신기안', class: 'btn-warning' },
  { label: '수신확인', class: 'btn-success' }
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
    alert("문서 코드 x");
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

</script>