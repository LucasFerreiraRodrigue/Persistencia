package br.ufg.inf.impl;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.xml.DomDriver;

import br.ufg.inf.domain.Pareceres;
import br.ufg.inf.domain.Radocs;
import br.ufg.inf.es.saep.sandbox.dominio.Avaliavel;
import br.ufg.inf.es.saep.sandbox.dominio.ExisteParecerReferenciandoRadoc;
import br.ufg.inf.es.saep.sandbox.dominio.IdentificadorDesconhecido;
import br.ufg.inf.es.saep.sandbox.dominio.IdentificadorExistente;
import br.ufg.inf.es.saep.sandbox.dominio.Nota;
import br.ufg.inf.es.saep.sandbox.dominio.Parecer;
import br.ufg.inf.es.saep.sandbox.dominio.ParecerRepository;
import br.ufg.inf.es.saep.sandbox.dominio.Pontuacao;
import br.ufg.inf.es.saep.sandbox.dominio.Radoc;
import br.ufg.inf.es.saep.sandbox.dominio.Regra;
import br.ufg.inf.es.saep.sandbox.dominio.Relato;
import br.ufg.inf.es.saep.sandbox.dominio.Valor;

public class ParecerRepositoryImpl implements ParecerRepository {
	private static final String IDENTIFICADOR = "id";
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

			try {
				XStream stream = new XStream();
				stream.alias("Pareceres", Pareceres.class);
				stream.alias("Parecer", Parecer.class);
				stream.alias("Regra", Regra.class);

				Pareceres pareceres = (Pareceres) stream.fromXML(this.parecerFile);

				for (Parecer parecerSalvo : pareceres.getPareceres()) {
					if (parecerSalvo.getId().equals(parecer)) {

						parecerSalvo.getNotas().add(nota);
						break;
					}
				}

				OutputStream out = new FileOutputStream(this.parecerFile);
				stream.toXML(pareceres, out);

			} catch (FileNotFoundException e) {
				log.log(Level.SEVERE, e.getLocalizedMessage(), e);
			}
		}
	}

	public void removeNota(String id, Avaliavel original) {

		Nota notaRemover = null;

		if (this.parecerFile.exists()) {

			try {
				XStream stream = new XStream();
				stream.alias("Pareceres", Pareceres.class);
				stream.alias("Parecer", Parecer.class);
				stream.alias("Regra", Regra.class);

				Pareceres pareceres = (Pareceres) stream.fromXML(this.parecerFile);

				for (Parecer parecerSalvo : pareceres.getPareceres()) {
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

				OutputStream out = new FileOutputStream(this.parecerFile);
				stream.toXML(pareceres, out);

			} catch (FileNotFoundException e) {
				log.log(Level.SEVERE, e.getLocalizedMessage(), e);
			}
		}

	}

	public void persisteParecer(Parecer parecer) {
		this.parecerFile.getParentFile().mkdirs();

		XStream stream = new XStream(new DomDriver());
		stream.alias("Pareceres", Pareceres.class);
		stream.alias("Parecer", Parecer.class);

		Pareceres pareceres = new Pareceres();

		try {
			if (this.parecerFile.exists()) {
				pareceres = (Pareceres) stream.fromXML(this.parecerFile);
			} else {
				this.parecerFile.createNewFile();
				pareceres.setPareceres(new ArrayList<Parecer>());
			}

			for (Parecer parec : pareceres.getPareceres()) {
				if (parec.getId().equals(parecer.getId())) {
					throw new IdentificadorExistente(ParecerRepositoryImpl.IDENTIFICADOR);
				}
			}

			pareceres.getPareceres().add(parecer);

			final OutputStream out = new FileOutputStream(this.parecerFile);

			stream.toXML(pareceres, out);
		} catch (IOException e) {
			log.log(Level.SEVERE, e.getMessage(), e);
		}

	}

	public void atualizaFundamentacao(String parecer, String fundamentacao) {

		Parecer parecerRemover = null;
		Parecer parecerAtualizado = null;

		if (this.parecerFile.exists()) {

			try {
				XStream stream = new XStream();
				stream.alias("Pareceres", Pareceres.class);
				stream.alias("Parecer", Parecer.class);
				stream.alias("Regra", Regra.class);

				Pareceres pareceres = (Pareceres) stream.fromXML(this.parecerFile);

				for (Parecer parecerSalvo : pareceres.getPareceres()) {
					if (parecerSalvo.getId().equals(parecer)) {
						parecerRemover = parecerSalvo;
						parecerAtualizado = new Parecer(parecerSalvo.getId(), parecerSalvo.getResolucao(),
								parecerSalvo.getRadocs(), parecerSalvo.getPontuacoes(), fundamentacao,
								parecerSalvo.getNotas());
						break;
					}
				}

				if (parecerAtualizado == null) {
					throw new IdentificadorDesconhecido("Identificador do parecer não localizado.");
				}
				pareceres.getPareceres().remove(parecerRemover);
				pareceres.getPareceres().add(parecerAtualizado);

				OutputStream out = new FileOutputStream(this.parecerFile);
				stream.toXML(pareceres, out);
			} catch (FileNotFoundException e) {
				log.log(Level.SEVERE, e.getLocalizedMessage(), e);
			}

		}

	}

	public Parecer byId(String id) {
		Parecer retorno = null;

		if (this.parecerFile.exists()) {

			XStream stream = new XStream();
			stream.alias("Pareceres", Pareceres.class);
			stream.alias("Parecer", Parecer.class);
			stream.alias("Regra", Regra.class);

			Pareceres pareceres = (Pareceres) stream.fromXML(this.parecerFile);

			for (Parecer parecer : pareceres.getPareceres()) {
				if (parecer.getId().equals(id)) {
					retorno = parecer;
				}
			}
		}
		return retorno;
	}

	private Boolean existeParecerComRadoc(Radoc radoc) {
		Boolean retorno = false;

		if (this.parecerFile.exists()) {

			XStream stream = new XStream();
			stream.alias("Pareceres", Pareceres.class);
			stream.alias("Parecer", Parecer.class);
			stream.alias("Regra", Regra.class);

			Pareceres pareceres = (Pareceres) stream.fromXML(this.parecerFile);

			for (Parecer parecer : pareceres.getPareceres()) {
				if (parecer.getRadocs().contains(radoc.getId())) {
					retorno = true;
					break;
				}
			}
		}
		return retorno;
	}

	public void removeParecer(String id) {

		if (this.parecerFile.exists()) {

			Parecer parecerRemover = null;
			try {

				XStream stream = new XStream();
				stream.alias("Pareceres", Pareceres.class);
				stream.alias("Parecer", Parecer.class);
				stream.alias("Relato", Relato.class);
				stream.alias("Valor", Valor.class);

				Pareceres pareceres = (Pareceres) stream.fromXML(this.parecerFile);

				for (Parecer parecer : pareceres.getPareceres()) {
					if (parecer.getId().equals(id)) {
						parecerRemover = parecer;
						break;
					}
				}

				if (parecerRemover != null) {
					pareceres.getPareceres().remove(parecerRemover);

					OutputStream out = new FileOutputStream(this.parecerFile);

					stream.toXML(pareceres, out);
				}
			} catch (FileNotFoundException e) {
				log.log(Level.SEVERE, e.getLocalizedMessage(), e);
			}

		}
	}

	public Radoc radocById(String identificador) {
		Radoc retorno = null;

		if (this.radocFile.exists()) {

			XStream stream = new XStream();
			stream.alias("Radocs", Radocs.class);
			stream.alias("Radoc", Radoc.class);
			stream.alias("Relato", Relato.class);
			stream.alias("Valor", Valor.class);

			Radocs radocs = (Radocs) stream.fromXML(this.radocFile);

			for (Radoc radoc : radocs.getRadocs()) {
				if (radoc.getId().equals(identificador)) {
					retorno = radoc;
				}
			}
		}

		return retorno;
	}

	public String persisteRadoc(Radoc radoc) {
		this.radocFile.getParentFile().mkdirs();

		XStream stream = new XStream(new DomDriver());
		stream.alias("Radocs", Radocs.class);
		stream.alias("Radoc", Radoc.class);
		stream.alias("Relato", Relato.class);
		stream.alias("Valor", Valor.class);

		Radocs radocs = new Radocs();

		try {
			if (this.radocFile.exists()) {
				radocs = (Radocs) stream.fromXML(this.radocFile);
			} else {
				this.radocFile.createNewFile();
				radocs.setRadocs(new ArrayList<Radoc>());
			}

			for (Radoc rado : radocs.getRadocs()) {
				if (rado.getId().equals(radoc.getId())) {
					throw new IdentificadorExistente("Identificadorr duplicado, não foi possível persistir Radoc.");
				}
			}

			radocs.getRadocs().add(radoc);

			final OutputStream out = new FileOutputStream(this.radocFile);

			stream.toXML(radocs, out);
		} catch (IOException e) {
			log.log(Level.SEVERE, e.getMessage(), e);
		}

		return radoc.getId();
	}

	public void removeRadoc(String identificador) {
		if (this.radocFile.exists()) {

			Radoc radocRemover = null;
			try {

				XStream stream = new XStream();
				stream.alias("Radocs", Radocs.class);
				stream.alias("Radoc", Radoc.class);
				stream.alias("Relato", Relato.class);
				stream.alias("Valor", Valor.class);

				Radocs radocs = (Radocs) stream.fromXML(this.radocFile);

				for (Radoc radoc : radocs.getRadocs()) {
					if (radoc.getId().equals(identificador)) {
						radocRemover = radoc;
						break;
					}
				}

				if (radocRemover != null && !existeParecerComRadoc(radocRemover)) {
					radocs.getRadocs().remove(radocRemover);

					OutputStream out;
					out = new FileOutputStream(this.radocFile);

					stream.toXML(radocs, out);
				} else {
					throw new ExisteParecerReferenciandoRadoc(
							"Radoc não pode ser excluido pois existe um parecer o referenciando.");
				}
			} catch (FileNotFoundException e) {
				log.log(Level.SEVERE, e.getLocalizedMessage(), e);
			}
		}
	}
}
