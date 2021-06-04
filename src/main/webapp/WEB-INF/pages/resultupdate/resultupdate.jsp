<%@ taglib uri="http://tiles.apache.org/tags-tiles" prefix="tiles" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>

<html>
<head>
    <link rel="stylesheet"
          href="${pageContext.request.contextPath}/resources/assets/css/plategenerate/plategenerate.css">
    <style>
        .result {
            display: inline-block;
            color: #444;
            border: 1px solid #CCC;
            background: #DDD;
            box-shadow: 0 0 5px -1px rgba(0, 0, 0, 0.2);
            cursor: pointer;
            vertical-align: middle;
            max-width: 100px;
            padding: 5px;
            text-align: center;
        }

        .result:active {
            color: red;
            box-shadow: 0 0 5px -1px rgba(0, 0, 0, 0.6);
        }
    </style>

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

            $('#receivedDate').datepicker().on('changeDate', function (ev) {
                getCorrespondingPlateList();
            });

            $('#exampleModal').on('shown.bs.modal', function () {
                $('#myInput').trigger('focus')
            })

        });


        function resetSearch() {
            //reset date value
            $("#receivedDate").datepicker('setDate', getReceivedDate());
            //reset plate list
            getCorrespondingPlateList();
            // reset table
            $('#resultplate').empty();
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

        function getResultTypeList() {
            $.ajax({
                url: "${pageContext.request.contextPath}/getResultList.json",
                data: {},
                dataType: "json",
                type: 'GET',
                contentType: "application/json",
                success: function (data) {
                    $('#resultId')[0].options.length = 0;
                    //append the new list
                    var options = '<option selected value=""><strong>Select Result</strong></option>';
                    if (data && data.length > 0) {
                        $(data).each(function (index, value) {
                            options += '<option value="' + value.code + '">' + value.description + '</option>';
                        });
                    }
                    $('#resultId').html(options);
                },
                error: function (data) {
                    window.location = "${pageContext.request.contextPath}/logout.htm";
                }
            });
        }

        function search() {
            if ($('#plateId').val()) {
                $.ajax({
                    type: 'POST',
                    url: '${pageContext.request.contextPath}/generateMasterPlate.json',
                    data: {plateid: $('#plateId').val()},
                    success: function (res) {
                        _generatePlates(res);
                    },
                    error: function (jqXHR) {
                        window.location = "${pageContext.request.contextPath}/logout.htm";
                    }
                });
            } else {
                swal.fire({
                    text: "Please select a plate",
                    icon: "error",
                    buttonsStyling: false,
                    confirmButtonText: "Proceed",
                    customClass: {
                        confirmButton: "btn font-weight-bold btn-light-primary"
                    }
                });
            }
        }

        /*
          Generate Plates Function
          @param - platesArray {}
        */
        function _generatePlates(platesArray) {
            // get modulus
            let module = Object.keys(platesArray).length % 93;
            // get plate count
            let round = Math.floor(Object.keys(platesArray).length / 93);

            // plate count final
            if (module != 0) {
                round++;
            }

            // html obj
            let html = "";
            // shift for next plate
            let shift = 0;
            let shift_val = 0;
            // monitor if pooled or not
            let pool_count = 0;
            // fill plates
            for (let k = 0; k < 1; k++) {
                // plate rounds
                // check box
                html += "<div class='row master-plate' id='master-plate-" + (k + 1) + "'><div class='col-12 plate-title'>Master Plate<span>#" + (k + 1) + "</span></div>\n";

                // plate vertical letters
                html += "<div class='col-1'><div class='row'><div class='col-12 first-elmt'>&nbsp;</div></div>\n";
                for (let x = 1; x < 9; x++) {
                    html += "<div class='row'><div class='col-12 vertical-elmt'>" + (x + 9).toString(36).toUpperCase() + "</div></div>\n";
                }
                html += "</div>\n";

                // plate horizontal numbers
                html += "<div class='col-11'>\n";
                html += "<div class='row'>\n";
                for (let y = 1; y < 13; y++) {
                    html += "<div class='col-1 horizontal-elmt'>" + y + "</div>\n";
                }
                // fill cells
                let val;
                for (let j = 0; j < 8; j++) { // columns
                    for (let i = 0; i < 12; i++) { // rows
                        val = i + (12 * j); // change normal filling
                        // [&& (val + 1) !== 27] => C3
                        if ((val + 1) !== 84 && (val + 1) !== 96) {
                            if (platesArray[val + shift + shift_val] !== undefined) { // after finish filling
                                // tooltip creation
                                let ul = '<span class="label label-success label-inline ">Candidate Details</span>' +
                                    '<ul style="text-align: left !important;" class="list-group">';
                                $.each(platesArray[val + shift + shift_val], (j, e) => {
                                    for (let i = 0; i < e['id'].length; i++) {
                                        ul += '<li style="text-align: left !important;" class="list-group-item">' + e['referenceNo'][i] + '</li>';
                                        ul += '<li style="text-align: left !important;" class="list-group-item">' + e['name'][i] + '</li>';
                                        ul += '<li style="text-align: left !important;" class="list-group-item">' + e['nic'][i] + '</li>';
                                    }
                                    ul += '<li style="text-align: left !important;" class="list-group-item">' + e['barcode'] + '</li>';
                                });
                                ul += '</ul>';
                                // check if pooled or not
                                if (platesArray[pool_count][0]['iscomplete'] === '0') {
                                    html += "<div data-html='true' data-toggle='tooltip' data-placement='right' class='col-1 cell-elmt cell-click plate-" + (k + 1) + "' data-cellNum='" + (val + 1) + "' data-key='" + (val + shift + shift_val) + "' data-value='" + platesArray[val + shift + shift_val][0]['barcode'] + "' title='" + ul + "'>" + platesArray[val + shift + shift_val][0]['barcode'] + "</div>\n";
                                } else {
                                    html += "<div data-html='true' data-toggle='tooltip' data-placement='right' class='col-1 cell-elmt cell-disable plate-" + (k + 1) + "' data-cellNum='" + (val + 1) + "' data-key='" + (val + shift + shift_val) + "' data-value='" + platesArray[val + shift + shift_val][0]['barcode'] + "' title='" + ul + "'>" + platesArray[val + shift + shift_val][0]['barcode'] + "</div>\n";
                                }
                            } else {
                                html += "<div class='col-1 cell-elmt cell-disable' data-cellNum='" + (val + 1) + "' data-key='" + (val + shift + shift_val) + "'>N/A</div>\n";
                            }
                        } else {
                            shift_val--;
                            // tooltip creation
                            if ((val + 1) === 84) {
                                let span = '<span class="label label-success label-inline ">Negative control</span>';
                                html += "<div class='col-1 cell-elmt cell-disable' data-html='true' data-toggle='tooltip' data-cellNum='" + (val + 1) + "' data-key='" + (val + shift + shift_val) + "'  title='" + span + "'>N/A</div>\n";
                            } else if ((val + 1) === 96) {
                                let span = '<span class="label label-success label-inline ">Positive control</span>';
                                html += "<div class='col-1 cell-elmt cell-disable' data-html='true' data-toggle='tooltip' data-cellNum='" + (val + 1) + "' data-key='" + (val + shift + shift_val) + "'  title='" + span + "'>N/A</div>\n";
                            } else {
                                html += "<div class='col-1 cell-elmt cell-disable' data-cellNum='" + (val + 1) + "' data-key='" + (val + shift + shift_val) + "'>N/A</div>\n";
                            }
                        }
                    }
                }

                // close tags
                html += "</div>\n";
                html += "</div>\n";
                html += '</div>\n'
                // to next plate
                shift += 96;
                pool_count += 94;

            }
            // set final html obj
            $('#resultplate').empty();
            $('#resultplate').append(html);

            // tooltip init
            $(function () {
                $('[data-toggle="tooltip"]').tooltip()
            })

        }

        function removeItemOnce(arr, value) {
            var index = arr.indexOf(value);
            if (index > -1) {
                arr.splice(index, 1);
            }
            return arr;
        }

        $(document).on("click", ".cell-click", function () {
            //result list
            getResultTypeList();
            let checkIfAlreadySelected = $('.cell-elmt.active');
            $.each(checkIfAlreadySelected, function (x, y) {
                if ($(this).hasClass('active')) {
                    $(this).removeClass("active");
                } else {
                    $(this).addClass("active");
                }
            });
            if ($(this).hasClass('active')) {
                $(this).removeClass("active");
                $('#resultModal').modal('hide');
            } else {
                $(this).addClass("active");
                $('#resultModal').modal('show');
            }
            let activeElements = $('.cell-elmt.active');
            $.each(activeElements, function (x, y) {
                document.getElementById('barcode').value = y.dataset.value;
            });
        });
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
                            <h3 class="card-title">Search Result Update</h3>
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
                                        <label>Plate :</label>
                                        <select id="plateId" name="plateId" class="form-control">
                                            <option selected value="">Select Plate</option>
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
                <div class="card-body">
                    <div class="py-8">
                        <div id="resultplate"></div>
                    </div>
                </div>
            </div>

            <!-- Modal -->
            <div class="modal fade" id="resultModal" tabindex="-1" role="dialog" aria-labelledby="resultModalLabel"
                 aria-hidden="true">
                <div class="modal-dialog" role="document">
                    <div class="modal-content">
                        <div class="modal-header">
                            <h5 class="modal-title" id="exampleModalLabel">Result Update</h5>
                            <button type="button" class="close" data-dismiss="modal" aria-label="Close">
                                <span aria-hidden="true">&times;</span>
                            </button>
                        </div>
                        <div class="modal-body">
                            <div class="card-body">
                                <div class="form-group row">
                                    <div class="col-lg-2">
                                        <label>Barcode:</label>
                                    </div>
                                    <div class="col-lg-6">
                                        <div class="btn-group div-inline input-group input-group-sm input-append date">
                                            <input path="receivedDate" name="barcode" id="barcode"
                                                   class="form-control" readonly="true"
                                                   autocomplete="off" type="text"/>
                                        </div>
                                    </div>
                                </div>
                                <div class="form-group row">
                                    <div class="col-lg-2">
                                        <label>Result:</label>
                                    </div>
                                    <div class="col-lg-6">
                                        <select id="resultId" name="resultId" class="form-control">

                                        </select>
                                    </div>
                                </div>
                            </div>
                            <div class="card-footer">
                                <div class="row">
                                    <div class="col-lg-6">
                                        <button type="button" class="btn btn-primary mr-2">
                                            Update
                                        </button>
                                        <button type="button" class="btn btn-secondary" data-dismiss="modal">
                                            Cancel
                                        </button>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </div>
    <!-- start include jsp files -->
    <jsp:include page="resultupdate_detected.jsp"/>
    <jsp:include page="resultupdate_notdetected.jsp"/>
    <jsp:include page="resultupdate_repeat.jsp"/>
    <!-- end include jsp files -->
</html>