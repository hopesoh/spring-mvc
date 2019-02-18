package br.com.casadocodigo.loja.controllers;

import java.util.List;

import javax.servlet.http.Part;
import javax.transaction.Transactional;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import br.com.casadocodigo.loja.daos.ProductDAO;
import br.com.casadocodigo.loja.infra.FileSaver;
import br.com.casadocodigo.loja.infra.HttpPartUtils;
import br.com.casadocodigo.loja.models.BookType;
import br.com.casadocodigo.loja.models.Product;
import br.com.casadocodigo.loja.validation.ProductValidator;

/*
 *POST: usado quando existe a necessidade de criação de algum recurso;
 *GET: usado quando o interesse é o de recuperar alguma informação;
 *DELETE: como o nome diz, deve ser usado para excluir algum recurso;
 *PUT: associado com alguma operação de atualização de recursos no servidor.
 *
 *Antes, todos os RequestMapping internos tinha "/produtos". Por conta disso, foi colocado o
 *RequestMapping externo à classe com o "/produtos" que será aplicado para todos. (forma de generalizar)
 *redirect: indica para o SringMVC que, em vez de simplesmente fazer um forward, é necessário que ele
 *solicite que faça um novo request para o novo endereço.
 *
 *A taglib colocada em form.jsp <%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
 *facilita a exibição das mensagens de erro. Ver na página 66 sobre a tag hasBindErrors.
 *
 *@InitBinder indica que esse método deve ser chamado sempre que um request cair no controller em questão.
 *@Valid é utilizada para indicar o disparo do processo de validação.
 *Part: é a interface responsável por representar a o arquivo que foi enviado pelo formulário.
 *getName: retorna o nome do input usado no formulário.
 *getHeader: usado para recuperar alguma informação sobre o que foi enviado.
 *@PathVariable: para indicar o parâmetro do nosso método que deve ser populado com a respectiva parte da url.
 *Função, mvcURL ajuda a referenciar as urls dos métodos dentro da jsp.
 *
 *Cacheable: faz com que o Spring MVC saiba que, uma vez que o código for executado, ele deve guardar
 *o retorno e utilizá-lo em todas as próximas requisições que caiam no mesmo lugar.
 *
 *CacheEvict: faz com que, quando for salvo um novo livro, a lista seja atualizada.
 *
 *@ResponseBody: Ela informa ao Spring que o retorno do método é para ser usado diretamente como
 *corpo da resposta. Importante no caso do método que retorna os resultados em Json
 */

@Controller
@Transactional
@RequestMapping("/produtos")
public class ProductsController {
	//A Autowired é responsável por indicar os pontos de injeção dentro da sua classe.
	@Autowired
	private ProductDAO productDAO;
	@Autowired
	private FileSaver fileSaver;
	
	@InitBinder
	protected void initBinder(WebDataBinder binder) {
		//binder.setValidator(new ProductValidator()); Não precisa por enquanto
	}
	
	//Para salvar um novo produto e um arquivo recebido.
	//Aqui, o fluxo ideal é voltar com o usuário para a listagem, talvez mostrando uma mensagem de sucesso
	@RequestMapping(method=RequestMethod.POST)
	@CacheEvict(value="books", allEntries=true)
	public ModelAndView save(MultipartFile summary, @ModelAttribute("product") @Valid Product product, BindingResult bindingResult, RedirectAttributes redirectAttributes) {
		if(bindingResult.hasErrors()){
			return form(product);
		}
		String webPath = fileSaver.write("uploaded-images",summary);
		product.setSummaryPath(webPath);
		productDAO.save(product);
		redirectAttributes.addFlashAttribute("success", "Produto cadastrado com sucesso!!!!");
		return new ModelAndView("redirect:/produtos");
	}
	
	
	//A classe ModelAndView possui métodos que nos permitem ir adicionando objetos que serão disponibilizados na view.
	@RequestMapping("/form")
	public ModelAndView form(@ModelAttribute Product product){
		ModelAndView modelAndView = new ModelAndView("products/form");
		modelAndView.addObject("types", BookType.values());
		return modelAndView;
	}
	
	//Para listar os produtos cadastrados
	@RequestMapping(method=RequestMethod.GET)
	@Cacheable(value="lastProducts")
	public ModelAndView list() {
		ModelAndView modelAndView = new ModelAndView("products/list");
		modelAndView.addObject("products", productDAO.list());
		return modelAndView;
	}
	
	//Método que carrega o produto na página show
	//É usado {nomeDoParametro} para criar URl com argumentos misturados. casadocodigo.com.br/products/livro-apis-java.
	@RequestMapping("/{id}")
	public ModelAndView show(@PathVariable("id") Integer id) {
		ModelAndView modelAndView = new ModelAndView("products/show");
		Product product = productDAO.find(id);
		modelAndView.addObject("product", product);
		return modelAndView;
	}
	
	//método capaz de retornar a lista de livros em outro formato, no caso, json
//	@RequestMapping(method=RequestMethod.GET, value="json")
//	@ResponseBody
//	public List<Product> listJson() {
//		return productDAO.list();
//	}
//	
	

}
