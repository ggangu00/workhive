<template>
  <div class="content">
    <div class="container-fluid">
      <card>
        <div class="mb-3 row">
          <label for="staticEmail" class="col-sm-2 col-form-label">검색</label>
          <div class="col-auto">
            <select class="form-select" aria-label="Default select example">
              <option value="1">아이디</option>
              <option value="2">아이피</option>
            </select>
          </div>
          <div class="col-auto">
            <input type="text" class="form-control" placeholder="검색어를 입력해주세요">
          </div>
        </div>
        <div class="mb-3 row">
          <label for="inputPassword" class="col-sm-2 col-form-label">조회 기간</label>
          <div class="col-auto">
            <input type="date" name="START_DT" class="form-control">
          </div>
          <div class="col-auto">~</div>
          <div class="col-auto">
            <input type="date" name="END_DT" class="form-control">
          </div>
        </div>
        <div class="text-center">
          <button type="submit" class="btn btn-secondary btn-fill">
            초기화
          </button>
          <button class="btn btn-info btn-fill">
            검색
          </button>
        </div>
      </card>
      <card>
        <div class="table-responsive">
          <table class="table table-hover project">
            <thead>
              <tr>
                <th>번호</th>
                <th>로그인 아이디</th>
                <th>로그인 아이피</th>
                <th>성공여부</th>
                <th>상태변경</th>
                <th>로그인 일시</th>
              </tr>
            </thead>
            <tbody>
              <tr>
                <td>1</td>
                <td>testuser</td>
                <td>58.238.119.35</td>
                <td>실패</td>
                <td><button class="btn btn-danger btn-sm">계정잠금</button></td>
                <td>2024-12-01 10:22:58</td>
              </tr>
              <tr>
                <td>2</td>
                <td>admin1</td>
                <td>58.238.119.35</td>
                <td>실패</td>
                <td><button class="btn btn-primary btn-sm">정상</button></td>
                <td>2024-12-01 10:22:58</td>
              </tr>
              <tr>
                <td>3</td>
                <td>admin1</td>
                <td>58.238.119.35</td>
                <td>실패</td>
                <td><button class="btn btn-primary btn-sm">정상</button></td>
                <td>2024-12-01 10:22:58</td>
              </tr>
              <tr>
                <td>3</td>
                <td>admin1</td>
                <td>58.238.119.35</td>
                <td>실패</td>
                <td><button class="btn btn-primary btn-sm">정상</button></td>
                <td>2024-12-01 10:22:58</td>
              </tr>
            </tbody>
          </table>
        </div>
      </card>
    </div>
  </div>
</template>

<script>
import Card from '@/components/Cards/Card.vue'

export default {
  components: {
    Card
  },
  data() {
    return {
      user: {
        company: 'Light dashboard',
        username: 'michael23',
        email: '',
        firstName: 'Mike',
        lastName: 'Andrew',
        address: 'Melbourne, Australia',
        city: 'melbourne',
        country: 'Australia',
        postalCode: '',
        aboutMe: `Lamborghini Mercy, Your chick she so thirsty, I'm in that two seat Lambo.`
      }
    }
  },
  methods: {
    updateProfile() {
      alert('Your data: ' + JSON.stringify(this.user))
    }
  }
}

</script>
<style></style>