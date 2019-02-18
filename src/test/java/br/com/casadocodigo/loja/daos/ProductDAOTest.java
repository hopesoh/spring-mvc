package br.com.casadocodigo.loja.daos;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import javax.transaction.Transactional;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import br.com.casadocodigo.loja.builders.ProductBuilder;
import br.com.casadocodigo.loja.conf.DataSourceConfigurationTest;
import br.com.casadocodigo.loja.conf.JPAConfiguration;
import br.com.casadocodigo.loja.models.BookType;
import br.com.casadocodigo.loja.models.Product;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {DataSourceConfigurationTest.class, ProductDAO.class,JPAConfiguration.class})
@ActiveProfiles("test")
public class ProductDAOTest {
	
	@Autowired
	public ProductDAO productDAO;
	
	@Transactional
	@Test
	public void shouldSumAllPricesOfEachBookPerType() {
		
		List<Product> printedBooks = ProductBuilder.newProduct(BookType.PRINTED, BigDecimal.TEN).more(4).buildAll();
		printedBooks.stream()
		.forEach(productDAO::save);
		
		List<Product> ebooks = ProductBuilder.newProduct(BookType.EBOOK, BigDecimal.TEN).more(4).buildAll();				
		ebooks.stream().forEach(productDAO::save);
		
		BigDecimal value = productDAO.sumPricesPerType(BookType.PRINTED);
		Assert.assertEquals(new	BigDecimal(50).setScale(2),value);
	}
	
	
//	public static void main(String[] args) {
//		List<Character> asList = Arrays.asList('a', 'b', 'c');
//		
//		imperative(asList);
//		
//		declarative(asList);
//	
//		
//	}
//
//	private static void imperative(List<Character> asList) {
//		List<Character> myList = new ArrayList<>();
//		for(Character character : asList) {
//			if( character.equals('c') ) {
//				myList.add(character);
//			}
//		}
//		
//		for (Character character : myList) {
//			System.out.println( character );
//		}
//	}
//	
//	private static void declarative(List<Character> asList) {
//		List<Character> myList = 
//				asList.stream()
//					.filter(letter ->{ return letter.equals('c'); })
//					.collect(Collectors.toList());
//				
//		myList.stream().forEach(System.out::println);
//	}
	
	
}
