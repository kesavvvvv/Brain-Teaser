package android.arch.lifecycle;

import android.support.annotation.RestrictTo;
import java.util.HashMap;
import java.util.Map;

@RestrictTo({RestrictTo.Scope.LIBRARY_GROUP})
public class MethodCallsLogger {
  private Map<String, Integer> mCalledMethods = new HashMap();
  
  @RestrictTo({RestrictTo.Scope.LIBRARY_GROUP})
  public boolean approveCall(String paramString, int paramInt) {
    boolean bool;
    int i;
    Integer integer = (Integer)this.mCalledMethods.get(paramString);
    boolean bool1 = false;
    if (integer != null) {
      i = integer.intValue();
    } else {
      i = 0;
    } 
    if ((i & paramInt) != 0) {
      bool = true;
    } else {
      bool = false;
    } 
    this.mCalledMethods.put(paramString, Integer.valueOf(i | paramInt));
    if (!bool)
      bool1 = true; 
    return bool1;
  }
}


/* Location:              D:\Apks\New folder\classes-dex2jar.jar!\android\arch\lifecycle\MethodCallsLogger.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.0.7
 */