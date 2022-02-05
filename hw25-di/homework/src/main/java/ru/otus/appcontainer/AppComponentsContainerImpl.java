package ru.otus.appcontainer;

import ru.otus.appcontainer.api.AppComponent;
import ru.otus.appcontainer.api.AppComponentsContainer;
import ru.otus.appcontainer.api.AppComponentsContainerConfig;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.*;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class AppComponentsContainerImpl implements AppComponentsContainer {

    private final List<Object> appComponents = new ArrayList<>();
    private final Map<String, Object> appComponentsByName = new HashMap<>();

    public AppComponentsContainerImpl(Class<?> initialConfigClass) {
        processConfig(initialConfigClass);
    }

    @Override
    public <C> C getAppComponent(final Class<C> componentClass) {
        return (C) findComponent(componentClass);
    }

    @Override
    public <C> C getAppComponent(String componentName) {
        return (C) appComponentsByName.get(componentName);
    }

    private void processConfig(Class<?> configClass) {
        checkConfigClass(configClass);
        final Object instance = getInstanceClass(configClass);
        for (Method method : getMethods(configClass)) {
            final String componentName = method.getDeclaredAnnotation(AppComponent.class).name();
            final Object[] args = getArguments(method);
            try {
                final Object executionResult = method.invoke(instance, args);
                appComponentsByName.put(componentName, executionResult);
                appComponents.add(executionResult);
            } catch (IllegalAccessException | InvocationTargetException e) {
                throw new IllegalArgumentException(String.format("Failed to initialize app component %s. ", componentName));
            }
        }
    }

    private void checkConfigClass(Class<?> configClass) {
        if (!configClass.isAnnotationPresent(AppComponentsContainerConfig.class)) {
            throw new IllegalArgumentException(String.format("Given class is not config %s", configClass.getName()));
        }
    }

    private Object getInstanceClass(Class<?> clazz) {
        try {
            return clazz.getConstructor().newInstance();
        } catch (NoSuchMethodException | SecurityException | InstantiationException | IllegalAccessException | InvocationTargetException e) {
            throw new IllegalArgumentException(String.format("Failed to initialize class %s.", clazz));
        }
    }

    private List<Method> getMethods(Class<?> clazz) {
        final Predicate<Method> isAppComponent = method -> method.isAnnotationPresent(AppComponent.class);
        final Comparator<Method> orderComparator = Comparator.comparingInt(
                method -> method.getAnnotation(AppComponent.class).order());
        return Stream.of(clazz.getDeclaredMethods())
                .filter(isAppComponent)
                .sorted(orderComparator)
                .collect(Collectors.toList());
    }

    private Object[] getArguments(Method method) {
        final Class<?>[] parameterTypes = method.getParameterTypes();
        final int parameterCount = parameterTypes.length;
        if (parameterCount == 0) {
            return new Object[0];
        }
        return Stream.of(parameterTypes)
                .map(this::findComponent)
                .toArray();
    }

    private Object findComponent(Class<?> param) {
        return appComponents.stream()
                .filter(component -> param.isAssignableFrom(component.getClass()))
                .findFirst()
                .get();
    }
}
