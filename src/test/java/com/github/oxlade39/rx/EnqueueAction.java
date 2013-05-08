package com.github.oxlade39.rx;

import rx.util.functions.Action1;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

/**
* @author dan
*/
public class EnqueueAction<T> implements Action1<T> {

    private final BlockingQueue<T> enqueued;

    public EnqueueAction() {
        this.enqueued = new LinkedBlockingQueue<>();
    }

    @Override
    public void call(T item) {
        enqueued.add(item);
    }

    public BlockingQueue<T> getEnqueued() {
        return enqueued;
    }

    public T next() {
        return enqueued.poll();
    }
}
