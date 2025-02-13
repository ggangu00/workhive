<template>
    <div class="content">
      <ApprovalRegister
        :headButtons="headButtons"
        :ApprovalButtons="true"
        :showFile="true"
      />
      <!--  이벤트추가-->
    </div>
  </template>
  
  <script>
  import ApprovalRegister from '@/components/PaymentLayout/ApprovalRegister.vue';
  
  export default {
    components: {
        ApprovalRegister
    },
    data() {
      return {
        headButtons: [ //빈값넣으면 버튼안나옴
          { label: "양식선택", class: "btn-primary" },
          { label: "초기화", class: "btn-warning" },
          { label: "결재문서첨부", class: "btn-success" },
          { label: "상신", class: "btn-success" }
        ],
      };
    }
  };
  </script>