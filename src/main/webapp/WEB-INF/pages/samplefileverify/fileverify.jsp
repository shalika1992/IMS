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
    <script type="text/javascript"
            src="${pageContext.request.contextPath}/resources/datatable/js/jquery.dataTables.min.js"></script>
    <script type="text/javascript"
            src="${pageContext.request.contextPath}/resources/datatablecheckbox/js/dataTables.checkboxes.min.js"></script>
    <style>
        table.dataTable tr th.select-checkbox.selected::after {
            content: "✔";
            margin-top: -11px;
            margin-left: -4px;
            text-align: center;
            text-shadow: rgb(176, 190, 217) 1px 1px, rgb(176, 190, 217) -1px -1px, rgb(176, 190, 217) 1px -1px, rgb(176, 190, 217) -1px 1px;
        }
    </style>
    <script type="text/javascript">
        var oTable;
        var rows_selected = [];
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
                order: [
                    [1, 'asc']
                ],
                stateSave: false,
                columnDefs: [
                    {
                        'targets': 0,
                        'searchable': false,
                        'orderable': false,
                        'width': '1%',
                        'className': 'dt-body-center',
                        'render': function (data, type, full, meta) {
                            return '<input type="checkbox">';
                        },
                        defaultContent: "--"
                    },
                    {
                        title: "Id",
                        targets: 1,
                        mDataProp: "id",
                        defaultContent: "--",
                        visible: false
                    },
                    {
                        title: "Reference Number",
                        targets: 2,
                        mDataProp: "referenceNo",
                        defaultContent: "--"
                    },

                    {
                        title: "Institution Code",
                        targets: 3,
                        mDataProp: "institutionCode",
                        defaultContent: "--"
                    },
                    {
                        title: "Name",
                        targets: 4,
                        mDataProp: "name",
                        defaultContent: "--"
                    },
                    {
                        title: "Age",
                        targets: 5,
                        mDataProp: "age",
                        defaultContent: "--"
                    },
                    {
                        title: "Gender",
                        targets: 6,
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
                        targets: 7,
                        mDataProp: "symptomatic",
                        defaultContent: "--"
                    },
                    {
                        title: "Contact Type",
                        targets: 8,
                        mDataProp: "contactType",
                        defaultContent: "--"
                    },
                    {
                        title: "NIC",
                        targets: 9,
                        mDataProp: "nic",
                        defaultContent: "--"
                    },
                    {
                        title: "Address",
                        targets: 10,
                        mDataProp: "address",
                        defaultContent: "--"
                    },
                    {
                        title: "District",
                        targets: 11,
                        mDataProp: "residentDistrict",
                        defaultContent: "--"
                    },
                    {
                        title: "Contact Number",
                        targets: 12,
                        mDataProp: "contactNumber",
                        defaultContent: "--"
                    },
                    {
                        title: "Secondary Contact Number",
                        targets: 13,
                        mDataProp: "secondaryContactNumber",
                        defaultContent: "--"
                    },
                    {
                        title: "Received Date",
                        targets: 14,
                        mDataProp: "receivedDate",
                        defaultContent: "--"
                    },
                    {
                        title: "Status",
                        targets: 15,
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
                        title: "Created User",
                        targets: 16,
                        mDataProp: "createdUser",
                        defaultContent: "--"
                    },
                    {
                        label: 'Created Time',
                        name: 'createdTime',
                        targets: 17,
                        mDataProp: "createdTime",
                        render: function (data) {
                            return moment(data).format("YYYY-MM-DD HH:mm:ss A")
                        }
                    }
                ],
                select: {
                    style: 'multi',
                    selector: 'td:first-child + td'
                },
                rowCallback: function (row, data, dataIndex) {
                    // Get row ID
                    var rowId = data[1];
                    // If row ID is in the list of selected row IDs
                    if ($.inArray(rowId, rows_selected) !== -1) {
                        $(row).find('input[type="checkbox"]').prop('checked', true);
                        $(row).addClass('selected');
                    }
                },
                fnDrawCallback: function (oSettings, json) {
                    $("#btnInvalid").prop("disabled", true);
                    $("#btnValid").prop("disabled", true);
                    $("#btnNosample").prop("disabled", true);

                    //disable and enable the select_all input
                    if (oTable.fnGetData().length > 0) {
                        document.getElementById("select_all").disabled = false;
                    } else {
                        document.getElementById("select_all").disabled = true;
                    }

                    document.getElementById("select_all").checked = false;
                    rows_selected = [];
                }
            });

            // Handle click on checkbox
            $('#table tbody').on('click', 'input[type="checkbox"]', function (e) {
                var $row = $(this).closest('tr');

                // Get row data
                var data = oTable.api().row($row).data();

                // Get row ID
                var rowId = data['id'];
                // Determine whether row ID is in the list of selected row IDs
                var index = $.inArray(rowId, rows_selected);

                // If checkbox is checked and row ID is not in list of selected row IDs
                if (this.checked && index === -1) {
                    rows_selected.push(rowId);
                    // Otherwise, if checkbox is not checked and row ID is in list of selected row IDs
                } else if (!this.checked && index !== -1) {
                    rows_selected.splice(index, 1);
                }

                if (this.checked) {
                    $row.addClass('selected');
                } else {
                    $row.removeClass('selected');
                }

                // Update state of "Select all" control
                updateDataTableSelectAllCtrl(oTable);

                // Prevent click event from propagating to parent
                e.stopPropagation();
                //enable buttons
                if (rows_selected.length > 0 && $("#status").val() === 'PEND') {
                    $("#btnInvalid").prop("disabled", false);
                    $("#btnValid").prop("disabled", false);
                    $("#btnNosample").prop("disabled", false);
                } else {
                    $("#btnInvalid").prop("disabled", true);
                    $("#btnValid").prop("disabled", true);
                    $("#btnNosample").prop("disabled", true);
                }
            });

            $('thead input[name="select_all"]', oTable.api().table().container()).on('click', function (e) {
                if (this.checked) {
                    $('#table tbody input[type="checkbox"]:not(:checked)').trigger('click');
                } else {
                    $('#table tbody input[type="checkbox"]:checked').trigger('click');
                }
                // Prevent click event from propagating to parent
                e.stopPropagation();
            });

            // Handle table draw event
            oTable.on('draw', function () {
                // Update state of "Select all" control
                updateDataTableSelectAllCtrl(oTable);
            });
        }

        function updateDataTableSelectAllCtrl(table) {
            var $table = table.api().table().node();
            var $chkbox_all = $('tbody input[type="checkbox"]', $table);
            var $chkbox_checked = $('tbody input[type="checkbox"]:checked', $table);
            var chkbox_select_all = $('thead input[name="select_all"]', $table).get(0);

            // If none of the checkboxes are checked
            if ($chkbox_checked.length === 0) {
                chkbox_select_all.checked = false;
                if ('indeterminate' in chkbox_select_all) {
                    chkbox_select_all.indeterminate = false;
                }

                // If all of the checkboxes are checked
            } else if ($chkbox_checked.length === $chkbox_all.length) {
                chkbox_select_all.checked = true;
                if ('indeterminate' in chkbox_select_all) {
                    chkbox_select_all.indeterminate = false;
                }

                // If some of the checkboxes are checked
            } else {
                chkbox_select_all.checked = true;
                if ('indeterminate' in chkbox_select_all) {
                    chkbox_select_all.indeterminate = true;
                }
            }
        }

        function searchStart() {
            oTable.fnDraw();
        }

        function resetSearch() {
            $("#receivedDate").datepicker('setDate', getReceivedDate());
            $('#institutionCode').val("");
            $('#referenceNo').val("");
            $('#status').val("PEND");

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

        function openValidModal() {
            $('#modalValid').modal('show');
        }

        function markAsValid() {
            let dataS = {"idList": rows_selected};
            $.ajax({
                type: 'POST',
                url: "${pageContext.request.contextPath}/validsample.json",
                contentType: "application/json",
                data: JSON.stringify(dataS),
                success: function (data) {
                    //hide the modal
                    $('#modalValid').modal('hide');
                    if (data.errorMessage) {
                        $('#responseHeader').text('Sample Verification');
                        $('#responseMsg').text(data.errorMessage);
                        $('#modalResponse').modal('show');
                    } else {
                        $('#responseHeader').text('Sample Verification');
                        $('#responseMsg').text('Sample verification executed successfully');
                        $('#modalResponse').modal('show');
                    }
                    oTable.fnDraw();
                },
                error: function (e) {
                    window.location = "${pageContext.request.contextPath}/logout.htm";
                }
            });
        }

        function openNoSampleFoundModal() {
            $('#modalNotFound').modal('show');
        }

        function markAsNotFound() {
            let dataS = {"idList": rows_selected};
            $.ajax({
                type: 'POST',
                url: "${pageContext.request.contextPath}/notfoundsample.json",
                contentType: "application/json",
                data: JSON.stringify(dataS),
                success: function (e) {
                    //hide the modal
                    $('#modalNotFound').modal('hide');
                    if (data.errorMessage) {
                        $('#responseHeader').text('Samples Mark as Not Found');
                        $('#responseMsg').text(data.errorMessage);
                        $('#modalResponse').modal('show');
                    } else {
                        $('#responseHeader').text('Samples Mark as Not Found');
                        $('#responseMsg').text('Samples updated successfully');
                        $('#modalResponse').modal('show');
                    }
                    oTable.fnDraw();
                },
                error: function (e) {
                    window.location = "${pageContext.request.contextPath}/logout.htm";
                }
            });
        }

        function openInvalidModal() {
            $('#modalInvalid').modal('show');
        }

        function markAsInvalid() {
            let dataS = {"idList": rows_selected};
            $.ajax({
                type: 'POST',
                url: "${pageContext.request.contextPath}/invalidsample.json",
                contentType: "application/json",
                data: JSON.stringify(dataS),
                success: function (e) {
                    //hide the modal
                    $('#modalInvalid').modal('hide');
                    if (data.errorMessage) {
                        $('#responseHeader').text('Samples Invalid');
                        $('#responseMsg').text(data.errorMessage);
                        $('#modalResponse').modal('show');
                    } else {
                        $('#responseHeader').text('Samples Invalid');
                        $('#responseMsg').text('Samples updated successfully');
                        $('#modalResponse').modal('show');
                    }
                    oTable.fnDraw();
                },
                error: function (e) {
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
                            <h3 class="card-title">Verify Data</h3>
                        </div>

                        <!--begin::Form-->
                        <form:form class="form" id="sampleverifyform" name="sampleverifyform" action=""
                                   theme="simple" method="post" modelAttribute="sampleverify">
                            <%--                        <form class="form">--%>
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
                                        <label>Institution Code:</label>
                                        <select id="institutionCode" name="institutionCode" class="form-control">
                                            <option selected value="">Select Institution Code</option>
                                            <c:forEach items="${sampleverify.institutionList}" var="institution">
                                                <option value="${institution.institutionCode}">${institution.institutionName}</option>
                                            </c:forEach>
                                        </select>
                                        <span class="form-text text-muted">Please enter institution code</span>
                                    </div>

                                    <div class="col-lg-3">
                                        <label>Reference No:</label>
                                        <input id="referenceNo" name="referenceNo" type="text"
                                               maxlength="10"
                                               class="form-control form-control-sm" placeholder="Reference Number">

                                        <span class="form-text text-muted">Please enter reference number</span>
                                    </div>

                                    <div class="col-lg-3">
                                        <label>Status:</label>
                                        <select id="status" name="status" class="form-control">
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
                                        <button type="button" class="btn btn-secondary btn-sm" onclick="resetSearch()">
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
                        <h3 class="card-label">Sample Data Verification</h3>
                    </div>
                    <div class="card-toolbar">
                        <button onclick="openValidModal()" class="btn btn-primary font-weight-bolder" id="btnValid">
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
                            </span>Mark As Valid
                        </button>
                        <!--end::Button-->
                    </div>

                    <div class="card-toolbar">
                        <button onclick="openInvalidModal()" class="btn btn-primary font-weight-bolder" id="btnInvalid">
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
                            </span>Mark As Invalid
                        </button>
                        <!--end::Button-->
                    </div>

                    <div class="card-toolbar">
                        <button onclick="openNoSampleFoundModal()" class="btn btn-primary font-weight-bolder"
                                id="btnNosample">
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
                            </span>Mark As Sample Not Found
                        </button>
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
                                <th><input id="select_all" name="select_all" value="1" type="checkbox"></th>
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
<jsp:include page="fileverify_confirm.jsp"/>
<jsp:include page="fileverify_invalid.jsp"/>
<jsp:include page="fileverify_notfound.jsp"/>
<jsp:include page="fileverify_response.jsp"/>
<!-- end include jsp files -->
</html>
