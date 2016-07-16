package br.ufg.inf.impl;

import org.junit.Before;
import org.junit.Test;

import br.ufg.inf.es.saep.sandbox.dominio.ParecerRepository;
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

	public void adicionaNota() {
		// TODO Auto-generated method stub

	}

	public void removeNota() {
		// TODO Auto-generated method stub

	}

	@Test
	public void persisteParecer() {

		this.parecerRepository.persisteParecer(this.parecerFabrica.novoParecer());

	}

	public void atualizaFundamentacao() {
		// TODO Auto-generated method stub

	}

	public void byId() {
		// TODO Auto-generated method stub Parecer by id
	}

	public void removeParecer() {
		// TODO Auto-generated method stub

	}

	public void radocById() {
		// TODO Auto-generated method stub
	}

	@Test
	public void persisteRadoc() {
		this.parecerRepository.persisteRadoc(this.radocFabrica.novoRadoc());
	}

	public void removeRadoc() {
		// TODO Auto-generated method stub

	}
}
