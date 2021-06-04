<%--
  Created by IntelliJ IDEA.
  User: shalika_w
  Date: 5/31/2021
  Time: 7:42 PM
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>

<!-- delete modal popup start -->
<div id="modalGenerateLabCode" class="modal fade" data-backdrop="static">
    <div class="modal-dialog modal-dialog-centered">
        <div class="modal-content">
            <div class="modal-header">
                <h6 class="modal-title">Generate Lab Code for Sample Data</h6>
                <button type="button" id="responsePopupClose" class="close" data-dismiss="modal" aria-label="Close">
                    <i aria-hidden="true" class="ki ki-close"></i>
                </button>
            </div>
            <!-- end of send hidden fields to backend -->
            <div class="modal-body">
                <input id="labCode" name="labCode" type="text"
                       maxlength="10"
                       class="form-control form-control-sm" placeholder="Initial Lab Code">

                <span class="form-text text-muted">Please enter initial lab code</span>
            </div>
            <div class="modal-footer justify-content-end">
                <button type="button" class="btn btn-default" data-dismiss="modal">Cancel</button>
                <button id="generateLabCodeBtn" type="button" onclick="generateLabCodes()" class="btn btn-primary">
                    yes
                </button>
            </div>
        </div>
        <!-- /.modal-content -->
    </div>
    <!-- /.modal-dialog -->
</div>
