<%--
  Created by IntelliJ IDEA.
  User: akila
  Date: 5/11/2021
  Time: 2:42 PM
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>

<div class="modal fade" id="modalUpdateInstitution" data-backdrop="static"  tabindex="-1" role="dialog" aria-labelledby="modalUpdateInstitutionLabel" aria-hidden="true">
    <div class="modal-dialog modal-dialog-centered modal-lg" role="document">
        <div class="modal-content">
            <div class="modal-header">
                <h6 class="modal-title" id="modalUpdateInstitutionLabel">Update Institution</h6>
                <button type="button"  id="addPopupClose" class="close" data-dismiss="modal" aria-label="Close" >
                    <i aria-hidden="true" class="ki ki-close"></i>
                </button>
            </div>

            <form:form class="form-horizontal sm" id="updateInstitutionForm" modelAttribute="institution" method="post"
                       name="updateInstitutionForm">
                <div class="modal-body">
                    <div class="form-group"><span id="responseMsgUpdate"></span></div>

                    <div class="form-group row" hidden="true">
                        <label for="userTask" class="col-sm-4 col-form-label">User Task<span
                                class="text-danger">*</span></label>
                        <div class="col-sm-8">
                            <form:input path="userTask" name="userTask" type="text" class="form-control form-control-sm"
                                        id="eUserTask" value="UPDATE" placeholder="User Task"/>
                        </div>
                    </div>

                    <div class="form-group row">
                        <div class="col-lg-4">
                            <label>Institution Code<span class="text-danger">*</span></label>
                            <form:input path="institutionCode" name="userName" type="text"
                                        class="form-control form-control-sm"
                                        id="eInstitutionCode" maxlength="16" placeholder="Institution Code"
                                        onkeyup="$(this).val($(this).val().replace(/[^a-zA-Z0-9]/g,''))" readonly="true"/>
                            <span class="form-text text-muted"></span>
                        </div>
                        <div class="col-lg-4">
                            <label>Institution Name<span class="text-danger">*</span></label>
                            <form:input path="institutionName" name="institutionName" type="text"
                                        onkeyup="$(this).val($(this).val().replace(/[^a-zA-Z0-9 ]/g,''))"
                                        class="form-control form-control-sm" maxlength="256"
                                        id="eInstitutionName" placeholder="Institution Name"/>
                            <span class="form-text text-muted">Please enter institution name</span>
                        </div>
                        <div class="col-lg-4">
                            <label>Status<span class="text-danger">*</span></label>
                            <form:select path="status" name="status" class="form-control form-control-sm"
                                         id="eStatus"
                                         readonly="true">
                                <c:forEach items="${institution.statusList}" var="status">
                                    <form:option value="${status.statusCode}">${status.description}</form:option>
                                </c:forEach>
                            </form:select>
                            <span class="form-text text-muted">Please enter Status</span>
                        </div>
                    </div>

                    <div class="form-group row">
                        <div class="col-lg-4">
                            <label>Address<span class="text-danger">*</span></label>
                            <form:input path="address" name="address" type="text"
                                        onkeyup="" class="form-control form-control-sm" maxlength="512"
                                        id="eAddress" placeholder="Address"/>
                            <span class="form-text text-muted">Please enter Address</span>
                        </div>

                        <div class="col-lg-4">
                            <label>Contact Number<span class="text-danger">*</span></label>
                            <form:input path="contactNumber" name="contactNumber" type="text"
                                        onkeyup="$(this).val($(this).val().replace(/[^\d]/ig, ''))"
                                        class="form-control form-control-sm" maxlength="10"
                                        id="eContactNumber" placeholder="Contact Number"/>
                            <span class="form-text text-muted">Please enter Contact Number</span>
                        </div>

                        <div class="col-lg-4">

                        </div>
                    </div>

                    <div class="form-group">
                        <span class="text-danger">Required fields are marked by the '*'</span>
                    </div>

                </div>
                <div class="modal-footer justify-content-end">
                    <button id="updateReset" type="button" class="btn btn-default" onclick="resetUpdate()">Reset
                    </button>
                    <button id="updateBtn" type="button" onclick="update()" class="btn btn-primary">
                        Submit
                    </button>
                </div>
            </form:form>
        </div>
    </div>
</div>

<script type="text/javascript">

    function update() {
        resetUpdateInstitutionForm();
        $.ajax({
            type: 'POST',
            url: '${pageContext.request.contextPath}/updateInstitution.json',
            data: $('form[name=updateInstitutionForm]').serialize(),
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
                    searchStart();
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
            url: "${pageContext.request.contextPath}/getInstitution.json",
            data: {
                institutionCode: $('#eInstitutionCode').val()
            },
            dataType: "json",
            type: 'GET',
            contentType: "application/json",
            success: function (data) {
                $('#eInstitutionCode').val(data.institutionCode);
                $('#eInstitutionCode').attr('readOnly', true);
                $('#eInstitutionName').val(data.institutionName);
                $('#eAddress').val(data.address);
                $('#eContactNumber').val(data.contactNumber);
                $('#eStatus').val(data.status);
            },
            error: function (data) {
                window.location = "${pageContext.request.contextPath}/logout.htm";
            }
        });
        resetUpdateInstitutionForm();
    }

    function resetUpdateInstitutionForm() {
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
