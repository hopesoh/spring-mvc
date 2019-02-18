package br.com.casadocodigo.loja;

public class SoniaImpl implements Sonia {

	private String altura;
	private String idade;

	public SoniaImpl(String idade, String altura) {
		this.idade = idade;
		this.altura = altura;
	}
	
	@Override
	public String desenha()
		{
			return "      .-'-.\n" + 
					"       __|     `\\\n" + 
					"      `-,-`--._  `\\\n" + 
					" []  .->'  a   `|-'\n" + 
					"  `=/ (__/_     /\n" + 
					"    \\_,    `  _)\n" + 
					"jgs   `----; | \n+ "
					+ "idade: " +idade + " altura: " + altura ;
		}
	

}
