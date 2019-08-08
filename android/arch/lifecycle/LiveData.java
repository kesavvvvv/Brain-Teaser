package android.arch.lifecycle;

import android.arch.core.executor.ArchTaskExecutor;
import android.arch.core.internal.SafeIterableMap;
import android.support.annotation.MainThread;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import java.util.Map;

public abstract class LiveData<T> extends Object {
  private static final Object NOT_SET = new Object();
  
  static final int START_VERSION = -1;
  
  private int mActiveCount = 0;
  
  private final Object mDataLock = new Object();
  
  private boolean mDispatchInvalidated;
  
  private boolean mDispatchingValue;
  
  private SafeIterableMap<Observer<T>, ObserverWrapper> mObservers = new SafeIterableMap();
  
  private final Runnable mPostValueRunnable;
  
  private int mVersion;
  
  public LiveData() {
    Object object = NOT_SET;
    this.mData = object;
    this.mPendingData = object;
    this.mVersion = -1;
    this.mPostValueRunnable = new Runnable() {
        public void run() {
          synchronized (LiveData.this.mDataLock) {
            Object object = LiveData.this.mPendingData;
            LiveData.access$102(LiveData.this, NOT_SET);
            LiveData.this.setValue(object);
            return;
          } 
        }
      };
  }
  
  private static void assertMainThread(String paramString) {
    if (ArchTaskExecutor.getInstance().isMainThread())
      return; 
    StringBuilder stringBuilder = new StringBuilder();
    stringBuilder.append("Cannot invoke ");
    stringBuilder.append(paramString);
    stringBuilder.append(" on a background");
    stringBuilder.append(" thread");
    throw new IllegalStateException(stringBuilder.toString());
  }
  
  private void considerNotify(ObserverWrapper paramObserverWrapper) {
    if (!paramObserverWrapper.mActive)
      return; 
    if (!paramObserverWrapper.shouldBeActive()) {
      paramObserverWrapper.activeStateChanged(false);
      return;
    } 
    int i = paramObserverWrapper.mLastVersion;
    int j = this.mVersion;
    if (i >= j)
      return; 
    paramObserverWrapper.mLastVersion = j;
    paramObserverWrapper.mObserver.onChanged(this.mData);
  }
  
  private void dispatchingValue(@Nullable ObserverWrapper paramObserverWrapper) {
    if (this.mDispatchingValue) {
      this.mDispatchInvalidated = true;
      return;
    } 
    this.mDispatchingValue = true;
    while (true) {
      ObserverWrapper observerWrapper;
      this.mDispatchInvalidated = false;
      if (paramObserverWrapper != null) {
        considerNotify(paramObserverWrapper);
        observerWrapper = null;
      } else {
        SafeIterableMap.IteratorWithAdditions iteratorWithAdditions = this.mObservers.iteratorWithAdditions();
        while (true) {
          observerWrapper = paramObserverWrapper;
          if (iteratorWithAdditions.hasNext()) {
            considerNotify((ObserverWrapper)((Map.Entry)iteratorWithAdditions.next()).getValue());
            if (this.mDispatchInvalidated) {
              observerWrapper = paramObserverWrapper;
              break;
            } 
            continue;
          } 
          break;
        } 
      } 
      if (!this.mDispatchInvalidated) {
        this.mDispatchingValue = false;
        return;
      } 
      paramObserverWrapper = observerWrapper;
    } 
  }
  
  @Nullable
  public T getValue() {
    Object object = this.mData;
    return (object != NOT_SET) ? (T)object : null;
  }
  
  int getVersion() { return this.mVersion; }
  
  public boolean hasActiveObservers() { return (this.mActiveCount > 0); }
  
  public boolean hasObservers() { return (this.mObservers.size() > 0); }
  
  @MainThread
  public void observe(@NonNull LifecycleOwner paramLifecycleOwner, @NonNull Observer<T> paramObserver) {
    if (paramLifecycleOwner.getLifecycle().getCurrentState() == Lifecycle.State.DESTROYED)
      return; 
    LifecycleBoundObserver lifecycleBoundObserver = new LifecycleBoundObserver(paramLifecycleOwner, paramObserver);
    ObserverWrapper observerWrapper = (ObserverWrapper)this.mObservers.putIfAbsent(paramObserver, lifecycleBoundObserver);
    if (observerWrapper == null || observerWrapper.isAttachedTo(paramLifecycleOwner)) {
      if (observerWrapper != null)
        return; 
      paramLifecycleOwner.getLifecycle().addObserver(lifecycleBoundObserver);
      return;
    } 
    throw new IllegalArgumentException("Cannot add the same observer with different lifecycles");
  }
  
  @MainThread
  public void observeForever(@NonNull Observer<T> paramObserver) {
    AlwaysActiveObserver alwaysActiveObserver = new AlwaysActiveObserver(paramObserver);
    ObserverWrapper observerWrapper = (ObserverWrapper)this.mObservers.putIfAbsent(paramObserver, alwaysActiveObserver);
    if (observerWrapper == null || !(observerWrapper instanceof LifecycleBoundObserver)) {
      if (observerWrapper != null)
        return; 
      alwaysActiveObserver.activeStateChanged(true);
      return;
    } 
    throw new IllegalArgumentException("Cannot add the same observer with different lifecycles");
  }
  
  protected void onActive() {}
  
  protected void onInactive() {}
  
  protected void postValue(T paramT) { // Byte code:
    //   0: aload_0
    //   1: getfield mDataLock : Ljava/lang/Object;
    //   4: astore_3
    //   5: aload_3
    //   6: monitorenter
    //   7: iconst_0
    //   8: istore_2
    //   9: aload_0
    //   10: getfield mPendingData : Ljava/lang/Object;
    //   13: getstatic android/arch/lifecycle/LiveData.NOT_SET : Ljava/lang/Object;
    //   16: if_acmpne -> 21
    //   19: iconst_1
    //   20: istore_2
    //   21: aload_0
    //   22: aload_1
    //   23: putfield mPendingData : Ljava/lang/Object;
    //   26: aload_3
    //   27: monitorexit
    //   28: iload_2
    //   29: ifne -> 33
    //   32: return
    //   33: invokestatic getInstance : ()Landroid/arch/core/executor/ArchTaskExecutor;
    //   36: aload_0
    //   37: getfield mPostValueRunnable : Ljava/lang/Runnable;
    //   40: invokevirtual postToMainThread : (Ljava/lang/Runnable;)V
    //   43: return
    //   44: astore_1
    //   45: aload_3
    //   46: monitorexit
    //   47: aload_1
    //   48: athrow
    // Exception table:
    //   from	to	target	type
    //   9	19	44	finally
    //   21	28	44	finally
    //   45	47	44	finally }
  
  @MainThread
  public void removeObserver(@NonNull Observer<T> paramObserver) {
    assertMainThread("removeObserver");
    ObserverWrapper observerWrapper = (ObserverWrapper)this.mObservers.remove(paramObserver);
    if (observerWrapper == null)
      return; 
    observerWrapper.detachObserver();
    observerWrapper.activeStateChanged(false);
  }
  
  @MainThread
  public void removeObservers(@NonNull LifecycleOwner paramLifecycleOwner) {
    assertMainThread("removeObservers");
    for (Map.Entry entry : this.mObservers) {
      if (((ObserverWrapper)entry.getValue()).isAttachedTo(paramLifecycleOwner))
        removeObserver((Observer)entry.getKey()); 
    } 
  }
  
  @MainThread
  protected void setValue(T paramT) {
    assertMainThread("setValue");
    this.mVersion++;
    this.mData = paramT;
    dispatchingValue(null);
  }
  
  private class AlwaysActiveObserver extends ObserverWrapper {
    AlwaysActiveObserver(Observer<T> param1Observer) { super(LiveData.this, param1Observer); }
    
    boolean shouldBeActive() { return true; }
  }
  
  class LifecycleBoundObserver extends ObserverWrapper implements GenericLifecycleObserver {
    @NonNull
    final LifecycleOwner mOwner;
    
    LifecycleBoundObserver(LifecycleOwner param1LifecycleOwner, Observer<T> param1Observer) {
      super(LiveData.this, param1Observer);
      this.mOwner = param1LifecycleOwner;
    }
    
    void detachObserver() { this.mOwner.getLifecycle().removeObserver(this); }
    
    boolean isAttachedTo(LifecycleOwner param1LifecycleOwner) { return (this.mOwner == param1LifecycleOwner); }
    
    public void onStateChanged(LifecycleOwner param1LifecycleOwner, Lifecycle.Event param1Event) {
      if (this.mOwner.getLifecycle().getCurrentState() == Lifecycle.State.DESTROYED) {
        LiveData.this.removeObserver(this.mObserver);
        return;
      } 
      activeStateChanged(shouldBeActive());
    }
    
    boolean shouldBeActive() { return this.mOwner.getLifecycle().getCurrentState().isAtLeast(Lifecycle.State.STARTED); }
  }
  
  private abstract class ObserverWrapper {
    boolean mActive;
    
    int mLastVersion = -1;
    
    final Observer<T> mObserver;
    
    ObserverWrapper(Observer<T> param1Observer) { this.mObserver = param1Observer; }
    
    void activeStateChanged(boolean param1Boolean) {
      if (param1Boolean == this.mActive)
        return; 
      this.mActive = param1Boolean;
      int i = LiveData.this.mActiveCount;
      int j = 1;
      if (i == 0) {
        i = 1;
      } else {
        i = 0;
      } 
      LiveData liveData;
      int k = liveData.mActiveCount;
      if (!this.mActive)
        j = -1; 
      LiveData.access$302(liveData, k + j);
      if (i != 0 && this.mActive)
        LiveData.this.onActive(); 
      if (LiveData.this.mActiveCount == 0 && !this.mActive)
        LiveData.this.onInactive(); 
      if (this.mActive)
        LiveData.this.dispatchingValue(this); 
    }
    
    void detachObserver() {}
    
    boolean isAttachedTo(LifecycleOwner param1LifecycleOwner) { return false; }
    
    abstract boolean shouldBeActive();
  }
}


/* Location:              D:\Apks\New folder\classes-dex2jar.jar!\android\arch\lifecycle\LiveData.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.0.7
 */