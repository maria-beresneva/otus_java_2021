package ru.otus.testclasses;

public interface ICalculator {

    void calculation(int param);

    void calculation(String param);

    int calculation(int param1, int param2);

    void calculation(int param1, int param2, String param3, ICalculator target);

    void calculation(String param3, int param1, int param2, ICalculator target);
}
