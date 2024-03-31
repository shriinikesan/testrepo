import java.io.*;
import java.util.*;
import java.lang.Math;
import java.util.regex.*;

enum Status{

	PENDING,
	ORDERED,
	DELIVERING,
	DELIVERED,
	CANCELLED
}

abstract class FoodItem implements Serializable {

    	private String name;
    	private double price;

    	public FoodItem(String name, double price)
	{
        	this.name = name;
        	this.price = price;
    	}

    	public abstract String getItemDetails();

    	public String getName()
	{
        	return name;
    	}

    	public double getPrice() 
	{
        	return price;
    	}
}

class Burger extends FoodItem {

	public Burger(String name, double price) 
	{
        	super(name, price);
    	}

    	public String getItemDetails() 
	{
        	return "Burger: " + getName() + " \t Rs." + getPrice();
    	}
}

class Pizza extends FoodItem {

    	public Pizza(String name, double price) 
	{
        	super(name, price);
    	}

    	public String getItemDetails()
	{
        	return "Pizza: " + getName() + " \t Rs" + getPrice();
    	}
}

class OtherFoodItem extends FoodItem{

	public OtherFoodItem(String name, double price) 
	{
        	super(name, price);
    	}

    	public String getItemDetails()
	{
        	return "Item: " + getName() + " \t Rs" + getPrice();
    	}
}

interface Order {
    
	void addToCart(FoodItem item, int quantity , Restaurant restaurant);
    	void removeFromCart(FoodItem item, int quantity);
    	double calculateTotal();
}


class FoodOrder implements Order, Serializable {

	private static final long serialVersionUID = 1L;
	private int id;
    	private HashMap<FoodItem, Integer> cart;
	private Customer customer;
	private Restaurant restaurant;
	private Date date;
	private double startCoordinates[];
	private double endCoordinates[];
	private float amount;
	private float deliveryCharge ;
	private Status status;
	
	public Status getStatus()
	{
		return this.status;
	}
	public void setStatus( Status status)
	{
		this.status = status;
	}
	public int getId()
	{
		return id;
	}
	public void setId()
	{
		this.id=id;
	}
	public void setCustomer(Customer customer)
	{
		this.customer=customer;
	}

    	public FoodOrder(int id) 
	{
		this.setId(id);
        	this.cart = new HashMap<>();
		this.status = Status.PENDING;
    	}

    	public void addToCart(FoodItem item, int quantity , Restaurant restaurant) 
	{
		this.restaurant = restaurant;
        	cart.put(item, cart.getOrDefault(item, 0) + quantity);
    	}

    	public void removeFromCart(FoodItem item, int quantity)
	{
        	int currentQuantity = cart.getOrDefault(item, 0);
        	if (currentQuantity > quantity)
		{
           		cart.put(item, currentQuantity - quantity);
        	}
		else
		{
          		cart.remove(item);
        	}
    	}

   	public double calculateTotal()
	{
        	double total = 0;
        	for ( FoodItem entry : cart.keySet() )
		{
            		total += entry.getPrice() * cart.getValue(entry);
        	}
        	return total;
    	}

	public double deliveryDistance()
	{
		return Math.sqrt(Math.pow(startCoordinates[0]-endCoordinates[0],2) + Math.pow(startCoordinates[1]-endCoordinates[1],2));
	}

    	public void placeOrder(DeliveryPerson deliveryPerson , SystemAdmin admin)
	{
        	System.out.println("Order placed successfully!");
		this.date = new Date();
		this.startCoordinates = restaurant.getCoordinates();
		this.endCoordinates = customer.getCoordinates();
		this.deliveryCharge = this.deliveryDistance() * 100;
		this.amount = this.deliveryCharge + this.calculateTotal();
		this.viewOrder();
		this.status = Status.ORDERED;
		for(Restaurant i : admin.getRestaurants())
		{
			if(i.equals(this.restaurant))
			{
				i.addOrder(this);
			}
		}
		for(Customer i : admin.getCustomers())
		{
			if(i.equals(this.customer))
			{
				i.addOrder(this);
			}
		}
		for(DeliveryPerson i : admin.getDeliveryPeople())
		{
			if(i.equals(deliveryPerson))
			{
				deliveryperson.addDelivery(this);
			}
		}		
    	}
	public void viewOrder()
	{
		
		System.out.println("Order details :" );
		System.out.println("Customer name :" +  this.customer.getName());
		System.out.println(" Date and time of order : " +  this.date + " \t Status : " + this.status);
		System.out.println(" Item name \t Item cost \t Item Quantity \t");
		for( Map<FoodItem, Integer> entry : cart.entrySet())
		{
			System.out.println(entry.getKey().getItemDetails() + "\t" + entry.getValue());
		}
		System.out.println("\t\t\t DeliveryCharges : " + this.deliveryCharge + "\n" );
		System.out.println("\t\t\t\t Total : " + this.amount() );
	}

	public double[] getStartCoordinates()
	{
        	return startCoordinates;
    	}
	public double[] getEndCoordinates()
	{
        	return endCoordinates;
    	}

}


class Customer implements Serializable {

	private static final long serialVersionUID = 1L;
    	private String name;
    	private String address;
    	private double[] coordinates;
	private ArrayList<FoodOrder> history;
	boolean login;
	
	public void ChangeOrderStatus( FoodOrder order , Status status)
	{
		for(FoodOrder i : this.history)
		{
			if(i.equals(order))
			{
				i.setStatus(status);
			}
		}
	}
	private int id;
	public int getId()
	{
		return id;
	}
	public void setId()
	{
		this.id=id;
	}

    	public Customer(int id, String name, String address, double[] coordinates)
	{
		this.setId(id);
        	this.name = name;
        	this.address = address;
        	this.coordinates = coordinates;
        	this.history= new ArrayList<>();
		login = false;
    	}

     	public String getName()
	{
        	return name;
    	}

    	public void setName(String name)
	{
        	this.name = name;
    	}

    	public String getAddress()
	{
        	return address;
    	}

    	public void setAddress(String address)
	{
        	this.address = address;
    	}

    	public double[] getCoordinates()
	{
        	return coordinates;
    	}

    	public void setCoordinates(double[] coordinates)
	{
        	this.coordinates = coordinates;
    	}

    	public ArrayList<FoodOrder> getOrderHistory()
	{
        	return this.hitory;
    	}

    	public void addOrder(FoodOrder order)
	{
		if(order.customer.equals(this) )
        	{	history.add(order);	}
	}

	public void getOrderStatus(Scanner scanner)
	{
		do{
		System.out.println("Check order history/ orders yet to be delivered / cancelled orders ");
		System.out.println("1.History of orders: ");
		System.out.println("2.Current orders to be delivered: ");
		System.out.println("3.Cancelled orders: ");
		System.out.println("4.Go back to previous menu");
		int choice1 = scanner.nextInt();
		switch(choice1)
		{
			case 1 :
				System.out.println("Orders History : ");
				for(FoodOrder i : history)
				{
					i.viewOrder();
				}
				break;
			case 2:
				System.out.println("Orders Pending/Yet to be delivered : ");
				for(FoodOrder i : history)
				{
					if(i.getStatus() == Status.ORDERED || i.getStatus() == Status.DELIVERING)
					{	i.viewOrder();	}
				}
			case 3:
				System.out.println("CAncelled Orders : ");
				for(FoodOrder i : history)
				{
					if(i.getStatus() == Status.CANCELLED)
					{	i.viewOrder();	}
				}
				break;
			case 4:
				break;
			default:
				System.out.println("Enter valid option");
				break;
		}
		}while(choice1 != 4);
	}
}


class Restaurant implements Serializable {

    	private static final long serialVersionUID = 1L;
    	private String name;
    	private String location;
    	private double[] coordinates; 
    	private LinkedList<FoodItem> menu;
	private ArrayList<FoodOrder> History;

	private int id;
	public int getId()
	{
		return id;
	}
	public void setId()
	{
		this.id=id;
	}

    	public Restaurant(int id, String name, String location, double[] coordinates)
	{
		this.setId(id);
        	this.name = name;
        	this.location = location;
        	this.coordinates = coordinates;
        	this.menu = new LinkedList<>();
    	}
	
	public String getName()
	{
        	return name;
    	}

    	public void setName(String name)
	{
        	this.name = name;
    	}

    	public String getLocation()
	{
        	return location;
    	}

    	public void setLocation(String location)
	{
        	this.location = location;
    	}

    	public double[] getCoordinates()
	{
        	return coordinates;
    	}

    	public void setCoordinates(double[] coordinates)
	{
        	this.coordinates = coordinates;
    	}

    	public LinkedList<FoodItem> getMenu()
	{
        	return menu;
    	}

    	public void setMenu(LinkedList<FoodItem> menu)
	{
        	this.menu.add();
    	}
	
	public void addMenu(FoodItem item)
	{
        	this.menu.add(item);
    	}
	public void addOrder(FoodOrder order)
	{
		if(order.customer.equals(this) )
        	{	history.add(order);	}
	}
}

class DeliveryPerson extends Thread implements Serializable {

    	private static final long serialVersionUID = 1L;
    	private String name;
    	private double[] coordinates;
    	private LinkedList<FoodOrder> ordersToDeliver;
	boolean currentEmployee ;
	private ArrayList<FoodOrder> history;

	private int id;
	public int getId()
	{
		return id;
	}
	public void setId()
	{
		this.id=id;
	}
    	public DeliveryPerson(int id ,String name, double[] coordinates)
	{
		this.setId(id);
        	this.name = name;
        	this.coordinates = coordinates;
        	this.ordersToDeliver = new LinkedList<>();
		this.currentEmployee = false;
    	}
	public void addDelivery(FoodOrder foodOrder)
	{
		this.ordersToDeliver.add(foodOrder);
	}
	
    	public String getName()
	{
        	return name;
    	}

    	public void setName(String name)
	{
        	this.name = name;
    	}

    	public double[] getCoordinates()
	{
        	return coordinates;
    	}

    	public void setCoordinates(double[] coordinates)
	{
        	this.coordinates = coordinates;
    	}

    	public LinkedList<FoodOrder> getOrdersToDeliver()
	{
       		return ordersToDeliver;
    	}

    	public void setOrdersToDeliver(LinkedList<FoodOrder> ordersToDeliver)
	{
        	this.ordersToDeliver = ordersToDeliver;
    	}

	public void run()
	{

		if(this.currentEmployee = true)
		while(!this.ordersToDeliver.isEmpty())
		{
			try
			{
			    synchronized (this.ordersToDeliver)
			    {
				double start[] = this.ordersToDeliver.first().getStartCoordinates();
				double distance = Math.sqrt(Math.pow(this.coordinates[0]-start[0],2) + Math.pow(this.coordinates[1]-start[1],2));
				Thread.currentThread().sleep((int)(distance * 100));
				System.out.println(this.getName() + "- id: " + this.getId() + "Has arrived to restaurant: " + this.orderstoDeliver.first().restaurant.getName() );
				Thread.currentThread().sleep(5000);
				System.out.println(this.getName() + "- id: " + this.getId() + "Has received your order");
				if(this.ordersToDeliver.first().getStatus() == Status.CANCELLED)
				{
					this.coordinates = start;
					double end[] = this.ordersToDeliver.first().getEndCoordinates();
					distance = this.ordersToDeliver.first().deliveryDistance();
					Thread.currentThread.sleep((int)(distance * 100));
					System.out.println(this.getName()+ "- id: " + this.getId() + "Has delivered " + this.ordersToDeliver.first().customer.getName() + "'s order");
					Thread.currentThread().sleep(1000);
					System.out.println(this.getName() + "- id: " + this.getId() + "Has Successfully Delivered");
					this.history.add(this.ordersToDeliver.first());
				}
			    }
			}
			catch(InterruptedException e)
			{	
				System.out.println("error : some interruption has been occured");
			}
		}
	}
}


class SystemAdmin {

	private static SystemAdmin instance;
    	private LinkedList<Customer> customers;
    	private LinkedList<Restaurant> restaurants;
    	private LinkedList<DeliveryPerson> deliveryPeople;
	private LinkedList<Restaurant> queueRestaurants;
    	private LinkedList<DeliveryPerson> queueDeliveryPeople;
	private LinkedList<FoodOrder> orders;
	static Scanner scanner = new Scanner(System.in);
	int countCustomerId;
	int countRestaurantId;
	int countDeliveryPersonId;
	int countOrderId;
	private char [] password = {'a','d','m','i','n','0','1'};

    	private SystemAdmin()
	{
        	customers = loadCustomers("Customers");
        	restaurants = loadRestaurant("Restaurants");
        	deliveryPeople = loadDeliveryPeople("DeliveryPeople");
		queueRestaurants = loadRestaurants("QueueRestaurants");
		queueDeliveryPeople = loadDeliveryPeople("QueueDeliveryPeople");
		orders = loadOrders("Orders");
		countCustomerId = customers.size();
		countRestaurantId = restaurants.size() + queueRestaurants.size();
		countDeliveryPersonId = deliveryPeople.size() + queueDeliveryPeople.size();
		countOrderId = orders.size();
    	}

    	public static SystemAdmin getInstance()
	{
        	if (instance == null)
		{
            		instance = new SystemAdmin();
        	}
        	return instance;
    	}

    	public void addCustomer(Customer customer)
	{
        	customers.add(customer);
    	}

    	public void addRestaurant(Restaurant restaurant)
	{
        	queueRestaurants.add(restaurant);
    	}

    	public void addDeliveryPerson(DeliveryPerson deliveryPerson)
	{
       		queueDeliveryPeople.add(deliveryPerson);
    	}

	public boolean checkPassword(String check)
	{
		if(this.password.equals(check))
		{	return true;	}
		else
		{	return false;	}
	}

	public void employRestaurant()
	{
		System.out.println("Restaurants currently under requests:");
		for(Restaurant i : queueRestaurants)
		{
			System.out.println((queueRestaurants.indexOf(i)+1)  + ". " + i.getName() + " \t" + i.getLocation());
		}
		System.out.println("select restaurants to add. if finished selecting, press -1: ");
		do{
			
			int select = scanner.nextInt();
			{
				if(select > 0 && select <= queueRestaurants.size())
				{
					restaurants.add(queueRestaurants.get(select-1));
					queueRestaurants.remove(select-1);
				}
				else
				{
					if(select != -1)
					{	System.out.println("Enter valid restaurant index");	}
				}
			}
		}while(select != -1);
		
    	}

    	public void employDeliveryPerson()
	{
		
       		System.out.println("Delivery employees currently under requests:");
		for(DeliveryPerson i : queueDeliveryPeople)
		{
			System.out.println((queueDeliveryPeople.indexOf(i)+1)  + ". " + i.getName() );
		}
		System.out.println("select applicants to be employed. if finished selecting, press -1: ");
		do{
			int select = scanner.nextInt();
			{
				if(select > 0 && select <= queueDeliveryPeople.size())
				{
					queueDeliveryPeople.get(select-1).currentEmployee = true;
					deliverypeople.add(queueDeliveryPeople.get(select-1));
					queueDeliveyPeople.remove(select-1);
				}
				else
				{
					if(select != -1)
					{	System.out.println("Enter valid Applicant index to add");	}
				}
			}
		}while(select != -1);
		
    	}

	public void deleteRestaurant()
	{
		System.out.println("Restaurants under system:");
		for(Restaurant i : restaurants)
		{
			System.out.println((restaurants.indexOf(i)+1)  + ". " + i.getName() + " \t" + i.getLocation());
		}
		System.out.println("select restaurants to remove. if finished selecting, press -1: ");
		do{
			
			int select = scanner.nextInt();
			{
				if(select > 0 && select <= restaurants.size())
				{
					restaurants.remove(select-1);
				}
				else
				{
					if(select != -1)
					{	System.out.println("Enter valid restaurant index");	}
				}
			}
		}while(select != -1);
		
    	}

    	public void deleteDeliveryPerson()
	{
		
       		System.out.println("Delivery employees currently under work");
		for(DeliveryPerson i : deliveryPeople)
		{
			System.out.println((deliveryPeople.indexOf(i)+1)  + ". " + i.getName() );
		}
		System.out.println("select Employees to be removed. if finished selecting, press -1: ");
		do{
			int select = scanner.nextInt();
			{
				if(select > 0 && select <= deliveryPeople.size())
				{
					queueDeliveryPeople.get(select-1).currentEmployee = true;
					deliverypeople.add(queueDeliveryPeople.get(select-1));
					queueDeliveyPeople.remove(select-1);
				}
				else
				{
					if(select != -1)
					{	System.out.println("Enter valid Employee index to be removed");	}
				}
			}
		}while(select != -1);
		
    	}	


	public void saveAll()
	{
		this.saveRestaurant(restaurants,"Restaurants");
		this.saveCustomers(customers ,"Customers");
        	this.saveDeliveryPeople(deliveryPeople,"DeliveryPeople");
		this.saveRestaurants(queueRestaurants,"QueueRestaurants");
		this.saveDeliveryPeople(queueDeliveryPeople,"QueueDeliveryPeople");
		this.saveOrders(orders,"Orders");

	}

    	public LinkedList<Customer> getCustomers()
	{
        	return customers;
    	}

    	public LinkedList<Restaurant> getRestaurants()
	{
        	return restaurants;
    	}

	public LinkedList<DeliveryPerson> getDeliveryPeople()
	{
        	return deliveryPeople;
    	}

	public LinkedList<FoodOrder> getOrders()
	{
        	return orders;
    	}

    	private static LinkedList<FoodOrder> loadOrders(String filename)
	{
        	try
		{
			ObjectInputStream ois = new ObjectInputStream(new FileInputStream(filename));
            		return (LinkedList<FoodOrder>) ois.readObject();
        	}
		catch (Exception e)
		{
            		System.out.println("No existing restaurants found. Creating new restaurant.");
            		return new LinkedList<>();
        	}
    	}

    	private static void saveOrders(LinkedList<FoodOrder> orders, String filename)
	{
        	try
		{
			ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(filename));
            		oos.writeObject(orders);
        	} 
		catch (Exception e)
		{
            		e.printStackTrace();
        	}
    	}

    	private static LinkedList<Restaurant> loadRestaurant(String filename)
	{
        	try
		{
			ObjectInputStream ois = new ObjectInputStream(new FileInputStream(filename));
            		return (LinkedList<Restaurant>) ois.readObject();
        	}
		catch (Exception e)
		{
            		System.out.println("No existing restaurants found. Creating new restaurant.");
            		return new LinkedList<>();
        	}
    	}

    	private static void saveRestaurant(LinkedList<Restaurant> restaurants, String filename)
	{
        	try
		{
			ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(filename));
            		oos.writeObject(restaurants);
        	} 
		catch (Exception e)
		{
            		e.printStackTrace();
        	}
    	}

	private static void saveCustomers(LinkedList<Customer> customers, String filename)
	{
        	try
		{
			ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(filename));
            		oos.writeObject(customers);
        	}
		catch (Exception e)
		{
            		e.printStackTrace();
        	}
    	}

    	private static LinkedList<Customer> loadCustomers(String filename)
	{
        	try
		{
			ObjectInputStream ois = new ObjectInputStream(new FileInputStream(filename));
            		return (LinkedList<Customer>) ois.readObject();
        	} 
		catch (Exception e)
		{
            		System.out.println("No existing restaurants found. Creating new restaurant.");
            		return new LinkedList<>();
        	}
    	}

    	private static void saveDeliveryPeople(String filename)
	{
        	try
		{
			ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(filename));
            		oos.writeObject(deliveryPeople);
            		System.out.println("Delivery people data saved successfully.");
        	}
		catch (IOException e)
		{
            		e.printStackTrace();
        	}
    	}

    	private static LinkedList<DeliveryPerson> loadDeliveryPeople(String filename)
	{
        	try
		{
			ObjectInputStream ois = new ObjectInputStream(new FileInputStream(filename));
            		return (LinkedList<DeliveryPerson>) ois.readObject();
        	}
		catch (IOException | ClassNotFoundException e)
		{
            		System.out.println("No existing restaurants found. Creating new restaurant.");
            		return new LinkedList<>();
        	}
    	}

}

class PhysicalMap{

	private static PhysicalMap instance;
	private static Hashmap <String , double[]> AddressCoordinates
	private PhysicalMap()
	{
		AddressCoordinates = loadCoordinates("Address_Coordinates");
	}
	public static PhysicalMap getInstance()
	{
		if (instance == null)
		{
            		instance = new PhysicalMap();
        	}
        	return instance;
	}
	public void saveCoordinates(String filename)
	{
        	try
		{
			ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(filename));
            		oos.writeObject(addressCoordinates);
            		System.out.println("Coordinates data saved successfully.");
        	}
		catch (IOException e)
		{
            		e.printStackTrace();
        	}
    	}

    	public HashMap<String , double[]> loadCoordinates(String filename)
	{
        	try 
		{
			ObjectInputStream ois = new ObjectInputStream(new FileInputStream(filename));
            		return (HashMap <String , double[]>) ois.readObject();
        	}
		catch (Exception e)
		{
            		System.out.println("No co-ordinates found");
            		return new HashMap<>();
        	}
    	}
	static double[] getCoordinates(String temp)
	{
		if(AddressCoordinates.containsKey(temp))
		{
			return AddressCoordinates.getValue(temp);
		}
		else
		{
			System.out.println("Enter latitude and longitudinal co-ordinates or XY co-ordinates");
			double [] tempCo = new double [2];
			System.out.println("Enter latitude or X co-ordinates");
			tempCo[0] = scanner.nextDouble();
			System.out.println("Enter longitude or Y co-ordinates");
			tempCo[1] = scanner.nextDouble();
			return tempCo;
		}
	}
}


public class FoodOrderingApp {

	static SystemAdmin admin = SystemAdmin.getInstance(); 
	static Scanner scanner = new Scanner(System.in);
	Console cons = System.console();
	static PhysicalMap mapp = PhysicalMap.getInstance();
    	public static void main(String[] args)
	{
        	Scanner scanner = new Scanner(System.in);
        	while (true) 
		{
			Hashmap <String, Double> AddressCoordinates = loadCoordinates("Address_Coordinates");
            		System.out.println("\n \t Welcome to Food Ordering App!");
            		System.out.println("1. New Customer");
            		System.out.println("2. Customer login");
			System.out.println("3. Request admin to add restaurants/delivery person");
			System.out.println("4. Add menu on a restaurant");
			System.out.println("5. Admin login");
            		System.out.println("6. Exit");

            		System.out.print("Enter your choice: ");
            		int choice = scanner.nextInt();
            		scanner.nextLine(); // kills next line

            		switch (choice)
			{
                		case 1:
                    			System.out.println("Enter customer name : ");
					String tempName= scanner.nextLine();
					System.out.println("Enter customer address: ");
					String tempAddress = scanner.nextLine();
					double [] tempCoordinates = mapp.getCoordinates(tempAddress);
					admin.addCustomers(new Customer(admin.countCustomerId++ , tempName , tempAddress, tempCoordinates));
                    			break;

                		case 2:
					System.out.println("Enter customer name/Id : ");
					String inputCustomer = scanner.nextLine();
					int customerIndex = 0;
					boolean flag = false;
					if(Pattern.matches("^[0-9]+$",inputCustomer))
					{
						int tempId = Integer.parseInt(inputCustomer);
						for(Customer i : admin.customers)
						{
							if(i.getId == tempId)
							{
								i.login = true;
								customerIndex = admin.customers.indexOf(i);
								flag = true;
								break;
							}
						}
					}
					else
					{
						for(Customer i : admin.customers)
						{
							if(i.getName().equals(inputCustomer) )
							{
								i.login = true;
								customerIndex = admin.customers.indexOf(i);
								flag = true;
								break;
							}
						}
					}
					if(flag)
					{	System.out.println("Customer not found !!!");	}
					else
					{
						ArrayList<FoodOrder> tempOrder = new ArrayList<>();
						tempOrder.addCustomer(admin.customers.get(CustomerIndex));
						int choice3 = 0;
						while(choice3 != 4)
						{
							System.out.println("select the operation to be done");
							System.out.println("1. Search for food items");
							System.out.println("2. List Menu of all restaurants");
							System.out.println("3. Remove food items from cart ");
							System.out.println("4. Place the order");
							System.out.println("5. orders history");
							System.out.println("6. Return to previous window \n Enter your choice");
							choice3 = scanner.nextInt();
							switch(choice3)
							{
								case 1:
								System.out.println("type to Search for Food items by name/price: ");
									String search = scanner.nextLine();
									Pattern patten = Pattern.compile(search);
									tempOrder = searchFoodItems(tempOrder,pattern);	
									for(FoodOrder i : tempOrder)
								{	i.getCustomer = admin.customers.indexOf(CustomerIndex);}
									break;
								case 2:
									Pattern pattern = Pattern.compile("\\w*");
									tempOrder = searchFoodItems(tempOrder,pattern);
									for(FoodOrder i : tempOrder)
								{	i.getCustomer = admin.customers.indexOf(CustomerIndex);}
									break;
								case 3:
									tempOrder = removeFoodItems(tempOrder);
									break;
								case 4:
									placeCustomerOrder();
									break;
								case 5:
									admin.customers.get(customerIndex).getOrderDetails();
									break;
								case 6:
									break;
								default:
									System.out.println("Enter Valid options");
							}
						}
					}
                    			break;

				case 3:
					do{
					System.out.println("Do you want to register restaurant or delivery Employee:");
					System.out.println("1. Request to add restaurant");
					System.out.println("2. Request to add Delivery Employee");
					System.out.println("3. Go back to previous menu");
					int option = scanner.nextInt();
					switch(option)
					{
						case 1:
							System.out.println("Enter Restaurant name : ");
							String tempName= scanner.nextLine();
							System.out.println("Enter Restaurant Location: ");
							String tempAddress = scanner.nextLine();
							double [] tempCoordinates = mapp.getCoordinates(tempAddress);
				admin.addRestaurant(new Customer(admin.countRestaurantId++ , tempName , tempAddress, tempCoordinates));
                    					break;
						case 2:
							System.out.println("Enter Applicant name : ");
							String tempName= scanner.nextLine();
							System.out.println("Enter Applicant current location: ");
							String tempAddress = scanner.nextLine();
							double [] tempCoordinates = mapp.getCoordinates(tempAddress);
				admin.addDeliveryPerson(new Customer(admin.countDeliveryPersonId++ , tempName , tempCoordinates));
                    					break;
						case 3:
							break;
						default:
							System.out.println("Invalid option, please try again");
							break;
					}
					}while(option == 3);
					break;

				case 4:
					System.out.println("Enter Restaurant name/id : ");
					String inputRestaurant = scanner.nextLine();
					int restaurantIndex;
					boolean flag = false;
					if(Pattern.matches("^[0-9]+$",inputRestaurant))
					{
						int tempId = Integer.parseInt(inputRestaurant);
						for(Restaurant i : admin.restaurants)
						{
							if(i.getId == tempId)
							{
								restaurantIndex = admin.restaurants.indexOf(i);
								flag = true;
								break;
							}
						}
					}
					else
					{
						for(Restaurant i : admin.restaurants)
						{
							if(i.getName().equals(inputRestaurant) )
							{
								restaurantIndex = admin.restaurants.indexOf(i);
								flag = true;
								break;
							}
						}
					}
					if(!flag)
					{
						while(true){
				System.out.println("Enter food items to Add in menu(if you are finished with process, type done):");
						String tempName = scanner.nextLine();
						if( tempName.equals("done"))
						{	break;	}
						System.out.println("Enter price of food");
						float tempPrice = scanner.nextFloat();
						int choice4 = 0;
						while(choice4 !=1 || choice4 != 2 || choice4 != 3 )
						System.out.println("Enter type of food item (Sub-category)");
						System.out.println("1. Burger ");
						System.out.println("2. Pizza ");
						System.out.println("3. other/Suplimentry \n Enter your choice");
						switch(choice4 = scanner.nextInt())
						{
							case 1:
							admin.restaurants.get(restaurantIndex).addMenu( new Burger(tempName,tempPrice));
								break;
							case 2:
							admin.restaurants.get(restaurantindex).addMenu( new Pizza(tempName,tempPrice));
								break;
							case 3:
						 admin.restaurants.get(restaurantindex).addMenu( new OtherFoodItem(tempName,tempPrice));
								break;
							default:
								System.out.println("Enter valid options !!");
								break;
						}}
					}
					else
					{	System.out.println("Enter valid Restaurant name/id : ");	}
					break;


				case 5:
					System.out.println("Enter Password : ");
					if(admin.checkPassword(cons.readPassword()))
					{
						do{
						System.out.println("Admin Options:");
						System.out.println("1. Grant permission for restaurants");
						System.out.println("2. Employ Delivery People");
						System.out.println("3. Remove Restaurants from service");
						System.out.println("4. Employ Delivery People");
						System.out.println("5. Go back to previous menu \n enter your options");
						int choice2 = scanner.nextInt();
						switch(choice2)
						{
							case 1:
								admin.employRestaurant();
								break;
							case 2:
								admin.employDeliveryPerson();
								break;
							case 3:
								admin.deleteRestaurant();
								break;
							case 4:
								admin.deleteDeliveryPerson();
								break;
							case 5:
								break;
							default:
								System.out.println("Enter valid options");
								break;	
						}
						}while(choice2 != 5);
					}
					break;

               			case 6:
                    			admin.saveAll();
					mapp.saveCoordinates(AddressCoordinates,"Address_Coordinates");
                    			System.out.println("Exiting Food Ordering App!");
                    			System.exit(0);

               			default:
                    			System.out.println("Invalid choice. Please enter again.");
					break;

            		}
        	}
    	}
	
	static ArrayList<FoodOrder> searchFoodItems(ArrayList<FoodOrder> orders , Pattern search)
	{
		System.out.println("Here is the list as you requested. Remember ordering from multiple restaurants comes as multiple orders !!");
		for(Restaurant i : admin.getRestaurant())
		{
			System.out.println((restaurants.indexOf(i))  + ". " + i.getName() + " \t" + i.getLocation());
			for( FoodItem j : i.getMenu())
			{
				Matcher matcher = search.matcher(j.getItemDetails());
				if(matcher.matches())
				{	System.out.println("\t" + i.getMenu().indexOf(j) + j.getItemDetails() ); 	}
			}
			
		}
		System.out.println("Select your orders by [RestaurantCode]-[ItemCode]-[Quantity]. if you are done, type 'done' ");
		String input;
		ArrayList<String> orders = new ArrayList<>();
		while((input = scanner.nextLine()) != "done")
		{
			Pattern pat = Pattern.compile("^[0-9]+(-)[0-9]+(-)?[0-9]*$");
			Matcher m = pattern.matcher(input);
			if(m.matches())
			{
				String[] str = pat.split(input);
				int quantity;
				if(str.size() == 2)
				{	quantity = 1;	}
				else
				{	quantity = Integer.parseInt(str[2]);	}
				Boolean flag1 = false;
				int restaurantCode = Integer.parseInt(str[0]);
				if( restaurantCode >=0 && restaurantCode < admin.getRestaurant.size() )
				{
					for(FoodOrder i : orders)
					{
						if(i.restaurant.equals(admin.restaurants.get(restaurantCode)))
						{	flag1 = true;	break;	}
					}
					if(!flag1)
					{
						System.out.println("Orders under this restaurant will be added as separate order!");
					}
					int itemCode = Integer.parseInt(str[1]);
					if( itemcode >= 0 && itemCode < admin.getRestaurant.get(restaurantCode).getMenu().size())
					{
					  for(FoodItem i : admin.getRestaurant.get(restaurantCode).getMenu())
					   {
					     if(admin.getRestaurant.get(restaurantCode).getMenu().get(itemCode).equals(i))
					      {
						if(flag1)
						{
						  for(FoodOrder j : orders)	
						  {
						    if( j.getRestaurant() = admin.getRestaurant.get(restaurantCode) )
						    { j.addToCart(i, quantity ,admin.getRestaurant.get(restaurantCode)); break;}
						  }
						}
						else
						{
						  FoodOrder temp = new FoodOrder(admin.countOrderId++);
						  temp.addtoCart(i, quantity ,admin.getRestaurant.get(restaurantCode));
						  orders.add(temp);
						}
						break;
					      }
					   }
					}
					else
					{	System.out.println("Enter Valid Food Item code");}
				}
				else
				{	System.out.println("Enter valid restaurant code") ;	}
			}
		}
		return orders;
	}

	public ArrayList<FoodOrder> removeFoodItem(ArrayList<FoodOrder> orders)
	{
		String input2;
		 System.out.println("Remove your Items by [OrderCode]-[ItemCode]-[Quantity]. if you are done, type 'done' ");
		 for(FoodOrder i : orders)
		 {
			System.out.println("Foodorders from order " + orders.indexOf(i) + " : ");
			System.out.println("SI no \t ItemName  -  Price \t Quantity");
			for( FoodItem j : i.cart.keySet())
			{
				System.out.println(i.cart.indexOf(j) +"\t" + j.getItemDetails() + "\t" + i.cart.getValue(j));
			}
		 }
		do{
		 System.out.println("Enter your choice : ");
		 input2 = scanner.nextLine;
		 if(input2 != "done")
		 {
			Pattern pat = Pattern.compile("^[0-9]+(-)[0-9]+(-)?[0-9]*$");
			Matcher m = pattern.matcher(input);
			if(m.matches())
			{
				String[] str = pat.split(input);
				int quantity;
				if(str.size() == 2)
				{	quantity = 1;	}
				else
				{	quantity = Integer.parseInt(str[2]);	}
				Boolean flag1 = false;
				int orderCode = Integer.parseInt(str[0]);
				int itemCode = Integer.parseInt(str[1]);
				if( orderCode >=0 && orderCode < orders.size())
				{
				    if(itemCode >=0 && itemCode < orders.get(orderCode).cart.keySet().size())
				    {
					FoodOrder temp = orders.get(orderCode).cart.getKey(itemCode);
					orders.get(orderCode).removeFromcart(temp, orders.get(orderCode).cart.getValue(temp) );
				    }
				    else
				    {	System.out.println("Item Code Mismatch !!");	}
				}
				else
				{
					System.out.println("Order Code Mismatch !!");
				}
			}
			else
			{	System.out.println("Pattern Mismatch !! . enter the code correctly");}
		 }
		}while(input2 != "done");
	}

	public static void placeCustomerOrder(ArrayList<FoodOrder> orders)
	{
	   for(Foodorder j : orders)
	   {
		DeliveryPerson tempPerson = new DeliveryPerson();
		int score = 6;
		for(DeliveryPerson i : admin.deliveryPeople)
		{
			int tempNum = i.ordersToDeliver.size();
			if( tempNum < score )
			{
				score = tempNum;	
				tempPerson = i;
			}
		}
		j.placeOrder(tempPerson, admin);
	   }
	}
}
