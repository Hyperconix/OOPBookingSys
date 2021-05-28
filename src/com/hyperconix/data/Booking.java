package com.hyperconix.data;

import java.io.Serializable;
import java.util.Objects;

/**
 * Encapsulates the details for a booking made in the Room Booking System. A booking contains a status which can
 * either be Booked or Cancelled. This is used for generating reports about bookings.
 * 
 * A booking cannot exist without a client who made the booking and a room that was booked for them
 * 
 * @author Luke S
 *
 */
public class Booking implements Serializable
{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * Responsible for storing the room that was booked 
	 */
	private Room bookedRoom;
	
	/**
	 * Responsible for the client that made the booking
	 */
	private Client assignedClient;
	
	/**
	 * Responsible for storing requirements of the booking
	 */
	private BookingRequirements bookingRequirements;
	
	/**
	 * Responsible for storing the reference number, this is used to identify a booking 
	 */
	private int refNum;
	
	
	
	/**
	 * Creates the state of a booking in the Room Booking System. 
	 * A booking consists of the room that was booked, the client that made the booking and the duration, date and time.
	 * @param refNum The reference number for the booking
	 * @param bookedRoom The room that was booked for the client
	 * @param assignedClient The client that booked the room
	 * @param bookingRequirements The requirements requested by the client for the booking
	 * @throws NullPointerException if any of the values are null
	 */
	Booking(int refNum, Room bookedRoom, Client assignedClient, BookingRequirements bookingRequirements)
	{
		Objects.requireNonNull(bookedRoom, "The room booked cannot be null");
		Objects.requireNonNull(assignedClient, "The assigned client cannot be null");
	
	
		
		this.bookedRoom = bookedRoom;
		this.assignedClient = assignedClient;
		this.bookingRequirements = bookingRequirements;
		this.refNum = refNum;
		
	}
	
	/**
	 * Returns the room that was booked by the client
	 * 
	 * @return bookedRoom
	 */
	public Room getBookedRoom()
	{
		return bookedRoom;
	}
	
	/**
	 * Returns the client that made this booking
	 * 
	 * @return assignedClient
	 */
	public Client getAssignedClient()
	{
		return assignedClient;
	}
	
	
	public BookingRequirements getBookingRequirements() {
		return bookingRequirements;
	}
	
	/**
	 * Returns the reference number for the booking
	 * 
	 * @return refNum
	 */
	public int getRefNum()
	{
		return refNum;
	}

	@Override
	public String toString()
	{
		return "Booking [bookedRoom=" + bookedRoom + ", assignedClient=" + assignedClient +  ", refNum=" + refNum + "]\n\n"
				+ bookingRequirements;
	}
	
	
	

	

	


	
	
	
	
}
