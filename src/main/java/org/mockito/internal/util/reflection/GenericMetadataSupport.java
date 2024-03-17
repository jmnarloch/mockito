/*
 * Copyright (c) 2007 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockito.internal.util.reflection;

import java.lang.reflect.GenericArrayType;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.lang.reflect.TypeVariable;
import java.lang.reflect.WildcardType;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.Set;
import org.mockito.exceptions.base.MockitoException;
import org.mockito.internal.util.Checks;

/**
 * This class can retrieve generic meta-data that the compiler stores on classes
 * and accessible members.
 *
 * <p>
 * The main idea of this code is to create a Map that will help to resolve return types.
 * In order to actually work with nested generics, this map will have to be passed along new instances
 * as a type context.
 * </p>
 *
 * <p>
 * Hence :
 * <ul>
 * <li>A new instance representing the metadata is created using the {@link #inferFrom(Type)} method from a real
 * <code>Class</code> or from a <code>ParameterizedType</code>, other types are not yet supported.</li>
 *
 * <li>Then from this metadata, we can extract meta-data for a generic return type of a method, using
 * {@link #resolveGenericReturnType(Method)}.</li>
 * </ul>
 * </p>
 *
 * <p>
 * For now this code support the following kind of generic declarations :
 * <pre class="code"><code class="java">
 * interface GenericsNest&lt;K extends Comparable&lt;K&gt; & Cloneable&gt; extends Map&lt;K, Set&lt;Number&gt;&gt; {
 *     Set&lt;Number&gt; remove(Object key); // override with fixed ParameterizedType
 *     List&lt;? super Integer&gt; returning_wildcard_with_class_lower_bound();
 *     List&lt;? super K&gt; returning_wildcard_with_typeVar_lower_bound();
 *     List&lt;? extends K&gt; returning_wildcard_with_typeVar_upper_bound();
 *     K returningK();
 *     &lt;O extends K&gt; List&lt;O&gt; paramType_with_type_params();
 *     &lt;S, T extends S&gt; T two_type_params();
 *     &lt;O extends K&gt; O typeVar_with_type_params();
 *     Number returningNonGeneric();
 * }
 * </code></pre>
 *
 * @see #inferFrom(Type)
 * @see #resolveGenericReturnType(Method)
 * @see org.mockito.internal.stubbing.defaultanswers.ReturnsDeepStubs
 */
public abstract class GenericMetadataSupport {

    // public static MockitoLogger logger = new ConsoleMockitoLogger();

    /** Represents actual type variables resolved for current class. */
    protected Map<TypeVariable<?>, Type> contextualActualTypeParameters = new HashMap<>();

    /**
     * Registers the type variables for the given type and all of its superclasses and superinterfaces.
     */
    protected void registerAllTypeVariables(Type classType) {
        
    }

    protected Class<?> extractRawTypeOf(Type type) {
        
    }

    protected void registerTypeVariablesOn(Type classType) {
        
    }

    protected void registerTypeParametersOn(TypeVariable<?>[] typeParameters) {
        
    }

    private void registerTypeVariableIfNotPresent(TypeVariable<?> typeVariable) {
        
    }

    /**
     * @param typeParameter The TypeVariable parameter
     * @return A {@link BoundedType} for easy bound information, if first bound is a TypeVariable
     * then retrieve BoundedType of this TypeVariable
     */
    private BoundedType boundsOf(TypeVariable<?> typeParameter) {
        
    }

    /**
     * @param wildCard The WildCard type
     * @return A {@link BoundedType} for easy bound information, if first bound is a TypeVariable
     * then retrieve BoundedType of this TypeVariable
     */
    private BoundedType boundsOf(WildcardType wildCard) {
        /*
         *  According to JLS(https://docs.oracle.com/javase/specs/jls/se8/html/jls-4.html#jls-4.5.1):
         *  - Lower and upper can't coexist: (for instance, this is not allowed:
         *    <? extends List<String> & super MyInterface>)
         *  - Multiple concrete type bounds are not supported (for instance, this is not allowed:
         *    <? extends ArrayList<String> & MyInterface>)
         *    But the following form is possible where there is a single concrete tyep bound followed by interface type bounds
         *    <T extends List<String> & Comparable>
         */

        
    }

    /**
     * @return Raw type of the current instance.
     */
    public abstract Class<?> rawType();

    /**
     * @return Returns extra interfaces <strong>if relevant</strong>, otherwise empty List.
     */
    public List<Type> extraInterfaces() {
        
    }

    /**
     * @return Returns an array with the raw types of {@link #extraInterfaces()} <strong>if relevant</strong>.
     */
    public Class<?>[] rawExtraInterfaces() {
        
    }

    /**
     * @return Returns true if metadata knows about extra-interfaces {@link #extraInterfaces()} <strong>if relevant</strong>.
     */
    public boolean hasRawExtraInterfaces() {
        
    }

    /**
     * @return Actual type arguments matching the type variables of the raw type represented by this {@link GenericMetadataSupport} instance.
     */
    public Map<TypeVariable<?>, Type> actualTypeArguments() {
        
    }

    protected Type getActualTypeArgumentFor(TypeVariable<?> typeParameter) {
        
    }

    /**
     * Resolve current method generic return type to a {@link GenericMetadataSupport}.
     *
     * @param method Method to resolve the return type.
     * @return {@link GenericMetadataSupport} representing this generic return type.
     */
    public GenericMetadataSupport resolveGenericReturnType(Method method) {
        
    }

    private GenericMetadataSupport resolveGenericType(Type type, Method method) {

        
    }

    /**
     * Create an new instance of {@link GenericMetadataSupport} inferred from a {@link Type}.
     *
     * <p>
     * At the moment <code>type</code> can only be a {@link Class} or a {@link ParameterizedType}, otherwise
     * it'll throw a {@link MockitoException}.
     * </p>
     *
     * @param type The class from which the {@link GenericMetadataSupport} should be built.
     * @return The new {@link GenericMetadataSupport}.
     * @throws MockitoException Raised if type is not a {@link Class} or a {@link ParameterizedType}.
     */
    public static GenericMetadataSupport inferFrom(Type type) {
        
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    //// Below are specializations of GenericMetadataSupport that could handle retrieval of possible
    // Types
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    /**
     * Generic metadata implementation for {@link Class}.
     * <p>
     * Offer support to retrieve generic metadata on a {@link Class} by reading type parameters and type variables on
     * the class and its ancestors and interfaces.
     */
    private static class FromClassGenericMetadataSupport extends GenericMetadataSupport {
        private final Class<?> clazz;

        public FromClassGenericMetadataSupport(Class<?> clazz) {
            
        }

        @Override
        public Class<?> rawType() {
            
        }
    }

    /**
     * Generic metadata implementation for "standalone" {@link ParameterizedType}.
     * <p>
     * Offer support to retrieve generic metadata on a {@link ParameterizedType} by reading type variables of
     * the related raw type and declared type variable of this parameterized type.
     * <p>
     * This class is not designed to work on ParameterizedType returned by {@link Method#getGenericReturnType()}, as
     * the ParameterizedType instance return in these cases could have Type Variables that refer to type declaration(s).
     * That's what meant the "standalone" word at the beginning of the Javadoc.
     * Instead use {@link ParameterizedReturnType}.
     */
    private static class FromParameterizedTypeGenericMetadataSupport
            extends GenericMetadataSupport {
        private final ParameterizedType parameterizedType;

        public FromParameterizedTypeGenericMetadataSupport(ParameterizedType parameterizedType) {
            
        }

        private void readActualTypeParameters() {
            
        }

        @Override
        public Class<?> rawType() {
            
        }
    }

    /**
     * Generic metadata specific to {@link ParameterizedType} returned via {@link Method#getGenericReturnType()}.
     */
    private static class ParameterizedReturnType extends GenericMetadataSupport {
        private final ParameterizedType parameterizedType;
        private final TypeVariable<?>[] typeParameters;

        public ParameterizedReturnType(
                GenericMetadataSupport source,
                TypeVariable<?>[] typeParameters,
                ParameterizedType parameterizedType) {
            
        }

        private void readTypeParameters() {
            
        }

        private void readTypeVariables() {
            
        }

        @Override
        public Class<?> rawType() {
            
        }
    }

    /**
     * Generic metadata for {@link TypeVariable} returned via {@link Method#getGenericReturnType()}.
     */
    private static class TypeVariableReturnType extends GenericMetadataSupport {
        private final TypeVariable<?> typeVariable;
        private final TypeVariable<?>[] typeParameters;
        private Class<?> rawType;
        private List<Type> extraInterfaces;

        public TypeVariableReturnType(
                GenericMetadataSupport source,
                TypeVariable<?>[] typeParameters,
                TypeVariable<?> typeVariable) {
            
        }

        private void readTypeParameters() {
            
        }

        private void readTypeVariables() {
            
        }

        @Override
        public Class<?> rawType() {
            
        }

        @Override
        public List<Type> extraInterfaces() {
            
        }

        /**
         * @return Returns an array with the extracted raw types of {@link #extraInterfaces()}.
         * @see #extractRawTypeOf(java.lang.reflect.Type)
         */
        @Override
        public Class<?>[] rawExtraInterfaces() {
            
        }

        private Type extractActualBoundedTypeOf(Type type) {
             // irrelevant, we don't manage other types as they are not bounded.
        }
    }

    private static class GenericArrayReturnType extends GenericMetadataSupport {

        private final GenericMetadataSupport genericArrayType;

        private final int arity;

        public GenericArrayReturnType(GenericMetadataSupport genericArrayType, int arity) {
            
        }

        @Override
        public Class<?> rawType() {
            
        }
    }

    /**
     * Non-Generic metadata for {@link Class} returned via {@link Method#getGenericReturnType()}.
     */
    private static class NotGenericReturnTypeSupport extends GenericMetadataSupport {
        private final Class<?> returnType;

        public NotGenericReturnTypeSupport(GenericMetadataSupport source, Type genericReturnType) {
            
        }

        @Override
        public Class<?> rawType() {
            
        }
    }

    /**
     * Type representing bounds of a type
     *
     * @see TypeVarBoundedType
     * @see <a href="https://docs.oracle.com/javase/specs/jls/se8/html/jls-4.html#jls-4.4">https://docs.oracle.com/javase/specs/jls/se8/html/jls-4.html#jls-4.4</a>
     * @see WildCardBoundedType
     * @see <a href="https://docs.oracle.com/javase/specs/jls/se8/html/jls-4.html#jls-4.5.1">https://docs.oracle.com/javase/specs/jls/se8/html/jls-4.html#jls-4.5.1</a>
     */
    public interface BoundedType extends Type {
        Type firstBound();

        Type[] interfaceBounds();
    }

    /**
     * Type representing bounds of a type variable, allows to keep all bounds information.
     *
     * <p>It uses the first bound in the array, as this array is never null and always contains at least
     * one element (Object is always here if no bounds are declared).</p>
     *
     * <p>If upper bounds are declared with SomeClass and additional interfaces, then firstBound will be SomeClass and
     * interfacesBound will be an array of the additional interfaces.
     * <p>
     * i.e. <code>SomeClass</code>.
     * <pre class="code"><code class="java">
     *     interface UpperBoundedTypeWithClass<E extends Comparable<E> & Cloneable> {
     *         E get();
     *     }
     *     // will return Comparable type
     * </code></pre>
     * </p>
     *
     * @see <a href="https://docs.oracle.com/javase/specs/jls/se8/html/jls-4.html#jls-4.4">https://docs.oracle.com/javase/specs/jls/se8/html/jls-4.html#jls-4.4</a>
     */
    public static class TypeVarBoundedType implements BoundedType {
        private final TypeVariable<?> typeVariable;

        public TypeVarBoundedType(TypeVariable<?> typeVariable) {
            
        }

        /**
         * @return either a class or an interface (parameterized or not), if no bounds declared Object is returned.
         */
        @Override
        public Type firstBound() {
             //
        }

        /**
         * On a Type Variable (typeVar extends C_0 & I_1 & I_2 & etc), will return an array
         * containing I_1 and I_2.
         *
         * @return other bounds for this type, these bounds can only be only interfaces as the JLS says,
         * empty array if no other bound declared.
         */
        @Override
        public Type[] interfaceBounds() {
            
        }

        @Override
        public boolean equals(Object o) {
            
        }

        @Override
        public int hashCode() {
            
        }

        @Override
        public String toString() {
            
        }

        public TypeVariable<?> typeVariable() {
            
        }
    }

    /**
     * Type representing bounds of a wildcard, allows to keep all bounds information.
     *
     * <p>The JLS says that lower bound and upper bound are mutually exclusive, and that multiple bounds
     * are not allowed.
     *
     * @see <a href="https://docs.oracle.com/javase/specs/jls/se8/html/jls-4.html#jls-4.4">https://docs.oracle.com/javase/specs/jls/se8/html/jls-4.html#jls-4.4</a>
     */
    public static class WildCardBoundedType implements BoundedType {
        private final WildcardType wildcard;

        public WildCardBoundedType(WildcardType wildcard) {
            
        }

        /** @return The first bound, either a type or a reference to a TypeVariable */
        @Override
        public Type firstBound() {
            
        }

        /** @return An empty array as, wildcard don't support multiple bounds. */
        @Override
        public Type[] interfaceBounds() {
            
        }

        @Override
        public boolean equals(Object o) {
            
        }

        @Override
        public int hashCode() {
            
        }

        @Override
        public String toString() {
            
        }

        public WildcardType wildCard() {
            
        }
    }
}
