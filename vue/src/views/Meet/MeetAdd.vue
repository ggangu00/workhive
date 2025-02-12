<template>
    <div class="content">
        <div class="container-fluid">
            <div class="card">
                <div class="card-body">
                    <h4 class="card-title float-left mt-1">회의 등록</h4>
                    <button class="btn btn-primary btn-sm btn-fill float-right"
                        onclick="location.href ='#/admin/meet/add'">등록</button>
                    <button class="btn btn-secondary btn-sm btn-fill float-right">초기화</button>
                </div>
            </div>
            <form action="BoardAdd" method="post">
                <div class="card">
                    <div class="card-header">
                        <h5 class="card-title">1. 회의 정보</h5>
                    </div>
                    <div class="card-body">
                        <div class="mb-3">
                            <div class="form-group has-label">
                                <label>회의주제 <em class="point-red">*</em></label>
                            </div>
                            <input type="text" name="project_nm" class="form-control" placeholder="프로젝트명을 입력해주세요">
                        </div>
                        <div class="mb-3">
                            <label class="form-label">회의일시 <em class="point-red">*</em></label>
                            <div class="row">
                                <div class="col-auto">
                                    <input type="date" name="start_dt" class="form-control">
                                </div>
                                <div class="col-auto">
                                    <input type="time" name="start_tm" class="form-control">
                                </div>
                                <div class="col-auto">~</div>
                                <div class="col-auto">
                                    <input type="time" name="end_tm" class="form-control">
                                </div>
                            </div>
                        </div>
                        <div class="mb-3">
                            <label class="form-label">회의실 구분 <em class="point-red">*</em></label>
                            <select class="form-select w30" aria-label="Default select example">
                                <option value="A03">회의실A</option>
                                <option value="A04">회의실B</option>
                                <option value="A04">회의실C</option>
                            </select>
                        </div>
                        <div class="mb-3">
                            <label class="form-label">회의 구분 <em class="point-red">*</em></label>
                            <select class="form-select w30" aria-label="Default select example">
                                <option value="A03">내부</option>
                                <option value="A04">외부</option>
                                <option value="A04">프로젝트</option>
                                <option value="A04">기타</option>
                            </select>
                        </div>
                    </div>
                </div>

                <div class="card">
                    <div class="card-header">
                        <h5 class="card-title">2. 회의 안건 내용</h5>
                    </div>
                    <div class="card-body">
                        <div class="mb-3">
                            <div class="form-group has-label">
                                <label>회의 안건 내용 <em class="point-red">*</em></label>
                            </div>
                            <textarea type="text" name="project_nm" class="form-control"
                                placeholder="회의안건내용을 입력해주세요"></textarea>
                        </div>
                    </div>
                </div>

                <div class="card">
                    <div class="card-header">
                        <h5 class="card-title">3. 회의 결과 내용</h5>
                    </div>
                    <div class="card-body">
                        <div class="mb-3">
                            <div class="form-group has-label">
                                <label>회의 결과 내용 <em class="point-red">*</em></label>
                            </div>
                            <textarea type="text" name="project_nm" class="form-control"
                                placeholder="회의결과내용을 입력해주세요"></textarea>
                        </div>
                    </div>
                </div>
            </form>
        </div>
    </div>
</template>
