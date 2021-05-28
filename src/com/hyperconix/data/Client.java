package com.hyperconix.data;

import java.io.Serializable;
import java.util.Objects;

/**
 * Encapsulates the details of a client in the room booking system. A client is required to make bookings.
 * 
 * @author Luke S
 *
 */
public class Client implements Serializable
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * Responsible for storing the clients ID, this is used to reference them in the rest of the system
	 */
	private int clientID;
	
	/**
	 * Responsible for storing the clients full name
	 */
	private String clientName;
	
	/**
	 * Responsible for storing the clients phone number
	 */
	private String phoneNum;
	
	/**
	 * Responsible for storing the clients email address
	 */
	private String clientEmail;

	
	
	/**
	 * Creates the state of a Client in the room booking system. 
	 * A client will be given an id which will be used to reference them, clients make bookings in the room booking system
	 * but clients themselves do not have an official reference to their booking, the booking has a reference to which client made the booking.
	 *
	 * A client can be instantiated with an email but this is not required. However, a phone number must be provided.
	 *
	 * @param clientID The number used to reference the client 
	 * @param clientName The clients full name
	 * @param phoneNum The clients phone number
	 * @throws IllegalArgumentException if the client details are null, empty or if the ID number is less than 1
	 * @throws NullPointerException if any of the client details are null
	 */
	Client(int clientID, String clientName, String phoneNum)
	{
		this(clientID, clientName, phoneNum, "N/A");
	}
	
	/**
	 * Creates the state of a Client in the room booking system. 
	 * A client will be given an id which will be used to reference them, clients make bookings in the room booking system
	 * but clients themselves do not have an official reference to their booking, the booking has a reference to which client made the booking.
	 *
	 * A client can be instantiated with an email but this is not required. However, a phone number must be provided.
	 *
	 * @param clientID The number used to reference the client 
	 * @param clientName The clients full name
	 * @param phoneNum The clients phone number
	 * @param clientEmail The clients email address
	 * @throws IllegalArgumentException if the client details are null, empty or if the ID number is less than 1
	 * @throws NullPointerException if any of the client details are null
	 */
	Client(int clientID, String clientName, String phoneNum, String clientEmail)
	{
		Objects.requireNonNull(clientName, "The clients name cannot be null");
		Objects.requireNonNull(phoneNum, "The clients phone number cannot be null");
		Objects.requireNonNull(clientEmail, "The clients email cannot be null");
		
		if(clientName.isEmpty()) 
			throw new IllegalArgumentException("The clients name cannot be empty");

		if(phoneNum.isEmpty()) 
			throw new IllegalArgumentException("The clients name cannot be empty");
	
		
		if(clientEmail.isEmpty()) 
			throw new IllegalArgumentException("The clients name cannot be empty");
	
		if(clientID < 1)
			throw new IllegalArgumentException("The clients ID cannot be less than 1");
		
		
		this.clientID = clientID;
		this.clientName = clientName;
		this.phoneNum = phoneNum;
		this.clientEmail = clientEmail;
	}
	
	
	/**
	 * Returns the clients identification number
	 * 
	 * @return clientID
	 */
	public int getClientID() 
	{
		return clientID;
	}
	
	/**
	 * Returns the clients full name
	 * 
	 * @return clientName
	 */
	public String getClientName()
	{
		return clientName;
	}

	/**
	 * Returns the clients phone number
	 * 
	 * @return phoneNum
	 */
	public String getPhoneNum()
	{
		return phoneNum;
	}
	
	/**
	 * Sets the clients phone number to the provided phone number
	 * 
	 * @param phoneNum the clients phone number
	 */
	public void setPhoneNum(String phoneNum) 
	{
		this.phoneNum = phoneNum;
	}
	
	/**
	 * Returns the clients email address
	 * 
	 * @return clientEmail
	 */
	public String getClientEmail()
	{
		return clientEmail;
	}
	
	/**
	 * Sets the clients email address to the provided email address
	 * 
	 * @param clientEmail
	 */
	public void setClientEmail(String clientEmail) 
	{
		this.clientEmail = clientEmail;
	}

	@Override
	public String toString()
	{
		return "Client [clientID=" + clientID + ", clientName=" + clientName + ", phoneNum=" + phoneNum
				+ ", clientEmail=" + clientEmail + "]";
	}


	
	
	
	
	
}
