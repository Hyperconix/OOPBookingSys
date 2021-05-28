package com.hyperconix.data;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * Manages the core functionality of booking a room in the room booking system. This manages the clients that make the bookings
 * as well as the bookings themselves. It is important to note that a booking cannot exist without a client I.E a client that
 * has been successfully added via the addClient method and can be referenced by their ID number. There are a number of methods
 * <br><br>
 * 
 * Rooms are also managed within the booking manager as they are required to make a successful booking.
 * 
 * @author Luke S
 */
public class BookingManager
{
	/**
	 * Responsible for storing the bookings in system, mapped from the booking reference number to the booking itself
	 */
	private Map<Integer, Booking> bookings;
	
	/**
	 * Responsible for storing the rooms in the system, created as a list because rooms are ordered to find best match
	 */
    private List<Room> rooms;
	
    /**
     * Responsible for storing the clients in the system
     */
	private List<Client> clients;
	
	/**
	 * Responsible for holding the storage location of the data in the system
	 */
	private File storageLocation;
	
	/**
	 * Responsible for storing the count of clients
	 */
	private int clientIDCounter;
	
	/**
	 * Responsible for storing the count of bookings
	 */
	private int refNumCounter;
	
	/**
	 * Responsible for storing the name of the college
	 */
	private String collegeName;
	
	/**
	 * Creates the state of a BookingManager in the system. Bookings are persistently kept therefore a storage location
	 * is required for instantiation. 
	 * 
	 * @param storageLocation
	 * @throws IOException 
	 * @throws FileNotFoundException 
	 * @throws ClassNotFoundException 
	 * @throws NullPointerException If the storage location provided is null
	 */
	public BookingManager(String collegeName, File storageLocation) throws FileNotFoundException, IOException, ClassNotFoundException 
	{
		Objects.requireNonNull(storageLocation, "The storage location cannot be null");
		Objects.requireNonNull(collegeName, "The college name cannot be null");
		collegeName = collegeName.trim();
		
		if(collegeName.isEmpty()) 
		{
			throw new IllegalArgumentException("The college name cannot be empty");
		}
		
		this.storageLocation = storageLocation;
		this.collegeName = collegeName;
		
		bookings = new HashMap<>();
		clients = new ArrayList<>();
		rooms = new ArrayList<>();
		
		if(storageLocation.exists())
		{
			loadBookings();
		}
		else 
		{
			saveBookings();
		}
		
		populateRooms();
	}
	
	
	/**
	 * This method is responsible for creating a booking using the provided client requirements
	 * 
	 * @param clientID The ID number of the client the booking is being made for
	 * @param bookingDuration The duration that 
	 * @param bookingDate
	 * @param bookingTime
	 * @return refNumCounter the reference number of the booking, {@code -1} if the client could not be found or {@code -2} if a room
	 * @throws FileNotFoundException
	 * @throws IOException
	 */
	public int createBooking(int clientID, BookingRequirements requestedRequirements) throws FileNotFoundException, IOException 
	{
		return insertBooking(clientID, requestedRequirements);
	}
	
	
	private int insertBooking(int clientID, BookingRequirements requestedRequirements) throws FileNotFoundException, IOException {
		Client assignedClient = findClient(clientID);
		
	
		if(assignedClient == null)
			return -1;
		
		Room bookedRoom;
		
		List<Room> avaRooms = findBestMatch(requestedRequirements);
		
		if(avaRooms.size() >= 1) {
			bookedRoom = avaRooms.get(0);
		}
		else {
			return -2;
		}
		
		Booking b = new Booking(refNumCounter + 1, bookedRoom, assignedClient, requestedRequirements);
		
		
		bookings.put(b.getRefNum(), b);
		refNumCounter++;
		
		saveBookings();
		
		return refNumCounter; 
	}
	
	
	/**
	 * Cancels the booking using the provided reference number. If the booking could not be cancelled then this method will
	 * always return false.
	 * 
	 * @param refNum
	 * @return {@code true} if the booking was successfully cancelled, {@code false} otherwise.
	 * @throws FileNotFoundException
	 * @throws IOException
	 */
	public boolean cancelBooking(int refNum) throws FileNotFoundException, IOException 
	{
		return removeBooking(refNum);
	}
	
	private boolean removeBooking(int refNum) throws FileNotFoundException, IOException {
		Booking bookingToCancel = bookings.remove(refNum);
		
		saveBookings();
		
		return bookingToCancel != null;
		
	}
	
	/**
	 * This method searches through the bookings contained in the system and compares the reference number provided
	 * to the reference numbers of those bookings. If it finds a match it will return true, otherwise it will return false.
	 * @param refNum - The reference number for the booking
	 * @return {@code true} if a booking was found using the reference number {@code false} otherwise
	 */
	private boolean findBooking(int refNum) 
	{
		boolean found = false;
		
		for(Booking b : bookings.values()) 
		{
			if(b.getRefNum() == refNum) 
			{
				found = true;
				break;
			}
		}
		
		
		return found;
	}
	

	
	/**
	 * Add a client to the system using their name and phone num. The client's name must be a valid name and cannot contain
	 * any numerical values. 
	 * 
	 * @param clientName The name of the client
	 * @param phoneNum The phone number used by the client
	 * @return clientID or -1 if the client could not be added
	 * @throws FileNotFoundException
	 * @throws IOException
	 */
	public int addClient(String clientName, String phoneNum) throws FileNotFoundException, IOException 
	{
		return insertClient(clientName, phoneNum);
	}
	
	/**
	 * Add a client to the system using their name, phone num and email address. The client's name must be a valid name and cannot contain
	 * any numerical values. 
	 * 
	 * @param clientName The name of the client
	 * @param phoneNum The phone number used by the client
	 * @param email  The clients email address
	 * @return clientID or -1 if the client could not be added
	 * @throws FileNotFoundException
	 * @throws IOException
	 */
	public int addClient(String clientName, String phoneNum, String email) throws FileNotFoundException, IOException {
		return insertClient(clientName, phoneNum, email);
	}
	
	private int insertClient(String clientName, String phoneNum) throws FileNotFoundException, IOException 
	{
		Client c = new Client(clientIDCounter + 1, clientName, phoneNum);
		boolean addSuccessful = clients.add(c);
		
		if(!addSuccessful) 
		{
			return -1;
		}
		
		clientIDCounter++;
		
		saveBookings();
		
		return clientIDCounter;
	}
	
	private int insertClient(String clientName, String phoneNum, String email) throws FileNotFoundException, IOException {
		Client c = new Client(clientIDCounter + 1, clientName, phoneNum, email);
		boolean addSuccessful = clients.add(c);
		
		if(!addSuccessful) 
		{
			return -1;
		}
		
		clientIDCounter++;
		
		saveBookings();
		
		return clientIDCounter;
	}
	
	
	
	/**
	 * This method will return a string which contains a report of the bookings that were found, it will perform
	 * a date range between the start date and end date provided. IMPORTANT: This is a non inclusive search,
	 * meaning that it will not include bookings on the start or end date, only the bookings in that range.
	 * 
	 * @param date The date to generate the reports on
	 * @return report A string that contains the content of the report or an empty string if no results were found
	 */
	public String generateReport(LocalDate startDate, LocalDate endDate) 
	{
		return generateReportByDateRange(startDate, endDate);
	}
	
	/**
	 * This method will return a string which contains a report of the bookings that were found, it will perform
	 * a search based on the client name provided.
	 * 
	 * @param clientName The name of the client to perform the search on
	 * @return report A string that contains the content of the report or an empty string if no results were found
	 */
	public String generateReport(String clientName) 
	{
		return generateReportByClientName(clientName);
	}
	
	
	private String generateReportByDateRange(LocalDate startDate, LocalDate endDate) {
		StringBuilder report = new StringBuilder("");
		int results = 0;
		
		for(Booking currentBooking : bookings.values()) 
		{
			Client currentClient = currentBooking.getAssignedClient();
			BookingRequirements bookingRequirements = currentBooking.getBookingRequirements();
			Room bookedRoom = currentBooking.getBookedRoom();
			
			
			if(bookingRequirements.getBookingDate().isAfter(startDate) && bookingRequirements.getBookingDate().isBefore(endDate)) 
			{
				results++;
				
				
				report.append("\n\nResult No: ")
					  .append(results)
					  .append("\nClient Name: ")
					  .append(currentClient.getClientName())
					  .append("\nPhone Number: ")
					  .append(currentClient.getPhoneNum())
					  .append("\nRoom Number: ")
					  .append("\nEmail Address: ")
					  .append(currentClient.getClientEmail())
					  .append(bookedRoom.getRoomNumber())
					  .append("\nBooking Date: ")
					  .append(bookingRequirements.getBookingDate())
					  .append("\nBooking Time: ")
					  .append(bookingRequirements.getBookingTime())
					  .append("\nBooking Duration: ")
					  .append(bookingRequirements.getBookingDuration());
			}
		}
			
		
		return report.toString();
	}
	
	private String generateReportByClientName(String clientName) {
		StringBuilder report = new StringBuilder("");
		int results = 0;
		
		for(Booking currentBooking : bookings.values()) 
		{
			Client currentClient = currentBooking.getAssignedClient();
			BookingRequirements bookingRequirements = currentBooking.getBookingRequirements();
			Room bookedRoom = currentBooking.getBookedRoom();
			
			if(currentClient.getClientName().equalsIgnoreCase(clientName)) 
			{
				results++;
				
		
				report.append("\n\nResult No: ")
					  .append(results)
					  .append("\nClient Name: ")
					  .append(currentClient.getClientName())
					  .append("\nPhone Number: ")
					  .append(currentClient.getPhoneNum())
					  .append("\nEmail Address: ")
					  .append(currentClient.getClientEmail())
					  .append("\nRoom Number: ")
					  .append(bookedRoom.getRoomNumber())
					  .append("\nBooking Date: ")
					  .append(bookingRequirements.getBookingDate())
					  .append("\nBooking Time: ")
					  .append(bookingRequirements.getBookingTime())
					  .append("\nBooking Duration: ")
					  .append(bookingRequirements.getBookingDuration());
			}
		}
			
		
		return report.toString();
	}
	
	
	
	/**
	 * Returns a detailed summary of a specific booking if that booking can be located within the system.
	 * This will simply return the string literal "Not Found" should the BookingManager be unable to find the booking.
	 * The booking is located by using the reference number which is given when a booking is created. 
	 * 
	 * @param refNum
	 * @return reportString or an empty string if no results were found
	 */
	public String getBookingSummary(int refNum) 
	{
		return createBookingSummary(refNum);
	}
	
	private String createBookingSummary(int refNum) {
		
		boolean bookingExists = findBooking(refNum);
		
		StringBuilder bookingSummary = new StringBuilder("");
		
		
		if(bookingExists) {
			
			Booking b = bookings.get(refNum);
			
			Client currentClient = b.getAssignedClient();
			BookingRequirements bookingRequirements = b.getBookingRequirements();
			Room bookedRoom = b.getBookedRoom();
			
			bookingSummary
			.append("\n\nReference Number: ")
			.append(b.getRefNum())
			.append("\nClient Name: ")
			.append(currentClient.getClientName())
			.append("\nPhone Number: ")
			.append(currentClient.getPhoneNum())
			.append("\nEmail Address: ")
			.append(currentClient.getClientEmail())
			.append("\nRoom Number: ")
			.append(bookedRoom.getRoomNumber())
			.append("\nBooking Date: ")
			.append(bookingRequirements.getBookingDate())
			.append("\nBooking Time: ")
			.append(bookingRequirements.getBookingTime())
			.append("\nBooking Duration: ")
			.append(bookingRequirements.getBookingDuration());
			
		
		}
		
		return bookingSummary.toString();
		
	}
	
	private List<Room> findBestMatch(BookingRequirements requestedRequirements) 
	{
		
		List<Room> tmp = new ArrayList<>();
		
		tmp.addAll(rooms);
		
		tmp = RoomFinder.performSearch(bookings.values(), tmp, requestedRequirements);
	
		return tmp;
		
	}
	


	
	/**
	 * Searches for a client in the system using their identification number, this is used in conjunction with the
	 * createBooking method to correctly assign the client that is making the booking.
	 * 
	 * @param clientID
	 * @return The client that matches the ID number provided, {@code null} otherwise
	 */
	private Client findClient(int clientID) 
	{
		
		Client locatedClient = null;
		
		for(Client c : clients) 
		{
			if(c.getClientID() == clientID) 
			{
				locatedClient = c;
			}
		}
		
		return locatedClient;
		
	}
	
	/**
	 * Save the state of the room booking system and its records
	 * @throws FileNotFoundException
	 * @throws IOException
	 */
	private void saveBookings() throws FileNotFoundException, IOException 
	{
		try(ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(storageLocation)))
		{
			oos.writeObject(bookings);
			oos.writeObject(clients);
			oos.writeInt(clientIDCounter);
			oos.writeInt(refNumCounter);
			oos.writeUTF(collegeName);
		}
	}
	
	/**
	 * Load the booking records
	 * @throws FileNotFoundException
	 * @throws IOException
	 * @throws ClassNotFoundException
	 */
	@SuppressWarnings("unchecked")
	private void loadBookings() throws FileNotFoundException, IOException, ClassNotFoundException 
	{
		try(ObjectInputStream ois = new ObjectInputStream(new FileInputStream(storageLocation)))
		{
			bookings = (Map<Integer, Booking>) ois.readObject();
			clients = (List<Client>) ois.readObject();
			clientIDCounter = ois.readInt();
			refNumCounter = ois.readInt();
			collegeName = ois.readUTF();
		}
	}
	

	private boolean addRoom(Room r) 
	{
		return rooms.add(r);
	}
	
	private void populateRooms() 
	{
		addRoom(new Room(0, 12, 4, 	false, false));
		addRoom(new Room(18, 10, 8, true, true));
		addRoom(new Room(20, 0, 11, true, true));
		addRoom(new Room(6, 0, 12, false, true));
		addRoom(new Room(18, 2, 14, true, true));
		addRoom(new Room(18, 10, 13, true, true));
		addRoom(new Room(14, 10, 201, true, true));
		addRoom(new Room(0, 20, 71, true, false));
		addRoom(new Room(18, 0, 9, true, true));
		addRoom(new Room(12, 6, 100, true, true));
	}
	

	
}
