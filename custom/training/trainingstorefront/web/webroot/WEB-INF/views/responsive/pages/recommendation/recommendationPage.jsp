<%@ page trimDirectiveWhitespaces="true" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="template" tagdir="/WEB-INF/tags/responsive/template" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="cms" uri="http://hybris.com/tld/cmstags" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="product" tagdir="/WEB-INF/tags/responsive/product" %>

<spring:htmlEscape defaultHtmlEscape="true" />

<template:page pageTitle="${pageTitle}">

    <c:url value="/" var="homePageUrl" />


    <div class="container">
        <cms:pageSlot position="MiddleContent" var="comp" element="div" class="errorNotFoundPageMiddle">
            <cms:component component="${comp}" element="div" class="errorNotFoundPageMiddle-component" />
        </cms:pageSlot>
        <div class="row">
            <c:forEach items="${productList}" var="product" varStatus="loop">
                <div class="col-md-4">
                    <div class="product-card">
                        <div class="image-product">
                            <product:productPrimaryImage product="${product}" format="product"/>
                        </div>
                        <h3><c:out value="${product.name}"/></h3>
                        <p><c:out value="${product.sku}"/></p>
                        <p><c:out value="${product.material}"/></p>
                    </div>
                </div>
            </c:forEach>
        </div>
    </div>

</template:page>