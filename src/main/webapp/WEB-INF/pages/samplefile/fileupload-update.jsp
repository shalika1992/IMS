<%--
  Created by IntelliJ IDEA.
  User: shalika_w
  Date: 5/10/2021
  Time: 3:17 PM
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>

<div class="modal fade" id="modalUpdateSampleRecord" data-backdrop="static">
    <div class="modal-dialog modal-dialog-centered">
        <div class="modal-content">
            <div class="modal-header">
                <h6 class="modal-title">Update Sample Record</h6>
                <button type="button" id="addPopupClose" class="close" data-dismiss="modal" aria-label="Close"
                        onclick="resetAdd()">
                    <i aria-hidden="true" class="ki ki-close"></i>
                </button>
            </div>

            <form:form class="form-horizontal sm" id="updateSampleRecordForm" modelAttribute="samplefile" method="post"
                       name="updateSampleRecordForm">
                <div class="modal-body">
                    <div class="form-group"><span id="responseMsgUpdate"></span></div>

                    <div class="form-group row" hidden="true">
                        <label class="col-sm-4 col-form-label">
                            ID<span class="text-danger">*</span>
                        </label>
                        <div class="col-sm-8">
                            <form:input path="id" name="id" type="text" class="form-control form-control-sm"
                                        id="eid" maxlength="8" placeholder="ID"/>
                        </div>
                    </div>

                    <div class="form-group row">
                        <label class="col-sm-4 col-form-label">Reference No<span
                                class="text-danger">*</span></label>
                        <div class="col-sm-8">
                            <form:input path="referenceNo" name="referenceNo" type="text"
                                        class="form-control form-control-sm" maxlength="64"
                                        id="eReferenceNo" placeholder="Reference No" readonly="true"/>
                        </div>
                    </div>


                    <div class="form-group row">
                        <label class="col-sm-4 col-form-label">Name<span
                                class="text-danger">*</span></label>
                        <div class="col-sm-8">
                            <form:input path="name" name="name" type="text"
                                        class="form-control form-control-sm" maxlength="256"
                                        id="eName" placeholder="Name"/>
                        </div>
                    </div>

                    <div class="form-group row">
                        <label class="col-sm-4 col-form-label">Age<span
                                class="text-danger">*</span></label>
                        <div class="col-sm-8">
                            <form:input path="age" name="age" type="text"
                                        class="form-control form-control-sm" maxlength="8"
                                        id="eAge" placeholder="Age"/>
                        </div>
                    </div>

                    <div class="form-group row">
                        <label class="col-sm-4 col-form-label">Gender<span
                                class="text-danger">*</span></label>
                        <div class="col-sm-8">
                            <form:select path="gender" name="gender" class="form-control form-control-sm" id="eGender"
                                         readonly="true">
                                <form:option value="M">Male</form:option>
                                <form:option value="F">FeMale</form:option>
                            </form:select>
                        </div>
                    </div>

                    <div class="form-group row">
                        <label class="col-sm-4 col-form-label">Nic<span
                                class="text-danger">*</span></label>
                        <div class="col-sm-8">
                            <form:input path="nic" name="nic" type="text"
                                        class="form-control form-control-sm" maxlength="16"
                                        id="eNic" placeholder="Nic"/>
                        </div>
                    </div>

                    <div class="form-group row">
                        <label class="col-sm-4 col-form-label">Address<span
                                class="text-danger">*</span></label>
                        <div class="col-sm-8">
                            <form:input path="address" name="address" type="text"
                                        class="form-control form-control-sm" maxlength="512"
                                        id="eAddress" placeholder="Address"/>
                        </div>
                    </div>

                    <div class="form-group row">
                        <label class="col-sm-4 col-form-label">District<span
                                class="text-danger">*</span></label>
                        <div class="col-sm-8">
                            <form:select path="residentDistrict" name="residentDistrict"
                                         class="form-control form-control-sm" id="eDistrict" readonly="true">
                                <c:forEach items="${samplefile.districtList}" var="district">
                                    <form:option value="${district.code}">${district.description}</form:option>
                                </c:forEach>
                            </form:select>
                        </div>
                    </div>

                    <div class="form-group row">
                        <label class="col-sm-4 col-form-label">Contact No<span
                                class="text-danger">*</span></label>
                        <div class="col-sm-8">
                            <form:input path="contactNumber" name="contactNumber" type="text"
                                        class="form-control form-control-sm" maxlength="16"
                                        id="eContactno" placeholder="Contact No"/>
                        </div>
                    </div>

                    <div class="form-group row">
                        <label class="col-sm-4 col-form-label">Secondary Contact No<span
                                class="text-danger">*</span></label>
                        <div class="col-sm-8">
                            <form:input path="secondaryContactNumber" name="secondaryContactNumber" type="text"
                                        class="form-control form-control-sm" maxlength="16"
                                        id="eSecondaryContactNo" placeholder="Secondary Contact No"/>
                        </div>
                    </div>

                </div>
                <div class="modal-footer justify-content-end">
                    <button id="updateReset" type="button" class="btn btn-default" onclick="resetUpdate()">
                        Reset
                    </button>
                    <button id="updateBtn" type="button" onclick="update()" class="btn btn-primary">
                        Submit
                    </button>
                </div>

            </form:form>
        </div>
    </div>
</div>
<script>
    function update() {
        $.ajax({
            type: 'POST',
            url: '${pageContext.request.contextPath}/updateSampleFileRecord.json',
            data: $('form[name=updateSampleRecordForm]').serialize(),
            beforeSend: function (xhr) {
                if (header && token) {
                    xhr.setRequestHeader(header, token);
                }
            },
            success: function (res) {
                if (res.flag) {
                    //success
                    $('#responseMsgUpdate').show();
                    $('#responseMsgUpdate').addClass('success-response').text(res.successMessage);
                    search();
                } else {
                    $('#responseMsgUpdate').show();
                    $('#responseMsgUpdate').addClass('error-response').text(res.errorMessage);
                }
            },
            error: function (jqXHR) {
                window.location = "${pageContext.request.contextPath}/logout.htm";
            }
        });
    }

    function resetUpdate() {
        $.ajax({
            url: "${pageContext.request.contextPath}/getSampleFileRecord.json",
            data: {
                id: $('#eid').val()
            },
            dataType: "json",
            type: 'GET',
            contentType: "application/json",
            success: function (data) {
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

            },
            error: function (data) {
                window.location = "${pageContext.request.contextPath}/logout.htm";
            }
        });
        resetUpdateSampleRecordForm();
    }

    function resetUpdateSampleRecordForm() {
        $(".validation-err").remove();
        if ($('#responseMsgUpdate').hasClass('success-response')) {
            $('#responseMsgUpdate').removeClass('success-response');
        }
        if ($('#responseMsgUpdate').hasClass('error-response')) {
            $('#responseMsgUpdate').removeClass('error-response');
        }
        $('#responseMsgUpdate').hide();
    }
</script>