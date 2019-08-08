package android.support.constraint.solver.widgets;

import android.support.constraint.solver.LinearSystem;
import android.support.constraint.solver.Metrics;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ConstraintWidgetContainer extends WidgetContainer {
  private static final boolean DEBUG = false;
  
  static final boolean DEBUG_GRAPH = false;
  
  private static final boolean DEBUG_LAYOUT = false;
  
  private static final int MAX_ITERATIONS = 8;
  
  private static final boolean USE_SNAPSHOT = true;
  
  int mDebugSolverPassCount = 0;
  
  public boolean mGroupsWrapOptimized = false;
  
  private boolean mHeightMeasuredTooSmall = false;
  
  ChainHead[] mHorizontalChainsArray = new ChainHead[4];
  
  int mHorizontalChainsSize = 0;
  
  public boolean mHorizontalWrapOptimized = false;
  
  private boolean mIsRtl = false;
  
  private int mOptimizationLevel = 7;
  
  int mPaddingBottom;
  
  int mPaddingLeft;
  
  int mPaddingRight;
  
  int mPaddingTop;
  
  public boolean mSkipSolver = false;
  
  private Snapshot mSnapshot;
  
  protected LinearSystem mSystem = new LinearSystem();
  
  ChainHead[] mVerticalChainsArray = new ChainHead[4];
  
  int mVerticalChainsSize = 0;
  
  public boolean mVerticalWrapOptimized = false;
  
  public List<ConstraintWidgetGroup> mWidgetGroups = new ArrayList();
  
  private boolean mWidthMeasuredTooSmall = false;
  
  public int mWrapFixedHeight = 0;
  
  public int mWrapFixedWidth = 0;
  
  public ConstraintWidgetContainer() {}
  
  public ConstraintWidgetContainer(int paramInt1, int paramInt2) { super(paramInt1, paramInt2); }
  
  public ConstraintWidgetContainer(int paramInt1, int paramInt2, int paramInt3, int paramInt4) { super(paramInt1, paramInt2, paramInt3, paramInt4); }
  
  private void addHorizontalChain(ConstraintWidget paramConstraintWidget) {
    int i = this.mHorizontalChainsSize;
    ChainHead[] arrayOfChainHead = this.mHorizontalChainsArray;
    if (i + 1 >= arrayOfChainHead.length)
      this.mHorizontalChainsArray = (ChainHead[])Arrays.copyOf(arrayOfChainHead, arrayOfChainHead.length * 2); 
    this.mHorizontalChainsArray[this.mHorizontalChainsSize] = new ChainHead(paramConstraintWidget, 0, isRtl());
    this.mHorizontalChainsSize++;
  }
  
  private void addVerticalChain(ConstraintWidget paramConstraintWidget) {
    int i = this.mVerticalChainsSize;
    ChainHead[] arrayOfChainHead = this.mVerticalChainsArray;
    if (i + 1 >= arrayOfChainHead.length)
      this.mVerticalChainsArray = (ChainHead[])Arrays.copyOf(arrayOfChainHead, arrayOfChainHead.length * 2); 
    this.mVerticalChainsArray[this.mVerticalChainsSize] = new ChainHead(paramConstraintWidget, 1, isRtl());
    this.mVerticalChainsSize++;
  }
  
  private void resetChains() {
    this.mHorizontalChainsSize = 0;
    this.mVerticalChainsSize = 0;
  }
  
  void addChain(ConstraintWidget paramConstraintWidget, int paramInt) {
    if (paramInt == 0) {
      addHorizontalChain(paramConstraintWidget);
      return;
    } 
    if (paramInt == 1)
      addVerticalChain(paramConstraintWidget); 
  }
  
  public boolean addChildrenToSolver(LinearSystem paramLinearSystem) {
    addToSolver(paramLinearSystem);
    int i = this.mChildren.size();
    for (byte b = 0; b < i; b++) {
      ConstraintWidget constraintWidget = (ConstraintWidget)this.mChildren.get(b);
      if (constraintWidget instanceof ConstraintWidgetContainer) {
        ConstraintWidget.DimensionBehaviour dimensionBehaviour1 = constraintWidget.mListDimensionBehaviors[0];
        ConstraintWidget.DimensionBehaviour dimensionBehaviour2 = constraintWidget.mListDimensionBehaviors[1];
        if (dimensionBehaviour1 == ConstraintWidget.DimensionBehaviour.WRAP_CONTENT)
          constraintWidget.setHorizontalDimensionBehaviour(ConstraintWidget.DimensionBehaviour.FIXED); 
        if (dimensionBehaviour2 == ConstraintWidget.DimensionBehaviour.WRAP_CONTENT)
          constraintWidget.setVerticalDimensionBehaviour(ConstraintWidget.DimensionBehaviour.FIXED); 
        constraintWidget.addToSolver(paramLinearSystem);
        if (dimensionBehaviour1 == ConstraintWidget.DimensionBehaviour.WRAP_CONTENT)
          constraintWidget.setHorizontalDimensionBehaviour(dimensionBehaviour1); 
        if (dimensionBehaviour2 == ConstraintWidget.DimensionBehaviour.WRAP_CONTENT)
          constraintWidget.setVerticalDimensionBehaviour(dimensionBehaviour2); 
      } else {
        Optimizer.checkMatchParent(this, paramLinearSystem, constraintWidget);
        constraintWidget.addToSolver(paramLinearSystem);
      } 
    } 
    if (this.mHorizontalChainsSize > 0)
      Chain.applyChainConstraints(this, paramLinearSystem, 0); 
    if (this.mVerticalChainsSize > 0)
      Chain.applyChainConstraints(this, paramLinearSystem, 1); 
    return true;
  }
  
  public void analyze(int paramInt) {
    super.analyze(paramInt);
    int i = this.mChildren.size();
    for (byte b = 0; b < i; b++)
      ((ConstraintWidget)this.mChildren.get(b)).analyze(paramInt); 
  }
  
  public void fillMetrics(Metrics paramMetrics) { this.mSystem.fillMetrics(paramMetrics); }
  
  public ArrayList<Guideline> getHorizontalGuidelines() {
    ArrayList arrayList = new ArrayList();
    byte b = 0;
    int i = this.mChildren.size();
    while (b < i) {
      ConstraintWidget constraintWidget = (ConstraintWidget)this.mChildren.get(b);
      if (constraintWidget instanceof Guideline) {
        constraintWidget = (Guideline)constraintWidget;
        if (constraintWidget.getOrientation() == 0)
          arrayList.add(constraintWidget); 
      } 
      b++;
    } 
    return arrayList;
  }
  
  public int getOptimizationLevel() { return this.mOptimizationLevel; }
  
  public LinearSystem getSystem() { return this.mSystem; }
  
  public String getType() { return "ConstraintLayout"; }
  
  public ArrayList<Guideline> getVerticalGuidelines() {
    ArrayList arrayList = new ArrayList();
    byte b = 0;
    int i = this.mChildren.size();
    while (b < i) {
      ConstraintWidget constraintWidget = (ConstraintWidget)this.mChildren.get(b);
      if (constraintWidget instanceof Guideline) {
        constraintWidget = (Guideline)constraintWidget;
        if (constraintWidget.getOrientation() == 1)
          arrayList.add(constraintWidget); 
      } 
      b++;
    } 
    return arrayList;
  }
  
  public List<ConstraintWidgetGroup> getWidgetGroups() { return this.mWidgetGroups; }
  
  public boolean handlesInternalConstraints() { return false; }
  
  public boolean isHeightMeasuredTooSmall() { return this.mHeightMeasuredTooSmall; }
  
  public boolean isRtl() { return this.mIsRtl; }
  
  public boolean isWidthMeasuredTooSmall() { return this.mWidthMeasuredTooSmall; }
  
  public void layout() { // Byte code:
    //   0: aload_0
    //   1: getfield mX : I
    //   4: istore #10
    //   6: aload_0
    //   7: getfield mY : I
    //   10: istore #11
    //   12: iconst_0
    //   13: aload_0
    //   14: invokevirtual getWidth : ()I
    //   17: invokestatic max : (II)I
    //   20: istore #12
    //   22: iconst_0
    //   23: aload_0
    //   24: invokevirtual getHeight : ()I
    //   27: invokestatic max : (II)I
    //   30: istore #13
    //   32: aload_0
    //   33: iconst_0
    //   34: putfield mWidthMeasuredTooSmall : Z
    //   37: aload_0
    //   38: iconst_0
    //   39: putfield mHeightMeasuredTooSmall : Z
    //   42: aload_0
    //   43: getfield mParent : Landroid/support/constraint/solver/widgets/ConstraintWidget;
    //   46: ifnull -> 110
    //   49: aload_0
    //   50: getfield mSnapshot : Landroid/support/constraint/solver/widgets/Snapshot;
    //   53: ifnonnull -> 68
    //   56: aload_0
    //   57: new android/support/constraint/solver/widgets/Snapshot
    //   60: dup
    //   61: aload_0
    //   62: invokespecial <init> : (Landroid/support/constraint/solver/widgets/ConstraintWidget;)V
    //   65: putfield mSnapshot : Landroid/support/constraint/solver/widgets/Snapshot;
    //   68: aload_0
    //   69: getfield mSnapshot : Landroid/support/constraint/solver/widgets/Snapshot;
    //   72: aload_0
    //   73: invokevirtual updateFrom : (Landroid/support/constraint/solver/widgets/ConstraintWidget;)V
    //   76: aload_0
    //   77: aload_0
    //   78: getfield mPaddingLeft : I
    //   81: invokevirtual setX : (I)V
    //   84: aload_0
    //   85: aload_0
    //   86: getfield mPaddingTop : I
    //   89: invokevirtual setY : (I)V
    //   92: aload_0
    //   93: invokevirtual resetAnchors : ()V
    //   96: aload_0
    //   97: aload_0
    //   98: getfield mSystem : Landroid/support/constraint/solver/LinearSystem;
    //   101: invokevirtual getCache : ()Landroid/support/constraint/solver/Cache;
    //   104: invokevirtual resetSolverVariables : (Landroid/support/constraint/solver/Cache;)V
    //   107: goto -> 120
    //   110: aload_0
    //   111: iconst_0
    //   112: putfield mX : I
    //   115: aload_0
    //   116: iconst_0
    //   117: putfield mY : I
    //   120: aload_0
    //   121: getfield mOptimizationLevel : I
    //   124: ifeq -> 164
    //   127: aload_0
    //   128: bipush #8
    //   130: invokevirtual optimizeFor : (I)Z
    //   133: ifne -> 140
    //   136: aload_0
    //   137: invokevirtual optimizeReset : ()V
    //   140: aload_0
    //   141: bipush #32
    //   143: invokevirtual optimizeFor : (I)Z
    //   146: ifne -> 153
    //   149: aload_0
    //   150: invokevirtual optimize : ()V
    //   153: aload_0
    //   154: getfield mSystem : Landroid/support/constraint/solver/LinearSystem;
    //   157: iconst_1
    //   158: putfield graphOptimizer : Z
    //   161: goto -> 172
    //   164: aload_0
    //   165: getfield mSystem : Landroid/support/constraint/solver/LinearSystem;
    //   168: iconst_0
    //   169: putfield graphOptimizer : Z
    //   172: iconst_0
    //   173: istore_2
    //   174: aload_0
    //   175: getfield mListDimensionBehaviors : [Landroid/support/constraint/solver/widgets/ConstraintWidget$DimensionBehaviour;
    //   178: iconst_1
    //   179: aaload
    //   180: astore #17
    //   182: aload_0
    //   183: getfield mListDimensionBehaviors : [Landroid/support/constraint/solver/widgets/ConstraintWidget$DimensionBehaviour;
    //   186: iconst_0
    //   187: aaload
    //   188: astore #18
    //   190: aload_0
    //   191: invokespecial resetChains : ()V
    //   194: aload_0
    //   195: getfield mWidgetGroups : Ljava/util/List;
    //   198: invokeinterface size : ()I
    //   203: ifne -> 236
    //   206: aload_0
    //   207: getfield mWidgetGroups : Ljava/util/List;
    //   210: invokeinterface clear : ()V
    //   215: aload_0
    //   216: getfield mWidgetGroups : Ljava/util/List;
    //   219: iconst_0
    //   220: new android/support/constraint/solver/widgets/ConstraintWidgetGroup
    //   223: dup
    //   224: aload_0
    //   225: getfield mChildren : Ljava/util/ArrayList;
    //   228: invokespecial <init> : (Ljava/util/List;)V
    //   231: invokeinterface add : (ILjava/lang/Object;)V
    //   236: aload_0
    //   237: getfield mWidgetGroups : Ljava/util/List;
    //   240: invokeinterface size : ()I
    //   245: istore_1
    //   246: aload_0
    //   247: getfield mChildren : Ljava/util/ArrayList;
    //   250: astore #19
    //   252: aload_0
    //   253: invokevirtual getHorizontalDimensionBehaviour : ()Landroid/support/constraint/solver/widgets/ConstraintWidget$DimensionBehaviour;
    //   256: getstatic android/support/constraint/solver/widgets/ConstraintWidget$DimensionBehaviour.WRAP_CONTENT : Landroid/support/constraint/solver/widgets/ConstraintWidget$DimensionBehaviour;
    //   259: if_acmpeq -> 281
    //   262: aload_0
    //   263: invokevirtual getVerticalDimensionBehaviour : ()Landroid/support/constraint/solver/widgets/ConstraintWidget$DimensionBehaviour;
    //   266: getstatic android/support/constraint/solver/widgets/ConstraintWidget$DimensionBehaviour.WRAP_CONTENT : Landroid/support/constraint/solver/widgets/ConstraintWidget$DimensionBehaviour;
    //   269: if_acmpne -> 275
    //   272: goto -> 281
    //   275: iconst_0
    //   276: istore #5
    //   278: goto -> 284
    //   281: iconst_1
    //   282: istore #5
    //   284: iconst_0
    //   285: istore #6
    //   287: iload #6
    //   289: iload_1
    //   290: if_icmpge -> 1285
    //   293: aload_0
    //   294: getfield mSkipSolver : Z
    //   297: ifne -> 1285
    //   300: aload_0
    //   301: getfield mWidgetGroups : Ljava/util/List;
    //   304: iload #6
    //   306: invokeinterface get : (I)Ljava/lang/Object;
    //   311: checkcast android/support/constraint/solver/widgets/ConstraintWidgetGroup
    //   314: getfield mSkipSolver : Z
    //   317: ifeq -> 323
    //   320: goto -> 1276
    //   323: aload_0
    //   324: bipush #32
    //   326: invokevirtual optimizeFor : (I)Z
    //   329: ifeq -> 403
    //   332: aload_0
    //   333: invokevirtual getHorizontalDimensionBehaviour : ()Landroid/support/constraint/solver/widgets/ConstraintWidget$DimensionBehaviour;
    //   336: getstatic android/support/constraint/solver/widgets/ConstraintWidget$DimensionBehaviour.FIXED : Landroid/support/constraint/solver/widgets/ConstraintWidget$DimensionBehaviour;
    //   339: if_acmpne -> 379
    //   342: aload_0
    //   343: invokevirtual getVerticalDimensionBehaviour : ()Landroid/support/constraint/solver/widgets/ConstraintWidget$DimensionBehaviour;
    //   346: getstatic android/support/constraint/solver/widgets/ConstraintWidget$DimensionBehaviour.FIXED : Landroid/support/constraint/solver/widgets/ConstraintWidget$DimensionBehaviour;
    //   349: if_acmpne -> 379
    //   352: aload_0
    //   353: aload_0
    //   354: getfield mWidgetGroups : Ljava/util/List;
    //   357: iload #6
    //   359: invokeinterface get : (I)Ljava/lang/Object;
    //   364: checkcast android/support/constraint/solver/widgets/ConstraintWidgetGroup
    //   367: invokevirtual getWidgetsToSolve : ()Ljava/util/List;
    //   370: checkcast java/util/ArrayList
    //   373: putfield mChildren : Ljava/util/ArrayList;
    //   376: goto -> 403
    //   379: aload_0
    //   380: aload_0
    //   381: getfield mWidgetGroups : Ljava/util/List;
    //   384: iload #6
    //   386: invokeinterface get : (I)Ljava/lang/Object;
    //   391: checkcast android/support/constraint/solver/widgets/ConstraintWidgetGroup
    //   394: getfield mConstrainedGroup : Ljava/util/List;
    //   397: checkcast java/util/ArrayList
    //   400: putfield mChildren : Ljava/util/ArrayList;
    //   403: aload_0
    //   404: invokespecial resetChains : ()V
    //   407: aload_0
    //   408: getfield mChildren : Ljava/util/ArrayList;
    //   411: invokevirtual size : ()I
    //   414: istore #7
    //   416: iconst_0
    //   417: istore_3
    //   418: iconst_0
    //   419: istore #4
    //   421: iload #4
    //   423: iload #7
    //   425: if_icmpge -> 467
    //   428: aload_0
    //   429: getfield mChildren : Ljava/util/ArrayList;
    //   432: iload #4
    //   434: invokevirtual get : (I)Ljava/lang/Object;
    //   437: checkcast android/support/constraint/solver/widgets/ConstraintWidget
    //   440: astore #16
    //   442: aload #16
    //   444: instanceof android/support/constraint/solver/widgets/WidgetContainer
    //   447: ifeq -> 458
    //   450: aload #16
    //   452: checkcast android/support/constraint/solver/widgets/WidgetContainer
    //   455: invokevirtual layout : ()V
    //   458: iload #4
    //   460: iconst_1
    //   461: iadd
    //   462: istore #4
    //   464: goto -> 421
    //   467: iconst_1
    //   468: istore #14
    //   470: iload_3
    //   471: istore #4
    //   473: iload_1
    //   474: istore_3
    //   475: iload_2
    //   476: istore_1
    //   477: iload #7
    //   479: istore_2
    //   480: iload #14
    //   482: ifeq -> 1255
    //   485: iload #4
    //   487: iconst_1
    //   488: iadd
    //   489: istore #9
    //   491: aload_0
    //   492: getfield mSystem : Landroid/support/constraint/solver/LinearSystem;
    //   495: invokevirtual reset : ()V
    //   498: aload_0
    //   499: invokespecial resetChains : ()V
    //   502: aload_0
    //   503: aload_0
    //   504: getfield mSystem : Landroid/support/constraint/solver/LinearSystem;
    //   507: invokevirtual createObjectVariables : (Landroid/support/constraint/solver/LinearSystem;)V
    //   510: iconst_0
    //   511: istore #4
    //   513: iload #4
    //   515: iload_2
    //   516: if_icmpge -> 559
    //   519: iload #14
    //   521: istore #15
    //   523: aload_0
    //   524: getfield mChildren : Ljava/util/ArrayList;
    //   527: iload #4
    //   529: invokevirtual get : (I)Ljava/lang/Object;
    //   532: checkcast android/support/constraint/solver/widgets/ConstraintWidget
    //   535: astore #16
    //   537: aload #16
    //   539: aload_0
    //   540: getfield mSystem : Landroid/support/constraint/solver/LinearSystem;
    //   543: invokevirtual createObjectVariables : (Landroid/support/constraint/solver/LinearSystem;)V
    //   546: iload #4
    //   548: iconst_1
    //   549: iadd
    //   550: istore #4
    //   552: iload #15
    //   554: istore #14
    //   556: goto -> 513
    //   559: iload_1
    //   560: istore #4
    //   562: aload_0
    //   563: aload_0
    //   564: getfield mSystem : Landroid/support/constraint/solver/LinearSystem;
    //   567: invokevirtual addChildrenToSolver : (Landroid/support/constraint/solver/LinearSystem;)Z
    //   570: istore #15
    //   572: iload #15
    //   574: istore #14
    //   576: iload #14
    //   578: ifeq -> 599
    //   581: aload_0
    //   582: getfield mSystem : Landroid/support/constraint/solver/LinearSystem;
    //   585: invokevirtual minimize : ()V
    //   588: goto -> 599
    //   591: astore #16
    //   593: iload #4
    //   595: istore_1
    //   596: goto -> 612
    //   599: iload #4
    //   601: istore_1
    //   602: goto -> 658
    //   605: astore #16
    //   607: goto -> 612
    //   610: astore #16
    //   612: aload #16
    //   614: invokevirtual printStackTrace : ()V
    //   617: getstatic java/lang/System.out : Ljava/io/PrintStream;
    //   620: astore #20
    //   622: new java/lang/StringBuilder
    //   625: dup
    //   626: invokespecial <init> : ()V
    //   629: astore #21
    //   631: aload #21
    //   633: ldc_w 'EXCEPTION : '
    //   636: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   639: pop
    //   640: aload #21
    //   642: aload #16
    //   644: invokevirtual append : (Ljava/lang/Object;)Ljava/lang/StringBuilder;
    //   647: pop
    //   648: aload #20
    //   650: aload #21
    //   652: invokevirtual toString : ()Ljava/lang/String;
    //   655: invokevirtual println : (Ljava/lang/String;)V
    //   658: iload #14
    //   660: ifeq -> 677
    //   663: aload_0
    //   664: aload_0
    //   665: getfield mSystem : Landroid/support/constraint/solver/LinearSystem;
    //   668: getstatic android/support/constraint/solver/widgets/Optimizer.flags : [Z
    //   671: invokevirtual updateChildrenFromSolver : (Landroid/support/constraint/solver/LinearSystem;[Z)V
    //   674: goto -> 790
    //   677: aload_0
    //   678: aload_0
    //   679: getfield mSystem : Landroid/support/constraint/solver/LinearSystem;
    //   682: invokevirtual updateFromSolver : (Landroid/support/constraint/solver/LinearSystem;)V
    //   685: iconst_0
    //   686: istore #4
    //   688: iload #4
    //   690: iload_2
    //   691: if_icmpge -> 790
    //   694: aload_0
    //   695: getfield mChildren : Ljava/util/ArrayList;
    //   698: iload #4
    //   700: invokevirtual get : (I)Ljava/lang/Object;
    //   703: checkcast android/support/constraint/solver/widgets/ConstraintWidget
    //   706: astore #16
    //   708: aload #16
    //   710: getfield mListDimensionBehaviors : [Landroid/support/constraint/solver/widgets/ConstraintWidget$DimensionBehaviour;
    //   713: iconst_0
    //   714: aaload
    //   715: getstatic android/support/constraint/solver/widgets/ConstraintWidget$DimensionBehaviour.MATCH_CONSTRAINT : Landroid/support/constraint/solver/widgets/ConstraintWidget$DimensionBehaviour;
    //   718: if_acmpne -> 746
    //   721: aload #16
    //   723: invokevirtual getWidth : ()I
    //   726: aload #16
    //   728: invokevirtual getWrapWidth : ()I
    //   731: if_icmpge -> 743
    //   734: getstatic android/support/constraint/solver/widgets/Optimizer.flags : [Z
    //   737: iconst_2
    //   738: iconst_1
    //   739: bastore
    //   740: goto -> 790
    //   743: goto -> 746
    //   746: aload #16
    //   748: getfield mListDimensionBehaviors : [Landroid/support/constraint/solver/widgets/ConstraintWidget$DimensionBehaviour;
    //   751: iconst_1
    //   752: aaload
    //   753: getstatic android/support/constraint/solver/widgets/ConstraintWidget$DimensionBehaviour.MATCH_CONSTRAINT : Landroid/support/constraint/solver/widgets/ConstraintWidget$DimensionBehaviour;
    //   756: if_acmpne -> 781
    //   759: aload #16
    //   761: invokevirtual getHeight : ()I
    //   764: aload #16
    //   766: invokevirtual getWrapHeight : ()I
    //   769: if_icmpge -> 781
    //   772: getstatic android/support/constraint/solver/widgets/Optimizer.flags : [Z
    //   775: iconst_2
    //   776: iconst_1
    //   777: bastore
    //   778: goto -> 790
    //   781: iload #4
    //   783: iconst_1
    //   784: iadd
    //   785: istore #4
    //   787: goto -> 688
    //   790: iconst_0
    //   791: istore #14
    //   793: iload #5
    //   795: ifeq -> 1020
    //   798: iload #9
    //   800: bipush #8
    //   802: if_icmpge -> 1020
    //   805: getstatic android/support/constraint/solver/widgets/Optimizer.flags : [Z
    //   808: iconst_2
    //   809: baload
    //   810: ifeq -> 1020
    //   813: iconst_0
    //   814: istore #4
    //   816: iconst_0
    //   817: istore #8
    //   819: iconst_0
    //   820: istore #7
    //   822: iload #7
    //   824: iload_2
    //   825: if_icmpge -> 887
    //   828: aload_0
    //   829: getfield mChildren : Ljava/util/ArrayList;
    //   832: iload #7
    //   834: invokevirtual get : (I)Ljava/lang/Object;
    //   837: checkcast android/support/constraint/solver/widgets/ConstraintWidget
    //   840: astore #16
    //   842: iload #8
    //   844: aload #16
    //   846: getfield mX : I
    //   849: aload #16
    //   851: invokevirtual getWidth : ()I
    //   854: iadd
    //   855: invokestatic max : (II)I
    //   858: istore #8
    //   860: iload #4
    //   862: aload #16
    //   864: getfield mY : I
    //   867: aload #16
    //   869: invokevirtual getHeight : ()I
    //   872: iadd
    //   873: invokestatic max : (II)I
    //   876: istore #4
    //   878: iload #7
    //   880: iconst_1
    //   881: iadd
    //   882: istore #7
    //   884: goto -> 822
    //   887: iload_2
    //   888: istore #7
    //   890: aload_0
    //   891: getfield mMinWidth : I
    //   894: iload #8
    //   896: invokestatic max : (II)I
    //   899: istore_2
    //   900: aload_0
    //   901: getfield mMinHeight : I
    //   904: iload #4
    //   906: invokestatic max : (II)I
    //   909: istore #8
    //   911: aload #18
    //   913: getstatic android/support/constraint/solver/widgets/ConstraintWidget$DimensionBehaviour.WRAP_CONTENT : Landroid/support/constraint/solver/widgets/ConstraintWidget$DimensionBehaviour;
    //   916: if_acmpne -> 950
    //   919: aload_0
    //   920: invokevirtual getWidth : ()I
    //   923: iload_2
    //   924: if_icmpge -> 950
    //   927: aload_0
    //   928: iload_2
    //   929: invokevirtual setWidth : (I)V
    //   932: aload_0
    //   933: getfield mListDimensionBehaviors : [Landroid/support/constraint/solver/widgets/ConstraintWidget$DimensionBehaviour;
    //   936: iconst_0
    //   937: getstatic android/support/constraint/solver/widgets/ConstraintWidget$DimensionBehaviour.WRAP_CONTENT : Landroid/support/constraint/solver/widgets/ConstraintWidget$DimensionBehaviour;
    //   940: aastore
    //   941: iconst_1
    //   942: istore #4
    //   944: iconst_1
    //   945: istore #15
    //   947: goto -> 957
    //   950: iload #14
    //   952: istore #15
    //   954: iload_1
    //   955: istore #4
    //   957: iload #4
    //   959: istore_1
    //   960: iload #15
    //   962: istore #14
    //   964: iload #7
    //   966: istore_2
    //   967: aload #17
    //   969: getstatic android/support/constraint/solver/widgets/ConstraintWidget$DimensionBehaviour.WRAP_CONTENT : Landroid/support/constraint/solver/widgets/ConstraintWidget$DimensionBehaviour;
    //   972: if_acmpne -> 1023
    //   975: iload #4
    //   977: istore_1
    //   978: iload #15
    //   980: istore #14
    //   982: iload #7
    //   984: istore_2
    //   985: aload_0
    //   986: invokevirtual getHeight : ()I
    //   989: iload #8
    //   991: if_icmpge -> 1023
    //   994: aload_0
    //   995: iload #8
    //   997: invokevirtual setHeight : (I)V
    //   1000: aload_0
    //   1001: getfield mListDimensionBehaviors : [Landroid/support/constraint/solver/widgets/ConstraintWidget$DimensionBehaviour;
    //   1004: iconst_1
    //   1005: getstatic android/support/constraint/solver/widgets/ConstraintWidget$DimensionBehaviour.WRAP_CONTENT : Landroid/support/constraint/solver/widgets/ConstraintWidget$DimensionBehaviour;
    //   1008: aastore
    //   1009: iconst_1
    //   1010: istore_1
    //   1011: iconst_1
    //   1012: istore #14
    //   1014: iload #7
    //   1016: istore_2
    //   1017: goto -> 1023
    //   1020: iconst_0
    //   1021: istore #14
    //   1023: aload_0
    //   1024: getfield mMinWidth : I
    //   1027: aload_0
    //   1028: invokevirtual getWidth : ()I
    //   1031: invokestatic max : (II)I
    //   1034: istore #4
    //   1036: iload #4
    //   1038: aload_0
    //   1039: invokevirtual getWidth : ()I
    //   1042: if_icmple -> 1065
    //   1045: aload_0
    //   1046: iload #4
    //   1048: invokevirtual setWidth : (I)V
    //   1051: aload_0
    //   1052: getfield mListDimensionBehaviors : [Landroid/support/constraint/solver/widgets/ConstraintWidget$DimensionBehaviour;
    //   1055: iconst_0
    //   1056: getstatic android/support/constraint/solver/widgets/ConstraintWidget$DimensionBehaviour.FIXED : Landroid/support/constraint/solver/widgets/ConstraintWidget$DimensionBehaviour;
    //   1059: aastore
    //   1060: iconst_1
    //   1061: istore_1
    //   1062: iconst_1
    //   1063: istore #14
    //   1065: aload_0
    //   1066: getfield mMinHeight : I
    //   1069: aload_0
    //   1070: invokevirtual getHeight : ()I
    //   1073: invokestatic max : (II)I
    //   1076: istore #4
    //   1078: iload #4
    //   1080: aload_0
    //   1081: invokevirtual getHeight : ()I
    //   1084: if_icmple -> 1107
    //   1087: aload_0
    //   1088: iload #4
    //   1090: invokevirtual setHeight : (I)V
    //   1093: aload_0
    //   1094: getfield mListDimensionBehaviors : [Landroid/support/constraint/solver/widgets/ConstraintWidget$DimensionBehaviour;
    //   1097: iconst_1
    //   1098: getstatic android/support/constraint/solver/widgets/ConstraintWidget$DimensionBehaviour.FIXED : Landroid/support/constraint/solver/widgets/ConstraintWidget$DimensionBehaviour;
    //   1101: aastore
    //   1102: iconst_1
    //   1103: istore_1
    //   1104: iconst_1
    //   1105: istore #14
    //   1107: iload_1
    //   1108: ifne -> 1248
    //   1111: iload_1
    //   1112: istore #4
    //   1114: iload #14
    //   1116: istore #15
    //   1118: aload_0
    //   1119: getfield mListDimensionBehaviors : [Landroid/support/constraint/solver/widgets/ConstraintWidget$DimensionBehaviour;
    //   1122: iconst_0
    //   1123: aaload
    //   1124: getstatic android/support/constraint/solver/widgets/ConstraintWidget$DimensionBehaviour.WRAP_CONTENT : Landroid/support/constraint/solver/widgets/ConstraintWidget$DimensionBehaviour;
    //   1127: if_acmpne -> 1184
    //   1130: iload_1
    //   1131: istore #4
    //   1133: iload #14
    //   1135: istore #15
    //   1137: iload #12
    //   1139: ifle -> 1184
    //   1142: iload_1
    //   1143: istore #4
    //   1145: iload #14
    //   1147: istore #15
    //   1149: aload_0
    //   1150: invokevirtual getWidth : ()I
    //   1153: iload #12
    //   1155: if_icmple -> 1184
    //   1158: aload_0
    //   1159: iconst_1
    //   1160: putfield mWidthMeasuredTooSmall : Z
    //   1163: iconst_1
    //   1164: istore #4
    //   1166: aload_0
    //   1167: getfield mListDimensionBehaviors : [Landroid/support/constraint/solver/widgets/ConstraintWidget$DimensionBehaviour;
    //   1170: iconst_0
    //   1171: getstatic android/support/constraint/solver/widgets/ConstraintWidget$DimensionBehaviour.FIXED : Landroid/support/constraint/solver/widgets/ConstraintWidget$DimensionBehaviour;
    //   1174: aastore
    //   1175: aload_0
    //   1176: iload #12
    //   1178: invokevirtual setWidth : (I)V
    //   1181: iconst_1
    //   1182: istore #15
    //   1184: aload_0
    //   1185: getfield mListDimensionBehaviors : [Landroid/support/constraint/solver/widgets/ConstraintWidget$DimensionBehaviour;
    //   1188: iconst_1
    //   1189: aaload
    //   1190: getstatic android/support/constraint/solver/widgets/ConstraintWidget$DimensionBehaviour.WRAP_CONTENT : Landroid/support/constraint/solver/widgets/ConstraintWidget$DimensionBehaviour;
    //   1193: if_acmpne -> 1238
    //   1196: iload #13
    //   1198: ifle -> 1238
    //   1201: aload_0
    //   1202: invokevirtual getHeight : ()I
    //   1205: iload #13
    //   1207: if_icmple -> 1238
    //   1210: aload_0
    //   1211: iconst_1
    //   1212: putfield mHeightMeasuredTooSmall : Z
    //   1215: iconst_1
    //   1216: istore_1
    //   1217: aload_0
    //   1218: getfield mListDimensionBehaviors : [Landroid/support/constraint/solver/widgets/ConstraintWidget$DimensionBehaviour;
    //   1221: iconst_1
    //   1222: getstatic android/support/constraint/solver/widgets/ConstraintWidget$DimensionBehaviour.FIXED : Landroid/support/constraint/solver/widgets/ConstraintWidget$DimensionBehaviour;
    //   1225: aastore
    //   1226: aload_0
    //   1227: iload #13
    //   1229: invokevirtual setHeight : (I)V
    //   1232: iconst_1
    //   1233: istore #14
    //   1235: goto -> 1248
    //   1238: iload #15
    //   1240: istore #14
    //   1242: iload #4
    //   1244: istore_1
    //   1245: goto -> 1248
    //   1248: iload #9
    //   1250: istore #4
    //   1252: goto -> 480
    //   1255: aload_0
    //   1256: getfield mWidgetGroups : Ljava/util/List;
    //   1259: iload #6
    //   1261: invokeinterface get : (I)Ljava/lang/Object;
    //   1266: checkcast android/support/constraint/solver/widgets/ConstraintWidgetGroup
    //   1269: invokevirtual updateUnresolvedWidgets : ()V
    //   1272: iload_1
    //   1273: istore_2
    //   1274: iload_3
    //   1275: istore_1
    //   1276: iload #6
    //   1278: iconst_1
    //   1279: iadd
    //   1280: istore #6
    //   1282: goto -> 287
    //   1285: aload_0
    //   1286: aload #19
    //   1288: checkcast java/util/ArrayList
    //   1291: putfield mChildren : Ljava/util/ArrayList;
    //   1294: aload_0
    //   1295: getfield mParent : Landroid/support/constraint/solver/widgets/ConstraintWidget;
    //   1298: ifnull -> 1366
    //   1301: aload_0
    //   1302: getfield mMinWidth : I
    //   1305: aload_0
    //   1306: invokevirtual getWidth : ()I
    //   1309: invokestatic max : (II)I
    //   1312: istore_1
    //   1313: aload_0
    //   1314: getfield mMinHeight : I
    //   1317: aload_0
    //   1318: invokevirtual getHeight : ()I
    //   1321: invokestatic max : (II)I
    //   1324: istore_3
    //   1325: aload_0
    //   1326: getfield mSnapshot : Landroid/support/constraint/solver/widgets/Snapshot;
    //   1329: aload_0
    //   1330: invokevirtual applyTo : (Landroid/support/constraint/solver/widgets/ConstraintWidget;)V
    //   1333: aload_0
    //   1334: aload_0
    //   1335: getfield mPaddingLeft : I
    //   1338: iload_1
    //   1339: iadd
    //   1340: aload_0
    //   1341: getfield mPaddingRight : I
    //   1344: iadd
    //   1345: invokevirtual setWidth : (I)V
    //   1348: aload_0
    //   1349: aload_0
    //   1350: getfield mPaddingTop : I
    //   1353: iload_3
    //   1354: iadd
    //   1355: aload_0
    //   1356: getfield mPaddingBottom : I
    //   1359: iadd
    //   1360: invokevirtual setHeight : (I)V
    //   1363: goto -> 1378
    //   1366: aload_0
    //   1367: iload #10
    //   1369: putfield mX : I
    //   1372: aload_0
    //   1373: iload #11
    //   1375: putfield mY : I
    //   1378: iload_2
    //   1379: ifeq -> 1398
    //   1382: aload_0
    //   1383: getfield mListDimensionBehaviors : [Landroid/support/constraint/solver/widgets/ConstraintWidget$DimensionBehaviour;
    //   1386: iconst_0
    //   1387: aload #18
    //   1389: aastore
    //   1390: aload_0
    //   1391: getfield mListDimensionBehaviors : [Landroid/support/constraint/solver/widgets/ConstraintWidget$DimensionBehaviour;
    //   1394: iconst_1
    //   1395: aload #17
    //   1397: aastore
    //   1398: aload_0
    //   1399: aload_0
    //   1400: getfield mSystem : Landroid/support/constraint/solver/LinearSystem;
    //   1403: invokevirtual getCache : ()Landroid/support/constraint/solver/Cache;
    //   1406: invokevirtual resetSolverVariables : (Landroid/support/constraint/solver/Cache;)V
    //   1409: aload_0
    //   1410: aload_0
    //   1411: invokevirtual getRootConstraintContainer : ()Landroid/support/constraint/solver/widgets/ConstraintWidgetContainer;
    //   1414: if_acmpne -> 1421
    //   1417: aload_0
    //   1418: invokevirtual updateDrawPosition : ()V
    //   1421: return
    //   1422: astore #16
    //   1424: iload #15
    //   1426: istore #14
    //   1428: goto -> 612
    // Exception table:
    //   from	to	target	type
    //   491	510	610	java/lang/Exception
    //   523	537	1422	java/lang/Exception
    //   537	546	605	java/lang/Exception
    //   562	572	605	java/lang/Exception
    //   581	588	591	java/lang/Exception }
  
  public void optimize() {
    if (!optimizeFor(8))
      analyze(this.mOptimizationLevel); 
    solveGraph();
  }
  
  public boolean optimizeFor(int paramInt) { return ((this.mOptimizationLevel & paramInt) == paramInt); }
  
  public void optimizeForDimensions(int paramInt1, int paramInt2) {
    if (this.mListDimensionBehaviors[false] != ConstraintWidget.DimensionBehaviour.WRAP_CONTENT && this.mResolutionWidth != null)
      this.mResolutionWidth.resolve(paramInt1); 
    if (this.mListDimensionBehaviors[true] != ConstraintWidget.DimensionBehaviour.WRAP_CONTENT && this.mResolutionHeight != null)
      this.mResolutionHeight.resolve(paramInt2); 
  }
  
  public void optimizeReset() {
    int i = this.mChildren.size();
    resetResolutionNodes();
    for (byte b = 0; b < i; b++)
      ((ConstraintWidget)this.mChildren.get(b)).resetResolutionNodes(); 
  }
  
  public void preOptimize() {
    optimizeReset();
    analyze(this.mOptimizationLevel);
  }
  
  public void reset() {
    this.mSystem.reset();
    this.mPaddingLeft = 0;
    this.mPaddingRight = 0;
    this.mPaddingTop = 0;
    this.mPaddingBottom = 0;
    this.mWidgetGroups.clear();
    this.mSkipSolver = false;
    super.reset();
  }
  
  public void resetGraph() {
    ResolutionAnchor resolutionAnchor1 = getAnchor(ConstraintAnchor.Type.LEFT).getResolutionNode();
    ResolutionAnchor resolutionAnchor2 = getAnchor(ConstraintAnchor.Type.TOP).getResolutionNode();
    resolutionAnchor1.invalidateAnchors();
    resolutionAnchor2.invalidateAnchors();
    resolutionAnchor1.resolve(null, 0.0F);
    resolutionAnchor2.resolve(null, 0.0F);
  }
  
  public void setOptimizationLevel(int paramInt) { this.mOptimizationLevel = paramInt; }
  
  public void setPadding(int paramInt1, int paramInt2, int paramInt3, int paramInt4) {
    this.mPaddingLeft = paramInt1;
    this.mPaddingTop = paramInt2;
    this.mPaddingRight = paramInt3;
    this.mPaddingBottom = paramInt4;
  }
  
  public void setRtl(boolean paramBoolean) { this.mIsRtl = paramBoolean; }
  
  public void solveGraph() {
    ResolutionAnchor resolutionAnchor1 = getAnchor(ConstraintAnchor.Type.LEFT).getResolutionNode();
    ResolutionAnchor resolutionAnchor2 = getAnchor(ConstraintAnchor.Type.TOP).getResolutionNode();
    resolutionAnchor1.resolve(null, 0.0F);
    resolutionAnchor2.resolve(null, 0.0F);
  }
  
  public void updateChildrenFromSolver(LinearSystem paramLinearSystem, boolean[] paramArrayOfBoolean) {
    paramArrayOfBoolean[2] = false;
    updateFromSolver(paramLinearSystem);
    int i = this.mChildren.size();
    for (byte b = 0; b < i; b++) {
      ConstraintWidget constraintWidget = (ConstraintWidget)this.mChildren.get(b);
      constraintWidget.updateFromSolver(paramLinearSystem);
      if (constraintWidget.mListDimensionBehaviors[false] == ConstraintWidget.DimensionBehaviour.MATCH_CONSTRAINT && constraintWidget.getWidth() < constraintWidget.getWrapWidth())
        paramArrayOfBoolean[2] = true; 
      if (constraintWidget.mListDimensionBehaviors[true] == ConstraintWidget.DimensionBehaviour.MATCH_CONSTRAINT && constraintWidget.getHeight() < constraintWidget.getWrapHeight())
        paramArrayOfBoolean[2] = true; 
    } 
  }
}


/* Location:              D:\Apks\New folder\classes-dex2jar.jar!\android\support\constraint\solver\widgets\ConstraintWidgetContainer.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.0.7
 */