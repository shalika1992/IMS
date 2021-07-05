<%--
  Created by IntelliJ IDEA.
  User: akila
  Date: 5/15/2021
  Time: 6:09 PM
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
            getCorrespondingPlateList()
            $('#receivedDate').datepicker().on('changeDate', function (ev) {
                getCorrespondingPlateList();
            });
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
                sAjaxSource: "${pageContext.servletContext.contextPath}/listMasterData.json",
                fnServerData: function (sSource, aoData, fnCallback) {
                    aoData.push(
                        {'name': 'csrf_token', 'value': token},
                        {'name': 'header', 'value': header},
                        {'name': 'receivedDate', 'value': $('#receivedDate').val()},
                        {'name': 'referenceNumber', 'value': $('#referenceNumber').val()},
                        {'name': 'name', 'value': $('#name').val()},
                        {'name': 'nic', 'value': $('#nic').val()},
                        {'name': 'institutionCode', 'value': $('#institutionCode').val()},
                        {'name': 'status', 'value': $('#status').val()},
                        {'name': 'result', 'value': $('#result').val()},
                        {'name': 'plateID', 'value': $('#plateId').val()}
                    );
                    $.ajax({
                        dataType: 'json',
                        type: 'POST',
                        url: "${pageContext.request.contextPath}/listMasterData.json",
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
                initComplete: function (settings, json) {
                    document.getElementById('data-table-loading').style.display = "none";
                    document.getElementById('data-table-wrapper').style.display = "block";
                },
                fnDrawCallback: function (oSettings) {
                    $(".table ").css({"width": "100%"});
                },
                columnDefs: [
                    {
                        title: "Lab Number",
                        targets: 0,
                        mDataProp: "barcode",
                        defaultContent: "--"
                    },
                    {
                        title: "Serial No",
                        targets: 1,
                        mDataProp: "referenceNumber",
                        defaultContent: "--"
                    },
                    {
                        title: "Institution Name",
                        targets: 2,
                        mDataProp: "institutionName",
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
                        title: "NIC",
                        targets: 6,
                        mDataProp: "nic",
                        defaultContent: "--"
                    },
                    {
                        title: "Contact Number",
                        targets: 7,
                        mDataProp: "contactNumber",
                        defaultContent: "--"
                    },
                    {
                        title: "Received Date",
                        targets: 8,
                        mDataProp: "receivedDate;",
                        defaultContent: "--",
                        render: function (data) {
                            return moment(data).format("YYYY-MM-DD")
                        }
                    },
                    {
                        title: "Result",
                        targets: 9,
                        mDataProp: "resultDescription",
                        defaultContent: "--"
                    },
                    {
                        title: "Status",
                        targets: 10,
                        mDataProp: "statusDescription",
                        defaultContent: "--"

                    },
                    {
                        title: "Created User",
                        targets: 11,
                        mDataProp: "createdUser",
                        defaultContent: "--"
                    },
                    {
                        title: "Created Time",
                        targets: 12,
                        mDataProp: "createdTime",
                        defaultContent: "--",
                        render: function (data) {
                            return moment(data).format("YYYY-MM-DD HH:mm:ss A")
                        }
                    },
                    {
                        title: "Download",
                        sortable: false,
                        className: "dt-center",
                        mRender: function (data, type, full) {
                            if(full.statusCode === 'PLASG' ){
                                return '--';
                            }else{
                                return '' +
                                    '<div>' +
                                        '<a href="javascript:;" class="btn btn-sm btn-clean btn-icon mr-2" title="Download" id=' + full.institutionCode + ' onclick="downloadIndividualReport(\'' + full.id + '\')">' +
                                        '<span class="svg-icon svg-icon-md"><svg xmlns="http://www.w3.org/2000/svg" width="24" height="24" viewBox="0 0 24 24"><path d="M0,0H24V24H0Z" fill="none"/><path d="M19,9H15V3H9V9H5l7,7ZM5,18v2H19V18Z" fill="#b5b5c3"/></svg></span>' +
                                        '</a>' +
                                    '</div>';
                            }

                        },
                        targets: 13,
                        defaultContent: "--"
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
            $('#id').val("");
            $("#receivedDate").datepicker('setDate', getReceivedDate());
            $('#referenceNumber').val("");
            $('#name').val("");
            $('#nic').val("");
            $('#institutionCode').val("");
            $('#status').val("");
            $('#result').val("");
            $('#plateId').val("");
            //reset plate list
            getCorrespondingPlateList();
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
            form = document.getElementById('masterDataForm');
            form.action = 'downloadMasterDataPdf.htm';
            form.submit();
        }

        function downloadIndividualReport(id) {
            $('#id').val(id);
            form = document.getElementById('masterDataForm');
            form.action = 'downloadMasterDataIndividualPdf.htm';
            form.submit();
        }


        function getCorrespondingPlateList() {
            var receivedDate = $("#receivedDate").val();
            $.ajax({
                url: "${pageContext.request.contextPath}/getPlateList.json",
                data: {
                    receivedDate: receivedDate
                },
                dataType: "json",
                type: 'GET',
                contentType: "application/json",
                success: function (data) {
                    $('#plateId')[0].options.length = 0;
                    //append the new list
                    var options = '<option selected value="0"><strong>Select Plate</strong></option>';
                    if (data && data.length > 0) {
                        $(data).each(function (index, value) {
                            options += '<option value="' + value.code + '">' + value.code + '</option>';
                        });
                    }
                    $('#plateId').html(options);
                },
                error: function (data) {
                    window.location = "${pageContext.request.contextPath}/logout.htm";
                }
            });
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
                    <h5 class="text-dark font-weight-bold my-1 mr-5">Report Generation</h5>
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
                            <h3 class="card-title">Search Master Data</h3>
                        </div>
                        <!--begin::Form-->
                        <form:form class="form" id="masterDataForm" name="masterDataForm" action=""
                                   theme="simple" method="post" modelAttribute="masterData">
                            <div class="card-body">
                                <div class="form-group row mb-0">

                                    <div class="col-lg-3" hidden="true">
                                        <label class="label-right">ID</label>
                                        <input path="id" name="id" id="id" type="text" maxlength="8"
                                               class="form-control" placeholder="ID"/>
                                    </div>

                                    <div class="col-lg-3">
                                        <label class="label-right">Received Date:</label>
                                        <div class="btn-group div-inline input-group input-group-sm input-append date">
                                            <input path="receivedDate" name="receivedDate" id="receivedDate"
                                                   class="form-control" readonly="true"
                                                   autocomplete="off" type="text" onkeydown="return false"/>
                                        </div>
                                    </div>

                                    <div class="col-lg-3">
                                        <label class="label-right">Serial No:</label>
                                        <input id="referenceNumber" name="referenceNumber" type="text"
                                               maxlength="64"
                                               class="form-control" placeholder="Serial No"/>
                                    </div>

                                    <div class="col-lg-3">
                                        <label class="label-right">Name:</label>
                                        <input id="name" name="name" type="text"
                                               maxlength="256"
                                               class="form-control form-control" placeholder="Name"/>
                                    </div>

                                    <div class="col-lg-3">
                                        <label  class="label-right" >NIC:</label>
                                        <input id="nic" name="nic" type="text"
                                               maxlength="16"
                                               class="form-control form-control" placeholder="NIC"/>
                                    </div>

                                    <div class="col-lg-3">
                                        <label class="label-right">Institution:</label>
                                        <select id="institutionCode" name="institutionCode" class="form-control">
                                            <option selected value="">Select Institution</option>
                                            <c:forEach items="${masterData.commonInstitutionList}" var="institution">
                                                <option value="${institution.institutionCode}">${institution.institutionCode}
                                                    - ${institution.institutionName} </option>
                                            </c:forEach>
                                        </select>
                                    </div>

                                    <div class="col-lg-3">
                                        <label class="label-right">Status:</label>
                                        <select id="status" name="status" class="form-control">
                                            <option selected value="">Select Status</option>
                                            <c:forEach items="${masterData.statusList}" var="status">
                                                <option value="${status.statusCode}">${status.description}</option>
                                            </c:forEach>
                                        </select>
                                    </div>

                                    <div class="col-lg-3">
                                        <label class="label-right">Result:</label>
                                        <select id="result" name="result" class="form-control">
                                            <option selected value="">Select Result</option>
                                            <c:forEach items="${masterData.resultList}" var="result">
                                                <option value="${result.code}">${result.description}</option>
                                            </c:forEach>
                                        </select>
                                    </div>


                                    <div class="col-lg-3">
                                        <label class="label-right">Plate:</label>
                                        <select id="plateId" name="plateID" class="form-control">
                                            <option selected value="0">Select Plate</option>
                                            <c:forEach items="${resultupdate.plateList}" var="plate">
                                                <option value="${plate.code}">${plate.code}</option>
                                            </c:forEach>
                                        </select>
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
                                        <button type="button" class="btn btn-secondary btn-sm" onclick="resetSearch()">
                                            Reset
                                        </button>
                                    </div>

                                    <div class="col-lg-6">
                                        <button id="viewPDF" type="button" class="btn btn-primary pull-r btn-sm"
                                                onclick="downloadPDFReport()">
                                            View Report
                                        </button>
                                    </div>
                                </div>
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
                        <h3 class="card-label">Master Data Report
                            <span class="d-block text-muted pt-2 font-size-sm">Master Data List</span></h3>
                    </div>
                    <div class="card-toolbar"></div>
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
                                <th>Lab Number</th>
                                <th>Serial Number</th>
                                <th>Institution Name</th>
                                <th>Name</th>
                                <th>Age</th>
                                <th>Gender</th>
                                <th>NIC</th>
                                <th>Contact Number</th>
                                <th>Received Date</th>
                                <th>Result</th>
                                <th>Status</th>
                                <th>Created User</th>
                                <th>Created Time</th>
                                <th>Download</th>
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
