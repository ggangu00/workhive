<template>
    <div class="content">
    <div class="container-fluid">
      <div>
        <div class="card">
          <div class="card-body">
            <h4 class="card-title float-left mt-1">게시글 목록</h4>           
           

          </div>
        </div>
      </div>

      <div class="white-background">
        <div class="controls">
          <select  v-model="searchColumn" class="custom-select">
            <option value="" disabled selected>선택하세요</option>
            <option value="nttSj">제목</option>
            
          </select>
          <input type="text" v-model="searchKeyword" @input="filterGrid" placeholder="검색어를 입력하세요" class="custom-input" />           
        </div>

        <!-- 업무 목록 -->
        <div class="row mt-2">
          <div class="col" style="height: 550px;">
            <div id="bulletinGrid"></div>
            <div id="pagination" class="tui-pagination"></div>
          </div>
        </div>
      </div>
    </div>
  </div>
   
   </template>
<script setup>
import { onMounted, onBeforeUnmount, ref } from 'vue';
import axios from 'axios';

const gridInstance = ref(null);
const BulletinList = ref([]);


//조회
const BulletinGetList = async () => {
  try {
    const { data } = await axios.get('/api/bulletin/bulletinList?bbsId=BBS001');
    BulletinList.value = (data.resultList || []).map((item, index) => ({
      rowNum: index + 1, // 1부터 시작하는 행번호 추가
      ...item
    }));
    gridInstance.value?.resetData(BulletinList.value); //  Grid에 데이터 업데이트
  } catch (error) {
    console.error('게시글 목록 불러오는 중 오류 발생:', error);
    BulletinList.value = [];
  }
  console.log("테스트",BulletinList.value);
};
 
 


const initializeGrid = () => {
  destroyGrid(); // 중복 방지 위해 기존 인스턴스 제거
  
  gridInstance.value = new window.tui.Grid({
    el: document.getElementById('bulletinGrid'),
    data: BulletinList.value,
    scrollX: false, // ✅ 수평 스크롤 제거 (열이 전체 너비 사용)
    scrollY: true,
    bodyHeight: 480, // ✅ 표 높이 지정    
    pageOptions: {
      useClient: false, // ✅ 클라이언트 사이드 페이지네이션
      perPage: 10,
    },
    columnOptions: {
      resizable: true, // ✅ 사용자 열 너비 조정 가능
    },
    columns: [
      { header: '번호', name: 'rowNum', align: 'center', width: 60, sortable: true },
      { 
        header: '제목', 
        name: 'nttSj', 
        sortable: true, 
        align: 'left', 
        minWidth: 200, // ✅ 최소 너비 설정
      },
      { 
        header: '작성자', 
        name: 'ntcrNm', 
        sortable: true, 
        align: 'center', 
        width: 120, 
      },
      { 
        header: '작성일', 
        name: 'frstRegistPnttm', 
        sortable: true, 
        align: 'center', 
        width: 140, 
      },
      { 
        header: '조회수', 
        name: 'inqireCo', 
        sortable: true, 
        align: 'center', 
        width: 140,         
      },
      { 
        header: '관리', 
        name: 'action', 
        align: 'center', 
        width: 150, // ✅ 버튼 공간 확보
        
      },
    ],
  });
  
  // ✅ 열 너비 자동 맞춤
  gridInstance.value.refreshLayout();
};

//  Grid 제거 함수
const destroyGrid = () => {
  if (gridInstance.value) {
    gridInstance.value.destroy(); // 기존 Grid 제거
    gridInstance.value = null; // 참조 초기화
  }
};

// 컴포넌트 마운트 시 초기화
onMounted(async () => {
  await BulletinGetList(); // 데이터 가져오기
  initializeGrid(); // Grid 생성
});

// 페이지 이동 시 Grid 제거
onBeforeUnmount(() => {
  destroyGrid(); // 중복 방지 및 메모리 해제
});

</script>