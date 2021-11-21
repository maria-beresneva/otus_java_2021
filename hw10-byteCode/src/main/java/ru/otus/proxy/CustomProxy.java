package ru.otus.proxy;

import ru.otus.logger.Log;
import ru.otus.testclasses.Calculator;
import ru.otus.testclasses.ICalculator;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.Arrays;
import java.util.Map;
import java.util.stream.Collectors;

public class CustomProxy {

     public static ICalculator createCalculator() {
        InvocationHandler handler = new DemoInvocationHandler(new Calculator());
        return (ICalculator) java.lang.reflect.Proxy.newProxyInstance(CustomProxy.class.getClassLoader(),
                new Class<?>[]{ICalculator.class}, handler);
    }

    static class DemoInvocationHandler implements InvocationHandler {
        private final ICalculator myClass;
        private final Map<String, Boolean> isUsingProxy;
        private final Method[] declaredMethods;

        DemoInvocationHandler(ICalculator myClass) {
            this.myClass = myClass;

            declaredMethods = myClass.getClass().getDeclaredMethods();

            Class<ICalculator> calcClass = ICalculator.class;
            isUsingProxy =
                    Arrays.stream(calcClass.getDeclaredMethods())
                            .collect(
                                    Collectors.toMap(
                                            Method::toString,
                                            this::hasAnnotation));
        }

        @Override
        public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
            if (isUsingProxy.get(method.toString())) {
                System.out.println("Logging params :" + Arrays.toString(args));
            }
            return method.invoke(myClass, args);
        }

        boolean hasAnnotation(Method method) {
            for (final Method methodItem : declaredMethods) {
                if(methodItem.getName().equals(method.getName()) &&
                        isTheSameParamSet(method.getParameters(), methodItem.getParameters()))
                    if (methodItem.isAnnotationPresent(Log.class)) {
                        return true;
                    }
            }
            return false;
        }

        static boolean isTheSameParamSet(Parameter[] calledMethodParams, Parameter[] declaredMethodParams) {
            if( calledMethodParams.length != declaredMethodParams.length) return false;
            for (int i = 0; i< calledMethodParams.length; i++){
                if(!calledMethodParams[i].getType().equals(declaredMethodParams[i].getType())) {
                    return false;
                }
            }
            return true;
        }
    }
}
