package net.intelie.candidateResolution.application;

import net.intelie.candidateResolution.services.EventStoreController;
import net.intelie.challenges.Event;

public class Program {
    public static void main(String[] args) {
        Event event = new Event("add_type", 500L);

        EventStoreController controller = new EventStoreController();

        controller.insert(event);

    }
}
