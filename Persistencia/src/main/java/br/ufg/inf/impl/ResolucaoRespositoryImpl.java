package br.ufg.inf.impl;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;

import br.ufg.inf.domain.Resolucoes;
import br.ufg.inf.domain.Tipos;
import br.ufg.inf.es.saep.sandbox.dominio.Resolucao;
import br.ufg.inf.es.saep.sandbox.dominio.ResolucaoRepository;
import br.ufg.inf.es.saep.sandbox.dominio.Tipo;
import br.ufg.inf.util.XMLParserUtil;

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
			if (!this.resolucaoFile.exists()) {
				this.resolucaoFile.createNewFile();
			}
			Files.write(Paths.get(this.resolucaoFile.getPath()), XMLParserUtil.objectToXmlString(resolucao).getBytes(),
					StandardOpenOption.APPEND);
		} catch (IOException e) {
			// exception handling left as an exercise for the reader
		} catch (IllegalArgumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return resolucao.getId();
	}

	public void persisteTipo(Tipo tipo) {

		this.tipoFile.getParentFile().mkdirs();

		try {
			if (!this.tipoFile.exists()) {
				this.tipoFile.createNewFile();
			}
			Files.write(Paths.get(this.tipoFile.getPath()), XMLParserUtil.objectToXmlString(tipo).getBytes(),
					StandardOpenOption.APPEND);
		} catch (IOException e) {
			// exception handling left as an exercise for the reader
		} catch (IllegalArgumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
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