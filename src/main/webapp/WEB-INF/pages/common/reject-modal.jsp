<%--
  Created by IntelliJ IDEA.
  User: dilanka_w
  Date: 1/20/2021
  Time: 3:43 PM
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>

<!-- reject modal popup start -->

<div id="modalRejectCommon" class="modal fade" data-backdrop="static">
    <div class="modal-dialog modal-dialog-centered">
        <div class="modal-content">
            <div class="modal-header bg-info">
                <h6 class="modal-title">Are you sure?</h6>
            </div>
            <!-- send hidden fields to backend -->
            <div hidden>
                <input name="id" id="idReject"/>
            </div>
            <!-- end of send hidden fields to backend -->
            <div class="modal-body">
                <p>Do you want to reject this record?</p>
            </div>
            <div class="modal-footer justify-content-end">
                <button type="button" class="btn btn-default" data-dismiss="modal">Cancel</button>
                <%--<c:if test="${userPrivileges.deleteTask}">--%>
                <button id="confirmBtn" type="button" onclick="rejectCommon()" class="btn btn-primary">
                    yes
                </button>
                <%--</c:if>--%>
            </div>
        </div>
        <!-- /.modal-content -->
    </div>
    <!-- /.modal-dialog -->
</div>
<!-- reject modal popup end -->
<!-- reject modal success/error popup start -->
<div id="modalRejectProcessCommon" class="modal fade" data-backdrop="static">
    <div class="modal-dialog modal-dialog-centered">
        <div class="modal-content">
            <div class="modal-header bg-info">
                <h6 class="modal-title">Reject Process</h6>
            </div>
            <div class="modal-body">
                <div class="form-group"><span id="responseMsgReject"></span></div>
            </div>
            <div class="modal-footer justify-content-end">
                <button type="button" class="btn btn-default" data-dismiss="modal">Close</button>
            </div>
        </div>
        <!-- /.modal-content -->
    </div>
    <!-- /.modal-dialog -->
</div>
<!-- confirm modal success/error popup end -->

