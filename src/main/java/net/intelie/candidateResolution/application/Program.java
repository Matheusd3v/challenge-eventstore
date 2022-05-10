package net.intelie.candidateResolution.application;

import net.intelie.candidateResolution.services.EventStoreController;
import net.intelie.challenges.Event;
import net.intelie.challenges.EventIterator;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.Scanner;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.LinkedBlockingQueue;

public class Program {
    public static void main(String[] args) {
//
//        Thread t = Thread.currentThread();
//
//        System.out.println(t.getName());
//
        Event event1 = new Event("A", 500);
        Event event2 = new Event("A", 800);
        Event event3 = new Event("A", 600);
        Event event4 = new Event("A", 650);
        Event event5 = new Event("B", 500);
//
        EventStoreController controller = new EventStoreController();
//
        System.out.println("First store: ");
        System.out.println();
        System.out.println(controller.getEventStore());
//
        System.out.println();
//
        controller.insert(event1);
        controller.insert(event2);
        controller.insert(event3);
        controller.insert(event4);
        controller.insert(event5);
//
        System.out.println(controller.getEventStore());
//
        EventIterator teste = controller.query("A", 100, 150);
//
        teste.moveNext();
        teste.moveNext();
        teste.moveNext();
////
        System.out.println(teste.current());
////
        teste.remove();
//
        System.out.println(controller.getEventStore());

       controller.removeAll("A");

        System.out.println(controller.getEventStore());

//


    }
}
