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
            if (selectedDate && Object.keys(platesNum).length > 0) {
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

        function merge() {
            if ($('#kt_datepicker_1').val()) {
                selectedDate = $('#kt_datepicker_1').val();
                $.ajax({
                    type: 'POST',
                    url: '${pageContext.request.contextPath}/generateDefaultPlate.json',
                    data: {receivedDate: $('#kt_datepicker_1').val()},
                    success: function (res) {
                        _storeMergePlate(platesNum);
                    }
                });
            }
        }

        function _storeMergePlate(platesArray) {
            // get modulus
            let module = Object.keys(platesArray).length % 93;
            // get plate count
            let round = Math.floor(Object.keys(platesArray).length / 93);

            // plate count final
            if (module != 0) {
                round++;
            }
            let count = 0;
            // plate rounds
            for (let k = 0; k < round; k++) {
                var selectedPlate = document.getElementById("checkbox-" + (k + 1) + "");
                if (selectedPlate.checked) {
                    count++;
                }
            }
            if (count <= 1) {
                swal.fire({
                    text: "Need a minimum of 2 plates to pool",
                    icon: "error",
                    buttonsStyling: false,
                    confirmButtonText: "OK",
                    customClass: {
                        confirmButton: "btn font-weight-bold btn-light-primary"
                    }
                });
            } else if (count > 4) {
                swal.fire({
                    text: "Maximum number of plates which can be pooled together is 4",
                    icon: "error",
                    buttonsStyling: false,
                    confirmButtonText: "OK",
                    customClass: {
                        confirmButton: "btn font-weight-bold btn-light-primary"
                    }
                });
            } else {
                _processMergingPlate(platesArray) ;
            }
        }

        function _processMergingPlate(platesArray) {
            let mergedArr = [];
            let finalizedMergedArr = [];
            $(this).addClass("active");
            // get modulus
            let module = Object.keys(platesArray).length % 93;
            // get plate count
            let round = Math.floor(Object.keys(platesArray).length / 93);

            // plate count final
            if (module != 0) {
                round++;
            }
            let shift = 0;
            let shift_val = 0;

            // plate rounds
            for (let k = 0; k < round; k++) {
                let val;
                let tmpArr = [];
                var selectedPlate = document.getElementById("checkbox-" + (k + 1) + "");
                for (let j = 0; j < 8; j++) { // columns
                    for (let i = 0; i < 12; i++) { // rows
                        val = i + (12 * j); // change normal filling
                        if ((val + 1) !== 84 && (val + 1) !== 96) {
                            if (platesArray[val + shift + shift_val] !== undefined) {
                                if (selectedPlate.checked) {
                                    tmpArr.push(platesArray[val + shift + shift_val][0]['labcode']);
                                }
                            }
                        } else {
                            shift_val--;
                        }
                    }
                }
                shift += 96;
                if (selectedPlate.checked) {
                    if (mergedArr.length === 0) {
                        mergedArr = tmpArr;
                    } else {
                        mergedArr = mergedArr.map((value, index) => {
                            return [value, tmpArr[index]]
                        });
                    }
                }
            }
            for (let i = 0; i < mergedArr.length; i++) {
                finalizedMergedArr.push(mergedArr[i].flat(Infinity))
            }
            this._updateMergeDatabase(finalizedMergedArr);
        }

        function _updateMergeDatabase(mergeArray) {
            $.ajax({
                type: 'POST',
                url: '${pageContext.request.contextPath}/mergeBlockPlate.json',
                contentType: "application/json",
                data: JSON.stringify(Object.assign({}, mergeArray)),
                dataType: 'json',
                success: function (res) {
                    swal.fire({
                        text: "Successful",
                        icon: "success",
                        buttonsStyling: false,
                        confirmButtonText: "Exit",
                        customClass: {
                            confirmButton: "btn font-weight-bold btn-light-primary"
                        }
                    })
                },
                error: function (jqXHR) {
                    swal.fire({
                        text: "Error occurred while processing.",
                        icon: "error",
                        buttonsStyling: false,
                        confirmButtonText: "Exit",
                        customClass: {
                            confirmButton: "btn font-weight-bold btn-light-primary"
                        }
                    });
                }
            });
        }

        function swap() {
            const swap = [];
            let swapArray = {}
            let activeElements = $('.cell-elmt.active');
            $.each(activeElements, function (x, y) {
                swapArray[y.dataset.key] = y.dataset.value;
                swap.push(y.dataset.value);
            });

            let swapModel = {"labCode1": swap[0], "labCode2": swap[1]};

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
                            _updateSwapDatabase(platesNum, swapModel);
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

        function _updateSwapDatabase(plateArray, updateArray) {
            $.ajax({
                type: 'POST',
                url: '${pageContext.request.contextPath}/swapBlockPlate.json',
                contentType: "application/json",
                data: JSON.stringify(updateArray),
                dataType: 'json',
                success: function (res) {
                    swal.fire({
                        text: "Successful",
                        icon: "success",
                        buttonsStyling: false,
                        confirmButtonText: "Exit",
                        customClass: {
                            confirmButton: "btn font-weight-bold btn-light-primary"
                        }
                    })
                },
                error: function (jqXHR) {
                    swal.fire({
                        text: "Error occurred while processing.",
                        icon: "error",
                        buttonsStyling: false,
                        confirmButtonText: "Exit",
                        customClass: {
                            confirmButton: "btn font-weight-bold btn-light-primary"
                        }
                    });
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
                                <label>Pool selected tables</label>
                                <button type="button" class="btn btn-success btn-hover-light btn-block"
                                        onclick="merge()">Pool
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
            data-original-title="Merge selected tables">
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
