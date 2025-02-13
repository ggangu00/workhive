<template>
   <li>
      <div class="menu-container">
         <span @click="toggleNode">{{ node.expanded ? '▼' : '▶' }} {{ node.name }} ({{ node.count }})</span>
         <button class="menu-button" @click.stop="toggleMenu">⋮</button>
      </div>
      <div v-if="node.showMenu" class="dropdown-menu">
         <button @click="$emit('action', 'edit', node)">조직 수정</button>
         <button @click="$emit('action', 'add', node)">하위 조직 추가</button>
         <button @click="$emit('action', 'delete', node)">조직 삭제</button>
      </div>
      <ul v-if="node.expanded" class="nested">
         <TreeNode v-for="child in node.children" :key="child.id" :node="child" @toggle="$emit('toggle', child)" @menuToggle="$emit('menuToggle', child)" @action="$emit('action', $event, child)" />
      </ul>
   </li>
 </template>
 
 <script setup>
 import { defineProps, defineEmits } from 'vue';
 
 const props = defineProps({
   node: Object
 });
 
 const emits = defineEmits(['toggle', 'menuToggle', 'action']);
 
 const toggleNode = () => {
   emits('toggle', props.node);
 };
 
 const toggleMenu = () => {
   emits('menuToggle', props.node);
 };
 </script>
 
 <style scoped>
 .menu-container {
   display: flex;
   justify-content: space-between;
   align-items: center;
   width: 100%;
   cursor: pointer;
 }
 .menu-button {
   border: none;
   background: none;
   font-size: 16px;
   cursor: pointer;
 }
 .dropdown-menu {
   position: absolute;
   background: white;
   box-shadow: 0px 0px 5px rgba(0, 0, 0, 0.2);
   border-radius: 5px;
   padding: 5px;
   z-index: 1000;
 }
 .dropdown-menu button {
   display: block;
   width: 100%;
   border: none;
   background: none;
   padding: 5px;
   text-align: left;
 }
 .nested {
   padding-left: 20px;
 }
 </style>
 