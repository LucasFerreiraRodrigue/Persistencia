package br.ufg.inf.teste;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import br.ufg.inf.es.saep.sandbox.dominio.Regra;
import br.ufg.inf.es.saep.sandbox.dominio.Resolucao;
import br.ufg.inf.impl.ResolucaoRespositoryImpl;

public class Teste {

	public static void main(String[] args) {
		final ResolucaoRespositoryImpl resp = new ResolucaoRespositoryImpl();

		List<Regra> regras = new ArrayList<Regra>();
		List<String> dd = new ArrayList<String>(1);
		dd.add("a");

		float f = (float) 0.0;

		Regra regra = new Regra("var", 1, "desc", f, f, "exp", "entao", "senao", "tipoRelato", f, dd);

		regras.add(regra);

		Resolucao resolucao = new Resolucao("id", "nome", "descricao", new Date(), regras);

		// resp.persiste(resolucao);

		try {
			XMLparser(resolucao);
		} catch (IllegalArgumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public static String XMLparser(Resolucao res) throws IllegalArgumentException, IllegalAccessException {

		final StringBuilder sb = new StringBuilder();

		sb.append("<").append(res.getClass().getSimpleName()).append(">");
		for (Field field : res.getClass().getDeclaredFields()) {
			field.setAccessible(true);
			sb.append("<").append(field.getName()).append(">");
			sb.append(field.get(res));
			sb.append("</").append(field.getName()).append(">");
		}
		res.getClass().getDeclaredConstructors();

		return sb.toString();
	}

}
