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
          <select  v-model="searchColumn" class="custom-select">
            <option value="" disabled selected>선택하세요</option>
            <option value="bbsNm">게시판명</option>
            
          </select>
          <input type="text" v-model="searchKeyword" @input="filterGrid" placeholder="검색어를 입력하세요" class="custom-input" />           
        </div>

        <!-- 업무 목록 -->
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
import axios from 'axios';
import { useRouter } from 'vue-router';

const searchKeyword = ref(''); // 검색어 상태
const searchColumn = ref('');  // 검색할 컬럼 선택
const router = useRouter();
const gridInstance = ref(null);
const BoardList = ref([]);



//조회
const BoardGetList = async () => {
  try {
    const { data } = await axios.get('/api/board/boardList');
    BoardList.value = (data.resultList || []).map((item, index) => ({
      rowNum: index + 1, // 1부터 시작하는 행번호 추가
      ...item
    }));
    gridInstance.value?.resetData(BoardList.value); //  Grid에 데이터 업데이트
  } catch (error) {
    console.error('게시글 목록 불러오는 중 오류 발생:', error);
    BoardList.value = [];
  }
  console.log("테스트",BoardList.value);
};

// ✅ 조건 검색 필터링 함수
const filterGrid = () => {
  const keyword = searchKeyword.value.trim().toLowerCase();
  const column = searchColumn.value;

  if (!keyword) {
    gridInstance.value.resetData(BoardList.value); // 검색어 없으면 전체 데이터 표시
    return;
  }

  const filteredData = BoardList.value.filter((item) => {
    if (column) {
      // ✅ 특정 컬럼만 검색
      return String(item[column] ?? '').toLowerCase().includes(keyword);
    } else {
      // ✅ 전체 컬럼 검색
      return Object.values(item).some((value) =>
        String(value).toLowerCase().includes(keyword)
      );
    }
  });

  gridInstance.value.resetData(filteredData); // 필터링된 데이터로 Grid 갱신
};

//  Grid 제거 함수
const destroyGrid = () => {
  if (gridInstance.value) {
    gridInstance.value.destroy(); // 기존 Grid 제거
    gridInstance.value = null; // 참조 초기화
  }
};



const initializeGrid = () => {
  destroyGrid(); // 중복 방지 위해 기존 인스턴스 제거
  
  gridInstance.value = new window.tui.Grid({
    el: document.getElementById('boardGrid'),
    data: BoardList.value,
    scrollX: false, // ✅ 수평 스크롤 제거 (열이 전체 너비 사용)
    scrollY: true,
    bodyHeight: 480, // ✅ 표 높이 지정    
    pageOptions: {
      useClient: true, // ✅ 클라이언트 사이드 페이지네이션
      perPage: 10,
    },
    columnOptions: {
      resizable: true, // ✅ 사용자 열 너비 조정 가능
    },
    columns: [
      { header: '번호', name: 'rowNum', align: 'center', width: 60, sortable: true },
      { 
        header: '게시판명', 
        name: 'bbsNm', 
        sortable: true, 
        align: 'left', 
        minWidth: 200, // ✅ 최소 너비 설정
      },
      { 
        header: '작성자', 
        name: 'frstRegisterNm', 
        sortable: true, 
        align: 'center', 
        width: 120, 
      },
      { 
        header: '작성일', 
        name: 'frstRegisterPnttm', 
        sortable: true, 
        align: 'center', 
        width: 140, 
      },
      { 
        header: '사용여부', 
        name: 'useAt', 
        sortable: true, 
        align: 'center', 
        width: 140, 
        formatter: ({ value }) => value === 'A01' ? 'Y' : value === 'A02' ? 'N' : value
      },
      { 
        header: '관리', 
        name: 'action', 
        align: 'center', 
        width: 150, // ✅ 버튼 공간 확보
        renderer: BtnRenderer, 
      },
    ],
  });
  
  // ✅ 열 너비 자동 맞춤
  gridInstance.value.refreshLayout();
};


// 컴포넌트 마운트 시 초기화
onMounted(async () => {
  await BoardGetList(); // 데이터 가져오기
  initializeGrid(); // Grid 생성
});

// 페이지 이동 시 Grid 제거
onBeforeUnmount(() => {
  destroyGrid(); // 중복 방지 및 메모리 해제
});

//  버튼 렌더러 클래스
class BtnRenderer {
  constructor(props) {
    const el = document.createElement('div');
    el.className = 'btn-group';
    el.innerHTML = `
    <button class="btn btn-success btn-fill me-2" data-type="edit">수정</button>      
    `;

    // 버튼X <button class="btn btn-danger btn-fill cell-btn-custom" data-type="delete">삭제</button>
    
    el.addEventListener('click', (event) => {
      const type = event.target.dataset.type;
      const rowKey = props.rowKey;
      
      if (rowKey === undefined) {
        console.error('BtnRenderer: rowKey를 가져올 수 없습니다.', props);
        return;
      }
      
      type === 'edit' ? udtEvent(rowKey) : delEvent(rowKey);
    });
    
    this.el = el;
  }
  
  getElement() {
    return this.el;
  }
}

// 수정 버튼 클릭 시 페이지 이동 (쿼리 파라미터로 여러 값 전달)
const udtEvent = (rowKey) => {
  const selectedRow = gridInstance.value?.getRow(rowKey); // 선택된 행 데이터 가져오기

  if (!selectedRow) {
    alert('선택된 게시글을 찾을 수 없습니다.');
    return;
  }
  console.log(`수정 버튼 클릭됨:`, selectedRow);  
  
  //  쿼리 파라미터로 값 전달
  router.push({
    path: '/board/boardUpdate',
    query: {
      bbsId : selectedRow.bbsId,
      bbsNm: selectedRow.bbsNm,
      bbsTyCode: selectedRow.bbsTyCode,
      fileAtchPosblAt: selectedRow.fileAtchPosblAt,
      answerAt: selectedRow.answerAt,
      useAt: selectedRow.useAt
    }    
   
  });
};

// 삭제
const delEvent = async (rowKey) => {
  const selectedRowData = gridInstance.value?.getRow(rowKey);  
    console.log(`삭제 버튼 클릭됨, 화면 표시 번호: ${rowKey}`,selectedRowData.bbsId);

    await axios.post(`/api/board/boardRemove`,{params:{bbsId:selectedRowData.bbsId}});

    await BoardGetList();
  }




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
