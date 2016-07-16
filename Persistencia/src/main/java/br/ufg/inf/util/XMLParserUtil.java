package br.ufg.inf.util;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.Collection;
import java.util.List;
import java.util.Map;

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

	private static boolean isPrimitiveInstanceObject(Object obj, Field field) throws IllegalAccessException {
		return field.get(obj) instanceof Number || field.get(obj) instanceof String
				|| field.get(obj) instanceof Boolean;
	}
}
