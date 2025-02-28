<template>
   <div v-for="(category, index) in permissions" :key="index" class="top-line" >
      <div class="m-4 d-flex">
         <div class="col-4">
            <span class="font-14">{{ category.name }} ({{ category.items.length }}/{{ category.items.length }})</span>
         </div>
         <div class="col-8">
            <ul class="m-0">
               <li v-for="(item, idx) in category.items" :key="idx" class="font-13 py-1">
                  - {{ item }}
               </li>
            </ul>
         </div>
      </div>
   </div>
</template>

<script setup>
import { ref } from "vue";

// 권한 데이터 설정
const permissions = ref([
   {
      name: "기본환경",
      items: ["회사 정보", "맞춤 설정", "도메인"],
   },
   {
      name: "보안",
      items: ["계정보안", "조직도 조회 제한", "서비스 권한"],
   },
   {
      name: "통계",
      items: ["계정"],
   },
   {
      name: "감사",
      items: [
         "Admin",
         "주소록",
         "Developer Console",
         "로그인",
         "파일",
         "모바일 화면 캡처(iOS)",
         "로그 다운로드",
      ],
   },
   {
      name: "구매 및 청구",
      items: ["이용 현황", "상품 신청 및 변경"],
   },
]);
</script>

<style lang="scss" scoped>

</style>
