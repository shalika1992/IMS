<%--
  Created by IntelliJ IDEA.
  User: maheshi_c
  Date: 5/19/2021
  Time: 10:45 PM
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>

<!-- delete modal popup start -->
<div id="modalInvalid" class="modal fade" data-backdrop="static">
    <div class="modal-dialog modal-dialog-centered">
        <div class="modal-content">
            <div class="modal-header">
                <h6 class="modal-title">Are you sure?</h6>
                <button type="button"  id="addPopupClose" class="close" data-dismiss="modal" aria-label="Close" >
                    <i aria-hidden="true" class="ki ki-close"></i>
                </button>
            </div>
            <!-- end of send hidden fields to backend -->
            <div class="modal-body">
                <p>Do you want to sure confirm these records as invalid?</p>
            </div>
            <div class="modal-footer justify-content-end">
                <button type="button" class="btn btn-default" data-dismiss="modal">Cancel</button>
                <button id="deleteBtn" type="button" onclick="markAsInvalid()" class="btn btn-primary">
                    yes
                </button>
            </div>

        </div>
        <!-- /.modal-content -->
    </div>
    <!-- /.modal-dialog -->
</div>

