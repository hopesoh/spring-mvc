package br.com.casadocodigo.loja.conf;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.concurrent.ConcurrentMapCacheManager;
import org.springframework.cache.guava.GuavaCacheManager;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;
import org.springframework.format.datetime.DateFormatter;
import org.springframework.format.datetime.DateFormatterRegistrar;
import org.springframework.format.support.DefaultFormattingConversionService;
import org.springframework.format.support.FormattingConversionService;
import org.springframework.web.accept.ContentNegotiationManager;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartResolver;
import org.springframework.web.multipart.support.StandardServletMultipartResolver;
import org.springframework.web.servlet.LocaleResolver;
import org.springframework.web.servlet.ViewResolver;
import org.springframework.web.servlet.config.annotation.DefaultServletHandlerConfigurer;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;
import org.springframework.web.servlet.i18n.CookieLocaleResolver;
import org.springframework.web.servlet.i18n.LocaleChangeInterceptor;
import org.springframework.web.servlet.view.ContentNegotiatingViewResolver;
import org.springframework.web.servlet.view.InternalResourceViewResolver;

import com.google.common.cache.CacheBuilder;

import br.com.casadocodigo.loja.SoniaImpl;
import br.com.casadocodigo.loja.controllers.HomeController;
import br.com.casadocodigo.loja.daos.ProductDAO;
import br.com.casadocodigo.loja.infra.FileSaver;
import br.com.casadocodigo.loja.models.ShoppingCart;
import br.com.casadocodigo.loja.viewresolver.JsonViewResolver;

/*
* ComponentScan indica quais pacotes devem ser lidosno caso, a HomeController
* Caso eu crio um pacote novo e esqueça de alterar a configuração annotation da
* @ComponentScan, provavelmente uma exception vai ser lançada no início da aplicação,
* já que o spring mão vai achar a classe do novo pacote. Uma forma de solucionar é usar
* @ComponentScan(basePackages="br.com.casadocodigo.loja")
* 
* DateFormatterRegistrar implementa a interface FormatterRegistrar, que é usada quando é necessário
* agrupar vários tipos de conversões.
* 
* MultipartResolver é a que	define os métodos necessários para o tratamento inicial de um request
* cujo modo de envio, também conhecido como contentType, é o "multipart/form-data".
* 
* 
* A memoria cache é um tipo de memória ultra rápida que armazena os dados e instruções mais
* utilizadas pelo processador, permitindo que estas sejam acessadas rapidamente. Reduzindo
* o número de operações em que é preciso buscar dados diretamente na memória RAM, que é mais lenta.
* EnableCaching: habilitar o uso do cache para que o Spring possa começar a guardar os retornos dos
* locais indicados com a annotation @Cacheable.
* Para cobrir problemas como: quanto tempo vamos manter os itens lá, qual o número limite de itens
* e etc, pode-se usar uma implementação provida pelo Guava, uma biblioteca criada pelo
* Google.
* https://github.com/google/guava
* 
* internalResourceViewresolver é o método que retorna o objeto responsável pelasS JSP.
* View Resolver ensina ao Spring sobre qual tipo ele deve retornar.
*/

@EnableWebMvc
@ComponentScan(basePackageClasses = {HomeController.class, ProductDAO.class, FileSaver.class, ShoppingCart.class})
@EnableCaching
public class AppWebConfiguration extends WebMvcConfigurerAdapter{
	
	//O objetivo é expor para a servlet do spring mvc quais são as classes que devem ser lidas e carregadas.
	//Bean indica para o Spring que o retorno do método deve ser registrado como um objeto gerenciado pelo container.
	@Bean
	public InternalResourceViewResolver internalResourceViewResolver() {
		InternalResourceViewResolver resolver = new InternalResourceViewResolver();
		resolver.setPrefix("/WEB-INF/views/");
		resolver.setSuffix(".jsp");
		resolver.setExposeContextBeansAsAttributes(true);
		resolver.setExposedContextBeanNames("shoppingCart");
		return resolver;
	}
	
	//indica a localização do arquivo base de mensagens. O nome do método deve ser esse, pois o Bean
	//Spring vai procurar por um método com esse nome.
	@Bean
	public MessageSource messageSource() {
		ReloadableResourceBundleMessageSource bundle = new ReloadableResourceBundleMessageSource();
		bundle.setBasename("/WEB-INF/messages");
		bundle.setDefaultEncoding("UTF-8");
		bundle.setCacheSeconds(1);
		return bundle;
	}
	
	
	//o nome do método tem que ser esse, pois esse é o nome usado internamente pelo SpringMVC para registrar
	//o objeto por agrupar os conversores.
	@Bean
	public FormattingConversionService	mvcConversionService()	{
		DefaultFormattingConversionService conversionService = new DefaultFormattingConversionService(true);
		DateFormatterRegistrar registrar = new DateFormatterRegistrar();
		registrar.setFormatter(new	DateFormatter("yyyy-MM-dd"));
		registrar.registerFormatters(conversionService);//
		return	conversionService;
	}
	
	@Bean
	public MultipartResolver multipartResolver() {
		return new StandardServletMultipartResolver();
	}
	
	
	 //O objeto do tipo RestTemplate possui diversos métodos que podemos usar para realizar diversos
	 //tipos de requisições.
	@Bean
	public RestTemplate restTemplate() {
		return new RestTemplate();
	}
	
	@Bean
	public CacheManager cacheManager() {
		//ConcurrentMapcacheManager é uma forma de implementação de cache mais simples.
		//return new ConcurrentMapCacheManager();
		CacheBuilder<Object, Object> builder = CacheBuilder.newBuilder().maximumSize(100).expireAfterAccess(5, TimeUnit.MINUTES);
		GuavaCacheManager cacheManager = new GuavaCacheManager();
		cacheManager.setCacheBuilder(builder);
		return cacheManager;
	}
	
	@Bean
	public ViewResolver contentNegotiatingViewResolver(ContentNegotiationManager manager) {
		List<ViewResolver> resolvers = new ArrayList<ViewResolver>();
		resolvers.add(internalResourceViewResolver());
		resolvers.add(new	JsonViewResolver());
		ContentNegotiatingViewResolver resolver = new ContentNegotiatingViewResolver();
		resolver.setViewResolvers(resolvers);
		resolver.setContentNegotiationManager(manager);
		return	resolver;
	}
	
	@Override
	public void addInterceptors(InterceptorRegistry	registry)	{
		registry.addInterceptor(new	LocaleChangeInterceptor());
	}
	
	@Bean
	public LocaleResolver localeResolver(){
		return new CookieLocaleResolver();
	}
	
	@Override
	public void configureDefaultServletHandling (DefaultServletHandlerConfigurer configurer) {
		configurer.enable();
	}
	
//	@Bean
//	public Sonia sonia() {
//		return new SoniaImpl("32", "1,78");
//	}
	
}
