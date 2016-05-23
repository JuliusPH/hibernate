package com.exist.app;

import com.exist.model.Address;
import com.exist.model.Contact;
import com.exist.model.Person;
import com.exist.model.enums.ContactType;
import com.exist.model.enums.Gender;
import com.exist.model.enums.Sort;
import com.exist.service.AddressService;
import com.exist.service.ContactService;
import com.exist.service.PersonService;
import com.exist.service.Validator;
import com.exist.service.impl.AddressServiceImpl;
import com.exist.service.impl.ContactServiceImpl;
import com.exist.service.impl.PersonServiceImpl;
import com.exist.util.HibernateUtil;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Comparator;
import java.util.Date;
import java.util.GregorianCalendar;;
import java.util.HashSet;
import java.util.List;
import java.util.Scanner;
import java.util.Set;
import java.util.stream.Collectors;
import org.apache.commons.lang3.text.WordUtils;

public class Menu{
    private Scanner inputScanner = new Scanner(System.in);
    private Validator validator = new Validator(inputScanner);
    private PersonService personService = new PersonServiceImpl(HibernateUtil.getSessionFactory());
    private AddressService addressService = new AddressServiceImpl(HibernateUtil.getSessionFactory());
    private ContactService contactService = new ContactServiceImpl(HibernateUtil.getSessionFactory());
    public boolean displayDefaultList = true;
    
    public void addPerson(){
        System.out.println("[ENTER CREATE MODE]\n[NAME]");
        String firstName = validator.validateText("Input first name: ", 
                                                  "Please input a valid first name: ");
        String middleName = validator.validateText("Input middle name: ", 
                                                   "Please input a valid middle name: ");
        String lastName = validator.validateText("Input last name: ", 
                                                 "Please input a valid last name: ");
                                                 
        System.out.println("[ADDRESS]");
        String streetNumber = validator.validateText("Input street number: ", 
                                                     "Please input a valid street number: ");
        String barangay = validator.validateText("Input barangay: ", 
                                                 "Please input a valid barangay: ");
        String city = validator.validateText("Input city: ", 
                                             "Please input a valid city: ");
        String zipCode = validator.validateZipCode("Input zip code: ", 
                                                   "Please input a valid zip code: ");
        Address address = new Address(streetNumber, barangay, city, zipCode);
                                                   
        System.out.println("[BIRTHDAY]");
        int year = validator.validateNumber(1900, 2016, "Input year (1900-2016): ", 
                                                        "Please input year from 1900 to 2016 only: "); 
        int month = validator.validateNumber(1, 12, "Input month (1-12): ", 
                                                    "Please input month from 1 to 12 only: ");
        int day = validator.validateNumber(1, validator.getLastDay(year, month), 
                                              "Input day (1-" + validator.getLastDay(year, month) + "): ", 
                                              "Please input month from 1 to " + validator.getLastDay(year, month) + " only: ");
        Date birthday = new GregorianCalendar(year, month - 1, day).getTime();  
              
        System.out.println("[GWA]");
        float gwa = validator.validateGwa("Input GWA: ", 
                                          "Please input a valid GWA: ");

        System.out.println("[EMPLOYMENT]");
        String[] yesOrNo = {"Y", "N"};
        
        String employedCheck = validator.validateText(yesOrNo, "Input Y if employed and N if not: ",
                                                               "Please input Y or N only: ");
        boolean isEmployed = employedCheck.equalsIgnoreCase("Y") ? true : false;
        
        Date dateHired = null;
        if(isEmployed){
            year = validator.validateNumber(1900, 2016, "Input year (1900-2016): ", 
                                                    "Please input year from 1900 to 2016 only: "); 
            month = validator.validateNumber(1, 12, "Input month (1-12): ", 
                                                    "Please input month from 1 to 12 only: ");
            day = validator.validateNumber(1, validator.getLastDay(year, month), 
                                              "Input day (1-" + validator.getLastDay(year, month) + "): ", 
                                              "Please input month from 1 to " + validator.getLastDay(year, month) + " only: ");
            dateHired = new GregorianCalendar(year, month - 1, day).getTime();
        }
        
        System.out.println("[CONTACTS]");
        String emailCheck = validator.validateText(yesOrNo, "Input Y if email exists and N if not: ",
                                                               "Please input Y or N only: ");
        boolean haveEmail = emailCheck.equalsIgnoreCase("Y") ? true : false;
        String email = "";
        Contact emailContact = null;
        if(haveEmail){
            email = validator.validateEmail("Input email: ", "Please input a valid email: ");
            emailContact = new Contact(ContactType.Email, email);
        }
        
        String mobileNumberCheck = validator.validateText(yesOrNo, "Input Y if mobile number exists and N if not: ",
                                                               "Please input Y or N only: ");
        boolean haveMobileNumber = mobileNumberCheck.equalsIgnoreCase("Y") ? true : false;
        String mobileNumber = "";
        Contact mobileContact = null;
        if(haveMobileNumber){
            mobileNumber = validator.validateMobileNumber("Input mobile number: ", "Please input a valid mobile number: ");
            mobileContact = new Contact(ContactType.Mobile, mobileNumber);
        }
        
        String landlineCheck = validator.validateText(yesOrNo, "Input Y if landline number exists and N if not: ",
                                                               "Please input Y or N only: ");
        boolean haveLandlineNumber = landlineCheck.equalsIgnoreCase("Y") ? true : false;
        String landlineNumber = "";
        Contact landlineContact = null;
        if(haveLandlineNumber){
            landlineNumber = validator.validateLandlineNumber("Input landline number: ", "Please input a valid landline number: ");
            landlineContact = new Contact(ContactType.Landline, landlineNumber);
        }
        
        Set<Contact> contacts = new HashSet<Contact>();
        if(emailContact != null){
            contacts.add(emailContact);
        }
        if(mobileContact != null){
            contacts.add(mobileContact);
        }
        if(landlineContact != null){
            contacts.add(landlineContact);
        }
        
        System.out.println("[GENDER]");
        String[] maleOrFemale = {"M", "F"};
        String genderCheck = validator.validateText(maleOrFemale, "Input M if male and F if female: ",
                                                                  "Please input M or F only: ");
        Gender gender = Gender.Male;
        if(genderCheck.equalsIgnoreCase("F")){
            gender = Gender.Female;
        }
        
        Person person = new Person(firstName, middleName, lastName, address, birthday, gwa, dateHired, isEmployed, contacts, gender);
        if(personService.add(person)){
            System.out.println("Successfully Added: " + firstName + " " + middleName + " " + lastName);
        }
        else{
            System.out.println("\nFailed to add person, please try again");
        }
        System.out.println("\n[EXIT CREATE MODE]\n");
        displayDefaultList = true;
    }
    
    public void deletePerson(){
        System.out.println("[ENTER DELETE MODE]\n");
        List<Person> personList = personService.list();
        if(personList.size() > 0){
            Long id = validator.validateId("\nInput id of person you want to delete: ", "Please input a valid number: ");
            Person person = personService.get(id);
            while(person == null){
                id = validator.validateId("\nInputted id does not exist, please repeat: ", "Please input a valid number: ");
                person = personService.get(id);
            }
            if(personService.remove(person)){
                System.out.println("Successfully deleted: " + person.getFirstName() + " " 
                                                            + person.getMiddleName() + " " 
                                                            + person.getLastName());
            }
            else{
                System.out.println("\nFailed to delete person, please try again");
            }
        }
        else{
            System.out.println("\nNo person to delete");
        }
        System.out.println("\n[EXIT DELETE MODE]\n");
        displayDefaultList = true;
    }
    
    public void updatePerson(){
        System.out.println("[ENTER UPDATE MODE]\n");
        List<Person> personList = personService.list();
        if(personList.size() > 0){
            Long id = validator.validateId("\nInput id of person you want to update: ", "Please input a valid number: ");
            Person person = personService.get(id);
            while(person == null){
                id = validator.validateId("\nInputted id does not exist, please repeat: ", "Please input a valid number: ");
                person = personService.get(id);
            }
            
            boolean continueUpdate = true;
            
            while(continueUpdate){
                String contacts = "";
                int count = 0;
                for(Contact contact : (Set<Contact>)person.getContacts()){
                    if(count > 0){
                        contacts += " ";
                    }
                    contacts += contact.getValue();
                    count++;
                }
                System.out.println("\nWhat information do you want to update?\n"  + 
                    "\n1 - NAME\n    " +
                    "[" + person.getFirstName() + " " + 
                          person.getMiddleName() + " " + 
                          person.getLastName() + "]\n" +
                    "\n2 - ADDRESS\n    " + 
                    "[" + person.getAddress().getStreetNumber() + " " + 
                          person.getAddress().getBarangay() + " " + 
                          person.getAddress().getCity() + " " + 
                          person.getAddress().getZipCode() + "]\n" +
                    "\n3 - BIRTHDAY\n    " + 
                    "[" + new SimpleDateFormat("yyyy-MM-dd").format(person.getBirthday()) + "]\n" +
                    "\n4 - GWA\n    " + 
                    "[" + person.getGwa() + "]\n" +
                    "\n5 - EMPLOYMENT\n    " + 
                    "[" + (person.isEmployed() ? "Employed " + new SimpleDateFormat("yyyy-MM-dd").format(person.getDateHired()): "Not employed") + 
                           "]\n" +
                    "\n6 - CONTACTS\n    " + "[" +
                          contacts + "]\n" +
                    "\n7 - GENDER\n    " + 
                    "[" + person.getGender() + "]\n" +
                    "\n8 - CANCEL\n");
                    
                int option = validator.validateNumber(1, 8, "Input a number from 1 to 8: ", "Please input a number from 1 to 8 only: ");
                
                switch(option){
                    case 1:
                        updatePersonName(person);
                        break;
                    case 2:
                        updatePersonAddress(person);
                        break;
                    case 3:
                        updatePersonBirthday(person);
                        break;
                    case 4:
                        updatePersonGwa(person);
                        break;
                    case 5:
                        updatePersonEmployment(person);
                        break;
                    case 6:
                        updatePersonContacts(person);
                        break;
                    case 7:
                        updatePersonGender(person);
                        break;
                    case 8:
                        continueUpdate = false;
                        break;
                    default:
                        break;
                }
                option = 0;
            }
        }
        else{
            System.out.println("\nNo person to update");
        }
        System.out.println("\n[EXIT UPDATE MODE]\n");
        displayDefaultList = true;
    }
    
    public void listPerson(){
        System.out.println("[ENTER LIST PERSON MODE]\n");
        
        String[] choices = {"G", "D", "L"};
        String option = validator.validateText(choices, "Input G to list person by GWA, D by date hired, or L by last name: ", "Please input G, D, or L only: ");
        option = option.toUpperCase();
        System.out.println();
        
        switch(option){
            case "G":
                listPersonBy(Sort.Gwa);
                break;
            case "D":
                listPersonBy(Sort.DateHired);
                break;
            case "L":
                listPersonBy(Sort.LastName);
                break;
            default:
                break;
        }
        System.out.println("\n[EXIT LIST PERSON MODE]\n");
        displayDefaultList = false;
    }
    
    public void updateContact(){
        System.out.println("[ENTER UPDATE CONTACT MODE]\n");
        List<Person> personList = personService.list();
        if(personList.size() > 0){
            Long id = validator.validateId("\nInput id of person you want to update the contact: ", "Please input a valid number: ");
            Person person = personService.get(id);
            while(person == null){
                id = validator.validateId("\nInputted id does not exist, please repeat: ", "Please input a valid number: ");
                person = personService.get(id);
            }
            
            
            Contact oldEmailContact = null;
            Contact oldMobileContact = null;
            Contact oldLandlineContact = null;
            List<String> choicesList = new ArrayList<String>();
            System.out.println("\n" + person.getFirstName() + " " + person.getMiddleName() + " " + person.getLastName() + "\n");
            if(person.getContacts().size() > 0){
                for(Contact contact : (Set<Contact>)person.getContacts()){
                    switch(contact.getContactType()){
                        case Email:
                            oldEmailContact = contact;
                            choicesList.add("E");
                            break;
                        case Mobile:
                            oldMobileContact = contact;
                            choicesList.add("M");
                            break;
                        case Landline:
                            oldLandlineContact = contact;
                            choicesList.add("L");
                            break;
                        default:
                            break;
                    }
                }
                choicesList.add("C");

                String[] choices = choicesList.toArray(new String[choicesList.size()]);
                String description = oldEmailContact == null ? "" : "\nE - Update Email [Old: " + oldEmailContact.getValue() + "]";
                description += oldMobileContact == null ? "" : "\nM - Update Mobile [Old: " + oldMobileContact.getValue() + "]";
                description += oldLandlineContact == null ? "" : "\nL - Update Landline [Old: " + oldLandlineContact.getValue() + "]";
                description += "\nC - Cancel\n";
                String option = validator.validateText(choices, description + "\nInput: ", description + "\nPlease input only from the choices above: ");
                option = option.toUpperCase();
                
                Contact emailContact = oldEmailContact;
                Contact mobileContact = oldMobileContact;
                Contact landlineContact = oldLandlineContact;
                switch(option){
                    case "E":
                        String email = validator.validateEmail("Input email (Old: " + oldEmailContact.getValue() + "): ", "Please input a valid email: ");
                        emailContact.setValue(email);
                        if(contactService.update(emailContact)){
                            System.out.println("Successfully updated email: " + oldEmailContact.getValue() + "->" + emailContact.getValue());
                        }
                        else{
                            System.out.println("Failed updating, please try again");
                        }
                        break;
                    case "M":
                        String mobile = validator.validateMobileNumber("Input mobile (Old: " + oldMobileContact.getValue() + "): ", "Please input a valid mobile: ");
                        mobileContact.setValue(mobile);
                        if(contactService.update(mobileContact)){
                            System.out.println("Successfully updated mobile: " + oldMobileContact.getValue() + "->" + mobileContact.getValue());
                        }
                        else{
                            System.out.println("Failed updating, please try again");
                        }
                        break;
                    case "L":
                        String landline = validator.validateLandlineNumber("Input landline (Old: " +  oldLandlineContact.getValue() + "): ", "Please input a valid landline: ");
                        landlineContact.setValue(landline);
                        if(contactService.update(landlineContact)){
                            System.out.println("Successfully updated landline: " + oldLandlineContact.getValue() + "->" + landlineContact.getValue());
                        }
                        else{
                            System.out.println("Failed updating, please try again");
                        }
                        break;
                    case "C":
                        break;
                    default:
                        break;
                }
            }
            else{
                System.out.println("\nThere is no contact to update, please add first");
            }
        }
        else{
            System.out.println("\nNo person to update contacts");
        }
        System.out.println("\n[EXIT UPDATE CONTACT MODE]\n");
        displayDefaultList = true;
    }
    
    public void deleteContact(){
        System.out.println("[ENTER DELETE CONTACT MODE]\n");
        List<Person> personList = personService.list();
        if(personList.size() > 0){
            Long id = validator.validateId("\nInput id of person you want to delete the contact: ", "Please input a valid number: ");
            Person person = personService.get(id);
            while(person == null){
                id = validator.validateId("\nInputted id does not exist, please repeat: ", "Please input a valid number: ");
                person = personService.get(id);
            }
            
            Contact oldEmailContact = null;
            Contact oldMobileContact = null;
            Contact oldLandlineContact = null;
            List<String> choicesList = new ArrayList<String>();
            System.out.println("\n" + person.getFirstName() + " " + person.getMiddleName() + " " + person.getLastName() + "\n");
            if(person.getContacts().size() > 0){
                for(Contact contact : (Set<Contact>)person.getContacts()){
                    switch(contact.getContactType()){
                        case Email:
                            oldEmailContact = contact;
                            choicesList.add("E");
                            break;
                        case Mobile:
                            oldMobileContact = contact;
                            choicesList.add("M");
                            break;
                        case Landline:
                            oldLandlineContact = contact;
                            choicesList.add("L");
                            break;
                        default:
                            break;
                    }
                }
                choicesList.add("C");

                String[] choices = choicesList.toArray(new String[choicesList.size()]);
                String description = oldEmailContact == null ? "" : "\nE - Delete Email [" + oldEmailContact.getValue() + "]";
                description += oldMobileContact == null ? "" : "\nM - Delete Mobile [" + oldMobileContact.getValue() + "]";
                description += oldLandlineContact == null ? "" : "\nL - Delete Landline [" + oldLandlineContact.getValue() + "]";
                description += "\nC - Cancel\n";
                String option = validator.validateText(choices, description + "\nInput: ", description + "\nPlease input only from the choices above: ");
                option = option.toUpperCase();
                
                Contact emailContact = oldEmailContact;
                Contact mobileContact = oldMobileContact;
                Contact landlineContact = oldLandlineContact;
                switch(option){
                    case "E":
                        if(contactService.remove(emailContact)){
                            System.out.println("Successfully deleted email: " + emailContact.getValue());
                        }
                        else{
                            System.out.println("Failed deleting, please try again");
                        }
                        break;
                    case "M":
                        if(contactService.remove(mobileContact)){
                            System.out.println("Successfully deleted mobile: " + mobileContact.getValue());
                        }
                        else{
                            System.out.println("Failed deleting, please try again");
                        }
                        break;
                    case "L":
                        if(contactService.remove(landlineContact)){
                            System.out.println("Successfully deleted landline: " + landlineContact.getValue());
                        }
                        else{
                            System.out.println("Failed deleting, please try again");
                        }
                        break;
                    case "C":
                        break;
                    default:
                        break;
                }
            }
            else{
                System.out.println("\nThere is no contact to delete, please add first");
            }
        }
        else{
            System.out.println("\nNo person to delete contacts");
        }
        System.out.println("\n[EXIT DELETE CONTACT MODE]\n");
        displayDefaultList = true;
    }
    
    private void updatePersonName(Person person){
        System.out.println("\n[UPDATE NAME]");
        
        String oldFirstName = person.getFirstName();
        String oldMiddleName = person.getMiddleName();
        String oldLastName = person.getLastName();
        
        String firstName = validator.validateText("Input first name (Old: " + oldFirstName + "): ", 
                                                  "Please input a valid first name: ");
        String middleName = validator.validateText("Input middle name (Old: " + oldMiddleName + "): ", 
                                                   "Please input a valid middle name: ");
        String lastName = validator.validateText("Input last name (Old: " + oldLastName + "): ", 
                                                 "Please input a valid last name: ");
        person.setFirstName(firstName);
        person.setMiddleName(middleName);
        person.setLastName(lastName);
        if(personService.update(person)){
            System.out.println("\nSuccessfully updated:\n" + "First name: " + oldFirstName + " -> " + firstName + "\n" 
                                                           + "Middle name: " + oldMiddleName + " -> " + middleName + "\n" 
                                                           + "Last name: " + oldLastName + " -> " + lastName);
        }
        else{
            System.out.println("\nFailed to update name, please try again");
        }
    }
    
    private void updatePersonAddress(Person person){
        System.out.println("[UPDATE ADDRESS]");
        Address address = person.getAddress();
        
        String oldStreetNumber = address.getStreetNumber();
        String oldBarangay = address.getBarangay();
        String oldCity = address.getCity();
        String oldZipCode = address.getZipCode();
        
        String streetNumber = validator.validateText("Input street number (Old: " + oldStreetNumber + "): ", 
                                                     "Please input a valid street number: ");
        String barangay = validator.validateText("Input barangay (Old: " + oldBarangay + "): ", 
                                                 "Please input a valid barangay: ");
        String city = validator.validateText("Input city (Old: " + oldCity + "): ", 
                                             "Please input a valid city: ");
        String zipCode = validator.validateZipCode("Input zip code (Old: " + oldZipCode + "): ", 
                                                   "Please input a valid zip code: ");
        address.setStreetNumber(streetNumber);
        address.setBarangay(barangay);
        address.setCity(city);
        address.setZipCode(zipCode);
        if(addressService.update(address)){
            System.out.println("\nSuccessfully updated:\n" + "Street number: " + oldStreetNumber + " -> " + streetNumber + "\n" 
                                                           + "Barangay: " + oldBarangay + " -> " + barangay + "\n" 
                                                           + "City: " + oldCity + " -> " + city + "\n" 
                                                           + "Zip code: " + oldZipCode + " -> " + zipCode);
        }
        else{
            System.out.println("\nFailed to update address, please try again");
        }
    }
    
    private void updatePersonBirthday(Person person){
        System.out.println("\n[UPDATE BIRTHDAY]");
        
        Date oldBirthday = person.getBirthday();
        
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(oldBirthday);
        
        int oldYear = calendar.get(Calendar.YEAR);
        int oldMonth = calendar.get(Calendar.MONTH) + 1;
        int oldDay = calendar.get(Calendar.DAY_OF_MONTH);
        
        int year = validator.validateNumber(1900, 2016, "Input year (1900-2016) (Old: " + oldYear + "): ", 
                                                        "Please input year from 1900 to 2016 only: "); 
        int month = validator.validateNumber(1, 12, "Input month (1-12) (Old: " + oldMonth + "): ", 
                                                    "Please input month from 1 to 12 only: ");
        int day = validator.validateNumber(1, validator.getLastDay(year, month), 
                                              "Input day (1-" + validator.getLastDay(year, month) + ") (Old: " + oldDay + "): ", 
                                              "Please input month from 1 to " + validator.getLastDay(year, month) + " only: ");
        Date birthday = new GregorianCalendar(year, month - 1, day).getTime();  
        
        person.setBirthday(birthday);
        if(personService.update(person)){
            System.out.println("\nSuccessfully updated:\n" + "Birthday: " + oldBirthday + " -> " + birthday);
        }
        else{
            System.out.println("\nFailed to update birthday, please try again");
        }
    }
    
    private void updatePersonGwa(Person person){
        System.out.println("\n[UPDATE GWA]");
        
        float oldGwa = person.getGwa();
        
        float gwa = validator.validateGwa("Input GWA (Old: " + oldGwa + "): ", "Please input a valid GWA: ");
        
        person.setGwa(gwa);
        if(personService.update(person)){
            System.out.println("\nSuccessfully updated:\n" + "GWA: " + oldGwa + " -> " + gwa);
        }
        else{
            System.out.println("\nFailed to update gwa, please try again");
        }
    }
    
    private void updatePersonEmployment(Person person){
        System.out.println("\n[UPDATE EMPLOYMENT]");
        
        boolean oldEmployed = person.isEmployed();
        
        Date oldDateHired = person.getDateHired();
        
        Calendar calendar = Calendar.getInstance();
        int oldYear = 0;
        int oldMonth = 0;
        int oldDay = 0;
        
        if(oldDateHired != null){
            calendar.setTime(oldDateHired);
            oldYear = calendar.get(Calendar.YEAR);
            oldMonth = calendar.get(Calendar.MONTH) + 1;
            oldDay = calendar.get(Calendar.DAY_OF_MONTH);
        }
        
        String[] yesOrNo = {"Y", "N"};
        String employedCheck = validator.validateText(yesOrNo, "Input Y if employed and N if not: ",
                                                               "Please input Y or N only: ");
        boolean isEmployed = employedCheck.equalsIgnoreCase("Y") ? true : false;
        
        Date dateHired = null;
        int year = 0; 
        int month = 0;
        int day = 0;
        if(isEmployed){
            year = validator.validateNumber(1900, 2016, "Input year (1900-2016) (Old: " + (oldYear == 0 ? "" : oldYear) + "): ", 
                                                        "Please input year from 1900 to 2016 only: "); 
            month = validator.validateNumber(1, 12, "Input month (1-12) (Old: " + (oldMonth == 0 ? "" : oldMonth) + "): ", 
                                                    "Please input month from 1 to 12 only: ");
            day = validator.validateNumber(1, validator.getLastDay(year, month), 
                                              "Input day (1-" + validator.getLastDay(year, month) + ") (Old: " + (oldDay == 0 ? "" : oldDay) + "): ", 
                                              "Please input month from 1 to " + validator.getLastDay(year, month) + " only: ");
            dateHired = new GregorianCalendar(year, month - 1, day).getTime();  
        }
        
        person.setEmployed(isEmployed);
        person.setDateHired(dateHired);
        if(personService.update(person)){
            System.out.println("\nSuccessfully updated:\n" + "Is Employed: " + oldEmployed + " -> " + isEmployed + "\n" 
                                                           + "Date Hired: " + oldDateHired + " -> " + year + "-" + month + "-" + day);
        }
        else{
            System.out.println("\nFailed to update employment, please try again");
        }
    }
    
    private void updatePersonContacts(Person person){
        System.out.println("\n[UPDATE CONTACTS]");
        
        Set<Contact> oldContacts = (Set<Contact>)person.getContacts();
        Contact oldEmailContact = null;
        Contact oldMobileContact = null;
        Contact oldLandlineContact = null;
        
        for(Contact contact : oldContacts){
            switch(contact.getContactType()){
                case Email:
                    oldEmailContact = contact;
                    break;
                case Mobile:
                    oldMobileContact = contact;
                    break;
                case Landline:
                    oldLandlineContact = contact;
                    break;
                default:
                    break;
            }
        }
        
        Contact emailContact = oldEmailContact;
        Contact mobileContact = oldMobileContact;
        Contact landlineContact = oldLandlineContact;
        
        String[] yesNoDelete = {"Y", "N", "D"};
        String[] yesOrNo = {"Y", "N"};
        
        String emailCheck = "";
        if(oldEmailContact != null){
            emailCheck = validator.validateText(yesNoDelete, "Input Y if you want to edit email, N if no, and D if you want to delete: ",
                                                             "Please input Y, N, or D only: ");
            if(emailCheck.equalsIgnoreCase("Y")){
                String email = validator.validateEmail("Input email (Old: " + oldEmailContact + "): ", "Please input a valid email: ");
                emailContact.setValue(email);
            }
            else if(emailCheck.equalsIgnoreCase("D")){
                emailContact = null;
            }
        }
        else{
            System.out.println("No email contact");
            emailCheck = validator.validateText(yesOrNo, "Input Y if you want to add email and N if not: ",
                                                             "Please input Y or N only: ");
            if(emailCheck.equalsIgnoreCase("Y")){
                String email = validator.validateEmail("Input email: ", "Please input a valid email: ");
                emailContact = new Contact(ContactType.Email, email);
            }
            else{
                emailContact = null;
            }
        }
        
        String mobileCheck = "";
        if(oldMobileContact != null){
            mobileCheck = validator.validateText(yesNoDelete, "Input Y if you want to edit mobile, N if no, and D if you want to delete: ",
                                                              "Please input Y, N, or D only: ");
            if(mobileCheck.equalsIgnoreCase("Y")){
                String email = validator.validateMobileNumber("Input mobile (Old: " + oldMobileContact + "): ", "Please input a valid mobile number: ");
                mobileContact.setValue(email);
            }
            else if(mobileCheck.equalsIgnoreCase("D")){
                mobileContact = null;
            }
        }
        else{
            System.out.println("No mobile contact");
            mobileCheck = validator.validateText(yesOrNo, "Input Y if you want to add mobile and N if no: ",
                                                              "Please input Y or N only: ");
            if(mobileCheck.equalsIgnoreCase("Y")){
                String mobile = validator.validateMobileNumber("Input mobile: ", "Please input a valid mobile number: ");
                mobileContact = new Contact(ContactType.Mobile, mobile);
            }
            else{
                mobileContact = null;
            }
        }
        
        String landlineCheck = "";
        if(oldLandlineContact != null){
            landlineCheck = validator.validateText(yesNoDelete, "Input Y if you want to edit landline, N if no, and D if you want to delete: ",
                                                                "Please input Y, N, or D only: ");
            if(landlineCheck.equalsIgnoreCase("Y")){
                String landline = validator.validateLandlineNumber("Input landline (Old: " + oldLandlineContact + "): ", "Please input a valid landline number: ");
                landlineContact.setValue(landline);
            }
            else if(landlineCheck.equalsIgnoreCase("D")){
                landlineContact = null;
            }
        }
        else{
            System.out.println("No landline contact");
            landlineCheck = validator.validateText(yesOrNo, "Input Y if you want to add landline and N if no: ",
                                                                "Please input Y or N only: ");
            if(landlineCheck.equalsIgnoreCase("Y")){
                String landline = validator.validateLandlineNumber("Input landline: ", "Please input a valid landline number: ");
                landlineContact = new Contact(ContactType.Landline, landline);
            }
            else{
                landlineContact = null;
            }
        }
        
        if(emailCheck.equalsIgnoreCase("Y")){
            if(oldEmailContact != null){
                if(contactService.add(emailContact)){
                    System.out.println("Added email");
                }
                else{
                    System.out.println("Failed to add email");
                }
            }
            else{
                if(contactService.update(emailContact)){
                    System.out.println("Updated email");
                }
                else{
                    System.out.println("Failed to update email");
                }
            }
        }
        else if(emailCheck.equalsIgnoreCase("D")){
            if(contactService.remove(oldEmailContact)){
                System.out.println("Deleted email");
            }
            else{
                System.out.println("Failed to delete email");
            }
        }
        
        if(mobileCheck.equalsIgnoreCase("Y")){
            if(oldMobileContact != null){
                if(contactService.add(mobileContact)){
                    System.out.println("Added mobile");
                }
                else{
                    System.out.println("Failed to add mobile");
                }
            }
            else{
                if(contactService.update(mobileContact)){
                    System.out.println("Updated mobile");
                }
                else{
                    System.out.println("Failed to update mobile");
                }
            }
        }
        else if(mobileCheck.equalsIgnoreCase("D")){
            if(contactService.remove(oldMobileContact)){
                System.out.println("Deleted mobile");
            }
            else{
                System.out.println("Failed to delete mobile");
            }
        }
        
        if(landlineCheck.equalsIgnoreCase("Y")){
            if(oldLandlineContact != null){
                if(contactService.add(landlineContact)){
                    System.out.println("Added landline");
                }
                else{
                    System.out.println("Failed to add landline");
                }
            }
            else{
                if(contactService.update(landlineContact)){
                    System.out.println("Updated landline");
                }
                else{
                    System.out.println("Failed to update landline");
                }
            }
        }
        else if(landlineCheck.equalsIgnoreCase("D")){
            if(contactService.remove(oldLandlineContact)){
                System.out.println("Deleted landline");
            }
            else{
                System.out.println("Failed to delete landline");
            }
        }
        
        Set<Contact> contacts = new HashSet<Contact>();
        if(emailContact != null){
            contacts.add(emailContact);
        }
        
        if(mobileContact != null){
            contacts.add(mobileContact);
        }
        
        if(landlineContact != null){
            contacts.add(landlineContact);
        }
        
        String oldEmailContactValue = oldEmailContact != null ? oldEmailContact.getValue() : "None";
        String oldMobileContactValue = oldMobileContact != null ? oldMobileContact.getValue() : "None";
        String oldLandlineContactValue = oldLandlineContact != null ? oldLandlineContact.getValue() : "None";
        
        String emailContactValue = emailContact != null ? emailContact.getValue() : "None";
        String mobileContactValue = mobileContact != null ? mobileContact.getValue() : "None";
        String landlineContactValue = landlineContact != null ? landlineContact.getValue() : "None";
        
        person.setContacts(contacts);
        if(personService.update(person)){
            System.out.println("\nSuccessfully updated:\n" + "Email: " + oldEmailContactValue + " -> " + emailContactValue + "\n" 
                                                           + "Mobile: " + oldMobileContactValue + " -> " + mobileContactValue + "\n" 
                                                           + "Landline: " + oldLandlineContactValue + " -> " + landlineContactValue);
        }
        else{
            System.out.println("\nFailed to update contacts, please try again");
        }
    }
    
    private void updatePersonGender(Person person){
        System.out.println("\n[UPDATE GENDER]");
        
        Gender oldGender = person.getGender();
        
        String[] maleOrFemale = {"M", "F"};
        String genderCheck = validator.validateText(maleOrFemale, "Input M if male and F if female: ",
                                                                  "Please input M or F only: ");
        Gender gender = Gender.Male;
        if(genderCheck.equalsIgnoreCase("F")){
            gender = Gender.Female;
        }
        
        person.setGender(gender);
        
        if(personService.update(person)){
            System.out.println("\nSuccessfully updated:\n" + "Gender: " + oldGender + " -> " + gender);
        }
        else{
            System.out.println("\nFailed to update gender, please try again");
        }
    }
    
    public void listPersonBy(Sort sort){
        System.out.println();
        List<Person> list = null;
        if(sort != null){
            list = personService.list(sort);
        }
        else{
            list = personService.list();
        }
        List<String> idList = list.stream()
                                  .map((Person p) -> " " + p.getId() + " ")
                                  .collect(Collectors.toList());
        idList.add(0, " ID ");
        
        List<String> nameList = list.stream()
                                    .map((Person p) -> " " + 
                                        WordUtils.capitalize(p.getFirstName()) + " " + 
                                        p.getMiddleName().substring(0, 1).toUpperCase() + ". " + 
                                        WordUtils.capitalize(p.getLastName()) + " ")
                                    .collect(Collectors.toList());
        nameList.add(0, " NAME ");
        
        List<String> addressList = list.stream()
                                       .map((Person p) -> " " +
                                           p.getAddress().getStreetNumber() + " " + 
                                           p.getAddress().getBarangay() + " " + 
                                           p.getAddress().getCity() + " " + 
                                           p.getAddress().getZipCode() + " ")
                                       .collect(Collectors.toList());
        addressList.add(0, " ADDRESS ");
        
        List<String> birthdayList = list.stream()
                                        .map((Person p) -> " " + p.getBirthday() + " ")
                                        .collect(Collectors.toList());
        birthdayList.add(0, " BIRTHDAY ");
        
        List<String> gwaList = list.stream()
                                   .map((Person p) -> " " + String.format("%.02f",p.getGwa()) + " ")
                                   .collect(Collectors.toList());
        gwaList.add(0, " GWA ");
        
        List<String> employmentList = list.stream()
                                          .map((Person p) -> " " + (p.isEmployed() ? "Employed" : "Not employed") + ", " + p.getDateHired() + " ")
                                          .collect(Collectors.toList());
        employmentList.add(0, " EMPLOYMENT ");
        
        List<Set<Contact>> contactSetList = list.stream()
                                                .map((Person p) -> (Set<Contact>)p.getContacts())
                                                .collect(Collectors.toList());
        
        List<String> contactList = new ArrayList<String>();
        for(Set<Contact> contactSet : contactSetList){
            contactList.add(
                contactSet.stream()
                          .map((Contact c) -> c.getValue())
                          .reduce("", (value1, value2) -> value1 + " " + value2 + " "));
        }
        contactList.add(0, " CONTACTS ");
        
        List<String> genderList = list.stream()
                                      .map((Person p) -> " " + p.getGender() + " ")
                                      .collect(Collectors.toList());
        genderList.add(0, " GENDER ");
        
        System.out.println();
        int tableLength = getMaxLength(idList) +
                          getMaxLength(nameList) +
                          getMaxLength(addressList) +
                          getMaxLength(birthdayList) +
                          getMaxLength(gwaList) +
                          getMaxLength(employmentList) +
                          getMaxLength(contactList) +
                          getMaxLength(genderList) + 9;
        
        System.out.println(format("", tableLength, '-'));
        for(int i = 0; i < idList.size(); i++){
            System.out.print("|" + format(idList.get(i), getMaxLength(idList)) + "|");
            System.out.print(format(nameList.get(i), getMaxLength(nameList)) + "|");
            System.out.print(format(addressList.get(i), getMaxLength(addressList)) + "|");
            System.out.print(format(birthdayList.get(i), getMaxLength(birthdayList)) + "|");
            System.out.print(format(gwaList.get(i), getMaxLength(gwaList)) + "|");
            System.out.print(format(employmentList.get(i), getMaxLength(employmentList)) + "|");
            System.out.print(format(contactList.get(i), getMaxLength(contactList)) + "|");
            System.out.println(format(genderList.get(i), getMaxLength(genderList)) + "|");
            System.out.println(format("", tableLength, '-'));
        }
        System.out.println();
    }
    
    private String format(String input, int length){
        while(input.length() < length){
            input += " ";
        }
        return input;
    }
    
    private String format(String input, int length, char c){
        while(input.length() < length){
            input += c;
        }
        return input;
    }
    
    private int getMaxLength(List<String> list){
        return list.stream().max(Comparator.comparingInt(String::length)).get().length();
    }

    public Validator getValidator(){
        return validator;
    }
    
}
