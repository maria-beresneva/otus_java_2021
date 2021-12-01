package ru.otus.atm.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Arrays;

@Getter
@AllArgsConstructor
public enum Nominal {
    NOMINAL_100 (100),
    NOMINAL_200 (200),
    NOMINAL_500 (500),
    NOMINAL_1000 (1000),
    NOMINAL_2000 (2000),
    NOMINAL_5000 (5000);

    private int nominal;

    public static boolean checkNominalSupported(int nominal) {
        return Arrays.stream(Nominal.values()).anyMatch(nominalItem -> nominalItem.getNominal() == nominal);
    }
}
