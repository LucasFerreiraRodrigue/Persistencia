package br.ufg.inf.impl;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import javax.xml.namespace.QName;

import br.ufg.inf.domain.Resolucoes;
import br.ufg.inf.domain.Tipos;
import br.ufg.inf.es.saep.sandbox.dominio.Resolucao;
import br.ufg.inf.es.saep.sandbox.dominio.ResolucaoRepository;
import br.ufg.inf.es.saep.sandbox.dominio.Tipo;

public class ResolucaoRespositoryImpl implements ResolucaoRepository {

	private static String CAMINHO_BASE = "data";
	private static String NOME_ARQUIVO_RESOLUCAO = "/Resolucoes/Resolucao";
	private static String NOME_ARQUIVO_TIPO = "/Tipos/Tipo";
	private static String XML = ".xml";

	private File resolucaoFile = new File(ResolucaoRespositoryImpl.CAMINHO_BASE
			+ ResolucaoRespositoryImpl.NOME_ARQUIVO_RESOLUCAO + ResolucaoRespositoryImpl.XML);

	private File tipoFile = new File(ResolucaoRespositoryImpl.CAMINHO_BASE + ResolucaoRespositoryImpl.NOME_ARQUIVO_TIPO
			+ ResolucaoRespositoryImpl.XML);

	public Resolucao byId(String arg0) {

		Resolucao retorno = null;
		if (this.resolucaoFile.exists()) {

			JAXBContext jaxbContext;
			try {
				jaxbContext = JAXBContext.newInstance(Resolucoes.class);
				Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();

				Resolucoes resolucoes = (Resolucoes) jaxbUnmarshaller.unmarshal(this.resolucaoFile);

				for (Resolucao resolucao : resolucoes.getResolucoes()) {
					if (resolucao.getId().equals(arg0)) {
						retorno = resolucao;
						break;
					}
				}
			} catch (JAXBException e) {
				e.printStackTrace();
			}
		}
		return retorno;
	}

	public String persiste(Resolucao resolucao) {

		this.resolucaoFile.getParentFile().mkdirs();

		try {
			JAXBContext jc = JAXBContext.newInstance(Resolucoes.class);

			final Resolucoes res = new Resolucoes();
			res.getResolucoes().add(resolucao);

			JAXBElement<Resolucoes> je2 = new JAXBElement<Resolucoes>(new QName("resolucao"), Resolucoes.class, res);
			Marshaller marshaller = jc.createMarshaller();
			
			marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
			marshaller.setAdapter(new AnyTypeAdapter());
			marshaller.marshal(je2, this.resolucaoFile);
		} catch (JAXBException e) {
			e.printStackTrace();
		}
		return resolucao.getId();
	}

	public void persisteTipo(Tipo tipo) {

		JAXBContext jaxbContext;
		try {
			jaxbContext = JAXBContext.newInstance(Tipos.class);
			Marshaller jaxbMarshaller = jaxbContext.createMarshaller();
			jaxbMarshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
			Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();

			Tipos tiposSalvos;

			if (this.tipoFile.exists()) {
				tiposSalvos = (Tipos) jaxbUnmarshaller.unmarshal(this.tipoFile);
				if (tiposSalvos.getTipos() != null) {
					for (Tipo tipoSalvo : tiposSalvos.getTipos()) {
						if (tipoSalvo.getId().equals(tipo.getId())) {
							return;
						}
					}
				} else {
					tiposSalvos.setTipos(new ArrayList<Tipo>());
				}
			} else {
				tiposSalvos = new Tipos();
				tiposSalvos.setTipos(new ArrayList<Tipo>());
			}

			tiposSalvos.getTipos().add(tipo);

			this.tipoFile.getParentFile().mkdirs();
			jaxbMarshaller.marshal(tiposSalvos, this.tipoFile);
		} catch (JAXBException e) {
			e.printStackTrace();
		}

	}

	public boolean remove(String id) {

		boolean executado = false;

		if (this.resolucaoFile.exists()) {

			Resolucao resolucaoRemover = null;

			JAXBContext jaxbContext;
			try {
				jaxbContext = JAXBContext.newInstance(Resolucoes.class);
				Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();
				Marshaller jaxbMarshaller = jaxbContext.createMarshaller();
				jaxbMarshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);

				Resolucoes resolucoes = (Resolucoes) jaxbUnmarshaller.unmarshal(this.resolucaoFile);

				for (Resolucao resolucao : resolucoes.getResolucoes()) {
					if (resolucao.getId().equals(id)) {
						resolucaoRemover = resolucao;
					}
				}
				resolucoes.getResolucoes().remove(resolucaoRemover);

				jaxbMarshaller.marshal(resolucoes, resolucaoFile);

				executado = true;
			} catch (JAXBException e) {
				e.printStackTrace();
			}
		}
		return executado;
	}

	public void removeTipo(String codigo) {
		Tipo tipoRemover = null;

		if (this.tipoFile.exists()) {

			JAXBContext jaxbContext;
			try {
				jaxbContext = JAXBContext.newInstance(Tipos.class);
				Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();
				Marshaller jaxbMarshaller = jaxbContext.createMarshaller();
				jaxbMarshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);

				Tipos tipos = (Tipos) jaxbUnmarshaller.unmarshal(this.tipoFile);

				for (Tipo tipo : tipos.getTipos()) {
					if (tipo.getId().equals(codigo)) {
						tipoRemover = tipo;
					}
				}
				tipos.getTipos().remove(tipoRemover);

				jaxbMarshaller.marshal(tipos, this.tipoFile);
			} catch (JAXBException e) {
				e.printStackTrace();
			}
		}
	}

	public List<String> resolucoes() {
		final List<String> listaIdResolucao = new ArrayList<String>();

		if (this.resolucaoFile.exists()) {
			JAXBContext jaxbContext;
			try {
				jaxbContext = JAXBContext.newInstance(Resolucoes.class);
				Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();

				Resolucoes resolucoes = (Resolucoes) jaxbUnmarshaller.unmarshal(this.resolucaoFile);

				for (Resolucao resolucao : resolucoes.getResolucoes()) {
					listaIdResolucao.add(resolucao.getId());
				}

			} catch (JAXBException e) {
				e.printStackTrace();
			}
		}
		return listaIdResolucao;
	}

	public Tipo tipoPeloCodigo(String codigo) {
		Tipo tipoRetorno = null;

		if (this.tipoFile.exists()) {

			JAXBContext jaxbContext;
			try {

				jaxbContext = JAXBContext.newInstance(Tipos.class);
				Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();

				Tipos resolucoes = (Tipos) jaxbUnmarshaller.unmarshal(this.tipoFile);

				for (Tipo tipo : resolucoes.getTipos()) {
					if (tipo.getId().equals(codigo)) {
						tipoRetorno = tipo;
						break;
					}
				}

			} catch (JAXBException e) {
				e.printStackTrace();
			}
		}
		return tipoRetorno;
	}

	public List<Tipo> tiposPeloNome(String nome) {
		final List<Tipo> listaTipo = new ArrayList<Tipo>();

		if (this.tipoFile.exists()) {

			JAXBContext jaxbContext;
			try {
				jaxbContext = JAXBContext.newInstance(Tipos.class);
				Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();

				Tipos tipos = (Tipos) jaxbUnmarshaller.unmarshal(this.tipoFile);

				for (Tipo tipo : tipos.getTipos()) {
					if (tipo.getNome().equals(nome)) {
						listaTipo.add(tipo);
					}
				}
			} catch (JAXBException e) {
				e.printStackTrace();
			}
		}
		return listaTipo;
	}

}