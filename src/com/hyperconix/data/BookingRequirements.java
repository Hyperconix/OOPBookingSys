package com.hyperconix.data;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Objects;

/**
 * This class is a model of the requirements the client requested for a booking.
 * 
 * @author Luke S
 *
 */
public class BookingRequirements implements Serializable
{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * Responsible for storing the number of computers that was requested
	 */
	private int computerCapacity;
	
	/**
	 * Responsible for storing the number of breakout seats that was requested
	 */
	private int breakoutSeats;
	
	/**
	 * Responsible for storing whether or not a smartboard is required
	 */
	private boolean smartboard;
	
	/**
	 * Responsible for storing whether or not a printer is required
	 */
	private boolean printer;
	
	/**
	 * Responsible for storing the duration of the booking in hours that was requested
	 */
	private long bookingDuration;
	
	/**
	 * Responsible for storing the date of the booking
	 */
	private LocalDate bookingDate;
	
	/**
	 * Responsible for storing the time of the booking
	 * 
	 */
	private LocalTime bookingTime;
	
	
	/**
	 * 
	 * Creates the state of the BookingRequirements, this groups together all the requirements that a client needs for a booking
	 * 
	 * @param computerCapacity The computer capacity that the client requested
	 * @param duration The duration of the booking that was requested by the client
	 * @param bookingDate The date the client is booking on
	 * @param bookingTime The time that the booking is being made at
	 */
	public BookingRequirements(int computerCapacity,  long duration, LocalDate bookingDate, LocalTime bookingTime) {
			Objects.requireNonNull(bookingDate, "The booking date cannot be null");
			Objects.requireNonNull(bookingTime, "The booking time cannot be null");
			
			if(computerCapacity < 0) {
				throw new IllegalArgumentException("The number of computers must be a positive number");
			}
			
			if(duration < 1 || duration > 24) {
				throw new IllegalArgumentException("The duration must be between the given range 1-24");
			}
			
			this.computerCapacity = computerCapacity;

			this.bookingDuration = duration;
			this.bookingDate = bookingDate;
			this.bookingTime = bookingTime;
		
	}

	
	/**
	 * Get the number of computers that were required for the booking
	 * @return computerCapacity
	 */
	public int getComputerCapacity()
	{
		return computerCapacity;
	}

	/**
	 * Get the number of breakout seats that were required for the booking
	 * @return computerCapacity
	 */
	public int getBreakoutSeats()
	{
		return breakoutSeats;
	}


	public boolean isSmartboard()
	{
		return smartboard;
	}


	public boolean isPrinter()
	{
		return printer;
	}

	
	/**
	 * Returns the duration of the booking
	 * 
	 * @return bookingDuration
	 */
	public long getBookingDuration()
	{
		return bookingDuration;
	}


	/**
	 * Returns the date of the booking
	 * 
	 * @return bookingDate
	 */
	public LocalDate getBookingDate()
	{
		return bookingDate;
	}

	/**
	 * Returns the time of the booking
	 * 
	 * @return bookingTime
	 */
	public LocalTime getBookingTime()
	{
		return bookingTime;
	}


	@Override
	public String toString()
	{
		return "BookingRequirements [computerCapacity=" + computerCapacity + ", bookingDuration=" + bookingDuration
				+ ", bookingDate=" + bookingDate + ", bookingTime=" + bookingTime + "]";
	}
	
	
	
	
	
	
	
}
