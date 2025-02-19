<template>
  <div class="content">
    <div class="container-fluid">
      <!-- 페이지 헤더 -->
      <div class="row">
        <div class="col">
          <h3>게시판 목록</h3>
        </div>
      </div>
     
      <div class="white-background">
        <div class="controls">
          <select class="custom-select">
            <option value="">선택</option>
            <option value="공지사항">공지사항</option>
            <option value="사내게시판">사내게시판</option>
          </select>
          <input type="text" class="custom-input">
          <button class="btn btn-info btn-fill mr-1">조회</button>
          <button type="submit" class="btn btn-primary btn-fill">등록</button>
        </div>     

        <!-- 업무 목록 -->
        <div class="row mt-2">
          <div class="col" style="height: 550px;">
            <div id="boardGrid"></div>    
          </div> 
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { onMounted, ref } from 'vue';
import axios from 'axios';

let gridInstance = ref();

//===========axios===============
const BoardList = ref([]); //초기값 배열

// API 호출 (게시글 불러오기)
const BoardGetList = async () => {
  try {
    const result = await axios.get('/api/board/boardList');
    BoardList.value = result.data.resultList;
    gridInstance.value.resetData(BoardList.value); // 데이터가 성공적으로 불러와진 후에 그리드에 데이터 반영
  } catch (error) {
    console.error('게시글 목록 불러오는중 오류 발생:', error)
    BoardList.value = [];
  }
};

// 수정 버튼 클릭
const udtEvent = (rowKey) => {
  const selectedRow = gridInstance.value.getRow(rowKey); // 선택된 행 데이터 저장
  console.log(`수정 버튼 클릭됨, 행 번호: ${rowKey}`, selectedRow);
  // 이제 selectedRow를 사용하여 수정 기능을 구현
};

// 삭제 버튼 클릭
const delEvent = async (rowKey) => {
  const selectedRow = gridInstance.value.getRow(rowKey); // 선택된 행 데이터 저장
  console.log(`삭제 버튼 클릭됨, 행 번호: ${rowKey}`, selectedRow.bbsId);

  await axios.delete('/api/board/boardRemove', { params: { bbsId: selectedRow.bbsId } });

  BoardGetList(); // 게시판 목록 새로고침
};

//============ Toast UI Grid 초기화============
onMounted(() => {
  gridInstance.value = new window.tui.Grid({
    el: document.getElementById('boardGrid'),
    data: BoardList.value,
    scrollX: false,
    scrollY: true,
    rowHeaders: ['checkbox'],
    columns: [
      { header: '번호', name: 'rowNum', sortable: true, align: 'center' },
      { header: '게시판명', name: 'bbsNm', sortable: true },
      { header: '작성자', name: '', sortable: true },
      { header: '작성일', name: 'frstRegistPnttm', sortable: true },
      {
        header: '관리', name: 'action', align: 'center', renderer: BtnRenderer,
      }
    ]
  });

  BoardGetList(); // 그리드 초기화 후 게시글 목록 불러오기
});

// BtnRenderer
class BtnRenderer {
  constructor(props) {
    const el = document.createElement("div");
    el.className = "btn-group";

    el.innerHTML = `
      <button class="btn btn-success btn-fill me-2" data-type="edit">수정</button>
      <button class="btn btn-danger btn-fill" data-type="delete">삭제</button>
    `;

    el.addEventListener("click", (event) => {
      const type = event.target.dataset.type;
      const rowKey = props.rowKey; // rowKey를 직접 추출

      if (rowKey === undefined) {
        console.error("BtnRenderer: rowKey를 가져올 수 없습니다.", props);
        return;
      }

      // 수정 또는 삭제 처리
      if (type === "edit") {
        udtEvent(rowKey); // 수정 이벤트 처리
      } else if (type === "delete") {
        delEvent(rowKey); // 삭제 이벤트 처리
      }
    });

    this.el = el;
  }

  getElement() {
    return this.el;
  }
}
</script>

