package br.com.casadocodigo.loja.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import br.com.casadocodigo.loja.Sonia;

/*
 * Um framework em desenvolvimento de software, é uma abstração que une códigos comuns entre vários projetos de software
 * provendo uma funcionalidade genérica. Um framework pode atingir uma funcionalidade específica, por configuração,
 * durante a programação de uma aplicação. Ao contrário das bibliotecas, é o framework quem dita o fluxo de controle da aplicação,
 * chamado de Inversão de Controle.
 */


@Controller
public class HomeController {

//	@Autowired
//	private Sonia sonia;
	
	@RequestMapping("/")
	public String index(){
//		System.out.println(sonia.desenha());
		
		return "hello-world";
	}
}
