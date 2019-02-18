package br.com.casadocodigo.loja.conf;

import org.springframework.orm.jpa.support.OpenEntityManagerInViewFilter;
import org.springframework.web.context.request.RequestContextListener;
import org.springframework.web.servlet.support.AbstractAnnotationConfigDispatcherServletInitializer;

import javax.servlet.Filter;
import javax.servlet.MultipartConfigElement;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletRegistration.Dynamic;

/*
 * Quando foi configurado o tratamento de upload, podemos definir algumas coisas, como lugar de armazenamento,
 * tamanho máximo do arquivo e tamannho máximo do request como um todo. Essa configuração é feita com o MultipartConfigElement.
 * 
 * customizeRegistration recebendo apenas uma string vazia indica que o próprio servidor web vai decidir qual é o local de
 * armazenamento temporário dos arquivos.
 * 
 * O capítulo 6 do livro até o capítulo 6.3, mostra como guardar o arquivo do qual fizemos upload na pŕopria aplicação web.
 * O problema é que, a cada nova alteração que temos no projeto, a ide força um hot deploy no servidor, que apaga
 * a aplicação que estava lá e cria uma nova, fazendo com que seja perdido todos os arquivos que foram carregados.
 * Amazon S3: é um servidor onde podemos enviar nossos próprios arquivos e eles ficam disponíveis para serem acessados via web.
 * 
 */

public class ServletSpringMVC extends
		AbstractAnnotationConfigDispatcherServletInitializer {

	@Override
	protected Class<?>[] getRootConfigClasses() {
		return new Class[] {AppWebConfiguration.class, SecurityConfiguration.class, JPAConfiguration.class};
	}

	@Override
	protected Class<?>[] getServletConfigClasses() {
		// Tem que colocar aqui para ser adicionado no carregamento da servlet
		// base
		return new Class[] {};
	}
	
	@Override
	protected Filter[] getServletFilters() {
		return new Filter[] {new OpenEntityManagerInViewFilter()};
	}

	@Override
	protected String[] getServletMappings() {
		// TODO Auto-generated method stub
		return new String[] { "/" };
	}
	
	@Override
	protected void customizeRegistration(Dynamic registration) {
		registration.setMultipartConfig(new MultipartConfigElement(""));
	}
	
	@Override
	public void	onStartup(ServletContext servletContext) throws	ServletException {
		super.onStartup(servletContext);
		servletContext.addListener(RequestContextListener.class);
		servletContext.setInitParameter("spring.profiles.active", "dev");
	}
	
}
