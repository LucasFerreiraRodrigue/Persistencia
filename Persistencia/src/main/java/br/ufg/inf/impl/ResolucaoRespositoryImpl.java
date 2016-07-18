package br.ufg.inf.impl;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.xml.DomDriver;

import br.ufg.inf.domain.Resolucoes;
import br.ufg.inf.domain.Tipos;
import br.ufg.inf.es.saep.sandbox.dominio.Regra;
import br.ufg.inf.es.saep.sandbox.dominio.Resolucao;
import br.ufg.inf.es.saep.sandbox.dominio.ResolucaoRepository;
import br.ufg.inf.es.saep.sandbox.dominio.Tipo;

public class ResolucaoRespositoryImpl implements ResolucaoRepository {

	private static String CAMINHO_BASE = "data";
	private static String NOME_ARQUIVO_RESOLUCAO = "/Resolucoes/Resolucao";
	private static String NOME_ARQUIVO_TIPO = "/Tipos/Tipo";
	private static String XML = ".xml";

	Logger log = Logger.getLogger(ResolucaoRespositoryImpl.class.getName());

	private File resolucaoFile = new File(ResolucaoRespositoryImpl.CAMINHO_BASE
			+ ResolucaoRespositoryImpl.NOME_ARQUIVO_RESOLUCAO + ResolucaoRespositoryImpl.XML);

	private File tipoFile = new File(ResolucaoRespositoryImpl.CAMINHO_BASE + ResolucaoRespositoryImpl.NOME_ARQUIVO_TIPO
			+ ResolucaoRespositoryImpl.XML);

	public Resolucao byId(String id) {

		Resolucao retorno = null;

		if (this.resolucaoFile.exists()) {

			XStream stream = new XStream();
			stream.alias("Resolucoes", Resolucoes.class);
			stream.alias("Resolucao", Resolucao.class);
			stream.alias("Regra", Regra.class);

			Resolucoes resolucoes = (Resolucoes) stream.fromXML(this.resolucaoFile);

			for (Resolucao resolucao : resolucoes.getResolucoes()) {
				if (resolucao.getId().equals(id)) {
					retorno = resolucao;
				}
			}
		}

		return retorno;
	}

	public String persiste(Resolucao resolucao) {

		this.resolucaoFile.getParentFile().mkdirs();

		XStream stream = new XStream(new DomDriver());
		stream.alias("Resolucoes", Resolucoes.class);
		stream.alias("Resolucao", Resolucao.class);
		stream.alias("Regra", Regra.class);

		Resolucoes resolucoes = new Resolucoes();

		try {
			if (this.resolucaoFile.exists()) {
				resolucoes = (Resolucoes) stream.fromXML(this.resolucaoFile);
			} else {
				this.resolucaoFile.createNewFile();
				resolucoes.setResolucoes(new ArrayList<Resolucao>());
			}

			for (Resolucao resol : resolucoes.getResolucoes()) {
				if (resol.getId().equals(resolucao.getId())) {
					return null;
				}
			}

			resolucoes.getResolucoes().add(resolucao);

			final OutputStream out = new FileOutputStream(this.resolucaoFile);

			stream.toXML(resolucoes, out);
		} catch (IOException e) {
			log.log(Level.SEVERE, e.getMessage(), e);
		}

		return resolucao.getId();
	}

	public void persisteTipo(Tipo tipo) {

		this.tipoFile.getParentFile().mkdirs();

		XStream stream = new XStream(new DomDriver());
		stream.alias("Tipos", Tipos.class);
		stream.alias("Tipo", Tipo.class);

		Tipos tipos = new Tipos();

		try {
			if (this.tipoFile.exists()) {
				tipos = (Tipos) stream.fromXML(this.tipoFile);
			} else {
				this.tipoFile.createNewFile();
				tipos.setTipos(new ArrayList<Tipo>());
			}

			for (Tipo tip : tipos.getTipos()) {
				if (tip.getId().equals(tipo.getId())) {
					return;
				}
			}

			tipos.getTipos().add(tipo);

			final OutputStream out = new FileOutputStream(this.tipoFile);

			stream.toXML(tipos, out);
		} catch (IOException e) {
			log.log(Level.SEVERE, e.getMessage(), e);
		}

	}

	public boolean remove(String id) {

		boolean executado = false;

		if (this.resolucaoFile.exists()) {

			Resolucao resolucaoRemover = null;

			try {

				XStream stream = new XStream();
				stream.alias("Resolucoes", Resolucoes.class);
				stream.alias("Resolucao", Resolucao.class);
				stream.alias("Regra", Regra.class);

				Resolucoes resolucoes = (Resolucoes) stream.fromXML(this.resolucaoFile);

				for (Resolucao resolucao : resolucoes.getResolucoes()) {
					if (resolucao.getId().equals(id)) {
						resolucaoRemover = resolucao;
					}
				}

				if (resolucaoRemover != null) {
					resolucoes.getResolucoes().remove(resolucaoRemover);

					final OutputStream out = new FileOutputStream(this.resolucaoFile);

					stream.toXML(resolucoes, out);

					executado = true;

				}

			} catch (Exception e) {
				log.log(Level.SEVERE, e.getMessage(), e);
			}
		}
		return executado;
	}

	public void removeTipo(String codigo) {

		if (this.tipoFile.exists()) {

			Tipo tipoRemover = null;

			try {

				if (this.tipoFile.exists()) {

					XStream stream = new XStream();
					stream.alias("Tipos", Tipos.class);
					stream.alias("Tipo", Tipo.class);

					Tipos tipos = (Tipos) stream.fromXML(this.tipoFile);

					for (Tipo tipo : tipos.getTipos()) {
						if (tipo.getId().equals(codigo)) {
							tipoRemover = tipo;
						}
					}

					if (tipoRemover != null) {
						tipos.getTipos().remove(tipoRemover);

						final OutputStream out = new FileOutputStream(this.tipoFile);

						stream.toXML(tipos, out);
					}
				}

			} catch (Exception e) {
				log.log(Level.SEVERE, e.getMessage(), e);
			}
		}
	}

	public List<String> resolucoes() {
		final List<String> listaId = new ArrayList<String>();

		if (this.resolucaoFile.exists()) {

			XStream stream = new XStream();
			stream.alias("Resolucoes", Resolucoes.class);
			stream.alias("Resolucao", Resolucao.class);
			stream.alias("Regra", Regra.class);

			Resolucoes resolucoes = (Resolucoes) stream.fromXML(this.resolucaoFile);

			for (Resolucao resolucao : resolucoes.getResolucoes()) {
				listaId.add(resolucao.getId());
			}
		}

		return listaId;
	}

	public Tipo tipoPeloCodigo(String codigo) {
		Tipo tipoRetorno = null;

		if (this.tipoFile.exists()) {

			XStream stream = new XStream();
			stream.alias("Tipos", Tipos.class);
			stream.alias("Tipo", Tipo.class);

			Tipos tipos = (Tipos) stream.fromXML(this.tipoFile);

			for (Tipo tipo : tipos.getTipos()) {
				if (tipo.getId().equals(codigo)) {
					tipoRetorno = tipo;
					break;
				}
			}
		}
		return tipoRetorno;
	}

	public List<Tipo> tiposPeloNome(String nome) {
		final List<Tipo> listaTipo = new ArrayList<Tipo>();

		if (this.tipoFile.exists()) {

			XStream stream = new XStream();
			stream.alias("Tipos", Tipos.class);
			stream.alias("Tipo", Tipo.class);

			Tipos tipos = (Tipos) stream.fromXML(this.tipoFile);

			for (Tipo tipo : tipos.getTipos()) {
				if (tipo.getNome().equals(nome)) {
					listaTipo.add(tipo);
				}
			}
		}
		return listaTipo;
	}

}