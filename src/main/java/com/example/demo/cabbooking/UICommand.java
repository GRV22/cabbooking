package com.example.demo.cabbooking;

public enum UICommand {
    REGISTER_RIDER(1),
    REGISTER_DRIVER(2),
    REGISTER_CAB(3),
    UPDATE_CAB_LOCATION(4),
    DRIVER_AVAILABILITY(5),
    BOOK_CAB(6),
    FETCH_RIDE_HISTORY(7),
    END_TRIP(8);

    private int id;

    UICommand(int id) {
        this.id = id;
    }

    public static void printAvailableCommands() {
        System.out.println("Available Commands");
        for (UICommand command : UICommand.values()) {
            System.out.println(command.id + " : " + command.name());
        }
    }

}
