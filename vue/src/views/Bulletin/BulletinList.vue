<template>
  <div class="content">
    <div class="container-fluid">
      <div>
        <div class="card">
          <div class="card-body">
            <h4 class="card-title float-left mt-1">공지사항List</h4>      
            <button class="btn btn-primary btn-sm btn-fill float-right" @click="goToBulletinAdd">등록</button>    
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
import { useRouter } from 'vue-router';
import axios from 'axios';


const router = useRouter();
let bbsId = "BBS001";//router.query.bbsId;

console.log("전체테스트:",router.params);

const gridInstance = ref(null);
const BulletinList = ref([]);
const searchColumn = ref('');
const searchKeyword = ref('');

// 등록 버튼 클릭 시 bulletinAdd 페이지로 이동
const goToBulletinAdd = () => {
  router.push('/bulletin/bulletinAdd');
};

// 조회 함수
const BulletinGetList = async () => {
  try {
    const { data } = await axios.get(`/api/bulletin/bulletinList?bbsId=${bbsId}`);
    BulletinList.value = (data.resultList || []).map((item, index) => ({
      rowNum: index + 1, // 1부터 시작하는 행번호 추가
      ...item
    }));
    gridInstance.value?.resetData(BulletinList.value); // Grid에 데이터 업데이트
  } catch (error) {
    console.error('게시글 목록 불러오는 중 오류 발생:', error);
    BulletinList.value = [];
  }
  console.log("테스트", BulletinList.value);
};

// Grid 초기화
const initializeGrid = () => {
  destroyGrid(); // 기존 인스턴스 제거
  
  gridInstance.value = new window.tui.Grid({
    el: document.getElementById('bulletinGrid'),
    data: BulletinList.value,
    scrollX: false,
    scrollY: true,
    bodyHeight: 480,  
    pageOptions: {
      useClient: false,
      perPage: 10,
    },
    columnOptions: {
      resizable: true,
    },
    columns: [
      { header: '번호', name: 'rowNum', align: 'center', width: 60, sortable: true },
      { header: '제목', name: 'nttSj', sortable: true, align: 'left', minWidth: 200 },
      { header: '작성자', name: 'ntcrNm', sortable: true, align: 'center', width: 120 },
      { header: '작성일', name: 'frstRegistPnttm', sortable: true, align: 'center', width: 140 },
      { header: '조회수', name: 'inqireCo', sortable: true, align: 'center', width: 140 },
      { header: '관리', name: 'action', align: 'center', width: 150 },
    ],
  });
  
  gridInstance.value.refreshLayout();

  // 제목 클릭 시 상세조회 페이지 이동 이벤트
  gridInstance.value.on('click', (e) => {
    if (e.columnName === 'nttSj') {
      const row = gridInstance.value.getRow(e.rowKey);
      goToBulletinInfo(row);
    }
  });
};

// Grid 제거 함수
const destroyGrid = () => {
  if (gridInstance.value) {
    gridInstance.value.destroy();
    gridInstance.value = null;
  }
};

// 상세조회 페이지 이동 함수
const goToBulletinInfo = (row) => {
  router.push({ name: 'BulletinInfo', params: { bulletinId: row.nttId } });
};

// 검색 필터 함수
const filterGrid = () => {
  // 검색 로직 구현 (필요에 따라 그리드 API 활용)
};

onMounted(async () => {
  await BulletinGetList();
  initializeGrid();
});

onBeforeUnmount(() => {
  destroyGrid();
});
</script>

<style scoped>
.custom-select {
  margin-right: 10px;
}
.custom-input {
  padding: 5px;
}
</style>
