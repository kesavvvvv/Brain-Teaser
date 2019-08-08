package android.support.constraint.solver.widgets;

import java.util.ArrayList;

public class ChainHead {
  private boolean mDefined;
  
  protected ConstraintWidget mFirst;
  
  protected ConstraintWidget mFirstMatchConstraintWidget;
  
  protected ConstraintWidget mFirstVisibleWidget;
  
  protected boolean mHasComplexMatchWeights;
  
  protected boolean mHasDefinedWeights;
  
  protected boolean mHasUndefinedWeights;
  
  protected ConstraintWidget mHead;
  
  private boolean mIsRtl = false;
  
  protected ConstraintWidget mLast;
  
  protected ConstraintWidget mLastMatchConstraintWidget;
  
  protected ConstraintWidget mLastVisibleWidget;
  
  private int mOrientation;
  
  protected float mTotalWeight = 0.0F;
  
  protected ArrayList<ConstraintWidget> mWeightedMatchConstraintsWidgets;
  
  protected int mWidgetsCount;
  
  protected int mWidgetsMatchCount;
  
  public ChainHead(ConstraintWidget paramConstraintWidget, int paramInt, boolean paramBoolean) {
    this.mFirst = paramConstraintWidget;
    this.mOrientation = paramInt;
    this.mIsRtl = paramBoolean;
  }
  
  private void defineChainProperties() { // Byte code:
    //   0: aload_0
    //   1: getfield mOrientation : I
    //   4: iconst_2
    //   5: imul
    //   6: istore_3
    //   7: aload_0
    //   8: getfield mFirst : Landroid/support/constraint/solver/widgets/ConstraintWidget;
    //   11: astore #7
    //   13: aload_0
    //   14: getfield mFirst : Landroid/support/constraint/solver/widgets/ConstraintWidget;
    //   17: astore #6
    //   19: aload_0
    //   20: getfield mFirst : Landroid/support/constraint/solver/widgets/ConstraintWidget;
    //   23: astore #5
    //   25: iconst_0
    //   26: istore_2
    //   27: iconst_1
    //   28: istore #4
    //   30: iload_2
    //   31: ifne -> 409
    //   34: aload_0
    //   35: aload_0
    //   36: getfield mWidgetsCount : I
    //   39: iconst_1
    //   40: iadd
    //   41: putfield mWidgetsCount : I
    //   44: aload #6
    //   46: getfield mNextChainWidget : [Landroid/support/constraint/solver/widgets/ConstraintWidget;
    //   49: aload_0
    //   50: getfield mOrientation : I
    //   53: aconst_null
    //   54: aastore
    //   55: aload #6
    //   57: getfield mListNextMatchConstraintsWidget : [Landroid/support/constraint/solver/widgets/ConstraintWidget;
    //   60: aload_0
    //   61: getfield mOrientation : I
    //   64: aconst_null
    //   65: aastore
    //   66: aload #6
    //   68: invokevirtual getVisibility : ()I
    //   71: bipush #8
    //   73: if_icmpeq -> 299
    //   76: aload_0
    //   77: getfield mFirstVisibleWidget : Landroid/support/constraint/solver/widgets/ConstraintWidget;
    //   80: ifnonnull -> 89
    //   83: aload_0
    //   84: aload #6
    //   86: putfield mFirstVisibleWidget : Landroid/support/constraint/solver/widgets/ConstraintWidget;
    //   89: aload_0
    //   90: aload #6
    //   92: putfield mLastVisibleWidget : Landroid/support/constraint/solver/widgets/ConstraintWidget;
    //   95: aload #6
    //   97: getfield mListDimensionBehaviors : [Landroid/support/constraint/solver/widgets/ConstraintWidget$DimensionBehaviour;
    //   100: aload_0
    //   101: getfield mOrientation : I
    //   104: aaload
    //   105: getstatic android/support/constraint/solver/widgets/ConstraintWidget$DimensionBehaviour.MATCH_CONSTRAINT : Landroid/support/constraint/solver/widgets/ConstraintWidget$DimensionBehaviour;
    //   108: if_acmpne -> 299
    //   111: aload #6
    //   113: getfield mResolvedMatchConstraintDefault : [I
    //   116: aload_0
    //   117: getfield mOrientation : I
    //   120: iaload
    //   121: ifeq -> 152
    //   124: aload #6
    //   126: getfield mResolvedMatchConstraintDefault : [I
    //   129: aload_0
    //   130: getfield mOrientation : I
    //   133: iaload
    //   134: iconst_3
    //   135: if_icmpeq -> 152
    //   138: aload #6
    //   140: getfield mResolvedMatchConstraintDefault : [I
    //   143: aload_0
    //   144: getfield mOrientation : I
    //   147: iaload
    //   148: iconst_2
    //   149: if_icmpne -> 299
    //   152: aload_0
    //   153: aload_0
    //   154: getfield mWidgetsMatchCount : I
    //   157: iconst_1
    //   158: iadd
    //   159: putfield mWidgetsMatchCount : I
    //   162: aload #6
    //   164: getfield mWeight : [F
    //   167: aload_0
    //   168: getfield mOrientation : I
    //   171: faload
    //   172: fstore_1
    //   173: fload_1
    //   174: fconst_0
    //   175: fcmpl
    //   176: ifle -> 198
    //   179: aload_0
    //   180: aload_0
    //   181: getfield mTotalWeight : F
    //   184: aload #6
    //   186: getfield mWeight : [F
    //   189: aload_0
    //   190: getfield mOrientation : I
    //   193: faload
    //   194: fadd
    //   195: putfield mTotalWeight : F
    //   198: aload #6
    //   200: aload_0
    //   201: getfield mOrientation : I
    //   204: invokestatic isMatchConstraintEqualityCandidate : (Landroid/support/constraint/solver/widgets/ConstraintWidget;I)Z
    //   207: ifeq -> 257
    //   210: fload_1
    //   211: fconst_0
    //   212: fcmpg
    //   213: ifge -> 224
    //   216: aload_0
    //   217: iconst_1
    //   218: putfield mHasUndefinedWeights : Z
    //   221: goto -> 229
    //   224: aload_0
    //   225: iconst_1
    //   226: putfield mHasDefinedWeights : Z
    //   229: aload_0
    //   230: getfield mWeightedMatchConstraintsWidgets : Ljava/util/ArrayList;
    //   233: ifnonnull -> 247
    //   236: aload_0
    //   237: new java/util/ArrayList
    //   240: dup
    //   241: invokespecial <init> : ()V
    //   244: putfield mWeightedMatchConstraintsWidgets : Ljava/util/ArrayList;
    //   247: aload_0
    //   248: getfield mWeightedMatchConstraintsWidgets : Ljava/util/ArrayList;
    //   251: aload #6
    //   253: invokevirtual add : (Ljava/lang/Object;)Z
    //   256: pop
    //   257: aload_0
    //   258: getfield mFirstMatchConstraintWidget : Landroid/support/constraint/solver/widgets/ConstraintWidget;
    //   261: ifnonnull -> 270
    //   264: aload_0
    //   265: aload #6
    //   267: putfield mFirstMatchConstraintWidget : Landroid/support/constraint/solver/widgets/ConstraintWidget;
    //   270: aload_0
    //   271: getfield mLastMatchConstraintWidget : Landroid/support/constraint/solver/widgets/ConstraintWidget;
    //   274: astore #5
    //   276: aload #5
    //   278: ifnull -> 293
    //   281: aload #5
    //   283: getfield mListNextMatchConstraintsWidget : [Landroid/support/constraint/solver/widgets/ConstraintWidget;
    //   286: aload_0
    //   287: getfield mOrientation : I
    //   290: aload #6
    //   292: aastore
    //   293: aload_0
    //   294: aload #6
    //   296: putfield mLastMatchConstraintWidget : Landroid/support/constraint/solver/widgets/ConstraintWidget;
    //   299: aload #7
    //   301: aload #6
    //   303: if_acmpeq -> 318
    //   306: aload #7
    //   308: getfield mNextChainWidget : [Landroid/support/constraint/solver/widgets/ConstraintWidget;
    //   311: aload_0
    //   312: getfield mOrientation : I
    //   315: aload #6
    //   317: aastore
    //   318: aload #6
    //   320: astore #7
    //   322: aload #6
    //   324: getfield mListAnchors : [Landroid/support/constraint/solver/widgets/ConstraintAnchor;
    //   327: iload_3
    //   328: iconst_1
    //   329: iadd
    //   330: aaload
    //   331: getfield mTarget : Landroid/support/constraint/solver/widgets/ConstraintAnchor;
    //   334: astore #5
    //   336: aload #5
    //   338: ifnull -> 389
    //   341: aload #5
    //   343: getfield mOwner : Landroid/support/constraint/solver/widgets/ConstraintWidget;
    //   346: astore #8
    //   348: aload #8
    //   350: getfield mListAnchors : [Landroid/support/constraint/solver/widgets/ConstraintAnchor;
    //   353: iload_3
    //   354: aaload
    //   355: getfield mTarget : Landroid/support/constraint/solver/widgets/ConstraintAnchor;
    //   358: ifnull -> 383
    //   361: aload #8
    //   363: astore #5
    //   365: aload #8
    //   367: getfield mListAnchors : [Landroid/support/constraint/solver/widgets/ConstraintAnchor;
    //   370: iload_3
    //   371: aaload
    //   372: getfield mTarget : Landroid/support/constraint/solver/widgets/ConstraintAnchor;
    //   375: getfield mOwner : Landroid/support/constraint/solver/widgets/ConstraintWidget;
    //   378: aload #6
    //   380: if_acmpeq -> 392
    //   383: aconst_null
    //   384: astore #5
    //   386: goto -> 392
    //   389: aconst_null
    //   390: astore #5
    //   392: aload #5
    //   394: ifnull -> 404
    //   397: aload #5
    //   399: astore #6
    //   401: goto -> 406
    //   404: iconst_1
    //   405: istore_2
    //   406: goto -> 27
    //   409: aload_0
    //   410: aload #6
    //   412: putfield mLast : Landroid/support/constraint/solver/widgets/ConstraintWidget;
    //   415: aload_0
    //   416: getfield mOrientation : I
    //   419: ifne -> 440
    //   422: aload_0
    //   423: getfield mIsRtl : Z
    //   426: ifeq -> 440
    //   429: aload_0
    //   430: aload_0
    //   431: getfield mLast : Landroid/support/constraint/solver/widgets/ConstraintWidget;
    //   434: putfield mHead : Landroid/support/constraint/solver/widgets/ConstraintWidget;
    //   437: goto -> 448
    //   440: aload_0
    //   441: aload_0
    //   442: getfield mFirst : Landroid/support/constraint/solver/widgets/ConstraintWidget;
    //   445: putfield mHead : Landroid/support/constraint/solver/widgets/ConstraintWidget;
    //   448: aload_0
    //   449: getfield mHasDefinedWeights : Z
    //   452: ifeq -> 465
    //   455: aload_0
    //   456: getfield mHasUndefinedWeights : Z
    //   459: ifeq -> 465
    //   462: goto -> 468
    //   465: iconst_0
    //   466: istore #4
    //   468: aload_0
    //   469: iload #4
    //   471: putfield mHasComplexMatchWeights : Z
    //   474: return }
  
  private static boolean isMatchConstraintEqualityCandidate(ConstraintWidget paramConstraintWidget, int paramInt) { return (paramConstraintWidget.getVisibility() != 8 && paramConstraintWidget.mListDimensionBehaviors[paramInt] == ConstraintWidget.DimensionBehaviour.MATCH_CONSTRAINT && (paramConstraintWidget.mResolvedMatchConstraintDefault[paramInt] == 0 || paramConstraintWidget.mResolvedMatchConstraintDefault[paramInt] == 3)); }
  
  public void define() {
    if (!this.mDefined)
      defineChainProperties(); 
    this.mDefined = true;
  }
  
  public ConstraintWidget getFirst() { return this.mFirst; }
  
  public ConstraintWidget getFirstMatchConstraintWidget() { return this.mFirstMatchConstraintWidget; }
  
  public ConstraintWidget getFirstVisibleWidget() { return this.mFirstVisibleWidget; }
  
  public ConstraintWidget getHead() { return this.mHead; }
  
  public ConstraintWidget getLast() { return this.mLast; }
  
  public ConstraintWidget getLastMatchConstraintWidget() { return this.mLastMatchConstraintWidget; }
  
  public ConstraintWidget getLastVisibleWidget() { return this.mLastVisibleWidget; }
  
  public float getTotalWeight() { return this.mTotalWeight; }
}


/* Location:              D:\Apks\New folder\classes-dex2jar.jar!\android\support\constraint\solver\widgets\ChainHead.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.0.7
 */