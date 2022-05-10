package net.intelie.challenges;

import net.intelie.candidateResolution.services.EventStoreController;
import org.junit.Before;
import org.junit.Test;

import java.util.Iterator;
import java.util.Random;
import java.util.concurrent.LinkedBlockingQueue;

import static org.junit.Assert.*;

public class EventTest {

    EventStoreController controller;

    @Before
    public void buildStore() {
        this.controller = new EventStoreController();
    }

    @Test
    public void thisIsAWarning() throws Exception {
        Event event = new Event("some_type", 123L);

        //THIS IS A WARNING:
        //Some of us (not everyone) are coverage freaks.
        assertEquals(123L, event.timestamp());
        assertEquals("some_type", event.type());
    }

    @Test
    public void createEvent() {
        Event newEvent = new Event("add_type", 500L);

        assertEquals(500, newEvent.timestamp());
        assertEquals("add_type", newEvent.type());
    }

    @Test
    public void insertEventInStore() {
        Event newEvent = new Event("add_type", 500L);
        controller.insert(newEvent);

        LinkedBlockingQueue<Event> store =  controller.getEventStore().get("add_type");

        assertTrue(store.contains(newEvent));
    }

    @Test
    public void insertThreeDifferentsTypes() {
        Event newEventA = new Event("get_type", 500L);
        Event newEventB = new Event("post_type", 700L);
        Event newEventC = new Event("patch_type", 300L);

        controller.insert(newEventA);
        controller.insert(newEventB);
        controller.insert(newEventC);

        assertTrue(controller.getEventStore().containsKey("get_type"));
        assertTrue(controller.getEventStore().containsKey("post_type"));
        assertTrue(controller.getEventStore().containsKey("patch_type"));
    }

    @Test
    public void removeAllEventType() {
        Random generate = new Random();

        for (int i = 0; i < 10; i++) {
            controller.insert(new Event("remove_type", generate.nextInt(500) ));
        }

        int typeBLength = controller.getEventStore().get("remove_type").size();

        assertEquals(typeBLength, 10);

        controller.removeAll("remove_type");

        assertNull(controller.getEventStore().get("remove_type"));
    }

    @Test
    public void removeEventByQueryIterator () {
        Event newEvent = new Event("add_type", 999L);
        controller.insert(newEvent);
        Random generate = new Random();

        for (int i = 0; i < 10; i++) {
            controller.insert(new Event("add_type", generate.nextInt(500) ));
        }

        EventIterator iterator = controller.query("add_type", 400L, 10000L);
        iterator.moveNext();

        assertTrue(controller.getEventStore().get("add_type").contains(newEvent));
        assertEquals(iterator.current(), newEvent);

        iterator.remove();

        assertFalse(controller.getEventStore().get("add_type").contains(newEvent));
    }

    @Test
    public void matchSecondtEventByQueryIterator() {
        Event newEventA = new Event("add_type", 989L);
        Event newEventB = new Event("add_type", 990L);

        controller.insert(newEventA);
        controller.insert(newEventB);

        EventIterator iterator = controller.query("add_type", 900L, 1000L);
        iterator.moveNext();
        iterator.moveNext();

        assertEquals(iterator.current(), newEventB);
    }

    @Test
    public void strictEqualMessageErrorIllegalStateException () {
        Exception exception = assertThrows(IllegalStateException.class, () -> {
            Event newEventA = new Event("add_type", 1000L);
            controller.insert(newEventA);

            EventIterator iterator = controller.query("add_type", 10L, 1001L);

            iterator.moveNext();
            iterator.moveNext();

            iterator.current();
        });

        String expectedMessage = "MoveNext was never called or its last result was false";
        String actualMessage = exception.getMessage();

        assertTrue(actualMessage.contains(expectedMessage));
    }

    @Test
    public void insert1000EventsInStore () {
        Random generate = new Random();

        for (int i = 0; i < 500; i++) {
            controller.insert(new Event("add_type", generate.nextInt(900) ));
            controller.insert(new Event("test_type", generate.nextInt(900) ));
        }

        int typeALength = controller.getEventStore().get("add_type").size();
        int typeBLength = controller.getEventStore().get("test_type").size();

        assertEquals(typeALength, 500);
        assertEquals(typeBLength, 500);
    }


    @Test
    public void insert10000EventsInStore () {
        Random generate = new Random();

        for (int i = 0; i < 5000; i++) {
            controller.insert(new Event("add_type", generate.nextInt(900) ));
            controller.insert(new Event("test_type", generate.nextInt(900) ));
        }

        int typeALength = controller.getEventStore().get("add_type").size();
        int typeBLength = controller.getEventStore().get("test_type").size();

        assertEquals(typeALength, 5000);
        assertEquals(typeBLength, 5000);
    }


}