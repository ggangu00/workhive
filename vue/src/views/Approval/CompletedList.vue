<template>
  <ApprovalList
    :buttons="buttons"
    :columnDefs="columnDefs"
    :status="status"
    :title="title"
    @button-click="buttonClick"
    ref="approvalRegisterRef"
  />
</template>

<script setup>
import ApprovalList from '@/components/PaymentLayout/ApprovalList.vue'
//import axios from '../../assets/js/customAxios.js';
import {ref} from'vue';
import { useRouter } from 'vue-router';

const router = useRouter();

const buttonClick = async (buttonName) => {
  switch (buttonName) {
    case '기안':
      router.push('/approval/registerTest'); 
      break;

    // case '인쇄/다운로드':
    //   await approvalBtn();
    //   break;
  }
};

  const buttons = ref([
  { label: '기안', class: 'btn-warning btn-fill' },
  //{ label: '공람발송', class: 'btn-primary btn-fill' },
  ]);

  const columnDefs = ref([
  { header: '문서번호', name: 'docCd', sortable: true },
  { header: '현재결재상태', name: 'crntSignStat', sortable: true },
  { header: '종류', name: 'docKind', sortable: true },
  { header: '양식', name: 'formType', sortable: true },
  { header: '제목', name: 'docTitle', sortable: true },
  { header: '기안부서', name: 'deptNm', sortable: true },
  { header: '기안자', name: 'mberNm', sortable: true },
  { header: '기안일시', name: 'draftDt', sortable: true },
]);
const status = "H04";
const title = "완료함";

</script>
