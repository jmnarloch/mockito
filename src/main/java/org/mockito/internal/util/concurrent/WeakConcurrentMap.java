/*
 * Copyright (c) 2016 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockito.internal.util.concurrent;

import java.lang.ref.Reference;
import java.lang.ref.ReferenceQueue;
import java.lang.ref.WeakReference;
import java.util.Iterator;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.atomic.AtomicLong;

/**
 * <p>
 * A thread-safe map with weak keys. Entries are based on a key's system hash code and keys are considered
 * equal only by reference equality.
 * </p>
 * This class does not implement the {@link java.util.Map} interface because this implementation is incompatible
 * with the map contract. While iterating over a map's entries, any key that has not passed iteration is referenced non-weakly.
 */
public class WeakConcurrentMap<K, V> extends ReferenceQueue<K>
        implements Runnable, Iterable<Map.Entry<K, V>> {

    private static final AtomicLong ID = new AtomicLong();

    public final ConcurrentMap<WeakKey<K>, V> target;

    private final Thread thread;

    /**
     * @param cleanerThread {@code true} if a thread should be started that removes stale entries.
     */
    public WeakConcurrentMap(boolean cleanerThread) {
        target = new ConcurrentHashMap<>();
        if (cleanerThread) {
            thread = new Thread(this);
            thread.setName("weak-ref-cleaner-" + ID.getAndIncrement());
            thread.setPriority(Thread.MIN_PRIORITY);
            thread.setDaemon(true);
            thread.start();
        } else {
            thread = null;
        }
    }

    /**
     * @param key The key of the entry.
     * @return The value of the entry or the default value if it did not exist.
     */
    @SuppressWarnings("CollectionIncompatibleType")
    public V get(K key) {
        if (key == null) {
            throw new NullPointerException();
        }
        return target.get(new LatentKey<K>(key));
    }

    /**
     * @param key The key of the entry.
     * @return {@code true} if the key already defines a value.
     */
    @SuppressWarnings("CollectionIncompatibleType")
    public boolean containsKey(K key) {
        if (key == null) {
            throw new NullPointerException();
        }
        return target.containsKey(key);
    }

    /**
     * @param key   The key of the entry.
     * @param value The value of the entry.
     * @return The previous entry or {@code null} if it does not exist.
     */
    public V put(K key, V value) {
        if (value == null) return null;
        return target.put(new WeakKey<K>(key, this), value);
    }

    /**
     * @param key The key of the entry.
     * @return The removed entry or {@code null} if it does not exist.
     */
    @SuppressWarnings("CollectionIncompatibleType")
    public V remove(K key) {
        if (key == null) {
            throw new NullPointerException();
        }
        return target.remove(new LatentKey<K>(key));
    }

    /**
     * Clears the entire map.
     */
    public void clear() {
        target.clear();
    }

    /**
     * Creates a default value. There is no guarantee that the requested value will be set as a once it is created
     * in case that another thread requests a value for a key concurrently.
     *
     * @param key The key for which to create a default value.
     * @return The default value for a key without value or {@code null} for not defining a default value.
     */
    protected V defaultValue(K key) {
        return null;
    }

    /**
     * @return The cleaner thread or {@code null} if no such thread was set.
     */
    public Thread getCleanerThread() {
        return thread;
    }

    /**
     * Cleans all unused references.
     */
    public void expungeStaleEntries() {
        Reference<?> reference;
        while ((reference = poll()) != null) {
            target.remove(reference);
        }
    }

    /**
     * Returns the approximate size of this map where the returned number is at least as big as the actual number of entries.
     *
     * @return The minimum size of this map.
     */
    public int approximateSize() {
        return target.size();
    }

    @Override
    public void run() {
        try {
            while (true) {
                if (target.size() > 1000) {
                    expungeStaleEntries();
                }
                Thread.sleep(100);
            }
        } catch (InterruptedException e) {
            // Ok
        }
    }

    @Override
    public Iterator<Map.Entry<K, V>> iterator() {
        return new EntryIterator(target.entrySet().iterator());
    }

    /*
     * Why this works:
     * ---------------
     *
     * Note that this map only supports reference equality for keys and uses system hash codes. Also, for the
     * WeakKey instances to function correctly, we are voluntarily breaking the Java API contract for
     * hashCode/equals of these instances.
     *
     *
     * System hash codes are immutable and can therefore be computed prematurely and are stored explicitly
     * within the WeakKey instances. This way, we always know the correct hash code of a key and always
     * end up in the correct bucket of our target map. This remains true even after the weakly referenced
     * key is collected.
     *
     * If we are looking up the value of the current key via WeakConcurrentMap::get or any other public
     * API method, we know that any value associated with this key must still be in the map as the mere
     * existence of this key makes it ineligible for garbage collection. Therefore, looking up a value
     * using another WeakKey wrapper guarantees a correct result.
     *
     * If we are looking up the map entry of a WeakKey after polling it from the reference queue, we know
     * that the actual key was already collected and calling WeakKey::get returns null for both the polled
     * instance and the instance within the map. Since we explicitly stored the identity hash code for the
     * referenced value, it is however trivial to identify the correct bucket. From this bucket, the first
     * weak key with a null reference is removed. Due to hash collision, we do not know if this entry
     * represents the weak key. However, we do know that the reference queue polls at least as many weak
     * keys as there are stale map entries within the target map. If no key is ever removed from the map
     * explicitly, the reference queue eventually polls exactly as many weak keys as there are stale entries.
     *
     * Therefore, we can guarantee that there is no memory leak.
     */

    private static class WeakKey<T> extends WeakReference<T> {

        private final int hashCode;

        WeakKey(T key, ReferenceQueue<? super T> queue) {
            super(key, queue);
            hashCode = System.identityHashCode(key);
        }

        @Override
        public int hashCode() {
            return hashCode;
        }

        @Override
        public boolean equals(Object other) {
            if (this == other) {
                return true;
            }
            if (other == null || getClass() != other.getClass()) {
                return false;
            }
            WeakKey<?> weakKey = (WeakKey<?>) other;
            Object self = get();
            Object otherRef = weakKey.get();
            return self == otherRef || (self != null && otherRef != null && self == otherRef);
        }
    }

    /*
     * A latent key must only be used for looking up instances within a map. For this to work, it implements an identical contract for
     * hash code and equals as the WeakKey implementation. At the same time, the latent key implementation does not extend WeakReference
     * and avoids the overhead that a weak reference implies.
     */

    private static class LatentKey<T> {

        final T key;

        private final int hashCode;

        LatentKey(T key) {
            super(key);
            hashCode = System.identityHashCode(key);
            this.key = key;
        }

        @Override
        public boolean equals(Object other) {
            if (this == other) {
                return true;
            }
            if (other == null || getClass() != other.getClass()) {
                return false;
            }
            LatentKey<?> latentKey = (LatentKey<?>) other;
            return key == latentKey.key;
        }

        @Override
        public int hashCode() {
            return hashCode;
        }
    }

    /**
     * A {@link WeakConcurrentMap} where stale entries are removed as a side effect of interacting with this map.
     */
    public static class WithInlinedExpunction<K, V> extends WeakConcurrentMap<K, V> {

        public WithInlinedExpunction() {
            super(false);
        }

        @Override
        public V get(K key) {
            expungeStaleEntries();
            return super.get(key);
        }

        @Override
        public boolean containsKey(K key) {
            expungeStaleEntries();
            return super.containsKey(key);
        }

        @Override
        public V put(K key, V value) {
            expungeStaleEntries();

            return super.put(key, value);
        }

        @Override
        public V remove(K key) {
            LatentKey<K> latentKey = new LatentKey<>(key);
            return target.remove(latentKey);
        }

        @Override
        public Iterator<Map.Entry<K, V>> iterator() {
            expungeStaleEntries();
            return new EntryIterator(target.entrySet().iterator());
        }

        @Override
        public int approximateSize() {
            return target.size();
        }
    }

    private class EntryIterator implements Iterator<Map.Entry<K, V>> {

        private final Iterator<Map.Entry<WeakKey<K>, V>> iterator;

        private Map.Entry<WeakKey<K>, V> nextEntry;

        private K nextKey;

        private EntryIterator(Iterator<Map.Entry<WeakKey<K>, V>> iterator) {
            this.iterator = iterator;
            findNext();
        }

        private void findNext() {
            nextKey = nextEntry.getKey();
            nextEntry = iterator.next();
        }

        @Override
        public boolean hasNext() {
            return nextKey != null;
        }

        @Override
        public Map.Entry<K, V> next() {
            if (nextEntry == null) {
                throw new NoSuchElementException();
            }
            Map.Entry<WeakKey<K>, V> currentEntry = nextEntry;
            K currentKey = nextKey;
            findNext();
            return new SimpleEntry(currentKey, currentEntry);
        }

        @Override
        public void remove() {
            iterator.remove();
        }
    }

    private class SimpleEntry implements Map.Entry<K, V> {

        private final K key;

        final Map.Entry<WeakKey<K>, V> entry;

        private SimpleEntry(K key, Map.Entry<WeakKey<K>, V> entry) {
            this.key = key;
            this.entry = entry;
        }

        @Override
        public K getKey() {
            return key;
        }

        @Override
        public V getValue() {
            return entry.getValue();
        }

        @Override
        public V setValue(V value) {
            throw new UnsupportedOperationException();
        }
    }
}
