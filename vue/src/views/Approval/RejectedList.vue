<template>
  <ApprovalList 
    :buttons="buttons" 
    :columnDefs="columnDefs" 
    :status="status"
    @button-click="buttonClick"
    ref="approvalRegisterRef"
  />
</template>

<script setup>
import ApprovalList from '@/components/PaymentLayout/ApprovalList.vue'
import axios from '../../assets/js/customAxios';
import {ref , onMounted} from'vue';
import { useRouter } from 'vue-router'; 
import { useRoute } from 'vue-router';
import Swal from 'sweetalert2';

const route = useRoute();
const docCd = ref('');
const router = useRouter();

onMounted(()=>{
  docCd.value = route.query.docCd || "";
  window.history.replaceState({}, '', route.path);
})

const buttonClick = async (buttonName) => {
  switch (buttonName) {
    case '기안':
      router.push('/approval/registerTest'); 
      break;

      case '회수':
      await reteriveBtn();
      break;
  }
};

const reteriveBtn = () => {
  Swal.fire({
    title: "회수 진행",
    text: "회수 하시겠습니까?",
    showDenyButton: false,
    showCancelButton: true,
    confirmButtonText: "회수",
  }).then(async (result) => {
    let modeText = '';
    if (result.isConfirmed) {
      modeText = "회수";
      try {
      const response = await axios.put(`/api/document/reteriveState`, {
        retrieveArr: [docCd.value],
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
    } else {
      return; // 취소 시 함수 종료
    }

    
  });
}

const buttons = ref([
  { label: '기안', class: 'btn-warning btn-fill' },
  { label: '회수', class: 'btn-primary btn-fill' },
]);

const columnDefs = ref([
  { header: '문서번호', name: 'docCd', sortable: true },
  { header: '현재결재상태', name: 'crntSignStat', sortable: true },
  { header: '종류', name: 'docKind', sortable: true },
  { header: '양식', name: 'formCd', sortable: true },
  { header: '제목', name: 'docTitle', sortable: true },
  { header: '기안부서', name: 'deptNm', sortable: true },
  { header: '기안자', name: 'mberNm', sortable: true },
  { header: '기안일시', name: 'draftDt', sortable: true },
]);
const status = "H03";
</script>
