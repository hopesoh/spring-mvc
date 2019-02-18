<%@taglib uri="http://java.sun.com/jsp/jstl/core"  prefix="c"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/fmt"  prefix="fmt"%>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@taglib uri="http://www.springframework.org/tags" prefix="spring" %>
<!doctype html>
<!--[if lt IE 7]><html class="no-js lt-ie9 lt-ie8 lt-ie7" lang="pt"><![endif]-->
<!--[if IE 7]><html class="no-js lt-ie9 lt-ie8" lang="pt"><![endif]-->
<!--[if IE 8]><html class="no-js lt-ie9" lang="pt"><![endif]-->
<!--[if gt IE 8]><!-->
<html class="no-js" lang="pt">
<!--<![endif]-->
<head>
<meta charset="utf-8">
<meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
<meta name="viewport"
	content="width=device-width, initial-scale=1, maximum-scale=1">

<title>${product.title}</title>
</head>
<body class="${bodyClass}">
<%@include file="/WEB-INF/header.jsp"	%>
	
	<nav class="categories-nav">
		<ul class="container">
			<li class="category"><a href="http://www.casadocodigo.com.br">Home</a>
			<li class="category"><a href="/collections/livros-de-agile">
					Agile </a>
			<li class="category"><a href="/collections/livros-de-front-end">
					Front End </a>
			<li class="category"><a href="/collections/livros-de-games">
					Games </a>
			<li class="category"><a href="/collections/livros-de-java">
					Java </a>
			<li class="category"><a href="/collections/livros-de-mobile">
					Mobile </a>
			<li class="category"><a
				href="/collections/livros-desenvolvimento-web"> Web </a>
			<li class="category"><a href="/collections/outros"> Outros </a>
		</ul>
	</nav>


	<article id="${product.title}" itemscope
		itemtype="http://schema.org/Book">
		<header id="product-highlight" class="clearfix">
			<div id="product-overview" class="container">
				<img itemprop="image" width="280px" height="395px"
					src=''
					class="product-featured-image" alt="${product.title}">
				<h1 class="product-title" itemprop="name">${product.title}</h1>
				<p class="product-author">
					<span class="product-author-link"> ${product.title} </span>
				</p>

				<p itemprop="description" class="book-description">
				${product.description}
				 Veja o <a
						href="<c:url value='/${product.summaryPath}'/>"
						target="_blank">sum&#225;rio</a> completo do livro!
				</p>
			</div>
		</header>

		
		<section class="buy-options clearfix">
			<form:form servletRelativeAction="/shopping" cssClass="container">
				<input type="hidden" value="${product.id}" name="productId"/>
				<ul id="variants" class="clearfix">
					<c:forEach items="${product.prices}" var="price">
						<li class="buy-option">
							
							<input type="radio"
							name="bookType" class="variant-radio" id="${product.id}-${price.bookType}"
							value="${price.bookType}" ${price.bookType.name() == 'COMBO' ? 'checked' : ''}>
							 
							<label  class="variant-label" for="${product.id}-${price.bookType}"> 
								${price.bookType}								
							</label> 
							<p class="variant-price">${price.value}</p>
						</li>
					</c:forEach>




				</ul>

				<input type="submit" class="submit-image icon-basket-alt"
					alt="Compre agora"
					title="Compre agora '${product.title}'!" value="comprar"/>

			</form:form>

		</section>

		<div class="container">

			<section class="author product-detail" itemprop="author" itemscope
				itemtype="http://schema.org/Person">
				<h2 class="section-title" itemprop="name">${product.title}</h2>
				<span itemprop="description">

					<p class="book-description">${product.description}</p>

				</span>
			</section>

			<section class="data product-detail">
				<h2 class="section-title">Dados do livro:</h2>
				<p>
					Número de paginas: <span itemprop="numberOfPages">${product.pages}</span>
				</p>


				<p></p>
				<p>
					Encontrou um erro? <a href='/submissao-errata' target='_blank'>Submeta
						uma errata</a>
				</p>
			</section>
		</div>


	</article>

	<%@include file="/WEB-INF/footer.jsp" %>
	
</body>
</html>