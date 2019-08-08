package android.support.v4.util;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

public class SparseArrayCompat<E> extends Object implements Cloneable {
  private static final Object DELETED = new Object();
  
  private boolean mGarbage = false;
  
  private int[] mKeys;
  
  private int mSize;
  
  private Object[] mValues;
  
  public SparseArrayCompat() { this(10); }
  
  public SparseArrayCompat(int paramInt) {
    if (paramInt == 0) {
      this.mKeys = ContainerHelpers.EMPTY_INTS;
      this.mValues = ContainerHelpers.EMPTY_OBJECTS;
    } else {
      paramInt = ContainerHelpers.idealIntArraySize(paramInt);
      this.mKeys = new int[paramInt];
      this.mValues = new Object[paramInt];
    } 
    this.mSize = 0;
  }
  
  private void gc() {
    int i = this.mSize;
    byte b2 = 0;
    int[] arrayOfInt = this.mKeys;
    Object[] arrayOfObject = this.mValues;
    byte b1 = 0;
    while (b1 < i) {
      Object object = arrayOfObject[b1];
      byte b = b2;
      if (object != DELETED) {
        if (b1 != b2) {
          arrayOfInt[b2] = arrayOfInt[b1];
          arrayOfObject[b2] = object;
          arrayOfObject[b1] = null;
        } 
        b = b2 + true;
      } 
      b1++;
      b2 = b;
    } 
    this.mGarbage = false;
    this.mSize = b2;
  }
  
  public void append(int paramInt, E paramE) {
    int i = this.mSize;
    if (i != 0 && paramInt <= this.mKeys[i - 1]) {
      put(paramInt, paramE);
      return;
    } 
    if (this.mGarbage && this.mSize >= this.mKeys.length)
      gc(); 
    i = this.mSize;
    if (i >= this.mKeys.length) {
      int j = ContainerHelpers.idealIntArraySize(i + 1);
      int[] arrayOfInt1 = new int[j];
      Object[] arrayOfObject1 = new Object[j];
      int[] arrayOfInt2 = this.mKeys;
      System.arraycopy(arrayOfInt2, 0, arrayOfInt1, 0, arrayOfInt2.length);
      Object[] arrayOfObject2 = this.mValues;
      System.arraycopy(arrayOfObject2, 0, arrayOfObject1, 0, arrayOfObject2.length);
      this.mKeys = arrayOfInt1;
      this.mValues = arrayOfObject1;
    } 
    this.mKeys[i] = paramInt;
    this.mValues[i] = paramE;
    this.mSize = i + 1;
  }
  
  public void clear() {
    int i = this.mSize;
    Object[] arrayOfObject = this.mValues;
    for (byte b = 0; b < i; b++)
      arrayOfObject[b] = null; 
    this.mSize = 0;
    this.mGarbage = false;
  }
  
  public SparseArrayCompat<E> clone() {
    try {
      SparseArrayCompat sparseArrayCompat = (SparseArrayCompat)super.clone();
      sparseArrayCompat.mKeys = (int[])this.mKeys.clone();
      sparseArrayCompat.mValues = (Object[])this.mValues.clone();
      return sparseArrayCompat;
    } catch (CloneNotSupportedException cloneNotSupportedException) {
      throw new AssertionError(cloneNotSupportedException);
    } 
  }
  
  public boolean containsKey(int paramInt) { return (indexOfKey(paramInt) >= 0); }
  
  public boolean containsValue(E paramE) { return (indexOfValue(paramE) >= 0); }
  
  public void delete(int paramInt) {
    paramInt = ContainerHelpers.binarySearch(this.mKeys, this.mSize, paramInt);
    if (paramInt >= 0) {
      Object[] arrayOfObject = this.mValues;
      Object object1 = arrayOfObject[paramInt];
      Object object2 = DELETED;
      if (object1 != object2) {
        arrayOfObject[paramInt] = object2;
        this.mGarbage = true;
      } 
    } 
  }
  
  @Nullable
  public E get(int paramInt) { return (E)get(paramInt, null); }
  
  public E get(int paramInt, E paramE) {
    paramInt = ContainerHelpers.binarySearch(this.mKeys, this.mSize, paramInt);
    if (paramInt >= 0) {
      Object[] arrayOfObject = this.mValues;
      return (arrayOfObject[paramInt] == DELETED) ? paramE : (E)arrayOfObject[paramInt];
    } 
    return paramE;
  }
  
  public int indexOfKey(int paramInt) {
    if (this.mGarbage)
      gc(); 
    return ContainerHelpers.binarySearch(this.mKeys, this.mSize, paramInt);
  }
  
  public int indexOfValue(E paramE) {
    if (this.mGarbage)
      gc(); 
    for (byte b = 0; b < this.mSize; b++) {
      if (this.mValues[b] == paramE)
        return b; 
    } 
    return -1;
  }
  
  public boolean isEmpty() { return (size() == 0); }
  
  public int keyAt(int paramInt) {
    if (this.mGarbage)
      gc(); 
    return this.mKeys[paramInt];
  }
  
  public void put(int paramInt, E paramE) {
    int i = ContainerHelpers.binarySearch(this.mKeys, this.mSize, paramInt);
    if (i >= 0) {
      this.mValues[i] = paramE;
      return;
    } 
    int j = i ^ 0xFFFFFFFF;
    if (j < this.mSize) {
      Object[] arrayOfObject = this.mValues;
      if (arrayOfObject[j] == DELETED) {
        this.mKeys[j] = paramInt;
        arrayOfObject[j] = paramE;
        return;
      } 
    } 
    i = j;
    if (this.mGarbage) {
      i = j;
      if (this.mSize >= this.mKeys.length) {
        gc();
        i = ContainerHelpers.binarySearch(this.mKeys, this.mSize, paramInt) ^ 0xFFFFFFFF;
      } 
    } 
    j = this.mSize;
    if (j >= this.mKeys.length) {
      j = ContainerHelpers.idealIntArraySize(j + 1);
      int[] arrayOfInt1 = new int[j];
      Object[] arrayOfObject1 = new Object[j];
      int[] arrayOfInt2 = this.mKeys;
      System.arraycopy(arrayOfInt2, 0, arrayOfInt1, 0, arrayOfInt2.length);
      Object[] arrayOfObject2 = this.mValues;
      System.arraycopy(arrayOfObject2, 0, arrayOfObject1, 0, arrayOfObject2.length);
      this.mKeys = arrayOfInt1;
      this.mValues = arrayOfObject1;
    } 
    j = this.mSize;
    if (j - i != 0) {
      int[] arrayOfInt = this.mKeys;
      System.arraycopy(arrayOfInt, i, arrayOfInt, i + 1, j - i);
      Object[] arrayOfObject = this.mValues;
      System.arraycopy(arrayOfObject, i, arrayOfObject, i + 1, this.mSize - i);
    } 
    this.mKeys[i] = paramInt;
    this.mValues[i] = paramE;
    this.mSize++;
  }
  
  public void putAll(@NonNull SparseArrayCompat<? extends E> paramSparseArrayCompat) {
    byte b = 0;
    int i = paramSparseArrayCompat.size();
    while (b < i) {
      put(paramSparseArrayCompat.keyAt(b), paramSparseArrayCompat.valueAt(b));
      b++;
    } 
  }
  
  public void remove(int paramInt) { delete(paramInt); }
  
  public void removeAt(int paramInt) {
    Object[] arrayOfObject = this.mValues;
    Object object1 = arrayOfObject[paramInt];
    Object object2 = DELETED;
    if (object1 != object2) {
      arrayOfObject[paramInt] = object2;
      this.mGarbage = true;
    } 
  }
  
  public void removeAtRange(int paramInt1, int paramInt2) {
    paramInt2 = Math.min(this.mSize, paramInt1 + paramInt2);
    while (paramInt1 < paramInt2) {
      removeAt(paramInt1);
      paramInt1++;
    } 
  }
  
  public void setValueAt(int paramInt, E paramE) {
    if (this.mGarbage)
      gc(); 
    this.mValues[paramInt] = paramE;
  }
  
  public int size() {
    if (this.mGarbage)
      gc(); 
    return this.mSize;
  }
  
  public String toString() {
    if (size() <= 0)
      return "{}"; 
    StringBuilder stringBuilder = new StringBuilder(this.mSize * 28);
    stringBuilder.append('{');
    for (byte b = 0; b < this.mSize; b++) {
      if (b)
        stringBuilder.append(", "); 
      stringBuilder.append(keyAt(b));
      stringBuilder.append('=');
      Object object = valueAt(b);
      if (object != this) {
        stringBuilder.append(object);
      } else {
        stringBuilder.append("(this Map)");
      } 
    } 
    stringBuilder.append('}');
    return stringBuilder.toString();
  }
  
  public E valueAt(int paramInt) {
    if (this.mGarbage)
      gc(); 
    return (E)this.mValues[paramInt];
  }
}


/* Location:              D:\Apks\New folder\classes-dex2jar.jar!\android\support\v\\util\SparseArrayCompat.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.0.7
 */