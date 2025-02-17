<template>
    <ApprovalList :buttons="buttons" :columnDefs="columnDefs" :rowData="rowData"
    @button-click="linkButton" />

</template>
  
  <script>
  import ApprovalList from '@/components/PaymentLayout/ApprovalList.vue'
  import axios from 'axios';

  export default {
    components: { ApprovalList },
    methods:{
      //문서기안버튼
      linkButton(action){
        window.location.href = action;
      },

      //정보요청
      async documentList(){
        const response = await axios.get("/api/document/list", {
        params: { status: "H02",
                  page: 1,
                  pageSize: 10
        }
          
      });

      console.log("응답데이터=>", response.data);

      this.rowData = response.data;

      //console.log("바인딩된 데이터=>",this.rowData[0].docCnEditor)
    }
    },
    
    data() {
      return {
        buttons: [
          { label: '기안', class: 'btn-warning btn-fill', action: 'http://localhost:8077/RegisterTest'},
          { label: '인쇄/다운로드', class: 'btn-excel' }
        ],
        columnDefs: [
          { header: '문서번호', name: 'docCd' },
          { header: '현재결재상태', name: 'crntSignStat' },
          { header: '종류', name: 'docKind' },
          { header: '양식', name: 'formCd' },
          { header: '제목', name: 'docTitle' },
          { header: '기안부서', name: 'deptNm' },
          { header: '기안자', name: 'memCd' },
          { header: '기안일시', name: 'draftDt' },
          //{ header: '내용', name:'docCnEditor'}
        ],
        rowData: []
      }
    },
    mounted(){
      this.documentList();
    }
  }
  </script>
  