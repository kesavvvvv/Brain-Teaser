package android.arch.lifecycle;

import java.util.HashMap;
import java.util.Iterator;

public class ViewModelStore {
  private final HashMap<String, ViewModel> mMap = new HashMap();
  
  public final void clear() {
    Iterator iterator = this.mMap.values().iterator();
    while (iterator.hasNext())
      ((ViewModel)iterator.next()).onCleared(); 
    this.mMap.clear();
  }
  
  final ViewModel get(String paramString) { return (ViewModel)this.mMap.get(paramString); }
  
  final void put(String paramString, ViewModel paramViewModel) {
    ViewModel viewModel = (ViewModel)this.mMap.put(paramString, paramViewModel);
    if (viewModel != null)
      viewModel.onCleared(); 
  }
}


/* Location:              D:\Apks\New folder\classes-dex2jar.jar!\android\arch\lifecycle\ViewModelStore.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.0.7
 */