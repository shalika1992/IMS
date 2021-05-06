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
    <title>Title</title>
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
                                               maxlength="128" class="form-control "
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
                                        <label>User Role Code:</label>
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
                                        <button type="button" class="btn btn-primary mr-2 btn-sm" onclick="searchStart()">
                                            Search
                                        </button>
                                        <button type="reset" class="btn btn-secondary btn-sm" onclick="resetSearch()">Reset
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
                        <c:if test="${systemuser.vadd}">
                            <a href="#" onclick="openAddModal()" class="btn btn-sm btn-primary font-weight-bolder">
											<span class="svg-icon svg-icon-md">
												<!--begin::Svg Icon | path:assets/media/svg/icons/Design/Flatten.svg-->
												<svg xmlns="http://www.w3.org/2000/svg"
                                                     width="24px"
                                                     height="24px" viewBox="0 0 24 24" version="1.1">
													<g stroke="none" stroke-width="1" fill="none" fill-rule="evenodd">
														<rect x="0" y="0" width="24" height="24"/>
														<circle fill="#000000" cx="9" cy="15" r="6"/>
														<path d="M8.8012943,7.00241953 C9.83837775,5.20768121 11.7781543,4 14,4 C17.3137085,4 20,6.6862915 20,10 C20,12.2218457 18.7923188,14.1616223 16.9975805,15.1987057 C16.9991904,15.1326658 17,15.0664274 17,15 C17,10.581722 13.418278,7 9,7 C8.93357256,7 8.86733422,7.00080962 8.8012943,7.00241953 Z"
                                                              fill="#000000" opacity="0.3"/>
													</g>
												</svg>
                                                <!--end::Svg Icon-->
											</span>New Record</a>
                        </c:if>
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
                                <th>User Role Code</th>
                                <th>Email</th>
                                <th>Mobile Number</th>
                                <th>Last Logged Date</th>
                                <th>Status</th>
                                <th>Created Time</th>
                                <th>Last Updated Time</th>
                                <th>Last Updated User</th>
                                <th>Update</th>
                                <th>Delete</th>
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
</html>
