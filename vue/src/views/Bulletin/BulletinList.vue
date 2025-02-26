<template>
  <div class="content">
    <div class="container-fluid">
      <div>
        <div class="card">
          <div class="card-body">
            <h4 class="card-title float-left mt-1">공지사항 목록</h4>
            <button class="btn btn-primary btn-sm btn-fill float-right" @click="goToBulletinAdd">등록</button>
          </div>
        </div>
      </div>

      <div class="white-background">
        <div class="controls">
          <select v-model="searchColumn" class="custom-select">
            <option value="" disabled selected>선택하세요</option>
            <option value="nttSj">제목</option>
          </select>
          <input
            type="text"
            v-model="searchKeyword"
            @input="filterGrid"
            placeholder="검색어를 입력하세요"
            class="custom-input"
          />
        </div>

        <!-- 게시글 목록 -->
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
import { useRoute, useRouter } from 'vue-router';
import axios from 'axios';

const route = useRoute();
const router = useRouter();
const bbsId = ref(route.params.bbsId); // bbsId를 ref로 감싸서 반응형 처리

console.log('라우트 파라미터:', route.params);

const gridInstance = ref(null);
const BulletinList = ref([]);
const searchColumn = ref('');
const searchKeyword = ref('');

// 등록 버튼 클릭 시 bulletinAdd 페이지로 이동
const goToBulletinAdd = () => {
  router.push({
    name: 'BulletinAdd',
    query: { bbsId: bbsId.value}, // .value 사용하여 값 전달
  });
};

// 게시글 목록 조회 함수
const BulletinGetList = async () => {
  try {
    const { data } = await axios.get(`/api/bulletin/bulletinList?bbsId=${bbsId.value}`);
    BulletinList.value = (data.resultList || []).map((item, index) => ({
      rowNum: index + 1,
      ...item,
    }));

    if (gridInstance.value) {
      gridInstance.value.resetData(BulletinList.value); // 그리드 데이터 갱신
    }
  } catch (error) {
    console.error('게시글 목록 불러오기 오류:', error);
    BulletinList.value = [];
  }
  console.log('게시글 목록:', BulletinList.value);
};

// 그리드 초기화
const initializeGrid = () => {
  destroyGrid(); // 기존 그리드 제거

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
      { header: '제목', name: 'nttSj', align: 'left', minWidth: 200, sortable: true },
      { header: '작성자', name: 'ntcrNm', align: 'center', width: 120, sortable: true },
      { header: '작성일', name: 'frstRegistPnttm', align: 'center', width: 140, sortable: true },
      { header: '조회수', name: 'inqireCo', align: 'center', width: 100, sortable: true },
      { header: '관리', name: 'action', align: 'center', width: 150 },
    ],
  });

  gridInstance.value.on('click', (e) => {
    if (e.columnName === 'nttSj') {
      const row = gridInstance.value.getRow(e.rowKey);
      goToBulletinInfo(row);
    }
  });
};

// 게시글 상세 조회 이동 함수
const goToBulletinInfo = (row) => {
  console.log("테스트:",row.nttId);
  router.push({ path: `/bulletin/bulletinInfo/${bbsId.value}/${row.nttId}`})//, params: { bulletinId: row.nttId, bbsId: bbsId.value } });
};


// 그리드 제거 함수
const destroyGrid = () => {
  if (gridInstance.value) {
    gridInstance.value.destroy();
    gridInstance.value = null;
  }
};

// 검색 필터링 함수
const filterGrid = () => {
  if (!searchKeyword.value) {
    gridInstance.value?.resetData(BulletinList.value);
    return;
  }

  const filteredData = BulletinList.value.filter((item) => {
    if (searchColumn.value && item[searchColumn.value]) {
      return item[searchColumn.value].includes(searchKeyword.value);
    }
    return false;
  });

  gridInstance.value?.resetData(filteredData);
};

// 컴포넌트 마운트 시 실행
onMounted(async () => {
  await BulletinGetList();
  initializeGrid();
});

// 컴포넌트 언마운트 시 그리드 제거
onBeforeUnmount(() => {
  destroyGrid();
});
</script>

<style scoped>
.custom-select {
  margin-right: 10px;
  padding: 5px;
  border-radius: 4px;
  border: 1px solid #ccc;
}

.custom-input {
  padding: 5px;
  border-radius: 4px;
  border: 1px solid #ccc;
}

.white-background {
  background-color: #fff;
  padding: 15px;
  border-radius: 5px;
}
</style>
