package android.support.constraint.solver.widgets;

import android.support.constraint.solver.LinearSystem;

class Chain {
  private static final boolean DEBUG = false;
  
  static void applyChainConstraints(ConstraintWidgetContainer paramConstraintWidgetContainer, LinearSystem paramLinearSystem, int paramInt) {
    ChainHead[] arrayOfChainHead;
    int i;
    byte b1;
    if (paramInt == 0) {
      b1 = 0;
      i = paramConstraintWidgetContainer.mHorizontalChainsSize;
      arrayOfChainHead = paramConstraintWidgetContainer.mHorizontalChainsArray;
    } else {
      b1 = 2;
      i = paramConstraintWidgetContainer.mVerticalChainsSize;
      arrayOfChainHead = paramConstraintWidgetContainer.mVerticalChainsArray;
    } 
    byte b2;
    for (b2 = 0; b2 < i; b2++) {
      ChainHead chainHead = arrayOfChainHead[b2];
      chainHead.define();
      if (paramConstraintWidgetContainer.optimizeFor(4)) {
        if (!Optimizer.applyChainOptimized(paramConstraintWidgetContainer, paramLinearSystem, paramInt, b1, chainHead))
          applyChainConstraints(paramConstraintWidgetContainer, paramLinearSystem, paramInt, b1, chainHead); 
      } else {
        applyChainConstraints(paramConstraintWidgetContainer, paramLinearSystem, paramInt, b1, chainHead);
      } 
    } 
  }
  
  static void applyChainConstraints(ConstraintWidgetContainer paramConstraintWidgetContainer, LinearSystem paramLinearSystem, int paramInt1, int paramInt2, ChainHead paramChainHead) { // Byte code:
    //   0: aload #4
    //   2: getfield mFirst : Landroid/support/constraint/solver/widgets/ConstraintWidget;
    //   5: astore #19
    //   7: aload #4
    //   9: getfield mLast : Landroid/support/constraint/solver/widgets/ConstraintWidget;
    //   12: astore #23
    //   14: aload #4
    //   16: getfield mFirstVisibleWidget : Landroid/support/constraint/solver/widgets/ConstraintWidget;
    //   19: astore #16
    //   21: aload #4
    //   23: getfield mLastVisibleWidget : Landroid/support/constraint/solver/widgets/ConstraintWidget;
    //   26: astore #24
    //   28: aload #4
    //   30: getfield mHead : Landroid/support/constraint/solver/widgets/ConstraintWidget;
    //   33: astore #22
    //   35: aload #4
    //   37: getfield mTotalWeight : F
    //   40: fstore #5
    //   42: aload #4
    //   44: getfield mFirstMatchConstraintWidget : Landroid/support/constraint/solver/widgets/ConstraintWidget;
    //   47: astore #20
    //   49: aload #4
    //   51: getfield mLastMatchConstraintWidget : Landroid/support/constraint/solver/widgets/ConstraintWidget;
    //   54: astore #18
    //   56: aload_0
    //   57: getfield mListDimensionBehaviors : [Landroid/support/constraint/solver/widgets/ConstraintWidget$DimensionBehaviour;
    //   60: iload_2
    //   61: aaload
    //   62: getstatic android/support/constraint/solver/widgets/ConstraintWidget$DimensionBehaviour.WRAP_CONTENT : Landroid/support/constraint/solver/widgets/ConstraintWidget$DimensionBehaviour;
    //   65: if_acmpne -> 74
    //   68: iconst_1
    //   69: istore #12
    //   71: goto -> 77
    //   74: iconst_0
    //   75: istore #12
    //   77: iload_2
    //   78: ifne -> 160
    //   81: aload #22
    //   83: getfield mHorizontalChainStyle : I
    //   86: ifne -> 95
    //   89: iconst_1
    //   90: istore #8
    //   92: goto -> 98
    //   95: iconst_0
    //   96: istore #8
    //   98: aload #22
    //   100: getfield mHorizontalChainStyle : I
    //   103: istore #9
    //   105: iload #8
    //   107: istore #11
    //   109: iload #9
    //   111: iconst_1
    //   112: if_icmpne -> 121
    //   115: iconst_1
    //   116: istore #8
    //   118: goto -> 124
    //   121: iconst_0
    //   122: istore #8
    //   124: aload #22
    //   126: getfield mHorizontalChainStyle : I
    //   129: iconst_2
    //   130: if_icmpne -> 139
    //   133: iconst_1
    //   134: istore #9
    //   136: goto -> 142
    //   139: iconst_0
    //   140: istore #9
    //   142: iload #8
    //   144: istore #10
    //   146: aload #19
    //   148: astore #15
    //   150: iconst_0
    //   151: istore #8
    //   153: iload #9
    //   155: istore #13
    //   157: goto -> 240
    //   160: aload #22
    //   162: getfield mVerticalChainStyle : I
    //   165: ifne -> 174
    //   168: iconst_1
    //   169: istore #8
    //   171: goto -> 177
    //   174: iconst_0
    //   175: istore #8
    //   177: aload #22
    //   179: getfield mVerticalChainStyle : I
    //   182: istore #9
    //   184: iload #8
    //   186: istore #11
    //   188: iload #9
    //   190: iconst_1
    //   191: if_icmpne -> 200
    //   194: iconst_1
    //   195: istore #8
    //   197: goto -> 203
    //   200: iconst_0
    //   201: istore #8
    //   203: aload #22
    //   205: getfield mVerticalChainStyle : I
    //   208: iconst_2
    //   209: if_icmpne -> 218
    //   212: iconst_1
    //   213: istore #9
    //   215: goto -> 221
    //   218: iconst_0
    //   219: istore #9
    //   221: aload #19
    //   223: astore #15
    //   225: iconst_0
    //   226: istore #14
    //   228: iload #8
    //   230: istore #10
    //   232: iload #9
    //   234: istore #13
    //   236: iload #14
    //   238: istore #8
    //   240: iload #8
    //   242: ifne -> 612
    //   245: aload #15
    //   247: getfield mListAnchors : [Landroid/support/constraint/solver/widgets/ConstraintAnchor;
    //   250: iload_3
    //   251: aaload
    //   252: astore #17
    //   254: iconst_4
    //   255: istore #9
    //   257: iload #12
    //   259: ifne -> 267
    //   262: iload #13
    //   264: ifeq -> 270
    //   267: iconst_1
    //   268: istore #9
    //   270: aload #17
    //   272: invokevirtual getMargin : ()I
    //   275: istore #14
    //   277: aload #17
    //   279: getfield mTarget : Landroid/support/constraint/solver/widgets/ConstraintAnchor;
    //   282: ifnull -> 308
    //   285: aload #15
    //   287: aload #19
    //   289: if_acmpeq -> 308
    //   292: iload #14
    //   294: aload #17
    //   296: getfield mTarget : Landroid/support/constraint/solver/widgets/ConstraintAnchor;
    //   299: invokevirtual getMargin : ()I
    //   302: iadd
    //   303: istore #14
    //   305: goto -> 308
    //   308: iload #13
    //   310: ifeq -> 334
    //   313: aload #15
    //   315: aload #19
    //   317: if_acmpeq -> 334
    //   320: aload #15
    //   322: aload #16
    //   324: if_acmpeq -> 334
    //   327: bipush #6
    //   329: istore #9
    //   331: goto -> 350
    //   334: iload #11
    //   336: ifeq -> 350
    //   339: iload #12
    //   341: ifeq -> 350
    //   344: iconst_4
    //   345: istore #9
    //   347: goto -> 350
    //   350: aload #17
    //   352: getfield mTarget : Landroid/support/constraint/solver/widgets/ConstraintAnchor;
    //   355: ifnull -> 434
    //   358: aload #15
    //   360: aload #16
    //   362: if_acmpne -> 388
    //   365: aload_1
    //   366: aload #17
    //   368: getfield mSolverVariable : Landroid/support/constraint/solver/SolverVariable;
    //   371: aload #17
    //   373: getfield mTarget : Landroid/support/constraint/solver/widgets/ConstraintAnchor;
    //   376: getfield mSolverVariable : Landroid/support/constraint/solver/SolverVariable;
    //   379: iload #14
    //   381: iconst_5
    //   382: invokevirtual addGreaterThan : (Landroid/support/constraint/solver/SolverVariable;Landroid/support/constraint/solver/SolverVariable;II)V
    //   385: goto -> 409
    //   388: aload_1
    //   389: aload #17
    //   391: getfield mSolverVariable : Landroid/support/constraint/solver/SolverVariable;
    //   394: aload #17
    //   396: getfield mTarget : Landroid/support/constraint/solver/widgets/ConstraintAnchor;
    //   399: getfield mSolverVariable : Landroid/support/constraint/solver/SolverVariable;
    //   402: iload #14
    //   404: bipush #6
    //   406: invokevirtual addGreaterThan : (Landroid/support/constraint/solver/SolverVariable;Landroid/support/constraint/solver/SolverVariable;II)V
    //   409: aload_1
    //   410: aload #17
    //   412: getfield mSolverVariable : Landroid/support/constraint/solver/SolverVariable;
    //   415: aload #17
    //   417: getfield mTarget : Landroid/support/constraint/solver/widgets/ConstraintAnchor;
    //   420: getfield mSolverVariable : Landroid/support/constraint/solver/SolverVariable;
    //   423: iload #14
    //   425: iload #9
    //   427: invokevirtual addEquality : (Landroid/support/constraint/solver/SolverVariable;Landroid/support/constraint/solver/SolverVariable;II)Landroid/support/constraint/solver/ArrayRow;
    //   430: pop
    //   431: goto -> 434
    //   434: iload #12
    //   436: ifeq -> 522
    //   439: aload #15
    //   441: invokevirtual getVisibility : ()I
    //   444: bipush #8
    //   446: if_icmpeq -> 493
    //   449: aload #15
    //   451: getfield mListDimensionBehaviors : [Landroid/support/constraint/solver/widgets/ConstraintWidget$DimensionBehaviour;
    //   454: iload_2
    //   455: aaload
    //   456: getstatic android/support/constraint/solver/widgets/ConstraintWidget$DimensionBehaviour.MATCH_CONSTRAINT : Landroid/support/constraint/solver/widgets/ConstraintWidget$DimensionBehaviour;
    //   459: if_acmpne -> 493
    //   462: aload_1
    //   463: aload #15
    //   465: getfield mListAnchors : [Landroid/support/constraint/solver/widgets/ConstraintAnchor;
    //   468: iload_3
    //   469: iconst_1
    //   470: iadd
    //   471: aaload
    //   472: getfield mSolverVariable : Landroid/support/constraint/solver/SolverVariable;
    //   475: aload #15
    //   477: getfield mListAnchors : [Landroid/support/constraint/solver/widgets/ConstraintAnchor;
    //   480: iload_3
    //   481: aaload
    //   482: getfield mSolverVariable : Landroid/support/constraint/solver/SolverVariable;
    //   485: iconst_0
    //   486: iconst_5
    //   487: invokevirtual addGreaterThan : (Landroid/support/constraint/solver/SolverVariable;Landroid/support/constraint/solver/SolverVariable;II)V
    //   490: goto -> 493
    //   493: aload_1
    //   494: aload #15
    //   496: getfield mListAnchors : [Landroid/support/constraint/solver/widgets/ConstraintAnchor;
    //   499: iload_3
    //   500: aaload
    //   501: getfield mSolverVariable : Landroid/support/constraint/solver/SolverVariable;
    //   504: aload_0
    //   505: getfield mListAnchors : [Landroid/support/constraint/solver/widgets/ConstraintAnchor;
    //   508: iload_3
    //   509: aaload
    //   510: getfield mSolverVariable : Landroid/support/constraint/solver/SolverVariable;
    //   513: iconst_0
    //   514: bipush #6
    //   516: invokevirtual addGreaterThan : (Landroid/support/constraint/solver/SolverVariable;Landroid/support/constraint/solver/SolverVariable;II)V
    //   519: goto -> 522
    //   522: aload #15
    //   524: getfield mListAnchors : [Landroid/support/constraint/solver/widgets/ConstraintAnchor;
    //   527: iload_3
    //   528: iconst_1
    //   529: iadd
    //   530: aaload
    //   531: getfield mTarget : Landroid/support/constraint/solver/widgets/ConstraintAnchor;
    //   534: astore #17
    //   536: aload #17
    //   538: ifnull -> 591
    //   541: aload #17
    //   543: getfield mOwner : Landroid/support/constraint/solver/widgets/ConstraintWidget;
    //   546: astore #17
    //   548: aload #17
    //   550: getfield mListAnchors : [Landroid/support/constraint/solver/widgets/ConstraintAnchor;
    //   553: iload_3
    //   554: aaload
    //   555: getfield mTarget : Landroid/support/constraint/solver/widgets/ConstraintAnchor;
    //   558: ifnull -> 585
    //   561: aload #17
    //   563: getfield mListAnchors : [Landroid/support/constraint/solver/widgets/ConstraintAnchor;
    //   566: iload_3
    //   567: aaload
    //   568: getfield mTarget : Landroid/support/constraint/solver/widgets/ConstraintAnchor;
    //   571: getfield mOwner : Landroid/support/constraint/solver/widgets/ConstraintWidget;
    //   574: aload #15
    //   576: if_acmpeq -> 582
    //   579: goto -> 585
    //   582: goto -> 594
    //   585: aconst_null
    //   586: astore #17
    //   588: goto -> 594
    //   591: aconst_null
    //   592: astore #17
    //   594: aload #17
    //   596: ifnull -> 606
    //   599: aload #17
    //   601: astore #15
    //   603: goto -> 609
    //   606: iconst_1
    //   607: istore #8
    //   609: goto -> 240
    //   612: aload #24
    //   614: ifnull -> 677
    //   617: aload #23
    //   619: getfield mListAnchors : [Landroid/support/constraint/solver/widgets/ConstraintAnchor;
    //   622: iload_3
    //   623: iconst_1
    //   624: iadd
    //   625: aaload
    //   626: getfield mTarget : Landroid/support/constraint/solver/widgets/ConstraintAnchor;
    //   629: ifnull -> 677
    //   632: aload #24
    //   634: getfield mListAnchors : [Landroid/support/constraint/solver/widgets/ConstraintAnchor;
    //   637: iload_3
    //   638: iconst_1
    //   639: iadd
    //   640: aaload
    //   641: astore #17
    //   643: aload_1
    //   644: aload #17
    //   646: getfield mSolverVariable : Landroid/support/constraint/solver/SolverVariable;
    //   649: aload #23
    //   651: getfield mListAnchors : [Landroid/support/constraint/solver/widgets/ConstraintAnchor;
    //   654: iload_3
    //   655: iconst_1
    //   656: iadd
    //   657: aaload
    //   658: getfield mTarget : Landroid/support/constraint/solver/widgets/ConstraintAnchor;
    //   661: getfield mSolverVariable : Landroid/support/constraint/solver/SolverVariable;
    //   664: aload #17
    //   666: invokevirtual getMargin : ()I
    //   669: ineg
    //   670: iconst_5
    //   671: invokevirtual addLowerThan : (Landroid/support/constraint/solver/SolverVariable;Landroid/support/constraint/solver/SolverVariable;II)V
    //   674: goto -> 677
    //   677: iload #12
    //   679: ifeq -> 723
    //   682: aload_1
    //   683: aload_0
    //   684: getfield mListAnchors : [Landroid/support/constraint/solver/widgets/ConstraintAnchor;
    //   687: iload_3
    //   688: iconst_1
    //   689: iadd
    //   690: aaload
    //   691: getfield mSolverVariable : Landroid/support/constraint/solver/SolverVariable;
    //   694: aload #23
    //   696: getfield mListAnchors : [Landroid/support/constraint/solver/widgets/ConstraintAnchor;
    //   699: iload_3
    //   700: iconst_1
    //   701: iadd
    //   702: aaload
    //   703: getfield mSolverVariable : Landroid/support/constraint/solver/SolverVariable;
    //   706: aload #23
    //   708: getfield mListAnchors : [Landroid/support/constraint/solver/widgets/ConstraintAnchor;
    //   711: iload_3
    //   712: iconst_1
    //   713: iadd
    //   714: aaload
    //   715: invokevirtual getMargin : ()I
    //   718: bipush #6
    //   720: invokevirtual addGreaterThan : (Landroid/support/constraint/solver/SolverVariable;Landroid/support/constraint/solver/SolverVariable;II)V
    //   723: aload #4
    //   725: getfield mWeightedMatchConstraintsWidgets : Ljava/util/ArrayList;
    //   728: astore_0
    //   729: aload_0
    //   730: ifnull -> 1048
    //   733: aload_0
    //   734: invokevirtual size : ()I
    //   737: istore #8
    //   739: iload #8
    //   741: iconst_1
    //   742: if_icmple -> 1039
    //   745: aload #4
    //   747: getfield mHasUndefinedWeights : Z
    //   750: ifeq -> 772
    //   753: aload #4
    //   755: getfield mHasComplexMatchWeights : Z
    //   758: ifne -> 772
    //   761: aload #4
    //   763: getfield mWidgetsMatchCount : I
    //   766: i2f
    //   767: fstore #6
    //   769: goto -> 776
    //   772: fload #5
    //   774: fstore #6
    //   776: aconst_null
    //   777: astore #21
    //   779: iconst_0
    //   780: istore #9
    //   782: fconst_0
    //   783: fstore #7
    //   785: aload #20
    //   787: astore #17
    //   789: aload #21
    //   791: astore #20
    //   793: iload #9
    //   795: iload #8
    //   797: if_icmpge -> 1030
    //   800: aload_0
    //   801: iload #9
    //   803: invokevirtual get : (I)Ljava/lang/Object;
    //   806: checkcast android/support/constraint/solver/widgets/ConstraintWidget
    //   809: astore #21
    //   811: aload #21
    //   813: getfield mWeight : [F
    //   816: iload_2
    //   817: faload
    //   818: fstore #5
    //   820: fload #5
    //   822: fconst_0
    //   823: fcmpg
    //   824: ifge -> 877
    //   827: aload #4
    //   829: getfield mHasComplexMatchWeights : Z
    //   832: ifeq -> 871
    //   835: aload_1
    //   836: aload #21
    //   838: getfield mListAnchors : [Landroid/support/constraint/solver/widgets/ConstraintAnchor;
    //   841: iload_3
    //   842: iconst_1
    //   843: iadd
    //   844: aaload
    //   845: getfield mSolverVariable : Landroid/support/constraint/solver/SolverVariable;
    //   848: aload #21
    //   850: getfield mListAnchors : [Landroid/support/constraint/solver/widgets/ConstraintAnchor;
    //   853: iload_3
    //   854: aaload
    //   855: getfield mSolverVariable : Landroid/support/constraint/solver/SolverVariable;
    //   858: iconst_0
    //   859: iconst_4
    //   860: invokevirtual addEquality : (Landroid/support/constraint/solver/SolverVariable;Landroid/support/constraint/solver/SolverVariable;II)Landroid/support/constraint/solver/ArrayRow;
    //   863: pop
    //   864: fload #7
    //   866: fstore #5
    //   868: goto -> 1017
    //   871: fconst_1
    //   872: fstore #5
    //   874: goto -> 877
    //   877: fload #5
    //   879: fconst_0
    //   880: fcmpl
    //   881: ifne -> 921
    //   884: aload_1
    //   885: aload #21
    //   887: getfield mListAnchors : [Landroid/support/constraint/solver/widgets/ConstraintAnchor;
    //   890: iload_3
    //   891: iconst_1
    //   892: iadd
    //   893: aaload
    //   894: getfield mSolverVariable : Landroid/support/constraint/solver/SolverVariable;
    //   897: aload #21
    //   899: getfield mListAnchors : [Landroid/support/constraint/solver/widgets/ConstraintAnchor;
    //   902: iload_3
    //   903: aaload
    //   904: getfield mSolverVariable : Landroid/support/constraint/solver/SolverVariable;
    //   907: iconst_0
    //   908: bipush #6
    //   910: invokevirtual addEquality : (Landroid/support/constraint/solver/SolverVariable;Landroid/support/constraint/solver/SolverVariable;II)Landroid/support/constraint/solver/ArrayRow;
    //   913: pop
    //   914: fload #7
    //   916: fstore #5
    //   918: goto -> 1017
    //   921: aload #20
    //   923: ifnull -> 1013
    //   926: aload #20
    //   928: getfield mListAnchors : [Landroid/support/constraint/solver/widgets/ConstraintAnchor;
    //   931: iload_3
    //   932: aaload
    //   933: getfield mSolverVariable : Landroid/support/constraint/solver/SolverVariable;
    //   936: astore #25
    //   938: aload #20
    //   940: getfield mListAnchors : [Landroid/support/constraint/solver/widgets/ConstraintAnchor;
    //   943: iload_3
    //   944: iconst_1
    //   945: iadd
    //   946: aaload
    //   947: getfield mSolverVariable : Landroid/support/constraint/solver/SolverVariable;
    //   950: astore #20
    //   952: aload #21
    //   954: getfield mListAnchors : [Landroid/support/constraint/solver/widgets/ConstraintAnchor;
    //   957: iload_3
    //   958: aaload
    //   959: getfield mSolverVariable : Landroid/support/constraint/solver/SolverVariable;
    //   962: astore #26
    //   964: aload #21
    //   966: getfield mListAnchors : [Landroid/support/constraint/solver/widgets/ConstraintAnchor;
    //   969: iload_3
    //   970: iconst_1
    //   971: iadd
    //   972: aaload
    //   973: getfield mSolverVariable : Landroid/support/constraint/solver/SolverVariable;
    //   976: astore #27
    //   978: aload_1
    //   979: invokevirtual createRow : ()Landroid/support/constraint/solver/ArrayRow;
    //   982: astore #28
    //   984: aload #28
    //   986: fload #7
    //   988: fload #6
    //   990: fload #5
    //   992: aload #25
    //   994: aload #20
    //   996: aload #26
    //   998: aload #27
    //   1000: invokevirtual createRowEqualMatchDimensions : (FFFLandroid/support/constraint/solver/SolverVariable;Landroid/support/constraint/solver/SolverVariable;Landroid/support/constraint/solver/SolverVariable;Landroid/support/constraint/solver/SolverVariable;)Landroid/support/constraint/solver/ArrayRow;
    //   1003: pop
    //   1004: aload_1
    //   1005: aload #28
    //   1007: invokevirtual addConstraint : (Landroid/support/constraint/solver/ArrayRow;)V
    //   1010: goto -> 1013
    //   1013: aload #21
    //   1015: astore #20
    //   1017: iload #9
    //   1019: iconst_1
    //   1020: iadd
    //   1021: istore #9
    //   1023: fload #5
    //   1025: fstore #7
    //   1027: goto -> 793
    //   1030: aload #15
    //   1032: astore_0
    //   1033: aload #18
    //   1035: astore_0
    //   1036: goto -> 1060
    //   1039: aload #15
    //   1041: astore_0
    //   1042: aload #18
    //   1044: astore_0
    //   1045: goto -> 1060
    //   1048: aload_0
    //   1049: astore #17
    //   1051: aload #18
    //   1053: astore_0
    //   1054: aload #15
    //   1056: astore_0
    //   1057: aload #17
    //   1059: astore_0
    //   1060: aload #16
    //   1062: ifnull -> 1275
    //   1065: aload #16
    //   1067: aload #24
    //   1069: if_acmpeq -> 1083
    //   1072: iload #13
    //   1074: ifeq -> 1080
    //   1077: goto -> 1083
    //   1080: goto -> 1275
    //   1083: aload #19
    //   1085: getfield mListAnchors : [Landroid/support/constraint/solver/widgets/ConstraintAnchor;
    //   1088: iload_3
    //   1089: aaload
    //   1090: astore #15
    //   1092: aload #23
    //   1094: getfield mListAnchors : [Landroid/support/constraint/solver/widgets/ConstraintAnchor;
    //   1097: iload_3
    //   1098: iconst_1
    //   1099: iadd
    //   1100: aaload
    //   1101: astore #17
    //   1103: aload #19
    //   1105: getfield mListAnchors : [Landroid/support/constraint/solver/widgets/ConstraintAnchor;
    //   1108: iload_3
    //   1109: aaload
    //   1110: getfield mTarget : Landroid/support/constraint/solver/widgets/ConstraintAnchor;
    //   1113: ifnull -> 1133
    //   1116: aload #19
    //   1118: getfield mListAnchors : [Landroid/support/constraint/solver/widgets/ConstraintAnchor;
    //   1121: iload_3
    //   1122: aaload
    //   1123: getfield mTarget : Landroid/support/constraint/solver/widgets/ConstraintAnchor;
    //   1126: getfield mSolverVariable : Landroid/support/constraint/solver/SolverVariable;
    //   1129: astore_0
    //   1130: goto -> 1135
    //   1133: aconst_null
    //   1134: astore_0
    //   1135: aload #23
    //   1137: getfield mListAnchors : [Landroid/support/constraint/solver/widgets/ConstraintAnchor;
    //   1140: iload_3
    //   1141: iconst_1
    //   1142: iadd
    //   1143: aaload
    //   1144: getfield mTarget : Landroid/support/constraint/solver/widgets/ConstraintAnchor;
    //   1147: ifnull -> 1170
    //   1150: aload #23
    //   1152: getfield mListAnchors : [Landroid/support/constraint/solver/widgets/ConstraintAnchor;
    //   1155: iload_3
    //   1156: iconst_1
    //   1157: iadd
    //   1158: aaload
    //   1159: getfield mTarget : Landroid/support/constraint/solver/widgets/ConstraintAnchor;
    //   1162: getfield mSolverVariable : Landroid/support/constraint/solver/SolverVariable;
    //   1165: astore #4
    //   1167: goto -> 1173
    //   1170: aconst_null
    //   1171: astore #4
    //   1173: aload #16
    //   1175: aload #24
    //   1177: if_acmpne -> 1203
    //   1180: aload #16
    //   1182: getfield mListAnchors : [Landroid/support/constraint/solver/widgets/ConstraintAnchor;
    //   1185: iload_3
    //   1186: aaload
    //   1187: astore #15
    //   1189: aload #16
    //   1191: getfield mListAnchors : [Landroid/support/constraint/solver/widgets/ConstraintAnchor;
    //   1194: iload_3
    //   1195: iconst_1
    //   1196: iadd
    //   1197: aaload
    //   1198: astore #17
    //   1200: goto -> 1203
    //   1203: aload_0
    //   1204: ifnull -> 1272
    //   1207: aload #4
    //   1209: ifnull -> 1272
    //   1212: iload_2
    //   1213: ifne -> 1226
    //   1216: aload #22
    //   1218: getfield mHorizontalBiasPercent : F
    //   1221: fstore #5
    //   1223: goto -> 1233
    //   1226: aload #22
    //   1228: getfield mVerticalBiasPercent : F
    //   1231: fstore #5
    //   1233: aload #15
    //   1235: invokevirtual getMargin : ()I
    //   1238: istore_2
    //   1239: aload #17
    //   1241: invokevirtual getMargin : ()I
    //   1244: istore #8
    //   1246: aload_1
    //   1247: aload #15
    //   1249: getfield mSolverVariable : Landroid/support/constraint/solver/SolverVariable;
    //   1252: aload_0
    //   1253: iload_2
    //   1254: fload #5
    //   1256: aload #4
    //   1258: aload #17
    //   1260: getfield mSolverVariable : Landroid/support/constraint/solver/SolverVariable;
    //   1263: iload #8
    //   1265: iconst_5
    //   1266: invokevirtual addCentering : (Landroid/support/constraint/solver/SolverVariable;Landroid/support/constraint/solver/SolverVariable;IFLandroid/support/constraint/solver/SolverVariable;Landroid/support/constraint/solver/SolverVariable;II)V
    //   1269: goto -> 1272
    //   1272: goto -> 2361
    //   1275: iload #11
    //   1277: ifeq -> 1780
    //   1280: aload #16
    //   1282: ifnull -> 1780
    //   1285: aload #4
    //   1287: getfield mWidgetsMatchCount : I
    //   1290: ifle -> 1312
    //   1293: aload #4
    //   1295: getfield mWidgetsCount : I
    //   1298: aload #4
    //   1300: getfield mWidgetsMatchCount : I
    //   1303: if_icmpne -> 1312
    //   1306: iconst_1
    //   1307: istore #12
    //   1309: goto -> 1315
    //   1312: iconst_0
    //   1313: istore #12
    //   1315: aload #16
    //   1317: astore #15
    //   1319: aload #16
    //   1321: astore_0
    //   1322: aload #15
    //   1324: ifnull -> 1777
    //   1327: aload #15
    //   1329: getfield mNextChainWidget : [Landroid/support/constraint/solver/widgets/ConstraintWidget;
    //   1332: iload_2
    //   1333: aaload
    //   1334: astore #4
    //   1336: aload #4
    //   1338: ifnull -> 1363
    //   1341: aload #4
    //   1343: invokevirtual getVisibility : ()I
    //   1346: bipush #8
    //   1348: if_icmpne -> 1363
    //   1351: aload #4
    //   1353: getfield mNextChainWidget : [Landroid/support/constraint/solver/widgets/ConstraintWidget;
    //   1356: iload_2
    //   1357: aaload
    //   1358: astore #4
    //   1360: goto -> 1336
    //   1363: aload #4
    //   1365: ifnonnull -> 1381
    //   1368: aload #15
    //   1370: aload #24
    //   1372: if_acmpne -> 1378
    //   1375: goto -> 1381
    //   1378: goto -> 1754
    //   1381: aload #15
    //   1383: getfield mListAnchors : [Landroid/support/constraint/solver/widgets/ConstraintAnchor;
    //   1386: iload_3
    //   1387: aaload
    //   1388: astore #20
    //   1390: aload #20
    //   1392: getfield mSolverVariable : Landroid/support/constraint/solver/SolverVariable;
    //   1395: astore #25
    //   1397: aload #20
    //   1399: getfield mTarget : Landroid/support/constraint/solver/widgets/ConstraintAnchor;
    //   1402: ifnull -> 1418
    //   1405: aload #20
    //   1407: getfield mTarget : Landroid/support/constraint/solver/widgets/ConstraintAnchor;
    //   1410: getfield mSolverVariable : Landroid/support/constraint/solver/SolverVariable;
    //   1413: astore #17
    //   1415: goto -> 1421
    //   1418: aconst_null
    //   1419: astore #17
    //   1421: aload_0
    //   1422: aload #15
    //   1424: if_acmpeq -> 1443
    //   1427: aload_0
    //   1428: getfield mListAnchors : [Landroid/support/constraint/solver/widgets/ConstraintAnchor;
    //   1431: iload_3
    //   1432: iconst_1
    //   1433: iadd
    //   1434: aaload
    //   1435: getfield mSolverVariable : Landroid/support/constraint/solver/SolverVariable;
    //   1438: astore #17
    //   1440: goto -> 1493
    //   1443: aload #15
    //   1445: aload #16
    //   1447: if_acmpne -> 1493
    //   1450: aload_0
    //   1451: aload #15
    //   1453: if_acmpne -> 1493
    //   1456: aload #19
    //   1458: getfield mListAnchors : [Landroid/support/constraint/solver/widgets/ConstraintAnchor;
    //   1461: iload_3
    //   1462: aaload
    //   1463: getfield mTarget : Landroid/support/constraint/solver/widgets/ConstraintAnchor;
    //   1466: ifnull -> 1487
    //   1469: aload #19
    //   1471: getfield mListAnchors : [Landroid/support/constraint/solver/widgets/ConstraintAnchor;
    //   1474: iload_3
    //   1475: aaload
    //   1476: getfield mTarget : Landroid/support/constraint/solver/widgets/ConstraintAnchor;
    //   1479: getfield mSolverVariable : Landroid/support/constraint/solver/SolverVariable;
    //   1482: astore #17
    //   1484: goto -> 1490
    //   1487: aconst_null
    //   1488: astore #17
    //   1490: goto -> 1493
    //   1493: aconst_null
    //   1494: astore #18
    //   1496: aload #20
    //   1498: invokevirtual getMargin : ()I
    //   1501: istore #13
    //   1503: aload #15
    //   1505: getfield mListAnchors : [Landroid/support/constraint/solver/widgets/ConstraintAnchor;
    //   1508: iload_3
    //   1509: iconst_1
    //   1510: iadd
    //   1511: aaload
    //   1512: invokevirtual getMargin : ()I
    //   1515: istore #9
    //   1517: aload #4
    //   1519: ifnull -> 1559
    //   1522: aload #4
    //   1524: getfield mListAnchors : [Landroid/support/constraint/solver/widgets/ConstraintAnchor;
    //   1527: iload_3
    //   1528: aaload
    //   1529: astore #18
    //   1531: aload #18
    //   1533: getfield mSolverVariable : Landroid/support/constraint/solver/SolverVariable;
    //   1536: astore #20
    //   1538: aload #15
    //   1540: getfield mListAnchors : [Landroid/support/constraint/solver/widgets/ConstraintAnchor;
    //   1543: iload_3
    //   1544: iconst_1
    //   1545: iadd
    //   1546: aaload
    //   1547: getfield mSolverVariable : Landroid/support/constraint/solver/SolverVariable;
    //   1550: astore #21
    //   1552: aload #20
    //   1554: astore #22
    //   1556: goto -> 1607
    //   1559: aload #23
    //   1561: getfield mListAnchors : [Landroid/support/constraint/solver/widgets/ConstraintAnchor;
    //   1564: iload_3
    //   1565: iconst_1
    //   1566: iadd
    //   1567: aaload
    //   1568: getfield mTarget : Landroid/support/constraint/solver/widgets/ConstraintAnchor;
    //   1571: astore #20
    //   1573: aload #20
    //   1575: ifnull -> 1585
    //   1578: aload #20
    //   1580: getfield mSolverVariable : Landroid/support/constraint/solver/SolverVariable;
    //   1583: astore #18
    //   1585: aload #15
    //   1587: getfield mListAnchors : [Landroid/support/constraint/solver/widgets/ConstraintAnchor;
    //   1590: iload_3
    //   1591: iconst_1
    //   1592: iadd
    //   1593: aaload
    //   1594: getfield mSolverVariable : Landroid/support/constraint/solver/SolverVariable;
    //   1597: astore #21
    //   1599: aload #18
    //   1601: astore #22
    //   1603: aload #20
    //   1605: astore #18
    //   1607: iload #9
    //   1609: istore #8
    //   1611: aload #18
    //   1613: ifnull -> 1626
    //   1616: iload #9
    //   1618: aload #18
    //   1620: invokevirtual getMargin : ()I
    //   1623: iadd
    //   1624: istore #8
    //   1626: iload #13
    //   1628: istore #9
    //   1630: aload_0
    //   1631: ifnull -> 1650
    //   1634: iload #13
    //   1636: aload_0
    //   1637: getfield mListAnchors : [Landroid/support/constraint/solver/widgets/ConstraintAnchor;
    //   1640: iload_3
    //   1641: iconst_1
    //   1642: iadd
    //   1643: aaload
    //   1644: invokevirtual getMargin : ()I
    //   1647: iadd
    //   1648: istore #9
    //   1650: aload #25
    //   1652: ifnull -> 1754
    //   1655: aload #17
    //   1657: ifnull -> 1754
    //   1660: aload #22
    //   1662: ifnull -> 1754
    //   1665: aload #21
    //   1667: ifnull -> 1754
    //   1670: aload #15
    //   1672: aload #16
    //   1674: if_acmpne -> 1692
    //   1677: aload #16
    //   1679: getfield mListAnchors : [Landroid/support/constraint/solver/widgets/ConstraintAnchor;
    //   1682: iload_3
    //   1683: aaload
    //   1684: invokevirtual getMargin : ()I
    //   1687: istore #9
    //   1689: goto -> 1692
    //   1692: aload #15
    //   1694: aload #24
    //   1696: if_acmpne -> 1716
    //   1699: aload #24
    //   1701: getfield mListAnchors : [Landroid/support/constraint/solver/widgets/ConstraintAnchor;
    //   1704: iload_3
    //   1705: iconst_1
    //   1706: iadd
    //   1707: aaload
    //   1708: invokevirtual getMargin : ()I
    //   1711: istore #8
    //   1713: goto -> 1716
    //   1716: iload #12
    //   1718: ifeq -> 1728
    //   1721: bipush #6
    //   1723: istore #13
    //   1725: goto -> 1731
    //   1728: iconst_4
    //   1729: istore #13
    //   1731: aload_1
    //   1732: aload #25
    //   1734: aload #17
    //   1736: iload #9
    //   1738: ldc 0.5
    //   1740: aload #22
    //   1742: aload #21
    //   1744: iload #8
    //   1746: iload #13
    //   1748: invokevirtual addCentering : (Landroid/support/constraint/solver/SolverVariable;Landroid/support/constraint/solver/SolverVariable;IFLandroid/support/constraint/solver/SolverVariable;Landroid/support/constraint/solver/SolverVariable;II)V
    //   1751: goto -> 1754
    //   1754: aload #15
    //   1756: invokevirtual getVisibility : ()I
    //   1759: bipush #8
    //   1761: if_icmpeq -> 1770
    //   1764: aload #15
    //   1766: astore_0
    //   1767: goto -> 1770
    //   1770: aload #4
    //   1772: astore #15
    //   1774: goto -> 1322
    //   1777: goto -> 2361
    //   1780: iload #10
    //   1782: ifeq -> 2361
    //   1785: aload #16
    //   1787: ifnull -> 2361
    //   1790: aload #4
    //   1792: getfield mWidgetsMatchCount : I
    //   1795: ifle -> 1817
    //   1798: aload #4
    //   1800: getfield mWidgetsCount : I
    //   1803: aload #4
    //   1805: getfield mWidgetsMatchCount : I
    //   1808: if_icmpne -> 1817
    //   1811: iconst_1
    //   1812: istore #8
    //   1814: goto -> 1820
    //   1817: iconst_0
    //   1818: istore #8
    //   1820: aload #16
    //   1822: astore #15
    //   1824: aload #16
    //   1826: astore #4
    //   1828: aload #15
    //   1830: ifnull -> 2199
    //   1833: aload #15
    //   1835: getfield mNextChainWidget : [Landroid/support/constraint/solver/widgets/ConstraintWidget;
    //   1838: iload_2
    //   1839: aaload
    //   1840: astore_0
    //   1841: aload_0
    //   1842: ifnull -> 1864
    //   1845: aload_0
    //   1846: invokevirtual getVisibility : ()I
    //   1849: bipush #8
    //   1851: if_icmpne -> 1864
    //   1854: aload_0
    //   1855: getfield mNextChainWidget : [Landroid/support/constraint/solver/widgets/ConstraintWidget;
    //   1858: iload_2
    //   1859: aaload
    //   1860: astore_0
    //   1861: goto -> 1841
    //   1864: aload #15
    //   1866: aload #16
    //   1868: if_acmpeq -> 2176
    //   1871: aload #15
    //   1873: aload #24
    //   1875: if_acmpeq -> 2176
    //   1878: aload_0
    //   1879: ifnull -> 2176
    //   1882: aload_0
    //   1883: aload #24
    //   1885: if_acmpne -> 1893
    //   1888: aconst_null
    //   1889: astore_0
    //   1890: goto -> 1893
    //   1893: aload #15
    //   1895: getfield mListAnchors : [Landroid/support/constraint/solver/widgets/ConstraintAnchor;
    //   1898: iload_3
    //   1899: aaload
    //   1900: astore #18
    //   1902: aload #18
    //   1904: getfield mSolverVariable : Landroid/support/constraint/solver/SolverVariable;
    //   1907: astore #22
    //   1909: aload #18
    //   1911: getfield mTarget : Landroid/support/constraint/solver/widgets/ConstraintAnchor;
    //   1914: ifnull -> 1930
    //   1917: aload #18
    //   1919: getfield mTarget : Landroid/support/constraint/solver/widgets/ConstraintAnchor;
    //   1922: getfield mSolverVariable : Landroid/support/constraint/solver/SolverVariable;
    //   1925: astore #17
    //   1927: goto -> 1930
    //   1930: aload #4
    //   1932: getfield mListAnchors : [Landroid/support/constraint/solver/widgets/ConstraintAnchor;
    //   1935: iload_3
    //   1936: iconst_1
    //   1937: iadd
    //   1938: aaload
    //   1939: getfield mSolverVariable : Landroid/support/constraint/solver/SolverVariable;
    //   1942: astore #25
    //   1944: aconst_null
    //   1945: astore #17
    //   1947: aload #18
    //   1949: invokevirtual getMargin : ()I
    //   1952: istore #13
    //   1954: aload #15
    //   1956: getfield mListAnchors : [Landroid/support/constraint/solver/widgets/ConstraintAnchor;
    //   1959: iload_3
    //   1960: iconst_1
    //   1961: iadd
    //   1962: aaload
    //   1963: invokevirtual getMargin : ()I
    //   1966: istore #12
    //   1968: aload_0
    //   1969: ifnull -> 2018
    //   1972: aload_0
    //   1973: getfield mListAnchors : [Landroid/support/constraint/solver/widgets/ConstraintAnchor;
    //   1976: iload_3
    //   1977: aaload
    //   1978: astore #20
    //   1980: aload #20
    //   1982: getfield mSolverVariable : Landroid/support/constraint/solver/SolverVariable;
    //   1985: astore #18
    //   1987: aload #20
    //   1989: getfield mTarget : Landroid/support/constraint/solver/widgets/ConstraintAnchor;
    //   1992: ifnull -> 2008
    //   1995: aload #20
    //   1997: getfield mTarget : Landroid/support/constraint/solver/widgets/ConstraintAnchor;
    //   2000: getfield mSolverVariable : Landroid/support/constraint/solver/SolverVariable;
    //   2003: astore #17
    //   2005: goto -> 2011
    //   2008: aconst_null
    //   2009: astore #17
    //   2011: aload #18
    //   2013: astore #21
    //   2015: goto -> 2070
    //   2018: aload #15
    //   2020: getfield mListAnchors : [Landroid/support/constraint/solver/widgets/ConstraintAnchor;
    //   2023: iload_3
    //   2024: iconst_1
    //   2025: iadd
    //   2026: aaload
    //   2027: getfield mTarget : Landroid/support/constraint/solver/widgets/ConstraintAnchor;
    //   2030: astore #18
    //   2032: aload #18
    //   2034: ifnull -> 2044
    //   2037: aload #18
    //   2039: getfield mSolverVariable : Landroid/support/constraint/solver/SolverVariable;
    //   2042: astore #17
    //   2044: aload #15
    //   2046: getfield mListAnchors : [Landroid/support/constraint/solver/widgets/ConstraintAnchor;
    //   2049: iload_3
    //   2050: iconst_1
    //   2051: iadd
    //   2052: aaload
    //   2053: getfield mSolverVariable : Landroid/support/constraint/solver/SolverVariable;
    //   2056: astore #20
    //   2058: aload #17
    //   2060: astore #21
    //   2062: aload #20
    //   2064: astore #17
    //   2066: aload #18
    //   2068: astore #20
    //   2070: iload #12
    //   2072: istore #9
    //   2074: aload #20
    //   2076: ifnull -> 2089
    //   2079: iload #12
    //   2081: aload #20
    //   2083: invokevirtual getMargin : ()I
    //   2086: iadd
    //   2087: istore #9
    //   2089: iload #13
    //   2091: istore #12
    //   2093: aload #4
    //   2095: ifnull -> 2115
    //   2098: iload #13
    //   2100: aload #4
    //   2102: getfield mListAnchors : [Landroid/support/constraint/solver/widgets/ConstraintAnchor;
    //   2105: iload_3
    //   2106: iconst_1
    //   2107: iadd
    //   2108: aaload
    //   2109: invokevirtual getMargin : ()I
    //   2112: iadd
    //   2113: istore #12
    //   2115: iload #8
    //   2117: ifeq -> 2127
    //   2120: bipush #6
    //   2122: istore #13
    //   2124: goto -> 2130
    //   2127: iconst_4
    //   2128: istore #13
    //   2130: aload #22
    //   2132: ifnull -> 2173
    //   2135: aload #25
    //   2137: ifnull -> 2173
    //   2140: aload #21
    //   2142: ifnull -> 2173
    //   2145: aload #17
    //   2147: ifnull -> 2173
    //   2150: aload_1
    //   2151: aload #22
    //   2153: aload #25
    //   2155: iload #12
    //   2157: ldc 0.5
    //   2159: aload #21
    //   2161: aload #17
    //   2163: iload #9
    //   2165: iload #13
    //   2167: invokevirtual addCentering : (Landroid/support/constraint/solver/SolverVariable;Landroid/support/constraint/solver/SolverVariable;IFLandroid/support/constraint/solver/SolverVariable;Landroid/support/constraint/solver/SolverVariable;II)V
    //   2170: goto -> 2173
    //   2173: goto -> 2176
    //   2176: aload #15
    //   2178: invokevirtual getVisibility : ()I
    //   2181: bipush #8
    //   2183: if_icmpeq -> 2193
    //   2186: aload #15
    //   2188: astore #4
    //   2190: goto -> 2193
    //   2193: aload_0
    //   2194: astore #15
    //   2196: goto -> 1828
    //   2199: aload #16
    //   2201: getfield mListAnchors : [Landroid/support/constraint/solver/widgets/ConstraintAnchor;
    //   2204: iload_3
    //   2205: aaload
    //   2206: astore #15
    //   2208: aload #19
    //   2210: getfield mListAnchors : [Landroid/support/constraint/solver/widgets/ConstraintAnchor;
    //   2213: iload_3
    //   2214: aaload
    //   2215: getfield mTarget : Landroid/support/constraint/solver/widgets/ConstraintAnchor;
    //   2218: astore #17
    //   2220: aload #24
    //   2222: getfield mListAnchors : [Landroid/support/constraint/solver/widgets/ConstraintAnchor;
    //   2225: iload_3
    //   2226: iconst_1
    //   2227: iadd
    //   2228: aaload
    //   2229: astore #4
    //   2231: aload #23
    //   2233: getfield mListAnchors : [Landroid/support/constraint/solver/widgets/ConstraintAnchor;
    //   2236: iload_3
    //   2237: iconst_1
    //   2238: iadd
    //   2239: aaload
    //   2240: getfield mTarget : Landroid/support/constraint/solver/widgets/ConstraintAnchor;
    //   2243: astore_0
    //   2244: aload #17
    //   2246: ifnull -> 2326
    //   2249: aload #16
    //   2251: aload #24
    //   2253: if_acmpeq -> 2280
    //   2256: aload_1
    //   2257: aload #15
    //   2259: getfield mSolverVariable : Landroid/support/constraint/solver/SolverVariable;
    //   2262: aload #17
    //   2264: getfield mSolverVariable : Landroid/support/constraint/solver/SolverVariable;
    //   2267: aload #15
    //   2269: invokevirtual getMargin : ()I
    //   2272: iconst_5
    //   2273: invokevirtual addEquality : (Landroid/support/constraint/solver/SolverVariable;Landroid/support/constraint/solver/SolverVariable;II)Landroid/support/constraint/solver/ArrayRow;
    //   2276: pop
    //   2277: goto -> 2326
    //   2280: aload_0
    //   2281: ifnull -> 2323
    //   2284: aload_1
    //   2285: aload #15
    //   2287: getfield mSolverVariable : Landroid/support/constraint/solver/SolverVariable;
    //   2290: aload #17
    //   2292: getfield mSolverVariable : Landroid/support/constraint/solver/SolverVariable;
    //   2295: aload #15
    //   2297: invokevirtual getMargin : ()I
    //   2300: ldc 0.5
    //   2302: aload #4
    //   2304: getfield mSolverVariable : Landroid/support/constraint/solver/SolverVariable;
    //   2307: aload_0
    //   2308: getfield mSolverVariable : Landroid/support/constraint/solver/SolverVariable;
    //   2311: aload #4
    //   2313: invokevirtual getMargin : ()I
    //   2316: iconst_5
    //   2317: invokevirtual addCentering : (Landroid/support/constraint/solver/SolverVariable;Landroid/support/constraint/solver/SolverVariable;IFLandroid/support/constraint/solver/SolverVariable;Landroid/support/constraint/solver/SolverVariable;II)V
    //   2320: goto -> 2326
    //   2323: goto -> 2326
    //   2326: aload_0
    //   2327: ifnull -> 2361
    //   2330: aload #16
    //   2332: aload #24
    //   2334: if_acmpeq -> 2361
    //   2337: aload_1
    //   2338: aload #4
    //   2340: getfield mSolverVariable : Landroid/support/constraint/solver/SolverVariable;
    //   2343: aload_0
    //   2344: getfield mSolverVariable : Landroid/support/constraint/solver/SolverVariable;
    //   2347: aload #4
    //   2349: invokevirtual getMargin : ()I
    //   2352: ineg
    //   2353: iconst_5
    //   2354: invokevirtual addEquality : (Landroid/support/constraint/solver/SolverVariable;Landroid/support/constraint/solver/SolverVariable;II)Landroid/support/constraint/solver/ArrayRow;
    //   2357: pop
    //   2358: goto -> 2361
    //   2361: iload #11
    //   2363: ifne -> 2371
    //   2366: iload #10
    //   2368: ifeq -> 2580
    //   2371: aload #16
    //   2373: ifnull -> 2580
    //   2376: aload #16
    //   2378: getfield mListAnchors : [Landroid/support/constraint/solver/widgets/ConstraintAnchor;
    //   2381: iload_3
    //   2382: aaload
    //   2383: astore #15
    //   2385: aload #24
    //   2387: getfield mListAnchors : [Landroid/support/constraint/solver/widgets/ConstraintAnchor;
    //   2390: iload_3
    //   2391: iconst_1
    //   2392: iadd
    //   2393: aaload
    //   2394: astore #17
    //   2396: aload #15
    //   2398: getfield mTarget : Landroid/support/constraint/solver/widgets/ConstraintAnchor;
    //   2401: ifnull -> 2417
    //   2404: aload #15
    //   2406: getfield mTarget : Landroid/support/constraint/solver/widgets/ConstraintAnchor;
    //   2409: getfield mSolverVariable : Landroid/support/constraint/solver/SolverVariable;
    //   2412: astore #4
    //   2414: goto -> 2420
    //   2417: aconst_null
    //   2418: astore #4
    //   2420: aload #17
    //   2422: getfield mTarget : Landroid/support/constraint/solver/widgets/ConstraintAnchor;
    //   2425: ifnull -> 2440
    //   2428: aload #17
    //   2430: getfield mTarget : Landroid/support/constraint/solver/widgets/ConstraintAnchor;
    //   2433: getfield mSolverVariable : Landroid/support/constraint/solver/SolverVariable;
    //   2436: astore_0
    //   2437: goto -> 2442
    //   2440: aconst_null
    //   2441: astore_0
    //   2442: aload #23
    //   2444: aload #24
    //   2446: if_acmpeq -> 2482
    //   2449: aload #23
    //   2451: getfield mListAnchors : [Landroid/support/constraint/solver/widgets/ConstraintAnchor;
    //   2454: iload_3
    //   2455: iconst_1
    //   2456: iadd
    //   2457: aaload
    //   2458: astore_0
    //   2459: aload_0
    //   2460: getfield mTarget : Landroid/support/constraint/solver/widgets/ConstraintAnchor;
    //   2463: ifnull -> 2477
    //   2466: aload_0
    //   2467: getfield mTarget : Landroid/support/constraint/solver/widgets/ConstraintAnchor;
    //   2470: getfield mSolverVariable : Landroid/support/constraint/solver/SolverVariable;
    //   2473: astore_0
    //   2474: goto -> 2479
    //   2477: aconst_null
    //   2478: astore_0
    //   2479: goto -> 2482
    //   2482: aload #16
    //   2484: aload #24
    //   2486: if_acmpne -> 2512
    //   2489: aload #16
    //   2491: getfield mListAnchors : [Landroid/support/constraint/solver/widgets/ConstraintAnchor;
    //   2494: iload_3
    //   2495: aaload
    //   2496: astore #15
    //   2498: aload #16
    //   2500: getfield mListAnchors : [Landroid/support/constraint/solver/widgets/ConstraintAnchor;
    //   2503: iload_3
    //   2504: iconst_1
    //   2505: iadd
    //   2506: aaload
    //   2507: astore #16
    //   2509: goto -> 2516
    //   2512: aload #17
    //   2514: astore #16
    //   2516: aload #4
    //   2518: ifnull -> 2580
    //   2521: aload_0
    //   2522: ifnull -> 2580
    //   2525: aload #15
    //   2527: invokevirtual getMargin : ()I
    //   2530: istore_2
    //   2531: aload #24
    //   2533: astore #17
    //   2535: aload #24
    //   2537: ifnonnull -> 2544
    //   2540: aload #23
    //   2542: astore #17
    //   2544: aload #17
    //   2546: getfield mListAnchors : [Landroid/support/constraint/solver/widgets/ConstraintAnchor;
    //   2549: iload_3
    //   2550: iconst_1
    //   2551: iadd
    //   2552: aaload
    //   2553: invokevirtual getMargin : ()I
    //   2556: istore_3
    //   2557: aload_1
    //   2558: aload #15
    //   2560: getfield mSolverVariable : Landroid/support/constraint/solver/SolverVariable;
    //   2563: aload #4
    //   2565: iload_2
    //   2566: ldc 0.5
    //   2568: aload_0
    //   2569: aload #16
    //   2571: getfield mSolverVariable : Landroid/support/constraint/solver/SolverVariable;
    //   2574: iload_3
    //   2575: iconst_5
    //   2576: invokevirtual addCentering : (Landroid/support/constraint/solver/SolverVariable;Landroid/support/constraint/solver/SolverVariable;IFLandroid/support/constraint/solver/SolverVariable;Landroid/support/constraint/solver/SolverVariable;II)V
    //   2579: return
    //   2580: return }
}


/* Location:              D:\Apks\New folder\classes-dex2jar.jar!\android\support\constraint\solver\widgets\Chain.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.0.7
 */