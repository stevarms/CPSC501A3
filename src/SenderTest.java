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
		String result = "serialized {\n\tobject (class=\"java.lang.String\" id=\"0\"){\n\t\tfield (name=\"value\" declaringclass=\"java.lang.String\"){\n\t\t\treference {1}\n\t\t}\n\t\tfield (name=\"offset\" declaringclass=\"java.lang.String\"){\n\t\t\tvalue {0}\n\t\t}\n\t\tfield (name=\"count\" declaringclass=\"java.lang.String\"){\n\t\t\tvalue {14}\n\t\t}\n\t\tfield (name=\"hash\" declaringclass=\"java.lang.String\"){\n\t\t\tvalue {0}\n\t\t}\n\t}\n\tobject (class=\"[C\" id=\"1\" length=\"14\"){\n\t\tvalue {t}\n\t\tvalue {h}\n\t\tvalue {i}\n\t\tvalue {s}\n\t\tvalue { }\n\t\tvalue {i}\n\t\tvalue {s}\n\t\tvalue { }\n\t\tvalue {a}\n\t\tvalue { }\n\t\tvalue {t}\n\t\tvalue {e}\n\t\tvalue {s}\n\t\tvalue {t}\n\t}\n}\n";
		
		assertEquals(output.toString(), result);
	}
}
