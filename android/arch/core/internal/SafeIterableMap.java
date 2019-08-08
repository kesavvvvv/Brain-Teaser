package android.arch.core.internal;

import android.support.annotation.NonNull;
import android.support.annotation.RestrictTo;
import java.util.Iterator;
import java.util.Map;
import java.util.WeakHashMap;

@RestrictTo({RestrictTo.Scope.LIBRARY_GROUP})
public class SafeIterableMap<K, V> extends Object implements Iterable<Map.Entry<K, V>> {
  private Entry<K, V> mEnd;
  
  private WeakHashMap<SupportRemove<K, V>, Boolean> mIterators = new WeakHashMap();
  
  private int mSize = 0;
  
  private Entry<K, V> mStart;
  
  public Iterator<Map.Entry<K, V>> descendingIterator() {
    DescendingIterator descendingIterator = new DescendingIterator(this.mEnd, this.mStart);
    this.mIterators.put(descendingIterator, Boolean.valueOf(false));
    return descendingIterator;
  }
  
  public Map.Entry<K, V> eldest() { return this.mStart; }
  
  public boolean equals(Object paramObject) {
    if (paramObject == this)
      return true; 
    if (!(paramObject instanceof SafeIterableMap))
      return false; 
    SafeIterableMap safeIterableMap = (SafeIterableMap)paramObject;
    if (size() != safeIterableMap.size())
      return false; 
    paramObject = iterator();
    Iterator iterator = safeIterableMap.iterator();
    while (paramObject.hasNext() && iterator.hasNext()) {
      Map.Entry entry = (Map.Entry)paramObject.next();
      Object object = iterator.next();
      if ((entry == null && object != null) || (entry != null && !entry.equals(object)))
        return false; 
    } 
    return (!paramObject.hasNext() && !iterator.hasNext());
  }
  
  protected Entry<K, V> get(K paramK) {
    Entry entry;
    for (entry = this.mStart; entry != null; entry = entry.mNext) {
      if (entry.mKey.equals(paramK))
        return entry; 
    } 
    return entry;
  }
  
  @NonNull
  public Iterator<Map.Entry<K, V>> iterator() {
    AscendingIterator ascendingIterator = new AscendingIterator(this.mStart, this.mEnd);
    this.mIterators.put(ascendingIterator, Boolean.valueOf(false));
    return ascendingIterator;
  }
  
  public IteratorWithAdditions iteratorWithAdditions() {
    IteratorWithAdditions iteratorWithAdditions = new IteratorWithAdditions(null);
    this.mIterators.put(iteratorWithAdditions, Boolean.valueOf(false));
    return iteratorWithAdditions;
  }
  
  public Map.Entry<K, V> newest() { return this.mEnd; }
  
  protected Entry<K, V> put(@NonNull K paramK, @NonNull V paramV) { // Byte code:
    //   0: new android/arch/core/internal/SafeIterableMap$Entry
    //   3: dup
    //   4: aload_1
    //   5: aload_2
    //   6: invokespecial <init> : (Ljava/lang/Object;Ljava/lang/Object;)V
    //   9: astore_1
    //   10: aload_0
    //   11: aload_0
    //   12: getfield mSize : I
    //   15: iconst_1
    //   16: iadd
    //   17: putfield mSize : I
    //   20: aload_0
    //   21: getfield mEnd : Landroid/arch/core/internal/SafeIterableMap$Entry;
    //   24: astore_2
    //   25: aload_2
    //   26: ifnonnull -> 44
    //   29: aload_0
    //   30: aload_1
    //   31: putfield mStart : Landroid/arch/core/internal/SafeIterableMap$Entry;
    //   34: aload_0
    //   35: aload_0
    //   36: getfield mStart : Landroid/arch/core/internal/SafeIterableMap$Entry;
    //   39: putfield mEnd : Landroid/arch/core/internal/SafeIterableMap$Entry;
    //   42: aload_1
    //   43: areturn
    //   44: aload_2
    //   45: aload_1
    //   46: putfield mNext : Landroid/arch/core/internal/SafeIterableMap$Entry;
    //   49: aload_1
    //   50: aload_2
    //   51: putfield mPrevious : Landroid/arch/core/internal/SafeIterableMap$Entry;
    //   54: aload_0
    //   55: aload_1
    //   56: putfield mEnd : Landroid/arch/core/internal/SafeIterableMap$Entry;
    //   59: aload_1
    //   60: areturn }
  
  public V putIfAbsent(@NonNull K paramK, @NonNull V paramV) {
    Entry entry = get(paramK);
    if (entry != null)
      return (V)entry.mValue; 
    put(paramK, paramV);
    return null;
  }
  
  public V remove(@NonNull K paramK) {
    paramK = (K)get(paramK);
    if (paramK == null)
      return null; 
    this.mSize--;
    if (!this.mIterators.isEmpty()) {
      Iterator iterator = this.mIterators.keySet().iterator();
      while (iterator.hasNext())
        ((SupportRemove)iterator.next()).supportRemove(paramK); 
    } 
    if (paramK.mPrevious != null) {
      paramK.mPrevious.mNext = paramK.mNext;
    } else {
      this.mStart = paramK.mNext;
    } 
    if (paramK.mNext != null) {
      paramK.mNext.mPrevious = paramK.mPrevious;
    } else {
      this.mEnd = paramK.mPrevious;
    } 
    paramK.mNext = null;
    paramK.mPrevious = null;
    return (V)paramK.mValue;
  }
  
  public int size() { return this.mSize; }
  
  public String toString() {
    StringBuilder stringBuilder = new StringBuilder();
    stringBuilder.append("[");
    Iterator iterator = iterator();
    while (iterator.hasNext()) {
      stringBuilder.append(((Map.Entry)iterator.next()).toString());
      if (iterator.hasNext())
        stringBuilder.append(", "); 
    } 
    stringBuilder.append("]");
    return stringBuilder.toString();
  }
  
  static class AscendingIterator<K, V> extends ListIterator<K, V> {
    AscendingIterator(SafeIterableMap.Entry<K, V> param1Entry1, SafeIterableMap.Entry<K, V> param1Entry2) { super(param1Entry1, param1Entry2); }
    
    SafeIterableMap.Entry<K, V> backward(SafeIterableMap.Entry<K, V> param1Entry) { return param1Entry.mPrevious; }
    
    SafeIterableMap.Entry<K, V> forward(SafeIterableMap.Entry<K, V> param1Entry) { return param1Entry.mNext; }
  }
  
  private static class DescendingIterator<K, V> extends ListIterator<K, V> {
    DescendingIterator(SafeIterableMap.Entry<K, V> param1Entry1, SafeIterableMap.Entry<K, V> param1Entry2) { super(param1Entry1, param1Entry2); }
    
    SafeIterableMap.Entry<K, V> backward(SafeIterableMap.Entry<K, V> param1Entry) { return param1Entry.mNext; }
    
    SafeIterableMap.Entry<K, V> forward(SafeIterableMap.Entry<K, V> param1Entry) { return param1Entry.mPrevious; }
  }
  
  static class Entry<K, V> extends Object implements Map.Entry<K, V> {
    @NonNull
    final K mKey;
    
    Entry<K, V> mNext;
    
    Entry<K, V> mPrevious;
    
    @NonNull
    final V mValue;
    
    Entry(@NonNull K param1K, @NonNull V param1V) {
      this.mKey = param1K;
      this.mValue = param1V;
    }
    
    public boolean equals(Object param1Object) {
      if (param1Object == this)
        return true; 
      if (!(param1Object instanceof Entry))
        return false; 
      param1Object = (Entry)param1Object;
      return (this.mKey.equals(param1Object.mKey) && this.mValue.equals(param1Object.mValue));
    }
    
    @NonNull
    public K getKey() { return (K)this.mKey; }
    
    @NonNull
    public V getValue() { return (V)this.mValue; }
    
    public V setValue(V param1V) { throw new UnsupportedOperationException("An entry modification is not supported"); }
    
    public String toString() {
      StringBuilder stringBuilder = new StringBuilder();
      stringBuilder.append(this.mKey);
      stringBuilder.append("=");
      stringBuilder.append(this.mValue);
      return stringBuilder.toString();
    }
  }
  
  private class IteratorWithAdditions extends Object implements Iterator<Map.Entry<K, V>>, SupportRemove<K, V> {
    private boolean mBeforeStart = true;
    
    private SafeIterableMap.Entry<K, V> mCurrent;
    
    private IteratorWithAdditions() {}
    
    public boolean hasNext() {
      if (this.mBeforeStart)
        return (SafeIterableMap.this.mStart != null); 
      SafeIterableMap.Entry entry = this.mCurrent;
      return (entry != null && entry.mNext != null);
    }
    
    public Map.Entry<K, V> next() {
      if (this.mBeforeStart) {
        this.mBeforeStart = false;
        this.mCurrent = SafeIterableMap.this.mStart;
      } else {
        SafeIterableMap.Entry entry = this.mCurrent;
        if (entry != null) {
          entry = entry.mNext;
        } else {
          entry = null;
        } 
        this.mCurrent = entry;
      } 
      return this.mCurrent;
    }
    
    public void supportRemove(@NonNull SafeIterableMap.Entry<K, V> param1Entry) {
      SafeIterableMap.Entry entry = this.mCurrent;
      if (param1Entry == entry) {
        boolean bool;
        this.mCurrent = entry.mPrevious;
        if (this.mCurrent == null) {
          bool = true;
        } else {
          bool = false;
        } 
        this.mBeforeStart = bool;
      } 
    }
  }
  
  private static abstract class ListIterator<K, V> extends Object implements Iterator<Map.Entry<K, V>>, SupportRemove<K, V> {
    SafeIterableMap.Entry<K, V> mExpectedEnd;
    
    SafeIterableMap.Entry<K, V> mNext;
    
    ListIterator(SafeIterableMap.Entry<K, V> param1Entry1, SafeIterableMap.Entry<K, V> param1Entry2) {
      this.mExpectedEnd = param1Entry2;
      this.mNext = param1Entry1;
    }
    
    private SafeIterableMap.Entry<K, V> nextNode() {
      SafeIterableMap.Entry entry1 = this.mNext;
      SafeIterableMap.Entry entry2 = this.mExpectedEnd;
      return (entry1 == entry2 || entry2 == null) ? null : forward(entry1);
    }
    
    abstract SafeIterableMap.Entry<K, V> backward(SafeIterableMap.Entry<K, V> param1Entry);
    
    abstract SafeIterableMap.Entry<K, V> forward(SafeIterableMap.Entry<K, V> param1Entry);
    
    public boolean hasNext() { return (this.mNext != null); }
    
    public Map.Entry<K, V> next() {
      SafeIterableMap.Entry entry = this.mNext;
      this.mNext = nextNode();
      return entry;
    }
    
    public void supportRemove(@NonNull SafeIterableMap.Entry<K, V> param1Entry) {
      if (this.mExpectedEnd == param1Entry && param1Entry == this.mNext) {
        this.mNext = null;
        this.mExpectedEnd = null;
      } 
      SafeIterableMap.Entry entry = this.mExpectedEnd;
      if (entry == param1Entry)
        this.mExpectedEnd = backward(entry); 
      if (this.mNext == param1Entry)
        this.mNext = nextNode(); 
    }
  }
  
  static interface SupportRemove<K, V> {
    void supportRemove(@NonNull SafeIterableMap.Entry<K, V> param1Entry);
  }
}


/* Location:              D:\Apks\New folder\classes-dex2jar.jar!\android\arch\core\internal\SafeIterableMap.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.0.7
 */