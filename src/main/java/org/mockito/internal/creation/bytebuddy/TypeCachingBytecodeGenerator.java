/*
 * Copyright (c) 2016 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockito.internal.creation.bytebuddy;

import java.lang.ref.ReferenceQueue;
import java.util.Set;
import java.util.concurrent.Callable;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

import net.bytebuddy.TypeCache;
import org.mockito.mock.SerializableMode;

class TypeCachingBytecodeGenerator extends ReferenceQueue<ClassLoader>
        implements BytecodeGenerator {
    /**
     * The size of the {@link #cacheLocks}.
     * <p>Caution: This must match the {@link #CACHE_LOCK_MASK}.
     */
    private static final int CACHE_LOCK_SIZE = 16;

    /**
     * The mask to use to mask out the {@link MockitoMockKey#hashCode()} to find the {@link #cacheLocks}.
     * <p>Caution: this must match the bits of the {@link #CACHE_LOCK_SIZE}.
     */
    private static final int CACHE_LOCK_MASK = 0x0F;

    private final BytecodeGenerator bytecodeGenerator;

    private final TypeCache<MockitoMockKey> typeCache;

    private final ReadWriteLock lock = new ReentrantReadWriteLock();

    /**
     * This array contains {@link TypeCachingLock} instances, which are used as java monitor locks for
     * {@link TypeCache#findOrInsert(ClassLoader, Object, Callable, Object)}.
     * The locks spread the lock to acquire over multiple locks instead of using a single lock
     * {@code BOOTSTRAP_LOCK} for all {@link MockitoMockKey}.
     *
     * <p>Note: We can't simply use the mockedType class lock as a lock,
     * because the {@link MockitoMockKey}, will be the same for different mockTypes + interfaces.
     *
     * <p>#3035: Excessive locking in TypeCachingBytecodeGenerator#BOOTSTRAP_LOCK
     */
    private final TypeCachingLock[] cacheLocks;

    public TypeCachingBytecodeGenerator(BytecodeGenerator bytecodeGenerator, boolean weak) {
        
    }

    @SuppressWarnings("unchecked")
    @Override
    public <T> Class<T> mockClass(final MockFeatures<T> params) {
        
    }

    /**
     * Returns a {@link TypeCachingLock}, which locks the {@link TypeCache#findOrInsert(ClassLoader, Object, Callable, Object)}.
     *
     * @param key the key to lock
     * @return the {@link TypeCachingLock} to use to lock the {@link TypeCache}
     */
    private TypeCachingLock getCacheLockForKey(MockitoMockKey key) {
        
    }

    @Override
    public void mockClassStatic(Class<?> type) {
        
    }

    @Override
    public void mockClassConstruction(Class<?> type) {
        
    }

    @Override
    public void clearAllCaches() {
        
    }

    private static final class TypeCachingLock {}

    private static class MockitoMockKey extends TypeCache.SimpleKey {

        private final SerializableMode serializableMode;
        private final boolean stripAnnotations;

        private MockitoMockKey(
                Class<?> type,
                Set<Class<?>> additionalType,
                SerializableMode serializableMode,
                boolean stripAnnotations) {
            
        }

        @Override
        public boolean equals(Object object) {
            
        }

        @Override
        public int hashCode() {
            
        }
    }
}
