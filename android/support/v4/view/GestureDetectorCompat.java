package android.support.v4.view;

import android.content.Context;
import android.os.Build;
import android.os.Handler;
import android.os.Message;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.ViewConfiguration;

public final class GestureDetectorCompat {
  private final GestureDetectorCompatImpl mImpl;
  
  public GestureDetectorCompat(Context paramContext, GestureDetector.OnGestureListener paramOnGestureListener) { this(paramContext, paramOnGestureListener, null); }
  
  public GestureDetectorCompat(Context paramContext, GestureDetector.OnGestureListener paramOnGestureListener, Handler paramHandler) {
    if (Build.VERSION.SDK_INT > 17) {
      this.mImpl = new GestureDetectorCompatImplJellybeanMr2(paramContext, paramOnGestureListener, paramHandler);
      return;
    } 
    this.mImpl = new GestureDetectorCompatImplBase(paramContext, paramOnGestureListener, paramHandler);
  }
  
  public boolean isLongpressEnabled() { return this.mImpl.isLongpressEnabled(); }
  
  public boolean onTouchEvent(MotionEvent paramMotionEvent) { return this.mImpl.onTouchEvent(paramMotionEvent); }
  
  public void setIsLongpressEnabled(boolean paramBoolean) { this.mImpl.setIsLongpressEnabled(paramBoolean); }
  
  public void setOnDoubleTapListener(GestureDetector.OnDoubleTapListener paramOnDoubleTapListener) { this.mImpl.setOnDoubleTapListener(paramOnDoubleTapListener); }
  
  static interface GestureDetectorCompatImpl {
    boolean isLongpressEnabled();
    
    boolean onTouchEvent(MotionEvent param1MotionEvent);
    
    void setIsLongpressEnabled(boolean param1Boolean);
    
    void setOnDoubleTapListener(GestureDetector.OnDoubleTapListener param1OnDoubleTapListener);
  }
  
  static class GestureDetectorCompatImplBase implements GestureDetectorCompatImpl {
    private static final int DOUBLE_TAP_TIMEOUT;
    
    private static final int LONGPRESS_TIMEOUT = ViewConfiguration.getLongPressTimeout();
    
    private static final int LONG_PRESS = 2;
    
    private static final int SHOW_PRESS = 1;
    
    private static final int TAP = 3;
    
    private static final int TAP_TIMEOUT = ViewConfiguration.getTapTimeout();
    
    private boolean mAlwaysInBiggerTapRegion;
    
    private boolean mAlwaysInTapRegion;
    
    MotionEvent mCurrentDownEvent;
    
    boolean mDeferConfirmSingleTap;
    
    GestureDetector.OnDoubleTapListener mDoubleTapListener;
    
    private int mDoubleTapSlopSquare;
    
    private float mDownFocusX;
    
    private float mDownFocusY;
    
    private final Handler mHandler;
    
    private boolean mInLongPress;
    
    private boolean mIsDoubleTapping;
    
    private boolean mIsLongpressEnabled;
    
    private float mLastFocusX;
    
    private float mLastFocusY;
    
    final GestureDetector.OnGestureListener mListener;
    
    private int mMaximumFlingVelocity;
    
    private int mMinimumFlingVelocity;
    
    private MotionEvent mPreviousUpEvent;
    
    boolean mStillDown;
    
    private int mTouchSlopSquare;
    
    private VelocityTracker mVelocityTracker;
    
    static  {
      DOUBLE_TAP_TIMEOUT = ViewConfiguration.getDoubleTapTimeout();
    }
    
    GestureDetectorCompatImplBase(Context param1Context, GestureDetector.OnGestureListener param1OnGestureListener, Handler param1Handler) {
      if (param1Handler != null) {
        this.mHandler = new GestureHandler(param1Handler);
      } else {
        this.mHandler = new GestureHandler();
      } 
      this.mListener = param1OnGestureListener;
      if (param1OnGestureListener instanceof GestureDetector.OnDoubleTapListener)
        setOnDoubleTapListener((GestureDetector.OnDoubleTapListener)param1OnGestureListener); 
      init(param1Context);
    }
    
    private void cancel() {
      this.mHandler.removeMessages(1);
      this.mHandler.removeMessages(2);
      this.mHandler.removeMessages(3);
      this.mVelocityTracker.recycle();
      this.mVelocityTracker = null;
      this.mIsDoubleTapping = false;
      this.mStillDown = false;
      this.mAlwaysInTapRegion = false;
      this.mAlwaysInBiggerTapRegion = false;
      this.mDeferConfirmSingleTap = false;
      if (this.mInLongPress)
        this.mInLongPress = false; 
    }
    
    private void cancelTaps() {
      this.mHandler.removeMessages(1);
      this.mHandler.removeMessages(2);
      this.mHandler.removeMessages(3);
      this.mIsDoubleTapping = false;
      this.mAlwaysInTapRegion = false;
      this.mAlwaysInBiggerTapRegion = false;
      this.mDeferConfirmSingleTap = false;
      if (this.mInLongPress)
        this.mInLongPress = false; 
    }
    
    private void init(Context param1Context) {
      if (param1Context != null) {
        if (this.mListener != null) {
          this.mIsLongpressEnabled = true;
          ViewConfiguration viewConfiguration = ViewConfiguration.get(param1Context);
          int i = viewConfiguration.getScaledTouchSlop();
          int j = viewConfiguration.getScaledDoubleTapSlop();
          this.mMinimumFlingVelocity = viewConfiguration.getScaledMinimumFlingVelocity();
          this.mMaximumFlingVelocity = viewConfiguration.getScaledMaximumFlingVelocity();
          this.mTouchSlopSquare = i * i;
          this.mDoubleTapSlopSquare = j * j;
          return;
        } 
        throw new IllegalArgumentException("OnGestureListener must not be null");
      } 
      throw new IllegalArgumentException("Context must not be null");
    }
    
    private boolean isConsideredDoubleTap(MotionEvent param1MotionEvent1, MotionEvent param1MotionEvent2, MotionEvent param1MotionEvent3) {
      boolean bool2 = this.mAlwaysInBiggerTapRegion;
      boolean bool1 = false;
      if (!bool2)
        return false; 
      if (param1MotionEvent3.getEventTime() - param1MotionEvent2.getEventTime() > DOUBLE_TAP_TIMEOUT)
        return false; 
      int i = (int)param1MotionEvent1.getX() - (int)param1MotionEvent3.getX();
      int j = (int)param1MotionEvent1.getY() - (int)param1MotionEvent3.getY();
      if (i * i + j * j < this.mDoubleTapSlopSquare)
        bool1 = true; 
      return bool1;
    }
    
    void dispatchLongPress() {
      this.mHandler.removeMessages(3);
      this.mDeferConfirmSingleTap = false;
      this.mInLongPress = true;
      this.mListener.onLongPress(this.mCurrentDownEvent);
    }
    
    public boolean isLongpressEnabled() { return this.mIsLongpressEnabled; }
    
    public boolean onTouchEvent(MotionEvent param1MotionEvent) { // Byte code:
      //   0: aload_1
      //   1: invokevirtual getAction : ()I
      //   4: istore #9
      //   6: aload_0
      //   7: getfield mVelocityTracker : Landroid/view/VelocityTracker;
      //   10: ifnonnull -> 20
      //   13: aload_0
      //   14: invokestatic obtain : ()Landroid/view/VelocityTracker;
      //   17: putfield mVelocityTracker : Landroid/view/VelocityTracker;
      //   20: aload_0
      //   21: getfield mVelocityTracker : Landroid/view/VelocityTracker;
      //   24: aload_1
      //   25: invokevirtual addMovement : (Landroid/view/MotionEvent;)V
      //   28: iload #9
      //   30: sipush #255
      //   33: iand
      //   34: bipush #6
      //   36: if_icmpne -> 45
      //   39: iconst_1
      //   40: istore #6
      //   42: goto -> 48
      //   45: iconst_0
      //   46: istore #6
      //   48: iload #6
      //   50: ifeq -> 62
      //   53: aload_1
      //   54: invokevirtual getActionIndex : ()I
      //   57: istore #7
      //   59: goto -> 65
      //   62: iconst_m1
      //   63: istore #7
      //   65: fconst_0
      //   66: fstore_3
      //   67: fconst_0
      //   68: fstore_2
      //   69: aload_1
      //   70: invokevirtual getPointerCount : ()I
      //   73: istore #10
      //   75: iconst_0
      //   76: istore #8
      //   78: iload #8
      //   80: iload #10
      //   82: if_icmpge -> 122
      //   85: iload #7
      //   87: iload #8
      //   89: if_icmpne -> 95
      //   92: goto -> 113
      //   95: fload_3
      //   96: aload_1
      //   97: iload #8
      //   99: invokevirtual getX : (I)F
      //   102: fadd
      //   103: fstore_3
      //   104: fload_2
      //   105: aload_1
      //   106: iload #8
      //   108: invokevirtual getY : (I)F
      //   111: fadd
      //   112: fstore_2
      //   113: iload #8
      //   115: iconst_1
      //   116: iadd
      //   117: istore #8
      //   119: goto -> 78
      //   122: iload #6
      //   124: ifeq -> 136
      //   127: iload #10
      //   129: iconst_1
      //   130: isub
      //   131: istore #8
      //   133: goto -> 140
      //   136: iload #10
      //   138: istore #8
      //   140: fload_3
      //   141: iload #8
      //   143: i2f
      //   144: fdiv
      //   145: fstore_3
      //   146: fload_2
      //   147: iload #8
      //   149: i2f
      //   150: fdiv
      //   151: fstore_2
      //   152: iconst_0
      //   153: istore #13
      //   155: iconst_0
      //   156: istore #14
      //   158: iconst_0
      //   159: istore #8
      //   161: iconst_0
      //   162: istore #12
      //   164: iload #9
      //   166: sipush #255
      //   169: iand
      //   170: tableswitch default -> 212, 0 -> 900, 1 -> 621, 2 -> 392, 3 -> 386, 4 -> 212, 5 -> 360, 6 -> 214
      //   212: iconst_0
      //   213: ireturn
      //   214: aload_0
      //   215: fload_3
      //   216: putfield mLastFocusX : F
      //   219: aload_0
      //   220: fload_3
      //   221: putfield mDownFocusX : F
      //   224: aload_0
      //   225: fload_2
      //   226: putfield mLastFocusY : F
      //   229: aload_0
      //   230: fload_2
      //   231: putfield mDownFocusY : F
      //   234: aload_0
      //   235: getfield mVelocityTracker : Landroid/view/VelocityTracker;
      //   238: sipush #1000
      //   241: aload_0
      //   242: getfield mMaximumFlingVelocity : I
      //   245: i2f
      //   246: invokevirtual computeCurrentVelocity : (IF)V
      //   249: aload_1
      //   250: invokevirtual getActionIndex : ()I
      //   253: istore #8
      //   255: aload_1
      //   256: iload #8
      //   258: invokevirtual getPointerId : (I)I
      //   261: istore #9
      //   263: aload_0
      //   264: getfield mVelocityTracker : Landroid/view/VelocityTracker;
      //   267: iload #9
      //   269: invokevirtual getXVelocity : (I)F
      //   272: fstore_2
      //   273: aload_0
      //   274: getfield mVelocityTracker : Landroid/view/VelocityTracker;
      //   277: iload #9
      //   279: invokevirtual getYVelocity : (I)F
      //   282: fstore_3
      //   283: iconst_0
      //   284: istore #9
      //   286: iload #9
      //   288: iload #10
      //   290: if_icmpge -> 358
      //   293: iload #9
      //   295: iload #8
      //   297: if_icmpne -> 303
      //   300: goto -> 349
      //   303: aload_1
      //   304: iload #9
      //   306: invokevirtual getPointerId : (I)I
      //   309: istore #11
      //   311: aload_0
      //   312: getfield mVelocityTracker : Landroid/view/VelocityTracker;
      //   315: iload #11
      //   317: invokevirtual getXVelocity : (I)F
      //   320: fload_2
      //   321: fmul
      //   322: aload_0
      //   323: getfield mVelocityTracker : Landroid/view/VelocityTracker;
      //   326: iload #11
      //   328: invokevirtual getYVelocity : (I)F
      //   331: fload_3
      //   332: fmul
      //   333: fadd
      //   334: fconst_0
      //   335: fcmpg
      //   336: ifge -> 349
      //   339: aload_0
      //   340: getfield mVelocityTracker : Landroid/view/VelocityTracker;
      //   343: invokevirtual clear : ()V
      //   346: goto -> 358
      //   349: iload #9
      //   351: iconst_1
      //   352: iadd
      //   353: istore #9
      //   355: goto -> 286
      //   358: iconst_0
      //   359: ireturn
      //   360: aload_0
      //   361: fload_3
      //   362: putfield mLastFocusX : F
      //   365: aload_0
      //   366: fload_3
      //   367: putfield mDownFocusX : F
      //   370: aload_0
      //   371: fload_2
      //   372: putfield mLastFocusY : F
      //   375: aload_0
      //   376: fload_2
      //   377: putfield mDownFocusY : F
      //   380: aload_0
      //   381: invokespecial cancelTaps : ()V
      //   384: iconst_0
      //   385: ireturn
      //   386: aload_0
      //   387: invokespecial cancel : ()V
      //   390: iconst_0
      //   391: ireturn
      //   392: aload_0
      //   393: getfield mInLongPress : Z
      //   396: ifeq -> 401
      //   399: iconst_0
      //   400: ireturn
      //   401: aload_0
      //   402: getfield mLastFocusX : F
      //   405: fload_3
      //   406: fsub
      //   407: fstore #4
      //   409: aload_0
      //   410: getfield mLastFocusY : F
      //   413: fload_2
      //   414: fsub
      //   415: fstore #5
      //   417: aload_0
      //   418: getfield mIsDoubleTapping : Z
      //   421: ifeq -> 437
      //   424: iconst_0
      //   425: aload_0
      //   426: getfield mDoubleTapListener : Landroid/view/GestureDetector$OnDoubleTapListener;
      //   429: aload_1
      //   430: invokeinterface onDoubleTapEvent : (Landroid/view/MotionEvent;)Z
      //   435: ior
      //   436: ireturn
      //   437: aload_0
      //   438: getfield mAlwaysInTapRegion : Z
      //   441: ifeq -> 568
      //   444: fload_3
      //   445: aload_0
      //   446: getfield mDownFocusX : F
      //   449: fsub
      //   450: f2i
      //   451: istore #6
      //   453: fload_2
      //   454: aload_0
      //   455: getfield mDownFocusY : F
      //   458: fsub
      //   459: f2i
      //   460: istore #7
      //   462: iload #6
      //   464: iload #6
      //   466: imul
      //   467: iload #7
      //   469: iload #7
      //   471: imul
      //   472: iadd
      //   473: istore #6
      //   475: iload #6
      //   477: aload_0
      //   478: getfield mTouchSlopSquare : I
      //   481: if_icmple -> 543
      //   484: aload_0
      //   485: getfield mListener : Landroid/view/GestureDetector$OnGestureListener;
      //   488: aload_0
      //   489: getfield mCurrentDownEvent : Landroid/view/MotionEvent;
      //   492: aload_1
      //   493: fload #4
      //   495: fload #5
      //   497: invokeinterface onScroll : (Landroid/view/MotionEvent;Landroid/view/MotionEvent;FF)Z
      //   502: istore #12
      //   504: aload_0
      //   505: fload_3
      //   506: putfield mLastFocusX : F
      //   509: aload_0
      //   510: fload_2
      //   511: putfield mLastFocusY : F
      //   514: aload_0
      //   515: iconst_0
      //   516: putfield mAlwaysInTapRegion : Z
      //   519: aload_0
      //   520: getfield mHandler : Landroid/os/Handler;
      //   523: iconst_3
      //   524: invokevirtual removeMessages : (I)V
      //   527: aload_0
      //   528: getfield mHandler : Landroid/os/Handler;
      //   531: iconst_1
      //   532: invokevirtual removeMessages : (I)V
      //   535: aload_0
      //   536: getfield mHandler : Landroid/os/Handler;
      //   539: iconst_2
      //   540: invokevirtual removeMessages : (I)V
      //   543: iload #12
      //   545: istore #13
      //   547: iload #6
      //   549: aload_0
      //   550: getfield mTouchSlopSquare : I
      //   553: if_icmple -> 565
      //   556: aload_0
      //   557: iconst_0
      //   558: putfield mAlwaysInBiggerTapRegion : Z
      //   561: iload #12
      //   563: istore #13
      //   565: iload #13
      //   567: ireturn
      //   568: fload #4
      //   570: invokestatic abs : (F)F
      //   573: fconst_1
      //   574: fcmpl
      //   575: ifge -> 588
      //   578: fload #5
      //   580: invokestatic abs : (F)F
      //   583: fconst_1
      //   584: fcmpl
      //   585: iflt -> 565
      //   588: aload_0
      //   589: getfield mListener : Landroid/view/GestureDetector$OnGestureListener;
      //   592: aload_0
      //   593: getfield mCurrentDownEvent : Landroid/view/MotionEvent;
      //   596: aload_1
      //   597: fload #4
      //   599: fload #5
      //   601: invokeinterface onScroll : (Landroid/view/MotionEvent;Landroid/view/MotionEvent;FF)Z
      //   606: istore #12
      //   608: aload_0
      //   609: fload_3
      //   610: putfield mLastFocusX : F
      //   613: aload_0
      //   614: fload_2
      //   615: putfield mLastFocusY : F
      //   618: iload #12
      //   620: ireturn
      //   621: aload_0
      //   622: iconst_0
      //   623: putfield mStillDown : Z
      //   626: aload_1
      //   627: invokestatic obtain : (Landroid/view/MotionEvent;)Landroid/view/MotionEvent;
      //   630: astore #15
      //   632: aload_0
      //   633: getfield mIsDoubleTapping : Z
      //   636: ifeq -> 656
      //   639: iconst_0
      //   640: aload_0
      //   641: getfield mDoubleTapListener : Landroid/view/GestureDetector$OnDoubleTapListener;
      //   644: aload_1
      //   645: invokeinterface onDoubleTapEvent : (Landroid/view/MotionEvent;)Z
      //   650: ior
      //   651: istore #12
      //   653: goto -> 834
      //   656: aload_0
      //   657: getfield mInLongPress : Z
      //   660: ifeq -> 683
      //   663: aload_0
      //   664: getfield mHandler : Landroid/os/Handler;
      //   667: iconst_3
      //   668: invokevirtual removeMessages : (I)V
      //   671: aload_0
      //   672: iconst_0
      //   673: putfield mInLongPress : Z
      //   676: iload #14
      //   678: istore #12
      //   680: goto -> 834
      //   683: aload_0
      //   684: getfield mAlwaysInTapRegion : Z
      //   687: ifeq -> 744
      //   690: aload_0
      //   691: getfield mListener : Landroid/view/GestureDetector$OnGestureListener;
      //   694: aload_1
      //   695: invokeinterface onSingleTapUp : (Landroid/view/MotionEvent;)Z
      //   700: istore #13
      //   702: iload #13
      //   704: istore #12
      //   706: aload_0
      //   707: getfield mDeferConfirmSingleTap : Z
      //   710: ifeq -> 834
      //   713: aload_0
      //   714: getfield mDoubleTapListener : Landroid/view/GestureDetector$OnDoubleTapListener;
      //   717: astore #16
      //   719: iload #13
      //   721: istore #12
      //   723: aload #16
      //   725: ifnull -> 834
      //   728: aload #16
      //   730: aload_1
      //   731: invokeinterface onSingleTapConfirmed : (Landroid/view/MotionEvent;)Z
      //   736: pop
      //   737: iload #13
      //   739: istore #12
      //   741: goto -> 834
      //   744: aload_0
      //   745: getfield mVelocityTracker : Landroid/view/VelocityTracker;
      //   748: astore #16
      //   750: aload_1
      //   751: iconst_0
      //   752: invokevirtual getPointerId : (I)I
      //   755: istore #6
      //   757: aload #16
      //   759: sipush #1000
      //   762: aload_0
      //   763: getfield mMaximumFlingVelocity : I
      //   766: i2f
      //   767: invokevirtual computeCurrentVelocity : (IF)V
      //   770: aload #16
      //   772: iload #6
      //   774: invokevirtual getYVelocity : (I)F
      //   777: fstore_2
      //   778: aload #16
      //   780: iload #6
      //   782: invokevirtual getXVelocity : (I)F
      //   785: fstore_3
      //   786: fload_2
      //   787: invokestatic abs : (F)F
      //   790: aload_0
      //   791: getfield mMinimumFlingVelocity : I
      //   794: i2f
      //   795: fcmpl
      //   796: ifgt -> 816
      //   799: iload #14
      //   801: istore #12
      //   803: fload_3
      //   804: invokestatic abs : (F)F
      //   807: aload_0
      //   808: getfield mMinimumFlingVelocity : I
      //   811: i2f
      //   812: fcmpl
      //   813: ifle -> 834
      //   816: aload_0
      //   817: getfield mListener : Landroid/view/GestureDetector$OnGestureListener;
      //   820: aload_0
      //   821: getfield mCurrentDownEvent : Landroid/view/MotionEvent;
      //   824: aload_1
      //   825: fload_3
      //   826: fload_2
      //   827: invokeinterface onFling : (Landroid/view/MotionEvent;Landroid/view/MotionEvent;FF)Z
      //   832: istore #12
      //   834: aload_0
      //   835: getfield mPreviousUpEvent : Landroid/view/MotionEvent;
      //   838: astore_1
      //   839: aload_1
      //   840: ifnull -> 847
      //   843: aload_1
      //   844: invokevirtual recycle : ()V
      //   847: aload_0
      //   848: aload #15
      //   850: putfield mPreviousUpEvent : Landroid/view/MotionEvent;
      //   853: aload_0
      //   854: getfield mVelocityTracker : Landroid/view/VelocityTracker;
      //   857: astore_1
      //   858: aload_1
      //   859: ifnull -> 871
      //   862: aload_1
      //   863: invokevirtual recycle : ()V
      //   866: aload_0
      //   867: aconst_null
      //   868: putfield mVelocityTracker : Landroid/view/VelocityTracker;
      //   871: aload_0
      //   872: iconst_0
      //   873: putfield mIsDoubleTapping : Z
      //   876: aload_0
      //   877: iconst_0
      //   878: putfield mDeferConfirmSingleTap : Z
      //   881: aload_0
      //   882: getfield mHandler : Landroid/os/Handler;
      //   885: iconst_1
      //   886: invokevirtual removeMessages : (I)V
      //   889: aload_0
      //   890: getfield mHandler : Landroid/os/Handler;
      //   893: iconst_2
      //   894: invokevirtual removeMessages : (I)V
      //   897: iload #12
      //   899: ireturn
      //   900: iload #8
      //   902: istore #6
      //   904: aload_0
      //   905: getfield mDoubleTapListener : Landroid/view/GestureDetector$OnDoubleTapListener;
      //   908: ifnull -> 1026
      //   911: aload_0
      //   912: getfield mHandler : Landroid/os/Handler;
      //   915: iconst_3
      //   916: invokevirtual hasMessages : (I)Z
      //   919: istore #12
      //   921: iload #12
      //   923: ifeq -> 934
      //   926: aload_0
      //   927: getfield mHandler : Landroid/os/Handler;
      //   930: iconst_3
      //   931: invokevirtual removeMessages : (I)V
      //   934: aload_0
      //   935: getfield mCurrentDownEvent : Landroid/view/MotionEvent;
      //   938: astore #15
      //   940: aload #15
      //   942: ifnull -> 1009
      //   945: aload_0
      //   946: getfield mPreviousUpEvent : Landroid/view/MotionEvent;
      //   949: astore #16
      //   951: aload #16
      //   953: ifnull -> 1009
      //   956: iload #12
      //   958: ifeq -> 1009
      //   961: aload_0
      //   962: aload #15
      //   964: aload #16
      //   966: aload_1
      //   967: invokespecial isConsideredDoubleTap : (Landroid/view/MotionEvent;Landroid/view/MotionEvent;Landroid/view/MotionEvent;)Z
      //   970: ifeq -> 1009
      //   973: aload_0
      //   974: iconst_1
      //   975: putfield mIsDoubleTapping : Z
      //   978: aload_0
      //   979: getfield mDoubleTapListener : Landroid/view/GestureDetector$OnDoubleTapListener;
      //   982: aload_0
      //   983: getfield mCurrentDownEvent : Landroid/view/MotionEvent;
      //   986: invokeinterface onDoubleTap : (Landroid/view/MotionEvent;)Z
      //   991: iconst_0
      //   992: ior
      //   993: aload_0
      //   994: getfield mDoubleTapListener : Landroid/view/GestureDetector$OnDoubleTapListener;
      //   997: aload_1
      //   998: invokeinterface onDoubleTapEvent : (Landroid/view/MotionEvent;)Z
      //   1003: ior
      //   1004: istore #6
      //   1006: goto -> 1026
      //   1009: aload_0
      //   1010: getfield mHandler : Landroid/os/Handler;
      //   1013: iconst_3
      //   1014: getstatic android/support/v4/view/GestureDetectorCompat$GestureDetectorCompatImplBase.DOUBLE_TAP_TIMEOUT : I
      //   1017: i2l
      //   1018: invokevirtual sendEmptyMessageDelayed : (IJ)Z
      //   1021: pop
      //   1022: iload #8
      //   1024: istore #6
      //   1026: aload_0
      //   1027: fload_3
      //   1028: putfield mLastFocusX : F
      //   1031: aload_0
      //   1032: fload_3
      //   1033: putfield mDownFocusX : F
      //   1036: aload_0
      //   1037: fload_2
      //   1038: putfield mLastFocusY : F
      //   1041: aload_0
      //   1042: fload_2
      //   1043: putfield mDownFocusY : F
      //   1046: aload_0
      //   1047: getfield mCurrentDownEvent : Landroid/view/MotionEvent;
      //   1050: astore #15
      //   1052: aload #15
      //   1054: ifnull -> 1062
      //   1057: aload #15
      //   1059: invokevirtual recycle : ()V
      //   1062: aload_0
      //   1063: aload_1
      //   1064: invokestatic obtain : (Landroid/view/MotionEvent;)Landroid/view/MotionEvent;
      //   1067: putfield mCurrentDownEvent : Landroid/view/MotionEvent;
      //   1070: aload_0
      //   1071: iconst_1
      //   1072: putfield mAlwaysInTapRegion : Z
      //   1075: aload_0
      //   1076: iconst_1
      //   1077: putfield mAlwaysInBiggerTapRegion : Z
      //   1080: aload_0
      //   1081: iconst_1
      //   1082: putfield mStillDown : Z
      //   1085: aload_0
      //   1086: iconst_0
      //   1087: putfield mInLongPress : Z
      //   1090: aload_0
      //   1091: iconst_0
      //   1092: putfield mDeferConfirmSingleTap : Z
      //   1095: aload_0
      //   1096: getfield mIsLongpressEnabled : Z
      //   1099: ifeq -> 1136
      //   1102: aload_0
      //   1103: getfield mHandler : Landroid/os/Handler;
      //   1106: iconst_2
      //   1107: invokevirtual removeMessages : (I)V
      //   1110: aload_0
      //   1111: getfield mHandler : Landroid/os/Handler;
      //   1114: iconst_2
      //   1115: aload_0
      //   1116: getfield mCurrentDownEvent : Landroid/view/MotionEvent;
      //   1119: invokevirtual getDownTime : ()J
      //   1122: getstatic android/support/v4/view/GestureDetectorCompat$GestureDetectorCompatImplBase.TAP_TIMEOUT : I
      //   1125: i2l
      //   1126: ladd
      //   1127: getstatic android/support/v4/view/GestureDetectorCompat$GestureDetectorCompatImplBase.LONGPRESS_TIMEOUT : I
      //   1130: i2l
      //   1131: ladd
      //   1132: invokevirtual sendEmptyMessageAtTime : (IJ)Z
      //   1135: pop
      //   1136: aload_0
      //   1137: getfield mHandler : Landroid/os/Handler;
      //   1140: iconst_1
      //   1141: aload_0
      //   1142: getfield mCurrentDownEvent : Landroid/view/MotionEvent;
      //   1145: invokevirtual getDownTime : ()J
      //   1148: getstatic android/support/v4/view/GestureDetectorCompat$GestureDetectorCompatImplBase.TAP_TIMEOUT : I
      //   1151: i2l
      //   1152: ladd
      //   1153: invokevirtual sendEmptyMessageAtTime : (IJ)Z
      //   1156: pop
      //   1157: iload #6
      //   1159: aload_0
      //   1160: getfield mListener : Landroid/view/GestureDetector$OnGestureListener;
      //   1163: aload_1
      //   1164: invokeinterface onDown : (Landroid/view/MotionEvent;)Z
      //   1169: ior
      //   1170: ireturn }
    
    public void setIsLongpressEnabled(boolean param1Boolean) { this.mIsLongpressEnabled = param1Boolean; }
    
    public void setOnDoubleTapListener(GestureDetector.OnDoubleTapListener param1OnDoubleTapListener) { this.mDoubleTapListener = param1OnDoubleTapListener; }
    
    private class GestureHandler extends Handler {
      GestureHandler() {}
      
      GestureHandler(Handler param2Handler) { super(param2Handler.getLooper()); }
      
      public void handleMessage(Message param2Message) {
        StringBuilder stringBuilder;
        switch (param2Message.what) {
          default:
            stringBuilder = new StringBuilder();
            stringBuilder.append("Unknown message ");
            stringBuilder.append(param2Message);
            throw new RuntimeException(stringBuilder.toString());
          case 3:
            if (GestureDetectorCompat.GestureDetectorCompatImplBase.this.mDoubleTapListener != null) {
              if (!GestureDetectorCompat.GestureDetectorCompatImplBase.this.mStillDown) {
                GestureDetectorCompat.GestureDetectorCompatImplBase.this.mDoubleTapListener.onSingleTapConfirmed(GestureDetectorCompat.GestureDetectorCompatImplBase.this.mCurrentDownEvent);
                return;
              } 
              GestureDetectorCompat.GestureDetectorCompatImplBase.this.mDeferConfirmSingleTap = true;
              return;
            } 
            return;
          case 2:
            GestureDetectorCompat.GestureDetectorCompatImplBase.this.dispatchLongPress();
            return;
          case 1:
            break;
        } 
        GestureDetectorCompat.GestureDetectorCompatImplBase.this.mListener.onShowPress(GestureDetectorCompat.GestureDetectorCompatImplBase.this.mCurrentDownEvent);
      }
    }
  }
  
  private class GestureHandler extends Handler {
    GestureHandler() {}
    
    GestureHandler(Handler param1Handler) { super(param1Handler.getLooper()); }
    
    public void handleMessage(Message param1Message) {
      StringBuilder stringBuilder;
      switch (param1Message.what) {
        default:
          stringBuilder = new StringBuilder();
          stringBuilder.append("Unknown message ");
          stringBuilder.append(param1Message);
          throw new RuntimeException(stringBuilder.toString());
        case 3:
          if (this.this$0.mDoubleTapListener != null) {
            if (!this.this$0.mStillDown) {
              this.this$0.mDoubleTapListener.onSingleTapConfirmed(this.this$0.mCurrentDownEvent);
              return;
            } 
            this.this$0.mDeferConfirmSingleTap = true;
            return;
          } 
          return;
        case 2:
          this.this$0.dispatchLongPress();
          return;
        case 1:
          break;
      } 
      this.this$0.mListener.onShowPress(this.this$0.mCurrentDownEvent);
    }
  }
  
  static class GestureDetectorCompatImplJellybeanMr2 implements GestureDetectorCompatImpl {
    private final GestureDetector mDetector;
    
    GestureDetectorCompatImplJellybeanMr2(Context param1Context, GestureDetector.OnGestureListener param1OnGestureListener, Handler param1Handler) { this.mDetector = new GestureDetector(param1Context, param1OnGestureListener, param1Handler); }
    
    public boolean isLongpressEnabled() { return this.mDetector.isLongpressEnabled(); }
    
    public boolean onTouchEvent(MotionEvent param1MotionEvent) { return this.mDetector.onTouchEvent(param1MotionEvent); }
    
    public void setIsLongpressEnabled(boolean param1Boolean) { this.mDetector.setIsLongpressEnabled(param1Boolean); }
    
    public void setOnDoubleTapListener(GestureDetector.OnDoubleTapListener param1OnDoubleTapListener) { this.mDetector.setOnDoubleTapListener(param1OnDoubleTapListener); }
  }
}


/* Location:              D:\Apks\New folder\classes-dex2jar.jar!\android\support\v4\view\GestureDetectorCompat.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.0.7
 */