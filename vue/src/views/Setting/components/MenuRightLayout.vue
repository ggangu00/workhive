<template>

   <!-- 오른쪽 레이아웃 : 메뉴목록 조회 -->
   <div>
      <div class="d-flex justify-content-between align-items-center bottom-line p-2">
         <span>{{ selectedRole ? selectedRole.authorityNm : '' }}</span>
         <div class="button-group">
            <button class="btn btn-success btn-sm btn-fill">수정</button>
         </div>
         <div class="button-group">
            <button class="btn btn-secondary btn-sm btn-fill">취소</button>
            <button class="btn btn-success btn-sm btn-fill">저장</button>
         </div>
      </div>
      <div>
         <menuTree v-for="(item, idx) in menuData" :key="idx" :item="item" ></menuTree>
         <!-- <menuListView></menuListView> -->
      </div>
   </div>

</template>

<script setup>

</script>

<style lang="scss" scoped>

</style>