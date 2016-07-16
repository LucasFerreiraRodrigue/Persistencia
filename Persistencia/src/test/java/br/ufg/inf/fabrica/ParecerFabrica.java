package br.ufg.inf.fabrica;

import java.util.ArrayList;
import java.util.List;

import br.ufg.inf.es.saep.sandbox.dominio.Avaliavel;
import br.ufg.inf.es.saep.sandbox.dominio.Nota;
import br.ufg.inf.es.saep.sandbox.dominio.Parecer;
import br.ufg.inf.es.saep.sandbox.dominio.Pontuacao;
import br.ufg.inf.es.saep.sandbox.dominio.Valor;

/**
 * Classe responsável por manter possiveis criações de novos pareceres.
 *
 */
public class ParecerFabrica {

	/**
	 * Método responsável por criar um novo parecer.
	 */
	public Parecer novoParecer() {
		final List<String> listaRadocs = new ArrayList<String>();
		listaRadocs.add("1");
		listaRadocs.add("2");

		final List<Pontuacao> listaPontuacao = new ArrayList<Pontuacao>();

		final Valor valor = new Valor(Float.valueOf("10"));

		final Pontuacao pontuacao = new Pontuacao("pont1", valor);

		listaPontuacao.add(pontuacao);

		final List<Nota> listaNota = new ArrayList<Nota>();

		Avaliavel o = new Pontuacao("o", new Valor("o"));
		Avaliavel s = new Pontuacao("s", new Valor("o"));

		Nota nota = new Nota(o, s, "simples erro");

		listaNota.add(nota);

		Parecer parecer = new Parecer("id", "resolucaoId", listaRadocs, listaPontuacao, "fundamentacao", listaNota);

		return parecer;
	}
}
