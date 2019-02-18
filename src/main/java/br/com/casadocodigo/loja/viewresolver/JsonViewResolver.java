package br.com.casadocodigo.loja.viewresolver;

import java.util.Locale;

import org.springframework.web.servlet.View;
import org.springframework.web.servlet.ViewResolver;
import org.springframework.web.servlet.view.json.MappingJackson2JsonView;

/*
 * Responsável por tratar o Json.
 */
public class JsonViewResolver implements ViewResolver {
	
	@Override
	public View resolveViewName(String viewName, Locale	locale) throws Exception {
		MappingJackson2JsonView view = new	MappingJackson2JsonView();
		view.setPrettyPrint(true);	
		return	view;
	}

}
