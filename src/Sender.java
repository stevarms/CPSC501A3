import java.io.*;
import java.util.*;
import org.jdom2.*;
import org.jdom2.output.XMLOutputter;
import java.net.Socket;

public class Sender {
	static List<Object> objList;

	public static void main(String[] args) throws Exception {
		String server = "localhost";
		int port = Integer.parseInt("3333");
		objList = new ArrayList<Object>();
		boolean exit_flag = false;
		while (!exit_flag) {
			System.out.println("Create an object?");
			System.out.println("1. Yes");
			System.out.println("2. Serialize");
			System.out.println("3. Exit");
			try {
				BufferedReader bufferRead = new BufferedReader(
						new InputStreamReader(System.in));
				String input = bufferRead.readLine();
				if (input.equals("1")) {
					System.out.println("what kind of object?");
					System.out.println("1. Simple Object(int,boolean)");
					System.out.println("2. Simple Object With Reference");
					System.out.println("3. Array(int[])");
					System.out.println("4. Array with References");
					System.out.println("5. Java Collection Class");
					System.out.println("6. Back");

					bufferRead = new BufferedReader(new InputStreamReader(
							System.in));
					input = bufferRead.readLine();

					if (input.equals("1")) {
						System.out.println("Enter field values.  Eg: '1,true'");
						bufferRead = new BufferedReader(new InputStreamReader(
								System.in));
						input = bufferRead.readLine();
						String[] fields = input.split(",");
						if (fields.length != 2) {
							System.out
									.println("You must enter exactly 2 parameters");
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
					} else if (input.equals("2")) {
					} else if (input.equals("3")) {
						System.out.println("Enter field values.  Eg: '1,2,3'");
						bufferRead = new BufferedReader(new InputStreamReader(
								System.in));
						input = bufferRead.readLine();
						String[] fields = input.split(",");
						int[] ints = new int[fields.length];
						for (int i = 0; i < ints.length; i++) {
							try {
								ints[i] = Integer.parseInt(fields[i]);
							} catch (Exception e) {
								System.out
										.println("You can only enter numbers");
							}
						}
						Object obj = new simpleArray(ints);
						objList.add(obj);

					} else if (input.equals("4")) {
					} else if (input.equals("5")) {
					} else if (input.equals("6")) {
						// do nothing
					} else {
						System.out.println("invalid input");
					}
				} else if (input.equals("2")) {
					for (Object obj : objList) {
						System.out.println("Deserializing object...");
						XMLOutputter out = new XMLOutputter();
						Document doc = Serializer.serialize(obj);

						File aFile = new File("sentdata.xml");
						BufferedWriter writer = new BufferedWriter(
								new FileWriter(aFile));
						out.output(doc, writer);
						writer.close();

						System.out.println("Transferring file...");
						try {
							Socket s = new Socket(server, port);
							OutputStream output = s.getOutputStream();
							FileInputStream fileInputStream = new FileInputStream(
									aFile);
							byte[] buffer = new byte[1024 * 1024];
							int bytesRead = 0;
							while ((bytesRead = fileInputStream.read(buffer)) > 0) {
								output.write(buffer, 0, bytesRead);
							}
							fileInputStream.close();
							s.close();
						} catch (IOException e) {
							System.out.println("connection refused");
						}
						System.out.println("Transfer Complete");
					}
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
}
