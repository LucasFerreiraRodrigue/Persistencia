package br.ufg.inf.fabrica;

import java.util.HashSet;
import java.util.Set;

import br.ufg.inf.es.saep.sandbox.dominio.Atributo;
import br.ufg.inf.es.saep.sandbox.dominio.Tipo;

/**
 * Classe responsável por manter possiveis criações de novos tipos.
 *
 */
public class TipoFabrica {

	/**
	 * Método responsável por criar um novo tipo.
	 */
	public Tipo novoTipo() {
		final Set<Atributo> lista = new HashSet<Atributo>();

		final Atributo atributo = new Atributo("nomeAtributo", "descAtributo", 1);

		lista.add(atributo);

		Tipo tipo = new Tipo("t", "nomeTipo", "desc", lista);

		return tipo;
	}
}
