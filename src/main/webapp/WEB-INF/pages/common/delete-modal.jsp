<%--
  Created by IntelliJ IDEA.
  User: dilanka_w
  Date: 1/19/2021
  Time: 11:31 AM
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>

<!-- delete modal popup start -->

<div id="modalDeleteCommon" class="modal fade" data-backdrop="static">
    <div class="modal-dialog modal-dialog-centered">
        <div class="modal-content">
            <div class="modal-header">
                <h6 class="modal-title">Are you sure?</h6>
                <button type="button"  id="addPopupClose" class="close" data-dismiss="modal" aria-label="Close" >
                    <i aria-hidden="true" class="ki ki-close"></i>
                </button>
            </div>
            <!-- send hidden fields to backend -->
            <div hidden>
                <input name="code" id="deleteCodeCommon"/>
            </div>
            <!-- end of send hidden fields to backend -->
            <div class="modal-body">
                <p>Do you want to delete this record?</p>
            </div>
            <div class="modal-footer justify-content-end">
                <button type="button" class="btn btn-default" data-dismiss="modal">Cancel</button>
                <button id="deleteBtn" type="button" onclick="deleteCommon()" class="btn btn-primary">
                    yes
                </button>
            </div>

        </div>
        <!-- /.modal-content -->
    </div>
    <!-- /.modal-dialog -->
</div>
<!-- delete modal popup end -->
<!-- delete modal success/error popup start -->
<div id="modalDeleteProcessCommon" class="modal fade" data-backdrop="static">
    <div class="modal-dialog modal-dialog-centered">
        <div class="modal-content">
            <div class="modal-header bg-info">
                <h6 class="modal-title">Delete Process</h6>
            </div>
            <div class="modal-body">
                <div class="form-group"><span id="responseMsgDelete"></span></div>
            </div>
            <div class="modal-footer justify-content-end">
                <button type="button" class="btn btn-default" data-dismiss="modal">Close</button>
            </div>
        </div>
        <!-- /.modal-content -->
    </div>
    <!-- /.modal-dialog -->
</div>
<!-- delete modal success/error popup end -->


