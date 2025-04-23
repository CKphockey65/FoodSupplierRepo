import java.io.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.sql.Date;

public class FoodSupplierRunner {

    private static final String ORDER_FILE = "supplyOrders.dat";
    private static FoodSupplier supplier = new FoodSupplier(new HashMap<>(), new TreeSet<>());
    private static final Scanner input = new Scanner(System.in);

    public static void main(String[] args) throws Exception {
        System.out.println("\n--- FOOD SUPPLIER SYSTEM ---\n");
        loadOrdersFromFile();

        int choice;
        do {
            System.out.println("\nSelect an option:");
            System.out.println("1. What date had the most orders of a product?");
            System.out.println("2. What company ordered the most of a product?");
            System.out.println("3. Store with longest gap between orders?");
            System.out.println("4. Company that ordered most of a product in a year?");
            System.out.println("5. Get all orders on a given day or month?");
            System.out.println("6. Find duplicate orders on 01-09-2010?");
            System.out.println("7. Companies that ordered at least X of a product in a year?");
            System.out.println("8. Orders for a company in a date interval?");
            System.out.println("9. Summary/total for a company in a date interval?");
            System.out.println("10. Total amount of a product in a date interval?");
            System.out.println("0. Exit");
            System.out.print("Choice: ");
            choice = input.nextInt();
            input.nextLine();

            switch (choice) {
                case 1 -> {
                    System.out.print("Enter product name: ");
                    String product = input.nextLine();
                    Date result = supplier.mostOnADate(product);
                    System.out.println("Date: " + result);
                }
                case 2 -> {
                    System.out.print("Enter product name: ");
                    String product = input.nextLine();
                    String result = supplier.mostOrderedBurgers(product);
                    System.out.println("Company: " + result);
                }
                case 3 -> {
                    String result = supplier.mostDaysBetweenOrders();
                    System.out.println("Store with longest gap: " + result);
                }
                case 4 -> {
                    System.out.print("Enter product: ");
                    String product = input.nextLine();
                    System.out.print("Enter year (yyyy): ");
                    int year = input.nextInt();
                    input.nextLine();
                    String result = supplier.mostProductInYear(product, year);
                    System.out.println("Company: " + result);
                }
                case 5 -> {
                    System.out.print("Enter date (MM-dd-yyyy) or month (MM): ");
                    String query = input.nextLine();
                    Set<order> orders;
                    if (query.contains("-")) {
                        orders = supplier.ordersOnDate(toSqlDate(query));
                    } else {
                        orders = supplier.ordersInMonth(Integer.parseInt(query));
                    }
                    System.out.println("Orders: " + orders);
                }
                case 6 -> {
                    Set<order> duplicates = supplier.duplicateOrdersOnDate(toSqlDate("01-09-2010"));
                    System.out.println("Duplicate Orders: " + duplicates);
                }
                case 7 -> {
                    System.out.print("Product: ");
                    String product = input.nextLine();
                    System.out.print("Minimum quantity: ");
                    int qty = input.nextInt();
                    System.out.print("Year: ");
                    int year = input.nextInt();
                    input.nextLine();
                    Set<String> result = supplier.companiesAtLeastXInYear(product, qty, year);
                    System.out.println("Companies: " + result);
                }
                case 8 -> {
                    System.out.print("Company: ");
                    String company = input.nextLine();
                    System.out.print("Start Date (MM-dd-yyyy): ");
                    Date start = toSqlDate(input.nextLine());
                    System.out.print("End Date (MM-dd-yyyy): ");
                    Date end = toSqlDate(input.nextLine());
                    Set<order> orders = supplier.ordersInRangeForCompany(company, start, end);
                    System.out.println("Orders: " + orders);
                }
                case 9 -> {
                    System.out.print("Company: ");
                    String company = input.nextLine();
                    System.out.print("Start Date (MM-dd-yyyy): ");
                    Date start = toSqlDate(input.nextLine());
                    System.out.print("End Date (MM-dd-yyyy): ");
                    Date end = toSqlDate(input.nextLine());
                    Map<String, Integer> summary = supplier.companySummaryBetweenDates(company, start, end);
                    System.out.println("Summary: " + summary);
                }
                case 10 -> {
                    System.out.print("Product: ");
                    String product = input.nextLine();
                    System.out.print("Start Date (MM-dd-yyyy): ");
                    Date start = toSqlDate(input.nextLine());
                    System.out.print("End Date (MM-dd-yyyy): ");
                    Date end = toSqlDate(input.nextLine());
                    int total = supplier.totalProductBetween(product, start, end);
                    System.out.println("Total Ordered: " + total);
                }
                case 0 -> System.out.println("Goodbye!");
                default -> System.out.println("Invalid choice.");
            }

        } while (choice != 0);
    }

    private static void loadOrdersFromFile() throws Exception {
        BufferedReader br = new BufferedReader(new FileReader(ORDER_FILE));
        int numLines = Integer.parseInt(br.readLine());
        String line;
        while ((line = br.readLine()) != null) {
            supplier.populateMap(line);
        }
        br.close();
        System.out.println("Loaded " + numLines + " orders from file.");
    }

    private static Date toSqlDate(String s) {
        try {
            java.util.Date parsed = new SimpleDateFormat("MM-dd-yyyy").parse(s);
            return new java.sql.Date(parsed.getTime());
        } catch (ParseException e) {
            throw new RuntimeException("Invalid date format. Use MM-dd-yyyy.");
        }
    }
}
