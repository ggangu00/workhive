<template>
  <div class="content">
    <div class="container-fluid">
      <div>
        <div class="card">
          <div class="card-body">
            <h4 class="card-title float-left mt-1">게시판 목록</h4>
          </div>
        </div>
      </div>

      <div class="white-background">
        <div class="controls">
          <select v-model="searchColumn" class="custom-select">
            <option value="" disabled selected>선택하세요</option>
            <option value="bbsNm">게시판명</option>
            <option value="frstRegisterNm">작성자</option>
          </select>
          <input
            type="text"
            v-model="searchKeyword"
            @input="filterGrid"
            placeholder="검색어를 입력하세요"
            class="custom-input"
          />
        </div>

        <!-- 게시판 목록 -->
        <div class="row mt-2">
          <div class="col" style="height: 550px;">
            <div id="boardGrid"></div>
            <div id="pagination" class="tui-pagination"></div>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { onMounted, onBeforeUnmount, ref } from 'vue';
import axios from '../../assets/js/customAxios';
import { useRouter } from 'vue-router';

const searchKeyword = ref('');
const searchColumn = ref('');
const router = useRouter();
const gridInstance = ref(null);
const BoardList = ref([]);

// 📌 게시판 목록 조회 API
const BoardGetList = async () => {
  try {
    const { data } = await axios.get('/api/board/boardList');
    BoardList.value = (data.resultList || []).map((item, index) => ({
      rowNum: index + 1,
      ...item
    }));
    gridInstance.value?.resetData(BoardList.value);
  } catch (error) {
    console.error('게시글 목록 불러오는 중 오류 발생:', error);
    BoardList.value = [];
  }
};


// 📌 게시판 상세 페이지 이동 (bbsNm, bbsIntrcn 추가)
const goToBoardInfo = (bbsId, bbsNm, bbsIntrcn) => {
  console.log("📌 이동할 게시판 정보:", { bbsId, bbsNm, bbsIntrcn });

  if (!bbsId) {
    alert("게시판 정보를 찾을 수 없습니다.");
    return;
  }

  router.push({
    path: "/board/boardInfo",
    query: {
      bbsId,
      bbsNm: bbsNm ?? '',
      bbsIntrcn: bbsIntrcn ?? '',
    }
  });
};


// 📌 Grid 초기화
const initializeGrid = () => {
  destroyGrid();

  gridInstance.value = new window.tui.Grid({
    el: document.getElementById('boardGrid'),
    data: BoardList.value,
    scrollX: false,
    scrollY: true,
    bodyHeight: 480,
    pageOptions: {
      useClient: true,
      perPage: 10,
    },
    columnOptions: {
      resizable: true,
    },
    columns: [
      { header: '번호', name: 'rowNum', align: 'center', width: 60, sortable: true },
      {
        header: '게시판명',
  name: 'bbsNm',
  sortable: true,
  align: 'left',
  minWidth: 200,
  formatter: ({ row }) => {
    const bbsId = row.bbsId ? row.bbsId : '';  // ✅ `undefined` 방지
    const bbsNm = row.bbsNm ? row.bbsNm : '제목 없음';  // ✅ `bbsNm` 기본값 설정
    return `<a href="javascript:void(0);" class="bbs-link" data-id="${bbsId}">${bbsNm}</a>`;
        },
      },
      {
        header: '작성일',
        name: 'frstRegisterPnttm',
        sortable: true,
        align: 'center',
        width: 140,
      },
      {
        header: '관리',
        name: 'action',
        align: 'center',
        width: 150,
        renderer: BtnRenderer,
      },
    ],
  });

  // 📌 게시판명 클릭 이벤트 리스너 추가
  gridInstance.value.on('click', (event) => {


  if (event.columnName === "bbsNm") {
    const rowKey = event.rowKey;
    const selectedRow = gridInstance.value.getRow(rowKey);

    if (!selectedRow || !selectedRow.bbsId) {
      console.error("⚠️ bbsId를 찾을 수 없음!", selectedRow);
      return;
    }

    // 📌 `bbsNm`, `bbsIntrcn` 추가하여 상세 페이지로 이동
    goToBoardInfo(selectedRow.bbsId, selectedRow.bbsNm, selectedRow.bbsIntrcn);
  }
});



  gridInstance.value.refreshLayout();
};

// 📌 Grid 제거 함수
const destroyGrid = () => {
  if (gridInstance.value) {
    gridInstance.value.destroy();
    gridInstance.value = null;
  }
};

// 📌 버튼 렌더러 클래스
class BtnRenderer {
  constructor(props) {
    const el = document.createElement('div');
    el.className = 'btn-group';
    el.innerHTML = `<button class="btn btn-success btn-fill me-2" data-type="edit">수정</button>`;

    el.addEventListener('click', () => {
      const rowKey = props.rowKey;
      if (rowKey === undefined) {
        console.error('BtnRenderer: rowKey를 가져올 수 없습니다.', props);
        return;
      }
      udtEvent(rowKey);
    });

    this.el = el;
  }

  getElement() {
    return this.el;
  }
}

// 📌 게시판 수정 페이지 이동
const udtEvent = (rowKey) => {
  const selectedRow = gridInstance.value?.getRow(rowKey);

  if (!selectedRow) {
    alert('선택된 게시글을 찾을 수 없습니다.');
    return;
  }

  router.push({
    path: '/board/boardModify',
    query: {
      bbsId: selectedRow.bbsId,
      bbsNm: selectedRow.bbsNm,
      bbsIntrcn: selectedRow.bbsIntrcn,
      bbsTyCode: selectedRow.bbsTyCode,
    }
  });
};

// 📌 컴포넌트 마운트 시 초기화
onMounted(async () => {
  await BoardGetList();
  initializeGrid();
});

// 📌 페이지 이동 시 Grid 제거
onBeforeUnmount(() => {
  destroyGrid();
});
</script>

<style>
.controls {
  display: flex;
  justify-content: flex-end;
  margin-bottom: 10px;
}

.custom-input,
.custom-select {
  width: 200px;
  height: 30px;
  padding: 5px;
  margin-right: 10px;
  font-size: 14px;
  border: 1px solid #ccc;
  border-radius: 5px;
  background-color: #fff;
  color: #333;
  appearance: none;
}

.white-background {
  background-color: white;
  padding: 20px;
  border-radius: 10px;
  box-shadow: 0 4px 6px rgba(0, 0, 0, 0.1);
  margin-top: 10px;
}
</style>
