package android.arch.lifecycle;

import android.arch.core.internal.SafeIterableMap;
import android.support.annotation.CallSuper;
import android.support.annotation.MainThread;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import java.util.Iterator;
import java.util.Map;

public class MediatorLiveData<T> extends MutableLiveData<T> {
  private SafeIterableMap<LiveData<?>, Source<?>> mSources = new SafeIterableMap();
  
  @MainThread
  public <S> void addSource(@NonNull LiveData<S> paramLiveData, @NonNull Observer<S> paramObserver) {
    Source source2 = new Source(paramLiveData, paramObserver);
    Source source1 = (Source)this.mSources.putIfAbsent(paramLiveData, source2);
    if (source1 == null || source1.mObserver == paramObserver) {
      if (source1 != null)
        return; 
      if (hasActiveObservers())
        source2.plug(); 
      return;
    } 
    throw new IllegalArgumentException("This source was already added with the different observer");
  }
  
  @CallSuper
  protected void onActive() {
    Iterator iterator = this.mSources.iterator();
    while (iterator.hasNext())
      ((Source)((Map.Entry)iterator.next()).getValue()).plug(); 
  }
  
  @CallSuper
  protected void onInactive() {
    Iterator iterator = this.mSources.iterator();
    while (iterator.hasNext())
      ((Source)((Map.Entry)iterator.next()).getValue()).unplug(); 
  }
  
  @MainThread
  public <S> void removeSource(@NonNull LiveData<S> paramLiveData) {
    Source source = (Source)this.mSources.remove(paramLiveData);
    if (source != null)
      source.unplug(); 
  }
  
  private static class Source<V> extends Object implements Observer<V> {
    final LiveData<V> mLiveData;
    
    final Observer<V> mObserver;
    
    int mVersion = -1;
    
    Source(LiveData<V> param1LiveData, Observer<V> param1Observer) {
      this.mLiveData = param1LiveData;
      this.mObserver = param1Observer;
    }
    
    public void onChanged(@Nullable V param1V) {
      if (this.mVersion != this.mLiveData.getVersion()) {
        this.mVersion = this.mLiveData.getVersion();
        this.mObserver.onChanged(param1V);
      } 
    }
    
    void plug() { this.mLiveData.observeForever(this); }
    
    void unplug() { this.mLiveData.removeObserver(this); }
  }
}


/* Location:              D:\Apks\New folder\classes-dex2jar.jar!\android\arch\lifecycle\MediatorLiveData.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.0.7
 */