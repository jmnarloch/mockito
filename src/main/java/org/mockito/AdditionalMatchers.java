/*
 * Copyright (c) 2007 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockito;

import static org.mockito.internal.progress.ThreadSafeMockingProgress.mockingProgress;

import org.mockito.internal.matchers.ArrayEquals;
import org.mockito.internal.matchers.CompareEqual;
import org.mockito.internal.matchers.EqualsWithDelta;
import org.mockito.internal.matchers.Find;
import org.mockito.internal.matchers.GreaterOrEqual;
import org.mockito.internal.matchers.GreaterThan;
import org.mockito.internal.matchers.LessOrEqual;
import org.mockito.internal.matchers.LessThan;

/**
 * See {@link ArgumentMatchers} for general info about matchers.
 * <p>
 * AdditionalMatchers provides rarely used matchers, kept only for somewhat compatibility with EasyMock.
 * Use additional matchers very judiciously because they may impact readability of a test.
 * It is recommended to use matchers from {@link ArgumentMatchers} and keep stubbing and verification simple.
 * <p>
 * Example of using logical and(), not(), or() matchers:
 *
 * <pre class="code"><code class="java">
 *   //anything but not "ejb"
 *   mock.someMethod(not(eq("ejb")));
 *
 *   //not "ejb" and not "michael jackson"
 *   mock.someMethod(and(not(eq("ejb")), not(eq("michael jackson"))));
 *
 *   //1 or 10
 *   mock.someMethod(or(eq(1), eq(10)));
 * </code></pre>
 *
 * Scroll down to see all methods - full list of matchers.
 */
@SuppressWarnings("ALL")
public final class AdditionalMatchers {

    /**
     * argument greater than or equal the given value.
     * <p>
     * See examples in javadoc for {@link AdditionalMatchers} class
     *
     * @param value
     *            the given value.
     * @return <code>null</code>.
     */
    public static <T extends Comparable<T>> T geq(T value) {
        
    }

    /**
     * byte argument greater than or equal to the given value.
     * <p>
     * See examples in javadoc for {@link AdditionalMatchers} class
     *
     * @param value
     *            the given value.
     * @return <code>0</code>.
     */
    public static byte geq(byte value) {
        
    }

    /**
     * double argument greater than or equal to the given value.
     * <p>
     * See examples in javadoc for {@link AdditionalMatchers} class
     *
     * @param value
     *            the given value.
     * @return <code>0</code>.
     */
    public static double geq(double value) {
        
    }

    /**
     * float argument greater than or equal to the given value.
     * <p>
     * See examples in javadoc for {@link AdditionalMatchers} class
     *
     * @param value
     *            the given value.
     * @return <code>0</code>.
     */
    public static float geq(float value) {
        
    }

    /**
     * int argument greater than or equal to the given value.
     * <p>
     * See examples in javadoc for {@link AdditionalMatchers} class
     *
     * @param value
     *            the given value.
     * @return <code>0</code>.
     */
    public static int geq(int value) {
        
    }

    /**
     * long argument greater than or equal to the given value.
     * <p>
     * See examples in javadoc for {@link AdditionalMatchers} class
     *
     * @param value
     *            the given value.
     * @return <code>0</code>.
     */
    public static long geq(long value) {
        
    }

    /**
     * short argument greater than or equal to the given value.
     * <p>
     * See examples in javadoc for {@link AdditionalMatchers} class
     *
     * @param value
     *            the given value.
     * @return <code>0</code>.
     */
    public static short geq(short value) {
        
    }

    /**
     * comparable argument less than or equal the given value details.
     * <p>
     * See examples in javadoc for {@link AdditionalMatchers} class
     *
     * @param value
     *            the given value.
     * @return <code>null</code>.
     */
    public static <T extends Comparable<T>> T leq(T value) {
        
    }

    /**
     * byte argument less than or equal to the given value.
     * <p>
     * See examples in javadoc for {@link AdditionalMatchers} class
     *
     * @param value
     *            the given value.
     * @return <code>0</code>.
     */
    public static byte leq(byte value) {
        
    }

    /**
     * double argument less than or equal to the given value.
     * <p>
     * See examples in javadoc for {@link AdditionalMatchers} class
     *
     * @param value
     *            the given value.
     * @return <code>0</code>.
     */
    public static double leq(double value) {
        
    }

    /**
     * float argument less than or equal to the given value.
     * <p>
     * See examples in javadoc for {@link AdditionalMatchers} class
     *
     * @param value
     *            the given value.
     * @return <code>0</code>.
     */
    public static float leq(float value) {
        
    }

    /**
     * int argument less than or equal to the given value.
     * <p>
     * See examples in javadoc for {@link AdditionalMatchers} class
     *
     * @param value
     *            the given value.
     * @return <code>0</code>.
     */
    public static int leq(int value) {
        
    }

    /**
     * long argument less than or equal to the given value.
     * <p>
     * See examples in javadoc for {@link AdditionalMatchers} class
     *
     * @param value
     *            the given value.
     * @return <code>0</code>.
     */
    public static long leq(long value) {
        
    }

    /**
     * short argument less than or equal to the given value.
     * <p>
     * See examples in javadoc for {@link AdditionalMatchers} class
     *
     * @param value
     *            the given value.
     * @return <code>0</code>.
     */
    public static short leq(short value) {
        
    }

    /**
     * comparable argument greater than the given value.
     * <p>
     * See examples in javadoc for {@link AdditionalMatchers} class
     *
     * @param value
     *            the given value.
     * @return <code>null</code>.
     */
    public static <T extends Comparable<T>> T gt(T value) {
        
    }

    /**
     * byte argument greater than the given value.
     * <p>
     * See examples in javadoc for {@link AdditionalMatchers} class
     *
     * @param value
     *            the given value.
     * @return <code>0</code>.
     */
    public static byte gt(byte value) {
        
    }

    /**
     * double argument greater than the given value.
     * <p>
     * See examples in javadoc for {@link AdditionalMatchers} class
     *
     * @param value
     *            the given value.
     * @return <code>0</code>.
     */
    public static double gt(double value) {
        
    }

    /**
     * float argument greater than the given value.
     * <p>
     * See examples in javadoc for {@link AdditionalMatchers} class
     *
     * @param value
     *            the given value.
     * @return <code>0</code>.
     */
    public static float gt(float value) {
        
    }

    /**
     * int argument greater than the given value.
     * <p>
     * See examples in javadoc for {@link AdditionalMatchers} class
     *
     * @param value
     *            the given value.
     * @return <code>0</code>.
     */
    public static int gt(int value) {
        
    }

    /**
     * long argument greater than the given value.
     * <p>
     * See examples in javadoc for {@link AdditionalMatchers} class
     *
     * @param value
     *            the given value.
     * @return <code>0</code>.
     */
    public static long gt(long value) {
        
    }

    /**
     * short argument greater than the given value.
     * <p>
     * See examples in javadoc for {@link AdditionalMatchers} class
     *
     * @param value
     *            the given value.
     * @return <code>0</code>.
     */
    public static short gt(short value) {
        
    }

    /**
     * comparable argument less than the given value.
     * <p>
     * See examples in javadoc for {@link AdditionalMatchers} class
     *
     * @param value
     *            the given value.
     * @return <code>null</code>.
     */
    public static <T extends Comparable<T>> T lt(T value) {
        
    }

    /**
     * byte argument less than the given value.
     * <p>
     * See examples in javadoc for {@link AdditionalMatchers} class
     *
     * @param value
     *            the given value.
     * @return <code>0</code>.
     */
    public static byte lt(byte value) {
        
    }

    /**
     * double argument less than the given value.
     * <p>
     * See examples in javadoc for {@link AdditionalMatchers} class
     *
     * @param value
     *            the given value.
     * @return <code>0</code>.
     */
    public static double lt(double value) {
        
    }

    /**
     * float argument less than the given value.
     * <p>
     * See examples in javadoc for {@link AdditionalMatchers} class
     *
     * @param value
     *            the given value.
     * @return <code>0</code>.
     */
    public static float lt(float value) {
        
    }

    /**
     * int argument less than the given value.
     * <p>
     * See examples in javadoc for {@link AdditionalMatchers} class
     *
     * @param value
     *            the given value.
     * @return <code>0</code>.
     */
    public static int lt(int value) {
        
    }

    /**
     * long argument less than the given value.
     * <p>
     * See examples in javadoc for {@link AdditionalMatchers} class
     *
     * @param value
     *            the given value.
     * @return <code>0</code>.
     */
    public static long lt(long value) {
        
    }

    /**
     * short argument less than the given value.
     * <p>
     * See examples in javadoc for {@link AdditionalMatchers} class
     *
     * @param value
     *            the given value.
     * @return <code>0</code>.
     */
    public static short lt(short value) {
        
    }

    /**
     * comparable argument equals to the given value according to their
     * compareTo method.
     * <p>
     * See examples in javadoc for {@link AdditionalMatchers} class
     *
     * @param value
     *            the given value.
     * @return <code>null</code>.
     */
    public static <T extends Comparable<T>> T cmpEq(T value) {
        
    }

    /**
     * String argument that contains a substring that matches the given regular
     * expression.
     *
     * @param regex
     *            the regular expression.
     * @return <code>null</code>.
     */
    public static String find(String regex) {
        
    }

    /**
     * Object array argument that is equal to the given array, i.e. it has to
     * have the same type, length, and each element has to be equal.
     * <p>
     * See examples in javadoc for {@link AdditionalMatchers} class
     *
     * @param <T>
     *            the type of the array, it is passed through to prevent casts.
     * @param value
     *            the given array.
     * @return <code>null</code>.
     */
    public static <T> T[] aryEq(T[] value) {
        
    }

    /**
     * short array argument that is equal to the given array, i.e. it has to
     * have the same length, and each element has to be equal.
     * <p>
     * See examples in javadoc for {@link AdditionalMatchers} class
     *
     * @param value
     *            the given array.
     * @return <code>null</code>.
     */
    public static short[] aryEq(short[] value) {
        
    }

    /**
     * long array argument that is equal to the given array, i.e. it has to have
     * the same length, and each element has to be equal.
     * <p>
     * See examples in javadoc for {@link AdditionalMatchers} class
     *
     * @param value
     *            the given array.
     * @return <code>null</code>.
     */
    public static long[] aryEq(long[] value) {
        
    }

    /**
     * int array argument that is equal to the given array, i.e. it has to have
     * the same length, and each element has to be equal.
     * <p>
     * See examples in javadoc for {@link AdditionalMatchers} class
     *
     * @param value
     *            the given array.
     * @return <code>null</code>.
     */
    public static int[] aryEq(int[] value) {
        
    }

    /**
     * float array argument that is equal to the given array, i.e. it has to
     * have the same length, and each element has to be equal.
     * <p>
     * See examples in javadoc for {@link AdditionalMatchers} class
     *
     * @param value
     *            the given array.
     * @return <code>null</code>.
     */
    public static float[] aryEq(float[] value) {
        
    }

    /**
     * double array argument that is equal to the given array, i.e. it has to
     * have the same length, and each element has to be equal.
     * <p>
     * See examples in javadoc for {@link AdditionalMatchers} class
     *
     * @param value
     *            the given array.
     * @return <code>null</code>.
     */
    public static double[] aryEq(double[] value) {
        
    }

    /**
     * char array argument that is equal to the given array, i.e. it has to have
     * the same length, and each element has to be equal.
     * <p>
     * See examples in javadoc for {@link AdditionalMatchers} class
     *
     * @param value
     *            the given array.
     * @return <code>null</code>.
     */
    public static char[] aryEq(char[] value) {
        
    }

    /**
     * byte array argument that is equal to the given array, i.e. it has to have
     * the same length, and each element has to be equal.
     * <p>
     * See examples in javadoc for {@link AdditionalMatchers} class
     *
     * @param value
     *            the given array.
     * @return <code>null</code>.
     */
    public static byte[] aryEq(byte[] value) {
        
    }

    /**
     * boolean array argument that is equal to the given array, i.e. it has to
     * have the same length, and each element has to be equal.
     * <p>
     * See examples in javadoc for {@link AdditionalMatchers} class
     *
     * @param value
     *            the given array.
     * @return <code>null</code>.
     */
    public static boolean[] aryEq(boolean[] value) {
        
    }

    /**
     * boolean argument that matches both given matchers.
     * <p>
     * See examples in javadoc for {@link AdditionalMatchers} class
     *
     * @param first
     *            placeholder for the first argument matcher.
     * @param second
     *            placeholder for the second argument matcher.
     * @return <code>false</code>.
     */
    public static boolean and(boolean first, boolean second) {
        
    }

    /**
     * byte argument that matches both given argument matchers.
     * <p>
     * See examples in javadoc for {@link AdditionalMatchers} class
     *
     * @param first
     *            placeholder for the first argument matcher.
     * @param second
     *            placeholder for the second argument matcher.
     * @return <code>0</code>.
     */
    public static byte and(byte first, byte second) {
        
    }

    /**
     * char argument that matches both given argument matchers.
     * <p>
     * See examples in javadoc for {@link AdditionalMatchers} class
     *
     * @param first
     *            placeholder for the first argument matcher.
     * @param second
     *            placeholder for the second argument matcher.
     * @return <code>0</code>.
     */
    public static char and(char first, char second) {
        
    }

    /**
     * double argument that matches both given argument matchers.
     * <p>
     * See examples in javadoc for {@link AdditionalMatchers} class
     *
     * @param first
     *            placeholder for the first argument matcher.
     * @param second
     *            placeholder for the second argument matcher.
     * @return <code>0</code>.
     */
    public static double and(double first, double second) {
        
    }

    /**
     * float argument that matches both given argument matchers.
     * <p>
     * See examples in javadoc for {@link AdditionalMatchers} class
     *
     * @param first
     *            placeholder for the first argument matcher.
     * @param second
     *            placeholder for the second argument matcher.
     * @return <code>0</code>.
     */
    public static float and(float first, float second) {
        
    }

    /**
     * int argument that matches both given argument matchers.
     * <p>
     * See examples in javadoc for {@link AdditionalMatchers} class
     *
     * @param first
     *            placeholder for the first argument matcher.
     * @param second
     *            placeholder for the second argument matcher.
     * @return <code>0</code>.
     */
    public static int and(int first, int second) {
        
    }

    /**
     * long argument that matches both given argument matchers.
     * <p>
     * See examples in javadoc for {@link AdditionalMatchers} class
     *
     * @param first
     *            placeholder for the first argument matcher.
     * @param second
     *            placeholder for the second argument matcher.
     * @return <code>0</code>.
     */
    public static long and(long first, long second) {
        
    }

    /**
     * short argument that matches both given argument matchers.
     * <p>
     * See examples in javadoc for {@link AdditionalMatchers} class
     *
     * @param first
     *            placeholder for the first argument matcher.
     * @param second
     *            placeholder for the second argument matcher.
     * @return <code>0</code>.
     */
    public static short and(short first, short second) {
        
    }

    /**
     * Object argument that matches both given argument matchers.
     * <p>
     * See examples in javadoc for {@link AdditionalMatchers} class
     *
     * @param <T>
     *            the type of the object, it is passed through to prevent casts.
     * @param first
     *            placeholder for the first argument matcher.
     * @param second
     *            placeholder for the second argument matcher.
     * @return <code>null</code>.
     */
    public static <T> T and(T first, T second) {
        
    }

    /**
     * boolean argument that matches any of the given argument matchers.
     * <p>
     * See examples in javadoc for {@link AdditionalMatchers} class
     *
     * @param first
     *            placeholder for the first argument matcher.
     * @param second
     *            placeholder for the second argument matcher.
     * @return <code>false</code>.
     */
    public static boolean or(boolean first, boolean second) {
        
    }

    /**
     * Object argument that matches any of the given argument matchers.
     * <p>
     * See examples in javadoc for {@link AdditionalMatchers} class
     *
     * @param <T>
     *            the type of the object, it is passed through to prevent casts.
     * @param first
     *            placeholder for the first argument matcher.
     * @param second
     *            placeholder for the second argument matcher.
     * @return <code>null</code>.
     */
    public static <T> T or(T first, T second) {
        
    }

    /**
     * short argument that matches any of the given argument matchers.
     * <p>
     * See examples in javadoc for {@link AdditionalMatchers} class
     *
     * @param first
     *            placeholder for the first argument matcher.
     * @param second
     *            placeholder for the second argument matcher.
     * @return <code>0</code>.
     */
    public static short or(short first, short second) {
        
    }

    /**
     * long argument that matches any of the given argument matchers.
     * <p>
     * See examples in javadoc for {@link AdditionalMatchers} class
     *
     * @param first
     *            placeholder for the first argument matcher.
     * @param second
     *            placeholder for the second argument matcher.
     * @return <code>0</code>.
     */
    public static long or(long first, long second) {
        
    }

    /**
     * int argument that matches any of the given argument matchers.
     * <p>
     * See examples in javadoc for {@link AdditionalMatchers} class
     *
     * @param first
     *            placeholder for the first argument matcher.
     * @param second
     *            placeholder for the second argument matcher.
     * @return <code>0</code>.
     */
    public static int or(int first, int second) {
        
    }

    /**
     * float argument that matches any of the given argument matchers.
     * <p>
     * See examples in javadoc for {@link AdditionalMatchers} class
     *
     * @param first
     *            placeholder for the first argument matcher.
     * @param second
     *            placeholder for the second argument matcher.
     * @return <code>0</code>.
     */
    public static float or(float first, float second) {
        
    }

    /**
     * double argument that matches any of the given argument matchers.
     * <p>
     * See examples in javadoc for {@link AdditionalMatchers} class
     *
     * @param first
     *            placeholder for the first argument matcher.
     * @param second
     *            placeholder for the second argument matcher.
     * @return <code>0</code>.
     */
    public static double or(double first, double second) {
        
    }

    /**
     * char argument that matches any of the given argument matchers.
     * <p>
     * See examples in javadoc for {@link AdditionalMatchers} class
     *
     * @param first
     *            placeholder for the first argument matcher.
     * @param second
     *            placeholder for the second argument matcher.
     * @return <code>0</code>.
     */
    public static char or(char first, char second) {
        
    }

    /**
     * byte argument that matches any of the given argument matchers.
     * <p>
     * See examples in javadoc for {@link AdditionalMatchers} class
     *
     * @param first
     *            placeholder for the first argument matcher.
     * @param second
     *            placeholder for the second argument matcher.
     * @return <code>0</code>.
     */
    public static byte or(byte first, byte second) {
        
    }

    /**
     * Object argument that does not match the given argument matcher.
     * <p>
     * See examples in javadoc for {@link AdditionalMatchers} class
     *
     * @param <T>
     *            the type of the object, it is passed through to prevent casts.
     * @param first
     *            placeholder for the argument matcher.
     * @return <code>null</code>.
     */
    public static <T> T not(T first) {
        
    }

    /**
     * short argument that does not match the given argument matcher.
     * <p>
     * See examples in javadoc for {@link AdditionalMatchers} class
     *
     * @param first
     *            placeholder for the argument matcher.
     * @return <code>0</code>.
     */
    public static short not(short first) {
        
    }

    /**
     * int argument that does not match the given argument matcher.
     * <p>
     * See examples in javadoc for {@link AdditionalMatchers} class
     *
     * @param first
     *            placeholder for the argument matcher.
     * @return <code>0</code>.
     */
    public static int not(int first) {
        
    }

    /**
     * long argument that does not match the given argument matcher.
     * <p>
     * See examples in javadoc for {@link AdditionalMatchers} class
     *
     * @param first
     *            placeholder for the argument matcher.
     * @return <code>0</code>.
     */
    public static long not(long first) {
        
    }

    /**
     * float argument that does not match the given argument matcher.
     * <p>
     * See examples in javadoc for {@link AdditionalMatchers} class
     *
     * @param first
     *            placeholder for the argument matcher.
     * @return <code>0</code>.
     */
    public static float not(float first) {
        
    }

    /**
     * double argument that does not match the given argument matcher.
     * <p>
     * See examples in javadoc for {@link AdditionalMatchers} class
     *
     * @param first
     *            placeholder for the argument matcher.
     * @return <code>0</code>.
     */
    public static double not(double first) {
        
    }

    /**
     * char argument that does not match the given argument matcher.
     * <p>
     * See examples in javadoc for {@link AdditionalMatchers} class
     *
     * @param first
     *            placeholder for the argument matcher.
     * @return <code>0</code>.
     */
    public static char not(char first) {
        
    }

    /**
     * boolean argument that does not match the given argument matcher.
     * <p>
     * See examples in javadoc for {@link AdditionalMatchers} class
     *
     * @param first
     *            placeholder for the argument matcher.
     * @return <code>false</code>.
     */
    public static boolean not(boolean first) {
        
    }

    /**
     * byte argument that does not match the given argument matcher.
     * <p>
     * See examples in javadoc for {@link AdditionalMatchers} class
     *
     * @param first
     *            placeholder for the argument matcher.
     * @return <code>0</code>.
     */
    public static byte not(byte first) {
        
    }

    /**
     * double argument that has an absolute difference to the given value that
     * is less than the given delta details.
     * <p>
     * See examples in javadoc for {@link AdditionalMatchers} class
     *
     * @param value
     *            the given value.
     * @param delta
     *            the given delta.
     * @return <code>0</code>.
     */
    public static double eq(double value, double delta) {
        
    }

    /**
     * float argument that has an absolute difference to the given value that is
     * less than the given delta details.
     * <p>
     * See examples in javadoc for {@link AdditionalMatchers} class
     *
     * @param value
     *            the given value.
     * @param delta
     *            the given delta.
     * @return <code>0</code>.
     */
    public static float eq(float value, float delta) {
        
    }

    private static void reportMatcher(ArgumentMatcher<?> matcher) {
        
    }

    private AdditionalMatchers() { }
}
