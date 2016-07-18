package br.ufg.inf.impl;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import br.ufg.inf.es.saep.sandbox.dominio.Avaliavel;
import br.ufg.inf.es.saep.sandbox.dominio.ExisteParecerReferenciandoRadoc;
import br.ufg.inf.es.saep.sandbox.dominio.IdentificadorDesconhecido;
import br.ufg.inf.es.saep.sandbox.dominio.IdentificadorExistente;
import br.ufg.inf.es.saep.sandbox.dominio.Nota;
import br.ufg.inf.es.saep.sandbox.dominio.Parecer;
import br.ufg.inf.es.saep.sandbox.dominio.ParecerRepository;
import br.ufg.inf.es.saep.sandbox.dominio.Pontuacao;
import br.ufg.inf.es.saep.sandbox.dominio.Radoc;
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

		final Parecer parecer = this.parecerFabrica.novoParecer();

		this.parecerRepository.persisteParecer(parecer);

		this.parecerRepository.adicionaNota(parecer.getId(), nota);

		final Parecer parecerRetorno = this.parecerRepository.byId(parecer.getId());

		Assert.assertTrue(parecerRetorno.getNotas().size() > parecer.getNotas().size());
	}

	@Test
	public void removeNota() {
		Avaliavel o = new Pontuacao("o", new Valor("o1"));
		Avaliavel s = new Pontuacao("s", new Valor("o2"));

		Nota nota = new Nota(o, s, "simples erro");

		final Parecer parecer = this.parecerFabrica.novoParecer();

		this.parecerRepository.persisteParecer(parecer);

		this.parecerRepository.removeNota(parecer.getId(), nota.getItemOriginal());

		final Parecer parecerRetorno = this.parecerRepository.byId(parecer.getId());

		Assert.assertTrue(parecerRetorno.getNotas().isEmpty());
	}

	@Test
	public void persisteParecer() {

		final Parecer parecer = this.parecerFabrica.novoParecer();

		this.parecerRepository.persisteParecer(parecer);

		final Parecer parecerRetorno = this.parecerRepository.byId(parecer.getId());

		Assert.assertNotNull(parecerRetorno);
	}

	@Test(expected = IdentificadorExistente.class)
	public void persisteParecerJaExistente() {

		final Parecer parecer = this.parecerFabrica.novoParecer();

		this.parecerRepository.persisteParecer(parecer);

		this.parecerRepository.persisteParecer(parecer);

		final Parecer parecerRetorno = this.parecerRepository.byId(parecer.getId());

		Assert.assertNotNull(parecerRetorno);
	}

	@Test
	public void atualizaFundamentacao() {
		final Parecer parecer = this.parecerFabrica.novoParecer();

		this.parecerRepository.persisteParecer(parecer);

		this.parecerRepository.atualizaFundamentacao(parecer.getId(), "novafundamentacao");

		final Parecer parecerRetorno = this.parecerRepository.byId(parecer.getId());

		Assert.assertNotEquals(parecer.getFundamentacao(), parecerRetorno.getFundamentacao());
	}

	@Test(expected = IdentificadorDesconhecido.class)
	public void atualizaFundamentacaoIdDesconhecido() {
		final Parecer parecer = this.parecerFabrica.novoParecer();

		this.parecerRepository.persisteParecer(parecer);

		this.parecerRepository.atualizaFundamentacao("id", "novafundamentacao");

		final Parecer parecerRetorno = this.parecerRepository.byId(parecer.getId());

		Assert.assertNotEquals(parecer.getFundamentacao(), parecerRetorno.getFundamentacao());
	}

	@Test
	public void byId() {
		final Parecer parecer = this.parecerFabrica.novoParecer();

		this.parecerRepository.persisteParecer(parecer);

		final Parecer parecerRetorno = this.parecerRepository.byId(parecer.getId());

		Assert.assertNotNull(parecerRetorno);
	}

	@Test
	public void removeParecer() {
		final Parecer parecer = this.parecerFabrica.novoParecer();

		this.parecerRepository.persisteParecer(parecer);

		this.parecerRepository.removeParecer(parecer.getId());

		final Parecer parecerRetorno = this.parecerRepository.byId(parecer.getId());

		Assert.assertNull(parecerRetorno);

	}

	@Test
	public void radocById() {
		final Radoc radoc = this.radocFabrica.novoRadoc();

		this.parecerRepository.persisteRadoc(radoc);

		final Radoc radocRetorno = this.parecerRepository.radocById(radoc.getId());

		Assert.assertNotNull(radocRetorno);
	}

	@Test
	public void persisteRadoc() {
		final Radoc radoc = this.radocFabrica.novoRadoc();

		final String idSalvo = this.parecerRepository.persisteRadoc(radoc);

		final Radoc radocRetorno = this.parecerRepository.radocById(radoc.getId());

		Assert.assertNotNull(idSalvo);
		Assert.assertNotNull(radocRetorno);
	}

	@Test(expected = IdentificadorExistente.class)
	public void persisteRadocJaExistente() {
		final Radoc radoc = this.radocFabrica.novoRadoc();

		this.parecerRepository.persisteRadoc(radoc);

		this.parecerRepository.persisteRadoc(radoc);

		final Radoc radocRetorno = this.parecerRepository.radocById(radoc.getId());

		Assert.assertNull(radocRetorno);
	}

	@Test
	public void removeRadoc() {
		final Radoc radoc = this.radocFabrica.novoRadoc();

		final String idSalvo = this.parecerRepository.persisteRadoc(radoc);

		this.parecerRepository.removeRadoc(radoc.getId());

		final Radoc radocRetorno = this.parecerRepository.radocById(radoc.getId());

		Assert.assertNotNull(idSalvo);
		Assert.assertNull(radocRetorno);
	}

	@Test(expected = ExisteParecerReferenciandoRadoc.class)
	public void removeRadocReferenciado() {

		final Parecer parecer = this.parecerFabrica.novoParecer();
		this.parecerRepository.persisteParecer(parecer);
		parecer.getRadocs();

		Radoc radoc = null;
		for (String id : parecer.getRadocs()) {
			radoc = this.radocFabrica.novoRadocIdFixo(id);
			this.parecerRepository.persisteRadoc(radoc);
		}

		this.parecerRepository.removeRadoc(radoc.getId());

		final Radoc radocRetorno = this.parecerRepository.radocById(radoc.getId());

		Assert.assertNull(radocRetorno);
	}
}
