<template>
   <div class="content">
      <div class="container-fluid">
         <div class="card">
            <div class="card-header d-flex justify-content-between align-items-center">
               <h4 class="card-title">부서 관리</h4>
               <div class="button-group">
                  <button class="btn btn-warning btn-fill mr-1">부서 이동</button>
                  <button class="btn btn-primary btn-fill mr-1">부서 등록</button>
               </div>
            </div>
         </div>

         <div class="card">
            <div class="card-body">
               <div class="m-group mx-auto w90">
                  <div class="table-line">
                     <div class="d-flex justify-content-between align-items-center p-2">
                        <div class="w10">
                           <p class="m-0">전체 <span class="cnt">10</span></p>
                        </div>
                        
                        <div class="d-flex justify-content-between align-items-center w50">
                           <select name="searchSel" id="searchSel" class="form-select w25">
                              <option value="name">이름</option>
                              <option value="auth">권한</option>
                           </select>
                           <input type="text" class="form-control w50" placeholder="검색어 입력"/>
                           <button class="btn btn-info btn-fill w20">검색</button>
                        </div>
                     </div>
                  </div>

                  <table class="table m-table">
                     <thead class="table-light">
                        <tr>
                           <th class="w5">
                              <div class="form-check">
                                 <input class="form-check-input" type="checkbox">
                              </div>
                           </th>
                           <th class="text-start w75">부서명</th>
                           <th class="w20">부서장</th>
                        </tr>
                     </thead>

                     <tbody>
                        <tr v-for="dept in organizations" :key="dept.id" :class="['depth_' + dept.level, { hidden: !isVisible(dept) }]">
                           <td>
                              <div class="form-check">
                                 <input class="form-check-input" type="checkbox">
                              </div>
                           </td>
                           <td :class="['text-start', 'depth_' + dept.level]">
                              <button class="toggle-btn" @click="toggleExpand(dept)">
                                 <i :class="dept.expanded ? 'fa-solid fa-chevron-down' : 'fa-solid fa-chevron-right'"></i>
                              </button>
                              {{ dept.name }}
                           </td>
                           <td class="profile-cell">{{ dept.manager || '-' }}</td>
                        </tr>
                     </tbody>
                  </table>

               </div>
               
            </div>
         </div>
      </div>
   </div>
</template>
   
<script setup>
   import { ref } from 'vue'

   // 조직 리스트 (부서 깊이 반영)
   const organizations = ref([
      { id: 1, name: '총무팀', manager: '김옹심', level: 1, expanded: true, parentId: null },
      { id: 2, name: '인사팀2', manager: '', level: 2, expanded: false, parentId: 1 },
      { id: 3, name: '인사팀3', manager: '', level: 3, expanded: false, parentId: 2 },
      { id: 4, name: '새 조직1', manager: '', level: 4, expanded: false, parentId: 3 },
      { id: 5, name: '새 조직2', manager: '', level: 5, expanded: false, parentId: 4 },
      { id: 6, name: 'ㅇㅇㅇ', manager: '', level: 3, expanded: false, parentId: 2 },
      { id: 7, name: 'ㅇㅇ', manager: '', level: 1, expanded: false, parentId: null },
      { id: 8, name: '인사팀', manager: '', level: 1, expanded: false, parentId: null },
      { id: 9, name: 'ㅁㄴㅇㄹ', manager: '', level: 1, expanded: false, parentId: null },
   ]);

   // 트리 확장/축소 기능 (하위 부서까지 반영)
   const toggleExpand = (dept) => {
      dept.expanded = !dept.expanded;
   };

   // 부모 부서가 열려 있는지 확인
   const isVisible = (dept) => {
      if (dept.parentId === null) return true; // 최상위 부서는 항상 보임
      const parent = organizations.value.find(org => org.id === dept.parentId);
      return parent && parent.expanded;
   };
</script>

<style>

   /* 부서명 들여쓰기 */
   tbody tr td:first-child {
      padding-left: 10px;
   }

   .depth_1 td:first-child { padding-left: 10px; }
   .depth_2 td:first-child { padding-left: 30px; }
   .depth_3 td:first-child { padding-left: 50px; }
   .depth_4 td:first-child { padding-left: 70px; }
   .depth_5 td:first-child { padding-left: 90px; }

   /* 트리 토글 버튼 스타일 */
   .toggle-btn {
      background: none;
      border: none;
      cursor: pointer;
      font-size: 14px;
      margin-right: 5px;
   }

   /* 숨김 처리 */
   .hidden {
      display: none;
   }
</style>