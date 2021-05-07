<%--
  Created by IntelliJ IDEA.
  User: akila_s
  Date: 5/7/2021
  Time: 2:43 PM
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>

<div class="modal fade" id="modalPwdChangeSystemUser" data-backdrop="static"  tabindex="-1" role="dialog" aria-labelledby="modalPwdChangeSystemUser" aria-hidden="true">
    <div class="modal-dialog modal-dialog-centered modal-lg" role="document">
        <div class="modal-content">
            <div class="modal-header">
                <h6 class="modal-title" id="modalPwdChangeSystemUserLabel">Change Password</h6>
                <button type="button"  id="addPopupClose" class="close" data-dismiss="modal" aria-label="Close" >
                    <i aria-hidden="true" class="ki ki-close"></i>
                </button>
            </div>

            <form:form class="form-horizontal sm" id="PwdChangeSystemUserForm" modelAttribute="systemuser" method="post"
                       name="PwdChangeSystemUserForm">
                <div class="modal-body">
                    <div class="form-group"><span id="responseMsgPwdChange"></span></div>

                    <div class="form-group row" hidden="true">
                        <label for="userTask" class="col-sm-4 col-form-label">User Task<span
                                class="text-danger">*</span></label>
                        <div class="col-sm-8">
                            <form:input path="userTask" name="userTask" type="text" class="form-control form-control-sm"
                                        id="cUserTask" value="CHGPW" placeholder="User Task"/>
                        </div>
                    </div>

                    <div class="form-group row" >
                        <div class="col-lg-4" hidden="true">
                            <label>Username<span class="text-danger">*</span></label>
                            <form:input path="userName" name="userName" type="text" class="form-control form-control-sm"
                                        id="cUserName" maxlength="16" placeholder="Username"
                                        onkeyup="$(this).val($(this).val().replace(/[^a-zA-Z0-9]/g,''))"/>
                            <span class="form-text text-muted">Please enter username</span>
                        </div>
                    </div>

                    <div class="form-group row">
                        <div class="col-lg-12">
                            <label>Password<span class="text-danger">*</span></label>
                            <form:input path="password" name="password" type="password"
                                        class="form-control form-control-sm" maxlength="128"
                                        id="cPassword" placeholder="Password"/>
                            <span class="form-text text-muted">Please enter Password</span>
                        </div>

                        <div class="col-lg-12">
                            <label>Confirm Password<span class="text-danger">*</span></label>
                            <form:input path="confirmPassword" name="confirmPassword" type="password"
                                        class="form-control form-control-sm" maxlength="128"
                                        id="cConfirmPassword" placeholder="Confirm Password"/>
                            <span class="form-text text-muted">Please enter Confirm Password</span>
                        </div>
                    </div>

                    <div class="form-group">
                        <span class="text-danger">Required fields are marked by the '*'</span>
                    </div>

                </div>
                <div class="modal-footer justify-content-end">
                    <button id="updateReset" type="button" class="btn btn-default" onclick="resetPwdChangeSystemUserForm()">Reset
                    </button>
                    <button id="updateBtn" type="button" onclick="change()" class="btn btn-primary">
                        Change
                    </button>
                </div>
            </form:form>
        </div>
    </div>
</div>

<script>
    function change() {
        $.ajax({
            type: 'POST',
            url: '${pageContext.request.contextPath}/changePwdSystemUser.json',
            data: $('form[name=PwdChangeSystemUserForm]').serialize(),
            beforeSend: function (xhr) {
                if (header && token) {
                    xhr.setRequestHeader(header, token);
                }
            },
            success: function (res) {
                if (res.flag) {
                    //success
                    $('#responseMsgPwdChange').show();
                    $('#responseMsgPwdChange').addClass('success-response').text(res.successMessage);
                    searchStart();
                } else {
                    $('#responseMsgPwdChange').show();
                    $('#responseMsgPwdChange').addClass('error-response').text(res.errorMessage);
                }
            },
            error: function (jqXHR) {
                window.location = "${pageContext.request.contextPath}/logout.htm";
            }
        });
    }



    function resetPwdChangeSystemUserForm() {
        $(".validation-err").remove();
        if ($('#responseMsgPwdChange').hasClass('success-response')) {
            $('#responseMsgPwdChange').removeClass('success-response');
        }
        if ($('#responseMsgPwdChange').hasClass('error-response')) {
            $('#responseMsgPwdChange').removeClass('error-response');
        }
        $('#responseMsgPwdChange').hide();
    }
</script>
