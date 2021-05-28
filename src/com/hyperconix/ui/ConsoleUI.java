package com.hyperconix.ui;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Scanner;

import com.hyperconix.data.BookingManager;
import com.hyperconix.data.BookingRequirements;
import com.hyperconix.utilities.ConsoleInputUtility;

/**
 * The console user interface for the Room Booking System
 * 
 * 
 * @author Luke S
 *
 */
public class ConsoleUI
{
	/**
	 * Responsible for storing the suffix which will be appended at the end of the storage file for the bookings
	 */
	private static final String FILE_SUFFIX = "_bookings.dat";
	
	/**
	 * Responsible for storing the regular expression for validating phone numbers (UK Format)
	 */
	private static final String PHONE_NUM_REGEX = "^(\\+44\\s?7\\d{3}|\\(?07\\d{3}\\)?)\\s?\\d{3}\\s?\\d{3}$";
	
	/**
	 * Reposible for storing the regular expression for validating an email address
	 */
	private static final String EMAIL_REGEX = "^\\w+@[a-zA-Z_]+?\\.[a-zA-Z]{2,3}$";
	
	/**
	 * Responsible for storing the opening time of the college - 09:00am
	 */
	private static final LocalTime OPENING_TIME = LocalTime.of(9, 0);
	
	/**
	 * Responsible for storing the closing time of the college - 18:00pm
	 */
	private static final LocalTime CLOSING_TIME = LocalTime.of(18, 0);
	
	/** Stores the booking manager for the system, controls the functional operations that are required for the system to run */
	private BookingManager bookingManager;
	
	/**
	 * Stores the ConsoleInputUtility for the system, used for reading and validating user input
	 */
	private ConsoleInputUtility inputUtility;
	
	
	ConsoleUI(Scanner input) {
		inputUtility = new ConsoleInputUtility(input);
	}
	
	/**
	 * Starts user interface, this will only proceed if the system initlia
	 * @throws IOException 
	 * @throws ClassNotFoundException 
	 * @throws FileNotFoundException 
	 */
	void start() throws FileNotFoundException, ClassNotFoundException, IOException {
		if(initializeSystem()) {
			processInput();
		}
		else {
			System.out.println("Something went wrong with system initialization, try again");
		}
	}
	
	
	/**
	 * Handles initial setup for the system and allows the booking management process to start
	 * @throws IOException 
	 * @throws ClassNotFoundException 
	 * @throws FileNotFoundException 
	 */
	private boolean initializeSystem() throws FileNotFoundException, ClassNotFoundException, IOException {
		String collegeName = "Stirling";
		String fileName = (collegeName + FILE_SUFFIX).toLowerCase();
		
		File storageLocation = new File(fileName);
		
		if(!storageLocation.exists()) 
		{
			String userMsg = String.format("File for %s college not found\nDo you want to create one? (y/n): ", collegeName);
		
			
			boolean ok = inputUtility.readBooleanConfirmation(userMsg, "y", "n", true);
			
			if(!ok) 
			{
				System.out.println("Cannot run without a file to store the bookings");
				return false;
			}
		}
		
		bookingManager = new BookingManager(collegeName, storageLocation);
		return true;
		
	}
	
	/**
	 * Receives the input from the user and processes the input given
	 * @throws IOException 
	 * @throws FileNotFoundException 
	 */
	private void processInput() {
	boolean alive = true;
		
		do 
		{
			printUserOptions();
			
			int option = inputUtility.readValidInteger("Please enter your choice: ", 1, 6);
			
			try {
				switch(option) 
				{
					case 1:
						addClient();
						break;
					case 2:
						createBooking();
						break;
					case 3:
						findBooking();
						break;
					case 4: 
						cancelBooking();
						break;
					case 5:
						generateReport();
						break;
					case 6:
						alive = !inputUtility.readBooleanConfirmation("Are you sure you want to exit? (y/n): ", 
								"y", "n", true);
						break;
				}
			}
			catch(IOException ioex) {
				System.out.println("Error: An IO exception occured when attempting to save data - " + ioex.getMessage());
				ioex.printStackTrace();
			}
		}
		while(alive);
	}
	
	
	
	/**
	 * Reads the required input for a client and then adds them to the system using the booking manager
	 * @throws FileNotFoundException
	 * @throws IOException
	 */
	private void addClient() throws FileNotFoundException, IOException {
		
		String clientName = inputUtility.readUnemptyString("Please enter the client's name: ", "[a-zA-Z][a-zA-Z ]+", "The input must be alphabetical only" );
		
		String phoneNum = inputUtility.readUnemptyString("Please enter the client's phone num: ", PHONE_NUM_REGEX, 
				"Phone num must be in the UK format, I.E 07345 555432 | (07345) 555432 | +44 7345 555 432");
		
		boolean hasEmail = inputUtility.readBooleanConfirmation("Do you have an email address? (y/n): ", "y", "n", true);
		
		int clientID;
		
		if(hasEmail) {
			String email = inputUtility.readUnemptyString("Please enter your email address: ", EMAIL_REGEX, "Please make sure you enter a valid email address I.E joebloggs@gmail.com");
			
			clientID = bookingManager.addClient(clientName, phoneNum, email);
		}
		else {
			clientID = bookingManager.addClient(clientName, phoneNum);
		}
		
		
		if(clientID == -1) {
			System.out.println("The client was not added correctly");
			return;
		}
		
		System.out.printf("Client Added Successfully! - Client ID: %d", clientID);
	}
	
	
	/**
	 * Responsible for creating a new booking for a client in the room booking system
	 * @throws IOException 
	 * @throws FileNotFoundException 
	 */
	private void createBooking() throws FileNotFoundException, IOException {
	
		
		int clientID = inputUtility.readValidInteger("Please enter the clients ID: ");
		
		BookingRequirements requestedRequirements = getValidBookingRequirements();
		
		int refNum = bookingManager.createBooking(clientID, requestedRequirements);
		
		if(refNum == -1) {
			System.out.println("There was an issue finding the client using the ID you provided");
			return;
		}
		else if (refNum == -2) {
			System.out.println("A room could not be found using the clients requirements");
			return;
		}
		
		
	    System.out.printf("Booking Created Successfully! - Ref Num %d", refNum);
	    
	    String summary = bookingManager.getBookingSummary(refNum);
	    
	    System.out.println("\n" + summary);
	    
	    
		
	}
	
	/**
	 * Responsible for retrieving the requirements for the booking requested by the client 
	 * 
	 * @return BookingRequirements - Validated requirements for this booking
	 */
	private BookingRequirements getValidBookingRequirements() {
		
		int computerCapacity = inputUtility.readValidInteger("Please enter the computer capacity (0-20): ", 0, 20);
		
		
		//TODO Implement further filtering first
		
		//int breakoutCapacity = inputUtility.readValidInteger("Please enter the breakout capacity (0-20): ", 0, 20);
		
		//boolean smartboard = inputUtility.readBooleanConfirmation("Do you require a smartboard? (y/n): ", "y", "n", true);
		
		//boolean printer = inputUtility.readBooleanConfirmation("Do you require a printer? (y/n): ", "y", "n", true);
		
		
		int duration = inputUtility.readValidInteger("Please enter your duration (1-6): ", 1, 6);
		
		LocalDate bookingDate = inputUtility.readLocalDate("Please enter booking date (yyyy-mm-dd): ");
		
		LocalTime bookingTime;
		boolean isWithinOpenHours;
		
		do {
			
			bookingTime = inputUtility.readLocalTime("Please enter the booking time (hh:mm:ss): ");
			
			isWithinOpenHours = bookingTime.equals(OPENING_TIME) || bookingTime.isAfter(OPENING_TIME) && bookingTime.isBefore(CLOSING_TIME) 
								&& bookingTime.plusHours(duration).isBefore(CLOSING_TIME);
			
			if(!isWithinOpenHours) {
				System.out.printf("You entered a time that was outside of the opening hours %s - %s\n", OPENING_TIME, CLOSING_TIME);
				
			}
			
		} while(!isWithinOpenHours);
		
		
		return new BookingRequirements(computerCapacity, duration, bookingDate, bookingTime);
	}
	
	/**
	 * Responsible for finding a booking in the system and giving the user a summary of that booking
	 */
	private void findBooking() {
		int refNum = inputUtility.readValidInteger("Please enter the booking reference number: ");
		
		String result = bookingManager.getBookingSummary(refNum);
		
		System.out.println(result);
	}
	
	/**
	 * Responsible for cancelling an existing booking for a client in the room booking system
	 * @throws IOException 
	 * @throws FileNotFoundException 
	 */
	private void cancelBooking() throws FileNotFoundException, IOException {
		int refNum = inputUtility.readValidInteger("Please enter the reference number for the booking you wish to cancel: ");
		
		boolean cancelled = bookingManager.cancelBooking(refNum);
		
		String message = String.format(cancelled ? "Booking cancelled - Ref Num: %d" : "Cancellation Unsuccessful - Ref Num: %d", refNum);
		
		System.out.println(message);
	}
	
	/**
	 * Responsible for generating a report of historical information client bookings in the room booking system
	 */
	private void generateReport() {
		boolean alive = true;
		
		do {
			
			System.out.println("\nGenerate Report\n");
			
			System.out.println(
					  "1. By Client Name\n"
					+ "2. Date Range\n"
					+ "3. Exit\n"
					);
			
			String result;
			
			int userOption = inputUtility.readValidInteger("Please enter your choice: ", 1, 3);
			
			switch(userOption) {
				case 1:
					String clientName = inputUtility.readUnemptyString("Please enter the clients name: ");
					
					result = bookingManager.generateReport(clientName);
					
					System.out.println(result);
					break;
				case 2:
					LocalDate startDate = inputUtility.readLocalDate("Please enter the start date: ");
					
					LocalDate endDate = inputUtility.readLocalDate("Please enter the end date: ");
					
					result = bookingManager.generateReport(startDate, endDate);
					
					System.out.println(result);
					break;
				case 3:
					alive = !inputUtility.readBooleanConfirmation("Are you sure you want to exit? (y/n): ", "y", "n", true);
				
			}
			
			
		}while(alive);
	}
	
	
	
	/**
	 * Responsible for printing the choices to the user
	 */
	private void printUserOptions() {
		System.out.println("\nWelcome to the Room Booking System!\n");
		System.out.println("Start by adding a client to the system, and then create a booking using that client's ID number");
	
		System.out.printf("The opening hours for the college are %sam to %spm, please make sure the clients requirements are within this time\n\n",
				OPENING_TIME, CLOSING_TIME);
		
		System.out.println(
				  "1. Add Client\n"
				+ "2. Create booking\n"
				+ "3. Find Booking\n"
				+ "4. Cancel Booking\n"
				+ "5. Generate Report\n"
				+ "6. Exit");
	}
	
	
}
