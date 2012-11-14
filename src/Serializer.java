import java.lang.reflect.*;
import java.util.*;

import org.jdom2.Document;
import org.jdom2.Element;

//adapted from examples in the textbook
public class Serializer {

	public static Document serialize(Object obj) throws Exception {
		return serializeHelper(obj, new Document(new Element("serialized")),
				new IdentityHashMap());
	}

	private static Document serializeHelper(Object source, Document target,
			Map table) throws Exception {
		String id = Integer.toString(table.size());
		table.put(source, id);
		Class sourceclass = source.getClass();

		Element oElt = new Element("object");
		oElt.setAttribute("class", sourceclass.getName());
		oElt.setAttribute("id", id);
		target.getRootElement().addContent(oElt);

		if (!sourceclass.isArray()) {
			Field[] fields = sourceclass.getDeclaredFields();
			for (int i = 0; i < fields.length; i++) {
				if (!Modifier.isPublic(fields[i].getModifiers()))
					fields[i].setAccessible(true);

				Element fElt = new Element("field");
				fElt.setAttribute("name", fields[i].getName());
				Class declClass = fields[i].getDeclaringClass();
				fElt.setAttribute("declaringclass", declClass.getName());

				Class fieldtype = fields[i].getType();
				Object child = fields[i].get(source);

				if (Modifier.isTransient(fields[i].getModifiers()))
					child = null;

				fElt.addContent(serializeVariable(fieldtype, child, target,
						table));
				oElt.addContent(fElt);
			}
		} else {
			Class componentType = sourceclass.getComponentType();

			int length = Array.getLength(source);
			oElt.setAttribute("length", Integer.toString(length));
			for (int i = 0; i < length; i++)
				oElt.addContent(serializeVariable(componentType,
						Array.get(source, i), target, table));
		}
		return target;
	}

	private static Element serializeVariable(Class<?> fieldtype, Object child,
			Document target, Map table) throws Exception {
		if (child == null)
			return new Element("null");

		else if (!fieldtype.isPrimitive()) {
			Element reference = new Element("reference");
			if (table.containsKey(child))
				reference.setText(table.get(child).toString());
			else {
				reference.setText(Integer.toString(table.size()));
				serializeHelper(child, target, table);
			}
			return reference;
		} else {
			Element value = new Element("value");
			value.setText(child.toString());
			return value;
		}
	}
}
