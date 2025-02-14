<template>
    <div class="content">
        <div class="container-fluid">
            <div class="card">
                <div class="card-body">
                    <h4 class="card-title float-left">프로젝트 참여자관리</h4>
                </div>
            </div>

            <div class="row m-0">
                <div class="col-7">
                    <div class="card">
                        <div class="card-body">
                            <div class="row">
                                <!-- 트리 뷰 (왼쪽) -->
                                <div class="col-3 treeview">
                                    <!-- 조직 트리 -->
                                    <div class="border-end">
                                        <ul class="list-unstyled">
                                            <li>📂 개발팀 (137)</li>
                                            <li class="ms-3">• 개발 1팀 (10)</li>
                                            <li class="ms-3">• 개발 2팀 (12)</li>
                                            <li class="ms-3">• 개발 3팀 (15)</li>
                                            <li>📂 디자인팀 (13)</li>
                                            <li class="ms-3">• 디자인 1팀 (5)</li>
                                            <li class="ms-3">• 디자인 2팀 (6)</li>
                                            <li class="ms-3">• 디자인 3팀 (2)</li>
                                            <li>📂 인사팀 (6)</li>
                                            <li>📂 기획팀 (9)</li>
                                        </ul>
                                    </div>
                                </div>

                                <!-- 구성원 테이블 (오른쪽) -->
                                <div class="col-9 personal-group">
                                    <table class="table personal-table">
                                        <thead class="table-light">
                                            <tr>
                                                <th>
                                                    <div class="form-check">
                                                        <input class="form-check-input" type="checkbox" value="option1">
                                                    </div>
                                                </th>
                                                <th>이름</th>
                                                <th>직급</th>
                                                <th>진행중인 프로젝트</th>
                                            </tr>
                                        </thead>

                                        <tbody>
                                            <tr>
                                                <td>
                                                    <div class="form-check">
                                                        <input class="form-check-input" type="checkbox"
                                                            id="inlineCheckbox1" value="option1">
                                                    </div>
                                                </td>
                                                <td class="profile-cell">
                                                    <img src="../../assets/img/vue-logo.png" alt="프로필 이미지"
                                                        class="profile-img">
                                                    <div class="profile-text">
                                                        <span class="team-label">개발팀</span>
                                                        <span class="user-name">박주현(20181716)</span>
                                                    </div>
                                                </td>
                                                <td>팀장</td>
                                                <td>3건</td>
                                            </tr>
                                        </tbody>
                                    </table>

                                </div>
                            </div>
                        </div>
                    </div>
                </div>

                <div class="col-5">
                    <div class="card">
                        <div class="card-body">
                            <div class="treeview">
                                <!-- 조직 트리 -->
                                <div class="border-end">
                                    <ul class="list-unstyled">
                                        <li>📂 개발팀 (137)</li>
                                        <li class="ms-3">• 개발 1팀 (10)</li>
                                        <li class="ms-3">• 개발 2팀 (12)</li>
                                        <li class="ms-3">• 개발 3팀 (15)</li>
                                        <li>📂 디자인팀 (13)</li>
                                        <li class="ms-3">• 디자인 1팀 (5)</li>
                                        <li class="ms-3">• 디자인 2팀 (6)</li>
                                        <li class="ms-3">• 디자인 3팀 (2)</li>
                                        <li>📂 인사팀 (6)</li>
                                        <li>📂 기획팀 (9)</li>
                                    </ul>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </div>
</template>