<template>
   <div class="content">
      <div class="container-fluid">
         <div class="card">
            <div class="card-header d-flex justify-content-between align-items-center">
               <h4 class="card-title">부서 관리</h4>
               <div class="button-group">
                  <button class="btn btn-secondary btn-sm btn-fill mr-1">부서 이동</button>
                  <button class="btn btn-primary btn-sm btn-fill mr-1">부서 등록</button>
               </div>
            </div>
         </div>

         <div class="card">
            <div class="card-body">
               <!-- 반응형 트리 테이블 -->
               <div class="table-wrapper">
                  <table class="custom-table">
                     <thead>
                        <tr>
                        <th style="width: 5%;">
                           <input type="checkbox" class="checkbox" id="check_all">
                        </th>
                        <th style="width: 50%;">조직명</th>
                        <th style="width: 25%;">조직장</th>
                        <th style="width: 20%;">사용 용량</th>
                        </tr>
                     </thead>
                     <tbody>
                        <tr v-for="org in organizations" :key="org.id" :style="{ paddingLeft: `${org.level * 20}px` }">
                        <td>
                           <input type="checkbox" class="checkbox">
                        </td>
                        <td class="group-name">
                           <button @click="toggleExpand(org)" class="toggle-btn">
                              {{ org.expanded ? '▼' : '▶' }}
                           </button>
                           <span>{{ org.name }} ({{ org.count }})</span>
                        </td>
                        <td class="center">{{ org.manager || '-' }}</td>
                        <td class="center">{{ org.storage }}GB</td>
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

const organizations = ref([
  { id: 1, name: '총무팀', count: 3, manager: '김선용', storage: 0.00 },
  { id: 2, name: 'ㅇㅇ', count: 0, manager: '', storage: 0.00 },
  { id: 3, name: '인사팀', count: 1, manager: '', storage: 0.00 },
  { id: 4, name: 'ㅁㄴㅇㄹ', count: 0, manager: '', storage: 0.00 }
])
</script>

<style scoped>
   /* 테이블 스타일 */
.overflow-x-auto {
  max-width: 100%;
  overflow-x: auto;
  background: white;
  padding: 15px;
  border-radius: 10px;
  box-shadow: 0 2px 5px rgba(0, 0, 0, 0.1);
}

table {
  width: 100%;
  border-collapse: collapse;
}

th, td {
  padding: 14px;
  border-bottom: 1px solid #ddd;
  text-align: left;
}

th {
  background: #f8f9fa;
  font-weight: bold;
}

td {
  font-size: 14px;
}

/* 정렬 스타일 */
.leader, .storage {
  text-align: center;
}

/* 체크박스 */
.lw_checkbox {
  cursor: pointer;
}

/* 조직명 스타일 */
.group_name {
  display: flex;
  align-items: center;
  font-size: 16px;
  font-weight: 500;
}

.btn_toggle_tree {
  background: none;
  border: none;
  cursor: pointer;
  font-size: 18px;
  margin-right: 8px;
}

/* 반응형 - 모바일에서 테이블을 줄이고 가독성 확보 */
@media (max-width: 768px) {
  .container-fluid {
    padding: 15px;
  }

  .card-header {
    flex-direction: column;
    align-items: flex-start;
  }

  .button-group {
    flex-wrap: wrap;
    justify-content: flex-start;
  }

  .leader, .storage {
    display: none;
  }
}
</style>