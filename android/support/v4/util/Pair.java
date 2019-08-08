package android.support.v4.util;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

public class Pair<F, S> extends Object {
  @Nullable
  public final F first;
  
  @Nullable
  public final S second;
  
  public Pair(@Nullable F paramF, @Nullable S paramS) {
    this.first = paramF;
    this.second = paramS;
  }
  
  @NonNull
  public static <A, B> Pair<A, B> create(@Nullable A paramA, @Nullable B paramB) { return new Pair(paramA, paramB); }
  
  public boolean equals(Object paramObject) {
    boolean bool = paramObject instanceof Pair;
    boolean bool1 = false;
    if (!bool)
      return false; 
    paramObject = (Pair)paramObject;
    bool = bool1;
    if (ObjectsCompat.equals(paramObject.first, this.first)) {
      bool = bool1;
      if (ObjectsCompat.equals(paramObject.second, this.second))
        bool = true; 
    } 
    return bool;
  }
  
  public int hashCode() {
    int i;
    Object object = this.first;
    int j = 0;
    if (object == null) {
      i = 0;
    } else {
      i = object.hashCode();
    } 
    object = this.second;
    if (object != null)
      j = object.hashCode(); 
    return i ^ j;
  }
  
  public String toString() {
    StringBuilder stringBuilder = new StringBuilder();
    stringBuilder.append("Pair{");
    stringBuilder.append(String.valueOf(this.first));
    stringBuilder.append(" ");
    stringBuilder.append(String.valueOf(this.second));
    stringBuilder.append("}");
    return stringBuilder.toString();
  }
}


/* Location:              D:\Apks\New folder\classes-dex2jar.jar!\android\support\v\\util\Pair.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.0.7
 */