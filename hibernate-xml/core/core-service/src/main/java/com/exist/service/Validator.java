package com.exist.service;

import java.util.Scanner;
import java.util.Set;
import java.util.TreeSet;
import java.util.Arrays;
import org.apache.commons.validator.GenericValidator;

public class Validator{
    private Scanner inputScanner;
    private static final int DEFAULT_MAX = 255;
    
    public Validator(Scanner inputScanner){
        this.inputScanner = inputScanner;
    }
    
    public String validateText(String description, String error){
        System.out.print(description);
        String input = inputScanner.nextLine();
        while(!(GenericValidator.maxLength(input, DEFAULT_MAX) && !GenericValidator.isBlankOrNull(input))){
            System.out.print(error);
            input = inputScanner.nextLine();
        }
        return input;
    }
    
    public String validateText(int maxLength, String description, String error){
        System.out.print(description);
        String input = inputScanner.nextLine();
        while(!(GenericValidator.maxLength(input, maxLength) && !GenericValidator.isBlankOrNull(input))){
            System.out.print(error);
            input = inputScanner.nextLine();
        }
        return input;
    }
    
    public String validateText(int minLength, int maxLength, String description, String error){
        System.out.print(description);
        String input = inputScanner.nextLine();
        while(!(GenericValidator.minLength(input, minLength) && GenericValidator.maxLength(input, maxLength))){
            System.out.print(error);
            input = inputScanner.nextLine();
        }
        return input;
    }
    
    public String validateText(String[] choices, String description, String error){
        System.out.print(description);
        Set<String> choiceSet = new TreeSet<String>(String.CASE_INSENSITIVE_ORDER);
        for(String choice : choices){
            choiceSet.add(choice);
        }
        String input = inputScanner.nextLine();
        while(!choiceSet.contains(input)){
            System.out.print(error);
            input = inputScanner.nextLine();
        }
        return input;
    }
    
    public String validateZipCode(String description, String error){
        System.out.print(description);
        String input = inputScanner.nextLine();
        int length = 4;
        while(!(GenericValidator.minLength(input, length) && 
                GenericValidator.maxLength(input, length) && 
                GenericValidator.isInt(input))){
            System.out.print(error);
            input = inputScanner.nextLine();
        }
        return input;
    }
    
    public String validateEmail(String description, String error){
        System.out.print(description);
        String input = inputScanner.nextLine();
        while(!(GenericValidator.isEmail(input) && 
                GenericValidator.maxLength(input, DEFAULT_MAX) && 
                !GenericValidator.isBlankOrNull(input))){
            System.out.print(error);
            input = inputScanner.nextLine();
        }
        return input;
    }
    
    public String validateMobileNumber(String description, String error){
        System.out.print(description);
        String input = inputScanner.nextLine();
        int length = 11;
        while(!(GenericValidator.minLength(input, length) && GenericValidator.maxLength(input, length) && GenericValidator.isFloat(input) && input.substring(0,2).equals("09"))){
            System.out.print(error);
            input = inputScanner.nextLine();
        }
        return input;
    }
    
    public String validateLandlineNumber(String description, String error){
        System.out.print(description);
        String input = inputScanner.nextLine();
        int length = 7;
        while(!(GenericValidator.minLength(input, length) && GenericValidator.maxLength(input, length) && GenericValidator.isFloat(input))){
            if(input.equalsIgnoreCase("exit")){
                System.out.println("asd");
                break;
            }
            
            System.out.print(error);
            input = inputScanner.nextLine();
        }
        return input;
    }
    
    public Float validateGwa(String description, String error){
        System.out.print(description);
        String input = inputScanner.nextLine();
        float min = 10f;
        float max = 100f;
        float gwa = 0f;
        while(!(GenericValidator.isFloat(input) && GenericValidator.isInRange(gwa, min, max))){
            if(input.equalsIgnoreCase("exit")){
                System.out.println("asd");
                break;
            }
            
            if(GenericValidator.isFloat(input)){
                gwa = Float.parseFloat(input);
                if(GenericValidator.isInRange(gwa, min, max)){
                    return gwa;
                }
                else{
                    System.out.print(error);
                    input = inputScanner.nextLine();
                }
            }
            else{
                System.out.print(error);
                input = inputScanner.nextLine();
            }
        }
        return gwa;
    }
    
    public Long validateId(String description, String error){
        System.out.print(description);
        String input = inputScanner.nextLine();
        long number = 0L;
        while(!(GenericValidator.isLong(input) && GenericValidator.minValue(number, 1L))){
            if(input.equalsIgnoreCase("exit")){
                System.out.println("asd");
                break;
            }
            
            if(GenericValidator.isLong(input)){
                number = Long.parseLong(input);
                return number;
            }
            else{
                System.out.print(error);
                input = inputScanner.nextLine();
            }
        }
        return number;
    }
    
    public Integer validateNumber(String description, String error){
        System.out.print(description);
        String input = inputScanner.nextLine();
        int number = 0;
        while(!(GenericValidator.isInt(input) && GenericValidator.minValue(number, 1))){
            if(input.equalsIgnoreCase("exit")){
                System.out.println("asd");
                break;
            }
            
            if(GenericValidator.isInt(input)){
                number = Integer.parseInt(input);
                return number;
            }
            else{
                System.out.print(error);
                input = inputScanner.nextLine();
            }
        }
        return number;
    }
    
    public Integer validateNumber(int min, int max, String description, String error){
        System.out.print(description);
        String input = inputScanner.nextLine();
        int number = 0;
        while(!(GenericValidator.isInt(input) && GenericValidator.isInRange(number, min, max))){
            if(input.equalsIgnoreCase("exit")){
                System.out.println("asd");
                break;
            }
            
            if(GenericValidator.isInt(input)){
                number = Integer.parseInt(input);
                if(GenericValidator.isInRange(number, min, max)){
                    return number;
                }
                else{
                    System.out.print(error);
                    input = inputScanner.nextLine();
                }
            }
            else{
                System.out.print(error);
                input = inputScanner.nextLine();
            }
        }
        return number;
    }
    
    public int getLastDay(int year, int month){
        int lastDay = 1;
        switch(month){
            case 1:
            case 3:
            case 5:
            case 7:
            case 8:
            case 10:
            case 12:
                lastDay = 31;
                break;
            case 4:
            case 6:
            case 9:
            case 11:
                lastDay = 30;
                break;
            case 2:
                if (year % 4 == 0 && year % 100 != 0 || year % 400 == 0)
                {
                    lastDay = 29;
                }
                else
                {
                    lastDay = 28;
                }
                break;
        }
        return lastDay;
    }
}
