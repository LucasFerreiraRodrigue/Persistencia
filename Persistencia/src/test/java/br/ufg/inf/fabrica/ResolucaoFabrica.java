package br.ufg.inf.fabrica;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;

import br.ufg.inf.es.saep.sandbox.dominio.Regra;
import br.ufg.inf.es.saep.sandbox.dominio.Resolucao;

/**
 * Classe responsável por manter possiveis criações de novas resoluçoes.
 *
 */
public class ResolucaoFabrica {

	float f = (float) 0.0;

	/**
	 * Método responsável por criar um novo parecer.
	 */
	public Resolucao novaResolucao() {

		Random rd = new Random();

		List<Regra> regras = new ArrayList<Regra>();
		List<String> dd = new ArrayList<String>(1);
		dd.add("a");

		Regra regra = new Regra("var", 1, "desc", f, f, "exp", "entao", "senao", "tipoRelato", f, dd);
		regras.add(regra);

		Resolucao resolucao = new Resolucao(String.valueOf(rd.nextInt(100)), "nome", "descricao", new Date(), regras);

		return resolucao;
	}
}
