package android.arch.lifecycle;

import android.arch.core.util.Function;
import android.support.annotation.MainThread;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

public class Transformations {
  @MainThread
  public static <X, Y> LiveData<Y> map(@NonNull LiveData<X> paramLiveData, @NonNull final Function<X, Y> func) {
    final MediatorLiveData result = new MediatorLiveData();
    mediatorLiveData.addSource(paramLiveData, new Observer<X>() {
          public void onChanged(@Nullable X param1X) { result.setValue(func.apply(param1X)); }
        });
    return mediatorLiveData;
  }
  
  @MainThread
  public static <X, Y> LiveData<Y> switchMap(@NonNull LiveData<X> paramLiveData, @NonNull final Function<X, LiveData<Y>> func) {
    final MediatorLiveData result = new MediatorLiveData();
    mediatorLiveData.addSource(paramLiveData, new Observer<X>() {
          LiveData<Y> mSource;
          
          public void onChanged(@Nullable X param1X) {
            param1X = (X)(LiveData)func.apply(param1X);
            LiveData liveData = this.mSource;
            if (liveData == param1X)
              return; 
            if (liveData != null)
              result.removeSource(liveData); 
            this.mSource = param1X;
            param1X = (X)this.mSource;
            if (param1X != null)
              result.addSource(param1X, new Observer<Y>() {
                    public void onChanged(@Nullable Y param2Y) { result.setValue(param2Y); }
                  }); 
          }
        });
    return mediatorLiveData;
  }
}


/* Location:              D:\Apks\New folder\classes-dex2jar.jar!\android\arch\lifecycle\Transformations.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.0.7
 */