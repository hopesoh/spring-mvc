package br.com.casadocodigo.loja.validation;

import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

import br.com.casadocodigo.loja.models.Product;

/*
 *A classe ValidationUtils é um helper do Spring Validation para realizar algumas validações básicas.
 *O método supports recebe a classe do objeto que está querendo ser validado e retorna se o validador consegue
 *lidar com ele. Essa é a forma que o spring MVC controla qual validação deve ser aplicada.
 *O método validate é responsável pela validação em si.
 */
public class ProductValidator implements Validator{
	
	@Override
	public boolean supports(Class<?> clazz) {
		return Product.class.isAssignableFrom(clazz);
	}

	@Override
	public void validate(Object target, Errors errors) {
		ValidationUtils.rejectIfEmptyOrWhitespace(errors,"title","field.required");
		ValidationUtils.rejectIfEmptyOrWhitespace(errors,"title","field.required");
		
		Product product = (Product) target;
		if(product.getPages()==0) {
			errors.rejectValue("pages", "field.required");
		}
		
	}

}
