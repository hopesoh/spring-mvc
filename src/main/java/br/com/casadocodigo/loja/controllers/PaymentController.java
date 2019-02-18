package br.com.casadocodigo.loja.controllers;

import java.math.BigDecimal;
import java.util.concurrent.Callable;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.web.bind.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.ModelAndView;

import br.com.casadocodigo.loja.models.PaymentData;
import br.com.casadocodigo.loja.models.ShoppingCart;

/*
 * Callable: interface para implementação de uma execução em paralelo. Parece muito
 * com uma outra interface, a Runnable, que não retorna nenhum valor, enquanto a
 * Callable deve retornar um valor ao final da execução.
 * 
 * O uso do Callable é a maneira mais simples de realizar a execução de um código
 * assíncrono dentro do nosso controller. O único defeito é que nunca sabemos como
 * esse código vai ser executado.
 * https://www.devmedia.com.br/processamento-assincrono-em-java-com-future-e-futuretask/33851
 * 
 * DefferedResult (retorno postergado): Serve para resolver problemas como: Quantas novas threads
 * existem disponíveis para a execução do código assíncrono; Caso eu queira controlar a criação
 * das threads; Caso precise rodar esse código dentro de uma fila de processamento. O uso do
 * DefferedResult permite um crontrole mais fino da execução assíncrona, disponibilizando alguns
 * métodos que podem ser úteis.
 * 
 */


@Controller
@RequestMapping("/payment")
public class PaymentController {
	
	@Autowired
	private ShoppingCart shoppingCart;
	@Autowired
	private RestTemplate restTemplate;
	
	@RequestMapping(value = "checkout", method = RequestMethod.POST)
	public Callable<String> checkout() {
		return () -> {
			BigDecimal total = shoppingCart.getTotal();
			String uriToPay = "http://book-payment.herokuapp.com/payment";
			try {
				String response = restTemplate.postForObject(uriToPay, new PaymentData(total), String.class);
				return "redirect:/payment/success";
			} catch (HttpClientErrorException exception) {
				return "redirect:/payment/error";
			}			
		};
	}
}
