package de.tum.in.ase.eist;

import java.util.List;

public class Main {

    public static void main(String[] args) {
        detectDeadlock(new SwimmingPool());
    }

    public static void detectDeadlock(SwimmingPool swimmingPool) {
        // TODO 2
        Swimmer swimmer = new Swimmer();
        Thread thread1 = new Thread(() -> swimmingPool.handleEntryRequest(swimmer, SwimmingPoolActionOrder.CHANGING_ROOM_BEFORE_LOCKER));
        thread1.start();

        Swimmer swimmer2 = new Swimmer();
        Thread thread2 = new Thread(() -> swimmingPool.handleEntryRequest(swimmer2, SwimmingPoolActionOrder.LOCKER_BEFORE_CHANGING_ROOM));
        thread2.start();

        Swimmer swimmer3 = new Swimmer();
        Thread thread3 = new Thread(() -> swimmingPool.handleEntryRequest(swimmer3, SwimmingPoolActionOrder.CHANGING_ROOM_BEFORE_LOCKER));
        thread3.start();

        Swimmer swimmer4 = new Swimmer();
        Thread thread4 = new Thread(() -> swimmingPool.handleEntryRequest(swimmer4, SwimmingPoolActionOrder.LOCKER_BEFORE_CHANGING_ROOM));
        thread4.start();
    }
}
