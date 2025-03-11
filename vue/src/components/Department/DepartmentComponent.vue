<template>
   <div>
      <Tree :value="treeData" class="custom-tree" lazy draggable>
         <template #togglericon="{ expanded, node }">
            <!-- ✅ children이 없어도 무조건 공간 유지 -->
            <i
               :class="expanded ? 'fa-solid fa-angle-down me-2' : 'fa-solid fa-angle-right me-2'"
               :style="{ opacity: node.children && node.children.length > 0 ? 1 : 0.3 }">
            </i>
         </template>

         <template #default="slotProps">
            <div :data-department-cd="slotProps.node.key">
               <VueDraggableNext
                  :list="departmentTree.children || []"
                  :group="'department'"
                  :componentData="{ tag: 'div' }"
                  :data-department-id="slotProps.node.key"
                  @add="onDrop"
               >
                  <div class="node-buttons d-flex justify-content-between align-items-center">
                     <span class="p-treenode-label" @click="departmentToMemList(slotProps.node)"  :data-department-cd="slotProps.node.key">
                        {{ slotProps.node.label }}
                     </span>

                     <button class="btn-toggle" @click.stop="btnDepartmentMenuOpen($event, slotProps.node)">
                        <i class="fa-solid fa-ellipsis-vertical"></i>
                     </button>
                  </div>
               </VueDraggableNext>
            </div>
         </template>
      </Tree>

      <!-- 드롭다운 메뉴 -->
      <OverlayPanel ref="op">
         <ul class="department-dropdown-menu"> <!-- ✅ 클래스명 변경 -->
            <li @click="btnDepartmentModify(selectedNode)">부서 수정</li>
            <li @click="btnDepartmentRemove(selectedNode)">부서 삭제</li>
            <li @click="btnDepartmentAdd(selectedNode)">하위 부서 추가</li>
         </ul>
      </OverlayPanel>
   </div>

</template>

<script setup>
   import Tree from 'primevue/tree';
   import OverlayPanel from 'primevue/overlaypanel';
   import { VueDraggableNext } from 'vue-draggable-next'

   import { ref, defineProps, defineEmits, watch, defineComponent } from 'vue';

   const op = ref(null) // OverlayPanel 참조
   const selectedNode = ref(null) // 클릭한 노드 저장

   const props = defineProps({
      departmentTree: {
         type: Array,
         default: () => []
      }
   });

   defineComponent({
      components: { VueDraggableNext }
   });

   // const onDrop = (event) => {
   //    const droppedElement = event.to.querySelector("[data-department-cd]");

   //    if (!droppedElement) {
   //       console.error("data-department-cd 값을 가진 요소를 찾을 수 없습니다.");
   //       return;
   //    }

   //    const deptCd = droppedElement.getAttribute("data-department-cd");
   //    console.log("드롭된 프로젝트 ID: ", deptCd);
   // };

   // 부모에게 등록, 수정, 삭제 이벤트 전달
   const emit = defineEmits([
         "departmentToMemList",
         "btnDepartmentAdd",
         "btnDepartmentModify",
         "btnDepartmentRemove",
         "memberDropped"
      ]);

   const onDrop = (event) => {
      const memberData = event.item._underlying_vm_;

      // 가장 가까운 data-department-cd 가진 상위 div 찾기
      const droppedElement = event.to.closest('div[data-department-cd]');

      if (!droppedElement) {
         console.error("🚨 부서 코드를 찾을 수 없습니다.");
         return;
      }

      const deptCd = droppedElement.getAttribute("data-department-cd");
      const deptNm = droppedElement.querySelector(".p-treenode-label")?.innerText.trim() || '';

      emit("memberDropped", {
         dept: { deptCd, deptNm },
         member: {
            esntlId: memberData.esntlId,
            mberNm: memberData.mberNm,
            mberId: memberData.mberId,
         }
      });
   };


   const treeData = ref([]); // 초기화
   const treeKey = ref(0);   // 강제 리렌더링을 위한 key 값

   // 부모에서 전달된 데이터 변경 감지
   watch(() => props.departmentTree, (newVal) => {
      if (!newVal || newVal.length === 0) {
         return;
      }

      treeData.value = JSON.parse(JSON.stringify(newVal)); // 반응형 적용
      treeKey.value++; // ✅ 트리 강제 리렌더링
   }, { deep: true, immediate: true });

// ================================================== 버튼 이벤트 ==================================================
   // 드롭다운 메뉴 열기
   const btnDepartmentMenuOpen = (event, node) => {
      selectedNode.value = node // 현재 클릭한 노드 저장
      op.value.toggle(event, event.currentTarget); // 클릭한 버튼을 기준으로 정렬
      setTimeout(() => {
         // const panel = document.querySelector('.p-overlaypanel'); // OverlayPanel 요소 가져오기
         // if (panel) {
         //    panel.style.left = '555.219px'; // 강제 left 값 설정
         // }

         const panelContent = document.querySelector('.p-overlaypanel-content'); // ✅ 내부 컨텐츠 요소 가져오기
         if (panelContent) {
            panelContent.style.padding = '0.5rem'; // 내부 패딩 적용
         }
      }, 10); // OverlayPanel이 렌더링된 후 적용되도록 약간의 딜레이 추가
   };

   // 부서 선택 시 부서코드 부모에게 전달
   const departmentToMemList = (node) => {
      emit("departmentToMemList", node);
   };


   // 하위 부서 추가
   const btnDepartmentAdd = (node) => {
      emit("btnDepartmentAdd", node); // 부모로 수정 이벤트 전달
   };

   // 부서 수정
   const btnDepartmentModify = (node) => {
      emit("btnDepartmentModify", node); // 부모로 수정 이벤트 전달
   };

   // 부서 삭제
   const btnDepartmentRemove = (node) => {
      emit("btnDepartmentRemove", node); // 부모로 삭제 이벤트 전달
   };
</script>

<style lang="scss">

   /* 트리 전체 */
   .custom-tree {
      padding: 0;
      border: none !important;
      padding: 0 !important;
   }

   .custom-tree .p-tree-container {
      padding-bottom: 10px;
   }

   /* 각 노드 */
   .custom-tree .p-treenode {
      padding: 0;
   }

   /* 노드 내용 (아이콘 + 텍스트) */
   .custom-tree .p-treenode-content {
      display: flex;
      align-items: center;
      height: 35px;
      border-radius: 3px;
      transition: background-color 0.2s;
      padding: 20px 0px;
   }

   /* 펼침/닫힘 아이콘 */
   .custom-tree .p-tree-toggler {
      font-size: 12px;
      color: #747474;
      margin: 0px !important;
   }

   .p-tree .p-treenode .p-treenode-content .p-tree-toggler {
      visibility: visible !important; /* ✅ 숨기지 않음 */
      opacity: 0.5; /* ✅ 연하게 표시 */
      width: 1rem !important;
      height: 1rem !important;

      i {
         font-size: 15px !important;
      }
   }

   .p-tree-toggler:hover {
      background: #BFDBFE !important;
      border-radius: 3px !important;
      color: #343a40 !important; /* ✅ hover 시 색상 변경 */
   }

   .p-tree-toggler:focus {
      outline: 0 none;
      outline-offset: 0;
      box-shadow: 0 0 0 0.2rem #BFDBFE;
      border-radius: 3px !important;
   }

   /* 노드 텍스트 */
   .custom-tree .p-treenode-label {
      font-size: 14px;
      color: #747474;
      font-weight: 600;
   }

   .p-treenode-children {
      padding-left: 1rem !important; /* ✅ 하위 노드의 들여쓰기 제거 */
      margin-left: 0px !important; /* ✅ 혹시 모를 마진도 제거 */
      font-size: 14px;

      >span {
         font-size: 12px !important;
      }
   }

   .p-treenode-icon {
      display: none;
   }

   /* 자식 없는 노드의 토글버튼 강제 숨김 */
   .custom-tree .p-treenode-leaf .p-tree-toggler {
      display: none !important;
   }

   .btn-toggle {
      all: unset;
   }

   .p-treenode-label {
      width: 100%;
      font-size: 10px;
      cursor: pointer;
   }

   .p-overlaypanel {
      min-width: 150px !important;
      background: #fff !important;
      box-shadow: 0 2px 10px rgba(0, 0, 0, 0.1);
   }

   .department-dropdown-menu {
      list-style: none;
      margin: 0;
      padding-left: 0px;
      background: #fff;
      min-width: 100px;
   }

   .department-dropdown-menu li {
      padding: 15px 5px;
      cursor: pointer;
      font-size: 13px;
      color: #747474; /* ✅ 글씨 색상 설정 */
      font-weight: bold;
      transition: background 0.2s;
   }

   .department-dropdown-menu li:hover {
      background: #f5f5f5;
   }
   .p-treenode-leaf .p-tree-toggler {
      display: inline-block !important; /* ✅ 리프 노드에도 토글 아이콘 표시 */
      visibility: visible !important;
   }

</style>
