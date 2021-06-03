<%--
  Created by IntelliJ IDEA.
  User: akila_s
  Date: 5/10/2021
  Time: 3:11 PM
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<div class="modal fade" id="modalAddInstitution" data-backdrop="static" tabindex="-1" role="dialog"
     aria-labelledby="modalAddInstitutionLabel" aria-hidden="true">
    <div class="modal-dialog modal-dialog-centered modal-lg" role="document">
        <div class="modal-content">
            <div class="modal-header">
                <h6 class="modal-title" id="modalAddInstitutionLabel">Insert Institution</h6>
                <button type="button" id="addPopupClose" class="close" data-dismiss="modal" aria-label="Close"
                        onclick="resetAdd()">
                    <i aria-hidden="true" class="ki ki-close"></i>
                </button>
            </div>
            <form:form class="form-horizontal sm" id="addInstitutionForm" modelAttribute="institution" method="post"
                       name="addInstitutionForm">
                <div class="modal-body">
                    <div class="card-body">
                        <div class="form-group"><span id="responseMsgAdd"></span></div>

                        <div class="form-group row" hidden="true">
                            <label for="userTask" class="col-sm-4 col-form-label">User Task<span
                                    class="text-danger">*</span></label>
                            <div class="col-sm-8">
                                <form:input path="userTask" name="userTask" type="text" class="form-control form-control-sm"
                                            id="aUserTask" value="ADD" placeholder="User Task"/>
                            </div>
                        </div>

                        <div class="form-group row">
                            <div class="col-lg-4">
                                <label class="label-right">Institution Code<span class="text-danger">*</span></label>
                                <form:input path="institutionCode" name="userName" type="text"
                                            class="form-control form-control-sm"
                                            id="aInstitutionCode" maxlength="16" placeholder="Institution Code"
                                            onkeyup="$(this).val($(this).val().replace(/[^a-zA-Z0-9]/g,''))"/>
                            </div>
                            <div class="col-lg-4">
                                <label class="label-right">Institution Name<span class="text-danger">*</span></label>
                                <form:input path="institutionName" name="institutionName" type="text"
                                            onkeyup="$(this).val($(this).val().replace(/[^a-zA-Z0-9 ]/g,''))"
                                            class="form-control form-control-sm" maxlength="256"
                                            id="aInstitutionName" placeholder="Institution Name"/>
                            </div>
                            <div class="col-lg-4">
                                <label class="label-right">Address<span class="text-danger">*</span></label>
                                <form:input path="address" name="address" type="text"
                                            onkeyup="" class="form-control form-control-sm" maxlength="512"
                                            id="aAddress" placeholder="Address"/>
                            </div>
                        </div>

                        <div class="form-group row">

                            <div class="col-lg-4">
                                <label class="label-right">Status<span class="text-danger">*</span></label>
                                <form:select path="status" name="status" class="form-control form-control-sm"
                                             id="aStatus"
                                             readonly="true">
                                    <c:forEach items="${institution.statusActList}" var="status">
                                        <form:option value="${status.statusCode}">${status.description}</form:option>
                                    </c:forEach>
                                </form:select>
                            </div>

                            <div class="col-lg-4">
                                <label class="label-right">Contact Number<span class="text-danger">*</span></label>
                                <form:input path="contactNumber" name="contactNumber" type="text"
                                            onkeyup="$(this).val($(this).val().replace(/[^\d]/ig, ''))"
                                            class="form-control form-control-sm" maxlength="10"
                                            id="aContactNumber" placeholder="Contact Number"/>
                            </div>

                            <div class="col-lg-4">

                            </div>
                        </div>


                        <div class="form-group">
                            <span class="text-danger">Required fields are marked by the '*'</span>
                        </div>
                    </div>

                </div>
                <div class="modal-footer justify-content-end">
                    <button id="addReset" type="button" class="btn btn-default" onclick="resetAdd()">Reset</button>
                    <button id="addBtn" type="button" onclick="add()" class="btn btn-primary">
                        Submit
                    </button>
                </div>
            </form:form>
        </div>
    </div>
</div>

<script>
    function resetAdd() {
        $('form[name=addInstitutionForm]').trigger("reset");
        $('#responseMsgAdd').hide();
    }

    function add() {
        resetAddInstitutionError();

        $.ajax({
            type: 'POST',
            url: '${pageContext.request.contextPath}/addInstitution.json',
            data: $('form[name=addInstitutionForm]').serialize(),
            beforeSend: function (xhr) {
                if (header && token) {
                    xhr.setRequestHeader(header, token);
                }
            },
            success: function (res) {
                if (res.flag) {
                    // handle success response
                    $('#responseMsgAdd').show();
                    $('#responseMsgAdd').addClass('success-response').text(res.successMessage);
                    $('form[name=addInstitutionForm]').trigger("reset");
                    searchStart();
                } else {
                    $('#responseMsgAdd').show();
                    $('#responseMsgAdd').addClass('error-response').text(res.errorMessage);
                }
            },
            error: function (jqXHR) {
                window.location = "${pageContext.request.contextPath}/logout.htm";
            }
        });
    }

    function resetAddInstitutionError() {
        if ($('#responseMsgAdd').hasClass('success-response')) {
            $('#responseMsgAdd').removeClass('success-response');
        }
        if ($('#responseMsgAdd').hasClass('error-response')) {
            $('#responseMsgAdd').removeClass('error-response');
        }
        $('#responseMsgAdd').hide();
    }
</script>
