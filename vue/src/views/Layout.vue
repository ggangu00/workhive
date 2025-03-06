<template>
   <div class="container">
      <table class="table">
         <tbody>
            <VueDraggableNext v-if="projectList.length > 0" :list="projectList" tag="tbody" :animation="300">
               <tr class="text-center align-middle" v-for="Flow in projectList" :key="Flow.prCd">
                  <td>
                     <div class="form-check col-4 col-md-2">{{ Flow.prCd }} </div>
                  </td>
               </tr>
            </VueDraggableNext>
            <tr v-else>
               <td colspan="1" class="text-center">데이터가 없습니다.</td>
            </tr>
         </tbody>
      </table>
   </div>
</template>

<script setup>
import { ref, onBeforeMount, defineComponent } from "vue";
import { VueDraggableNext } from 'vue-draggable-next'
import axios from "../assets/js/customAxios.js";

// ✅ draggable을 setup 방식에서 수동으로 등록
defineComponent({
   components: { VueDraggableNext }
});

const projectList = ref([]);

// ✅ 비동기 API 호출
const projectGetList = async () => {
   try {
      const result = await axios.get('/api/project/tree');
      projectList.value = result.data;
      console.log("✅ 프로젝트 리스트 로드 완료:", projectList.value);
   } catch (error) {
      console.error("❌ 프로젝트 리스트 불러오기 실패:", error);
   }
};

// ✅ onBeforeMount 안에서 실행되도록 정의된 함수 위치 수정
onBeforeMount(() => {
   projectGetList();
});
</script>

<style scoped>
.container {
   padding: 20px;
}
.table {
   width: 100%;
   border: 1px solid #ddd;
}
.text-center {
   text-align: center;
}
</style>
