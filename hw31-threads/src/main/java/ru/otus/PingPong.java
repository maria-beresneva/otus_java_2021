package ru.otus;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class PingPong {
    private static final Logger logger = LoggerFactory.getLogger(PingPong.class);
    private final static Integer MAX_COUNT = 10;
    private Integer lastId = 2;

    private synchronized void action(Integer threadId, Integer counter, Boolean direction) {
        while(!Thread.currentThread().isInterrupted()) {
            try {
                while (lastId.equals(threadId)) {
                    this.wait();
                }
                counter = (direction) ? counter + 1: counter - 1;
                if(counter > 0) {
                    logger.debug("Thread with id {} - {}", threadId, counter);
                }
                if(counter.equals(MAX_COUNT)) direction = false;
                if(counter.equals(0)) direction = true;

                lastId = threadId;
                sleep();
                notifyAll();
            } catch (InterruptedException ex) {
                Thread.currentThread().interrupt();
            }
        }
    }

    public static void main(String[] args) {
        PingPong pingPong = new PingPong();
        new Thread(() -> pingPong.action(1, 0, true)).start();
        new Thread(() -> pingPong.action(2, 0, true)).start();
    }

    private static void sleep() {
        try {
            Thread.sleep(1_000);
        } catch (InterruptedException e) {
            logger.error(e.getMessage());
            Thread.currentThread().interrupt();
        }
    }
}
