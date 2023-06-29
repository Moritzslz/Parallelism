package de.tum.in.ase.eist;

import java.util.List;

public class Main {

    public static void main(String[] args) {
        detectDeadlock(new SwimmingPool());
    }

    public static void detectDeadlock(SwimmingPool swimmingPool) {
        // TODO 2
        Swimmer swimmer = new Swimmer();
        Thread thread1 = new Thread(() -> swimmer.goToSwimmingPool(swimmingPool, SwimmingPoolActionOrder.CHANGING_ROOM_BEFORE_LOCKER));
        thread1.start();

        Swimmer swimmer2 = new Swimmer();
        Thread thread2 = new Thread(() -> swimmer2.goToSwimmingPool(swimmingPool, SwimmingPoolActionOrder.LOCKER_BEFORE_CHANGING_ROOM));
        thread2.start();

        try {
            thread1.join();
            thread2.join();
        } catch (InterruptedException e) {}
    }
}
