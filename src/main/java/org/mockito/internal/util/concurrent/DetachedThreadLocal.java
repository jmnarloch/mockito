/*
 * Copyright (c) 2016 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockito.internal.util.concurrent;

/**
 * <p>
 * A detached local that allows for explicit control of setting and removing values from a thread-local
 * context.
 * </p>
 * Instances of this class are non-blocking and fully thread safe.
 */
public class DetachedThreadLocal<T> implements Runnable {

    final WeakConcurrentMap<Thread, T> map;

    public DetachedThreadLocal(Cleaner cleaner) {
        
    }

    public T get() {
        
    }

    /**
     * @param thread The thread for which to set a thread-local value.
     * @return The value associated with this thread.
     */
    public T get(Thread thread) {
        
    }

    public void set(T value) {
        
    }

    public void clear() {
        
    }

    /**
     * Clears all thread local references for all threads.
     */
    public void clearAll() {
        
    }

    /**
     * @param thread The thread to which this thread's thread local value should be pushed.
     * @return The value being set.
     */
    public T pushTo(Thread thread) {
        
    }

    /**
     * @param thread The thread from which the thread thread local value should be fetched.
     * @return The value being set.
     */
    public T fetchFrom(Thread thread) {
        
    }

    /**
     * @param thread The thread for which to set a thread-local value.
     * @param value The value to set.
     */
    public void define(Thread thread, T value) {
        
    }

    /**
     * @param thread The thread for which an initial value is created.
     * @return The initial value for any thread local. If no default is set, the default value is {@code null}.
     */
    protected T initialValue(Thread thread) { }

    /**
     * @param value The value that is inherited.
     * @return The inherited value.
     */
    protected T inheritValue(T value) {
        
    }

    /**
     * @return The weak map that backs this detached thread local.
     */
    public WeakConcurrentMap<Thread, T> getBackingMap() {
        
    }

    @Override
    public void run() {
        
    }

    /**
     * Determines the cleaning format. A reference is removed either by an explicitly started cleaner thread
     * associated with this instance ({@link Cleaner#THREAD}), as a result of interacting with this thread local
     * from any thread ({@link Cleaner#INLINE} or manually by submitting the detached thread local to a thread
     * ({@link Cleaner#MANUAL}).
     */
    public enum Cleaner {
        THREAD,
        INLINE,
        MANUAL
    }
}
