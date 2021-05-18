<%--
  Created by IntelliJ IDEA.
  User: shalika_w
  Date: 5/12/2021
  Time: 9:14 PM
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
                sAjaxSource: "${pageContext.servletContext.contextPath}/listSampleVerification.json",
                fnServerData: function (sSource, aoData, fnCallback) {
                    aoData.push(
                        {'name': 'csrf_token', 'value': token},
                        {'name': 'header', 'value': header},
                        {'name': 'receivedDate', 'value': $('#receivedDate').val()},
                        {'name': 'institutionCode', 'value': $('#institutionCode').val()},
                        {'name': 'referenceNo', 'value': $('#referenceNo').val()},
                        {'name': 'status', 'value': $('#status').val()}
                    );
                    $.ajax({
                        dataType: 'json',
                        type: 'POST',
                        url: "${pageContext.request.contextPath}/listSampleVerification.json",
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
                    $("#table ").css({"width": "100%"});
                },
                columnDefs: [
                    {
                        title: "Id",
                        targets: 0,
                        visible:false,
                        mDataProp: "id",
                        defaultContent: "--"
                    },
                    {
                        title: "Reference Number",
                        targets: 1,
                        mDataProp: "referenceNo",
                        defaultContent: "--"
                    },
                    {
                        title: "Institution Code",
                        targets: 2,
                        mDataProp: "institutionCode",
                        defaultContent: "--"
                    },
                    {
                        title: "Name",
                        targets: 3,
                        mDataProp: "name",
                        defaultContent: "--"
                    },
                    {
                        title: "Age",
                        targets: 4,
                        mDataProp: "age",
                        defaultContent: "--"
                    },
                    {
                        title: "Gender",
                        targets: 5,
                        mDataProp: "gender",
                        defaultContent: "--"
                    },
                    {
                        title: "Symptomatic",
                        targets: 6,
                        mDataProp: "symptomatic",
                        defaultContent: "--"
                    },
                    {
                        title: "Contact Type",
                        targets: 7,
                        mDataProp: "contactType",
                        defaultContent: "--"
                    },
                    {
                        title: "NIC",
                        targets: 8,
                        mDataProp: "nic",
                        defaultContent: "--"
                    },
                    {
                        title: "Address",
                        targets: 9,
                        mDataProp: "address",
                        defaultContent: "--"
                    },
                    {
                        title: "District",
                        targets: 10,
                        mDataProp: "residentDistrict",
                        defaultContent: "--"
                    },
                    {
                        title: "Contact Number",
                        targets: 11,
                        mDataProp: "contactNumber",
                        defaultContent: "--"
                    },
                    {
                        title: "Secondary Contact Number",
                        targets: 12,
                        mDataProp: "secondaryContactNumber",
                        defaultContent: "--"
                    },
                    {
                        title: "Received Date",
                        targets: 13,
                        mDataProp: "receivedDate",
                        defaultContent: "--"
                    },
                    {
                        title: "Status",
                        targets: 14,
                        mDataProp: "status",
                        defaultContent: "--",
                        render: function (data, type, full, meta) {
                            var status = {
                                'Active': {
                                    'title': 'Valid',
                                    'class': ' label-light-info'
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
                    },
                    {
                        title: "Created User",
                        targets: 15,
                        mDataProp: "createdUser",
                        defaultContent: "--"
                    },
                    {
                        label: 'Created Time',
                        name: 'createdTime',
                        targets: 16,
                        mDataProp: "createdTime",
                        render: function (data) {
                            return moment(data).format("YYYY-MM-DD hh:mm a")
                        }
                    },
                    {
                        title: "Last Updated Time",
                        targets: 17,
                        mDataProp: "lastUpdatedTime",
                        defaultContent: "--",
                        render: function (data) {
                            return moment(data).format("YYYY-MM-DD hh:mm a")
                        }
                    },
                    {
                        title: "Last Updated User",
                        targets: 18,
                        mDataProp: "lastUpdatedUser",
                        defaultContent: "--"
                    },

                ]
            });
        }



        function searchStart() {
            oTable.fnDraw();
        }

        function resetSearch() {
            $("#receivedDate").val("");
            $('#institutionCode').val("");
            $('#referenceNo').val("");
            $('#status').val("");

            oTable.fnDraw();
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
                    <h5 class="text-dark font-weight-bold my-1 mr-5">Sample Data Verification</h5>
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
                            <h3 class="card-title">Search sample data</h3>
                        </div>
                        <!--begin::Form-->
                        <form:form class="form" id="sampleverifyform" name="sampleverifyform" action=""
                                   theme="simple" method="post" modelAttribute="sampleverify">
                            <%--                        <form class="form">--%>
                            <div class="card-body">
                                <div class="form-group row">
                                    <div class="col-lg-3">
                                        <label>Received Date:</label>
                                        <input id="receivedDate" name="receivedDate" type="date"
                                               onkeyup="$(this).val($(this).val().replace(/[^a-zA-Z0-9 ]/g, ''))"
                                                class="form-control "
                                               placeholder="Received Date">
                                        <span class="form-text text-muted">Please enter Received Date</span>
                                    </div>
                                    <div class="col-lg-4">
                                        <label>Institution Code:</label>
                                        <select id="institutionCode" name="institutionCode" class="form-control">
                                            <option selected value="">Select Institution Code</option>
                                            <c:forEach items="${sampleverify.institutionList}" var="institution">
                                                <option value="${institution.institutionCode}">${institution.institutionName}</option>
                                            </c:forEach>
                                        </select>
                                    </div>

                                    <div class="col-lg-3">
                                        <label>Reference Number</label>
                                        <input id="referenceNo" name="referenceNo" type="text"
                                        onkeyup="$(this).val($(this).val().replace(/^[A-Za-z0-9\!\@\#\$\%\^\&\*\)\(+\=\._-]+$/g, ''))"
                                               maxlength="16" class="form-control "
                                               placeholder="Reference Number">
                                        <span class="form-text text-muted">Please enter reference Number</span>
                                    </div>
                                    <div class="col-lg-3">
                                        <label>Status:</label>
                                        <select id="status" name="status" class="form-control">
                                            <option selected value="">Select Status</option>
                                            <c:forEach items="${sampleverify.statusList}" var="status">
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
                        <h3 class="card-label">Sample Data Verification
                            <span class="d-block text-muted pt-2 font-size-sm">Sample Data list</span></h3>
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
                                <th>Id</th>
                                <th>Reference Number</th>
                                <th>Institution Code</th>
                                <th>Name</th>
                                <th>Age</th>
                                <th>Gender</th>
                                <th>Symptomatic</th>
                                <th>Contact Type</th>
                                <th>NIC</th>
                                <th>Address</th>
                                <th>District</th>
                                <th>Contact Number</th>
                                <th>Secondary Contact Number</th>
                                <th>Received Date</th>
                                <th>Status</th>
                                <th>Created User</th>
                                <th>Created Time</th>
                                <th>Last Updated Time</th>
                                <th>Last Updated User</th>
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
<jsp:include page="../common/delete-modal.jsp"/>
<!-- end include jsp files -->
</html>
