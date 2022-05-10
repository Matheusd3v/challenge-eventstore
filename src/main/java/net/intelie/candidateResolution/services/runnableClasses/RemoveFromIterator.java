package net.intelie.candidateResolution.services.runnableClasses;

import net.intelie.challenges.Event;

import java.util.concurrent.LinkedBlockingQueue;

public class RemoveFromIterator implements Runnable{
    int count;
    boolean lastMoveNext;
    LinkedBlockingQueue<Event> list;
    Event currentEvent;

    public RemoveFromIterator(int count, boolean lastMoveNext, LinkedBlockingQueue<Event> list, Event currentEvent) {
        this.count = count;
        this.lastMoveNext = lastMoveNext;
        this.list = list;
        this.currentEvent = currentEvent;
    }

    @Override
    public void run() {
        if (count == 0 || !lastMoveNext ) {
            throw new IllegalStateException("MoveNext was never called or its last result was false");
        }

        list.removeIf(x -> x == currentEvent);
    }
}
