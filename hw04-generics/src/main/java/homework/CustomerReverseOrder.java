package homework;


import java.util.*;

public class CustomerReverseOrder {

    LinkedList<Customer> customerSet = new LinkedList<>();

    public void add(Customer customer) {
        customerSet.add(customer);
    }

    public Customer take() {
        return customerSet.pollLast();
    }
}
