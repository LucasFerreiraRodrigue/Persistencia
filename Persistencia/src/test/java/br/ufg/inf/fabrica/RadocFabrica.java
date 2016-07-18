package br.ufg.inf.fabrica;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import br.ufg.inf.es.saep.sandbox.dominio.Radoc;
import br.ufg.inf.es.saep.sandbox.dominio.Relato;
import br.ufg.inf.es.saep.sandbox.dominio.Valor;

/**
 * Classe responsável por manter possiveis criações de novos radocs.
 *
 */
public class RadocFabrica {

	/**
	 * Método responsável por criar um novo radoc.
	 */
	public Radoc novoRadoc() {

		Random rd = new Random();

		Map<String, Valor> valores = new HashMap<String, Valor>(1);
		valores.put("ano", new Valor(2016));

		final List<Relato> relatos = new ArrayList<Relato>();

		relatos.add(new Relato("a", valores));

		Radoc radoc = new Radoc(String.valueOf(rd.nextInt(100000)), 0, relatos);

		return radoc;
	}

	public Radoc novoRadocIdFixo(String id) {

		Map<String, Valor> valores = new HashMap<String, Valor>(1);
		valores.put("ano", new Valor(2016));

		final List<Relato> relatos = new ArrayList<Relato>();

		relatos.add(new Relato("a", valores));

		Radoc radoc = new Radoc(id, 0, relatos);

		return radoc;
	}
}
