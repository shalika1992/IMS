<%--
  Created by IntelliJ IDEA.
  User: prathibha_w
  Date: 2/11/2021
  Time: 10:41 AM
  To change this template use File | Settings | File Templates.
--%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib uri="http://tiles.apache.org/tags-tiles" prefix="tiles" %>

<head>
    <c:set var="changePwdMode" value="${sessionBean.changePwdMode}"/>
    <c:set var="sectionList" value="${sessionBean.sectionList}"/>
    <c:set var="pageMap" value="${sessionBean.pageMap}"/>
    <c:set var="daysToExpire" value="${sessionBean.daysToExpire}"/>
    <c:set var="user" value="${sessionBean.user}"/>
</head>
<div id="kt_header" class="header header-fixed">
    <!--begin::Container-->
    <div class="container-fluid d-flex align-items-stretch justify-content-between">
        <!--begin::Header Menu Wrapper-->
        <div class="header-menu-wrapper header-menu-wrapper-left" id="kt_header_menu_wrapper">
            <!--begin::Header Menu-->
            <div id="kt_header_menu" class="header-menu header-menu-mobile header-menu-layout-default">
                <ul class="menu-nav">
                    <li class="menu-item menu-item-submenu menu-item-rel menu-item-active" data-menu-toggle="click" aria-haspopup="true">
                        <!-- <div class="menu-link menu-toggle">
                            <span class="menu-text text-combank">IMS</span>
                            <i class="menu-arrow"></i>
                        </div> -->
                </ul>
            </div>
            <!--end::Header Menu-->
        </div>
        <!--end::Header Menu Wrapper-->
        <!--begin::Topbar-->
        <div class="topbar">
            <!--begin::User-->
            <div class="topbar-item">
                <div class="btn btn-icon btn-icon-mobile w-auto btn-clean d-flex align-items-center btn-lg px-2" id="kt_quick_user_toggle">
                    <span class="text-muted font-weight-bold font-size-base d-none d-md-inline mr-1">Welcome,</span>
                    <span class="primary-text-color font-weight-bolder font-size-base d-none d-md-inline mr-3">${user.fullName}</span>
                    <span class="symbol symbol-lg-35 symbol-25 symbol-light-success">
										<span class="symbol-label font-size-h5 font-weight-bold">S</span>
                                    </span>
                </div>
            </div>
            <!--end::User-->
        </div>
        <!--end::Topbar-->
    </div>
    <!--end::Container-->
</div>
