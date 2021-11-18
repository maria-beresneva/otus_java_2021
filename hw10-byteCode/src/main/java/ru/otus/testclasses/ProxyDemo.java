package ru.otus.testclasses;

import ru.otus.proxy.CustomProxy;

public class ProxyDemo {
    public static void main(String[] args) {
        ICalculator myClass = CustomProxy.createCalculator();
        myClass.calculation(1);
        System.out.println("----------------------------------");
        myClass.calculation(10, 1);
        System.out.println("----------------------------------");
        myClass.calculation(1, 10);
        System.out.println("----------------------------------");
        myClass.calculation(1, 2, "3", myClass);
        System.out.println("----------------------------------");
        myClass.calculation("4");
        System.out.println("----------------------------------");
        myClass.calculation("5", 6, 7, myClass);
        System.out.println("----------------------------------");
    }
}
