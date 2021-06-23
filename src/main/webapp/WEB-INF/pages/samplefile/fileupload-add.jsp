<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>

<div class="modal fade" id="modalUploadSampleFile" data-backdrop="static">
    <div class="modal-dialog modal-dialog-centered">
        <div class="modal-content">
            <div class="modal-header">
                <h6 class="modal-title">Upload Sample File</h6>
                <button type="button" id="addPopupClose" class="close" data-dismiss="modal" aria-label="Close"
                        onclick="resetAdd()">
                    <i aria-hidden="true" class="ki ki-close"></i>
                </button>
            </div>

            <form:form class="form-horizontal sm" id="sampleFileUploadForm" modelAttribute="samplefile" method="post"
                       name="sampleFileUploadForm" enctype="multipart/form-data">
                <div class="modal-body">
                    <div class="form-group"><span id="responseMsgAddSuccess"></span></div>
                    <div class="form-group"><span id="responseMsgAddError"></span></div>

                    <div class="form-group row">
                        <label for="uReceivedDate" class="col-sm-4 col-form-label">Received Date<span
                                class="text-danger">*</span></label>
                        <div class="col-sm-8 btn-group div-inline input-group input-group-sm input-append date">
                            <input path="receivedDate" name="receivedDate" id="uReceivedDate"
                                   class="form-control" readonly="true" onkeydown="return false"
                                   autocomplete="off" type="text"/>
                        </div>
                    </div>

                    <div class="form-group row">
                        <label for="file" class="col-sm-4 col-form-label">Sample File<span
                                class="text-danger">*</span></label>
                        <div class="col-sm-8">
                            <input type="file" path="sampleFile" name="sampleFile" id="sampleFile"
                                   class="form-control" style="padding: 0.4rem 0.75rem;">
                        </div>
                    </div>
                </div>
                <div class="modal-footer justify-content-end">
                    <button id="addReset" type="button" class="btn btn-default" onclick="resetAdd()">Reset</button>
                    <button id="addBtn" type="button" class="btn btn-primary" onclick="add()">
                        Submit
                    </button>
                </div>
            </form:form>
        </div>
    </div>
</div>
<script>
    $(document).ready(function () {
        $('#uReceivedDate').datepicker({
            format: 'yyyy-mm-dd',
            endDate: '+0d',
            setDate: new Date(),
            todayHighlight: true,
            forceParse: false,
            autoclose: true
        });
        setReceivedDate();
    });

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
        $("#uReceivedDate").datepicker('setDate', today);
    }

    function resetAdd() {
        $('#responseMsgAddSuccess').hide();
        $('#responseMsgAddError').hide();
        $("#sampleFile").val(null);
        setReceivedDate();
    }

    function add() {
        var formData = new FormData();
        formData.append('sampleFile', $('input[type=file]')[0].files[0]);
        formData.append('receivedDate', $("#uReceivedDate").val());
        if ($('input[type=file]')[0].files[0] !== undefined) {
            $.ajax({
                url: '${pageContext.request.contextPath}/sampleFileUpload.json',
                data: formData,
                processData: false,
                contentType: false,
                type: 'POST',
                success: function (res) {
                    if (res.flag) {
                        // handle success response
                        $('#responseMsgAddSuccess').show();
                        $('#responseMsgAddError').hide();
                        $('#responseMsgAddSuccess').addClass('success-response').text(res.successMessage);
                        search();
                    } else {
                        $('#responseMsgAddSuccess').hide();
                        $('#responseMsgAddError').show();
                        $('#responseMsgAddError').addClass('error-response').text(res.errorMessage);
                    }
                    $('form[name=sampleFileUploadForm]').trigger("reset");
                    $("#sampleFile").val(null);
                    setReceivedDate();
                },
                error: function (err) {
                    window.location = "${pageContext.request.contextPath}/logout.htm";
                }
            });
        } else {
            swal.fire({
                text: "Please upload a file to proceed.",
                icon: "error",
                buttonsStyling: false,
                confirmButtonText: "OK",
                customClass: {
                    confirmButton: "btn font-weight-bold btn-light-primary"
                }
            });
        }
    }
</script>
