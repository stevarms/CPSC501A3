import static org.junit.Assert.*;

import java.io.FileOutputStream;
import java.io.PrintStream;

import org.jdom.Document;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;


public class ReceiverTest {
	
	String testString;
	@Before
	public void setUp() throws Exception {
		testString = "this is a test";
	}

	@After
	public void tearDown() throws Exception {
	}
	
	@Test
	//Tests to see that after you serialize an object and then deserialize it, you end up with
	//essentially the same object.
	public void testSerialize() throws Exception {
		Document serializedDoc = Serializer.serialize(testString);
		String deserializedObject = (String) Deserializer.deserialize(serializedDoc);
		
		FileOutputStream fos1 = new FileOutputStream("testSerializeBefore.txt");
		PrintStream ps = new PrintStream(fos1);
		System.setOut(ps);
		
		Inspector inspector = new Inspector();
		inspector.inspect(testString, false);
		
		FileOutputStream fos2 = new FileOutputStream("testSerializeAfter.txt");
		ps = new PrintStream(fos2);
		System.setOut(ps);
		
		inspector.inspect(deserializedObject, false);
		fos1.close();
		fos2.close();
		
		assertEquals(testString, deserializedObject);
	}
}
