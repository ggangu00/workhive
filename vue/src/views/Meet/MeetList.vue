<template>
    <div class="content">
        <div class="container-fluid">
            <div class="card">
                <div class="card-body">
                    <h4 class="card-title float-left mt-1">회의 조회</h4>
                    <button class="btn btn-primary btn-sm btn-fill float-right" onclick="location.href ='/meet/add'">회의
                        등록</button>
                </div>
            </div>
            <card>
                <h5>금일 예정 회의 (3건)</h5>
                <div class="table-responsive">
                    <table class="table table-hover project">
                        <thead>
                            <tr>
                                <th>번호</th>
                                <th>구분</th>
                                <th>회의주제</th>
                                <th>회의일시</th>
                                <th>회의실</th>
                                <th>참여자</th>
                                <th>관리</th>
                            </tr>
                        </thead>
                        <tbody>
                            <tr>
                                <td>1</td>
                                <td>내부</td>
                                <td>
                                    <div class="subject">세미나 일정관련 회의</div>
                                </td>
                                <td>2025-02-04(화) 13:00 ~ 13:30</td>
                                <td>회의실A</td>
                                <td>김민진, 신강현, 박주현 외 3명</td>
                                <td>
                                    <button class="btn btn-success btn-fill btn-sm mr-1">수정</button>
                                    <button class="btn btn-danger btn-fill btn-sm mr-1">삭제</button>
                                </td>
                            </tr>
                            <tr>
                                <td>2</td>
                                <td>프로젝트</td>
                                <td>
                                    <div class="subject">'빵돌2 빵순2 MES 시스템' 현황 보고</div>
                                </td>
                                <td>2025-02-04(화) 13:00 ~ 13:30</td>
                                <td>회의실A</td>
                                <td>김민진, 신강현</td>
                                <td>
                                    <button class="btn btn-success btn-fill btn-sm mr-1">수정</button>
                                    <button class="btn btn-danger btn-fill btn-sm mr-1">삭제</button>
                                </td>
                            </tr>
                            <tr>
                                <td>2</td>
                                <td>프로젝트</td>
                                <td>
                                    <div class="subject">'빵돌2 빵순2 MES 시스템' 현황 보고</div>
                                </td>
                                <td>2025-02-04(화) 13:00 ~ 13:30</td>
                                <td>회의실A</td>
                                <td>김민진, 신강현</td>
                                <td>
                                    <button class="btn btn-success btn-fill btn-sm mr-1">수정</button>
                                    <button class="btn btn-danger btn-fill btn-sm mr-1">삭제</button>
                                </td>
                            </tr>
                        </tbody>
                    </table>
                </div>
            </card>
            <card>
                <div class="d-flex">
                    <div class="p-2 w80">
                        <h5>전체 회의 (30건)</h5>
                    </div>
                    <div class="col-auto">
                        <input type="text" class="form-control form-control-sm" placeholder="제목을 입력해주세요">
                    </div>
                    <div class="col-auto">
                        <button class="btn btn-info btn-fill btn-sm">검색</button>
                    </div>
                </div>

                <div class="table-responsive">
                    <table class="table table-hover project">
                        <thead>
                            <tr>
                                <th>번호</th>
                                <th>상태</th>
                                <th>구분</th>
                                <th>회의주제</th>
                                <th>회의일시</th>
                                <th>회의실</th>
                                <th>참여자</th>
                                <th>관리</th>
                            </tr>
                        </thead>
                        <tbody>
                            <tr>
                                <td>1</td>
                                <td>진행예정</td>
                                <td>내부</td>
                                <td>
                                    <div class="subject">세미나 일정관련 회의</div>
                                </td>
                                <td>2025-02-04(화) 13:00 ~ 13:30</td>
                                <td>회의실A</td>
                                <td>김민진, 신강현, 박주현 외 3명</td>
                                <td>
                                    <button class="btn btn-success btn-fill btn-sm mr-1">수정</button>
                                    <button class="btn btn-danger btn-fill btn-sm mr-1">삭제</button>
                                </td>
                            </tr>
                            <tr>
                                <td>2</td>
                                <td>진행예정</td>
                                <td>프로젝트</td>
                                <td>
                                    <div class="subject">'빵돌2 빵순2 MES 시스템' 현황 보고</div>
                                </td>
                                <td>2025-02-04(화) 13:00 ~ 13:30</td>
                                <td>회의실A</td>
                                <td>김민진, 신강현</td>
                                <td>
                                    <button class="btn btn-success btn-fill btn-sm mr-1">수정</button>
                                    <button class="btn btn-danger btn-fill btn-sm mr-1">삭제</button>
                                </td>
                            </tr>
                            <tr class="table-secondary">
                                <td>2</td>
                                <td>완료</td>
                                <td>프로젝트</td>
                                <td>
                                    <div class="subject">'빵돌2 빵순2 MES 시스템' 현황 보고</div>
                                </td>
                                <td>2025-02-04(화) 13:00 ~ 13:30</td>
                                <td>회의실A</td>
                                <td>김민진, 신강현</td>
                                <td>
                                    <button class="btn btn-success btn-fill btn-sm mr-1">수정</button>
                                    <button class="btn btn-danger btn-fill btn-sm mr-1">삭제</button>
                                </td>
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