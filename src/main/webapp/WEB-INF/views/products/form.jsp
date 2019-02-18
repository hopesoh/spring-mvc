<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@ taglib uri="http://www.casadocodigo.com.br/tags/form" prefix="customForm" %>


<!DOCTYPE html>
<html>
<head>
<meta	charset="UTF-8">
<title>Cadastro	de	produtos</title>
</head>
<body>
	<spring:hasBindErrors name="product">
		<ul>
		<c:forEach var="error" items="${errors.allErrors}">		
			<li>${error.code}-${error.field}</li>
		</c:forEach>
		</ul>
	</spring:hasBindErrors>
	<form:form method="post" action="/casadocodigo/produtos" commandName="product" enctype="multipart/form-data">
		<div>
			<label	for="title">Titulo</label>
			<form:input	path="title"/>
			<form:errors path="title"/>
		</div>
		<div>
			<label	for="description">Descrição</label>
			<textarea name="description" rows="10" cols="20" id="description"></textarea>
			<form:errors path="description"/>
		</div>
		<div>
			<label	for="pages">Número	de	paginas</label>
			<input	type="text"	name="pages"	id="pages"/>
			<form:errors path="pages"/>
		</div>
		<div>
			<label	for="releaseDate">Data	de	lançamento</label>
			<input	type="date"	name="releaseDate"/>
			<form:errors	path="releaseDate"/>
		</div>
		<div>
			<c:forEach items="${bookTypes}" var="bookType" varStatus="status">
				<div>
					<label for="preco_${bookType}">${bookType}</label>
					<input type="text" name="prices[${status.index}].value" id="preco_${bookType}"/>
					<input type="hidden" name="prices[${status.index}].bookType" value="${bookType}"/>
				</div>
			</c:forEach>
		</div>
		<div>
			<c:forEach	items="${products}"	var="product">
				<tr>
					<td>
						<a	
						href="${spring:mvcUrl('PC#show').arg(0,product.id)
						.build()}">
							${product.title}
						</a>
					</td>
					<td>
						<c:forEach	items="${product.prices}"	var="price">
							[${price.value}	-	${price.bookType}]
						</c:forEach>
					</td>
				</tr>
			</c:forEach>
		</div>
		<div>
			<label for="summary">Sumario do livro</label>
			<input type="file"	name="summary"/>
			<form:errors path="summaryPath"/>				
		</div>
		<div>
			<input	type="submit"	value="Enviar">
		</div>
	</form:form>
</body>
</html>