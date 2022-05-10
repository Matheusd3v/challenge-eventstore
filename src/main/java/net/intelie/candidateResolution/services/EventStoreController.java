package net.intelie.candidateResolution.services;

import net.intelie.candidateResolution.services.runnableClasses.FilterListRunnable;
import net.intelie.candidateResolution.services.runnableClasses.InsertNewEvent;
import net.intelie.challenges.Event;
import net.intelie.challenges.EventIterator;
import net.intelie.challenges.EventStore;

import java.util.concurrent.*;


public class EventStoreController implements EventStore {
    private CopyOnWriteArraySet<String> existingTypes;
    private ConcurrentHashMap<String, LinkedBlockingQueue<Event>> eventStore;

    public EventStoreController() {
        this.existingTypes = new CopyOnWriteArraySet<>();
        this.eventStore = new ConcurrentHashMap<>();
    }

    @Override
    public void insert(Event event) {

        InsertNewEvent newInsert = new InsertNewEvent(this.existingTypes, this.eventStore, event);

        ExecutorService threadExecutor = Executors.newSingleThreadExecutor();

        threadExecutor.execute(newInsert);

        threadExecutor.shutdown();
        try {
          threadExecutor.awaitTermination(100, TimeUnit.MILLISECONDS);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void removeAll(String type) {
        eventStore.remove(type);
    }

    @Override
    public EventIterator query(String type, long startTime, long endTime) {
        LinkedBlockingQueue<Event> typeList = eventStore.get(type);

        LinkedBlockingQueue<Event> queryList = new LinkedBlockingQueue<>();

        ExecutorService threadExecutor = Executors.newSingleThreadExecutor();

        threadExecutor.execute(new FilterListRunnable(typeList, queryList, startTime, endTime));

        threadExecutor.shutdown();
        try {
           threadExecutor.awaitTermination(250, TimeUnit.MILLISECONDS);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        return new QueryIterator(queryList, typeList);
    }


    public ConcurrentHashMap<String, LinkedBlockingQueue<Event>> getEventStore() {
        return eventStore;
    }
}
