<template>
    <div class="content">
      <ApprovalInfo
        :headButtons="headButtons"
        :ApprovalButtons="true"
        :showFile="true"
        @button-click="retrieveBtn"
      />
      <!--  이벤트추가-->
    </div>
  </template>
  
  <script>
  import ApprovalInfo from '@/components/PaymentLayout/ApprovalInfo.vue';
  import axios from 'axios';
  
  export default {
    components: {
      ApprovalInfo
    },
    data() {
      return {
        headButtons: [ //빈값넣으면 버튼안나옴
          { label: "회수", class: "btn-primary"},
          { label: "재기안", class: "btn-warning" },
          { label: "삭제", class: "btn-success" },
        ],
      };
    },
    methods:{
      async retrieveBtn(docCd){
        await axios.put(`/api/retrieve/${docCd}`)
        //window.location.href = action;
      }
    }
  };
  </script>