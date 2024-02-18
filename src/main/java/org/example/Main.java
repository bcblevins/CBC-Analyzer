package org.example;

public class Main {
    public static IOSystem iOSys = new IOSystem();
    public static Analyzer analyzer = new Analyzer();

    public static void main(String[] args) {

        while (true) {
            String choice = iOSys.displayMenu("Analyze blood values", "Search old tests", "Quit");
            if (choice.equals("1")) {
                analyzer.analyzeNewValues();
            } else if (choice.equals("2")) {
                searchMain();
            } else if (choice.equals("3")) {
                break;
            }
        }
    }


    public static void searchMain(){
        while (true) {
            String searchMethod = iOSys.displayMenu("Search by name", "Search by date", "Go back to main menu");
            if (searchMethod.equals("1")) {
                System.out.println(iOSys.searchLog(iOSys.SEARCH_BY_NAME));
            } else if (searchMethod.equals("2")){
                System.out.println(iOSys.searchLog(iOSys.SEARCH_BY_DATE));
            } else if (searchMethod.equals("3")) {
                return;
            } else {
                System.out.println("invalid selection");
            }
        }
    }

}