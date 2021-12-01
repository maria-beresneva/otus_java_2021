package ru.otus.atm.storage;

public interface MoneyTraysSet {

    void removeTray();

    int getTotalNotesCount();

    int getFreeSpace();

    void putSingleNote();

    void withdrawMoney(long banknotesToWithdraw);

    void addTray(int countNotes);

    int registeredTraysCount();
}

