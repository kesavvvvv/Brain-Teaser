package android.arch.lifecycle;

import android.support.annotation.Nullable;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

class ClassesInfoCache {
  private static final int CALL_TYPE_NO_ARG = 0;
  
  private static final int CALL_TYPE_PROVIDER = 1;
  
  private static final int CALL_TYPE_PROVIDER_WITH_EVENT = 2;
  
  static ClassesInfoCache sInstance = new ClassesInfoCache();
  
  private final Map<Class, CallbackInfo> mCallbackMap = new HashMap();
  
  private final Map<Class, Boolean> mHasLifecycleMethods = new HashMap();
  
  private CallbackInfo createInfo(Class paramClass, @Nullable Method[] paramArrayOfMethod) {
    Class clazz = paramClass.getSuperclass();
    HashMap hashMap = new HashMap();
    if (clazz != null) {
      CallbackInfo callbackInfo1 = getInfo(clazz);
      if (callbackInfo1 != null)
        hashMap.putAll(callbackInfo1.mHandlerToEvent); 
    } 
    Class[] arrayOfClass = paramClass.getInterfaces();
    int i = arrayOfClass.length;
    byte b;
    for (b = 0; b < i; b++) {
      for (Map.Entry entry : (getInfo(arrayOfClass[b])).mHandlerToEvent.entrySet())
        verifyAndPutHandler(hashMap, (MethodReference)entry.getKey(), (Lifecycle.Event)entry.getValue(), paramClass); 
    } 
    if (paramArrayOfMethod == null)
      paramArrayOfMethod = getDeclaredMethods(paramClass); 
    int j = paramArrayOfMethod.length;
    boolean bool = false;
    for (i = 0; i < j; i++) {
      Method method = paramArrayOfMethod[i];
      OnLifecycleEvent onLifecycleEvent = (OnLifecycleEvent)method.getAnnotation(OnLifecycleEvent.class);
      if (onLifecycleEvent != null) {
        bool = true;
        Class[] arrayOfClass1 = method.getParameterTypes();
        b = 0;
        if (arrayOfClass1.length > 0) {
          b = 1;
          if (!arrayOfClass1[0].isAssignableFrom(LifecycleOwner.class))
            throw new IllegalArgumentException("invalid parameter type. Must be one and instanceof LifecycleOwner"); 
        } 
        Lifecycle.Event event = onLifecycleEvent.value();
        if (arrayOfClass1.length > 1) {
          b = 2;
          if (arrayOfClass1[1].isAssignableFrom(Lifecycle.Event.class)) {
            if (event != Lifecycle.Event.ON_ANY)
              throw new IllegalArgumentException("Second arg is supported only for ON_ANY value"); 
          } else {
            throw new IllegalArgumentException("invalid parameter type. second arg must be an event");
          } 
        } 
        if (arrayOfClass1.length <= 2) {
          verifyAndPutHandler(hashMap, new MethodReference(b, method), event, paramClass);
        } else {
          throw new IllegalArgumentException("cannot have more than 2 params");
        } 
      } 
    } 
    CallbackInfo callbackInfo = new CallbackInfo(hashMap);
    this.mCallbackMap.put(paramClass, callbackInfo);
    this.mHasLifecycleMethods.put(paramClass, Boolean.valueOf(bool));
    return callbackInfo;
  }
  
  private Method[] getDeclaredMethods(Class paramClass) {
    try {
      return paramClass.getDeclaredMethods();
    } catch (NoClassDefFoundError paramClass) {
      throw new IllegalArgumentException("The observer class has some methods that use newer APIs which are not available in the current OS version. Lifecycles cannot access even other methods so you should make sure that your observer classes only access framework classes that are available in your min API level OR use lifecycle:compiler annotation processor.", paramClass);
    } 
  }
  
  private void verifyAndPutHandler(Map<MethodReference, Lifecycle.Event> paramMap, MethodReference paramMethodReference, Lifecycle.Event paramEvent, Class paramClass) {
    Lifecycle.Event event = (Lifecycle.Event)paramMap.get(paramMethodReference);
    if (event == null || paramEvent == event) {
      if (event == null)
        paramMap.put(paramMethodReference, paramEvent); 
      return;
    } 
    Method method = paramMethodReference.mMethod;
    StringBuilder stringBuilder = new StringBuilder();
    stringBuilder.append("Method ");
    stringBuilder.append(method.getName());
    stringBuilder.append(" in ");
    stringBuilder.append(paramClass.getName());
    stringBuilder.append(" already declared with different @OnLifecycleEvent value: previous value ");
    stringBuilder.append(event);
    stringBuilder.append(", new value ");
    stringBuilder.append(paramEvent);
    throw new IllegalArgumentException(stringBuilder.toString());
  }
  
  CallbackInfo getInfo(Class paramClass) {
    CallbackInfo callbackInfo = (CallbackInfo)this.mCallbackMap.get(paramClass);
    return (callbackInfo != null) ? callbackInfo : createInfo(paramClass, null);
  }
  
  boolean hasLifecycleMethods(Class paramClass) {
    if (this.mHasLifecycleMethods.containsKey(paramClass))
      return ((Boolean)this.mHasLifecycleMethods.get(paramClass)).booleanValue(); 
    Method[] arrayOfMethod = getDeclaredMethods(paramClass);
    int i = arrayOfMethod.length;
    for (byte b = 0; b < i; b++) {
      if ((OnLifecycleEvent)arrayOfMethod[b].getAnnotation(OnLifecycleEvent.class) != null) {
        createInfo(paramClass, arrayOfMethod);
        return true;
      } 
    } 
    this.mHasLifecycleMethods.put(paramClass, Boolean.valueOf(false));
    return false;
  }
  
  static class CallbackInfo {
    final Map<Lifecycle.Event, List<ClassesInfoCache.MethodReference>> mEventToHandlers;
    
    final Map<ClassesInfoCache.MethodReference, Lifecycle.Event> mHandlerToEvent;
    
    CallbackInfo(Map<ClassesInfoCache.MethodReference, Lifecycle.Event> param1Map) {
      this.mHandlerToEvent = param1Map;
      this.mEventToHandlers = new HashMap();
      for (Map.Entry entry : param1Map.entrySet()) {
        Lifecycle.Event event = (Lifecycle.Event)entry.getValue();
        List list2 = (List)this.mEventToHandlers.get(event);
        List list1 = list2;
        if (list2 == null) {
          list1 = new ArrayList();
          this.mEventToHandlers.put(event, list1);
        } 
        list1.add(entry.getKey());
      } 
    }
    
    private static void invokeMethodsForEvent(List<ClassesInfoCache.MethodReference> param1List, LifecycleOwner param1LifecycleOwner, Lifecycle.Event param1Event, Object param1Object) {
      if (param1List != null) {
        int i;
        for (i = param1List.size() - 1; i >= 0; i--)
          ((ClassesInfoCache.MethodReference)param1List.get(i)).invokeCallback(param1LifecycleOwner, param1Event, param1Object); 
      } 
    }
    
    void invokeCallbacks(LifecycleOwner param1LifecycleOwner, Lifecycle.Event param1Event, Object param1Object) {
      invokeMethodsForEvent((List)this.mEventToHandlers.get(param1Event), param1LifecycleOwner, param1Event, param1Object);
      invokeMethodsForEvent((List)this.mEventToHandlers.get(Lifecycle.Event.ON_ANY), param1LifecycleOwner, param1Event, param1Object);
    }
  }
  
  static class MethodReference {
    final int mCallType;
    
    final Method mMethod;
    
    MethodReference(int param1Int, Method param1Method) {
      this.mCallType = param1Int;
      this.mMethod = param1Method;
      this.mMethod.setAccessible(true);
    }
    
    public boolean equals(Object param1Object) {
      if (this == param1Object)
        return true; 
      if (param1Object != null) {
        if (getClass() != param1Object.getClass())
          return false; 
        param1Object = (MethodReference)param1Object;
        return (this.mCallType == param1Object.mCallType && this.mMethod.getName().equals(param1Object.mMethod.getName()));
      } 
      return false;
    }
    
    public int hashCode() { return this.mCallType * 31 + this.mMethod.getName().hashCode(); }
    
    void invokeCallback(LifecycleOwner param1LifecycleOwner, Lifecycle.Event param1Event, Object param1Object) {
      try {
        switch (this.mCallType) {
          case 2:
            this.mMethod.invoke(param1Object, new Object[] { param1LifecycleOwner, param1Event });
            return;
          case 1:
            this.mMethod.invoke(param1Object, new Object[] { param1LifecycleOwner });
            return;
          case 0:
            this.mMethod.invoke(param1Object, new Object[0]);
            return;
        } 
      } catch (InvocationTargetException param1LifecycleOwner) {
        throw new RuntimeException("Failed to call observer method", param1LifecycleOwner.getCause());
      } catch (IllegalAccessException param1LifecycleOwner) {
        throw new RuntimeException(param1LifecycleOwner);
      } 
    }
  }
}


/* Location:              D:\Apks\New folder\classes-dex2jar.jar!\android\arch\lifecycle\ClassesInfoCache.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.0.7
 */