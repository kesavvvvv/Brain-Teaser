package android.support.constraint.solver;

import java.io.PrintStream;
import java.util.Arrays;

public class ArrayLinkedVariables {
  private static final boolean DEBUG = false;
  
  private static final boolean FULL_NEW_CHECK = false;
  
  private static final int NONE = -1;
  
  private int ROW_SIZE = 8;
  
  private SolverVariable candidate = null;
  
  int currentSize = 0;
  
  private int[] mArrayIndices;
  
  private int[] mArrayNextIndices;
  
  private float[] mArrayValues;
  
  private final Cache mCache;
  
  private boolean mDidFillOnce;
  
  private int mHead;
  
  private int mLast;
  
  private final ArrayRow mRow;
  
  ArrayLinkedVariables(ArrayRow paramArrayRow, Cache paramCache) {
    int i = this.ROW_SIZE;
    this.mArrayIndices = new int[i];
    this.mArrayNextIndices = new int[i];
    this.mArrayValues = new float[i];
    this.mHead = -1;
    this.mLast = -1;
    this.mDidFillOnce = false;
    this.mRow = paramArrayRow;
    this.mCache = paramCache;
  }
  
  private boolean isNew(SolverVariable paramSolverVariable, LinearSystem paramLinearSystem) { return (paramSolverVariable.usageInRowCount <= 1); }
  
  final void add(SolverVariable paramSolverVariable, float paramFloat, boolean paramBoolean) {
    if (paramFloat == 0.0F)
      return; 
    if (this.mHead == -1) {
      this.mHead = 0;
      float[] arrayOfFloat = this.mArrayValues;
      int m = this.mHead;
      arrayOfFloat[m] = paramFloat;
      this.mArrayIndices[m] = paramSolverVariable.id;
      this.mArrayNextIndices[this.mHead] = -1;
      paramSolverVariable.usageInRowCount++;
      paramSolverVariable.addToRow(this.mRow);
      this.currentSize++;
      if (!this.mDidFillOnce) {
        m = ++this.mLast;
        arrayOfInt1 = this.mArrayIndices;
        if (m >= arrayOfInt1.length) {
          this.mDidFillOnce = true;
          this.mLast = arrayOfInt1.length - 1;
        } 
      } 
      return;
    } 
    int i = this.mHead;
    int k = -1;
    int j;
    for (j = 0; i != -1 && j < this.currentSize; j++) {
      if (this.mArrayIndices[i] == arrayOfInt1.id) {
        float[] arrayOfFloat = this.mArrayValues;
        arrayOfFloat[i] = arrayOfFloat[i] + paramFloat;
        if (arrayOfFloat[i] == 0.0F) {
          if (i == this.mHead) {
            this.mHead = this.mArrayNextIndices[i];
          } else {
            int[] arrayOfInt = this.mArrayNextIndices;
            arrayOfInt[k] = arrayOfInt[i];
          } 
          if (paramBoolean)
            arrayOfInt1.removeFromRow(this.mRow); 
          if (this.mDidFillOnce)
            this.mLast = i; 
          arrayOfInt1.usageInRowCount--;
          this.currentSize--;
        } 
        return;
      } 
      if (this.mArrayIndices[i] < arrayOfInt1.id)
        k = i; 
      i = this.mArrayNextIndices[i];
    } 
    j = this.mLast;
    i = j + 1;
    if (this.mDidFillOnce) {
      int[] arrayOfInt = this.mArrayIndices;
      if (arrayOfInt[j] == -1) {
        i = this.mLast;
      } else {
        i = arrayOfInt.length;
      } 
    } 
    int[] arrayOfInt2 = this.mArrayIndices;
    j = i;
    if (i >= arrayOfInt2.length) {
      j = i;
      if (this.currentSize < arrayOfInt2.length) {
        int m = 0;
        while (true) {
          arrayOfInt2 = this.mArrayIndices;
          j = i;
          if (m < arrayOfInt2.length) {
            if (arrayOfInt2[m] == -1) {
              j = m;
              break;
            } 
            m++;
            continue;
          } 
          break;
        } 
      } 
    } 
    arrayOfInt2 = this.mArrayIndices;
    i = j;
    if (j >= arrayOfInt2.length) {
      i = arrayOfInt2.length;
      this.ROW_SIZE *= 2;
      this.mDidFillOnce = false;
      this.mLast = i - 1;
      this.mArrayValues = Arrays.copyOf(this.mArrayValues, this.ROW_SIZE);
      this.mArrayIndices = Arrays.copyOf(this.mArrayIndices, this.ROW_SIZE);
      this.mArrayNextIndices = Arrays.copyOf(this.mArrayNextIndices, this.ROW_SIZE);
    } 
    this.mArrayIndices[i] = arrayOfInt1.id;
    this.mArrayValues[i] = paramFloat;
    if (k != -1) {
      arrayOfInt2 = this.mArrayNextIndices;
      arrayOfInt2[i] = arrayOfInt2[k];
      arrayOfInt2[k] = i;
    } else {
      this.mArrayNextIndices[i] = this.mHead;
      this.mHead = i;
    } 
    arrayOfInt1.usageInRowCount++;
    arrayOfInt1.addToRow(this.mRow);
    this.currentSize++;
    if (!this.mDidFillOnce)
      this.mLast++; 
    i = this.mLast;
    int[] arrayOfInt1 = this.mArrayIndices;
    if (i >= arrayOfInt1.length) {
      this.mDidFillOnce = true;
      this.mLast = arrayOfInt1.length - 1;
    } 
  }
  
  SolverVariable chooseSubject(LinearSystem paramLinearSystem) {
    SolverVariable solverVariable2 = null;
    SolverVariable solverVariable1 = null;
    float f2 = 0.0F;
    float f1 = 0.0F;
    boolean bool = false;
    byte b2 = 0;
    int i = this.mHead;
    byte b1 = 0;
    while (i != -1 && b1 < this.currentSize) {
      float f3;
      float f4 = this.mArrayValues[i];
      SolverVariable solverVariable3 = this.mCache.mIndexedVariables[this.mArrayIndices[i]];
      if (f4 < 0.0F) {
        f3 = f4;
        if (f4 > -0.001F) {
          this.mArrayValues[i] = 0.0F;
          f3 = 0.0F;
          solverVariable3.removeFromRow(this.mRow);
        } 
      } else {
        f3 = f4;
        if (f4 < 0.001F) {
          this.mArrayValues[i] = 0.0F;
          f3 = 0.0F;
          solverVariable3.removeFromRow(this.mRow);
        } 
      } 
      SolverVariable solverVariable4 = solverVariable2;
      SolverVariable solverVariable5 = solverVariable1;
      f4 = f2;
      float f5 = f1;
      boolean bool1 = bool;
      byte b = b2;
      if (f3 != 0.0F)
        if (solverVariable3.mType == SolverVariable.Type.UNRESTRICTED) {
          if (solverVariable1 == null) {
            solverVariable5 = solverVariable3;
            bool1 = isNew(solverVariable3, paramLinearSystem);
            solverVariable4 = solverVariable2;
            f4 = f3;
            f5 = f1;
            b = b2;
          } else if (f2 > f3) {
            solverVariable5 = solverVariable3;
            bool1 = isNew(solverVariable3, paramLinearSystem);
            solverVariable4 = solverVariable2;
            f4 = f3;
            f5 = f1;
            b = b2;
          } else {
            solverVariable4 = solverVariable2;
            solverVariable5 = solverVariable1;
            f4 = f2;
            f5 = f1;
            bool1 = bool;
            b = b2;
            if (!bool) {
              solverVariable4 = solverVariable2;
              solverVariable5 = solverVariable1;
              f4 = f2;
              f5 = f1;
              bool1 = bool;
              b = b2;
              if (isNew(solverVariable3, paramLinearSystem)) {
                bool1 = true;
                solverVariable4 = solverVariable2;
                solverVariable5 = solverVariable3;
                f4 = f3;
                f5 = f1;
                b = b2;
              } 
            } 
          } 
        } else {
          solverVariable4 = solverVariable2;
          solverVariable5 = solverVariable1;
          f4 = f2;
          f5 = f1;
          bool1 = bool;
          b = b2;
          if (solverVariable1 == null) {
            solverVariable4 = solverVariable2;
            solverVariable5 = solverVariable1;
            f4 = f2;
            f5 = f1;
            bool1 = bool;
            b = b2;
            if (f3 < 0.0F)
              if (solverVariable2 == null) {
                solverVariable4 = solverVariable3;
                boolean bool2 = isNew(solverVariable3, paramLinearSystem);
                solverVariable5 = solverVariable1;
                f4 = f2;
                f5 = f3;
                bool1 = bool;
              } else if (f1 > f3) {
                solverVariable4 = solverVariable3;
                boolean bool2 = isNew(solverVariable3, paramLinearSystem);
                solverVariable5 = solverVariable1;
                f4 = f2;
                f5 = f3;
                bool1 = bool;
              } else {
                solverVariable4 = solverVariable2;
                solverVariable5 = solverVariable1;
                f4 = f2;
                f5 = f1;
                bool1 = bool;
                b = b2;
                if (b2 == 0) {
                  solverVariable4 = solverVariable2;
                  solverVariable5 = solverVariable1;
                  f4 = f2;
                  f5 = f1;
                  bool1 = bool;
                  b = b2;
                  if (isNew(solverVariable3, paramLinearSystem)) {
                    b = 1;
                    bool1 = bool;
                    f5 = f3;
                    f4 = f2;
                    solverVariable5 = solverVariable1;
                    solverVariable4 = solverVariable3;
                  } 
                } 
              }  
          } 
        }  
      i = this.mArrayNextIndices[i];
      b1++;
      solverVariable2 = solverVariable4;
      solverVariable1 = solverVariable5;
      f2 = f4;
      f1 = f5;
      bool = bool1;
      b2 = b;
    } 
    return (solverVariable1 != null) ? solverVariable1 : solverVariable2;
  }
  
  public final void clear() {
    int i = this.mHead;
    for (byte b = 0; i != -1 && b < this.currentSize; b++) {
      SolverVariable solverVariable = this.mCache.mIndexedVariables[this.mArrayIndices[i]];
      if (solverVariable != null)
        solverVariable.removeFromRow(this.mRow); 
      i = this.mArrayNextIndices[i];
    } 
    this.mHead = -1;
    this.mLast = -1;
    this.mDidFillOnce = false;
    this.currentSize = 0;
  }
  
  final boolean containsKey(SolverVariable paramSolverVariable) {
    if (this.mHead == -1)
      return false; 
    int i = this.mHead;
    for (byte b = 0; i != -1 && b < this.currentSize; b++) {
      if (this.mArrayIndices[i] == paramSolverVariable.id)
        return true; 
      i = this.mArrayNextIndices[i];
    } 
    return false;
  }
  
  public void display() {
    int i = this.currentSize;
    System.out.print("{ ");
    for (byte b = 0; b < i; b++) {
      SolverVariable solverVariable = getVariable(b);
      if (solverVariable != null) {
        PrintStream printStream = System.out;
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(solverVariable);
        stringBuilder.append(" = ");
        stringBuilder.append(getVariableValue(b));
        stringBuilder.append(" ");
        printStream.print(stringBuilder.toString());
      } 
    } 
    System.out.println(" }");
  }
  
  void divideByAmount(float paramFloat) {
    int i = this.mHead;
    for (byte b = 0; i != -1 && b < this.currentSize; b++) {
      float[] arrayOfFloat = this.mArrayValues;
      arrayOfFloat[i] = arrayOfFloat[i] / paramFloat;
      i = this.mArrayNextIndices[i];
    } 
  }
  
  public final float get(SolverVariable paramSolverVariable) {
    int i = this.mHead;
    for (byte b = 0; i != -1 && b < this.currentSize; b++) {
      if (this.mArrayIndices[i] == paramSolverVariable.id)
        return this.mArrayValues[i]; 
      i = this.mArrayNextIndices[i];
    } 
    return 0.0F;
  }
  
  SolverVariable getPivotCandidate() { // Byte code:
    //   0: aload_0
    //   1: getfield candidate : Landroid/support/constraint/solver/SolverVariable;
    //   4: astore_3
    //   5: aload_3
    //   6: ifnonnull -> 103
    //   9: aload_0
    //   10: getfield mHead : I
    //   13: istore_2
    //   14: iconst_0
    //   15: istore_1
    //   16: aconst_null
    //   17: astore_3
    //   18: iload_2
    //   19: iconst_m1
    //   20: if_icmpeq -> 101
    //   23: iload_1
    //   24: aload_0
    //   25: getfield currentSize : I
    //   28: if_icmpge -> 101
    //   31: aload_3
    //   32: astore #4
    //   34: aload_0
    //   35: getfield mArrayValues : [F
    //   38: iload_2
    //   39: faload
    //   40: fconst_0
    //   41: fcmpg
    //   42: ifge -> 84
    //   45: aload_0
    //   46: getfield mCache : Landroid/support/constraint/solver/Cache;
    //   49: getfield mIndexedVariables : [Landroid/support/constraint/solver/SolverVariable;
    //   52: aload_0
    //   53: getfield mArrayIndices : [I
    //   56: iload_2
    //   57: iaload
    //   58: aaload
    //   59: astore #5
    //   61: aload_3
    //   62: ifnull -> 80
    //   65: aload_3
    //   66: astore #4
    //   68: aload_3
    //   69: getfield strength : I
    //   72: aload #5
    //   74: getfield strength : I
    //   77: if_icmpge -> 84
    //   80: aload #5
    //   82: astore #4
    //   84: aload_0
    //   85: getfield mArrayNextIndices : [I
    //   88: iload_2
    //   89: iaload
    //   90: istore_2
    //   91: iload_1
    //   92: iconst_1
    //   93: iadd
    //   94: istore_1
    //   95: aload #4
    //   97: astore_3
    //   98: goto -> 18
    //   101: aload_3
    //   102: areturn
    //   103: aload_3
    //   104: areturn }
  
  SolverVariable getPivotCandidate(boolean[] paramArrayOfBoolean, SolverVariable paramSolverVariable) { // Byte code:
    //   0: aload_0
    //   1: getfield mHead : I
    //   4: istore #7
    //   6: iconst_0
    //   7: istore #6
    //   9: aconst_null
    //   10: astore #8
    //   12: fconst_0
    //   13: fstore_3
    //   14: iload #7
    //   16: iconst_m1
    //   17: if_icmpeq -> 184
    //   20: iload #6
    //   22: aload_0
    //   23: getfield currentSize : I
    //   26: if_icmpge -> 184
    //   29: aload #8
    //   31: astore #9
    //   33: fload_3
    //   34: fstore #4
    //   36: aload_0
    //   37: getfield mArrayValues : [F
    //   40: iload #7
    //   42: faload
    //   43: fconst_0
    //   44: fcmpg
    //   45: ifge -> 159
    //   48: aload_0
    //   49: getfield mCache : Landroid/support/constraint/solver/Cache;
    //   52: getfield mIndexedVariables : [Landroid/support/constraint/solver/SolverVariable;
    //   55: aload_0
    //   56: getfield mArrayIndices : [I
    //   59: iload #7
    //   61: iaload
    //   62: aaload
    //   63: astore #10
    //   65: aload_1
    //   66: ifnull -> 86
    //   69: aload #8
    //   71: astore #9
    //   73: fload_3
    //   74: fstore #4
    //   76: aload_1
    //   77: aload #10
    //   79: getfield id : I
    //   82: baload
    //   83: ifne -> 159
    //   86: aload #8
    //   88: astore #9
    //   90: fload_3
    //   91: fstore #4
    //   93: aload #10
    //   95: aload_2
    //   96: if_acmpeq -> 159
    //   99: aload #10
    //   101: getfield mType : Landroid/support/constraint/solver/SolverVariable$Type;
    //   104: getstatic android/support/constraint/solver/SolverVariable$Type.SLACK : Landroid/support/constraint/solver/SolverVariable$Type;
    //   107: if_acmpeq -> 128
    //   110: aload #8
    //   112: astore #9
    //   114: fload_3
    //   115: fstore #4
    //   117: aload #10
    //   119: getfield mType : Landroid/support/constraint/solver/SolverVariable$Type;
    //   122: getstatic android/support/constraint/solver/SolverVariable$Type.ERROR : Landroid/support/constraint/solver/SolverVariable$Type;
    //   125: if_acmpne -> 159
    //   128: aload_0
    //   129: getfield mArrayValues : [F
    //   132: iload #7
    //   134: faload
    //   135: fstore #5
    //   137: aload #8
    //   139: astore #9
    //   141: fload_3
    //   142: fstore #4
    //   144: fload #5
    //   146: fload_3
    //   147: fcmpg
    //   148: ifge -> 159
    //   151: fload #5
    //   153: fstore #4
    //   155: aload #10
    //   157: astore #9
    //   159: aload_0
    //   160: getfield mArrayNextIndices : [I
    //   163: iload #7
    //   165: iaload
    //   166: istore #7
    //   168: iload #6
    //   170: iconst_1
    //   171: iadd
    //   172: istore #6
    //   174: aload #9
    //   176: astore #8
    //   178: fload #4
    //   180: fstore_3
    //   181: goto -> 14
    //   184: aload #8
    //   186: areturn }
  
  final SolverVariable getVariable(int paramInt) {
    int i = this.mHead;
    for (byte b = 0; i != -1 && b < this.currentSize; b++) {
      if (b == paramInt)
        return this.mCache.mIndexedVariables[this.mArrayIndices[i]]; 
      i = this.mArrayNextIndices[i];
    } 
    return null;
  }
  
  final float getVariableValue(int paramInt) {
    int i = this.mHead;
    for (byte b = 0; i != -1 && b < this.currentSize; b++) {
      if (b == paramInt)
        return this.mArrayValues[i]; 
      i = this.mArrayNextIndices[i];
    } 
    return 0.0F;
  }
  
  boolean hasAtLeastOnePositiveVariable() {
    int i = this.mHead;
    for (byte b = 0; i != -1 && b < this.currentSize; b++) {
      if (this.mArrayValues[i] > 0.0F)
        return true; 
      i = this.mArrayNextIndices[i];
    } 
    return false;
  }
  
  void invert() {
    int i = this.mHead;
    for (byte b = 0; i != -1 && b < this.currentSize; b++) {
      float[] arrayOfFloat = this.mArrayValues;
      arrayOfFloat[i] = arrayOfFloat[i] * -1.0F;
      i = this.mArrayNextIndices[i];
    } 
  }
  
  public final void put(SolverVariable paramSolverVariable, float paramFloat) {
    if (paramFloat == 0.0F) {
      remove(paramSolverVariable, true);
      return;
    } 
    if (this.mHead == -1) {
      this.mHead = 0;
      float[] arrayOfFloat = this.mArrayValues;
      int m = this.mHead;
      arrayOfFloat[m] = paramFloat;
      this.mArrayIndices[m] = paramSolverVariable.id;
      this.mArrayNextIndices[this.mHead] = -1;
      paramSolverVariable.usageInRowCount++;
      paramSolverVariable.addToRow(this.mRow);
      this.currentSize++;
      if (!this.mDidFillOnce) {
        m = ++this.mLast;
        arrayOfInt1 = this.mArrayIndices;
        if (m >= arrayOfInt1.length) {
          this.mDidFillOnce = true;
          this.mLast = arrayOfInt1.length - 1;
        } 
      } 
      return;
    } 
    int i = this.mHead;
    int k = -1;
    int j;
    for (j = 0; i != -1 && j < this.currentSize; j++) {
      if (this.mArrayIndices[i] == arrayOfInt1.id) {
        this.mArrayValues[i] = paramFloat;
        return;
      } 
      if (this.mArrayIndices[i] < arrayOfInt1.id)
        k = i; 
      i = this.mArrayNextIndices[i];
    } 
    j = this.mLast;
    i = j + 1;
    if (this.mDidFillOnce) {
      int[] arrayOfInt = this.mArrayIndices;
      if (arrayOfInt[j] == -1) {
        i = this.mLast;
      } else {
        i = arrayOfInt.length;
      } 
    } 
    int[] arrayOfInt2 = this.mArrayIndices;
    j = i;
    if (i >= arrayOfInt2.length) {
      j = i;
      if (this.currentSize < arrayOfInt2.length) {
        int m = 0;
        while (true) {
          arrayOfInt2 = this.mArrayIndices;
          j = i;
          if (m < arrayOfInt2.length) {
            if (arrayOfInt2[m] == -1) {
              j = m;
              break;
            } 
            m++;
            continue;
          } 
          break;
        } 
      } 
    } 
    arrayOfInt2 = this.mArrayIndices;
    i = j;
    if (j >= arrayOfInt2.length) {
      i = arrayOfInt2.length;
      this.ROW_SIZE *= 2;
      this.mDidFillOnce = false;
      this.mLast = i - 1;
      this.mArrayValues = Arrays.copyOf(this.mArrayValues, this.ROW_SIZE);
      this.mArrayIndices = Arrays.copyOf(this.mArrayIndices, this.ROW_SIZE);
      this.mArrayNextIndices = Arrays.copyOf(this.mArrayNextIndices, this.ROW_SIZE);
    } 
    this.mArrayIndices[i] = arrayOfInt1.id;
    this.mArrayValues[i] = paramFloat;
    if (k != -1) {
      arrayOfInt2 = this.mArrayNextIndices;
      arrayOfInt2[i] = arrayOfInt2[k];
      arrayOfInt2[k] = i;
    } else {
      this.mArrayNextIndices[i] = this.mHead;
      this.mHead = i;
    } 
    arrayOfInt1.usageInRowCount++;
    arrayOfInt1.addToRow(this.mRow);
    this.currentSize++;
    if (!this.mDidFillOnce)
      this.mLast++; 
    if (this.currentSize >= this.mArrayIndices.length)
      this.mDidFillOnce = true; 
    i = this.mLast;
    int[] arrayOfInt1 = this.mArrayIndices;
    if (i >= arrayOfInt1.length) {
      this.mDidFillOnce = true;
      this.mLast = arrayOfInt1.length - 1;
    } 
  }
  
  public final float remove(SolverVariable paramSolverVariable, boolean paramBoolean) {
    if (this.candidate == paramSolverVariable)
      this.candidate = null; 
    if (this.mHead == -1)
      return 0.0F; 
    int i = this.mHead;
    int j = -1;
    byte b;
    for (b = 0; i != -1 && b < this.currentSize; b++) {
      if (this.mArrayIndices[i] == paramSolverVariable.id) {
        if (i == this.mHead) {
          this.mHead = this.mArrayNextIndices[i];
        } else {
          int[] arrayOfInt = this.mArrayNextIndices;
          arrayOfInt[j] = arrayOfInt[i];
        } 
        if (paramBoolean)
          paramSolverVariable.removeFromRow(this.mRow); 
        paramSolverVariable.usageInRowCount--;
        this.currentSize--;
        this.mArrayIndices[i] = -1;
        if (this.mDidFillOnce)
          this.mLast = i; 
        return this.mArrayValues[i];
      } 
      j = i;
      i = this.mArrayNextIndices[i];
    } 
    return 0.0F;
  }
  
  int sizeInBytes() { return 0 + this.mArrayIndices.length * 4 * 3 + 36; }
  
  public String toString() {
    String str = "";
    int i = this.mHead;
    for (byte b = 0; i != -1 && b < this.currentSize; b++) {
      StringBuilder stringBuilder = new StringBuilder();
      stringBuilder.append(str);
      stringBuilder.append(" -> ");
      str = stringBuilder.toString();
      stringBuilder = new StringBuilder();
      stringBuilder.append(str);
      stringBuilder.append(this.mArrayValues[i]);
      stringBuilder.append(" : ");
      str = stringBuilder.toString();
      stringBuilder = new StringBuilder();
      stringBuilder.append(str);
      stringBuilder.append(this.mCache.mIndexedVariables[this.mArrayIndices[i]]);
      str = stringBuilder.toString();
      i = this.mArrayNextIndices[i];
    } 
    return str;
  }
  
  final void updateFromRow(ArrayRow paramArrayRow1, ArrayRow paramArrayRow2, boolean paramBoolean) {
    int i = this.mHead;
    int j;
    for (j = 0; i != -1 && j < this.currentSize; j++) {
      if (this.mArrayIndices[i] == paramArrayRow2.variable.id) {
        float f = this.mArrayValues[i];
        remove(paramArrayRow2.variable, paramBoolean);
        ArrayLinkedVariables arrayLinkedVariables = (ArrayLinkedVariables)paramArrayRow2.variables;
        j = arrayLinkedVariables.mHead;
        for (i = 0; j != -1 && i < arrayLinkedVariables.currentSize; i++) {
          add(this.mCache.mIndexedVariables[arrayLinkedVariables.mArrayIndices[j]], arrayLinkedVariables.mArrayValues[j] * f, paramBoolean);
          j = arrayLinkedVariables.mArrayNextIndices[j];
        } 
        paramArrayRow1.constantValue += paramArrayRow2.constantValue * f;
        if (paramBoolean)
          paramArrayRow2.variable.removeFromRow(paramArrayRow1); 
        i = this.mHead;
        j = 0;
        continue;
      } 
      i = this.mArrayNextIndices[i];
    } 
  }
  
  void updateFromSystem(ArrayRow paramArrayRow, ArrayRow[] paramArrayOfArrayRow) {
    int i = this.mHead;
    int j;
    for (j = 0; i != -1 && j < this.currentSize; j++) {
      SolverVariable solverVariable = this.mCache.mIndexedVariables[this.mArrayIndices[i]];
      if (solverVariable.definitionId != -1) {
        float f = this.mArrayValues[i];
        remove(solverVariable, true);
        ArrayRow arrayRow = paramArrayOfArrayRow[solverVariable.definitionId];
        if (!arrayRow.isSimpleDefinition) {
          ArrayLinkedVariables arrayLinkedVariables = (ArrayLinkedVariables)arrayRow.variables;
          j = arrayLinkedVariables.mHead;
          for (i = 0; j != -1 && i < arrayLinkedVariables.currentSize; i++) {
            add(this.mCache.mIndexedVariables[arrayLinkedVariables.mArrayIndices[j]], arrayLinkedVariables.mArrayValues[j] * f, true);
            j = arrayLinkedVariables.mArrayNextIndices[j];
          } 
        } 
        paramArrayRow.constantValue += arrayRow.constantValue * f;
        arrayRow.variable.removeFromRow(paramArrayRow);
        i = this.mHead;
        j = 0;
        continue;
      } 
      i = this.mArrayNextIndices[i];
    } 
  }
}


/* Location:              D:\Apks\New folder\classes-dex2jar.jar!\android\support\constraint\solver\ArrayLinkedVariables.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.0.7
 */