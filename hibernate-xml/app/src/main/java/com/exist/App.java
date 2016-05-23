package com.exist.app;

public class App{
    private Menu menu = new Menu();
    
    public static void main(String[] args){
        new App().run();
    }
    
    private void run(){
        if(menu.displayDefaultList){
            menu.listPersonBy(null);
        }
        
        System.out.println(
                "\nMenu:\n\n" + 
                "1 - ADD PERSON\n" + 
                "2 - DELETE PERSON\n" + 
                "3 - UPDATE PERSON\n" + 
                "4 - LIST PERSON\n" + 
                "5 - UPDATE CONTACT\n" + 
                "6 - DELETE CONTACT\n" + 
                "7 - EXIT\n");
        
        int option = menu.getValidator().validateNumber(1, 7, "Input a number from 1 to 7: ", "Please input a number from 1 to 7 only: ");
        System.out.println();
        
        switch(option){
            case 1:
                menu.addPerson();
                break;
            case 2:
                menu.deletePerson();
                break;
            case 3:
                menu.updatePerson();
                break;
            case 4:
                menu.listPerson();
                break;
            case 5:
                menu.updateContact();
                break;
            case 6:
                menu.deleteContact();
                break;
            case 7:
                System.exit(0);
                break;
            default:
                break;
        }
        run();
    } 
}

