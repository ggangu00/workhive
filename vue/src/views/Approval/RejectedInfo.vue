<template>
    <div class="content">
      <ApprovalInfo
        :headButtons="headButtons"
        :ApprovalButtons="false"
        :showFile="false"
        :title="title"
        @button-click="buttonClick"
      />
      <!--  이벤트추가-->
    </div>
  </template>

  <script setup>
  import {onMounted, ref} from 'vue';
  import ApprovalInfo from '@/components/PaymentLayout/ApprovalInfo.vue';
  import axios from '../../assets/js/customAxios';
  import { useRoute } from 'vue-router';
  import { useRouter } from 'vue-router';
  import Swal from 'sweetalert2';


  const route = useRoute();
  const docCd = ref('');
  const docKind = ref('');
  const formType = ref('');
  const formCd = ref('');
  const deptNm = ref('');
  const docTitle = ref('');
  const docCnEditor = ref('');
  const atchFileId =ref('');

  //넘긴 문서코드
  onMounted(()=>{
    docCd.value = route.query.docCd || "";
    docKind.value = route.query.docKind || "";
    formType.value = route.query.formType || "";
    formCd.value = route.query.formCd || "";
    deptNm.value = route.query.deptNm || "";
    docTitle.value = route.query.docTitle || "";
    docCnEditor.value = route.query.docCnEditor || "";
    atchFileId.value=route.query.atchFileId || "";

    window.history.replaceState({}, '', route.path);
  })
  //버튼명
  const headButtons = ref([
    { label: '회수', class: 'btn-primary' },
    { label: '재기안', class: 'btn-warning' },
    { label: '삭제', class: 'btn-success' }
  ]);

  //버튼별 기능
  const buttonClick = async (buttonName)=>{
    switch(buttonName){
      case '회수' :
        await retrieveBtn();
        break;

      case '재기안':
        await restartDraft();
        break;

      case '삭제':
        await deleteDocument();
        break;

    }
  }

  //회수코드
  const retrieveBtn = async () => {
      if (!docCd.value) {
        Swal.fire({
        icon: "error",
        title: "등록실패",
        text: "문서가 없습니다.",
      })
        return;
      }
        const response = await axios.put(`/api/document/retrieve/${docCd.value}`, {});
        if (response.status == 200) {
          Swal.fire({
               icon: "success",
               title: "회수 성공",
               text: "임시함에서 확인하세요"
            })
            router.push({
              path: '/approval/retrieveList'})
        } else {
          Swal.fire({
          icon: "errowr",
          title: "회수실패"
          })
        }

    };

  //재기안코드
  const router = useRouter();
  const restartDraft = () => {
    if (!docCd.value) {
      Swal.fire({
      icon: "error",
      title: "등록실패",
      text: "문서가 없습니다.",
    })
      return;
    }
    router.push(`/approval/restartDraft?docCd=${docCd.value}`);
    router.push({
    path: '/approval/restartDraft',
    query: {
      docCd: docCd.value,
      docKind: docKind.value,
      formType: formType.value,
      formCd: formCd.value,
      deptNm: deptNm.value,
      docTitle: docTitle.value,
      docCnEditor: docCnEditor.value
    }
  });
  };

  //삭제코드
  const deleteDocument = async() => {
    const response = await axios.put(`/api/document/delete/${docCd.value}`,{})
    if (response.status == 200) {
      Swal.fire({
         icon: "success",
         title: "삭제성공",
      });
          router.push({path: '/approval/rejectedList'})
        } else {
         Swal.fire({
         icon: "error",
         title: "삭제실패",
      });
          router.push({path: '/approval/rejectedList'})
        }
  }
  const title="반려문서"
  </script>