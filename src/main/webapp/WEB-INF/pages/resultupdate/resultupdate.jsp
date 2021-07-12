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
                // datesDisabled: '+0d',
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

        function getResultTypeList(resulttype) {
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
                            //options += '<option value="' + value.code + '">' + value.description + '</option>';
                            options += '<option value="' + value.code + '"';
                            if(resulttype===value.code){
                                 options+= 'selected = "selected"';
                            }
                            options += '>' + value.description + '</option>';
                        });
                    }
                    $('#resultId').html(options);
                },
                error: function (data) {
                    window.location = "${pageContext.request.contextPath}/logout.htm";
                }
            });
        }

        function checkResultType(event) {
            if (event.value === 'DTCD') {
                document.getElementById("ct_txt").style.display = "block";
            } else {
                $('#ct1').val("");
                $('#ct2').val("");
                document.getElementById("ct_txt").style.display = "none";
            }

            if (event.value === 'RJCT') {
                document.getElementById("rej_remark_txt").style.display = "block";
            } else {
                $('#rej_remark').val("");
                document.getElementById("rej_remark_txt").style.display = "none";
            }

        }

        function search() {
            if ($('#plateId').val()) {
                $.ajax({
                    type: 'POST',
                    url: '${pageContext.request.contextPath}/generateMasterPlate.json',
                    data: {plateid: $('#plateId').val(), receivedDate: $('#receivedDate').val()},
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

        function getScaleAndPrecision(num) {
            console.log("num "+num);
            let scale;
            let precision;
            let msg = '';

            if(!isNaN(num)){
                num = parseFloat(num) + "";
                var dotPos = num.indexOf(".");

                if(dotPos!=-1){
                    if (dotPos == -1) return null;
                    scale = num.length - dotPos - 1;
                    integer = dotPos;
                    precision = scale + integer;
                }else{
                    scale = 0;
                    integer = num.length;
                    precision = num.length;
                }

                if(precision > 5){
                    msg = "error";
                }
            }else {
                msg = "error not a number";
            }
            //console.log("msg "+msg);
            return msg;
        }

        function updateValidation() {

            $('#responseMsg').text('');
            if ($('#resultId').val()) {
                if ($('#resultId').val() !== 'DTCD' && $('#resultId').val() !== 'RJCT') {
                    update();
                } else {

                    if ($('#resultId').val() === 'DTCD'){

                        if ($('#ct1').val()) {
                            if(getScaleAndPrecision($('#ct1').val())===''){
                                //if ct1 is ok
                                if ($('#ct2').val()) {
                                    if(getScaleAndPrecision($('#ct2').val())===''){
                                        //if ct2 is ok
                                        update();
                                    } else {
                                        //if ct2 not is ok
                                        swal.fire({
                                            text: "Please enter a valid value for ct2",
                                            icon: "error",
                                            buttonsStyling: false,
                                            confirmButtonText: "Proceed",
                                            customClass: {
                                                confirmButton: "btn font-weight-bold btn-light-primary"
                                            }
                                        });
                                    }

                                } else {
                                    swal.fire({
                                        text: "Please enter a value for ct2",
                                        icon: "error",
                                        buttonsStyling: false,
                                        confirmButtonText: "Proceed",
                                        customClass: {
                                            confirmButton: "btn font-weight-bold btn-light-primary"
                                        }
                                    });
                                }
                            } else {
                                //if ct1 not is ok
                                swal.fire({
                                    text: "Please enter a valid value for ct1",
                                    icon: "error",
                                    buttonsStyling: false,
                                    confirmButtonText: "Proceed",
                                    customClass: {
                                        confirmButton: "btn font-weight-bold btn-light-primary"
                                    }
                                });
                            }

                        } else {
                            swal.fire({
                                text: "Please enter a value for ct1",
                                icon: "error",
                                buttonsStyling: false,
                                confirmButtonText: "Proceed",
                                customClass: {
                                    confirmButton: "btn font-weight-bold btn-light-primary"
                                }
                            });
                        }
                    } else if ($('#resultId').val() === 'RJCT') {
                        if ($('#remark').val()) {
                            update();
                        } else {
                            swal.fire({
                                text: "Please enter a reject remark",
                                icon: "error",
                                buttonsStyling: false,
                                confirmButtonText: "Proceed",
                                customClass: {
                                    confirmButton: "btn font-weight-bold btn-light-primary"
                                }
                            });
                        }
                    }

                }

            } else {
                swal.fire({
                    text: "Please select a result",
                    icon: "error",
                    buttonsStyling: false,
                    confirmButtonText: "Proceed",
                    customClass: {
                        confirmButton: "btn font-weight-bold btn-light-primary"
                    }
                });
            }
        }

        // update
        function update() {
            $.ajax({
                url: "${pageContext.request.contextPath}/updatePlateResult.json",
                data: JSON.stringify({
                    barcode: $('#barcode').val(),
                    resultId: $('#resultId').val(),
                    ct1: $('#ct1').val(),
                    ct2: $('#ct2').val(),
                    plateid: $('#plateId').val(),
                    remark: $('#remark').val(),
                    receivedDate: $('#receivedDate').val()
                }),
                dataType: "json",
                type: 'POST',
                contentType: "application/json",
                success: function (data) {
                    $('#resultModal').modal('hide');
                    $('#responseMsg').text('Result Updated Successfully');
                    _generatePlates(data)
                },
                error: function (data) {
                    $('#responseMsg').text('Error occurred while updating result');
                }
            });
        }

        /*
          Generate Plates Function
          @param - platesArray {}
        */
        function _generatePlates(platesArray) {
            // get modulus
            let module = Object.keys(platesArray).length % 94;
            // get plate count
            let round = Math.floor(Object.keys(platesArray).length / 94);

            // plate count final
            if (module != 0) {
                round++;
            }

            // html obj
            let html = "";
            // shift for next plate
            let shift = 0;
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
                for (let i = 0; i < 8; i++) { // rows
                    for (let j = 0; j < 12; j++) { // columns
                        val = i + (8 * j); // change normal filling
                        if (val < 94)  {
                            if (platesArray[val + shift] !== undefined) { // after finish filling
                                // tooltip creation
                                let ul = '<span class="label label-success label-inline ">Candidate Details</span>' +
                                    '<ul style="text-align: left !important;" class="list-group">';
                                $.each(platesArray[val + shift], (j, e) => {
                                    for (let i = 0; i < e['id'].length; i++) {
                                        ul += '<li style="text-align: left !important;" class="list-group-item">' + e['referenceNo'][i] + '</li>';
                                        ul += '<li style="text-align: left !important;" class="list-group-item">' + e['name'][i] + '</li>';
                                        ul += '<li style="text-align: left !important;" class="list-group-item">' + e['nic'][i] + '</li>';
                                    }
                                    ul += '<li style="text-align: left !important;" class="list-group-item">' + e['barcode'] + '</li>';
                                });
                                ul += '</ul>';
                                // check if pooled or not
                                // if (platesArray[val + shift][0]['iscomplete'] === '0') {
                                //     html += "<div data-html='true' data-toggle='tooltip' data-placement='right' class='col-1 cell-elmt cell-click plate-" + (k + 1) + "' data-cellNum='" + (val + 1) + "' data-key='" + (val + shift) + "' data-value='" + platesArray[val + shift][0]['barcode'] + "' title='" + ul + "'>" + platesArray[val + shift][0]['barcode'] + "</div>\n";
                                // } else {
                                //     html += "<div data-html='true' data-toggle='tooltip' data-placement='right' class='col-1 cell-elmt cell-disable plate-" + (k + 1) + "' data-cellNum='" + (val + 1) + "' data-key='" + (val + shift) + "' data-value='" + platesArray[val + shift][0]['barcode'] + "' title='" + ul + "'>" + platesArray[val + shift][0]['barcode'] + "</div>\n";
                                // }

                                if (platesArray[val + shift][0]['iscomplete'] === '0') {
                                    html += "<div data-html='true' data-toggle='tooltip' data-placement='right' class='col-1 cell-elmt cell-click plate-" + (k + 1) + "' data-cellNum='" + (val + 1) + "' data-key='" + (val + shift) + "' data-value='" + platesArray[val + shift][0]['barcode'] + "' title='" + ul + "'>" + platesArray[val + shift][0]['barcode'] + "</div>\n";
                                } else {
                                    html += "<div data-html='true' data-toggle='tooltip' data-placement='right' class='col-1 cell-elmt cell-click plate-" + (k + 1) + "' data-cellNum='" + (val + 1) + "' data-key='" + (val + shift) + "' data-value='" + platesArray[val + shift][0]['barcode'] + "' title='" + ul + "'>" + platesArray[val + shift][0]['barcode'] + "</div>\n";
                                }

                            } else {
                                html += "<div class='col-1 cell-elmt cell-disable' data-cellNum='" + (val + 1) + "' data-key='" + (val + shift) + "'>N/A</div>\n";
                            }
                        } else {
                            // tooltip creation
                            if ((val + 1) === 95) {
                                let span = '<span class="label label-success label-inline ">Negative control</span>';
                                html += "<div class='col-1 cell-elmt cell-disable' data-html='true' data-toggle='tooltip' data-cellNum='" + (val + 1) + "' data-key='" + (val + shift) + "'  title='" + span + "'>N/A</div>\n";
                            } else if ((val + 1) === 96) {
                                let span = '<span class="label label-success label-inline ">Positive control</span>';
                                html += "<div class='col-1 cell-elmt cell-disable' data-html='true' data-toggle='tooltip' data-cellNum='" + (val + 1) + "' data-key='" + (val + shift) + "'  title='" + span + "'>N/A</div>\n";
                            } else {
                                html += "<div class='col-1 cell-elmt cell-disable' data-cellNum='" + (val + 1) + "' data-key='" + (val + shift) + "'>N/A</div>\n";
                            }
                        }
                    }
                }

                // close tags
                html += "</div>\n";
                html += "</div>\n";
                html += '</div>\n'
                // to next plate
                shift += 94;
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

        $(document).on("click", ".cell-click", function () {

            // hide ct fields
            document.getElementById("ct_txt").style.display = "none";
            document.getElementById("rej_remark_txt").style.display = "none";
            //clear fields
            $('#responseMsg').text('');
            $("#ct1").val("");
            $("#ct2").val("");
            $("#remark").val("");

            // monitor selected plates
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

            //get marked cell's detail when click
            getMarkedDetails(activeElements);
            //set barcode on modelbox
            $.each(activeElements, function (x, y) {
                document.getElementById('barcode').value = y.dataset.value;
            });
        });

        //get marked cell's detail
        function getMarkedDetails(activeElements) {
            const well = [];

            $.each(activeElements, function (x, y) {
                well.push(y.dataset.value);
            });

            if (well.length == 1) {
                let barcodeNo = well[0];
                $.ajax({
                    url: "${pageContext.request.contextPath}/getMarkedDetails.json",
                    data: {
                        barcode: barcodeNo
                    },
                    dataType: "json",
                    type: 'GET',
                    contentType: "application/json",
                    success: function (data) {
                        //console.log(data.barcode+" :: "+data.ct1 +" :: "+data.ct2+" :: "+data.remark+" :: "+data.resultId);
                        //get result list
                        getResultTypeList(data.resultId);
                        switch (data.resultId) {
                            case "DTCD" :  // Detected
                                $("#ct_txt").css("display","block");
                                $("#ct1").val(data.ct1);
                                $("#ct2").val(data.ct2);
                                break;
                            case "RJCT" :  // Rejected
                                $("#rej_remark_txt").css("display","block");
                                $("#remark").val(data.remark);
                                break;
                            case "INCON" : // Inconclusive
                                break;
                            case "INVALD" :// Invalid
                                break;
                            case "NDTD" :  // Not Detected
                                break;
                            case "RPTD" :  // Pending Result
                                break;

                        }

                    },
                    error: function (data) {
                        window.location = "${pageContext.request.contextPath}/logout.htm";
                    }
                });
            }
        }

        function cancel(){
            $('#responseMsg').text('');
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
                            <h3 class="card-title">Search Result Update</h3>
                        </div>
                        <!--begin::Form-->
                        <form:form class="form" id="resultupdateform" name="resultupdateform"
                                   action="addResultUpdate" theme="simple" method="post"
                                   modelAttribute="resultupdate">
                            <div class="card-body">
                                <div class="form-group row">
                                    <div class="col-lg-4">
                                        <label>Plate Created Date:</label>
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
                            <div>
                                <p id="responseMsg"></p>
                            </div>
                            <div class="card-body">
                                <div class="form-group row">
                                    <div class="col-lg-3">
                                        <label>Lab Code:</label>
                                    </div>
                                    <div class="col-lg-8">
                                        <div class="btn-group div-inline input-group input-group-sm input-append date">
                                            <input path="barcode" name="barcode" id="barcode"
                                                   class="form-control" readonly="true"
                                                   autocomplete="off" type="text"/>
                                        </div>
                                    </div>
                                </div>
                                <div class="form-group row">
                                    <div class="col-lg-3">
                                        <label>Result:</label>
                                    </div>
                                    <div class="col-lg-8">
                                        <select id="resultId" onchange="checkResultType(this)" name="resultId"
                                                class="form-control">
                                        </select>
                                    </div>
                                </div>
                                <div id="ct_txt">
                                    <div class="form-group row">
                                        <div class="col-lg-3">
                                            <label>CT1:</label>
                                        </div>
                                        <div class="col-lg-8">
                                            <div class="btn-group div-inline input-group input-group-sm input-append date">
                                                <input path="ct1" name="ct1" id="ct1"
                                                       class="form-control"
                                                       autocomplete="off" type="text" maxlength="6"/>
                                            </div>
                                        </div>
                                    </div>
                                    <div class="form-group row">
                                        <div class="col-lg-3">
                                            <label>CT2:</label>
                                        </div>
                                        <div class="col-lg-8">
                                            <div class="btn-group div-inline input-group input-group-sm input-append date">
                                                <input path="ct2" name="ct2" id="ct2"
                                                       class="form-control"
                                                       autocomplete="off" type="text" maxlength="6"/>
                                            </div>
                                        </div>
                                    </div>
                                </div>

                                <div id="rej_remark_txt">
                                    <div class="form-group row">
                                        <div class="col-lg-3">
                                            <label>REMARK:</label>
                                        </div>
                                        <div class="col-lg-8">
                                            <div class="btn-group div-inline input-group input-group-sm input-append date">
                                                <input path="remark" name="remark" id="remark"
                                                       class="form-control"
                                                       autocomplete="off" type="text"/>
                                            </div>
                                        </div>
                                    </div>
                                </div>
                            </div>
                            <div class="card-footer">
                                <div class="row">
                                    <div class="col-lg-6">
                                        <button type="button" class="btn btn-primary mr-2" onclick="updateValidation()">
                                            Update
                                        </button>
                                        <button type="button" class="btn btn-secondary"
                                                data-dismiss="modal" onclick="cancel()">
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