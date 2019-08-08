package android.support.v4.util;

public final class CircularArray<E> extends Object {
  private int mCapacityBitmask;
  
  private E[] mElements;
  
  private int mHead;
  
  private int mTail;
  
  public CircularArray() { this(8); }
  
  public CircularArray(int paramInt) {
    if (paramInt >= 1) {
      if (paramInt <= 1073741824) {
        if (Integer.bitCount(paramInt) != 1)
          paramInt = Integer.highestOneBit(paramInt - 1) << 1; 
        this.mCapacityBitmask = paramInt - 1;
        this.mElements = (Object[])new Object[paramInt];
        return;
      } 
      throw new IllegalArgumentException("capacity must be <= 2^30");
    } 
    throw new IllegalArgumentException("capacity must be >= 1");
  }
  
  private void doubleCapacity() {
    Object[] arrayOfObject = this.mElements;
    int i = arrayOfObject.length;
    int j = this.mHead;
    int k = i - j;
    int m = i << 1;
    if (m >= 0) {
      Object[] arrayOfObject1 = new Object[m];
      System.arraycopy(arrayOfObject, j, arrayOfObject1, 0, k);
      System.arraycopy(this.mElements, 0, arrayOfObject1, k, this.mHead);
      this.mElements = (Object[])arrayOfObject1;
      this.mHead = 0;
      this.mTail = i;
      this.mCapacityBitmask = m - 1;
      return;
    } 
    throw new RuntimeException("Max array capacity exceeded");
  }
  
  public void addFirst(E paramE) {
    this.mHead = this.mHead - 1 & this.mCapacityBitmask;
    Object[] arrayOfObject = this.mElements;
    int i = this.mHead;
    arrayOfObject[i] = paramE;
    if (i == this.mTail)
      doubleCapacity(); 
  }
  
  public void addLast(E paramE) {
    Object[] arrayOfObject = this.mElements;
    int i = this.mTail;
    arrayOfObject[i] = paramE;
    this.mTail = this.mCapacityBitmask & i + 1;
    if (this.mTail == this.mHead)
      doubleCapacity(); 
  }
  
  public void clear() { removeFromStart(size()); }
  
  public E get(int paramInt) {
    if (paramInt >= 0 && paramInt < size())
      return (E)this.mElements[this.mHead + paramInt & this.mCapacityBitmask]; 
    throw new ArrayIndexOutOfBoundsException();
  }
  
  public E getFirst() {
    int i = this.mHead;
    if (i != this.mTail)
      return (E)this.mElements[i]; 
    throw new ArrayIndexOutOfBoundsException();
  }
  
  public E getLast() {
    int i = this.mHead;
    int j = this.mTail;
    if (i != j)
      return (E)this.mElements[j - 1 & this.mCapacityBitmask]; 
    throw new ArrayIndexOutOfBoundsException();
  }
  
  public boolean isEmpty() { return (this.mHead == this.mTail); }
  
  public E popFirst() {
    int i = this.mHead;
    if (i != this.mTail) {
      Object[] arrayOfObject = this.mElements;
      Object object = arrayOfObject[i];
      arrayOfObject[i] = null;
      this.mHead = i + 1 & this.mCapacityBitmask;
      return (E)object;
    } 
    throw new ArrayIndexOutOfBoundsException();
  }
  
  public E popLast() {
    int i = this.mHead;
    int j = this.mTail;
    if (i != j) {
      i = this.mCapacityBitmask & j - 1;
      Object[] arrayOfObject = this.mElements;
      Object object = arrayOfObject[i];
      arrayOfObject[i] = null;
      this.mTail = i;
      return (E)object;
    } 
    throw new ArrayIndexOutOfBoundsException();
  }
  
  public void removeFromEnd(int paramInt) {
    if (paramInt <= 0)
      return; 
    if (paramInt <= size()) {
      int k;
      int i = 0;
      int j = this.mTail;
      if (paramInt < j)
        i = j - paramInt; 
      j = i;
      while (true) {
        k = this.mTail;
        if (j < k) {
          this.mElements[j] = null;
          j++;
          continue;
        } 
        break;
      } 
      i = k - i;
      paramInt -= i;
      this.mTail = k - i;
      if (paramInt > 0) {
        this.mTail = this.mElements.length;
        i = this.mTail - paramInt;
        for (paramInt = i; paramInt < this.mTail; paramInt++)
          this.mElements[paramInt] = null; 
        this.mTail = i;
      } 
      return;
    } 
    throw new ArrayIndexOutOfBoundsException();
  }
  
  public void removeFromStart(int paramInt) {
    if (paramInt <= 0)
      return; 
    if (paramInt <= size()) {
      int j = this.mElements.length;
      int k = this.mHead;
      int i = j;
      if (paramInt < j - k)
        i = k + paramInt; 
      for (j = this.mHead; j < i; j++)
        this.mElements[j] = null; 
      j = this.mHead;
      k = i - j;
      i = paramInt - k;
      this.mHead = j + k & this.mCapacityBitmask;
      if (i > 0) {
        for (paramInt = 0; paramInt < i; paramInt++)
          this.mElements[paramInt] = null; 
        this.mHead = i;
      } 
      return;
    } 
    throw new ArrayIndexOutOfBoundsException();
  }
  
  public int size() { return this.mTail - this.mHead & this.mCapacityBitmask; }
}


/* Location:              D:\Apks\New folder\classes-dex2jar.jar!\android\support\v\\util\CircularArray.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.0.7
 */