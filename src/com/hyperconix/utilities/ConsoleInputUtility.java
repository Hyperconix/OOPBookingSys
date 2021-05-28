package com.hyperconix.utilities;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeParseException;
import java.util.Objects;
import java.util.Scanner;

/**
 * This class is a stripped down version of a set of console based input utilities for
 * making console based applications easier to work with. This includes reading integers, boolean confirmations dates etc.
 * <br>
 * 
 * 
 * 
 * @author Luke S
 *
 */
public class ConsoleInputUtility
{
	 /**
     * The scanner used for reading input from the console
     */
    private Scanner consoleInput;
    

    /**
     * Creates the state of the console input utility, a scanner must be provided in order for input to be
     * read from the console and manipulated. 
     * @param consoleInput The scanner used to read input from the console
     */
    public ConsoleInputUtility(Scanner consoleInput) {
    	
        Objects.requireNonNull(consoleInput, "The inputted scanner cannot be null");
        
        this.consoleInput = consoleInput;
        
    }

    /**
     * This method reads a string value from the console and ensures that the value is not empty, this will always
     * return a string that contains at least one character. This function is used in all other similar methods
     * to ensure the input is not empty.
     *
     * @param userMessage The message prompt to display to the user
     * @return A string that contains at least one character
     * @throws NullPointerException If the user message provided is null
     */
    public String readUnemptyString(String userMessage) {
    	
        Objects.requireNonNull(userMessage, "The message prompt cannot be null");

        System.out.print(userMessage);

        String userInput = consoleInput.nextLine();
        
        userInput = userInput.trim();

        while(userInput.isEmpty()) {
        	
            System.out.println("Error: The provided input cannot be empty! Please try again");
            
            System.out.print(userMessage);

            userInput = consoleInput.nextLine();
            
            userInput = userInput.trim();
            
        }

        return userInput;

    }
    
    /**
     * This method extends the basic readUnemptyString method to validate the input using a regular expression. Use this method
     * if you want to enforce additional rules when reading a string from the user. Additionally, you should provide a help prompt
     * that details the format the users input should be in to match the regular expression. However, this string can be empty.
     * 
     * @param userMessage The message prompt to display to the user
     * @param regex The regular expression to validate the users input against
     * @param helpPrompt A message that describes the correct format the input should be in to match the regex
     * @return A string that contains at least one character and is validated against the provided regex
     */
    public String readUnemptyString(String userMessage, String regex, String helpPrompt) {
     
        String userInput = readUnemptyString(userMessage);

        while(!userInput.matches(regex)) {
            System.out.println("Error: The provided input is not in the correct format");
            
           	System.out.println(helpPrompt);

            userInput = readUnemptyString(userMessage);
            
        }

        return userInput;
    }

    /**
     * This functions reads a confirmation from the user and returns a boolean value depending on their
     * choice. This simplifies the need to write these statements for every single user
     * confirmation that may exist. This confirmation works by checking a yes or no value which
     * is provided as a parameter when this function is called. The confirmation can be strict where the user must
     * enter only the yes or no values or not strict where any value other than the yes value will result in false.
     * <br>
     * In the strict mode, if the user enters a value does not match either the yes value or the no value then they
     * will be continuously asked until they enter a correct input.
     *
     * This is controlled by the parameter strict.
     *
     * @param userMessage The message prompt to display
     * @param yesValue The value to be used for a positive or true confirmation (yes)
     * @param noValue The value to be used for a negative or false confirmation (no)
     * @param strict Whether the confirmation should be strict or not
     * @return A boolean representing the confirmation choice
     * @throws NullPointerException If the yes or no values are null
     * @throws IllegalArgumentException If the yes or no values are empty
     */
    public boolean readBooleanConfirmation(String userMessage, String yesValue, String noValue, boolean strict) {
    	
        Objects.requireNonNull(yesValue, "The value for yes cannot be null");
        
        Objects.requireNonNull(noValue, "The value for no cannot be null");

        if(yesValue.trim().isEmpty())
            throw new IllegalArgumentException("The value used for yes cannot be empty!");

        if(noValue.trim().isEmpty())
            throw new IllegalArgumentException("The value used for no cannot be empty!");

        String userInput = readUnemptyString(userMessage);
        
        boolean confirm = userInput.equalsIgnoreCase(yesValue);

        if(strict) {
            while(!confirm && !userInput.equalsIgnoreCase(noValue)) {
            	
                System.out.printf("Error: The input must match either %s or %s\n", yesValue, noValue);
                
                userInput = readUnemptyString(userMessage);

                confirm = userInput.equalsIgnoreCase(yesValue);
             }
        }

        return confirm;

    }
    
    
    public int readValidInteger(String userMessage) {
    	return readValidInteger(userMessage, 0, Integer.MAX_VALUE);
    }

    /**
     * This function reads a whole number integer from the user within a range, it must only be numerical and will
     * continuously ask the user until they enter a valid integer.
     * @param userMessage The prompt to display to the user
     * @param minNum The lower bound that the integer is allowed to be
     * @param maxNum The upper bound that the integer is allowed to be
     *
     * @return A valid integer representation of the user input
     *
     * @throws IllegalArgumentException If the bounds are not correctly inputted
     * @throws NumberFormatException If the value surpasses the max integer
     */
    public int readValidInteger(String userMessage, int minNum, int maxNum) {

        if(minNum > maxNum)
            throw new IllegalArgumentException("The min boundary cannot be greater than the maximum or vice versa");

        String userInput;
        
        boolean isValid;
        
        int validInt = 0;

        do {
            userInput = readUnemptyString(userMessage);

            if(isNumericOnly(userInput)) {
            	//Does not prevent overflow
                validInt = Integer.parseInt(userInput);

                isValid = validInt >= minNum && validInt <= maxNum;
            }
            else {
                isValid = false;
            }

            if(!isValid)
                System.out.printf("Error: Please enter a valid integer between the given range %d and %d\n", minNum, maxNum);

        } while(!isValid);


        return validInt;
    }
    
    /**
     * 
     * @param userMessage The prompt to display to the user
     * @return A LocalDate
     */
    public LocalDate readLocalDate(String userMessage){

        String userInput = readUnemptyString(userMessage);
        
        //TODO Add DateTimeFormatter to make more modular 
        
        while(!isValidDate(userInput)) {
        	
            System.out.println("Error: Please enter a valid date (yyyy-mm-dd)");
            
            userInput = readUnemptyString(userMessage);
        }

        LocalDate date = LocalDate.parse(userInput);

        return date;

    }

    public LocalTime readLocalTime(String userMessage) {
    	
        String userInput = readUnemptyString(userMessage);

        while(!isValidTime(userInput)) {
        	
            System.out.println("Error: Please enter the time in the correct format hh:mm:ss");
            
            userInput = readUnemptyString(userMessage);
            
        }
        
        LocalTime time = LocalTime.parse(userInput);

        return time;
        
    }
    
    
    private boolean isValidTime(String time) {
        try {
            LocalTime.parse(time);
        }
        catch(DateTimeParseException e) {
            return false;
        }

        return true;
    }

    private boolean isValidDate(String date) {
        try {
            LocalDate.parse(date);
        }
        catch (DateTimeParseException e) {
            return false;
        }
        return true;
    }

    private boolean isNumericOnly(String input) {
        return input.matches("^-?[0-9]+$");
    }
}
