package net.intelie.candidateResolution.services;

import net.intelie.challenges.Event;
import net.intelie.challenges.EventIterator;
import net.intelie.challenges.EventStore;

import java.util.concurrent.*;

import static java.util.stream.Collectors.toCollection;

public class EventStoreController implements EventStore {
    private CopyOnWriteArraySet<String> existingTypes;
    private ConcurrentHashMap<String, LinkedBlockingQueue<Event>> eventStore;

    public EventStoreController() {
        this.existingTypes = new CopyOnWriteArraySet<>();
        this.eventStore = new ConcurrentHashMap<>();
    }

    @Override
    public void insert(Event event) {
        if (!existingTypes.contains(event.type())) {
            existingTypes.add(event.type());

            LinkedBlockingQueue<Event> newList = new LinkedBlockingQueue<>();

            newList.add(event);
            eventStore.put(event.type(), newList);

            return;
        }

        LinkedBlockingQueue<Event> listToAdd = eventStore.get(event.type());

        listToAdd.add(event);
    }

    @Override
    public void removeAll(String type) {
        eventStore.remove(type);
    }

    @Override
    public EventIterator query(String type, long startTime, long endTime) {
        LinkedBlockingQueue<Event> typeList = eventStore.get(type);

        LinkedBlockingQueue<Event> queryList =  filterList(typeList, startTime, endTime);

        return new QueryIterator(queryList, typeList);
    }

    private LinkedBlockingQueue<Event> filterList(LinkedBlockingQueue<Event> eventList, long startTime, long endTime) {

        return eventList.stream()
                .filter(event -> event.timestamp() >= startTime && event.timestamp() < endTime)
                .collect(toCollection(LinkedBlockingQueue::new));
    }

    public ConcurrentHashMap<String, LinkedBlockingQueue<Event>> getEventStore() {
        return eventStore;
    }
}
