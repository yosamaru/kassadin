package com.yosamaru.kassadin.util;

import java.lang.reflect.Array;
import java.lang.reflect.GenericArrayType;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.lang.reflect.TypeVariable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ClassUtil {
	public ClassUtil() {
	}

	public static <T> List<Class<?>> getTypeArguments(final Class<T> baseClass, final Class<? extends T> childClass) {
		Map<Type, Type> resolvedTypes = new HashMap();
		Object type = childClass;

		while(true) {
			int i;
			while(!getClass((Type)type).equals(baseClass)) {
				if (type instanceof Class) {
					type = ((Class)type).getGenericSuperclass();
				} else {
					ParameterizedType parameterizedType = (ParameterizedType)type;
					Class<?> rawType = (Class)parameterizedType.getRawType();
					Type[] actualTypeArguments = parameterizedType.getActualTypeArguments();
					TypeVariable<?>[] typeParameters = rawType.getTypeParameters();

					for(i = 0; i < actualTypeArguments.length; ++i) {
						resolvedTypes.put(typeParameters[i], actualTypeArguments[i]);
					}

					if (!rawType.equals(baseClass)) {
						type = rawType.getGenericSuperclass();
					}
				}
			}

			Object actualTypeArguments;
			if (type instanceof Class) {
				actualTypeArguments = ((Class)type).getTypeParameters();
			} else {
				actualTypeArguments = ((ParameterizedType)type).getActualTypeArguments();
			}

			List<Class<?>> typeArgumentsAsClasses = new ArrayList();
			Object var12 = actualTypeArguments;
			int var13 = ((Object[])actualTypeArguments).length;

			for(i = 0; i < var13; ++i) {
				Object baseType;
				for(baseType = ((Object[])var12)[i]; resolvedTypes.containsKey(baseType); baseType = (Type)resolvedTypes.get(baseType)) {
				}

				typeArgumentsAsClasses.add(getClass((Type)baseType));
			}

			return typeArgumentsAsClasses;
		}
	}

	private static Class<?> getClass(final Type type) {
		if (type instanceof Class) {
			return (Class)type;
		} else if (type instanceof ParameterizedType) {
			return getClass(((ParameterizedType)type).getRawType());
		} else if (type instanceof GenericArrayType) {
			Type componentType = ((GenericArrayType)type).getGenericComponentType();
			Class<?> componentClass = getClass(componentType);
			return componentClass != null ? Array.newInstance(componentClass, 0).getClass() : null;
		} else {
			return null;
		}
	}

	public static boolean exists(final String classFullName) {
		boolean ret = false;

		try {
			Class.forName(classFullName);
			ret = true;
		} catch (ClassNotFoundException var3) {
		}

		return ret;
	}
}
