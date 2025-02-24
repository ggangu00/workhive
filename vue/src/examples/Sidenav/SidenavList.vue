<template>
   <div class="w-auto h-auto collapse navbar-collapse max-height-vh-100 h-100" id="sidenav-collapse-main">
      <ul class="navbar-nav">
         <li class="nav-item" v-for="(item, i) in menus" :key="i">
            <sidenav-collapse  :aria-controls="''" :collapse="false" :collapseRef="item.url"
               :navText="item.menuNm">
               <template v-slot:icon>
                  <i :class="item.iconClass"></i>
               </template>

               <!-- 서브 메뉴 (Depth 2) -->
               <template v-if="item.subMenus.length" v-slot:list>
                  <li class="sub-item" v-for="(sub, j) in item.subMenus" :key="j" @click="movePage(sub.routerNm)">
                     {{ sub.menuNm }}
                     <i class="fa-solid fa-chevron-down arrow-icon" v-if="sub.subSubMenus.length > 0"></i>
                     <ul v-show="isHide" v-if="sub.subSubMenus.length > 0" class="menu-box">
                        <li class="sub-item" v-for="(child, k) in sub.subSubMenus" :key="k" @click="movePage(child.routerNm)">
                           {{ child.menuNm }}
                        </li>
                     </ul>
                  </li>
               </template>

            </sidenav-collapse>
         </li>
      </ul>
   </div>
</template>

<script setup>
   import { ref, onMounted } from "vue";
   import { useRouter } from "vue-router";
   import SidenavCollapse from "./SidenavCollapse.vue";
   import axios from "axios";
   import Swal from 'sweetalert2';

   const router = useRouter();

   // ============================================= Axios Event =============================================
   // id, pass값 서버로 보내기
   const menus = ref([]);
   const menuGetList = async () => {
      try {
         const response = await axios.get('/api/menu');

         menuBuildTree(response.data);
      } catch (err) {
         Swal.fire({
            icon: "error",
            title: "API 조회 오류",
            text: "Error : " + err
         });
      }
   }

   // 메뉴 구조 생성
   const menuBuildTree = (menuArr) => {
      let mainMenus = new Map();
      let subMenus = new Map();

      menuArr.forEach(menu => {
         if (menu.depth === 1) {
            mainMenus.set(menu.menuCd, {
               url: menu.url,
               menuNm: menu.menuNm,
               iconClass: menu.iconClass || '',
               subMenus: [],
            });
         }
      });

      menuArr.forEach(menu => {
         if (menu.depth === 2 && menu.parentMenuCd) {
            const parentMenu = mainMenus.get(menu.parentMenuCd); // mainMenus의 key값으로 value를 가져옴

            if (parentMenu) {
               // parentMenu의 subMenus에
               parentMenu.subMenus.push({
                  url: menu.url,
                  menuNm: menu.menuNm,
                  iconClass: menu.iconClass || '',
                  routerNm: menu.routerNm,
                  subSubMenus: [],
               });
               subMenus.set(menu.menuCd, parentMenu.subMenus[parentMenu.subMenus.length - 1]); // 저장
            }
         }
      });

      // 손자 메뉴
      menuArr.forEach((menu) => {
         if (menu.depth === 3 && menu.parentMenuCd) {
            const parentSubMenu = subMenus.get(menu.parentMenuCd);
            if (parentSubMenu) {
               parentSubMenu.subSubMenus.push({
                  url: menu.url,
                  menuNm: menu.menuNm,
                  iconClass: menu.iconClass || "",
                  routerNm: menu.routerNm,
               });
            }
         }
      });
      console.log("ddd => ", Array.from(mainMenus, ([key, value]) => ({
         menuCd: key,
         ...value,
      })))

      menus.value = Array.from(mainMenus, ([key, value]) => ({
         menuCd: key,
         ...value,
      }));
   }

   const isHide = ref(true)

   const movePage = (page) => {
      console.log("page => ", page)
      if (!page) {
         isHide.value = !isHide.value
         return;
      }
      if (router.hasRoute(page)) {
         router.push({ name: page });
      } else {
         console.warn("라우트가 존재하지 않습니다:", page);
         router.push(page);
      }

   };

   onMounted(() => {
      menuGetList(); // 메뉴 목록 가져오기
   });

</script>


<style scoped>
.sub-item {
   color: #cfcfcf;
   font-size: 13px;
   padding: 10px 0px;
}

.menu-box {
   padding-left: 1rem;
   margin-top: 8px;
}

.arrow-icon{
   padding-right: 15px;
   float: right;
   font-size: 10px;
}

.sub2-item {
   color: #cfcfcf;
   font-size: 13px;
   padding: none;
   list-style: none;
   margin: 6px 0px 0px 0px;
}

li {
   cursor: pointer;
}
</style>
