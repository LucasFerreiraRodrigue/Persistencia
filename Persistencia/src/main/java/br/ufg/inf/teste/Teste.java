package br.ufg.inf.teste;

import br.ufg.inf.es.saep.sandbox.dominio.Avaliavel;
import br.ufg.inf.es.saep.sandbox.dominio.Nota;
import br.ufg.inf.es.saep.sandbox.dominio.Pontuacao;
import br.ufg.inf.es.saep.sandbox.dominio.Valor;
import br.ufg.inf.impl.ParecerRepositoryImpl;
import br.ufg.inf.impl.ResolucaoRespositoryImpl;

public class Teste {

	public static void main(String[] args) {

		final ResolucaoRespositoryImpl dao = new ResolucaoRespositoryImpl();
		final ParecerRepositoryImpl parDao = new ParecerRepositoryImpl();

		// dao.resolucoes();

		// adicionaNotaParecer(parDao);

		// removeNotaParecer(parDao);

		// atualizaFundamentacao(parDao);
	}

	private static void atualizaFundamentacao(ParecerRepositoryImpl parDao) {
		parDao.atualizaFundamentacao("id", "novafundamentacao");

	}

	private static void adicionaNotaParecer(ParecerRepositoryImpl parDao) {
		Avaliavel o = new Pontuacao("o", new Valor("o1"));
		Avaliavel s = new Pontuacao("s", new Valor("o2"));

		Nota nota = new Nota(o, s, "simples erro");

		parDao.adicionaNota("id", nota);

	}

	private static void removeNotaParecer(ParecerRepositoryImpl parDao) {
		Avaliavel o = new Pontuacao("o", new Valor("o1"));
		Avaliavel s = new Pontuacao("s", new Valor("o2"));

		Nota nota = new Nota(o, s, "simples erro");

		parDao.removeNota("id", nota.getItemOriginal());

	}
}
