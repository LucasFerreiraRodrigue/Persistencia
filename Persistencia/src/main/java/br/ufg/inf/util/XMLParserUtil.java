package br.ufg.inf.util;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Modifier;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import br.ufg.inf.es.saep.sandbox.dominio.Avaliavel;
import br.ufg.inf.es.saep.sandbox.dominio.Parecer;
import br.ufg.inf.es.saep.sandbox.dominio.Radoc;
import br.ufg.inf.es.saep.sandbox.dominio.Resolucao;
import br.ufg.inf.es.saep.sandbox.dominio.Tipo;
import br.ufg.inf.es.saep.sandbox.dominio.Valor;

/**
 * Classe responsável por armazenar os métodos que serão utilizados para
 * transformar um objeto em uma string xml.
 *
 */
public class XMLParserUtil {

	public static String objectToXmlString(Object obj) throws IllegalArgumentException, IllegalAccessException {

		final StringBuilder sb = new StringBuilder();

		sb.append("<").append(obj.getClass().getSimpleName()).append(">");
		if (obj instanceof String) {
			sb.append(obj);
		} else {
			sb.append("\n");
			for (Field field : obj.getClass().getDeclaredFields()) {
				field.setAccessible(true);
				if (!Modifier.isStatic(field.getModifiers())) {
					if (field.get(obj) instanceof List<?>) {
						final List<?> lista = (List<?>) field.get(obj);
						for (Object object : lista) {
							sb.append(objectToXmlString(object));
						}
					} else if (field.get(obj) instanceof Collection<?>) {
						final Collection<?> lista = (Collection<?>) field.get(obj);
						for (Object object : lista) {
							sb.append(objectToXmlString(object));
						}
					} else if (field.get(obj) instanceof Map<?, ?>) {
						final Map<?, ?> map = (Map<?, ?>) field.get(obj);
						sb.append("<").append(field.getName()).append(">\n");
						for (Object objet : map.keySet()) {
							sb.append(objectToXmlString(objet));
							sb.append(objectToXmlString(map.get(objet)));
						}
						sb.append("</").append(field.getName()).append(">").append("\n");
					} else if (field.get(obj) instanceof Avaliavel) {
						sb.append("<").append(field.getName()).append(">\n");
						sb.append(objectToXmlString(field.get(obj)));
						sb.append("</").append(field.getName()).append(">").append("\n");
					} else if (field.get(obj) instanceof Valor) {
						sb.append("<").append(field.getName()).append(">\n");
						sb.append(objectToXmlString(field.get(obj)));
						sb.append("</").append(field.getName()).append(">").append("\n");
					} else {
						sb.append("<").append(field.getName()).append(">");
						sb.append(field.get(obj));
						sb.append("</").append(field.getName()).append(">").append("\n");
					}
				}
			}
		}
		sb.append("</").append(obj.getClass().getSimpleName()).append(">").append("\n");

		return sb.toString();

	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	public static void xmlStringToObject(Class obj, String xmlString) {

		if (obj.isAssignableFrom(Resolucao.class)) {
			instanciateResolucao(obj);
		} else if (obj.isAssignableFrom(Tipo.class)) {
		} else if (obj.isAssignableFrom(Parecer.class)) {
		} else if (obj.isAssignableFrom(Radoc.class)) {
		}

	}

	private static void instanciateResolucao(Object obj) {
		Resolucao reso = (Resolucao) obj;

		for (Constructor<?> constructor : obj.getClass().getConstructors()) {
			try {
				constructor.newInstance(reso.getId(), reso.getNome(), reso.getNome(), reso.getDataAprovacao(),
						reso.getRegras());
			} catch (InstantiationException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IllegalArgumentException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (InvocationTargetException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

}
