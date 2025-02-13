<template>
     <div class="content">
    <div class="container-fluid">

    
      <div class="card">
        <div class="card-body">          
          <h4 class="card-title float-left mt-1">외부지출 승인/반려 관리</h4>   
          <button class="btn btn-danger float-right">반려</button>     
          <button class="btn btn-primary float-right">승인</button>            
        </div>
      </div>

      <div class="card">
        <div class="card-body">
          <form action="BoardAdd" method="post">
            <div class="mb-3">
              <div class="col" style="height: 550px;">
              <div id="paymentGrid"></div>
            </div>
          </div>
          </form>
        </div>
      </div>
    </div>
</div>

    </template>
    
    <script setup>      
    import { ref, onMounted } from 'vue'
    
    let gridInstance = ref();
    
    // Toast UI Grid 초기화
    onMounted(() => {
      gridInstance.value = new window.tui.Grid({
        el: document.getElementById('paymentGrid'),
        data: [],   
        scrollX: false,
        scrollY: true,
        pageOptions:{
            useClient:true,
            perPage:5
         },  
         rowHeaders: [
        { type: 'rowNum', width: 100, align: 'left', valign: 'bottom' },
        { type: 'checkbox' }
      ],     
        columns: [
        {header : "부서", name:"dept", editor: "text"},
        {header : "작성자", name:"writer",  editor: "text"},
        {header : "지출항목", name:"EXPEN_ITEM",  editor: "text"},
        {header : "금액", name:"amount",  editor: "text"},
        {header : "사용일", name:"date",  editor: "text"},
        {header : "승인상태", name:"PYM_STATUS",  editor: "text"},
        ],    
      })
    })
    </script>