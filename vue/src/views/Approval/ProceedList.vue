<template>
  <ApprovalList 
    :buttons="buttons" 
    :columnDefs="columnDefs" 
    :status="status"
    :status1="status1"
    :title="title"
    @button-click="buttonClick"
    ref="approvalRegisterRef"
  />
</template>

<script setup>
import ApprovalList from '@/components/PaymentLayout/ApprovalList.vue'
//import axios from 'axios';
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
  ]);

  const columnDefs = ref([
  { header: '문서번호', name: 'docCd', sortable: true, align : 'center'  },
  { header: '현재결재상태', name: 'crntSignStat', sortable: true, align : 'center'  },
  { header: '종류', name: 'docKind', sortable: true, align : 'center'  },
  { header: '양식', name: 'formType', sortable: true, align : 'center'  },
  { header: '제목', name: 'docTitle', sortable: true, align : 'center'  },
  { header: '기안부서', name: 'deptNm', sortable: true, align : 'center'  },
  { header: '기안자', name: 'mberNm', sortable: true, align : 'center'  },
  { header: '기안일시', name: 'draftDt', sortable: true },
]);
const status = "H02";
const status1 = "H01";
const title = "진행함";
</script>
