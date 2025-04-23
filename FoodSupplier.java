import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.*;


public class FoodSupplier {

	private Map<Date, LinkedList<order>> orderMap; // Holds a Date and all the orders placed that date
	private Set<String> listOfCompanies; // Holds every company name
	
	public FoodSupplier(Map<Date, LinkedList<order>> orderMap, Set<String> listOfCompanies) {
		this.orderMap = orderMap; // use the passed-in map
		this.listOfCompanies = listOfCompanies;
		for(Date d : orderMap.keySet()) { // iterates day by day and returns a linked list
			for(order obj : orderMap.get(d)) { //iterates the linked list and returns an order object
				for( String s : obj.getProduct().keySet()) { //iterates the map in an order object
					if(!listOfCompanies.contains(s)) {
						listOfCompanies.add(s); //adds the company to the Set
					}
				}
			}
		} 
		
	
		
}
	
	public void populateMap(String line) throws Exception {
	    String[] arr = line.split(" ");
	    SimpleDateFormat formatter = new SimpleDateFormat("MM-dd-yyyy");

	   
	    java.util.Date utilDate = formatter.parse(arr[0]);
	    java.sql.Date date = new java.sql.Date(utilDate.getTime());

	    String company = arr[1].substring(0, arr[1].indexOf("#"));
	    String restname = arr[1];

	    Map<String, Integer> food = new HashMap<>();
	    for (int i = 2; i < arr.length; i++) {
	    	String item = arr[i].substring(0, arr[i].indexOf("-")).toLowerCase();
	    	int quantity = Integer.parseInt(arr[i].substring(arr[i].indexOf("-") + 1));
	    	food.put(item, food.getOrDefault(item, 0) + quantity);

	    }

	    order ord = new order(company, restname, food);

	    orderMap.putIfAbsent(date, new LinkedList<>());
	    orderMap.get(date).add(ord);
	}

	
	/**
	 * Returns the date on which the given product had its highest total orders.
	 * Product matching is case-insensitive.
	 */
	public Date mostOnADate(String product) {
	    String prod = product.toLowerCase();      // normalize user input
	    Date bestDate = null;
	    int bestTotal = -1;                       // so even days with 0 count win initially

	    for (Date d : orderMap.keySet()) {
	        int dayTotal = 0;
	        for (order o : orderMap.get(d)) {
	            // getOrDefault(…,0) handles missing keys
	            dayTotal += o.getProduct().getOrDefault(prod, 0);
	        }
	        // debug print — remove once you verify it works
	        System.out.println(new SimpleDateFormat("MM-dd-yyyy").format(d)
	                           + " → " + dayTotal);

	        if (dayTotal > bestTotal) {
	            bestTotal = dayTotal;
	            bestDate  = d;
	        }
	    }

	    return bestDate;
	}


	/*
	 * lets first populate the Map by iterating day by day.
	 * take the order object and add the product to the map if it doesn't exist
	 * after increase the quantity of the product in the map
	 * iterate the Map food and find the largest integer and return its key
	 */
	public String mostOrderedBurgers(String product) {
	    Map<String, Integer> companyOrders = new HashMap<>();
	    for (Date d : orderMap.keySet()) {
	        for (order obj : orderMap.get(d)) {
	            int qty = obj.getProduct().getOrDefault(product, 0);
	            companyOrders.put(obj.getCompany(), companyOrders.getOrDefault(obj.getCompany(), 0) + qty);
	        }
	    }
	    String topCompany = "";
	    int max = 0;
	    for (String comp : companyOrders.keySet()) {
	        if (companyOrders.get(comp) > max) {
	            max = companyOrders.get(comp);
	            topCompany = comp;
	        }
	    }
	    return topCompany;
	}

	
	
	/*
	 * iterate the dates
	 * check to see if a company exists 
	 * if not daysBetween++
	 * arr indicie 1 = arr indicie 0
	 * if company exists days between = 0
	 * take the time map and see which company has the largest integer in indicie 1
	 */
	

	public String mostProductInYear(String product, int year) {
		Map<String, Integer> map = new HashMap<>();
		for (Date d : orderMap.keySet()) {
			Calendar cal = Calendar.getInstance(); cal.setTime(d);
			if (cal.get(Calendar.YEAR) == year) {
				for (order o : orderMap.get(d)) {
					int qty = o.getProduct().getOrDefault(product, 0);
					map.put(o.getCompany(), map.getOrDefault(o.getCompany(), 0) + qty);
				}
			}
		}
		return map.entrySet().stream().max(Map.Entry.comparingByValue()).map(Map.Entry::getKey).orElse("None");
	}

	public Set<order> ordersOnDate(Date date) {
		return new HashSet<>(orderMap.getOrDefault(date, new LinkedList<>()));
	}

	public Set<order> ordersInMonth(int month) {
		Set<order> result = new HashSet<>();
		Calendar cal = Calendar.getInstance();
		for (Date d : orderMap.keySet()) {
			cal.setTime(d);
			if (cal.get(Calendar.MONTH) + 1 == month) {
				result.addAll(orderMap.get(d));
			}
		}
		return result;
	}

	public Set<order> duplicateOrdersOnDate(Date date) {
		Set<order> result = new HashSet<>();
		LinkedList<order> orders = orderMap.getOrDefault(date, new LinkedList<>());
		for (int i = 0; i < orders.size(); i++) {
			for (int j = i + 1; j < orders.size(); j++) {
				if (orders.get(i).compareTo(orders.get(j))) result.add(orders.get(i));
			}
		}
		return result;
	}

	public Set<String> companiesAtLeastXInYear(String product, int qty, int year) {
		Map<String, Integer> tracker = new HashMap<>();
		Calendar cal = Calendar.getInstance();
		for (Date d : orderMap.keySet()) {
			cal.setTime(d);
			if (cal.get(Calendar.YEAR) == year) {
				for (order o : orderMap.get(d)) {
					int amount = o.getProduct().getOrDefault(product, 0);
					tracker.put(o.getCompany(), tracker.getOrDefault(o.getCompany(), 0) + amount);
				}
			}
		}
		Set<String> result = new HashSet<>();
		for (String c : tracker.keySet()) if (tracker.get(c) >= qty) result.add(c);
		return result;
	}

	public Set<order> ordersInRangeForCompany(String company, Date start, Date end) {
		Set<order> result = new HashSet<>();
		for (Date d : orderMap.keySet()) {
			if (!d.before(start) && !d.after(end)) {
				for (order o : orderMap.get(d)) if (o.getCompany().equals(company)) result.add(o);
			}
		}
		return result;
	}

	public Map<String, Integer> companySummaryBetweenDates(String company, Date start, Date end) {
		Map<String, Integer> summary = new HashMap<>();
		for (Date d : orderMap.keySet()) {
			if (!d.before(start) && !d.after(end)) {
				for (order o : orderMap.get(d)) {
					if (o.getCompany().equals(company)) {
						for (String prod : o.getProduct().keySet()) {
							summary.put(prod, summary.getOrDefault(prod, 0) + o.getProduct().get(prod));
						}
					}
				}
			}
		}
		return summary;
	}

	public int totalProductBetween(String product, Date start, Date end) {
		int total = 0;
		for (Date d : orderMap.keySet()) {
			if (!d.before(start) && !d.after(end)) {
				for (order o : orderMap.get(d)) {
					total += o.getProduct().getOrDefault(product, 0);
				}
			}
		}
		return total;
	}

	public String mostDaysBetweenOrders() {
		Map<String, List<Date>> companyDates = new HashMap<>();
		for (Date d : orderMap.keySet()) {
			for (order o : orderMap.get(d)) {
				companyDates.putIfAbsent(o.getRestname(), new ArrayList<>());
				companyDates.get(o.getRestname()).add(d);
			}
		}
		String longest = "";
		long maxDiff = 0;
		for (String store : companyDates.keySet()) {
			List<Date> dates = companyDates.get(store);
			dates.sort(Comparator.naturalOrder());
			for (int i = 1; i < dates.size(); i++) {
				long diff = Math.abs(dates.get(i).getTime() - dates.get(i - 1).getTime());
				if (diff > maxDiff) {
					maxDiff = diff;
					longest = store;
				}
			}
		}
		return longest;
	}

}





















