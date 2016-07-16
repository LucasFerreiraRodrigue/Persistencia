package br.ufg.inf.util;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.List;

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
}
