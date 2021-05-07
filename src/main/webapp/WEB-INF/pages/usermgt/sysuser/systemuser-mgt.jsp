<%--
  Created by IntelliJ IDEA.
  User: akila_s
  Date: 5/5/2021
  Time: 2:33 PM
  To change this template use File | Settings | File Templates.
--%>
<%@ taglib uri="http://tiles.apache.org/tags-tiles" prefix="tiles" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>

<html>
<head>
    <script type="text/javascript">
        var oTable;

        var token = $("meta[name='_csrf']").attr("content");
        var header = $("meta[name='_csrf_header']").attr("content");

        $(document).ready(function () {
            loadDataTable();
        });

        function loadDataTable() {
            var token = $("meta[name='_csrf']").attr("content");
            var header = $("meta[name='_csrf_header']").attr("content");
            var stringify_aoData = function (aoData) {
                var o = {};
                var modifiers = ['mDataProp_', 'sSearch_', 'iSortCol_', 'bSortable_', 'bRegex_', 'bSearchable_', 'sSortDir_'];
                jQuery.each(aoData, function (idx, obj) {
                    if (obj.name) {
                        for (var i = 0; i < modifiers.length; i++) {
                            if (obj.name.substring(0, modifiers[i].length) == modifiers[i]) {
                                var index = parseInt(obj.name.substring(modifiers[i].length));
                                var key = 'a' + modifiers[i].substring(0, modifiers[i].length - 1);
                                if (!o[key]) {
                                    o[key] = [];
                                }
                                o[key][index] = obj.value;
                                return;
                            }
                        }
                        o[obj.name] = obj.value;
                    } else {
                        o[idx] = obj;
                    }
                });
                return JSON.stringify(o);
            };

            oTable = $('#table').dataTable({
                bServerSide: true,
                sAjaxSource: "${pageContext.servletContext.contextPath}/listSystemUser.json",
                fnServerData: function (sSource, aoData, fnCallback) {
                    aoData.push(
                        {'name': 'csrf_token', 'value': token},
                        {'name': 'header', 'value': header},
                        {'name': 'userName', 'value': $('#userName').val()},
                        {'name': 'fullName', 'value': $('#fullName').val()},
                        {'name': 'mobileNumber', 'value': $('#mobileNumber').val()},
                        {'name': 'email', 'value': $('#email').val()},
                        {'name': 'userRoleCode', 'value': $('#userRoleCode').val()},
                        {'name': 'status', 'value': $('#status').val()}
                    );
                    $.ajax({
                        dataType: 'json',
                        type: 'POST',
                        url: "${pageContext.request.contextPath}/listSystemUser.json",
                        contentType: "application/json",
                        data: stringify_aoData(aoData),
                        success: fnCallback,
                        error: function (e) {
                            window.location = "${pageContext.request.contextPath}/logout.htm";
                        }
                    });
                },
                bJQueryUI: true,
                sPaginationType: "full_numbers",
                bDeferRender: true,
                responsive: true,
                lengthMenu: [5, 10, 20, 50, 100],
                searching: false,
                scrollY: '50vh',
                scrollX: true,
                scrollCollapse: true,
                initComplete: function (settings, json) {
                    document.getElementById('data-table-loading').style.display = "none";
                    document.getElementById('data-table-wrapper').style.display = "block";
                },
                fnDrawCallback: function (oSettings) {
                    $(".table ").css({"width": "100%"});
                },
                columnDefs: [
                    {
                        title: "User Name",
                        targets: 0,
                        mDataProp: "userName",
                        defaultContent: "--"
                    },
                    {
                        title: "Full Name",
                        targets: 1,
                        mDataProp: "fullName",
                        defaultContent: "--"
                    },
                    {
                        title: "User Role",
                        targets: 2,
                        mDataProp: "userRole",
                        defaultContent: "--"
                    },
                    {
                        title: "Email",
                        targets: 3,
                        mDataProp: "email",
                        defaultContent: "--"
                    },
                    {
                        title: "Mobile Number",
                        targets: 4,
                        mDataProp: "mobileNumber",
                        defaultContent: "--"
                    },
                    {
                        title: "Last Logged Date",
                        targets: 5,
                        mDataProp: "lastLoggedDate",
                        defaultContent: "--",
                        render: function (data) {
                            return moment(data).format("YYYY-MM-DD hh:mm a")
                        }
                    }
                    ,
                    {
                        title: "Status",
                        targets: 6,
                        mDataProp: "status",
                        defaultContent: "--",
                        render: function (data, type, full, meta) {
                            var status = {
                                'Active': {
                                    'title': 'Active',
                                    'class': ' label-light-info'
                                },
                                'Inactive': {
                                    'title': 'Inactive',
                                    'class': ' label-light-danger'
                                },
                                'New': {
                                    'title': 'New',
                                    'class': ' label-light-primary'
                                },
                                'Changed': {
                                    'title': 'Changed',
                                    'class': ' label-light-success'
                                },
                                'Reset': {
                                    'title': 'Reset',
                                    'class': ' label-light-warning'
                                },
                                'De-Active': {
                                    'title': 'De-Active',
                                    'class': ' label-light-danger'
                                },
                                'Pending': {
                                    'title': 'Pending',
                                    'class': 'label-light-warning'
                                }
                            };
                            if (typeof status[data] === 'undefined') {
                                return data;
                            }
                            return '<span class="label label-lg font-weight-bold' + status[data].class + ' label-inline">' + status[data].title + '</span>';
                        },
                    }
                    , {
                        label: 'Created Time',
                        name: 'createdTime',
                        targets: 7,
                        mDataProp: "createdTime",
                        render: function (data) {
                            return moment(data).format("YYYY-MM-DD hh:mm a")
                        }
                    },
                    {
                        title: "Last Updated Time",
                        targets: 8,
                        mDataProp: "lastUpdatedTime",
                        defaultContent: "--",
                        render: function (data) {
                            return moment(data).format("YYYY-MM-DD hh:mm a")
                        }
                    },
                    {
                        title: "Last Updated User",
                        targets: 9,
                        mDataProp: "lastUpdatedUser",
                        defaultContent: "--"
                    },
                    {
                        title: "Update",
                        sortable: false,
                        className: "dt-center",
                        mRender: function (data, type, full) {
                            return '<div><a href="javascript:;" class="btn btn-sm btn-clean btn-icon mr-2"  title="Update" id=' + full.userName + ' onclick="editSystemUser(\'' + full.userName + '\')"><span class="svg-icon svg-icon-md"><svg xmlns="http://www.w3.org/2000/svg" xmlns:xlink="http://www.w3.org/1999/xlink" width="24px" height="24px" viewBox="0 0 24 24" version="1.1"><g stroke="none" stroke-width="1" fill="none" fill-rule="evenodd"><rect x="0" y="0" width="24" height="24"/><path d="M8,17.9148182 L8,5.96685884 C8,5.56391781 8.16211443,5.17792052 8.44982609,4.89581508 L10.965708,2.42895648 C11.5426798,1.86322723 12.4640974,1.85620921 13.0496196,2.41308426 L15.5337377,4.77566479 C15.8314604,5.0588212 16,5.45170806 16,5.86258077 L16,17.9148182 C16,18.7432453 15.3284271,19.4148182 14.5,19.4148182 L9.5,19.4148182 C8.67157288,19.4148182 8,18.7432453 8,17.9148182 Z" fill="#000000" fill-rule="nonzero"\ transform="translate(12.000000, 10.707409) rotate(-135.000000) translate(-12.000000, -10.707409) "/><rect fill="#000000" opacity="0.3" x="5" y="20" width="15" height="2" rx="1"/></g></svg></span></a></div>';
                        },
                        targets: 10,
                        defaultContent: "--"
                    },
                    {
                        title: "Change password",
                        sortable: false,
                        className: "dt-center",
                        mRender: function (data, type, full) {
                            return '<div><a href="javascript:;" class="btn btn-sm btn-clean btn-icon mr-2"  title="Update" id=' + full.userName + ' onclick="changePwdSystemUser(\'' + full.userName + '\')"><span class="svg-icon svg-icon-md"><svg xmlns="http://www.w3.org/2000/svg" xmlns:xlink="http://www.w3.org/1999/xlink" width="24px" height="24px" viewBox="0 0 24 24" version="1.1"><g stroke="none" stroke-width="1" fill="none" fill-rule="evenodd"><rect x="0" y="0" width="24" height="24"/><path d="M8,17.9148182 L8,5.96685884 C8,5.56391781 8.16211443,5.17792052 8.44982609,4.89581508 L10.965708,2.42895648 C11.5426798,1.86322723 12.4640974,1.85620921 13.0496196,2.41308426 L15.5337377,4.77566479 C15.8314604,5.0588212 16,5.45170806 16,5.86258077 L16,17.9148182 C16,18.7432453 15.3284271,19.4148182 14.5,19.4148182 L9.5,19.4148182 C8.67157288,19.4148182 8,18.7432453 8,17.9148182 Z" fill="#000000" fill-rule="nonzero"\ transform="translate(12.000000, 10.707409) rotate(-135.000000) translate(-12.000000, -10.707409) "/><rect fill="#000000" opacity="0.3" x="5" y="20" width="15" height="2" rx="1"/></g></svg></span></a></div>';
                        },
                        targets: 11,
                        defaultContent: "--"
                    }
                ]
            });
        }

        function editSystemUser(username) {
            $.ajax({
                url: "${pageContext.request.contextPath}/getSystemUser.json",
                data: {
                    userName: username
                },
                dataType: "json",
                type: 'GET',
                contentType: "application/json",
                success: function (data) {
                    $('#responseMsgUpdate').hide();

                    $('#eUserName').val(data.userName);
                    $('#eUserName').attr('readOnly', true);
                    $('#eFullName').val(data.fullName);
                    $('#eEmail').val(data.email);
                    $('#eUserRoleCode').val(data.userRoleCode);
                    $('#eStatus').val(data.status);
                    $('#eMobileNumber').val(data.mobileNumber);

                    $('#modalUpdateSystemUser').modal('toggle');
                    $('#modalUpdateSystemUser').modal('show');
                },
                error: function (data) {
                    window.location = "${pageContext.request.contextPath}/logout.htm";
                }
            });
        }

        function changePwdSystemUser(username) {
            $.ajax({
                url: "${pageContext.request.contextPath}/getSystemUser.json",
                data: {
                    userName: username
                },
                dataType: "json",
                type: 'GET',
                contentType: "application/json",
                success: function (data) {
                    $('#responseMsgUpdate').hide();

                    $('#eUserName').val(data.userName);
                    $('#eUserName').attr('readOnly', true);
                    $('#eFullName').val(data.fullName);
                    $('#eEmail').val(data.email);
                    $('#eUserRoleCode').val(data.userRoleCode);
                    $('#eStatus').val(data.status);
                    $('#eMobileNumber').val(data.mobileNumber);

                    $('#modalUpdateSystemUser').modal('toggle');
                    $('#modalUpdateSystemUser').modal('show');
                },
                error: function (data) {
                    window.location = "${pageContext.request.contextPath}/logout.htm";
                }
            });
        }

        function searchStart() {
            oTable.fnDraw();
        }

        function resetSearch() {
            $('#userName').val("");
            $('#fullName').val("");
            $('#email').val("");
            $('#userRoleCode').val("");
            $('#status').val("");
            $('#mobileNumber').val("");

            oTable.fnDraw();
        }

        function openAddModal() {
            $('#modalAddSystemUser').modal('toggle');
            $('#modalAddSystemUser').modal('show');
        }
    </script>
</head>
<!--begin::Content-->
<div class="content d-flex flex-column flex-column-fluid" id="kt_content">
    <!--begin::Subheader-->
    <div class="subheader py-2 py-lg-6 subheader-solid" id="kt_subheader">
        <div class="container-fluid d-flex align-items-center justify-content-between flex-wrap flex-sm-nowrap">
            <!--begin::Info-->
            <div class="d-flex align-items-center flex-wrap mr-1">
                <!--begin::Page Heading-->
                <div class="d-flex align-items-baseline flex-wrap mr-5">
                    <!--begin::Page Title-->
                    <h5 class="text-dark font-weight-bold my-1 mr-5">System User Management</h5>
                    <!--end::Page Title-->
                </div>
                <!--end::Page Heading-->
            </div>
            <!--end::Info-->
        </div>
    </div>
    <!--end::Subheader-->
    <!--begin::Entry-->
    <div class="d-flex flex-column-fluid">
        <!--begin::Container-->
        <div class="container">
            <div class="row">
                <div class="col-lg-12">
                    <!--begin::Card-->
                    <div class="card card-custom gutter-b">
                        <div class="card-header">
                            <h3 class="card-title">Search System User</h3>
                        </div>
                        <!--begin::Form-->
                        <form:form class="form" id="systemuserform" name="systemuserform" action="addSystemUser"
                                   theme="simple" method="post" modelAttribute="systemuser">
                            <%--                        <form class="form">--%>
                            <div class="card-body">
                                <div class="form-group row">
                                    <div class="col-lg-3">
                                        <label>Username:</label>
                                        <div class="input-group">
                                            <div class="input-group-prepend">
																	<span class="input-group-text">
																		<i class="la la-bookmark-o"></i>
																	</span>
                                            </div>
                                            <input id="userName" name="userName" type="text"
                                                   onkeyup="$(this).val($(this).val().replace(/[^a-zA-Z0-9 ]/g, ''))"
                                                   maxlength="16" class="form-control"
                                                   placeholder="Username">
                                        </div>
                                        <span class="form-text text-muted">Please enter username</span>
                                    </div>
                                    <div class="col-lg-3">
                                        <label>Full Name:</label>
                                        <input id="fullName" name="fullName" type="text"
                                               onkeyup="$(this).val($(this).val().replace(/[^a-zA-Z0-9 ]/g, ''))"
                                               maxlength="512" class="form-control "
                                               placeholder="Full Name">
                                        <span class="form-text text-muted">Please enter full name</span>
                                    </div>
                                    <div class="col-lg-3">
                                        <label>Email:</label>
                                        <input id="email" name="email" type="text" onkeyup="" maxlength="128"
                                               class="form-control" placeholder="Email">
                                        <span class="form-text text-muted">Please enter email</span>
                                    </div>
                                    <div class="col-lg-3">
                                        <label>Mobile Number:</label>
                                        <input id="mobileNumber" name="mobileNumber" type="text"
                                               onkeyup="$(this).val($(this).val().replace(/[^\d]/ig, ''))"
                                               maxlength="10"
                                               class="form-control form-control-sm" placeholder="Mobile Number">

                                        <span class="form-text text-muted">Please enter mobile number</span>
                                    </div>
                                    <div class="col-lg-3">
                                        <label>User Role:</label>
                                        <select id="userRoleCode" name="userRoleCode"
                                                class="form-control">
                                            <option selected value="">Select User Role Code</option>
                                            <c:forEach items="${systemuser.userRoleList}" var="userRole">
                                                <option value="${userRole.userroleCode}">${userRole.description}</option>
                                            </c:forEach>
                                        </select>
                                        <span class="form-text text-muted">Please select user role code</span>
                                    </div>
                                    <div class="col-lg-3">
                                        <label>Status:</label>
                                        <select id="status" name="status" class="form-control">
                                            <option selected value="">Select Status</option>
                                            <c:forEach items="${systemuser.statusList}" var="status">
                                                <option value="${status.statusCode}">${status.description}</option>
                                            </c:forEach>
                                        </select>
                                        <span class="form-text text-muted">Please select status</span>
                                    </div>
                                </div>
                            </div>
                            <div class="card-footer">
                                <div class="row">
                                    <div class="col-lg-6">
                                        <button type="button" class="btn btn-primary mr-2 btn-sm"
                                                onclick="searchStart()">
                                            Search
                                        </button>
                                        <button type="reset" class="btn btn-secondary btn-sm" onclick="resetSearch()">
                                            Reset
                                        </button>
                                    </div>
                                </div>

                                <!--end::Form-->
                            </div>
                        </form:form>
                        <!--end::Card-->
                    </div>
                </div>
            </div>
            <!--begin::Card-->
            <div class="card card-custom gutter-b">
                <div class="card-header flex-wrap border-0 pt-6 pb-0">
                    <div class="card-title">
                        <h3 class="card-label">System User Management
                            <span class="d-block text-muted pt-2 font-size-sm">System User list</span></h3>
                    </div>
                    <div class="card-toolbar">
                        <!--begin::Button-->
                        <a href="#" onclick="openAddModal()" class="btn btn-sm btn-primary font-weight-bolder">
											<span class="svg-icon svg-icon-md">
												<!--begin::Svg Icon | path:assets/media/svg/icons/Design/Flatten.svg-->
												<svg xmlns="http://www.w3.org/2000/svg"
                                                     width="24px"
                                                     height="24px" viewBox="0 0 24 24" version="1.1">
													<g stroke="none" stroke-width="1" fill="none" fill-rule="evenodd">
														<rect x="0" y="0" width="24" height="24"></rect>
														<circle fill="#000000" cx="9" cy="15" r="6"></circle>
														<path d="M8.8012943,7.00241953 C9.83837775,5.20768121 11.7781543,4 14,4 C17.3137085,4 20,6.6862915 20,10 C20,12.2218457 18.7923188,14.1616223 16.9975805,15.1987057 C16.9991904,15.1326658 17,15.0664274 17,15 C17,10.581722 13.418278,7 9,7 C8.93357256,7 8.86733422,7.00080962 8.8012943,7.00241953 Z"
                                                              fill="#000000" opacity="0.3"></path>
													</g>
												</svg>
                                                <!--end::Svg Icon-->
											</span>New Record</a>
                        <!--end::Button-->
                    </div>
                </div>
                <div class="card-body">
                    <!--begin: Datatable-->
                    <div id="data-table-loading" style="display: block;">
                        <div class="loader"></div>
                        <div class="loading-text">Loading..</div>
                    </div>
                    <div id="data-table-wrapper" style="display: none;">
                        <table class="table table-separate table-head-custom table-checkable" id="table">
                            <thead>
                            <tr>
                                <th>User Name</th>
                                <th>Full Name</th>
                                <th>User Role</th>
                                <th>Email</th>
                                <th>Mobile Number</th>
                                <th>Last Logged Date</th>
                                <th>Status</th>
                                <th>Created Time</th>
                                <th>Last Updated Time</th>
                                <th>Last Updated User</th>
                                <th>Update</th>
                                <th>Change Password</th>
                            </tr>
                            </thead>
                            <tbody></tbody>
                        </table>
                        <!--end: Datatable-->
                    </div>
                </div>
            </div>
            <!--end::Card-->

        </div>
    </div>
</div>
<!-- start include jsp files -->
<jsp:include page="systemuser-mgt-add.jsp"/>
<jsp:include page="systemuser-mgt-update.jsp"/>
<!-- end include jsp files -->
</html>
