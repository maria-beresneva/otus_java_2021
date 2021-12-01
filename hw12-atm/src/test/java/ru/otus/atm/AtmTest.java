package ru.otus.atm;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.otus.atm.enums.AtmErrorType;
import ru.otus.atm.utils.AtmResponse;

import static org.assertj.core.api.Assertions.assertThat;

public class AtmTest {
    private static Atm atm;

    @BeforeEach
    void setUp() {
        atm = new AtmImp();
        atm.registerAndFillTrayForNominal(100, 50);
        atm.registerAndFillTrayForNominal(500, 50);
        atm.registerAndFillTrayForNominal(1000, 50);
        atm.printOutAtmState();
    }

    @Test
    void unknownNominalTrayTest() {
        AtmResponse response = atm.registerTrayIfNecessary(49);
        assertThat(response.getErrorType()).isEqualTo(AtmErrorType.INCORRECT_INPUT);
    }

    @Test
    void insertNote() {
        var atmMoneyAmount = atm.getTotal();
        var atmFreeTrays = atm.freeTraysCount();
        AtmResponse response = atm.insert(new BanknoteImp( 5000));
        assertThat(response.getErrorType())
                .as("Inserting money should be success")
                .isEqualTo(AtmErrorType.OK);
        assertThat(atm.getTotal())
                .as("ATM money amount should change after successful money deposit.")
                .isEqualTo(atmMoneyAmount + 5000);
        assertThat(atm.freeTraysCount())
                .as("ATM free trays count should change after successful money deposit.")
                .isEqualTo(atmFreeTrays - 1);
    }

    @Test
    void insertInvalidNominal() {
        AtmResponse response = atm.insert(new BanknoteImp(101));
        assertThat(response.getErrorType())
                .as("Unexpected money nominal.")
                .isEqualTo(AtmErrorType.INCORRECT_INPUT);
    }

    @Test
    void insertTooMuch() {
        for (int i = 0; i < 100; i++) {
            atm.insert(new BanknoteImp( 2000));
        }
        AtmResponse response = atm.insert(new BanknoteImp(2000));
        assertThat(response.getErrorType())
                .as("Count of atm's money tray got a limit")
                .isEqualTo(AtmErrorType.INSUFFICIENT_STORAGE);
        atm.printOutAtmState();
    }

    @Test
    void withdrawMoreThenAtmHas() {
        var amount = atm.getTotal() + 500;
        AtmResponse response = atm.withdraw( amount);
        assertThat(response.getErrorType())
                .as("User Vasya shouldn't be able to withdraw 700 Rub as the ATM only has 550 in small notes.")
                .isEqualTo(AtmErrorType.INSUFFICIENT_ATM_FUNDS);
    }

    @Test
    void withdrawLessThenMinBanknote() {
        int amount = 50;
        AtmResponse response = atm.withdraw( amount);
        assertThat(response.getErrorType())
                .as("Atm doesn't have a banknote for collect this sum")
                .isEqualTo(AtmErrorType.INCORRECT_INPUT);
    }

    @Test
    void withdrawByOneBanknote() {
        var balance = atm.getTotal();
        AtmResponse response = atm.withdraw(500);
        assertThat(response.getErrorType())
                .as("User should be able to successfully withdraw his money")
                .isEqualTo(AtmErrorType.OK);
        assertThat(atm.getTotal())
                .as("Users balance should change after successful withdrawal.")
                .isEqualTo(balance - 500);
    }

    @Test
    void withdrawBySeveralBanknotes() {
        var balance = atm.getTotal();
        AtmResponse response = atm.withdraw(1400);
        assertThat(response.getErrorType())
                .as("User should be able to successfully withdraw his money")
                .isEqualTo(AtmErrorType.OK);
        assertThat(atm.getTotal())
                .as("Users balance should change after successful withdrawal.")
                .isEqualTo(balance - 1400);
    }

    @Test
    void withdrawBySeveralBanknotesAndUnregisterTray() {
        var balance = atm.getTotal();
        var countTrays = atm.countTraysForNominal(100);
        AtmResponse response = atm.withdraw(100 * 50);
        assertThat(response.getErrorType())
                .as("User should be able to successfully withdraw his money")
                .isEqualTo(AtmErrorType.OK);
        assertThat(atm.getTotal())
                .as("Users balance should change after successful withdrawal.")
                .isEqualTo(balance - 100 * 50);
        assertThat(atm.countTraysForNominal(100))
                .as("One of trays for nominal 100 should be unregistered")
                .isEqualTo(countTrays - 1);
    }
}
