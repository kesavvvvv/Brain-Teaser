package android.support.v4.util;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RestrictTo;
import java.lang.reflect.Array;
import java.util.Collection;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

public final class ArraySet<E> extends Object implements Collection<E>, Set<E> {
  private static final int BASE_SIZE = 4;
  
  private static final int CACHE_SIZE = 10;
  
  private static final boolean DEBUG = false;
  
  private static final int[] INT = new int[0];
  
  private static final Object[] OBJECT = new Object[0];
  
  private static final String TAG = "ArraySet";
  
  @Nullable
  private static Object[] sBaseCache;
  
  private static int sBaseCacheSize;
  
  @Nullable
  private static Object[] sTwiceBaseCache;
  
  private static int sTwiceBaseCacheSize;
  
  Object[] mArray;
  
  private MapCollections<E, E> mCollections;
  
  private int[] mHashes;
  
  int mSize;
  
  public ArraySet() { this(0); }
  
  public ArraySet(int paramInt) {
    if (paramInt == 0) {
      this.mHashes = INT;
      this.mArray = OBJECT;
    } else {
      allocArrays(paramInt);
    } 
    this.mSize = 0;
  }
  
  public ArraySet(@Nullable ArraySet<E> paramArraySet) {
    this();
    if (paramArraySet != null)
      addAll(paramArraySet); 
  }
  
  public ArraySet(@Nullable Collection<E> paramCollection) {
    this();
    if (paramCollection != null)
      addAll(paramCollection); 
  }
  
  private void allocArrays(int paramInt) { // Byte code:
    //   0: iload_1
    //   1: bipush #8
    //   3: if_icmpne -> 81
    //   6: ldc android/support/v4/util/ArraySet
    //   8: monitorenter
    //   9: getstatic android/support/v4/util/ArraySet.sTwiceBaseCache : [Ljava/lang/Object;
    //   12: ifnull -> 69
    //   15: getstatic android/support/v4/util/ArraySet.sTwiceBaseCache : [Ljava/lang/Object;
    //   18: astore_2
    //   19: aload_0
    //   20: aload_2
    //   21: putfield mArray : [Ljava/lang/Object;
    //   24: aload_2
    //   25: iconst_0
    //   26: aaload
    //   27: checkcast [Ljava/lang/Object;
    //   30: checkcast [Ljava/lang/Object;
    //   33: putstatic android/support/v4/util/ArraySet.sTwiceBaseCache : [Ljava/lang/Object;
    //   36: aload_0
    //   37: aload_2
    //   38: iconst_1
    //   39: aaload
    //   40: checkcast [I
    //   43: checkcast [I
    //   46: putfield mHashes : [I
    //   49: aload_2
    //   50: iconst_1
    //   51: aconst_null
    //   52: aastore
    //   53: aload_2
    //   54: iconst_0
    //   55: aconst_null
    //   56: aastore
    //   57: getstatic android/support/v4/util/ArraySet.sTwiceBaseCacheSize : I
    //   60: iconst_1
    //   61: isub
    //   62: putstatic android/support/v4/util/ArraySet.sTwiceBaseCacheSize : I
    //   65: ldc android/support/v4/util/ArraySet
    //   67: monitorexit
    //   68: return
    //   69: ldc android/support/v4/util/ArraySet
    //   71: monitorexit
    //   72: goto -> 161
    //   75: astore_2
    //   76: ldc android/support/v4/util/ArraySet
    //   78: monitorexit
    //   79: aload_2
    //   80: athrow
    //   81: iload_1
    //   82: iconst_4
    //   83: if_icmpne -> 161
    //   86: ldc android/support/v4/util/ArraySet
    //   88: monitorenter
    //   89: getstatic android/support/v4/util/ArraySet.sBaseCache : [Ljava/lang/Object;
    //   92: ifnull -> 149
    //   95: getstatic android/support/v4/util/ArraySet.sBaseCache : [Ljava/lang/Object;
    //   98: astore_2
    //   99: aload_0
    //   100: aload_2
    //   101: putfield mArray : [Ljava/lang/Object;
    //   104: aload_2
    //   105: iconst_0
    //   106: aaload
    //   107: checkcast [Ljava/lang/Object;
    //   110: checkcast [Ljava/lang/Object;
    //   113: putstatic android/support/v4/util/ArraySet.sBaseCache : [Ljava/lang/Object;
    //   116: aload_0
    //   117: aload_2
    //   118: iconst_1
    //   119: aaload
    //   120: checkcast [I
    //   123: checkcast [I
    //   126: putfield mHashes : [I
    //   129: aload_2
    //   130: iconst_1
    //   131: aconst_null
    //   132: aastore
    //   133: aload_2
    //   134: iconst_0
    //   135: aconst_null
    //   136: aastore
    //   137: getstatic android/support/v4/util/ArraySet.sBaseCacheSize : I
    //   140: iconst_1
    //   141: isub
    //   142: putstatic android/support/v4/util/ArraySet.sBaseCacheSize : I
    //   145: ldc android/support/v4/util/ArraySet
    //   147: monitorexit
    //   148: return
    //   149: ldc android/support/v4/util/ArraySet
    //   151: monitorexit
    //   152: goto -> 161
    //   155: astore_2
    //   156: ldc android/support/v4/util/ArraySet
    //   158: monitorexit
    //   159: aload_2
    //   160: athrow
    //   161: aload_0
    //   162: iload_1
    //   163: newarray int
    //   165: putfield mHashes : [I
    //   168: aload_0
    //   169: iload_1
    //   170: anewarray java/lang/Object
    //   173: putfield mArray : [Ljava/lang/Object;
    //   176: return
    // Exception table:
    //   from	to	target	type
    //   9	49	75	finally
    //   57	68	75	finally
    //   69	72	75	finally
    //   76	79	75	finally
    //   89	129	155	finally
    //   137	148	155	finally
    //   149	152	155	finally
    //   156	159	155	finally }
  
  private static void freeArrays(int[] paramArrayOfInt, Object[] paramArrayOfObject, int paramInt) { // Byte code:
    //   0: aload_0
    //   1: arraylength
    //   2: bipush #8
    //   4: if_icmpne -> 57
    //   7: ldc android/support/v4/util/ArraySet
    //   9: monitorenter
    //   10: getstatic android/support/v4/util/ArraySet.sTwiceBaseCacheSize : I
    //   13: bipush #10
    //   15: if_icmpge -> 47
    //   18: aload_1
    //   19: iconst_0
    //   20: getstatic android/support/v4/util/ArraySet.sTwiceBaseCache : [Ljava/lang/Object;
    //   23: aastore
    //   24: aload_1
    //   25: iconst_1
    //   26: aload_0
    //   27: aastore
    //   28: iload_2
    //   29: iconst_1
    //   30: isub
    //   31: istore_2
    //   32: goto -> 114
    //   35: aload_1
    //   36: putstatic android/support/v4/util/ArraySet.sTwiceBaseCache : [Ljava/lang/Object;
    //   39: getstatic android/support/v4/util/ArraySet.sTwiceBaseCacheSize : I
    //   42: iconst_1
    //   43: iadd
    //   44: putstatic android/support/v4/util/ArraySet.sTwiceBaseCacheSize : I
    //   47: ldc android/support/v4/util/ArraySet
    //   49: monitorexit
    //   50: return
    //   51: astore_0
    //   52: ldc android/support/v4/util/ArraySet
    //   54: monitorexit
    //   55: aload_0
    //   56: athrow
    //   57: aload_0
    //   58: arraylength
    //   59: iconst_4
    //   60: if_icmpne -> 113
    //   63: ldc android/support/v4/util/ArraySet
    //   65: monitorenter
    //   66: getstatic android/support/v4/util/ArraySet.sBaseCacheSize : I
    //   69: bipush #10
    //   71: if_icmpge -> 103
    //   74: aload_1
    //   75: iconst_0
    //   76: getstatic android/support/v4/util/ArraySet.sBaseCache : [Ljava/lang/Object;
    //   79: aastore
    //   80: aload_1
    //   81: iconst_1
    //   82: aload_0
    //   83: aastore
    //   84: iload_2
    //   85: iconst_1
    //   86: isub
    //   87: istore_2
    //   88: goto -> 130
    //   91: aload_1
    //   92: putstatic android/support/v4/util/ArraySet.sBaseCache : [Ljava/lang/Object;
    //   95: getstatic android/support/v4/util/ArraySet.sBaseCacheSize : I
    //   98: iconst_1
    //   99: iadd
    //   100: putstatic android/support/v4/util/ArraySet.sBaseCacheSize : I
    //   103: ldc android/support/v4/util/ArraySet
    //   105: monitorexit
    //   106: return
    //   107: astore_0
    //   108: ldc android/support/v4/util/ArraySet
    //   110: monitorexit
    //   111: aload_0
    //   112: athrow
    //   113: return
    //   114: iload_2
    //   115: iconst_2
    //   116: if_icmplt -> 35
    //   119: aload_1
    //   120: iload_2
    //   121: aconst_null
    //   122: aastore
    //   123: iload_2
    //   124: iconst_1
    //   125: isub
    //   126: istore_2
    //   127: goto -> 114
    //   130: iload_2
    //   131: iconst_2
    //   132: if_icmplt -> 91
    //   135: aload_1
    //   136: iload_2
    //   137: aconst_null
    //   138: aastore
    //   139: iload_2
    //   140: iconst_1
    //   141: isub
    //   142: istore_2
    //   143: goto -> 130
    // Exception table:
    //   from	to	target	type
    //   10	24	51	finally
    //   35	47	51	finally
    //   47	50	51	finally
    //   52	55	51	finally
    //   66	80	107	finally
    //   91	103	107	finally
    //   103	106	107	finally
    //   108	111	107	finally }
  
  private MapCollections<E, E> getCollection() {
    if (this.mCollections == null)
      this.mCollections = new MapCollections<E, E>() {
          protected void colClear() { ArraySet.this.clear(); }
          
          protected Object colGetEntry(int param1Int1, int param1Int2) { return ArraySet.this.mArray[param1Int1]; }
          
          protected Map<E, E> colGetMap() { throw new UnsupportedOperationException("not a map"); }
          
          protected int colGetSize() { return ArraySet.this.mSize; }
          
          protected int colIndexOfKey(Object param1Object) { return ArraySet.this.indexOf(param1Object); }
          
          protected int colIndexOfValue(Object param1Object) { return ArraySet.this.indexOf(param1Object); }
          
          protected void colPut(E param1E1, E param1E2) { ArraySet.this.add(param1E1); }
          
          protected void colRemoveAt(int param1Int) { ArraySet.this.removeAt(param1Int); }
          
          protected E colSetValue(int param1Int, E param1E) { throw new UnsupportedOperationException("not a map"); }
        }; 
    return this.mCollections;
  }
  
  private int indexOf(Object paramObject, int paramInt) {
    int j = this.mSize;
    if (j == 0)
      return -1; 
    int k = ContainerHelpers.binarySearch(this.mHashes, j, paramInt);
    if (k < 0)
      return k; 
    if (paramObject.equals(this.mArray[k]))
      return k; 
    int i;
    for (i = k + 1; i < j && this.mHashes[i] == paramInt; i++) {
      if (paramObject.equals(this.mArray[i]))
        return i; 
    } 
    for (j = k - 1; j >= 0 && this.mHashes[j] == paramInt; j--) {
      if (paramObject.equals(this.mArray[j]))
        return j; 
    } 
    return i ^ 0xFFFFFFFF;
  }
  
  private int indexOfNull() {
    int j = this.mSize;
    if (j == 0)
      return -1; 
    int k = ContainerHelpers.binarySearch(this.mHashes, j, 0);
    if (k < 0)
      return k; 
    if (this.mArray[k] == null)
      return k; 
    int i;
    for (i = k + 1; i < j && this.mHashes[i] == 0; i++) {
      if (this.mArray[i] == null)
        return i; 
    } 
    for (j = k - 1; j >= 0 && this.mHashes[j] == 0; j--) {
      if (this.mArray[j] == null)
        return j; 
    } 
    return i ^ 0xFFFFFFFF;
  }
  
  public boolean add(@Nullable E paramE) {
    int j;
    if (paramE == null) {
      j = 0;
      i = indexOfNull();
    } else {
      j = paramE.hashCode();
      i = indexOf(paramE, j);
    } 
    if (i >= 0)
      return false; 
    int k = i ^ 0xFFFFFFFF;
    int m = this.mSize;
    if (m >= this.mHashes.length) {
      i = 4;
      if (m >= 8) {
        i = (m >> 1) + m;
      } else if (m >= 4) {
        i = 8;
      } 
      int[] arrayOfInt1 = this.mHashes;
      Object[] arrayOfObject = this.mArray;
      allocArrays(i);
      int[] arrayOfInt2 = this.mHashes;
      if (arrayOfInt2.length > 0) {
        System.arraycopy(arrayOfInt1, 0, arrayOfInt2, 0, arrayOfInt1.length);
        System.arraycopy(arrayOfObject, 0, this.mArray, 0, arrayOfObject.length);
      } 
      freeArrays(arrayOfInt1, arrayOfObject, this.mSize);
    } 
    int i = this.mSize;
    if (k < i) {
      int[] arrayOfInt = this.mHashes;
      System.arraycopy(arrayOfInt, k, arrayOfInt, k + 1, i - k);
      Object[] arrayOfObject = this.mArray;
      System.arraycopy(arrayOfObject, k, arrayOfObject, k + 1, this.mSize - k);
    } 
    this.mHashes[k] = j;
    this.mArray[k] = paramE;
    this.mSize++;
    return true;
  }
  
  public void addAll(@NonNull ArraySet<? extends E> paramArraySet) {
    int i = paramArraySet.mSize;
    ensureCapacity(this.mSize + i);
    if (this.mSize == 0) {
      if (i > 0) {
        System.arraycopy(paramArraySet.mHashes, 0, this.mHashes, 0, i);
        System.arraycopy(paramArraySet.mArray, 0, this.mArray, 0, i);
        this.mSize = i;
        return;
      } 
    } else {
      for (byte b = 0; b < i; b++)
        add(paramArraySet.valueAt(b)); 
    } 
  }
  
  public boolean addAll(@NonNull Collection<? extends E> paramCollection) {
    ensureCapacity(this.mSize + paramCollection.size());
    boolean bool = false;
    Iterator iterator = paramCollection.iterator();
    while (iterator.hasNext())
      bool |= add(iterator.next()); 
    return bool;
  }
  
  @RestrictTo({RestrictTo.Scope.LIBRARY_GROUP})
  public void append(E paramE) {
    int i;
    int j = this.mSize;
    if (paramE == null) {
      i = 0;
    } else {
      i = paramE.hashCode();
    } 
    int[] arrayOfInt = this.mHashes;
    if (j < arrayOfInt.length) {
      if (j > 0 && arrayOfInt[j - 1] > i) {
        add(paramE);
        return;
      } 
      this.mSize = j + 1;
      this.mHashes[j] = i;
      this.mArray[j] = paramE;
      return;
    } 
    throw new IllegalStateException("Array is full");
  }
  
  public void clear() {
    int i = this.mSize;
    if (i != 0) {
      freeArrays(this.mHashes, this.mArray, i);
      this.mHashes = INT;
      this.mArray = OBJECT;
      this.mSize = 0;
    } 
  }
  
  public boolean contains(@Nullable Object paramObject) { return (indexOf(paramObject) >= 0); }
  
  public boolean containsAll(@NonNull Collection<?> paramCollection) {
    Iterator iterator = paramCollection.iterator();
    while (iterator.hasNext()) {
      if (!contains(iterator.next()))
        return false; 
    } 
    return true;
  }
  
  public void ensureCapacity(int paramInt) {
    if (this.mHashes.length < paramInt) {
      int[] arrayOfInt = this.mHashes;
      Object[] arrayOfObject = this.mArray;
      allocArrays(paramInt);
      paramInt = this.mSize;
      if (paramInt > 0) {
        System.arraycopy(arrayOfInt, 0, this.mHashes, 0, paramInt);
        System.arraycopy(arrayOfObject, 0, this.mArray, 0, this.mSize);
      } 
      freeArrays(arrayOfInt, arrayOfObject, this.mSize);
    } 
  }
  
  public boolean equals(Object paramObject) {
    if (this == paramObject)
      return true; 
    if (paramObject instanceof Set) {
      paramObject = (Set)paramObject;
      if (size() != paramObject.size())
        return false; 
      byte b = 0;
      try {
        while (b < this.mSize) {
          boolean bool = paramObject.contains(valueAt(b));
          if (!bool)
            return false; 
          b++;
        } 
        return true;
      } catch (NullPointerException paramObject) {
        return false;
      } catch (ClassCastException paramObject) {
        return false;
      } 
    } 
    return false;
  }
  
  public int hashCode() {
    int[] arrayOfInt = this.mHashes;
    int i = 0;
    byte b = 0;
    int j = this.mSize;
    while (b < j) {
      i += arrayOfInt[b];
      b++;
    } 
    return i;
  }
  
  public int indexOf(@Nullable Object paramObject) { return (paramObject == null) ? indexOfNull() : indexOf(paramObject, paramObject.hashCode()); }
  
  public boolean isEmpty() { return (this.mSize <= 0); }
  
  public Iterator<E> iterator() { return getCollection().getKeySet().iterator(); }
  
  public boolean remove(@Nullable Object paramObject) {
    int i = indexOf(paramObject);
    if (i >= 0) {
      removeAt(i);
      return true;
    } 
    return false;
  }
  
  public boolean removeAll(@NonNull ArraySet<? extends E> paramArraySet) {
    int i = paramArraySet.mSize;
    int j = this.mSize;
    for (byte b = 0; b < i; b++)
      remove(paramArraySet.valueAt(b)); 
    return (j != this.mSize);
  }
  
  public boolean removeAll(@NonNull Collection<?> paramCollection) {
    boolean bool = false;
    Iterator iterator = paramCollection.iterator();
    while (iterator.hasNext())
      bool |= remove(iterator.next()); 
    return bool;
  }
  
  public E removeAt(int paramInt) {
    Object[] arrayOfObject = this.mArray;
    Object object = arrayOfObject[paramInt];
    int j = this.mSize;
    if (j <= 1) {
      freeArrays(this.mHashes, arrayOfObject, j);
      this.mHashes = INT;
      this.mArray = OBJECT;
      this.mSize = 0;
      return (E)object;
    } 
    int[] arrayOfInt = this.mHashes;
    int k = arrayOfInt.length;
    int i = 8;
    if (k > 8 && j < arrayOfInt.length / 3) {
      if (j > 8)
        i = j + (j >> 1); 
      arrayOfInt = this.mHashes;
      Object[] arrayOfObject1 = this.mArray;
      allocArrays(i);
      this.mSize--;
      if (paramInt > 0) {
        System.arraycopy(arrayOfInt, 0, this.mHashes, 0, paramInt);
        System.arraycopy(arrayOfObject1, 0, this.mArray, 0, paramInt);
      } 
      i = this.mSize;
      if (paramInt < i) {
        System.arraycopy(arrayOfInt, paramInt + 1, this.mHashes, paramInt, i - paramInt);
        System.arraycopy(arrayOfObject1, paramInt + 1, this.mArray, paramInt, this.mSize - paramInt);
      } 
      return (E)object;
    } 
    i = --this.mSize;
    if (paramInt < i) {
      arrayOfInt = this.mHashes;
      System.arraycopy(arrayOfInt, paramInt + 1, arrayOfInt, paramInt, i - paramInt);
      Object[] arrayOfObject1 = this.mArray;
      System.arraycopy(arrayOfObject1, paramInt + 1, arrayOfObject1, paramInt, this.mSize - paramInt);
    } 
    this.mArray[this.mSize] = null;
    return (E)object;
  }
  
  public boolean retainAll(@NonNull Collection<?> paramCollection) {
    boolean bool = false;
    for (int i = this.mSize - 1; i >= 0; i--) {
      if (!paramCollection.contains(this.mArray[i])) {
        removeAt(i);
        bool = true;
      } 
    } 
    return bool;
  }
  
  public int size() { return this.mSize; }
  
  @NonNull
  public Object[] toArray() {
    int i = this.mSize;
    Object[] arrayOfObject = new Object[i];
    System.arraycopy(this.mArray, 0, arrayOfObject, 0, i);
    return arrayOfObject;
  }
  
  @NonNull
  public <T> T[] toArray(@NonNull T[] paramArrayOfT) {
    T[] arrayOfT = paramArrayOfT;
    if (paramArrayOfT.length < this.mSize)
      arrayOfT = (T[])(Object[])Array.newInstance(paramArrayOfT.getClass().getComponentType(), this.mSize); 
    System.arraycopy(this.mArray, 0, arrayOfT, 0, this.mSize);
    int i = arrayOfT.length;
    int j = this.mSize;
    if (i > j)
      arrayOfT[j] = null; 
    return arrayOfT;
  }
  
  public String toString() {
    if (isEmpty())
      return "{}"; 
    StringBuilder stringBuilder = new StringBuilder(this.mSize * 14);
    stringBuilder.append('{');
    for (byte b = 0; b < this.mSize; b++) {
      if (b)
        stringBuilder.append(", "); 
      Object object = valueAt(b);
      if (object != this) {
        stringBuilder.append(object);
      } else {
        stringBuilder.append("(this Set)");
      } 
    } 
    stringBuilder.append('}');
    return stringBuilder.toString();
  }
  
  @Nullable
  public E valueAt(int paramInt) { return (E)this.mArray[paramInt]; }
}


/* Location:              D:\Apks\New folder\classes-dex2jar.jar!\android\support\v\\util\ArraySet.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.0.7
 */