<%--
  Created by IntelliJ IDEA.
  User: prathibha_w
  Date: 2/11/2021
  Time: 11:17 AM
  To change this template use File | Settings | File Templates.
--%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib uri="http://tiles.apache.org/tags-tiles" prefix="tiles" %>

<c:set var="changePwdMode" value="${sessionBean.changePwdMode}"/>
<c:set var="sectionList" value="${sessionBean.sectionList}"/>
<c:set var="pageMap" value="${sessionBean.pageMap}"/>
<c:set var="daysToExpire" value="${sessionBean.daysToExpire}"/>

<div class="content d-flex flex-column flex-column-fluid" id="kt_content">
    <!--begin::Subheader-->
    <div class="subheader py-2 py-lg-4 subheader-solid" id="kt_subheader">
        <div class="container-fluid d-flex align-items-center justify-content-between flex-wrap flex-sm-nowrap">
            <!--begin::Info-->
            <div class="d-flex align-items-center flex-wrap mr-2">
                <!--begin::Page Title-->
                <h5 class="text-dark font-weight-bold mt-2 mb-2 mr-5">Home</h5>
                <!--end::Page Title-->
                <!--begin::Actions-->
                <div class="subheader-separator subheader-separator-ver mt-2 mb-2 mr-4 bg-gray-200">
                </div>
                <span class="text-muted font-weight-bold mr-4">IMS</span>
            </div>
        </div>
    </div>
    <!--end::Subheader-->

    <!--begin::Entry-->
    <div class="d-flex flex-column-fluid">
        <!--begin::Container-->
        <div class="container">
            <!--begin::Dashboard-->
            <!--begin::Row-->
            <div class="row">
                <c:if test="${not changePwdMode}" var="condition">
                    <c:forEach items="${sectionList}" var="section">
                        <div class="col-xl-4">
                            <div class="card card-custom card-stretch gutter-b">
                                <div class="card-body d-flex p-0">
                                    <div class="flex-grow-1 p-8 card-rounded flex-grow-1 bgi-no-repeat ${section.sectionCode}"
                                         style="background-position: calc(100% + 0.5rem) bottom; background-size: auto 70%; ">
                                        <h4 >${section.description}</h4>
                                        <c:if test="${not changePwdMode}" var="condition">
                                            <c:set var="sectionCode" value="${section.sectionCode}"/>
                                            <c:forEach items="${pageMap[sectionCode]}" var="page">
                                                <ul>
                                                    <li>
                                                        <a href="${pageContext.request.contextPath}/${page.url}"
                                                           id="${page.pageCode}">${page.description}</a>
                                                    </li>
                                                </ul>
                                            </c:forEach>
                                        </c:if>
                                    </div>
                                </div>
                            </div>
                            <!--end::Engage Widget 2-->
                        </div>
                    </c:forEach>
                </c:if>
            </div>
        </div>
        <!--end::Container-->
    </div>
    <!--end::Entry-->
</div>
