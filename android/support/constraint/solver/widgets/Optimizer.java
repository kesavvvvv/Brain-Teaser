package android.support.constraint.solver.widgets;

import android.support.constraint.solver.LinearSystem;

public class Optimizer {
  static final int FLAG_CHAIN_DANGLING = 1;
  
  static final int FLAG_RECOMPUTE_BOUNDS = 2;
  
  static final int FLAG_USE_OPTIMIZE = 0;
  
  public static final int OPTIMIZATION_BARRIER = 2;
  
  public static final int OPTIMIZATION_CHAIN = 4;
  
  public static final int OPTIMIZATION_DIMENSIONS = 8;
  
  public static final int OPTIMIZATION_DIRECT = 1;
  
  public static final int OPTIMIZATION_GROUPS = 32;
  
  public static final int OPTIMIZATION_NONE = 0;
  
  public static final int OPTIMIZATION_RATIO = 16;
  
  public static final int OPTIMIZATION_STANDARD = 7;
  
  static boolean[] flags = new boolean[3];
  
  static void analyze(int paramInt, ConstraintWidget paramConstraintWidget) {
    int i;
    paramConstraintWidget.updateResolutionNodes();
    ResolutionAnchor resolutionAnchor1 = paramConstraintWidget.mLeft.getResolutionNode();
    ResolutionAnchor resolutionAnchor2 = paramConstraintWidget.mTop.getResolutionNode();
    ResolutionAnchor resolutionAnchor3 = paramConstraintWidget.mRight.getResolutionNode();
    ResolutionAnchor resolutionAnchor4 = paramConstraintWidget.mBottom.getResolutionNode();
    if ((paramInt & 0x8) == 8) {
      paramInt = 1;
    } else {
      paramInt = 0;
    } 
    if (paramConstraintWidget.mListDimensionBehaviors[false] == ConstraintWidget.DimensionBehaviour.MATCH_CONSTRAINT && optimizableMatchConstraint(paramConstraintWidget, 0)) {
      i = 1;
    } else {
      i = 0;
    } 
    if (resolutionAnchor1.type != 4 && resolutionAnchor3.type != 4)
      if (paramConstraintWidget.mListDimensionBehaviors[false] == ConstraintWidget.DimensionBehaviour.FIXED || (i && paramConstraintWidget.getVisibility() == 8)) {
        if (paramConstraintWidget.mLeft.mTarget == null && paramConstraintWidget.mRight.mTarget == null) {
          resolutionAnchor1.setType(1);
          resolutionAnchor3.setType(1);
          if (paramInt != 0) {
            resolutionAnchor3.dependsOn(resolutionAnchor1, 1, paramConstraintWidget.getResolutionWidth());
          } else {
            resolutionAnchor3.dependsOn(resolutionAnchor1, paramConstraintWidget.getWidth());
          } 
        } else if (paramConstraintWidget.mLeft.mTarget != null && paramConstraintWidget.mRight.mTarget == null) {
          resolutionAnchor1.setType(1);
          resolutionAnchor3.setType(1);
          if (paramInt != 0) {
            resolutionAnchor3.dependsOn(resolutionAnchor1, 1, paramConstraintWidget.getResolutionWidth());
          } else {
            resolutionAnchor3.dependsOn(resolutionAnchor1, paramConstraintWidget.getWidth());
          } 
        } else if (paramConstraintWidget.mLeft.mTarget == null && paramConstraintWidget.mRight.mTarget != null) {
          resolutionAnchor1.setType(1);
          resolutionAnchor3.setType(1);
          resolutionAnchor1.dependsOn(resolutionAnchor3, -paramConstraintWidget.getWidth());
          if (paramInt != 0) {
            resolutionAnchor1.dependsOn(resolutionAnchor3, -1, paramConstraintWidget.getResolutionWidth());
          } else {
            resolutionAnchor1.dependsOn(resolutionAnchor3, -paramConstraintWidget.getWidth());
          } 
        } else if (paramConstraintWidget.mLeft.mTarget != null && paramConstraintWidget.mRight.mTarget != null) {
          resolutionAnchor1.setType(2);
          resolutionAnchor3.setType(2);
          if (paramInt != 0) {
            paramConstraintWidget.getResolutionWidth().addDependent(resolutionAnchor1);
            paramConstraintWidget.getResolutionWidth().addDependent(resolutionAnchor3);
            resolutionAnchor1.setOpposite(resolutionAnchor3, -1, paramConstraintWidget.getResolutionWidth());
            resolutionAnchor3.setOpposite(resolutionAnchor1, 1, paramConstraintWidget.getResolutionWidth());
          } else {
            resolutionAnchor1.setOpposite(resolutionAnchor3, -paramConstraintWidget.getWidth());
            resolutionAnchor3.setOpposite(resolutionAnchor1, paramConstraintWidget.getWidth());
          } 
        } 
      } else if (i) {
        i = paramConstraintWidget.getWidth();
        resolutionAnchor1.setType(1);
        resolutionAnchor3.setType(1);
        if (paramConstraintWidget.mLeft.mTarget == null && paramConstraintWidget.mRight.mTarget == null) {
          if (paramInt != 0) {
            resolutionAnchor3.dependsOn(resolutionAnchor1, 1, paramConstraintWidget.getResolutionWidth());
          } else {
            resolutionAnchor3.dependsOn(resolutionAnchor1, i);
          } 
        } else if (paramConstraintWidget.mLeft.mTarget != null && paramConstraintWidget.mRight.mTarget == null) {
          if (paramInt != 0) {
            resolutionAnchor3.dependsOn(resolutionAnchor1, 1, paramConstraintWidget.getResolutionWidth());
          } else {
            resolutionAnchor3.dependsOn(resolutionAnchor1, i);
          } 
        } else if (paramConstraintWidget.mLeft.mTarget == null && paramConstraintWidget.mRight.mTarget != null) {
          if (paramInt != 0) {
            resolutionAnchor1.dependsOn(resolutionAnchor3, -1, paramConstraintWidget.getResolutionWidth());
          } else {
            resolutionAnchor1.dependsOn(resolutionAnchor3, -i);
          } 
        } else if (paramConstraintWidget.mLeft.mTarget != null && paramConstraintWidget.mRight.mTarget != null) {
          if (paramInt != 0) {
            paramConstraintWidget.getResolutionWidth().addDependent(resolutionAnchor1);
            paramConstraintWidget.getResolutionWidth().addDependent(resolutionAnchor3);
          } 
          if (paramConstraintWidget.mDimensionRatio == 0.0F) {
            resolutionAnchor1.setType(3);
            resolutionAnchor3.setType(3);
            resolutionAnchor1.setOpposite(resolutionAnchor3, 0.0F);
            resolutionAnchor3.setOpposite(resolutionAnchor1, 0.0F);
          } else {
            resolutionAnchor1.setType(2);
            resolutionAnchor3.setType(2);
            resolutionAnchor1.setOpposite(resolutionAnchor3, -i);
            resolutionAnchor3.setOpposite(resolutionAnchor1, i);
            paramConstraintWidget.setWidth(i);
          } 
        } 
      }  
    if (paramConstraintWidget.mListDimensionBehaviors[true] == ConstraintWidget.DimensionBehaviour.MATCH_CONSTRAINT && optimizableMatchConstraint(paramConstraintWidget, 1)) {
      i = 1;
    } else {
      i = 0;
    } 
    if (resolutionAnchor2.type != 4 && resolutionAnchor4.type != 4) {
      if (paramConstraintWidget.mListDimensionBehaviors[true] == ConstraintWidget.DimensionBehaviour.FIXED || (i != 0 && paramConstraintWidget.getVisibility() == 8)) {
        if (paramConstraintWidget.mTop.mTarget == null && paramConstraintWidget.mBottom.mTarget == null) {
          resolutionAnchor2.setType(1);
          resolutionAnchor4.setType(1);
          if (paramInt != 0) {
            resolutionAnchor4.dependsOn(resolutionAnchor2, 1, paramConstraintWidget.getResolutionHeight());
          } else {
            resolutionAnchor4.dependsOn(resolutionAnchor2, paramConstraintWidget.getHeight());
          } 
          if (paramConstraintWidget.mBaseline.mTarget != null) {
            paramConstraintWidget.mBaseline.getResolutionNode().setType(1);
            resolutionAnchor2.dependsOn(1, paramConstraintWidget.mBaseline.getResolutionNode(), -paramConstraintWidget.mBaselineDistance);
            return;
          } 
        } else if (paramConstraintWidget.mTop.mTarget != null && paramConstraintWidget.mBottom.mTarget == null) {
          resolutionAnchor2.setType(1);
          resolutionAnchor4.setType(1);
          if (paramInt != 0) {
            resolutionAnchor4.dependsOn(resolutionAnchor2, 1, paramConstraintWidget.getResolutionHeight());
          } else {
            resolutionAnchor4.dependsOn(resolutionAnchor2, paramConstraintWidget.getHeight());
          } 
          if (paramConstraintWidget.mBaselineDistance > 0) {
            paramConstraintWidget.mBaseline.getResolutionNode().dependsOn(1, resolutionAnchor2, paramConstraintWidget.mBaselineDistance);
            return;
          } 
        } else if (paramConstraintWidget.mTop.mTarget == null && paramConstraintWidget.mBottom.mTarget != null) {
          resolutionAnchor2.setType(1);
          resolutionAnchor4.setType(1);
          if (paramInt != 0) {
            resolutionAnchor2.dependsOn(resolutionAnchor4, -1, paramConstraintWidget.getResolutionHeight());
          } else {
            resolutionAnchor2.dependsOn(resolutionAnchor4, -paramConstraintWidget.getHeight());
          } 
          if (paramConstraintWidget.mBaselineDistance > 0) {
            paramConstraintWidget.mBaseline.getResolutionNode().dependsOn(1, resolutionAnchor2, paramConstraintWidget.mBaselineDistance);
            return;
          } 
        } else if (paramConstraintWidget.mTop.mTarget != null && paramConstraintWidget.mBottom.mTarget != null) {
          resolutionAnchor2.setType(2);
          resolutionAnchor4.setType(2);
          if (paramInt != 0) {
            resolutionAnchor2.setOpposite(resolutionAnchor4, -1, paramConstraintWidget.getResolutionHeight());
            resolutionAnchor4.setOpposite(resolutionAnchor2, 1, paramConstraintWidget.getResolutionHeight());
            paramConstraintWidget.getResolutionHeight().addDependent(resolutionAnchor2);
            paramConstraintWidget.getResolutionWidth().addDependent(resolutionAnchor4);
          } else {
            resolutionAnchor2.setOpposite(resolutionAnchor4, -paramConstraintWidget.getHeight());
            resolutionAnchor4.setOpposite(resolutionAnchor2, paramConstraintWidget.getHeight());
          } 
          if (paramConstraintWidget.mBaselineDistance > 0)
            paramConstraintWidget.mBaseline.getResolutionNode().dependsOn(1, resolutionAnchor2, paramConstraintWidget.mBaselineDistance); 
        } 
        return;
      } 
      if (i != 0) {
        i = paramConstraintWidget.getHeight();
        resolutionAnchor2.setType(1);
        resolutionAnchor4.setType(1);
        if (paramConstraintWidget.mTop.mTarget == null && paramConstraintWidget.mBottom.mTarget == null) {
          if (paramInt != 0) {
            resolutionAnchor4.dependsOn(resolutionAnchor2, 1, paramConstraintWidget.getResolutionHeight());
            return;
          } 
          resolutionAnchor4.dependsOn(resolutionAnchor2, i);
          return;
        } 
        if (paramConstraintWidget.mTop.mTarget != null && paramConstraintWidget.mBottom.mTarget == null) {
          if (paramInt != 0) {
            resolutionAnchor4.dependsOn(resolutionAnchor2, 1, paramConstraintWidget.getResolutionHeight());
            return;
          } 
          resolutionAnchor4.dependsOn(resolutionAnchor2, i);
          return;
        } 
        if (paramConstraintWidget.mTop.mTarget == null && paramConstraintWidget.mBottom.mTarget != null) {
          if (paramInt != 0) {
            resolutionAnchor2.dependsOn(resolutionAnchor4, -1, paramConstraintWidget.getResolutionHeight());
            return;
          } 
          resolutionAnchor2.dependsOn(resolutionAnchor4, -i);
          return;
        } 
        if (paramConstraintWidget.mTop.mTarget != null && paramConstraintWidget.mBottom.mTarget != null) {
          if (paramInt != 0) {
            paramConstraintWidget.getResolutionHeight().addDependent(resolutionAnchor2);
            paramConstraintWidget.getResolutionWidth().addDependent(resolutionAnchor4);
          } 
          if (paramConstraintWidget.mDimensionRatio == 0.0F) {
            resolutionAnchor2.setType(3);
            resolutionAnchor4.setType(3);
            resolutionAnchor2.setOpposite(resolutionAnchor4, 0.0F);
            resolutionAnchor4.setOpposite(resolutionAnchor2, 0.0F);
            return;
          } 
          resolutionAnchor2.setType(2);
          resolutionAnchor4.setType(2);
          resolutionAnchor2.setOpposite(resolutionAnchor4, -i);
          resolutionAnchor4.setOpposite(resolutionAnchor2, i);
          paramConstraintWidget.setHeight(i);
          if (paramConstraintWidget.mBaselineDistance > 0) {
            paramConstraintWidget.mBaseline.getResolutionNode().dependsOn(1, resolutionAnchor2, paramConstraintWidget.mBaselineDistance);
            return;
          } 
        } 
      } 
    } 
  }
  
  static boolean applyChainOptimized(ConstraintWidgetContainer paramConstraintWidgetContainer, LinearSystem paramLinearSystem, int paramInt1, int paramInt2, ChainHead paramChainHead) { // Byte code:
    //   0: iload_2
    //   1: istore #18
    //   3: aload #4
    //   5: getfield mFirst : Landroid/support/constraint/solver/widgets/ConstraintWidget;
    //   8: astore #20
    //   10: aload #4
    //   12: getfield mLast : Landroid/support/constraint/solver/widgets/ConstraintWidget;
    //   15: astore #22
    //   17: aload #4
    //   19: getfield mFirstVisibleWidget : Landroid/support/constraint/solver/widgets/ConstraintWidget;
    //   22: astore #23
    //   24: aload #4
    //   26: getfield mLastVisibleWidget : Landroid/support/constraint/solver/widgets/ConstraintWidget;
    //   29: astore #24
    //   31: aload #4
    //   33: getfield mHead : Landroid/support/constraint/solver/widgets/ConstraintWidget;
    //   36: astore #25
    //   38: iconst_0
    //   39: istore #15
    //   41: aload #4
    //   43: getfield mTotalWeight : F
    //   46: fstore #10
    //   48: aload #4
    //   50: getfield mFirstMatchConstraintWidget : Landroid/support/constraint/solver/widgets/ConstraintWidget;
    //   53: astore #21
    //   55: aload #4
    //   57: getfield mLastMatchConstraintWidget : Landroid/support/constraint/solver/widgets/ConstraintWidget;
    //   60: astore #4
    //   62: aload_0
    //   63: getfield mListDimensionBehaviors : [Landroid/support/constraint/solver/widgets/ConstraintWidget$DimensionBehaviour;
    //   66: iload #18
    //   68: aaload
    //   69: getstatic android/support/constraint/solver/widgets/ConstraintWidget$DimensionBehaviour.WRAP_CONTENT : Landroid/support/constraint/solver/widgets/ConstraintWidget$DimensionBehaviour;
    //   72: if_acmpne -> 81
    //   75: iconst_1
    //   76: istore #19
    //   78: goto -> 84
    //   81: iconst_0
    //   82: istore #19
    //   84: iload #18
    //   86: ifne -> 157
    //   89: aload #25
    //   91: getfield mHorizontalChainStyle : I
    //   94: ifne -> 103
    //   97: iconst_1
    //   98: istore #11
    //   100: goto -> 106
    //   103: iconst_0
    //   104: istore #11
    //   106: iload #11
    //   108: istore #13
    //   110: aload #25
    //   112: getfield mHorizontalChainStyle : I
    //   115: iconst_1
    //   116: if_icmpne -> 125
    //   119: iconst_1
    //   120: istore #11
    //   122: goto -> 128
    //   125: iconst_0
    //   126: istore #11
    //   128: aload #25
    //   130: getfield mHorizontalChainStyle : I
    //   133: istore #12
    //   135: iload #11
    //   137: istore #14
    //   139: iload #12
    //   141: iconst_2
    //   142: if_icmpne -> 151
    //   145: iconst_1
    //   146: istore #11
    //   148: goto -> 222
    //   151: iconst_0
    //   152: istore #11
    //   154: goto -> 222
    //   157: aload #25
    //   159: getfield mVerticalChainStyle : I
    //   162: ifne -> 171
    //   165: iconst_1
    //   166: istore #11
    //   168: goto -> 174
    //   171: iconst_0
    //   172: istore #11
    //   174: iload #11
    //   176: istore #13
    //   178: aload #25
    //   180: getfield mVerticalChainStyle : I
    //   183: iconst_1
    //   184: if_icmpne -> 193
    //   187: iconst_1
    //   188: istore #11
    //   190: goto -> 196
    //   193: iconst_0
    //   194: istore #11
    //   196: aload #25
    //   198: getfield mVerticalChainStyle : I
    //   201: istore #12
    //   203: iload #11
    //   205: istore #14
    //   207: iload #12
    //   209: iconst_2
    //   210: if_icmpne -> 219
    //   213: iconst_1
    //   214: istore #11
    //   216: goto -> 222
    //   219: iconst_0
    //   220: istore #11
    //   222: iconst_0
    //   223: istore #12
    //   225: iconst_0
    //   226: istore #16
    //   228: fconst_0
    //   229: fstore #7
    //   231: fconst_0
    //   232: fstore #9
    //   234: aload #20
    //   236: astore #4
    //   238: iload #15
    //   240: ifne -> 604
    //   243: iload #16
    //   245: istore #17
    //   247: fload #9
    //   249: fstore #5
    //   251: fload #7
    //   253: fstore #6
    //   255: aload #4
    //   257: invokevirtual getVisibility : ()I
    //   260: bipush #8
    //   262: if_icmpeq -> 387
    //   265: iload #16
    //   267: iconst_1
    //   268: iadd
    //   269: istore #17
    //   271: iload #18
    //   273: ifne -> 290
    //   276: fload #9
    //   278: aload #4
    //   280: invokevirtual getWidth : ()I
    //   283: i2f
    //   284: fadd
    //   285: fstore #5
    //   287: goto -> 301
    //   290: fload #9
    //   292: aload #4
    //   294: invokevirtual getHeight : ()I
    //   297: i2f
    //   298: fadd
    //   299: fstore #5
    //   301: fload #5
    //   303: fstore #6
    //   305: aload #4
    //   307: aload #23
    //   309: if_acmpeq -> 328
    //   312: fload #5
    //   314: aload #4
    //   316: getfield mListAnchors : [Landroid/support/constraint/solver/widgets/ConstraintAnchor;
    //   319: iload_3
    //   320: aaload
    //   321: invokevirtual getMargin : ()I
    //   324: i2f
    //   325: fadd
    //   326: fstore #6
    //   328: fload #6
    //   330: fstore #5
    //   332: aload #4
    //   334: aload #24
    //   336: if_acmpeq -> 357
    //   339: fload #6
    //   341: aload #4
    //   343: getfield mListAnchors : [Landroid/support/constraint/solver/widgets/ConstraintAnchor;
    //   346: iload_3
    //   347: iconst_1
    //   348: iadd
    //   349: aaload
    //   350: invokevirtual getMargin : ()I
    //   353: i2f
    //   354: fadd
    //   355: fstore #5
    //   357: fload #7
    //   359: aload #4
    //   361: getfield mListAnchors : [Landroid/support/constraint/solver/widgets/ConstraintAnchor;
    //   364: iload_3
    //   365: aaload
    //   366: invokevirtual getMargin : ()I
    //   369: i2f
    //   370: fadd
    //   371: aload #4
    //   373: getfield mListAnchors : [Landroid/support/constraint/solver/widgets/ConstraintAnchor;
    //   376: iload_3
    //   377: iconst_1
    //   378: iadd
    //   379: aaload
    //   380: invokevirtual getMargin : ()I
    //   383: i2f
    //   384: fadd
    //   385: fstore #6
    //   387: aload #4
    //   389: getfield mListAnchors : [Landroid/support/constraint/solver/widgets/ConstraintAnchor;
    //   392: iload_3
    //   393: aaload
    //   394: astore_0
    //   395: iload #12
    //   397: istore #16
    //   399: aload #4
    //   401: invokevirtual getVisibility : ()I
    //   404: bipush #8
    //   406: if_icmpeq -> 508
    //   409: iload #12
    //   411: istore #16
    //   413: aload #4
    //   415: getfield mListDimensionBehaviors : [Landroid/support/constraint/solver/widgets/ConstraintWidget$DimensionBehaviour;
    //   418: iload #18
    //   420: aaload
    //   421: getstatic android/support/constraint/solver/widgets/ConstraintWidget$DimensionBehaviour.MATCH_CONSTRAINT : Landroid/support/constraint/solver/widgets/ConstraintWidget$DimensionBehaviour;
    //   424: if_acmpne -> 508
    //   427: iload #12
    //   429: iconst_1
    //   430: iadd
    //   431: istore #16
    //   433: iload #18
    //   435: ifne -> 466
    //   438: aload #4
    //   440: getfield mMatchConstraintDefaultWidth : I
    //   443: ifeq -> 448
    //   446: iconst_0
    //   447: ireturn
    //   448: aload #4
    //   450: getfield mMatchConstraintMinWidth : I
    //   453: ifne -> 464
    //   456: aload #4
    //   458: getfield mMatchConstraintMaxWidth : I
    //   461: ifeq -> 494
    //   464: iconst_0
    //   465: ireturn
    //   466: aload #4
    //   468: getfield mMatchConstraintDefaultHeight : I
    //   471: ifeq -> 476
    //   474: iconst_0
    //   475: ireturn
    //   476: aload #4
    //   478: getfield mMatchConstraintMinHeight : I
    //   481: ifne -> 506
    //   484: aload #4
    //   486: getfield mMatchConstraintMaxHeight : I
    //   489: ifeq -> 494
    //   492: iconst_0
    //   493: ireturn
    //   494: aload #4
    //   496: getfield mDimensionRatio : F
    //   499: fconst_0
    //   500: fcmpl
    //   501: ifeq -> 508
    //   504: iconst_0
    //   505: ireturn
    //   506: iconst_0
    //   507: ireturn
    //   508: aload #4
    //   510: getfield mListAnchors : [Landroid/support/constraint/solver/widgets/ConstraintAnchor;
    //   513: iload_3
    //   514: iconst_1
    //   515: iadd
    //   516: aaload
    //   517: getfield mTarget : Landroid/support/constraint/solver/widgets/ConstraintAnchor;
    //   520: astore_0
    //   521: aload_0
    //   522: ifnull -> 570
    //   525: aload_0
    //   526: getfield mOwner : Landroid/support/constraint/solver/widgets/ConstraintWidget;
    //   529: astore_0
    //   530: aload_0
    //   531: getfield mListAnchors : [Landroid/support/constraint/solver/widgets/ConstraintAnchor;
    //   534: iload_3
    //   535: aaload
    //   536: getfield mTarget : Landroid/support/constraint/solver/widgets/ConstraintAnchor;
    //   539: ifnull -> 565
    //   542: aload_0
    //   543: getfield mListAnchors : [Landroid/support/constraint/solver/widgets/ConstraintAnchor;
    //   546: iload_3
    //   547: aaload
    //   548: getfield mTarget : Landroid/support/constraint/solver/widgets/ConstraintAnchor;
    //   551: getfield mOwner : Landroid/support/constraint/solver/widgets/ConstraintWidget;
    //   554: aload #4
    //   556: if_acmpeq -> 562
    //   559: goto -> 565
    //   562: goto -> 572
    //   565: aconst_null
    //   566: astore_0
    //   567: goto -> 572
    //   570: aconst_null
    //   571: astore_0
    //   572: aload_0
    //   573: ifnull -> 582
    //   576: aload_0
    //   577: astore #4
    //   579: goto -> 585
    //   582: iconst_1
    //   583: istore #15
    //   585: iload #16
    //   587: istore #12
    //   589: iload #17
    //   591: istore #16
    //   593: fload #5
    //   595: fstore #9
    //   597: fload #6
    //   599: fstore #7
    //   601: goto -> 238
    //   604: aload #20
    //   606: getfield mListAnchors : [Landroid/support/constraint/solver/widgets/ConstraintAnchor;
    //   609: iload_3
    //   610: aaload
    //   611: invokevirtual getResolutionNode : ()Landroid/support/constraint/solver/widgets/ResolutionAnchor;
    //   614: astore #21
    //   616: aload #22
    //   618: getfield mListAnchors : [Landroid/support/constraint/solver/widgets/ConstraintAnchor;
    //   621: iload_3
    //   622: iconst_1
    //   623: iadd
    //   624: aaload
    //   625: invokevirtual getResolutionNode : ()Landroid/support/constraint/solver/widgets/ResolutionAnchor;
    //   628: astore_0
    //   629: aload #21
    //   631: getfield target : Landroid/support/constraint/solver/widgets/ResolutionAnchor;
    //   634: ifnull -> 1863
    //   637: aload_0
    //   638: getfield target : Landroid/support/constraint/solver/widgets/ResolutionAnchor;
    //   641: ifnonnull -> 646
    //   644: iconst_0
    //   645: ireturn
    //   646: aload #21
    //   648: getfield target : Landroid/support/constraint/solver/widgets/ResolutionAnchor;
    //   651: getfield state : I
    //   654: iconst_1
    //   655: if_icmpne -> 1861
    //   658: aload_0
    //   659: getfield target : Landroid/support/constraint/solver/widgets/ResolutionAnchor;
    //   662: getfield state : I
    //   665: iconst_1
    //   666: if_icmpeq -> 671
    //   669: iconst_0
    //   670: ireturn
    //   671: iload #12
    //   673: ifle -> 685
    //   676: iload #12
    //   678: iload #16
    //   680: if_icmpeq -> 685
    //   683: iconst_0
    //   684: ireturn
    //   685: fconst_0
    //   686: fstore #5
    //   688: fconst_0
    //   689: fstore #6
    //   691: iload #11
    //   693: ifne -> 706
    //   696: iload #13
    //   698: ifne -> 706
    //   701: iload #14
    //   703: ifeq -> 751
    //   706: aload #23
    //   708: ifnull -> 724
    //   711: aload #23
    //   713: getfield mListAnchors : [Landroid/support/constraint/solver/widgets/ConstraintAnchor;
    //   716: iload_3
    //   717: aaload
    //   718: invokevirtual getMargin : ()I
    //   721: i2f
    //   722: fstore #6
    //   724: fload #6
    //   726: fstore #5
    //   728: aload #24
    //   730: ifnull -> 751
    //   733: fload #6
    //   735: aload #24
    //   737: getfield mListAnchors : [Landroid/support/constraint/solver/widgets/ConstraintAnchor;
    //   740: iload_3
    //   741: iconst_1
    //   742: iadd
    //   743: aaload
    //   744: invokevirtual getMargin : ()I
    //   747: i2f
    //   748: fadd
    //   749: fstore #5
    //   751: aload #21
    //   753: getfield target : Landroid/support/constraint/solver/widgets/ResolutionAnchor;
    //   756: getfield resolvedOffset : F
    //   759: fstore #8
    //   761: aload_0
    //   762: getfield target : Landroid/support/constraint/solver/widgets/ResolutionAnchor;
    //   765: getfield resolvedOffset : F
    //   768: fstore #6
    //   770: fload #8
    //   772: fload #6
    //   774: fcmpg
    //   775: ifge -> 791
    //   778: fload #6
    //   780: fload #8
    //   782: fsub
    //   783: fload #9
    //   785: fsub
    //   786: fstore #6
    //   788: goto -> 801
    //   791: fload #8
    //   793: fload #6
    //   795: fsub
    //   796: fload #9
    //   798: fsub
    //   799: fstore #6
    //   801: iload #12
    //   803: ifle -> 1140
    //   806: iload #12
    //   808: iload #16
    //   810: if_icmpne -> 1140
    //   813: aload #4
    //   815: invokevirtual getParent : ()Landroid/support/constraint/solver/widgets/ConstraintWidget;
    //   818: ifnull -> 840
    //   821: aload #4
    //   823: invokevirtual getParent : ()Landroid/support/constraint/solver/widgets/ConstraintWidget;
    //   826: getfield mListDimensionBehaviors : [Landroid/support/constraint/solver/widgets/ConstraintWidget$DimensionBehaviour;
    //   829: iload #18
    //   831: aaload
    //   832: getstatic android/support/constraint/solver/widgets/ConstraintWidget$DimensionBehaviour.WRAP_CONTENT : Landroid/support/constraint/solver/widgets/ConstraintWidget$DimensionBehaviour;
    //   835: if_acmpne -> 840
    //   838: iconst_0
    //   839: ireturn
    //   840: fload #6
    //   842: fload #9
    //   844: fadd
    //   845: fload #7
    //   847: fsub
    //   848: fstore #9
    //   850: aload #20
    //   852: astore #4
    //   854: fload #8
    //   856: fstore #7
    //   858: fload #5
    //   860: fstore #6
    //   862: aload #4
    //   864: ifnull -> 1138
    //   867: getstatic android/support/constraint/solver/LinearSystem.sMetrics : Landroid/support/constraint/solver/Metrics;
    //   870: ifnull -> 927
    //   873: getstatic android/support/constraint/solver/LinearSystem.sMetrics : Landroid/support/constraint/solver/Metrics;
    //   876: astore #20
    //   878: aload #20
    //   880: aload #20
    //   882: getfield nonresolvedWidgets : J
    //   885: lconst_1
    //   886: lsub
    //   887: putfield nonresolvedWidgets : J
    //   890: getstatic android/support/constraint/solver/LinearSystem.sMetrics : Landroid/support/constraint/solver/Metrics;
    //   893: astore #20
    //   895: aload #20
    //   897: aload #20
    //   899: getfield resolvedWidgets : J
    //   902: lconst_1
    //   903: ladd
    //   904: putfield resolvedWidgets : J
    //   907: getstatic android/support/constraint/solver/LinearSystem.sMetrics : Landroid/support/constraint/solver/Metrics;
    //   910: astore #20
    //   912: aload #20
    //   914: aload #20
    //   916: getfield chainConnectionResolved : J
    //   919: lconst_1
    //   920: ladd
    //   921: putfield chainConnectionResolved : J
    //   924: goto -> 927
    //   927: aload #4
    //   929: getfield mNextChainWidget : [Landroid/support/constraint/solver/widgets/ConstraintWidget;
    //   932: iload #18
    //   934: aaload
    //   935: astore #20
    //   937: aload #20
    //   939: ifnonnull -> 955
    //   942: aload #4
    //   944: aload #22
    //   946: if_acmpne -> 952
    //   949: goto -> 955
    //   952: goto -> 1131
    //   955: fload #9
    //   957: iload #12
    //   959: i2f
    //   960: fdiv
    //   961: fstore #5
    //   963: fload #10
    //   965: fconst_0
    //   966: fcmpl
    //   967: ifle -> 1006
    //   970: aload #4
    //   972: getfield mWeight : [F
    //   975: iload #18
    //   977: faload
    //   978: ldc -1.0
    //   980: fcmpl
    //   981: ifne -> 990
    //   984: fconst_0
    //   985: fstore #5
    //   987: goto -> 1006
    //   990: aload #4
    //   992: getfield mWeight : [F
    //   995: iload #18
    //   997: faload
    //   998: fload #9
    //   1000: fmul
    //   1001: fload #10
    //   1003: fdiv
    //   1004: fstore #5
    //   1006: aload #4
    //   1008: invokevirtual getVisibility : ()I
    //   1011: bipush #8
    //   1013: if_icmpne -> 1019
    //   1016: fconst_0
    //   1017: fstore #5
    //   1019: fload #7
    //   1021: aload #4
    //   1023: getfield mListAnchors : [Landroid/support/constraint/solver/widgets/ConstraintAnchor;
    //   1026: iload_3
    //   1027: aaload
    //   1028: invokevirtual getMargin : ()I
    //   1031: i2f
    //   1032: fadd
    //   1033: fstore #7
    //   1035: aload #4
    //   1037: getfield mListAnchors : [Landroid/support/constraint/solver/widgets/ConstraintAnchor;
    //   1040: iload_3
    //   1041: aaload
    //   1042: invokevirtual getResolutionNode : ()Landroid/support/constraint/solver/widgets/ResolutionAnchor;
    //   1045: aload #21
    //   1047: getfield resolvedTarget : Landroid/support/constraint/solver/widgets/ResolutionAnchor;
    //   1050: fload #7
    //   1052: invokevirtual resolve : (Landroid/support/constraint/solver/widgets/ResolutionAnchor;F)V
    //   1055: aload #4
    //   1057: getfield mListAnchors : [Landroid/support/constraint/solver/widgets/ConstraintAnchor;
    //   1060: iload_3
    //   1061: iconst_1
    //   1062: iadd
    //   1063: aaload
    //   1064: invokevirtual getResolutionNode : ()Landroid/support/constraint/solver/widgets/ResolutionAnchor;
    //   1067: aload #21
    //   1069: getfield resolvedTarget : Landroid/support/constraint/solver/widgets/ResolutionAnchor;
    //   1072: fload #7
    //   1074: fload #5
    //   1076: fadd
    //   1077: invokevirtual resolve : (Landroid/support/constraint/solver/widgets/ResolutionAnchor;F)V
    //   1080: aload #4
    //   1082: getfield mListAnchors : [Landroid/support/constraint/solver/widgets/ConstraintAnchor;
    //   1085: iload_3
    //   1086: aaload
    //   1087: invokevirtual getResolutionNode : ()Landroid/support/constraint/solver/widgets/ResolutionAnchor;
    //   1090: aload_1
    //   1091: invokevirtual addResolvedValue : (Landroid/support/constraint/solver/LinearSystem;)V
    //   1094: aload #4
    //   1096: getfield mListAnchors : [Landroid/support/constraint/solver/widgets/ConstraintAnchor;
    //   1099: iload_3
    //   1100: iconst_1
    //   1101: iadd
    //   1102: aaload
    //   1103: invokevirtual getResolutionNode : ()Landroid/support/constraint/solver/widgets/ResolutionAnchor;
    //   1106: aload_1
    //   1107: invokevirtual addResolvedValue : (Landroid/support/constraint/solver/LinearSystem;)V
    //   1110: fload #7
    //   1112: fload #5
    //   1114: fadd
    //   1115: aload #4
    //   1117: getfield mListAnchors : [Landroid/support/constraint/solver/widgets/ConstraintAnchor;
    //   1120: iload_3
    //   1121: iconst_1
    //   1122: iadd
    //   1123: aaload
    //   1124: invokevirtual getMargin : ()I
    //   1127: i2f
    //   1128: fadd
    //   1129: fstore #7
    //   1131: aload #20
    //   1133: astore #4
    //   1135: goto -> 862
    //   1138: iconst_1
    //   1139: ireturn
    //   1140: fload #6
    //   1142: fconst_0
    //   1143: fcmpg
    //   1144: ifge -> 1156
    //   1147: iconst_0
    //   1148: istore #13
    //   1150: iconst_0
    //   1151: istore #14
    //   1153: iconst_1
    //   1154: istore #11
    //   1156: iload #11
    //   1158: ifeq -> 1409
    //   1161: aload #20
    //   1163: iload #18
    //   1165: invokevirtual getBiasPercent : (I)F
    //   1168: fstore #7
    //   1170: aload #20
    //   1172: astore_0
    //   1173: fload #7
    //   1175: fload #6
    //   1177: fload #5
    //   1179: fsub
    //   1180: fmul
    //   1181: fload #8
    //   1183: fadd
    //   1184: fstore #5
    //   1186: aload_0
    //   1187: ifnull -> 1859
    //   1190: getstatic android/support/constraint/solver/LinearSystem.sMetrics : Landroid/support/constraint/solver/Metrics;
    //   1193: ifnull -> 1247
    //   1196: getstatic android/support/constraint/solver/LinearSystem.sMetrics : Landroid/support/constraint/solver/Metrics;
    //   1199: astore #4
    //   1201: aload #4
    //   1203: aload #4
    //   1205: getfield nonresolvedWidgets : J
    //   1208: lconst_1
    //   1209: lsub
    //   1210: putfield nonresolvedWidgets : J
    //   1213: getstatic android/support/constraint/solver/LinearSystem.sMetrics : Landroid/support/constraint/solver/Metrics;
    //   1216: astore #4
    //   1218: aload #4
    //   1220: aload #4
    //   1222: getfield resolvedWidgets : J
    //   1225: lconst_1
    //   1226: ladd
    //   1227: putfield resolvedWidgets : J
    //   1230: getstatic android/support/constraint/solver/LinearSystem.sMetrics : Landroid/support/constraint/solver/Metrics;
    //   1233: astore #4
    //   1235: aload #4
    //   1237: aload #4
    //   1239: getfield chainConnectionResolved : J
    //   1242: lconst_1
    //   1243: ladd
    //   1244: putfield chainConnectionResolved : J
    //   1247: aload_0
    //   1248: getfield mNextChainWidget : [Landroid/support/constraint/solver/widgets/ConstraintWidget;
    //   1251: iload #18
    //   1253: aaload
    //   1254: astore #4
    //   1256: aload #4
    //   1258: ifnonnull -> 1271
    //   1261: fload #5
    //   1263: fstore #6
    //   1265: aload_0
    //   1266: aload #22
    //   1268: if_acmpne -> 1399
    //   1271: iload #18
    //   1273: ifne -> 1286
    //   1276: aload_0
    //   1277: invokevirtual getWidth : ()I
    //   1280: i2f
    //   1281: fstore #6
    //   1283: goto -> 1293
    //   1286: aload_0
    //   1287: invokevirtual getHeight : ()I
    //   1290: i2f
    //   1291: fstore #6
    //   1293: fload #5
    //   1295: aload_0
    //   1296: getfield mListAnchors : [Landroid/support/constraint/solver/widgets/ConstraintAnchor;
    //   1299: iload_3
    //   1300: aaload
    //   1301: invokevirtual getMargin : ()I
    //   1304: i2f
    //   1305: fadd
    //   1306: fstore #5
    //   1308: aload_0
    //   1309: getfield mListAnchors : [Landroid/support/constraint/solver/widgets/ConstraintAnchor;
    //   1312: iload_3
    //   1313: aaload
    //   1314: invokevirtual getResolutionNode : ()Landroid/support/constraint/solver/widgets/ResolutionAnchor;
    //   1317: aload #21
    //   1319: getfield resolvedTarget : Landroid/support/constraint/solver/widgets/ResolutionAnchor;
    //   1322: fload #5
    //   1324: invokevirtual resolve : (Landroid/support/constraint/solver/widgets/ResolutionAnchor;F)V
    //   1327: aload_0
    //   1328: getfield mListAnchors : [Landroid/support/constraint/solver/widgets/ConstraintAnchor;
    //   1331: iload_3
    //   1332: iconst_1
    //   1333: iadd
    //   1334: aaload
    //   1335: invokevirtual getResolutionNode : ()Landroid/support/constraint/solver/widgets/ResolutionAnchor;
    //   1338: aload #21
    //   1340: getfield resolvedTarget : Landroid/support/constraint/solver/widgets/ResolutionAnchor;
    //   1343: fload #5
    //   1345: fload #6
    //   1347: fadd
    //   1348: invokevirtual resolve : (Landroid/support/constraint/solver/widgets/ResolutionAnchor;F)V
    //   1351: aload_0
    //   1352: getfield mListAnchors : [Landroid/support/constraint/solver/widgets/ConstraintAnchor;
    //   1355: iload_3
    //   1356: aaload
    //   1357: invokevirtual getResolutionNode : ()Landroid/support/constraint/solver/widgets/ResolutionAnchor;
    //   1360: aload_1
    //   1361: invokevirtual addResolvedValue : (Landroid/support/constraint/solver/LinearSystem;)V
    //   1364: aload_0
    //   1365: getfield mListAnchors : [Landroid/support/constraint/solver/widgets/ConstraintAnchor;
    //   1368: iload_3
    //   1369: iconst_1
    //   1370: iadd
    //   1371: aaload
    //   1372: invokevirtual getResolutionNode : ()Landroid/support/constraint/solver/widgets/ResolutionAnchor;
    //   1375: aload_1
    //   1376: invokevirtual addResolvedValue : (Landroid/support/constraint/solver/LinearSystem;)V
    //   1379: fload #5
    //   1381: fload #6
    //   1383: fadd
    //   1384: aload_0
    //   1385: getfield mListAnchors : [Landroid/support/constraint/solver/widgets/ConstraintAnchor;
    //   1388: iload_3
    //   1389: iconst_1
    //   1390: iadd
    //   1391: aaload
    //   1392: invokevirtual getMargin : ()I
    //   1395: i2f
    //   1396: fadd
    //   1397: fstore #6
    //   1399: aload #4
    //   1401: astore_0
    //   1402: fload #6
    //   1404: fstore #5
    //   1406: goto -> 1186
    //   1409: iload #13
    //   1411: ifne -> 1419
    //   1414: iload #14
    //   1416: ifeq -> 1859
    //   1419: iload #13
    //   1421: ifeq -> 1434
    //   1424: fload #6
    //   1426: fload #5
    //   1428: fsub
    //   1429: fstore #7
    //   1431: goto -> 1450
    //   1434: fload #6
    //   1436: fstore #7
    //   1438: iload #14
    //   1440: ifeq -> 1450
    //   1443: fload #6
    //   1445: fload #5
    //   1447: fsub
    //   1448: fstore #7
    //   1450: fload #7
    //   1452: iload #16
    //   1454: iconst_1
    //   1455: iadd
    //   1456: i2f
    //   1457: fdiv
    //   1458: fstore #6
    //   1460: iload #14
    //   1462: ifeq -> 1490
    //   1465: iload #16
    //   1467: iconst_1
    //   1468: if_icmple -> 1484
    //   1471: fload #7
    //   1473: iload #16
    //   1475: iconst_1
    //   1476: isub
    //   1477: i2f
    //   1478: fdiv
    //   1479: fstore #6
    //   1481: goto -> 1490
    //   1484: fload #7
    //   1486: fconst_2
    //   1487: fdiv
    //   1488: fstore #6
    //   1490: fload #8
    //   1492: fstore #7
    //   1494: fload #7
    //   1496: fstore #5
    //   1498: aload #20
    //   1500: invokevirtual getVisibility : ()I
    //   1503: bipush #8
    //   1505: if_icmpeq -> 1515
    //   1508: fload #7
    //   1510: fload #6
    //   1512: fadd
    //   1513: fstore #5
    //   1515: fload #5
    //   1517: fstore #7
    //   1519: iload #14
    //   1521: ifeq -> 1550
    //   1524: fload #5
    //   1526: fstore #7
    //   1528: iload #16
    //   1530: iconst_1
    //   1531: if_icmple -> 1550
    //   1534: fload #8
    //   1536: aload #23
    //   1538: getfield mListAnchors : [Landroid/support/constraint/solver/widgets/ConstraintAnchor;
    //   1541: iload_3
    //   1542: aaload
    //   1543: invokevirtual getMargin : ()I
    //   1546: i2f
    //   1547: fadd
    //   1548: fstore #7
    //   1550: iload #13
    //   1552: ifeq -> 1586
    //   1555: aload #23
    //   1557: ifnull -> 1586
    //   1560: aload #23
    //   1562: getfield mListAnchors : [Landroid/support/constraint/solver/widgets/ConstraintAnchor;
    //   1565: iload_3
    //   1566: aaload
    //   1567: invokevirtual getMargin : ()I
    //   1570: i2f
    //   1571: fstore #5
    //   1573: aload #20
    //   1575: astore_0
    //   1576: fload #7
    //   1578: fload #5
    //   1580: fadd
    //   1581: fstore #5
    //   1583: goto -> 1593
    //   1586: aload #20
    //   1588: astore_0
    //   1589: fload #7
    //   1591: fstore #5
    //   1593: iload_2
    //   1594: istore #11
    //   1596: aload_0
    //   1597: ifnull -> 1859
    //   1600: getstatic android/support/constraint/solver/LinearSystem.sMetrics : Landroid/support/constraint/solver/Metrics;
    //   1603: ifnull -> 1657
    //   1606: getstatic android/support/constraint/solver/LinearSystem.sMetrics : Landroid/support/constraint/solver/Metrics;
    //   1609: astore #4
    //   1611: aload #4
    //   1613: aload #4
    //   1615: getfield nonresolvedWidgets : J
    //   1618: lconst_1
    //   1619: lsub
    //   1620: putfield nonresolvedWidgets : J
    //   1623: getstatic android/support/constraint/solver/LinearSystem.sMetrics : Landroid/support/constraint/solver/Metrics;
    //   1626: astore #4
    //   1628: aload #4
    //   1630: aload #4
    //   1632: getfield resolvedWidgets : J
    //   1635: lconst_1
    //   1636: ladd
    //   1637: putfield resolvedWidgets : J
    //   1640: getstatic android/support/constraint/solver/LinearSystem.sMetrics : Landroid/support/constraint/solver/Metrics;
    //   1643: astore #4
    //   1645: aload #4
    //   1647: aload #4
    //   1649: getfield chainConnectionResolved : J
    //   1652: lconst_1
    //   1653: ladd
    //   1654: putfield chainConnectionResolved : J
    //   1657: aload_0
    //   1658: getfield mNextChainWidget : [Landroid/support/constraint/solver/widgets/ConstraintWidget;
    //   1661: iload #11
    //   1663: aaload
    //   1664: astore #4
    //   1666: aload #4
    //   1668: ifnonnull -> 1683
    //   1671: aload_0
    //   1672: aload #22
    //   1674: if_acmpne -> 1680
    //   1677: goto -> 1683
    //   1680: goto -> 1853
    //   1683: iload #11
    //   1685: ifne -> 1698
    //   1688: aload_0
    //   1689: invokevirtual getWidth : ()I
    //   1692: i2f
    //   1693: fstore #7
    //   1695: goto -> 1705
    //   1698: aload_0
    //   1699: invokevirtual getHeight : ()I
    //   1702: i2f
    //   1703: fstore #7
    //   1705: aload_0
    //   1706: aload #23
    //   1708: if_acmpeq -> 1729
    //   1711: fload #5
    //   1713: aload_0
    //   1714: getfield mListAnchors : [Landroid/support/constraint/solver/widgets/ConstraintAnchor;
    //   1717: iload_3
    //   1718: aaload
    //   1719: invokevirtual getMargin : ()I
    //   1722: i2f
    //   1723: fadd
    //   1724: fstore #5
    //   1726: goto -> 1729
    //   1729: aload_0
    //   1730: getfield mListAnchors : [Landroid/support/constraint/solver/widgets/ConstraintAnchor;
    //   1733: iload_3
    //   1734: aaload
    //   1735: invokevirtual getResolutionNode : ()Landroid/support/constraint/solver/widgets/ResolutionAnchor;
    //   1738: aload #21
    //   1740: getfield resolvedTarget : Landroid/support/constraint/solver/widgets/ResolutionAnchor;
    //   1743: fload #5
    //   1745: invokevirtual resolve : (Landroid/support/constraint/solver/widgets/ResolutionAnchor;F)V
    //   1748: aload_0
    //   1749: getfield mListAnchors : [Landroid/support/constraint/solver/widgets/ConstraintAnchor;
    //   1752: iload_3
    //   1753: iconst_1
    //   1754: iadd
    //   1755: aaload
    //   1756: invokevirtual getResolutionNode : ()Landroid/support/constraint/solver/widgets/ResolutionAnchor;
    //   1759: aload #21
    //   1761: getfield resolvedTarget : Landroid/support/constraint/solver/widgets/ResolutionAnchor;
    //   1764: fload #5
    //   1766: fload #7
    //   1768: fadd
    //   1769: invokevirtual resolve : (Landroid/support/constraint/solver/widgets/ResolutionAnchor;F)V
    //   1772: aload_0
    //   1773: getfield mListAnchors : [Landroid/support/constraint/solver/widgets/ConstraintAnchor;
    //   1776: iload_3
    //   1777: aaload
    //   1778: invokevirtual getResolutionNode : ()Landroid/support/constraint/solver/widgets/ResolutionAnchor;
    //   1781: aload_1
    //   1782: invokevirtual addResolvedValue : (Landroid/support/constraint/solver/LinearSystem;)V
    //   1785: aload_0
    //   1786: getfield mListAnchors : [Landroid/support/constraint/solver/widgets/ConstraintAnchor;
    //   1789: iload_3
    //   1790: iconst_1
    //   1791: iadd
    //   1792: aaload
    //   1793: invokevirtual getResolutionNode : ()Landroid/support/constraint/solver/widgets/ResolutionAnchor;
    //   1796: aload_1
    //   1797: invokevirtual addResolvedValue : (Landroid/support/constraint/solver/LinearSystem;)V
    //   1800: fload #5
    //   1802: aload_0
    //   1803: getfield mListAnchors : [Landroid/support/constraint/solver/widgets/ConstraintAnchor;
    //   1806: iload_3
    //   1807: iconst_1
    //   1808: iadd
    //   1809: aaload
    //   1810: invokevirtual getMargin : ()I
    //   1813: i2f
    //   1814: fload #7
    //   1816: fadd
    //   1817: fadd
    //   1818: fstore #7
    //   1820: aload #4
    //   1822: ifnull -> 1849
    //   1825: fload #7
    //   1827: fstore #5
    //   1829: aload #4
    //   1831: invokevirtual getVisibility : ()I
    //   1834: bipush #8
    //   1836: if_icmpeq -> 1853
    //   1839: fload #7
    //   1841: fload #6
    //   1843: fadd
    //   1844: fstore #5
    //   1846: goto -> 1853
    //   1849: fload #7
    //   1851: fstore #5
    //   1853: aload #4
    //   1855: astore_0
    //   1856: goto -> 1593
    //   1859: iconst_1
    //   1860: ireturn
    //   1861: iconst_0
    //   1862: ireturn
    //   1863: iconst_0
    //   1864: ireturn }
  
  static void checkMatchParent(ConstraintWidgetContainer paramConstraintWidgetContainer, LinearSystem paramLinearSystem, ConstraintWidget paramConstraintWidget) {
    if (paramConstraintWidgetContainer.mListDimensionBehaviors[false] != ConstraintWidget.DimensionBehaviour.WRAP_CONTENT && paramConstraintWidget.mListDimensionBehaviors[false] == ConstraintWidget.DimensionBehaviour.MATCH_PARENT) {
      int i = paramConstraintWidget.mLeft.mMargin;
      int j = paramConstraintWidgetContainer.getWidth() - paramConstraintWidget.mRight.mMargin;
      paramConstraintWidget.mLeft.mSolverVariable = paramLinearSystem.createObjectVariable(paramConstraintWidget.mLeft);
      paramConstraintWidget.mRight.mSolverVariable = paramLinearSystem.createObjectVariable(paramConstraintWidget.mRight);
      paramLinearSystem.addEquality(paramConstraintWidget.mLeft.mSolverVariable, i);
      paramLinearSystem.addEquality(paramConstraintWidget.mRight.mSolverVariable, j);
      paramConstraintWidget.mHorizontalResolution = 2;
      paramConstraintWidget.setHorizontalDimension(i, j);
    } 
    if (paramConstraintWidgetContainer.mListDimensionBehaviors[true] != ConstraintWidget.DimensionBehaviour.WRAP_CONTENT && paramConstraintWidget.mListDimensionBehaviors[true] == ConstraintWidget.DimensionBehaviour.MATCH_PARENT) {
      int i = paramConstraintWidget.mTop.mMargin;
      int j = paramConstraintWidgetContainer.getHeight() - paramConstraintWidget.mBottom.mMargin;
      paramConstraintWidget.mTop.mSolverVariable = paramLinearSystem.createObjectVariable(paramConstraintWidget.mTop);
      paramConstraintWidget.mBottom.mSolverVariable = paramLinearSystem.createObjectVariable(paramConstraintWidget.mBottom);
      paramLinearSystem.addEquality(paramConstraintWidget.mTop.mSolverVariable, i);
      paramLinearSystem.addEquality(paramConstraintWidget.mBottom.mSolverVariable, j);
      if (paramConstraintWidget.mBaselineDistance > 0 || paramConstraintWidget.getVisibility() == 8) {
        paramConstraintWidget.mBaseline.mSolverVariable = paramLinearSystem.createObjectVariable(paramConstraintWidget.mBaseline);
        paramLinearSystem.addEquality(paramConstraintWidget.mBaseline.mSolverVariable, paramConstraintWidget.mBaselineDistance + i);
      } 
      paramConstraintWidget.mVerticalResolution = 2;
      paramConstraintWidget.setVerticalDimension(i, j);
    } 
  }
  
  private static boolean optimizableMatchConstraint(ConstraintWidget paramConstraintWidget, int paramInt) {
    ConstraintWidget.DimensionBehaviour[] arrayOfDimensionBehaviour;
    if (paramConstraintWidget.mListDimensionBehaviors[paramInt] != ConstraintWidget.DimensionBehaviour.MATCH_CONSTRAINT)
      return false; 
    float f = paramConstraintWidget.mDimensionRatio;
    int i = 1;
    if (f != 0.0F) {
      arrayOfDimensionBehaviour = paramConstraintWidget.mListDimensionBehaviors;
      if (paramInt == 0) {
        paramInt = i;
      } else {
        paramInt = 0;
      } 
      return (arrayOfDimensionBehaviour[paramInt] == ConstraintWidget.DimensionBehaviour.MATCH_CONSTRAINT) ? false : false;
    } 
    if (paramInt == 0) {
      if (arrayOfDimensionBehaviour.mMatchConstraintDefaultWidth != 0)
        return false; 
      if (arrayOfDimensionBehaviour.mMatchConstraintMinWidth != 0 || arrayOfDimensionBehaviour.mMatchConstraintMaxWidth != 0)
        return false; 
    } else {
      return (arrayOfDimensionBehaviour.mMatchConstraintDefaultHeight != 0) ? false : ((arrayOfDimensionBehaviour.mMatchConstraintMinHeight == 0) ? (!(arrayOfDimensionBehaviour.mMatchConstraintMaxHeight != 0)) : false);
    } 
    return true;
  }
  
  static void setOptimizedWidget(ConstraintWidget paramConstraintWidget, int paramInt1, int paramInt2) {
    int i = paramInt1 * 2;
    int j = i + 1;
    (paramConstraintWidget.mListAnchors[i].getResolutionNode()).resolvedTarget = (paramConstraintWidget.getParent()).mLeft.getResolutionNode();
    (paramConstraintWidget.mListAnchors[i].getResolutionNode()).resolvedOffset = paramInt2;
    (paramConstraintWidget.mListAnchors[i].getResolutionNode()).state = 1;
    (paramConstraintWidget.mListAnchors[j].getResolutionNode()).resolvedTarget = paramConstraintWidget.mListAnchors[i].getResolutionNode();
    (paramConstraintWidget.mListAnchors[j].getResolutionNode()).resolvedOffset = paramConstraintWidget.getLength(paramInt1);
    (paramConstraintWidget.mListAnchors[j].getResolutionNode()).state = 1;
  }
}


/* Location:              D:\Apks\New folder\classes-dex2jar.jar!\android\support\constraint\solver\widgets\Optimizer.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.0.7
 */