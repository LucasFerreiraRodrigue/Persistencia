package br.ufg.inf.impl;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;

import br.ufg.inf.domain.Pareceres;
import br.ufg.inf.domain.Radocs;
import br.ufg.inf.es.saep.sandbox.dominio.Avaliavel;
import br.ufg.inf.es.saep.sandbox.dominio.Nota;
import br.ufg.inf.es.saep.sandbox.dominio.Parecer;
import br.ufg.inf.es.saep.sandbox.dominio.ParecerRepository;
import br.ufg.inf.es.saep.sandbox.dominio.Pontuacao;
import br.ufg.inf.es.saep.sandbox.dominio.Radoc;
import br.ufg.inf.util.XMLParserUtil;

public class ParecerRepositoryImpl implements ParecerRepository {
	private static String CAMINHO_BASE = "data";
	private static String NOME_ARQUIVO_PARECER = "/Pareceres/Parecer";
	private static String NOME_ARQUIVO_RADOC = "/Radocs/Radocs";
	private static String EXTENSAO_XML = ".xml";

	Logger log = Logger.getLogger(ParecerRepositoryImpl.class.getName());

	private File parecerFile = new File(ParecerRepositoryImpl.CAMINHO_BASE + ParecerRepositoryImpl.NOME_ARQUIVO_PARECER
			+ ParecerRepositoryImpl.EXTENSAO_XML);

	private File radocFile = new File(ParecerRepositoryImpl.CAMINHO_BASE + ParecerRepositoryImpl.NOME_ARQUIVO_RADOC
			+ ParecerRepositoryImpl.EXTENSAO_XML);

	public void adicionaNota(String parecer, Nota nota) {
		if (this.parecerFile.exists()) {

			JAXBContext jaxbContext;
			try {
				jaxbContext = JAXBContext.newInstance(Pareceres.class);
				Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();
				Marshaller jaxbMarshaller = jaxbContext.createMarshaller();
				jaxbMarshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);

				Pareceres pareceresSalvos = (Pareceres) jaxbUnmarshaller.unmarshal(this.parecerFile);

				for (Parecer parecerSalvo : pareceresSalvos.getPareceres()) {
					if (parecerSalvo.getId().equals(parecer)) {

						parecerSalvo.getNotas().add(nota);
						break;
					}
				}

				this.parecerFile.getParentFile().mkdirs();
				jaxbMarshaller.marshal(pareceresSalvos, this.parecerFile);
			} catch (JAXBException e) {
				log.log(Level.SEVERE, e.getMessage(), e);
			}
		}

	}

	public void removeNota(String id, Avaliavel original) {
		if (this.parecerFile.exists()) {

			Nota notaRemover = null;

			JAXBContext jaxbContext;
			try {
				jaxbContext = JAXBContext.newInstance(Pareceres.class);
				Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();
				Marshaller jaxbMarshaller = jaxbContext.createMarshaller();
				jaxbMarshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);

				Pareceres pareceresSalvos = (Pareceres) jaxbUnmarshaller.unmarshal(this.parecerFile);

				for (Parecer parecerSalvo : pareceresSalvos.getPareceres()) {
					if (parecerSalvo.getId().equals(id)) {

						for (Nota nota : parecerSalvo.getNotas()) {
							if (((Pontuacao) nota.getItemOriginal()).getAtributo()
									.equals(((Pontuacao) original).getAtributo())) {
								notaRemover = nota;
								break;
							}
						}

						parecerSalvo.getNotas().remove(notaRemover);
						break;
					}
				}

				this.parecerFile.getParentFile().mkdirs();
				jaxbMarshaller.marshal(pareceresSalvos, this.parecerFile);
			} catch (JAXBException e) {
				log.log(Level.SEVERE, e.getMessage(), e);
			}
		}

	}

	@SuppressWarnings("resource")
	public void persisteParecer(Parecer parecer) {
		this.parecerFile.getParentFile().mkdirs();

		try {
			if (!this.parecerFile.exists()) {
				this.parecerFile.createNewFile();
			}
			Scanner scanner = new Scanner(this.parecerFile);
			while (scanner.hasNextLine()) {
				String line = scanner.nextLine();
				if (line.contains("<id>" + parecer.getId() + "</id>")) {
					return;
				}
			}
			Files.write(Paths.get(this.parecerFile.getPath()), XMLParserUtil.objectToXmlString(parecer).getBytes(),
					StandardOpenOption.APPEND);
		} catch (IOException e) {
			log.log(Level.SEVERE, e.getMessage(), e);
		} catch (IllegalArgumentException e) {
			log.log(Level.SEVERE, e.getMessage(), e);
		} catch (IllegalAccessException e) {
			log.log(Level.SEVERE, e.getMessage(), e);
		}

	}

	public void atualizaFundamentacao(String parecer, String fundamentacao) {
		if (this.parecerFile.exists()) {

			Parecer parecerRemover = null;
			Parecer parecerAtualizado = null;

			JAXBContext jaxbContext;
			try {
				jaxbContext = JAXBContext.newInstance(Pareceres.class);
				Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();
				Marshaller jaxbMarshaller = jaxbContext.createMarshaller();
				jaxbMarshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);

				Pareceres pareceresSalvos = (Pareceres) jaxbUnmarshaller.unmarshal(this.parecerFile);

				for (Parecer parecerSalvo : pareceresSalvos.getPareceres()) {
					if (parecerSalvo.getId().equals(parecer)) {
						parecerRemover = parecerSalvo;
						parecerAtualizado = new Parecer(parecerSalvo.getId(), parecerSalvo.getResolucao(),
								parecerSalvo.getRadocs(), parecerSalvo.getPontuacoes(), fundamentacao,
								parecerSalvo.getNotas());
						break;
					}
				}

				pareceresSalvos.getPareceres().remove(parecerRemover);
				pareceresSalvos.getPareceres().add(parecerAtualizado);

				this.parecerFile.getParentFile().mkdirs();
				jaxbMarshaller.marshal(pareceresSalvos, this.parecerFile);
			} catch (JAXBException e) {
				log.log(Level.SEVERE, e.getMessage(), e);
			}
		}

	}

	public Parecer byId(String id) {
		Parecer retorno = null;

		if (this.parecerFile.exists()) {

		}
		return retorno;
	}

	public void removeParecer(String id) {

		if (this.parecerFile.exists()) {

			Parecer parecerRemover = null;

			JAXBContext jaxbContext;
			try {
				jaxbContext = JAXBContext.newInstance(Pareceres.class);
				Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();
				Marshaller jaxbMarshaller = jaxbContext.createMarshaller();
				jaxbMarshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);

				Pareceres pareceres = (Pareceres) jaxbUnmarshaller.unmarshal(this.parecerFile);

				for (Parecer parecer : pareceres.getPareceres()) {
					if (parecer.getId().equals(id)) {
						parecerRemover = parecer;
					}
				}
				pareceres.getPareceres().remove(parecerRemover);

				jaxbMarshaller.marshal(pareceres, parecerFile);

			} catch (JAXBException e) {
				log.log(Level.SEVERE, e.getMessage(), e);
			}
		}
	}

	public Radoc radocById(String identificador) {
		Radoc retorno = null;

		if (this.radocFile.exists()) {
			JAXBContext jaxbContext;
			try {
				jaxbContext = JAXBContext.newInstance(Radocs.class);
				Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();

				Radocs radocs = (Radocs) jaxbUnmarshaller.unmarshal(this.radocFile);

				for (Radoc radoc : radocs.getRadocs()) {
					if (radoc.getId().equals(identificador)) {
						retorno = radoc;
						break;
					}
				}
			} catch (JAXBException e) {
				log.log(Level.SEVERE, e.getMessage(), e);
			}
		}

		return retorno;
	}

	@SuppressWarnings("resource")
	public String persisteRadoc(Radoc radoc) {
		this.radocFile.getParentFile().mkdirs();

		try {
			if (!this.radocFile.exists()) {
				this.radocFile.createNewFile();
			}
			Scanner scanner = new Scanner(this.radocFile);
			while (scanner.hasNextLine()) {
				String line = scanner.nextLine();
				if (line.contains("<id>" + radoc.getId() + "</id>")) {
					return null;
				}
			}
			Files.write(Paths.get(this.radocFile.getPath()), XMLParserUtil.objectToXmlString(radoc).getBytes(),
					StandardOpenOption.APPEND);
		} catch (IOException e) {
			log.log(Level.SEVERE, e.getMessage(), e);
		} catch (IllegalArgumentException e) {
			log.log(Level.SEVERE, e.getMessage(), e);
		} catch (IllegalAccessException e) {
			log.log(Level.SEVERE, e.getMessage(), e);
		}

		return radoc.getId();
	}

	public void removeRadoc(String identificador) {
		if (this.radocFile.exists()) {
			Radoc radocRemover = null;

			JAXBContext jaxbContext;
			try {
				jaxbContext = JAXBContext.newInstance(Radocs.class);
				Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();
				Marshaller jaxbMarshaller = jaxbContext.createMarshaller();
				jaxbMarshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);

				Radocs radocs = (Radocs) jaxbUnmarshaller.unmarshal(this.radocFile);

				for (Radoc radoc : radocs.getRadocs()) {
					if (radoc.getId().equals(identificador)) {
						radocRemover = radoc;
					}
				}
				radocs.getRadocs().remove(radocRemover);

				jaxbMarshaller.marshal(radocs, radocFile);

			} catch (JAXBException e) {
				log.log(Level.SEVERE, e.getMessage(), e);
			}
		}
	}
}
