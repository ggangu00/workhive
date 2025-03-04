<template>
   <div class="w-auto h-auto collapse navbar-collapse max-height-vh-100 h-100" id="sidenav-collapse-main">
      <ul class="navbar-nav">
         <li class="nav-item">
            <sidenav-collapse url="#" :aria-controls="''" v-bind:collapse="false" collapseRef="/guide" navText="레이아웃">
               <template v-slot:icon>
                  <i class="material-icons-round opacity-10 fs-5">dashboard</i>
               </template>
            </sidenav-collapse>
         </li>

         <li class="nav-item">
            <sidenav-collapse url="#" :aria-controls="''" v-bind:collapse="false" collapseRef="/layout" navText="레이아웃틀">
               <template v-slot:icon>
                  <i class="material-icons-round opacity-10 fs-5">dashboard</i>
               </template>
            </sidenav-collapse>
         </li>

         <li class="nav-item" v-for="(item, i) in menus" :key="i">
            <sidenav-collapse
               :aria-controls="''"
               :collapse="false"
               :collapseRef="item.url"
               :navText="item.menuNm"
               @click="item.subMenus.length > 0 ? toggleSubMenu(item) : movePage(item.url, item.menuCd)"
            >
               <template v-slot:icon>
                  <i :class="item.iconClass"></i>
               </template>

               <!-- 서브 메뉴 (Depth 2) -->
               <template v-if="item.subMenus.length" v-slot:list>
                  <div class="sub-item" v-for="(sub, j) in item.subMenus" :key="j"
                     @click="sub.subSubMenus.length > 0 ? toggleSubMenu(sub) : movePage(sub.url, sub.menuCd)">

                     <li class="sub-li">
                        {{ sub.menuNm }}
                        <i v-if="sub.subSubMenus.length > 0"
                           :class="['fa-solid', 'arrow-icon', sub.isHidden ? 'fa-chevron-up' : 'fa-chevron-down']"></i>
                     </li>

                     <div v-for="(child, k) in sub.subSubMenus" class="sub2-item" :key="k" v-show="sub.isHidden"
                        :id="'menuDropdown' + sub.menuCd">
                        <template v-if="sub.subSubMenus.length > 0">
                           <li @click.stop="childMovePage(child.url, child.menuCd)">
                              {{ child.menuNm }}
                           </li>
                        </template>
                     </div>
                  </div>
               </template>

            </sidenav-collapse>
         </li>

         <!--게시글 목록 작업하는 곳[S]-->
         <li class="nav-item">
            <sidenav-collapse
            url="#"
            :aria-controls="''"
            v-bind:collapse="false"
            collapseRef="/bulletin"
            navText="게시글 관리"
            >
               <template v-slot:icon>
                  <i class="fa-solid fa-pen-to-square"></i>
               </template>

               <!-- 서브 메뉴 (Depth 2) -->
               <template v-slot:list>
                  <div class="sub-item">
                     <li class="sub-li" @click="movePage('/bulletin/bulletinList/BBS001')">공지사항</li>
                     <li class="sub-li" @click="movePage('/bulletin/bulletinList/BBS001')">사내게시판</li>
                     <li class="sub-li" @click="movePage('/bulletin/secretAt')">비밀글</li>

                  </div>
               </template>
            </sidenav-collapse>
         </li>
         <!--게시판 목록 작업하는 곳[E]-->
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
   const menus = ref([]);
   const menuGetList = async () => {
      try {
         const response = await axios.get('/api/menu');

         menuBuildTree(response.data);
      } catch (err) {
         Swal.fire({
            icon: "error",
            title: "API 조회 오류",
            text: "Error : " + err.response.data.error
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
               // parentMenu의 subMenus에 배열 넣기
               parentMenu.subMenus.push({
                  menuCd: menu.menuCd,
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
                  menuCd: menu.menuCd,
                  url: menu.url,
                  menuNm: menu.menuNm,
                  iconClass: menu.iconClass || "",
                  routerNm: menu.routerNm,
               });
            }
         }
      });

      menus.value = Array.from(mainMenus, ([key, value]) => ({
         menuCd: key,
         ...value,
      }));
   }

   const movePage = (page, menuCd) => {
      if (!page) {
         isHidden.value = !isHidden.value;
         return;
      }
      router.push({
         path: page,
         query: { menuCd }
      });
   };

   const isHidden = ref(false);
   const toggleSubMenu = (subMenu) => {
      subMenu.isHidden = !subMenu.isHidden;
   };

   const childMovePage = (page, menuCd) => {
      if (!page) {
         isHidden.value = !isHidden.value;
         return;
      }
      if (router.hasRoute(page)) {
         router.push({
            path: page,
            query: { menuCd }
         });
      } else {
         console.warn("라우트가 존재하지 않습니다:", page);
         router.push({
            path: page,
            query: { menuCd }
         });
      }
   };

   onMounted(() => {
      menuGetList(); // 메뉴 목록 가져오기
   });

</script>


<style lang="scss" scoped>
.sub-item {
   color: #cfcfcf;
   font-size: 13px;
}

.sub-item>li {
   padding-left: 21%;
}

.sub-li {
   padding-top: 13px;
   padding-bottom: 13px;
   padding-left: 21%;
}

.menu-box {
   padding-left: 1rem;
   margin-top: 8px;
}

.arrow-icon {
   padding-right: 15px;
   padding-top: 5px;
   float: right;
   font-size: 10px;
}

.sub2-item {
   background-color: #333333;
   color: #cfcfcf;
   font-size: 13px;
   padding: none;
   list-style: none;
   list-style: none;

   >li {
      padding-top: 13px;
      padding-bottom: 13px;
      padding-left: 21%;
   }
}

li {
   cursor: pointer;
}
</style>
