<%--
  Created by IntelliJ IDEA.
  User: akila
  Date: 5/14/2021
  Time: 9:47 AM
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
            $('#receivedDate').datepicker({
                format: 'yyyy-mm-dd',
                endDate: '+0d',
                setDate: new Date(),
                todayHighlight: true,
                forceParse: false,
                autoclose: true
            });
            setReceivedDate();
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
                sAjaxSource: "${pageContext.servletContext.contextPath}/listRejectedSample.json",
                fnServerData: function (sSource, aoData, fnCallback) {
                    aoData.push(
                        {'name': 'csrf_token', 'value': token},
                        {'name': 'header', 'value': header},
                        {'name': 'receivedDate', 'value': $('#receivedDate').val()},
                        {'name': 'referenceNo', 'value': $('#referenceNo').val()},
                        {'name': 'institutionCode', 'value': $('#institutionCode').val()},
                        {'name': 'name', 'value': $('#name').val()},
                        {'name': 'nic', 'value': $('#nic').val()}
                    );
                    $.ajax({
                        dataType: 'json',
                        type: 'POST',
                        url: "${pageContext.request.contextPath}/listRejectedSample.json",
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
                lengthMenu: [25, 50, 75, 100, 200],
                searching: false,
                scrollCollapse: true,
                initComplete: function (settings, json) {
                    document.getElementById('data-table-loading').style.display = "none";
                    document.getElementById('data-table-wrapper').style.display = "block";
                },
                fnDrawCallback: function (oSettings) {
                    $(".table ").css({"width": "100%"});
                },
                stateSave: false,
                columnDefs: [
                    {
                        title: "Reference No",
                        targets: 0,
                        mDataProp: "referenceNo",
                        defaultContent: "--"
                    },
                    {
                        title: "Institution Code",
                        targets: 1,
                        mDataProp: "institutionCode",
                        defaultContent: "--"
                    },
                    {
                        title: "Name",
                        targets: 2,
                        mDataProp: "name",
                        defaultContent: "--"
                    },
                    {
                        title: "Age",
                        targets: 3,
                        mDataProp: "age",
                        defaultContent: "--"
                    },
                    {
                        title: "Gender",
                        targets: 4,
                        mDataProp: "gender",
                        defaultContent: "--",
                        render: function (data) {
                            if (data === 'M') {
                                return 'Male';
                            } else {
                                return 'Female';
                            }
                        }
                    },
                    {
                        title: "Symptomatic",
                        targets: 5,
                        mDataProp: "symptomatic",
                        defaultContent: "--"
                    },
                    {
                        title: "Contact Type",
                        targets: 6,
                        mDataProp: "contactType",
                        defaultContent: "--"
                    },
                    {
                        title: "NIC",
                        targets: 7,
                        mDataProp: "nic",
                        defaultContent: "--"
                    },
                    {
                        title: "Address",
                        targets: 8,
                        mDataProp: "address",
                        defaultContent: "--"
                    },
                    {
                        title: "District",
                        targets: 9,
                        mDataProp: "district",
                        defaultContent: "--"
                    },
                    {
                        title: "Contact No",
                        targets: 10,
                        mDataProp: "contactNo",
                        defaultContent: "--"
                    },
                    {
                        title: "Secondary Contact Number",
                        targets: 11,
                        mDataProp: "secondaryContactNo",
                        defaultContent: "--"
                    },
                    {
                        title: "Received Date",
                        targets: 12,
                        mDataProp: "receivedDate",
                        defaultContent: "--",
                        render: function (data) {
                            return moment(data).format("YYYY-MM-DD")
                        }
                    },
                    {
                        title: "Status",
                        targets: 13,
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
                    },
                    {
                        title: "Remark",
                        targets: 14,
                        mDataProp: "remark",
                        defaultContent: "--"
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
                            return moment(data).format("YYYY-MM-DD HH:mm:ss A")
                        }
                    }
                ],
                fnDrawCallback: function (oSettings, json) {
                    //disable and enable the select_all input
                    if (oTable.fnGetData().length > 0) {
                        $("#viewPDF").prop("disabled", false);
                    } else {
                        $("#viewPDF").prop("disabled", true);
                    }
                }
            });
        }

        function searchStart() {
            oTable.fnDraw();
        }

        function resetSearch() {
            $("#receivedDate").datepicker('setDate', getReceivedDate());
            $('#referenceNo').val("");
            $('#institutionCode').val("");
            $('#name').val("");
            $('#nic').val("");

            oTable.fnDraw();
        }

        function setReceivedDate() {
            var date = new Date();
            var month = date.getMonth() + 1;
            var day = date.getDate();
            if (day < 10) {
                day = '0' + day;
            }
            if (month < 10) {
                month = '0' + month;
            }
            var today = (date.getFullYear() + "-" + month + "-" + day);
            $('#receivedDate').val(today);
        }

        function getReceivedDate() {
            var date = new Date();
            var month = date.getMonth() + 1;
            var day = date.getDate();
            if (day < 10) {
                day = '0' + day;
            }
            if (month < 10) {
                month = '0' + month;
            }
            return (date.getFullYear() + "-" + month + "-" + day);
        }

        function downloadPDFReport() {
            form = document.getElementById('rejectedsampleform');
            form.action = 'pdfReportRejected.htm';
            form.submit();
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
                    <h5 class="text-dark font-weight-bold my-1 mr-5">Rejected Sample Management</h5>
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
                            <h3 class="card-title">Search Rejected Sample</h3>
                        </div>
                        <!--begin::Form-->
                        <form:form class="form" id="rejectedsampleform" name="rejectedsampleform" action=""
                                   theme="simple" method="post" modelAttribute="rejectedsample">
                            <div class="card-body">
                                <div class="form-group row">
                                    <div class="col-lg-3">
                                        <label>Received Date:</label>
                                        <div class="btn-group div-inline input-group input-group-sm input-append date">
                                            <input path="receivedDate" name="receivedDate" id="receivedDate"
                                                   class="form-control" readonly="true"
                                                   autocomplete="off" type="text" onkeydown="return false"/>
                                        </div>

                                        <span class="form-text text-muted">Please enter received date</span>
                                    </div>

                                    <div class="col-lg-3">
                                        <label>Reference No:</label>
                                        <div class="input-group">
                                            <div class="input-group-prepend">
                                                <span class="input-group-text">
                                                    <i class="la la-bookmark-o"></i>
                                                </span>
                                            </div>
                                            <input id="referenceNo" name="referenceNo" type="text"
                                                   maxlength="16" class="form-control"
                                                   placeholder="Reference No">
                                        </div>
                                        <span class="form-text text-muted">Please enter reference number</span>
                                    </div>

                                    <div class="col-lg-3">
                                        <label>Institution Code:</label>
                                        <select id="institutionCode" name="institutionCode" class="form-control">
                                            <option selected value="">Select Institution</option>
                                            <c:forEach items="${rejectedsample.institutionList}" var="institution">
                                                <option value="${institution.institutionCode}">${institution.institutionCode}</option>
                                            </c:forEach>
                                        </select>
                                        <span class="form-text text-muted">Please select institution</span>
                                    </div>

                                    <div class="col-lg-3">
                                        <label>Name:</label>
                                        <input id="name" name="name" type="text"
                                               onkeyup="$(this).val($(this).val().replace(/[^a-zA-Z0-9 ]/g, ''))"
                                               maxlength="512" class="form-control "
                                               placeholder="Name">
                                        <span class="form-text text-muted">Please enter name</span>
                                    </div>

                                    <div class="col-lg-3">
                                        <label>NIC:</label>
                                        <input id="nic" name="nic" type="text" onkeyup="" maxlength="10"
                                               class="form-control" placeholder="NIC">
                                        <span class="form-text text-muted">Please enter nic</span>
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

                                    <div class="col-lg-4"></div>

                                    <div class="col-lg-2">
                                        <button id="viewPDF" type="button" class="btn btn-primary mr-2"
                                                onclick="downloadPDFReport()">
                                            View Report
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
                        <h3 class="card-label">Rejected Data Management
                            <span class="d-block text-muted pt-2 font-size-sm"></span></h3>
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
                                <th>Reference No</th>
                                <th>Institution Code</th>
                                <th>Name</th>
                                <th>Age</th>
                                <th>Gender</th>
                                <th>Symptomatic</th>
                                <th>Contact Type</th>
                                <th>NIC</th>
                                <th>Address</th>
                                <th>District</th>
                                <th>Contact No</th>
                                <th>Secondary Contact No</th>
                                <th>Received Date</th>
                                <th>Status</th>
                                <th>Remark</th>
                                <th>Created User</th>
                                <th>Created Time</th>
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
