package br.ufg.inf.impl;

import org.junit.Before;
import org.junit.Test;

import br.ufg.inf.es.saep.sandbox.dominio.Avaliavel;
import br.ufg.inf.es.saep.sandbox.dominio.Nota;
import br.ufg.inf.es.saep.sandbox.dominio.ParecerRepository;
import br.ufg.inf.es.saep.sandbox.dominio.Pontuacao;
import br.ufg.inf.es.saep.sandbox.dominio.Valor;
import br.ufg.inf.fabrica.ParecerFabrica;
import br.ufg.inf.fabrica.RadocFabrica;

/**
 * Classe de testes para {@link ParecerRepositoryImpl}
 *
 */
public class ParecerRepositoryTest {

	private ParecerRepository parecerRepository;

	private ParecerFabrica parecerFabrica;

	private RadocFabrica radocFabrica;

	@Before
	public void setUp() {
		this.parecerRepository = new ParecerRepositoryImpl();
		this.parecerFabrica = new ParecerFabrica();
		this.radocFabrica = new RadocFabrica();
	}

	@Test
	public void adicionaNota() {
		Avaliavel o = new Pontuacao("o", new Valor("o1"));
		Avaliavel s = new Pontuacao("s", new Valor("o2"));

		Nota nota = new Nota(o, s, "simples erro");

		this.parecerRepository.adicionaNota("id", nota);

	}

	@Test
	public void removeNota() {
		Avaliavel o = new Pontuacao("o", new Valor("o1"));
		Avaliavel s = new Pontuacao("s", new Valor("o2"));

		Nota nota = new Nota(o, s, "simples erro");

		this.parecerRepository.removeNota("id", nota.getItemOriginal());

	}

	@Test
	public void persisteParecer() {

		this.parecerRepository.persisteParecer(this.parecerFabrica.novoParecer());

	}

	@Test
	public void atualizaFundamentacao() {
		this.parecerRepository.atualizaFundamentacao("id", "novafundamentacao");

	}

	@Test
	public void byId() {
		// TODO Auto-generated method stub Parecer by id
	}

	@Test
	public void removeParecer() {
		// TODO Auto-generated method stub

	}

	@Test
	public void radocById() {
		// TODO Auto-generated method stub
	}

	@Test
	public void persisteRadoc() {
		this.parecerRepository.persisteRadoc(this.radocFabrica.novoRadoc());
	}

	@Test
	public void removeRadoc() {
		// TODO Auto-generated method stub

	}
}
