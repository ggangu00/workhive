<template>
    <div class="content">
      <ApprovalInfo
        :headButtons="headButtons"
        :ApprovalButtons="false"
        :showFile="false"
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
  //import Modal from '../../components/Modal.vue';

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
        alert("문서 코드 x");
        return;
      }
        const response = await axios.put(`/api/document/retrieve/${docCd.value}`, {});
        if (response.status == 200) {
          alert("회수 성공");
        } else {
          alert("회수 실패");
        }

    };

  //재기안코드
  const router = useRouter();
  const restartDraft = () => {
    if (!docCd.value) {
      alert("문서 코드 x");
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
    console.log(response)
    if (response.status == 200) {
          alert("삭제 성공");
          router.push({path: '/approval/rejectedList'})
        } else {
          alert("삭제 실패");
          router.push({path: '/approval/rejectedList'})
        }

  }
  </script>