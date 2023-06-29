package de.tum.in.ase.eist;

import java.util.Optional;
import java.util.concurrent.locks.ReentrantLock;

public class SwimmingPool {
    private final ChangingRoom changingRoom;
    private final Locker locker;
    private int totalVisitors;
    private final ReentrantLock totalVisitorsLock;
    private int bouncer;

    public SwimmingPool() {
        this.changingRoom = new ChangingRoom();
        this.locker = new Locker();
        this.totalVisitors = 0;
        this.totalVisitorsLock = new ReentrantLock();
        this.bouncer = -1;
    }

    public void handleEntryRequest(Swimmer swimmer, SwimmingPoolActionOrder order) {
        switch (order) {
            case CHANGING_ROOM_BEFORE_LOCKER -> {
                    changingRoom.acquireKey(swimmer);
                    locker.storeClothes(swimmer);

                    System.out.printf("Swimmer %d has gone swimming.\n", swimmer.getId());

                    locker.retrieveClothes();
                    changingRoom.releaseKey();
            }
            case LOCKER_BEFORE_CHANGING_ROOM -> {
                    locker.storeClothes(swimmer);
                    changingRoom.acquireKey(swimmer);

                    System.out.printf("Swimmer %d has gone swimming.\n", swimmer.getId());

                    changingRoom.releaseKey();
                    locker.retrieveClothes();
            }
        }
        totalVisitorsLock.lock();
        totalVisitors++;
        totalVisitorsLock.unlock();
    }

    public void handleEntryRequestDeadlockFree(Swimmer swimmer, SwimmingPoolActionOrder order) {
        // TODO 3
        switch (order) {
            case CHANGING_ROOM_BEFORE_LOCKER -> {
                if (bouncer == -1 || bouncer == 0) {
                    bouncer = 0;
                    changingRoom.acquireKey(swimmer);
                    locker.storeClothes(swimmer);

                    System.out.printf("Swimmer %d has gone swimming.\n", swimmer.getId());

                    locker.retrieveClothes();
                    changingRoom.releaseKey();
                } else {
                    System.out.println("Go away");
                }
            }
            case LOCKER_BEFORE_CHANGING_ROOM -> {
                if (bouncer == -1 || bouncer == 1) {
                    bouncer = 1;
                    locker.storeClothes(swimmer);
                    changingRoom.acquireKey(swimmer);

                    System.out.printf("Swimmer %d has gone swimming.\n", swimmer.getId());

                    changingRoom.releaseKey();
                    locker.retrieveClothes();
                }
                else {
                    System.out.println("Go away");
                }
            }
        }
        totalVisitorsLock.lock();
        totalVisitors++;
        totalVisitorsLock.unlock();
        bouncer = -1;
    }

    public int getTotalVisitors() {
        return this.totalVisitors;
    }
}
