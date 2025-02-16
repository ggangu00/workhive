<template>
   <div class="content">
      <div class="container-fluid">
         <div class="card">
            <div class="card-header d-flex justify-content-between align-items-center">
               <h4 class="card-title">권한 관리</h4>
            </div>
         </div>

         <div class="card">
            <div class="card-body">
               <div class="row m-0">

                  <div class="col-3 treeview">
                     <div class="bottom-line p-2">
                        <button class="btn btn-success btn-sm"><i class="fa-solid fa-plus"></i> 권한 추가</button>
                     </div>
                     <div class="p-2">
                        <div v-for="role in roles" :key="role">
                           <div class="d-flex justify-content-between align-items-center">
                              <span class="role-item">{{ role }}</span>
                              <div class="options" @click.stop="toggleOptions(role)">
                                 <i class="fa-solid fa-ellipsis-vertical"></i>
                              </div>
                           </div>

                           <!-- 드롭다운 메뉴 -->
                           <div v-show="selectedRole === role" class="editing-dropdown-menu">
                              <div class="font-14 p-2" @click="editRole(role)">권한 수정</div>
                              <div class="font-14 p-2" @click="deleteRole(role)">권한 삭제</div>
                           </div>
                        </div>
                     </div>
                  </div>

                  <!-- 오른쪽 패널 -->
                  <div class="col-9 treeview">
                     <div class="d-flex justify-content-between align-items-center bottom-line p-2">
                        <span>권한명</span>
                        <div class="">
                           <button class="btn btn-secondary btn-sm btn-fill">취소</button>
                           <button class="btn btn-success btn-sm btn-fill">저장</button>
                        </div>
                     </div>

                     <div v-for="(menu, index) in menus" :key="index" class="bottom-line">
                        <div class="menu-title">
                           <div class="">
                              <input class="me-2" type="checkbox" v-model="menu.selected" />
                              <span class="me-2 font-15">{{ menu.title }}</span>
                              <span class="font-14">({{ selectedSubmenus(menu) }}/{{ menu.submenus.length }})</span>
                           </div>
                           <di @click="toggleMenu(index)">
                              <i class="fa-solid fa-angle-down" :class="{ 'rotated': menu.open }"></i>
                           </di>
                        </div>
                        <div v-if="menu.open" class="submenu px-4 py-2">
                           <div v-for="(sub, i) in menu.submenus" :key="i" class="px-3 py-2">
                              <input class="me-2" type="checkbox" v-model="menu.submenus[i].selected" />
                              <span class="font-13">{{ sub.title }}</span>
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
   import { ref, onMounted } from 'vue';

   onMounted(() => {
      document.addEventListener("click", handleClickOutside);
   });

   const roles = ref(['최고 관리자', '부관리자', '운영 관리자']);
   const selectedRole = ref(null);

   const editRole = (role) => {
      alert(`${role} 수정`);
   };

   const deleteRole = (role) => {
      roles.value = roles.value.filter(r => r !== role);
   };

   const menus = ref([
      {
         title: '대메뉴 1',
         open: false,
         selected: false,
         submenus: [
            { title: '소메뉴 1', selected: false },
            { title: '소메뉴 2', selected: false },
            { title: '소메뉴 3', selected: false },
            { title: '소메뉴 4', selected: false }
         ]
      },
      {
         title: '대메뉴 2',
         open: false,
         selected: false,
         submenus: [
            { title: '소메뉴 1', selected: false },
            { title: '소메뉴 2', selected: false },
            { title: '소메뉴 3', selected: false },
            { title: '소메뉴 4', selected: false }
         ]
      },
   ]);

   const toggleMenu = (index) => {
      menus.value[index].open = !menus.value[index].open;
   };

   const selectedSubmenus = (menu) => {
      return menu.submenus.filter(sub => sub.selected).length;
   };

   const toggleOptions = (role) => {
      selectedRole.value = selectedRole.value === role ? null : role;
   };

   // 바깥 클릭 시 dropdown 닫기
   const handleClickOutside = (event) => {
      if (!event.target.closest(".editing-dropdown-menu") && !event.target.closest(".options")) {
         selectedRole.value = null;
      }
   };


</script>