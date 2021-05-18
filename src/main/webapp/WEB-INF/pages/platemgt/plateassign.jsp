<%--
  Created by IntelliJ IDEA.
  User: shalika_w
  Date: 5/7/2021
  Time: 3:27 PM
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

    <link rel="stylesheet"
          href="${pageContext.request.contextPath}/resources/assets/css/plategenerate/plategenerate.css">
    <script type="text/javascript">

        let platesNum;
        let mergeArray;
        let selectedDate;
        let initArray;
        let haveData;

        function resetSearch() {
            if (selectedDate && Object.keys(platesNum).length>0) {
                swal.fire({
                    text: "Are you sure to proceed the operation? All the submitted data will be lost. Last generated plates will show",
                    // icon: "success",
                    buttonsStyling: false,
                    confirmButtonText: "Proceed",
                    showCancelButton: true,
                    customClass: {
                        confirmButton: "btn font-weight-bold btn-light-primary",
                        cancelButton: "btn font-weight-bold btn-light-primary"
                    }
                }).then((result) => {
                    $("#generateDiv").show();
                    $("#stickyOp").show();
                    $.ajax({
                        type: 'POST',
                        url: '${pageContext.request.contextPath}/generateDefaultPlate.json',
                        data: {receivedDate: selectedDate},
                        success: function (res) {
                            platesNum = res;
                            initArray = res;
                            _generatePlates(platesNum);
                        },
                        error: function (jqXHR) {
                            window.location = "${pageContext.request.contextPath}/logout.htm";
                        }
                    });
                });
            } else {
                $("#generateDiv").hide();
                $("#stickyOp").hide();
            }
        }

        function generatePlates() {

            if ($('#kt_datepicker_1').val()) {

                selectedDate = $('#kt_datepicker_1').val();
                $.ajax({
                    type: 'POST',
                    url: '${pageContext.request.contextPath}/generateDefaultPlate.json',
                    data: {receivedDate: $('#kt_datepicker_1').val()},
                    success: function (res) {
                        platesNum = res;
                        if (Object.keys(res).length > 0) {
                            $("#generateDiv").show();
                            $("#stickyOp").show();
                            _generatePlates(platesNum);
                        } else {
                            $("#generateDiv").hide();
                            $("#stickyOp").hide();
                            swal.fire({
                                text: "No records available for selected date",
                                icon: "error",
                                buttonsStyling: false,
                                confirmButtonText: "OK",
                                customClass: {
                                    confirmButton: "btn font-weight-bold btn-light-primary"
                                }
                            });
                        }
                    },
                    error: function (jqXHR) {
                        window.location = "${pageContext.request.contextPath}/logout.htm";
                    }
                });
            } else {
                $("#generateDiv").hide();
                swal.fire({
                    text: "Please select a date",
                    icon: "error",
                    buttonsStyling: false,
                    confirmButtonText: "Proceed",
                    customClass: {
                        confirmButton: "btn font-weight-bold btn-light-primary"
                    }
                });
            }

        }

        function swap() {

            let swapArray = {}
            let activeElements = $('.cell-elmt.active');
            $.each(activeElements, function (x, y) {
                swapArray[y.dataset.key] = y.dataset.value;
            });

            console.log("Swap array", swapArray);

            if (!$.isEmptyObject(swapArray)) {
                if (Object.keys(swapArray).length == 2) {
                    swal.fire({
                        text: "Are you sure to proceed the operation?",
                        // icon: "success",
                        buttonsStyling: false,
                        confirmButtonText: "Proceed",
                        showCancelButton: true,
                        customClass: {
                            confirmButton: "btn font-weight-bold btn-light-primary",
                            cancelButton: "btn font-weight-bold btn-light-primary"
                        }
                    }).then((result) => {
                        if (result.isConfirmed) {
                            platesNum = _swapCells(platesNum, swapArray);
                            _generatePlates(platesNum);
                            _updateDatabase(platesNum,swapArray,'SWAP');
                        } else {
                            Swal.fire('Changes are not saved', '', 'info');
                        }
                    });

                } else {
                    Swal.fire('Only 2 values can swap', '', 'error');
                }
            } else {
                Swal.fire('No values to swap', '', 'error');
            }

        }

        function merge() {

            let mergeArray = {}
            let activeElements = $('.cell-elmt.active');
            $.each(activeElements, function (x, y) {
                mergeArray[y.dataset.key] = y.dataset.value;
            });

            console.log("Merge Array", mergeArray);

            if (!$.isEmptyObject(mergeArray)) {
                if (Object.keys(mergeArray).length != 1) {
                    swal.fire({
                        text: "Are you sure to proceed the operation?",
                        // icon: "success",
                        buttonsStyling: false,
                        confirmButtonText: "Proceed",
                        showCancelButton: true,
                        customClass: {
                            confirmButton: "btn font-weight-bold btn-light-primary",
                            cancelButton: "btn font-weight-bold btn-light-primary"
                        }
                    }).then((result) => {
                        if (result.isConfirmed) {
                            platesNum = _mergeCells(platesNum, mergeArray);
                            _generatePlates(platesNum);
                            _updateDatabase(platesNum,mergeArray,'MERGE');
                        } else {
                            Swal.fire('Changes are not saved', '', 'info');
                        }
                    });
                } else {
                    Swal.fire('Less values to merge', '', 'error')
                }
            } else {
                Swal.fire('No values to merge', '', 'error');
            }

        }

        function _updateDatabase(plateArray,updateArray,operation) {
            if(operation==='SWAP'){
                let dataS= {"plateArray": plateArray, "swapArray" : updateArray};

                $.ajax({
                    type: 'POST',
                    url: '${pageContext.request.contextPath}/swapBlockPlate.json',
                    contentType: "application/json",
                    data : JSON.stringify(dataS),
                    dataType: 'text',
                    success: function (res) {
                        console.log("message",res);
                        swal.fire({
                            text: res,
                            // icon: "success",
                            buttonsStyling: false,
                            confirmButtonText: "Exit",
                            customClass: {
                                confirmButton: "btn font-weight-bold btn-light-primary"
                            }
                        });

                    },
                    error: function (jqXHR) {
                        <%--window.location = "${pageContext.request.contextPath}/logout.htm";--%>
                    }
                });
            }else if(operation==='MERGE'){
                let dataM= {"plateArray": plateArray, "mergeArray" : updateArray};

                $.ajax({
                    type: 'POST',
                    url: '${pageContext.request.contextPath}/mergeBlockPlate.json',
                    contentType: "application/json",
                    data : JSON.stringify(dataM),
                    dataType: 'text',
                    success: function (res) {
                        console.log(res);
                        swal.fire({
                            text: res,
                            // icon: "success",
                            buttonsStyling: false,
                            confirmButtonText: "Exit",
                            customClass: {
                                confirmButton: "btn font-weight-bold btn-light-primary"
                            }
                        });
                    },
                    error: function (jqXHR) {
                        window.location = "${pageContext.request.contextPath}/logout.htm";
                    }
                });
            }
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
                    <h5 class="text-dark font-weight-bold my-1 mr-5">Plate Management</h5>
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
                    <div class="card card-custom gutter-b">
                        <div class="card-header">
                            <h3 class="card-title">Generate Plate</h3>
                        </div>
                        <!--begin::Form-->
                        <form:form class="form" id="plateform" name="plateform" action=""
                                   theme="simple" method="post" modelAttribute="plate">
                            <div class="card-body">
                                <div class="form-group row">

                                    <div class="col-lg-5">
                                        <label>Plate Generate Date :</label>
                                        <div class="input-group">
                                            <input type="text" class="form-control" id="kt_datepicker_1"
                                                   readonly="readonly" placeholder="Select date"/>
                                            <div class="input-group-append">
                                                <button class="btn btn-success" type="button"
                                                        onclick="generatePlates()">
                                                    Generate
                                                </button>
                                                <button class="btn btn-secondary" type="reset" onclick="resetSearch()">
                                                    Reset
                                                </button>
                                            </div>
                                        </div>
                                        <span class="form-text text-muted">Please Select Date</span>
                                    </div>
                                </div>
                            </div>
                        </form:form>
                        <!--end::Card-->
                    </div>
                </div>
            </div>

            <div class="card card-custom gutter-b" id="generateDiv" style="display: none">
                <div class="card-body">
                    <div class="py-8">
                        <h4>Following operations can be done</h4>
                        <div class="form-group row">
                            <div class="col-lg-3">
                                <label>Merge selected cells</label>
                                <button type="button" class="btn btn-success btn-hover-light btn-block"
                                        onclick="merge()">Merge
                                </button>
                            </div>
                            <div class="col-lg-3">
                                <label>Swap selected cells</label>
                                <button type="button" class="btn btn-success btn-hover-light btn-block"
                                        onclick="swap()">Swap
                                </button>
                            </div>
                        </div>
                        <div id="plates"></div>
                    </div>
                </div>
            </div>
        </div>
    </div>

    <ul class="sticky-toolbar nav flex-column pl-2 pr-2 pt-3 pb-3 mt-4" id="stickyOp" style="display: none">
        <!--begin::Item-->
        <li class="nav-item mb-2" id="kt_demo_panel_toggle" data-toggle="tooltip" title="" data-placement="right"
            data-original-title="Merge selected cells">
            <a class="btn btn-sm btn-icon btn-bg-light btn-icon-success btn-hover-success" href="javascript:void(0);"
               onclick="merge()">
                <i class="flaticon2-size"></i>
            </a>
        </li>
        <!--end::Item-->
        <!--begin::Item-->
        <li class="nav-item mb-2" data-toggle="tooltip" title="" data-placement="left"
            data-original-title="Swap selected cells">
            <a class="btn btn-sm btn-icon btn-bg-light btn-icon-primary btn-hover-primary" href="javascript:void(0);"
               onclick="swap()">
                <i class="flaticon-refresh"></i>
            </a>
        </li>
        <!--end::Item-->
    </ul>

    <script src="${pageContext.request.contextPath}/resources/assets/js/scripts.bundle.js"></script>
    <script src="${pageContext.request.contextPath}/resources/assets/js/plategenerate/plategenerate.js"></script>
    <script>

        var arrows;
        if (KTUtil.isRTL()) {
            arrows = {
                leftArrow: '<i class="la la-angle-right"></i>',
                rightArrow: '<i class="la la-angle-left"></i>'
            }
        } else {
            arrows = {
                leftArrow: '<i class="la la-angle-left"></i>',
                rightArrow: '<i class="la la-angle-right"></i>'
            }
        }

        $('#kt_datepicker_1').datepicker({
            rtl: KTUtil.isRTL(),
            todayHighlight: true,
            orientation: "bottom left",
            templates: arrows,
            autoclose: true,
            endDate: new Date(),
            format: "yyyy-mm-dd"
        });


    </script>
    <!-- start include jsp files -->
    <!-- end include jsp files -->
</html>
