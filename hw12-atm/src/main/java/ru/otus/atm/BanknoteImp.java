package ru.otus.atm;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public class BanknoteImp implements Banknote {
    private int nominal;

    public int getNominal() {
        return nominal;
    }
}
