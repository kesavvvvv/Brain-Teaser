package android.support.v4.util;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import java.util.ConcurrentModificationException;
import java.util.Map;

public class SimpleArrayMap<K, V> extends Object {
  private static final int BASE_SIZE = 4;
  
  private static final int CACHE_SIZE = 10;
  
  private static final boolean CONCURRENT_MODIFICATION_EXCEPTIONS = true;
  
  private static final boolean DEBUG = false;
  
  private static final String TAG = "ArrayMap";
  
  @Nullable
  static Object[] mBaseCache;
  
  static int mBaseCacheSize;
  
  @Nullable
  static Object[] mTwiceBaseCache;
  
  static int mTwiceBaseCacheSize;
  
  Object[] mArray;
  
  int[] mHashes;
  
  int mSize;
  
  public SimpleArrayMap() {
    this.mHashes = ContainerHelpers.EMPTY_INTS;
    this.mArray = ContainerHelpers.EMPTY_OBJECTS;
    this.mSize = 0;
  }
  
  public SimpleArrayMap(int paramInt) {
    if (paramInt == 0) {
      this.mHashes = ContainerHelpers.EMPTY_INTS;
      this.mArray = ContainerHelpers.EMPTY_OBJECTS;
    } else {
      allocArrays(paramInt);
    } 
    this.mSize = 0;
  }
  
  public SimpleArrayMap(SimpleArrayMap<K, V> paramSimpleArrayMap) {
    this();
    if (paramSimpleArrayMap != null)
      putAll(paramSimpleArrayMap); 
  }
  
  private void allocArrays(int paramInt) { // Byte code:
    //   0: iload_1
    //   1: bipush #8
    //   3: if_icmpne -> 81
    //   6: ldc android/support/v4/util/ArrayMap
    //   8: monitorenter
    //   9: getstatic android/support/v4/util/SimpleArrayMap.mTwiceBaseCache : [Ljava/lang/Object;
    //   12: ifnull -> 69
    //   15: getstatic android/support/v4/util/SimpleArrayMap.mTwiceBaseCache : [Ljava/lang/Object;
    //   18: astore_2
    //   19: aload_0
    //   20: aload_2
    //   21: putfield mArray : [Ljava/lang/Object;
    //   24: aload_2
    //   25: iconst_0
    //   26: aaload
    //   27: checkcast [Ljava/lang/Object;
    //   30: checkcast [Ljava/lang/Object;
    //   33: putstatic android/support/v4/util/SimpleArrayMap.mTwiceBaseCache : [Ljava/lang/Object;
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
    //   57: getstatic android/support/v4/util/SimpleArrayMap.mTwiceBaseCacheSize : I
    //   60: iconst_1
    //   61: isub
    //   62: putstatic android/support/v4/util/SimpleArrayMap.mTwiceBaseCacheSize : I
    //   65: ldc android/support/v4/util/ArrayMap
    //   67: monitorexit
    //   68: return
    //   69: ldc android/support/v4/util/ArrayMap
    //   71: monitorexit
    //   72: goto -> 161
    //   75: astore_2
    //   76: ldc android/support/v4/util/ArrayMap
    //   78: monitorexit
    //   79: aload_2
    //   80: athrow
    //   81: iload_1
    //   82: iconst_4
    //   83: if_icmpne -> 161
    //   86: ldc android/support/v4/util/ArrayMap
    //   88: monitorenter
    //   89: getstatic android/support/v4/util/SimpleArrayMap.mBaseCache : [Ljava/lang/Object;
    //   92: ifnull -> 149
    //   95: getstatic android/support/v4/util/SimpleArrayMap.mBaseCache : [Ljava/lang/Object;
    //   98: astore_2
    //   99: aload_0
    //   100: aload_2
    //   101: putfield mArray : [Ljava/lang/Object;
    //   104: aload_2
    //   105: iconst_0
    //   106: aaload
    //   107: checkcast [Ljava/lang/Object;
    //   110: checkcast [Ljava/lang/Object;
    //   113: putstatic android/support/v4/util/SimpleArrayMap.mBaseCache : [Ljava/lang/Object;
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
    //   137: getstatic android/support/v4/util/SimpleArrayMap.mBaseCacheSize : I
    //   140: iconst_1
    //   141: isub
    //   142: putstatic android/support/v4/util/SimpleArrayMap.mBaseCacheSize : I
    //   145: ldc android/support/v4/util/ArrayMap
    //   147: monitorexit
    //   148: return
    //   149: ldc android/support/v4/util/ArrayMap
    //   151: monitorexit
    //   152: goto -> 161
    //   155: astore_2
    //   156: ldc android/support/v4/util/ArrayMap
    //   158: monitorexit
    //   159: aload_2
    //   160: athrow
    //   161: aload_0
    //   162: iload_1
    //   163: newarray int
    //   165: putfield mHashes : [I
    //   168: aload_0
    //   169: iload_1
    //   170: iconst_1
    //   171: ishl
    //   172: anewarray java/lang/Object
    //   175: putfield mArray : [Ljava/lang/Object;
    //   178: return
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
  
  private static int binarySearchHashes(int[] paramArrayOfInt, int paramInt1, int paramInt2) {
    try {
      return ContainerHelpers.binarySearch(paramArrayOfInt, paramInt1, paramInt2);
    } catch (ArrayIndexOutOfBoundsException paramArrayOfInt) {
      throw new ConcurrentModificationException();
    } 
  }
  
  private static void freeArrays(int[] paramArrayOfInt, Object[] paramArrayOfObject, int paramInt) { // Byte code:
    //   0: aload_0
    //   1: arraylength
    //   2: bipush #8
    //   4: if_icmpne -> 59
    //   7: ldc android/support/v4/util/ArrayMap
    //   9: monitorenter
    //   10: getstatic android/support/v4/util/SimpleArrayMap.mTwiceBaseCacheSize : I
    //   13: bipush #10
    //   15: if_icmpge -> 49
    //   18: aload_1
    //   19: iconst_0
    //   20: getstatic android/support/v4/util/SimpleArrayMap.mTwiceBaseCache : [Ljava/lang/Object;
    //   23: aastore
    //   24: aload_1
    //   25: iconst_1
    //   26: aload_0
    //   27: aastore
    //   28: iload_2
    //   29: iconst_1
    //   30: ishl
    //   31: iconst_1
    //   32: isub
    //   33: istore_2
    //   34: goto -> 118
    //   37: aload_1
    //   38: putstatic android/support/v4/util/SimpleArrayMap.mTwiceBaseCache : [Ljava/lang/Object;
    //   41: getstatic android/support/v4/util/SimpleArrayMap.mTwiceBaseCacheSize : I
    //   44: iconst_1
    //   45: iadd
    //   46: putstatic android/support/v4/util/SimpleArrayMap.mTwiceBaseCacheSize : I
    //   49: ldc android/support/v4/util/ArrayMap
    //   51: monitorexit
    //   52: return
    //   53: astore_0
    //   54: ldc android/support/v4/util/ArrayMap
    //   56: monitorexit
    //   57: aload_0
    //   58: athrow
    //   59: aload_0
    //   60: arraylength
    //   61: iconst_4
    //   62: if_icmpne -> 117
    //   65: ldc android/support/v4/util/ArrayMap
    //   67: monitorenter
    //   68: getstatic android/support/v4/util/SimpleArrayMap.mBaseCacheSize : I
    //   71: bipush #10
    //   73: if_icmpge -> 107
    //   76: aload_1
    //   77: iconst_0
    //   78: getstatic android/support/v4/util/SimpleArrayMap.mBaseCache : [Ljava/lang/Object;
    //   81: aastore
    //   82: aload_1
    //   83: iconst_1
    //   84: aload_0
    //   85: aastore
    //   86: iload_2
    //   87: iconst_1
    //   88: ishl
    //   89: iconst_1
    //   90: isub
    //   91: istore_2
    //   92: goto -> 134
    //   95: aload_1
    //   96: putstatic android/support/v4/util/SimpleArrayMap.mBaseCache : [Ljava/lang/Object;
    //   99: getstatic android/support/v4/util/SimpleArrayMap.mBaseCacheSize : I
    //   102: iconst_1
    //   103: iadd
    //   104: putstatic android/support/v4/util/SimpleArrayMap.mBaseCacheSize : I
    //   107: ldc android/support/v4/util/ArrayMap
    //   109: monitorexit
    //   110: return
    //   111: astore_0
    //   112: ldc android/support/v4/util/ArrayMap
    //   114: monitorexit
    //   115: aload_0
    //   116: athrow
    //   117: return
    //   118: iload_2
    //   119: iconst_2
    //   120: if_icmplt -> 37
    //   123: aload_1
    //   124: iload_2
    //   125: aconst_null
    //   126: aastore
    //   127: iload_2
    //   128: iconst_1
    //   129: isub
    //   130: istore_2
    //   131: goto -> 118
    //   134: iload_2
    //   135: iconst_2
    //   136: if_icmplt -> 95
    //   139: aload_1
    //   140: iload_2
    //   141: aconst_null
    //   142: aastore
    //   143: iload_2
    //   144: iconst_1
    //   145: isub
    //   146: istore_2
    //   147: goto -> 134
    // Exception table:
    //   from	to	target	type
    //   10	24	53	finally
    //   37	49	53	finally
    //   49	52	53	finally
    //   54	57	53	finally
    //   68	82	111	finally
    //   95	107	111	finally
    //   107	110	111	finally
    //   112	115	111	finally }
  
  public void clear() {
    if (this.mSize > 0) {
      int[] arrayOfInt = this.mHashes;
      Object[] arrayOfObject = this.mArray;
      int i = this.mSize;
      this.mHashes = ContainerHelpers.EMPTY_INTS;
      this.mArray = ContainerHelpers.EMPTY_OBJECTS;
      this.mSize = 0;
      freeArrays(arrayOfInt, arrayOfObject, i);
    } 
    if (this.mSize <= 0)
      return; 
    throw new ConcurrentModificationException();
  }
  
  public boolean containsKey(@Nullable Object paramObject) { return (indexOfKey(paramObject) >= 0); }
  
  public boolean containsValue(Object paramObject) { return (indexOfValue(paramObject) >= 0); }
  
  public void ensureCapacity(int paramInt) {
    int i = this.mSize;
    if (this.mHashes.length < paramInt) {
      int[] arrayOfInt = this.mHashes;
      Object[] arrayOfObject = this.mArray;
      allocArrays(paramInt);
      if (this.mSize > 0) {
        System.arraycopy(arrayOfInt, 0, this.mHashes, 0, i);
        System.arraycopy(arrayOfObject, 0, this.mArray, 0, i << 1);
      } 
      freeArrays(arrayOfInt, arrayOfObject, i);
    } 
    if (this.mSize == i)
      return; 
    throw new ConcurrentModificationException();
  }
  
  public boolean equals(Object paramObject) {
    if (this == paramObject)
      return true; 
    if (paramObject instanceof SimpleArrayMap) {
      paramObject = (SimpleArrayMap)paramObject;
      if (size() != paramObject.size())
        return false; 
      byte b = 0;
      try {
        while (b < this.mSize) {
          Object object1 = keyAt(b);
          Object object2 = valueAt(b);
          Object object3 = paramObject.get(object1);
          if (object2 == null) {
            if (object3 == null) {
              if (!paramObject.containsKey(object1))
                return false; 
            } else {
              return false;
            } 
          } else {
            boolean bool = object2.equals(object3);
            if (!bool)
              return false; 
          } 
          b++;
        } 
        return true;
      } catch (NullPointerException paramObject) {
        return false;
      } catch (ClassCastException paramObject) {
        return false;
      } 
    } 
    if (paramObject instanceof Map) {
      paramObject = (Map)paramObject;
      if (size() != paramObject.size())
        return false; 
      byte b = 0;
      try {
        while (b < this.mSize) {
          Object object1 = keyAt(b);
          Object object2 = valueAt(b);
          Object object3 = paramObject.get(object1);
          if (object2 == null) {
            if (object3 == null) {
              if (!paramObject.containsKey(object1))
                return false; 
            } else {
              return false;
            } 
          } else {
            boolean bool = object2.equals(object3);
            if (!bool)
              return false; 
          } 
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
  
  @Nullable
  public V get(Object paramObject) {
    int i = indexOfKey(paramObject);
    return (i >= 0) ? (V)this.mArray[(i << 1) + 1] : null;
  }
  
  public int hashCode() {
    int[] arrayOfInt = this.mHashes;
    Object[] arrayOfObject = this.mArray;
    int i = 0;
    byte b2 = 0;
    byte b1 = 1;
    int j = this.mSize;
    while (b2 < j) {
      int k;
      Object object = arrayOfObject[b1];
      int m = arrayOfInt[b2];
      if (object == null) {
        k = 0;
      } else {
        k = object.hashCode();
      } 
      i += (m ^ k);
      b2++;
      b1 += 2;
    } 
    return i;
  }
  
  int indexOf(Object paramObject, int paramInt) {
    int j = this.mSize;
    if (j == 0)
      return -1; 
    int k = binarySearchHashes(this.mHashes, j, paramInt);
    if (k < 0)
      return k; 
    if (paramObject.equals(this.mArray[k << 1]))
      return k; 
    int i;
    for (i = k + 1; i < j && this.mHashes[i] == paramInt; i++) {
      if (paramObject.equals(this.mArray[i << 1]))
        return i; 
    } 
    for (j = k - 1; j >= 0 && this.mHashes[j] == paramInt; j--) {
      if (paramObject.equals(this.mArray[j << 1]))
        return j; 
    } 
    return i ^ 0xFFFFFFFF;
  }
  
  public int indexOfKey(@Nullable Object paramObject) { return (paramObject == null) ? indexOfNull() : indexOf(paramObject, paramObject.hashCode()); }
  
  int indexOfNull() {
    int j = this.mSize;
    if (j == 0)
      return -1; 
    int k = binarySearchHashes(this.mHashes, j, 0);
    if (k < 0)
      return k; 
    if (this.mArray[k << true] == null)
      return k; 
    int i;
    for (i = k + 1; i < j && this.mHashes[i] == 0; i++) {
      if (this.mArray[i << true] == null)
        return i; 
    } 
    for (j = k - 1; j >= 0 && this.mHashes[j] == 0; j--) {
      if (this.mArray[j << true] == null)
        return j; 
    } 
    return i ^ 0xFFFFFFFF;
  }
  
  int indexOfValue(Object paramObject) {
    int i = this.mSize * 2;
    Object[] arrayOfObject = this.mArray;
    if (paramObject == null) {
      for (byte b = 1; b < i; b += 2) {
        if (arrayOfObject[b] == null)
          return b >> true; 
      } 
    } else {
      for (byte b = 1; b < i; b += 2) {
        if (paramObject.equals(arrayOfObject[b]))
          return b >> true; 
      } 
    } 
    return -1;
  }
  
  public boolean isEmpty() { return (this.mSize <= 0); }
  
  public K keyAt(int paramInt) { return (K)this.mArray[paramInt << 1]; }
  
  @Nullable
  public V put(K paramK, V paramV) {
    int j;
    int k = this.mSize;
    if (paramK == null) {
      j = 0;
      i = indexOfNull();
    } else {
      j = paramK.hashCode();
      i = indexOf(paramK, j);
    } 
    if (i >= 0) {
      i = (i << 1) + 1;
      paramK = (K)this.mArray;
      K k1 = paramK[i];
      paramK[i] = paramV;
      return (V)k1;
    } 
    int m = i ^ 0xFFFFFFFF;
    if (k >= this.mHashes.length) {
      i = 4;
      if (k >= 8) {
        i = (k >> 1) + k;
      } else if (k >= 4) {
        i = 8;
      } 
      int[] arrayOfInt = this.mHashes;
      Object[] arrayOfObject = this.mArray;
      allocArrays(i);
      if (k == this.mSize) {
        int[] arrayOfInt1 = this.mHashes;
        if (arrayOfInt1.length > 0) {
          System.arraycopy(arrayOfInt, 0, arrayOfInt1, 0, arrayOfInt.length);
          System.arraycopy(arrayOfObject, 0, this.mArray, 0, arrayOfObject.length);
        } 
        freeArrays(arrayOfInt, arrayOfObject, k);
      } else {
        throw new ConcurrentModificationException();
      } 
    } 
    if (m < k) {
      int[] arrayOfInt = this.mHashes;
      System.arraycopy(arrayOfInt, m, arrayOfInt, m + 1, k - m);
      Object[] arrayOfObject = this.mArray;
      System.arraycopy(arrayOfObject, m << 1, arrayOfObject, m + 1 << 1, this.mSize - m << 1);
    } 
    int i = this.mSize;
    if (k == i) {
      int[] arrayOfInt = this.mHashes;
      if (m < arrayOfInt.length) {
        arrayOfInt[m] = j;
        Object[] arrayOfObject = this.mArray;
        arrayOfObject[m << 1] = paramK;
        arrayOfObject[(m << 1) + 1] = paramV;
        this.mSize = i + 1;
        return null;
      } 
    } 
    throw new ConcurrentModificationException();
  }
  
  public void putAll(@NonNull SimpleArrayMap<? extends K, ? extends V> paramSimpleArrayMap) {
    int i = paramSimpleArrayMap.mSize;
    ensureCapacity(this.mSize + i);
    if (this.mSize == 0) {
      if (i > 0) {
        System.arraycopy(paramSimpleArrayMap.mHashes, 0, this.mHashes, 0, i);
        System.arraycopy(paramSimpleArrayMap.mArray, 0, this.mArray, 0, i << 1);
        this.mSize = i;
        return;
      } 
    } else {
      for (byte b = 0; b < i; b++)
        put(paramSimpleArrayMap.keyAt(b), paramSimpleArrayMap.valueAt(b)); 
    } 
  }
  
  @Nullable
  public V remove(Object paramObject) {
    int i = indexOfKey(paramObject);
    return (i >= 0) ? (V)removeAt(i) : null;
  }
  
  public V removeAt(int paramInt) {
    Object[] arrayOfObject = this.mArray;
    Object object = arrayOfObject[(paramInt << 1) + 1];
    int i = this.mSize;
    if (i <= 1) {
      freeArrays(this.mHashes, arrayOfObject, i);
      this.mHashes = ContainerHelpers.EMPTY_INTS;
      this.mArray = ContainerHelpers.EMPTY_OBJECTS;
      paramInt = 0;
    } else {
      int k = i - 1;
      int[] arrayOfInt = this.mHashes;
      int m = arrayOfInt.length;
      int j = 8;
      if (m > 8 && this.mSize < arrayOfInt.length / 3) {
        if (i > 8)
          j = i + (i >> 1); 
        arrayOfInt = this.mHashes;
        Object[] arrayOfObject1 = this.mArray;
        allocArrays(j);
        if (i == this.mSize) {
          if (paramInt > 0) {
            System.arraycopy(arrayOfInt, 0, this.mHashes, 0, paramInt);
            System.arraycopy(arrayOfObject1, 0, this.mArray, 0, paramInt << 1);
          } 
          if (paramInt < k) {
            System.arraycopy(arrayOfInt, paramInt + 1, this.mHashes, paramInt, k - paramInt);
            System.arraycopy(arrayOfObject1, paramInt + 1 << 1, this.mArray, paramInt << 1, k - paramInt << 1);
          } 
          paramInt = k;
        } else {
          throw new ConcurrentModificationException();
        } 
      } else {
        if (paramInt < k) {
          arrayOfInt = this.mHashes;
          System.arraycopy(arrayOfInt, paramInt + 1, arrayOfInt, paramInt, k - paramInt);
          Object[] arrayOfObject2 = this.mArray;
          System.arraycopy(arrayOfObject2, paramInt + 1 << 1, arrayOfObject2, paramInt << 1, k - paramInt << 1);
        } 
        Object[] arrayOfObject1 = this.mArray;
        arrayOfObject1[k << 1] = null;
        arrayOfObject1[(k << 1) + 1] = null;
        paramInt = k;
      } 
    } 
    if (i == this.mSize) {
      this.mSize = paramInt;
      return (V)object;
    } 
    throw new ConcurrentModificationException();
  }
  
  public V setValueAt(int paramInt, V paramV) {
    paramInt = (paramInt << 1) + 1;
    Object[] arrayOfObject = this.mArray;
    Object object = arrayOfObject[paramInt];
    arrayOfObject[paramInt] = paramV;
    return (V)object;
  }
  
  public int size() { return this.mSize; }
  
  public String toString() {
    if (isEmpty())
      return "{}"; 
    StringBuilder stringBuilder = new StringBuilder(this.mSize * 28);
    stringBuilder.append('{');
    for (byte b = 0; b < this.mSize; b++) {
      if (b)
        stringBuilder.append(", "); 
      Object object = keyAt(b);
      if (object != this) {
        stringBuilder.append(object);
      } else {
        stringBuilder.append("(this Map)");
      } 
      stringBuilder.append('=');
      object = valueAt(b);
      if (object != this) {
        stringBuilder.append(object);
      } else {
        stringBuilder.append("(this Map)");
      } 
    } 
    stringBuilder.append('}');
    return stringBuilder.toString();
  }
  
  public V valueAt(int paramInt) { return (V)this.mArray[(paramInt << 1) + 1]; }
}


/* Location:              D:\Apks\New folder\classes-dex2jar.jar!\android\support\v\\util\SimpleArrayMap.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.0.7
 */