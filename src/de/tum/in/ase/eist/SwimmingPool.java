package de.tum.in.ase.eist;

import java.util.Optional;
import java.util.concurrent.locks.ReentrantLock;

public class SwimmingPool {
    private final ChangingRoom changingRoom;
    private final Locker locker;
    private int totalVisitors;
    private final ReentrantLock totalVisitorsLock;

    public SwimmingPool() {
        this.changingRoom = new ChangingRoom();
        this.locker = new Locker();
        this.totalVisitors = 0;
        this.totalVisitorsLock = new ReentrantLock();
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
                while (changingRoom.getOccupant().isPresent() && locker.getOccupant().isPresent()) {

                }
                changingRoom.getMutex().lock();
                locker.getMutex().lock();
                changingRoom.acquireKey(swimmer);
                locker.storeClothes(swimmer);

                System.out.printf("Swimmer %d has gone swimming.\n", swimmer.getId());

                locker.retrieveClothes();
                changingRoom.releaseKey();
                changingRoom.getMutex().unlock();
                locker.getMutex().unlock();
            }
            case LOCKER_BEFORE_CHANGING_ROOM -> {
                while (changingRoom.getOccupant().isPresent() && locker.getOccupant().isPresent()) {

                }
                changingRoom.getMutex().lock();
                locker.getMutex().lock();
                locker.storeClothes(swimmer);
                changingRoom.acquireKey(swimmer);

                System.out.printf("Swimmer %d has gone swimming.\n", swimmer.getId());

                changingRoom.releaseKey();
                locker.retrieveClothes();
                changingRoom.getMutex().unlock();
                locker.getMutex().unlock();
                }
        }
        totalVisitorsLock.lock();
        totalVisitors++;
        totalVisitorsLock.unlock();
    }

    public int getTotalVisitors() {
        return this.totalVisitors;
    }
}
