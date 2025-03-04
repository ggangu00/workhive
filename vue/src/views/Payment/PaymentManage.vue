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
        data: [
        { id: 1, name: '홍길동', items: 30, },
        { id: 2, name: '김철수', items: 25 },
        { id: 3, name: '이영희', items: 28 },
        { id: 1, name: '홍길동', items: 30 },
        { id: 2, name: '김철수', items: 25 },
        { id: 3, name: '이영희', items: 28 }
        
        
        ], 
          
        scrollX: false,
        scrollY: true,
        pageOptions:{
            useClient:true,
            perPage:3
         },  
         rowHeaders: [       
        { type: 'checkbox' }
      ],    
       
        columns: [
        { name: 'id', header: '부서', sortable: true },  // 정렬 가능
        { name: 'name', header: '작성자', sortable: true, filter: 'text' }, // 검색 필터 추가
        { name: 'items', header: '지출항목', sortable: true },
        { name : 'age', header: '금액', sortable: true },
        { name: 'id', header: '사용일', sortable: true },  
        { name: 'name', header: '승인상태', sortable: true, filter: 'text' }, 
       
        
        ],   
         
      })
    })
    </script>