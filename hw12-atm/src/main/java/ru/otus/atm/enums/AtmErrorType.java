package ru.otus.atm.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum AtmErrorType {
    OK(0, ""),
    INSUFFICIENT_ATM_FUNDS(2, "Couldn't execute the required operation due to insufficient funds in ATM"),
    INSUFFICIENT_STORAGE(3, "Couldn't execute the required operation due to insufficient storage in ATM"),
    INTERNAL_ERROR(4, "Atm internal error. Please contact Atm customer support."),
    INCORRECT_INPUT(5, "Atm can't handle such input values.");

    private int code;
    private String type;
}
