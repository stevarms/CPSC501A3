import org.jdom2.Document;
import org.jdom2.Element;
import java.lang.reflect.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

//adapted from examples in the textbook
public class Deserializer {
	
	public Object deserialize(Document document) throws Exception {
		List objList = document.getRootElement().getChildren();
		Map table = new HashMap();
		createInstances(table, objList);
		assignFieldValues(table, objList);
		return table.get("0");
	}

	private void createInstances(Map table, List objList) throws Exception {
		for (int i=0; i<objList.size(); i++) {
			Element oElt = (Element) objList.get(i);
			Class cls = Class.forName(oElt.getAttributeValue("class"));
			Object instance = null;
			if (!cls.isArray()) {
				Constructor c = cls.getDeclaredConstructor(null);
				if (!Modifier.isPublic(c.getModifiers()))
					c.setAccessible(true);
				instance = c.newInstance(null);
			} else
				instance = Array.newInstance(cls.getComponentType(),
											Integer.parseInt(oElt.getAttributeValue("length")));
			table.put(oElt.getAttributeValue("id"), instance);
		}
	}
	
	private void assignFieldValues(Map table, List objList) throws Exception {
		for (int i=0; i < objList.size(); i++) {
			Element oElt = (Element) objList.get(i);
			Object instance = table.get(oElt.getAttributeValue("id"));
			List fElts = oElt.getChildren();
			if (!instance.getClass().isArray()) {
				for (int j=0; j<fElts.size(); j++) {
					Element fElt = (Element) fElts.get(j);
					String className = fElt.getAttributeValue("declaringclass");
					Class fieldDC = Class.forName(className);
					String fieldName = fElt.getAttributeValue("name");
					Field f = fieldDC.getDeclaredField(fieldName);
					if (!Modifier.isPublic(f.getModifiers()))
						f.setAccessible(true);
					
					Element vElt = (Element) fElt.getChildren().get(0);
					f.set(instance, deserializeValue(vElt, f.getType(), table));
				}
			} else {
				Class comptype = instance.getClass().getComponentType();
				for (int j=0; j<fElts.size(); j++)
					Array.set(instance, j, deserializeValue((Element)fElts.get(j), comptype, table));
			}
		}
	}
	
	private Object deserializeValue(Element vElt, Class fieldType, Map table) {
		String valtype = vElt.getName();
		if (valtype.equals("null"))
			return null;
		else if (valtype.equals("reference"))
			return table.get(vElt.getText());
		else {
			if (fieldType.equals(boolean.class)) {
				if (vElt.getText().equals("true"))
					return Boolean.TRUE;
				else
					return Boolean.FALSE;
			}
			else if (fieldType.equals(byte.class))
				return Byte.valueOf(vElt.getText());
			else if (fieldType.equals(short.class))
				return Short.valueOf(vElt.getText());
			else if (fieldType.equals(int.class))
				return Integer.valueOf(vElt.getText());
			else if (fieldType.equals(long.class))
				return Long.valueOf(vElt.getText());
			else if (fieldType.equals(float.class))
				return Float.valueOf(vElt.getText());
			else if (fieldType.equals(double.class))
				return Double.valueOf(vElt.getText());
			else if (fieldType.equals(char.class))
				return new Character(vElt.getText().charAt(0));
			else
				return vElt.getText();
		}
	}
}
