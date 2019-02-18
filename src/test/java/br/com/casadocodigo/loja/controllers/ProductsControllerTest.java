package br.com.casadocodigo.loja.controllers;

import javax.servlet.Filter;
import javax.transaction.Transactional;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.RequestPostProcessor;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import br.com.casadocodigo.loja.conf.AppWebConfiguration;
import br.com.casadocodigo.loja.conf.DataSourceConfigurationTest;
import br.com.casadocodigo.loja.conf.JPAConfiguration;
import br.com.casadocodigo.loja.conf.SecurityConfiguration;
import br.com.casadocodigo.loja.daos.ProductDAO;

/*
 * WebAppConfiguration: responsável por fazer o Runner do SpringTest carregar os objetos necessários
 * para uma aplicação web, por exemplo, a HttpServletRequest e o HttpServletResponse. Precisa dele
 * para montar o MockMvc (simular requests, response...)
 */

@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration
@ContextConfiguration(classes =	{AppWebConfiguration.class,JPAConfiguration.class, SecurityConfiguration.class,DataSourceConfigurationTest.class	})
@ActiveProfiles("test")
public class ProductsControllerTest {
	
	@Autowired
	private ProductDAO productDAO;
	
	@Autowired
	private WebApplicationContext wac;
	
	private MockMvc mockMvc;
	
	@Autowired
	private Filter springSecurityFilterChain;
	
	@Before
	public void setup() {
		this.mockMvc = MockMvcBuilders.webAppContextSetup(this.wac)
				.addFilter(springSecurityFilterChain).build();
	}
	
	@Test
	@Transactional
	public void	shouldListAllBooksInTheHome() throws Exception {
		this.mockMvc.perform(get("/produtos"))
		.andExpect(MockMvcResultMatchers
		.forwardedUrl("/WEB-INF/views/products/list.jsp"));
	}

	@Test
	public void onlyAdminShouldAccessProductForm() throws Exception {
		RequestPostProcessor processor = SecurityMockMvcRequestPostProcessors
				.user("comprador@gmail.com").password("123456")
				.roles("COMPRADOR");
		this.mockMvc.perform(get("/produtos/form")
				.with(processor))
				.andExpect(MockMvcResultMatchers.status().is(403));
	}
	
}
