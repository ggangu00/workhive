<template>
   <div class="content">
      <div class="container-fluid">
         <div class="card">
            <div class="card-header d-flex justify-content-between align-items-center">
               <h4 class="card-title">권한 관리</h4>
            </div>
         </div>

         <div class="card" @keydown.esc="modalClose">
            <div class="card-body">
               <div class="row m-0">

                  <!-- 왼쪽 레이아웃 : 권한 목록 -->
                  <div class="col-3 treeview">
                     <div class="bottom-line p-2">
                        <button class="btn btn-primary btn-fill" @click="btnAuthorityAdd">
                           권한 <i class="fa-solid fa-plus"></i>
                        </button>
                     </div>

                     <div class="p-2">
                        <div v-for="(role, idx) in roles" :key="idx" @click="authorityGet(role, idx)" class="role-wrapper">
                           <div class="d-flex justify-content-between align-items-center"
                                 :class="{ 'selected-role': selectedRoleIdx === idx }">
                              <span class="role-item">{{ role.authorityNm }}</span>
                              <div class="options pe-2" @click.stop="optionsToggle(idx)">
                                 <i class="fa-solid fa-ellipsis-vertical font-13"></i>
                              </div>
                           </div>

                           <!-- 드롭다운 메뉴 -->
                           <div v-show="selectedRoleIdx === idx && isEditMenu" class="editing-dropdown-menu">
                              <div class="font-14 p-2" @click="btnAuthorityModify(role.authorityCd)">권한 수정</div>
                              <div class="font-14 p-2" @click="btnAuthorityRemove(role.authorityNm, role.authorityCd)">권한 삭제</div>
                           </div>
                        </div>
                     </div>
                  </div>

                  <!-- 오른쪽 레이아웃 : 메뉴목록 조회 -->
                  <div class="col-9 treeview">
                     <div class="d-flex justify-content-between align-items-center bottom-line p-2">
                        <span>{{ selectedRole ? selectedRole.authorityNm : '' }}</span>

                        <!-- 수정 버튼 (수정 모드 진입용) -->
                        <div v-if="!isMenuEditing" class="button-group">
                           <button class="btn btn-success btn-fill" @click="btnMenuModifyMode">수정</button>
                        </div>

                        <!-- 취소/저장 버튼 (수정 모드일 때) -->
                        <div v-else class="button-group">
                           <button class="btn btn-secondary btn-fill" @click="btnAuthorityMenuCancel">취소</button>
                           <button class="btn btn-success btn-fill" @click="btnAuthorityMenuSave">저장</button>
                        </div>
                     </div>

                     <div>
                        <MenuListListComponent
                           v-for="(item, idx) in menuData"
                           :key="idx"
                           :item="item"
                           :isMenuEditing="isMenuEditing"
                        />
                     </div>
                  </div>

               </div>

               <!-- [s]-->
               <Modal :isShowModal="isShowModal" :modalTitle="modalTitle" @click.self="modalClose">
                  <!-- 모달 바디 -->
                  <template v-slot:body>
                     <card class="mb-0">
                        <div class="mb-3" v-show="isEditMode">
                           <label class="form-label">권한코드 <i class="fa-solid fa-asterisk point-red"></i></label>
                           <input type="text" v-model="authorityCd" class="form-control w30" readonly disabled>
                        </div>
                        <div class="mb-3">
                           <label class="form-label">권한명 <i class="fa-solid fa-asterisk point-red"></i></label>
                           <input type="text" v-model="authorityNm" class="form-control w30">
                        </div>
                        <div class="mb-3">
                           <div class="form-group has-label">
                              <label>권한에 대한 설명</label>
                           </div>
                           <textarea type="text" v-model="description" class="form-control" placeholder="권한에 대한 설명을 입력하세요." style="height: 130px;"></textarea>
                        </div>
                     </card>
                  </template>

                  <!-- 모달 푸터 -->
                  <template v-slot:footer>
                     <button type="button" class="btn btn-secondary btn-fill" @click="btnModalClose">닫기</button>
                     <!-- 등록 모드 -->
                     <button v-if="!isEditMode" type="button" class="btn btn-primary btn-fill" @click="authorityAdd">등록</button>
                     <!-- 수정 모드 -->
                     <button v-else type="button" class="btn btn-success btn-fill" @click="authorityModify(authorityCd)">저장</button>
                  </template>
               </Modal>

            </div>
         </div>
      </div>
   </div>
</template>

<script setup>
   // 권한 및 메뉴 관리 화면 구성
   import { ref, onMounted, onBeforeMount, onBeforeUnmount } from 'vue';
   import Swal from 'sweetalert2';
   import Card from '../../components/Cards/Card.vue';
   import Modal from '../../components/Modal.vue';
   import MenuListListComponent from './components/MenuListComponent.vue';
   import axios from "../../assets/js/customAxios.js"; // 공통 Axios 설정 파일

// ================================================== 생명주기 함수 ==================================================
   // 컴포넌트가 마운트되기 전에 권한 및 메뉴 목록 조회 실행
   onBeforeMount(() => {
      authorityGetList();   // 권한 목록 조회
      menuGetList();        // 전체 메뉴 목록 조회
   });

   // 컴포넌트 마운트 시 이벤트 리스너 등록
   onMounted(() => {
      document.addEventListener("click", outsideClickHandler); // 외부 클릭 감지용 이벤트 등록
      document.addEventListener('keydown', modalClose);        // ESC 키 감지 이벤트 등록
   });

   // 컴포넌트 언마운트 시 이벤트 리스너 해제
   onBeforeUnmount(() => {
      document.removeEventListener('keydown', modalClose);     // ESC 이벤트 해제
   });

// ================================================== 모달 제어 관련 ==================================================
   // 모달 및 드롭다운 상태 관리 ref
   const isShowModal = ref(false);
   const isEditMenu = ref(false);
   let modalTitle = ref("");
   let isEditMode = ref(false);  // 모달이 등록인지, 수정인지 판별

   // 모달 열기
   const modalOpen = (mode, title) => {
      modalReset();            // 모달 필드 초기화
      isEditMode.value = mode;  // 등록(true)/수정(false) 모드 설정
      modalTitle.value = title; // 모달 제목 설정
      isShowModal.value = true; // 모달 열기
   };
   // 모달 닫기 (ESC 키 또는 직접 호출)
   const modalClose = (e) => {
      if (e.key === "Escape" || e === "remove") {
         isShowModal.value = false; // 모달 닫기
         isEditMenu.value = false;  // 드롭다운 닫기
      }
   };

   // 모달 입력 필드 초기화
   const modalReset = () => {
      authorityCd.value = '';    // 권한코드 초기화
      authorityNm.value = '';    // 권한명 초기화
      description.value = '';    // 설명 초기화
   };

// ================================================== 버튼 이벤트 ==================================================
   const btnAuthorityAdd = () => {
      modalOpen(false, "권한 등록");   // 등록 모드로 모달 오픈
   };

   // 권한 수정 버튼 클릭
   const btnAuthorityModify = (code) => {
      modalOpen(true, "권한 수정");    // 수정 모드로 모달 오픈
      authorityGet({ authorityCd: code });  // 해당 권한 단건 조회
   };

   // 권한 삭제 버튼 클릭
   const btnAuthorityRemove = (name, code) => {
      Swal.fire({
         title: `"${name}" 권한을 삭제하시겠습니까?`,
         icon: "question",
         showCancelButton: true,
         confirmButtonText: "닫기",
         cancelButtonText: "삭제",
         customClass: {
            confirmButton: "btn btn-secondary btn-fill",
            cancelButton: "btn btn-danger btn-fill"
         }
      }).then((result) => {
         if (result.dismiss === Swal.DismissReason.cancel) {
            authorityRemove(code);  // 삭제 요청
            Swal.fire({
               title: "삭제 완료",
               icon: "success"
            });
         }
      });
   };

   // 모달 닫기 버튼 클릭
   const btnModalClose = () => {
      modalClose("remove");      // 직접 모달 닫기 호출
   };

   // 메뉴 수정모드 여부
   const isMenuEditing = ref(false);

   // 수정 모드 진입
   const btnMenuModifyMode = () => {
      isMenuEditing.value = true;
      menuData.value = JSON.parse(JSON.stringify(fullMenuList.value)); // 전체 메뉴 복사
      applySelectedState(menuData.value); // 선택 상태 재적용
   };

   // 메뉴 수정모드 취소
   const btnAuthorityMenuCancel = () => {
      Swal.fire({
         icon: "question",
         //title: "수정 모드를 종료하시겠습니까?",
         text: "변경사항이 반영되지 않습니다. 그대로 진행하시겠습니까 ? ",
         showCancelButton: true,
         customClass: {
            confirmButton: "btn btn-secondary btn-fill",
            cancelButton: "btn btn-primary btn-fill"
         },
         confirmButtonText: "아니오", // "삭제" → "닫기"
         cancelButtonText: "예", // "닫기" → "삭제"
      }).then(result => {
         if (result.dismiss == Swal.DismissReason.cancel) {
            isMenuEditing.value = false;   // 수정모드 종료
            updateMenuDataByMode();        // 조회 모드로 데이터 다시 구성
         };
      });
   };

   const btnAuthorityMenuSave = () => {
      Swal.fire({
         title: `메뉴를 수정하시겠습니까?`,
         icon: "question",
         showCancelButton: true,
         reverseButtons: true,
            customClass: {
               cancelButton: "btn btn-secondary btn-fill",  // 아니오 버튼
               confirmButton: "btn btn-primary btn-fill",   // 예 버튼
            },
            confirmButtonText: "예",
            cancelButtonText: "아니오"
      }).then((result) => {
         console.log(authorityMenuModify);

         if (result.isConfirmed) {
            Swal.fire({
               title: "수정 완료",
               icon: "success"
            });
         }
      });
   }

   const getSelectedMenus = () => {
      const selectedMenus = [];      // 선택된 메뉴 리스트

      const collectSelected = (menus) => {

         menus.forEach(menu => {
            if (menu.selected) {
               selectedMenus.push({
                  menuCd: menu.menuCd,  // 필요한 데이터만 추려서 담아야 함 (예: 메뉴코드, 상위코드 등)
                  parentMenuCd: menu.parentMenuCd,
                  menuNm: menu.menuNm
               });
            }
            if (menu.subMenus && menu.subMenus.length > 0) {
               collectSelected(menu.subMenus);  // 서브메뉴까지 전부 탐색
            }
         });
      };

      collectSelected(fullMenuList.value);
      return selectedMenus;
   };


   const authorityMenuModify = async () => {
      const selectedMenu = getSelectedMenus();
      console.log("selectedMenu => ", selectedMenu)
      try {
         // const data = {
         //    authorityCd: selectedRole.value.authorityCd,   // 현재 선택된 권한코드
         //    menuList: selectedMenu                           // 선택된 메뉴 리스트
         // };

         const result = await axios.post(`/api/authority/authMenu`, {authorityCd: selectedRole.value.authorityCd,});
         console.log("result => ", result.data)
         //roles.value = result.data;

         // if (roles.value.length > 0) {
         //    // ✅ 첫 번째 권한 자동 선택
         //    authorityGet(roles.value[0], 0);
         // }

      } catch (err) {
         roles.value = [];
         Swal.fire({
            icon: "error",
            title: "API 조회 실패",
            text: `Error: ${err.response?.data?.error || err.message}`
         });
      }
   };

// ================================================== 권한 관리 API ==================================================
   const roles = ref([]);  // 권한 목록

   // 권한 전체 조회
   const authorityGetList = async () => {
      try {
         const result = await axios.get('/api/authority');
         roles.value = result.data;

         if (roles.value.length > 0) {
            authorityGet(roles.value[0], 0); // 첫 권한 자동 선택
         }

      } catch (err) {
         roles.value = [];
         Swal.fire({
            icon: "error",
            title: "API 조회 실패",
            text: `Error: ${err.response?.data?.error || err.message}`
         });
      }
   };

   // 선택된 권한 정보
   let authorityCd = ref('');
   let authorityNm = ref('');
   let description = ref('');
   const selectedRole = ref(null);   // 현재 선택된 권한 객체
   const selectedRoleIdx = ref(null); // 선택된 권한 인덱스

   // 권한 단건 조회
   const authorityGet = async (role, idx) => {
      if (isMenuEditing.value) {   // 수정 모드일 때만 알림창 띄움
         const result = await Swal.fire({
            icon: "question",
            text: `변경사항이 저장되지 않습니다. "${role.authorityNm}" 권한을 확인하시겠습니까?`,
            showCancelButton: true,
            reverseButtons: true,
            customClass: {
               confirmButton: "btn btn-primary btn-fill",   // 예 버튼
               cancelButton: "btn btn-secondary btn-fill"   // 아니오 버튼
            },
            confirmButtonText: "예",
            cancelButtonText: "아니오"
         });

         if (result.isDismissed || result.isDenied) {
            // '아니오' 선택 시 → 아무 것도 안 하고 함수 종료
            return;
         }

         // '예' 선택 시 → 수정모드 종료 + 데이터 재조회
         isMenuEditing.value = false;
         updateMenuDataByMode();
      }

      // 권한 데이터 조회 처리 (알림창 띄우지 않아도 실행되는 부분)
      try {
         selectedRole.value = role || {};
         selectedRoleIdx.value = idx;

         const response = await axios.get(`/api/authority/${role.authorityCd}`);

         if (response.data.result) {
            authorityCd.value = response.data.info.authorityCd;
            authorityNm.value = response.data.info.authorityNm;
            description.value = response.data.info.description;
         }

         authorityMenuGetList(role.authorityCd); // 권한에 따른 메뉴 조회
      } catch (err) {
         Swal.fire({
            icon: "error",
            title: "API 조회 실패",
            text: `Error: ${err.response?.data?.error || err.message}`
         });
      }
   };

   // 권한 등록
   const authorityAdd = async () => {
      try {
         const response = await axios.post('/api/authority', {
               authorityNm: authorityNm.value,
               description: description.value,
         });

         if (response.data.result) {
               Swal.fire({
                  icon: "success",
                  title: "등록 성공"
               });
               roles.value = response.data.list;
               modalClose();
         }
      } catch (err) {
         Swal.fire({
            icon: "error",
            title: "등록 실패",
            text: err.response?.data?.error || err.message
         });
      }
   };

   // 권한 수정
   const authorityModify = async (code) => {
      try {
         const result = await Swal.fire({
            title: `"${authorityNm.value}" 권한을 수정하시겠습니까?`,
            icon: "question",
            showCancelButton: true,
            confirmButtonText: "닫기",
            cancelButtonText: "수정"
         });

         if (result.dismiss === Swal.DismissReason.cancel) {
            const response = await axios.put(`/api/authority`, {
               authorityCd: code,
               authorityNm: authorityNm.value,
               description: description.value,
               updateId: "admin"
            });

            if (response.data.result) {
               Swal.fire({ title: "수정 완료", icon: "success" });
               roles.value = response.data.list;
               modalClose();
            }
         }
      } catch (err) {
         Swal.fire({ icon: "error", title: "수정 실패", text: err.response?.data?.error || err.message });
      }
   };

   // 권한 삭제
   const authorityRemove = async (code) => {
      try {
         const response = await axios.delete(`/api/authority/${code}`);

         if (response.data.result) {
               roles.value = response.data.list;
         }
      } catch (err) {
         Swal.fire({
            icon: "error",
            title: "삭제 실패",
            text: err.response?.data?.error || err.message
         });
      }
   };

// ================================================== 메뉴 관련 API ==================================================
   const menuData = ref([]);

   // 메뉴 전체 조회
   const menuGetList = async () => {
      try {
         const response = await axios.get('/api/menu');
         menuData.value = menuGetListCallbackTreeBuild(response.data);
      } catch (err) {
         Swal.fire({
            icon: "error",
            title: "메뉴 조회 실패",
            text: err.response?.data || err.message
         });
      }
   };
   // 전체 메뉴 데이터 + 권한 체크 정보
   const fullMenuList = ref([]);

   // 권한별 메뉴 (authYn=1인 것만)
   const authorizedMenuList = ref([]);

   // 권한별 메뉴 조회
   const authorityMenuGetList = async (code) => {
      try {
         const response = await axios.get(`/api/menu/info/${code}`);

         fullMenuList.value = menuGetListCallbackTreeBuild(response.data);  // 이미 selected 세팅 포함

         // 조회모드 기준 메뉴 구성 (authYn=1인 것만 필터링)
         authorizedMenuList.value = fullMenuList.value.filter(menu => menu.selected || menu.subMenus.some(sub => sub.selected));

         // 화면 표시용 menuData 구성
         updateMenuDataByMode();
      } catch (err) {
         Swal.fire({ icon: "error", title: "메뉴 조회 실패", text: err.response?.data || err.message });
      }
   };

   const updateMenuDataByMode = () => {
      if (isMenuEditing.value) {
         menuData.value = JSON.parse(JSON.stringify(fullMenuList.value));
      } else {
         menuData.value = fullMenuList.value.filter(menu => menu.selected || menu.subMenus.some(sub => sub.selected));
      }
   }

   // 메뉴 트리 구조 변환 함수
   function menuGetListCallbackTreeBuild(menuArray) {
      const map = new Map();
      const tree = [];

      menuArray.forEach(item => {
         map.set(item.menuCd, {
            ...item,
            subMenus: [],
            selected: item.authYn === 1
         });
      });

      menuArray.forEach(item => {
         if (!item.parentMenuCd) {
            tree.push(map.get(item.menuCd));
         } else {
            if (map.has(item.parentMenuCd)) {
               map.get(item.parentMenuCd).subMenus.push(map.get(item.menuCd));
            }
         }
      });

      return tree;
   }

   // 수정 모드 진입 시 체크 상태 재적용
   const applySelectedState = (menus) => {
      menus.forEach(menu => {
         menu.selected = menu.authYn === 1;
         if (menu.subMenus && menu.subMenus.length > 0) {
            applySelectedState(menu.subMenus);
         }
      });
   };

// ================================================== 드롭다운 토글 ==================================================
   const optionsToggle = (idx) => {
      isEditMenu.value = (selectedRoleIdx.value === idx) ? !isEditMenu.value : true; selectedRoleIdx.value = idx;
   };
   const outsideClickHandler = (e) => {
      if (!e.target.closest('.editing-dropdown-menu')) {
         isEditMenu.value = false;
      }
   };
</script>


<style>
   .role-wrapper {
      cursor: pointer;
      padding: 5px;
   }

   .selected-role {
      background-color: #d0d9ee;
      font-weight: 500;
      border-radius: 3px;
   }

   .form-control[readonly] {
      background-color: #f5f5f5; /* 🔹 연한 회색 배경 */
      border: 1px solid #dcdcdc; /* 🔹 흐린 테두리 */
      color: #999; /* 🔹 글씨 색 연하게 */
      cursor: not-allowed; /* 🔹 입력 불가 커서 */
      pointer-events: auto; /* 🔹 클릭 가능하도록 변경 */
   }
</style>