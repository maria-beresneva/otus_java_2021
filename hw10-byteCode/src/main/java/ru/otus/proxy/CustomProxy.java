package ru.otus.proxy;

import ru.otus.logger.Log;
import ru.otus.testclasses.Calculator;
import ru.otus.testclasses.ICalculator;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class CustomProxy {

     public static ICalculator createCalculator() {
        InvocationHandler handler = new DemoInvocationHandler(new Calculator());
        return (ICalculator) java.lang.reflect.Proxy.newProxyInstance(CustomProxy.class.getClassLoader(),
                new Class<?>[]{ICalculator.class}, handler);
    }

    static class DemoInvocationHandler implements InvocationHandler {
        private final ICalculator myClass;
        private final List<Method> methods;

        DemoInvocationHandler(ICalculator myClass) {
            this.myClass = myClass;
            methods = new ArrayList<>(Arrays.asList(myClass.getClass().getDeclaredMethods()));
        }

        @Override
        public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
            for (final Method methodItem : methods) {
                if(methodItem.getName().equals(method.getName()) &&
                        isTheSameParamSet(method.getParameters(), methodItem.getParameters()))
                if (methodItem.isAnnotationPresent(Log.class)) {
                    System.out.println("Logging params :" + Arrays.toString(args));
                }
            }
            return method.invoke(myClass, args);
        }

        static boolean isTheSameParamSet(Parameter[] calledMethodParams, Parameter[] declaredMethodParams) {
            if( calledMethodParams.length != declaredMethodParams.length) return false;
            for (int i = 0; i< calledMethodParams.length; i++){
                if(!calledMethodParams[i].getName().equals(declaredMethodParams[i].getName()) ||
                        !calledMethodParams[i].getType().equals(declaredMethodParams[i].getType())) {
                    return false;
                }
            }
            return true;
        }
    }
}
