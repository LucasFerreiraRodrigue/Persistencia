package br.ufg.inf.domain;

import java.util.List;

import br.ufg.inf.es.saep.sandbox.dominio.Parecer;

/**
 * Classe utilizada para agrupar as entidades {@link Parecer}.
 *
 */
public class Pareceres {

	private List<Parecer> pareceres;

	public List<Parecer> getPareceres() {
		return pareceres;
	}

	public void setPareceres(List<Parecer> pareceres) {
		this.pareceres = pareceres;
	}
}
