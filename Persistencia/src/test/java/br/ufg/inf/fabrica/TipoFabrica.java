package br.ufg.inf.fabrica;

import java.util.HashSet;
import java.util.Set;

import br.ufg.inf.es.saep.sandbox.dominio.Atributo;
import br.ufg.inf.es.saep.sandbox.dominio.Tipo;

/**
 * Classe respons�vel por manter possiveis cria��es de novos tipos.
 *
 */
public class TipoFabrica {

	/**
	 * M�todo respons�vel por criar um novo tipo.
	 */
	public Tipo novoTipo() {
		final Set<Atributo> lista = new HashSet<Atributo>();

		final Atributo atributo = new Atributo("nomeAtributo", "descAtributo", 1);

		lista.add(atributo);

		Tipo tipo = new Tipo("t", "nomeTipo", "desc", lista);

		return tipo;
	}
}
