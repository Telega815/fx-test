package org.example;

import org.junit.jupiter.api.Test;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import static org.junit.jupiter.api.Assertions.*;

class RingBufferTest {

    @Test
    public void put_test() throws InterruptedException {
        String expected = "expected";

        RingBuffer ringBuffer = new RingBuffer(1);

        ringBuffer.put(expected);

        assertTrue(true);
    }

    @Test
    public void get_test() throws InterruptedException {
        String expected = "expected";

        RingBuffer ringBuffer = new RingBuffer(1);

        ringBuffer.put(expected);

        Object actual = ringBuffer.get();

        assertEquals(actual, expected);
    }

//    @Test
//    public void get_from_empty_buffer() {
//        RingBuffer ringBuffer = new RingBuffer(1);
//
//        assertThrows(EmptyBufferException.class, ringBuffer::get);
//    }
//
//    @Test
//    public void put_more_than_capacity() throws InterruptedException {
//        String expected = "expected";
//        String expected2 = "expected2";
//
//        RingBuffer ringBuffer = new RingBuffer(1);
//
//        ringBuffer.put(expected);
//
//        assertThrows(ArrayIndexOutOfBoundsException.class, () -> ringBuffer.put(expected2));
//    }
//
    @Test
    public void get_two_elements() throws InterruptedException {
        String expected = "expected";
        String expected2 = "expected2";

        RingBuffer ringBuffer = new RingBuffer(2);

        ringBuffer.put(expected);
        ringBuffer.put(expected2);

        assertEquals(expected, ringBuffer.get());
        assertEquals(expected2, ringBuffer.get());
    }

    @Test
    public void insert_two_than_read_than_insert() throws InterruptedException {
        String expected = "expected";
        String expected2 = "expected2";
        String expected3 = "expected3";

        RingBuffer ringBuffer = new RingBuffer(2);

        ringBuffer.put(expected);
        ringBuffer.put(expected2);

        assertEquals(expected, ringBuffer.get());

        ringBuffer.put(expected3);

        assertEquals(expected2, ringBuffer.get());
        assertEquals(expected3, ringBuffer.get());
    }

    @Test
    public void insert_two_than_read_than_insert_than_insert() throws InterruptedException {
        String expected = "expected";
        String expected2 = "expected2";
        String expected3 = "expected3";
        String expected4 = "expected4";

        RingBuffer ringBuffer = new RingBuffer(3);

        ringBuffer.put(expected);
        ringBuffer.put(expected2);

        assertEquals(expected, ringBuffer.get());
        assertEquals(expected2, ringBuffer.get());

        ringBuffer.put(expected3);
        ringBuffer.put(expected4);

        assertEquals(expected3, ringBuffer.get());
        assertEquals(expected4, ringBuffer.get());
    }

    @Test
    public void get_waits_put() throws InterruptedException, ExecutionException {
        String expected = "expected";

        ExecutorService executorService = Executors.newSingleThreadExecutor();

        RingBuffer ringBuffer = new RingBuffer(1);

        Future<Object> actual = executorService.submit(ringBuffer::get);

        Thread.sleep(1000);

        ringBuffer.put(expected);

        assertEquals(expected, actual.get());
    }

    @Test
    public void put_waits_get() throws InterruptedException {
        String expected = "expected";
        String expected2 = "expected2";

        ExecutorService executorService = Executors.newSingleThreadExecutor();

        RingBuffer ringBuffer = new RingBuffer(1);

        ringBuffer.put(expected);
        executorService.submit(() -> {
            ringBuffer.put(expected2);
            return true;
        });

        Thread.sleep(1000);

        var actual = ringBuffer.get();
        var actual2 = ringBuffer.get();

        assertEquals(expected, actual);
        assertEquals(expected2, actual2);
    }
}