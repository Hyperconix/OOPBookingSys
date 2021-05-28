package com.hyperconix.data;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class RoomFinder {
	
	
	/**
	 * Filters a list of rooms based on the requirements and rooms that are currently available
	 * 
	 * @param requestedRequirements
	 * @return filtered list of rooms based on requirements
	 */
	static List<Room> performSearch(Collection<Booking> bookings, List<Room> rooms, BookingRequirements requestedRequirements) {
		
		List<Room> tmp = new ArrayList<>();
		
		tmp.addAll(rooms);
		
		int requestedComputers = requestedRequirements.getComputerCapacity();
		
		LocalDate requestedDate = requestedRequirements.getBookingDate();
		
		LocalTime requestedTime = requestedRequirements.getBookingTime();
		
		filterDateTime(bookings, tmp, requestedDate, requestedTime);
		
		tmp.removeIf(r -> r.getComputerCapacity() < requestedComputers);

	    tmp.sort((Room r1, Room r2) -> (r1.getComputerCapacity() - requestedComputers) - (r2.getComputerCapacity() - requestedComputers));
	    
	    return tmp;
	    
	}
	
	/**
	 * Filters a list of rooms and removes rooms which are already booked on the same date and time.
	 * This ensures that a room cannot be booked by two people at the same date and time but allows
	 * rooms to be booked at different dates and times.
	 * 
	 * This methods takes a list and returns a filtered version of that list
	 * 
	 * @param bookings collection of bookings
	 * @param filterList The list to be filtered
	 * @param filterDate The date to filter on
	 * @param filterTime The time to filter on
	 * @return The filtered version of the list
	 */
	private static List<Room> filterDateTime(Collection<Booking> bookings, List<Room> filteredList, LocalDate requestedDate, LocalTime requestedTime) {
		
		for(Booking currentBooking : bookings) {
			BookingRequirements existingRequirements = currentBooking.getBookingRequirements();
			
			Room bookedRoom = currentBooking.getBookedRoom();
			
			LocalDate bookingDate = existingRequirements.getBookingDate();
			
			long bookingDuration = existingRequirements.getBookingDuration();
			
			LocalTime bookingStartTime = existingRequirements.getBookingTime();
			
			LocalTime bookingEndTime = bookingStartTime.plusHours(bookingDuration); 
			
			
			//Remove if the room is booked at the same time on the same date/before the start time
			filteredList.removeIf(r -> bookedRoom.equals(r) && bookingDate.equals(requestedDate) && ( bookingStartTime.equals(requestedTime) || requestedTime.isBefore(bookingStartTime) ) );
			
			
			//Remove if the room is booked on the same date and the time is between the start and end of the booking
			filteredList.removeIf(r -> bookedRoom.equals(r) && bookingDate.equals(requestedDate) && isBetweenStartAndStop(requestedTime, bookingStartTime, bookingEndTime));
		}
		
		return filteredList;
		
	}
	
	/**
	 * This method determines if a target time is between a start and end time (Non Inclusive).
	 * @param target
	 * @param start
	 * @param end
	 * @return {@code true} if the target is between the start and stop {@code false} otherwise
	 */
	private static boolean isBetweenStartAndStop(LocalTime target, LocalTime start, LocalTime end) {
		return (target.isAfter(start) && target.isBefore(end));
	}
}
