package br.ufg.inf.impl;

import java.util.List;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import br.ufg.inf.es.saep.sandbox.dominio.ResolucaoRepository;
import br.ufg.inf.fabrica.ResolucaoFabrica;
import br.ufg.inf.fabrica.TipoFabrica;

/**
 * Classe de testes para {@link ResolucaoRespositoryImpl}
 *
 */
public class ResolucaoRespositoryTest {

	private ResolucaoRepository resolucaoRepository;

	private ResolucaoFabrica resolucaoFabrica;

	private TipoFabrica tipoFabrica;

	@Before
	public void setUp() {
		this.resolucaoRepository = new ResolucaoRespositoryImpl();
		this.resolucaoFabrica = new ResolucaoFabrica();
		this.tipoFabrica = new TipoFabrica();
	}

	@Test
	public void byId() {
		// TODO
	}

	@Test
	public void persiste() {

		final String idSalvo = this.resolucaoRepository.persiste(this.resolucaoFabrica.novaResolucao());

		Assert.assertNotNull(idSalvo);
	}

	@Test
	public void persisteComIdJaExistente() {

		final String id = this.resolucaoRepository.persiste(this.resolucaoFabrica.novaResolucao());

		final String idSalvo = this.resolucaoRepository.persiste(this.resolucaoFabrica.novaResolucaoComIdFixo(id));

		Assert.assertNull(idSalvo);
	}

	public void remove() {
		// TODO Auto-generated method stub
	}

	@Test
	public void resolucoes() {

		final String id = this.resolucaoRepository.persiste(this.resolucaoFabrica.novaResolucao());

		final List<String> listaIds = this.resolucaoRepository.resolucoes();

		Assert.assertTrue(listaIds.contains(id));
	}

	@Test
	public void persisteTipo() {

		this.resolucaoRepository.persisteTipo(this.tipoFabrica.novoTipo());

	}

	public void removeTipo() {
		// TODO Auto-generated method stub

	}

	public void tipoPeloCodigo() {
		// TODO Auto-generated method stub
	}

	public void tiposPeloNome() {
		// TODO Auto-generated method stub
	}

}
