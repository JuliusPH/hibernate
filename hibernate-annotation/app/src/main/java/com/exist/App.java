package com.exist.app;

public class App{
    Validator validator = new Validator();
    Menu menu = new Menu(validator);
    public static void main(String[] args){
        new App().run();
    }
    
    private void run(){
        menu.listPersons(null, true);
        System.out.println(
                "\n------ MENU ------\n" + 
                "[1] ADD PERSON\n" + 
                "[2] DELETE PERSON\n" + 
                "[3] UPDATE PERSON\n" + 
                "[4] LIST PERSON\n" + 
                "[5] UPDATE CONTACT\n" + 
                "[6] DELETE CONTACT\n" + 
                "[7] EXIT\n" + 
                "------------------");
                
        System.out.print("Input a number from 1 to 7: ");
        String option = validator.validateNumber(7, "Please input a number from 1 to 7 only: ");
        System.out.println();
        
        if(option.equals("Cancelled")){
            menu.exit();
        }
        
        switch(option){
            case "1":
                printWithSeparator("ENTER ADD PERSON MODE");
                menu.addPerson();
                printWithSeparator("EXIT ADD PERSON MODE");
                break;
            case "2":
                printWithSeparator("ENTER DELETE PERSON MODE");
                menu.deletePerson();
                printWithSeparator("EXIT DELETE PERSON MODE");
                break;
            case "3":
                printWithSeparator("ENTER UPDATE PERSON MODE");
                menu.updatePerson();
                printWithSeparator("EXIT UPDATE PERSON MODE");
                break;
            case "4":
                printWithSeparator("ENTER LIST PERSON MODE");
                menu.listPersons();
                printWithSeparator("EXIT LIST PERSON MODE");
                break;
            case "5":
                printWithSeparator("ENTER UPDATE PERSON CONTACT MODE");
                menu.updateContacts();
                printWithSeparator("EXIT UPDATE PERSON CONTACT MODE");
                break;
            case "6":
                printWithSeparator("ENTER DELETE PERSON CONTACT MODE");
                menu.deleteContacts();
                printWithSeparator("EXIT DELETE PERSON CONTACT MODE");
                break;
            case "7":
                menu.exit();
                break;
            default:
                break;
        }
        run();
    }
    
    public void printWithSeparator(String text){
        System.out.println("------------------------------------------");
        System.out.println(text);
        System.out.println("------------------------------------------");
    }
}

