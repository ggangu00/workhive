<template>
   <div class="content">
      <div class="container-fluid">
         <div class="card">
            <div class="card-header d-flex justify-content-between align-items-center">
               <h4 class="card-title">WorkHive 조직 관리</h4>
            </div>
         </div>
         
         <div class="row">
            <!-- 트리 뷰 (왼쪽) -->
            <div class="col-md-3 treeview">
               <ul>
                  <TreeNode v-for="node in organizationTree" :key="node.id" :node="node" @toggle="toggleTree" @menuToggle="toggleMenu" @action="handleAction" />
               </ul>
            </div>

            <!-- 구성원 테이블 (오른쪽) -->
            <div class="col-md-7">
               <div class="d-flex justify-content-between mb-2">
                  <div class="button-group">
                     <button class="btn btn-secondary btn-sm btn-fill mr-1">이동</button>
                     <button class="btn btn-primary btn-sm btn-fill mr-1">저장</button>
                     <button class="btn btn-danger btn-sm btn-fill mr-1">삭제</button>
                  </div>
                  <div>
                     <input type="text" class="form-control d-inline-block w-auto" placeholder="검색어 입력">
                     <button class="btn btn-secondary">검색</button>
                  </div>
               </div>
               <table class="table table-bordered">
                  <thead class="table-light">
                     <tr>
                        <th><input type="checkbox"></th>
                        <th>사번</th>
                        <th>이름</th>
                        <th>직급</th>
                        <th>접근 권한</th>
                        <th>메뉴 권한</th>
                     </tr>
                  </thead>
                  <tbody>
                     <tr v-for="member in members" :key="member.id">
                        <td><input type="checkbox"></td>
                        <td>{{ member.id }}</td>
                        <td>{{ member.name }}</td>
                        <td>{{ member.rank }}</td>
                        <td>
                           <select class="form-select">
                              <option>읽기</option>
                              <option>쓰기</option>
                           </select>
                        </td>
                        <td>
                           <select class="form-select">
                              <option>전체</option>
                              <option>제한</option>
                           </select>
                        </td>
                     </tr>
                  </tbody>
               </table>
            </div>
         </div>
            
         
      </div>
   </div>
</template>

<script setup>
   import { ref } from 'vue';
   import TreeNode from '../../components/TreeView/TreeNode.vue';

   const organizationTree = ref([
      { id: 'workhive', name: 'WorkHive', count: 50, children: [
         { id: 'devteam', name: '개발팀', count: 37, children: [
            { id: 'dev1', name: '개발 1팀', count: 10, children: [] },
            { id: 'dev2', name: '개발 2팀', count: 12, children: [] },
            { id: 'dev3', name: '개발 3팀', count: 15, children: [] }
         ]},
         { id: 'designteam', name: '디자인팀', count: 13, children: [
            { id: 'design1', name: '디자인 1팀', count: 5, children: [] },
            { id: 'design2', name: '디자인 2팀', count: 6, children: [] },
            { id: 'design3', name: '디자인 3팀', count: 2, children: [] }
         ]}
      ]}
   ]);

   const toggleTree = (node) => {
      node.expanded = !node.expanded;
   };

   const toggleMenu = (node) => {
      node.showMenu = !node.showMenu;
   };

   const handleAction = (action, node) => {
      if (action === 'edit') {
         console.log('조직 수정:', node.name);
      } else if (action === 'add') {
         console.log('하위 조직 추가:', node.name);
      } else if (action === 'delete') {
         console.log('조직 삭제:', node.name);
      }
   };

   // const expandedTree = ref([]);
   // const openMenu = ref(null);

   const members = ref([
      { id: '2025001', name: '김민진', rank: '부장' },
      { id: '2025002', name: '박주현', rank: '부장' }
   ]);
</script>

<style scoped>
   .treeview {
      border-right: 2px solid #dee2e6;
      padding-right: 15px;
      min-height: 600px;
   }
   .treeview ul {
      list-style: none;
      padding-left: 0;
   }
   .treeview .nested {
      display: none;
      padding-left: 20px;
   }
   .treeview .nested.expanded {
      display: block;
   }
   .menu-container {
      display: flex;
      justify-content: space-between;
      align-items: center;
      width: 100%;
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
</style>
