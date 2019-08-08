package android.support.constraint.solver;

import android.support.constraint.solver.widgets.ConstraintAnchor;
import android.support.constraint.solver.widgets.ConstraintWidget;
import java.io.PrintStream;
import java.util.Arrays;
import java.util.HashMap;

public class LinearSystem {
  private static final boolean DEBUG = false;
  
  public static final boolean FULL_DEBUG = false;
  
  private static int POOL_SIZE = 1000;
  
  public static Metrics sMetrics;
  
  private int TABLE_SIZE = 32;
  
  public boolean graphOptimizer;
  
  private boolean[] mAlreadyTestedCandidates;
  
  final Cache mCache;
  
  private Row mGoal;
  
  private int mMaxColumns;
  
  private int mMaxRows;
  
  int mNumColumns;
  
  int mNumRows;
  
  private SolverVariable[] mPoolVariables;
  
  private int mPoolVariablesCount;
  
  ArrayRow[] mRows;
  
  private final Row mTempGoal;
  
  private HashMap<String, SolverVariable> mVariables = null;
  
  int mVariablesID = 0;
  
  private ArrayRow[] tempClientsCopy;
  
  public LinearSystem() {
    int i = this.TABLE_SIZE;
    this.mMaxColumns = i;
    this.mRows = null;
    this.graphOptimizer = false;
    this.mAlreadyTestedCandidates = new boolean[i];
    this.mNumColumns = 1;
    this.mNumRows = 0;
    this.mMaxRows = i;
    this.mPoolVariables = new SolverVariable[POOL_SIZE];
    this.mPoolVariablesCount = 0;
    this.tempClientsCopy = new ArrayRow[i];
    this.mRows = new ArrayRow[i];
    releaseRows();
    this.mCache = new Cache();
    this.mGoal = new GoalRow(this.mCache);
    this.mTempGoal = new ArrayRow(this.mCache);
  }
  
  private SolverVariable acquireSolverVariable(SolverVariable.Type paramType, String paramString) {
    SolverVariable solverVariable1;
    SolverVariable solverVariable2 = (SolverVariable)this.mCache.solverVariablePool.acquire();
    if (solverVariable2 == null) {
      solverVariable2 = new SolverVariable(paramType, paramString);
      solverVariable2.setType(paramType, paramString);
      solverVariable1 = solverVariable2;
    } else {
      solverVariable2.reset();
      solverVariable2.setType(solverVariable1, paramString);
      solverVariable1 = solverVariable2;
    } 
    int i = this.mPoolVariablesCount;
    int j = POOL_SIZE;
    if (i >= j) {
      POOL_SIZE = j * 2;
      this.mPoolVariables = (SolverVariable[])Arrays.copyOf(this.mPoolVariables, POOL_SIZE);
    } 
    SolverVariable[] arrayOfSolverVariable = this.mPoolVariables;
    i = this.mPoolVariablesCount;
    this.mPoolVariablesCount = i + 1;
    arrayOfSolverVariable[i] = solverVariable1;
    return solverVariable1;
  }
  
  private void addError(ArrayRow paramArrayRow) { paramArrayRow.addError(this, 0); }
  
  private final void addRow(ArrayRow paramArrayRow) {
    if (this.mRows[this.mNumRows] != null)
      this.mCache.arrayRowPool.release(this.mRows[this.mNumRows]); 
    this.mRows[this.mNumRows] = paramArrayRow;
    SolverVariable solverVariable = paramArrayRow.variable;
    int i = this.mNumRows;
    solverVariable.definitionId = i;
    this.mNumRows = i + 1;
    paramArrayRow.variable.updateReferencesWithNewDefinition(paramArrayRow);
  }
  
  private void addSingleError(ArrayRow paramArrayRow, int paramInt) { addSingleError(paramArrayRow, paramInt, 0); }
  
  private void computeValues() {
    for (byte b = 0; b < this.mNumRows; b++) {
      ArrayRow arrayRow = this.mRows[b];
      arrayRow.variable.computedValue = arrayRow.constantValue;
    } 
  }
  
  public static ArrayRow createRowCentering(LinearSystem paramLinearSystem, SolverVariable paramSolverVariable1, SolverVariable paramSolverVariable2, int paramInt1, float paramFloat, SolverVariable paramSolverVariable3, SolverVariable paramSolverVariable4, int paramInt2, boolean paramBoolean) {
    ArrayRow arrayRow = paramLinearSystem.createRow();
    arrayRow.createRowCentering(paramSolverVariable1, paramSolverVariable2, paramInt1, paramFloat, paramSolverVariable3, paramSolverVariable4, paramInt2);
    if (paramBoolean) {
      arrayRow.addError(paramLinearSystem, 4);
      return arrayRow;
    } 
    return arrayRow;
  }
  
  public static ArrayRow createRowDimensionPercent(LinearSystem paramLinearSystem, SolverVariable paramSolverVariable1, SolverVariable paramSolverVariable2, SolverVariable paramSolverVariable3, float paramFloat, boolean paramBoolean) {
    ArrayRow arrayRow = paramLinearSystem.createRow();
    if (paramBoolean)
      paramLinearSystem.addError(arrayRow); 
    return arrayRow.createRowDimensionPercent(paramSolverVariable1, paramSolverVariable2, paramSolverVariable3, paramFloat);
  }
  
  public static ArrayRow createRowEquals(LinearSystem paramLinearSystem, SolverVariable paramSolverVariable1, SolverVariable paramSolverVariable2, int paramInt, boolean paramBoolean) {
    ArrayRow arrayRow = paramLinearSystem.createRow();
    arrayRow.createRowEquals(paramSolverVariable1, paramSolverVariable2, paramInt);
    if (paramBoolean)
      paramLinearSystem.addSingleError(arrayRow, 1); 
    return arrayRow;
  }
  
  public static ArrayRow createRowGreaterThan(LinearSystem paramLinearSystem, SolverVariable paramSolverVariable1, SolverVariable paramSolverVariable2, int paramInt, boolean paramBoolean) {
    SolverVariable solverVariable = paramLinearSystem.createSlackVariable();
    ArrayRow arrayRow = paramLinearSystem.createRow();
    arrayRow.createRowGreaterThan(paramSolverVariable1, paramSolverVariable2, solverVariable, paramInt);
    if (paramBoolean)
      paramLinearSystem.addSingleError(arrayRow, (int)(-1.0F * arrayRow.variables.get(solverVariable))); 
    return arrayRow;
  }
  
  public static ArrayRow createRowLowerThan(LinearSystem paramLinearSystem, SolverVariable paramSolverVariable1, SolverVariable paramSolverVariable2, int paramInt, boolean paramBoolean) {
    SolverVariable solverVariable = paramLinearSystem.createSlackVariable();
    ArrayRow arrayRow = paramLinearSystem.createRow();
    arrayRow.createRowLowerThan(paramSolverVariable1, paramSolverVariable2, solverVariable, paramInt);
    if (paramBoolean)
      paramLinearSystem.addSingleError(arrayRow, (int)(-1.0F * arrayRow.variables.get(solverVariable))); 
    return arrayRow;
  }
  
  private SolverVariable createVariable(String paramString, SolverVariable.Type paramType) {
    Metrics metrics = sMetrics;
    if (metrics != null)
      metrics.variables++; 
    if (this.mNumColumns + 1 >= this.mMaxColumns)
      increaseTableSize(); 
    SolverVariable solverVariable = acquireSolverVariable(paramType, null);
    solverVariable.setName(paramString);
    this.mVariablesID++;
    this.mNumColumns++;
    solverVariable.id = this.mVariablesID;
    if (this.mVariables == null)
      this.mVariables = new HashMap(); 
    this.mVariables.put(paramString, solverVariable);
    this.mCache.mIndexedVariables[this.mVariablesID] = solverVariable;
    return solverVariable;
  }
  
  private void displayRows() {
    displaySolverVariables();
    String str = "";
    for (byte b = 0; b < this.mNumRows; b++) {
      StringBuilder stringBuilder1 = new StringBuilder();
      stringBuilder1.append(str);
      stringBuilder1.append(this.mRows[b]);
      str = stringBuilder1.toString();
      stringBuilder1 = new StringBuilder();
      stringBuilder1.append(str);
      stringBuilder1.append("\n");
      str = stringBuilder1.toString();
    } 
    StringBuilder stringBuilder = new StringBuilder();
    stringBuilder.append(str);
    stringBuilder.append(this.mGoal);
    stringBuilder.append("\n");
    str = stringBuilder.toString();
    System.out.println(str);
  }
  
  private void displaySolverVariables() {
    StringBuilder stringBuilder = new StringBuilder();
    stringBuilder.append("Display Rows (");
    stringBuilder.append(this.mNumRows);
    stringBuilder.append("x");
    stringBuilder.append(this.mNumColumns);
    stringBuilder.append(")\n");
    String str = stringBuilder.toString();
    System.out.println(str);
  }
  
  private int enforceBFS(Row paramRow) throws Exception { // Byte code:
    //   0: iconst_0
    //   1: istore #7
    //   3: iconst_0
    //   4: istore #8
    //   6: iconst_0
    //   7: istore #6
    //   9: iload #8
    //   11: istore #5
    //   13: iload #6
    //   15: aload_0
    //   16: getfield mNumRows : I
    //   19: if_icmpge -> 74
    //   22: aload_0
    //   23: getfield mRows : [Landroid/support/constraint/solver/ArrayRow;
    //   26: iload #6
    //   28: aaload
    //   29: getfield variable : Landroid/support/constraint/solver/SolverVariable;
    //   32: getfield mType : Landroid/support/constraint/solver/SolverVariable$Type;
    //   35: getstatic android/support/constraint/solver/SolverVariable$Type.UNRESTRICTED : Landroid/support/constraint/solver/SolverVariable$Type;
    //   38: if_acmpne -> 44
    //   41: goto -> 65
    //   44: aload_0
    //   45: getfield mRows : [Landroid/support/constraint/solver/ArrayRow;
    //   48: iload #6
    //   50: aaload
    //   51: getfield constantValue : F
    //   54: fconst_0
    //   55: fcmpg
    //   56: ifge -> 65
    //   59: iconst_1
    //   60: istore #5
    //   62: goto -> 74
    //   65: iload #6
    //   67: iconst_1
    //   68: iadd
    //   69: istore #6
    //   71: goto -> 9
    //   74: iload #7
    //   76: istore #6
    //   78: iload #5
    //   80: ifeq -> 552
    //   83: iconst_0
    //   84: istore #8
    //   86: iconst_0
    //   87: istore #5
    //   89: iload #5
    //   91: istore #6
    //   93: iload #8
    //   95: ifne -> 552
    //   98: getstatic android/support/constraint/solver/LinearSystem.sMetrics : Landroid/support/constraint/solver/Metrics;
    //   101: astore_1
    //   102: aload_1
    //   103: ifnull -> 116
    //   106: aload_1
    //   107: aload_1
    //   108: getfield bfs : J
    //   111: lconst_1
    //   112: ladd
    //   113: putfield bfs : J
    //   116: iload #5
    //   118: iconst_1
    //   119: iadd
    //   120: istore #15
    //   122: ldc_w 3.4028235E38
    //   125: fstore_2
    //   126: iconst_0
    //   127: istore #5
    //   129: iconst_m1
    //   130: istore #6
    //   132: iconst_m1
    //   133: istore #7
    //   135: iconst_0
    //   136: istore #9
    //   138: iload #9
    //   140: aload_0
    //   141: getfield mNumRows : I
    //   144: if_icmpge -> 450
    //   147: aload_0
    //   148: getfield mRows : [Landroid/support/constraint/solver/ArrayRow;
    //   151: iload #9
    //   153: aaload
    //   154: astore_1
    //   155: aload_1
    //   156: getfield variable : Landroid/support/constraint/solver/SolverVariable;
    //   159: getfield mType : Landroid/support/constraint/solver/SolverVariable$Type;
    //   162: getstatic android/support/constraint/solver/SolverVariable$Type.UNRESTRICTED : Landroid/support/constraint/solver/SolverVariable$Type;
    //   165: if_acmpne -> 185
    //   168: fload_2
    //   169: fstore_3
    //   170: iload #5
    //   172: istore #11
    //   174: iload #6
    //   176: istore #12
    //   178: iload #7
    //   180: istore #13
    //   182: goto -> 427
    //   185: aload_1
    //   186: getfield isSimpleDefinition : Z
    //   189: ifeq -> 209
    //   192: fload_2
    //   193: fstore_3
    //   194: iload #5
    //   196: istore #11
    //   198: iload #6
    //   200: istore #12
    //   202: iload #7
    //   204: istore #13
    //   206: goto -> 427
    //   209: fload_2
    //   210: fstore_3
    //   211: iload #5
    //   213: istore #11
    //   215: iload #6
    //   217: istore #12
    //   219: iload #7
    //   221: istore #13
    //   223: aload_1
    //   224: getfield constantValue : F
    //   227: fconst_0
    //   228: fcmpg
    //   229: ifge -> 427
    //   232: iconst_1
    //   233: istore #10
    //   235: fload_2
    //   236: fstore_3
    //   237: iload #5
    //   239: istore #11
    //   241: iload #6
    //   243: istore #12
    //   245: iload #7
    //   247: istore #13
    //   249: iload #10
    //   251: aload_0
    //   252: getfield mNumColumns : I
    //   255: if_icmpge -> 427
    //   258: aload_0
    //   259: getfield mCache : Landroid/support/constraint/solver/Cache;
    //   262: getfield mIndexedVariables : [Landroid/support/constraint/solver/SolverVariable;
    //   265: iload #10
    //   267: aaload
    //   268: astore #16
    //   270: aload_1
    //   271: getfield variables : Landroid/support/constraint/solver/ArrayLinkedVariables;
    //   274: aload #16
    //   276: invokevirtual get : (Landroid/support/constraint/solver/SolverVariable;)F
    //   279: fstore #4
    //   281: fload #4
    //   283: fconst_0
    //   284: fcmpg
    //   285: ifgt -> 305
    //   288: fload_2
    //   289: fstore_3
    //   290: iload #5
    //   292: istore #12
    //   294: iload #6
    //   296: istore #13
    //   298: iload #7
    //   300: istore #14
    //   302: goto -> 404
    //   305: iconst_0
    //   306: istore #12
    //   308: iload #6
    //   310: istore #11
    //   312: iload #5
    //   314: istore #6
    //   316: iload #12
    //   318: istore #5
    //   320: fload_2
    //   321: fstore_3
    //   322: iload #6
    //   324: istore #12
    //   326: iload #11
    //   328: istore #13
    //   330: iload #7
    //   332: istore #14
    //   334: iload #5
    //   336: bipush #7
    //   338: if_icmpge -> 404
    //   341: aload #16
    //   343: getfield strengthVector : [F
    //   346: iload #5
    //   348: faload
    //   349: fload #4
    //   351: fdiv
    //   352: fstore_3
    //   353: fload_3
    //   354: fload_2
    //   355: fcmpg
    //   356: ifge -> 366
    //   359: iload #5
    //   361: iload #6
    //   363: if_icmpeq -> 377
    //   366: iload #6
    //   368: istore #12
    //   370: iload #5
    //   372: iload #6
    //   374: if_icmple -> 391
    //   377: fload_3
    //   378: fstore_2
    //   379: iload #9
    //   381: istore #11
    //   383: iload #10
    //   385: istore #7
    //   387: iload #5
    //   389: istore #12
    //   391: iload #5
    //   393: iconst_1
    //   394: iadd
    //   395: istore #5
    //   397: iload #12
    //   399: istore #6
    //   401: goto -> 320
    //   404: iload #10
    //   406: iconst_1
    //   407: iadd
    //   408: istore #10
    //   410: fload_3
    //   411: fstore_2
    //   412: iload #12
    //   414: istore #5
    //   416: iload #13
    //   418: istore #6
    //   420: iload #14
    //   422: istore #7
    //   424: goto -> 235
    //   427: iload #9
    //   429: iconst_1
    //   430: iadd
    //   431: istore #9
    //   433: fload_3
    //   434: fstore_2
    //   435: iload #11
    //   437: istore #5
    //   439: iload #12
    //   441: istore #6
    //   443: iload #13
    //   445: istore #7
    //   447: goto -> 138
    //   450: iload #6
    //   452: iconst_m1
    //   453: if_icmpeq -> 528
    //   456: aload_0
    //   457: getfield mRows : [Landroid/support/constraint/solver/ArrayRow;
    //   460: iload #6
    //   462: aaload
    //   463: astore_1
    //   464: aload_1
    //   465: getfield variable : Landroid/support/constraint/solver/SolverVariable;
    //   468: iconst_m1
    //   469: putfield definitionId : I
    //   472: getstatic android/support/constraint/solver/LinearSystem.sMetrics : Landroid/support/constraint/solver/Metrics;
    //   475: astore #16
    //   477: aload #16
    //   479: ifnull -> 494
    //   482: aload #16
    //   484: aload #16
    //   486: getfield pivots : J
    //   489: lconst_1
    //   490: ladd
    //   491: putfield pivots : J
    //   494: aload_1
    //   495: aload_0
    //   496: getfield mCache : Landroid/support/constraint/solver/Cache;
    //   499: getfield mIndexedVariables : [Landroid/support/constraint/solver/SolverVariable;
    //   502: iload #7
    //   504: aaload
    //   505: invokevirtual pivot : (Landroid/support/constraint/solver/SolverVariable;)V
    //   508: aload_1
    //   509: getfield variable : Landroid/support/constraint/solver/SolverVariable;
    //   512: iload #6
    //   514: putfield definitionId : I
    //   517: aload_1
    //   518: getfield variable : Landroid/support/constraint/solver/SolverVariable;
    //   521: aload_1
    //   522: invokevirtual updateReferencesWithNewDefinition : (Landroid/support/constraint/solver/ArrayRow;)V
    //   525: goto -> 531
    //   528: iconst_1
    //   529: istore #8
    //   531: iload #15
    //   533: aload_0
    //   534: getfield mNumColumns : I
    //   537: iconst_2
    //   538: idiv
    //   539: if_icmple -> 545
    //   542: iconst_1
    //   543: istore #8
    //   545: iload #15
    //   547: istore #5
    //   549: goto -> 89
    //   552: iload #6
    //   554: ireturn }
  
  private String getDisplaySize(int paramInt) {
    int i = paramInt * 4 / 1024 / 1024;
    if (i > 0) {
      StringBuilder stringBuilder1 = new StringBuilder();
      stringBuilder1.append("");
      stringBuilder1.append(i);
      stringBuilder1.append(" Mb");
      return stringBuilder1.toString();
    } 
    i = paramInt * 4 / 1024;
    if (i > 0) {
      StringBuilder stringBuilder1 = new StringBuilder();
      stringBuilder1.append("");
      stringBuilder1.append(i);
      stringBuilder1.append(" Kb");
      return stringBuilder1.toString();
    } 
    StringBuilder stringBuilder = new StringBuilder();
    stringBuilder.append("");
    stringBuilder.append(paramInt * 4);
    stringBuilder.append(" bytes");
    return stringBuilder.toString();
  }
  
  private String getDisplayStrength(int paramInt) { return (paramInt == 1) ? "LOW" : ((paramInt == 2) ? "MEDIUM" : ((paramInt == 3) ? "HIGH" : ((paramInt == 4) ? "HIGHEST" : ((paramInt == 5) ? "EQUALITY" : ((paramInt == 6) ? "FIXED" : "NONE"))))); }
  
  public static Metrics getMetrics() { return sMetrics; }
  
  private void increaseTableSize() {
    this.TABLE_SIZE *= 2;
    this.mRows = (ArrayRow[])Arrays.copyOf(this.mRows, this.TABLE_SIZE);
    Cache cache = this.mCache;
    cache.mIndexedVariables = (SolverVariable[])Arrays.copyOf(cache.mIndexedVariables, this.TABLE_SIZE);
    int i = this.TABLE_SIZE;
    this.mAlreadyTestedCandidates = new boolean[i];
    this.mMaxColumns = i;
    this.mMaxRows = i;
    Metrics metrics = sMetrics;
    if (metrics != null) {
      metrics.tableSizeIncrease++;
      metrics = sMetrics;
      metrics.maxTableSize = Math.max(metrics.maxTableSize, this.TABLE_SIZE);
      metrics = sMetrics;
      metrics.lastTableSize = metrics.maxTableSize;
    } 
  }
  
  private final int optimize(Row paramRow, boolean paramBoolean) {
    byte b2;
    boolean bool;
    Metrics metrics = sMetrics;
    if (metrics != null)
      metrics.optimize++; 
    byte b3 = 0;
    byte b4 = 0;
    byte b1 = 0;
    while (true) {
      bool = b3;
      b2 = b4;
      if (b1 < this.mNumColumns) {
        this.mAlreadyTestedCandidates[b1] = false;
        b1++;
        continue;
      } 
      break;
    } 
    while (!bool) {
      metrics = sMetrics;
      if (metrics != null)
        metrics.iterations++; 
      b4 = b2 + true;
      if (b4 >= this.mNumColumns * 2)
        return b4; 
      if (paramRow.getKey() != null)
        this.mAlreadyTestedCandidates[(paramRow.getKey()).id] = true; 
      SolverVariable solverVariable = paramRow.getPivotCandidate(this, this.mAlreadyTestedCandidates);
      if (solverVariable != null) {
        if (this.mAlreadyTestedCandidates[solverVariable.id])
          return b4; 
        this.mAlreadyTestedCandidates[solverVariable.id] = true;
      } 
      if (solverVariable != null) {
        float f = Float.MAX_VALUE;
        b2 = -1;
        b1 = 0;
        while (b1 < this.mNumRows) {
          float f1;
          ArrayRow arrayRow = this.mRows[b1];
          if (arrayRow.variable.mType == SolverVariable.Type.UNRESTRICTED) {
            f1 = f;
            b3 = b2;
          } else if (arrayRow.isSimpleDefinition) {
            f1 = f;
            b3 = b2;
          } else {
            f1 = f;
            b3 = b2;
            if (arrayRow.hasVariable(solverVariable)) {
              float f2 = arrayRow.variables.get(solverVariable);
              f1 = f;
              b3 = b2;
              if (f2 < 0.0F) {
                f2 = -arrayRow.constantValue / f2;
                f1 = f;
                b3 = b2;
                if (f2 < f) {
                  f1 = f2;
                  b3 = b1;
                } 
              } 
            } 
          } 
          b1++;
          f = f1;
          b2 = b3;
        } 
        if (b2 > -1) {
          ArrayRow arrayRow = this.mRows[b2];
          arrayRow.variable.definitionId = -1;
          Metrics metrics1 = sMetrics;
          if (metrics1 != null)
            metrics1.pivots++; 
          arrayRow.pivot(solverVariable);
          arrayRow.variable.definitionId = b2;
          arrayRow.variable.updateReferencesWithNewDefinition(arrayRow);
        } else {
          bool = true;
        } 
      } else {
        bool = true;
      } 
      b2 = b4;
    } 
    return b2;
  }
  
  private void releaseRows() {
    byte b = 0;
    while (true) {
      ArrayRow[] arrayOfArrayRow = this.mRows;
      if (b < arrayOfArrayRow.length) {
        ArrayRow arrayRow = arrayOfArrayRow[b];
        if (arrayRow != null)
          this.mCache.arrayRowPool.release(arrayRow); 
        this.mRows[b] = null;
        b++;
        continue;
      } 
      break;
    } 
  }
  
  private final void updateRowFromVariables(ArrayRow paramArrayRow) {
    if (this.mNumRows > 0) {
      paramArrayRow.variables.updateFromSystem(paramArrayRow, this.mRows);
      if (paramArrayRow.variables.currentSize == 0)
        paramArrayRow.isSimpleDefinition = true; 
    } 
  }
  
  public void addCenterPoint(ConstraintWidget paramConstraintWidget1, ConstraintWidget paramConstraintWidget2, float paramFloat, int paramInt) {
    SolverVariable solverVariable3 = createObjectVariable(paramConstraintWidget1.getAnchor(ConstraintAnchor.Type.LEFT));
    SolverVariable solverVariable5 = createObjectVariable(paramConstraintWidget1.getAnchor(ConstraintAnchor.Type.TOP));
    SolverVariable solverVariable4 = createObjectVariable(paramConstraintWidget1.getAnchor(ConstraintAnchor.Type.RIGHT));
    SolverVariable solverVariable7 = createObjectVariable(paramConstraintWidget1.getAnchor(ConstraintAnchor.Type.BOTTOM));
    SolverVariable solverVariable1 = createObjectVariable(paramConstraintWidget2.getAnchor(ConstraintAnchor.Type.LEFT));
    SolverVariable solverVariable8 = createObjectVariable(paramConstraintWidget2.getAnchor(ConstraintAnchor.Type.TOP));
    SolverVariable solverVariable6 = createObjectVariable(paramConstraintWidget2.getAnchor(ConstraintAnchor.Type.RIGHT));
    SolverVariable solverVariable2 = createObjectVariable(paramConstraintWidget2.getAnchor(ConstraintAnchor.Type.BOTTOM));
    ArrayRow arrayRow2 = createRow();
    double d1 = Math.sin(paramFloat);
    double d2 = paramInt;
    Double.isNaN(d2);
    arrayRow2.createRowWithAngle(solverVariable5, solverVariable7, solverVariable8, solverVariable2, (float)(d1 * d2));
    addConstraint(arrayRow2);
    ArrayRow arrayRow1 = createRow();
    d1 = Math.cos(paramFloat);
    d2 = paramInt;
    Double.isNaN(d2);
    arrayRow1.createRowWithAngle(solverVariable3, solverVariable4, solverVariable1, solverVariable6, (float)(d1 * d2));
    addConstraint(arrayRow1);
  }
  
  public void addCentering(SolverVariable paramSolverVariable1, SolverVariable paramSolverVariable2, int paramInt1, float paramFloat, SolverVariable paramSolverVariable3, SolverVariable paramSolverVariable4, int paramInt2, int paramInt3) {
    ArrayRow arrayRow = createRow();
    arrayRow.createRowCentering(paramSolverVariable1, paramSolverVariable2, paramInt1, paramFloat, paramSolverVariable3, paramSolverVariable4, paramInt2);
    if (paramInt3 != 6)
      arrayRow.addError(this, paramInt3); 
    addConstraint(arrayRow);
  }
  
  public void addConstraint(ArrayRow paramArrayRow) {
    if (paramArrayRow == null)
      return; 
    Metrics metrics = sMetrics;
    if (metrics != null) {
      metrics.constraints++;
      if (paramArrayRow.isSimpleDefinition) {
        metrics = sMetrics;
        metrics.simpleconstraints++;
      } 
    } 
    if (this.mNumRows + 1 >= this.mMaxRows || this.mNumColumns + 1 >= this.mMaxColumns)
      increaseTableSize(); 
    byte b1 = 0;
    byte b2 = 0;
    if (!paramArrayRow.isSimpleDefinition) {
      updateRowFromVariables(paramArrayRow);
      if (paramArrayRow.isEmpty())
        return; 
      paramArrayRow.ensurePositiveConstant();
      b1 = b2;
      if (paramArrayRow.chooseSubject(this)) {
        SolverVariable solverVariable = createExtraVariable();
        paramArrayRow.variable = solverVariable;
        addRow(paramArrayRow);
        b2 = 1;
        this.mTempGoal.initFromRow(paramArrayRow);
        optimize(this.mTempGoal, true);
        b1 = b2;
        if (solverVariable.definitionId == -1) {
          if (paramArrayRow.variable == solverVariable) {
            solverVariable = paramArrayRow.pickPivot(solverVariable);
            if (solverVariable != null) {
              Metrics metrics1 = sMetrics;
              if (metrics1 != null)
                metrics1.pivots++; 
              paramArrayRow.pivot(solverVariable);
            } 
          } 
          if (!paramArrayRow.isSimpleDefinition)
            paramArrayRow.variable.updateReferencesWithNewDefinition(paramArrayRow); 
          this.mNumRows--;
          b1 = b2;
        } 
      } 
      if (!paramArrayRow.hasKeyVariable())
        return; 
    } 
    if (b1 == 0)
      addRow(paramArrayRow); 
  }
  
  public ArrayRow addEquality(SolverVariable paramSolverVariable1, SolverVariable paramSolverVariable2, int paramInt1, int paramInt2) {
    ArrayRow arrayRow = createRow();
    arrayRow.createRowEquals(paramSolverVariable1, paramSolverVariable2, paramInt1);
    if (paramInt2 != 6)
      arrayRow.addError(this, paramInt2); 
    addConstraint(arrayRow);
    return arrayRow;
  }
  
  public void addEquality(SolverVariable paramSolverVariable, int paramInt) {
    int i = paramSolverVariable.definitionId;
    if (paramSolverVariable.definitionId != -1) {
      ArrayRow arrayRow1 = this.mRows[i];
      if (arrayRow1.isSimpleDefinition) {
        arrayRow1.constantValue = paramInt;
      } else if (arrayRow1.variables.currentSize == 0) {
        arrayRow1.isSimpleDefinition = true;
        arrayRow1.constantValue = paramInt;
      } else {
        arrayRow1 = createRow();
        arrayRow1.createRowEquals(paramSolverVariable, paramInt);
        addConstraint(arrayRow1);
      } 
      return;
    } 
    ArrayRow arrayRow = createRow();
    arrayRow.createRowDefinition(paramSolverVariable, paramInt);
    addConstraint(arrayRow);
  }
  
  public void addEquality(SolverVariable paramSolverVariable, int paramInt1, int paramInt2) {
    int i = paramSolverVariable.definitionId;
    if (paramSolverVariable.definitionId != -1) {
      ArrayRow arrayRow1 = this.mRows[i];
      if (arrayRow1.isSimpleDefinition) {
        arrayRow1.constantValue = paramInt1;
      } else {
        arrayRow1 = createRow();
        arrayRow1.createRowEquals(paramSolverVariable, paramInt1);
        arrayRow1.addError(this, paramInt2);
        addConstraint(arrayRow1);
      } 
      return;
    } 
    ArrayRow arrayRow = createRow();
    arrayRow.createRowDefinition(paramSolverVariable, paramInt1);
    arrayRow.addError(this, paramInt2);
    addConstraint(arrayRow);
  }
  
  public void addGreaterBarrier(SolverVariable paramSolverVariable1, SolverVariable paramSolverVariable2, boolean paramBoolean) {
    ArrayRow arrayRow = createRow();
    SolverVariable solverVariable = createSlackVariable();
    solverVariable.strength = 0;
    arrayRow.createRowGreaterThan(paramSolverVariable1, paramSolverVariable2, solverVariable, 0);
    if (paramBoolean)
      addSingleError(arrayRow, (int)(-1.0F * arrayRow.variables.get(solverVariable)), 1); 
    addConstraint(arrayRow);
  }
  
  public void addGreaterThan(SolverVariable paramSolverVariable, int paramInt) {
    ArrayRow arrayRow = createRow();
    SolverVariable solverVariable = createSlackVariable();
    solverVariable.strength = 0;
    arrayRow.createRowGreaterThan(paramSolverVariable, paramInt, solverVariable);
    addConstraint(arrayRow);
  }
  
  public void addGreaterThan(SolverVariable paramSolverVariable1, SolverVariable paramSolverVariable2, int paramInt1, int paramInt2) {
    ArrayRow arrayRow = createRow();
    SolverVariable solverVariable = createSlackVariable();
    solverVariable.strength = 0;
    arrayRow.createRowGreaterThan(paramSolverVariable1, paramSolverVariable2, solverVariable, paramInt1);
    if (paramInt2 != 6)
      addSingleError(arrayRow, (int)(-1.0F * arrayRow.variables.get(solverVariable)), paramInt2); 
    addConstraint(arrayRow);
  }
  
  public void addLowerBarrier(SolverVariable paramSolverVariable1, SolverVariable paramSolverVariable2, boolean paramBoolean) {
    ArrayRow arrayRow = createRow();
    SolverVariable solverVariable = createSlackVariable();
    solverVariable.strength = 0;
    arrayRow.createRowLowerThan(paramSolverVariable1, paramSolverVariable2, solverVariable, 0);
    if (paramBoolean)
      addSingleError(arrayRow, (int)(-1.0F * arrayRow.variables.get(solverVariable)), 1); 
    addConstraint(arrayRow);
  }
  
  public void addLowerThan(SolverVariable paramSolverVariable1, SolverVariable paramSolverVariable2, int paramInt1, int paramInt2) {
    ArrayRow arrayRow = createRow();
    SolverVariable solverVariable = createSlackVariable();
    solverVariable.strength = 0;
    arrayRow.createRowLowerThan(paramSolverVariable1, paramSolverVariable2, solverVariable, paramInt1);
    if (paramInt2 != 6)
      addSingleError(arrayRow, (int)(-1.0F * arrayRow.variables.get(solverVariable)), paramInt2); 
    addConstraint(arrayRow);
  }
  
  public void addRatio(SolverVariable paramSolverVariable1, SolverVariable paramSolverVariable2, SolverVariable paramSolverVariable3, SolverVariable paramSolverVariable4, float paramFloat, int paramInt) {
    ArrayRow arrayRow = createRow();
    arrayRow.createRowDimensionRatio(paramSolverVariable1, paramSolverVariable2, paramSolverVariable3, paramSolverVariable4, paramFloat);
    if (paramInt != 6)
      arrayRow.addError(this, paramInt); 
    addConstraint(arrayRow);
  }
  
  void addSingleError(ArrayRow paramArrayRow, int paramInt1, int paramInt2) { paramArrayRow.addSingleError(createErrorVariable(paramInt2, null), paramInt1); }
  
  public SolverVariable createErrorVariable(int paramInt, String paramString) {
    Metrics metrics = sMetrics;
    if (metrics != null)
      metrics.errors++; 
    if (this.mNumColumns + 1 >= this.mMaxColumns)
      increaseTableSize(); 
    SolverVariable solverVariable = acquireSolverVariable(SolverVariable.Type.ERROR, paramString);
    this.mVariablesID++;
    this.mNumColumns++;
    solverVariable.id = this.mVariablesID;
    solverVariable.strength = paramInt;
    this.mCache.mIndexedVariables[this.mVariablesID] = solverVariable;
    this.mGoal.addError(solverVariable);
    return solverVariable;
  }
  
  public SolverVariable createExtraVariable() {
    Metrics metrics = sMetrics;
    if (metrics != null)
      metrics.extravariables++; 
    if (this.mNumColumns + 1 >= this.mMaxColumns)
      increaseTableSize(); 
    SolverVariable solverVariable = acquireSolverVariable(SolverVariable.Type.SLACK, null);
    this.mVariablesID++;
    this.mNumColumns++;
    solverVariable.id = this.mVariablesID;
    this.mCache.mIndexedVariables[this.mVariablesID] = solverVariable;
    return solverVariable;
  }
  
  public SolverVariable createObjectVariable(Object paramObject) {
    SolverVariable solverVariable;
    if (paramObject == null)
      return null; 
    if (this.mNumColumns + 1 >= this.mMaxColumns)
      increaseTableSize(); 
    null = null;
    if (paramObject instanceof ConstraintAnchor) {
      null = ((ConstraintAnchor)paramObject).getSolverVariable();
      solverVariable = null;
      if (null == null) {
        ((ConstraintAnchor)paramObject).resetSolverVariable(this.mCache);
        solverVariable = ((ConstraintAnchor)paramObject).getSolverVariable();
      } 
      if (solverVariable.id != -1 && solverVariable.id <= this.mVariablesID) {
        null = solverVariable;
        if (this.mCache.mIndexedVariables[solverVariable.id] == null) {
          if (solverVariable.id != -1)
            solverVariable.reset(); 
          this.mVariablesID++;
          this.mNumColumns++;
          solverVariable.id = this.mVariablesID;
          solverVariable.mType = SolverVariable.Type.UNRESTRICTED;
          this.mCache.mIndexedVariables[this.mVariablesID] = solverVariable;
          return solverVariable;
        } 
        return null;
      } 
    } else {
      return null;
    } 
    if (solverVariable.id != -1)
      solverVariable.reset(); 
    this.mVariablesID++;
    this.mNumColumns++;
    solverVariable.id = this.mVariablesID;
    solverVariable.mType = SolverVariable.Type.UNRESTRICTED;
    this.mCache.mIndexedVariables[this.mVariablesID] = solverVariable;
    return solverVariable;
  }
  
  public ArrayRow createRow() {
    ArrayRow arrayRow = (ArrayRow)this.mCache.arrayRowPool.acquire();
    if (arrayRow == null) {
      arrayRow = new ArrayRow(this.mCache);
    } else {
      arrayRow.reset();
    } 
    SolverVariable.increaseErrorId();
    return arrayRow;
  }
  
  public SolverVariable createSlackVariable() {
    Metrics metrics = sMetrics;
    if (metrics != null)
      metrics.slackvariables++; 
    if (this.mNumColumns + 1 >= this.mMaxColumns)
      increaseTableSize(); 
    SolverVariable solverVariable = acquireSolverVariable(SolverVariable.Type.SLACK, null);
    this.mVariablesID++;
    this.mNumColumns++;
    solverVariable.id = this.mVariablesID;
    this.mCache.mIndexedVariables[this.mVariablesID] = solverVariable;
    return solverVariable;
  }
  
  void displayReadableRows() {
    displaySolverVariables();
    String str1 = " #  ";
    for (byte b = 0; b < this.mNumRows; b++) {
      StringBuilder stringBuilder = new StringBuilder();
      stringBuilder.append(str1);
      stringBuilder.append(this.mRows[b].toReadableString());
      str1 = stringBuilder.toString();
      stringBuilder = new StringBuilder();
      stringBuilder.append(str1);
      stringBuilder.append("\n #  ");
      str1 = stringBuilder.toString();
    } 
    String str2 = str1;
    if (this.mGoal != null) {
      StringBuilder stringBuilder = new StringBuilder();
      stringBuilder.append(str1);
      stringBuilder.append(this.mGoal);
      stringBuilder.append("\n");
      str2 = stringBuilder.toString();
    } 
    System.out.println(str2);
  }
  
  void displaySystemInformations() {
    int i = 0;
    int j = 0;
    while (j < this.TABLE_SIZE) {
      ArrayRow[] arrayOfArrayRow = this.mRows;
      int m = i;
      if (arrayOfArrayRow[j] != null)
        m = i + arrayOfArrayRow[j].sizeInBytes(); 
      j++;
      i = m;
    } 
    j = 0;
    int k = 0;
    while (k < this.mNumRows) {
      ArrayRow[] arrayOfArrayRow = this.mRows;
      int m = j;
      if (arrayOfArrayRow[k] != null)
        m = j + arrayOfArrayRow[k].sizeInBytes(); 
      k++;
      j = m;
    } 
    PrintStream printStream = System.out;
    StringBuilder stringBuilder = new StringBuilder();
    stringBuilder.append("Linear System -> Table size: ");
    stringBuilder.append(this.TABLE_SIZE);
    stringBuilder.append(" (");
    k = this.TABLE_SIZE;
    stringBuilder.append(getDisplaySize(k * k));
    stringBuilder.append(") -- row sizes: ");
    stringBuilder.append(getDisplaySize(i));
    stringBuilder.append(", actual size: ");
    stringBuilder.append(getDisplaySize(j));
    stringBuilder.append(" rows: ");
    stringBuilder.append(this.mNumRows);
    stringBuilder.append("/");
    stringBuilder.append(this.mMaxRows);
    stringBuilder.append(" cols: ");
    stringBuilder.append(this.mNumColumns);
    stringBuilder.append("/");
    stringBuilder.append(this.mMaxColumns);
    stringBuilder.append(" ");
    stringBuilder.append(0);
    stringBuilder.append(" occupied cells, ");
    stringBuilder.append(getDisplaySize(0));
    printStream.println(stringBuilder.toString());
  }
  
  public void displayVariablesReadableRows() {
    displaySolverVariables();
    String str = "";
    byte b = 0;
    while (b < this.mNumRows) {
      String str1 = str;
      if ((this.mRows[b]).variable.mType == SolverVariable.Type.UNRESTRICTED) {
        StringBuilder stringBuilder1 = new StringBuilder();
        stringBuilder1.append(str);
        stringBuilder1.append(this.mRows[b].toReadableString());
        str = stringBuilder1.toString();
        stringBuilder1 = new StringBuilder();
        stringBuilder1.append(str);
        stringBuilder1.append("\n");
        str1 = stringBuilder1.toString();
      } 
      b++;
      str = str1;
    } 
    StringBuilder stringBuilder = new StringBuilder();
    stringBuilder.append(str);
    stringBuilder.append(this.mGoal);
    stringBuilder.append("\n");
    str = stringBuilder.toString();
    System.out.println(str);
  }
  
  public void fillMetrics(Metrics paramMetrics) { sMetrics = paramMetrics; }
  
  public Cache getCache() { return this.mCache; }
  
  Row getGoal() { return this.mGoal; }
  
  public int getMemoryUsed() {
    int i = 0;
    byte b = 0;
    while (b < this.mNumRows) {
      ArrayRow[] arrayOfArrayRow = this.mRows;
      int j = i;
      if (arrayOfArrayRow[b] != null)
        j = i + arrayOfArrayRow[b].sizeInBytes(); 
      b++;
      i = j;
    } 
    return i;
  }
  
  public int getNumEquations() { return this.mNumRows; }
  
  public int getNumVariables() { return this.mVariablesID; }
  
  public int getObjectVariableValue(Object paramObject) {
    paramObject = ((ConstraintAnchor)paramObject).getSolverVariable();
    return (paramObject != null) ? (int)(paramObject.computedValue + 0.5F) : 0;
  }
  
  ArrayRow getRow(int paramInt) { return this.mRows[paramInt]; }
  
  float getValueFor(String paramString) {
    SolverVariable solverVariable = getVariable(paramString, SolverVariable.Type.UNRESTRICTED);
    return (solverVariable == null) ? 0.0F : solverVariable.computedValue;
  }
  
  SolverVariable getVariable(String paramString, SolverVariable.Type paramType) {
    if (this.mVariables == null)
      this.mVariables = new HashMap(); 
    SolverVariable solverVariable2 = (SolverVariable)this.mVariables.get(paramString);
    SolverVariable solverVariable1 = solverVariable2;
    if (solverVariable2 == null)
      solverVariable1 = createVariable(paramString, paramType); 
    return solverVariable1;
  }
  
  public void minimize() {
    Metrics metrics = sMetrics;
    if (metrics != null)
      metrics.minimize++; 
    if (this.graphOptimizer) {
      boolean bool1;
      metrics = sMetrics;
      if (metrics != null)
        metrics.graphOptimizer++; 
      boolean bool2 = true;
      byte b = 0;
      while (true) {
        bool1 = bool2;
        if (b < this.mNumRows) {
          if (!(this.mRows[b]).isSimpleDefinition) {
            bool1 = false;
            break;
          } 
          b++;
          continue;
        } 
        break;
      } 
      if (!bool1) {
        minimizeGoal(this.mGoal);
      } else {
        metrics = sMetrics;
        if (metrics != null)
          metrics.fullySolved++; 
        computeValues();
      } 
      return;
    } 
    minimizeGoal(this.mGoal);
  }
  
  void minimizeGoal(Row paramRow) throws Exception {
    Metrics metrics = sMetrics;
    if (metrics != null) {
      metrics.minimizeGoal++;
      metrics = sMetrics;
      metrics.maxVariables = Math.max(metrics.maxVariables, this.mNumColumns);
      metrics = sMetrics;
      metrics.maxRows = Math.max(metrics.maxRows, this.mNumRows);
    } 
    updateRowFromVariables((ArrayRow)paramRow);
    enforceBFS(paramRow);
    optimize(paramRow, false);
    computeValues();
  }
  
  public void reset() {
    byte b;
    for (b = 0; b < this.mCache.mIndexedVariables.length; b++) {
      SolverVariable solverVariable = this.mCache.mIndexedVariables[b];
      if (solverVariable != null)
        solverVariable.reset(); 
    } 
    this.mCache.solverVariablePool.releaseAll(this.mPoolVariables, this.mPoolVariablesCount);
    this.mPoolVariablesCount = 0;
    Arrays.fill(this.mCache.mIndexedVariables, null);
    HashMap hashMap = this.mVariables;
    if (hashMap != null)
      hashMap.clear(); 
    this.mVariablesID = 0;
    this.mGoal.clear();
    this.mNumColumns = 1;
    for (b = 0; b < this.mNumRows; b++)
      (this.mRows[b]).used = false; 
    releaseRows();
    this.mNumRows = 0;
  }
  
  static interface Row {
    void addError(SolverVariable param1SolverVariable);
    
    void clear();
    
    SolverVariable getKey();
    
    SolverVariable getPivotCandidate(LinearSystem param1LinearSystem, boolean[] param1ArrayOfBoolean);
    
    void initFromRow(Row param1Row) throws Exception;
    
    boolean isEmpty();
  }
}


/* Location:              D:\Apks\New folder\classes-dex2jar.jar!\android\support\constraint\solver\LinearSystem.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.0.7
 */