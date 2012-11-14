import static org.junit.Assert.*;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.PrintStream;

import org.jdom.Document;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;


public class SenderTest {
	String testString;
	
	@Before
	public void setUp() throws Exception {
		testString = "this is a test";
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	//Test to see that the ObjectSerializer prints out the CMLDocument to a file in the proper format
	public void testCMLDocOutput() throws Exception {
		Document doc = Serializer.serialize(testString);

		Sender.createFile(doc);
		
		BufferedReader inStream = new BufferedReader(new FileReader("sentdata.xml"));
		StringBuffer output = new StringBuffer();
		while (inStream.ready())
			output.append((char)inStream.read());
		String result = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n<serialized><object class=\"java.lang.String\" id=\"0\"><field name=\"value\" declaringclass=\"java.lang.String\"><reference>1</reference></field><field name=\"offset\" declaringclass=\"java.lang.String\"><value>0</value></field><field name=\"count\" declaringclass=\"java.lang.String\"><value>14</value></field><field name=\"hash\" declaringclass=\"java.lang.String\"><value>0</value></field><field name=\"serialVersionUID\" declaringclass=\"java.lang.String\"><value>-6849794470754667710</value></field><field name=\"serialPersistentFields\" declaringclass=\"java.lang.String\"><reference>2</reference></field><field name=\"CASE_INSENSITIVE_ORDER\" declaringclass=\"java.lang.String\"><reference>3</reference></field></object><object class=\"[C\" id=\"1\" length=\"14\"><value>t</value><value>h</value><value>i</value><value>s</value><value /><value>i</value><value>s</value><value /><value>a</value><value /><value>t</value><value>e</value><value>s</value><value>t</value></object><object class=\"[Ljava.io.ObjectStreamField;\" id=\"2\" length=\"0\" /><object class=\"java.lang.String$CaseInsensitiveComparator\" id=\"3\"><field name=\"serialVersionUID\" declaringclass=\"java.lang.String$CaseInsensitiveComparator\"><value>8575799808933029326</value></field></object></serialized>";
		
		assertEquals(output.toString(), result);
	}
}
