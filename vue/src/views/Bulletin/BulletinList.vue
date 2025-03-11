<template>
  <div class="content">
    <div class="container-fluid">
      <div>
        <div class="card">
          <div class="card-body">
            <h4 v-if="bbsNm" class="card-title float-left mt-1 font-weight-bold">{{ bbsNm }}</h4>   
            <button class="btn btn-primary btn-fill float-right" @click="goToBulletinAdd">등록</button>
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
import axios from '../../assets/js/customAxios';

const route = useRoute();
const router = useRouter();
const bbsId = ref(route.params.bbsId);

const gridInstance = ref(null);
const BulletinList = ref([]);
const searchColumn = ref('');
const searchKeyword = ref('');

const bbsNm = ref(''); // 기본값 설정

// 등록 버튼 클릭 시 이동
const goToBulletinAdd = () => {
  router.push({
    name: 'BulletinAdd',
    query: { bbsId: bbsId.value },
  });
};

//  게시글 목록 조회 + 공지사항 정렬
const BulletinGetList = async () => {
  try {
    const { data } = await axios.get(`/api/bulletin/bulletinList?bbsId=${bbsId.value}`);

    // ✅ bbsNm을 올바른 경로에서 가져오기
    if (data.master && data.master.bbsNm) {
      bbsNm.value = data.master.bbsNm; // API에서 받은 게시판 이름으로 설정
    } else {
      alert("게시판 이름을 찾을 수 없습니다.");
    }

    let noticeCount = 0;
    BulletinList.value = (data.resultList || []).map((item, index) => {
      const isNotice = item.noticeAt?.trim().toUpperCase() === 'Y';
      if (isNotice) noticeCount += 1;

      return {
        rowNum: isNotice ? '📢' : index + 1 - noticeCount,
        noticeAt: isNotice ? 'Y' : 'N',
        ...item,
      };
    }).sort((a, b) => {
      if (a.noticeAt === 'Y' && b.noticeAt !== 'Y') return -1;
      if (a.noticeAt !== 'Y' && b.noticeAt === 'Y') return 1;
      return new Date(b.frstRegistPnttm) - new Date(a.frstRegistPnttm);
    });

    if (gridInstance.value) {
      gridInstance.value.resetData(BulletinList.value);
    }
  } catch (error) {
    alert("게시글 목록을 불러오는 데 실패했습니다. 다시 시도해주세요.");
    BulletinList.value = [];
  }
};



//  그리드 초기화
const initializeGrid = () => {
  destroyGrid();

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
      {
        header: '번호',
        name: 'rowNum',
        align: 'center',
        width: 60,
        className: 'bold-text',
        formatter: ({ value }) => {
          return value === '📢' ? `<span class="notice-icon">${value}</span>` : `${value}`;
        }
      },
      {
        header: '제목',
        name: 'nttSj',
        align: 'left',
        minWidth: 200,
        sortable: true,
        formatter: ({ value, row }) => {
          return row.noticeAt === 'Y'
            ? `<strong class="notice-text">${value}</strong>` // 공지글: 진하게
            : `<span class="normal-text">${value}</span>`; // 일반글: 덜 진하게
        }
      },
      {
        header: '등록자',
        name: 'wrterNm',
        align: 'center',
        width: 120,
        sortable: true,
        className: 'bold-text'
      },
      {
        header: '등록일',
        name: 'frstRegistPnttm',
        align: 'center',
        width: 140,
        sortable: true,
        formatter: ({ value }) => `${value}`
      },
      {
        header: '조회수',
        name: 'inqireCo',
        align: 'center',
        width: 100,
        sortable: true,
        className: 'bold-text'
      },
    ],
  });

  gridInstance.value.on('click', (e) => {
    if (e.columnName === 'nttSj') {
      const row = gridInstance.value.getRow(e.rowKey);
      goToBulletinInfo(row);
    }
  });
};

// 게시글 상세 조회 이동
const goToBulletinInfo = (row) => {
  router.push({ path: `/bulletin/bulletinInfo/${bbsId.value}/${row.nttId}` });
};

// 그리드 제거
const destroyGrid = () => {
  if (gridInstance.value) {
    gridInstance.value.destroy();
    gridInstance.value = null;
  }
};

// 검색 필터링
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
/* 공통 스타일 */
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

/* 공지사항 텍스트 스타일 (진하게) */
.notice-text {
  font-weight: bold;
  color: #333;
}

/* 일반 게시글 텍스트 스타일 (덜 진하게) */
.normal-text {
  font-weight: normal;
  color: #555;
}

/* 공지사항 아이콘 스타일 */
.notice-icon {
  color: #d9534f;
  font-weight: bold;
}
</style>



