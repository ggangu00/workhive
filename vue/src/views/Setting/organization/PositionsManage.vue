<template>
      <div class="content">
      <div class="container-fluid">
         <div class="card">
            <div class="card-body d-flex justify-content-between align-items-center">
               <h4 class="card-title float-left">직책 / 직급 관리</h4>
               <!-- 수정 버튼 (수정 모드 진입용) -->
               <div v-if="!isMenuEditing" class="button-group">
                  <button class="btn btn-success btn-fill" @click="btnMenuModifyMode">수정</button>
               </div>

               <!-- 취소/저장 버튼 (수정 모드일 때) -->
               <div v-else class="button-group">
                  <button class="btn btn-secondary btn-fill" @click="btnMenuCancel">취소</button>
                  <button class="btn btn-success btn-fill" @click="btnMenuSave">저장</button>
               </div>
            </div>
         </div>

         <div class="card">
            <div class="card-body">
               <div class="row m-0">
                  <section class="member_title_cover col-6">
                     <div class="lw_table tb_cols_multihead wrapped">
                        <div class="lw_tr ">
                           <div class="lw_th move">
                              <i class="blind">순서변경</i>
                           </div>
                           <div class="lw_th idx">순서</div>
                           <div class="lw_th title">직책</div>
                        </div>
                        <div class="lw_tr ">
                           <div class="lw_td move" draggable="false">
                              <i class="fa-solid fa-grip-vertical"></i>
                           </div>
                           <div class="lw_td idx">1</div>
                           <div class="lw_td title">이사</div>
                        </div>
                        <div class="lw_tr ">
                           <div class="lw_td move" draggable="false">
                              <i class="fa-solid fa-grip-vertical"></i>
                           </div>
                           <div class="lw_td idx">2</div>
                           <div class="lw_td title">이사</div>
                        </div>
                        <div class="lw_tr ">
                           <div class="lw_td move" draggable="false">
                              <i class="fa-solid fa-grip-vertical"></i>
                           </div>
                           <div class="lw_td idx">3</div>
                           <div class="lw_td title">이사</div>
                        </div>
                        <div class="lw_tr ">
                           <div class="lw_td move" draggable="false">
                              <i class="fa-solid fa-grip-vertical"></i>
                           </div>
                           <div class="lw_td idx">4</div>
                           <div class="lw_td title">이사</div>
                        </div>
                        <div class="lw_tr ">
                           <div class="lw_td move" draggable="false">
                              <i class="fa-solid fa-grip-vertical"></i>
                           </div>
                           <div class="lw_td idx">5</div>
                           <div class="lw_td title">이사</div>
                        </div>
                     </div>
                  </section>

                  <section class="member_title_cover col-6">
                     <div class="lw_table tb_cols_multihead wrapped">
                        <div class="lw_tr ">
                           <div class="lw_th move">
                              <i class="blind">순서변경</i>
                           </div>
                           <div class="lw_th idx">순서</div>
                           <div class="lw_th title">직책</div>
                        </div>
                        <div class="lw_tr ">
                           <div class="lw_td move" draggable="false">
                              <i class="fa-solid fa-grip-vertical"></i>
                           </div>
                           <div class="lw_td idx">1</div>
                           <div class="lw_td title">이사</div>
                        </div>
                        <div class="lw_tr ">
                           <div class="lw_td move" draggable="false">
                              <i class="fa-solid fa-grip-vertical"></i>
                           </div>
                           <div class="lw_td idx">2</div>
                           <div class="lw_td title">이사</div>
                        </div>
                        <div class="lw_tr ">
                           <div class="lw_td move" draggable="false">
                              <i class="fa-solid fa-grip-vertical"></i>
                           </div>
                           <div class="lw_td idx">3</div>
                           <div class="lw_td title">이사</div>
                        </div>
                        <div class="lw_tr ">
                           <div class="lw_td move" draggable="false">
                              <i class="fa-solid fa-grip-vertical"></i>
                           </div>
                           <div class="lw_td idx">4</div>
                           <div class="lw_td title">이사</div>
                        </div>
                        <div class="lw_tr ">
                           <div class="lw_td move" draggable="false">
                              <i class="fa-solid fa-grip-vertical"></i>
                           </div>
                           <div class="lw_td idx">5</div>
                           <div class="lw_td title">이사</div>
                        </div>
                     </div>
                  </section>
               </div>
            </div>
         </div>
      </div>
   </div>
</template>

<script setup>
   import { ref } from 'vue';
   // import axios from '../../../assets/js/customAxios';
   // import Swal from 'sweetalert2';

// ============================================= 버튼이벤트 =============================================
   // 메뉴 수정 모드로 변환
   const isMenuEditing = ref(false)
   // 메뉴 수정 모드 진입
   const btnMenuModifyMode = () => {
      isMenuEditing.value = true
   }

   // 메뉴 수정 모드 취소
   const btnMenuCancel = () => {
      isMenuEditing.value = false
   }
</script>

<style lang="scss" scoped>
   .member_title_cover {
      border-radius: 3px;
      overflow: hidden;
      background-color: #fff;
   }

   .lw_tr:first-child {
      background-color: #ededfa;
   }

   .lw_tr:last-child {
      border-bottom: none;
   }

   .lw_td {
      width: 100%;
      display: flex;
      justify-content: space-between;
      align-items: center;
      color: #333;

      >.move {
         width: 5%;
      }

      >.idx {
         width: 45%;
      }

      >.title {
         width: 45%;
      }
   }



   /* 헤더 아이콘 숨김 처리 */
   i.blind {
      display: none;
   }

   /* 전체 표 스타일 정리 */
   .wrapped {
      border: 1px solid #cecece;
      border-radius: 3px;
      overflow: hidden;
   }
</style>
