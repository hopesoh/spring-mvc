package br.com.casadocodigo.loja.models;

import java.math.BigDecimal;

/*
 *Aqui, é importante que o nome da variável seja value, justamente para que o Spring MVC possa pegar
 *o objeto e gerar o Json com as chaves corretas.
 */

public class PaymentData {
	private BigDecimal value;
	
	public PaymentData() {
	}
	
	public PaymentData(BigDecimal value) {
		this.value = value;
	}
	
	public BigDecimal getValue() {
		return value;
	}
}
