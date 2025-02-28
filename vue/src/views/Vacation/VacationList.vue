<template>

  <!-- 휴가 신청 등록/수정 -->
  <div class="row mt-5">
    <div class="col">
        
      <div class="row">
        <div class="col header">
          <h4>  [ 휴가 신청 목록 ]</h4>
          <hr>
        </div>
      </div>

      <!-- 조회 조건 -->
      <div class="row search align-items-center justify-content-end">
        <div class="col-auto">
          <select class="input-custom" v-model="searchData.vcType">
            <option value="" selected>휴가종류</option>
            <option value="E01">연차</option>
            <option value="E02">오전반차</option>
            <option value="E03">오후반차</option>
            <option value="E04">공가</option>
          </select>
        </div>

        <div class="col-auto">
          <select class="input-custom" v-model="searchData.signState">
            <option value="" selected>결재상태</option>
            <option value="D01">대기</option>
            <option value="D02">승인</option>
            <option value="D03">보완</option>
            <option value="D04">반려</option>
          </select>
        </div>

        <div class="col-auto">
          <input type="date" id="startDate" class="input-custom" v-model="searchData.startDate">
          <a class="align-middle"> ~ </a>
          <input type="date" id="endDate" class="input-custom" v-model="searchData.endDate">
        </div>
      </div>

      <!-- 휴가 신청 목록 -->
      <div class="row mt-3">
        <div class="col" style="height: 300px; width: 800px;">
          <div id="vcGrid"></div>
        </div>
      </div>

    </div>
  </div>
  
</template>

<script setup>
import { ref, onMounted, onBeforeUnmount, watch } from 'vue';
// import { dateTimeFormat } from '../../assets/js/common.js';
import Grid from 'tui-grid';
import axios from 'axios';
import { useRouter } from 'vue-router';
import { useUserInfoStore } from '../../store/userStore.js';

const userInfoStore = useUserInfoStore();
let loginUser = userInfoStore.user.mberId;
console.log("로그인 정보 : ", loginUser);

let gridInstance = ref();
// let rowData = ref([]);

// 검색 데이터
const searchData = ref({
  createId: loginUser,
  vcType: '',
  signState: '',
  startDate: '',
  endDate: '',
});

const vcGetList = () => {
  gridInstance.value.readData(1, searchData.value);
}
const dataSource = {
  api: {
    readData: { url: '/api/vacation/vcList', method: 'GET', initParams: searchData.value}
  }
};


// 휴가 신청 목록 조회
// const vcGetList = async () => {
//   const result = await axios.get('/api/vacation/vcList', { params : searchData.value });
//   rowData.value = result.data;

//   rowData.value.forEach((i) => {
//     i.vcStartDt = dateTimeFormat(i.vcStartDt, 'yyyy-MM-dd');
//     i.vcEndDt = dateTimeFormat(i.vcEndDt, 'yyyy-MM-dd');
//     i.createDt = dateTimeFormat(i.createDt, 'yyyy-MM-dd');
//   })

//   gridInstance.value.resetData(rowData.value);
// }

// 조회 조건 변경 감지
watch(() => searchData, () => {
  vcGetList();
}, {deep:true});

// Toast UI Grid 초기화
onMounted(() => {
  gridInstance.value = new Grid({
    el: document.getElementById('vcGrid'),
    data: dataSource,
    scrollX: false,
    scrollY: true,
    pageOptions: {
      useClient: false,
      perPage: 5,
    },
    columns: [ // 시작일 / 종료일 / 휴가종류 / 사용일수 / 신청일 / 결재자 / 결재상태
      { header: '시작일', name: 'vcStartDt', align: 'center'},
      { header: '종료일', name: 'vcEndDt', align: 'center'},
      { header: '휴가종류', name: 'vcType', align: 'center',
        formatter: ({ value }) => {
          let type = '';
          if(value === 'E01') type = '연차';
          else if(value === 'E02') type = '오전반차';
          else if(value === 'E03') type = '오후반차';
          else if(value === 'E04') type = '공가';
          return type;
        }
      },
      { header: '사용일수', name: 'useDays', align: 'center',
        formatter: ({ value }) => { // 소숫점 숫자 표시
          const num = Number(value);
          return num % 1 === 0 ? num : num.toFixed(1);
        }
      },
      { header: '신청일', name: 'createDt', align: 'center'},
      { header: '결재자', name: 'signId', align: 'center'},
      { header: '결재상태', name: 'signState', align: 'center', renderer: BtnRenderer }
    ]
  })
  
  // vcGetList();
})

// 그리드 버튼
class BtnRenderer {
  constructor(props) {
    const el = document.createElement("div");
    el.className = "btn-group";

    const signState = props.grid.getRow(props.rowKey).signState;

    if (signState === "D01") {
      el.innerHTML = `
        <button class="btn btn-success btn-fill cell-btn-custom me-2" data-type="wait">대기</button>
        <button class="btn btn-danger btn-fill cell-btn-custom" data-type="delete">삭제</button>
      `;
    } else if (signState === "D02") {
      el.innerHTML = `<span class="text-success fw-bold">승인</span>`;
    } else if (signState === "D03") {
      el.innerHTML = `
        <button class="btn btn-warning btn-fill cell-btn-custom me-2" data-type="edit">보완</button>
        <button class="btn btn-danger btn-fill cell-btn-custom" data-type="delete">삭제</button>
      `;
    } else if (signState === "D04") {
      el.innerHTML = `<span">반려</span>`;
    }

    el.addEventListener("click", (event) => {
      const type = event.target.dataset.type;

      // props.row가 없을 경우 grid에서 데이터 가져오기
      const rowKey = props.row?.rowKey ?? props.grid.getRow(props.rowKey)?.rowKey;

      if (rowKey === undefined) {
        console.error("BtnRenderer: rowKey를 가져올 수 없습니다.", props);
        return;
      }

      if (type === "wait") {
        btnVcUpdate(rowKey);
      } else if (type === "edit") {
        btnVcUpdate(rowKey);
      } else if (type === "delete") {
        btnVcDelete(rowKey);
      }
    });

    this.el = el;
  }

  getElement() {
    return this.el;
  }
}


const router = useRouter();
const btnVcUpdate = (rowKey) => {
  let selectedRowData = gridInstance.value.getRow(rowKey);

  router.push({ name: 'VcManage', query: { vcCd: selectedRowData.vcCd, isUpdate: true } });
};
const btnVcDelete = async (rowKey) => {
  let vcCd = gridInstance.value.getRow(rowKey).vcCd;

  await axios.post(`/api/vacation/vcRemove?vcCd=${vcCd}`);
  vcGetList();
};

// 페이지 이동 시 Grid 제거하여 중복 방지
onBeforeUnmount(() => {
  if (gridInstance.value) {
    gridInstance.value.destroy();
    gridInstance.value = null;
  }
});
</script>

<style scoped>

</style>
