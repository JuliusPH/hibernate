package com.exist.app;

import java.util.Scanner;
import java.util.Set;
import java.util.TreeSet;
import java.util.Arrays;
import org.apache.commons.validator.GenericValidator;

public class Validator{
    private static final Scanner inputScanner = new Scanner(System.in);
    private static final int DEFAULT_MAX = 255;
    
    public String validateText(int minLength, int maxLength, String error){
        String input = getInput("");
        while(!isLengthInRange(input, minLength, maxLength)){
            input = getInput(error);
        }
        if(input.equalsIgnoreCase("Cancel")){
            return "Cancelled";
        }
        return input;
    }
    
    public String validateText(int maxLength, String error){
        return validateText(1, maxLength, error);
    }
    
    public String validateText(String error){
        return validateText(1, DEFAULT_MAX, error);
    }
    
    public String validateChoice(String[] choices, String error){
        String input = getInput("");
        Set<String> choiceSet = new TreeSet<String>(String.CASE_INSENSITIVE_ORDER);
        for(String choice : choices){
            choiceSet.add(choice);
        }
        while(!choiceSet.contains(input)){
            if(input.equalsIgnoreCase("Cancel")){
                return "Cancelled";
            }
            
            input = getInput(error);
        }
        return input;
    }
    
    public String validateYesOrNo(String error){
        return validateChoice(new String[]{"Y", "N"}, error);
    }
    
    public String validateZipCode(String error){
        String input = getInput("");
        int length = 4;
        while(!(isLengthInRange(input, length, length) && GenericValidator.isInt(input))){
            if(input.equalsIgnoreCase("Cancel")){
                return "Cancelled";
            }
            
            input = getInput(error);
        }
        return input;
    }
    
    public String validateEmail(String error){
        String input = getInput("");
        while(!GenericValidator.isEmail(input)){
            if(input.equalsIgnoreCase("Cancel")){
                return "Cancelled";
            }
            
            input = getInput(error);
        }
        return input;
    }
    
    public String validateMobileNumber(String error){
        String input = getInput("");
        int length = 11;
        while(!GenericValidator.matchRegexp(input, "^(09|\\+639)\\d{9}$")){
            if(input.equalsIgnoreCase("Cancel")){
                return "Cancelled";
            }
            
            input = getInput(error);
        }
        return input;
    }
    
    public String validateLandlineNumber(String error){
        String input = getInput("");
        while(!GenericValidator.matchRegexp(input, "^\\d{7}$")){
            if(input.equalsIgnoreCase("Cancel")){
                return "Cancelled";
            }
            
            input = getInput(error);
        }
        return input;
    }
    
    public String validateGwa(String error){
        String input = getInput("");
        float min = 50f;
        float max = 100f;
        float gwa = 0f;
        while(!GenericValidator.isInRange(gwa, min, max)){
            if(input.equalsIgnoreCase("Cancel")){
                return "Cancelled";
            }
            
            if(GenericValidator.isFloat(input)){
                gwa = Float.parseFloat(input);
                if(GenericValidator.isInRange(gwa, min, max)){
                    break;
                }
            }
            
            input = getInput(error);
        }
        return String.valueOf(gwa);
    }
    
    public String validateId(String error){
        String input = getInput("");
        long id = 0L;
        while(!GenericValidator.minValue(id, 1L)){
            if(input.equalsIgnoreCase("Cancel")){
                return "Cancelled";
            }
            
            if(GenericValidator.isLong(input)){
                id = Long.parseLong(input);
                if(GenericValidator.minValue(id, 1L)){
                    break;
                }
            }
            
            input = getInput(error);
        }
        return String.valueOf(id);
    }
    
    public String validateNumber(int min, int max, String error){
        String input = getInput("");
        int number = 0;
        while(!GenericValidator.isInRange(number, min, max)){
            if(input.equalsIgnoreCase("Cancel")){
                return "Cancelled";
            }
            
            if(GenericValidator.isInt(input)){
                number = Integer.parseInt(input);
                if(GenericValidator.isInRange(number, min, max)){
                    break;
                }
            }
            
            input = getInput(error);
        }
        return String.valueOf(number);
    }
    
    public String validateNumber(int max, String error){
        return validateNumber(1, max, error);
    }
    
    public String validateYear(String error){
        return validateNumber(1900, 2016, error);
    }
    
    public String validateMonth(String error){
        return validateNumber(12, error);
    }
    
    public String validateDay(int month, int year, String error){
        return validateNumber(getLastDay(month, year), error);
    }
    
    private int getLastDay(int month, int year){
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
                if (year % 4 == 0 && year % 100 != 0 || year % 400 == 0){
                    lastDay = 29;
                }
                else{
                    lastDay = 28;
                }
                break;
        }
        return lastDay;
    }
    
    private String getInput(String error){
        System.out.print(error);
        return inputScanner.nextLine();
    }
    
    private boolean isLengthInRange(String input, int minLength, int maxLength){
        return GenericValidator.minLength(input, minLength) && 
               GenericValidator.maxLength(input, maxLength) &&
               !GenericValidator.isBlankOrNull(input);
    }
}
