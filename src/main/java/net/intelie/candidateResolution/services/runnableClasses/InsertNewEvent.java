package net.intelie.candidateResolution.services.runnableClasses;

import net.intelie.challenges.Event;

import java.util.Random;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArraySet;
import java.util.concurrent.LinkedBlockingQueue;

public class InsertNewEvent implements Runnable{
    Event event;
    Random generator = new Random();
    int sleepTime;
    private CopyOnWriteArraySet<String> existingTypes;
    private ConcurrentHashMap<String, LinkedBlockingQueue<Event>> eventStore;

    public InsertNewEvent(CopyOnWriteArraySet<String> existingTypes, ConcurrentHashMap<String, LinkedBlockingQueue<Event>> eventStore, Event event) {
        this.existingTypes = existingTypes;
        this.eventStore = eventStore;
        this.sleepTime = generator.nextInt(50);
        this.event = event;
    }

    @Override
    public void run() {
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
}
