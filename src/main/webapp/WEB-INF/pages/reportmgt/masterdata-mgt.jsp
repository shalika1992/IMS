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
        $(document).ready(function (){
            let today = new Date().toISOString().split("T")[0];
            $("#sreceivedDate").attr("max", today);
        })

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
                sAjaxSource: "${pageContext.servletContext.contextPath}/listMasterData.json",
                fnServerData: function (sSource, aoData, fnCallback) {
                    aoData.push(
                        {'name': 'csrf_token', 'value': token},
                        {'name': 'header', 'value': header},
                        {'name': 'receivedDate', 'value': $('#sreceivedDate').val()},
                        {'name': 'referenceNumber', 'value': $('#referenceNumber').val()},
                        {'name': 'name', 'value': $('#name').val()},
                        {'name': 'nic', 'value': $('#nic').val()},
                        {'name': 'institutionCode', 'value': $('#institutionCode').val()},
                        {'name': 'status', 'value': $('#status').val()},
                        {'name': 'result', 'value': $('#result').val()}
                    );
                    $.ajax({
                        dataType: 'json',
                        type: 'POST',
                        url: "${pageContext.request.contextPath}/listMasterData.json",
                        contentType: "application/json",
                        data: stringify_aoData(aoData),
                        success: function (data) {

                            if(data.iTotalRecords===0){
                                $("#viewPDF").attr("disabled","true");
                            }else{
                                $("#viewPDF").removeAttr("disabled");
                            }

                            fnCallback(data);
                        },
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
                        title: "Reference Number",
                        targets: 0,
                        mDataProp: "referenceNumber",
                        defaultContent: "--"
                    },
                    {
                        title: "Institution Name",
                        targets: 1,
                        mDataProp: "institutionName",
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
                        defaultContent: "--"
                    },
                    {
                        title: "NIC",
                        targets: 5,
                        mDataProp: "nic",
                        defaultContent: "--"
                    },
                    {
                        title: "Contact Number",
                        targets: 6,
                        mDataProp: "contactNumber",
                        defaultContent: "--"
                    },
                    {
                        title: "Serial Number",
                        targets: 7,
                        mDataProp: "serialNumber",
                        defaultContent: "--"
                    },
                    {
                        title: "Specimen ID",
                        targets: 8,
                        mDataProp: "specimenID",
                        defaultContent: "--"
                    },
                    {
                        title: "Barcode Number",
                        targets: 9,
                        mDataProp: "barcode",
                        defaultContent: "--"
                    },
                    {
                        title: "Received Date",
                        targets: 10,
                        mDataProp: "receivedDate;",
                        defaultContent: "--",
                        render: function (data) {
                            return moment(data).format("YYYY-MM-DD")
                        }
                    },
                    {
                        title: "Result",
                        targets: 11,
                        mDataProp: "resultDescription",
                        defaultContent: "--"
                    },
                    {
                        title: "Status",
                        targets: 12,
                        mDataProp: "statusDescription",
                        defaultContent: "--"

                    },
                    {
                        title: "Created User",
                        targets: 13,
                        mDataProp: "createdUser",
                        defaultContent: "--"
                    },
                    {
                        title: "Created Time",
                        targets: 14,
                        mDataProp: "createdTime",
                        defaultContent: "--",
                        render: function (data) {
                            return moment(data).format("YYYY-MM-DD hh:mm a")
                        }
                    },
                    {
                        title: "Report Time",
                        targets: 15,
                        mDataProp: "reportTime",
                        defaultContent: "--",
                        render: function (data) {
                            return moment(data).format("YYYY-MM-DD hh:mm a")
                        }
                    },
                    {
                        title: "Download",
                        sortable: false,
                        className: "dt-center",
                        mRender: function (data, type, full) {
                            return '<div><a href="javascript:;" class="btn btn-sm btn-clean btn-icon mr-2"' +
                                '  title="Download" id=' + full.id + ' onclick="downloadRecord(\'' + full.id + '\')">' +
                                '<span class="svg-icon svg-icon-md"><svg xmlns="http://www.w3.org/2000/svg" width="24" height="24" viewBox="0 0 24 24"><path d="M0,0H24V24H0Z" fill="none"/><path d="M19,9H15V3H9V9H5l7,7ZM5,18v2H19V18Z" fill="#b5b5c3"/></svg></span></a></div>';
                        },
                        targets: 16,
                        defaultContent: "--"
                    }
                ]
            });
        }

        function downloadRecord(id){
            $.ajax({
                type: 'GET',
                url: "${pageContext.request.contextPath}/downloadMasterDataRecordPdf.json",
                contentType: 'application/json;charset=UTF-8',
                data:{
                  id : id
                },
                cache: false,
                xhr: function () {
                    let xhr = new XMLHttpRequest();
                    xhr.onreadystatechange = function () {
                        if (xhr.readyState == 2) {
                            if (xhr.status == 200) {
                                xhr.responseType = "blob";
                            } else {
                                xhr.responseType = "text";
                            }
                        }
                    };
                    return xhr;
                },
                success: function (data){
                    //Convert the Byte Data to BLOB object.
                    let blob = new Blob([data], { type: "application/octetstream" });
                    let filename = "masterDataRecord.pdf"

                    //Check the Browser type and download the File.
                    let isIE = false || !!document.documentMode;
                    if (isIE) {
                        window.navigator.msSaveBlob(blob, filename);
                    } else {
                        let url = window.URL || window.webkitURL;
                        link = url.createObjectURL(blob);
                        let a = $("<a />");
                        a.attr("download", filename);
                        a.attr("href", link);
                        $("body").append(a);
                        a[0].click();
                        $("body").remove(a);
                    }

                },

                error: function (e) {

                    window.location = "${pageContext.request.contextPath}/logout.htm";
                }
            });
        }

        function downloadPDF() {
            let token = $("meta[name='_csrf']").attr("content");
            let header = $("meta[name='_csrf_header']").attr("content");
            let stringify_aoData = function (aoData) {
                let o = {};
                let modifiers = ['mDataProp_', 'sSearch_', 'iSortCol_', 'bSortable_', 'bRegex_', 'bSearchable_', 'sSortDir_'];
                jQuery.each(aoData, function (idx, obj) {
                    if (obj.name) {
                        for (var i = 0; i < modifiers.length; i++) {
                            if (obj.name.substring(0, modifiers[i].length) == modifiers[i]) {
                                let index = parseInt(obj.name.substring(modifiers[i].length));
                                let key = 'a' + modifiers[i].substring(0, modifiers[i].length - 1);
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

            let aoData =[{'name': 'csrf_token', 'value': token},
                {'name': 'header', 'value': header},
                {'name': 'receivedDate', 'value': $('#sreceivedDate').val()},
                {'name': 'referenceNumber', 'value': $('#referenceNumber').val()},
                {'name': 'name', 'value': $('#name').val()},
                {'name': 'nic', 'value': $('#nic').val()},
                {'name': 'institutionCode', 'value': $('#institutionCode').val()},
                {'name': 'status', 'value': $('#status').val()},
                {'name': 'result', 'value': $('#result').val()}];


            $.ajax({
                type: 'POST',
                url: "${pageContext.request.contextPath}/downloadMasterDataPdf.json",
                contentType: 'application/json;charset=UTF-8',
                cache: false,
                xhr: function () {
                    let xhr = new XMLHttpRequest();
                    xhr.onreadystatechange = function () {
                        if (xhr.readyState == 2) {
                            if (xhr.status == 200) {
                                xhr.responseType = "blob";
                            } else {
                                xhr.responseType = "text";
                            }
                        }
                    };
                    return xhr;
                },
                data: stringify_aoData(aoData),
                success: function (data){
                    //Convert the Byte Data to BLOB object.
                    let blob = new Blob([data], { type: "application/octetstream" });
                    let filename = "masterData.pdf"

                    //Check the Browser type and download the File.
                    let isIE = false || !!document.documentMode;
                    if (isIE) {
                        window.navigator.msSaveBlob(blob, filename);
                    } else {
                        let url = window.URL || window.webkitURL;
                        link = url.createObjectURL(blob);
                        let a = $("<a />");
                        a.attr("download", filename);
                        a.attr("href", link);
                        $("body").append(a);
                        a[0].click();
                        $("body").remove(a);
                    }

                },

                error: function (e) {

                    window.location = "${pageContext.request.contextPath}/logout.htm";
                }
            });
        }

        function searchStart() {
            oTable.fnDraw();
        }

        function resetSearch() {
            $('#sreceivedDate').val("");
            $('#referenceNumber').val("");
            $('#name').val("");
            $('#nic').val("");
            $('#institutionCode').val("");
            $('#status').val("");
            $('#result').val("");

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
                        <form:form class="form" id="masterDataForm" name="masterDataForm" action="MasterData"
                                   theme="simple" method="post" modelAttribute="masterData">
                            <%--                        <form class="form">--%>
                            <div class="card-body">
                                <div class="form-group row">

                                    <div class="col-lg-3">
                                        <label>Received Date:</label>
                                        <input id="sreceivedDate" class="form-control" name="sreceivedDate" type="date" max=""/>
                                        <span class="form-text text-muted">Please enter received date</span>
                                    </div>

                                    <div class="col-lg-3">
                                        <label>Reference Number:</label>
                                        <input id="referenceNumber" name="referenceNumber" type="text" onkeyup="" maxlength="64"
                                               class="form-control" placeholder="Reference Number"/>
                                        <span class="form-text text-muted">Please enter reference number</span>
                                    </div>


                                    <div class="col-lg-3">
                                        <label>Name:</label>
                                        <input id="name" name="name" type="text"
                                               maxlength="256"
                                               class="form-control form-control-sm" placeholder="Name"/>

                                        <span class="form-text text-muted">Please enter Name</span>
                                    </div>

                                    <div class="col-lg-3">
                                        <label>NIC:</label>
                                        <input id="nic" name="nic" type="text"
                                               maxlength="16"
                                               class="form-control form-control-sm" placeholder="NIC"/>

                                        <span class="form-text text-muted">Please enter NIC</span>
                                    </div>

                                    <div class="col-lg-3">
                                        <label>Institution:</label>
                                        <select id="institutionCode" name="institutionCode" class="form-control">
                                            <option selected value="">Select Institution</option>
                                            <c:forEach items="${masterData.commonInstitutionList}" var="institution">
                                                <option value="${institution.institutionCode}">${institution.institutionCode} - ${institution.institutionName} </option>
                                            </c:forEach>
                                        </select>

                                        <span class="form-text text-muted">Please enter institution code</span>
                                    </div>

                                    <div class="col-lg-3">
                                        <label>Status:</label>
                                        <select id="status" name="status" class="form-control">
                                            <option selected value="">Select Status</option>
                                            <c:forEach items="${masterData.statusList}" var="status">
                                                <option value="${status.statusCode}">${status.description}</option>
                                            </c:forEach>
                                        </select>
                                        <span class="form-text text-muted">Please select status</span>
                                    </div>

                                    <div class="col-lg-3">
                                        <label>Result:</label>
                                        <select id="result" name="result" class="form-control">
                                            <option selected value="">Select Result</option>
                                            <c:forEach items="${masterData.resultList}" var="result">
                                                <option value="${result.code}">${result.description}</option>
                                            </c:forEach>
                                        </select>
                                        <span class="form-text text-muted">Please select result</span>
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

                            <div class="col-md-2 col-sm-4">
                                <button id="viewPDF" type="button"
                                        class="btn btn-block btn-primary btn-sm"
                                        onclick="downloadPDF()" >View PDF
                                </button>
                            </div>
                        </form:form>
                        <!--end::Card-->
                    </div>
                </div>
            </div>
            <%--                <!--begin::Card-->--%>
                            <div class="card card-custom gutter-b">
                                <div class="card-header flex-wrap border-0 pt-6 pb-0">
                                    <div class="card-title">
                                        <h3 class="card-label">Master Data Report
                                            <span class="d-block text-muted pt-2 font-size-sm">Master Data List</span></h3>
                                    </div>
                                    <div class="card-toolbar">
                                        <!--begin::Button-->
<%--                                        --%>
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
                                                <th>Reference Number</th>
                                                <th>Institution Name</th>
                                                <th>Name</th>
                                                <th>Age</th>
                                                <th>Gender</th>
                                                <th>NIC</th>
                                                <th>Contact Number</th>
                                                <th>Serial Number</th>
                                                <th>Specimen ID</th>
                                                <th>Barcode Number</th>
                                                <th>Received Date</th>
                                                <th>Result</th>
                                                <th>Status</th>
                                                <th>Created User</th>
                                                <th>Created Time</th>
                                                <th>Report Time</th>
                                                <th>Download</th >
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
