package android.arch.lifecycle;

import android.support.annotation.RestrictTo;

@RestrictTo({RestrictTo.Scope.LIBRARY})
public interface GenericLifecycleObserver extends LifecycleObserver {
  void onStateChanged(LifecycleOwner paramLifecycleOwner, Lifecycle.Event paramEvent);
}


/* Location:              D:\Apks\New folder\classes-dex2jar.jar!\android\arch\lifecycle\GenericLifecycleObserver.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.0.7
 */