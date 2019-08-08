package android.support.v4.util;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

public class LongSparseArray<E> extends Object implements Cloneable {
  private static final Object DELETED = new Object();
  
  private boolean mGarbage = false;
  
  private long[] mKeys;
  
  private int mSize;
  
  private Object[] mValues;
  
  public LongSparseArray() { this(10); }
  
  public LongSparseArray(int paramInt) {
    if (paramInt == 0) {
      this.mKeys = ContainerHelpers.EMPTY_LONGS;
      this.mValues = ContainerHelpers.EMPTY_OBJECTS;
    } else {
      paramInt = ContainerHelpers.idealLongArraySize(paramInt);
      this.mKeys = new long[paramInt];
      this.mValues = new Object[paramInt];
    } 
    this.mSize = 0;
  }
  
  private void gc() {
    int i = this.mSize;
    byte b2 = 0;
    long[] arrayOfLong = this.mKeys;
    Object[] arrayOfObject = this.mValues;
    byte b1 = 0;
    while (b1 < i) {
      Object object = arrayOfObject[b1];
      byte b = b2;
      if (object != DELETED) {
        if (b1 != b2) {
          arrayOfLong[b2] = arrayOfLong[b1];
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
  
  public void append(long paramLong, E paramE) {
    int i = this.mSize;
    if (i != 0 && paramLong <= this.mKeys[i - 1]) {
      put(paramLong, paramE);
      return;
    } 
    if (this.mGarbage && this.mSize >= this.mKeys.length)
      gc(); 
    i = this.mSize;
    if (i >= this.mKeys.length) {
      int j = ContainerHelpers.idealLongArraySize(i + 1);
      long[] arrayOfLong1 = new long[j];
      Object[] arrayOfObject1 = new Object[j];
      long[] arrayOfLong2 = this.mKeys;
      System.arraycopy(arrayOfLong2, 0, arrayOfLong1, 0, arrayOfLong2.length);
      Object[] arrayOfObject2 = this.mValues;
      System.arraycopy(arrayOfObject2, 0, arrayOfObject1, 0, arrayOfObject2.length);
      this.mKeys = arrayOfLong1;
      this.mValues = arrayOfObject1;
    } 
    this.mKeys[i] = paramLong;
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
  
  public LongSparseArray<E> clone() {
    try {
      LongSparseArray longSparseArray = (LongSparseArray)super.clone();
      longSparseArray.mKeys = (long[])this.mKeys.clone();
      longSparseArray.mValues = (Object[])this.mValues.clone();
      return longSparseArray;
    } catch (CloneNotSupportedException cloneNotSupportedException) {
      throw new AssertionError(cloneNotSupportedException);
    } 
  }
  
  public boolean containsKey(long paramLong) { return (indexOfKey(paramLong) >= 0); }
  
  public boolean containsValue(E paramE) { return (indexOfValue(paramE) >= 0); }
  
  public void delete(long paramLong) {
    int i = ContainerHelpers.binarySearch(this.mKeys, this.mSize, paramLong);
    if (i >= 0) {
      Object[] arrayOfObject = this.mValues;
      Object object1 = arrayOfObject[i];
      Object object2 = DELETED;
      if (object1 != object2) {
        arrayOfObject[i] = object2;
        this.mGarbage = true;
      } 
    } 
  }
  
  @Nullable
  public E get(long paramLong) { return (E)get(paramLong, null); }
  
  public E get(long paramLong, E paramE) {
    int i = ContainerHelpers.binarySearch(this.mKeys, this.mSize, paramLong);
    if (i >= 0) {
      Object[] arrayOfObject = this.mValues;
      return (arrayOfObject[i] == DELETED) ? paramE : (E)arrayOfObject[i];
    } 
    return paramE;
  }
  
  public int indexOfKey(long paramLong) {
    if (this.mGarbage)
      gc(); 
    return ContainerHelpers.binarySearch(this.mKeys, this.mSize, paramLong);
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
  
  public long keyAt(int paramInt) {
    if (this.mGarbage)
      gc(); 
    return this.mKeys[paramInt];
  }
  
  public void put(long paramLong, E paramE) {
    int i = ContainerHelpers.binarySearch(this.mKeys, this.mSize, paramLong);
    if (i >= 0) {
      this.mValues[i] = paramE;
      return;
    } 
    int j = i ^ 0xFFFFFFFF;
    if (j < this.mSize) {
      Object[] arrayOfObject = this.mValues;
      if (arrayOfObject[j] == DELETED) {
        this.mKeys[j] = paramLong;
        arrayOfObject[j] = paramE;
        return;
      } 
    } 
    i = j;
    if (this.mGarbage) {
      i = j;
      if (this.mSize >= this.mKeys.length) {
        gc();
        i = ContainerHelpers.binarySearch(this.mKeys, this.mSize, paramLong) ^ 0xFFFFFFFF;
      } 
    } 
    j = this.mSize;
    if (j >= this.mKeys.length) {
      j = ContainerHelpers.idealLongArraySize(j + 1);
      long[] arrayOfLong1 = new long[j];
      Object[] arrayOfObject1 = new Object[j];
      long[] arrayOfLong2 = this.mKeys;
      System.arraycopy(arrayOfLong2, 0, arrayOfLong1, 0, arrayOfLong2.length);
      Object[] arrayOfObject2 = this.mValues;
      System.arraycopy(arrayOfObject2, 0, arrayOfObject1, 0, arrayOfObject2.length);
      this.mKeys = arrayOfLong1;
      this.mValues = arrayOfObject1;
    } 
    j = this.mSize;
    if (j - i != 0) {
      long[] arrayOfLong = this.mKeys;
      System.arraycopy(arrayOfLong, i, arrayOfLong, i + 1, j - i);
      Object[] arrayOfObject = this.mValues;
      System.arraycopy(arrayOfObject, i, arrayOfObject, i + 1, this.mSize - i);
    } 
    this.mKeys[i] = paramLong;
    this.mValues[i] = paramE;
    this.mSize++;
  }
  
  public void putAll(@NonNull LongSparseArray<? extends E> paramLongSparseArray) {
    byte b = 0;
    int i = paramLongSparseArray.size();
    while (b < i) {
      put(paramLongSparseArray.keyAt(b), paramLongSparseArray.valueAt(b));
      b++;
    } 
  }
  
  public void remove(long paramLong) { delete(paramLong); }
  
  public void removeAt(int paramInt) {
    Object[] arrayOfObject = this.mValues;
    Object object1 = arrayOfObject[paramInt];
    Object object2 = DELETED;
    if (object1 != object2) {
      arrayOfObject[paramInt] = object2;
      this.mGarbage = true;
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


/* Location:              D:\Apks\New folder\classes-dex2jar.jar!\android\support\v\\util\LongSparseArray.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.0.7
 */