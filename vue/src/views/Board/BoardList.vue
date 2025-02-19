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
          <select class="custom-select">
            <option value="">선택</option>
            <option value="공지사항">공지사항</option>
            <option value="사내게시판">사내게시판</option>
          </select>
          <input type="text" class="custom-input" />               
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

const router = useRouter();
const gridInstance = ref(null);
const BoardList = ref([]);

//수정기능
// 🖱️ 수정 버튼 클릭 시 페이지 이동 (쿼리 파라미터로 여러 값 전달)
const udtEvent = (rowKey) => {
  const selectedRow = gridInstance.value?.getRow(rowKey); // 선택된 행 데이터 가져오기

  if (!selectedRow) {
    alert('선택된 게시글을 찾을 수 없습니다.');
    return;
  }

  console.log(`수정 버튼 클릭됨:`, selectedRow);
  
  console.log(`수정 버튼:`, selectedRow.bbsNm);
  console.log(`수정 버튼:`, selectedRow.bbsTyCode);
  console.log(`수정 버튼:`, selectedRow.fileAtchPosblAt);
  console.log(`수정 버튼:`, selectedRow.answerAt);

  //  쿼리 파라미터로 값 전달
  router.push({
    path: '/board/boardUpdate',
    query: {
      bbsNm: selectedRow.bbsNm,
      bbsTyCode: selectedRow.bbsTyCode,
      fileAtchPosblAt: selectedRow.fileAtchPosblAt,
      answerAt: selectedRow.answerAt
    }
    
   
  });
};

const BoardGetList = async () => {
  try {
    const { data } = await axios.get('/api/board/boardList');
    BoardList.value = (data.resultList || []).map((item, index) => ({
      rowNum: index + 1, // ✅ 1부터 시작하는 행번호 추가
      ...item
    }));
    gridInstance.value?.resetData(BoardList.value); // 🔄 Grid에 데이터 업데이트
  } catch (error) {
    console.error('게시글 목록 불러오는 중 오류 발생:', error);
    BoardList.value = [];
  }
  console.log("테스트",BoardList.value);
};

//  Grid 제거 함수
const destroyGrid = () => {
  if (gridInstance.value) {
    gridInstance.value.destroy(); // 기존 Grid 제거
    gridInstance.value = null; // 참조 초기화
  }
};



const delEvent = async (rowKey) => {
  const selectedRow = gridInstance.value?.getRow(rowKey);

  if (selectedRow) {
    console.log(`✅ 삭제 버튼 클릭됨, 화면 표시 번호: ${selectedRow.rowNum}, bbsId: ${selectedRow.bbsId}`);
    await axios.delete('/api/board/boardRemove', { params: { bbsId: selectedRow.bbsId } });
    await BoardGetList();
  }
};




// 🏗️ Grid 초기화
const initializeGrid = () => {
  destroyGrid(); // 중복 방지 위해 기존 인스턴스 제거

  gridInstance.value = new window.tui.Grid({
    el: document.getElementById('boardGrid'),
    data: BoardList.value,
    scrollX: false,
    scrollY: true,
    rowHeaders: ['checkbox'],
    pageOptions: {
      useClient: true, // 서버 사이드 페이지네이션 사용
      perPage: 5,
    },
    
    columns: [
      { header: '번호', name: 'rowNum', sortable: true, align: 'center' },
      { header: '게시판명', name: 'bbsNm', sortable: true },
      { header: '작성자', name: 'frstRegisterNm', sortable: true },
      { header: '작성일', name: 'frstRegisterPnttm', sortable: true },
      {
        header: '관리', name: 'action', align: 'center', renderer: BtnRenderer,
      }
    ]
  });
};

// 🏗️ 컴포넌트 마운트 시 초기화
onMounted(async () => {
  await BoardGetList(); // 데이터 가져오기
  initializeGrid(); // Grid 생성
});

// 🚪 페이지 이동 시 Grid 제거
onBeforeUnmount(() => {
  destroyGrid(); // 중복 방지 및 메모리 해제
});

// 🎛️ 버튼 렌더러 클래스
class BtnRenderer {
  constructor(props) {
    const el = document.createElement('div');
    el.className = 'btn-group';
    el.innerHTML = `
      <button class="btn btn-success btn-fill me-2" data-type="edit">수정</button>
      <button class="btn btn-danger btn-fill" data-type="delete">삭제</button>
    `;

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
