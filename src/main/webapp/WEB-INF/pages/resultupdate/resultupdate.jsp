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
            padding: 0;
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

            var rowletter = ['A', 'B', 'C', 'D', 'E', 'F', 'G', 'H'];
            var platedata = [];
            var length = 96;
            for (var i = 0; i < length; i++) {
                platedata.push(i);
            }

            let html = "";
            html += "<div class='row'><div class='card-body result' style='display: inline;width: 56px;height:56px'><div class='col-12 first-elmt'>&nbsp;</div></div>\n";
            for (let y = 1; y < 13; y++) {
                html += "<div class='card-body result' style='display: inline;width: 56px;height:56px;'><div class='col-12 horizontal-elmt'>" + y + " </div>"
                html += "</div>\n";
            }
            html += "</div>\n";

            for (let z = 0; z < platedata.length; z++) {
                let count = z;
                if ((count % 12 === 0)) {
                    let rownumber = count / 12;
                    var letter = rowletter[rownumber];
                    html += "<div class='row'><div class='card-body result' style='display: inline;width: 56px;height:56px'><div class='col-12 vertical-elmt'>" + letter + "</div></div>\n";
                    for (let y = 1; y < 13; y++) {
                        let value = platedata[z + (y - 1)];
                        html += "<div class='card-body result' style='display: inline;width: 56px;height:56px;background-color: #ffffff;'>"
                        html += "<p class='card-title'></p>"
                        html += "<p class='card-text'>" + value + "</p>"
                        html += "</div>\n";
                    }
                    html += "</div>\n";
                }
            }
            $('#resultplate').empty();
            $('#resultplate').append(html);
        });


        function resetSearch() {
            //reset date value
            $("#receivedDate").datepicker('setDate', getReceivedDate());
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
                            <h3 class="card-title">Search Result Update</h3>
                        </div>
                        <!--begin::Form-->
                        <form:form class="form" id="resultupdateform" name="resultupdateform"
                                   action="addResultUpdate" theme="simple" method="post"
                                   modelAttribute="resultupdate">
                            <div class="card-body">
                                <div class="form-group row mb-0">
                                    <div class="col-lg-3">
                                        <label class="label-right">Received Date:</label>
                                        <div class="btn-group div-inline input-group input-group-sm input-append date">
                                            <input path="receivedDate" name="receivedDate" id="receivedDate"
                                                   class="form-control" readonly="true"
                                                   autocomplete="off" type="text" onkeydown="return false"/>
                                        </div>
                                    </div>

                                    <div class="col-lg-3">
                                        <label class="label-right">Plate :</label>
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
                <div class="card-header">
                    <h3 class="card-title">Plate Details</h3>
                </div>
                <div class="card-body">
                    <div class="card card-custom gutter-b" id="resultplate">
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