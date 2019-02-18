package br.com.casadocodigo.loja.controllers;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import br.com.casadocodigo.loja.daos.ProductDAO;
import br.com.casadocodigo.loja.models.BookType;
import br.com.casadocodigo.loja.models.Product;
import br.com.casadocodigo.loja.models.ShoppingCart;
import br.com.casadocodigo.loja.models.ShoppingItem;

/*
 * É interessante mencionar outra característica importantes dos escopos que é a visibilidade da informação.
 * No escopo de sessão cada usuário abre uma sessão com o servidor e somente esse usuário tem acesso a essa sessão.
 * Desta forma, ficam várias sessões abertas no servidor para cada usuário que acessa o mesmo.
 * Já o escopo de aplicação é compartilhado para todos os usuários.
 * 
 * O modelo de trabalho onde, alguém fica esperando o outro acabar para aí sim
 * continuar seu trabalho, é conhecido como modelo síncrono. O problema desse modelo
 * é que o Tomcat tem um número limitado de threads que podem ser criadas e, caso
 * esse número chegue no limite, as requisições dos usuários começam a ser enfileiradas
 * e podem ter uma lentidão na aplicação.
 */

@Controller
@RequestMapping("/shopping")
@Lazy
public class ShoppingCartController {
	
	@Autowired
	private ProductDAO productDAO;
	@Autowired
	private ShoppingCart shoppingCart;
	
//	@InitBinder
//    protected void initBinder(HttpSession session) {
//		session.setAttribute("shoppingCart", shoppingCart);
//    }

	@RequestMapping(method=RequestMethod.POST)	
	public ModelAndView add(Integer productId,@RequestParam BookType bookType){
		System.out.println(bookType);
		ShoppingItem item = createItem(productId, bookType);
		shoppingCart.add(item);
		return new ModelAndView("redirect:/produtos");
	}

	private ShoppingItem createItem(Integer productId, BookType bookType) {
		Product product = productDAO.find(productId);
		ShoppingItem item = new ShoppingItem(product,bookType);
		return item;
	}

	@RequestMapping(method=RequestMethod.GET)
	public String items(){
		return "shoppingCart/items";
	}

	@RequestMapping(method=RequestMethod.POST,value="/{productId}")
	public String remove(@PathVariable("productId") Integer productId,BookType bookType){
		shoppingCart.remove(createItem(productId, bookType));
		return "redirect:/shopping";
	}
}