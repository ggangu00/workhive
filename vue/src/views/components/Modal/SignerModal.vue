<template>
  <!-- 모달 컴포넌트 -->
  <Modal
    :isShowModal="props.isShowModal"
    :modalTitle="'사원 선택'"
    @click.self="modalClose"
  >
    <template v-slot:body>
      <div class="content">
        <div class="container-fluid">

          <!-- 등록폼 [S] -->
          <div class="card">
            <div class="card-body">

              <div class="signerGrid"></div>

            </div>
          </div>

        </div>
      </div>
    </template>
    <template v-slot:footer>
      <button class="btn btn-secondary btn-fill" @click="modalClose">닫기</button>
    </template>
  </Modal>
</template>


<script setup>
import Modal from '../../../components/Modal.vue';
import Grid from 'tui-grid';
import { ref, watch } from 'vue';

const props = defineProps({
  isShowModal: Boolean,
});

const token = localStorage.getItem("token");

let gridInstance = ref();

// 조회 조건
let params = {
  startDate: '',
  endDate: ''
};

// const signerGetList = () => {
//   gridInstance.value.readData(1, params);
// }
const dataSource = {
  api: {
    readData: { 
      url: '/api/sign', 
      method: 'GET', 
      initParams: params,
      headers: {'Authorization': `Bearer ${token}`},
    }
  }
};
// Grid 초기화
const initGrid = () => {
  const gridElement = document.querySelector('.signerGrid');
  if (!gridElement) {
    console.error("그리드 요소를 찾을 수 없습니다.");
    return;
  }

  gridInstance.value = new Grid({
    el: gridElement,
    data: dataSource,
    scrollX: false,
    scrollY: true,
    rowHeaders: ['rowNum'],
    pageOptions: {
      useClient: false,
      perPage: 5,
    },
    columns: [
      { header: '이름', name: 'mberNm' },
      { header: '직급', name: 'gradeNm' },
      { header: '전화번호', name: 'mbtlnum', align: 'center' },
      { header: '선택', name: 'action', align: 'center', renderer: BtnRenderer }
    ]
  });
};

// 그리드 버튼
class BtnRenderer {
  constructor(props) {
    const el = document.createElement("div");
    el.className = "btn-group";

    el.innerHTML = `
      <button class="btn btn-success btn-fill cell-btn-custom me-2" data-type="edit">선택</button>
    `;

    el.addEventListener("click", () => {
      // props.row가 없을 경우 grid에서 데이터 가져오기
      const rowKey = props.row?.rowKey ?? props.grid.getRow(props.rowKey)?.rowKey;

      if (rowKey === undefined) {
        console.error("BtnRenderer: rowKey를 가져올 수 없습니다.", props);
        return;
      }

      modalConfirm(rowKey);
    });

    this.el = el;
  }

  getElement() {
    return this.el;
  }
}

// 모달이 열릴 때 그리드 초기화
watch(() => props.isShowModal, (newVal) => {
  if(newVal) initGrid();
  else gridInstance.value.destroy();
});

const emit = defineEmits(['modalClose', 'modalConfirm']);
const modalClose = () => {
  emit('modalClose');
}
const modalConfirm = (rowKey) => {
  let selectedRowData = gridInstance.value.getRow(rowKey);
  emit('modalConfirm', selectedRowData);
}

</script>


<style>

</style>

