package ru.otus.testclasses;

import ru.otus.logger.Log;

public class Calculator implements ICalculator{

    @Log
    public void calculation(int param) {
        System.out.println("Calculation result with one int param:" + param);
    }

    @Log
    public int calculation(int param1, int param2) {
        int result = param1 + param2;
        System.out.println("Calculation result with two int params:" + result);
        return result;
    }

    public void calculation(int param1, int param2, String param3, ICalculator target) {
        System.out.println(
                String.format("Calculation result1 with 3d param %s: ", param3) + target.calculation(param1, param2));
    }

    public void calculation(String param3, int param1, int param2, ICalculator target) {
        System.out.println(
                String.format("Calculation result2 with 3d param %s: ", param3) + target.calculation(param1, param2));
    }

    @Log
    public void calculation(String param) {
        System.out.println("Calculation result with one string param:" + param);
    }
}
