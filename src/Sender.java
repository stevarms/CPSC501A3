import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import org.jdom2.Document;

public class Sender {
	static List<Object> objList;

	public static void main(String[] args) {
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
					System.out.println("3. Array");
					System.out.println("4. Array with References");
					System.out.println("5. Java Collection Class");
					System.out.println("6. Back");

					bufferRead = new BufferedReader(new InputStreamReader(
							System.in));
					input = bufferRead.readLine();

					if (input.equals("1")) {
						System.out.println("enter field values.  Eg: '1,true'");
						bufferRead = new BufferedReader(new InputStreamReader(
								System.in));
						input = bufferRead.readLine();
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
								System.out.println("you must enter true or false for a boolean");
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
					} else if (input.equals("4")) {
					} else if (input.equals("5")) {
					} else if (input.equals("6")) {
						// do nothing
					} else {
						System.out.println("invalid input");
					}
				} else if (input.equals("2")) {
					for (Object obj : objList) {
						System.out.println(obj.toString());
						// Document doc = Serializer.serialize(obj);
					}
					System.out.println("Serialized!");
				} else if (input.equals("3")) {
					System.out.println("Exiting...");
					exit_flag = true;
				} else {
					System.out.println("invalid input");
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
}
