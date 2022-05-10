package net.intelie.candidateResolution.services;

import net.intelie.candidateResolution.services.runnableClasses.FilterListRunnable;
import net.intelie.candidateResolution.services.runnableClasses.RemoveFromIterator;
import net.intelie.challenges.Event;
import net.intelie.challenges.EventIterator;

import java.util.Iterator;
import java.util.concurrent.*;

public class QueryIterator  implements EventIterator {
    private LinkedBlockingQueue<Event> list;
    private final Iterator<Event> listIterator;
    private int count = 0;
    private boolean lastMoveNext;
    private Event currentEvent;

    public QueryIterator(LinkedBlockingQueue<Event> listToIterate, LinkedBlockingQueue<Event> completeList) {
        this.list = completeList;
        this.listIterator = listToIterate.iterator();
    }

    @Override
    public boolean moveNext() {
        ++count;

        if (listIterator.hasNext()) {
            currentEvent = listIterator.next();
            lastMoveNext = true;

            return true;
        }

        lastMoveNext = false;

        return false;
    }

    @Override
    public Event current() {
        if (count == 0 || !lastMoveNext ) {
            throw new IllegalStateException("MoveNext was never called or its last result was false");
        }

        return currentEvent;
    }

    @Override
    public void remove() {

        ExecutorService threadExecutor = Executors.newSingleThreadExecutor();

        threadExecutor.execute(new RemoveFromIterator(count, lastMoveNext, list, currentEvent));

        threadExecutor.shutdown();
        try {
            threadExecutor.awaitTermination(100, TimeUnit.MILLISECONDS);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void close(){
        System.out.println("System close out");
    }
}

