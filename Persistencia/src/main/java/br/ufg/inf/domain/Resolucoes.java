/*
 * Copyright (c) 2016. Fábrica de Software - Instituto de Informática (UFG)
 * Creative Commons Attribution 4.0 International License.
 */

package br.ufg.inf.domain;

import java.util.ArrayList;
import java.util.List;

import br.ufg.inf.es.saep.sandbox.dominio.Resolucao;

public class Resolucoes {

	private List<Resolucao> resolucoes;

	public List<Resolucao> getResolucoes() {
		if (this.resolucoes == null) {
			this.resolucoes = new ArrayList<Resolucao>();
		}
		return resolucoes;
	}

	public void setResolucoes(List<Resolucao> resolucoes) {
		this.resolucoes = resolucoes;
	}

}
