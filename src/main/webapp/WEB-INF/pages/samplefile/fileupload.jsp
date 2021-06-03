<%--
  Created by IntelliJ IDEA.
  User: shalika_w
  Date: 5/9/2021
  Time: 9:03 PM
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
            $('#searchReceivedDate').datepicker({
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
                sAjaxSource: "${pageContext.servletContext.contextPath}/listSampleFile.json",
                fnServerData: function (sSource, aoData, fnCallback) {
                    aoData.push(
                        {'name': 'csrf_token', 'value': token},
                        {'name': 'header', 'value': header},
                        {'name': 'receivedDate', 'value': $('#searchReceivedDate').val()},
                        {'name': 'referenceNo', 'value': $('#referenceNo').val()},
                        {'name': 'institutionCode', 'value': $('#institutionCode').val()},
                        {'name': 'name', 'value': $('#name').val()},
                        {'name': 'nic', 'value': $('#nic').val()}
                    );
                    $.ajax({
                        dataType: 'json',
                        type: 'POST',
                        url: "${pageContext.request.contextPath}/listSampleFile.json",
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
                        title: "Contact No",
                        targets: 11,
                        mDataProp: "contactNumber",
                        defaultContent: "--"
                    },
                    {
                        title: "Secondary Contact No",
                        targets: 12,
                        mDataProp: "secondaryContactNumber",
                        defaultContent: "--"
                    },
                    {
                        title: "Received Date",
                        targets: 13,
                        mDataProp: "receivedDate",
                        defaultContent: "--",
                        render: function (data) {
                            return moment(data).format("YYYY-MM-DD")
                        }
                    },
                    {
                        title: "Status",
                        targets: 14,
                        mDataProp: "status",
                        defaultContent: "--"
                    },
                    {
                        title: "Ward",
                        targets: 15,
                        mDataProp: "ward",
                        defaultContent: "--"
                    },
                    {
                        title: "Created User",
                        targets: 16,
                        mDataProp: "createdUser",
                        defaultContent: "--"
                    },
                    {
                        title: "Created Time",
                        targets: 17,
                        mDataProp: "createdTime",
                        defaultContent: "--",
                        render: function (data) {
                            return moment(data).format("YYYY-MM-DD HH:mm:ss A")
                        }
                    },
                    {
                        title: "Update",
                        sortable: false,
                        className: "dt-center",
                        mRender: function (data, type, full) {
                            return '<button id="editBtn" class="btn btn-default btn-sm"  onclick="editSampleFileRecord(\'' + full.id + '\')"><img src="${pageContext.request.contextPath}/resources/images/action-edit.svg" alt=""></button>';
                        },
                        targets: 18,
                        defaultContent: "--"
                    }
                ]
            });
        }

        function openAddModal() {
            $('#modalUploadSampleFile').modal('toggle');
            $('#modalUploadSampleFile').modal('show');
        }

        function openWardEntryModal(){
            $('#modalAddWardEntry').modal('toggle');
            $('#modalAddWardEntry').modal('show');
        }

        function editSampleFileRecord(id) {
            $.ajax({
                url: "${pageContext.request.contextPath}/getSampleFileRecord.json",
                data: {
                    id: id
                },
                dataType: "json",
                type: 'GET',
                contentType: "application/json",
                success: function (data) {
                    $('#responseMsgUpdate').hide();

                    $('#eid').val(data.id);
                    $('#eid').attr('readOnly', true);

                    $('#eReferenceNo').val(data.referenceNo);
                    $('#eReferenceNo').attr('readOnly', true);

                    $('#eName').val(data.name);
                    $('#eAge').val(data.age);
                    $('#eGender').val(data.gender);
                    $('#eNic').val(data.nic);
                    $('#eAddress').val(data.address);
                    $('#eDistrict').val(data.residentDistrict);
                    $('#eContactno').val(data.contactNumber);
                    $('#eSecondaryContactNo').val(data.secondaryContactNumber);
                    $('#eWardNumber').val(data.ward);

                    $('#modalUpdateSampleRecord').modal('toggle');
                    $('#modalUpdateSampleRecord').modal('show');
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
            $("#searchReceivedDate").datepicker('setDate', getReceivedDate());
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
            $('#searchReceivedDate').val(today);
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
                    <h5 class="text-dark font-weight-bold my-1 mr-5">Sample File Upload</h5>
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
                            <h3 class="card-title">Search Sample File Record</h3>
                        </div>
                        <!--begin::Form-->
                        <form:form class="form" id="samplefileuploadform" name="samplefileuploadform"
                                   action="addSampleFileRecord" theme="simple" method="post"
                                   modelAttribute="samplefile">
                            <div class="card-body">
                                <div class="form-group row mb-0">
                                    <div class="col-lg-3">
                                        <label class="label-right">Received Date:</label>
                                        <div class="btn-group div-inline input-group input-group-sm input-append date">
                                            <input path="receivedDate" name="receivedDate" id="searchReceivedDate"
                                                   class="form-control" readonly="true"
                                                   autocomplete="off" type="text" onkeydown="return false"/>
                                        </div>
                                    </div>

                                    <div class="col-lg-3">
                                        <label class="label-right">Reference No:</label>
                                        <div class="input-group">
                                            <div class="input-group-prepend">
                                                <span class="input-group-text">
                                                    <i class="la la-bookmark-o"></i>
                                                </span>
                                            </div>
                                            <input id="referenceNo" name="referenceNo" type="text"
                                                   maxlength="64" class="form-control "
                                                   placeholder="Reference No" autocomplete="off">
                                        </div>
                                    </div>

                                    <div class="col-lg-3">
                                        <label class="label-right">Institution Code:</label>
                                        <select id="institutionCode" name="institutionCode" class="form-control">
                                            <option selected value="">Select Institution Code</option>
                                            <c:forEach items="${samplefile.institutionList}" var="institution">
                                                <option value="${institution.institutionCode}">${institution.institutionName}</option>
                                            </c:forEach>
                                        </select>
                                    </div>

                                    <div class="col-lg-3">
                                        <label class="label-right">Name :</label>
                                        <input id="name" name="name" type="text"
                                               maxlength="128" class="form-control "
                                               placeholder="Name" autocomplete="off">
                                    </div>

                                    <div class="col-lg-3">
                                        <label class="label-right">NIC:</label>
                                        <div class="input-group">
                                            <input path="nic" name="nic" id="nic" type="text"
                                                   onkeyup="$(this).val($(this).val().replace(/[^a-zA-Z0-9 ]/g, ''))"
                                                   maxlength="16"
                                                   class="form-control " placeholder="NIC">
                                        </div>
                                    </div>

                                </div>
                            </div>
                            <div class="card-footer">
                                <div class="row">
                                    <div class="col-lg-6">
                                        <button type="button" class="btn btn-primary mr-2" onclick="search()">Search
                                        </button>
                                        <button type="button" class="btn btn-secondary" onclick="resetSearch()">Reset
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
                <div class="card-header flex-wrap border-0 pt-6 pb-0">
                    <div class="card-title">
                        <h3 class="card-label">Sample Data Upload</h3>
                    </div>
                    <div class="d-flex flex-row-reverse">
                        <div class="card-toolbar">
                            <button onclick="openAddModal()" class="btn btn-primary font-weight-bolder" id="btnUpload">
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
                            </span>Bulk Upload</button>
                            <!--end::Button-->
                        </div>

                        <div class="card-toolbar">
                            <button onclick="openWardEntryModal()" class="btn btn-primary font-weight-bolder" id="btnWardEntry" >
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
                            </span>Add Ward Entry</button>
                            <!--end::Button-->
                        </div>
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
                                <th>ID</th>
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
                                <th>Ward</th>
                                <th>Created User</th>
                                <th>Created Time</th>
                                <th>Update</th>
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
<!-- start include jsp files -->
<jsp:include page="fileupload-update.jsp"/>
<jsp:include page="fileupload-add.jsp"/>
<jsp:include page="fileupload-add-wardentry.jsp"/>
<!-- end include jsp files -->
</html>
