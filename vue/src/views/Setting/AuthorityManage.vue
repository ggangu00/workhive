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
                        <button class="btn btn-success btn-sm" @click="btnAuthorityAdd"><i class="fa-solid fa-plus"></i> 권한 추가</button>
                     </div>
                     <div class="p-2">
                        <div v-for="(role, idx) in roles" :key="idx">
                           <div class="d-flex justify-content-between align-items-center">
                              <span class="role-item">{{ role.authorityNm }}</span>
                              <div class="options pe-2" @click.stop="optionsToggle(idx, role.authorityCd)">
                                 <i class="fa-solid fa-ellipsis-vertical font-13"></i>
                              </div>
                           </div>

                           <!-- 드롭다운 메뉴 -->
                           <div v-show="selectedRole === idx" class="editing-dropdown-menu">
                              <div class="font-14 p-2" @click="roleEdit(role.authorityCd)">권한 수정</div>
                              <div class="font-14 p-2" @click="roleDelete(role.authorityCd)">권한 삭제</div>
                           </div>
                        </div>
                     </div>
                  </div>

                  <!-- 오른쪽 패널 -->
                  <div class="col-9 treeview">
                     <div class="d-flex justify-content-between align-items-center bottom-line p-2">
                        <span>권한명</span>
                        <div class="button-group">
                           <button class="btn btn-secondary btn-sm btn-fill">취소</button>
                           <button class="btn btn-success btn-sm btn-fill">저장</button>
                        </div>
                     </div>

                     <div v-for="(menu, index) in menus" :key="index" class="bottom-line">
                        <div class="menu-title">
                           <div class="">
                              <div class="form-check form-check-inline">
                                 <input class="form-check-input" type="checkbox" v-model="menu.selected" />
                              </div>
                              <span class="me-2 font-15">{{ menu.title }}</span>
                              <span class="font-14">({{ submenusSelected(menu) }}/{{ menu.submenus.length }})</span>
                           </div>
                           <di @click="toggleMenu(index)">
                              <i class="fa-solid fa-angle-down" :class="{ 'rotated': menu.open }"></i>
                           </di>
                        </div>
                        <div v-if="menu.open" class="submenu px-4 py-2">
                           <div v-for="(sub, i) in menu.submenus" :key="i" class="px-4 py-2">
                              <div class="form-check form-check-inline">
                                 <input class="form-check-input" type="checkbox" v-model="menu.submenus[i].selected" />
                              </div>
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
   import { ref, onMounted, onBeforeMount } from 'vue';
   import axios from "axios";
   import Swal from 'sweetalert2';

   onBeforeMount(() => {
      // 권한 목록 조회
      authorityGetList();
   });

   onMounted(() => {
      document.addEventListener("click", outsideClickHandler);
   });

   const roles = ref([]);
   const selectedRole = ref(null);

   // ======================================== Axios 서버통신 ========================================
   const authorityGetList = async () => {
      try {
         const result = await axios.get('/api/authority/list');
         roles.value = result.data;
      } catch (err) {
         roles.value = [];

         Swal.fire({
            icon: "error",
            title: "API 조회 오류",
            text:  "Error : " + err
         });
      }
   };

   const btnAuthorityAdd = async () => {
      console.log("add Ready !");

      try {
         const result = await axios.get('/api/authority/list');
         roles.value = result.data;
      } catch (err) {
         roles.value = [];
         
         Swal.fire({
            icon: "error",
            title: "API 조회 오류",
            text:  "Error : " + err
         });
      }
   }

   const roleEdit = (role) => {
      console.log("수정 => ", role)
   };

   const roleDelete = (role) => {
      console.log("삭제 => ", role)
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

   const submenusSelected = (menu) => {
      return menu.submenus.filter(sub => sub.selected).length;
   };

   const optionsToggle = (role) => {
      console.log(role)
      selectedRole.value = selectedRole.value === role ? null : role;
   };

   // 바깥 클릭 시 dropdown 닫기
   const outsideClickHandler = (event) => {
      if (!event.target.closest(".editing-dropdown-menu") && !event.target.closest(".options")) {
         selectedRole.value = null;
      }
   };


</script>