package org.example;


import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * 1. FIFO
 * <p>
 * 2. Ring buffer, use all space in the buffer. Throw exception in corner cases:  no free space (on put) or data (on get)e.g.
 * <p>
 * created empty buffer with size = 3   _,_,_
 * get -> no data, buffer is empty (exception: non thread-save , block: thread-safe)
 * put 1 ->     1,_,_
 * put 2 ->     1,2,_
 * put 3 ->     1,2,3
 * put 4 -> buffer is full  (exception: non thread-save , block: thread-safe)
 * get -> 1     _,2,3
 * put 4 ->    4,2,3
 * get -> 2     4,_,3
 * <p>
 * 3. blocking when no free place (on put) or data (on get)
 * <p>
 * 4. Thread safe
 */
public class RingBuffer {

    private final Object[] data;
    private int size;
    private int pointer;
    private int getPointer;

    private final Lock lock = new ReentrantLock();
    private final Condition notFull  = lock.newCondition();
    private final Condition notEmpty = lock.newCondition();

    public RingBuffer(int maxBufferSize) {
        data = new Object[maxBufferSize];

        size = 0;
        pointer = 0;
        getPointer = 0;
    }

    public void put(Object o) throws InterruptedException {

        lock.lock();
        try {
            while (size == data.length)
                notFull.await();

            size++;
            data[pointer] = o;

            if (pointer == data.length - 1) {
                pointer = 0;
            } else {
                pointer++;
            }

            notEmpty.signal();
        } finally {
            lock.unlock();
        }
    }

    public Object get() throws InterruptedException {
        lock.lock();
        try {
            while (size == 0)
                notEmpty.await();

            Object res = data[getPointer];

            if (getPointer == data.length - 1) {
                getPointer = 0;
            } else {
                getPointer++;
            }
            size--;

            notFull.signal();

            return res;
        } finally {
            lock.unlock();
        }
    }
}


