package homework;


import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.Map;

public class CustomerService {

    Map<Customer, String> customerMap = new LinkedHashMap<>();

    public Map.Entry<Customer, String> getSmallest() {
        Customer smallestKey = customerMap.keySet().stream().min(Comparator.comparing(Customer::getScores)).get();

        return Map.entry(new Customer(smallestKey.getId(), smallestKey.getName(), smallestKey.getScores()),
                  customerMap.get(smallestKey));
    }

    public Map.Entry<Customer, String> getNext(Customer customer) {
        Customer nextKey = customerMap.keySet().stream()
                .filter(key -> key.getScores() > customer.getScores())
                .min(Comparator.comparing(Customer::getScores)).orElse(null);

        return (nextKey != null)
                ? Map.entry(new Customer(nextKey.getId(), nextKey.getName(), nextKey.getScores()),
                            customerMap.get(nextKey))
                : null;
    }

    public void add(Customer customer, String data) {
        customerMap.put(customer, data);
    }
}
