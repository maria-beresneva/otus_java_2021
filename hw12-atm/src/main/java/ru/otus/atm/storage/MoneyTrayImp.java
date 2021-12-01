package ru.otus.atm.storage;

import lombok.Getter;
import ru.otus.atm.enums.AtmErrorType;
import ru.otus.atm.utils.AtmErrorException;
import ru.otus.atm.utils.MoneyTrayUtils;

@Getter
public class MoneyTrayImp implements MoneyTray{
        private final int traySize;
        private int banknoteCount;
        private final int id;

        MoneyTrayImp(int notes, int traySize) {
            if (traySize < notes)
                throw new AtmErrorException(
                        AtmErrorType.INCORRECT_INPUT,
                        String.format("Tray can't held more notes(%d) than it's size(%d)!", notes, traySize));
            id = MoneyTrayUtils.registerTray();
            banknoteCount = notes;
            this.traySize = traySize;
        }

        @Override
        public int getFreeSpace() {
            return traySize - banknoteCount;
        }

        @Override
        public void withdraw(long banknotesNumber) {
            if (banknotesNumber < 0)
                throw new AtmErrorException(
                        AtmErrorType.INCORRECT_INPUT,
                        String.format("Impossible to withdraw negative number of notes: %d.", banknotesNumber));
            if (banknotesNumber > banknoteCount)
                throw new AtmErrorException(
                        AtmErrorType.INSUFFICIENT_ATM_FUNDS,
                        String.format("""
                                        Trying to withdraw more money that is present in the tray!
                                        Attempting to withdraw: %d
                                        Available for withdrawal: %d""",
                        banknotesNumber, banknoteCount));
            MoneyTrayUtils.transferNotesFromTrayToOutput(id, banknotesNumber);
            banknoteCount -= banknotesNumber;

        }

        @Override
        public void putSingleNote() {
            if (banknoteCount == traySize)
                throw new AtmErrorException(
                        AtmErrorType.INSUFFICIENT_STORAGE,
                        String.format("""
                                        Trying to put more money then the tray can hold! Tray is full
                                        Tray limit: %d""",
                        traySize));
            MoneyTrayUtils.transferNoteFromInputToTray(id);
            banknoteCount++;
      }
}
