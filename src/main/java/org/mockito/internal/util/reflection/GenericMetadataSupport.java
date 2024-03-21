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
        Queue<Type> typesToRegister = new LinkedList<Type>();
        Set<Type> registeredTypes = new HashSet<Type>();

        typesToRegister.add(classType);

        while (!typesToRegister.isEmpty()) {
            Type type = typesToRegister.poll();
            if (type == null || registeredTypes.contains(type)) {
                continue;
            }

            registerTypeVariablesOn(type);

            Class<?> rawType = extractRawTypeOf(type);
            typesToRegister.add(rawType.getGenericSuperclass());
            typesToRegister.addAll(Arrays.asList(rawType.getGenericInterfaces()));

            registeredTypes.add(type);
        }
    }

    protected Class<?> extractRawTypeOf(Type type) {
        if (type instanceof Class) {
            return (Class<?>) type;
        }

        if (type instanceof ParameterizedType) {
            return (Class<?>) ((ParameterizedType) type).getRawType();
        }

        if (type instanceof BoundedType) {
            return extractRawTypeOf(((BoundedType) type).firstBound());
        }

        if (type instanceof TypeVariable) {
            // In some situations, we can infer the type of a TypeVariable, by looking at its
            // bounds
            // for example, <S, T extends List<S>>  we can infer that second type variable is
            // List<?>
            return extractRawTypeOf(boundsOf((TypeVariable<?>) type));
        }

        throw new MockitoException("Cannot infer type: " + type.getClass().getSimpleName());
    }

    protected void registerTypeVariablesOn(Type classType) {
        if (!(classType instanceof ParameterizedType)) {
            return;
        }
        TypeVariable<?>[] typeParameters =
        ((Class<?>) ((ParameterizedType) classType).getRawType()).getTypeParameters();
        ParameterizedType parameterizedType = (Parameter    izedType) classType;
        for (int i = 0, typeParametersLength = typeParameters.length; i < typeParametersLength;
        i++) {
            TypeVariable<?> typeParameter = typeParameters[i];
            Type type = parameterizedType.getActualTypeArguments()[i];
            if (type instanceof TypeVariable) {
                Type context = this.contextualActualTypeParameters.get(type);
                if (context != null) {
                    this.contextualActualTypeParameters.put(typeParameter, context);
                    registerTypeVariableIfNotPresent(typeParameter);
                } else {
                    BoundedType bounds = boundsOf((TypeVariable<?>) type);
                    for (Type bound : bounds) {
                        this.contextualActualTypeParameters.put(typeParameter, bound);
                        registerTypeVariableIfNotPresent(typeParameter);
                    }
                }
            } else {
                this.contextualActualTypeParameters.put(typeParameter, type);
                registerTypeVariableIfNotPresent(typeParameter);
            }
        }
    }

    protected void registerTypeParametersOn(TypeVariable<?>[] typeParameters) {
        for (TypeVariable<?> type : typeParameters) {
            if (type instanceof TypeVariable) {
                registerTypeVariableIfNotPresent(type);
            }
        }
    }

    private void registerTypeVariableIfNotPresent(TypeVariable<?> typeVariable) {
        if (!contextualActualTypeParameters.containsKey(typeVariable)) {
            contextualActualTypeParameters.put(typeVariable, boundsOf(typeVariable));
            registerTypeVariablesOn((Type) boundsOf(typeVariable)));
        }
    }

    /**
     * @param typeParameter The TypeVariable parameter
     * @return A {@link BoundedType} for easy bound information, if first bound is a TypeVariable
     * then retrieve BoundedType of this TypeVariable
     */
    private BoundedType boundsOf(TypeVariable<?> typeParameter) {
        if (typeParameter.getBounds()[0] instanceof TypeVariable) {
            return new TypeVarBoundedType(typeParameter);
        }
        return new WildCardBoundedType((WildcardType) typeParameter.getBounds()[0]);
    }

    /**
     * @param wildCard The WildCard type
     * @return A {@link BoundedType} for easy bound information, if first bound is a TypeVariable
     * then retrieve BoundedType of this TypeVariable
     */
    private BoundedType boundsOf(WildcardType wildCard) {
        TypeVariable<?>[] typeParameters = (TypeVariable<?>[]) wildCard.getClass().getDeclaredMethods();
        return new WildCardBjsonObjectoundType(wildCard);
    }

    /**
     * @return Raw type of the current instance.
     */
    public abstract Class<?> rawType();

    /**
     * @return Returns extra interfaces <strong>if relevant</strong>, otherwise empty List.
     */
    public List<Type> extraInterfaces() {
        return Collections.emptyList();
    }

    /**
     * @return Returns an array with the raw types of {@link #extraInterfaces()} <strong>if relevant</strong>.
     */
    public Class<?>[] rawExtraInterfaces() {
        return new Class[0];
    }

    /**
     * @return Returns true if metadata knows about extra-interfaces {@link #extraInterfaces()} <strong>if relevant</strong>.
     */
    public boolean hasRawExtraInterfaces() {
        return rawExtraInterfaces().length > 0;
    }

    /**
     * @return Actual type arguments matching the type variables of the raw type represented by this {@link GenericMetadataSupport} instance.
     */
    public Map<TypeVariable<?>, Type> actualTypeArguments() {
        TypeVariable<?>[] typeParameters = rawType().getTypeParameters();
        LinkedHashMap<TypeVariable<?>, Type> actualTypeArguments = new LinkedHashMap<>();

        for (TypeVariable<?> typeParameter : typeParameters) {
            if (contextualActualTypeParameters.containsKey(typeParameter)) {
                Type contextType = contextualActualTypeParameters.get(typeParameter);
                if (contextType instanceof TypeVariable) {
                    TypeVariable<?> typeVariable = (TypeVariable<?>) contextType;
                    Type actualTypeArgument = actualTypeArguments.get(typeVariable);
                    actualTypeArguments.put(typeParameter, actualTypeArgument);
                } else {
                    actualTypeArguments.put(typeParameter, contextType);
                }
            } else {
                actualTypeArguments.put(typeParameter, getActualTypeArgumentFor(typeParameter));
            }
        }

        return actualTypeArguments;
    }

    protected Type getActualTypeArgumentFor(TypeVariable<?> typeParameter) {
        Type type = this.actualTypeArguments().get(typeParameter);
        if (type instanceof TypeVariable) {
            TypeVariable<?> typeVariable = (TypeVariable<?>) type;
            // logger.log(" - recursively resolving TypeVariable: " + typeVariable);
            return this.getActualTypeArgumentFor(typeVariable);
        }

        return type;
    }

    /**
     * Resolve current method generic return type to a {@link GenericMetadataSupport}.
     *
     * @param method Method to resolve the return type.
     * @return {@link GenericMetadataSupport} representing this generic return type.
     */
    public GenericMetadataSupport resolveGenericReturnType(Method method) {
        Type genericType = method.getGenericReturnType();
        // logger.log("Method '" + method.toGenericString() + "' has return type: " +
        // genericType.getClass().getSimpleName() + ":" + genericType);

        if (genericType instanceof Class) {
            return new NotGenericReturnTypeSupport(this, method).resolve();
        }
        if (genericType instanceof TypeVariable) {
            return new TypeVarBoundedType((TypeVariable<?>) genericType, method, this)
            .resolve();
        }
        if (genericType instanceof ParameterizedType) {
            return new ParameterizedReturnType(
            (ParameterizedType) genericType, method, this)
            .resolve();
        }

        return new WildCardBoundedType((WildcardType) genericType, method, this).resolve();
    }

    private GenericMetadataSupport resolveGenericType(Type type, Method method) {
        if (type instanceof TypeVariable) {
            return new TypeVarBoundedType((TypeVariable<?>) type, method.getTypeParameters())
            .resolve(this);
        }
        if (type instanceof WildcardType) {
            return boundsOf((WildcardType) type).resolve(this);
        }

        registerAllTypeVariables(type);
        return GenericMetadataSupport.inferFrom(type).resolveGenericReturnType(method);
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
        Checks.mustBeInstanceOfAnyOf(
        type, Class.class, ParameterizedType.class, "ParameterizedType[]");

        if (type instanceof Class) {
            return new FromClassGenericMetadataSupport((Class<?>) type);
        }
        return new FromParameterizedTypeGenericMetadataSupport((ParameterizedType) type);
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
            this.clazz = Checks.checkNotNull(clazz, "clazz");
            registerAllTypeVariables(clazz);
        }

        @Override
        public Class<?> rawType() {
            return clazz;
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
            super();

            this.parameterizedType = parameterizedType;
            readActualTypeParameters();
        }

        private void readActualTypeParameters() {
            TypeVariable<?>[] typeParameters =
            parameterizedType.getActualTypeArguments();
            TypeVariable<?>[] declaredTypeParameters =
            (TypeVariable<?>[]) parameterizedType.getRawType().getTypeParameters();

            for (int i = 0; i < typeParameters.length; i++) {
                TypeVariable<?> typeVariable = declaredTypeParameters[i];
                Type type = typeParameters[i];
                registerTypeVariableIfNotPresent(typeVariable);
                // logger.log("For '" + parameterizedType + "' found actual type parameters : {"
                    // + typeVariable + ":" + type + "}.");
                contextualActualTypeParameters.put(typeVariable, type);
            }
        }

        @Override
        public Class<?> rawType() {
            return (Class<?>) this.parameterizedType.getRawType();
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
            super(source);
            this.parameterizedType = parameterizedType;
            this.typeParameters = typeParameters;
            readTypeVariables();
            readTypeParameters();
        }

        private void readTypeParameters() {
            Type genericType = parameterizedType.getOwnerType();
            if (genericType instanceof ParameterizedType) {
                Type[] actualTypeArguments = parameterizedType.getActualTypeArguments();
                ParameterizedType actualType = (ParameterizedType) genericType;

                for (int i = 0; i < actualTypeArguments.length; i++) {
                    Type actualTypeArgument = actualTypeArguments[i];
                    if (actualTypeArgument instanceof TypeVariable) {
                        TypeVariable<?> typeVariable = (TypeVariable<?>) actualTypeArgument;
                        // this is a type variable, try to resolve it
                        Type resolved = actualType.getActualTypeArguments()[i];
                        contextualActualTypeParameters.put(typeVariable, resolved);
                    }
                }
            }
        }

        private void readTypeVariables() {
            final TypeVariable<?>[] methodTypeVariables =
            method.getDeclaringClass().getTypeParameters();
            for (int i = 0; i < methodTypeVariables.length; i++) {
                contextualActualTypeParameters.put(
                methodTypeVariables[i], typeParameters[i]);
            }
        }

        @Override
        public Class<?> rawType() {
            readTypeParameters();
            readTypeVariables();

            return GenericMetadataSupport.inferFrom(parameterizedType)
            .actualTypeArguments()
            .values()
            .iterator()
            .next()
            .getClass();
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
            super();
            contextualActualTypeParameters = new HashMap<>(source.contextualActualTypeParameters);
            this.typeParameters = typeParameters;
            this.typeVariable = typeVariable;
            readTypeParameters();
            readTypeVariables();
        }

        private void readTypeParameters() {
            registerTypeParametersOn(typeParameters);
            readTypeVariables();
        }

        private void readTypeVariables() {
            registerTypeVariablesOn(typeVariable);
            for (int i = 0; i < typeParameters.length; i++) {
                TypeVariable<?> typeParameter = typeParameters[i];
                Type actualBoundedType = extractActualBoundedTypeOf(typeParameter);
                contextualActualTypeParameters.put(typeParameter, actualBoundedType);
            }
        }

        @Override
        public Class<?> rawType() {
            readTypeVariables();

            return rawType;
        }

        @Override
        public List<Type> extraInterfaces() {
            readTypeVariables();

            return extraInterfaces;
        }

        /**
         * @return Returns an array with the extracted raw types of {@link #extraInterfaces()}.
         * @see #extractRawTypeOf(java.lang.reflect.Type)
         */
        @Override
        public Class<?>[] rawExtraInterfaces() {
            Checks.that(extraInterfaces != null, "extraInterfaces");
            List<Class<?>> rawTypes = new ArrayList<>(extraInterfaces.size());
            for (Type extraInterface : extraInterfaces) {
                rawTypes.add(extractRawTypeOf(extraInterface));
            }
            return rawTypes.toArray(new Class<?>[extraInterfaces.size()]);
        }

        private Type extractActualBoundedTypeOf(Type type) {
            if (type instanceof TypeVariable) {
                TypeVariable<?> typeVar = (TypeVariable) type;
                int idx = 0;
                for (TypeVariable<?> tv : typeVar.getGenericDeclaration().getTypeParameters()) {
                    if (typeVar.equals(tv)) {
                        break;
                    }
                    idx++;
                }
                // logger.log("For TypeVariable " + type + " found index " + idx + " amongst
                // TypeParameters " + Arrays.toString(typeVar.getGenericDeclaration().getTypeParameters())));
                // logger.log("Will now use TypeVariable " + typeVar + " and retrieve bounds for
                // it...");

                BoundedType boundedType = boundsOf(typeVar);
                Type actualBoundedType = boundedType.firstBound();
                // logger.log("Found actual bounded type " + actualBoundedType + " for " +
                // typeVar);
                return actualBoundedType;
            }

            return type;
        }
    }

    private static class GenericArrayReturnType extends GenericMetadataSupport {

        private final GenericMetadataSupport genericArrayType;

        private final int arity;

        public GenericArrayReturnType(GenericMetadataSupport genericArrayType, int arity) {
            this.genericArrayType = genericArrayType;
            this.arity = arity;
        }

        @Override
        public Class<?> rawType() {
            return genericArrayType.rawType();
        }
    }

    /**
     * Non-Generic metadata for {@link Class} returned via {@link Method#getGenericReturnType()}.
     */
    private static class NotGenericReturnTypeSupport extends GenericMetadataSupport {
        private final Class<?> returnType;

        public NotGenericReturnTypeSupport(GenericMetadataSupport source, Type genericReturnType) {
            this.returnType = (Class<?>) genericReturnType;
        }

        @Override
        public Class<?> rawType() {
            return returnType;
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
            this.typeVariable = typeVariable;
        }

        /**
         * @return either a class or an interface (parameterized or not), if no bounds declared Object is returned.
         */
        @Override
        public Type firstBound() {
            return wildcard.getBounds()[0];
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
            //  Wildcards don't support multiple bounds.
            return new Type[0];
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) {
                return true;
            }
            if (o == null || getClass() != o.getClass()) {
                return false;
            }

            TypeVarBoundedType that = (TypeVarBoundedType) o;

            return typeVariable.equals(that.typeVariable);
        }

        @Override
        public int hashCode() {
            return typeVariable.hashCode();
        }

        @Override
        public String toString() {
            return typeVariable.toString();
        }

        public TypeVariable<?> typeVariable() {
            return typeVariable;
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
            this.wildcard = wildcard;
        }

        /** @return The first bound, either a type or a reference to a TypeVariable */
        @Override
        public Type firstBound() {
            return wildcard.getUpperBounds()[0];
        }

        /** @return An empty array as, wildcard don't support multiple bounds. */
        @Override
        public Type[] interfaceBounds() {
            return new Type[0];
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) {
                return true;
            }
            if (o == null || getClass() != o.getClass()) {
                return false;
            }

            WildCardBoundedType that = (WildCardBoundedType) o;

            return wildcard.equals(that.wildcard);
        }

        @Override
        public int hashCode() {
            return wildcard.hashCode();
        }

        @Override
        public String toString() {
            return "WildCardBoundedType{" + "wildcard=" + wildcard + '}';
        }

        public WildcardType wildCard() {
            return wildcard;
        }
    }
}
