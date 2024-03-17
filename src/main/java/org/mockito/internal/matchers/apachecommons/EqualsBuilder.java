/*
 * Copyright (c) 2007 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockito.internal.matchers.apachecommons;

import org.mockito.internal.configuration.plugins.Plugins;
import org.mockito.plugins.MemberAccessor;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

// Class comes from Apache Commons Lang, added some tiny changes
/**
 * <p>Assists in implementing {@link Object#equals(Object)} methods.</p>
 *
 * <p> This class provides methods to build a good equals method for any
 * class. It follows rules laid out in
 * <a href="http://java.sun.com/docs/books/effective/index.html">Effective Java</a>
 * , by Joshua Bloch. In particular the rule for comparing <code>doubles</code>,
 * <code>floats</code>, and arrays can be tricky. Also, making sure that
 * <code>equals()</code> and <code>hashCode()</code> are consistent can be
 * difficult.</p>
 *
 * <p>Two Objects that compare as equals must generate the same hash code,
 * but two Objects with the same hash code do not have to be equal.</p>
 *
 * <p>All relevant fields should be included in the calculation of equals.
 * Derived fields may be ignored. In particular, any field used in
 * generating a hash code must be used in the equals method, and vice
 * versa.</p>
 *
 * <p>Typical use for the code is as follows:</p>
 * <pre class="code"><code class="java">
 * public boolean equals(Object obj) {
 *   if (obj == null) { return false; }
 *   if (obj == this) { return true; }
 *   if (obj.getClass() != getClass()) {
 *     return false;
 *   }
 *   MyClass rhs = (MyClass) obj;
 *   return new EqualsBuilder()
 *                 .appendSuper(super.equals(obj))
 *                 .append(field1, rhs.field1)
 *                 .append(field2, rhs.field2)
 *                 .append(field3, rhs.field3)
 *                 .isEquals();
 *  }
 * </code></pre>
 *
 * <p> Alternatively, there is a method that uses reflection to determine
 * the fields to test. Because these fields are usually private, the method,
 * <code>reflectionEquals</code>, uses <code>AccessibleObject.setAccessible</code> to
 * change the visibility of the fields. This will fail under a security
 * manager, unless the appropriate permissions are set up correctly. It is
 * also slower than testing explicitly.</p>
 *
 * <p> A typical invocation for this method would look like:</p>
 * <pre class="code"><code class="java">
 * public boolean equals(Object obj) {
 *   return EqualsBuilder.reflectionEquals(this, obj);
 * }
 * </code></pre>
 *
 * @author <a href="mailto:steve.downey@netfolio.com">Steve Downey</a>
 * @author Stephen Colebourne
 * @author Gary Gregory
 * @author Pete Gieser
 * @author Arun Mammen Thomas
 * @since 1.0
 * @version $Id: EqualsBuilder.java 611543 2008-01-13 07:00:22Z bayard $
 */
@SuppressWarnings("unchecked")
class EqualsBuilder {

    /**
     * If the fields tested are equals.
     * The default value is <code>true</code>.
     */
    private boolean isEquals = true;

    /**
     * <p>Constructor for EqualsBuilder.</p>
     *
     * <p>Starts off assuming that equals is <code>true</code>.</p>
     * @see Object#equals(Object)
     */
    public EqualsBuilder() { }

    // -------------------------------------------------------------------------

    /**
     * <p>This method uses reflection to determine if the two <code>Object</code>s
     * are equal.</p>
     *
     * <p>It uses <code>AccessibleObject.setAccessible</code> to gain access to private
     * fields. This means that it will throw a security exception if run under
     * a security manager, if the permissions are not set up correctly. It is also
     * not as efficient as testing explicitly.</p>
     *
     * <p>Transient members will be not be tested, as they are likely derived
     * fields, and not part of the value of the Object.</p>
     *
     * <p>Static fields will not be tested. Superclass fields will be included.</p>
     *
     * @param lhs  <code>this</code> object
     * @param rhs  the other object
     * @return <code>true</code> if the two Objects have tested equals.
     */
    public static boolean reflectionEquals(Object lhs, Object rhs) {
        
    }

    /**
     * <p>This method uses reflection to determine if the two <code>Object</code>s
     * are equal.</p>
     *
     * <p>It uses <code>AccessibleObject.setAccessible</code> to gain access to private
     * fields. This means that it will throw a security exception if run under
     * a security manager, if the permissions are not set up correctly. It is also
     * not as efficient as testing explicitly.</p>
     *
     * <p>Transient members will be not be tested, as they are likely derived
     * fields, and not part of the value of the Object.</p>
     *
     * <p>Static fields will not be tested. Superclass fields will be included.</p>
     *
     * @param lhs  <code>this</code> object
     * @param rhs  the other object
     * @param excludeFields  array of field names to exclude from testing
     * @return <code>true</code> if the two Objects have tested equals.
     */
    public static boolean reflectionEquals(Object lhs, Object rhs, String[] excludeFields) {
        
    }

    /**
     * <p>This method uses reflection to determine if the two <code>Object</code>s
     * are equal.</p>
     *
     * <p>It uses <code>AccessibleObject.setAccessible</code> to gain access to private
     * fields. This means that it will throw a security exception if run under
     * a security manager, if the permissions are not set up correctly. It is also
     * not as efficient as testing explicitly.</p>
     *
     * <p>If the TestTransients parameter is set to <code>true</code>, transient
     * members will be tested, otherwise they are ignored, as they are likely
     * derived fields, and not part of the value of the <code>Object</code>.</p>
     *
     * <p>Static fields will not be tested. Superclass fields will be included.</p>
     *
     * @param lhs  <code>this</code> object
     * @param rhs  the other object
     * @param testTransients  whether to include transient fields
     * @return <code>true</code> if the two Objects have tested equals.
     */
    public static boolean reflectionEquals(Object lhs, Object rhs, boolean testTransients) {
        
    }

    /**
     * <p>This method uses reflection to determine if the two <code>Object</code>s
     * are equal.</p>
     *
     * <p>It uses <code>AccessibleObject.setAccessible</code> to gain access to private
     * fields. This means that it will throw a security exception if run under
     * a security manager, if the permissions are not set up correctly. It is also
     * not as efficient as testing explicitly.</p>
     *
     * <p>If the testTransients parameter is set to <code>true</code>, transient
     * members will be tested, otherwise they are ignored, as they are likely
     * derived fields, and not part of the value of the <code>Object</code>.</p>
     *
     * <p>Static fields will not be included. Superclass fields will be appended
     * up to and including the specified superclass. A null superclass is treated
     * as java.lang.Object.</p>
     *
     * @param lhs  <code>this</code> object
     * @param rhs  the other object
     * @param testTransients  whether to include transient fields
     * @param reflectUpToClass  the superclass to reflect up to (inclusive),
     *  may be <code>null</code>
     * @return <code>true</code> if the two Objects have tested equals.
     * @since 2.1.0
     */
    public static boolean reflectionEquals(
            Object lhs, Object rhs, boolean testTransients, Class<?> reflectUpToClass) {
        
    }

    /**
     * <p>This method uses reflection to determine if the two <code>Object</code>s
     * are equal.</p>
     *
     * <p>It uses <code>AccessibleObject.setAccessible</code> to gain access to private
     * fields. This means that it will throw a security exception if run under
     * a security manager, if the permissions are not set up correctly. It is also
     * not as efficient as testing explicitly.</p>
     *
     * <p>If the testTransients parameter is set to <code>true</code>, transient
     * members will be tested, otherwise they are ignored, as they are likely
     * derived fields, and not part of the value of the <code>Object</code>.</p>
     *
     * <p>Static fields will not be included. Superclass fields will be appended
     * up to and including the specified superclass. A null superclass is treated
     * as java.lang.Object.</p>
     *
     * @param lhs  <code>this</code> object
     * @param rhs  the other object
     * @param testTransients  whether to include transient fields
     * @param reflectUpToClass  the superclass to reflect up to (inclusive),
     *  may be <code>null</code>
     * @param excludeFields  array of field names to exclude from testing
     * @return <code>true</code> if the two Objects have tested equals.
     * @since 2.1.0
     */
    public static boolean reflectionEquals(
            Object lhs,
            Object rhs,
            boolean testTransients,
            Class<?> reflectUpToClass,
            String[] excludeFields) {
        
    }

    /**
     * <p>Appends the fields and values defined by the given object of the
     * given Class.</p>
     *
     * @param lhs  the left hand object
     * @param rhs  the right hand object
     * @param clazz  the class to append details of
     * @param builder  the builder to append to
     * @param useTransients  whether to test transient fields
     * @param excludeFields  array of field names to exclude from testing
     */
    private static boolean reflectionAppend(
            Object lhs,
            Object rhs,
            Class<?> clazz,
            EqualsBuilder builder,
            boolean useTransients,
            String[] excludeFields) {
        
    }

    // -------------------------------------------------------------------------

    /**
     * <p>Adds the result of <code>super.equals()</code> to this builder.</p>
     *
     * @param superEquals  the result of calling <code>super.equals()</code>
     * @return EqualsBuilder - used to chain calls.
     * @since 2.1.0
     */
    public EqualsBuilder appendSuper(boolean superEquals) {
        
    }

    // -------------------------------------------------------------------------

    /**
     * <p>Test if two <code>Object</code>s are equal using their
     * <code>equals</code> method.</p>
     *
     * @param lhs  the left hand object
     * @param rhs  the right hand object
     * @return EqualsBuilder - used to chain calls.
     */
    public EqualsBuilder append(Object lhs, Object rhs) {
        
    }

    /**
     * <p>
     * Test if two <code>long</code> s are equal.
     * </p>
     *
     * @param lhs
     *                  the left hand <code>long</code>
     * @param rhs
     *                  the right hand <code>long</code>
     * @return EqualsBuilder - used to chain calls.
     */
    public EqualsBuilder append(long lhs, long rhs) {
        
    }

    /**
     * <p>Test if two <code>int</code>s are equal.</p>
     *
     * @param lhs  the left hand <code>int</code>
     * @param rhs  the right hand <code>int</code>
     * @return EqualsBuilder - used to chain calls.
     */
    public EqualsBuilder append(int lhs, int rhs) {
        
    }

    /**
     * <p>Test if two <code>short</code>s are equal.</p>
     *
     * @param lhs  the left hand <code>short</code>
     * @param rhs  the right hand <code>short</code>
     * @return EqualsBuilder - used to chain calls.
     */
    public EqualsBuilder append(short lhs, short rhs) {
        
    }

    /**
     * <p>Test if two <code>char</code>s are equal.</p>
     *
     * @param lhs  the left hand <code>char</code>
     * @param rhs  the right hand <code>char</code>
     * @return EqualsBuilder - used to chain calls.
     */
    public EqualsBuilder append(char lhs, char rhs) {
        
    }

    /**
     * <p>Test if two <code>byte</code>s are equal.</p>
     *
     * @param lhs  the left hand <code>byte</code>
     * @param rhs  the right hand <code>byte</code>
     * @return EqualsBuilder - used to chain calls.
     */
    public EqualsBuilder append(byte lhs, byte rhs) {
        
    }

    /**
     * <p>Test if two <code>double</code>s are equal by testing that the
     * pattern of bits returned by <code>doubleToLong</code> are equal.</p>
     *
     * <p>This handles NaNs, Infinities, and <code>-0.0</code>.</p>
     *
     * <p>It is compatible with the hash code generated by
     * <code>HashCodeBuilder</code>.</p>
     *
     * @param lhs  the left hand <code>double</code>
     * @param rhs  the right hand <code>double</code>
     * @return EqualsBuilder - used to chain calls.
     */
    public EqualsBuilder append(double lhs, double rhs) {
        
    }

    /**
     * <p>Test if two <code>float</code>s are equal byt testing that the
     * pattern of bits returned by doubleToLong are equal.</p>
     *
     * <p>This handles NaNs, Infinities, and <code>-0.0</code>.</p>
     *
     * <p>It is compatible with the hash code generated by
     * <code>HashCodeBuilder</code>.</p>
     *
     * @param lhs  the left hand <code>float</code>
     * @param rhs  the right hand <code>float</code>
     * @return EqualsBuilder - used to chain calls.
     */
    public EqualsBuilder append(float lhs, float rhs) {
        
    }

    /**
     * <p>Test if two <code>booleans</code>s are equal.</p>
     *
     * @param lhs  the left hand <code>boolean</code>
     * @param rhs  the right hand <code>boolean</code>
     * @return EqualsBuilder - used to chain calls.
     */
    public EqualsBuilder append(boolean lhs, boolean rhs) {
        
    }

    /**
     * <p>Performs a deep comparison of two <code>Object</code> arrays.</p>
     *
     * <p>This also will be called for the top level of
     * multi-dimensional, ragged, and multi-typed arrays.</p>
     *
     * @param lhs  the left hand <code>Object[]</code>
     * @param rhs  the right hand <code>Object[]</code>
     * @return EqualsBuilder - used to chain calls.
     */
    public EqualsBuilder append(Object[] lhs, Object[] rhs) {
        
    }

    /**
     * <p>Deep comparison of array of <code>long</code>. Length and all
     * values are compared.</p>
     *
     * <p>The method {@link #append(long, long)} is used.</p>
     *
     * @param lhs  the left hand <code>long[]</code>
     * @param rhs  the right hand <code>long[]</code>
     * @return EqualsBuilder - used to chain calls.
     */
    public EqualsBuilder append(long[] lhs, long[] rhs) {
        
    }

    /**
     * <p>Deep comparison of array of <code>int</code>. Length and all
     * values are compared.</p>
     *
     * <p>The method {@link #append(int, int)} is used.</p>
     *
     * @param lhs  the left hand <code>int[]</code>
     * @param rhs  the right hand <code>int[]</code>
     * @return EqualsBuilder - used to chain calls.
     */
    public EqualsBuilder append(int[] lhs, int[] rhs) {
        
    }

    /**
     * <p>Deep comparison of array of <code>short</code>. Length and all
     * values are compared.</p>
     *
     * <p>The method {@link #append(short, short)} is used.</p>
     *
     * @param lhs  the left hand <code>short[]</code>
     * @param rhs  the right hand <code>short[]</code>
     * @return EqualsBuilder - used to chain calls.
     */
    public EqualsBuilder append(short[] lhs, short[] rhs) {
        
    }

    /**
     * <p>Deep comparison of array of <code>char</code>. Length and all
     * values are compared.</p>
     *
     * <p>The method {@link #append(char, char)} is used.</p>
     *
     * @param lhs  the left hand <code>char[]</code>
     * @param rhs  the right hand <code>char[]</code>
     * @return EqualsBuilder - used to chain calls.
     */
    public EqualsBuilder append(char[] lhs, char[] rhs) {
        
    }

    /**
     * <p>Deep comparison of array of <code>byte</code>. Length and all
     * values are compared.</p>
     *
     * <p>The method {@link #append(byte, byte)} is used.</p>
     *
     * @param lhs  the left hand <code>byte[]</code>
     * @param rhs  the right hand <code>byte[]</code>
     * @return EqualsBuilder - used to chain calls.
     */
    public EqualsBuilder append(byte[] lhs, byte[] rhs) {
        
    }

    /**
     * <p>Deep comparison of array of <code>double</code>. Length and all
     * values are compared.</p>
     *
     * <p>The method {@link #append(double, double)} is used.</p>
     *
     * @param lhs  the left hand <code>double[]</code>
     * @param rhs  the right hand <code>double[]</code>
     * @return EqualsBuilder - used to chain calls.
     */
    public EqualsBuilder append(double[] lhs, double[] rhs) {
        
    }

    /**
     * <p>Deep comparison of array of <code>float</code>. Length and all
     * values are compared.</p>
     *
     * <p>The method {@link #append(float, float)} is used.</p>
     *
     * @param lhs  the left hand <code>float[]</code>
     * @param rhs  the right hand <code>float[]</code>
     * @return EqualsBuilder - used to chain calls.
     */
    public EqualsBuilder append(float[] lhs, float[] rhs) {
        
    }

    /**
     * <p>Deep comparison of array of <code>boolean</code>. Length and all
     * values are compared.</p>
     *
     * <p>The method {@link #append(boolean, boolean)} is used.</p>
     *
     * @param lhs  the left hand <code>boolean[]</code>
     * @param rhs  the right hand <code>boolean[]</code>
     * @return EqualsBuilder - used to chain calls.
     */
    public EqualsBuilder append(boolean[] lhs, boolean[] rhs) {
        
    }

    /**
     * <p>Returns <code>true</code> if the fields that have been checked
     * are all equal.</p>
     *
     * @return boolean
     */
    public boolean isEquals() {
        
    }

    /**
     * Sets the <code>isEquals</code> value.
     *
     * @param isEquals The value to set.
     * @since 2.1
     */
    protected void setEquals(boolean isEquals) {
        
    }
}
