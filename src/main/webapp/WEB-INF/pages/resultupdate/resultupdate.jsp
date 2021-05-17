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

            $('#receivedDate').datepicker().on('changeDate', function (ev) {
                getCorrespondingPlateList();
            });
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
                sAjaxSource: "${pageContext.servletContext.contextPath}/listResultUpdate.json",
                fnServerData: function (sSource, aoData, fnCallback) {
                    aoData.push(
                        {'name': 'csrf_token', 'value': token},
                        {'name': 'header', 'value': header},
                        {'name': 'receivedDate', 'value': $('#receivedDate').val()},
                        {'name': 'referenceNo', 'value': $('#referenceNo').val()},
                        {'name': 'name', 'value': $('#name').val()},
                        {'name': 'nic', 'value': $('#nic').val()},
                        {'name': 'institutionCode', 'value': $('#institutionCode').val()},
                        {'name': 'plateId', 'value': $('#plateId').val()}
                    );
                    $.ajax({
                        dataType: 'json',
                        type: 'POST',
                        url: "${pageContext.request.contextPath}/listResultUpdate.json",
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
                fixedColumns: {
                    rightColumns: 1
                },
                columnDefs: [
                    {
                        title: "ID",
                        targets: 0,
                        visible: false,
                        mDataProp: "id",
                        defaultContent: "--"
                    },
                    {
                        title: "Reference No",
                        targets: 1,
                        mDataProp: "referenceNo",
                        defaultContent: "--"
                    },
                    {
                        title: "Institution Code",
                        targets: 2,
                        mDataProp: "institutionCode",
                        defaultContent: "--"
                    }, {
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
                        title: "Plate Id",
                        targets: 7,
                        mDataProp: "",
                        defaultContent: "--"
                    },
                    {
                        title: "Block Value",
                        targets: 8,
                        mDataProp: "",
                        defaultContent: "--"
                    },
                    {
                        title: "Pool Or Un-Pool",
                        targets: 9,
                        mDataProp: "",
                        defaultContent: "--"
                    },
                    {
                        title: "Pool Id",
                        targets: 10,
                        mDataProp: "",
                        defaultContent: "--"
                    },
                    {
                        title: "Received Date",
                        targets: 11,
                        mDataProp: "receivedDate",
                        defaultContent: "--",
                        render: function (data) {
                            return moment(data).format("YYYY-MM-DD hh:mm a")
                        }
                    },
                    {
                        title: "Status",
                        targets: 12,
                        mDataProp: "status",
                        defaultContent: "--"
                    },
                    {
                        title: "View",
                        sortable: false,
                        className: "dt-center",
                        mRender: function (data, type, full) {
                            return '<button id=' + full.id + ' class="btn btn-default btn-sm"  onclick="viewRecord(\'' + full.id + '\')"><img src="${pageContext.request.contextPath}/resources/images/action-view.svg" alt=""></button>';
                        },
                        targets: 13,
                        defaultContent: "--"
                    }
                ]
            });
        }

        function viewRecord(id) {
            $.ajax({
                url: "${pageContext.request.contextPath}/getRecordResultUpdate.json",
                data: {
                    id: id
                },
                dataType: "json",
                type: 'GET',
                contentType: "application/json",
                success: function (data) {
                    alert('--TODO--');
                },
                error: function (data) {
                    window.location = "${pageContext.request.contextPath}/logout.htm";
                }
            });
        }

        function search() {
            oTable.fnDraw();
        }

        function resetSearch() {
            //reset date value
            $("#receivedDate").datepicker('setDate', getReceivedDate());
            //reset grid
            oTable.fnDraw();
            //reset plate list
            getCorrespondingPlateList();
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
                    var options = '<option selected value=""><strong>Select Plate</strong></option>';
                    if (data && data.length > 0) {
                        $(data).each(function (index, value) {
                            options += '<option value="' + value.id + '">' + value.code + '</option>';
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
                    <h5 class="text-dark font-weight-bold my-1 mr-5">Result Update</h5>
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
                            <h3 class="card-title">Search Sample Record</h3>
                        </div>
                        <!--begin::Form-->
                        <form:form class="form" id="resultupdateform" name="resultupdateform"
                                   action="addResultUpdate" theme="simple" method="post"
                                   modelAttribute="resultupdate">
                            <div class="card-body">
                                <div class="form-group row">
                                    <div class="col-lg-4">
                                        <label>Received Date:</label>
                                        <div class="btn-group div-inline input-group input-group-sm input-append date">
                                            <input path="receivedDate" name="receivedDate" id="receivedDate"
                                                   class="form-control" readonly="true"
                                                   autocomplete="off" type="text" onkeydown="return false"/>
                                        </div>
                                        <span class="form-text text-muted">Please select received date</span>
                                    </div>

                                    <div class="col-lg-4">
                                        <label>Reference No:</label>
                                        <div class="input-group">
                                            <div class="input-group-prepend">
                                                <span class="input-group-text">
                                                    <i class="la la-bookmark-o"></i>
                                                </span>
                                            </div>
                                            <input id="referenceNo" name="referenceNo" type="text"
                                                   maxlength="64" class="form-control form-control-sm"
                                                   placeholder="Reference No" autocomplete="off">
                                        </div>
                                        <span class="form-text text-muted">Please enter reference no</span>
                                    </div>

                                    <div class="col-lg-4">
                                        <label>Name :</label>
                                        <input id="name" name="name" type="text"
                                               maxlength="128" class="form-control form-control-sm"
                                               placeholder="Name" autocomplete="off">
                                        <span class="form-text text-muted">Please enter name</span>
                                    </div>

                                    <div class="col-lg-4">
                                        <label>NIC:</label>
                                        <div class="input-group">
                                            <input path="nic" name="nic" id="nic" type="text"
                                                   onkeyup="$(this).val($(this).val().replace(/[^a-zA-Z0-9 ]/g, ''))"
                                                   maxlength="16"
                                                   class="form-control form-control-sm" placeholder="NIC">
                                        </div>
                                        <span class="form-text text-muted">Please enter nic</span>
                                    </div>

                                    <div class="col-lg-4">
                                        <label>Institution Code:</label>
                                        <select id="institutionCode" name="institutionCode" class="form-control">
                                            <option selected value="">Select Institution Code</option>
                                            <c:forEach items="${resultupdate.institutionList}" var="institution">
                                                <option value="${institution.institutionCode}">${institution.institutionName}</option>
                                            </c:forEach>
                                        </select>
                                    </div>

                                    <div class="col-lg-4">
                                        <label>Plate :</label>
                                        <select id="plateId" name="plateId" class="form-control">
                                            <option selected value="">Select Plate</option>
                                            <c:forEach items="${resultupdate.plateList}" var="plate">
                                                <option value="${plate.id}">${plate.code}</option>
                                            </c:forEach>
                                        </select>
                                    </div>

                                </div>
                            </div>
                            <div class="card-footer">
                                <div class="row">
                                    <div class="col-lg-6">
                                        <button type="button" class="btn btn-primary mr-2" onclick="search()">
                                            Search
                                        </button>
                                        <button type="button" class="btn btn-secondary" onclick="resetSearch()">
                                            Reset
                                        </button>
                                    </div>
                                </div>
                                <!--end::Form-->
                            </div>
                        </form:form>
                    </div>
                </div>
            </div>

            <div class="card card-custom gutter-b">
                <div class="card-header flex-wrap border-0 pt-1 pb-0">
                    <div class="card-title"></div>
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
                                <th>ID</th>
                                <th>Reference No</th>
                                <th>Institution Code</th>
                                <th>Name</th>
                                <th>Age</th>
                                <th>Gender</th>
                                <th>NIC</th>
                                <th>Plate Id</th>
                                <th>Block Value</th>
                                <th>Is Pool</th>
                                <th>Pool Id</th>
                                <th>Received Date</th>
                                <th>Status</th>
                                <th>View</th>
                            </tr>
                            </thead>
                            <tbody></tbody>
                        </table>
                        <!--end: Datatable-->
                    </div>
                </div>
            </div>
        </div>
    </div>
</div>
</html>