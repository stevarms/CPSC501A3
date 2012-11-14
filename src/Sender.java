import java.awt.Point;
import java.io.*;
import java.util.*;
import org.jdom2.*;
import org.jdom2.output.XMLOutputter;
import java.net.Socket;

public class Sender implements createObjects {
	static List<Object> objList;

	public static void main(String[] args) throws Exception {
		String server = "localhost";
		int port = Integer.parseInt("3333");
		objList = new ArrayList<Object>();
		boolean exit_flag = false;
		while (!exit_flag) {
			System.out.println("Create an object?");
			System.out.println("1. Yes");
			System.out.println("2. No");
			System.out.println("3. Exit");
			try {
				BufferedReader bufferRead = new BufferedReader(
						new InputStreamReader(System.in));
				String input = bufferRead.readLine();
				if (input.equals("1")) {
					System.out.println("what kind of object?");
					System.out.println("1. Simple Object(int,boolean)");
					System.out.println("2. Simple Object With Reference(string)");
					System.out.println("3. Array(int[])");
					System.out.println("4. Array with References(Point[])");
					System.out.println("5. Java Collection Class");
					System.out.println("6. Back");

					bufferRead = new BufferedReader(new InputStreamReader(
							System.in));
					input = bufferRead.readLine();

					if (input.equals("1")) {
						createSimpleObject();
					} else if (input.equals("2")) {
						createRefObject();
					} else if (input.equals("3")) {
						createSimpleArray();
					} else if (input.equals("4")) {
						createRefArray();
					} else if (input.equals("5")) {
						createCollection();
					} else if (input.equals("6")) {
						// do nothing
					} else {
						System.out.println("invalid input");
					}
				} else if (input.equals("2")) {
					serialize(server, port);
				} else if (input.equals("3")) {
					System.out.println("Exiting...");
					exit_flag = true;
				} else {
					System.out.println("Invalid input");
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	private static void serialize(String server, int port) throws IOException, Exception {
		System.out.println("Serialize and transfer to receiver?");
		System.out.println("1. Yes");
		System.out.println("2. No");
		BufferedReader bufferRead = new BufferedReader(new InputStreamReader(
				System.in));
		String input = bufferRead.readLine();
		if (input.equals("1")) {
			for (Object obj : objList) {
				System.out.println("Deserializing object...");
				XMLOutputter out = new XMLOutputter();
				Document doc = Serializer.serialize(obj);

				File aFile = createFile(out, doc);

				transferFile(server, port, aFile);
			}
		} else if (input.equals("2")) {

		} else {
			System.out.println("Invalid input");
		}
	}

	private static File createFile(XMLOutputter out, Document doc)
			throws IOException {
		File aFile = new File("sentdata.xml");
		BufferedWriter writer = new BufferedWriter(
				new FileWriter(aFile));
		out.output(doc, writer);
		writer.close();
		return aFile;
	}

	private static void transferFile(String server, int port, File aFile) {
		System.out.println("Transferring file...");
		try {
			Socket s = new Socket(server, port);
			OutputStream output = s.getOutputStream();
			FileInputStream fileInputStream = new FileInputStream(
					aFile);
			byte[] buffer = new byte[1024 * 1024];
			int bytesRead = 0;
			while ((bytesRead = fileInputStream
					.read(buffer)) > 0) {
				output.write(buffer, 0, bytesRead);
			}
			fileInputStream.close();
			s.close();
			System.out.println("Transfer Complete");
		} catch (IOException e) {
			System.out.println("connection refused");
		}
	}	
	
	private static void createCollection() throws IOException {
		System.out.println("Enter a list of strings. Eg: 'this,is,a,list'");
		BufferedReader bufferRead = new BufferedReader(new InputStreamReader(
				System.in));
		String input = bufferRead.readLine();
		String[] fields = input.split(",");
		ArrayList<String> list = new ArrayList<String>();
		for (String s : fields){
			list.add(s);
		}
		Object obj = new collectionObject(list);
		objList.add(obj);
	}

	private static void createSimpleArray() throws IOException {
		System.out.println("Enter field values. Eg: '1,2,3'");
		BufferedReader bufferRead = new BufferedReader(new InputStreamReader(
				System.in));
		String input = bufferRead.readLine();
		String[] fields = input.split(",");
		int[] ints = new int[fields.length];
		for (int i = 0; i < ints.length; i++) {
			try {
				ints[i] = Integer.parseInt(fields[i]);
			} catch (Exception e) {
				System.out.println("You can only enter numbers");
			}
		}
		Object obj = new simpleArray(ints);
		objList.add(obj);
	}

	private static void createRefArray() throws IOException {
		System.out.println("Enter number of points");
		BufferedReader bufferRead = new BufferedReader(new InputStreamReader(
				System.in));
		String input = bufferRead.readLine();
		int number = Integer.parseInt(input);
		List<int[]> points = new ArrayList<int[]>();
		System.out
				.println("Enter x and y values for a point: Eg '1,2'");
		for (int i = 0; i < number; i++) {
			bufferRead = new BufferedReader(
					new InputStreamReader(System.in));
			input = bufferRead.readLine();
			String[] xy = input.split(",");
			if (xy.length != 2) {
				System.out.println("must enter 2 values");
				break;
			} else {
				int[] _xy = new int[2];
				_xy[0] = Integer.parseInt(xy[0]);
				_xy[1] = Integer.parseInt(xy[1]);
				points.add(_xy);
			}
		}
		Point[] _points = new Point[points.size()];
		for (int i = 0; i < points.size(); i++) {
			Point point = new Point(points.get(i)[0],
					points.get(i)[1]);
			_points[i] = point;
		}
		Object obj = new refArray(_points);
		objList.add(obj);
	}

	private static void createRefObject() throws IOException {
		System.out.println("Enter String");
		BufferedReader bufferRead = new BufferedReader(new InputStreamReader(
				System.in));
		String input = bufferRead.readLine();
		Object obj = new refObject(input);
		objList.add(obj);
	}

	private static void createSimpleObject() throws IOException {
		System.out.println("Enter field values.  Eg: '1,true'");
		BufferedReader bufferRead = new BufferedReader(new InputStreamReader(
				System.in));
		String input = bufferRead.readLine();
		String[] fields = input.split(",");
		if (fields.length != 2) {
			System.out.println("You must enter exactly 2 parameters");
		} else {
			boolean bool = false;
			if (fields[1].toLowerCase().equals("true")) {
				bool = true;
			} else if (fields[1].toLowerCase().equals("false")) {
				bool = false;
			} else {
				System.out
						.println("You must enter true or false for a boolean");
			}
			try {
				int i = Integer.parseInt(fields[0]);
				Object obj = new simpleObject(i, bool);
				objList.add(obj);
			} catch (Exception e) {
				System.out.println("You must enter a number");
			}
		}
	}
}
