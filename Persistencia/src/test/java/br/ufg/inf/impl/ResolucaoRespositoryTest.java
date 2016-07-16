package br.ufg.inf.impl;

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
		// TODO Auto-generated method stub
	}

	@Test
	public void persiste() {

		this.resolucaoRepository.persiste(this.resolucaoFabrica.novaResolucao());
	}

	public void remove() {
		// TODO Auto-generated method stub
	}

	public void resolucoes() {
		// TODO Auto-generated method stub
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
