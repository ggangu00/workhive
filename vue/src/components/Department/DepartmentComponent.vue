<template>
   <Tree :value="props.departmentTree" class="custom-tree">
      <template #togglericon="{ expanded }">
         <i :class="expanded ? 'fa-solid fa-angle-down' : 'fa-solid fa-angle-right'"></i>
      </template>

      <template #default="slotProps">

         <div class="node-buttons d-flex justify-content-between align-items-center">
            <span class="p-treenode-label">{{ slotProps.node.label }}</span>
            <button class="btn-toggle" @click.stop="btnDepartmentAdd(slotProps.node)">
               <i class="fa-solid fa-ellipsis-vertical"></i>
            </button>
         </div>
      </template>

   </Tree>
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

<style lang="scss">

   /* 트리 전체 */
   .custom-tree {
      padding: 0;
      border: none;
   }

   .custom-tree .p-tree-container {
      padding-left: 0px;
   }

   /* 각 노드 */
   .custom-tree .p-treenode {
      margin: 4px 0;
      padding: 0;
   }

   /* 노드 내용 (아이콘 + 텍스트) */
   .custom-tree .p-treenode-content {
      display: flex;
      align-items: center;
      height: 30px;
      border-radius: 3px;
      transition: background-color 0.2s;
   }

   /* 펼침/닫힘 아이콘 */
   .custom-tree .p-tree-toggler {
      font-size: 12px;
      color: #747474;
   }

   /* 노드 텍스트 */
   .custom-tree .p-treenode-label {
      font-size: 14px;
      color: #747474;
      font-weight: 600;
   }

   /* 자식 없는 노드의 토글버튼 강제 숨김 */
   .custom-tree .p-treenode-leaf .p-tree-toggler {
      display: none !important;
   }

   .btn-toggle {
      all: unset;
   }

   .p-treenode-label {
      width: 100%;
      font-size: 10px;
   }
</style>
