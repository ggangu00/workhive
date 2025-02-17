<template>
    <div class="content">
      <ApprovalRegister
        :headButtons="headButtons"
        :ApprovalButtons="true"
        :showFile="true"
        @button-click="linkButton"
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
          { label: "회수", class: "btn-primary" },
          { label: "재기안", class: "btn-warning" },
          { label: "삭제", class: "btn-success" },
        ],
      };
    }
  };
  </script>