<template>
  <div class="content">
    <div class="container-fluid">
      <div class="row">
        <div class="col-12">
          <div class="card">
            <div class="card-header d-flex justify-content-between align-items-center">
              <h4 class="card-title">개인정보 설정</h4>
              <div class="button-group">
                <button class="btn btn-secondary btn-fill mr-1">초기화</button>
                <button class="btn btn-primary btn-fill mr-1">저장</button>
              </div>
            </div>
          </div>
        </div>
      </div>
      
      <div class="row">
        <div class="col-12">
          <div class="card">
            <div class="card-body">
              <div class="section">
                <div class="section-header">[ 소속정보 ]</div>
                <div class="row p-1">
                  <div class="col-md-3">
                    <label>소속부서 <i class="fa-solid fa-asterisk"></i></label>
                    <input type="text" v-model="department" class="form-control readonly" />
                  </div>
                  <div class="col-md-3">
                    <label>직급 <i class="fa-solid fa-asterisk"></i></label>
                    <input type="text" v-model="position" class="form-control readonly" />
                  </div>
                  <div class="col-md-3">
                    <label>직책</label>
                    <input type="text" v-model="role" class="readonly" />
                  </div>
                </div>
              </div>

              <div class="section">
                <div class="section-header">[ 기본정보 ]</div>
                <div class="row p-1">
                  <div class="col-md-3">
                    <label>아이디 <i class="fa-solid fa-asterisk"></i></label>
                    <input type="text" v-model="userId" disabled class="form-control readonly" />
                  </div>
                  <div class="col-md-3">
                    <label>사번 <i class="fa-solid fa-asterisk"></i></label>
                    <input type="text" v-model="employeeId" disabled class="form-control readonly" />
                  </div>
                  <div class="col-md-3">
                    <label>성명 <i class="fa-solid fa-asterisk"></i></label>
                    <input type="text" v-model="name" class="editable" />
                  </div>
                </div>
              </div>

              <div class="section">
                <div class="section-header">[ 연락처 ]</div>
                <div class="row p-1">
                  <div class="col-md-3">
                    <label>이메일 <i class="fa-solid fa-asterisk"></i></label>
                    <input type="text" v-model="email" class="form-control readonly" />
                  </div>
                  <div class="col-md-3">
                    <label>전화</label>
                    <input type="text" v-model="phone" class="form-control editable" />
                  </div>
                  <div class="col-md-3">
                    <label>휴대폰</label>
                    <input type="text" v-model="mobile" class="form-control editable" />
                  </div>
                </div>
              </div>

            </div>
          </div>
        </div>
      </div>
    </div>
  </div>

</template>

<script setup>
  import axios from "axios";
  import { onBeforeMount, ref } from 'vue';

  const department = ref("총무팀");
  const position = ref("부장");
  const role = ref("팀장");
  const userId = ref("ongsim");
  const employeeId = ref("2025001");
  const name = ref("Ongsim, Kim");
  const email = ref("ongsim@mail.com");
  const phone = ref("053-425-1234");
  const mobile = ref("010-1234-5678");
  const memberList = ref([]);

  onBeforeMount(() => {

      getMemberList(); // 자재 재고 조회
  });

  const getMemberList = async () => {
    try {
      const result = await axios.get('/api/member');
      memberList.value = result;
      console.log(result.data);
    } catch (err) {
      memberList.value = [];

      // Swal.fire({
      //   icon: "error",
      //   title: "API 요청 오류:",
      //   text: err.message || err
      // });
    }
  }
</script>

<style scoped>
  .section {
    margin-bottom: 70px;
  }

  .section-header {
    font-weight: bold;
    font-size: 16px;
    margin-bottom: 20px;
    border-bottom: 1px solid #374375;
    padding-bottom: 10px;
  }

  .form-group:last-child {
    margin-right: 0;
  }

  label {
    display: block;
    font-weight: bold;
    margin-bottom: 20px;
    color: #393939;
  }

  input {
    width: 100%;
    padding: 5px;
    border: none;
    border-bottom: 0.5px solid #858585;
    border-radius: 0;
  }

  .readonly {
    background: white;
    cursor: not-allowed;
    border-bottom: 0.5px solid #858585;
    padding: 8px;
  }

  .editable {
    background: #eee;
  }

  .button-group {
    display: flex;
    justify-content: center;
    align-items: center;
  }
</style>
