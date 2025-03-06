<template>
   <li>
      <i class="fa-solid fa-angle-down"></i> 개발팀 (137)
   </li>
   <li class="ms-3">
      <i class="fa-solid fa-angle-right" aria-hidden="true"></i> 개발 1팀(10)
   </li>
   <li class="ms-3">
      <i class="fa-solid fa-angle-right" aria-hidden="true"></i> 개발 2팀 (12)
   </li>
   <li class="ms-3 mb-1">
      <i class="fa-solid fa-angle-right" aria-hidden="true"></i> 개발 3팀 (15)
   </li>
   <li>
      <i class="fa-solid fa-angle-down" aria-hidden="true"></i> 디자인팀 (13)
   </li>
   <li class="ms-3">
      <i class="fa-solid fa-angle-right" aria-hidden="true"></i> 디자인 1팀 (5)
   </li>
   <li class="ms-3">
      <i class="fa-solid fa-angle-right" aria-hidden="true"></i> 디자인 2팀 (6)
   </li>
   <li class="ms-3 mb-1">
      <i class="fa-solid fa-angle-right" aria-hidden="true"></i> 디자인 3팀 (2)
   </li>
   <li class="mb-1">
      <i class="fa-solid fa-angle-right" aria-hidden="true"></i> 인사팀 (6)
   </li>
   <li>
      <i class="fa-solid fa-angle-right" aria-hidden="true"></i> 기획팀 (9)
   </li>
</template>

<script setup>
   import Tree from "vue3-tree";

   const data = ref([
      {
         "id": 1,
         "label": "Electronics",
         "nodes": [
            {
            "id": 2,
            "label": "Computers",
            "nodes": [
               {
                  "id": 4,
                  "label": "Laptops"
               },
               {
                  "id": 5,
                  "label": "Desktops"
               }
            ]
            },
            {
            "id": 3,
            "label": "Mobile Phones",
            "nodes": [
               {
                  "id": 6,
                  "label": "Smartphones",
                  "nodes": [
                  {
                     "id": 8,
                     "label": "Android"
                  },
                  {
                     "id": 9,
                     "label": "iOS"
                  }
                  ]
               },
               {
                  "id": 7,
                  "label": "Feature Phones"
               }
            ]
            }
         ]
      },
      {
         "id": 10,
         "label": "Books"
      }
   ]);
</script>

<style lang="scss" scoped>

</style>