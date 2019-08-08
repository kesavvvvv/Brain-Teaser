package android.arch.lifecycle;

import android.arch.core.executor.ArchTaskExecutor;
import android.support.annotation.MainThread;
import android.support.annotation.NonNull;
import android.support.annotation.RestrictTo;
import android.support.annotation.VisibleForTesting;
import android.support.annotation.WorkerThread;
import java.util.concurrent.Executor;
import java.util.concurrent.atomic.AtomicBoolean;

@RestrictTo({RestrictTo.Scope.LIBRARY_GROUP})
public abstract class ComputableLiveData<T> extends Object {
  private AtomicBoolean mComputing = new AtomicBoolean(false);
  
  private final Executor mExecutor;
  
  private AtomicBoolean mInvalid = new AtomicBoolean(true);
  
  @VisibleForTesting
  final Runnable mInvalidationRunnable = new Runnable() {
      @MainThread
      public void run() {
        boolean bool = ComputableLiveData.this.mLiveData.hasActiveObservers();
        if (ComputableLiveData.this.mInvalid.compareAndSet(false, true) && bool)
          ComputableLiveData.this.mExecutor.execute(ComputableLiveData.this.mRefreshRunnable); 
      }
    };
  
  private final LiveData<T> mLiveData;
  
  @VisibleForTesting
  final Runnable mRefreshRunnable = new Runnable() {
      @WorkerThread
      public void run() {
        byte b;
        do {
          b = 0;
          byte b1 = 0;
          if (!ComputableLiveData.this.mComputing.compareAndSet(false, true))
            continue; 
          null = null;
          b = b1;
          try {
            while (ComputableLiveData.this.mInvalid.compareAndSet(true, false)) {
              b = 1;
              null = ComputableLiveData.this.compute();
            } 
            if (b != 0)
              ComputableLiveData.this.mLiveData.postValue(null); 
          } finally {
            ComputableLiveData.this.mComputing.set(false);
          } 
        } while (b != 0 && ComputableLiveData.this.mInvalid.get());
      }
    };
  
  public ComputableLiveData() { this(ArchTaskExecutor.getIOThreadExecutor()); }
  
  public ComputableLiveData(@NonNull Executor paramExecutor) {
    this.mExecutor = paramExecutor;
    this.mLiveData = new LiveData<T>() {
        protected void onActive() { ComputableLiveData.this.mExecutor.execute(ComputableLiveData.this.mRefreshRunnable); }
      };
  }
  
  @WorkerThread
  protected abstract T compute();
  
  @NonNull
  public LiveData<T> getLiveData() { return this.mLiveData; }
  
  public void invalidate() { ArchTaskExecutor.getInstance().executeOnMainThread(this.mInvalidationRunnable); }
}


/* Location:              D:\Apks\New folder\classes-dex2jar.jar!\android\arch\lifecycle\ComputableLiveData.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.0.7
 */