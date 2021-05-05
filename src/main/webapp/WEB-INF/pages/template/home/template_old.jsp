<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib uri="http://tiles.apache.org/tags-tiles" prefix="tiles" %>

<!DOCTYPE html>
<html>

<head>
    <c:set var="changePwdMode" value="${sessionBean.changePwdMode}"/>
    <c:set var="sectionList" value="${sessionBean.sectionList}"/>
    <c:set var="pageMap" value="${sessionBean.pageMap}"/>
    <c:set var="daysToExpire" value="${sessionBean.daysToExpire}"/>

    <meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">

    <!--css list-->
    <!--jquery datatable-->
    <link href="${pageContext.request.contextPath}/resources/datatable/css/jquery.dataTables.min.css?${initParam['version']}"
          rel="stylesheet" type="text/css"/>

    <link href="${pageContext.request.contextPath}/resources/datatable/css/responsive.dataTables.min.css?${initParam['version']}"
          rel="stylesheet" type="text/css"/>

    <link href="${pageContext.request.contextPath}/resources/datatable/css/rowGroup.dataTables.min.css?${initParam['version']}"
          rel="stylesheet" type="text/css"/>

    <!--bootstrap-->
    <link href="${pageContext.request.contextPath}/resources/bootstrap/css/bootstrap.min.css?${initParam['version']}"
          rel="stylesheet" type="text/css"/>

    <link href="${pageContext.request.contextPath}/resources/bootstrap/css/bootstrap-grid.min.css?${initParam['version']}"
          rel="stylesheet" type="text/css"/>

    <link href="${pageContext.request.contextPath}/resources/bootstrap/css/bootstrap-grid.css?${initParam['version']}"
          rel="stylesheet" type="text/css"/>

    <!--bootstrap date picker-->
    <link href="${pageContext.request.contextPath}/resources/bootstrap/datepicker/css/datepicker.css?${initParam['version']}"
          rel="stylesheet" type="text/css"/>

    <!--bootstrap validator-->
    <link href="${pageContext.request.contextPath}/resources/css/bootstrapvalidator/bootstrapValidator.min.css?${initParam['version']}"
          rel="stylesheet" type="text/css"/>


    <link rel="stylesheet"
          href="https://cdnjs.cloudflare.com/ajax/libs/bootstrap3-dialog/1.34.7/css/bootstrap-dialog.min.css">

    <!--script list-->
    <!--jquery-->
    <script src="${pageContext.request.contextPath}/resources/jquery/js/jquery.min.js?${initParam['version']}"
            type="text/javascript"></script>

    <script src="${pageContext.request.contextPath}/resources/popper/popper.min.js?${initParam['version']}"
            type="text/javascript"></script>

    <!--bootstrap-->
    <script src="${pageContext.request.contextPath}/resources/bootstrap/js/bootstrap.bundle.min.js?${initParam['version']}"
            type="text/javascript"></script>

    <script src="${pageContext.request.contextPath}/resources/bootstrap/js/bootstrap.min.js?${initParam['version']}"
            type="text/javascript"></script>

    <script type="text/javascript"
            src="https://cdnjs.cloudflare.com/ajax/libs/bootstrap3-dialog/1.34.7/js/bootstrap-dialog.min.js"></script>

    <!--bootstrap date picker-->
    <script src="${pageContext.request.contextPath}/resources/bootstrap/datepicker/js/bootstrap-datepicker.js?${initParam['version']}"
            type="text/javascript"></script>


    <script type="text/javascript"
            src="${pageContext.request.contextPath}/resources/datatable/js/jquery.dataTables.min.js"></script>
    <script type="text/javascript"
            src="${pageContext.request.contextPath}/resources/datatable/js/dataTables.fixedColumns.min.js"></script>
    <script type="text/javascript"
            src="${pageContext.request.contextPath}/resources/datatable/js/dataTables.responsive.min.js"></script>

    <!--bootstrap validator-->
    <script src="${pageContext.request.contextPath}/resources/js/bootstrapvalidator/bootstrapValidator.min.js?${initParam['version']}"
            type="text/javascript"></script>

    <!--moment js-->
    <script src="https://cdnjs.cloudflare.com/ajax/libs/moment.js/2.18.1/moment.min.js"></script>

    <title><tiles:getAsString name="title"/></title>
</head>

<body>
<tiles:insertAttribute name="header"/>
<div class="container-fluid">
    <div class="row content">
        <div class="col-sm-3 sidenav">
            <tiles:insertAttribute name="menu"/>
        </div>

        <div class="col-sm-9">
            <tiles:insertAttribute name="body"/>
        </div>
    </div>
</div>
<footer class="container-fluid">
    <tiles:insertAttribute name="footer"/>
</footer>
</body>
</html>
