package android.support.constraint;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.os.Build;
import android.support.constraint.solver.Metrics;
import android.support.constraint.solver.widgets.Analyzer;
import android.support.constraint.solver.widgets.ConstraintAnchor;
import android.support.constraint.solver.widgets.ConstraintWidget;
import android.support.constraint.solver.widgets.ConstraintWidgetContainer;
import android.support.constraint.solver.widgets.Guideline;
import android.support.constraint.solver.widgets.ResolutionAnchor;
import android.util.AttributeSet;
import android.util.Log;
import android.util.SparseArray;
import android.util.SparseIntArray;
import android.view.View;
import android.view.ViewGroup;
import java.util.ArrayList;
import java.util.HashMap;

public class ConstraintLayout extends ViewGroup {
  static final boolean ALLOWS_EMBEDDED = false;
  
  private static final boolean CACHE_MEASURED_DIMENSION = false;
  
  private static final boolean DEBUG = false;
  
  public static final int DESIGN_INFO_ID = 0;
  
  private static final String TAG = "ConstraintLayout";
  
  private static final boolean USE_CONSTRAINTS_HELPER = true;
  
  public static final String VERSION = "ConstraintLayout-1.1.3";
  
  SparseArray<View> mChildrenByIds = new SparseArray();
  
  private ArrayList<ConstraintHelper> mConstraintHelpers = new ArrayList(4);
  
  private ConstraintSet mConstraintSet = null;
  
  private int mConstraintSetId = -1;
  
  private HashMap<String, Integer> mDesignIds = new HashMap();
  
  private boolean mDirtyHierarchy = true;
  
  private int mLastMeasureHeight = -1;
  
  int mLastMeasureHeightMode = 0;
  
  int mLastMeasureHeightSize = -1;
  
  private int mLastMeasureWidth = -1;
  
  int mLastMeasureWidthMode = 0;
  
  int mLastMeasureWidthSize = -1;
  
  ConstraintWidgetContainer mLayoutWidget = new ConstraintWidgetContainer();
  
  private int mMaxHeight = Integer.MAX_VALUE;
  
  private int mMaxWidth = Integer.MAX_VALUE;
  
  private Metrics mMetrics;
  
  private int mMinHeight = 0;
  
  private int mMinWidth = 0;
  
  private int mOptimizationLevel = 7;
  
  private final ArrayList<ConstraintWidget> mVariableDimensionsWidgets = new ArrayList(100);
  
  public ConstraintLayout(Context paramContext) {
    super(paramContext);
    init(null);
  }
  
  public ConstraintLayout(Context paramContext, AttributeSet paramAttributeSet) {
    super(paramContext, paramAttributeSet);
    init(paramAttributeSet);
  }
  
  public ConstraintLayout(Context paramContext, AttributeSet paramAttributeSet, int paramInt) {
    super(paramContext, paramAttributeSet, paramInt);
    init(paramAttributeSet);
  }
  
  private final ConstraintWidget getTargetWidget(int paramInt) {
    if (paramInt == 0)
      return this.mLayoutWidget; 
    View view2 = (View)this.mChildrenByIds.get(paramInt);
    View view1 = view2;
    if (view2 == null) {
      view2 = findViewById(paramInt);
      view1 = view2;
      if (view2 != null) {
        view1 = view2;
        if (view2 != this) {
          view1 = view2;
          if (view2.getParent() == this) {
            onViewAdded(view2);
            view1 = view2;
          } 
        } 
      } 
    } 
    return (view1 == this) ? this.mLayoutWidget : ((view1 == null) ? null : ((LayoutParams)view1.getLayoutParams()).widget);
  }
  
  private void init(AttributeSet paramAttributeSet) {
    this.mLayoutWidget.setCompanionWidget(this);
    this.mChildrenByIds.put(getId(), this);
    this.mConstraintSet = null;
    if (paramAttributeSet != null) {
      TypedArray typedArray = getContext().obtainStyledAttributes(paramAttributeSet, R.styleable.ConstraintLayout_Layout);
      int i = typedArray.getIndexCount();
      for (byte b = 0; b < i; b++) {
        int j = typedArray.getIndex(b);
        if (j == R.styleable.ConstraintLayout_Layout_android_minWidth) {
          this.mMinWidth = typedArray.getDimensionPixelOffset(j, this.mMinWidth);
        } else if (j == R.styleable.ConstraintLayout_Layout_android_minHeight) {
          this.mMinHeight = typedArray.getDimensionPixelOffset(j, this.mMinHeight);
        } else if (j == R.styleable.ConstraintLayout_Layout_android_maxWidth) {
          this.mMaxWidth = typedArray.getDimensionPixelOffset(j, this.mMaxWidth);
        } else if (j == R.styleable.ConstraintLayout_Layout_android_maxHeight) {
          this.mMaxHeight = typedArray.getDimensionPixelOffset(j, this.mMaxHeight);
        } else if (j == R.styleable.ConstraintLayout_Layout_layout_optimizationLevel) {
          this.mOptimizationLevel = typedArray.getInt(j, this.mOptimizationLevel);
        } else if (j == R.styleable.ConstraintLayout_Layout_constraintSet) {
          j = typedArray.getResourceId(j, 0);
          try {
            this.mConstraintSet = new ConstraintSet();
            this.mConstraintSet.load(getContext(), j);
          } catch (android.content.res.Resources.NotFoundException notFoundException) {
            this.mConstraintSet = null;
          } 
          this.mConstraintSetId = j;
        } 
      } 
      typedArray.recycle();
    } 
    this.mLayoutWidget.setOptimizationLevel(this.mOptimizationLevel);
  }
  
  private void internalMeasureChildren(int paramInt1, int paramInt2) {
    int i = getPaddingTop() + getPaddingBottom();
    int j = getPaddingLeft() + getPaddingRight();
    int k = getChildCount();
    byte b = 0;
    while (true) {
      int m = paramInt1;
      ConstraintLayout constraintLayout = this;
      if (b < k) {
        View view = constraintLayout.getChildAt(b);
        if (view.getVisibility() != 8) {
          LayoutParams layoutParams = (LayoutParams)view.getLayoutParams();
          ConstraintWidget constraintWidget = layoutParams.widget;
          if (!layoutParams.isGuideline && !layoutParams.isHelper) {
            int i3;
            constraintWidget.setVisibility(view.getVisibility());
            int i5 = layoutParams.width;
            int i6 = layoutParams.height;
            if (layoutParams.horizontalDimensionFixed || layoutParams.verticalDimensionFixed || (!layoutParams.horizontalDimensionFixed && layoutParams.matchConstraintDefaultWidth == 1) || layoutParams.width == -1 || (!layoutParams.verticalDimensionFixed && (layoutParams.matchConstraintDefaultHeight == 1 || layoutParams.height == -1))) {
              i3 = 1;
            } else {
              i3 = 0;
            } 
            int i7 = 0;
            int i1 = 0;
            int i8 = 0;
            byte b1 = 0;
            int i2 = 0;
            byte b2 = 0;
            int i4 = i5;
            int n = i6;
            if (i3) {
              boolean bool;
              if (i5 == 0) {
                i2 = getChildMeasureSpec(m, j, -2);
                n = 1;
              } else if (i5 == -1) {
                i2 = getChildMeasureSpec(m, j, -1);
                n = i7;
              } else {
                n = i8;
                if (i5 == -2)
                  n = 1; 
                i2 = getChildMeasureSpec(m, j, i5);
              } 
              if (i6 == 0) {
                i3 = getChildMeasureSpec(paramInt2, i, -2);
                i1 = 1;
              } else if (i6 == -1) {
                i3 = getChildMeasureSpec(paramInt2, i, -1);
                i1 = b1;
              } else {
                i1 = b2;
                if (i6 == -2)
                  i1 = 1; 
                i3 = getChildMeasureSpec(paramInt2, i, i6);
              } 
              view.measure(i2, i3);
              Metrics metrics = constraintLayout.mMetrics;
              if (metrics != null)
                metrics.measures++; 
              if (i5 == -2) {
                bool = true;
              } else {
                bool = false;
              } 
              constraintWidget.setWidthWrapContent(bool);
              if (i6 == -2) {
                bool = true;
              } else {
                bool = false;
              } 
              constraintWidget.setHeightWrapContent(bool);
              i4 = view.getMeasuredWidth();
              i3 = view.getMeasuredHeight();
              i2 = i1;
              i1 = n;
              n = i3;
            } 
            constraintWidget.setWidth(i4);
            constraintWidget.setHeight(n);
            if (i1 != 0)
              constraintWidget.setWrapWidth(i4); 
            if (i2 != 0)
              constraintWidget.setWrapHeight(n); 
            if (layoutParams.needsBaseline) {
              n = view.getBaseline();
              if (n != -1)
                constraintWidget.setBaselineDistance(n); 
            } 
          } 
        } 
        b++;
        continue;
      } 
      break;
    } 
  }
  
  private void internalMeasureDimensions(int paramInt1, int paramInt2) {
    ConstraintLayout constraintLayout = this;
    int j = getPaddingTop() + getPaddingBottom();
    int k = getPaddingLeft() + getPaddingRight();
    int m = getChildCount();
    int i;
    for (i = 0; i < m; i++) {
      View view = constraintLayout.getChildAt(i);
      if (view.getVisibility() != 8) {
        LayoutParams layoutParams = (LayoutParams)view.getLayoutParams();
        ConstraintWidget constraintWidget = layoutParams.widget;
        if (!layoutParams.isGuideline && !layoutParams.isHelper) {
          constraintWidget.setVisibility(view.getVisibility());
          int n = layoutParams.width;
          int i1 = layoutParams.height;
          if (n == 0 || i1 == 0) {
            constraintWidget.getResolutionWidth().invalidate();
            constraintWidget.getResolutionHeight().invalidate();
          } else {
            boolean bool1;
            int i2 = 0;
            boolean bool = false;
            if (n == -2)
              i2 = 1; 
            int i3 = getChildMeasureSpec(paramInt1, k, n);
            if (i1 == -2)
              bool = true; 
            view.measure(i3, getChildMeasureSpec(paramInt2, j, i1));
            Metrics metrics = constraintLayout.mMetrics;
            if (metrics != null)
              metrics.measures++; 
            if (n == -2) {
              bool1 = true;
            } else {
              bool1 = false;
            } 
            constraintWidget.setWidthWrapContent(bool1);
            if (i1 == -2) {
              bool1 = true;
            } else {
              bool1 = false;
            } 
            constraintWidget.setHeightWrapContent(bool1);
            n = view.getMeasuredWidth();
            i1 = view.getMeasuredHeight();
            constraintWidget.setWidth(n);
            constraintWidget.setHeight(i1);
            if (i2)
              constraintWidget.setWrapWidth(n); 
            if (bool)
              constraintWidget.setWrapHeight(i1); 
            if (layoutParams.needsBaseline) {
              i2 = view.getBaseline();
              if (i2 != -1)
                constraintWidget.setBaselineDistance(i2); 
            } 
            if (layoutParams.horizontalDimensionFixed && layoutParams.verticalDimensionFixed) {
              constraintWidget.getResolutionWidth().resolve(n);
              constraintWidget.getResolutionHeight().resolve(i1);
            } 
          } 
        } 
      } 
    } 
    constraintLayout.mLayoutWidget.solveGraph();
    byte b = 0;
    while (true) {
      int n = paramInt1;
      if (b < m) {
        ConstraintLayout constraintLayout1 = constraintLayout.getChildAt(b);
        if (constraintLayout1.getVisibility() == 8) {
          constraintLayout1 = constraintLayout;
        } else {
          LayoutParams layoutParams = (LayoutParams)constraintLayout1.getLayoutParams();
          ConstraintWidget constraintWidget = layoutParams.widget;
          if (!layoutParams.isGuideline) {
            if (layoutParams.isHelper) {
              constraintLayout1 = constraintLayout;
            } else {
              constraintWidget.setVisibility(constraintLayout1.getVisibility());
              int i1 = layoutParams.width;
              int i2 = layoutParams.height;
              if (i1 != 0 && i2 != 0) {
                constraintLayout1 = constraintLayout;
              } else {
                int i3;
                ResolutionAnchor resolutionAnchor1 = constraintWidget.getAnchor(ConstraintAnchor.Type.LEFT).getResolutionNode();
                ResolutionAnchor resolutionAnchor2 = constraintWidget.getAnchor(ConstraintAnchor.Type.RIGHT).getResolutionNode();
                if (constraintWidget.getAnchor(ConstraintAnchor.Type.LEFT).getTarget() != null && constraintWidget.getAnchor(ConstraintAnchor.Type.RIGHT).getTarget() != null) {
                  i = 1;
                } else {
                  i = 0;
                } 
                ResolutionAnchor resolutionAnchor3 = constraintWidget.getAnchor(ConstraintAnchor.Type.TOP).getResolutionNode();
                ResolutionAnchor resolutionAnchor4 = constraintWidget.getAnchor(ConstraintAnchor.Type.BOTTOM).getResolutionNode();
                if (constraintWidget.getAnchor(ConstraintAnchor.Type.TOP).getTarget() != null && constraintWidget.getAnchor(ConstraintAnchor.Type.BOTTOM).getTarget() != null) {
                  i3 = 1;
                } else {
                  i3 = 0;
                } 
                if (i1 == 0 && i2 == 0 && i != 0 && i3) {
                  constraintLayout1 = constraintLayout;
                } else {
                  boolean bool;
                  int i5;
                  byte b1 = 0;
                  byte b2 = 0;
                  int i6 = 0;
                  int i7 = 0;
                  if (constraintLayout.mLayoutWidget.getHorizontalDimensionBehaviour() != ConstraintWidget.DimensionBehaviour.WRAP_CONTENT) {
                    i5 = 1;
                  } else {
                    i5 = 0;
                  } 
                  if (constraintLayout.mLayoutWidget.getVerticalDimensionBehaviour() != ConstraintWidget.DimensionBehaviour.WRAP_CONTENT) {
                    i4 = 1;
                  } else {
                    i4 = 0;
                  } 
                  if (!i5)
                    constraintWidget.getResolutionWidth().invalidate(); 
                  if (!i4)
                    constraintWidget.getResolutionHeight().invalidate(); 
                  if (i1 == 0) {
                    if (i5 && constraintWidget.isSpreadWidth() && i != 0 && resolutionAnchor1.isResolved() && resolutionAnchor2.isResolved()) {
                      i1 = (int)(resolutionAnchor2.getResolvedValue() - resolutionAnchor1.getResolvedValue());
                      constraintWidget.getResolutionWidth().resolve(i1);
                      i = getChildMeasureSpec(n, k, i1);
                      n = i5;
                    } else {
                      i = getChildMeasureSpec(n, k, -2);
                      b1 = 1;
                      n = 0;
                    } 
                  } else if (i1 == -1) {
                    i = getChildMeasureSpec(n, k, -1);
                    n = i5;
                  } else {
                    b1 = b2;
                    if (i1 == -2)
                      b1 = 1; 
                    i = getChildMeasureSpec(n, k, i1);
                    n = i5;
                  } 
                  if (i2 == 0) {
                    if (i4 && constraintWidget.isSpreadHeight() && i3 && resolutionAnchor3.isResolved() && resolutionAnchor4.isResolved()) {
                      float f1 = resolutionAnchor4.getResolvedValue();
                      float f2 = resolutionAnchor3.getResolvedValue();
                      i3 = i4;
                      i2 = (int)(f1 - f2);
                      constraintWidget.getResolutionHeight().resolve(i2);
                      i4 = getChildMeasureSpec(paramInt2, j, i2);
                      i5 = i6;
                    } else {
                      i4 = getChildMeasureSpec(paramInt2, j, -2);
                      i5 = 1;
                      i3 = 0;
                    } 
                  } else {
                    i3 = i4;
                    if (i2 == -1) {
                      i4 = getChildMeasureSpec(paramInt2, j, -1);
                      i5 = i6;
                    } else {
                      i5 = i7;
                      if (i2 == -2)
                        i5 = 1; 
                      i4 = getChildMeasureSpec(paramInt2, j, i2);
                    } 
                  } 
                  constraintLayout1.measure(i, i4);
                  constraintLayout = this;
                  Metrics metrics = constraintLayout.mMetrics;
                  if (metrics != null)
                    metrics.measures++; 
                  if (i1 == -2) {
                    bool = true;
                  } else {
                    bool = false;
                  } 
                  constraintWidget.setWidthWrapContent(bool);
                  if (i2 == -2) {
                    bool = true;
                  } else {
                    bool = false;
                  } 
                  constraintWidget.setHeightWrapContent(bool);
                  i = constraintLayout1.getMeasuredWidth();
                  int i4 = constraintLayout1.getMeasuredHeight();
                  constraintWidget.setWidth(i);
                  constraintWidget.setHeight(i4);
                  if (b1 != 0)
                    constraintWidget.setWrapWidth(i); 
                  if (i5 != 0)
                    constraintWidget.setWrapHeight(i4); 
                  if (n != 0) {
                    constraintWidget.getResolutionWidth().resolve(i);
                  } else {
                    constraintWidget.getResolutionWidth().remove();
                  } 
                  if (i3 != 0) {
                    constraintWidget.getResolutionHeight().resolve(i4);
                  } else {
                    constraintWidget.getResolutionHeight().remove();
                  } 
                  if (layoutParams.needsBaseline) {
                    i = constraintLayout1.getBaseline();
                    constraintLayout1 = constraintLayout;
                    if (i != -1) {
                      constraintWidget.setBaselineDistance(i);
                      constraintLayout1 = constraintLayout;
                    } 
                  } else {
                    constraintLayout1 = constraintLayout;
                  } 
                } 
              } 
            } 
          } else {
            constraintLayout1 = constraintLayout;
          } 
        } 
        b++;
        constraintLayout = constraintLayout1;
        continue;
      } 
      break;
    } 
  }
  
  private void setChildrenConstraints() { // Byte code:
    //   0: aload_0
    //   1: invokevirtual isInEditMode : ()Z
    //   4: istore #13
    //   6: aload_0
    //   7: invokevirtual getChildCount : ()I
    //   10: istore #9
    //   12: iload #13
    //   14: ifeq -> 114
    //   17: iconst_0
    //   18: istore_2
    //   19: iload_2
    //   20: iload #9
    //   22: if_icmpge -> 114
    //   25: aload_0
    //   26: iload_2
    //   27: invokevirtual getChildAt : (I)Landroid/view/View;
    //   30: astore #16
    //   32: aload_0
    //   33: invokevirtual getResources : ()Landroid/content/res/Resources;
    //   36: aload #16
    //   38: invokevirtual getId : ()I
    //   41: invokevirtual getResourceName : (I)Ljava/lang/String;
    //   44: astore #15
    //   46: aload_0
    //   47: iconst_0
    //   48: aload #15
    //   50: aload #16
    //   52: invokevirtual getId : ()I
    //   55: invokestatic valueOf : (I)Ljava/lang/Integer;
    //   58: invokevirtual setDesignInformation : (ILjava/lang/Object;Ljava/lang/Object;)V
    //   61: aload #15
    //   63: bipush #47
    //   65: invokevirtual indexOf : (I)I
    //   68: istore_3
    //   69: aload #15
    //   71: astore #14
    //   73: iload_3
    //   74: iconst_m1
    //   75: if_icmpeq -> 88
    //   78: aload #15
    //   80: iload_3
    //   81: iconst_1
    //   82: iadd
    //   83: invokevirtual substring : (I)Ljava/lang/String;
    //   86: astore #14
    //   88: aload_0
    //   89: aload #16
    //   91: invokevirtual getId : ()I
    //   94: invokespecial getTargetWidget : (I)Landroid/support/constraint/solver/widgets/ConstraintWidget;
    //   97: aload #14
    //   99: invokevirtual setDebugName : (Ljava/lang/String;)V
    //   102: goto -> 107
    //   105: astore #14
    //   107: iload_2
    //   108: iconst_1
    //   109: iadd
    //   110: istore_2
    //   111: goto -> 19
    //   114: iconst_0
    //   115: istore_2
    //   116: iload_2
    //   117: iload #9
    //   119: if_icmpge -> 153
    //   122: aload_0
    //   123: aload_0
    //   124: iload_2
    //   125: invokevirtual getChildAt : (I)Landroid/view/View;
    //   128: invokevirtual getViewWidget : (Landroid/view/View;)Landroid/support/constraint/solver/widgets/ConstraintWidget;
    //   131: astore #14
    //   133: aload #14
    //   135: ifnonnull -> 141
    //   138: goto -> 146
    //   141: aload #14
    //   143: invokevirtual reset : ()V
    //   146: iload_2
    //   147: iconst_1
    //   148: iadd
    //   149: istore_2
    //   150: goto -> 116
    //   153: aload_0
    //   154: getfield mConstraintSetId : I
    //   157: iconst_m1
    //   158: if_icmpeq -> 215
    //   161: iconst_0
    //   162: istore_2
    //   163: iload_2
    //   164: iload #9
    //   166: if_icmpge -> 215
    //   169: aload_0
    //   170: iload_2
    //   171: invokevirtual getChildAt : (I)Landroid/view/View;
    //   174: astore #14
    //   176: aload #14
    //   178: invokevirtual getId : ()I
    //   181: aload_0
    //   182: getfield mConstraintSetId : I
    //   185: if_icmpne -> 208
    //   188: aload #14
    //   190: instanceof android/support/constraint/Constraints
    //   193: ifeq -> 208
    //   196: aload_0
    //   197: aload #14
    //   199: checkcast android/support/constraint/Constraints
    //   202: invokevirtual getConstraintSet : ()Landroid/support/constraint/ConstraintSet;
    //   205: putfield mConstraintSet : Landroid/support/constraint/ConstraintSet;
    //   208: iload_2
    //   209: iconst_1
    //   210: iadd
    //   211: istore_2
    //   212: goto -> 163
    //   215: aload_0
    //   216: getfield mConstraintSet : Landroid/support/constraint/ConstraintSet;
    //   219: astore #14
    //   221: aload #14
    //   223: ifnull -> 232
    //   226: aload #14
    //   228: aload_0
    //   229: invokevirtual applyToInternal : (Landroid/support/constraint/ConstraintLayout;)V
    //   232: aload_0
    //   233: getfield mLayoutWidget : Landroid/support/constraint/solver/widgets/ConstraintWidgetContainer;
    //   236: invokevirtual removeAllChildren : ()V
    //   239: aload_0
    //   240: getfield mConstraintHelpers : Ljava/util/ArrayList;
    //   243: invokevirtual size : ()I
    //   246: istore #8
    //   248: iload #8
    //   250: ifle -> 283
    //   253: iconst_0
    //   254: istore_2
    //   255: iload_2
    //   256: iload #8
    //   258: if_icmpge -> 283
    //   261: aload_0
    //   262: getfield mConstraintHelpers : Ljava/util/ArrayList;
    //   265: iload_2
    //   266: invokevirtual get : (I)Ljava/lang/Object;
    //   269: checkcast android/support/constraint/ConstraintHelper
    //   272: aload_0
    //   273: invokevirtual updatePreLayout : (Landroid/support/constraint/ConstraintLayout;)V
    //   276: iload_2
    //   277: iconst_1
    //   278: iadd
    //   279: istore_2
    //   280: goto -> 255
    //   283: iconst_0
    //   284: istore_2
    //   285: iload_2
    //   286: iload #9
    //   288: if_icmpge -> 322
    //   291: aload_0
    //   292: iload_2
    //   293: invokevirtual getChildAt : (I)Landroid/view/View;
    //   296: astore #14
    //   298: aload #14
    //   300: instanceof android/support/constraint/Placeholder
    //   303: ifeq -> 315
    //   306: aload #14
    //   308: checkcast android/support/constraint/Placeholder
    //   311: aload_0
    //   312: invokevirtual updatePreLayout : (Landroid/support/constraint/ConstraintLayout;)V
    //   315: iload_2
    //   316: iconst_1
    //   317: iadd
    //   318: istore_2
    //   319: goto -> 285
    //   322: iconst_0
    //   323: istore #10
    //   325: iload #10
    //   327: iload #9
    //   329: if_icmpge -> 2026
    //   332: aload_0
    //   333: iload #10
    //   335: invokevirtual getChildAt : (I)Landroid/view/View;
    //   338: astore #15
    //   340: aload_0
    //   341: aload #15
    //   343: invokevirtual getViewWidget : (Landroid/view/View;)Landroid/support/constraint/solver/widgets/ConstraintWidget;
    //   346: astore #16
    //   348: aload #16
    //   350: ifnonnull -> 356
    //   353: goto -> 2017
    //   356: aload #15
    //   358: invokevirtual getLayoutParams : ()Landroid/view/ViewGroup$LayoutParams;
    //   361: checkcast android/support/constraint/ConstraintLayout$LayoutParams
    //   364: astore #14
    //   366: aload #14
    //   368: invokevirtual validate : ()V
    //   371: aload #14
    //   373: getfield helped : Z
    //   376: ifeq -> 388
    //   379: aload #14
    //   381: iconst_0
    //   382: putfield helped : Z
    //   385: goto -> 458
    //   388: iload #13
    //   390: ifeq -> 458
    //   393: aload_0
    //   394: invokevirtual getResources : ()Landroid/content/res/Resources;
    //   397: aload #15
    //   399: invokevirtual getId : ()I
    //   402: invokevirtual getResourceName : (I)Ljava/lang/String;
    //   405: astore #17
    //   407: aload_0
    //   408: iconst_0
    //   409: aload #17
    //   411: aload #15
    //   413: invokevirtual getId : ()I
    //   416: invokestatic valueOf : (I)Ljava/lang/Integer;
    //   419: invokevirtual setDesignInformation : (ILjava/lang/Object;Ljava/lang/Object;)V
    //   422: aload #17
    //   424: aload #17
    //   426: ldc_w 'id/'
    //   429: invokevirtual indexOf : (Ljava/lang/String;)I
    //   432: iconst_3
    //   433: iadd
    //   434: invokevirtual substring : (I)Ljava/lang/String;
    //   437: astore #17
    //   439: aload_0
    //   440: aload #15
    //   442: invokevirtual getId : ()I
    //   445: invokespecial getTargetWidget : (I)Landroid/support/constraint/solver/widgets/ConstraintWidget;
    //   448: aload #17
    //   450: invokevirtual setDebugName : (Ljava/lang/String;)V
    //   453: goto -> 458
    //   456: astore #17
    //   458: aload #16
    //   460: aload #15
    //   462: invokevirtual getVisibility : ()I
    //   465: invokevirtual setVisibility : (I)V
    //   468: aload #14
    //   470: getfield isInPlaceholder : Z
    //   473: ifeq -> 483
    //   476: aload #16
    //   478: bipush #8
    //   480: invokevirtual setVisibility : (I)V
    //   483: aload #16
    //   485: aload #15
    //   487: invokevirtual setCompanionWidget : (Ljava/lang/Object;)V
    //   490: aload_0
    //   491: getfield mLayoutWidget : Landroid/support/constraint/solver/widgets/ConstraintWidgetContainer;
    //   494: aload #16
    //   496: invokevirtual add : (Landroid/support/constraint/solver/widgets/ConstraintWidget;)V
    //   499: aload #14
    //   501: getfield verticalDimensionFixed : Z
    //   504: ifeq -> 515
    //   507: aload #14
    //   509: getfield horizontalDimensionFixed : Z
    //   512: ifne -> 525
    //   515: aload_0
    //   516: getfield mVariableDimensionsWidgets : Ljava/util/ArrayList;
    //   519: aload #16
    //   521: invokevirtual add : (Ljava/lang/Object;)Z
    //   524: pop
    //   525: aload #14
    //   527: getfield isGuideline : Z
    //   530: ifeq -> 629
    //   533: aload #16
    //   535: checkcast android/support/constraint/solver/widgets/Guideline
    //   538: astore #15
    //   540: aload #14
    //   542: getfield resolvedGuideBegin : I
    //   545: istore_2
    //   546: aload #14
    //   548: getfield resolvedGuideEnd : I
    //   551: istore_3
    //   552: aload #14
    //   554: getfield resolvedGuidePercent : F
    //   557: fstore_1
    //   558: getstatic android/os/Build$VERSION.SDK_INT : I
    //   561: bipush #17
    //   563: if_icmpge -> 584
    //   566: aload #14
    //   568: getfield guideBegin : I
    //   571: istore_2
    //   572: aload #14
    //   574: getfield guideEnd : I
    //   577: istore_3
    //   578: aload #14
    //   580: getfield guidePercent : F
    //   583: fstore_1
    //   584: fload_1
    //   585: ldc_w -1.0
    //   588: fcmpl
    //   589: ifeq -> 601
    //   592: aload #15
    //   594: fload_1
    //   595: invokevirtual setGuidePercent : (F)V
    //   598: goto -> 626
    //   601: iload_2
    //   602: iconst_m1
    //   603: if_icmpeq -> 615
    //   606: aload #15
    //   608: iload_2
    //   609: invokevirtual setGuideBegin : (I)V
    //   612: goto -> 626
    //   615: iload_3
    //   616: iconst_m1
    //   617: if_icmpeq -> 626
    //   620: aload #15
    //   622: iload_3
    //   623: invokevirtual setGuideEnd : (I)V
    //   626: goto -> 2017
    //   629: aload #14
    //   631: getfield leftToLeft : I
    //   634: iconst_m1
    //   635: if_icmpne -> 791
    //   638: aload #14
    //   640: getfield leftToRight : I
    //   643: iconst_m1
    //   644: if_icmpne -> 791
    //   647: aload #14
    //   649: getfield rightToLeft : I
    //   652: iconst_m1
    //   653: if_icmpne -> 791
    //   656: aload #14
    //   658: getfield rightToRight : I
    //   661: iconst_m1
    //   662: if_icmpne -> 791
    //   665: aload #14
    //   667: getfield startToStart : I
    //   670: iconst_m1
    //   671: if_icmpne -> 791
    //   674: aload #14
    //   676: getfield startToEnd : I
    //   679: iconst_m1
    //   680: if_icmpne -> 791
    //   683: aload #14
    //   685: getfield endToStart : I
    //   688: iconst_m1
    //   689: if_icmpne -> 791
    //   692: aload #14
    //   694: getfield endToEnd : I
    //   697: iconst_m1
    //   698: if_icmpne -> 791
    //   701: aload #14
    //   703: getfield topToTop : I
    //   706: iconst_m1
    //   707: if_icmpne -> 791
    //   710: aload #14
    //   712: getfield topToBottom : I
    //   715: iconst_m1
    //   716: if_icmpne -> 791
    //   719: aload #14
    //   721: getfield bottomToTop : I
    //   724: iconst_m1
    //   725: if_icmpne -> 791
    //   728: aload #14
    //   730: getfield bottomToBottom : I
    //   733: iconst_m1
    //   734: if_icmpne -> 791
    //   737: aload #14
    //   739: getfield baselineToBaseline : I
    //   742: iconst_m1
    //   743: if_icmpne -> 791
    //   746: aload #14
    //   748: getfield editorAbsoluteX : I
    //   751: iconst_m1
    //   752: if_icmpne -> 791
    //   755: aload #14
    //   757: getfield editorAbsoluteY : I
    //   760: iconst_m1
    //   761: if_icmpne -> 791
    //   764: aload #14
    //   766: getfield circleConstraint : I
    //   769: iconst_m1
    //   770: if_icmpne -> 791
    //   773: aload #14
    //   775: getfield width : I
    //   778: iconst_m1
    //   779: if_icmpeq -> 791
    //   782: aload #14
    //   784: getfield height : I
    //   787: iconst_m1
    //   788: if_icmpne -> 626
    //   791: aload #14
    //   793: getfield resolvedLeftToLeft : I
    //   796: istore_3
    //   797: aload #14
    //   799: getfield resolvedLeftToRight : I
    //   802: istore #4
    //   804: aload #14
    //   806: getfield resolvedRightToLeft : I
    //   809: istore_2
    //   810: aload #14
    //   812: getfield resolvedRightToRight : I
    //   815: istore #6
    //   817: aload #14
    //   819: getfield resolveGoneLeftMargin : I
    //   822: istore #5
    //   824: aload #14
    //   826: getfield resolveGoneRightMargin : I
    //   829: istore #7
    //   831: aload #14
    //   833: getfield resolvedHorizontalBias : F
    //   836: fstore_1
    //   837: getstatic android/os/Build$VERSION.SDK_INT : I
    //   840: bipush #17
    //   842: if_icmpge -> 1077
    //   845: aload #14
    //   847: getfield leftToLeft : I
    //   850: istore_2
    //   851: aload #14
    //   853: getfield leftToRight : I
    //   856: istore #6
    //   858: aload #14
    //   860: getfield rightToLeft : I
    //   863: istore #7
    //   865: aload #14
    //   867: getfield rightToRight : I
    //   870: istore #12
    //   872: aload #14
    //   874: getfield goneLeftMargin : I
    //   877: istore #4
    //   879: aload #14
    //   881: getfield goneRightMargin : I
    //   884: istore #5
    //   886: aload #14
    //   888: getfield horizontalBias : F
    //   891: fstore_1
    //   892: iload_2
    //   893: iconst_m1
    //   894: if_icmpne -> 948
    //   897: iload #6
    //   899: iconst_m1
    //   900: if_icmpne -> 948
    //   903: aload #14
    //   905: getfield startToStart : I
    //   908: iconst_m1
    //   909: if_icmpeq -> 924
    //   912: aload #14
    //   914: getfield startToStart : I
    //   917: istore_3
    //   918: iload #6
    //   920: istore_2
    //   921: goto -> 953
    //   924: aload #14
    //   926: getfield startToEnd : I
    //   929: iconst_m1
    //   930: if_icmpeq -> 948
    //   933: aload #14
    //   935: getfield startToEnd : I
    //   938: istore #6
    //   940: iload_2
    //   941: istore_3
    //   942: iload #6
    //   944: istore_2
    //   945: goto -> 953
    //   948: iload_2
    //   949: istore_3
    //   950: iload #6
    //   952: istore_2
    //   953: iload #7
    //   955: iconst_m1
    //   956: if_icmpne -> 1052
    //   959: iload #12
    //   961: iconst_m1
    //   962: if_icmpne -> 1052
    //   965: iload_3
    //   966: istore #6
    //   968: aload #14
    //   970: getfield endToStart : I
    //   973: iconst_m1
    //   974: if_icmpeq -> 1008
    //   977: aload #14
    //   979: getfield endToStart : I
    //   982: istore #11
    //   984: iload #6
    //   986: istore_3
    //   987: iload #5
    //   989: istore #7
    //   991: iload #4
    //   993: istore #5
    //   995: iload_2
    //   996: istore #4
    //   998: iload #12
    //   1000: istore #6
    //   1002: iload #11
    //   1004: istore_2
    //   1005: goto -> 1077
    //   1008: aload #14
    //   1010: getfield endToEnd : I
    //   1013: iconst_m1
    //   1014: if_icmpeq -> 1052
    //   1017: aload #14
    //   1019: getfield endToEnd : I
    //   1022: istore #12
    //   1024: iload #6
    //   1026: istore_3
    //   1027: iload #5
    //   1029: istore #11
    //   1031: iload #4
    //   1033: istore #5
    //   1035: iload_2
    //   1036: istore #4
    //   1038: iload #12
    //   1040: istore #6
    //   1042: iload #7
    //   1044: istore_2
    //   1045: iload #11
    //   1047: istore #7
    //   1049: goto -> 1077
    //   1052: iload #5
    //   1054: istore #11
    //   1056: iload #4
    //   1058: istore #5
    //   1060: iload_2
    //   1061: istore #4
    //   1063: iload #12
    //   1065: istore #6
    //   1067: iload #7
    //   1069: istore_2
    //   1070: iload #11
    //   1072: istore #7
    //   1074: goto -> 1077
    //   1077: aload #14
    //   1079: getfield circleConstraint : I
    //   1082: iconst_m1
    //   1083: if_icmpeq -> 1125
    //   1086: aload_0
    //   1087: aload #14
    //   1089: getfield circleConstraint : I
    //   1092: invokespecial getTargetWidget : (I)Landroid/support/constraint/solver/widgets/ConstraintWidget;
    //   1095: astore #15
    //   1097: aload #15
    //   1099: ifnull -> 1122
    //   1102: aload #16
    //   1104: aload #15
    //   1106: aload #14
    //   1108: getfield circleAngle : F
    //   1111: aload #14
    //   1113: getfield circleRadius : I
    //   1116: invokevirtual connectCircularConstraint : (Landroid/support/constraint/solver/widgets/ConstraintWidget;FI)V
    //   1119: goto -> 1122
    //   1122: goto -> 1681
    //   1125: iload_3
    //   1126: iconst_m1
    //   1127: if_icmpeq -> 1168
    //   1130: aload_0
    //   1131: iload_3
    //   1132: invokespecial getTargetWidget : (I)Landroid/support/constraint/solver/widgets/ConstraintWidget;
    //   1135: astore #15
    //   1137: aload #15
    //   1139: ifnull -> 1165
    //   1142: aload #16
    //   1144: getstatic android/support/constraint/solver/widgets/ConstraintAnchor$Type.LEFT : Landroid/support/constraint/solver/widgets/ConstraintAnchor$Type;
    //   1147: aload #15
    //   1149: getstatic android/support/constraint/solver/widgets/ConstraintAnchor$Type.LEFT : Landroid/support/constraint/solver/widgets/ConstraintAnchor$Type;
    //   1152: aload #14
    //   1154: getfield leftMargin : I
    //   1157: iload #5
    //   1159: invokevirtual immediateConnect : (Landroid/support/constraint/solver/widgets/ConstraintAnchor$Type;Landroid/support/constraint/solver/widgets/ConstraintWidget;Landroid/support/constraint/solver/widgets/ConstraintAnchor$Type;II)V
    //   1162: goto -> 1210
    //   1165: goto -> 1210
    //   1168: iload #4
    //   1170: iconst_m1
    //   1171: if_icmpeq -> 1210
    //   1174: aload_0
    //   1175: iload #4
    //   1177: invokespecial getTargetWidget : (I)Landroid/support/constraint/solver/widgets/ConstraintWidget;
    //   1180: astore #15
    //   1182: aload #15
    //   1184: ifnull -> 1210
    //   1187: aload #16
    //   1189: getstatic android/support/constraint/solver/widgets/ConstraintAnchor$Type.LEFT : Landroid/support/constraint/solver/widgets/ConstraintAnchor$Type;
    //   1192: aload #15
    //   1194: getstatic android/support/constraint/solver/widgets/ConstraintAnchor$Type.RIGHT : Landroid/support/constraint/solver/widgets/ConstraintAnchor$Type;
    //   1197: aload #14
    //   1199: getfield leftMargin : I
    //   1202: iload #5
    //   1204: invokevirtual immediateConnect : (Landroid/support/constraint/solver/widgets/ConstraintAnchor$Type;Landroid/support/constraint/solver/widgets/ConstraintWidget;Landroid/support/constraint/solver/widgets/ConstraintAnchor$Type;II)V
    //   1207: goto -> 1210
    //   1210: aload #14
    //   1212: astore #15
    //   1214: iload_2
    //   1215: iconst_m1
    //   1216: if_icmpeq -> 1254
    //   1219: aload_0
    //   1220: iload_2
    //   1221: invokespecial getTargetWidget : (I)Landroid/support/constraint/solver/widgets/ConstraintWidget;
    //   1224: astore #17
    //   1226: aload #17
    //   1228: ifnull -> 1296
    //   1231: aload #16
    //   1233: getstatic android/support/constraint/solver/widgets/ConstraintAnchor$Type.RIGHT : Landroid/support/constraint/solver/widgets/ConstraintAnchor$Type;
    //   1236: aload #17
    //   1238: getstatic android/support/constraint/solver/widgets/ConstraintAnchor$Type.LEFT : Landroid/support/constraint/solver/widgets/ConstraintAnchor$Type;
    //   1241: aload #15
    //   1243: getfield rightMargin : I
    //   1246: iload #7
    //   1248: invokevirtual immediateConnect : (Landroid/support/constraint/solver/widgets/ConstraintAnchor$Type;Landroid/support/constraint/solver/widgets/ConstraintWidget;Landroid/support/constraint/solver/widgets/ConstraintAnchor$Type;II)V
    //   1251: goto -> 1296
    //   1254: iload #6
    //   1256: iconst_m1
    //   1257: if_icmpeq -> 1296
    //   1260: aload_0
    //   1261: iload #6
    //   1263: invokespecial getTargetWidget : (I)Landroid/support/constraint/solver/widgets/ConstraintWidget;
    //   1266: astore #17
    //   1268: aload #17
    //   1270: ifnull -> 1296
    //   1273: aload #16
    //   1275: getstatic android/support/constraint/solver/widgets/ConstraintAnchor$Type.RIGHT : Landroid/support/constraint/solver/widgets/ConstraintAnchor$Type;
    //   1278: aload #17
    //   1280: getstatic android/support/constraint/solver/widgets/ConstraintAnchor$Type.RIGHT : Landroid/support/constraint/solver/widgets/ConstraintAnchor$Type;
    //   1283: aload #15
    //   1285: getfield rightMargin : I
    //   1288: iload #7
    //   1290: invokevirtual immediateConnect : (Landroid/support/constraint/solver/widgets/ConstraintAnchor$Type;Landroid/support/constraint/solver/widgets/ConstraintWidget;Landroid/support/constraint/solver/widgets/ConstraintAnchor$Type;II)V
    //   1293: goto -> 1296
    //   1296: aload #15
    //   1298: getfield topToTop : I
    //   1301: iconst_m1
    //   1302: if_icmpeq -> 1347
    //   1305: aload_0
    //   1306: aload #15
    //   1308: getfield topToTop : I
    //   1311: invokespecial getTargetWidget : (I)Landroid/support/constraint/solver/widgets/ConstraintWidget;
    //   1314: astore #17
    //   1316: aload #17
    //   1318: ifnull -> 1398
    //   1321: aload #16
    //   1323: getstatic android/support/constraint/solver/widgets/ConstraintAnchor$Type.TOP : Landroid/support/constraint/solver/widgets/ConstraintAnchor$Type;
    //   1326: aload #17
    //   1328: getstatic android/support/constraint/solver/widgets/ConstraintAnchor$Type.TOP : Landroid/support/constraint/solver/widgets/ConstraintAnchor$Type;
    //   1331: aload #15
    //   1333: getfield topMargin : I
    //   1336: aload #15
    //   1338: getfield goneTopMargin : I
    //   1341: invokevirtual immediateConnect : (Landroid/support/constraint/solver/widgets/ConstraintAnchor$Type;Landroid/support/constraint/solver/widgets/ConstraintWidget;Landroid/support/constraint/solver/widgets/ConstraintAnchor$Type;II)V
    //   1344: goto -> 1398
    //   1347: aload #15
    //   1349: getfield topToBottom : I
    //   1352: iconst_m1
    //   1353: if_icmpeq -> 1398
    //   1356: aload_0
    //   1357: aload #15
    //   1359: getfield topToBottom : I
    //   1362: invokespecial getTargetWidget : (I)Landroid/support/constraint/solver/widgets/ConstraintWidget;
    //   1365: astore #17
    //   1367: aload #17
    //   1369: ifnull -> 1398
    //   1372: aload #16
    //   1374: getstatic android/support/constraint/solver/widgets/ConstraintAnchor$Type.TOP : Landroid/support/constraint/solver/widgets/ConstraintAnchor$Type;
    //   1377: aload #17
    //   1379: getstatic android/support/constraint/solver/widgets/ConstraintAnchor$Type.BOTTOM : Landroid/support/constraint/solver/widgets/ConstraintAnchor$Type;
    //   1382: aload #15
    //   1384: getfield topMargin : I
    //   1387: aload #15
    //   1389: getfield goneTopMargin : I
    //   1392: invokevirtual immediateConnect : (Landroid/support/constraint/solver/widgets/ConstraintAnchor$Type;Landroid/support/constraint/solver/widgets/ConstraintWidget;Landroid/support/constraint/solver/widgets/ConstraintAnchor$Type;II)V
    //   1395: goto -> 1398
    //   1398: aload #15
    //   1400: getfield bottomToTop : I
    //   1403: iconst_m1
    //   1404: if_icmpeq -> 1449
    //   1407: aload_0
    //   1408: aload #15
    //   1410: getfield bottomToTop : I
    //   1413: invokespecial getTargetWidget : (I)Landroid/support/constraint/solver/widgets/ConstraintWidget;
    //   1416: astore #17
    //   1418: aload #17
    //   1420: ifnull -> 1500
    //   1423: aload #16
    //   1425: getstatic android/support/constraint/solver/widgets/ConstraintAnchor$Type.BOTTOM : Landroid/support/constraint/solver/widgets/ConstraintAnchor$Type;
    //   1428: aload #17
    //   1430: getstatic android/support/constraint/solver/widgets/ConstraintAnchor$Type.TOP : Landroid/support/constraint/solver/widgets/ConstraintAnchor$Type;
    //   1433: aload #15
    //   1435: getfield bottomMargin : I
    //   1438: aload #15
    //   1440: getfield goneBottomMargin : I
    //   1443: invokevirtual immediateConnect : (Landroid/support/constraint/solver/widgets/ConstraintAnchor$Type;Landroid/support/constraint/solver/widgets/ConstraintWidget;Landroid/support/constraint/solver/widgets/ConstraintAnchor$Type;II)V
    //   1446: goto -> 1500
    //   1449: aload #15
    //   1451: getfield bottomToBottom : I
    //   1454: iconst_m1
    //   1455: if_icmpeq -> 1500
    //   1458: aload_0
    //   1459: aload #15
    //   1461: getfield bottomToBottom : I
    //   1464: invokespecial getTargetWidget : (I)Landroid/support/constraint/solver/widgets/ConstraintWidget;
    //   1467: astore #17
    //   1469: aload #17
    //   1471: ifnull -> 1500
    //   1474: aload #16
    //   1476: getstatic android/support/constraint/solver/widgets/ConstraintAnchor$Type.BOTTOM : Landroid/support/constraint/solver/widgets/ConstraintAnchor$Type;
    //   1479: aload #17
    //   1481: getstatic android/support/constraint/solver/widgets/ConstraintAnchor$Type.BOTTOM : Landroid/support/constraint/solver/widgets/ConstraintAnchor$Type;
    //   1484: aload #15
    //   1486: getfield bottomMargin : I
    //   1489: aload #15
    //   1491: getfield goneBottomMargin : I
    //   1494: invokevirtual immediateConnect : (Landroid/support/constraint/solver/widgets/ConstraintAnchor$Type;Landroid/support/constraint/solver/widgets/ConstraintWidget;Landroid/support/constraint/solver/widgets/ConstraintAnchor$Type;II)V
    //   1497: goto -> 1500
    //   1500: aload #15
    //   1502: getfield baselineToBaseline : I
    //   1505: iconst_m1
    //   1506: if_icmpeq -> 1629
    //   1509: aload_0
    //   1510: getfield mChildrenByIds : Landroid/util/SparseArray;
    //   1513: aload #15
    //   1515: getfield baselineToBaseline : I
    //   1518: invokevirtual get : (I)Ljava/lang/Object;
    //   1521: checkcast android/view/View
    //   1524: astore #18
    //   1526: aload_0
    //   1527: aload #15
    //   1529: getfield baselineToBaseline : I
    //   1532: invokespecial getTargetWidget : (I)Landroid/support/constraint/solver/widgets/ConstraintWidget;
    //   1535: astore #17
    //   1537: aload #17
    //   1539: ifnull -> 1629
    //   1542: aload #18
    //   1544: ifnull -> 1629
    //   1547: aload #18
    //   1549: invokevirtual getLayoutParams : ()Landroid/view/ViewGroup$LayoutParams;
    //   1552: instanceof android/support/constraint/ConstraintLayout$LayoutParams
    //   1555: ifeq -> 1629
    //   1558: aload #18
    //   1560: invokevirtual getLayoutParams : ()Landroid/view/ViewGroup$LayoutParams;
    //   1563: checkcast android/support/constraint/ConstraintLayout$LayoutParams
    //   1566: astore #18
    //   1568: aload #15
    //   1570: iconst_1
    //   1571: putfield needsBaseline : Z
    //   1574: aload #18
    //   1576: iconst_1
    //   1577: putfield needsBaseline : Z
    //   1580: aload #16
    //   1582: getstatic android/support/constraint/solver/widgets/ConstraintAnchor$Type.BASELINE : Landroid/support/constraint/solver/widgets/ConstraintAnchor$Type;
    //   1585: invokevirtual getAnchor : (Landroid/support/constraint/solver/widgets/ConstraintAnchor$Type;)Landroid/support/constraint/solver/widgets/ConstraintAnchor;
    //   1588: aload #17
    //   1590: getstatic android/support/constraint/solver/widgets/ConstraintAnchor$Type.BASELINE : Landroid/support/constraint/solver/widgets/ConstraintAnchor$Type;
    //   1593: invokevirtual getAnchor : (Landroid/support/constraint/solver/widgets/ConstraintAnchor$Type;)Landroid/support/constraint/solver/widgets/ConstraintAnchor;
    //   1596: iconst_0
    //   1597: iconst_m1
    //   1598: getstatic android/support/constraint/solver/widgets/ConstraintAnchor$Strength.STRONG : Landroid/support/constraint/solver/widgets/ConstraintAnchor$Strength;
    //   1601: iconst_0
    //   1602: iconst_1
    //   1603: invokevirtual connect : (Landroid/support/constraint/solver/widgets/ConstraintAnchor;IILandroid/support/constraint/solver/widgets/ConstraintAnchor$Strength;IZ)Z
    //   1606: pop
    //   1607: aload #16
    //   1609: getstatic android/support/constraint/solver/widgets/ConstraintAnchor$Type.TOP : Landroid/support/constraint/solver/widgets/ConstraintAnchor$Type;
    //   1612: invokevirtual getAnchor : (Landroid/support/constraint/solver/widgets/ConstraintAnchor$Type;)Landroid/support/constraint/solver/widgets/ConstraintAnchor;
    //   1615: invokevirtual reset : ()V
    //   1618: aload #16
    //   1620: getstatic android/support/constraint/solver/widgets/ConstraintAnchor$Type.BOTTOM : Landroid/support/constraint/solver/widgets/ConstraintAnchor$Type;
    //   1623: invokevirtual getAnchor : (Landroid/support/constraint/solver/widgets/ConstraintAnchor$Type;)Landroid/support/constraint/solver/widgets/ConstraintAnchor;
    //   1626: invokevirtual reset : ()V
    //   1629: fload_1
    //   1630: fconst_0
    //   1631: fcmpl
    //   1632: iflt -> 1649
    //   1635: fload_1
    //   1636: ldc_w 0.5
    //   1639: fcmpl
    //   1640: ifeq -> 1649
    //   1643: aload #16
    //   1645: fload_1
    //   1646: invokevirtual setHorizontalBiasPercent : (F)V
    //   1649: aload #15
    //   1651: getfield verticalBias : F
    //   1654: fconst_0
    //   1655: fcmpl
    //   1656: iflt -> 1681
    //   1659: aload #15
    //   1661: getfield verticalBias : F
    //   1664: ldc_w 0.5
    //   1667: fcmpl
    //   1668: ifeq -> 1681
    //   1671: aload #16
    //   1673: aload #15
    //   1675: getfield verticalBias : F
    //   1678: invokevirtual setVerticalBiasPercent : (F)V
    //   1681: iload #13
    //   1683: ifeq -> 1719
    //   1686: aload #14
    //   1688: getfield editorAbsoluteX : I
    //   1691: iconst_m1
    //   1692: if_icmpne -> 1704
    //   1695: aload #14
    //   1697: getfield editorAbsoluteY : I
    //   1700: iconst_m1
    //   1701: if_icmpeq -> 1719
    //   1704: aload #16
    //   1706: aload #14
    //   1708: getfield editorAbsoluteX : I
    //   1711: aload #14
    //   1713: getfield editorAbsoluteY : I
    //   1716: invokevirtual setOrigin : (II)V
    //   1719: aload #14
    //   1721: getfield horizontalDimensionFixed : Z
    //   1724: ifne -> 1796
    //   1727: aload #14
    //   1729: getfield width : I
    //   1732: iconst_m1
    //   1733: if_icmpne -> 1779
    //   1736: aload #16
    //   1738: getstatic android/support/constraint/solver/widgets/ConstraintWidget$DimensionBehaviour.MATCH_PARENT : Landroid/support/constraint/solver/widgets/ConstraintWidget$DimensionBehaviour;
    //   1741: invokevirtual setHorizontalDimensionBehaviour : (Landroid/support/constraint/solver/widgets/ConstraintWidget$DimensionBehaviour;)V
    //   1744: aload #16
    //   1746: getstatic android/support/constraint/solver/widgets/ConstraintAnchor$Type.LEFT : Landroid/support/constraint/solver/widgets/ConstraintAnchor$Type;
    //   1749: invokevirtual getAnchor : (Landroid/support/constraint/solver/widgets/ConstraintAnchor$Type;)Landroid/support/constraint/solver/widgets/ConstraintAnchor;
    //   1752: aload #14
    //   1754: getfield leftMargin : I
    //   1757: putfield mMargin : I
    //   1760: aload #16
    //   1762: getstatic android/support/constraint/solver/widgets/ConstraintAnchor$Type.RIGHT : Landroid/support/constraint/solver/widgets/ConstraintAnchor$Type;
    //   1765: invokevirtual getAnchor : (Landroid/support/constraint/solver/widgets/ConstraintAnchor$Type;)Landroid/support/constraint/solver/widgets/ConstraintAnchor;
    //   1768: aload #14
    //   1770: getfield rightMargin : I
    //   1773: putfield mMargin : I
    //   1776: goto -> 1814
    //   1779: aload #16
    //   1781: getstatic android/support/constraint/solver/widgets/ConstraintWidget$DimensionBehaviour.MATCH_CONSTRAINT : Landroid/support/constraint/solver/widgets/ConstraintWidget$DimensionBehaviour;
    //   1784: invokevirtual setHorizontalDimensionBehaviour : (Landroid/support/constraint/solver/widgets/ConstraintWidget$DimensionBehaviour;)V
    //   1787: aload #16
    //   1789: iconst_0
    //   1790: invokevirtual setWidth : (I)V
    //   1793: goto -> 1814
    //   1796: aload #16
    //   1798: getstatic android/support/constraint/solver/widgets/ConstraintWidget$DimensionBehaviour.FIXED : Landroid/support/constraint/solver/widgets/ConstraintWidget$DimensionBehaviour;
    //   1801: invokevirtual setHorizontalDimensionBehaviour : (Landroid/support/constraint/solver/widgets/ConstraintWidget$DimensionBehaviour;)V
    //   1804: aload #16
    //   1806: aload #14
    //   1808: getfield width : I
    //   1811: invokevirtual setWidth : (I)V
    //   1814: aload #14
    //   1816: getfield verticalDimensionFixed : Z
    //   1819: ifne -> 1891
    //   1822: aload #14
    //   1824: getfield height : I
    //   1827: iconst_m1
    //   1828: if_icmpne -> 1874
    //   1831: aload #16
    //   1833: getstatic android/support/constraint/solver/widgets/ConstraintWidget$DimensionBehaviour.MATCH_PARENT : Landroid/support/constraint/solver/widgets/ConstraintWidget$DimensionBehaviour;
    //   1836: invokevirtual setVerticalDimensionBehaviour : (Landroid/support/constraint/solver/widgets/ConstraintWidget$DimensionBehaviour;)V
    //   1839: aload #16
    //   1841: getstatic android/support/constraint/solver/widgets/ConstraintAnchor$Type.TOP : Landroid/support/constraint/solver/widgets/ConstraintAnchor$Type;
    //   1844: invokevirtual getAnchor : (Landroid/support/constraint/solver/widgets/ConstraintAnchor$Type;)Landroid/support/constraint/solver/widgets/ConstraintAnchor;
    //   1847: aload #14
    //   1849: getfield topMargin : I
    //   1852: putfield mMargin : I
    //   1855: aload #16
    //   1857: getstatic android/support/constraint/solver/widgets/ConstraintAnchor$Type.BOTTOM : Landroid/support/constraint/solver/widgets/ConstraintAnchor$Type;
    //   1860: invokevirtual getAnchor : (Landroid/support/constraint/solver/widgets/ConstraintAnchor$Type;)Landroid/support/constraint/solver/widgets/ConstraintAnchor;
    //   1863: aload #14
    //   1865: getfield bottomMargin : I
    //   1868: putfield mMargin : I
    //   1871: goto -> 1909
    //   1874: aload #16
    //   1876: getstatic android/support/constraint/solver/widgets/ConstraintWidget$DimensionBehaviour.MATCH_CONSTRAINT : Landroid/support/constraint/solver/widgets/ConstraintWidget$DimensionBehaviour;
    //   1879: invokevirtual setVerticalDimensionBehaviour : (Landroid/support/constraint/solver/widgets/ConstraintWidget$DimensionBehaviour;)V
    //   1882: aload #16
    //   1884: iconst_0
    //   1885: invokevirtual setHeight : (I)V
    //   1888: goto -> 1909
    //   1891: aload #16
    //   1893: getstatic android/support/constraint/solver/widgets/ConstraintWidget$DimensionBehaviour.FIXED : Landroid/support/constraint/solver/widgets/ConstraintWidget$DimensionBehaviour;
    //   1896: invokevirtual setVerticalDimensionBehaviour : (Landroid/support/constraint/solver/widgets/ConstraintWidget$DimensionBehaviour;)V
    //   1899: aload #16
    //   1901: aload #14
    //   1903: getfield height : I
    //   1906: invokevirtual setHeight : (I)V
    //   1909: aload #14
    //   1911: getfield dimensionRatio : Ljava/lang/String;
    //   1914: ifnull -> 1927
    //   1917: aload #16
    //   1919: aload #14
    //   1921: getfield dimensionRatio : Ljava/lang/String;
    //   1924: invokevirtual setDimensionRatio : (Ljava/lang/String;)V
    //   1927: aload #16
    //   1929: aload #14
    //   1931: getfield horizontalWeight : F
    //   1934: invokevirtual setHorizontalWeight : (F)V
    //   1937: aload #16
    //   1939: aload #14
    //   1941: getfield verticalWeight : F
    //   1944: invokevirtual setVerticalWeight : (F)V
    //   1947: aload #16
    //   1949: aload #14
    //   1951: getfield horizontalChainStyle : I
    //   1954: invokevirtual setHorizontalChainStyle : (I)V
    //   1957: aload #16
    //   1959: aload #14
    //   1961: getfield verticalChainStyle : I
    //   1964: invokevirtual setVerticalChainStyle : (I)V
    //   1967: aload #16
    //   1969: aload #14
    //   1971: getfield matchConstraintDefaultWidth : I
    //   1974: aload #14
    //   1976: getfield matchConstraintMinWidth : I
    //   1979: aload #14
    //   1981: getfield matchConstraintMaxWidth : I
    //   1984: aload #14
    //   1986: getfield matchConstraintPercentWidth : F
    //   1989: invokevirtual setHorizontalMatchStyle : (IIIF)V
    //   1992: aload #16
    //   1994: aload #14
    //   1996: getfield matchConstraintDefaultHeight : I
    //   1999: aload #14
    //   2001: getfield matchConstraintMinHeight : I
    //   2004: aload #14
    //   2006: getfield matchConstraintMaxHeight : I
    //   2009: aload #14
    //   2011: getfield matchConstraintPercentHeight : F
    //   2014: invokevirtual setVerticalMatchStyle : (IIIF)V
    //   2017: iload #10
    //   2019: iconst_1
    //   2020: iadd
    //   2021: istore #10
    //   2023: goto -> 325
    //   2026: return
    // Exception table:
    //   from	to	target	type
    //   32	69	105	android/content/res/Resources$NotFoundException
    //   78	88	105	android/content/res/Resources$NotFoundException
    //   88	102	105	android/content/res/Resources$NotFoundException
    //   393	453	456	android/content/res/Resources$NotFoundException }
  
  private void setSelfDimensionBehaviour(int paramInt1, int paramInt2) {
    int i1 = View.MeasureSpec.getMode(paramInt1);
    paramInt1 = View.MeasureSpec.getSize(paramInt1);
    int k = View.MeasureSpec.getMode(paramInt2);
    paramInt2 = View.MeasureSpec.getSize(paramInt2);
    int m = getPaddingTop();
    int n = getPaddingBottom();
    int i2 = getPaddingLeft();
    int i3 = getPaddingRight();
    ConstraintWidget.DimensionBehaviour dimensionBehaviour1 = ConstraintWidget.DimensionBehaviour.FIXED;
    ConstraintWidget.DimensionBehaviour dimensionBehaviour2 = ConstraintWidget.DimensionBehaviour.FIXED;
    int j = 0;
    int i = 0;
    getLayoutParams();
    if (i1 != Integer.MIN_VALUE) {
      if (i1 != 0) {
        if (i1 != 1073741824) {
          paramInt1 = j;
        } else {
          paramInt1 = Math.min(this.mMaxWidth, paramInt1) - i2 + i3;
        } 
      } else {
        dimensionBehaviour1 = ConstraintWidget.DimensionBehaviour.WRAP_CONTENT;
        paramInt1 = j;
      } 
    } else {
      dimensionBehaviour1 = ConstraintWidget.DimensionBehaviour.WRAP_CONTENT;
    } 
    if (k != Integer.MIN_VALUE) {
      if (k != 0) {
        if (k != 1073741824) {
          paramInt2 = i;
        } else {
          paramInt2 = Math.min(this.mMaxHeight, paramInt2) - m + n;
        } 
      } else {
        dimensionBehaviour2 = ConstraintWidget.DimensionBehaviour.WRAP_CONTENT;
        paramInt2 = i;
      } 
    } else {
      dimensionBehaviour2 = ConstraintWidget.DimensionBehaviour.WRAP_CONTENT;
    } 
    this.mLayoutWidget.setMinWidth(0);
    this.mLayoutWidget.setMinHeight(0);
    this.mLayoutWidget.setHorizontalDimensionBehaviour(dimensionBehaviour1);
    this.mLayoutWidget.setWidth(paramInt1);
    this.mLayoutWidget.setVerticalDimensionBehaviour(dimensionBehaviour2);
    this.mLayoutWidget.setHeight(paramInt2);
    this.mLayoutWidget.setMinWidth(this.mMinWidth - getPaddingLeft() - getPaddingRight());
    this.mLayoutWidget.setMinHeight(this.mMinHeight - getPaddingTop() - getPaddingBottom());
  }
  
  private void updateHierarchy() {
    boolean bool1;
    int i = getChildCount();
    boolean bool2 = false;
    byte b = 0;
    while (true) {
      bool1 = bool2;
      if (b < i) {
        if (getChildAt(b).isLayoutRequested()) {
          bool1 = true;
          break;
        } 
        b++;
        continue;
      } 
      break;
    } 
    if (bool1) {
      this.mVariableDimensionsWidgets.clear();
      setChildrenConstraints();
    } 
  }
  
  private void updatePostMeasures() {
    int i = getChildCount();
    byte b;
    for (b = 0; b < i; b++) {
      View view = getChildAt(b);
      if (view instanceof Placeholder)
        ((Placeholder)view).updatePostMeasure(this); 
    } 
    i = this.mConstraintHelpers.size();
    if (i > 0)
      for (b = 0; b < i; b++)
        ((ConstraintHelper)this.mConstraintHelpers.get(b)).updatePostMeasure(this);  
  }
  
  public void addView(View paramView, int paramInt, ViewGroup.LayoutParams paramLayoutParams) {
    super.addView(paramView, paramInt, paramLayoutParams);
    if (Build.VERSION.SDK_INT < 14)
      onViewAdded(paramView); 
  }
  
  protected boolean checkLayoutParams(ViewGroup.LayoutParams paramLayoutParams) { return paramLayoutParams instanceof LayoutParams; }
  
  public void dispatchDraw(Canvas paramCanvas) {
    super.dispatchDraw(paramCanvas);
    if (isInEditMode()) {
      int i = getChildCount();
      float f3 = getWidth();
      float f2 = getHeight();
      float f1 = 1080.0F;
      byte b;
      for (b = 0; b < i; b++) {
        View view = getChildAt(b);
        if (view.getVisibility() != 8) {
          Object object = view.getTag();
          if (object != null && object instanceof String) {
            String[] arrayOfString = ((String)object).split(",");
            if (arrayOfString.length == 4) {
              int k = Integer.parseInt(arrayOfString[0]);
              int n = Integer.parseInt(arrayOfString[1]);
              int m = Integer.parseInt(arrayOfString[2]);
              int j = Integer.parseInt(arrayOfString[3]);
              k = (int)(k / f1 * f3);
              n = (int)(n / 1920.0F * f2);
              m = (int)(m / f1 * f3);
              j = (int)(j / 1920.0F * f2);
              Paint paint = new Paint();
              paint.setColor(-65536);
              paramCanvas.drawLine(k, n, (k + m), n, paint);
              paramCanvas.drawLine((k + m), n, (k + m), (n + j), paint);
              paramCanvas.drawLine((k + m), (n + j), k, (n + j), paint);
              paramCanvas.drawLine(k, (n + j), k, n, paint);
              paint.setColor(-16711936);
              paramCanvas.drawLine(k, n, (k + m), (n + j), paint);
              paramCanvas.drawLine(k, (n + j), (k + m), n, paint);
            } 
          } 
        } 
      } 
      return;
    } 
  }
  
  public void fillMetrics(Metrics paramMetrics) {
    this.mMetrics = paramMetrics;
    this.mLayoutWidget.fillMetrics(paramMetrics);
  }
  
  protected LayoutParams generateDefaultLayoutParams() { return new LayoutParams(-2, -2); }
  
  public LayoutParams generateLayoutParams(AttributeSet paramAttributeSet) { return new LayoutParams(getContext(), paramAttributeSet); }
  
  protected ViewGroup.LayoutParams generateLayoutParams(ViewGroup.LayoutParams paramLayoutParams) { return new LayoutParams(paramLayoutParams); }
  
  public Object getDesignInformation(int paramInt, Object paramObject) {
    if (paramInt == 0 && paramObject instanceof String) {
      paramObject = (String)paramObject;
      HashMap hashMap = this.mDesignIds;
      if (hashMap != null && hashMap.containsKey(paramObject))
        return this.mDesignIds.get(paramObject); 
    } 
    return null;
  }
  
  public int getMaxHeight() { return this.mMaxHeight; }
  
  public int getMaxWidth() { return this.mMaxWidth; }
  
  public int getMinHeight() { return this.mMinHeight; }
  
  public int getMinWidth() { return this.mMinWidth; }
  
  public int getOptimizationLevel() { return this.mLayoutWidget.getOptimizationLevel(); }
  
  public View getViewById(int paramInt) { return (View)this.mChildrenByIds.get(paramInt); }
  
  public final ConstraintWidget getViewWidget(View paramView) { return (paramView == this) ? this.mLayoutWidget : ((paramView == null) ? null : ((LayoutParams)paramView.getLayoutParams()).widget); }
  
  protected void onLayout(boolean paramBoolean, int paramInt1, int paramInt2, int paramInt3, int paramInt4) {
    paramInt2 = getChildCount();
    paramBoolean = isInEditMode();
    for (paramInt1 = 0; paramInt1 < paramInt2; paramInt1++) {
      View view = getChildAt(paramInt1);
      LayoutParams layoutParams = (LayoutParams)view.getLayoutParams();
      ConstraintWidget constraintWidget = layoutParams.widget;
      if ((view.getVisibility() != 8 || layoutParams.isGuideline || layoutParams.isHelper || paramBoolean) && !layoutParams.isInPlaceholder) {
        paramInt3 = constraintWidget.getDrawX();
        paramInt4 = constraintWidget.getDrawY();
        int i = constraintWidget.getWidth() + paramInt3;
        int j = constraintWidget.getHeight() + paramInt4;
        view.layout(paramInt3, paramInt4, i, j);
        if (view instanceof Placeholder) {
          view = ((Placeholder)view).getContent();
          if (view != null) {
            view.setVisibility(0);
            view.layout(paramInt3, paramInt4, i, j);
          } 
        } 
      } 
    } 
    paramInt2 = this.mConstraintHelpers.size();
    if (paramInt2 > 0)
      for (paramInt1 = 0; paramInt1 < paramInt2; paramInt1++)
        ((ConstraintHelper)this.mConstraintHelpers.get(paramInt1)).updatePostLayout(this);  
  }
  
  protected void onMeasure(int paramInt1, int paramInt2) {
    boolean bool;
    System.currentTimeMillis();
    int k = View.MeasureSpec.getMode(paramInt1);
    int m = View.MeasureSpec.getSize(paramInt1);
    int i1 = View.MeasureSpec.getMode(paramInt2);
    int i2 = View.MeasureSpec.getSize(paramInt2);
    int j = getPaddingLeft();
    int i5 = getPaddingTop();
    this.mLayoutWidget.setX(j);
    this.mLayoutWidget.setY(i5);
    this.mLayoutWidget.setMaxWidth(this.mMaxWidth);
    this.mLayoutWidget.setMaxHeight(this.mMaxHeight);
    if (Build.VERSION.SDK_INT >= 17) {
      boolean bool1;
      ConstraintWidgetContainer constraintWidgetContainer = this.mLayoutWidget;
      if (getLayoutDirection() == 1) {
        bool1 = true;
      } else {
        bool1 = false;
      } 
      constraintWidgetContainer.setRtl(bool1);
    } 
    setSelfDimensionBehaviour(paramInt1, paramInt2);
    int n = this.mLayoutWidget.getWidth();
    int i4 = this.mLayoutWidget.getHeight();
    int i = 0;
    if (this.mDirtyHierarchy) {
      this.mDirtyHierarchy = false;
      updateHierarchy();
      i = 1;
    } 
    if ((this.mOptimizationLevel & 0x8) == 8) {
      bool = true;
    } else {
      bool = false;
    } 
    if (bool) {
      this.mLayoutWidget.preOptimize();
      this.mLayoutWidget.optimizeForDimensions(n, i4);
      internalMeasureDimensions(paramInt1, paramInt2);
    } else {
      internalMeasureChildren(paramInt1, paramInt2);
    } 
    updatePostMeasures();
    if (getChildCount() > 0 && i)
      Analyzer.determineGroups(this.mLayoutWidget); 
    if (this.mLayoutWidget.mGroupsWrapOptimized) {
      if (this.mLayoutWidget.mHorizontalWrapOptimized && k == Integer.MIN_VALUE) {
        if (this.mLayoutWidget.mWrapFixedWidth < m) {
          ConstraintWidgetContainer constraintWidgetContainer = this.mLayoutWidget;
          constraintWidgetContainer.setWidth(constraintWidgetContainer.mWrapFixedWidth);
        } 
        this.mLayoutWidget.setHorizontalDimensionBehaviour(ConstraintWidget.DimensionBehaviour.FIXED);
      } 
      if (this.mLayoutWidget.mVerticalWrapOptimized && i1 == Integer.MIN_VALUE) {
        if (this.mLayoutWidget.mWrapFixedHeight < i2) {
          ConstraintWidgetContainer constraintWidgetContainer = this.mLayoutWidget;
          constraintWidgetContainer.setHeight(constraintWidgetContainer.mWrapFixedHeight);
        } 
        this.mLayoutWidget.setVerticalDimensionBehaviour(ConstraintWidget.DimensionBehaviour.FIXED);
      } 
    } 
    i = this.mOptimizationLevel;
    int i3 = 0;
    if ((i & 0x20) == 32) {
      i = this.mLayoutWidget.getWidth();
      int i9 = this.mLayoutWidget.getHeight();
      if (this.mLastMeasureWidth != i && k == 1073741824)
        Analyzer.setPosition(this.mLayoutWidget.mWidgetGroups, 0, i); 
      if (this.mLastMeasureHeight != i9 && i1 == 1073741824)
        Analyzer.setPosition(this.mLayoutWidget.mWidgetGroups, 1, i9); 
      if (this.mLayoutWidget.mHorizontalWrapOptimized && this.mLayoutWidget.mWrapFixedWidth > m)
        Analyzer.setPosition(this.mLayoutWidget.mWidgetGroups, 0, m); 
      if (this.mLayoutWidget.mVerticalWrapOptimized && this.mLayoutWidget.mWrapFixedHeight > i2)
        Analyzer.setPosition(this.mLayoutWidget.mWidgetGroups, 1, i2); 
    } 
    int i6 = 0;
    if (getChildCount() > 0)
      solveLinearSystem("First pass"); 
    i1 = this.mVariableDimensionsWidgets.size();
    int i7 = getPaddingBottom() + i5;
    int i8 = j + getPaddingRight();
    if (i1 > 0) {
      boolean bool1;
      i2 = 0;
      if (this.mLayoutWidget.getHorizontalDimensionBehaviour() == ConstraintWidget.DimensionBehaviour.WRAP_CONTENT) {
        k = 1;
      } else {
        k = 0;
      } 
      if (this.mLayoutWidget.getVerticalDimensionBehaviour() == ConstraintWidget.DimensionBehaviour.WRAP_CONTENT) {
        bool1 = true;
      } else {
        bool1 = false;
      } 
      j = Math.max(this.mLayoutWidget.getWidth(), this.mMinWidth);
      i = Math.max(this.mLayoutWidget.getHeight(), this.mMinHeight);
      byte b = 0;
      m = 0;
      while (b < i1) {
        int i12;
        int i11;
        int i10;
        int i9;
        ConstraintWidget constraintWidget = (ConstraintWidget)this.mVariableDimensionsWidgets.get(b);
        View view = (View)constraintWidget.getCompanionWidget();
        if (view == null) {
          i9 = j;
          i10 = i;
          i11 = m;
          i12 = i2;
        } else {
          LayoutParams layoutParams = (LayoutParams)view.getLayoutParams();
          if (!layoutParams.isHelper) {
            if (layoutParams.isGuideline) {
              i9 = j;
              i10 = i;
              i11 = m;
              i12 = i2;
            } else if (view.getVisibility() == 8) {
              i9 = j;
              i10 = i;
              i11 = m;
              i12 = i2;
            } else if (bool && constraintWidget.getResolutionWidth().isResolved() && constraintWidget.getResolutionHeight().isResolved()) {
              i9 = j;
              i10 = i;
              i11 = m;
              i12 = i2;
            } else {
              if (layoutParams.width == -2 && layoutParams.horizontalDimensionFixed) {
                i9 = getChildMeasureSpec(paramInt1, i8, layoutParams.width);
              } else {
                i9 = View.MeasureSpec.makeMeasureSpec(constraintWidget.getWidth(), 1073741824);
              } 
              if (layoutParams.height == -2 && layoutParams.verticalDimensionFixed) {
                i10 = getChildMeasureSpec(paramInt2, i7, layoutParams.height);
              } else {
                i10 = View.MeasureSpec.makeMeasureSpec(constraintWidget.getHeight(), 1073741824);
              } 
              view.measure(i9, i10);
              Metrics metrics = this.mMetrics;
              if (metrics != null)
                metrics.additionalMeasures++; 
              int i13 = i3 + true;
              i3 = view.getMeasuredWidth();
              i9 = view.getMeasuredHeight();
              if (i3 != constraintWidget.getWidth()) {
                constraintWidget.setWidth(i3);
                if (bool)
                  constraintWidget.getResolutionWidth().resolve(i3); 
                if (k != 0 && constraintWidget.getRight() > j)
                  j = Math.max(j, constraintWidget.getRight() + constraintWidget.getAnchor(ConstraintAnchor.Type.RIGHT).getMargin()); 
                i3 = 1;
              } else {
                i3 = i2;
              } 
              i2 = i;
              if (i9 != constraintWidget.getHeight()) {
                constraintWidget.setHeight(i9);
                if (bool)
                  constraintWidget.getResolutionHeight().resolve(i9); 
                i2 = i;
                if (bool1) {
                  i2 = i;
                  if (constraintWidget.getBottom() > i)
                    i2 = Math.max(i, constraintWidget.getBottom() + constraintWidget.getAnchor(ConstraintAnchor.Type.BOTTOM).getMargin()); 
                } 
                i3 = 1;
              } 
              i = i3;
              if (layoutParams.needsBaseline) {
                i9 = view.getBaseline();
                i = i3;
                if (i9 != -1) {
                  i = i3;
                  if (i9 != constraintWidget.getBaselineDistance()) {
                    constraintWidget.setBaselineDistance(i9);
                    i = 1;
                  } 
                } 
              } 
              i9 = j;
              i10 = i2;
              i11 = m;
              i3 = i13;
              i12 = i;
              if (Build.VERSION.SDK_INT >= 11) {
                i11 = combineMeasuredStates(m, view.getMeasuredState());
                i9 = j;
                i10 = i2;
                i3 = i13;
                i12 = i;
              } 
            } 
          } else {
            i12 = i2;
            i11 = m;
            i10 = i;
            i9 = j;
          } 
        } 
        b++;
        j = i9;
        i = i10;
        m = i11;
        i2 = i12;
      } 
      i3 = i1;
      if (i2 != 0) {
        this.mLayoutWidget.setWidth(n);
        this.mLayoutWidget.setHeight(i4);
        if (bool)
          this.mLayoutWidget.solveGraph(); 
        solveLinearSystem("2nd pass");
        i1 = 0;
        if (this.mLayoutWidget.getWidth() < j) {
          this.mLayoutWidget.setWidth(j);
          i1 = 1;
        } 
        if (this.mLayoutWidget.getHeight() < i) {
          this.mLayoutWidget.setHeight(i);
          i1 = 1;
        } 
        if (i1 != 0)
          solveLinearSystem("3rd pass"); 
      } 
      i = n;
      j = 0;
      n = i6;
      while (j < i3) {
        ConstraintWidget constraintWidget = (ConstraintWidget)this.mVariableDimensionsWidgets.get(j);
        View view = (View)constraintWidget.getCompanionWidget();
        if (view != null && (view.getMeasuredWidth() != constraintWidget.getWidth() || view.getMeasuredHeight() != constraintWidget.getHeight()) && constraintWidget.getVisibility() != 8) {
          view.measure(View.MeasureSpec.makeMeasureSpec(constraintWidget.getWidth(), 1073741824), View.MeasureSpec.makeMeasureSpec(constraintWidget.getHeight(), 1073741824));
          Metrics metrics = this.mMetrics;
          if (metrics != null)
            metrics.additionalMeasures++; 
          n++;
        } 
        j++;
      } 
    } else {
      m = 0;
    } 
    i = this.mLayoutWidget.getWidth() + i8;
    j = this.mLayoutWidget.getHeight() + i7;
    if (Build.VERSION.SDK_INT >= 11) {
      paramInt1 = resolveSizeAndState(i, paramInt1, m);
      i = resolveSizeAndState(j, paramInt2, m << 16);
      paramInt2 = Math.min(this.mMaxWidth, paramInt1 & 0xFFFFFF);
      i = Math.min(this.mMaxHeight, i & 0xFFFFFF);
      paramInt1 = paramInt2;
      if (this.mLayoutWidget.isWidthMeasuredTooSmall())
        paramInt1 = paramInt2 | 0x1000000; 
      paramInt2 = i;
      if (this.mLayoutWidget.isHeightMeasuredTooSmall())
        paramInt2 = i | 0x1000000; 
      setMeasuredDimension(paramInt1, paramInt2);
      this.mLastMeasureWidth = paramInt1;
      this.mLastMeasureHeight = paramInt2;
      return;
    } 
    setMeasuredDimension(i, j);
    this.mLastMeasureWidth = i;
    this.mLastMeasureHeight = j;
  }
  
  public void onViewAdded(View paramView) {
    if (Build.VERSION.SDK_INT >= 14)
      super.onViewAdded(paramView); 
    ConstraintWidget constraintWidget = getViewWidget(paramView);
    if (paramView instanceof Guideline && !(constraintWidget instanceof Guideline)) {
      LayoutParams layoutParams = (LayoutParams)paramView.getLayoutParams();
      layoutParams.widget = new Guideline();
      layoutParams.isGuideline = true;
      ((Guideline)layoutParams.widget).setOrientation(layoutParams.orientation);
    } 
    if (paramView instanceof ConstraintHelper) {
      ConstraintHelper constraintHelper = (ConstraintHelper)paramView;
      constraintHelper.validateParams();
      ((LayoutParams)paramView.getLayoutParams()).isHelper = true;
      if (!this.mConstraintHelpers.contains(constraintHelper))
        this.mConstraintHelpers.add(constraintHelper); 
    } 
    this.mChildrenByIds.put(paramView.getId(), paramView);
    this.mDirtyHierarchy = true;
  }
  
  public void onViewRemoved(View paramView) {
    if (Build.VERSION.SDK_INT >= 14)
      super.onViewRemoved(paramView); 
    this.mChildrenByIds.remove(paramView.getId());
    ConstraintWidget constraintWidget = getViewWidget(paramView);
    this.mLayoutWidget.remove(constraintWidget);
    this.mConstraintHelpers.remove(paramView);
    this.mVariableDimensionsWidgets.remove(constraintWidget);
    this.mDirtyHierarchy = true;
  }
  
  public void removeView(View paramView) {
    super.removeView(paramView);
    if (Build.VERSION.SDK_INT < 14)
      onViewRemoved(paramView); 
  }
  
  public void requestLayout() {
    super.requestLayout();
    this.mDirtyHierarchy = true;
    this.mLastMeasureWidth = -1;
    this.mLastMeasureHeight = -1;
    this.mLastMeasureWidthSize = -1;
    this.mLastMeasureHeightSize = -1;
    this.mLastMeasureWidthMode = 0;
    this.mLastMeasureHeightMode = 0;
  }
  
  public void setConstraintSet(ConstraintSet paramConstraintSet) { this.mConstraintSet = paramConstraintSet; }
  
  public void setDesignInformation(int paramInt, Object paramObject1, Object paramObject2) {
    if (paramInt == 0 && paramObject1 instanceof String && paramObject2 instanceof Integer) {
      if (this.mDesignIds == null)
        this.mDesignIds = new HashMap(); 
      String str = (String)paramObject1;
      paramInt = str.indexOf("/");
      paramObject1 = str;
      if (paramInt != -1)
        paramObject1 = str.substring(paramInt + 1); 
      paramInt = ((Integer)paramObject2).intValue();
      this.mDesignIds.put(paramObject1, Integer.valueOf(paramInt));
    } 
  }
  
  public void setId(int paramInt) {
    this.mChildrenByIds.remove(getId());
    super.setId(paramInt);
    this.mChildrenByIds.put(getId(), this);
  }
  
  public void setMaxHeight(int paramInt) {
    if (paramInt == this.mMaxHeight)
      return; 
    this.mMaxHeight = paramInt;
    requestLayout();
  }
  
  public void setMaxWidth(int paramInt) {
    if (paramInt == this.mMaxWidth)
      return; 
    this.mMaxWidth = paramInt;
    requestLayout();
  }
  
  public void setMinHeight(int paramInt) {
    if (paramInt == this.mMinHeight)
      return; 
    this.mMinHeight = paramInt;
    requestLayout();
  }
  
  public void setMinWidth(int paramInt) {
    if (paramInt == this.mMinWidth)
      return; 
    this.mMinWidth = paramInt;
    requestLayout();
  }
  
  public void setOptimizationLevel(int paramInt) { this.mLayoutWidget.setOptimizationLevel(paramInt); }
  
  public boolean shouldDelayChildPressedState() { return false; }
  
  protected void solveLinearSystem(String paramString) {
    this.mLayoutWidget.layout();
    Metrics metrics = this.mMetrics;
    if (metrics != null)
      metrics.resolutions++; 
  }
  
  public static class LayoutParams extends ViewGroup.MarginLayoutParams {
    public static final int BASELINE = 5;
    
    public static final int BOTTOM = 4;
    
    public static final int CHAIN_PACKED = 2;
    
    public static final int CHAIN_SPREAD = 0;
    
    public static final int CHAIN_SPREAD_INSIDE = 1;
    
    public static final int END = 7;
    
    public static final int HORIZONTAL = 0;
    
    public static final int LEFT = 1;
    
    public static final int MATCH_CONSTRAINT = 0;
    
    public static final int MATCH_CONSTRAINT_PERCENT = 2;
    
    public static final int MATCH_CONSTRAINT_SPREAD = 0;
    
    public static final int MATCH_CONSTRAINT_WRAP = 1;
    
    public static final int PARENT_ID = 0;
    
    public static final int RIGHT = 2;
    
    public static final int START = 6;
    
    public static final int TOP = 3;
    
    public static final int UNSET = -1;
    
    public static final int VERTICAL = 1;
    
    public int baselineToBaseline = -1;
    
    public int bottomToBottom = -1;
    
    public int bottomToTop = -1;
    
    public float circleAngle = 0.0F;
    
    public int circleConstraint = -1;
    
    public int circleRadius = 0;
    
    public boolean constrainedHeight = false;
    
    public boolean constrainedWidth = false;
    
    public String dimensionRatio = null;
    
    int dimensionRatioSide = 1;
    
    float dimensionRatioValue = 0.0F;
    
    public int editorAbsoluteX = -1;
    
    public int editorAbsoluteY = -1;
    
    public int endToEnd = -1;
    
    public int endToStart = -1;
    
    public int goneBottomMargin = -1;
    
    public int goneEndMargin = -1;
    
    public int goneLeftMargin = -1;
    
    public int goneRightMargin = -1;
    
    public int goneStartMargin = -1;
    
    public int goneTopMargin = -1;
    
    public int guideBegin = -1;
    
    public int guideEnd = -1;
    
    public float guidePercent = -1.0F;
    
    public boolean helped = false;
    
    public float horizontalBias = 0.5F;
    
    public int horizontalChainStyle = 0;
    
    boolean horizontalDimensionFixed = true;
    
    public float horizontalWeight = -1.0F;
    
    boolean isGuideline = false;
    
    boolean isHelper = false;
    
    boolean isInPlaceholder = false;
    
    public int leftToLeft = -1;
    
    public int leftToRight = -1;
    
    public int matchConstraintDefaultHeight = 0;
    
    public int matchConstraintDefaultWidth = 0;
    
    public int matchConstraintMaxHeight = 0;
    
    public int matchConstraintMaxWidth = 0;
    
    public int matchConstraintMinHeight = 0;
    
    public int matchConstraintMinWidth = 0;
    
    public float matchConstraintPercentHeight = 1.0F;
    
    public float matchConstraintPercentWidth = 1.0F;
    
    boolean needsBaseline = false;
    
    public int orientation = -1;
    
    int resolveGoneLeftMargin = -1;
    
    int resolveGoneRightMargin = -1;
    
    int resolvedGuideBegin;
    
    int resolvedGuideEnd;
    
    float resolvedGuidePercent;
    
    float resolvedHorizontalBias = 0.5F;
    
    int resolvedLeftToLeft = -1;
    
    int resolvedLeftToRight = -1;
    
    int resolvedRightToLeft = -1;
    
    int resolvedRightToRight = -1;
    
    public int rightToLeft = -1;
    
    public int rightToRight = -1;
    
    public int startToEnd = -1;
    
    public int startToStart = -1;
    
    public int topToBottom = -1;
    
    public int topToTop = -1;
    
    public float verticalBias = 0.5F;
    
    public int verticalChainStyle = 0;
    
    boolean verticalDimensionFixed = true;
    
    public float verticalWeight = -1.0F;
    
    ConstraintWidget widget = new ConstraintWidget();
    
    public LayoutParams(int param1Int1, int param1Int2) { super(param1Int1, param1Int2); }
    
    public LayoutParams(Context param1Context, AttributeSet param1AttributeSet) {
      super(param1Context, param1AttributeSet);
      TypedArray typedArray = param1Context.obtainStyledAttributes(param1AttributeSet, R.styleable.ConstraintLayout_Layout);
      int i = typedArray.getIndexCount();
      byte b;
      for (b = 0; b < i; b++) {
        float f;
        int j = typedArray.getIndex(b);
        switch (Table.map.get(j)) {
          case 50:
            this.editorAbsoluteY = typedArray.getDimensionPixelOffset(j, this.editorAbsoluteY);
            break;
          case 49:
            this.editorAbsoluteX = typedArray.getDimensionPixelOffset(j, this.editorAbsoluteX);
            break;
          case 48:
            this.verticalChainStyle = typedArray.getInt(j, 0);
            break;
          case 47:
            this.horizontalChainStyle = typedArray.getInt(j, 0);
            break;
          case 46:
            this.verticalWeight = typedArray.getFloat(j, this.verticalWeight);
            break;
          case 45:
            this.horizontalWeight = typedArray.getFloat(j, this.horizontalWeight);
            break;
          case 44:
            this.dimensionRatio = typedArray.getString(j);
            this.dimensionRatioValue = NaNF;
            this.dimensionRatioSide = -1;
            str = this.dimensionRatio;
            if (str != null) {
              int k = str.length();
              j = this.dimensionRatio.indexOf(',');
              if (j > 0 && j < k - 1) {
                str = this.dimensionRatio.substring(0, j);
                if (str.equalsIgnoreCase("W")) {
                  this.dimensionRatioSide = 0;
                } else if (str.equalsIgnoreCase("H")) {
                  this.dimensionRatioSide = 1;
                } 
                j++;
              } else {
                j = 0;
              } 
              int m = this.dimensionRatio.indexOf(':');
              if (m >= 0 && m < k - 1) {
                str = this.dimensionRatio.substring(j, m);
                String str1 = this.dimensionRatio.substring(m + 1);
                if (str.length() > 0 && str1.length() > 0)
                  try {
                    float f1 = Float.parseFloat(str);
                    float f2 = Float.parseFloat(str1);
                    if (f1 > 0.0F && f2 > 0.0F) {
                      if (this.dimensionRatioSide == 1) {
                        this.dimensionRatioValue = Math.abs(f2 / f1);
                        break;
                      } 
                      this.dimensionRatioValue = Math.abs(f1 / f2);
                    } 
                  } catch (NumberFormatException str) {} 
                break;
              } 
              str = this.dimensionRatio.substring(j);
              if (str.length() > 0)
                try {
                  this.dimensionRatioValue = Float.parseFloat(str);
                } catch (NumberFormatException str) {} 
            } 
            break;
          case 38:
            this.matchConstraintPercentHeight = Math.max(0.0F, typedArray.getFloat(j, this.matchConstraintPercentHeight));
            break;
          case 37:
            try {
              this.matchConstraintMaxHeight = typedArray.getDimensionPixelSize(j, this.matchConstraintMaxHeight);
            } catch (Exception str) {}
            break;
          case 36:
            try {
              this.matchConstraintMinHeight = typedArray.getDimensionPixelSize(j, this.matchConstraintMinHeight);
            } catch (Exception str) {}
            break;
          case 35:
            this.matchConstraintPercentWidth = Math.max(0.0F, typedArray.getFloat(j, this.matchConstraintPercentWidth));
            break;
          case 34:
            try {
              this.matchConstraintMaxWidth = typedArray.getDimensionPixelSize(j, this.matchConstraintMaxWidth);
            } catch (Exception str) {}
            break;
          case 33:
            try {
              this.matchConstraintMinWidth = typedArray.getDimensionPixelSize(j, this.matchConstraintMinWidth);
            } catch (Exception str) {}
            break;
          case 32:
            this.matchConstraintDefaultHeight = typedArray.getInt(j, 0);
            if (this.matchConstraintDefaultHeight == 1)
              Log.e("ConstraintLayout", "layout_constraintHeight_default=\"wrap\" is deprecated.\nUse layout_height=\"WRAP_CONTENT\" and layout_constrainedHeight=\"true\" instead."); 
            break;
          case 31:
            this.matchConstraintDefaultWidth = typedArray.getInt(j, 0);
            if (this.matchConstraintDefaultWidth == 1)
              Log.e("ConstraintLayout", "layout_constraintWidth_default=\"wrap\" is deprecated.\nUse layout_width=\"WRAP_CONTENT\" and layout_constrainedWidth=\"true\" instead."); 
            break;
          case 30:
            this.verticalBias = typedArray.getFloat(j, this.verticalBias);
            break;
          case 29:
            this.horizontalBias = typedArray.getFloat(j, this.horizontalBias);
            break;
          case 28:
            this.constrainedHeight = typedArray.getBoolean(j, this.constrainedHeight);
            break;
          case 27:
            this.constrainedWidth = typedArray.getBoolean(j, this.constrainedWidth);
            break;
          case 26:
            this.goneEndMargin = typedArray.getDimensionPixelSize(j, this.goneEndMargin);
            break;
          case 25:
            this.goneStartMargin = typedArray.getDimensionPixelSize(j, this.goneStartMargin);
            break;
          case 24:
            this.goneBottomMargin = typedArray.getDimensionPixelSize(j, this.goneBottomMargin);
            break;
          case 23:
            this.goneRightMargin = typedArray.getDimensionPixelSize(j, this.goneRightMargin);
            break;
          case 22:
            this.goneTopMargin = typedArray.getDimensionPixelSize(j, this.goneTopMargin);
            break;
          case 21:
            this.goneLeftMargin = typedArray.getDimensionPixelSize(j, this.goneLeftMargin);
            break;
          case 20:
            this.endToEnd = typedArray.getResourceId(j, this.endToEnd);
            if (this.endToEnd == -1)
              this.endToEnd = typedArray.getInt(j, -1); 
            break;
          case 19:
            this.endToStart = typedArray.getResourceId(j, this.endToStart);
            if (this.endToStart == -1)
              this.endToStart = typedArray.getInt(j, -1); 
            break;
          case 18:
            this.startToStart = typedArray.getResourceId(j, this.startToStart);
            if (this.startToStart == -1)
              this.startToStart = typedArray.getInt(j, -1); 
            break;
          case 17:
            this.startToEnd = typedArray.getResourceId(j, this.startToEnd);
            if (this.startToEnd == -1)
              this.startToEnd = typedArray.getInt(j, -1); 
            break;
          case 16:
            this.baselineToBaseline = typedArray.getResourceId(j, this.baselineToBaseline);
            if (this.baselineToBaseline == -1)
              this.baselineToBaseline = typedArray.getInt(j, -1); 
            break;
          case 15:
            this.bottomToBottom = typedArray.getResourceId(j, this.bottomToBottom);
            if (this.bottomToBottom == -1)
              this.bottomToBottom = typedArray.getInt(j, -1); 
            break;
          case 14:
            this.bottomToTop = typedArray.getResourceId(j, this.bottomToTop);
            if (this.bottomToTop == -1)
              this.bottomToTop = typedArray.getInt(j, -1); 
            break;
          case 13:
            this.topToBottom = typedArray.getResourceId(j, this.topToBottom);
            if (this.topToBottom == -1)
              this.topToBottom = typedArray.getInt(j, -1); 
            break;
          case 12:
            this.topToTop = typedArray.getResourceId(j, this.topToTop);
            if (this.topToTop == -1)
              this.topToTop = typedArray.getInt(j, -1); 
            break;
          case 11:
            this.rightToRight = typedArray.getResourceId(j, this.rightToRight);
            if (this.rightToRight == -1)
              this.rightToRight = typedArray.getInt(j, -1); 
            break;
          case 10:
            this.rightToLeft = typedArray.getResourceId(j, this.rightToLeft);
            if (this.rightToLeft == -1)
              this.rightToLeft = typedArray.getInt(j, -1); 
            break;
          case 9:
            this.leftToRight = typedArray.getResourceId(j, this.leftToRight);
            if (this.leftToRight == -1)
              this.leftToRight = typedArray.getInt(j, -1); 
            break;
          case 8:
            this.leftToLeft = typedArray.getResourceId(j, this.leftToLeft);
            if (this.leftToLeft == -1)
              this.leftToLeft = typedArray.getInt(j, -1); 
            break;
          case 7:
            this.guidePercent = typedArray.getFloat(j, this.guidePercent);
            break;
          case 6:
            this.guideEnd = typedArray.getDimensionPixelOffset(j, this.guideEnd);
            break;
          case 5:
            this.guideBegin = typedArray.getDimensionPixelOffset(j, this.guideBegin);
            break;
          case 4:
            this.circleAngle = typedArray.getFloat(j, this.circleAngle) % 360.0F;
            f = this.circleAngle;
            if (f < 0.0F)
              this.circleAngle = (360.0F - f) % 360.0F; 
            break;
          case 3:
            this.circleRadius = typedArray.getDimensionPixelSize(j, this.circleRadius);
            break;
          case 2:
            this.circleConstraint = typedArray.getResourceId(j, this.circleConstraint);
            if (this.circleConstraint == -1)
              this.circleConstraint = typedArray.getInt(j, -1); 
            break;
          case 1:
            this.orientation = typedArray.getInt(j, this.orientation);
            break;
        } 
      } 
      typedArray.recycle();
      validate();
    }
    
    public LayoutParams(LayoutParams param1LayoutParams) {
      super(param1LayoutParams);
      this.guideBegin = param1LayoutParams.guideBegin;
      this.guideEnd = param1LayoutParams.guideEnd;
      this.guidePercent = param1LayoutParams.guidePercent;
      this.leftToLeft = param1LayoutParams.leftToLeft;
      this.leftToRight = param1LayoutParams.leftToRight;
      this.rightToLeft = param1LayoutParams.rightToLeft;
      this.rightToRight = param1LayoutParams.rightToRight;
      this.topToTop = param1LayoutParams.topToTop;
      this.topToBottom = param1LayoutParams.topToBottom;
      this.bottomToTop = param1LayoutParams.bottomToTop;
      this.bottomToBottom = param1LayoutParams.bottomToBottom;
      this.baselineToBaseline = param1LayoutParams.baselineToBaseline;
      this.circleConstraint = param1LayoutParams.circleConstraint;
      this.circleRadius = param1LayoutParams.circleRadius;
      this.circleAngle = param1LayoutParams.circleAngle;
      this.startToEnd = param1LayoutParams.startToEnd;
      this.startToStart = param1LayoutParams.startToStart;
      this.endToStart = param1LayoutParams.endToStart;
      this.endToEnd = param1LayoutParams.endToEnd;
      this.goneLeftMargin = param1LayoutParams.goneLeftMargin;
      this.goneTopMargin = param1LayoutParams.goneTopMargin;
      this.goneRightMargin = param1LayoutParams.goneRightMargin;
      this.goneBottomMargin = param1LayoutParams.goneBottomMargin;
      this.goneStartMargin = param1LayoutParams.goneStartMargin;
      this.goneEndMargin = param1LayoutParams.goneEndMargin;
      this.horizontalBias = param1LayoutParams.horizontalBias;
      this.verticalBias = param1LayoutParams.verticalBias;
      this.dimensionRatio = param1LayoutParams.dimensionRatio;
      this.dimensionRatioValue = param1LayoutParams.dimensionRatioValue;
      this.dimensionRatioSide = param1LayoutParams.dimensionRatioSide;
      this.horizontalWeight = param1LayoutParams.horizontalWeight;
      this.verticalWeight = param1LayoutParams.verticalWeight;
      this.horizontalChainStyle = param1LayoutParams.horizontalChainStyle;
      this.verticalChainStyle = param1LayoutParams.verticalChainStyle;
      this.constrainedWidth = param1LayoutParams.constrainedWidth;
      this.constrainedHeight = param1LayoutParams.constrainedHeight;
      this.matchConstraintDefaultWidth = param1LayoutParams.matchConstraintDefaultWidth;
      this.matchConstraintDefaultHeight = param1LayoutParams.matchConstraintDefaultHeight;
      this.matchConstraintMinWidth = param1LayoutParams.matchConstraintMinWidth;
      this.matchConstraintMaxWidth = param1LayoutParams.matchConstraintMaxWidth;
      this.matchConstraintMinHeight = param1LayoutParams.matchConstraintMinHeight;
      this.matchConstraintMaxHeight = param1LayoutParams.matchConstraintMaxHeight;
      this.matchConstraintPercentWidth = param1LayoutParams.matchConstraintPercentWidth;
      this.matchConstraintPercentHeight = param1LayoutParams.matchConstraintPercentHeight;
      this.editorAbsoluteX = param1LayoutParams.editorAbsoluteX;
      this.editorAbsoluteY = param1LayoutParams.editorAbsoluteY;
      this.orientation = param1LayoutParams.orientation;
      this.horizontalDimensionFixed = param1LayoutParams.horizontalDimensionFixed;
      this.verticalDimensionFixed = param1LayoutParams.verticalDimensionFixed;
      this.needsBaseline = param1LayoutParams.needsBaseline;
      this.isGuideline = param1LayoutParams.isGuideline;
      this.resolvedLeftToLeft = param1LayoutParams.resolvedLeftToLeft;
      this.resolvedLeftToRight = param1LayoutParams.resolvedLeftToRight;
      this.resolvedRightToLeft = param1LayoutParams.resolvedRightToLeft;
      this.resolvedRightToRight = param1LayoutParams.resolvedRightToRight;
      this.resolveGoneLeftMargin = param1LayoutParams.resolveGoneLeftMargin;
      this.resolveGoneRightMargin = param1LayoutParams.resolveGoneRightMargin;
      this.resolvedHorizontalBias = param1LayoutParams.resolvedHorizontalBias;
      this.widget = param1LayoutParams.widget;
    }
    
    public LayoutParams(ViewGroup.LayoutParams param1LayoutParams) { super(param1LayoutParams); }
    
    public void reset() {
      ConstraintWidget constraintWidget = this.widget;
      if (constraintWidget != null)
        constraintWidget.reset(); 
    }
    
    @TargetApi(17)
    public void resolveLayoutDirection(int param1Int) {
      int i = this.leftMargin;
      int j = this.rightMargin;
      super.resolveLayoutDirection(param1Int);
      this.resolvedRightToLeft = -1;
      this.resolvedRightToRight = -1;
      this.resolvedLeftToLeft = -1;
      this.resolvedLeftToRight = -1;
      this.resolveGoneLeftMargin = -1;
      this.resolveGoneRightMargin = -1;
      this.resolveGoneLeftMargin = this.goneLeftMargin;
      this.resolveGoneRightMargin = this.goneRightMargin;
      this.resolvedHorizontalBias = this.horizontalBias;
      this.resolvedGuideBegin = this.guideBegin;
      this.resolvedGuideEnd = this.guideEnd;
      this.resolvedGuidePercent = this.guidePercent;
      if (1 == getLayoutDirection()) {
        param1Int = 1;
      } else {
        param1Int = 0;
      } 
      if (param1Int != 0) {
        param1Int = 0;
        int k = this.startToEnd;
        if (k != -1) {
          this.resolvedRightToLeft = k;
          param1Int = 1;
        } else {
          k = this.startToStart;
          if (k != -1) {
            this.resolvedRightToRight = k;
            param1Int = 1;
          } 
        } 
        k = this.endToStart;
        if (k != -1) {
          this.resolvedLeftToRight = k;
          param1Int = 1;
        } 
        k = this.endToEnd;
        if (k != -1) {
          this.resolvedLeftToLeft = k;
          param1Int = 1;
        } 
        k = this.goneStartMargin;
        if (k != -1)
          this.resolveGoneRightMargin = k; 
        k = this.goneEndMargin;
        if (k != -1)
          this.resolveGoneLeftMargin = k; 
        if (param1Int != 0)
          this.resolvedHorizontalBias = 1.0F - this.horizontalBias; 
        if (this.isGuideline && this.orientation == 1) {
          float f = this.guidePercent;
          if (f != -1.0F) {
            this.resolvedGuidePercent = 1.0F - f;
            this.resolvedGuideBegin = -1;
            this.resolvedGuideEnd = -1;
          } else {
            param1Int = this.guideBegin;
            if (param1Int != -1) {
              this.resolvedGuideEnd = param1Int;
              this.resolvedGuideBegin = -1;
              this.resolvedGuidePercent = -1.0F;
            } else {
              param1Int = this.guideEnd;
              if (param1Int != -1) {
                this.resolvedGuideBegin = param1Int;
                this.resolvedGuideEnd = -1;
                this.resolvedGuidePercent = -1.0F;
              } 
            } 
          } 
        } 
      } else {
        param1Int = this.startToEnd;
        if (param1Int != -1)
          this.resolvedLeftToRight = param1Int; 
        param1Int = this.startToStart;
        if (param1Int != -1)
          this.resolvedLeftToLeft = param1Int; 
        param1Int = this.endToStart;
        if (param1Int != -1)
          this.resolvedRightToLeft = param1Int; 
        param1Int = this.endToEnd;
        if (param1Int != -1)
          this.resolvedRightToRight = param1Int; 
        param1Int = this.goneStartMargin;
        if (param1Int != -1)
          this.resolveGoneLeftMargin = param1Int; 
        param1Int = this.goneEndMargin;
        if (param1Int != -1)
          this.resolveGoneRightMargin = param1Int; 
      } 
      if (this.endToStart == -1 && this.endToEnd == -1 && this.startToStart == -1 && this.startToEnd == -1) {
        param1Int = this.rightToLeft;
        if (param1Int != -1) {
          this.resolvedRightToLeft = param1Int;
          if (this.rightMargin <= 0 && j > 0)
            this.rightMargin = j; 
        } else {
          param1Int = this.rightToRight;
          if (param1Int != -1) {
            this.resolvedRightToRight = param1Int;
            if (this.rightMargin <= 0 && j > 0)
              this.rightMargin = j; 
          } 
        } 
        param1Int = this.leftToLeft;
        if (param1Int != -1) {
          this.resolvedLeftToLeft = param1Int;
          if (this.leftMargin <= 0 && i > 0) {
            this.leftMargin = i;
            return;
          } 
        } else {
          param1Int = this.leftToRight;
          if (param1Int != -1) {
            this.resolvedLeftToRight = param1Int;
            if (this.leftMargin <= 0 && i > 0)
              this.leftMargin = i; 
          } 
        } 
      } 
    }
    
    public void validate() {
      this.isGuideline = false;
      this.horizontalDimensionFixed = true;
      this.verticalDimensionFixed = true;
      if (this.width == -2 && this.constrainedWidth) {
        this.horizontalDimensionFixed = false;
        this.matchConstraintDefaultWidth = 1;
      } 
      if (this.height == -2 && this.constrainedHeight) {
        this.verticalDimensionFixed = false;
        this.matchConstraintDefaultHeight = 1;
      } 
      if (this.width == 0 || this.width == -1) {
        this.horizontalDimensionFixed = false;
        if (this.width == 0 && this.matchConstraintDefaultWidth == 1) {
          this.width = -2;
          this.constrainedWidth = true;
        } 
      } 
      if (this.height == 0 || this.height == -1) {
        this.verticalDimensionFixed = false;
        if (this.height == 0 && this.matchConstraintDefaultHeight == 1) {
          this.height = -2;
          this.constrainedHeight = true;
        } 
      } 
      if (this.guidePercent != -1.0F || this.guideBegin != -1 || this.guideEnd != -1) {
        this.isGuideline = true;
        this.horizontalDimensionFixed = true;
        this.verticalDimensionFixed = true;
        if (!(this.widget instanceof Guideline))
          this.widget = new Guideline(); 
        ((Guideline)this.widget).setOrientation(this.orientation);
      } 
    }
    
    private static class Table {
      public static final int ANDROID_ORIENTATION = 1;
      
      public static final int LAYOUT_CONSTRAINED_HEIGHT = 28;
      
      public static final int LAYOUT_CONSTRAINED_WIDTH = 27;
      
      public static final int LAYOUT_CONSTRAINT_BASELINE_CREATOR = 43;
      
      public static final int LAYOUT_CONSTRAINT_BASELINE_TO_BASELINE_OF = 16;
      
      public static final int LAYOUT_CONSTRAINT_BOTTOM_CREATOR = 42;
      
      public static final int LAYOUT_CONSTRAINT_BOTTOM_TO_BOTTOM_OF = 15;
      
      public static final int LAYOUT_CONSTRAINT_BOTTOM_TO_TOP_OF = 14;
      
      public static final int LAYOUT_CONSTRAINT_CIRCLE = 2;
      
      public static final int LAYOUT_CONSTRAINT_CIRCLE_ANGLE = 4;
      
      public static final int LAYOUT_CONSTRAINT_CIRCLE_RADIUS = 3;
      
      public static final int LAYOUT_CONSTRAINT_DIMENSION_RATIO = 44;
      
      public static final int LAYOUT_CONSTRAINT_END_TO_END_OF = 20;
      
      public static final int LAYOUT_CONSTRAINT_END_TO_START_OF = 19;
      
      public static final int LAYOUT_CONSTRAINT_GUIDE_BEGIN = 5;
      
      public static final int LAYOUT_CONSTRAINT_GUIDE_END = 6;
      
      public static final int LAYOUT_CONSTRAINT_GUIDE_PERCENT = 7;
      
      public static final int LAYOUT_CONSTRAINT_HEIGHT_DEFAULT = 32;
      
      public static final int LAYOUT_CONSTRAINT_HEIGHT_MAX = 37;
      
      public static final int LAYOUT_CONSTRAINT_HEIGHT_MIN = 36;
      
      public static final int LAYOUT_CONSTRAINT_HEIGHT_PERCENT = 38;
      
      public static final int LAYOUT_CONSTRAINT_HORIZONTAL_BIAS = 29;
      
      public static final int LAYOUT_CONSTRAINT_HORIZONTAL_CHAINSTYLE = 47;
      
      public static final int LAYOUT_CONSTRAINT_HORIZONTAL_WEIGHT = 45;
      
      public static final int LAYOUT_CONSTRAINT_LEFT_CREATOR = 39;
      
      public static final int LAYOUT_CONSTRAINT_LEFT_TO_LEFT_OF = 8;
      
      public static final int LAYOUT_CONSTRAINT_LEFT_TO_RIGHT_OF = 9;
      
      public static final int LAYOUT_CONSTRAINT_RIGHT_CREATOR = 41;
      
      public static final int LAYOUT_CONSTRAINT_RIGHT_TO_LEFT_OF = 10;
      
      public static final int LAYOUT_CONSTRAINT_RIGHT_TO_RIGHT_OF = 11;
      
      public static final int LAYOUT_CONSTRAINT_START_TO_END_OF = 17;
      
      public static final int LAYOUT_CONSTRAINT_START_TO_START_OF = 18;
      
      public static final int LAYOUT_CONSTRAINT_TOP_CREATOR = 40;
      
      public static final int LAYOUT_CONSTRAINT_TOP_TO_BOTTOM_OF = 13;
      
      public static final int LAYOUT_CONSTRAINT_TOP_TO_TOP_OF = 12;
      
      public static final int LAYOUT_CONSTRAINT_VERTICAL_BIAS = 30;
      
      public static final int LAYOUT_CONSTRAINT_VERTICAL_CHAINSTYLE = 48;
      
      public static final int LAYOUT_CONSTRAINT_VERTICAL_WEIGHT = 46;
      
      public static final int LAYOUT_CONSTRAINT_WIDTH_DEFAULT = 31;
      
      public static final int LAYOUT_CONSTRAINT_WIDTH_MAX = 34;
      
      public static final int LAYOUT_CONSTRAINT_WIDTH_MIN = 33;
      
      public static final int LAYOUT_CONSTRAINT_WIDTH_PERCENT = 35;
      
      public static final int LAYOUT_EDITOR_ABSOLUTEX = 49;
      
      public static final int LAYOUT_EDITOR_ABSOLUTEY = 50;
      
      public static final int LAYOUT_GONE_MARGIN_BOTTOM = 24;
      
      public static final int LAYOUT_GONE_MARGIN_END = 26;
      
      public static final int LAYOUT_GONE_MARGIN_LEFT = 21;
      
      public static final int LAYOUT_GONE_MARGIN_RIGHT = 23;
      
      public static final int LAYOUT_GONE_MARGIN_START = 25;
      
      public static final int LAYOUT_GONE_MARGIN_TOP = 22;
      
      public static final int UNUSED = 0;
      
      public static final SparseIntArray map = new SparseIntArray();
      
      static  {
        map.append(R.styleable.ConstraintLayout_Layout_layout_constraintLeft_toLeftOf, 8);
        map.append(R.styleable.ConstraintLayout_Layout_layout_constraintLeft_toRightOf, 9);
        map.append(R.styleable.ConstraintLayout_Layout_layout_constraintRight_toLeftOf, 10);
        map.append(R.styleable.ConstraintLayout_Layout_layout_constraintRight_toRightOf, 11);
        map.append(R.styleable.ConstraintLayout_Layout_layout_constraintTop_toTopOf, 12);
        map.append(R.styleable.ConstraintLayout_Layout_layout_constraintTop_toBottomOf, 13);
        map.append(R.styleable.ConstraintLayout_Layout_layout_constraintBottom_toTopOf, 14);
        map.append(R.styleable.ConstraintLayout_Layout_layout_constraintBottom_toBottomOf, 15);
        map.append(R.styleable.ConstraintLayout_Layout_layout_constraintBaseline_toBaselineOf, 16);
        map.append(R.styleable.ConstraintLayout_Layout_layout_constraintCircle, 2);
        map.append(R.styleable.ConstraintLayout_Layout_layout_constraintCircleRadius, 3);
        map.append(R.styleable.ConstraintLayout_Layout_layout_constraintCircleAngle, 4);
        map.append(R.styleable.ConstraintLayout_Layout_layout_editor_absoluteX, 49);
        map.append(R.styleable.ConstraintLayout_Layout_layout_editor_absoluteY, 50);
        map.append(R.styleable.ConstraintLayout_Layout_layout_constraintGuide_begin, 5);
        map.append(R.styleable.ConstraintLayout_Layout_layout_constraintGuide_end, 6);
        map.append(R.styleable.ConstraintLayout_Layout_layout_constraintGuide_percent, 7);
        map.append(R.styleable.ConstraintLayout_Layout_android_orientation, 1);
        map.append(R.styleable.ConstraintLayout_Layout_layout_constraintStart_toEndOf, 17);
        map.append(R.styleable.ConstraintLayout_Layout_layout_constraintStart_toStartOf, 18);
        map.append(R.styleable.ConstraintLayout_Layout_layout_constraintEnd_toStartOf, 19);
        map.append(R.styleable.ConstraintLayout_Layout_layout_constraintEnd_toEndOf, 20);
        map.append(R.styleable.ConstraintLayout_Layout_layout_goneMarginLeft, 21);
        map.append(R.styleable.ConstraintLayout_Layout_layout_goneMarginTop, 22);
        map.append(R.styleable.ConstraintLayout_Layout_layout_goneMarginRight, 23);
        map.append(R.styleable.ConstraintLayout_Layout_layout_goneMarginBottom, 24);
        map.append(R.styleable.ConstraintLayout_Layout_layout_goneMarginStart, 25);
        map.append(R.styleable.ConstraintLayout_Layout_layout_goneMarginEnd, 26);
        map.append(R.styleable.ConstraintLayout_Layout_layout_constraintHorizontal_bias, 29);
        map.append(R.styleable.ConstraintLayout_Layout_layout_constraintVertical_bias, 30);
        map.append(R.styleable.ConstraintLayout_Layout_layout_constraintDimensionRatio, 44);
        map.append(R.styleable.ConstraintLayout_Layout_layout_constraintHorizontal_weight, 45);
        map.append(R.styleable.ConstraintLayout_Layout_layout_constraintVertical_weight, 46);
        map.append(R.styleable.ConstraintLayout_Layout_layout_constraintHorizontal_chainStyle, 47);
        map.append(R.styleable.ConstraintLayout_Layout_layout_constraintVertical_chainStyle, 48);
        map.append(R.styleable.ConstraintLayout_Layout_layout_constrainedWidth, 27);
        map.append(R.styleable.ConstraintLayout_Layout_layout_constrainedHeight, 28);
        map.append(R.styleable.ConstraintLayout_Layout_layout_constraintWidth_default, 31);
        map.append(R.styleable.ConstraintLayout_Layout_layout_constraintHeight_default, 32);
        map.append(R.styleable.ConstraintLayout_Layout_layout_constraintWidth_min, 33);
        map.append(R.styleable.ConstraintLayout_Layout_layout_constraintWidth_max, 34);
        map.append(R.styleable.ConstraintLayout_Layout_layout_constraintWidth_percent, 35);
        map.append(R.styleable.ConstraintLayout_Layout_layout_constraintHeight_min, 36);
        map.append(R.styleable.ConstraintLayout_Layout_layout_constraintHeight_max, 37);
        map.append(R.styleable.ConstraintLayout_Layout_layout_constraintHeight_percent, 38);
        map.append(R.styleable.ConstraintLayout_Layout_layout_constraintLeft_creator, 39);
        map.append(R.styleable.ConstraintLayout_Layout_layout_constraintTop_creator, 40);
        map.append(R.styleable.ConstraintLayout_Layout_layout_constraintRight_creator, 41);
        map.append(R.styleable.ConstraintLayout_Layout_layout_constraintBottom_creator, 42);
        map.append(R.styleable.ConstraintLayout_Layout_layout_constraintBaseline_creator, 43);
      }
    }
  }
  
  private static class Table {
    public static final int ANDROID_ORIENTATION = 1;
    
    public static final int LAYOUT_CONSTRAINED_HEIGHT = 28;
    
    public static final int LAYOUT_CONSTRAINED_WIDTH = 27;
    
    public static final int LAYOUT_CONSTRAINT_BASELINE_CREATOR = 43;
    
    public static final int LAYOUT_CONSTRAINT_BASELINE_TO_BASELINE_OF = 16;
    
    public static final int LAYOUT_CONSTRAINT_BOTTOM_CREATOR = 42;
    
    public static final int LAYOUT_CONSTRAINT_BOTTOM_TO_BOTTOM_OF = 15;
    
    public static final int LAYOUT_CONSTRAINT_BOTTOM_TO_TOP_OF = 14;
    
    public static final int LAYOUT_CONSTRAINT_CIRCLE = 2;
    
    public static final int LAYOUT_CONSTRAINT_CIRCLE_ANGLE = 4;
    
    public static final int LAYOUT_CONSTRAINT_CIRCLE_RADIUS = 3;
    
    public static final int LAYOUT_CONSTRAINT_DIMENSION_RATIO = 44;
    
    public static final int LAYOUT_CONSTRAINT_END_TO_END_OF = 20;
    
    public static final int LAYOUT_CONSTRAINT_END_TO_START_OF = 19;
    
    public static final int LAYOUT_CONSTRAINT_GUIDE_BEGIN = 5;
    
    public static final int LAYOUT_CONSTRAINT_GUIDE_END = 6;
    
    public static final int LAYOUT_CONSTRAINT_GUIDE_PERCENT = 7;
    
    public static final int LAYOUT_CONSTRAINT_HEIGHT_DEFAULT = 32;
    
    public static final int LAYOUT_CONSTRAINT_HEIGHT_MAX = 37;
    
    public static final int LAYOUT_CONSTRAINT_HEIGHT_MIN = 36;
    
    public static final int LAYOUT_CONSTRAINT_HEIGHT_PERCENT = 38;
    
    public static final int LAYOUT_CONSTRAINT_HORIZONTAL_BIAS = 29;
    
    public static final int LAYOUT_CONSTRAINT_HORIZONTAL_CHAINSTYLE = 47;
    
    public static final int LAYOUT_CONSTRAINT_HORIZONTAL_WEIGHT = 45;
    
    public static final int LAYOUT_CONSTRAINT_LEFT_CREATOR = 39;
    
    public static final int LAYOUT_CONSTRAINT_LEFT_TO_LEFT_OF = 8;
    
    public static final int LAYOUT_CONSTRAINT_LEFT_TO_RIGHT_OF = 9;
    
    public static final int LAYOUT_CONSTRAINT_RIGHT_CREATOR = 41;
    
    public static final int LAYOUT_CONSTRAINT_RIGHT_TO_LEFT_OF = 10;
    
    public static final int LAYOUT_CONSTRAINT_RIGHT_TO_RIGHT_OF = 11;
    
    public static final int LAYOUT_CONSTRAINT_START_TO_END_OF = 17;
    
    public static final int LAYOUT_CONSTRAINT_START_TO_START_OF = 18;
    
    public static final int LAYOUT_CONSTRAINT_TOP_CREATOR = 40;
    
    public static final int LAYOUT_CONSTRAINT_TOP_TO_BOTTOM_OF = 13;
    
    public static final int LAYOUT_CONSTRAINT_TOP_TO_TOP_OF = 12;
    
    public static final int LAYOUT_CONSTRAINT_VERTICAL_BIAS = 30;
    
    public static final int LAYOUT_CONSTRAINT_VERTICAL_CHAINSTYLE = 48;
    
    public static final int LAYOUT_CONSTRAINT_VERTICAL_WEIGHT = 46;
    
    public static final int LAYOUT_CONSTRAINT_WIDTH_DEFAULT = 31;
    
    public static final int LAYOUT_CONSTRAINT_WIDTH_MAX = 34;
    
    public static final int LAYOUT_CONSTRAINT_WIDTH_MIN = 33;
    
    public static final int LAYOUT_CONSTRAINT_WIDTH_PERCENT = 35;
    
    public static final int LAYOUT_EDITOR_ABSOLUTEX = 49;
    
    public static final int LAYOUT_EDITOR_ABSOLUTEY = 50;
    
    public static final int LAYOUT_GONE_MARGIN_BOTTOM = 24;
    
    public static final int LAYOUT_GONE_MARGIN_END = 26;
    
    public static final int LAYOUT_GONE_MARGIN_LEFT = 21;
    
    public static final int LAYOUT_GONE_MARGIN_RIGHT = 23;
    
    public static final int LAYOUT_GONE_MARGIN_START = 25;
    
    public static final int LAYOUT_GONE_MARGIN_TOP = 22;
    
    public static final int UNUSED = 0;
    
    public static final SparseIntArray map = new SparseIntArray();
    
    static  {
      map.append(R.styleable.ConstraintLayout_Layout_layout_constraintLeft_toLeftOf, 8);
      map.append(R.styleable.ConstraintLayout_Layout_layout_constraintLeft_toRightOf, 9);
      map.append(R.styleable.ConstraintLayout_Layout_layout_constraintRight_toLeftOf, 10);
      map.append(R.styleable.ConstraintLayout_Layout_layout_constraintRight_toRightOf, 11);
      map.append(R.styleable.ConstraintLayout_Layout_layout_constraintTop_toTopOf, 12);
      map.append(R.styleable.ConstraintLayout_Layout_layout_constraintTop_toBottomOf, 13);
      map.append(R.styleable.ConstraintLayout_Layout_layout_constraintBottom_toTopOf, 14);
      map.append(R.styleable.ConstraintLayout_Layout_layout_constraintBottom_toBottomOf, 15);
      map.append(R.styleable.ConstraintLayout_Layout_layout_constraintBaseline_toBaselineOf, 16);
      map.append(R.styleable.ConstraintLayout_Layout_layout_constraintCircle, 2);
      map.append(R.styleable.ConstraintLayout_Layout_layout_constraintCircleRadius, 3);
      map.append(R.styleable.ConstraintLayout_Layout_layout_constraintCircleAngle, 4);
      map.append(R.styleable.ConstraintLayout_Layout_layout_editor_absoluteX, 49);
      map.append(R.styleable.ConstraintLayout_Layout_layout_editor_absoluteY, 50);
      map.append(R.styleable.ConstraintLayout_Layout_layout_constraintGuide_begin, 5);
      map.append(R.styleable.ConstraintLayout_Layout_layout_constraintGuide_end, 6);
      map.append(R.styleable.ConstraintLayout_Layout_layout_constraintGuide_percent, 7);
      map.append(R.styleable.ConstraintLayout_Layout_android_orientation, 1);
      map.append(R.styleable.ConstraintLayout_Layout_layout_constraintStart_toEndOf, 17);
      map.append(R.styleable.ConstraintLayout_Layout_layout_constraintStart_toStartOf, 18);
      map.append(R.styleable.ConstraintLayout_Layout_layout_constraintEnd_toStartOf, 19);
      map.append(R.styleable.ConstraintLayout_Layout_layout_constraintEnd_toEndOf, 20);
      map.append(R.styleable.ConstraintLayout_Layout_layout_goneMarginLeft, 21);
      map.append(R.styleable.ConstraintLayout_Layout_layout_goneMarginTop, 22);
      map.append(R.styleable.ConstraintLayout_Layout_layout_goneMarginRight, 23);
      map.append(R.styleable.ConstraintLayout_Layout_layout_goneMarginBottom, 24);
      map.append(R.styleable.ConstraintLayout_Layout_layout_goneMarginStart, 25);
      map.append(R.styleable.ConstraintLayout_Layout_layout_goneMarginEnd, 26);
      map.append(R.styleable.ConstraintLayout_Layout_layout_constraintHorizontal_bias, 29);
      map.append(R.styleable.ConstraintLayout_Layout_layout_constraintVertical_bias, 30);
      map.append(R.styleable.ConstraintLayout_Layout_layout_constraintDimensionRatio, 44);
      map.append(R.styleable.ConstraintLayout_Layout_layout_constraintHorizontal_weight, 45);
      map.append(R.styleable.ConstraintLayout_Layout_layout_constraintVertical_weight, 46);
      map.append(R.styleable.ConstraintLayout_Layout_layout_constraintHorizontal_chainStyle, 47);
      map.append(R.styleable.ConstraintLayout_Layout_layout_constraintVertical_chainStyle, 48);
      map.append(R.styleable.ConstraintLayout_Layout_layout_constrainedWidth, 27);
      map.append(R.styleable.ConstraintLayout_Layout_layout_constrainedHeight, 28);
      map.append(R.styleable.ConstraintLayout_Layout_layout_constraintWidth_default, 31);
      map.append(R.styleable.ConstraintLayout_Layout_layout_constraintHeight_default, 32);
      map.append(R.styleable.ConstraintLayout_Layout_layout_constraintWidth_min, 33);
      map.append(R.styleable.ConstraintLayout_Layout_layout_constraintWidth_max, 34);
      map.append(R.styleable.ConstraintLayout_Layout_layout_constraintWidth_percent, 35);
      map.append(R.styleable.ConstraintLayout_Layout_layout_constraintHeight_min, 36);
      map.append(R.styleable.ConstraintLayout_Layout_layout_constraintHeight_max, 37);
      map.append(R.styleable.ConstraintLayout_Layout_layout_constraintHeight_percent, 38);
      map.append(R.styleable.ConstraintLayout_Layout_layout_constraintLeft_creator, 39);
      map.append(R.styleable.ConstraintLayout_Layout_layout_constraintTop_creator, 40);
      map.append(R.styleable.ConstraintLayout_Layout_layout_constraintRight_creator, 41);
      map.append(R.styleable.ConstraintLayout_Layout_layout_constraintBottom_creator, 42);
      map.append(R.styleable.ConstraintLayout_Layout_layout_constraintBaseline_creator, 43);
    }
  }
}


/* Location:              D:\Apks\New folder\classes-dex2jar.jar!\android\support\constraint\ConstraintLayout.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.0.7
 */