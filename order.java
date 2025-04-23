import java.util.HashMap;
import java.util.Map;

public class order{
	/*
	 * An ORDER object will contain the company name, individual restaurant name, and
amounts of how many of each product is ordered. Any products not mentioned in the
line should be considered to be product orders of zero.
o Your ORDER class must include all methods necessary to support the requests
made in the database
	 */
	String company;
	String restname;
	Map <String, Integer> product; //holds the item ordered and the quantity
	
	
	public order(String company, String restname, Map <String, Integer> product) {
		this.company = company;
		this.restname = restname;
		this.product = new HashMap();
		
	}
	
	
	public String getCompany() {
		return company;
	}
	public void setCompany(String company) {
		this.company = company;
	}
	public String getRestname() {
		return restname;
	}
	public void setRestname(String restname) {
		this.restname = restname;
	}
	public Map<String, Integer> getProduct() {
		return product;
	}
	public void setProduct(Map<String, Integer> product) {
		this.product = product;
	}


	@Override
	public String toString() {
		return company + " " + restname + " " + product.toString();
	}
	//this method checks for duplicate orders
	public boolean compareTo(order ord) {
		if(ord.getCompany().equals(company) && ord.getRestname().equals(restname) && ord.getProduct().equals(product)) {
			return true;
			
		}
		return false;
	}
	
}
