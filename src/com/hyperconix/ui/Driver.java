package com.hyperconix.ui;

import java.io.IOException;
import java.util.Scanner;

/**
 * Entry point for the room booking system
 * 
 * @author Luke S
 *
 */
public class Driver
{
	
	
	/**
	 * @param args
	 */
	public static void main(String[] args)
	{
		try(Scanner input = new Scanner(System.in)) 
		{
			ConsoleUI consoleUI = new ConsoleUI(input);
			consoleUI.start();
			
		} 
		catch(IOException e)
		{
			System.out.printf("Something went wrong during the system intialization stage, please review the below message\n\n%s\n", e.getMessage());
			e.printStackTrace();
		} 
		catch(ClassNotFoundException e)
		{
			
			System.out.printf("A critical error during the system intialization stage, please review the below message\n\n%s\n", e.getMessage());
			e.printStackTrace();
		}

	}
	
	
	

}
