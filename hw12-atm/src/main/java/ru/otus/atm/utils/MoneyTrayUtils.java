package ru.otus.atm.utils;

public class MoneyTrayUtils {
    private static int trayCounter = 0;

    public static int registerTray() {
        int newTrayId = trayCounter++;
        System.out.printf("New money tray registered with id: %d%n", newTrayId);
        return newTrayId;
    }

    public static void transferNoteFromInputToTray(int trayId) {
        System.out.printf("Transferring note from input device to tray with id: %d%n", trayId);
    }

    public static void transferNotesFromTrayToOutput(int trayId, long notesCount) {
        System.out.printf("Withdrawing %d note(s) from tray with id %d%n", notesCount, trayId);
    }
}
