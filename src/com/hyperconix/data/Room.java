package com.hyperconix.data;

import java.io.Serializable;
import java.util.Objects;

/**
 * Encapsulates the information about a room in the college, these rooms have a series of facilities which will be
 * matched to a clients need.
 * 
 * @author Luke S
 *
 */
public class Room implements Comparable<Room>, Serializable
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	
	/** Salt for generating a hash value */
	private static final String SALT_HASH = "uk.forthvalley.personnel.learn._551373.data.Room";

	/**
	 * Responsible for storing the amount of computers that are in the room
	 */
	private int computerCapacity;
	
	/**
	 * Responsible for storing the amount of breakout seats that are in the room
	 */
	private int breakoutCapacity; 
	
	/**
	 * Responsible for storing the room number
	 */
	private int roomNumber; 
	
	/**
	 * Responsible for storing whether the room has a printer or not
	 */
	private boolean printer;
	
	/**
	 * Responsible for storing whether the room has a smartboard or not
	 */
	private boolean smartboard;
	

	/**
	 * Creates the state of a room in the Room Booking System. 
	 * 
	 * @param computerCapacity
	 * @param breakoutCapacity
	 * @param roomNumber
	 * @param printer
	 * @param smartboard
	 */
	Room(int computerCapacity, int breakoutCapacity, int roomNumber, boolean printer, boolean smartboard)
	{
		this.computerCapacity = computerCapacity;
		this.breakoutCapacity = breakoutCapacity;
		this.roomNumber = roomNumber;
		this.printer = printer;
		this.smartboard = smartboard;
	}
	
	/**
	 * Returns the amount of computers the room has
	 * @return computerCapacity
	 */
	public int getComputerCapacity()
	{
		return computerCapacity;
	}
	
	/**
	 * Returns the amount of breakout seats the room has
	 * 
	 * @return breakoutCapacity
	 */
	public int getBreakoutCapacity()
	{
		return breakoutCapacity;
	}
	
	/**
	 * Returns the room number
	 * 
	 * @return roomNumber
	 */
	public int getRoomNumber()
	{
		return roomNumber;
	}
	
	/**
	 * Returns whether the room has a printer or not
	 * 
	 * @return printer
	 */
	public boolean isPrinter()
	{
		return printer;
	}
	
	/**
	 * Returns whether the room has a smartboard or not
	 * 
	 * @return smartboard
	 */
	public boolean isSmartboard()
	{
		return smartboard;
	}
	
	
	@Override
	public boolean equals(Object o) {
		if(!(o instanceof Room)) {
			return false;
		}
		
		Room r = (Room)o;
		
		return computerCapacity == r.computerCapacity && breakoutCapacity == r.breakoutCapacity && roomNumber == r.roomNumber;
	}
	
	@Override
	public int hashCode() {
		return Objects.hash(SALT_HASH, computerCapacity, breakoutCapacity, roomNumber);
	}

	@Override
	public int compareTo(Room o)
	{
		if(this.computerCapacity > o.computerCapacity) 
		{
			return 1;
		}
		else if(this.computerCapacity < o.computerCapacity) 
		{
			return -1;
		}
		
		return 0;
	}
	
	
		
	
	
}
