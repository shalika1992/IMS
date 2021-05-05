<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib uri="http://tiles.apache.org/tags-tiles" prefix="tiles"%>

<!DOCTYPE html>
<html>

<head>
    <meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
    <!--script list-->
    <!--jquery-->
    <script src="${pageContext.request.contextPath}/resources/jquery/js/jquery-3.2.1.slim.min.js?${initParam['version']}"
            type="text/javascript"></script>

    <script src="${pageContext.request.contextPath}/resources/popper/popper.min.js?${initParam['version']}"
            type="text/javascript"></script>

    <!--bootstrap-->
    <script src="${pageContext.request.contextPath}/resources/bootstrap/js/bootstrap.bundle.min.js?${initParam['version']}"
            type="text/javascript"></script>

    <script src="${pageContext.request.contextPath}/resources/bootstrap/js/bootstrap.min.js?${initParam['version']}"
            type="text/javascript"></script>

    <!--bootstrap date picker-->
    <script src="${pageContext.request.contextPath}/resources/bootstrap/datepicker/js/bootstrap-datepicker.js?${initParam['version']}"
            type="text/javascript"></script>


    <!--bootstrap validator-->
    <script src="${pageContext.request.contextPath}/resources/js/bootstrapvalidator/bootstrapValidator.min.js?${initParam['version']}"
            type="text/javascript"></script>

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

    <title><tiles:getAsString name="title"/></title>
</head>
<body>
    <div class="container-fluid">
        <tiles:insertAttribute name="body"/>
    </div>
</body>
</html>