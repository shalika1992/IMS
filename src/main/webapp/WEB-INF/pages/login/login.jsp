<%@ taglib uri="http://tiles.apache.org/tags-tiles" prefix="tiles" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>


<div class="login">
    <div class="login-container">
        <div class="login-left-content">
            <div class="left-image">
                <!-- <img src="${pageContext.request.contextPath}/resources/images/login-bg.png" alt="Doctor working for covid PCR add system"> -->
            </div>
        </div>
        <div class="login-right-content">
            <div class="login-content">
                <img src="${pageContext.request.contextPath}/resources/images/ecbh_logo.png" alt="Colombo East Base Hospital Logo">
                <h2 class="login-title">Login</h2>
                <p class="login-description">Enter your username and password</p>
                <!--begin::Form-->
                <form:form novalidate="novalidate" method="post" class="form" modelAttribute="loginform" action="checkuser.htm" id="kt_login_signin_form">
                    <div class="form-group">
                        <form:input path="username" name="username" class="login-input" placeholder="Username" type="text" autocomplete="off"/>
                    </div>
                    <div class="form-group">
                        <form:input path="password" name="password" class="login-input" placeholder="Password" type="password" autocomplete="off"/>
                    </div>
                    <!--begin::Action-->
                    <div class="form-group d-flex flex-wrap justify-content-between align-items-center">
                        <a href="javascript:;" class="text-dark-50 text-hover-primary my-3 mr-2"></a>
                        <button type="submit" id="kt_login_signin_submit" class="login-button">Login </button>
                    </div>
                    <!--end::Action-->
                </form:form>
                <!--end::Form-->

                <footer>
                    <p class="copy-right"> 	&copy; 2021 Colombo East Base Hospital. All rights recerved. <br> Solution by <a href="https://www.epictechnology.lk">Epic Lanka (Pvt) Ltd.</a></p>
                </footer>
            </div>
        </div>
    </div>
</div>
