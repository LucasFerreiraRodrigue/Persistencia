package br.ufg.inf.impl;

import java.util.List;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import br.ufg.inf.es.saep.sandbox.dominio.Resolucao;
import br.ufg.inf.es.saep.sandbox.dominio.ResolucaoRepository;
import br.ufg.inf.es.saep.sandbox.dominio.Tipo;
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

		final String idSalvo = this.resolucaoRepository.persiste(this.resolucaoFabrica.novaResolucao());

		final Resolucao resolucao = this.resolucaoRepository.byId(idSalvo);

		Assert.assertNotNull(resolucao);
	}

	@Test
	public void byIdInexistente() {

		final Resolucao resolucao = this.resolucaoRepository.byId("inexistente");

		Assert.assertNull(resolucao);
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

	@Test
	public void remove() {

		final String id = this.resolucaoRepository.persiste(this.resolucaoFabrica.novaResolucao());

		final Boolean removido = this.resolucaoRepository.remove(id);

		Assert.assertTrue(removido);
	}

	@Test
	public void removeNadaParaRemover() {

		final Boolean removido = this.resolucaoRepository.remove("id");

		Assert.assertFalse(removido);
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

	@Test
	public void tipoPeloCodigo() {
		final Tipo tipo = this.tipoFabrica.novoTipo();

		this.resolucaoRepository.persisteTipo(tipo);

		final Tipo tipoRetorno = this.resolucaoRepository.tipoPeloCodigo(tipo.getId());

		Assert.assertNotNull(tipoRetorno);
	}

	@Test
	public void tiposPeloNome() {
		final Tipo tipo = this.tipoFabrica.novoTipo();

		this.resolucaoRepository.persisteTipo(tipo);

		final List<Tipo> listaTipo = this.resolucaoRepository.tiposPeloNome(tipo.getNome());

		Assert.assertFalse(listaTipo.isEmpty());
	}

}
