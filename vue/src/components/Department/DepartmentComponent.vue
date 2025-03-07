<template>
   <Tree :value="props.departmentTree" class="custom-tree" />
</template>

<script setup>
   import Tree from 'primevue/tree'
   import { defineProps } from 'vue'

   const props = defineProps({
      departmentTree: {
         type: Array,
         default: () => []
      }
   })
</script>

<style lang="scss" scoped>
   .p-tree {
      padding: 0px;
   }

   /* 트리 전체 꾸미기 */
   .custom-tree {
      border: none;
   }

   /* 각 노드 스타일 */
   .custom-tree .p-treenode {
      margin: 4px 0;
      padding: 2px 0;
   }

   /* 노드 내용 (아이콘 + 텍스트) */
   .custom-tree .p-treenode-content {
      display: flex;
      align-items: center;
      /* gap: 5px; */
      height: 36px;
      padding: 0 8px;
      border-radius: 3px;
      transition: background-color 0.2s;
   }
   ul {
      padding-left: 0rem !important;
   }

   .p-tree-wrapper {


   }

   /* 펼침/닫힘 아이콘 */
   .custom-tree .p-tree-toggler {
      font-size: 14px;
      color: #666;
   }

   /* 노드 텍스트 */
   .custom-tree .p-treenode-label {
      font-size: 14px;
      color: #333;
      font-weight: 500;
   }

   /* 자식 노드 컨테이너 */
   .custom-tree .p-treenode-children {
      margin-left: 16px;
      border-left: 2px solid #eee;
   }
</style>
