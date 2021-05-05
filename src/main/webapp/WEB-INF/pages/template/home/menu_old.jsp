<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib uri="http://tiles.apache.org/tags-tiles" prefix="tiles" %>

<c:set var="changePwdMode" value="${sessionBean.changePwdMode}"/>
<c:set var="sectionList" value="${sessionBean.sectionList}"/>
<c:set var="pageMap" value="${sessionBean.pageMap}"/>
<c:set var="daysToExpire" value="${sessionBean.daysToExpire}"/>

<div>
    <c:if test="${sessionBean.daysToExpire gt 0}">
        <c:if test="${not changePwdMode}">
            <%--<spring:message code="login.expirywarning" arguments="${sessionBean.daysToExpire}" htmlEscape="false" argumentSeparator=";"/>--%>
        </c:if>
    </c:if>
</div>

<ul>
    <c:if test="${not changePwdMode}" var="condition">
        <div style="margin-top: 10px;"></div>
        <c:forEach items="${sectionList}" var="section">
            <a href="#" id="${section.sectionCode}">
                <span>${section.description}</span>
            </a>
            <ul>
                <c:set var="sectionCode" value="${section.sectionCode}"/>
                <c:forEach items="${pageMap[sectionCode]}" var="page">
                    <li>
                        <a href="${pageContext.request.contextPath}/${page.url}">${page.description}</a>
                    </li>
                </c:forEach>
            </ul>
        </c:forEach>
        <div style="margin-bottom: 10px;"></div>
    </c:if>
</ul>
<br>