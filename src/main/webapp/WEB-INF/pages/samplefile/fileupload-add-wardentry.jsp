<%--
  Created by IntelliJ IDEA.
  User: shalika_w
  Date: 5/21/2021
  Time: 4:14 PM
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>

<div class="modal fade" id="modalAddWardEntry" data-backdrop="static">
    <div class="modal-dialog modal-dialog-centered">
        <div class="modal-content">
            <div class="modal-header">
                <h6 class="modal-title" id="modalAddWardEntryLabel">Insert Sample Ward Entry</h6>
                <button type="button" id="addPopupClose" class="close" data-dismiss="modal" aria-label="Close"
                        onclick="resetEntry()">
                    <i aria-hidden="true" class="ki ki-close"></i>
                </button>
            </div>

            <form:form class="form-horizontal sm" id="sampleWardEntryForm" modelAttribute="samplefile" method="post"
                       name="sampleWardEntryForm">
                <div class="modal-body">
                    <div class="card-body">
                        <div class="form-group"><span id="responseMsgEntry"></span></div>
                        <div class="form-group row" hidden="true">
                            <label for="userTask" class="col-sm-4 col-form-label">User Task<span
                                    class="text-danger">*</span></label>
                            <div class="col-sm-8">
                                <form:input path="userTask" name="userTask" type="text"
                                            class="form-control form-control-sm"
                                            id="aUserTask" value="ADD" placeholder="User Task"/>
                            </div>
                        </div>

                        <div class="form-group row">
                            <label class="col-sm-4 col-form-label">Reference No<span
                                    class="text-danger">*</span></label>
                            <div class="col-sm-8">
                                <form:input path="referenceNo" name="referenceNo" type="text"
                                            class="form-control form-control-sm" maxlength="64"
                                            id="aReferenceNo" placeholder="Reference No"/>
                            </div>
                        </div>

                        <div class="form-group row">
                            <label class="col-sm-4 col-form-label">Institution Code<span
                                    class="text-danger">*</span></label>
                            <div class="col-sm-8">
                                <form:select path="institutionCode" name="institutionCode"
                                             class="form-control form-control-sm" id="aInstitutionCode" readonly="true">
                                    <c:forEach items="${samplefile.institutionList}" var="institution">
                                        <form:option
                                                value="${institution.institutionCode}">${institution.institutionName}</form:option>
                                    </c:forEach>
                                </form:select>
                            </div>
                        </div>

                        <div class="form-group row">
                            <label class="col-sm-4 col-form-label">Name<span
                                    class="text-danger">*</span></label>
                            <div class="col-sm-8">
                                <form:input path="name" name="name" type="text"
                                            class="form-control form-control-sm" maxlength="256"
                                            id="aName" placeholder="Name"/>
                            </div>
                        </div>

                        <div class="form-group row">
                            <label class="col-sm-4 col-form-label">Age<span
                                    class="text-danger">*</span></label>
                            <div class="col-sm-8">
                                <form:input path="age" name="age" type="text"
                                            class="form-control form-control-sm" maxlength="8"
                                            id="aAge" placeholder="Age"/>
                            </div>
                        </div>

                        <div class="form-group row">
                            <label class="col-sm-4 col-form-label">Gender<span
                                    class="text-danger">*</span></label>
                            <div class="col-sm-8">
                                <form:select path="gender" name="gender" class="form-control form-control-sm"
                                             id="aGender">
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
                                            id="aNic" placeholder="Nic"/>
                            </div>
                        </div>

                        <div class="form-group row">
                            <label class="col-sm-4 col-form-label">Address<span
                                    class="text-danger">*</span></label>
                            <div class="col-sm-8">
                                <form:input path="address" name="address" type="text"
                                            class="form-control form-control-sm" maxlength="512"
                                            id="aAddress" placeholder="Address"/>
                            </div>
                        </div>

                        <div class="form-group row">
                            <label class="col-sm-4 col-form-label">District<span
                                    class="text-danger">*</span></label>
                            <div class="col-sm-8">
                                <form:select path="residentDistrict" name="residentDistrict"
                                             class="form-control form-control-sm" id="aDistrict">
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
                                            id="aContactno" placeholder="Contact No"/>
                            </div>
                        </div>

                        <div class="form-group row">
                            <label class="col-sm-4 col-form-label">Secondary Contact No<span
                                    class="text-danger">*</span></label>
                            <div class="col-sm-8">
                                <form:input path="secondaryContactNumber" name="secondaryContactNumber" type="text"
                                            class="form-control form-control-sm" maxlength="16"
                                            id="aSecondaryContactNo" placeholder="Secondary Contact No"/>
                            </div>
                        </div>

                        <div class="form-group row">
                            <label class="col-sm-4 col-form-label">Ward No<span
                                    class="text-danger">*</span></label>
                            <div class="col-sm-8">
                                <form:input path="wardNumber" name="wardNumber" type="text"
                                            class="form-control form-control-sm" maxlength="4"
                                            id="aWardNumber" placeholder="Ward Number"/>
                            </div>
                        </div>
                    </div>
                </div>

                <div class="modal-footer justify-content-end">
                    <button id="entryReset" type="button" class="btn btn-default" onclick="resetEntry()">
                        Reset
                    </button>
                    <button id="entryBtn" type="button" onclick="addWardEntry()" class="btn btn-primary">
                        Submit
                    </button>
                </div>
            </form:form>
        </div>
    </div>
</div>
<script>
    function resetEntry() {
        $('form[name=sampleWardEntryForm]').trigger("reset");
        $('#responseMsgEntry').hide();
    }

    function addWardEntry() {
        $.ajax({
            type: 'POST',
            url: '${pageContext.request.contextPath}/addSampleWardEntry.json',
            data: $('form[name=sampleWardEntryForm]').serialize(),
            beforeSend: function (xhr) {
                if (header && token) {
                    xhr.setRequestHeader(header, token);
                }
            },
            success: function (res) {
                if (res.flag) {
                    // handle success response
                    $('#responseMsgEntry').show();
                    $('#responseMsgEntry').addClass('success-response').text(res.successMessage);
                    $('form[name=sampleWardEntryForm]').trigger("reset");
                    search();
                } else {
                    $('#responseMsgEntry').show();
                    $('#responseMsgEntry').addClass('error-response').text(res.errorMessage);
                }
            },
            error: function (jqXHR) {
                window.location = "${pageContext.request.contextPath}/logout.htm";
            }
        });
    }
</script>

