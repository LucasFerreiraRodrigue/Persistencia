package br.ufg.inf.impl;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

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

	Logger log = Logger.getLogger(ResolucaoRespositoryImpl.class.getName());

	private File resolucaoFile = new File(ResolucaoRespositoryImpl.CAMINHO_BASE
			+ ResolucaoRespositoryImpl.NOME_ARQUIVO_RESOLUCAO + ResolucaoRespositoryImpl.XML);

	private File tipoFile = new File(ResolucaoRespositoryImpl.CAMINHO_BASE + ResolucaoRespositoryImpl.NOME_ARQUIVO_TIPO
			+ ResolucaoRespositoryImpl.XML);

	public Resolucao byId(String arg0) {

		Resolucao retorno = null;

		return retorno;
	}

	@SuppressWarnings("resource")
	public String persiste(Resolucao resolucao) {

		this.resolucaoFile.getParentFile().mkdirs();

		try {
			if (!this.resolucaoFile.exists()) {
				this.resolucaoFile.createNewFile();
			}
			Scanner scanner = new Scanner(this.resolucaoFile);

			while (scanner.hasNextLine()) {
				String line = scanner.nextLine();
				if (line.contains("<id>" + resolucao.getId() + "</id>")) {
					return null;
				}
			}
			Files.write(Paths.get(this.resolucaoFile.getPath()), XMLParserUtil.objectToXmlString(resolucao).getBytes(),
					StandardOpenOption.APPEND);
		} catch (FileNotFoundException e) {
			log.log(Level.SEVERE, e.getMessage(), e);
		} catch (IOException e) {
			log.log(Level.SEVERE, e.getMessage(), e);
		} catch (IllegalArgumentException e) {
			log.log(Level.SEVERE, e.getMessage(), e);
		} catch (IllegalAccessException e) {
			log.log(Level.SEVERE, e.getMessage(), e);
		}
		return resolucao.getId();
	}

	@SuppressWarnings("resource")
	public void persisteTipo(Tipo tipo) {

		this.tipoFile.getParentFile().mkdirs();

		try {
			if (!this.tipoFile.exists()) {
				this.tipoFile.createNewFile();
			}
			Scanner scanner = new Scanner(this.resolucaoFile);

			while (scanner.hasNextLine()) {
				String line = scanner.nextLine();
				if (line.contains("<id>" + tipo.getId() + "</id>")) {
					return;
				}
			}
			Files.write(Paths.get(this.tipoFile.getPath()), XMLParserUtil.objectToXmlString(tipo).getBytes(),
					StandardOpenOption.APPEND);
		} catch (IOException e) {
			log.log(Level.SEVERE, e.getMessage(), e);
		} catch (IllegalArgumentException e) {
			log.log(Level.SEVERE, e.getMessage(), e);
		} catch (IllegalAccessException e) {
			log.log(Level.SEVERE, e.getMessage(), e);
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
				log.log(Level.SEVERE, e.getMessage(), e);
			} catch (Exception e) {
				log.log(Level.SEVERE, e.getMessage(), e);
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
				log.log(Level.SEVERE, e.getMessage(), e);
			}
		}
	}

	@SuppressWarnings("resource")
	public List<String> resolucoes() {
		final List<String> listaId = new ArrayList<String>();

		try {
			Scanner scanner = new Scanner(this.resolucaoFile);

			while (scanner.hasNextLine()) {
				String line = scanner.nextLine();
				if (line.contains("<id>")) {
					listaId.add(line.replace("<id>", "").replace("</id>", ""));
				}
			}
		} catch (FileNotFoundException e) {
			log.log(Level.SEVERE, e.getMessage(), e);
		}
		return listaId;
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
				log.log(Level.SEVERE, e.getMessage(), e);
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
				log.log(Level.SEVERE, e.getMessage(), e);
			} catch (Exception e) {
				log.log(Level.SEVERE, e.getMessage(), e);
			}
		}
		return listaTipo;
	}

}