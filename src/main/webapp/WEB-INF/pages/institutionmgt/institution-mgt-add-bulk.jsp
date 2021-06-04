<%--
  Created by IntelliJ IDEA.
  User: akila
  Date: 5/12/2021
  Time: 1:58 PM
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>

<div class="modal fade" id="modalAddInstitutionBulk" data-backdrop="static" tabindex="-1" role="dialog"
     aria-labelledby="modalAddInstitutionBulkLabel" aria-hidden="true">
    <div class="modal-dialog modal-dialog-centered modal-lg" role="document">
        <div class="modal-content">
            <div class="modal-header">
                <h6 class="modal-title" id="modalAddInstitutionBulkLabel">Upload Excel File</h6>
                <button type="button" id="addPopupClose" class="close" data-dismiss="modal" aria-label="Close"
                        onclick="resetAddBulk()">
                    <i aria-hidden="true" class="ki ki-close"></i>
                </button>
            </div>
            <form:form class="form-horizontal sm" id="addInstitutionBulkForm" enctype="multipart/form-data" modelAttribute="institution" method="post"
                       name="addInstitutionBulkForm">
                <div class="modal-body">
                    <div class="card-body">
                        <div class="form-group"><span id="responseMsgAddBulk"></span></div>

                        <div class="form-group row" hidden="true">
                            <label for="userTask" class="col-sm-4 col-form-label">User Task<span
                                    class="text-danger">*</span></label>
                            <div class="col-sm-8">
                                <form:input path="userTask" name="userTask" type="text" class="form-control form-control-sm"
                                            id="abUserTask" value="ADD_BULK" placeholder="User Task"/>
                            </div>
                        </div>

                        <div class="form-group row">
                            <div class="col-lg-4">
                                <label>Excel File<span class="text-danger">*</span></label>
                                <form:input path="institutionBulk" name="institutionBulk" type="file"
                                            class="form-control form-control-sm"
                                            style="padding: 0.4rem 1rem;"
                                            id="abInstitutionBulk" maxlength="16" placeholder="Upload excel file" accept=".xls,.xlsx"/>
                                <span class="form-text text-muted">Upload excel file type .xls or .xlsx</span>
                            </div>
                        </div>

                        <div class="form-group">
                            <span class="text-danger">Required fields are marked by the '*'</span>
                        </div>
                    </div>

                </div>
                <div class="modal-footer justify-content-end">
                    <button id="addReset" type="button" class="btn btn-default" onclick="resetAddBulk()">Reset</button>
                    <button id="addBtn" type="button" onclick="addBulk()" class="btn btn-primary">
                        Submit
                    </button>
                </div>
            </form:form>
        </div>
    </div>
</div>

<script>
    function resetAddBulk() {
        $('form[name=addInstitutionBulkForm]').trigger("reset");
        $('#responseMsgAddBulk').hide();
    }

    function addBulk() {
        resetAddInstitutionBulkError();

        let formData = new FormData();

        formData.append('userTask', $('#abUserTask').val());
        formData.append('institutionBulk', $('#abInstitutionBulk')[0].files[0]);
        console.log(formData)

        $.ajax({
            type: 'POST',
            url: '${pageContext.request.contextPath}/addInstitutionBulk.json',
            data: formData,
            processData: false,
            contentType: false,
            beforeSend: function (xhr) {
                if (header && token) {
                    xhr.setRequestHeader(header, token);
                }
            },
            success: function (res) {
                if (res.flag) {
                    // handle success response

                    $('#responseMsgAddBulk').show();
                    $('#responseMsgAddBulk').addClass('success-response').text(res.successMessage);
                    $('form[name=addInstitutionBulkForm]').trigger("reset");
                    searchStart();
                } else {
                    $('#responseMsgAddBulk').show();
                    $('#responseMsgAddBulk').addClass('error-response').text(res.errorMessage);
                }
            },
            error: function (jqXHR) {
                window.location = "${pageContext.request.contextPath}/logout.htm";
            }
        });
    }

    function resetAddInstitutionBulkError() {
        if ($('#responseMsgAddBulk').hasClass('success-response')) {
            $('#responseMsgAddBulk').removeClass('success-response');
        }
        if ($('#responseMsgAddBulk').hasClass('error-response')) {
            $('#responseMsgAddBulk').removeClass('error-response');
        }
        $('#responseMsgAddBulk').hide();
    }
</script>
