<template>
   <div class="content">
      <div class="container-fluid">
         <div class="card">
         <div class="card-header d-flex justify-content-between align-items-center">
            <h4 class="card-title">개인정보 설정</h4>
            <div class="button-group">
               <button class="btn btn-secondary btn-sm btn-fill mr-1">초기화</button>
               <button class="btn btn-primary btn-sm btn-fill mr-1">저장</button>
            </div>
         </div>
         </div>

         <div class="card">
            <div class="card-body">
               <div class="row m-0">

                  <div class="col-3 treeview">
                     <div class="bottom-line p-2">
                        <button class="btn btn-success btn-sm"><i class="fa-solid fa-plus"></i> 권한 추가</button>
                     </div>
                     <div class="role-list p-2">
                        <div class="">
                           <div class="d-flex justify-content-between align-items-center bottom-line">
                              <span>최고 관리자</span>
                              <div class="options" @click="toggleOptions(role)">
                                 <i class="fa-solid fa-ellipsis-vertical"></i>
                              </div>
                              <div class="dropdown" v-if="selectedRole == role">
                                 <div @click="editRole(role)">권한 수정</div>
                                 <div @click="deleteRole(role)">권한 삭제</div>
                              </div>
                           </div>
                        </div>

                        <div v-for="role in roles" :key="role">
                           <div class="d-flex justify-content-between align-items-center">
                              <span class="role-item">{{ role }}</span>
                              <div class="options" @click="toggleOptions(role)">
                                 <i class="fa-solid fa-ellipsis-vertical"></i>
                              </div>
                              <div class="dropdown" v-if="selectedRole == role">
                                 <div @click="editRole(role)">권한 수정</div>
                                 <div @click="deleteRole(role)">권한 삭제</div>
                              </div>
                           </div>

                        </div>
                     </div>
                  </div>

                  <!-- 오른쪽 패널 -->
                  <div class="col-9 treeview">
                     <div class="bottom-line p-2">
                        <button class="btn btn-secondary btn-sm btn-fill">취소</button>
                        <button class="btn btn-success btn-sm btn-fill">저장</button>
                     </div>
                     <div v-for="(menu, index) in menus" :key="index" class="bottom-line p-2">
                        <div class="menu-title" @click="toggleMenu(index)">
                           <input type="checkbox" v-model="menu.selected" />
                           {{ menu.title }} ({{ selectedSubmenus(menu) }}/{{ menu.submenus.length }})
                           <span :class="{ 'rotated': menu.open }">▼</span>
                        </div>
                        <div v-if="menu.open" class="submenu">
                           <div v-for="(sub, i) in menu.submenus" :key="i" class="submenu-item">
                              <input type="checkbox" v-model="menu.submenus[i].selected" />
                              {{ sub.title }}
                           </div>
                        </div>
                     </div>

                  </div>

               </div>

            </div>
         </div>
      </div>
   </div>
</template>

<script setup>
   import { ref } from 'vue';

   const roles = ref(['최고 관리자', '부관리자', '운영 관리자']);
   const selectedRole = ref(null);

   const toggleOptions = (role) => {
      selectedRole.value = selectedRole.value === role ? null : role;
   };

   const editRole = (role) => {
      alert(`${role} 수정`);
   };

   const deleteRole = (role) => {
      roles.value = roles.value.filter(r => r !== role);
   };

   const menus = ref([
   { title: '대메뉴 1', open: false, selected: false, submenus: [
      { title: '소메뉴 1', selected: false },
      { title: '소메뉴 2', selected: false },
      { title: '소메뉴 3', selected: false },
      { title: '소메뉴 4', selected: false }
   ]},
   { title: '대메뉴 2', open: false, selected: false, submenus: [
      { title: '소메뉴 1', selected: false },
      { title: '소메뉴 2', selected: false },
      { title: '소메뉴 3', selected: false },
      { title: '소메뉴 4', selected: false }
   ]}
   ]);

      const toggleMenu = (index) => {
      menus.value[index].open = !menus.value[index].open;
      };

   const selectedSubmenus = (menu) => {
      return menu.submenus.filter(sub => sub.selected).length;
   };
</script>

<style scoped>




   .menu-item {
      border-bottom: 1px solid #ccc;
      padding: 10px;
   }

   .menu-title {
      font-weight: bold;
      cursor: pointer;
      display: flex;
      align-items: center;
      justify-content: space-between;
   }

   .rotated {
      transform: rotate(180deg);
   }

   .submenu {
      background: #f4f4f4;
   }

   .submenu-item {
      padding: 10px;
   }

</style>