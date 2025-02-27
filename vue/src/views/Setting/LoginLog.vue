<template>
   <div class="content">
      <div class="container-fluid">
         <card>
            <div class="mb-3 row">
               <label for="staticEmail" class="col-sm-2 col-form-label">검색</label>
               <div class="col-auto">
                  <select class="form-select" aria-label="Default select example">
                     <option value="1">아이디</option>
                     <option value="2">아이피</option>
                  </select>
               </div>
               <div class="col-auto">
                  <input type="text" class="form-control" placeholder="검색어를 입력해주세요">
               </div>
            </div>
            <div class="mb-3 row">
               <label for="inputPassword" class="col-sm-2 col-form-label">조회 기간</label>
               <div class="col-auto">
                  <input type="date" name="START_DT" class="form-control">
               </div>
               <div class="col-auto">~</div>
               <div class="col-auto">
                  <input type="date" name="END_DT" class="form-control">
               </div>
            </div>
            <div class="text-center">
               <button type="submit" class="btn btn-secondary btn-fill">
                  초기화
               </button>
               <button class="btn btn-info btn-fill">
                  검색
               </button>
            </div>
         </card>
         <card>
            <div id="logGrid"></div>
         </card>
      </div>
   </div>
</template>

<script setup>
import axios from "axios";
import Swal from 'sweetalert2';
import { ref, onMounted, onBeforeMount } from 'vue';
import Card from '../../components/Cards/Card.vue'
import Grid from 'tui-grid';
import { dateFormat } from '../../assets/js/common.js'

//---------------데이터-------------- 

let gridInstance = ref();
let rowData = ref([]);

onBeforeMount(() => {
   loginLogGetList();
});

// Toast UI Grid 초기화
onMounted(() => {
   gridInstance.value = new Grid({
      el: document.getElementById('logGrid'),
      data: rowData.value,
      scrollX: false,
      scrollY: true,
      columns: [
         { header: '번호', name: 'rowNum', align: 'center', width: 100 },
         { header: '로그인 아이디', name: 'logId', align: 'center' },
         { header: '로그인 아이피', name: 'logIp', align: 'center' },
         { header: '성공여부', name: 'state', align: 'center' },
         { header: '로그인 일시', name: 'createDt', align: 'center', sortable: true }
      ]
   })
})

//---------------axios--------------

const logList = ref([]);
const loginLogGetList = async () => { //로그인 로그 전체조회
   try {
      const result = await axios.get('/api/comm/loginLog');
      logList.value = result.data;

      rowData.value = [...logList.value];

      logList.value = result.data.map((item, index) => ({
         ...item,
         rowNum: index + 1,
         createDt: dateFormat(item.createDt) // 날짜 포맷 변환 적용
      }));

      if (gridInstance.value) {
         gridInstance.value.resetData(logList.value);
      }
   } catch (err) {
      logList.value = [];

      Swal.fire({
         icon: "error",
         title: "API 조회 오류",
         text: "Error : " + err
      });
   }
}

</script>
