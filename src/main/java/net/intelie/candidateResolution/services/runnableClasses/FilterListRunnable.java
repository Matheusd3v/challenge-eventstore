package net.intelie.candidateResolution.services.runnableClasses;

import net.intelie.challenges.Event;

import java.util.Random;
import java.util.concurrent.LinkedBlockingQueue;


public class FilterListRunnable implements Runnable{
    Random generator = new Random();
    int sleepTime;
    LinkedBlockingQueue<Event> eventList;
    LinkedBlockingQueue<Event> response;
    long startTime;
    long endTime;

    public FilterListRunnable(LinkedBlockingQueue<Event> eventList, LinkedBlockingQueue<Event> response, long startTime, long endTime) {
        this.response = response;
        this.eventList = eventList;
        this.startTime = startTime;
        this.endTime = endTime;
        this.sleepTime = generator.nextInt(50);
    }

    @Override
    public void run() {
        eventList.forEach( event -> {
            if (event.timestamp() >= startTime && event.timestamp() < endTime) {
                response.add(event);
            }
        });

    }
}
