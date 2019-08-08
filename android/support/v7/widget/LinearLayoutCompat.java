package android.support.v7.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.support.annotation.RestrictTo;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewCompat;
import android.support.v7.appcompat.R;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.view.accessibility.AccessibilityEvent;
import android.view.accessibility.AccessibilityNodeInfo;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

public class LinearLayoutCompat extends ViewGroup {
  public static final int HORIZONTAL = 0;
  
  private static final int INDEX_BOTTOM = 2;
  
  private static final int INDEX_CENTER_VERTICAL = 0;
  
  private static final int INDEX_FILL = 3;
  
  private static final int INDEX_TOP = 1;
  
  public static final int SHOW_DIVIDER_BEGINNING = 1;
  
  public static final int SHOW_DIVIDER_END = 4;
  
  public static final int SHOW_DIVIDER_MIDDLE = 2;
  
  public static final int SHOW_DIVIDER_NONE = 0;
  
  public static final int VERTICAL = 1;
  
  private static final int VERTICAL_GRAVITY_COUNT = 4;
  
  private boolean mBaselineAligned = true;
  
  private int mBaselineAlignedChildIndex = -1;
  
  private int mBaselineChildTop = 0;
  
  private Drawable mDivider;
  
  private int mDividerHeight;
  
  private int mDividerPadding;
  
  private int mDividerWidth;
  
  private int mGravity = 8388659;
  
  private int[] mMaxAscent;
  
  private int[] mMaxDescent;
  
  private int mOrientation;
  
  private int mShowDividers;
  
  private int mTotalLength;
  
  private boolean mUseLargestChild;
  
  private float mWeightSum;
  
  public LinearLayoutCompat(Context paramContext) { this(paramContext, null); }
  
  public LinearLayoutCompat(Context paramContext, AttributeSet paramAttributeSet) { this(paramContext, paramAttributeSet, 0); }
  
  public LinearLayoutCompat(Context paramContext, AttributeSet paramAttributeSet, int paramInt) {
    super(paramContext, paramAttributeSet, paramInt);
    TintTypedArray tintTypedArray = TintTypedArray.obtainStyledAttributes(paramContext, paramAttributeSet, R.styleable.LinearLayoutCompat, paramInt, 0);
    paramInt = tintTypedArray.getInt(R.styleable.LinearLayoutCompat_android_orientation, -1);
    if (paramInt >= 0)
      setOrientation(paramInt); 
    paramInt = tintTypedArray.getInt(R.styleable.LinearLayoutCompat_android_gravity, -1);
    if (paramInt >= 0)
      setGravity(paramInt); 
    boolean bool = tintTypedArray.getBoolean(R.styleable.LinearLayoutCompat_android_baselineAligned, true);
    if (!bool)
      setBaselineAligned(bool); 
    this.mWeightSum = tintTypedArray.getFloat(R.styleable.LinearLayoutCompat_android_weightSum, -1.0F);
    this.mBaselineAlignedChildIndex = tintTypedArray.getInt(R.styleable.LinearLayoutCompat_android_baselineAlignedChildIndex, -1);
    this.mUseLargestChild = tintTypedArray.getBoolean(R.styleable.LinearLayoutCompat_measureWithLargestChild, false);
    setDividerDrawable(tintTypedArray.getDrawable(R.styleable.LinearLayoutCompat_divider));
    this.mShowDividers = tintTypedArray.getInt(R.styleable.LinearLayoutCompat_showDividers, 0);
    this.mDividerPadding = tintTypedArray.getDimensionPixelSize(R.styleable.LinearLayoutCompat_dividerPadding, 0);
    tintTypedArray.recycle();
  }
  
  private void forceUniformHeight(int paramInt1, int paramInt2) {
    int i = View.MeasureSpec.makeMeasureSpec(getMeasuredHeight(), 1073741824);
    for (byte b = 0; b < paramInt1; b++) {
      View view = getVirtualChildAt(b);
      if (view.getVisibility() != 8) {
        LayoutParams layoutParams = (LayoutParams)view.getLayoutParams();
        if (layoutParams.height == -1) {
          int j = layoutParams.width;
          layoutParams.width = view.getMeasuredWidth();
          measureChildWithMargins(view, paramInt2, 0, i, 0);
          layoutParams.width = j;
        } 
      } 
    } 
  }
  
  private void forceUniformWidth(int paramInt1, int paramInt2) {
    int i = View.MeasureSpec.makeMeasureSpec(getMeasuredWidth(), 1073741824);
    for (byte b = 0; b < paramInt1; b++) {
      View view = getVirtualChildAt(b);
      if (view.getVisibility() != 8) {
        LayoutParams layoutParams = (LayoutParams)view.getLayoutParams();
        if (layoutParams.width == -1) {
          int j = layoutParams.height;
          layoutParams.height = view.getMeasuredHeight();
          measureChildWithMargins(view, i, 0, paramInt2, 0);
          layoutParams.height = j;
        } 
      } 
    } 
  }
  
  private void setChildFrame(View paramView, int paramInt1, int paramInt2, int paramInt3, int paramInt4) { paramView.layout(paramInt1, paramInt2, paramInt1 + paramInt3, paramInt2 + paramInt4); }
  
  protected boolean checkLayoutParams(ViewGroup.LayoutParams paramLayoutParams) { return paramLayoutParams instanceof LayoutParams; }
  
  void drawDividersHorizontal(Canvas paramCanvas) {
    int j = getVirtualChildCount();
    boolean bool = ViewUtils.isLayoutRtl(this);
    int i;
    for (i = 0; i < j; i++) {
      View view = getVirtualChildAt(i);
      if (view != null && view.getVisibility() != 8 && hasDividerBeforeChildAt(i)) {
        int k;
        LayoutParams layoutParams = (LayoutParams)view.getLayoutParams();
        if (bool) {
          k = view.getRight() + layoutParams.rightMargin;
        } else {
          k = view.getLeft() - layoutParams.leftMargin - this.mDividerWidth;
        } 
        drawVerticalDivider(paramCanvas, k);
      } 
    } 
    if (hasDividerBeforeChildAt(j)) {
      View view = getVirtualChildAt(j - 1);
      if (view == null) {
        if (bool) {
          i = getPaddingLeft();
        } else {
          i = getWidth() - getPaddingRight() - this.mDividerWidth;
        } 
      } else {
        LayoutParams layoutParams = (LayoutParams)view.getLayoutParams();
        if (bool) {
          i = view.getLeft() - layoutParams.leftMargin - this.mDividerWidth;
        } else {
          i = view.getRight() + layoutParams.rightMargin;
        } 
      } 
      drawVerticalDivider(paramCanvas, i);
    } 
  }
  
  void drawDividersVertical(Canvas paramCanvas) {
    int j = getVirtualChildCount();
    int i;
    for (i = 0; i < j; i++) {
      View view = getVirtualChildAt(i);
      if (view != null && view.getVisibility() != 8 && hasDividerBeforeChildAt(i)) {
        LayoutParams layoutParams = (LayoutParams)view.getLayoutParams();
        drawHorizontalDivider(paramCanvas, view.getTop() - layoutParams.topMargin - this.mDividerHeight);
      } 
    } 
    if (hasDividerBeforeChildAt(j)) {
      View view = getVirtualChildAt(j - 1);
      if (view == null) {
        i = getHeight() - getPaddingBottom() - this.mDividerHeight;
      } else {
        LayoutParams layoutParams = (LayoutParams)view.getLayoutParams();
        i = view.getBottom() + layoutParams.bottomMargin;
      } 
      drawHorizontalDivider(paramCanvas, i);
    } 
  }
  
  void drawHorizontalDivider(Canvas paramCanvas, int paramInt) {
    this.mDivider.setBounds(getPaddingLeft() + this.mDividerPadding, paramInt, getWidth() - getPaddingRight() - this.mDividerPadding, this.mDividerHeight + paramInt);
    this.mDivider.draw(paramCanvas);
  }
  
  void drawVerticalDivider(Canvas paramCanvas, int paramInt) {
    this.mDivider.setBounds(paramInt, getPaddingTop() + this.mDividerPadding, this.mDividerWidth + paramInt, getHeight() - getPaddingBottom() - this.mDividerPadding);
    this.mDivider.draw(paramCanvas);
  }
  
  protected LayoutParams generateDefaultLayoutParams() {
    int i = this.mOrientation;
    return (i == 0) ? new LayoutParams(-2, -2) : ((i == 1) ? new LayoutParams(-1, -2) : null);
  }
  
  public LayoutParams generateLayoutParams(AttributeSet paramAttributeSet) { return new LayoutParams(getContext(), paramAttributeSet); }
  
  protected LayoutParams generateLayoutParams(ViewGroup.LayoutParams paramLayoutParams) { return new LayoutParams(paramLayoutParams); }
  
  public int getBaseline() {
    if (this.mBaselineAlignedChildIndex < 0)
      return super.getBaseline(); 
    int i = getChildCount();
    int j = this.mBaselineAlignedChildIndex;
    if (i > j) {
      View view = getChildAt(j);
      int k = view.getBaseline();
      if (k == -1) {
        if (this.mBaselineAlignedChildIndex == 0)
          return -1; 
        throw new RuntimeException("mBaselineAlignedChildIndex of LinearLayout points to a View that doesn't know how to get its baseline.");
      } 
      j = this.mBaselineChildTop;
      i = j;
      if (this.mOrientation == 1) {
        int m = this.mGravity & 0x70;
        i = j;
        if (m != 48)
          if (m != 16) {
            if (m != 80) {
              i = j;
            } else {
              i = getBottom() - getTop() - getPaddingBottom() - this.mTotalLength;
            } 
          } else {
            i = j + (getBottom() - getTop() - getPaddingTop() - getPaddingBottom() - this.mTotalLength) / 2;
          }  
      } 
      return ((LayoutParams)view.getLayoutParams()).topMargin + i + k;
    } 
    throw new RuntimeException("mBaselineAlignedChildIndex of LinearLayout set to an index that is out of bounds.");
  }
  
  public int getBaselineAlignedChildIndex() { return this.mBaselineAlignedChildIndex; }
  
  int getChildrenSkipCount(View paramView, int paramInt) { return 0; }
  
  public Drawable getDividerDrawable() { return this.mDivider; }
  
  public int getDividerPadding() { return this.mDividerPadding; }
  
  @RestrictTo({RestrictTo.Scope.LIBRARY_GROUP})
  public int getDividerWidth() { return this.mDividerWidth; }
  
  public int getGravity() { return this.mGravity; }
  
  int getLocationOffset(View paramView) { return 0; }
  
  int getNextLocationOffset(View paramView) { return 0; }
  
  public int getOrientation() { return this.mOrientation; }
  
  public int getShowDividers() { return this.mShowDividers; }
  
  View getVirtualChildAt(int paramInt) { return getChildAt(paramInt); }
  
  int getVirtualChildCount() { return getChildCount(); }
  
  public float getWeightSum() { return this.mWeightSum; }
  
  @RestrictTo({RestrictTo.Scope.LIBRARY})
  protected boolean hasDividerBeforeChildAt(int paramInt) {
    boolean bool1 = false;
    boolean bool = false;
    if (paramInt == 0) {
      if ((this.mShowDividers & true) != 0)
        bool = true; 
      return bool;
    } 
    if (paramInt == getChildCount()) {
      bool = bool1;
      if ((this.mShowDividers & 0x4) != 0)
        bool = true; 
      return bool;
    } 
    if ((this.mShowDividers & 0x2) != 0) {
      while (--paramInt >= 0) {
        if (getChildAt(paramInt).getVisibility() != 8)
          return true; 
        paramInt--;
      } 
      return false;
    } 
    return false;
  }
  
  public boolean isBaselineAligned() { return this.mBaselineAligned; }
  
  public boolean isMeasureWithLargestChildEnabled() { return this.mUseLargestChild; }
  
  void layoutHorizontal(int paramInt1, int paramInt2, int paramInt3, int paramInt4) {
    int k;
    int j;
    boolean bool1 = ViewUtils.isLayoutRtl(this);
    int i1 = getPaddingTop();
    int i2 = paramInt4 - paramInt2;
    int i3 = getPaddingBottom();
    int i4 = getPaddingBottom();
    int n = getVirtualChildCount();
    int i5 = this.mGravity;
    boolean bool2 = this.mBaselineAligned;
    int[] arrayOfInt1 = this.mMaxAscent;
    int[] arrayOfInt2 = this.mMaxDescent;
    int m = ViewCompat.getLayoutDirection(this);
    paramInt2 = GravityCompat.getAbsoluteGravity(i5 & 0x800007, m);
    if (paramInt2 != 1) {
      if (paramInt2 != 5) {
        paramInt1 = getPaddingLeft();
      } else {
        paramInt1 = getPaddingLeft() + paramInt3 - paramInt1 - this.mTotalLength;
      } 
    } else {
      paramInt1 = getPaddingLeft() + (paramInt3 - paramInt1 - this.mTotalLength) / 2;
    } 
    if (bool1) {
      j = n - 1;
      k = -1;
    } else {
      j = 0;
      k = 1;
    } 
    paramInt2 = 0;
    int i = i2;
    paramInt3 = i1;
    paramInt4 = paramInt1;
    while (paramInt2 < n) {
      int i6 = j + k * paramInt2;
      View view = getVirtualChildAt(i6);
      if (view == null) {
        paramInt4 += measureNullChild(i6);
      } else if (view.getVisibility() != 8) {
        int i8 = view.getMeasuredWidth();
        int i9 = view.getMeasuredHeight();
        LayoutParams layoutParams = (LayoutParams)view.getLayoutParams();
        if (bool2 && layoutParams.height != -1) {
          paramInt1 = view.getBaseline();
        } else {
          paramInt1 = -1;
        } 
        int i7 = layoutParams.gravity;
        if (i7 < 0)
          i7 = i5 & 0x70; 
        i7 &= 0x70;
        if (i7 != 16) {
          if (i7 != 48) {
            if (i7 != 80) {
              paramInt1 = paramInt3;
            } else {
              i7 = i2 - i3 - i9 - layoutParams.bottomMargin;
              if (paramInt1 != -1) {
                int i10 = view.getMeasuredHeight();
                paramInt1 = i7 - arrayOfInt2[2] - i10 - paramInt1;
              } else {
                paramInt1 = i7;
              } 
            } 
          } else {
            i7 = layoutParams.topMargin + paramInt3;
            if (paramInt1 != -1) {
              paramInt1 = i7 + arrayOfInt1[1] - paramInt1;
            } else {
              paramInt1 = i7;
            } 
          } 
        } else {
          paramInt1 = (i2 - i1 - i4 - i9) / 2 + paramInt3 + layoutParams.topMargin - layoutParams.bottomMargin;
        } 
        i7 = paramInt4;
        if (hasDividerBeforeChildAt(i6))
          i7 = paramInt4 + this.mDividerWidth; 
        paramInt4 = i7 + layoutParams.leftMargin;
        setChildFrame(view, paramInt4 + getLocationOffset(view), paramInt1, i8, i9);
        paramInt1 = layoutParams.rightMargin;
        i7 = getNextLocationOffset(view);
        paramInt2 += getChildrenSkipCount(view, i6);
        paramInt4 += i8 + paramInt1 + i7;
      } 
      paramInt2++;
    } 
  }
  
  void layoutVertical(int paramInt1, int paramInt2, int paramInt3, int paramInt4) {
    int i = getPaddingLeft();
    int j = paramInt3 - paramInt1;
    int k = getPaddingRight();
    int m = getPaddingRight();
    int n = getVirtualChildCount();
    int i1 = this.mGravity;
    paramInt1 = i1 & 0x70;
    if (paramInt1 != 16) {
      if (paramInt1 != 80) {
        paramInt1 = getPaddingTop();
      } else {
        paramInt1 = getPaddingTop() + paramInt4 - paramInt2 - this.mTotalLength;
      } 
    } else {
      paramInt1 = getPaddingTop() + (paramInt4 - paramInt2 - this.mTotalLength) / 2;
    } 
    paramInt2 = 0;
    paramInt3 = i;
    while (true) {
      paramInt4 = paramInt3;
      if (paramInt2 < n) {
        View view = getVirtualChildAt(paramInt2);
        if (view == null) {
          paramInt1 += measureNullChild(paramInt2);
        } else if (view.getVisibility() != 8) {
          int i4 = view.getMeasuredWidth();
          int i3 = view.getMeasuredHeight();
          LayoutParams layoutParams = (LayoutParams)view.getLayoutParams();
          paramInt3 = layoutParams.gravity;
          if (paramInt3 < 0)
            paramInt3 = i1 & 0x800007; 
          paramInt3 = GravityCompat.getAbsoluteGravity(paramInt3, ViewCompat.getLayoutDirection(this)) & 0x7;
          if (paramInt3 != 1) {
            if (paramInt3 != 5) {
              paramInt3 = layoutParams.leftMargin + paramInt4;
            } else {
              paramInt3 = j - k - i4 - layoutParams.rightMargin;
            } 
          } else {
            paramInt3 = (j - i - m - i4) / 2 + paramInt4 + layoutParams.leftMargin - layoutParams.rightMargin;
          } 
          int i2 = paramInt1;
          if (hasDividerBeforeChildAt(paramInt2))
            i2 = paramInt1 + this.mDividerHeight; 
          paramInt1 = i2 + layoutParams.topMargin;
          setChildFrame(view, paramInt3, paramInt1 + getLocationOffset(view), i4, i3);
          paramInt3 = layoutParams.bottomMargin;
          i2 = getNextLocationOffset(view);
          paramInt2 += getChildrenSkipCount(view, paramInt2);
          paramInt1 += i3 + paramInt3 + i2;
        } 
        paramInt2++;
        paramInt3 = paramInt4;
        continue;
      } 
      break;
    } 
  }
  
  void measureChildBeforeLayout(View paramView, int paramInt1, int paramInt2, int paramInt3, int paramInt4, int paramInt5) { measureChildWithMargins(paramView, paramInt2, paramInt3, paramInt4, paramInt5); }
  
  void measureHorizontal(int paramInt1, int paramInt2) { // Byte code:
    //   0: aload_0
    //   1: iconst_0
    //   2: putfield mTotalLength : I
    //   5: aload_0
    //   6: invokevirtual getVirtualChildCount : ()I
    //   9: istore #16
    //   11: iload_1
    //   12: invokestatic getMode : (I)I
    //   15: istore #10
    //   17: iload_2
    //   18: invokestatic getMode : (I)I
    //   21: istore #21
    //   23: aload_0
    //   24: getfield mMaxAscent : [I
    //   27: ifnull -> 37
    //   30: aload_0
    //   31: getfield mMaxDescent : [I
    //   34: ifnonnull -> 51
    //   37: aload_0
    //   38: iconst_4
    //   39: newarray int
    //   41: putfield mMaxAscent : [I
    //   44: aload_0
    //   45: iconst_4
    //   46: newarray int
    //   48: putfield mMaxDescent : [I
    //   51: aload_0
    //   52: getfield mMaxAscent : [I
    //   55: astore #27
    //   57: aload_0
    //   58: getfield mMaxDescent : [I
    //   61: astore #28
    //   63: aload #27
    //   65: iconst_3
    //   66: iconst_m1
    //   67: iastore
    //   68: aload #27
    //   70: iconst_2
    //   71: iconst_m1
    //   72: iastore
    //   73: aload #27
    //   75: iconst_1
    //   76: iconst_m1
    //   77: iastore
    //   78: aload #27
    //   80: iconst_0
    //   81: iconst_m1
    //   82: iastore
    //   83: aload #28
    //   85: iconst_3
    //   86: iconst_m1
    //   87: iastore
    //   88: aload #28
    //   90: iconst_2
    //   91: iconst_m1
    //   92: iastore
    //   93: aload #28
    //   95: iconst_1
    //   96: iconst_m1
    //   97: iastore
    //   98: aload #28
    //   100: iconst_0
    //   101: iconst_m1
    //   102: iastore
    //   103: aload_0
    //   104: getfield mBaselineAligned : Z
    //   107: istore #23
    //   109: iconst_0
    //   110: istore #13
    //   112: aload_0
    //   113: getfield mUseLargestChild : Z
    //   116: istore #24
    //   118: iload #10
    //   120: ldc 1073741824
    //   122: if_icmpne -> 131
    //   125: iconst_1
    //   126: istore #15
    //   128: goto -> 134
    //   131: iconst_0
    //   132: istore #15
    //   134: iconst_0
    //   135: istore #7
    //   137: iconst_0
    //   138: istore #5
    //   140: iconst_0
    //   141: istore #12
    //   143: iconst_1
    //   144: istore #9
    //   146: iconst_0
    //   147: istore #14
    //   149: fconst_0
    //   150: fstore_3
    //   151: iconst_0
    //   152: istore #11
    //   154: iconst_0
    //   155: istore #6
    //   157: iconst_0
    //   158: istore #8
    //   160: iload #11
    //   162: iload #16
    //   164: if_icmpge -> 930
    //   167: aload_0
    //   168: iload #11
    //   170: invokevirtual getVirtualChildAt : (I)Landroid/view/View;
    //   173: astore #29
    //   175: aload #29
    //   177: ifnonnull -> 210
    //   180: aload_0
    //   181: aload_0
    //   182: getfield mTotalLength : I
    //   185: aload_0
    //   186: iload #11
    //   188: invokevirtual measureNullChild : (I)I
    //   191: iadd
    //   192: putfield mTotalLength : I
    //   195: iload #7
    //   197: istore #17
    //   199: iload #6
    //   201: istore #7
    //   203: iload #17
    //   205: istore #6
    //   207: goto -> 909
    //   210: aload #29
    //   212: invokevirtual getVisibility : ()I
    //   215: bipush #8
    //   217: if_icmpne -> 248
    //   220: iload #11
    //   222: aload_0
    //   223: aload #29
    //   225: iload #11
    //   227: invokevirtual getChildrenSkipCount : (Landroid/view/View;I)I
    //   230: iadd
    //   231: istore #11
    //   233: iload #7
    //   235: istore #17
    //   237: iload #6
    //   239: istore #7
    //   241: iload #17
    //   243: istore #6
    //   245: goto -> 909
    //   248: aload_0
    //   249: iload #11
    //   251: invokevirtual hasDividerBeforeChildAt : (I)Z
    //   254: ifeq -> 270
    //   257: aload_0
    //   258: aload_0
    //   259: getfield mTotalLength : I
    //   262: aload_0
    //   263: getfield mDividerWidth : I
    //   266: iadd
    //   267: putfield mTotalLength : I
    //   270: aload #29
    //   272: invokevirtual getLayoutParams : ()Landroid/view/ViewGroup$LayoutParams;
    //   275: checkcast android/support/v7/widget/LinearLayoutCompat$LayoutParams
    //   278: astore #25
    //   280: fload_3
    //   281: aload #25
    //   283: getfield weight : F
    //   286: fadd
    //   287: fstore_3
    //   288: iload #10
    //   290: ldc 1073741824
    //   292: if_icmpne -> 400
    //   295: aload #25
    //   297: getfield width : I
    //   300: ifne -> 400
    //   303: aload #25
    //   305: getfield weight : F
    //   308: fconst_0
    //   309: fcmpl
    //   310: ifle -> 400
    //   313: iload #15
    //   315: ifeq -> 341
    //   318: aload_0
    //   319: aload_0
    //   320: getfield mTotalLength : I
    //   323: aload #25
    //   325: getfield leftMargin : I
    //   328: aload #25
    //   330: getfield rightMargin : I
    //   333: iadd
    //   334: iadd
    //   335: putfield mTotalLength : I
    //   338: goto -> 370
    //   341: aload_0
    //   342: getfield mTotalLength : I
    //   345: istore #17
    //   347: aload_0
    //   348: iload #17
    //   350: aload #25
    //   352: getfield leftMargin : I
    //   355: iload #17
    //   357: iadd
    //   358: aload #25
    //   360: getfield rightMargin : I
    //   363: iadd
    //   364: invokestatic max : (II)I
    //   367: putfield mTotalLength : I
    //   370: iload #23
    //   372: ifeq -> 394
    //   375: iconst_0
    //   376: iconst_0
    //   377: invokestatic makeMeasureSpec : (II)I
    //   380: istore #17
    //   382: aload #29
    //   384: iload #17
    //   386: iload #17
    //   388: invokevirtual measure : (II)V
    //   391: goto -> 590
    //   394: iconst_1
    //   395: istore #13
    //   397: goto -> 590
    //   400: aload #25
    //   402: getfield width : I
    //   405: ifne -> 431
    //   408: aload #25
    //   410: getfield weight : F
    //   413: fconst_0
    //   414: fcmpl
    //   415: ifle -> 431
    //   418: aload #25
    //   420: bipush #-2
    //   422: putfield width : I
    //   425: iconst_0
    //   426: istore #17
    //   428: goto -> 436
    //   431: ldc_w -2147483648
    //   434: istore #17
    //   436: fload_3
    //   437: fconst_0
    //   438: fcmpl
    //   439: ifne -> 451
    //   442: aload_0
    //   443: getfield mTotalLength : I
    //   446: istore #18
    //   448: goto -> 454
    //   451: iconst_0
    //   452: istore #18
    //   454: aload_0
    //   455: aload #29
    //   457: iload #11
    //   459: iload_1
    //   460: iload #18
    //   462: iload_2
    //   463: iconst_0
    //   464: invokevirtual measureChildBeforeLayout : (Landroid/view/View;IIIII)V
    //   467: iload #17
    //   469: ldc_w -2147483648
    //   472: if_icmpeq -> 485
    //   475: aload #25
    //   477: iload #17
    //   479: putfield width : I
    //   482: goto -> 485
    //   485: aload #25
    //   487: astore #26
    //   489: aload #29
    //   491: invokevirtual getMeasuredWidth : ()I
    //   494: istore #17
    //   496: iload #15
    //   498: ifeq -> 534
    //   501: aload_0
    //   502: aload_0
    //   503: getfield mTotalLength : I
    //   506: aload #26
    //   508: getfield leftMargin : I
    //   511: iload #17
    //   513: iadd
    //   514: aload #26
    //   516: getfield rightMargin : I
    //   519: iadd
    //   520: aload_0
    //   521: aload #29
    //   523: invokevirtual getNextLocationOffset : (Landroid/view/View;)I
    //   526: iadd
    //   527: iadd
    //   528: putfield mTotalLength : I
    //   531: goto -> 573
    //   534: aload_0
    //   535: getfield mTotalLength : I
    //   538: istore #18
    //   540: aload_0
    //   541: iload #18
    //   543: iload #18
    //   545: iload #17
    //   547: iadd
    //   548: aload #26
    //   550: getfield leftMargin : I
    //   553: iadd
    //   554: aload #26
    //   556: getfield rightMargin : I
    //   559: iadd
    //   560: aload_0
    //   561: aload #29
    //   563: invokevirtual getNextLocationOffset : (Landroid/view/View;)I
    //   566: iadd
    //   567: invokestatic max : (II)I
    //   570: putfield mTotalLength : I
    //   573: iload #24
    //   575: ifeq -> 590
    //   578: iload #17
    //   580: iload #5
    //   582: invokestatic max : (II)I
    //   585: istore #5
    //   587: goto -> 590
    //   590: iload #8
    //   592: istore #17
    //   594: iload #11
    //   596: istore #19
    //   598: iconst_0
    //   599: istore #11
    //   601: iload #11
    //   603: istore #18
    //   605: iload #12
    //   607: istore #8
    //   609: iload #21
    //   611: ldc 1073741824
    //   613: if_icmpeq -> 639
    //   616: iload #11
    //   618: istore #18
    //   620: iload #12
    //   622: istore #8
    //   624: aload #25
    //   626: getfield height : I
    //   629: iconst_m1
    //   630: if_icmpne -> 639
    //   633: iconst_1
    //   634: istore #8
    //   636: iconst_1
    //   637: istore #18
    //   639: aload #25
    //   641: getfield topMargin : I
    //   644: aload #25
    //   646: getfield bottomMargin : I
    //   649: iadd
    //   650: istore #11
    //   652: aload #29
    //   654: invokevirtual getMeasuredHeight : ()I
    //   657: iload #11
    //   659: iadd
    //   660: istore #12
    //   662: iload #7
    //   664: aload #29
    //   666: invokevirtual getMeasuredState : ()I
    //   669: invokestatic combineMeasuredStates : (II)I
    //   672: istore #20
    //   674: iload #23
    //   676: ifeq -> 769
    //   679: aload #29
    //   681: invokevirtual getBaseline : ()I
    //   684: istore #22
    //   686: iload #22
    //   688: iconst_m1
    //   689: if_icmpeq -> 766
    //   692: aload #25
    //   694: getfield gravity : I
    //   697: ifge -> 709
    //   700: aload_0
    //   701: getfield mGravity : I
    //   704: istore #7
    //   706: goto -> 716
    //   709: aload #25
    //   711: getfield gravity : I
    //   714: istore #7
    //   716: iload #7
    //   718: bipush #112
    //   720: iand
    //   721: iconst_4
    //   722: ishr
    //   723: bipush #-2
    //   725: iand
    //   726: iconst_1
    //   727: ishr
    //   728: istore #7
    //   730: aload #27
    //   732: iload #7
    //   734: aload #27
    //   736: iload #7
    //   738: iaload
    //   739: iload #22
    //   741: invokestatic max : (II)I
    //   744: iastore
    //   745: aload #28
    //   747: iload #7
    //   749: aload #28
    //   751: iload #7
    //   753: iaload
    //   754: iload #12
    //   756: iload #22
    //   758: isub
    //   759: invokestatic max : (II)I
    //   762: iastore
    //   763: goto -> 769
    //   766: goto -> 769
    //   769: iload #14
    //   771: iload #12
    //   773: invokestatic max : (II)I
    //   776: istore #14
    //   778: iload #9
    //   780: ifeq -> 798
    //   783: aload #25
    //   785: getfield height : I
    //   788: iconst_m1
    //   789: if_icmpne -> 798
    //   792: iconst_1
    //   793: istore #7
    //   795: goto -> 801
    //   798: iconst_0
    //   799: istore #7
    //   801: aload #25
    //   803: getfield weight : F
    //   806: fconst_0
    //   807: fcmpl
    //   808: ifle -> 835
    //   811: iload #18
    //   813: ifeq -> 819
    //   816: goto -> 823
    //   819: iload #12
    //   821: istore #11
    //   823: iload #17
    //   825: iload #11
    //   827: invokestatic max : (II)I
    //   830: istore #9
    //   832: goto -> 860
    //   835: iload #18
    //   837: ifeq -> 843
    //   840: goto -> 847
    //   843: iload #12
    //   845: istore #11
    //   847: iload #6
    //   849: iload #11
    //   851: invokestatic max : (II)I
    //   854: istore #6
    //   856: iload #17
    //   858: istore #9
    //   860: aload_0
    //   861: aload #29
    //   863: iload #19
    //   865: invokevirtual getChildrenSkipCount : (Landroid/view/View;I)I
    //   868: istore #12
    //   870: iload #7
    //   872: istore #11
    //   874: iload #9
    //   876: istore #17
    //   878: iload #6
    //   880: istore #7
    //   882: iload #20
    //   884: istore #6
    //   886: iload #19
    //   888: iload #12
    //   890: iadd
    //   891: istore #18
    //   893: iload #8
    //   895: istore #12
    //   897: iload #11
    //   899: istore #9
    //   901: iload #18
    //   903: istore #11
    //   905: iload #17
    //   907: istore #8
    //   909: iload #11
    //   911: iconst_1
    //   912: iadd
    //   913: istore #11
    //   915: iload #6
    //   917: istore #17
    //   919: iload #7
    //   921: istore #6
    //   923: iload #17
    //   925: istore #7
    //   927: goto -> 160
    //   930: iload #8
    //   932: istore #17
    //   934: iload #14
    //   936: istore #8
    //   938: iload #5
    //   940: istore #14
    //   942: aload_0
    //   943: getfield mTotalLength : I
    //   946: ifle -> 971
    //   949: aload_0
    //   950: iload #16
    //   952: invokevirtual hasDividerBeforeChildAt : (I)Z
    //   955: ifeq -> 971
    //   958: aload_0
    //   959: aload_0
    //   960: getfield mTotalLength : I
    //   963: aload_0
    //   964: getfield mDividerWidth : I
    //   967: iadd
    //   968: putfield mTotalLength : I
    //   971: aload #27
    //   973: iconst_1
    //   974: iaload
    //   975: iconst_m1
    //   976: if_icmpne -> 1013
    //   979: aload #27
    //   981: iconst_0
    //   982: iaload
    //   983: iconst_m1
    //   984: if_icmpne -> 1013
    //   987: aload #27
    //   989: iconst_2
    //   990: iaload
    //   991: iconst_m1
    //   992: if_icmpne -> 1013
    //   995: aload #27
    //   997: iconst_3
    //   998: iaload
    //   999: iconst_m1
    //   1000: if_icmpeq -> 1006
    //   1003: goto -> 1013
    //   1006: iload #8
    //   1008: istore #5
    //   1010: goto -> 1071
    //   1013: iload #8
    //   1015: aload #27
    //   1017: iconst_3
    //   1018: iaload
    //   1019: aload #27
    //   1021: iconst_0
    //   1022: iaload
    //   1023: aload #27
    //   1025: iconst_1
    //   1026: iaload
    //   1027: aload #27
    //   1029: iconst_2
    //   1030: iaload
    //   1031: invokestatic max : (II)I
    //   1034: invokestatic max : (II)I
    //   1037: invokestatic max : (II)I
    //   1040: aload #28
    //   1042: iconst_3
    //   1043: iaload
    //   1044: aload #28
    //   1046: iconst_0
    //   1047: iaload
    //   1048: aload #28
    //   1050: iconst_1
    //   1051: iaload
    //   1052: aload #28
    //   1054: iconst_2
    //   1055: iaload
    //   1056: invokestatic max : (II)I
    //   1059: invokestatic max : (II)I
    //   1062: invokestatic max : (II)I
    //   1065: iadd
    //   1066: invokestatic max : (II)I
    //   1069: istore #5
    //   1071: iload #7
    //   1073: istore #8
    //   1075: iload #24
    //   1077: ifeq -> 1302
    //   1080: iload #10
    //   1082: istore #7
    //   1084: iload #7
    //   1086: ldc_w -2147483648
    //   1089: if_icmpeq -> 1103
    //   1092: iload #7
    //   1094: ifne -> 1100
    //   1097: goto -> 1103
    //   1100: goto -> 1302
    //   1103: aload_0
    //   1104: iconst_0
    //   1105: putfield mTotalLength : I
    //   1108: iconst_0
    //   1109: istore #7
    //   1111: iload #5
    //   1113: istore #11
    //   1115: iload #7
    //   1117: iload #16
    //   1119: if_icmpge -> 1295
    //   1122: aload_0
    //   1123: iload #7
    //   1125: invokevirtual getVirtualChildAt : (I)Landroid/view/View;
    //   1128: astore #25
    //   1130: aload #25
    //   1132: ifnonnull -> 1157
    //   1135: aload_0
    //   1136: aload_0
    //   1137: getfield mTotalLength : I
    //   1140: aload_0
    //   1141: iload #7
    //   1143: invokevirtual measureNullChild : (I)I
    //   1146: iadd
    //   1147: putfield mTotalLength : I
    //   1150: iload #7
    //   1152: istore #5
    //   1154: goto -> 1286
    //   1157: aload #25
    //   1159: invokevirtual getVisibility : ()I
    //   1162: bipush #8
    //   1164: if_icmpne -> 1183
    //   1167: iload #7
    //   1169: aload_0
    //   1170: aload #25
    //   1172: iload #7
    //   1174: invokevirtual getChildrenSkipCount : (Landroid/view/View;I)I
    //   1177: iadd
    //   1178: istore #5
    //   1180: goto -> 1286
    //   1183: aload #25
    //   1185: invokevirtual getLayoutParams : ()Landroid/view/ViewGroup$LayoutParams;
    //   1188: checkcast android/support/v7/widget/LinearLayoutCompat$LayoutParams
    //   1191: astore #26
    //   1193: iload #15
    //   1195: ifeq -> 1243
    //   1198: aload_0
    //   1199: getfield mTotalLength : I
    //   1202: istore #18
    //   1204: aload #26
    //   1206: getfield leftMargin : I
    //   1209: istore #19
    //   1211: iload #7
    //   1213: istore #5
    //   1215: aload_0
    //   1216: iload #18
    //   1218: iload #19
    //   1220: iload #14
    //   1222: iadd
    //   1223: aload #26
    //   1225: getfield rightMargin : I
    //   1228: iadd
    //   1229: aload_0
    //   1230: aload #25
    //   1232: invokevirtual getNextLocationOffset : (Landroid/view/View;)I
    //   1235: iadd
    //   1236: iadd
    //   1237: putfield mTotalLength : I
    //   1240: goto -> 1286
    //   1243: iload #7
    //   1245: istore #5
    //   1247: aload_0
    //   1248: getfield mTotalLength : I
    //   1251: istore #7
    //   1253: aload_0
    //   1254: iload #7
    //   1256: iload #7
    //   1258: iload #14
    //   1260: iadd
    //   1261: aload #26
    //   1263: getfield leftMargin : I
    //   1266: iadd
    //   1267: aload #26
    //   1269: getfield rightMargin : I
    //   1272: iadd
    //   1273: aload_0
    //   1274: aload #25
    //   1276: invokevirtual getNextLocationOffset : (Landroid/view/View;)I
    //   1279: iadd
    //   1280: invokestatic max : (II)I
    //   1283: putfield mTotalLength : I
    //   1286: iload #5
    //   1288: iconst_1
    //   1289: iadd
    //   1290: istore #7
    //   1292: goto -> 1115
    //   1295: iload #11
    //   1297: istore #5
    //   1299: goto -> 1302
    //   1302: iload #10
    //   1304: istore #11
    //   1306: aload_0
    //   1307: aload_0
    //   1308: getfield mTotalLength : I
    //   1311: aload_0
    //   1312: invokevirtual getPaddingLeft : ()I
    //   1315: aload_0
    //   1316: invokevirtual getPaddingRight : ()I
    //   1319: iadd
    //   1320: iadd
    //   1321: putfield mTotalLength : I
    //   1324: aload_0
    //   1325: getfield mTotalLength : I
    //   1328: aload_0
    //   1329: invokevirtual getSuggestedMinimumWidth : ()I
    //   1332: invokestatic max : (II)I
    //   1335: iload_1
    //   1336: iconst_0
    //   1337: invokestatic resolveSizeAndState : (III)I
    //   1340: istore #10
    //   1342: iload #10
    //   1344: ldc_w 16777215
    //   1347: iand
    //   1348: istore #7
    //   1350: iload #7
    //   1352: aload_0
    //   1353: getfield mTotalLength : I
    //   1356: isub
    //   1357: istore #19
    //   1359: iload #13
    //   1361: ifne -> 1502
    //   1364: iload #19
    //   1366: ifeq -> 1378
    //   1369: fload_3
    //   1370: fconst_0
    //   1371: fcmpl
    //   1372: ifle -> 1378
    //   1375: goto -> 1502
    //   1378: iload #6
    //   1380: iload #17
    //   1382: invokestatic max : (II)I
    //   1385: istore #6
    //   1387: iload #24
    //   1389: ifeq -> 1491
    //   1392: iload #11
    //   1394: ldc 1073741824
    //   1396: if_icmpeq -> 1491
    //   1399: iconst_0
    //   1400: istore #11
    //   1402: iload #11
    //   1404: iload #16
    //   1406: if_icmpge -> 1488
    //   1409: aload_0
    //   1410: iload #11
    //   1412: invokevirtual getVirtualChildAt : (I)Landroid/view/View;
    //   1415: astore #25
    //   1417: aload #25
    //   1419: ifnull -> 1479
    //   1422: aload #25
    //   1424: invokevirtual getVisibility : ()I
    //   1427: bipush #8
    //   1429: if_icmpne -> 1435
    //   1432: goto -> 1479
    //   1435: aload #25
    //   1437: invokevirtual getLayoutParams : ()Landroid/view/ViewGroup$LayoutParams;
    //   1440: checkcast android/support/v7/widget/LinearLayoutCompat$LayoutParams
    //   1443: getfield weight : F
    //   1446: fconst_0
    //   1447: fcmpl
    //   1448: ifle -> 1476
    //   1451: aload #25
    //   1453: iload #14
    //   1455: ldc 1073741824
    //   1457: invokestatic makeMeasureSpec : (II)I
    //   1460: aload #25
    //   1462: invokevirtual getMeasuredHeight : ()I
    //   1465: ldc 1073741824
    //   1467: invokestatic makeMeasureSpec : (II)I
    //   1470: invokevirtual measure : (II)V
    //   1473: goto -> 1479
    //   1476: goto -> 1479
    //   1479: iload #11
    //   1481: iconst_1
    //   1482: iadd
    //   1483: istore #11
    //   1485: goto -> 1402
    //   1488: goto -> 1491
    //   1491: iload #16
    //   1493: istore #11
    //   1495: iload #6
    //   1497: istore #7
    //   1499: goto -> 2264
    //   1502: aload_0
    //   1503: getfield mWeightSum : F
    //   1506: fstore #4
    //   1508: fload #4
    //   1510: fconst_0
    //   1511: fcmpl
    //   1512: ifle -> 1521
    //   1515: fload #4
    //   1517: fstore_3
    //   1518: goto -> 1521
    //   1521: aload #27
    //   1523: iconst_3
    //   1524: iconst_m1
    //   1525: iastore
    //   1526: aload #27
    //   1528: iconst_2
    //   1529: iconst_m1
    //   1530: iastore
    //   1531: aload #27
    //   1533: iconst_1
    //   1534: iconst_m1
    //   1535: iastore
    //   1536: aload #27
    //   1538: iconst_0
    //   1539: iconst_m1
    //   1540: iastore
    //   1541: aload #28
    //   1543: iconst_3
    //   1544: iconst_m1
    //   1545: iastore
    //   1546: aload #28
    //   1548: iconst_2
    //   1549: iconst_m1
    //   1550: iastore
    //   1551: aload #28
    //   1553: iconst_1
    //   1554: iconst_m1
    //   1555: iastore
    //   1556: aload #28
    //   1558: iconst_0
    //   1559: iconst_m1
    //   1560: iastore
    //   1561: iconst_m1
    //   1562: istore #20
    //   1564: aload_0
    //   1565: iconst_0
    //   1566: putfield mTotalLength : I
    //   1569: iconst_0
    //   1570: istore #5
    //   1572: iload #6
    //   1574: istore #18
    //   1576: iload #8
    //   1578: istore #6
    //   1580: iload #17
    //   1582: istore #13
    //   1584: iload #16
    //   1586: istore #7
    //   1588: iload #5
    //   1590: istore #16
    //   1592: iload #10
    //   1594: istore #8
    //   1596: iload #19
    //   1598: istore #5
    //   1600: iload #11
    //   1602: istore #14
    //   1604: iload #20
    //   1606: istore #11
    //   1608: iload #18
    //   1610: istore #10
    //   1612: iload #16
    //   1614: iload #7
    //   1616: if_icmpge -> 2132
    //   1619: aload_0
    //   1620: iload #16
    //   1622: invokevirtual getVirtualChildAt : (I)Landroid/view/View;
    //   1625: astore #25
    //   1627: aload #25
    //   1629: ifnull -> 2123
    //   1632: aload #25
    //   1634: invokevirtual getVisibility : ()I
    //   1637: bipush #8
    //   1639: if_icmpne -> 1645
    //   1642: goto -> 2123
    //   1645: aload #25
    //   1647: invokevirtual getLayoutParams : ()Landroid/view/ViewGroup$LayoutParams;
    //   1650: checkcast android/support/v7/widget/LinearLayoutCompat$LayoutParams
    //   1653: astore #26
    //   1655: aload #26
    //   1657: getfield weight : F
    //   1660: fstore #4
    //   1662: fload #4
    //   1664: fconst_0
    //   1665: fcmpl
    //   1666: ifle -> 1829
    //   1669: iload #5
    //   1671: i2f
    //   1672: fload #4
    //   1674: fmul
    //   1675: fload_3
    //   1676: fdiv
    //   1677: f2i
    //   1678: istore #18
    //   1680: iload_2
    //   1681: aload_0
    //   1682: invokevirtual getPaddingTop : ()I
    //   1685: aload_0
    //   1686: invokevirtual getPaddingBottom : ()I
    //   1689: iadd
    //   1690: aload #26
    //   1692: getfield topMargin : I
    //   1695: iadd
    //   1696: aload #26
    //   1698: getfield bottomMargin : I
    //   1701: iadd
    //   1702: aload #26
    //   1704: getfield height : I
    //   1707: invokestatic getChildMeasureSpec : (III)I
    //   1710: istore #20
    //   1712: aload #26
    //   1714: getfield width : I
    //   1717: ifne -> 1762
    //   1720: iload #14
    //   1722: ldc 1073741824
    //   1724: if_icmpeq -> 1730
    //   1727: goto -> 1762
    //   1730: iload #18
    //   1732: ifle -> 1742
    //   1735: iload #18
    //   1737: istore #17
    //   1739: goto -> 1745
    //   1742: iconst_0
    //   1743: istore #17
    //   1745: aload #25
    //   1747: iload #17
    //   1749: ldc 1073741824
    //   1751: invokestatic makeMeasureSpec : (II)I
    //   1754: iload #20
    //   1756: invokevirtual measure : (II)V
    //   1759: goto -> 1798
    //   1762: aload #25
    //   1764: invokevirtual getMeasuredWidth : ()I
    //   1767: iload #18
    //   1769: iadd
    //   1770: istore #19
    //   1772: iload #19
    //   1774: istore #17
    //   1776: iload #19
    //   1778: ifge -> 1784
    //   1781: iconst_0
    //   1782: istore #17
    //   1784: aload #25
    //   1786: iload #17
    //   1788: ldc 1073741824
    //   1790: invokestatic makeMeasureSpec : (II)I
    //   1793: iload #20
    //   1795: invokevirtual measure : (II)V
    //   1798: iload #6
    //   1800: aload #25
    //   1802: invokevirtual getMeasuredState : ()I
    //   1805: ldc_w -16777216
    //   1808: iand
    //   1809: invokestatic combineMeasuredStates : (II)I
    //   1812: istore #6
    //   1814: fload_3
    //   1815: fload #4
    //   1817: fsub
    //   1818: fstore_3
    //   1819: iload #5
    //   1821: iload #18
    //   1823: isub
    //   1824: istore #5
    //   1826: goto -> 1829
    //   1829: iload #15
    //   1831: ifeq -> 1870
    //   1834: aload_0
    //   1835: aload_0
    //   1836: getfield mTotalLength : I
    //   1839: aload #25
    //   1841: invokevirtual getMeasuredWidth : ()I
    //   1844: aload #26
    //   1846: getfield leftMargin : I
    //   1849: iadd
    //   1850: aload #26
    //   1852: getfield rightMargin : I
    //   1855: iadd
    //   1856: aload_0
    //   1857: aload #25
    //   1859: invokevirtual getNextLocationOffset : (Landroid/view/View;)I
    //   1862: iadd
    //   1863: iadd
    //   1864: putfield mTotalLength : I
    //   1867: goto -> 1912
    //   1870: aload_0
    //   1871: getfield mTotalLength : I
    //   1874: istore #17
    //   1876: aload_0
    //   1877: iload #17
    //   1879: aload #25
    //   1881: invokevirtual getMeasuredWidth : ()I
    //   1884: iload #17
    //   1886: iadd
    //   1887: aload #26
    //   1889: getfield leftMargin : I
    //   1892: iadd
    //   1893: aload #26
    //   1895: getfield rightMargin : I
    //   1898: iadd
    //   1899: aload_0
    //   1900: aload #25
    //   1902: invokevirtual getNextLocationOffset : (Landroid/view/View;)I
    //   1905: iadd
    //   1906: invokestatic max : (II)I
    //   1909: putfield mTotalLength : I
    //   1912: iload #21
    //   1914: ldc 1073741824
    //   1916: if_icmpeq -> 1934
    //   1919: aload #26
    //   1921: getfield height : I
    //   1924: iconst_m1
    //   1925: if_icmpne -> 1934
    //   1928: iconst_1
    //   1929: istore #17
    //   1931: goto -> 1937
    //   1934: iconst_0
    //   1935: istore #17
    //   1937: aload #26
    //   1939: getfield topMargin : I
    //   1942: aload #26
    //   1944: getfield bottomMargin : I
    //   1947: iadd
    //   1948: istore #20
    //   1950: aload #25
    //   1952: invokevirtual getMeasuredHeight : ()I
    //   1955: iload #20
    //   1957: iadd
    //   1958: istore #19
    //   1960: iload #11
    //   1962: iload #19
    //   1964: invokestatic max : (II)I
    //   1967: istore #18
    //   1969: iload #17
    //   1971: ifeq -> 1981
    //   1974: iload #20
    //   1976: istore #11
    //   1978: goto -> 1985
    //   1981: iload #19
    //   1983: istore #11
    //   1985: iload #10
    //   1987: iload #11
    //   1989: invokestatic max : (II)I
    //   1992: istore #11
    //   1994: iload #9
    //   1996: ifeq -> 2014
    //   1999: aload #26
    //   2001: getfield height : I
    //   2004: iconst_m1
    //   2005: if_icmpne -> 2014
    //   2008: iconst_1
    //   2009: istore #9
    //   2011: goto -> 2017
    //   2014: iconst_0
    //   2015: istore #9
    //   2017: iload #23
    //   2019: ifeq -> 2112
    //   2022: aload #25
    //   2024: invokevirtual getBaseline : ()I
    //   2027: istore #17
    //   2029: iload #17
    //   2031: iconst_m1
    //   2032: if_icmpeq -> 2109
    //   2035: aload #26
    //   2037: getfield gravity : I
    //   2040: ifge -> 2052
    //   2043: aload_0
    //   2044: getfield mGravity : I
    //   2047: istore #10
    //   2049: goto -> 2059
    //   2052: aload #26
    //   2054: getfield gravity : I
    //   2057: istore #10
    //   2059: iload #10
    //   2061: bipush #112
    //   2063: iand
    //   2064: iconst_4
    //   2065: ishr
    //   2066: bipush #-2
    //   2068: iand
    //   2069: iconst_1
    //   2070: ishr
    //   2071: istore #10
    //   2073: aload #27
    //   2075: iload #10
    //   2077: aload #27
    //   2079: iload #10
    //   2081: iaload
    //   2082: iload #17
    //   2084: invokestatic max : (II)I
    //   2087: iastore
    //   2088: aload #28
    //   2090: iload #10
    //   2092: aload #28
    //   2094: iload #10
    //   2096: iaload
    //   2097: iload #19
    //   2099: iload #17
    //   2101: isub
    //   2102: invokestatic max : (II)I
    //   2105: iastore
    //   2106: goto -> 2112
    //   2109: goto -> 2112
    //   2112: iload #11
    //   2114: istore #10
    //   2116: iload #18
    //   2118: istore #11
    //   2120: goto -> 2123
    //   2123: iload #16
    //   2125: iconst_1
    //   2126: iadd
    //   2127: istore #16
    //   2129: goto -> 1612
    //   2132: aload_0
    //   2133: aload_0
    //   2134: getfield mTotalLength : I
    //   2137: aload_0
    //   2138: invokevirtual getPaddingLeft : ()I
    //   2141: aload_0
    //   2142: invokevirtual getPaddingRight : ()I
    //   2145: iadd
    //   2146: iadd
    //   2147: putfield mTotalLength : I
    //   2150: aload #27
    //   2152: iconst_1
    //   2153: iaload
    //   2154: iconst_m1
    //   2155: if_icmpne -> 2186
    //   2158: aload #27
    //   2160: iconst_0
    //   2161: iaload
    //   2162: iconst_m1
    //   2163: if_icmpne -> 2186
    //   2166: aload #27
    //   2168: iconst_2
    //   2169: iaload
    //   2170: iconst_m1
    //   2171: if_icmpne -> 2186
    //   2174: iload #11
    //   2176: istore #5
    //   2178: aload #27
    //   2180: iconst_3
    //   2181: iaload
    //   2182: iconst_m1
    //   2183: if_icmpeq -> 2244
    //   2186: iload #11
    //   2188: aload #27
    //   2190: iconst_3
    //   2191: iaload
    //   2192: aload #27
    //   2194: iconst_0
    //   2195: iaload
    //   2196: aload #27
    //   2198: iconst_1
    //   2199: iaload
    //   2200: aload #27
    //   2202: iconst_2
    //   2203: iaload
    //   2204: invokestatic max : (II)I
    //   2207: invokestatic max : (II)I
    //   2210: invokestatic max : (II)I
    //   2213: aload #28
    //   2215: iconst_3
    //   2216: iaload
    //   2217: aload #28
    //   2219: iconst_0
    //   2220: iaload
    //   2221: aload #28
    //   2223: iconst_1
    //   2224: iaload
    //   2225: aload #28
    //   2227: iconst_2
    //   2228: iaload
    //   2229: invokestatic max : (II)I
    //   2232: invokestatic max : (II)I
    //   2235: invokestatic max : (II)I
    //   2238: iadd
    //   2239: invokestatic max : (II)I
    //   2242: istore #5
    //   2244: iload #10
    //   2246: istore #13
    //   2248: iload #7
    //   2250: istore #11
    //   2252: iload #8
    //   2254: istore #10
    //   2256: iload #13
    //   2258: istore #7
    //   2260: iload #6
    //   2262: istore #8
    //   2264: iload #5
    //   2266: istore #6
    //   2268: iload #9
    //   2270: ifne -> 2288
    //   2273: iload #5
    //   2275: istore #6
    //   2277: iload #21
    //   2279: ldc 1073741824
    //   2281: if_icmpeq -> 2288
    //   2284: iload #7
    //   2286: istore #6
    //   2288: aload_0
    //   2289: iload #10
    //   2291: ldc_w -16777216
    //   2294: iload #8
    //   2296: iand
    //   2297: ior
    //   2298: iload #6
    //   2300: aload_0
    //   2301: invokevirtual getPaddingTop : ()I
    //   2304: aload_0
    //   2305: invokevirtual getPaddingBottom : ()I
    //   2308: iadd
    //   2309: iadd
    //   2310: aload_0
    //   2311: invokevirtual getSuggestedMinimumHeight : ()I
    //   2314: invokestatic max : (II)I
    //   2317: iload_2
    //   2318: iload #8
    //   2320: bipush #16
    //   2322: ishl
    //   2323: invokestatic resolveSizeAndState : (III)I
    //   2326: invokevirtual setMeasuredDimension : (II)V
    //   2329: iload #12
    //   2331: ifeq -> 2342
    //   2334: aload_0
    //   2335: iload #11
    //   2337: iload_1
    //   2338: invokespecial forceUniformHeight : (II)V
    //   2341: return
    //   2342: return }
  
  int measureNullChild(int paramInt) { return 0; }
  
  void measureVertical(int paramInt1, int paramInt2) {
    this.mTotalLength = 0;
    int i2 = 0;
    float f = 0.0F;
    int i3 = getVirtualChildCount();
    int i11 = View.MeasureSpec.getMode(paramInt1);
    int m = View.MeasureSpec.getMode(paramInt2);
    int i9 = this.mBaselineAlignedChildIndex;
    boolean bool1 = this.mUseLargestChild;
    boolean bool = false;
    int i1 = 0;
    int n = 0;
    int i = 0;
    int j = 0;
    int i5 = 0;
    int i4 = 0;
    int k = 1;
    while (i4 < i3) {
      View view = getVirtualChildAt(i4);
      if (view == null) {
        this.mTotalLength += measureNullChild(i4);
      } else if (view.getVisibility() == 8) {
        i4 += getChildrenSkipCount(view, i4);
      } else {
        int i12;
        if (hasDividerBeforeChildAt(i4))
          this.mTotalLength += this.mDividerHeight; 
        LayoutParams layoutParams = (LayoutParams)view.getLayoutParams();
        f += layoutParams.weight;
        if (m == 1073741824 && layoutParams.height == 0 && layoutParams.weight > 0.0F) {
          i5 = this.mTotalLength;
          this.mTotalLength = Math.max(i5, layoutParams.topMargin + i5 + layoutParams.bottomMargin);
          i12 = 1;
        } else {
          if (layoutParams.height == 0 && layoutParams.weight > 0.0F) {
            layoutParams.height = -2;
            i12 = 0;
          } else {
            i12 = Integer.MIN_VALUE;
          } 
          if (f == 0.0F) {
            i14 = this.mTotalLength;
          } else {
            i14 = 0;
          } 
          LayoutParams layoutParams1 = layoutParams;
          int i13 = j;
          measureChildBeforeLayout(view, i4, paramInt1, 0, paramInt2, i14);
          if (i12 != Integer.MIN_VALUE)
            layoutParams1.height = i12; 
          int i14 = view.getMeasuredHeight();
          j = this.mTotalLength;
          this.mTotalLength = Math.max(j, j + i14 + layoutParams1.topMargin + layoutParams1.bottomMargin + getNextLocationOffset(view));
          j = i13;
          i12 = i5;
          if (bool1) {
            j = Math.max(i14, i13);
            i12 = i5;
          } 
        } 
        i5 = i;
        if (i9 >= 0 && i9 == i4 + 1)
          this.mBaselineChildTop = this.mTotalLength; 
        if (i4 >= i9 || layoutParams.weight <= 0.0F) {
          int i13 = 0;
          if (i11 != 1073741824 && layoutParams.width == -1) {
            bool = true;
            i13 = 1;
          } 
          int i14 = layoutParams.leftMargin + layoutParams.rightMargin;
          int i15 = view.getMeasuredWidth() + i14;
          int i16 = Math.max(n, i15);
          i2 = View.combineMeasuredStates(i2, view.getMeasuredState());
          if (k && layoutParams.width == -1) {
            i = 1;
          } else {
            i = 0;
          } 
          if (layoutParams.weight > 0.0F) {
            if (!i13)
              i14 = i15; 
            n = Math.max(i5, i14);
            k = i1;
          } else {
            if (!i13)
              i14 = i15; 
            k = Math.max(i1, i14);
            n = i5;
          } 
          i1 = i;
          i4 += getChildrenSkipCount(view, i4);
          i = n;
          i13 = k;
          n = i16;
          k = i1;
          i5 = i12;
          i1 = i13;
        } else {
          throw new RuntimeException("A child of LinearLayout with index less than mBaselineAlignedChildIndex has weight > 0, which won't work.  Either remove the weight, or don't set mBaselineAlignedChildIndex.");
        } 
      } 
      i4++;
    } 
    i4 = j;
    j = n;
    int i6 = i;
    if (this.mTotalLength > 0 && hasDividerBeforeChildAt(i3))
      this.mTotalLength += this.mDividerHeight; 
    int i8 = i3;
    if (bool1) {
      i = m;
      if (i == Integer.MIN_VALUE || i == 0) {
        this.mTotalLength = 0;
        for (i = 0; i < i8; i++) {
          View view = getVirtualChildAt(i);
          if (view == null) {
            this.mTotalLength += measureNullChild(i);
          } else if (view.getVisibility() == 8) {
            i += getChildrenSkipCount(view, i);
          } else {
            LayoutParams layoutParams = (LayoutParams)view.getLayoutParams();
            i3 = this.mTotalLength;
            this.mTotalLength = Math.max(i3, i3 + i4 + layoutParams.topMargin + layoutParams.bottomMargin + getNextLocationOffset(view));
          } 
        } 
      } 
    } 
    int i7 = m;
    this.mTotalLength += getPaddingTop() + getPaddingBottom();
    int i10 = View.resolveSizeAndState(Math.max(this.mTotalLength, getSuggestedMinimumHeight()), paramInt2, 0);
    i = (i10 & 0xFFFFFF) - this.mTotalLength;
    if (i5 != 0 || (i != 0 && f > 0.0F)) {
      float f1 = this.mWeightSum;
      if (f1 > 0.0F)
        f = f1; 
      this.mTotalLength = 0;
      i5 = 0;
      j = i1;
      m = i;
      int i12 = n;
      i1 = i9;
      i3 = i4;
      i4 = i6;
      i = i2;
      i2 = i7;
      n = m;
      m = i12;
      while (i5 < i8) {
        View view = getVirtualChildAt(i5);
        if (view.getVisibility() != 8) {
          LayoutParams layoutParams = (LayoutParams)view.getLayoutParams();
          f1 = layoutParams.weight;
          if (f1 > 0.0F) {
            i7 = (int)(n * f1 / f);
            i12 = getPaddingLeft();
            i9 = getPaddingRight();
            f -= f1;
            int i13 = layoutParams.leftMargin;
            int i14 = layoutParams.rightMargin;
            i6 = n - i7;
            i12 = getChildMeasureSpec(paramInt1, i12 + i9 + i13 + i14, layoutParams.width);
            if (layoutParams.height != 0 || i2 != 1073741824) {
              i7 = view.getMeasuredHeight() + i7;
              n = i7;
              if (i7 < 0)
                n = 0; 
              view.measure(i12, View.MeasureSpec.makeMeasureSpec(n, 1073741824));
            } else {
              if (i7 > 0) {
                n = i7;
              } else {
                n = 0;
              } 
              view.measure(i12, View.MeasureSpec.makeMeasureSpec(n, 1073741824));
            } 
            i = View.combineMeasuredStates(i, view.getMeasuredState() & 0xFFFFFF00);
            n = i6;
          } 
          i7 = layoutParams.leftMargin + layoutParams.rightMargin;
          i12 = view.getMeasuredWidth() + i7;
          i6 = Math.max(m, i12);
          if (i11 != 1073741824 && layoutParams.width == -1) {
            m = 1;
          } else {
            m = 0;
          } 
          if (m != 0) {
            m = i7;
          } else {
            m = i12;
          } 
          i7 = Math.max(j, m);
          if (k != 0 && layoutParams.width == -1) {
            j = 1;
          } else {
            j = 0;
          } 
          k = this.mTotalLength;
          this.mTotalLength = Math.max(k, k + view.getMeasuredHeight() + layoutParams.topMargin + layoutParams.bottomMargin + getNextLocationOffset(view));
          m = i6;
          k = j;
          j = i7;
        } 
        i5++;
      } 
      this.mTotalLength += getPaddingTop() + getPaddingBottom();
      n = m;
      m = j;
      j = n;
      i2 = i;
    } else {
      m = Math.max(i1, i6);
      if (bool1) {
        if (i7 != 1073741824) {
          for (n = 0; n < i8; n++) {
            View view = getVirtualChildAt(n);
            if (view != null && view.getVisibility() != 8 && ((LayoutParams)view.getLayoutParams()).weight > 0.0F)
              view.measure(View.MeasureSpec.makeMeasureSpec(view.getMeasuredWidth(), 1073741824), View.MeasureSpec.makeMeasureSpec(i4, 1073741824)); 
          } 
          n = i;
          i = m;
          m = n;
        } else {
          n = i;
          i = m;
          m = n;
        } 
      } else {
        n = m;
        m = i;
        i = n;
      } 
      m = i;
    } 
    i = j;
    if (k == 0) {
      i = j;
      if (i11 != 1073741824)
        i = m; 
    } 
    setMeasuredDimension(View.resolveSizeAndState(Math.max(i + getPaddingLeft() + getPaddingRight(), getSuggestedMinimumWidth()), paramInt1, i2), i10);
    if (bool)
      forceUniformWidth(i8, paramInt2); 
  }
  
  protected void onDraw(Canvas paramCanvas) {
    if (this.mDivider == null)
      return; 
    if (this.mOrientation == 1) {
      drawDividersVertical(paramCanvas);
      return;
    } 
    drawDividersHorizontal(paramCanvas);
  }
  
  public void onInitializeAccessibilityEvent(AccessibilityEvent paramAccessibilityEvent) {
    super.onInitializeAccessibilityEvent(paramAccessibilityEvent);
    paramAccessibilityEvent.setClassName(LinearLayoutCompat.class.getName());
  }
  
  public void onInitializeAccessibilityNodeInfo(AccessibilityNodeInfo paramAccessibilityNodeInfo) {
    super.onInitializeAccessibilityNodeInfo(paramAccessibilityNodeInfo);
    paramAccessibilityNodeInfo.setClassName(LinearLayoutCompat.class.getName());
  }
  
  protected void onLayout(boolean paramBoolean, int paramInt1, int paramInt2, int paramInt3, int paramInt4) {
    if (this.mOrientation == 1) {
      layoutVertical(paramInt1, paramInt2, paramInt3, paramInt4);
      return;
    } 
    layoutHorizontal(paramInt1, paramInt2, paramInt3, paramInt4);
  }
  
  protected void onMeasure(int paramInt1, int paramInt2) {
    if (this.mOrientation == 1) {
      measureVertical(paramInt1, paramInt2);
      return;
    } 
    measureHorizontal(paramInt1, paramInt2);
  }
  
  public void setBaselineAligned(boolean paramBoolean) { this.mBaselineAligned = paramBoolean; }
  
  public void setBaselineAlignedChildIndex(int paramInt) {
    if (paramInt >= 0 && paramInt < getChildCount()) {
      this.mBaselineAlignedChildIndex = paramInt;
      return;
    } 
    StringBuilder stringBuilder = new StringBuilder();
    stringBuilder.append("base aligned child index out of range (0, ");
    stringBuilder.append(getChildCount());
    stringBuilder.append(")");
    throw new IllegalArgumentException(stringBuilder.toString());
  }
  
  public void setDividerDrawable(Drawable paramDrawable) {
    if (paramDrawable == this.mDivider)
      return; 
    this.mDivider = paramDrawable;
    boolean bool = false;
    if (paramDrawable != null) {
      this.mDividerWidth = paramDrawable.getIntrinsicWidth();
      this.mDividerHeight = paramDrawable.getIntrinsicHeight();
    } else {
      this.mDividerWidth = 0;
      this.mDividerHeight = 0;
    } 
    if (paramDrawable == null)
      bool = true; 
    setWillNotDraw(bool);
    requestLayout();
  }
  
  public void setDividerPadding(int paramInt) { this.mDividerPadding = paramInt; }
  
  public void setGravity(int paramInt) {
    if (this.mGravity != paramInt) {
      int i = paramInt;
      if ((0x800007 & paramInt) == 0)
        i = paramInt | 0x800003; 
      paramInt = i;
      if ((i & 0x70) == 0)
        paramInt = i | 0x30; 
      this.mGravity = paramInt;
      requestLayout();
    } 
  }
  
  public void setHorizontalGravity(int paramInt) {
    paramInt &= 0x800007;
    int i = this.mGravity;
    if ((0x800007 & i) != paramInt) {
      this.mGravity = 0xFF7FFFF8 & i | paramInt;
      requestLayout();
    } 
  }
  
  public void setMeasureWithLargestChildEnabled(boolean paramBoolean) { this.mUseLargestChild = paramBoolean; }
  
  public void setOrientation(int paramInt) {
    if (this.mOrientation != paramInt) {
      this.mOrientation = paramInt;
      requestLayout();
    } 
  }
  
  public void setShowDividers(int paramInt) {
    if (paramInt != this.mShowDividers)
      requestLayout(); 
    this.mShowDividers = paramInt;
  }
  
  public void setVerticalGravity(int paramInt) {
    paramInt &= 0x70;
    int i = this.mGravity;
    if ((i & 0x70) != paramInt) {
      this.mGravity = i & 0xFFFFFF8F | paramInt;
      requestLayout();
    } 
  }
  
  public void setWeightSum(float paramFloat) { this.mWeightSum = Math.max(0.0F, paramFloat); }
  
  public boolean shouldDelayChildPressedState() { return false; }
  
  @Retention(RetentionPolicy.SOURCE)
  @RestrictTo({RestrictTo.Scope.LIBRARY_GROUP})
  public static @interface DividerMode {}
  
  public static class LayoutParams extends ViewGroup.MarginLayoutParams {
    public int gravity = -1;
    
    public float weight;
    
    public LayoutParams(int param1Int1, int param1Int2) {
      super(param1Int1, param1Int2);
      this.weight = 0.0F;
    }
    
    public LayoutParams(int param1Int1, int param1Int2, float param1Float) {
      super(param1Int1, param1Int2);
      this.weight = param1Float;
    }
    
    public LayoutParams(Context param1Context, AttributeSet param1AttributeSet) {
      super(param1Context, param1AttributeSet);
      TypedArray typedArray = param1Context.obtainStyledAttributes(param1AttributeSet, R.styleable.LinearLayoutCompat_Layout);
      this.weight = typedArray.getFloat(R.styleable.LinearLayoutCompat_Layout_android_layout_weight, 0.0F);
      this.gravity = typedArray.getInt(R.styleable.LinearLayoutCompat_Layout_android_layout_gravity, -1);
      typedArray.recycle();
    }
    
    public LayoutParams(LayoutParams param1LayoutParams) {
      super(param1LayoutParams);
      this.weight = param1LayoutParams.weight;
      this.gravity = param1LayoutParams.gravity;
    }
    
    public LayoutParams(ViewGroup.LayoutParams param1LayoutParams) { super(param1LayoutParams); }
    
    public LayoutParams(ViewGroup.MarginLayoutParams param1MarginLayoutParams) { super(param1MarginLayoutParams); }
  }
  
  @Retention(RetentionPolicy.SOURCE)
  @RestrictTo({RestrictTo.Scope.LIBRARY_GROUP})
  public static @interface OrientationMode {}
}


/* Location:              D:\Apks\New folder\classes-dex2jar.jar!\android\support\v7\widget\LinearLayoutCompat.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.0.7
 */