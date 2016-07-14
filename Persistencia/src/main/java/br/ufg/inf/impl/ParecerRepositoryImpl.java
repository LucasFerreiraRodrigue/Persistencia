package br.ufg.inf.impl;

import java.io.File;
import java.util.ArrayList;

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

public class ParecerRepositoryImpl implements ParecerRepository {
	private static String CAMINHO_BASE = "data";
	private static String NOME_ARQUIVO_PARECER = "/Pareceres/Parecer";
	private static String NOME_ARQUIVO_RADOC = "/Radocs/Radocs";
	private static String EXTENSAO_XML = ".xml";

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
				e.printStackTrace();
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
				e.printStackTrace();
			}
		}

	}

	public void persisteParecer(Parecer parecer) {
		JAXBContext jaxbContext;
		try {
			jaxbContext = JAXBContext.newInstance(Pareceres.class);
			Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();
			Marshaller jaxbMarshaller = jaxbContext.createMarshaller();
			jaxbMarshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);

			Pareceres pareceresSalvos;

			if (this.parecerFile.exists()) {
				pareceresSalvos = (Pareceres) jaxbUnmarshaller.unmarshal(this.parecerFile);
				if (pareceresSalvos.getPareceres() != null) {
					for (Parecer parecerSalvo : pareceresSalvos.getPareceres()) {
						if (parecerSalvo.getId().equals(parecer.getId())) {
							return;
						}
					}
				} else {
					pareceresSalvos.setPareceres(new ArrayList<Parecer>());
				}
			} else {
				pareceresSalvos = new Pareceres();
				pareceresSalvos.setPareceres(new ArrayList<Parecer>());
			}

			pareceresSalvos.getPareceres().add(parecer);

			this.parecerFile.getParentFile().mkdirs();
			jaxbMarshaller.marshal(pareceresSalvos, this.parecerFile);
		} catch (JAXBException e) {
			e.printStackTrace();
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
				e.printStackTrace();
			}
		}

	}

	public Parecer byId(String id) {
		Parecer retorno = null;

		if (this.parecerFile.exists()) {

			JAXBContext jaxbContext;
			try {
				jaxbContext = JAXBContext.newInstance(Pareceres.class);
				Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();

				Pareceres pareceres = (Pareceres) jaxbUnmarshaller.unmarshal(this.parecerFile);

				for (Parecer parecer : pareceres.getPareceres()) {
					if (parecer.getId().equals(id)) {
						retorno = parecer;
						break;
					}
				}
			} catch (JAXBException e) {
				e.printStackTrace();
			}
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
				e.printStackTrace();
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
				e.printStackTrace();
			}
		}

		return retorno;
	}

	public String persisteRadoc(Radoc radoc) {
		JAXBContext jaxbContext;
		try {
			jaxbContext = JAXBContext.newInstance(Radocs.class);

			Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();
			Radocs radocsSalvos;
			if (this.radocFile.exists()) {
				radocsSalvos = (Radocs) jaxbUnmarshaller.unmarshal(this.radocFile);
				if (radocsSalvos.getRadocs() != null) {
					for (Radoc radocSalvo : radocsSalvos.getRadocs()) {
						if (radocSalvo.getId().equals(radoc.getId())) {
							return null;
						}
					}
				} else {
					radocsSalvos.setRadocs(new ArrayList<Radoc>());
				}
			} else {
				radocsSalvos = new Radocs();
				radocsSalvos.setRadocs(new ArrayList<Radoc>());
			}

			radocsSalvos.getRadocs().add(radoc);

			Marshaller jaxbMarshaller = jaxbContext.createMarshaller();

			jaxbMarshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);

			this.radocFile.getParentFile().mkdirs();
			jaxbMarshaller.marshal(radocsSalvos, this.radocFile);
		} catch (JAXBException e) {
			e.printStackTrace();
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
				e.printStackTrace();
			}
		}
	}
}
