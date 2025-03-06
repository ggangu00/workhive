<template>
   <Tree :value="props.departmentTree" class="department-tree" />
</template>


<script setup>
import { watch, onMounted, defineProps } from 'vue';
import Tree from 'primevue/tree'

const props = defineProps({
   departmentTree: {
      type: Array,
      default: () => []
   }
})

onMounted(() => {
   console.log("부모가 넘겨준 departmentTree:", props.departmentTree)
})

watch(() => props.departmentTree, (newVal) => {
   console.log("departmentTree 변경 감지:", newVal)
})
</script>

<style scoped>

</style>
