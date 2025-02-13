<template>
   <div class="content">
      <div class="container-fluid" @keydown.esc="modalClose">
         <div class="card">
            <div class="card-header d-flex justify-content-between align-items-center">
               <h4 class="card-title">부서 관리</h4>
            </div>
         </div>

         <div class="card">
            <div class="card-body">
               <div class="row m-0">
                  <!-- 트리 뷰 (왼쪽) -->
                  <div class="col-3 treeview">
                     <button @click="modalOpen">모달 오픈</button>
                     <ul>
                        <TreeNode
                           v-for="node in organizationTree"
                           :key="node.id"
                           :node="node"
                           @toggle="toggleTree"
                           @menuToggle="toggleMenu"
                           @action="handleAction"
                        />
                     </ul>
                  </div>

                  <!-- 구성원 테이블 (오른쪽) -->
                  <div class="col-9 personal-group">
                     <div class="table-line">
                        <div class="d-flex justify-content-between align-items-center p-2">
                           <div class="button-group justify-content-between">
                              <button class="btn btn-secondary btn-fill">이동</button>
                              <button class="btn btn-primary btn-fill">저장</button>
                              <button class="btn btn-danger btn-fill">삭제</button>
                           </div>
                           <div class="d-flex justify-content-between align-items-center">
                              <select name="searchSel" id="searchSel" class="form-select w-30">
                                 <option value="name">이름</option>
                                 <option value="auth">권한</option>
                              </select>
                              <input type="text" class="form-control w-50" placeholder="검색어 입력"/>
                              <button class="btn btn-info btn-fill">검색</button>
                           </div>
                        </div>
                     </div>

                     <table class="table personal-table">
                        <thead class="table-light">
                           <tr>
                              <th>
                                 <input type="checkbox" />
                              </th>
                              <th>사번</th>
                              <th>이름</th>
                              <th>직급</th>
                              <th>접근 권한</th>
                              <th>메뉴 권한</th>
                           </tr>
                        </thead>

                        <tbody>
                           <tr v-for="member in members" :key="member.id">
                              <td><input type="checkbox" /></td>
                              <td>{{ member.id }}</td>
                              <td class="profile-cell">
                                 <img :src="member.profileImage" alt="프로필 이미지" class="profile-img">
                                 <div class="profile-text">
                                    <span class="team-label">개발팀</span>
                                    <span class="user-name">{{ member.name }}</span>
                                 </div>
                              </td>
                              <td>{{ member.rank }}</td>
                              <td>
                                 <select class="form-select w-70">
                                    <option value="">선택하세요</option>
                                    <option value="">최고관리자</option>
                                    <option value="">부관리자</option>
                                    <option value="">운영관리자</option>
                                 </select>
                              </td>
                              <td>
                                 <select class="form-select w-70">
                                    <option value="">선택하세요</option>
                                    <option value="">메뉴1</option>
                                    <option value="">메뉴2</option>
                                 </select>
                              </td>
                           </tr>
                        </tbody>
                     </table>

                  </div>
               </div>
            </div>
         </div>

         <!-- [s]-->
         <Modal :isShowModal="isShowModal" :modalTitle="'부서 등록'" @click.self="modalClose">
            <!-- 모달 바디 -->
            <template v-slot:body>
               <div class="mb-3">
                  <label class="form-label">부서명 <i class="fa-solid fa-asterisk point-red"></i></label>
                  <input type="text" name="dept_name" class="form-control w30">
               </div>
               <div class="mb-3">
                  <div class="form-group has-label">
                     <label>부서 설명</label>
                  </div>
                  <textarea type="text" name="project_nm" class="form-control" placeholder="부서에 대한 설명을 입력하세요." style="height: 130px;"></textarea>
               </div>
            </template>

            <!-- 모달 푸터 -->
            <template v-slot:footer>
               <button type="button" class="btn btn-secondary btn-fill" @click="modalClose">닫기</button>
               <button type="button" class="btn btn-primary btn-fill" @click="confirm">등록</button>
            </template>
         </Modal>
         <!--업무등록 모달[e]-->
      </div>
   </div>
</template>

<script setup>
   import { ref } from "vue";
   import TreeNode from "../../components/TreeView/TreeNode.vue";
   import Modal from '../../components/Modal.vue';

   const isShowModal = ref(false);

   const organizationTree = ref([
   {
      id: "workhive",
      name: "WorkHive",
      count: 50,
      children: [
         {
            id: "devteam",
            name: "개발팀",
            count: 37,
            children: [
               { id: "dev1", name: "개발 1팀", count: 10, children: [] },
               { id: "dev2", name: "개발 2팀", count: 12, children: [] },
               { id: "dev3", name: "개발 3팀", count: 15, children: [] },
            ],
         },
         {
            id: "designteam",
            name: "디자인팀",
            count: 13,
            children: [
               { id: "design1", name: "디자인 1팀", count: 5, children: [] },
               { id: "design2", name: "디자인 2팀", count: 6, children: [] },
               { id: "design3", name: "디자인 3팀", count: 2, children: [] },
            ],
         },
      ],
   },
   ]);

   const toggleTree = (node) => {
      node.expanded = !node.expanded;
   };

   const toggleMenu = (node) => {
      node.showMenu = !node.showMenu;
   };

   const handleAction = (action, node) => {
      if (action === "edit") {
         console.log("조직 수정:", node.name);
      } else if (action === "add") {
         console.log("하위 조직 추가:", node.name);
      } else if (action === "delete") {
         console.log("조직 삭제:", node.name);
      }
   };

   const members = ref([
      { id: "2025001", name: "김민진", rank: "부장" },
      { id: "2025002", name: "박주현", rank: "부장" },
   ]);

   const modalClose = (e) => {
      if (e.key === "Escape") {
         if(isShowModal.value) {
            isShowModal.value = !isShowModal.value
         }
      } else {
         isShowModal.value = false;
      }
   }
   const modalOpen = () => {
      isShowModal.value = true;
   }

</script>

<style scoped>
   
   
   .menu-container {
      display: flex;
      justify-content: space-between;
      align-items: center;
      width: 100%;
   }
   .menu-button {
      border: none;
      background: none;
      font-size: 16px;
      cursor: pointer;
   }
   .dropdown-menu {
      position: absolute;
      background: white;
      box-shadow: 0px 0px 5px rgba(0, 0, 0, 0.2);
      border-radius: 5px;
      padding: 5px;
      z-index: 1000;
   }
   .dropdown-menu button {
      display: block;
      width: 100%;
      border: none;
      background: none;
      padding: 5px;
      text-align: left;
   }
</style>
