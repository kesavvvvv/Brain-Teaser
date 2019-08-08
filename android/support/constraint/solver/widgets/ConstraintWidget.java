package android.support.constraint.solver.widgets;

import android.support.constraint.solver.Cache;
import android.support.constraint.solver.LinearSystem;
import android.support.constraint.solver.SolverVariable;
import java.util.ArrayList;

public class ConstraintWidget {
  protected static final int ANCHOR_BASELINE = 4;
  
  protected static final int ANCHOR_BOTTOM = 3;
  
  protected static final int ANCHOR_LEFT = 0;
  
  protected static final int ANCHOR_RIGHT = 1;
  
  protected static final int ANCHOR_TOP = 2;
  
  private static final boolean AUTOTAG_CENTER = false;
  
  public static final int CHAIN_PACKED = 2;
  
  public static final int CHAIN_SPREAD = 0;
  
  public static final int CHAIN_SPREAD_INSIDE = 1;
  
  public static float DEFAULT_BIAS = 0.5F;
  
  static final int DIMENSION_HORIZONTAL = 0;
  
  static final int DIMENSION_VERTICAL = 1;
  
  protected static final int DIRECT = 2;
  
  public static final int GONE = 8;
  
  public static final int HORIZONTAL = 0;
  
  public static final int INVISIBLE = 4;
  
  public static final int MATCH_CONSTRAINT_PERCENT = 2;
  
  public static final int MATCH_CONSTRAINT_RATIO = 3;
  
  public static final int MATCH_CONSTRAINT_RATIO_RESOLVED = 4;
  
  public static final int MATCH_CONSTRAINT_SPREAD = 0;
  
  public static final int MATCH_CONSTRAINT_WRAP = 1;
  
  protected static final int SOLVER = 1;
  
  public static final int UNKNOWN = -1;
  
  public static final int VERTICAL = 1;
  
  public static final int VISIBLE = 0;
  
  private static final int WRAP = -2;
  
  protected ArrayList<ConstraintAnchor> mAnchors = new ArrayList();
  
  ConstraintAnchor mBaseline = new ConstraintAnchor(this, ConstraintAnchor.Type.BASELINE);
  
  int mBaselineDistance = 0;
  
  ConstraintWidgetGroup mBelongingGroup = null;
  
  ConstraintAnchor mBottom = new ConstraintAnchor(this, ConstraintAnchor.Type.BOTTOM);
  
  boolean mBottomHasCentered;
  
  ConstraintAnchor mCenter = new ConstraintAnchor(this, ConstraintAnchor.Type.CENTER);
  
  ConstraintAnchor mCenterX = new ConstraintAnchor(this, ConstraintAnchor.Type.CENTER_X);
  
  ConstraintAnchor mCenterY = new ConstraintAnchor(this, ConstraintAnchor.Type.CENTER_Y);
  
  private float mCircleConstraintAngle = 0.0F;
  
  private Object mCompanionWidget;
  
  private int mContainerItemSkip;
  
  private String mDebugName;
  
  protected float mDimensionRatio = 0.0F;
  
  protected int mDimensionRatioSide = -1;
  
  int mDistToBottom;
  
  int mDistToLeft;
  
  int mDistToRight;
  
  int mDistToTop;
  
  private int mDrawHeight = 0;
  
  private int mDrawWidth = 0;
  
  private int mDrawX = 0;
  
  private int mDrawY = 0;
  
  boolean mGroupsToSolver;
  
  int mHeight = 0;
  
  float mHorizontalBiasPercent;
  
  boolean mHorizontalChainFixedPosition;
  
  int mHorizontalChainStyle;
  
  ConstraintWidget mHorizontalNextWidget;
  
  public int mHorizontalResolution = -1;
  
  boolean mHorizontalWrapVisited;
  
  boolean mIsHeightWrapContent;
  
  boolean mIsWidthWrapContent;
  
  ConstraintAnchor mLeft = new ConstraintAnchor(this, ConstraintAnchor.Type.LEFT);
  
  boolean mLeftHasCentered;
  
  protected ConstraintAnchor[] mListAnchors = { this.mLeft, this.mRight, this.mTop, this.mBottom, this.mBaseline, this.mCenter };
  
  protected DimensionBehaviour[] mListDimensionBehaviors = { DimensionBehaviour.FIXED, DimensionBehaviour.FIXED };
  
  protected ConstraintWidget[] mListNextMatchConstraintsWidget;
  
  int mMatchConstraintDefaultHeight = 0;
  
  int mMatchConstraintDefaultWidth = 0;
  
  int mMatchConstraintMaxHeight = 0;
  
  int mMatchConstraintMaxWidth = 0;
  
  int mMatchConstraintMinHeight = 0;
  
  int mMatchConstraintMinWidth = 0;
  
  float mMatchConstraintPercentHeight = 1.0F;
  
  float mMatchConstraintPercentWidth = 1.0F;
  
  private int[] mMaxDimension = { Integer.MAX_VALUE, Integer.MAX_VALUE };
  
  protected int mMinHeight;
  
  protected int mMinWidth;
  
  protected ConstraintWidget[] mNextChainWidget;
  
  protected int mOffsetX = 0;
  
  protected int mOffsetY = 0;
  
  boolean mOptimizerMeasurable;
  
  boolean mOptimizerMeasured;
  
  ConstraintWidget mParent = null;
  
  int mRelX = 0;
  
  int mRelY = 0;
  
  ResolutionDimension mResolutionHeight;
  
  ResolutionDimension mResolutionWidth;
  
  float mResolvedDimensionRatio = 1.0F;
  
  int mResolvedDimensionRatioSide = -1;
  
  int[] mResolvedMatchConstraintDefault = new int[2];
  
  ConstraintAnchor mRight = new ConstraintAnchor(this, ConstraintAnchor.Type.RIGHT);
  
  boolean mRightHasCentered;
  
  ConstraintAnchor mTop = new ConstraintAnchor(this, ConstraintAnchor.Type.TOP);
  
  boolean mTopHasCentered;
  
  private String mType;
  
  float mVerticalBiasPercent;
  
  boolean mVerticalChainFixedPosition;
  
  int mVerticalChainStyle;
  
  ConstraintWidget mVerticalNextWidget;
  
  public int mVerticalResolution = -1;
  
  boolean mVerticalWrapVisited;
  
  private int mVisibility;
  
  float[] mWeight;
  
  int mWidth = 0;
  
  private int mWrapHeight;
  
  private int mWrapWidth;
  
  protected int mX = 0;
  
  protected int mY = 0;
  
  public ConstraintWidget() {
    float f = DEFAULT_BIAS;
    this.mHorizontalBiasPercent = f;
    this.mVerticalBiasPercent = f;
    this.mContainerItemSkip = 0;
    this.mVisibility = 0;
    this.mDebugName = null;
    this.mType = null;
    this.mOptimizerMeasurable = false;
    this.mOptimizerMeasured = false;
    this.mGroupsToSolver = false;
    this.mHorizontalChainStyle = 0;
    this.mVerticalChainStyle = 0;
    this.mWeight = new float[] { -1.0F, -1.0F };
    this.mListNextMatchConstraintsWidget = new ConstraintWidget[] { null, null };
    this.mNextChainWidget = new ConstraintWidget[] { null, null };
    this.mHorizontalNextWidget = null;
    this.mVerticalNextWidget = null;
    addAnchors();
  }
  
  public ConstraintWidget(int paramInt1, int paramInt2) { this(0, 0, paramInt1, paramInt2); }
  
  public ConstraintWidget(int paramInt1, int paramInt2, int paramInt3, int paramInt4) {
    float f = DEFAULT_BIAS;
    this.mHorizontalBiasPercent = f;
    this.mVerticalBiasPercent = f;
    this.mContainerItemSkip = 0;
    this.mVisibility = 0;
    this.mDebugName = null;
    this.mType = null;
    this.mOptimizerMeasurable = false;
    this.mOptimizerMeasured = false;
    this.mGroupsToSolver = false;
    this.mHorizontalChainStyle = 0;
    this.mVerticalChainStyle = 0;
    this.mWeight = new float[] { -1.0F, -1.0F };
    this.mListNextMatchConstraintsWidget = new ConstraintWidget[] { null, null };
    this.mNextChainWidget = new ConstraintWidget[] { null, null };
    this.mHorizontalNextWidget = null;
    this.mVerticalNextWidget = null;
    this.mX = paramInt1;
    this.mY = paramInt2;
    this.mWidth = paramInt3;
    this.mHeight = paramInt4;
    addAnchors();
    forceUpdateDrawPosition();
  }
  
  private void addAnchors() {
    this.mAnchors.add(this.mLeft);
    this.mAnchors.add(this.mTop);
    this.mAnchors.add(this.mRight);
    this.mAnchors.add(this.mBottom);
    this.mAnchors.add(this.mCenterX);
    this.mAnchors.add(this.mCenterY);
    this.mAnchors.add(this.mCenter);
    this.mAnchors.add(this.mBaseline);
  }
  
  private void applyConstraints(LinearSystem paramLinearSystem, boolean paramBoolean1, SolverVariable paramSolverVariable1, SolverVariable paramSolverVariable2, DimensionBehaviour paramDimensionBehaviour, boolean paramBoolean2, ConstraintAnchor paramConstraintAnchor1, ConstraintAnchor paramConstraintAnchor2, int paramInt1, int paramInt2, int paramInt3, int paramInt4, float paramFloat1, boolean paramBoolean3, boolean paramBoolean4, int paramInt5, int paramInt6, int paramInt7, float paramFloat2, boolean paramBoolean5) { // Byte code:
    //   0: aload_1
    //   1: aload #7
    //   3: invokevirtual createObjectVariable : (Ljava/lang/Object;)Landroid/support/constraint/solver/SolverVariable;
    //   6: astore #29
    //   8: aload_1
    //   9: aload #8
    //   11: invokevirtual createObjectVariable : (Ljava/lang/Object;)Landroid/support/constraint/solver/SolverVariable;
    //   14: astore #27
    //   16: aload_1
    //   17: aload #7
    //   19: invokevirtual getTarget : ()Landroid/support/constraint/solver/widgets/ConstraintAnchor;
    //   22: invokevirtual createObjectVariable : (Ljava/lang/Object;)Landroid/support/constraint/solver/SolverVariable;
    //   25: astore #31
    //   27: aload_1
    //   28: aload #8
    //   30: invokevirtual getTarget : ()Landroid/support/constraint/solver/widgets/ConstraintAnchor;
    //   33: invokevirtual createObjectVariable : (Ljava/lang/Object;)Landroid/support/constraint/solver/SolverVariable;
    //   36: astore #30
    //   38: aload_1
    //   39: getfield graphOptimizer : Z
    //   42: ifeq -> 128
    //   45: aload #7
    //   47: invokevirtual getResolutionNode : ()Landroid/support/constraint/solver/widgets/ResolutionAnchor;
    //   50: getfield state : I
    //   53: iconst_1
    //   54: if_icmpne -> 128
    //   57: aload #8
    //   59: invokevirtual getResolutionNode : ()Landroid/support/constraint/solver/widgets/ResolutionAnchor;
    //   62: getfield state : I
    //   65: iconst_1
    //   66: if_icmpne -> 128
    //   69: invokestatic getMetrics : ()Landroid/support/constraint/solver/Metrics;
    //   72: ifnull -> 89
    //   75: invokestatic getMetrics : ()Landroid/support/constraint/solver/Metrics;
    //   78: astore_3
    //   79: aload_3
    //   80: aload_3
    //   81: getfield resolvedWidgets : J
    //   84: lconst_1
    //   85: ladd
    //   86: putfield resolvedWidgets : J
    //   89: aload #7
    //   91: invokevirtual getResolutionNode : ()Landroid/support/constraint/solver/widgets/ResolutionAnchor;
    //   94: aload_1
    //   95: invokevirtual addResolvedValue : (Landroid/support/constraint/solver/LinearSystem;)V
    //   98: aload #8
    //   100: invokevirtual getResolutionNode : ()Landroid/support/constraint/solver/widgets/ResolutionAnchor;
    //   103: aload_1
    //   104: invokevirtual addResolvedValue : (Landroid/support/constraint/solver/LinearSystem;)V
    //   107: iload #15
    //   109: ifne -> 127
    //   112: iload_2
    //   113: ifeq -> 127
    //   116: aload_1
    //   117: aload #4
    //   119: aload #27
    //   121: iconst_0
    //   122: bipush #6
    //   124: invokevirtual addGreaterThan : (Landroid/support/constraint/solver/SolverVariable;Landroid/support/constraint/solver/SolverVariable;II)V
    //   127: return
    //   128: invokestatic getMetrics : ()Landroid/support/constraint/solver/Metrics;
    //   131: ifnull -> 151
    //   134: invokestatic getMetrics : ()Landroid/support/constraint/solver/Metrics;
    //   137: astore #28
    //   139: aload #28
    //   141: aload #28
    //   143: getfield nonresolvedWidgets : J
    //   146: lconst_1
    //   147: ladd
    //   148: putfield nonresolvedWidgets : J
    //   151: aload #7
    //   153: invokevirtual isConnected : ()Z
    //   156: istore #24
    //   158: aload #8
    //   160: invokevirtual isConnected : ()Z
    //   163: istore #25
    //   165: aload_0
    //   166: getfield mCenter : Landroid/support/constraint/solver/widgets/ConstraintAnchor;
    //   169: invokevirtual isConnected : ()Z
    //   172: istore #26
    //   174: iconst_0
    //   175: istore #23
    //   177: iconst_0
    //   178: istore #22
    //   180: iload #24
    //   182: ifeq -> 190
    //   185: iconst_0
    //   186: iconst_1
    //   187: iadd
    //   188: istore #22
    //   190: iload #22
    //   192: istore #21
    //   194: iload #25
    //   196: ifeq -> 205
    //   199: iload #22
    //   201: iconst_1
    //   202: iadd
    //   203: istore #21
    //   205: iload #21
    //   207: istore #22
    //   209: iload #26
    //   211: ifeq -> 220
    //   214: iload #21
    //   216: iconst_1
    //   217: iadd
    //   218: istore #22
    //   220: iload #14
    //   222: ifeq -> 231
    //   225: iconst_3
    //   226: istore #21
    //   228: goto -> 235
    //   231: iload #16
    //   233: istore #21
    //   235: getstatic android/support/constraint/solver/widgets/ConstraintWidget$1.$SwitchMap$android$support$constraint$solver$widgets$ConstraintWidget$DimensionBehaviour : [I
    //   238: aload #5
    //   240: invokevirtual ordinal : ()I
    //   243: iaload
    //   244: tableswitch default -> 276, 1 -> 310, 2 -> 304, 3 -> 298, 4 -> 283
    //   276: iload #23
    //   278: istore #16
    //   280: goto -> 313
    //   283: iconst_1
    //   284: istore #16
    //   286: iload #21
    //   288: iconst_4
    //   289: if_icmpne -> 313
    //   292: iconst_0
    //   293: istore #16
    //   295: goto -> 313
    //   298: iconst_0
    //   299: istore #16
    //   301: goto -> 313
    //   304: iconst_0
    //   305: istore #16
    //   307: goto -> 313
    //   310: iconst_0
    //   311: istore #16
    //   313: aload_0
    //   314: getfield mVisibility : I
    //   317: istore #23
    //   319: iload #23
    //   321: bipush #8
    //   323: if_icmpne -> 335
    //   326: iconst_0
    //   327: istore #10
    //   329: iconst_0
    //   330: istore #16
    //   332: goto -> 335
    //   335: iload #20
    //   337: ifeq -> 398
    //   340: iload #24
    //   342: ifne -> 366
    //   345: iload #25
    //   347: ifne -> 366
    //   350: iload #26
    //   352: ifne -> 366
    //   355: aload_1
    //   356: aload #29
    //   358: iload #9
    //   360: invokevirtual addEquality : (Landroid/support/constraint/solver/SolverVariable;I)V
    //   363: goto -> 398
    //   366: iload #24
    //   368: ifeq -> 395
    //   371: iload #25
    //   373: ifne -> 395
    //   376: aload_1
    //   377: aload #29
    //   379: aload #31
    //   381: aload #7
    //   383: invokevirtual getMargin : ()I
    //   386: bipush #6
    //   388: invokevirtual addEquality : (Landroid/support/constraint/solver/SolverVariable;Landroid/support/constraint/solver/SolverVariable;II)Landroid/support/constraint/solver/ArrayRow;
    //   391: pop
    //   392: goto -> 398
    //   395: goto -> 398
    //   398: iload #16
    //   400: ifne -> 489
    //   403: iload #6
    //   405: ifeq -> 461
    //   408: aload_1
    //   409: aload #27
    //   411: aload #29
    //   413: iconst_0
    //   414: iconst_3
    //   415: invokevirtual addEquality : (Landroid/support/constraint/solver/SolverVariable;Landroid/support/constraint/solver/SolverVariable;II)Landroid/support/constraint/solver/ArrayRow;
    //   418: pop
    //   419: iload #11
    //   421: ifle -> 439
    //   424: aload_1
    //   425: aload #27
    //   427: aload #29
    //   429: iload #11
    //   431: bipush #6
    //   433: invokevirtual addGreaterThan : (Landroid/support/constraint/solver/SolverVariable;Landroid/support/constraint/solver/SolverVariable;II)V
    //   436: goto -> 439
    //   439: iload #12
    //   441: ldc 2147483647
    //   443: if_icmpge -> 474
    //   446: aload_1
    //   447: aload #27
    //   449: aload #29
    //   451: iload #12
    //   453: bipush #6
    //   455: invokevirtual addLowerThan : (Landroid/support/constraint/solver/SolverVariable;Landroid/support/constraint/solver/SolverVariable;II)V
    //   458: goto -> 474
    //   461: aload_1
    //   462: aload #27
    //   464: aload #29
    //   466: iload #10
    //   468: bipush #6
    //   470: invokevirtual addEquality : (Landroid/support/constraint/solver/SolverVariable;Landroid/support/constraint/solver/SolverVariable;II)Landroid/support/constraint/solver/ArrayRow;
    //   473: pop
    //   474: iload #17
    //   476: istore #9
    //   478: iload #10
    //   480: istore #17
    //   482: iload #18
    //   484: istore #10
    //   486: goto -> 842
    //   489: iload #17
    //   491: istore #9
    //   493: iload #17
    //   495: bipush #-2
    //   497: if_icmpne -> 504
    //   500: iload #10
    //   502: istore #9
    //   504: iload #18
    //   506: istore #12
    //   508: iload #18
    //   510: bipush #-2
    //   512: if_icmpne -> 519
    //   515: iload #10
    //   517: istore #12
    //   519: iload #9
    //   521: ifle -> 548
    //   524: aload_1
    //   525: aload #27
    //   527: aload #29
    //   529: iload #9
    //   531: bipush #6
    //   533: invokevirtual addGreaterThan : (Landroid/support/constraint/solver/SolverVariable;Landroid/support/constraint/solver/SolverVariable;II)V
    //   536: iload #10
    //   538: iload #9
    //   540: invokestatic max : (II)I
    //   543: istore #17
    //   545: goto -> 552
    //   548: iload #10
    //   550: istore #17
    //   552: iload #17
    //   554: istore #10
    //   556: iload #12
    //   558: ifle -> 582
    //   561: aload_1
    //   562: aload #27
    //   564: aload #29
    //   566: iload #12
    //   568: bipush #6
    //   570: invokevirtual addLowerThan : (Landroid/support/constraint/solver/SolverVariable;Landroid/support/constraint/solver/SolverVariable;II)V
    //   573: iload #17
    //   575: iload #12
    //   577: invokestatic min : (II)I
    //   580: istore #10
    //   582: iload #21
    //   584: iconst_1
    //   585: if_icmpne -> 643
    //   588: iload_2
    //   589: ifeq -> 608
    //   592: aload_1
    //   593: aload #27
    //   595: aload #29
    //   597: iload #10
    //   599: bipush #6
    //   601: invokevirtual addEquality : (Landroid/support/constraint/solver/SolverVariable;Landroid/support/constraint/solver/SolverVariable;II)Landroid/support/constraint/solver/ArrayRow;
    //   604: pop
    //   605: goto -> 768
    //   608: iload #15
    //   610: ifeq -> 628
    //   613: aload_1
    //   614: aload #27
    //   616: aload #29
    //   618: iload #10
    //   620: iconst_4
    //   621: invokevirtual addEquality : (Landroid/support/constraint/solver/SolverVariable;Landroid/support/constraint/solver/SolverVariable;II)Landroid/support/constraint/solver/ArrayRow;
    //   624: pop
    //   625: goto -> 768
    //   628: aload_1
    //   629: aload #27
    //   631: aload #29
    //   633: iload #10
    //   635: iconst_1
    //   636: invokevirtual addEquality : (Landroid/support/constraint/solver/SolverVariable;Landroid/support/constraint/solver/SolverVariable;II)Landroid/support/constraint/solver/ArrayRow;
    //   639: pop
    //   640: goto -> 768
    //   643: iload #21
    //   645: iconst_2
    //   646: if_icmpne -> 768
    //   649: aload #7
    //   651: invokevirtual getType : ()Landroid/support/constraint/solver/widgets/ConstraintAnchor$Type;
    //   654: getstatic android/support/constraint/solver/widgets/ConstraintAnchor$Type.TOP : Landroid/support/constraint/solver/widgets/ConstraintAnchor$Type;
    //   657: if_acmpeq -> 709
    //   660: aload #7
    //   662: invokevirtual getType : ()Landroid/support/constraint/solver/widgets/ConstraintAnchor$Type;
    //   665: getstatic android/support/constraint/solver/widgets/ConstraintAnchor$Type.BOTTOM : Landroid/support/constraint/solver/widgets/ConstraintAnchor$Type;
    //   668: if_acmpne -> 674
    //   671: goto -> 709
    //   674: aload_1
    //   675: aload_0
    //   676: getfield mParent : Landroid/support/constraint/solver/widgets/ConstraintWidget;
    //   679: getstatic android/support/constraint/solver/widgets/ConstraintAnchor$Type.LEFT : Landroid/support/constraint/solver/widgets/ConstraintAnchor$Type;
    //   682: invokevirtual getAnchor : (Landroid/support/constraint/solver/widgets/ConstraintAnchor$Type;)Landroid/support/constraint/solver/widgets/ConstraintAnchor;
    //   685: invokevirtual createObjectVariable : (Ljava/lang/Object;)Landroid/support/constraint/solver/SolverVariable;
    //   688: astore #5
    //   690: aload_1
    //   691: aload_0
    //   692: getfield mParent : Landroid/support/constraint/solver/widgets/ConstraintWidget;
    //   695: getstatic android/support/constraint/solver/widgets/ConstraintAnchor$Type.RIGHT : Landroid/support/constraint/solver/widgets/ConstraintAnchor$Type;
    //   698: invokevirtual getAnchor : (Landroid/support/constraint/solver/widgets/ConstraintAnchor$Type;)Landroid/support/constraint/solver/widgets/ConstraintAnchor;
    //   701: invokevirtual createObjectVariable : (Ljava/lang/Object;)Landroid/support/constraint/solver/SolverVariable;
    //   704: astore #28
    //   706: goto -> 741
    //   709: aload_1
    //   710: aload_0
    //   711: getfield mParent : Landroid/support/constraint/solver/widgets/ConstraintWidget;
    //   714: getstatic android/support/constraint/solver/widgets/ConstraintAnchor$Type.TOP : Landroid/support/constraint/solver/widgets/ConstraintAnchor$Type;
    //   717: invokevirtual getAnchor : (Landroid/support/constraint/solver/widgets/ConstraintAnchor$Type;)Landroid/support/constraint/solver/widgets/ConstraintAnchor;
    //   720: invokevirtual createObjectVariable : (Ljava/lang/Object;)Landroid/support/constraint/solver/SolverVariable;
    //   723: astore #5
    //   725: aload_1
    //   726: aload_0
    //   727: getfield mParent : Landroid/support/constraint/solver/widgets/ConstraintWidget;
    //   730: getstatic android/support/constraint/solver/widgets/ConstraintAnchor$Type.BOTTOM : Landroid/support/constraint/solver/widgets/ConstraintAnchor$Type;
    //   733: invokevirtual getAnchor : (Landroid/support/constraint/solver/widgets/ConstraintAnchor$Type;)Landroid/support/constraint/solver/widgets/ConstraintAnchor;
    //   736: invokevirtual createObjectVariable : (Ljava/lang/Object;)Landroid/support/constraint/solver/SolverVariable;
    //   739: astore #28
    //   741: aload_1
    //   742: aload_1
    //   743: invokevirtual createRow : ()Landroid/support/constraint/solver/ArrayRow;
    //   746: aload #27
    //   748: aload #29
    //   750: aload #28
    //   752: aload #5
    //   754: fload #19
    //   756: invokevirtual createRowDimensionRatio : (Landroid/support/constraint/solver/SolverVariable;Landroid/support/constraint/solver/SolverVariable;Landroid/support/constraint/solver/SolverVariable;Landroid/support/constraint/solver/SolverVariable;F)Landroid/support/constraint/solver/ArrayRow;
    //   759: invokevirtual addConstraint : (Landroid/support/constraint/solver/ArrayRow;)V
    //   762: iconst_0
    //   763: istore #16
    //   765: goto -> 768
    //   768: iload #10
    //   770: istore #17
    //   772: iload #16
    //   774: ifeq -> 838
    //   777: iload #22
    //   779: iconst_2
    //   780: if_icmpeq -> 838
    //   783: iload #14
    //   785: ifne -> 838
    //   788: iconst_0
    //   789: istore #16
    //   791: iload #9
    //   793: iload #17
    //   795: invokestatic max : (II)I
    //   798: istore #18
    //   800: iload #18
    //   802: istore #10
    //   804: iload #12
    //   806: ifle -> 818
    //   809: iload #12
    //   811: iload #18
    //   813: invokestatic min : (II)I
    //   816: istore #10
    //   818: aload_1
    //   819: aload #27
    //   821: aload #29
    //   823: iload #10
    //   825: bipush #6
    //   827: invokevirtual addEquality : (Landroid/support/constraint/solver/SolverVariable;Landroid/support/constraint/solver/SolverVariable;II)Landroid/support/constraint/solver/ArrayRow;
    //   830: pop
    //   831: iload #12
    //   833: istore #10
    //   835: goto -> 842
    //   838: iload #12
    //   840: istore #10
    //   842: aload #31
    //   844: astore #5
    //   846: iload #20
    //   848: ifeq -> 1553
    //   851: iload #15
    //   853: ifeq -> 859
    //   856: goto -> 1553
    //   859: iload #24
    //   861: ifne -> 900
    //   864: iload #25
    //   866: ifne -> 900
    //   869: iload #26
    //   871: ifne -> 900
    //   874: iload_2
    //   875: ifeq -> 894
    //   878: aload_1
    //   879: aload #4
    //   881: aload #27
    //   883: iconst_0
    //   884: iconst_5
    //   885: invokevirtual addGreaterThan : (Landroid/support/constraint/solver/SolverVariable;Landroid/support/constraint/solver/SolverVariable;II)V
    //   888: aload #27
    //   890: astore_3
    //   891: goto -> 1538
    //   894: aload #27
    //   896: astore_3
    //   897: goto -> 1538
    //   900: iload #24
    //   902: ifeq -> 936
    //   905: iload #25
    //   907: ifne -> 936
    //   910: iload_2
    //   911: ifeq -> 930
    //   914: aload_1
    //   915: aload #4
    //   917: aload #27
    //   919: iconst_0
    //   920: iconst_5
    //   921: invokevirtual addGreaterThan : (Landroid/support/constraint/solver/SolverVariable;Landroid/support/constraint/solver/SolverVariable;II)V
    //   924: aload #27
    //   926: astore_3
    //   927: goto -> 1538
    //   930: aload #27
    //   932: astore_3
    //   933: goto -> 1538
    //   936: iload #24
    //   938: ifne -> 988
    //   941: iload #25
    //   943: ifeq -> 988
    //   946: aload_1
    //   947: aload #27
    //   949: aload #30
    //   951: aload #8
    //   953: invokevirtual getMargin : ()I
    //   956: ineg
    //   957: bipush #6
    //   959: invokevirtual addEquality : (Landroid/support/constraint/solver/SolverVariable;Landroid/support/constraint/solver/SolverVariable;II)Landroid/support/constraint/solver/ArrayRow;
    //   962: pop
    //   963: iload_2
    //   964: ifeq -> 982
    //   967: aload_1
    //   968: aload #29
    //   970: aload_3
    //   971: iconst_0
    //   972: iconst_5
    //   973: invokevirtual addGreaterThan : (Landroid/support/constraint/solver/SolverVariable;Landroid/support/constraint/solver/SolverVariable;II)V
    //   976: aload #27
    //   978: astore_3
    //   979: goto -> 1538
    //   982: aload #27
    //   984: astore_3
    //   985: goto -> 1538
    //   988: iload #24
    //   990: ifeq -> 1535
    //   993: iload #25
    //   995: ifeq -> 1535
    //   998: iconst_0
    //   999: istore #12
    //   1001: iconst_0
    //   1002: istore #17
    //   1004: iload #16
    //   1006: ifeq -> 1247
    //   1009: iload_2
    //   1010: ifeq -> 1032
    //   1013: iload #11
    //   1015: ifne -> 1032
    //   1018: aload_1
    //   1019: aload #27
    //   1021: aload #29
    //   1023: iconst_0
    //   1024: bipush #6
    //   1026: invokevirtual addGreaterThan : (Landroid/support/constraint/solver/SolverVariable;Landroid/support/constraint/solver/SolverVariable;II)V
    //   1029: goto -> 1032
    //   1032: iload #21
    //   1034: ifne -> 1125
    //   1037: bipush #6
    //   1039: istore #18
    //   1041: iload #10
    //   1043: ifgt -> 1059
    //   1046: iload #12
    //   1048: istore #11
    //   1050: iload #18
    //   1052: istore #12
    //   1054: iload #9
    //   1056: ifle -> 1065
    //   1059: iconst_4
    //   1060: istore #12
    //   1062: iconst_1
    //   1063: istore #11
    //   1065: aload_1
    //   1066: aload #29
    //   1068: aload #5
    //   1070: aload #7
    //   1072: invokevirtual getMargin : ()I
    //   1075: iload #12
    //   1077: invokevirtual addEquality : (Landroid/support/constraint/solver/SolverVariable;Landroid/support/constraint/solver/SolverVariable;II)Landroid/support/constraint/solver/ArrayRow;
    //   1080: pop
    //   1081: aload_1
    //   1082: aload #27
    //   1084: aload #30
    //   1086: aload #8
    //   1088: invokevirtual getMargin : ()I
    //   1091: ineg
    //   1092: iload #12
    //   1094: invokevirtual addEquality : (Landroid/support/constraint/solver/SolverVariable;Landroid/support/constraint/solver/SolverVariable;II)Landroid/support/constraint/solver/ArrayRow;
    //   1097: pop
    //   1098: iload #10
    //   1100: ifgt -> 1112
    //   1103: iload #17
    //   1105: istore #10
    //   1107: iload #9
    //   1109: ifle -> 1115
    //   1112: iconst_1
    //   1113: istore #10
    //   1115: iload #11
    //   1117: istore #9
    //   1119: iconst_5
    //   1120: istore #11
    //   1122: goto -> 1256
    //   1125: iload #21
    //   1127: iconst_1
    //   1128: if_icmpne -> 1144
    //   1131: bipush #6
    //   1133: istore #11
    //   1135: iconst_1
    //   1136: istore #9
    //   1138: iconst_1
    //   1139: istore #10
    //   1141: goto -> 1256
    //   1144: iload #21
    //   1146: iconst_3
    //   1147: if_icmpne -> 1235
    //   1150: iconst_4
    //   1151: istore #11
    //   1153: iload #14
    //   1155: ifne -> 1186
    //   1158: iload #11
    //   1160: istore #9
    //   1162: aload_0
    //   1163: getfield mResolvedDimensionRatioSide : I
    //   1166: iconst_m1
    //   1167: if_icmpeq -> 1190
    //   1170: iload #11
    //   1172: istore #9
    //   1174: iload #10
    //   1176: ifgt -> 1190
    //   1179: bipush #6
    //   1181: istore #9
    //   1183: goto -> 1190
    //   1186: iload #11
    //   1188: istore #9
    //   1190: aload_1
    //   1191: aload #29
    //   1193: aload #5
    //   1195: aload #7
    //   1197: invokevirtual getMargin : ()I
    //   1200: iload #9
    //   1202: invokevirtual addEquality : (Landroid/support/constraint/solver/SolverVariable;Landroid/support/constraint/solver/SolverVariable;II)Landroid/support/constraint/solver/ArrayRow;
    //   1205: pop
    //   1206: aload_1
    //   1207: aload #27
    //   1209: aload #30
    //   1211: aload #8
    //   1213: invokevirtual getMargin : ()I
    //   1216: ineg
    //   1217: iload #9
    //   1219: invokevirtual addEquality : (Landroid/support/constraint/solver/SolverVariable;Landroid/support/constraint/solver/SolverVariable;II)Landroid/support/constraint/solver/ArrayRow;
    //   1222: pop
    //   1223: iconst_1
    //   1224: istore #10
    //   1226: iconst_5
    //   1227: istore #11
    //   1229: iconst_1
    //   1230: istore #9
    //   1232: goto -> 1256
    //   1235: iconst_0
    //   1236: istore #10
    //   1238: iconst_5
    //   1239: istore #11
    //   1241: iconst_0
    //   1242: istore #9
    //   1244: goto -> 1256
    //   1247: iconst_1
    //   1248: istore #10
    //   1250: iconst_5
    //   1251: istore #11
    //   1253: iconst_0
    //   1254: istore #9
    //   1256: iconst_5
    //   1257: istore #12
    //   1259: iconst_5
    //   1260: istore #17
    //   1262: iload_2
    //   1263: istore #6
    //   1265: iload_2
    //   1266: istore #14
    //   1268: iload #10
    //   1270: ifeq -> 1413
    //   1273: aload_1
    //   1274: aload #29
    //   1276: aload #5
    //   1278: aload #7
    //   1280: invokevirtual getMargin : ()I
    //   1283: fload #13
    //   1285: aload #30
    //   1287: aload #27
    //   1289: aload #8
    //   1291: invokevirtual getMargin : ()I
    //   1294: iload #11
    //   1296: invokevirtual addCentering : (Landroid/support/constraint/solver/SolverVariable;Landroid/support/constraint/solver/SolverVariable;IFLandroid/support/constraint/solver/SolverVariable;Landroid/support/constraint/solver/SolverVariable;II)V
    //   1299: aload #7
    //   1301: getfield mTarget : Landroid/support/constraint/solver/widgets/ConstraintAnchor;
    //   1304: getfield mOwner : Landroid/support/constraint/solver/widgets/ConstraintWidget;
    //   1307: instanceof android/support/constraint/solver/widgets/Barrier
    //   1310: istore #24
    //   1312: aload #8
    //   1314: getfield mTarget : Landroid/support/constraint/solver/widgets/ConstraintAnchor;
    //   1317: getfield mOwner : Landroid/support/constraint/solver/widgets/ConstraintWidget;
    //   1320: instanceof android/support/constraint/solver/widgets/Barrier
    //   1323: istore #25
    //   1325: iload #24
    //   1327: ifeq -> 1353
    //   1330: iload #25
    //   1332: ifne -> 1353
    //   1335: bipush #6
    //   1337: istore #11
    //   1339: iconst_1
    //   1340: istore #20
    //   1342: iload #12
    //   1344: istore #10
    //   1346: iload #6
    //   1348: istore #15
    //   1350: goto -> 1429
    //   1353: iload #12
    //   1355: istore #10
    //   1357: iload #17
    //   1359: istore #11
    //   1361: iload #6
    //   1363: istore #15
    //   1365: iload #14
    //   1367: istore #20
    //   1369: iload #24
    //   1371: ifne -> 1429
    //   1374: iload #12
    //   1376: istore #10
    //   1378: iload #17
    //   1380: istore #11
    //   1382: iload #6
    //   1384: istore #15
    //   1386: iload #14
    //   1388: istore #20
    //   1390: iload #25
    //   1392: ifeq -> 1429
    //   1395: bipush #6
    //   1397: istore #10
    //   1399: iconst_1
    //   1400: istore #15
    //   1402: iload #17
    //   1404: istore #11
    //   1406: iload #14
    //   1408: istore #20
    //   1410: goto -> 1429
    //   1413: iload #14
    //   1415: istore #20
    //   1417: iload #6
    //   1419: istore #15
    //   1421: iload #17
    //   1423: istore #11
    //   1425: iload #12
    //   1427: istore #10
    //   1429: iload #9
    //   1431: ifeq -> 1445
    //   1434: bipush #6
    //   1436: istore #10
    //   1438: bipush #6
    //   1440: istore #11
    //   1442: goto -> 1445
    //   1445: iload #16
    //   1447: ifne -> 1455
    //   1450: iload #15
    //   1452: ifne -> 1460
    //   1455: iload #9
    //   1457: ifeq -> 1475
    //   1460: aload_1
    //   1461: aload #29
    //   1463: aload #5
    //   1465: aload #7
    //   1467: invokevirtual getMargin : ()I
    //   1470: iload #10
    //   1472: invokevirtual addGreaterThan : (Landroid/support/constraint/solver/SolverVariable;Landroid/support/constraint/solver/SolverVariable;II)V
    //   1475: iload #16
    //   1477: ifne -> 1485
    //   1480: iload #20
    //   1482: ifne -> 1490
    //   1485: iload #9
    //   1487: ifeq -> 1509
    //   1490: aload_1
    //   1491: aload #27
    //   1493: aload #30
    //   1495: aload #8
    //   1497: invokevirtual getMargin : ()I
    //   1500: ineg
    //   1501: iload #11
    //   1503: invokevirtual addLowerThan : (Landroid/support/constraint/solver/SolverVariable;Landroid/support/constraint/solver/SolverVariable;II)V
    //   1506: goto -> 1509
    //   1509: iload_2
    //   1510: ifeq -> 1529
    //   1513: aload_1
    //   1514: aload #29
    //   1516: aload_3
    //   1517: iconst_0
    //   1518: bipush #6
    //   1520: invokevirtual addGreaterThan : (Landroid/support/constraint/solver/SolverVariable;Landroid/support/constraint/solver/SolverVariable;II)V
    //   1523: aload #27
    //   1525: astore_3
    //   1526: goto -> 1538
    //   1529: aload #27
    //   1531: astore_3
    //   1532: goto -> 1538
    //   1535: aload #27
    //   1537: astore_3
    //   1538: iload_2
    //   1539: ifeq -> 1552
    //   1542: aload_1
    //   1543: aload #4
    //   1545: aload_3
    //   1546: iconst_0
    //   1547: bipush #6
    //   1549: invokevirtual addGreaterThan : (Landroid/support/constraint/solver/SolverVariable;Landroid/support/constraint/solver/SolverVariable;II)V
    //   1552: return
    //   1553: iload #22
    //   1555: iconst_2
    //   1556: if_icmpge -> 1584
    //   1559: iload_2
    //   1560: ifeq -> 1584
    //   1563: aload_1
    //   1564: aload #29
    //   1566: aload_3
    //   1567: iconst_0
    //   1568: bipush #6
    //   1570: invokevirtual addGreaterThan : (Landroid/support/constraint/solver/SolverVariable;Landroid/support/constraint/solver/SolverVariable;II)V
    //   1573: aload_1
    //   1574: aload #4
    //   1576: aload #27
    //   1578: iconst_0
    //   1579: bipush #6
    //   1581: invokevirtual addGreaterThan : (Landroid/support/constraint/solver/SolverVariable;Landroid/support/constraint/solver/SolverVariable;II)V
    //   1584: return }
  
  private boolean isChainHead(int paramInt) {
    paramInt *= 2;
    if ((this.mListAnchors[paramInt]).mTarget != null) {
      ConstraintAnchor constraintAnchor = (this.mListAnchors[paramInt]).mTarget.mTarget;
      ConstraintAnchor[] arrayOfConstraintAnchor = this.mListAnchors;
      if (constraintAnchor != arrayOfConstraintAnchor[paramInt] && (arrayOfConstraintAnchor[paramInt + true]).mTarget != null && (this.mListAnchors[paramInt + true]).mTarget.mTarget == this.mListAnchors[paramInt + true])
        return true; 
    } 
    return false;
  }
  
  public void addToSolver(LinearSystem paramLinearSystem) { // Byte code:
    //   0: aload_1
    //   1: aload_0
    //   2: getfield mLeft : Landroid/support/constraint/solver/widgets/ConstraintAnchor;
    //   5: invokevirtual createObjectVariable : (Ljava/lang/Object;)Landroid/support/constraint/solver/SolverVariable;
    //   8: astore #24
    //   10: aload_1
    //   11: aload_0
    //   12: getfield mRight : Landroid/support/constraint/solver/widgets/ConstraintAnchor;
    //   15: invokevirtual createObjectVariable : (Ljava/lang/Object;)Landroid/support/constraint/solver/SolverVariable;
    //   18: astore #23
    //   20: aload_1
    //   21: aload_0
    //   22: getfield mTop : Landroid/support/constraint/solver/widgets/ConstraintAnchor;
    //   25: invokevirtual createObjectVariable : (Ljava/lang/Object;)Landroid/support/constraint/solver/SolverVariable;
    //   28: astore #22
    //   30: aload_1
    //   31: aload_0
    //   32: getfield mBottom : Landroid/support/constraint/solver/widgets/ConstraintAnchor;
    //   35: invokevirtual createObjectVariable : (Ljava/lang/Object;)Landroid/support/constraint/solver/SolverVariable;
    //   38: astore #18
    //   40: aload_1
    //   41: aload_0
    //   42: getfield mBaseline : Landroid/support/constraint/solver/widgets/ConstraintAnchor;
    //   45: invokevirtual createObjectVariable : (Ljava/lang/Object;)Landroid/support/constraint/solver/SolverVariable;
    //   48: astore #21
    //   50: aload_0
    //   51: getfield mParent : Landroid/support/constraint/solver/widgets/ConstraintWidget;
    //   54: astore #19
    //   56: aload #19
    //   58: ifnull -> 317
    //   61: aload #19
    //   63: ifnull -> 85
    //   66: aload #19
    //   68: getfield mListDimensionBehaviors : [Landroid/support/constraint/solver/widgets/ConstraintWidget$DimensionBehaviour;
    //   71: iconst_0
    //   72: aaload
    //   73: getstatic android/support/constraint/solver/widgets/ConstraintWidget$DimensionBehaviour.WRAP_CONTENT : Landroid/support/constraint/solver/widgets/ConstraintWidget$DimensionBehaviour;
    //   76: if_acmpne -> 85
    //   79: iconst_1
    //   80: istore #11
    //   82: goto -> 88
    //   85: iconst_0
    //   86: istore #11
    //   88: aload_0
    //   89: getfield mParent : Landroid/support/constraint/solver/widgets/ConstraintWidget;
    //   92: astore #19
    //   94: aload #19
    //   96: ifnull -> 118
    //   99: aload #19
    //   101: getfield mListDimensionBehaviors : [Landroid/support/constraint/solver/widgets/ConstraintWidget$DimensionBehaviour;
    //   104: iconst_1
    //   105: aaload
    //   106: getstatic android/support/constraint/solver/widgets/ConstraintWidget$DimensionBehaviour.WRAP_CONTENT : Landroid/support/constraint/solver/widgets/ConstraintWidget$DimensionBehaviour;
    //   109: if_acmpne -> 118
    //   112: iconst_1
    //   113: istore #12
    //   115: goto -> 121
    //   118: iconst_0
    //   119: istore #12
    //   121: aload_0
    //   122: iconst_0
    //   123: invokespecial isChainHead : (I)Z
    //   126: ifeq -> 147
    //   129: aload_0
    //   130: getfield mParent : Landroid/support/constraint/solver/widgets/ConstraintWidget;
    //   133: checkcast android/support/constraint/solver/widgets/ConstraintWidgetContainer
    //   136: aload_0
    //   137: iconst_0
    //   138: invokevirtual addChain : (Landroid/support/constraint/solver/widgets/ConstraintWidget;I)V
    //   141: iconst_1
    //   142: istore #13
    //   144: goto -> 153
    //   147: aload_0
    //   148: invokevirtual isInHorizontalChain : ()Z
    //   151: istore #13
    //   153: aload_0
    //   154: iconst_1
    //   155: invokespecial isChainHead : (I)Z
    //   158: ifeq -> 179
    //   161: aload_0
    //   162: getfield mParent : Landroid/support/constraint/solver/widgets/ConstraintWidget;
    //   165: checkcast android/support/constraint/solver/widgets/ConstraintWidgetContainer
    //   168: aload_0
    //   169: iconst_1
    //   170: invokevirtual addChain : (Landroid/support/constraint/solver/widgets/ConstraintWidget;I)V
    //   173: iconst_1
    //   174: istore #14
    //   176: goto -> 185
    //   179: aload_0
    //   180: invokevirtual isInVerticalChain : ()Z
    //   183: istore #14
    //   185: iload #11
    //   187: ifeq -> 238
    //   190: aload_0
    //   191: getfield mVisibility : I
    //   194: bipush #8
    //   196: if_icmpeq -> 238
    //   199: aload_0
    //   200: getfield mLeft : Landroid/support/constraint/solver/widgets/ConstraintAnchor;
    //   203: getfield mTarget : Landroid/support/constraint/solver/widgets/ConstraintAnchor;
    //   206: ifnonnull -> 238
    //   209: aload_0
    //   210: getfield mRight : Landroid/support/constraint/solver/widgets/ConstraintAnchor;
    //   213: getfield mTarget : Landroid/support/constraint/solver/widgets/ConstraintAnchor;
    //   216: ifnonnull -> 238
    //   219: aload_1
    //   220: aload_1
    //   221: aload_0
    //   222: getfield mParent : Landroid/support/constraint/solver/widgets/ConstraintWidget;
    //   225: getfield mRight : Landroid/support/constraint/solver/widgets/ConstraintAnchor;
    //   228: invokevirtual createObjectVariable : (Ljava/lang/Object;)Landroid/support/constraint/solver/SolverVariable;
    //   231: aload #23
    //   233: iconst_0
    //   234: iconst_1
    //   235: invokevirtual addGreaterThan : (Landroid/support/constraint/solver/SolverVariable;Landroid/support/constraint/solver/SolverVariable;II)V
    //   238: iload #12
    //   240: ifeq -> 298
    //   243: aload_0
    //   244: getfield mVisibility : I
    //   247: bipush #8
    //   249: if_icmpeq -> 298
    //   252: aload_0
    //   253: getfield mTop : Landroid/support/constraint/solver/widgets/ConstraintAnchor;
    //   256: getfield mTarget : Landroid/support/constraint/solver/widgets/ConstraintAnchor;
    //   259: ifnonnull -> 298
    //   262: aload_0
    //   263: getfield mBottom : Landroid/support/constraint/solver/widgets/ConstraintAnchor;
    //   266: getfield mTarget : Landroid/support/constraint/solver/widgets/ConstraintAnchor;
    //   269: ifnonnull -> 298
    //   272: aload_0
    //   273: getfield mBaseline : Landroid/support/constraint/solver/widgets/ConstraintAnchor;
    //   276: ifnonnull -> 298
    //   279: aload_1
    //   280: aload_1
    //   281: aload_0
    //   282: getfield mParent : Landroid/support/constraint/solver/widgets/ConstraintWidget;
    //   285: getfield mBottom : Landroid/support/constraint/solver/widgets/ConstraintAnchor;
    //   288: invokevirtual createObjectVariable : (Ljava/lang/Object;)Landroid/support/constraint/solver/SolverVariable;
    //   291: aload #18
    //   293: iconst_0
    //   294: iconst_1
    //   295: invokevirtual addGreaterThan : (Landroid/support/constraint/solver/SolverVariable;Landroid/support/constraint/solver/SolverVariable;II)V
    //   298: iload #14
    //   300: istore #16
    //   302: iload #11
    //   304: istore #14
    //   306: iload #13
    //   308: istore #15
    //   310: iload #16
    //   312: istore #13
    //   314: goto -> 329
    //   317: iconst_0
    //   318: istore #15
    //   320: iconst_0
    //   321: istore #13
    //   323: iconst_0
    //   324: istore #14
    //   326: iconst_0
    //   327: istore #12
    //   329: aload_0
    //   330: getfield mWidth : I
    //   333: istore_3
    //   334: iload_3
    //   335: istore #5
    //   337: iload_3
    //   338: aload_0
    //   339: getfield mMinWidth : I
    //   342: if_icmpge -> 351
    //   345: aload_0
    //   346: getfield mMinWidth : I
    //   349: istore #5
    //   351: aload_0
    //   352: getfield mHeight : I
    //   355: istore_3
    //   356: iload_3
    //   357: istore #6
    //   359: iload_3
    //   360: aload_0
    //   361: getfield mMinHeight : I
    //   364: if_icmpge -> 373
    //   367: aload_0
    //   368: getfield mMinHeight : I
    //   371: istore #6
    //   373: aload_0
    //   374: getfield mListDimensionBehaviors : [Landroid/support/constraint/solver/widgets/ConstraintWidget$DimensionBehaviour;
    //   377: iconst_0
    //   378: aaload
    //   379: getstatic android/support/constraint/solver/widgets/ConstraintWidget$DimensionBehaviour.MATCH_CONSTRAINT : Landroid/support/constraint/solver/widgets/ConstraintWidget$DimensionBehaviour;
    //   382: if_acmpeq -> 391
    //   385: iconst_1
    //   386: istore #11
    //   388: goto -> 394
    //   391: iconst_0
    //   392: istore #11
    //   394: aload_0
    //   395: getfield mListDimensionBehaviors : [Landroid/support/constraint/solver/widgets/ConstraintWidget$DimensionBehaviour;
    //   398: iconst_1
    //   399: aaload
    //   400: getstatic android/support/constraint/solver/widgets/ConstraintWidget$DimensionBehaviour.MATCH_CONSTRAINT : Landroid/support/constraint/solver/widgets/ConstraintWidget$DimensionBehaviour;
    //   403: if_acmpeq -> 412
    //   406: iconst_1
    //   407: istore #16
    //   409: goto -> 415
    //   412: iconst_0
    //   413: istore #16
    //   415: iconst_0
    //   416: istore #7
    //   418: aload_0
    //   419: aload_0
    //   420: getfield mDimensionRatioSide : I
    //   423: putfield mResolvedDimensionRatioSide : I
    //   426: aload_0
    //   427: getfield mDimensionRatio : F
    //   430: fstore_2
    //   431: aload_0
    //   432: fload_2
    //   433: putfield mResolvedDimensionRatio : F
    //   436: aload_0
    //   437: getfield mMatchConstraintDefaultWidth : I
    //   440: istore #8
    //   442: aload_0
    //   443: getfield mMatchConstraintDefaultHeight : I
    //   446: istore #9
    //   448: fload_2
    //   449: fconst_0
    //   450: fcmpl
    //   451: ifle -> 776
    //   454: aload_0
    //   455: getfield mVisibility : I
    //   458: bipush #8
    //   460: if_icmpeq -> 776
    //   463: iconst_1
    //   464: istore #10
    //   466: iload #8
    //   468: istore_3
    //   469: aload_0
    //   470: getfield mListDimensionBehaviors : [Landroid/support/constraint/solver/widgets/ConstraintWidget$DimensionBehaviour;
    //   473: iconst_0
    //   474: aaload
    //   475: getstatic android/support/constraint/solver/widgets/ConstraintWidget$DimensionBehaviour.MATCH_CONSTRAINT : Landroid/support/constraint/solver/widgets/ConstraintWidget$DimensionBehaviour;
    //   478: if_acmpne -> 491
    //   481: iload #8
    //   483: istore_3
    //   484: iload #8
    //   486: ifne -> 491
    //   489: iconst_3
    //   490: istore_3
    //   491: iload #9
    //   493: istore #4
    //   495: aload_0
    //   496: getfield mListDimensionBehaviors : [Landroid/support/constraint/solver/widgets/ConstraintWidget$DimensionBehaviour;
    //   499: iconst_1
    //   500: aaload
    //   501: getstatic android/support/constraint/solver/widgets/ConstraintWidget$DimensionBehaviour.MATCH_CONSTRAINT : Landroid/support/constraint/solver/widgets/ConstraintWidget$DimensionBehaviour;
    //   504: if_acmpne -> 519
    //   507: iload #9
    //   509: istore #4
    //   511: iload #9
    //   513: ifne -> 519
    //   516: iconst_3
    //   517: istore #4
    //   519: aload_0
    //   520: getfield mListDimensionBehaviors : [Landroid/support/constraint/solver/widgets/ConstraintWidget$DimensionBehaviour;
    //   523: iconst_0
    //   524: aaload
    //   525: getstatic android/support/constraint/solver/widgets/ConstraintWidget$DimensionBehaviour.MATCH_CONSTRAINT : Landroid/support/constraint/solver/widgets/ConstraintWidget$DimensionBehaviour;
    //   528: if_acmpne -> 580
    //   531: aload_0
    //   532: getfield mListDimensionBehaviors : [Landroid/support/constraint/solver/widgets/ConstraintWidget$DimensionBehaviour;
    //   535: iconst_1
    //   536: aaload
    //   537: getstatic android/support/constraint/solver/widgets/ConstraintWidget$DimensionBehaviour.MATCH_CONSTRAINT : Landroid/support/constraint/solver/widgets/ConstraintWidget$DimensionBehaviour;
    //   540: if_acmpne -> 580
    //   543: iload_3
    //   544: iconst_3
    //   545: if_icmpne -> 580
    //   548: iload #4
    //   550: iconst_3
    //   551: if_icmpne -> 580
    //   554: aload_0
    //   555: iload #14
    //   557: iload #12
    //   559: iload #11
    //   561: iload #16
    //   563: invokevirtual setupDimensionRatio : (ZZZZ)V
    //   566: iload #4
    //   568: istore #9
    //   570: iload_3
    //   571: istore #8
    //   573: iload #10
    //   575: istore #7
    //   577: goto -> 776
    //   580: aload_0
    //   581: getfield mListDimensionBehaviors : [Landroid/support/constraint/solver/widgets/ConstraintWidget$DimensionBehaviour;
    //   584: iconst_0
    //   585: aaload
    //   586: getstatic android/support/constraint/solver/widgets/ConstraintWidget$DimensionBehaviour.MATCH_CONSTRAINT : Landroid/support/constraint/solver/widgets/ConstraintWidget$DimensionBehaviour;
    //   589: if_acmpne -> 665
    //   592: iload_3
    //   593: iconst_3
    //   594: if_icmpne -> 665
    //   597: aload_0
    //   598: iconst_0
    //   599: putfield mResolvedDimensionRatioSide : I
    //   602: aload_0
    //   603: getfield mResolvedDimensionRatio : F
    //   606: aload_0
    //   607: getfield mHeight : I
    //   610: i2f
    //   611: fmul
    //   612: f2i
    //   613: istore #7
    //   615: aload_0
    //   616: getfield mListDimensionBehaviors : [Landroid/support/constraint/solver/widgets/ConstraintWidget$DimensionBehaviour;
    //   619: iconst_1
    //   620: aaload
    //   621: getstatic android/support/constraint/solver/widgets/ConstraintWidget$DimensionBehaviour.MATCH_CONSTRAINT : Landroid/support/constraint/solver/widgets/ConstraintWidget$DimensionBehaviour;
    //   624: if_acmpeq -> 647
    //   627: iconst_4
    //   628: istore_3
    //   629: iload #6
    //   631: istore #8
    //   633: iconst_0
    //   634: istore #5
    //   636: iload #7
    //   638: istore #6
    //   640: iload #8
    //   642: istore #7
    //   644: goto -> 799
    //   647: iload #6
    //   649: istore #8
    //   651: iconst_1
    //   652: istore #5
    //   654: iload #7
    //   656: istore #6
    //   658: iload #8
    //   660: istore #7
    //   662: goto -> 799
    //   665: iload #4
    //   667: istore #9
    //   669: iload_3
    //   670: istore #8
    //   672: iload #10
    //   674: istore #7
    //   676: aload_0
    //   677: getfield mListDimensionBehaviors : [Landroid/support/constraint/solver/widgets/ConstraintWidget$DimensionBehaviour;
    //   680: iconst_1
    //   681: aaload
    //   682: getstatic android/support/constraint/solver/widgets/ConstraintWidget$DimensionBehaviour.MATCH_CONSTRAINT : Landroid/support/constraint/solver/widgets/ConstraintWidget$DimensionBehaviour;
    //   685: if_acmpne -> 776
    //   688: iload #4
    //   690: istore #9
    //   692: iload_3
    //   693: istore #8
    //   695: iload #10
    //   697: istore #7
    //   699: iload #4
    //   701: iconst_3
    //   702: if_icmpne -> 776
    //   705: aload_0
    //   706: iconst_1
    //   707: putfield mResolvedDimensionRatioSide : I
    //   710: aload_0
    //   711: getfield mDimensionRatioSide : I
    //   714: iconst_m1
    //   715: if_icmpne -> 728
    //   718: aload_0
    //   719: fconst_1
    //   720: aload_0
    //   721: getfield mResolvedDimensionRatio : F
    //   724: fdiv
    //   725: putfield mResolvedDimensionRatio : F
    //   728: aload_0
    //   729: getfield mResolvedDimensionRatio : F
    //   732: aload_0
    //   733: getfield mWidth : I
    //   736: i2f
    //   737: fmul
    //   738: f2i
    //   739: istore #7
    //   741: aload_0
    //   742: getfield mListDimensionBehaviors : [Landroid/support/constraint/solver/widgets/ConstraintWidget$DimensionBehaviour;
    //   745: iconst_0
    //   746: aaload
    //   747: getstatic android/support/constraint/solver/widgets/ConstraintWidget$DimensionBehaviour.MATCH_CONSTRAINT : Landroid/support/constraint/solver/widgets/ConstraintWidget$DimensionBehaviour;
    //   750: if_acmpeq -> 766
    //   753: iconst_4
    //   754: istore #4
    //   756: iload #5
    //   758: istore #6
    //   760: iconst_0
    //   761: istore #5
    //   763: goto -> 799
    //   766: iload #5
    //   768: istore #6
    //   770: iconst_1
    //   771: istore #5
    //   773: goto -> 799
    //   776: iload #9
    //   778: istore #4
    //   780: iload #8
    //   782: istore_3
    //   783: iload #7
    //   785: istore #8
    //   787: iload #6
    //   789: istore #7
    //   791: iload #5
    //   793: istore #6
    //   795: iload #8
    //   797: istore #5
    //   799: aload_0
    //   800: getfield mResolvedMatchConstraintDefault : [I
    //   803: astore #19
    //   805: aload #19
    //   807: iconst_0
    //   808: iload_3
    //   809: iastore
    //   810: aload #19
    //   812: iconst_1
    //   813: iload #4
    //   815: iastore
    //   816: iload #5
    //   818: ifeq -> 847
    //   821: aload_0
    //   822: getfield mResolvedDimensionRatioSide : I
    //   825: istore #8
    //   827: iload #8
    //   829: ifeq -> 841
    //   832: iload #8
    //   834: iconst_m1
    //   835: if_icmpne -> 847
    //   838: goto -> 841
    //   841: iconst_1
    //   842: istore #16
    //   844: goto -> 850
    //   847: iconst_0
    //   848: istore #16
    //   850: aload_0
    //   851: getfield mListDimensionBehaviors : [Landroid/support/constraint/solver/widgets/ConstraintWidget$DimensionBehaviour;
    //   854: iconst_0
    //   855: aaload
    //   856: getstatic android/support/constraint/solver/widgets/ConstraintWidget$DimensionBehaviour.WRAP_CONTENT : Landroid/support/constraint/solver/widgets/ConstraintWidget$DimensionBehaviour;
    //   859: if_acmpne -> 875
    //   862: aload_0
    //   863: instanceof android/support/constraint/solver/widgets/ConstraintWidgetContainer
    //   866: ifeq -> 875
    //   869: iconst_1
    //   870: istore #17
    //   872: goto -> 878
    //   875: iconst_0
    //   876: istore #17
    //   878: aload_0
    //   879: getfield mCenter : Landroid/support/constraint/solver/widgets/ConstraintAnchor;
    //   882: invokevirtual isConnected : ()Z
    //   885: ifeq -> 894
    //   888: iconst_0
    //   889: istore #11
    //   891: goto -> 897
    //   894: iconst_1
    //   895: istore #11
    //   897: aload_0
    //   898: getfield mHorizontalResolution : I
    //   901: iconst_2
    //   902: if_icmpeq -> 1030
    //   905: aload_0
    //   906: getfield mParent : Landroid/support/constraint/solver/widgets/ConstraintWidget;
    //   909: astore #19
    //   911: aload #19
    //   913: ifnull -> 930
    //   916: aload_1
    //   917: aload #19
    //   919: getfield mRight : Landroid/support/constraint/solver/widgets/ConstraintAnchor;
    //   922: invokevirtual createObjectVariable : (Ljava/lang/Object;)Landroid/support/constraint/solver/SolverVariable;
    //   925: astore #19
    //   927: goto -> 933
    //   930: aconst_null
    //   931: astore #19
    //   933: aload_0
    //   934: getfield mParent : Landroid/support/constraint/solver/widgets/ConstraintWidget;
    //   937: astore #20
    //   939: aload #20
    //   941: ifnull -> 958
    //   944: aload_1
    //   945: aload #20
    //   947: getfield mLeft : Landroid/support/constraint/solver/widgets/ConstraintAnchor;
    //   950: invokevirtual createObjectVariable : (Ljava/lang/Object;)Landroid/support/constraint/solver/SolverVariable;
    //   953: astore #20
    //   955: goto -> 961
    //   958: aconst_null
    //   959: astore #20
    //   961: aload_0
    //   962: aload_1
    //   963: iload #14
    //   965: aload #20
    //   967: aload #19
    //   969: aload_0
    //   970: getfield mListDimensionBehaviors : [Landroid/support/constraint/solver/widgets/ConstraintWidget$DimensionBehaviour;
    //   973: iconst_0
    //   974: aaload
    //   975: iload #17
    //   977: aload_0
    //   978: getfield mLeft : Landroid/support/constraint/solver/widgets/ConstraintAnchor;
    //   981: aload_0
    //   982: getfield mRight : Landroid/support/constraint/solver/widgets/ConstraintAnchor;
    //   985: aload_0
    //   986: getfield mX : I
    //   989: iload #6
    //   991: aload_0
    //   992: getfield mMinWidth : I
    //   995: aload_0
    //   996: getfield mMaxDimension : [I
    //   999: iconst_0
    //   1000: iaload
    //   1001: aload_0
    //   1002: getfield mHorizontalBiasPercent : F
    //   1005: iload #16
    //   1007: iload #15
    //   1009: iload_3
    //   1010: aload_0
    //   1011: getfield mMatchConstraintMinWidth : I
    //   1014: aload_0
    //   1015: getfield mMatchConstraintMaxWidth : I
    //   1018: aload_0
    //   1019: getfield mMatchConstraintPercentWidth : F
    //   1022: iload #11
    //   1024: invokespecial applyConstraints : (Landroid/support/constraint/solver/LinearSystem;ZLandroid/support/constraint/solver/SolverVariable;Landroid/support/constraint/solver/SolverVariable;Landroid/support/constraint/solver/widgets/ConstraintWidget$DimensionBehaviour;ZLandroid/support/constraint/solver/widgets/ConstraintAnchor;Landroid/support/constraint/solver/widgets/ConstraintAnchor;IIIIFZZIIIFZ)V
    //   1027: goto -> 1030
    //   1030: aload #23
    //   1032: astore #20
    //   1034: aload #22
    //   1036: astore #19
    //   1038: aload #18
    //   1040: astore #22
    //   1042: aload_0
    //   1043: getfield mVerticalResolution : I
    //   1046: iconst_2
    //   1047: if_icmpne -> 1051
    //   1050: return
    //   1051: aload_0
    //   1052: getfield mListDimensionBehaviors : [Landroid/support/constraint/solver/widgets/ConstraintWidget$DimensionBehaviour;
    //   1055: iconst_1
    //   1056: aaload
    //   1057: getstatic android/support/constraint/solver/widgets/ConstraintWidget$DimensionBehaviour.WRAP_CONTENT : Landroid/support/constraint/solver/widgets/ConstraintWidget$DimensionBehaviour;
    //   1060: if_acmpne -> 1076
    //   1063: aload_0
    //   1064: instanceof android/support/constraint/solver/widgets/ConstraintWidgetContainer
    //   1067: ifeq -> 1076
    //   1070: iconst_1
    //   1071: istore #14
    //   1073: goto -> 1079
    //   1076: iconst_0
    //   1077: istore #14
    //   1079: iload #5
    //   1081: ifeq -> 1105
    //   1084: aload_0
    //   1085: getfield mResolvedDimensionRatioSide : I
    //   1088: istore_3
    //   1089: iload_3
    //   1090: iconst_1
    //   1091: if_icmpeq -> 1099
    //   1094: iload_3
    //   1095: iconst_m1
    //   1096: if_icmpne -> 1105
    //   1099: iconst_1
    //   1100: istore #15
    //   1102: goto -> 1108
    //   1105: iconst_0
    //   1106: istore #15
    //   1108: aload_0
    //   1109: getfield mBaselineDistance : I
    //   1112: ifle -> 1201
    //   1115: aload_0
    //   1116: getfield mBaseline : Landroid/support/constraint/solver/widgets/ConstraintAnchor;
    //   1119: invokevirtual getResolutionNode : ()Landroid/support/constraint/solver/widgets/ResolutionAnchor;
    //   1122: getfield state : I
    //   1125: iconst_1
    //   1126: if_icmpne -> 1143
    //   1129: aload_0
    //   1130: getfield mBaseline : Landroid/support/constraint/solver/widgets/ConstraintAnchor;
    //   1133: invokevirtual getResolutionNode : ()Landroid/support/constraint/solver/widgets/ResolutionAnchor;
    //   1136: aload_1
    //   1137: invokevirtual addResolvedValue : (Landroid/support/constraint/solver/LinearSystem;)V
    //   1140: goto -> 1201
    //   1143: aload_1
    //   1144: astore #18
    //   1146: aload #18
    //   1148: aload #21
    //   1150: aload #19
    //   1152: aload_0
    //   1153: invokevirtual getBaselineDistance : ()I
    //   1156: bipush #6
    //   1158: invokevirtual addEquality : (Landroid/support/constraint/solver/SolverVariable;Landroid/support/constraint/solver/SolverVariable;II)Landroid/support/constraint/solver/ArrayRow;
    //   1161: pop
    //   1162: aload_0
    //   1163: getfield mBaseline : Landroid/support/constraint/solver/widgets/ConstraintAnchor;
    //   1166: getfield mTarget : Landroid/support/constraint/solver/widgets/ConstraintAnchor;
    //   1169: ifnull -> 1201
    //   1172: aload #18
    //   1174: aload #21
    //   1176: aload #18
    //   1178: aload_0
    //   1179: getfield mBaseline : Landroid/support/constraint/solver/widgets/ConstraintAnchor;
    //   1182: getfield mTarget : Landroid/support/constraint/solver/widgets/ConstraintAnchor;
    //   1185: invokevirtual createObjectVariable : (Ljava/lang/Object;)Landroid/support/constraint/solver/SolverVariable;
    //   1188: iconst_0
    //   1189: bipush #6
    //   1191: invokevirtual addEquality : (Landroid/support/constraint/solver/SolverVariable;Landroid/support/constraint/solver/SolverVariable;II)Landroid/support/constraint/solver/ArrayRow;
    //   1194: pop
    //   1195: iconst_0
    //   1196: istore #11
    //   1198: goto -> 1201
    //   1201: aload #19
    //   1203: astore #23
    //   1205: aload_1
    //   1206: astore #21
    //   1208: aload_0
    //   1209: getfield mParent : Landroid/support/constraint/solver/widgets/ConstraintWidget;
    //   1212: astore #18
    //   1214: aload #18
    //   1216: ifnull -> 1234
    //   1219: aload #21
    //   1221: aload #18
    //   1223: getfield mBottom : Landroid/support/constraint/solver/widgets/ConstraintAnchor;
    //   1226: invokevirtual createObjectVariable : (Ljava/lang/Object;)Landroid/support/constraint/solver/SolverVariable;
    //   1229: astore #18
    //   1231: goto -> 1237
    //   1234: aconst_null
    //   1235: astore #18
    //   1237: aload_0
    //   1238: getfield mParent : Landroid/support/constraint/solver/widgets/ConstraintWidget;
    //   1241: astore #19
    //   1243: aload #19
    //   1245: ifnull -> 1263
    //   1248: aload #21
    //   1250: aload #19
    //   1252: getfield mTop : Landroid/support/constraint/solver/widgets/ConstraintAnchor;
    //   1255: invokevirtual createObjectVariable : (Ljava/lang/Object;)Landroid/support/constraint/solver/SolverVariable;
    //   1258: astore #19
    //   1260: goto -> 1266
    //   1263: aconst_null
    //   1264: astore #19
    //   1266: aload_0
    //   1267: aload_1
    //   1268: iload #12
    //   1270: aload #19
    //   1272: aload #18
    //   1274: aload_0
    //   1275: getfield mListDimensionBehaviors : [Landroid/support/constraint/solver/widgets/ConstraintWidget$DimensionBehaviour;
    //   1278: iconst_1
    //   1279: aaload
    //   1280: iload #14
    //   1282: aload_0
    //   1283: getfield mTop : Landroid/support/constraint/solver/widgets/ConstraintAnchor;
    //   1286: aload_0
    //   1287: getfield mBottom : Landroid/support/constraint/solver/widgets/ConstraintAnchor;
    //   1290: aload_0
    //   1291: getfield mY : I
    //   1294: iload #7
    //   1296: aload_0
    //   1297: getfield mMinHeight : I
    //   1300: aload_0
    //   1301: getfield mMaxDimension : [I
    //   1304: iconst_1
    //   1305: iaload
    //   1306: aload_0
    //   1307: getfield mVerticalBiasPercent : F
    //   1310: iload #15
    //   1312: iload #13
    //   1314: iload #4
    //   1316: aload_0
    //   1317: getfield mMatchConstraintMinHeight : I
    //   1320: aload_0
    //   1321: getfield mMatchConstraintMaxHeight : I
    //   1324: aload_0
    //   1325: getfield mMatchConstraintPercentHeight : F
    //   1328: iload #11
    //   1330: invokespecial applyConstraints : (Landroid/support/constraint/solver/LinearSystem;ZLandroid/support/constraint/solver/SolverVariable;Landroid/support/constraint/solver/SolverVariable;Landroid/support/constraint/solver/widgets/ConstraintWidget$DimensionBehaviour;ZLandroid/support/constraint/solver/widgets/ConstraintAnchor;Landroid/support/constraint/solver/widgets/ConstraintAnchor;IIIIFZZIIIFZ)V
    //   1333: iload #5
    //   1335: ifeq -> 1385
    //   1338: aload_0
    //   1339: getfield mResolvedDimensionRatioSide : I
    //   1342: iconst_1
    //   1343: if_icmpne -> 1367
    //   1346: aload_1
    //   1347: aload #22
    //   1349: aload #23
    //   1351: aload #20
    //   1353: aload #24
    //   1355: aload_0
    //   1356: getfield mResolvedDimensionRatio : F
    //   1359: bipush #6
    //   1361: invokevirtual addRatio : (Landroid/support/constraint/solver/SolverVariable;Landroid/support/constraint/solver/SolverVariable;Landroid/support/constraint/solver/SolverVariable;Landroid/support/constraint/solver/SolverVariable;FI)V
    //   1364: goto -> 1385
    //   1367: aload_1
    //   1368: aload #20
    //   1370: aload #24
    //   1372: aload #22
    //   1374: aload #23
    //   1376: aload_0
    //   1377: getfield mResolvedDimensionRatio : F
    //   1380: bipush #6
    //   1382: invokevirtual addRatio : (Landroid/support/constraint/solver/SolverVariable;Landroid/support/constraint/solver/SolverVariable;Landroid/support/constraint/solver/SolverVariable;Landroid/support/constraint/solver/SolverVariable;FI)V
    //   1385: aload_0
    //   1386: getfield mCenter : Landroid/support/constraint/solver/widgets/ConstraintAnchor;
    //   1389: invokevirtual isConnected : ()Z
    //   1392: ifeq -> 1431
    //   1395: aload #21
    //   1397: aload_0
    //   1398: aload_0
    //   1399: getfield mCenter : Landroid/support/constraint/solver/widgets/ConstraintAnchor;
    //   1402: invokevirtual getTarget : ()Landroid/support/constraint/solver/widgets/ConstraintAnchor;
    //   1405: invokevirtual getOwner : ()Landroid/support/constraint/solver/widgets/ConstraintWidget;
    //   1408: aload_0
    //   1409: getfield mCircleConstraintAngle : F
    //   1412: ldc_w 90.0
    //   1415: fadd
    //   1416: f2d
    //   1417: invokestatic toRadians : (D)D
    //   1420: d2f
    //   1421: aload_0
    //   1422: getfield mCenter : Landroid/support/constraint/solver/widgets/ConstraintAnchor;
    //   1425: invokevirtual getMargin : ()I
    //   1428: invokevirtual addCenterPoint : (Landroid/support/constraint/solver/widgets/ConstraintWidget;Landroid/support/constraint/solver/widgets/ConstraintWidget;FI)V
    //   1431: return }
  
  public boolean allowedInBarrier() { return (this.mVisibility != 8); }
  
  public void analyze(int paramInt) { Optimizer.analyze(paramInt, this); }
  
  public void connect(ConstraintAnchor.Type paramType1, ConstraintWidget paramConstraintWidget, ConstraintAnchor.Type paramType2) { connect(paramType1, paramConstraintWidget, paramType2, 0, ConstraintAnchor.Strength.STRONG); }
  
  public void connect(ConstraintAnchor.Type paramType1, ConstraintWidget paramConstraintWidget, ConstraintAnchor.Type paramType2, int paramInt) { connect(paramType1, paramConstraintWidget, paramType2, paramInt, ConstraintAnchor.Strength.STRONG); }
  
  public void connect(ConstraintAnchor.Type paramType1, ConstraintWidget paramConstraintWidget, ConstraintAnchor.Type paramType2, int paramInt, ConstraintAnchor.Strength paramStrength) { connect(paramType1, paramConstraintWidget, paramType2, paramInt, paramStrength, 0); }
  
  public void connect(ConstraintAnchor.Type paramType1, ConstraintWidget paramConstraintWidget, ConstraintAnchor.Type paramType2, int paramInt1, ConstraintAnchor.Strength paramStrength, int paramInt2) { // Byte code:
    //   0: aload_1
    //   1: getstatic android/support/constraint/solver/widgets/ConstraintAnchor$Type.CENTER : Landroid/support/constraint/solver/widgets/ConstraintAnchor$Type;
    //   4: if_acmpne -> 422
    //   7: aload_3
    //   8: getstatic android/support/constraint/solver/widgets/ConstraintAnchor$Type.CENTER : Landroid/support/constraint/solver/widgets/ConstraintAnchor$Type;
    //   11: if_acmpne -> 289
    //   14: aload_0
    //   15: getstatic android/support/constraint/solver/widgets/ConstraintAnchor$Type.LEFT : Landroid/support/constraint/solver/widgets/ConstraintAnchor$Type;
    //   18: invokevirtual getAnchor : (Landroid/support/constraint/solver/widgets/ConstraintAnchor$Type;)Landroid/support/constraint/solver/widgets/ConstraintAnchor;
    //   21: astore_1
    //   22: aload_0
    //   23: getstatic android/support/constraint/solver/widgets/ConstraintAnchor$Type.RIGHT : Landroid/support/constraint/solver/widgets/ConstraintAnchor$Type;
    //   26: invokevirtual getAnchor : (Landroid/support/constraint/solver/widgets/ConstraintAnchor$Type;)Landroid/support/constraint/solver/widgets/ConstraintAnchor;
    //   29: astore_3
    //   30: aload_0
    //   31: getstatic android/support/constraint/solver/widgets/ConstraintAnchor$Type.TOP : Landroid/support/constraint/solver/widgets/ConstraintAnchor$Type;
    //   34: invokevirtual getAnchor : (Landroid/support/constraint/solver/widgets/ConstraintAnchor$Type;)Landroid/support/constraint/solver/widgets/ConstraintAnchor;
    //   37: astore #9
    //   39: aload_0
    //   40: getstatic android/support/constraint/solver/widgets/ConstraintAnchor$Type.BOTTOM : Landroid/support/constraint/solver/widgets/ConstraintAnchor$Type;
    //   43: invokevirtual getAnchor : (Landroid/support/constraint/solver/widgets/ConstraintAnchor$Type;)Landroid/support/constraint/solver/widgets/ConstraintAnchor;
    //   46: astore #10
    //   48: iconst_0
    //   49: istore #7
    //   51: iconst_0
    //   52: istore #8
    //   54: aload_1
    //   55: ifnull -> 69
    //   58: iload #7
    //   60: istore #4
    //   62: aload_1
    //   63: invokevirtual isConnected : ()Z
    //   66: ifne -> 122
    //   69: aload_3
    //   70: ifnull -> 87
    //   73: aload_3
    //   74: invokevirtual isConnected : ()Z
    //   77: ifeq -> 87
    //   80: iload #7
    //   82: istore #4
    //   84: goto -> 122
    //   87: aload_0
    //   88: getstatic android/support/constraint/solver/widgets/ConstraintAnchor$Type.LEFT : Landroid/support/constraint/solver/widgets/ConstraintAnchor$Type;
    //   91: aload_2
    //   92: getstatic android/support/constraint/solver/widgets/ConstraintAnchor$Type.LEFT : Landroid/support/constraint/solver/widgets/ConstraintAnchor$Type;
    //   95: iconst_0
    //   96: aload #5
    //   98: iload #6
    //   100: invokevirtual connect : (Landroid/support/constraint/solver/widgets/ConstraintAnchor$Type;Landroid/support/constraint/solver/widgets/ConstraintWidget;Landroid/support/constraint/solver/widgets/ConstraintAnchor$Type;ILandroid/support/constraint/solver/widgets/ConstraintAnchor$Strength;I)V
    //   103: aload_0
    //   104: getstatic android/support/constraint/solver/widgets/ConstraintAnchor$Type.RIGHT : Landroid/support/constraint/solver/widgets/ConstraintAnchor$Type;
    //   107: aload_2
    //   108: getstatic android/support/constraint/solver/widgets/ConstraintAnchor$Type.RIGHT : Landroid/support/constraint/solver/widgets/ConstraintAnchor$Type;
    //   111: iconst_0
    //   112: aload #5
    //   114: iload #6
    //   116: invokevirtual connect : (Landroid/support/constraint/solver/widgets/ConstraintAnchor$Type;Landroid/support/constraint/solver/widgets/ConstraintWidget;Landroid/support/constraint/solver/widgets/ConstraintAnchor$Type;ILandroid/support/constraint/solver/widgets/ConstraintAnchor$Strength;I)V
    //   119: iconst_1
    //   120: istore #4
    //   122: aload #9
    //   124: ifnull -> 139
    //   127: iload #8
    //   129: istore #7
    //   131: aload #9
    //   133: invokevirtual isConnected : ()Z
    //   136: ifne -> 194
    //   139: aload #10
    //   141: ifnull -> 159
    //   144: aload #10
    //   146: invokevirtual isConnected : ()Z
    //   149: ifeq -> 159
    //   152: iload #8
    //   154: istore #7
    //   156: goto -> 194
    //   159: aload_0
    //   160: getstatic android/support/constraint/solver/widgets/ConstraintAnchor$Type.TOP : Landroid/support/constraint/solver/widgets/ConstraintAnchor$Type;
    //   163: aload_2
    //   164: getstatic android/support/constraint/solver/widgets/ConstraintAnchor$Type.TOP : Landroid/support/constraint/solver/widgets/ConstraintAnchor$Type;
    //   167: iconst_0
    //   168: aload #5
    //   170: iload #6
    //   172: invokevirtual connect : (Landroid/support/constraint/solver/widgets/ConstraintAnchor$Type;Landroid/support/constraint/solver/widgets/ConstraintWidget;Landroid/support/constraint/solver/widgets/ConstraintAnchor$Type;ILandroid/support/constraint/solver/widgets/ConstraintAnchor$Strength;I)V
    //   175: aload_0
    //   176: getstatic android/support/constraint/solver/widgets/ConstraintAnchor$Type.BOTTOM : Landroid/support/constraint/solver/widgets/ConstraintAnchor$Type;
    //   179: aload_2
    //   180: getstatic android/support/constraint/solver/widgets/ConstraintAnchor$Type.BOTTOM : Landroid/support/constraint/solver/widgets/ConstraintAnchor$Type;
    //   183: iconst_0
    //   184: aload #5
    //   186: iload #6
    //   188: invokevirtual connect : (Landroid/support/constraint/solver/widgets/ConstraintAnchor$Type;Landroid/support/constraint/solver/widgets/ConstraintWidget;Landroid/support/constraint/solver/widgets/ConstraintAnchor$Type;ILandroid/support/constraint/solver/widgets/ConstraintAnchor$Strength;I)V
    //   191: iconst_1
    //   192: istore #7
    //   194: iload #4
    //   196: ifeq -> 228
    //   199: iload #7
    //   201: ifeq -> 228
    //   204: aload_0
    //   205: getstatic android/support/constraint/solver/widgets/ConstraintAnchor$Type.CENTER : Landroid/support/constraint/solver/widgets/ConstraintAnchor$Type;
    //   208: invokevirtual getAnchor : (Landroid/support/constraint/solver/widgets/ConstraintAnchor$Type;)Landroid/support/constraint/solver/widgets/ConstraintAnchor;
    //   211: aload_2
    //   212: getstatic android/support/constraint/solver/widgets/ConstraintAnchor$Type.CENTER : Landroid/support/constraint/solver/widgets/ConstraintAnchor$Type;
    //   215: invokevirtual getAnchor : (Landroid/support/constraint/solver/widgets/ConstraintAnchor$Type;)Landroid/support/constraint/solver/widgets/ConstraintAnchor;
    //   218: iconst_0
    //   219: iload #6
    //   221: invokevirtual connect : (Landroid/support/constraint/solver/widgets/ConstraintAnchor;II)Z
    //   224: pop
    //   225: goto -> 286
    //   228: iload #4
    //   230: ifeq -> 257
    //   233: aload_0
    //   234: getstatic android/support/constraint/solver/widgets/ConstraintAnchor$Type.CENTER_X : Landroid/support/constraint/solver/widgets/ConstraintAnchor$Type;
    //   237: invokevirtual getAnchor : (Landroid/support/constraint/solver/widgets/ConstraintAnchor$Type;)Landroid/support/constraint/solver/widgets/ConstraintAnchor;
    //   240: aload_2
    //   241: getstatic android/support/constraint/solver/widgets/ConstraintAnchor$Type.CENTER_X : Landroid/support/constraint/solver/widgets/ConstraintAnchor$Type;
    //   244: invokevirtual getAnchor : (Landroid/support/constraint/solver/widgets/ConstraintAnchor$Type;)Landroid/support/constraint/solver/widgets/ConstraintAnchor;
    //   247: iconst_0
    //   248: iload #6
    //   250: invokevirtual connect : (Landroid/support/constraint/solver/widgets/ConstraintAnchor;II)Z
    //   253: pop
    //   254: goto -> 286
    //   257: iload #7
    //   259: ifeq -> 286
    //   262: aload_0
    //   263: getstatic android/support/constraint/solver/widgets/ConstraintAnchor$Type.CENTER_Y : Landroid/support/constraint/solver/widgets/ConstraintAnchor$Type;
    //   266: invokevirtual getAnchor : (Landroid/support/constraint/solver/widgets/ConstraintAnchor$Type;)Landroid/support/constraint/solver/widgets/ConstraintAnchor;
    //   269: aload_2
    //   270: getstatic android/support/constraint/solver/widgets/ConstraintAnchor$Type.CENTER_Y : Landroid/support/constraint/solver/widgets/ConstraintAnchor$Type;
    //   273: invokevirtual getAnchor : (Landroid/support/constraint/solver/widgets/ConstraintAnchor$Type;)Landroid/support/constraint/solver/widgets/ConstraintAnchor;
    //   276: iconst_0
    //   277: iload #6
    //   279: invokevirtual connect : (Landroid/support/constraint/solver/widgets/ConstraintAnchor;II)Z
    //   282: pop
    //   283: goto -> 286
    //   286: goto -> 988
    //   289: aload_3
    //   290: getstatic android/support/constraint/solver/widgets/ConstraintAnchor$Type.LEFT : Landroid/support/constraint/solver/widgets/ConstraintAnchor$Type;
    //   293: if_acmpeq -> 370
    //   296: aload_3
    //   297: getstatic android/support/constraint/solver/widgets/ConstraintAnchor$Type.RIGHT : Landroid/support/constraint/solver/widgets/ConstraintAnchor$Type;
    //   300: if_acmpne -> 306
    //   303: goto -> 370
    //   306: aload_3
    //   307: getstatic android/support/constraint/solver/widgets/ConstraintAnchor$Type.TOP : Landroid/support/constraint/solver/widgets/ConstraintAnchor$Type;
    //   310: if_acmpeq -> 320
    //   313: aload_3
    //   314: getstatic android/support/constraint/solver/widgets/ConstraintAnchor$Type.BOTTOM : Landroid/support/constraint/solver/widgets/ConstraintAnchor$Type;
    //   317: if_acmpne -> 419
    //   320: aload_0
    //   321: getstatic android/support/constraint/solver/widgets/ConstraintAnchor$Type.TOP : Landroid/support/constraint/solver/widgets/ConstraintAnchor$Type;
    //   324: aload_2
    //   325: aload_3
    //   326: iconst_0
    //   327: aload #5
    //   329: iload #6
    //   331: invokevirtual connect : (Landroid/support/constraint/solver/widgets/ConstraintAnchor$Type;Landroid/support/constraint/solver/widgets/ConstraintWidget;Landroid/support/constraint/solver/widgets/ConstraintAnchor$Type;ILandroid/support/constraint/solver/widgets/ConstraintAnchor$Strength;I)V
    //   334: aload_0
    //   335: getstatic android/support/constraint/solver/widgets/ConstraintAnchor$Type.BOTTOM : Landroid/support/constraint/solver/widgets/ConstraintAnchor$Type;
    //   338: aload_2
    //   339: aload_3
    //   340: iconst_0
    //   341: aload #5
    //   343: iload #6
    //   345: invokevirtual connect : (Landroid/support/constraint/solver/widgets/ConstraintAnchor$Type;Landroid/support/constraint/solver/widgets/ConstraintWidget;Landroid/support/constraint/solver/widgets/ConstraintAnchor$Type;ILandroid/support/constraint/solver/widgets/ConstraintAnchor$Strength;I)V
    //   348: aload_0
    //   349: getstatic android/support/constraint/solver/widgets/ConstraintAnchor$Type.CENTER : Landroid/support/constraint/solver/widgets/ConstraintAnchor$Type;
    //   352: invokevirtual getAnchor : (Landroid/support/constraint/solver/widgets/ConstraintAnchor$Type;)Landroid/support/constraint/solver/widgets/ConstraintAnchor;
    //   355: aload_2
    //   356: aload_3
    //   357: invokevirtual getAnchor : (Landroid/support/constraint/solver/widgets/ConstraintAnchor$Type;)Landroid/support/constraint/solver/widgets/ConstraintAnchor;
    //   360: iconst_0
    //   361: iload #6
    //   363: invokevirtual connect : (Landroid/support/constraint/solver/widgets/ConstraintAnchor;II)Z
    //   366: pop
    //   367: goto -> 988
    //   370: aload_0
    //   371: getstatic android/support/constraint/solver/widgets/ConstraintAnchor$Type.LEFT : Landroid/support/constraint/solver/widgets/ConstraintAnchor$Type;
    //   374: aload_2
    //   375: aload_3
    //   376: iconst_0
    //   377: aload #5
    //   379: iload #6
    //   381: invokevirtual connect : (Landroid/support/constraint/solver/widgets/ConstraintAnchor$Type;Landroid/support/constraint/solver/widgets/ConstraintWidget;Landroid/support/constraint/solver/widgets/ConstraintAnchor$Type;ILandroid/support/constraint/solver/widgets/ConstraintAnchor$Strength;I)V
    //   384: getstatic android/support/constraint/solver/widgets/ConstraintAnchor$Type.RIGHT : Landroid/support/constraint/solver/widgets/ConstraintAnchor$Type;
    //   387: astore_1
    //   388: aload_0
    //   389: aload_1
    //   390: aload_2
    //   391: aload_3
    //   392: iconst_0
    //   393: aload #5
    //   395: iload #6
    //   397: invokevirtual connect : (Landroid/support/constraint/solver/widgets/ConstraintAnchor$Type;Landroid/support/constraint/solver/widgets/ConstraintWidget;Landroid/support/constraint/solver/widgets/ConstraintAnchor$Type;ILandroid/support/constraint/solver/widgets/ConstraintAnchor$Strength;I)V
    //   400: aload_0
    //   401: getstatic android/support/constraint/solver/widgets/ConstraintAnchor$Type.CENTER : Landroid/support/constraint/solver/widgets/ConstraintAnchor$Type;
    //   404: invokevirtual getAnchor : (Landroid/support/constraint/solver/widgets/ConstraintAnchor$Type;)Landroid/support/constraint/solver/widgets/ConstraintAnchor;
    //   407: aload_2
    //   408: aload_3
    //   409: invokevirtual getAnchor : (Landroid/support/constraint/solver/widgets/ConstraintAnchor$Type;)Landroid/support/constraint/solver/widgets/ConstraintAnchor;
    //   412: iconst_0
    //   413: iload #6
    //   415: invokevirtual connect : (Landroid/support/constraint/solver/widgets/ConstraintAnchor;II)Z
    //   418: pop
    //   419: goto -> 988
    //   422: aload_1
    //   423: getstatic android/support/constraint/solver/widgets/ConstraintAnchor$Type.CENTER_X : Landroid/support/constraint/solver/widgets/ConstraintAnchor$Type;
    //   426: if_acmpne -> 501
    //   429: aload_3
    //   430: getstatic android/support/constraint/solver/widgets/ConstraintAnchor$Type.LEFT : Landroid/support/constraint/solver/widgets/ConstraintAnchor$Type;
    //   433: if_acmpeq -> 443
    //   436: aload_3
    //   437: getstatic android/support/constraint/solver/widgets/ConstraintAnchor$Type.RIGHT : Landroid/support/constraint/solver/widgets/ConstraintAnchor$Type;
    //   440: if_acmpne -> 501
    //   443: aload_0
    //   444: getstatic android/support/constraint/solver/widgets/ConstraintAnchor$Type.LEFT : Landroid/support/constraint/solver/widgets/ConstraintAnchor$Type;
    //   447: invokevirtual getAnchor : (Landroid/support/constraint/solver/widgets/ConstraintAnchor$Type;)Landroid/support/constraint/solver/widgets/ConstraintAnchor;
    //   450: astore_1
    //   451: aload_2
    //   452: aload_3
    //   453: invokevirtual getAnchor : (Landroid/support/constraint/solver/widgets/ConstraintAnchor$Type;)Landroid/support/constraint/solver/widgets/ConstraintAnchor;
    //   456: astore_2
    //   457: aload_0
    //   458: getstatic android/support/constraint/solver/widgets/ConstraintAnchor$Type.RIGHT : Landroid/support/constraint/solver/widgets/ConstraintAnchor$Type;
    //   461: invokevirtual getAnchor : (Landroid/support/constraint/solver/widgets/ConstraintAnchor$Type;)Landroid/support/constraint/solver/widgets/ConstraintAnchor;
    //   464: astore_3
    //   465: aload_1
    //   466: aload_2
    //   467: iconst_0
    //   468: iload #6
    //   470: invokevirtual connect : (Landroid/support/constraint/solver/widgets/ConstraintAnchor;II)Z
    //   473: pop
    //   474: aload_3
    //   475: aload_2
    //   476: iconst_0
    //   477: iload #6
    //   479: invokevirtual connect : (Landroid/support/constraint/solver/widgets/ConstraintAnchor;II)Z
    //   482: pop
    //   483: aload_0
    //   484: getstatic android/support/constraint/solver/widgets/ConstraintAnchor$Type.CENTER_X : Landroid/support/constraint/solver/widgets/ConstraintAnchor$Type;
    //   487: invokevirtual getAnchor : (Landroid/support/constraint/solver/widgets/ConstraintAnchor$Type;)Landroid/support/constraint/solver/widgets/ConstraintAnchor;
    //   490: aload_2
    //   491: iconst_0
    //   492: iload #6
    //   494: invokevirtual connect : (Landroid/support/constraint/solver/widgets/ConstraintAnchor;II)Z
    //   497: pop
    //   498: goto -> 988
    //   501: aload_1
    //   502: getstatic android/support/constraint/solver/widgets/ConstraintAnchor$Type.CENTER_Y : Landroid/support/constraint/solver/widgets/ConstraintAnchor$Type;
    //   505: if_acmpne -> 576
    //   508: aload_3
    //   509: getstatic android/support/constraint/solver/widgets/ConstraintAnchor$Type.TOP : Landroid/support/constraint/solver/widgets/ConstraintAnchor$Type;
    //   512: if_acmpeq -> 522
    //   515: aload_3
    //   516: getstatic android/support/constraint/solver/widgets/ConstraintAnchor$Type.BOTTOM : Landroid/support/constraint/solver/widgets/ConstraintAnchor$Type;
    //   519: if_acmpne -> 576
    //   522: aload_2
    //   523: aload_3
    //   524: invokevirtual getAnchor : (Landroid/support/constraint/solver/widgets/ConstraintAnchor$Type;)Landroid/support/constraint/solver/widgets/ConstraintAnchor;
    //   527: astore_1
    //   528: aload_0
    //   529: getstatic android/support/constraint/solver/widgets/ConstraintAnchor$Type.TOP : Landroid/support/constraint/solver/widgets/ConstraintAnchor$Type;
    //   532: invokevirtual getAnchor : (Landroid/support/constraint/solver/widgets/ConstraintAnchor$Type;)Landroid/support/constraint/solver/widgets/ConstraintAnchor;
    //   535: aload_1
    //   536: iconst_0
    //   537: iload #6
    //   539: invokevirtual connect : (Landroid/support/constraint/solver/widgets/ConstraintAnchor;II)Z
    //   542: pop
    //   543: aload_0
    //   544: getstatic android/support/constraint/solver/widgets/ConstraintAnchor$Type.BOTTOM : Landroid/support/constraint/solver/widgets/ConstraintAnchor$Type;
    //   547: invokevirtual getAnchor : (Landroid/support/constraint/solver/widgets/ConstraintAnchor$Type;)Landroid/support/constraint/solver/widgets/ConstraintAnchor;
    //   550: aload_1
    //   551: iconst_0
    //   552: iload #6
    //   554: invokevirtual connect : (Landroid/support/constraint/solver/widgets/ConstraintAnchor;II)Z
    //   557: pop
    //   558: aload_0
    //   559: getstatic android/support/constraint/solver/widgets/ConstraintAnchor$Type.CENTER_Y : Landroid/support/constraint/solver/widgets/ConstraintAnchor$Type;
    //   562: invokevirtual getAnchor : (Landroid/support/constraint/solver/widgets/ConstraintAnchor$Type;)Landroid/support/constraint/solver/widgets/ConstraintAnchor;
    //   565: aload_1
    //   566: iconst_0
    //   567: iload #6
    //   569: invokevirtual connect : (Landroid/support/constraint/solver/widgets/ConstraintAnchor;II)Z
    //   572: pop
    //   573: goto -> 988
    //   576: aload_1
    //   577: getstatic android/support/constraint/solver/widgets/ConstraintAnchor$Type.CENTER_X : Landroid/support/constraint/solver/widgets/ConstraintAnchor$Type;
    //   580: if_acmpne -> 654
    //   583: aload_3
    //   584: getstatic android/support/constraint/solver/widgets/ConstraintAnchor$Type.CENTER_X : Landroid/support/constraint/solver/widgets/ConstraintAnchor$Type;
    //   587: if_acmpne -> 654
    //   590: aload_0
    //   591: getstatic android/support/constraint/solver/widgets/ConstraintAnchor$Type.LEFT : Landroid/support/constraint/solver/widgets/ConstraintAnchor$Type;
    //   594: invokevirtual getAnchor : (Landroid/support/constraint/solver/widgets/ConstraintAnchor$Type;)Landroid/support/constraint/solver/widgets/ConstraintAnchor;
    //   597: aload_2
    //   598: getstatic android/support/constraint/solver/widgets/ConstraintAnchor$Type.LEFT : Landroid/support/constraint/solver/widgets/ConstraintAnchor$Type;
    //   601: invokevirtual getAnchor : (Landroid/support/constraint/solver/widgets/ConstraintAnchor$Type;)Landroid/support/constraint/solver/widgets/ConstraintAnchor;
    //   604: iconst_0
    //   605: iload #6
    //   607: invokevirtual connect : (Landroid/support/constraint/solver/widgets/ConstraintAnchor;II)Z
    //   610: pop
    //   611: aload_0
    //   612: getstatic android/support/constraint/solver/widgets/ConstraintAnchor$Type.RIGHT : Landroid/support/constraint/solver/widgets/ConstraintAnchor$Type;
    //   615: invokevirtual getAnchor : (Landroid/support/constraint/solver/widgets/ConstraintAnchor$Type;)Landroid/support/constraint/solver/widgets/ConstraintAnchor;
    //   618: aload_2
    //   619: getstatic android/support/constraint/solver/widgets/ConstraintAnchor$Type.RIGHT : Landroid/support/constraint/solver/widgets/ConstraintAnchor$Type;
    //   622: invokevirtual getAnchor : (Landroid/support/constraint/solver/widgets/ConstraintAnchor$Type;)Landroid/support/constraint/solver/widgets/ConstraintAnchor;
    //   625: iconst_0
    //   626: iload #6
    //   628: invokevirtual connect : (Landroid/support/constraint/solver/widgets/ConstraintAnchor;II)Z
    //   631: pop
    //   632: aload_0
    //   633: getstatic android/support/constraint/solver/widgets/ConstraintAnchor$Type.CENTER_X : Landroid/support/constraint/solver/widgets/ConstraintAnchor$Type;
    //   636: invokevirtual getAnchor : (Landroid/support/constraint/solver/widgets/ConstraintAnchor$Type;)Landroid/support/constraint/solver/widgets/ConstraintAnchor;
    //   639: aload_2
    //   640: aload_3
    //   641: invokevirtual getAnchor : (Landroid/support/constraint/solver/widgets/ConstraintAnchor$Type;)Landroid/support/constraint/solver/widgets/ConstraintAnchor;
    //   644: iconst_0
    //   645: iload #6
    //   647: invokevirtual connect : (Landroid/support/constraint/solver/widgets/ConstraintAnchor;II)Z
    //   650: pop
    //   651: goto -> 988
    //   654: aload_1
    //   655: getstatic android/support/constraint/solver/widgets/ConstraintAnchor$Type.CENTER_Y : Landroid/support/constraint/solver/widgets/ConstraintAnchor$Type;
    //   658: if_acmpne -> 732
    //   661: aload_3
    //   662: getstatic android/support/constraint/solver/widgets/ConstraintAnchor$Type.CENTER_Y : Landroid/support/constraint/solver/widgets/ConstraintAnchor$Type;
    //   665: if_acmpne -> 732
    //   668: aload_0
    //   669: getstatic android/support/constraint/solver/widgets/ConstraintAnchor$Type.TOP : Landroid/support/constraint/solver/widgets/ConstraintAnchor$Type;
    //   672: invokevirtual getAnchor : (Landroid/support/constraint/solver/widgets/ConstraintAnchor$Type;)Landroid/support/constraint/solver/widgets/ConstraintAnchor;
    //   675: aload_2
    //   676: getstatic android/support/constraint/solver/widgets/ConstraintAnchor$Type.TOP : Landroid/support/constraint/solver/widgets/ConstraintAnchor$Type;
    //   679: invokevirtual getAnchor : (Landroid/support/constraint/solver/widgets/ConstraintAnchor$Type;)Landroid/support/constraint/solver/widgets/ConstraintAnchor;
    //   682: iconst_0
    //   683: iload #6
    //   685: invokevirtual connect : (Landroid/support/constraint/solver/widgets/ConstraintAnchor;II)Z
    //   688: pop
    //   689: aload_0
    //   690: getstatic android/support/constraint/solver/widgets/ConstraintAnchor$Type.BOTTOM : Landroid/support/constraint/solver/widgets/ConstraintAnchor$Type;
    //   693: invokevirtual getAnchor : (Landroid/support/constraint/solver/widgets/ConstraintAnchor$Type;)Landroid/support/constraint/solver/widgets/ConstraintAnchor;
    //   696: aload_2
    //   697: getstatic android/support/constraint/solver/widgets/ConstraintAnchor$Type.BOTTOM : Landroid/support/constraint/solver/widgets/ConstraintAnchor$Type;
    //   700: invokevirtual getAnchor : (Landroid/support/constraint/solver/widgets/ConstraintAnchor$Type;)Landroid/support/constraint/solver/widgets/ConstraintAnchor;
    //   703: iconst_0
    //   704: iload #6
    //   706: invokevirtual connect : (Landroid/support/constraint/solver/widgets/ConstraintAnchor;II)Z
    //   709: pop
    //   710: aload_0
    //   711: getstatic android/support/constraint/solver/widgets/ConstraintAnchor$Type.CENTER_Y : Landroid/support/constraint/solver/widgets/ConstraintAnchor$Type;
    //   714: invokevirtual getAnchor : (Landroid/support/constraint/solver/widgets/ConstraintAnchor$Type;)Landroid/support/constraint/solver/widgets/ConstraintAnchor;
    //   717: aload_2
    //   718: aload_3
    //   719: invokevirtual getAnchor : (Landroid/support/constraint/solver/widgets/ConstraintAnchor$Type;)Landroid/support/constraint/solver/widgets/ConstraintAnchor;
    //   722: iconst_0
    //   723: iload #6
    //   725: invokevirtual connect : (Landroid/support/constraint/solver/widgets/ConstraintAnchor;II)Z
    //   728: pop
    //   729: goto -> 988
    //   732: aload_0
    //   733: aload_1
    //   734: invokevirtual getAnchor : (Landroid/support/constraint/solver/widgets/ConstraintAnchor$Type;)Landroid/support/constraint/solver/widgets/ConstraintAnchor;
    //   737: astore #9
    //   739: aload_2
    //   740: aload_3
    //   741: invokevirtual getAnchor : (Landroid/support/constraint/solver/widgets/ConstraintAnchor$Type;)Landroid/support/constraint/solver/widgets/ConstraintAnchor;
    //   744: astore_2
    //   745: aload #9
    //   747: aload_2
    //   748: invokevirtual isValidConnection : (Landroid/support/constraint/solver/widgets/ConstraintAnchor;)Z
    //   751: ifeq -> 988
    //   754: aload_1
    //   755: getstatic android/support/constraint/solver/widgets/ConstraintAnchor$Type.BASELINE : Landroid/support/constraint/solver/widgets/ConstraintAnchor$Type;
    //   758: if_acmpne -> 799
    //   761: aload_0
    //   762: getstatic android/support/constraint/solver/widgets/ConstraintAnchor$Type.TOP : Landroid/support/constraint/solver/widgets/ConstraintAnchor$Type;
    //   765: invokevirtual getAnchor : (Landroid/support/constraint/solver/widgets/ConstraintAnchor$Type;)Landroid/support/constraint/solver/widgets/ConstraintAnchor;
    //   768: astore_1
    //   769: aload_0
    //   770: getstatic android/support/constraint/solver/widgets/ConstraintAnchor$Type.BOTTOM : Landroid/support/constraint/solver/widgets/ConstraintAnchor$Type;
    //   773: invokevirtual getAnchor : (Landroid/support/constraint/solver/widgets/ConstraintAnchor$Type;)Landroid/support/constraint/solver/widgets/ConstraintAnchor;
    //   776: astore_3
    //   777: aload_1
    //   778: ifnull -> 785
    //   781: aload_1
    //   782: invokevirtual reset : ()V
    //   785: aload_3
    //   786: ifnull -> 793
    //   789: aload_3
    //   790: invokevirtual reset : ()V
    //   793: iconst_0
    //   794: istore #4
    //   796: goto -> 962
    //   799: aload_1
    //   800: getstatic android/support/constraint/solver/widgets/ConstraintAnchor$Type.TOP : Landroid/support/constraint/solver/widgets/ConstraintAnchor$Type;
    //   803: if_acmpeq -> 891
    //   806: aload_1
    //   807: getstatic android/support/constraint/solver/widgets/ConstraintAnchor$Type.BOTTOM : Landroid/support/constraint/solver/widgets/ConstraintAnchor$Type;
    //   810: if_acmpne -> 816
    //   813: goto -> 891
    //   816: aload_1
    //   817: getstatic android/support/constraint/solver/widgets/ConstraintAnchor$Type.LEFT : Landroid/support/constraint/solver/widgets/ConstraintAnchor$Type;
    //   820: if_acmpeq -> 836
    //   823: aload_1
    //   824: getstatic android/support/constraint/solver/widgets/ConstraintAnchor$Type.RIGHT : Landroid/support/constraint/solver/widgets/ConstraintAnchor$Type;
    //   827: if_acmpne -> 833
    //   830: goto -> 836
    //   833: goto -> 962
    //   836: aload_0
    //   837: getstatic android/support/constraint/solver/widgets/ConstraintAnchor$Type.CENTER : Landroid/support/constraint/solver/widgets/ConstraintAnchor$Type;
    //   840: invokevirtual getAnchor : (Landroid/support/constraint/solver/widgets/ConstraintAnchor$Type;)Landroid/support/constraint/solver/widgets/ConstraintAnchor;
    //   843: astore_3
    //   844: aload_3
    //   845: invokevirtual getTarget : ()Landroid/support/constraint/solver/widgets/ConstraintAnchor;
    //   848: aload_2
    //   849: if_acmpeq -> 856
    //   852: aload_3
    //   853: invokevirtual reset : ()V
    //   856: aload_0
    //   857: aload_1
    //   858: invokevirtual getAnchor : (Landroid/support/constraint/solver/widgets/ConstraintAnchor$Type;)Landroid/support/constraint/solver/widgets/ConstraintAnchor;
    //   861: invokevirtual getOpposite : ()Landroid/support/constraint/solver/widgets/ConstraintAnchor;
    //   864: astore_1
    //   865: aload_0
    //   866: getstatic android/support/constraint/solver/widgets/ConstraintAnchor$Type.CENTER_X : Landroid/support/constraint/solver/widgets/ConstraintAnchor$Type;
    //   869: invokevirtual getAnchor : (Landroid/support/constraint/solver/widgets/ConstraintAnchor$Type;)Landroid/support/constraint/solver/widgets/ConstraintAnchor;
    //   872: astore_3
    //   873: aload_3
    //   874: invokevirtual isConnected : ()Z
    //   877: ifeq -> 962
    //   880: aload_1
    //   881: invokevirtual reset : ()V
    //   884: aload_3
    //   885: invokevirtual reset : ()V
    //   888: goto -> 962
    //   891: aload_0
    //   892: getstatic android/support/constraint/solver/widgets/ConstraintAnchor$Type.BASELINE : Landroid/support/constraint/solver/widgets/ConstraintAnchor$Type;
    //   895: invokevirtual getAnchor : (Landroid/support/constraint/solver/widgets/ConstraintAnchor$Type;)Landroid/support/constraint/solver/widgets/ConstraintAnchor;
    //   898: astore_3
    //   899: aload_3
    //   900: ifnull -> 907
    //   903: aload_3
    //   904: invokevirtual reset : ()V
    //   907: aload_0
    //   908: getstatic android/support/constraint/solver/widgets/ConstraintAnchor$Type.CENTER : Landroid/support/constraint/solver/widgets/ConstraintAnchor$Type;
    //   911: invokevirtual getAnchor : (Landroid/support/constraint/solver/widgets/ConstraintAnchor$Type;)Landroid/support/constraint/solver/widgets/ConstraintAnchor;
    //   914: astore_3
    //   915: aload_3
    //   916: invokevirtual getTarget : ()Landroid/support/constraint/solver/widgets/ConstraintAnchor;
    //   919: aload_2
    //   920: if_acmpeq -> 927
    //   923: aload_3
    //   924: invokevirtual reset : ()V
    //   927: aload_0
    //   928: aload_1
    //   929: invokevirtual getAnchor : (Landroid/support/constraint/solver/widgets/ConstraintAnchor$Type;)Landroid/support/constraint/solver/widgets/ConstraintAnchor;
    //   932: invokevirtual getOpposite : ()Landroid/support/constraint/solver/widgets/ConstraintAnchor;
    //   935: astore_1
    //   936: aload_0
    //   937: getstatic android/support/constraint/solver/widgets/ConstraintAnchor$Type.CENTER_Y : Landroid/support/constraint/solver/widgets/ConstraintAnchor$Type;
    //   940: invokevirtual getAnchor : (Landroid/support/constraint/solver/widgets/ConstraintAnchor$Type;)Landroid/support/constraint/solver/widgets/ConstraintAnchor;
    //   943: astore_3
    //   944: aload_3
    //   945: invokevirtual isConnected : ()Z
    //   948: ifeq -> 833
    //   951: aload_1
    //   952: invokevirtual reset : ()V
    //   955: aload_3
    //   956: invokevirtual reset : ()V
    //   959: goto -> 833
    //   962: aload #9
    //   964: aload_2
    //   965: iload #4
    //   967: aload #5
    //   969: iload #6
    //   971: invokevirtual connect : (Landroid/support/constraint/solver/widgets/ConstraintAnchor;ILandroid/support/constraint/solver/widgets/ConstraintAnchor$Strength;I)Z
    //   974: pop
    //   975: aload_2
    //   976: invokevirtual getOwner : ()Landroid/support/constraint/solver/widgets/ConstraintWidget;
    //   979: aload #9
    //   981: invokevirtual getOwner : ()Landroid/support/constraint/solver/widgets/ConstraintWidget;
    //   984: invokevirtual connectedTo : (Landroid/support/constraint/solver/widgets/ConstraintWidget;)V
    //   987: return
    //   988: return
    //   989: astore_1
    //   990: aload_1
    //   991: athrow
    // Exception table:
    //   from	to	target	type
    //   388	400	989	java/lang/Throwable }
  
  public void connect(ConstraintAnchor paramConstraintAnchor1, ConstraintAnchor paramConstraintAnchor2, int paramInt) { connect(paramConstraintAnchor1, paramConstraintAnchor2, paramInt, ConstraintAnchor.Strength.STRONG, 0); }
  
  public void connect(ConstraintAnchor paramConstraintAnchor1, ConstraintAnchor paramConstraintAnchor2, int paramInt1, int paramInt2) { connect(paramConstraintAnchor1, paramConstraintAnchor2, paramInt1, ConstraintAnchor.Strength.STRONG, paramInt2); }
  
  public void connect(ConstraintAnchor paramConstraintAnchor1, ConstraintAnchor paramConstraintAnchor2, int paramInt1, ConstraintAnchor.Strength paramStrength, int paramInt2) {
    if (paramConstraintAnchor1.getOwner() == this)
      connect(paramConstraintAnchor1.getType(), paramConstraintAnchor2.getOwner(), paramConstraintAnchor2.getType(), paramInt1, paramStrength, paramInt2); 
  }
  
  public void connectCircularConstraint(ConstraintWidget paramConstraintWidget, float paramFloat, int paramInt) {
    immediateConnect(ConstraintAnchor.Type.CENTER, paramConstraintWidget, ConstraintAnchor.Type.CENTER, paramInt, 0);
    this.mCircleConstraintAngle = paramFloat;
  }
  
  public void connectedTo(ConstraintWidget paramConstraintWidget) {}
  
  public void createObjectVariables(LinearSystem paramLinearSystem) {
    paramLinearSystem.createObjectVariable(this.mLeft);
    paramLinearSystem.createObjectVariable(this.mTop);
    paramLinearSystem.createObjectVariable(this.mRight);
    paramLinearSystem.createObjectVariable(this.mBottom);
    if (this.mBaselineDistance > 0)
      paramLinearSystem.createObjectVariable(this.mBaseline); 
  }
  
  public void disconnectUnlockedWidget(ConstraintWidget paramConstraintWidget) {
    ArrayList arrayList = getAnchors();
    byte b = 0;
    int i = arrayList.size();
    while (b < i) {
      ConstraintAnchor constraintAnchor = (ConstraintAnchor)arrayList.get(b);
      if (constraintAnchor.isConnected() && constraintAnchor.getTarget().getOwner() == paramConstraintWidget && constraintAnchor.getConnectionCreator() == 2)
        constraintAnchor.reset(); 
      b++;
    } 
  }
  
  public void disconnectWidget(ConstraintWidget paramConstraintWidget) {
    ArrayList arrayList = getAnchors();
    byte b = 0;
    int i = arrayList.size();
    while (b < i) {
      ConstraintAnchor constraintAnchor = (ConstraintAnchor)arrayList.get(b);
      if (constraintAnchor.isConnected() && constraintAnchor.getTarget().getOwner() == paramConstraintWidget)
        constraintAnchor.reset(); 
      b++;
    } 
  }
  
  public void forceUpdateDrawPosition() {
    int i = this.mX;
    int j = this.mY;
    int k = this.mX;
    int m = this.mWidth;
    int n = this.mY;
    int i1 = this.mHeight;
    this.mDrawX = i;
    this.mDrawY = j;
    this.mDrawWidth = k + m - i;
    this.mDrawHeight = n + i1 - j;
  }
  
  public ConstraintAnchor getAnchor(ConstraintAnchor.Type paramType) {
    switch (paramType) {
      default:
        throw new AssertionError(paramType.name());
      case null:
        return null;
      case null:
        return this.mCenterY;
      case null:
        return this.mCenterX;
      case null:
        return this.mCenter;
      case null:
        return this.mBaseline;
      case MATCH_CONSTRAINT:
        return this.mBottom;
      case MATCH_PARENT:
        return this.mRight;
      case WRAP_CONTENT:
        return this.mTop;
      case FIXED:
        break;
    } 
    return this.mLeft;
  }
  
  public ArrayList<ConstraintAnchor> getAnchors() { return this.mAnchors; }
  
  public int getBaselineDistance() { return this.mBaselineDistance; }
  
  public float getBiasPercent(int paramInt) { return (paramInt == 0) ? this.mHorizontalBiasPercent : ((paramInt == 1) ? this.mVerticalBiasPercent : -1.0F); }
  
  public int getBottom() { return getY() + this.mHeight; }
  
  public Object getCompanionWidget() { return this.mCompanionWidget; }
  
  public int getContainerItemSkip() { return this.mContainerItemSkip; }
  
  public String getDebugName() { return this.mDebugName; }
  
  public DimensionBehaviour getDimensionBehaviour(int paramInt) { return (paramInt == 0) ? getHorizontalDimensionBehaviour() : ((paramInt == 1) ? getVerticalDimensionBehaviour() : null); }
  
  public float getDimensionRatio() { return this.mDimensionRatio; }
  
  public int getDimensionRatioSide() { return this.mDimensionRatioSide; }
  
  public int getDrawBottom() { return getDrawY() + this.mDrawHeight; }
  
  public int getDrawHeight() { return this.mDrawHeight; }
  
  public int getDrawRight() { return getDrawX() + this.mDrawWidth; }
  
  public int getDrawWidth() { return this.mDrawWidth; }
  
  public int getDrawX() { return this.mDrawX + this.mOffsetX; }
  
  public int getDrawY() { return this.mDrawY + this.mOffsetY; }
  
  public int getHeight() { return (this.mVisibility == 8) ? 0 : this.mHeight; }
  
  public float getHorizontalBiasPercent() { return this.mHorizontalBiasPercent; }
  
  public ConstraintWidget getHorizontalChainControlWidget() {
    ConstraintWidget constraintWidget2 = null;
    ConstraintWidget constraintWidget1 = null;
    if (isInHorizontalChain()) {
      ConstraintWidget constraintWidget = this;
      while (true) {
        constraintWidget2 = constraintWidget1;
        if (constraintWidget1 == null) {
          constraintWidget2 = constraintWidget1;
          if (constraintWidget != null) {
            constraintWidget2 = constraintWidget.getAnchor(ConstraintAnchor.Type.LEFT);
            ConstraintAnchor constraintAnchor = null;
            if (constraintWidget2 == null) {
              constraintWidget2 = null;
            } else {
              constraintWidget2 = constraintWidget2.getTarget();
            } 
            if (constraintWidget2 == null) {
              constraintWidget2 = null;
            } else {
              constraintWidget2 = constraintWidget2.getOwner();
            } 
            if (constraintWidget2 == getParent())
              return constraintWidget; 
            if (constraintWidget2 != null)
              constraintAnchor = constraintWidget2.getAnchor(ConstraintAnchor.Type.RIGHT).getTarget(); 
            if (constraintAnchor != null && constraintAnchor.getOwner() != constraintWidget) {
              constraintWidget1 = constraintWidget;
              continue;
            } 
            constraintWidget = constraintWidget2;
            continue;
          } 
        } 
        break;
      } 
    } 
    return constraintWidget2;
  }
  
  public int getHorizontalChainStyle() { return this.mHorizontalChainStyle; }
  
  public DimensionBehaviour getHorizontalDimensionBehaviour() { return this.mListDimensionBehaviors[0]; }
  
  public int getInternalDrawBottom() { return this.mDrawY + this.mDrawHeight; }
  
  public int getInternalDrawRight() { return this.mDrawX + this.mDrawWidth; }
  
  int getInternalDrawX() { return this.mDrawX; }
  
  int getInternalDrawY() { return this.mDrawY; }
  
  public int getLeft() { return getX(); }
  
  public int getLength(int paramInt) { return (paramInt == 0) ? getWidth() : ((paramInt == 1) ? getHeight() : 0); }
  
  public int getMaxHeight() { return this.mMaxDimension[1]; }
  
  public int getMaxWidth() { return this.mMaxDimension[0]; }
  
  public int getMinHeight() { return this.mMinHeight; }
  
  public int getMinWidth() { return this.mMinWidth; }
  
  public int getOptimizerWrapHeight() {
    int i = this.mHeight;
    int j = i;
    if (this.mListDimensionBehaviors[true] == DimensionBehaviour.MATCH_CONSTRAINT) {
      if (this.mMatchConstraintDefaultHeight == 1) {
        i = Math.max(this.mMatchConstraintMinHeight, i);
      } else if (this.mMatchConstraintMinHeight > 0) {
        i = this.mMatchConstraintMinHeight;
        this.mHeight = i;
      } else {
        i = 0;
      } 
      int k = this.mMatchConstraintMaxHeight;
      j = i;
      if (k > 0) {
        j = i;
        if (k < i)
          j = this.mMatchConstraintMaxHeight; 
      } 
    } 
    return j;
  }
  
  public int getOptimizerWrapWidth() {
    int i = this.mWidth;
    int j = i;
    if (this.mListDimensionBehaviors[false] == DimensionBehaviour.MATCH_CONSTRAINT) {
      if (this.mMatchConstraintDefaultWidth == 1) {
        i = Math.max(this.mMatchConstraintMinWidth, i);
      } else if (this.mMatchConstraintMinWidth > 0) {
        i = this.mMatchConstraintMinWidth;
        this.mWidth = i;
      } else {
        i = 0;
      } 
      int k = this.mMatchConstraintMaxWidth;
      j = i;
      if (k > 0) {
        j = i;
        if (k < i)
          j = this.mMatchConstraintMaxWidth; 
      } 
    } 
    return j;
  }
  
  public ConstraintWidget getParent() { return this.mParent; }
  
  int getRelativePositioning(int paramInt) { return (paramInt == 0) ? this.mRelX : ((paramInt == 1) ? this.mRelY : 0); }
  
  public ResolutionDimension getResolutionHeight() {
    if (this.mResolutionHeight == null)
      this.mResolutionHeight = new ResolutionDimension(); 
    return this.mResolutionHeight;
  }
  
  public ResolutionDimension getResolutionWidth() {
    if (this.mResolutionWidth == null)
      this.mResolutionWidth = new ResolutionDimension(); 
    return this.mResolutionWidth;
  }
  
  public int getRight() { return getX() + this.mWidth; }
  
  public WidgetContainer getRootWidgetContainer() {
    ConstraintWidget constraintWidget;
    for (constraintWidget = this; constraintWidget.getParent() != null; constraintWidget = constraintWidget.getParent());
    return (constraintWidget instanceof WidgetContainer) ? (WidgetContainer)constraintWidget : null;
  }
  
  protected int getRootX() { return this.mX + this.mOffsetX; }
  
  protected int getRootY() { return this.mY + this.mOffsetY; }
  
  public int getTop() { return getY(); }
  
  public String getType() { return this.mType; }
  
  public float getVerticalBiasPercent() { return this.mVerticalBiasPercent; }
  
  public ConstraintWidget getVerticalChainControlWidget() {
    ConstraintWidget constraintWidget2 = null;
    ConstraintWidget constraintWidget1 = null;
    if (isInVerticalChain()) {
      ConstraintWidget constraintWidget = this;
      while (true) {
        constraintWidget2 = constraintWidget1;
        if (constraintWidget1 == null) {
          constraintWidget2 = constraintWidget1;
          if (constraintWidget != null) {
            constraintWidget2 = constraintWidget.getAnchor(ConstraintAnchor.Type.TOP);
            ConstraintAnchor constraintAnchor = null;
            if (constraintWidget2 == null) {
              constraintWidget2 = null;
            } else {
              constraintWidget2 = constraintWidget2.getTarget();
            } 
            if (constraintWidget2 == null) {
              constraintWidget2 = null;
            } else {
              constraintWidget2 = constraintWidget2.getOwner();
            } 
            if (constraintWidget2 == getParent())
              return constraintWidget; 
            if (constraintWidget2 != null)
              constraintAnchor = constraintWidget2.getAnchor(ConstraintAnchor.Type.BOTTOM).getTarget(); 
            if (constraintAnchor != null && constraintAnchor.getOwner() != constraintWidget) {
              constraintWidget1 = constraintWidget;
              continue;
            } 
            constraintWidget = constraintWidget2;
            continue;
          } 
        } 
        break;
      } 
    } 
    return constraintWidget2;
  }
  
  public int getVerticalChainStyle() { return this.mVerticalChainStyle; }
  
  public DimensionBehaviour getVerticalDimensionBehaviour() { return this.mListDimensionBehaviors[1]; }
  
  public int getVisibility() { return this.mVisibility; }
  
  public int getWidth() { return (this.mVisibility == 8) ? 0 : this.mWidth; }
  
  public int getWrapHeight() { return this.mWrapHeight; }
  
  public int getWrapWidth() { return this.mWrapWidth; }
  
  public int getX() { return this.mX; }
  
  public int getY() { return this.mY; }
  
  public boolean hasAncestor(ConstraintWidget paramConstraintWidget) {
    ConstraintWidget constraintWidget2 = getParent();
    if (constraintWidget2 == paramConstraintWidget)
      return true; 
    ConstraintWidget constraintWidget1 = constraintWidget2;
    if (constraintWidget2 == paramConstraintWidget.getParent())
      return false; 
    while (constraintWidget1 != null) {
      if (constraintWidget1 == paramConstraintWidget)
        return true; 
      if (constraintWidget1 == paramConstraintWidget.getParent())
        return true; 
      constraintWidget1 = constraintWidget1.getParent();
    } 
    return false;
  }
  
  public boolean hasBaseline() { return (this.mBaselineDistance > 0); }
  
  public void immediateConnect(ConstraintAnchor.Type paramType1, ConstraintWidget paramConstraintWidget, ConstraintAnchor.Type paramType2, int paramInt1, int paramInt2) { getAnchor(paramType1).connect(paramConstraintWidget.getAnchor(paramType2), paramInt1, paramInt2, ConstraintAnchor.Strength.STRONG, 0, true); }
  
  public boolean isFullyResolved() { return ((this.mLeft.getResolutionNode()).state == 1 && (this.mRight.getResolutionNode()).state == 1 && (this.mTop.getResolutionNode()).state == 1 && (this.mBottom.getResolutionNode()).state == 1); }
  
  public boolean isHeightWrapContent() { return this.mIsHeightWrapContent; }
  
  public boolean isInHorizontalChain() { return ((this.mLeft.mTarget != null && this.mLeft.mTarget.mTarget == this.mLeft) || (this.mRight.mTarget != null && this.mRight.mTarget.mTarget == this.mRight)); }
  
  public boolean isInVerticalChain() { return ((this.mTop.mTarget != null && this.mTop.mTarget.mTarget == this.mTop) || (this.mBottom.mTarget != null && this.mBottom.mTarget.mTarget == this.mBottom)); }
  
  public boolean isInsideConstraintLayout() {
    ConstraintWidget constraintWidget2 = getParent();
    ConstraintWidget constraintWidget1 = constraintWidget2;
    if (constraintWidget2 == null)
      return false; 
    while (constraintWidget1 != null) {
      if (constraintWidget1 instanceof ConstraintWidgetContainer)
        return true; 
      constraintWidget1 = constraintWidget1.getParent();
    } 
    return false;
  }
  
  public boolean isRoot() { return (this.mParent == null); }
  
  public boolean isRootContainer() {
    if (this instanceof ConstraintWidgetContainer) {
      ConstraintWidget constraintWidget = this.mParent;
      if (constraintWidget == null || !(constraintWidget instanceof ConstraintWidgetContainer))
        return true; 
    } 
    return false;
  }
  
  public boolean isSpreadHeight() { return (this.mMatchConstraintDefaultHeight == 0 && this.mDimensionRatio == 0.0F && this.mMatchConstraintMinHeight == 0 && this.mMatchConstraintMaxHeight == 0 && this.mListDimensionBehaviors[true] == DimensionBehaviour.MATCH_CONSTRAINT); }
  
  public boolean isSpreadWidth() {
    int i = this.mMatchConstraintDefaultWidth;
    byte b = 0;
    int j = b;
    if (i == 0) {
      j = b;
      if (this.mDimensionRatio == 0.0F) {
        j = b;
        if (this.mMatchConstraintMinWidth == 0) {
          j = b;
          if (this.mMatchConstraintMaxWidth == 0) {
            j = b;
            if (this.mListDimensionBehaviors[false] == DimensionBehaviour.MATCH_CONSTRAINT)
              j = 1; 
          } 
        } 
      } 
    } 
    return j;
  }
  
  public boolean isWidthWrapContent() { return this.mIsWidthWrapContent; }
  
  public void reset() {
    this.mLeft.reset();
    this.mTop.reset();
    this.mRight.reset();
    this.mBottom.reset();
    this.mBaseline.reset();
    this.mCenterX.reset();
    this.mCenterY.reset();
    this.mCenter.reset();
    this.mParent = null;
    this.mCircleConstraintAngle = 0.0F;
    this.mWidth = 0;
    this.mHeight = 0;
    this.mDimensionRatio = 0.0F;
    this.mDimensionRatioSide = -1;
    this.mX = 0;
    this.mY = 0;
    this.mDrawX = 0;
    this.mDrawY = 0;
    this.mDrawWidth = 0;
    this.mDrawHeight = 0;
    this.mOffsetX = 0;
    this.mOffsetY = 0;
    this.mBaselineDistance = 0;
    this.mMinWidth = 0;
    this.mMinHeight = 0;
    this.mWrapWidth = 0;
    this.mWrapHeight = 0;
    float f = DEFAULT_BIAS;
    this.mHorizontalBiasPercent = f;
    this.mVerticalBiasPercent = f;
    this.mListDimensionBehaviors[0] = DimensionBehaviour.FIXED;
    this.mListDimensionBehaviors[1] = DimensionBehaviour.FIXED;
    this.mCompanionWidget = null;
    this.mContainerItemSkip = 0;
    this.mVisibility = 0;
    this.mType = null;
    this.mHorizontalWrapVisited = false;
    this.mVerticalWrapVisited = false;
    this.mHorizontalChainStyle = 0;
    this.mVerticalChainStyle = 0;
    this.mHorizontalChainFixedPosition = false;
    this.mVerticalChainFixedPosition = false;
    float[] arrayOfFloat = this.mWeight;
    arrayOfFloat[0] = -1.0F;
    arrayOfFloat[1] = -1.0F;
    this.mHorizontalResolution = -1;
    this.mVerticalResolution = -1;
    int[] arrayOfInt = this.mMaxDimension;
    arrayOfInt[0] = Integer.MAX_VALUE;
    arrayOfInt[1] = Integer.MAX_VALUE;
    this.mMatchConstraintDefaultWidth = 0;
    this.mMatchConstraintDefaultHeight = 0;
    this.mMatchConstraintPercentWidth = 1.0F;
    this.mMatchConstraintPercentHeight = 1.0F;
    this.mMatchConstraintMaxWidth = Integer.MAX_VALUE;
    this.mMatchConstraintMaxHeight = Integer.MAX_VALUE;
    this.mMatchConstraintMinWidth = 0;
    this.mMatchConstraintMinHeight = 0;
    this.mResolvedDimensionRatioSide = -1;
    this.mResolvedDimensionRatio = 1.0F;
    ResolutionDimension resolutionDimension = this.mResolutionWidth;
    if (resolutionDimension != null)
      resolutionDimension.reset(); 
    resolutionDimension = this.mResolutionHeight;
    if (resolutionDimension != null)
      resolutionDimension.reset(); 
    this.mBelongingGroup = null;
    this.mOptimizerMeasurable = false;
    this.mOptimizerMeasured = false;
    this.mGroupsToSolver = false;
  }
  
  public void resetAllConstraints() {
    resetAnchors();
    setVerticalBiasPercent(DEFAULT_BIAS);
    setHorizontalBiasPercent(DEFAULT_BIAS);
    if (this instanceof ConstraintWidgetContainer)
      return; 
    if (getHorizontalDimensionBehaviour() == DimensionBehaviour.MATCH_CONSTRAINT)
      if (getWidth() == getWrapWidth()) {
        setHorizontalDimensionBehaviour(DimensionBehaviour.WRAP_CONTENT);
      } else if (getWidth() > getMinWidth()) {
        setHorizontalDimensionBehaviour(DimensionBehaviour.FIXED);
      }  
    if (getVerticalDimensionBehaviour() == DimensionBehaviour.MATCH_CONSTRAINT) {
      if (getHeight() == getWrapHeight()) {
        setVerticalDimensionBehaviour(DimensionBehaviour.WRAP_CONTENT);
        return;
      } 
      if (getHeight() > getMinHeight())
        setVerticalDimensionBehaviour(DimensionBehaviour.FIXED); 
    } 
  }
  
  public void resetAnchor(ConstraintAnchor paramConstraintAnchor) {
    if (getParent() != null && getParent() instanceof ConstraintWidgetContainer && ((ConstraintWidgetContainer)getParent()).handlesInternalConstraints())
      return; 
    ConstraintAnchor constraintAnchor1 = getAnchor(ConstraintAnchor.Type.LEFT);
    ConstraintAnchor constraintAnchor2 = getAnchor(ConstraintAnchor.Type.RIGHT);
    ConstraintAnchor constraintAnchor3 = getAnchor(ConstraintAnchor.Type.TOP);
    ConstraintAnchor constraintAnchor4 = getAnchor(ConstraintAnchor.Type.BOTTOM);
    ConstraintAnchor constraintAnchor5 = getAnchor(ConstraintAnchor.Type.CENTER);
    ConstraintAnchor constraintAnchor6 = getAnchor(ConstraintAnchor.Type.CENTER_X);
    ConstraintAnchor constraintAnchor7 = getAnchor(ConstraintAnchor.Type.CENTER_Y);
    if (paramConstraintAnchor == constraintAnchor5) {
      if (constraintAnchor1.isConnected() && constraintAnchor2.isConnected() && constraintAnchor1.getTarget() == constraintAnchor2.getTarget()) {
        constraintAnchor1.reset();
        constraintAnchor2.reset();
      } 
      if (constraintAnchor3.isConnected() && constraintAnchor4.isConnected() && constraintAnchor3.getTarget() == constraintAnchor4.getTarget()) {
        constraintAnchor3.reset();
        constraintAnchor4.reset();
      } 
      this.mHorizontalBiasPercent = 0.5F;
      this.mVerticalBiasPercent = 0.5F;
    } else if (paramConstraintAnchor == constraintAnchor6) {
      if (constraintAnchor1.isConnected() && constraintAnchor2.isConnected() && constraintAnchor1.getTarget().getOwner() == constraintAnchor2.getTarget().getOwner()) {
        constraintAnchor1.reset();
        constraintAnchor2.reset();
      } 
      this.mHorizontalBiasPercent = 0.5F;
    } else if (paramConstraintAnchor == constraintAnchor7) {
      if (constraintAnchor3.isConnected() && constraintAnchor4.isConnected() && constraintAnchor3.getTarget().getOwner() == constraintAnchor4.getTarget().getOwner()) {
        constraintAnchor3.reset();
        constraintAnchor4.reset();
      } 
      this.mVerticalBiasPercent = 0.5F;
    } else if (paramConstraintAnchor == constraintAnchor1 || paramConstraintAnchor == constraintAnchor2) {
      if (constraintAnchor1.isConnected() && constraintAnchor1.getTarget() == constraintAnchor2.getTarget())
        constraintAnchor5.reset(); 
    } else if ((paramConstraintAnchor == constraintAnchor3 || paramConstraintAnchor == constraintAnchor4) && constraintAnchor3.isConnected() && constraintAnchor3.getTarget() == constraintAnchor4.getTarget()) {
      constraintAnchor5.reset();
    } 
    paramConstraintAnchor.reset();
  }
  
  public void resetAnchors() {
    ConstraintWidget constraintWidget = getParent();
    if (constraintWidget != null && constraintWidget instanceof ConstraintWidgetContainer && ((ConstraintWidgetContainer)getParent()).handlesInternalConstraints())
      return; 
    byte b = 0;
    int i = this.mAnchors.size();
    while (b < i) {
      ((ConstraintAnchor)this.mAnchors.get(b)).reset();
      b++;
    } 
  }
  
  public void resetAnchors(int paramInt) {
    ConstraintWidget constraintWidget = getParent();
    if (constraintWidget != null && constraintWidget instanceof ConstraintWidgetContainer && ((ConstraintWidgetContainer)getParent()).handlesInternalConstraints())
      return; 
    byte b = 0;
    int i = this.mAnchors.size();
    while (b < i) {
      ConstraintAnchor constraintAnchor = (ConstraintAnchor)this.mAnchors.get(b);
      if (paramInt == constraintAnchor.getConnectionCreator()) {
        if (constraintAnchor.isVerticalAnchor()) {
          setVerticalBiasPercent(DEFAULT_BIAS);
        } else {
          setHorizontalBiasPercent(DEFAULT_BIAS);
        } 
        constraintAnchor.reset();
      } 
      b++;
    } 
  }
  
  public void resetResolutionNodes() {
    for (byte b = 0; b < 6; b++)
      this.mListAnchors[b].getResolutionNode().reset(); 
  }
  
  public void resetSolverVariables(Cache paramCache) {
    this.mLeft.resetSolverVariable(paramCache);
    this.mTop.resetSolverVariable(paramCache);
    this.mRight.resetSolverVariable(paramCache);
    this.mBottom.resetSolverVariable(paramCache);
    this.mBaseline.resetSolverVariable(paramCache);
    this.mCenter.resetSolverVariable(paramCache);
    this.mCenterX.resetSolverVariable(paramCache);
    this.mCenterY.resetSolverVariable(paramCache);
  }
  
  public void resolve() {}
  
  public void setBaselineDistance(int paramInt) { this.mBaselineDistance = paramInt; }
  
  public void setCompanionWidget(Object paramObject) { this.mCompanionWidget = paramObject; }
  
  public void setContainerItemSkip(int paramInt) {
    if (paramInt >= 0) {
      this.mContainerItemSkip = paramInt;
      return;
    } 
    this.mContainerItemSkip = 0;
  }
  
  public void setDebugName(String paramString) { this.mDebugName = paramString; }
  
  public void setDebugSolverName(LinearSystem paramLinearSystem, String paramString) {
    this.mDebugName = paramString;
    SolverVariable solverVariable4 = paramLinearSystem.createObjectVariable(this.mLeft);
    SolverVariable solverVariable3 = paramLinearSystem.createObjectVariable(this.mTop);
    SolverVariable solverVariable2 = paramLinearSystem.createObjectVariable(this.mRight);
    SolverVariable solverVariable1 = paramLinearSystem.createObjectVariable(this.mBottom);
    StringBuilder stringBuilder4 = new StringBuilder();
    stringBuilder4.append(paramString);
    stringBuilder4.append(".left");
    solverVariable4.setName(stringBuilder4.toString());
    StringBuilder stringBuilder3 = new StringBuilder();
    stringBuilder3.append(paramString);
    stringBuilder3.append(".top");
    solverVariable3.setName(stringBuilder3.toString());
    StringBuilder stringBuilder2 = new StringBuilder();
    stringBuilder2.append(paramString);
    stringBuilder2.append(".right");
    solverVariable2.setName(stringBuilder2.toString());
    StringBuilder stringBuilder1 = new StringBuilder();
    stringBuilder1.append(paramString);
    stringBuilder1.append(".bottom");
    solverVariable1.setName(stringBuilder1.toString());
    if (this.mBaselineDistance > 0) {
      SolverVariable solverVariable = paramLinearSystem.createObjectVariable(this.mBaseline);
      StringBuilder stringBuilder = new StringBuilder();
      stringBuilder.append(paramString);
      stringBuilder.append(".baseline");
      solverVariable.setName(stringBuilder.toString());
    } 
  }
  
  public void setDimension(int paramInt1, int paramInt2) {
    this.mWidth = paramInt1;
    paramInt1 = this.mWidth;
    int i = this.mMinWidth;
    if (paramInt1 < i)
      this.mWidth = i; 
    this.mHeight = paramInt2;
    paramInt1 = this.mHeight;
    paramInt2 = this.mMinHeight;
    if (paramInt1 < paramInt2)
      this.mHeight = paramInt2; 
  }
  
  public void setDimensionRatio(float paramFloat, int paramInt) {
    this.mDimensionRatio = paramFloat;
    this.mDimensionRatioSide = paramInt;
  }
  
  public void setDimensionRatio(String paramString) {
    float f1;
    if (paramString == null || paramString.length() == 0) {
      this.mDimensionRatio = 0.0F;
      return;
    } 
    byte b = -1;
    float f2 = 0.0F;
    float f4 = 0.0F;
    float f3 = 0.0F;
    int j = paramString.length();
    int i = paramString.indexOf(',');
    if (i > 0 && i < j - 1) {
      String str = paramString.substring(0, i);
      if (str.equalsIgnoreCase("W")) {
        b = 0;
      } else if (str.equalsIgnoreCase("H")) {
        b = 1;
      } 
      i++;
    } else {
      i = 0;
    } 
    int k = paramString.indexOf(':');
    if (k >= 0 && k < j - 1) {
      String str = paramString.substring(i, k);
      paramString = paramString.substring(k + 1);
      f1 = f2;
      if (str.length() > 0) {
        f1 = f2;
        if (paramString.length() > 0)
          try {
            f4 = Float.parseFloat(str);
            float f = Float.parseFloat(paramString);
            f1 = f3;
            if (f4 > 0.0F) {
              f1 = f3;
              if (f > 0.0F)
                if (b == 1) {
                  f1 = Math.abs(f / f4);
                } else {
                  f1 = Math.abs(f4 / f);
                }  
            } 
          } catch (NumberFormatException paramString) {
            f1 = f2;
          }  
      } 
    } else {
      paramString = paramString.substring(i);
      f1 = f4;
      if (paramString.length() > 0)
        try {
          f1 = Float.parseFloat(paramString);
        } catch (NumberFormatException paramString) {
          f1 = f4;
        }  
    } 
    if (f1 > 0.0F) {
      this.mDimensionRatio = f1;
      this.mDimensionRatioSide = b;
    } 
  }
  
  public void setDrawHeight(int paramInt) { this.mDrawHeight = paramInt; }
  
  public void setDrawOrigin(int paramInt1, int paramInt2) {
    this.mDrawX = paramInt1 - this.mOffsetX;
    this.mDrawY = paramInt2 - this.mOffsetY;
    this.mX = this.mDrawX;
    this.mY = this.mDrawY;
  }
  
  public void setDrawWidth(int paramInt) { this.mDrawWidth = paramInt; }
  
  public void setDrawX(int paramInt) {
    this.mDrawX = paramInt - this.mOffsetX;
    this.mX = this.mDrawX;
  }
  
  public void setDrawY(int paramInt) {
    this.mDrawY = paramInt - this.mOffsetY;
    this.mY = this.mDrawY;
  }
  
  public void setFrame(int paramInt1, int paramInt2, int paramInt3) {
    if (paramInt3 == 0) {
      setHorizontalDimension(paramInt1, paramInt2);
    } else if (paramInt3 == 1) {
      setVerticalDimension(paramInt1, paramInt2);
    } 
    this.mOptimizerMeasured = true;
  }
  
  public void setFrame(int paramInt1, int paramInt2, int paramInt3, int paramInt4) {
    int i = paramInt3 - paramInt1;
    paramInt3 = paramInt4 - paramInt2;
    this.mX = paramInt1;
    this.mY = paramInt2;
    if (this.mVisibility == 8) {
      this.mWidth = 0;
      this.mHeight = 0;
      return;
    } 
    paramInt1 = i;
    if (this.mListDimensionBehaviors[false] == DimensionBehaviour.FIXED) {
      paramInt1 = i;
      if (i < this.mWidth)
        paramInt1 = this.mWidth; 
    } 
    paramInt2 = paramInt3;
    if (this.mListDimensionBehaviors[true] == DimensionBehaviour.FIXED) {
      paramInt2 = paramInt3;
      if (paramInt3 < this.mHeight)
        paramInt2 = this.mHeight; 
    } 
    this.mWidth = paramInt1;
    this.mHeight = paramInt2;
    paramInt1 = this.mHeight;
    paramInt2 = this.mMinHeight;
    if (paramInt1 < paramInt2)
      this.mHeight = paramInt2; 
    paramInt1 = this.mWidth;
    paramInt2 = this.mMinWidth;
    if (paramInt1 < paramInt2)
      this.mWidth = paramInt2; 
    this.mOptimizerMeasured = true;
  }
  
  public void setGoneMargin(ConstraintAnchor.Type paramType, int paramInt) {
    switch (paramType) {
      default:
        return;
      case MATCH_CONSTRAINT:
        this.mBottom.mGoneMargin = paramInt;
        return;
      case MATCH_PARENT:
        this.mRight.mGoneMargin = paramInt;
        return;
      case WRAP_CONTENT:
        this.mTop.mGoneMargin = paramInt;
        return;
      case FIXED:
        break;
    } 
    this.mLeft.mGoneMargin = paramInt;
  }
  
  public void setHeight(int paramInt) {
    this.mHeight = paramInt;
    paramInt = this.mHeight;
    int i = this.mMinHeight;
    if (paramInt < i)
      this.mHeight = i; 
  }
  
  public void setHeightWrapContent(boolean paramBoolean) { this.mIsHeightWrapContent = paramBoolean; }
  
  public void setHorizontalBiasPercent(float paramFloat) { this.mHorizontalBiasPercent = paramFloat; }
  
  public void setHorizontalChainStyle(int paramInt) { this.mHorizontalChainStyle = paramInt; }
  
  public void setHorizontalDimension(int paramInt1, int paramInt2) {
    this.mX = paramInt1;
    this.mWidth = paramInt2 - paramInt1;
    paramInt1 = this.mWidth;
    paramInt2 = this.mMinWidth;
    if (paramInt1 < paramInt2)
      this.mWidth = paramInt2; 
  }
  
  public void setHorizontalDimensionBehaviour(DimensionBehaviour paramDimensionBehaviour) {
    this.mListDimensionBehaviors[0] = paramDimensionBehaviour;
    if (paramDimensionBehaviour == DimensionBehaviour.WRAP_CONTENT)
      setWidth(this.mWrapWidth); 
  }
  
  public void setHorizontalMatchStyle(int paramInt1, int paramInt2, int paramInt3, float paramFloat) {
    this.mMatchConstraintDefaultWidth = paramInt1;
    this.mMatchConstraintMinWidth = paramInt2;
    this.mMatchConstraintMaxWidth = paramInt3;
    this.mMatchConstraintPercentWidth = paramFloat;
    if (paramFloat < 1.0F && this.mMatchConstraintDefaultWidth == 0)
      this.mMatchConstraintDefaultWidth = 2; 
  }
  
  public void setHorizontalWeight(float paramFloat) { this.mWeight[0] = paramFloat; }
  
  public void setLength(int paramInt1, int paramInt2) {
    if (paramInt2 == 0) {
      setWidth(paramInt1);
      return;
    } 
    if (paramInt2 == 1)
      setHeight(paramInt1); 
  }
  
  public void setMaxHeight(int paramInt) { this.mMaxDimension[1] = paramInt; }
  
  public void setMaxWidth(int paramInt) { this.mMaxDimension[0] = paramInt; }
  
  public void setMinHeight(int paramInt) {
    if (paramInt < 0) {
      this.mMinHeight = 0;
      return;
    } 
    this.mMinHeight = paramInt;
  }
  
  public void setMinWidth(int paramInt) {
    if (paramInt < 0) {
      this.mMinWidth = 0;
      return;
    } 
    this.mMinWidth = paramInt;
  }
  
  public void setOffset(int paramInt1, int paramInt2) {
    this.mOffsetX = paramInt1;
    this.mOffsetY = paramInt2;
  }
  
  public void setOrigin(int paramInt1, int paramInt2) {
    this.mX = paramInt1;
    this.mY = paramInt2;
  }
  
  public void setParent(ConstraintWidget paramConstraintWidget) { this.mParent = paramConstraintWidget; }
  
  void setRelativePositioning(int paramInt1, int paramInt2) {
    if (paramInt2 == 0) {
      this.mRelX = paramInt1;
      return;
    } 
    if (paramInt2 == 1)
      this.mRelY = paramInt1; 
  }
  
  public void setType(String paramString) { this.mType = paramString; }
  
  public void setVerticalBiasPercent(float paramFloat) { this.mVerticalBiasPercent = paramFloat; }
  
  public void setVerticalChainStyle(int paramInt) { this.mVerticalChainStyle = paramInt; }
  
  public void setVerticalDimension(int paramInt1, int paramInt2) {
    this.mY = paramInt1;
    this.mHeight = paramInt2 - paramInt1;
    paramInt1 = this.mHeight;
    paramInt2 = this.mMinHeight;
    if (paramInt1 < paramInt2)
      this.mHeight = paramInt2; 
  }
  
  public void setVerticalDimensionBehaviour(DimensionBehaviour paramDimensionBehaviour) {
    this.mListDimensionBehaviors[1] = paramDimensionBehaviour;
    if (paramDimensionBehaviour == DimensionBehaviour.WRAP_CONTENT)
      setHeight(this.mWrapHeight); 
  }
  
  public void setVerticalMatchStyle(int paramInt1, int paramInt2, int paramInt3, float paramFloat) {
    this.mMatchConstraintDefaultHeight = paramInt1;
    this.mMatchConstraintMinHeight = paramInt2;
    this.mMatchConstraintMaxHeight = paramInt3;
    this.mMatchConstraintPercentHeight = paramFloat;
    if (paramFloat < 1.0F && this.mMatchConstraintDefaultHeight == 0)
      this.mMatchConstraintDefaultHeight = 2; 
  }
  
  public void setVerticalWeight(float paramFloat) { this.mWeight[1] = paramFloat; }
  
  public void setVisibility(int paramInt) { this.mVisibility = paramInt; }
  
  public void setWidth(int paramInt) {
    this.mWidth = paramInt;
    paramInt = this.mWidth;
    int i = this.mMinWidth;
    if (paramInt < i)
      this.mWidth = i; 
  }
  
  public void setWidthWrapContent(boolean paramBoolean) { this.mIsWidthWrapContent = paramBoolean; }
  
  public void setWrapHeight(int paramInt) { this.mWrapHeight = paramInt; }
  
  public void setWrapWidth(int paramInt) { this.mWrapWidth = paramInt; }
  
  public void setX(int paramInt) { this.mX = paramInt; }
  
  public void setY(int paramInt) { this.mY = paramInt; }
  
  public void setupDimensionRatio(boolean paramBoolean1, boolean paramBoolean2, boolean paramBoolean3, boolean paramBoolean4) {
    if (this.mResolvedDimensionRatioSide == -1)
      if (paramBoolean3 && !paramBoolean4) {
        this.mResolvedDimensionRatioSide = 0;
      } else if (!paramBoolean3 && paramBoolean4) {
        this.mResolvedDimensionRatioSide = 1;
        if (this.mDimensionRatioSide == -1)
          this.mResolvedDimensionRatio = 1.0F / this.mResolvedDimensionRatio; 
      }  
    if (this.mResolvedDimensionRatioSide == 0 && (!this.mTop.isConnected() || !this.mBottom.isConnected())) {
      this.mResolvedDimensionRatioSide = 1;
    } else if (this.mResolvedDimensionRatioSide == 1 && (!this.mLeft.isConnected() || !this.mRight.isConnected())) {
      this.mResolvedDimensionRatioSide = 0;
    } 
    if (this.mResolvedDimensionRatioSide == -1 && (!this.mTop.isConnected() || !this.mBottom.isConnected() || !this.mLeft.isConnected() || !this.mRight.isConnected()))
      if (this.mTop.isConnected() && this.mBottom.isConnected()) {
        this.mResolvedDimensionRatioSide = 0;
      } else if (this.mLeft.isConnected() && this.mRight.isConnected()) {
        this.mResolvedDimensionRatio = 1.0F / this.mResolvedDimensionRatio;
        this.mResolvedDimensionRatioSide = 1;
      }  
    if (this.mResolvedDimensionRatioSide == -1)
      if (paramBoolean1 && !paramBoolean2) {
        this.mResolvedDimensionRatioSide = 0;
      } else if (!paramBoolean1 && paramBoolean2) {
        this.mResolvedDimensionRatio = 1.0F / this.mResolvedDimensionRatio;
        this.mResolvedDimensionRatioSide = 1;
      }  
    if (this.mResolvedDimensionRatioSide == -1)
      if (this.mMatchConstraintMinWidth > 0 && this.mMatchConstraintMinHeight == 0) {
        this.mResolvedDimensionRatioSide = 0;
      } else if (this.mMatchConstraintMinWidth == 0 && this.mMatchConstraintMinHeight > 0) {
        this.mResolvedDimensionRatio = 1.0F / this.mResolvedDimensionRatio;
        this.mResolvedDimensionRatioSide = 1;
      }  
    if (this.mResolvedDimensionRatioSide == -1 && paramBoolean1 && paramBoolean2) {
      this.mResolvedDimensionRatio = 1.0F / this.mResolvedDimensionRatio;
      this.mResolvedDimensionRatioSide = 1;
    } 
  }
  
  public String toString() {
    String str;
    StringBuilder stringBuilder = new StringBuilder();
    if (this.mType != null) {
      StringBuilder stringBuilder1 = new StringBuilder();
      stringBuilder1.append("type: ");
      stringBuilder1.append(this.mType);
      stringBuilder1.append(" ");
      str = stringBuilder1.toString();
    } else {
      str = "";
    } 
    stringBuilder.append(str);
    if (this.mDebugName != null) {
      StringBuilder stringBuilder1 = new StringBuilder();
      stringBuilder1.append("id: ");
      stringBuilder1.append(this.mDebugName);
      stringBuilder1.append(" ");
      String str1 = stringBuilder1.toString();
    } else {
      str = "";
    } 
    stringBuilder.append(str);
    stringBuilder.append("(");
    stringBuilder.append(this.mX);
    stringBuilder.append(", ");
    stringBuilder.append(this.mY);
    stringBuilder.append(") - (");
    stringBuilder.append(this.mWidth);
    stringBuilder.append(" x ");
    stringBuilder.append(this.mHeight);
    stringBuilder.append(") wrap: (");
    stringBuilder.append(this.mWrapWidth);
    stringBuilder.append(" x ");
    stringBuilder.append(this.mWrapHeight);
    stringBuilder.append(")");
    return stringBuilder.toString();
  }
  
  public void updateDrawPosition() {
    int i = this.mX;
    int j = this.mY;
    int k = this.mX;
    int m = this.mWidth;
    int n = this.mY;
    int i1 = this.mHeight;
    this.mDrawX = i;
    this.mDrawY = j;
    this.mDrawWidth = k + m - i;
    this.mDrawHeight = n + i1 - j;
  }
  
  public void updateFromSolver(LinearSystem paramLinearSystem) { // Byte code:
    //   0: aload_1
    //   1: aload_0
    //   2: getfield mLeft : Landroid/support/constraint/solver/widgets/ConstraintAnchor;
    //   5: invokevirtual getObjectVariableValue : (Ljava/lang/Object;)I
    //   8: istore_3
    //   9: aload_1
    //   10: aload_0
    //   11: getfield mTop : Landroid/support/constraint/solver/widgets/ConstraintAnchor;
    //   14: invokevirtual getObjectVariableValue : (Ljava/lang/Object;)I
    //   17: istore #4
    //   19: aload_1
    //   20: aload_0
    //   21: getfield mRight : Landroid/support/constraint/solver/widgets/ConstraintAnchor;
    //   24: invokevirtual getObjectVariableValue : (Ljava/lang/Object;)I
    //   27: istore #5
    //   29: aload_1
    //   30: aload_0
    //   31: getfield mBottom : Landroid/support/constraint/solver/widgets/ConstraintAnchor;
    //   34: invokevirtual getObjectVariableValue : (Ljava/lang/Object;)I
    //   37: istore #6
    //   39: iload #5
    //   41: iload_3
    //   42: isub
    //   43: iflt -> 115
    //   46: iload #6
    //   48: iload #4
    //   50: isub
    //   51: iflt -> 115
    //   54: iload_3
    //   55: ldc_w -2147483648
    //   58: if_icmpeq -> 115
    //   61: iload_3
    //   62: ldc 2147483647
    //   64: if_icmpeq -> 115
    //   67: iload #4
    //   69: ldc_w -2147483648
    //   72: if_icmpeq -> 115
    //   75: iload #4
    //   77: ldc 2147483647
    //   79: if_icmpeq -> 115
    //   82: iload #5
    //   84: ldc_w -2147483648
    //   87: if_icmpeq -> 115
    //   90: iload #5
    //   92: ldc 2147483647
    //   94: if_icmpeq -> 115
    //   97: iload #6
    //   99: ldc_w -2147483648
    //   102: if_icmpeq -> 115
    //   105: iload #6
    //   107: istore_2
    //   108: iload #6
    //   110: ldc 2147483647
    //   112: if_icmpne -> 125
    //   115: iconst_0
    //   116: istore_3
    //   117: iconst_0
    //   118: istore #4
    //   120: iconst_0
    //   121: istore #5
    //   123: iconst_0
    //   124: istore_2
    //   125: aload_0
    //   126: iload_3
    //   127: iload #4
    //   129: iload #5
    //   131: iload_2
    //   132: invokevirtual setFrame : (IIII)V
    //   135: return }
  
  public void updateResolutionNodes() {
    for (byte b = 0; b < 6; b++)
      this.mListAnchors[b].getResolutionNode().update(); 
  }
  
  public enum ContentAlignment {
    BEGIN, BOTTOM, END, LEFT, MIDDLE, RIGHT, TOP, VERTICAL_MIDDLE;
    
    static  {
      END = new ContentAlignment("END", 2);
      TOP = new ContentAlignment("TOP", 3);
      VERTICAL_MIDDLE = new ContentAlignment("VERTICAL_MIDDLE", 4);
      BOTTOM = new ContentAlignment("BOTTOM", 5);
      LEFT = new ContentAlignment("LEFT", 6);
      RIGHT = new ContentAlignment("RIGHT", 7);
      $VALUES = new ContentAlignment[] { BEGIN, MIDDLE, END, TOP, VERTICAL_MIDDLE, BOTTOM, LEFT, RIGHT };
    }
  }
  
  public enum DimensionBehaviour {
    FIXED, MATCH_CONSTRAINT, MATCH_PARENT, WRAP_CONTENT;
    
    static  {
      MATCH_CONSTRAINT = new DimensionBehaviour("MATCH_CONSTRAINT", 2);
      MATCH_PARENT = new DimensionBehaviour("MATCH_PARENT", 3);
      $VALUES = new DimensionBehaviour[] { FIXED, WRAP_CONTENT, MATCH_CONSTRAINT, MATCH_PARENT };
    }
  }
}


/* Location:              D:\Apks\New folder\classes-dex2jar.jar!\android\support\constraint\solver\widgets\ConstraintWidget.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.0.7
 */