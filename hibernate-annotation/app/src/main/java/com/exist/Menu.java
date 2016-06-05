package com.exist.app;

import com.exist.dto.AddressDto;
import com.exist.dto.ContactDto;
import com.exist.dto.PersonDto;
import com.exist.dto.RoleDto;
import com.exist.model.Name;
import com.exist.model.enums.ContactType;
import com.exist.model.enums.Gender;
import com.exist.model.enums.Sort;
import com.exist.service.ContactService;
import com.exist.service.PersonService;
import com.exist.service.RoleService;
import com.exist.service.AddressService;
import com.exist.service.impl.ContactServiceImpl;
import com.exist.service.impl.PersonServiceImpl;
import com.exist.service.impl.RoleServiceImpl;
import com.exist.service.impl.AddressServiceImpl;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import org.apache.commons.lang.SerializationUtils;
import org.apache.commons.lang3.StringUtils;
import de.vandermeer.asciitable.v2.RenderedTable;
import de.vandermeer.asciitable.v2.V2_AsciiTable;
import de.vandermeer.asciitable.v2.render.V2_AsciiTableRenderer;
import de.vandermeer.asciitable.v2.render.WidthFixedColumns;
import de.vandermeer.asciitable.v2.themes.V2_E_TableThemes;

public class Menu{
    private Validator validator;
    private PersonService personService = new PersonServiceImpl();
    private ContactService contactService = new ContactServiceImpl();
    private RoleService roleService = new RoleServiceImpl();
    private AddressService addressService = new AddressServiceImpl();
    
    public static final String RED_COLOR = "\u001B[31m";
    public static final String WHITE_COLOR = "\u001B[37m";
    public static final String GREEN_COLOR = "\u001B[32m";
    
    public Menu(Validator validator){
        this.validator = validator;
    }
    
    public void addPerson(){
        PersonDto person = new PersonDto();
        person.setGwa(50f);
        person.setEmployed(false);
        person.setContacts(new HashSet<ContactDto>());
        person.setGender(Gender.Male);
        person.setRoles(new HashSet<RoleDto>());
        while(true){
            System.out.println("\n------- CHOOSE THE DETAIL TO EDIT -------\n");
            System.out.println("[1] NAME*      (Current: " + getPersonName(person) + ")");
            System.out.println("[2] ADDRESS*   (Current: "  + getPersonAddress(person) + ")");
            System.out.println("[3] BIRTHDAY*  (Current: "  + getPersonBirthday(person) + ")");
            System.out.println("[4] GWA        (Current: "  + getPersonGwa(person) + ")");
            System.out.println("[5] EMPLOYMENT (Current: "  + getPersonEmployment(person) + ")");
            System.out.println("[6] CONTACTS   (Current: "  + getPersonContacts(person) + ")");
            System.out.println("[7] GENDER     (Current: "  + getPersonGender(person) + ")");
            System.out.println("[8] ROLES      (Current: "  + getPersonRoles(person) + ")");
            System.out.println("[9] SAVE");
            System.out.println("------------------------------------------");
            System.out.print("[ADD PERSON] Input a number from 1 to 9: ");
            String option = validator.validateNumber(9, "[ADD PERSON] Please input a number from 1 to 9 only: ");
            
            if(option.equals("Cancelled")){
                printInRed("Cancelled");
                return;
            }
            
            switch(option){
                case "1":
                    Name nameInput = getInputName(person);
                    if(nameInput != null){
                        person.setName(nameInput);
                    }
                    break;
                case "2":
                    AddressDto addressInput = getInputAddress(person);
                    if(addressInput != null){
                        person.setAddress(addressInput);
                    }
                    break;
                case "3":
                    Date birthdayInput = getInputBirthday(person);
                    if(birthdayInput != null){
                        person.setBirthday(birthdayInput);
                    }
                    break;
                case "4":
                    float gwaInput = getInputGwa(person);
                    if(gwaInput >= 50f){
                        person.setGwa(gwaInput);
                    }
                    break;
                case "5":
                    boolean isEmployedInput = getInputIsEmployed(person);
                    person.setEmployed(isEmployedInput);
                    if(isEmployedInput){
                        Date dateHiredInput = getInputDateHired(person);
                        if(dateHiredInput != null){
                            person.setDateHired(dateHiredInput);
                        }
                    }
                    break;
                case "6":
                    Set<ContactDto> contactsInput = getInputContacts(person);
                    if(contactsInput != null){
                        person.setContacts(contactsInput);
                    }
                    break;
                case "7":
                    Gender genderInput = getInputGender(person);
                    if(genderInput != null){
                        person.setGender(genderInput);
                    }
                    break;
                case "8":
                    Set<RoleDto> rolesInput = getInputRoles(person);
                    if(rolesInput != null){
                        person.setRoles(rolesInput);
                    }
                    break;
                case "9":
                    if(isValidPerson(person)){
                        Set<ContactDto> contactSet = person.getContacts();
                        contactSet.stream().forEach(contact -> {
                            contact.setPerson(person);
                        });
                        if(personService.add(person)){
                            printInGreen("Successfully Added!");
                            return;
                        }
                        else{
                            printInRed("An error occurs, please try again");
                        }
                    }
                    else{
                        printInRed("Please supply the required fields");
                    }
                    break;
                default:
                    break;
            }
        }
    }
    
    public void deletePerson(){
        while(true){
            PersonDto person = getPersonById();
            if(person == null){
                return;
            }
            else{
                String name = person.getName().getFirstName() + " " + person.getName().getMiddleName() + " " + person.getName().getLastName();
                System.out.println("\n[DELETE PERSON] Person found: " + name + "\n");
                System.out.print("[DELETE PERSON] Are you sure you want to delete this person? Input 'Y' if yes and 'N' if no: ");
                String delete = validator.validateYesOrNo("[DELETE PERSON] Please input 'Y' or 'N' only: ");
                if(delete.equals("Cancelled")){
                    printInRed("Cancelled");
                    return;
                }
                else if(delete.equalsIgnoreCase("Y")){
                    if(personService.delete(person)){
                        printInGreen("Successfully deleted: " + name);
                        return;
                    }
                    else{
                        printInRed("An error occurs, please try again");
                    }
                }
            }
        }
    }
    
    public void updatePerson(){
        PersonDto person = getPersonById();
        if(person == null){
            return;
        }
        String name = person.getName().getFirstName() + " " + person.getName().getMiddleName() + " " + person.getName().getLastName();
        System.out.println("\n[UPDATE PERSON] Person found: " + name + "\n");
        PersonDto tempPerson = (PersonDto) SerializationUtils.clone(person);
        Set<ContactDto> oldContactSet = person.getContacts();
        while(true){
            System.out.println("\n------- CHOOSE THE DETAIL TO EDIT -------\n");
            System.out.println("[1] NAME*      (Current: " + getPersonName(tempPerson) + ", Original: " + getPersonName(person) + ")");
            System.out.println("[2] ADDRESS*   (Current: " + getPersonAddress(tempPerson) + ", Original: " + getPersonAddress(person) + ")");
            System.out.println("[3] BIRTHDAY*  (Current: " + getPersonBirthday(tempPerson) + ", Original: " + getPersonBirthday(person) + ")");
            System.out.println("[4] GWA        (Current: " + getPersonGwa(tempPerson) + ", Original: " + getPersonGwa(person) + ")");
            System.out.println("[5] EMPLOYMENT (Current: " + getPersonEmployment(tempPerson) + ", Original: " + getPersonEmployment(person) + ")");
            System.out.println("[6] CONTACTS   (Current: " + getPersonContacts(tempPerson) + ", Original: " + getPersonContacts(person) + ")");
            System.out.println("[7] GENDER     (Current: " + getPersonGender(tempPerson) + ", Original: " + getPersonGender(person) + ")");
            System.out.println("[8] ROLES      (Current: " + getPersonRoles(tempPerson) + ", Original: " + getPersonRoles(person) + ")");
            System.out.println("[9] SAVE");
            System.out.println("------------------------------------------");
            System.out.print("[UPDATE PERSON] Input a number from 1 to 9: ");
            String option = validator.validateNumber(9, "[UPDATE PERSON] Please input a number from 1 to 9 only: ");
            
            if(option.equals("Cancelled")){
                printInRed("Cancelled");
                return;
            }
            
            switch(option){
                case "1":
                    Name nameInput = getInputName(tempPerson);
                    if(nameInput != null){
                        tempPerson.setName(nameInput);
                    }
                    break;
                case "2":
                    AddressDto addressInput = getInputAddress(tempPerson);
                    if(addressInput != null){
                        tempPerson.setAddress(addressInput);
                    }
                    break;
                case "3":
                    Date birthdayInput = getInputBirthday(tempPerson);
                    if(birthdayInput != null){
                        tempPerson.setBirthday(birthdayInput);
                    }
                    break;
                case "4":
                    float gwaInput = getInputGwa(tempPerson);
                    if(gwaInput >= 50f){
                        tempPerson.setGwa(gwaInput);
                    }
                    break;
                case "5":
                    boolean isEmployedInput = getInputIsEmployed(tempPerson);
                    tempPerson.setEmployed(isEmployedInput);
                    if(isEmployedInput){
                        Date dateHiredInput = getInputDateHired(tempPerson);
                        if(dateHiredInput != null){
                            tempPerson.setDateHired(dateHiredInput);
                        }
                    }
                    break;
                case "6":
                    Set<ContactDto> contactsInput = getInputContacts(tempPerson);
                    if(contactsInput != null){
                        tempPerson.setContacts(contactsInput);
                    }
                    break;
                case "7":
                    Gender genderInput = getInputGender(tempPerson);
                    if(genderInput != null){
                        tempPerson.setGender(genderInput);
                    }
                    break;
                case "8":
                    Set<RoleDto> rolesInput = getInputRoles(tempPerson);
                    if(rolesInput != null){
                        tempPerson.setRoles(rolesInput);
                    }
                    break;
                case "9":
                    if(isValidPerson(tempPerson)){
                        Set<ContactDto> contactSet = tempPerson.getContacts();
                        contactSet.stream().forEach(contact -> {
                            if(oldContactSet.contains(contact)){
                                oldContactSet.remove(contact);
                            }
                            contact.setPerson(person);
                        });
                        contactService.deleteAll(oldContactSet);
                        name = tempPerson.getName().getFirstName() + " " + tempPerson.getName().getMiddleName() + " " + tempPerson.getName().getLastName();
                        if(personService.update(tempPerson)){
                            printInGreen("Successfully updated: " + name);
                            return;
                        }
                        else{
                            printInRed("An error occurs, please try again");
                        }
                    }
                    else{
                        printInRed("Please supply the required fields");
                    }
                    break;
                default:
                    break;
            }
        }
    }
    
    public void updateContacts(){
        PersonDto person = getPersonById();
        if(person == null){
            return;
        }
        while(true){
            PersonDto tempPerson = (PersonDto) SerializationUtils.clone(person);
            String name = person.getName().getFirstName() + " " + person.getName().getMiddleName() + " " + person.getName().getLastName();
            System.out.println("\n[UPDATE PERSON CONTACTS] Person found: " + name);
            Set<ContactDto> oldContactSet = person.getContacts();
            Set<ContactDto> contactSet = getInputContacts(tempPerson);
            if(contactSet == null){
                return;
            }
            else{
                contactSet.stream().forEach(contact -> {
                    if(oldContactSet.contains(contact)){
                        oldContactSet.remove(contact);
                    }
                    contact.setPerson(person);
                });
                if(contactService.updateAll(contactSet) && contactService.deleteAll(oldContactSet)){
                    person.setContacts(contactSet);
                    printInGreen("Successfully updated contacts of: " + name);
                    return;
                }
                else{
                    printInRed("An error occurs, please try again");
                }
            }
        }
    }
    
    public void deleteContacts(){
        Set<ContactDto> contactSet = null;
        Set<ContactDto> tempContactSet = new HashSet<ContactDto>();
        String name = "";
        PersonDto person = getPersonById();
        if(person == null){
            return;
        }
        else{
            name = person.getName().getFirstName() + " " + person.getName().getMiddleName() + " " + person.getName().getLastName();
            System.out.println("\n[DELETE PERSON CONTACTS] Person found: " + name);
            contactSet = person.getContacts();
            if(contactSet.size() < 1){
                printInRed("No contacts to delete, please add first");
                return;
            }
        }
        while(true){
            System.out.println("\n------- SELECT CONTACT/S YOU WANT TO DELETE -------");
            int optionCount = 1;
            for(ContactDto contact : contactSet){
                System.out.println("[" + optionCount + "] " + (tempContactSet.contains(contact) ? "[x] " : "[ ] ") + " " + contact.getValue());
                optionCount ++;
            }
            System.out.println("[" + optionCount + "] DELETE\n");
            System.out.print("[CONTACT] Input a number from 1 to " + optionCount + ": ");
            String option = validator.validateNumber(optionCount, "[CONTACT] Please input a number from 1 to " + optionCount + ": ");
            if(option.equals("Cancelled")){
                printInRed("Cancelled");
                return;
            }
            if(Integer.parseInt(option) < optionCount){
                ContactDto contact = new ArrayList<ContactDto>(contactSet).get(Integer.parseInt(option) - 1);
                if(tempContactSet.contains(contact)){
                    tempContactSet.remove(contact);
                }
                else{
                    tempContactSet.add(contact);
                }
            }
            else{
                if(tempContactSet.size() > 0){
                    if(contactService.deleteAll(tempContactSet)){
                        printInGreen("Successfully deleted contact/s of: " + name);
                        return;
                    }
                    else{
                        printInRed("An error occurs, please try again");
                    }
                }
                else{
                    printInRed("No contact/s selected, please select one or more");
                }
            }
        }
    }
    
    public void listPersons(){
        Sort sort = getInputSort();
        boolean isAscending = getInputIsAscending();
        listPersons(sort, isAscending);
    }
    
    public void listPersons(Sort sort, boolean isAscending){
        List<PersonDto> personList = null;
        if(sort == null){
            personList = personService.getAll();
        }
        else{
            personList = personService.getAllBy(sort, isAscending);
        }
        V2_AsciiTable table = new V2_AsciiTable();
        table.addRule();
        table.addRow("ID", "NAME", "ADDRESS", "BIRTHDAY", "GWA", "EMPLOYMENT", "CONTACTS", "GENDER", "ROLES");
        table.addRule();
        for(PersonDto person : personList){
            table.addRow(
                person.getId(),
                getPersonName(person),
                getPersonAddress(person),
                getPersonBirthday(person),
                getPersonGwa(person),
                getPersonEmployment(person),
                getPersonContacts(person),
                getPersonGender(person),
                getPersonRoles(person)
            );
            table.addRule();
        }
        V2_AsciiTableRenderer renderer = new V2_AsciiTableRenderer();
        renderer.setTheme(V2_E_TableThemes.UTF_LIGHT.get());
        WidthFixedColumns widthFixedColumns = new WidthFixedColumns();
        widthFixedColumns.add(5);
        widthFixedColumns.add(30);
        widthFixedColumns.add(30);
        widthFixedColumns.add(10);
        widthFixedColumns.add(10);
        widthFixedColumns.add(15);
        widthFixedColumns.add(15);
        widthFixedColumns.add(8);
        widthFixedColumns.add(15);
        renderer.setWidth(widthFixedColumns);
        RenderedTable renderedTable = renderer.render(table);
        System.out.println(renderedTable);
    }
    
    private PersonDto getPersonById(){
        System.out.print("[GET PERSON] Input id of the person: ");
        String id = validator.validateId("[GET PERSON] Please input a valid id: ");
        if(id.equals("Cancelled")){
            printInRed("Cancelled");
            return null;
        }
        PersonDto person = personService.get(Long.parseLong(id));
        while(person == null){
            System.out.print("\n[GET PERSON] Inputted id does not exist, please try again: ");
            id = validator.validateId("[GET PERSON] Please input a valid id: ");
            if(id.equals("Cancelled")){
                printInRed("Cancelled");
                return null;
            }
            System.out.println();
            person = personService.get(Long.parseLong(id));
        }
        return person;
    }
    
    private String getPersonName(PersonDto person){
        Name name = person.getName();
        return (name != null ? 
                name.getFirstName() + " " +
                name.getMiddleName() + " " +
                name.getLastName() : "none");
    }
    
    private String getPersonAddress(PersonDto person){
        AddressDto address = person.getAddress();
        return (address != null ? 
                address.getStreetNumber() + " " +
                address.getBarangay() + " " +
                address.getCity() + " " +
                address.getZipCode() : "none");
    }
    
    private String getPersonBirthday(PersonDto person){
        Date birthday = person.getBirthday();
        return (birthday != null ? new SimpleDateFormat("MM/dd/yyyy").format(birthday) : "none");
    }
    
    private String getPersonGwa(PersonDto person){
        return String.format("%.02f", person.getGwa());
    }
    
    private String getPersonEmployment(PersonDto person){
        boolean isEmployed = person.isEmployed();
        Date dateHired = person.getDateHired();
        return (isEmployed ? "Employed " + new SimpleDateFormat("MM/dd/yyyy").format(dateHired) : "Not employed");
    }
    
    private String getPersonContacts(PersonDto person){
        Set<String> contactValueSet = person.getContacts().stream().map(c -> c.getValue()).collect(Collectors.toCollection(HashSet::new));
        return (contactValueSet.size() > 0 ? StringUtils.join(contactValueSet, ", ") : "None");
    }
    
    private String getPersonGender(PersonDto person){
        return String.valueOf(person.getGender());
    }
    
    private String getPersonRoles(PersonDto person){
        Set<String> roleValueSet = person.getRoles().stream().map(r -> r.getValue()).collect(Collectors.toCollection(HashSet::new));
        return (roleValueSet.size() > 0 ? StringUtils.join(roleValueSet, ", ")  : "None");
    }
    
    private Name getInputName(PersonDto person){
        Name currentName = person.getName();
        if(currentName != null){
            System.out.println("[NAME] Current first name is `" + currentName.getFirstName() + "`");
        }
        System.out.print("[NAME] Input first name: ");
        String firstName = validator.validateText("[NAME] Please input a valid first name: ");
        if(firstName.equals("Cancelled")){
            printInRed("Cancelled");
            return null;
        }
        if(currentName != null){
            System.out.println("[NAME] Current middle name is `" + currentName.getMiddleName() + "`");
        }
        System.out.print("[NAME] Input middle name: ");
        String middleName = validator.validateText("[NAME] Please input a valid middle name: ");
        if(middleName.equals("Cancelled")){
            printInRed("Cancelled");
            return null;
        }
        if(currentName != null){
            System.out.println("[NAME] Current last name is `" + currentName.getLastName() + "`");
        }
        System.out.print("[NAME] Input last name: ");
        String lastName = validator.validateText("[NAME] Please input a valid last name: ");
        if(lastName.equals("Cancelled")){
            printInRed("Cancelled");
            return null;
        }
        printInGreen("Name set to -> " + firstName + " " + middleName + " " + lastName);
        return new Name(firstName, middleName, lastName);
    }
    
    private AddressDto getInputAddress(PersonDto person){
        AddressDto currentAddress = person.getAddress();
        if(currentAddress != null){
            System.out.println("[ADDRESS] Current street number is `" + currentAddress.getStreetNumber() + "`");
        }
        System.out.print("[ADDRESS] Input street number: ");
        String streetNumber = validator.validateText("[ADDRESS] Please input a valid street number: ");
        if(streetNumber.equals("Cancelled")){
            printInRed("Cancelled");
            return null;
        }
        if(currentAddress != null){
            System.out.println("[ADDRESS] Current barangay is `" + currentAddress.getBarangay() + "`");
        }
        System.out.print("[ADDRESS] Input barangay: ");
        String barangay = validator.validateText("[ADDRESS] Please input a valid barangay: ");
        if(barangay.equals("Cancelled")){
            printInRed("Cancelled");
            return null;
        }
        if(currentAddress != null){
            System.out.println("[ADDRESS] Current city is `" + currentAddress.getCity() + "`");
        }
        System.out.print("[ADDRESS] Input city: ");
        String city = validator.validateText("[ADDRESS] Please input a valid city: ");
        if(city.equals("Cancelled")){
            printInRed("Cancelled");
            return null;
        }
        if(currentAddress != null){
            System.out.println("[ADDRESS] Current zip code is `" + currentAddress.getZipCode() + "`");
        }
        System.out.print("[ADDRESS] Input zip code: ");
        String zipCode = validator.validateZipCode("[ADDRESS] Please input a valid zip code: ");
        if(zipCode.equals("Cancelled")){
            printInRed("Cancelled");
            return null;
        }
        printInGreen("Address set to -> " + streetNumber + " " + barangay + " " + city + " " + zipCode);
        return new AddressDto(streetNumber, barangay, city, zipCode);
    }
    
    private Date getInputBirthday(PersonDto person){
        Date currentBirthday = person.getBirthday();
        Calendar calendar = Calendar.getInstance();
        if(currentBirthday != null){
            calendar.setTime(currentBirthday);
        }
        int currentYear = calendar.get(Calendar.YEAR);
        int currentMonth = calendar.get(Calendar.MONTH) + 1;
        int currentDay = calendar.get(Calendar.DAY_OF_MONTH);
        if(currentBirthday != null){
            System.out.println("[BIRTHDAY] Current year is `" + currentYear + "`");
        }
        System.out.print("[BIRTHDAY] Input year (1900 - 2016): ");
        String year = validator.validateYear("[BIRTHDAY] Please input a valid year (1900 - 2016): ");
        if(year.equals("Cancelled")){
            printInRed("Cancelled");
            return null;
        }
        if(currentBirthday != null){
            System.out.println("[BIRTHDAY] Current month is `" + currentMonth + "`");
        }
        System.out.print("[BIRTHDAY] Input month (1 - 12): ");
        String month = validator.validateMonth("[BIRTHDAY] Please input a valid month (1 - 12): ");
        if(month.equals("Cancelled")){
            printInRed("Cancelled");
            return null;
        }
        if(currentBirthday != null){
            System.out.println("[BIRTHDAY] Current day is `" + currentDay + "`");
        }
        System.out.print("[BIRTHDAY] Input day: ");
        String day = validator.validateDay(Integer.parseInt(month), Integer.parseInt(year), "[BIRTHDAY] Please input a valid day: ");
        if(day.equals("Cancelled")){
            printInRed("Cancelled");
            return null;
        }
        Date birthday = new GregorianCalendar(Integer.parseInt(year), Integer.parseInt(month) - 1, Integer.parseInt(day)).getTime();
        printInGreen("Birthday set to -> " + new SimpleDateFormat("MM/dd/yyyy").format(birthday));
        return birthday; 
    }
    
    private float getInputGwa(PersonDto person){
        System.out.println("[GWA] Current gwa is `" + person.getGwa() + "`");
        System.out.print("[GWA] Input gwa (50 - 100): ");
        String gwa = validator.validateGwa("[GWA] Please input a valid gwa (50 - 100): ");
        if(gwa.equals("Cancelled")){
            printInRed("Cancelled");
            return 0f;
        }
        printInGreen("Gwa set to -> " + gwa);
        return Float.parseFloat(gwa);  
    }
    
    private boolean getInputIsEmployed(PersonDto person){
        System.out.println("[EMPLOYMENT] Current employment status is `" + (person.isEmployed() ? "Employed" : "Not employed" ) + "`");
        System.out.print("[EMPLOYMENT] Input 'Y' if employed or 'N' if not: ");
        String isEmployed = validator.validateYesOrNo("[EMPLOYMENT] Please input 'Y' or 'N' only: ");
        if(isEmployed.equals("Cancelled")){
            printInRed("Cancelled");
            return false;
        }
        if(isEmployed.equalsIgnoreCase("N")){
            printInGreen("Employment set to -> Not employed");
        }
        return isEmployed.equalsIgnoreCase("Y");
    }
    
    private Date getInputDateHired(PersonDto person){
        Date currentDateHired = person.getDateHired();
        Calendar calendar = Calendar.getInstance();
        if(currentDateHired != null){
            calendar.setTime(currentDateHired);
        }
        int currentYear = calendar.get(Calendar.YEAR);
        int currentMonth = calendar.get(Calendar.MONTH) + 1;
        int currentDay = calendar.get(Calendar.DAY_OF_MONTH);
        if(currentDateHired != null){
            System.out.println("[EMPLOYMENT] Current year is `" + currentYear + "`");
        }
        System.out.print("[EMPLOYMENT] Input year (1900 - 2016): ");
        String year = validator.validateYear("[EMPLOYMENT] Please input a valid year (1900 - 2016): ");
        if(year.equals("Cancelled")){
            printInRed("Cancelled");
            return null;
        }
        if(currentDateHired != null){
            System.out.println("[EMPLOYMENT] Current month is `" + currentMonth + "`");
        }
        System.out.print("[EMPLOYMENT] Input month (1 - 12): ");
        String month = validator.validateMonth("[EMPLOYMENT] Please input a valid month (1 - 12): ");
        if(month.equals("Cancelled")){
            printInRed("Cancelled");
            return null;
        }
        if(currentDateHired != null){
            System.out.println("[EMPLOYMENT] Current day is `" + currentDay + "`");
        }
        System.out.print("[EMPLOYMENT] Input day: ");
        String day = validator.validateDay(Integer.parseInt(month), Integer.parseInt(year), "[EMPLOYMENT] Please input a valid day: ");
        if(day.equals("Cancelled")){
            printInRed("Cancelled");
            return null;
        }
        Date dateHired = new GregorianCalendar(Integer.parseInt(year), Integer.parseInt(month) - 1, Integer.parseInt(day)).getTime();
        printInGreen("Employment set to -> Employed " + new SimpleDateFormat("MM/dd/yyyy").format(dateHired));
        return dateHired;  
    }
    
    private Set<ContactDto> getInputContacts(PersonDto person){
        Set<ContactDto> contactSet = new HashSet<ContactDto>();
        contactSet.addAll(person.getContacts());
        ContactDto emailContact = null;
        ContactDto mobileContact = null;
        ContactDto landlineContact = null;
        for(ContactDto contact : contactSet){
            switch(contact.getContactType()){
                case Email:
                    emailContact = contact;
                    break;
                case Mobile:
                    mobileContact = contact;
                    break;
                case Landline:
                    landlineContact = contact;
                    break;
            }
        }
        boolean finish = false;
        while(!finish){
            System.out.println("\n------ CHOOSE THE CONTACT TO EDIT ------");
            System.out.println("[1] EMAIL            (Current: " + (!contactSet.contains(emailContact) ? "None" : emailContact.getValue()) + ")");
            System.out.println("[2] MOBILE NUMBER    (Current: " + (!contactSet.contains(mobileContact) ? "None" : mobileContact.getValue()) + ")");
            System.out.println("[3] LANDLINE NUMBER  (Current: " + (!contactSet.contains(landlineContact) ? "None" : landlineContact.getValue()) + ")");
            System.out.println("[4] OK\n");
            System.out.print("[CONTACTS] Input a number from 1 to 4: ");
            String option = validator.validateNumber(4, "[CONTACTS] Please input a number from 1 to 4 only: ");
            
            if(option.equals("Cancelled")){
                printInRed("Cancelled");
                return null;
            }
            
            switch(option){
                case "1":
                    System.out.print("[CONTACTS] Input 'Y' if email exists or 'N' if no: ");
                    String emailExists = validator.validateYesOrNo("[CONTACTS] Please input 'Y' or 'N' only: ");
                    if(emailExists.equals("Cancelled")){
                        printInRed("Cancelled");
                        break;
                    }
                    else if(emailExists.equalsIgnoreCase("Y")){
                        System.out.print("[CONTACTS] Input email: ");
                        String email = validator.validateEmail("[CONTACTS] Please input a valid email: ");
                        if(email.equals("Cancelled")){
                            printInRed("Cancelled");
                            break;
                        }
                        if(emailContact == null){
                            emailContact = new ContactDto(ContactType.Email, null);
                        }
                        emailContact.setValue(email);
                        contactSet.add(emailContact);
                    }
                    else{
                        contactSet.remove(emailContact);
                    }
                    break;
                case "2":
                    System.out.print("[CONTACTS] Input 'Y' if mobile number exists or 'N' if no: ");
                    String mobileNumberExists = validator.validateYesOrNo("[CONTACTS] Please input 'Y' or 'N' only: ");
                    if(mobileNumberExists.equals("Cancelled")){
                        printInRed("Cancelled");
                        break;
                    }
                    else if(mobileNumberExists.equalsIgnoreCase("Y")){
                        System.out.print("[CONTACTS] Input mobile number: ");
                        String mobileNumber = validator.validateMobileNumber("[CONTACTS] Please input a valid mobile number: ");
                        if(mobileNumber.equals("Cancelled")){
                            printInRed("Cancelled");
                            break;
                        }
                        if(mobileContact == null){
                            mobileContact = new ContactDto(ContactType.Mobile, null);
                        }
                        mobileContact.setValue(mobileNumber);
                        contactSet.add(mobileContact);
                    }
                    else{
                        contactSet.remove(mobileContact);
                    }
                    break;
                case "3":
                    System.out.print("[CONTACTS] Input 'Y' if landline number exists or 'N' if no: ");
                    String landlineNumberExists = validator.validateYesOrNo("[CONTACTS] Please input 'Y' or 'N' only: ");
                    if(landlineNumberExists.equals("Cancelled")){
                        printInRed("Cancelled");
                        break;
                    }
                    else if(landlineNumberExists.equalsIgnoreCase("Y")){
                        System.out.print("[CONTACTS] Input landline number: ");
                        String landlineNumber = validator.validateLandlineNumber("[CONTACTS] Please input a valid landline number: ");
                        if(landlineNumber.equals("Cancelled")){
                            printInRed("Cancelled");
                            break;
                        }
                        if(landlineContact == null){
                            landlineContact = new ContactDto(ContactType.Landline, null);
                        }
                        landlineContact.setValue(landlineNumber);
                        contactSet.add(landlineContact);
                    }
                    else{
                        contactSet.remove(landlineContact);
                    }
                    break;
                case "4":
                    Set<String> contactValueSet = contactSet.stream().map(c -> c.getValue()).collect(Collectors.toCollection(HashSet::new));
                    printInGreen("Contacts set to -> " + (contactValueSet.size() > 0 ? StringUtils.join(contactValueSet, ", ") : "None"));
                    finish = true;
                    break;
            }
        }
        return contactSet;  
    }
    
    private Gender getInputGender(PersonDto person){
        System.out.println("[GENDER] Current gender is `" + person.getGender() + "`");
        System.out.print("[GENDER] Input 'M' if male or 'F' if female: ");
        String gender = validator.validateChoice(new String[]{"M", "F"}, "[GENDER] Please input 'M' or 'F' only: ");
        if(gender.equals("Cancelled")){
            printInRed("Cancelled");
            return null;
        }
        printInGreen("Gender set to -> " + (gender.equalsIgnoreCase("M") ? "Male" : "Female"));
        return (gender.equalsIgnoreCase("M") ? Gender.Male : Gender.Female);
    }
    
    private Set<RoleDto> getInputRoles(PersonDto person){
        Set<RoleDto> roleSet = person.getRoles();
        List<RoleDto> roleList = roleService.getAll();
        int optionCount = roleList.size() + 1;
        boolean finish = false;
        while(!finish){
            System.out.println("\n------- LIST OF ROLES -------");
            roleList.stream().forEach(role -> System.out.println("[" + role.getId() + "] " + (roleSet.contains(role) ? "[x] " : "[ ] ") + role.getValue()));
            System.out.println("[" + optionCount + "] OK");
            System.out.print("[ROLE] Input a number from 1 to " + optionCount + ": ");
            String option = validator.validateNumber(optionCount, "[ROLE] Please input a number from 1 to " + optionCount + ": ");
            if(option.equals("Cancelled")){
                printInRed("Cancelled");
                return null;
            }
            if(Long.parseLong(option) < optionCount){
                RoleDto role = roleList.get(Integer.parseInt(option) - 1);
                if(roleSet.contains(role)){
                    roleSet.remove(role);
                }
                else{
                    roleSet.add(role);
                }
            }
            else{
                finish = true;
            }
        }
        Set<String> roleValueSet = roleSet.stream().map(r -> r.getValue()).collect(Collectors.toCollection(HashSet::new));
        printInGreen("Roles set to -> " + (roleValueSet.size() > 0 ? StringUtils.join(roleValueSet, ", ") : "None"));
        return roleSet;
    }
    
    private Sort getInputSort(){
        System.out.print("[LIST PERSON] Input 'G' to sort by GWA, 'D' by date hired, or 'L' by last name: ");
        String sortChoice = validator.validateChoice(new String[]{"G", "D", "L"}, "[LIST PERSON] Please input 'G', 'D', or 'L' only: ");
        if(sortChoice.equals("Cancelled")){
            printInRed("Cancelled");
            return null;
        }
        sortChoice = sortChoice.toUpperCase();
        Sort sort = null;
        switch(sortChoice){
            case "G":
                sort = Sort.GWA;
                break;
            case "D":
                sort = Sort.DateHired;
                break;
            case "L":
                sort = Sort.LastName;
                break;
        }
        return sort;
    }
    
    private boolean getInputIsAscending(){
        System.out.print("[LIST PERSON] Input 'A' to sort in ascending order or 'D' in descending order: ");
        String isAscending = validator.validateChoice(new String[]{"A", "D"}, "[LIST PERSON] Please input 'Y' or 'N' only: ");
        if(isAscending.equals("Cancelled")){
            printInRed("Cancelled");
            return false;
        }
        return isAscending.equalsIgnoreCase("A");
    }
    
    private boolean isValidPerson(PersonDto person){
        return person.getName() != null &&
               person.getAddress() != null &&
               person.getBirthday() != null;
    }
    
    private void printInRed(String text){
         System.out.println("\n" + RED_COLOR + text + WHITE_COLOR);
    }
    
    private void printInGreen(String text){
         System.out.println("\n" + GREEN_COLOR + text + WHITE_COLOR);
    }
    
    public void exit(){
        System.exit(0);
    }
}
