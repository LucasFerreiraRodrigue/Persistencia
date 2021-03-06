package br.ufg.inf.fabrica;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import br.ufg.inf.es.saep.sandbox.dominio.Avaliavel;
import br.ufg.inf.es.saep.sandbox.dominio.Nota;
import br.ufg.inf.es.saep.sandbox.dominio.Parecer;
import br.ufg.inf.es.saep.sandbox.dominio.Pontuacao;
import br.ufg.inf.es.saep.sandbox.dominio.Valor;

/**
 * Classe respons�vel por manter possiveis cria��es de novos pareceres.
 *
 */
public class ParecerFabrica {

	/**
	 * M�todo respons�vel por criar um novo parecer.
	 */
	public Parecer novoParecer() {

		final Random rd = new Random();

		final List<String> listaRadocs = new ArrayList<String>();
		listaRadocs.add(String.valueOf(rd.nextInt(100000)));
		listaRadocs.add(String.valueOf(rd.nextInt(100000)));

		final List<Pontuacao> listaPontuacao = new ArrayList<Pontuacao>();

		final Valor valor = new Valor(Float.valueOf("10"));

		final Pontuacao pontuacao = new Pontuacao("pont1", valor);

		listaPontuacao.add(pontuacao);

		final List<Nota> listaNota = new ArrayList<Nota>();

		Avaliavel o = new Pontuacao("o", new Valor("o"));
		Avaliavel s = new Pontuacao("s", new Valor("o"));

		Nota nota = new Nota(o, s, "simples erro");

		listaNota.add(nota);

		Parecer parecer = new Parecer(String.valueOf(rd.nextInt(1000000)), "resolucaoId", listaRadocs, listaPontuacao,
				"fundamentacao", listaNota);

		return parecer;
	}
}
