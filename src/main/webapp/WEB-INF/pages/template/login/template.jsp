<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib uri="http://tiles.apache.org/tags-tiles" prefix="tiles" %>

<!DOCTYPE html>
<html lang="en">
<head>
<%--    <base href="${pageContext.request.contextPath}/resources/">--%>
    <meta charset="utf-8"/>
    <meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
    <title><tiles:getAsString name="title"/></title>
    <meta name="description" content="Login page example"/>
    <meta name="viewport" content="width=device-width, initial-scale=1, shrink-to-fit=no"/>
    <link rel="canonical" href="https://keenthemes.com/metronic"/>
    <!--begin::Fonts-->
    <link rel="stylesheet" href="https://fonts.googleapis.com/css?family=Poppins:300,400,500,600,700"/>
    <!--end::Fonts-->
    <!--begin::Page Custom Styles(used by this page)-->
    <%--<link href="${pageContext.request.contextPath}/resources/assets/css/pages/login/classic/login-1.css" rel="stylesheet" type="text/css"/>--%>
    <link href="${pageContext.request.contextPath}/resources/assets/css/pages/login/classic/login.css" rel="stylesheet" type="text/css"/>
    <!--end::Page Custom Styles-->
    <!--begin::Global Theme Styles(used by all pages)-->
    <link href="${pageContext.request.contextPath}/resources/assets/plugins/global/plugins.bundle.css" rel="stylesheet" type="text/css"/>
    <link href="${pageContext.request.contextPath}/resources/assets/plugins/custom/prismjs/prismjs.bundle.css" rel="stylesheet" type="text/css"/>
    <link href="${pageContext.request.contextPath}/resources/assets/css/style.bundle.css" rel="stylesheet" type="text/css"/>
    <!--end::Global Theme Styles-->
    <!--begin::Layout Themes(used by all pages)-->
    <link href="${pageContext.request.contextPath}/resources/assets/css/themes/layout/header/base/light.css" rel="stylesheet" type="text/css"/>
    <link href="${pageContext.request.contextPath}/resources/assets/css/themes/layout/header/menu/light.css" rel="stylesheet" type="text/css"/>
    <link href="${pageContext.request.contextPath}/resources/assets/css/themes/layout/brand/dark.css" rel="stylesheet" type="text/css"/>
    <link href="${pageContext.request.contextPath}/resources/assets/css/themes/layout/aside/dark.css" rel="stylesheet" type="text/css"/>
    <!--end::Layout Themes-->
    <link rel="shortcut icon" href="${pageContext.request.contextPath}/resources/assets/media/logos/favicon.ico"/>

    <title>Colombo East Base Hospital</title>

</head>

<tiles:insertAttribute name="body"/>

<!--begin::Global Config(global config for global JS scripts)-->
<script>var KTAppSettings = { "breakpoints": { "sm": 576, "md": 768, "lg": 992, "xl": 1200, "xxl": 1400 }, "colors": { "theme": { "base": { "white": "#ffffff", "primary": "#3699FF", "secondary": "#E5EAEE", "success": "#1BC5BD", "info": "#8950FC", "warning": "#FFA800", "danger": "#F64E60", "light": "#E4E6EF", "dark": "#181C32" }, "light": { "white": "#ffffff", "primary": "#E1F0FF", "secondary": "#EBEDF3", "success": "#C9F7F5", "info": "#EEE5FF", "warning": "#FFF4DE", "danger": "#FFE2E5", "light": "#F3F6F9", "dark": "#D6D6E0" }, "inverse": { "white": "#ffffff", "primary": "#ffffff", "secondary": "#3F4254", "success": "#ffffff", "info": "#ffffff", "warning": "#ffffff", "danger": "#ffffff", "light": "#464E5F", "dark": "#ffffff" } }, "gray": { "gray-100": "#F3F6F9", "gray-200": "#EBEDF3", "gray-300": "#E4E6EF", "gray-400": "#D1D3E0", "gray-500": "#B5B5C3", "gray-600": "#7E8299", "gray-700": "#5E6278", "gray-800": "#3F4254", "gray-900": "#181C32" } }, "font-family": "Poppins" };</script>
<!--end::Global Config-->
<!--begin::Global Theme Bundle(used by all pages)-->
<script src="${pageContext.request.contextPath}/resources/assets/plugins/global/plugins.bundle.js"></script>
<script src="${pageContext.request.contextPath}/resources/assets/plugins/custom/prismjs/prismjs.bundle.js"></script>
<script src="${pageContext.request.contextPath}/resources/assets/js/scripts.bundle.js"></script>
<!--end::Global Theme Bundle-->
<!--begin::Page Scripts(used by this page)-->
<script src="${pageContext.request.contextPath}/resources/assets/js/pages/custom/login/login-general.js"></script>
<!--end::Page Scripts-->
<!--begin::Side bar toggle-->
<script src="${pageContext.request.contextPath}/resources/assets/js/sidebartoggle.js"></script>
<!--end::Side bar toggle-->
</html>
