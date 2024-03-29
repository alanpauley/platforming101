package dev.apauley.utilities;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

/*
 * Contains helper functions that assist us anywhere in game
 */

public class Utilities {

	//Parses input file and creates and returns a long string
	public static String loadFileAsString(String path) {
		
		//StringBuilder allows us to add characters to a string easily
		StringBuilder builder = new StringBuilder();
		
		try {
			BufferedReader br = new BufferedReader(new FileReader(path));
			
			//The current line we're working with
			String line;
			
			//While we haven't reached the end of the file
			while((line = br.readLine()) != null)

				//Append new line to the line
				builder.append(line + "\n");

			br.close();
		}catch(IOException e) {
			e.printStackTrace();
		}

		//Converts everything we just appended into a string and returns it
		return builder.toString();
	}
	
	//Takes in a string and will convert each number to the integer value
	public static int parseInt(String number) {
		try {
			return Integer.parseInt(number);

		//If you try to supply a value that isn't a number, it will print contents to stack trace
		}catch(NumberFormatException e) {
			e.printStackTrace();
			return 0;
		}
	}
	
}
