<template>
   <!-- <Tree
      :data=props.treeData
      :draggable="false"
      @nodeClick="handleNodeClick"
   /> -->
   <div>

   </div>
</template>

<script setup>
   //import Tree from 'vue3-tree'
   import { onMounted } from 'vue'

   const props = defineProps({
      treeData: {
         type: Array,
         default: () => []  // 안전장치까지 추가 추천
      }
   });

   onMounted(() => {
      console.log("부모가 넘겨준 treeData =>", props.treeData)
   })

   // const handleNodeClick = (node) => {
   //    console.log('클릭한 노드:', node)
   // }
</script>
