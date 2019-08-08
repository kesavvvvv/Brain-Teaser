package android.arch.lifecycle;

interface FullLifecycleObserver extends LifecycleObserver {
  void onCreate(LifecycleOwner paramLifecycleOwner);
  
  void onDestroy(LifecycleOwner paramLifecycleOwner);
  
  void onPause(LifecycleOwner paramLifecycleOwner);
  
  void onResume(LifecycleOwner paramLifecycleOwner);
  
  void onStart(LifecycleOwner paramLifecycleOwner);
  
  void onStop(LifecycleOwner paramLifecycleOwner);
}


/* Location:              D:\Apks\New folder\classes-dex2jar.jar!\android\arch\lifecycle\FullLifecycleObserver.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.0.7
 */