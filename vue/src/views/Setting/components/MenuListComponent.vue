<template>
   <div :class="menu.parentMenuCd == null ? 'bottom-line' : ''">
      <div class="menu-title" @click="toggleMenu">
         <div>
            <div class="form-check form-check-inline">
               <input
                  v-if="isEditing"
                  class="form-check-input"
                  type="checkbox"
                  v-model="menu.selected"
                  @change="checkBoxSelectAll(menu)"
               />
               <span class="ms-2 font-15">{{ menu.menuNm }}</span>
               <span class="ms-2 font-14">({{ subMenusSelected(menu) }}/{{ menu.subMenus.length }})</span>
            </div>
         </div>

         <div v-if="menu.subMenus.length > 0">
            <i class="fa-solid fa-angle-down" :class="{ rotated: menu.open }"></i>
         </div>
      </div>

      <div v-if="menu.open && menu.subMenus.length > 0" class="submenu px-4" >
         <MenuListComponent v-for="(subItem, idx) in menu.subMenus" :key="idx" :item="subItem" />
         <!-- <div v-for="(sub, i) in menu.subMenus" :key="i" class="px-4 py-2">
            <div class="form-check form-check-inline">
               <input
                  class="form-check-input sub-check"
                  type="checkbox"
                  v-model="sub.selected"
               />
               <span class="ms-2 font-13">{{ sub.menuNm }}</span>
            </div>
         </div> -->
      </div>
   </div>
</template>

<script setup>
   import MenuListComponent from './MenuListComponent.vue'
   // 부모에서 전달한 prop 이름을 item으로 변경합니다.
   const props = defineProps({
      item: { type: Object, required: true },
      isMenuEditing: { type: Boolean },
   });

   const menu = props.item;
   const isEditing = props.isMenuEditing;
   /**
    * 헤더 체크박스 변경 시 서브 메뉴 전체 선택
    * @param {Object} menu - 선택된 메뉴 객체
    */
   const checkBoxSelectAll = (menu) => {
      menu.subMenus.forEach((sub) => {
         sub.selected = menu.selected;
      });
   };

   /**
    * 선택된 서브 메뉴 개수 계산
    * @param {Object} menu - 메뉴 객체
    * @returns {Number} 선택된 서브 메뉴의 개수
    */
   const subMenusSelected = (menu) => {
      return menu.subMenus.filter((sub) => sub.selected).length;
   };

   /**
    * 메뉴 토글 (열기/닫기)
    */
   const toggleMenu = () => {
      console.log("click")
      menu.open = !menu.open;
   };
</script>

<style lang="scss" scoped>

</style>
