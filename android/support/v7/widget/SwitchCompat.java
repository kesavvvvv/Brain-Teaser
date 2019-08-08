package android.support.v7.widget;

import android.animation.ObjectAnimator;
import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.PorterDuff;
import android.graphics.Rect;
import android.graphics.Region;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.annotation.Nullable;
import android.support.v4.graphics.drawable.DrawableCompat;
import android.support.v4.view.ViewCompat;
import android.support.v4.widget.TextViewCompat;
import android.support.v7.appcompat.R;
import android.support.v7.content.res.AppCompatResources;
import android.support.v7.text.AllCapsTransformationMethod;
import android.text.Layout;
import android.text.StaticLayout;
import android.text.TextPaint;
import android.text.TextUtils;
import android.text.method.TransformationMethod;
import android.util.AttributeSet;
import android.util.Property;
import android.view.ActionMode;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.ViewConfiguration;
import android.view.accessibility.AccessibilityEvent;
import android.view.accessibility.AccessibilityNodeInfo;
import android.widget.CompoundButton;

public class SwitchCompat extends CompoundButton {
  private static final String ACCESSIBILITY_EVENT_CLASS_NAME = "android.widget.Switch";
  
  private static final int[] CHECKED_STATE_SET;
  
  private static final int MONOSPACE = 3;
  
  private static final int SANS = 1;
  
  private static final int SERIF = 2;
  
  private static final int THUMB_ANIMATION_DURATION = 250;
  
  private static final Property<SwitchCompat, Float> THUMB_POS = new Property<SwitchCompat, Float>(Float.class, "thumbPos") {
      public Float get(SwitchCompat param1SwitchCompat) { return Float.valueOf(param1SwitchCompat.mThumbPosition); }
      
      public void set(SwitchCompat param1SwitchCompat, Float param1Float) { param1SwitchCompat.setThumbPosition(param1Float.floatValue()); }
    };
  
  private static final int TOUCH_MODE_DOWN = 1;
  
  private static final int TOUCH_MODE_DRAGGING = 2;
  
  private static final int TOUCH_MODE_IDLE = 0;
  
  private boolean mHasThumbTint = false;
  
  private boolean mHasThumbTintMode = false;
  
  private boolean mHasTrackTint = false;
  
  private boolean mHasTrackTintMode = false;
  
  private int mMinFlingVelocity;
  
  private Layout mOffLayout;
  
  private Layout mOnLayout;
  
  ObjectAnimator mPositionAnimator;
  
  private boolean mShowText;
  
  private boolean mSplitTrack;
  
  private int mSwitchBottom;
  
  private int mSwitchHeight;
  
  private int mSwitchLeft;
  
  private int mSwitchMinWidth;
  
  private int mSwitchPadding;
  
  private int mSwitchRight;
  
  private int mSwitchTop;
  
  private TransformationMethod mSwitchTransformationMethod;
  
  private int mSwitchWidth;
  
  private final Rect mTempRect = new Rect();
  
  private ColorStateList mTextColors;
  
  private CharSequence mTextOff;
  
  private CharSequence mTextOn;
  
  private final TextPaint mTextPaint = new TextPaint(1);
  
  private Drawable mThumbDrawable;
  
  float mThumbPosition;
  
  private int mThumbTextPadding;
  
  private ColorStateList mThumbTintList = null;
  
  private PorterDuff.Mode mThumbTintMode = null;
  
  private int mThumbWidth;
  
  private int mTouchMode;
  
  private int mTouchSlop;
  
  private float mTouchX;
  
  private float mTouchY;
  
  private Drawable mTrackDrawable;
  
  private ColorStateList mTrackTintList = null;
  
  private PorterDuff.Mode mTrackTintMode = null;
  
  private VelocityTracker mVelocityTracker = VelocityTracker.obtain();
  
  static  {
    CHECKED_STATE_SET = new int[] { 16842912 };
  }
  
  public SwitchCompat(Context paramContext) { this(paramContext, null); }
  
  public SwitchCompat(Context paramContext, AttributeSet paramAttributeSet) { this(paramContext, paramAttributeSet, R.attr.switchStyle); }
  
  public SwitchCompat(Context paramContext, AttributeSet paramAttributeSet, int paramInt) {
    super(paramContext, paramAttributeSet, paramInt);
    Resources resources = getResources();
    this.mTextPaint.density = (resources.getDisplayMetrics()).density;
    TintTypedArray tintTypedArray = TintTypedArray.obtainStyledAttributes(paramContext, paramAttributeSet, R.styleable.SwitchCompat, paramInt, 0);
    this.mThumbDrawable = tintTypedArray.getDrawable(R.styleable.SwitchCompat_android_thumb);
    Drawable drawable = this.mThumbDrawable;
    if (drawable != null)
      drawable.setCallback(this); 
    this.mTrackDrawable = tintTypedArray.getDrawable(R.styleable.SwitchCompat_track);
    drawable = this.mTrackDrawable;
    if (drawable != null)
      drawable.setCallback(this); 
    this.mTextOn = tintTypedArray.getText(R.styleable.SwitchCompat_android_textOn);
    this.mTextOff = tintTypedArray.getText(R.styleable.SwitchCompat_android_textOff);
    this.mShowText = tintTypedArray.getBoolean(R.styleable.SwitchCompat_showText, true);
    this.mThumbTextPadding = tintTypedArray.getDimensionPixelSize(R.styleable.SwitchCompat_thumbTextPadding, 0);
    this.mSwitchMinWidth = tintTypedArray.getDimensionPixelSize(R.styleable.SwitchCompat_switchMinWidth, 0);
    this.mSwitchPadding = tintTypedArray.getDimensionPixelSize(R.styleable.SwitchCompat_switchPadding, 0);
    this.mSplitTrack = tintTypedArray.getBoolean(R.styleable.SwitchCompat_splitTrack, false);
    ColorStateList colorStateList2 = tintTypedArray.getColorStateList(R.styleable.SwitchCompat_thumbTint);
    if (colorStateList2 != null) {
      this.mThumbTintList = colorStateList2;
      this.mHasThumbTint = true;
    } 
    PorterDuff.Mode mode2 = DrawableUtils.parseTintMode(tintTypedArray.getInt(R.styleable.SwitchCompat_thumbTintMode, -1), null);
    if (this.mThumbTintMode != mode2) {
      this.mThumbTintMode = mode2;
      this.mHasThumbTintMode = true;
    } 
    if (this.mHasThumbTint || this.mHasThumbTintMode)
      applyThumbTint(); 
    ColorStateList colorStateList1 = tintTypedArray.getColorStateList(R.styleable.SwitchCompat_trackTint);
    if (colorStateList1 != null) {
      this.mTrackTintList = colorStateList1;
      this.mHasTrackTint = true;
    } 
    PorterDuff.Mode mode1 = DrawableUtils.parseTintMode(tintTypedArray.getInt(R.styleable.SwitchCompat_trackTintMode, -1), null);
    if (this.mTrackTintMode != mode1) {
      this.mTrackTintMode = mode1;
      this.mHasTrackTintMode = true;
    } 
    if (this.mHasTrackTint || this.mHasTrackTintMode)
      applyTrackTint(); 
    paramInt = tintTypedArray.getResourceId(R.styleable.SwitchCompat_switchTextAppearance, 0);
    if (paramInt != 0)
      setSwitchTextAppearance(paramContext, paramInt); 
    tintTypedArray.recycle();
    ViewConfiguration viewConfiguration = ViewConfiguration.get(paramContext);
    this.mTouchSlop = viewConfiguration.getScaledTouchSlop();
    this.mMinFlingVelocity = viewConfiguration.getScaledMinimumFlingVelocity();
    refreshDrawableState();
    setChecked(isChecked());
  }
  
  private void animateThumbToCheckedState(boolean paramBoolean) {
    float f;
    if (paramBoolean) {
      f = 1.0F;
    } else {
      f = 0.0F;
    } 
    this.mPositionAnimator = ObjectAnimator.ofFloat(this, THUMB_POS, new float[] { f });
    this.mPositionAnimator.setDuration(250L);
    if (Build.VERSION.SDK_INT >= 18)
      this.mPositionAnimator.setAutoCancel(true); 
    this.mPositionAnimator.start();
  }
  
  private void applyThumbTint() {
    if (this.mThumbDrawable != null && (this.mHasThumbTint || this.mHasThumbTintMode)) {
      this.mThumbDrawable = this.mThumbDrawable.mutate();
      if (this.mHasThumbTint)
        DrawableCompat.setTintList(this.mThumbDrawable, this.mThumbTintList); 
      if (this.mHasThumbTintMode)
        DrawableCompat.setTintMode(this.mThumbDrawable, this.mThumbTintMode); 
      if (this.mThumbDrawable.isStateful())
        this.mThumbDrawable.setState(getDrawableState()); 
    } 
  }
  
  private void applyTrackTint() {
    if (this.mTrackDrawable != null && (this.mHasTrackTint || this.mHasTrackTintMode)) {
      this.mTrackDrawable = this.mTrackDrawable.mutate();
      if (this.mHasTrackTint)
        DrawableCompat.setTintList(this.mTrackDrawable, this.mTrackTintList); 
      if (this.mHasTrackTintMode)
        DrawableCompat.setTintMode(this.mTrackDrawable, this.mTrackTintMode); 
      if (this.mTrackDrawable.isStateful())
        this.mTrackDrawable.setState(getDrawableState()); 
    } 
  }
  
  private void cancelPositionAnimator() {
    ObjectAnimator objectAnimator = this.mPositionAnimator;
    if (objectAnimator != null)
      objectAnimator.cancel(); 
  }
  
  private void cancelSuperTouch(MotionEvent paramMotionEvent) {
    paramMotionEvent = MotionEvent.obtain(paramMotionEvent);
    paramMotionEvent.setAction(3);
    super.onTouchEvent(paramMotionEvent);
    paramMotionEvent.recycle();
  }
  
  private static float constrain(float paramFloat1, float paramFloat2, float paramFloat3) { return (paramFloat1 < paramFloat2) ? paramFloat2 : ((paramFloat1 > paramFloat3) ? paramFloat3 : paramFloat1); }
  
  private boolean getTargetCheckedState() { return (this.mThumbPosition > 0.5F); }
  
  private int getThumbOffset() {
    float f;
    if (ViewUtils.isLayoutRtl(this)) {
      f = 1.0F - this.mThumbPosition;
    } else {
      f = this.mThumbPosition;
    } 
    return (int)(getThumbScrollRange() * f + 0.5F);
  }
  
  private int getThumbScrollRange() {
    Drawable drawable = this.mTrackDrawable;
    if (drawable != null) {
      Rect rect1;
      Rect rect2 = this.mTempRect;
      drawable.getPadding(rect2);
      drawable = this.mThumbDrawable;
      if (drawable != null) {
        rect1 = DrawableUtils.getOpticalBounds(drawable);
      } else {
        rect1 = DrawableUtils.INSETS_NONE;
      } 
      return this.mSwitchWidth - this.mThumbWidth - rect2.left - rect2.right - rect1.left - rect1.right;
    } 
    return 0;
  }
  
  private boolean hitThumb(float paramFloat1, float paramFloat2) {
    Drawable drawable = this.mThumbDrawable;
    byte b = 0;
    if (drawable == null)
      return false; 
    int k = getThumbOffset();
    this.mThumbDrawable.getPadding(this.mTempRect);
    int i = this.mSwitchTop;
    int j = this.mTouchSlop;
    k = this.mSwitchLeft + k - j;
    int m = this.mThumbWidth;
    int n = this.mTempRect.left;
    int i1 = this.mTempRect.right;
    int i2 = this.mTouchSlop;
    int i3 = this.mSwitchBottom;
    int i4 = b;
    if (paramFloat1 > k) {
      i4 = b;
      if (paramFloat1 < (m + k + n + i1 + i2)) {
        i4 = b;
        if (paramFloat2 > (i - j)) {
          i4 = b;
          if (paramFloat2 < (i3 + i2))
            i4 = 1; 
        } 
      } 
    } 
    return i4;
  }
  
  private Layout makeLayout(CharSequence paramCharSequence) {
    byte b;
    TransformationMethod transformationMethod = this.mSwitchTransformationMethod;
    if (transformationMethod != null)
      paramCharSequence = transformationMethod.getTransformation(paramCharSequence, this); 
    TextPaint textPaint = this.mTextPaint;
    if (paramCharSequence != null) {
      b = (int)Math.ceil(Layout.getDesiredWidth(paramCharSequence, textPaint));
    } else {
      b = 0;
    } 
    return new StaticLayout(paramCharSequence, textPaint, b, Layout.Alignment.ALIGN_NORMAL, 1.0F, 0.0F, true);
  }
  
  private void setSwitchTypefaceByIndex(int paramInt1, int paramInt2) {
    Typeface typeface = null;
    switch (paramInt1) {
      case 3:
        typeface = Typeface.MONOSPACE;
        break;
      case 2:
        typeface = Typeface.SERIF;
        break;
      case 1:
        typeface = Typeface.SANS_SERIF;
        break;
    } 
    setSwitchTypeface(typeface, paramInt2);
  }
  
  private void stopDrag(MotionEvent paramMotionEvent) {
    this.mTouchMode = 0;
    int i = paramMotionEvent.getAction();
    boolean bool1 = true;
    if (i == 1 && isEnabled()) {
      i = 1;
    } else {
      i = 0;
    } 
    boolean bool2 = isChecked();
    if (i != 0) {
      this.mVelocityTracker.computeCurrentVelocity(1000);
      float f = this.mVelocityTracker.getXVelocity();
      if (Math.abs(f) > this.mMinFlingVelocity) {
        if (ViewUtils.isLayoutRtl(this) ? (f < 0.0F) : (f > 0.0F))
          bool1 = false; 
      } else {
        bool1 = getTargetCheckedState();
      } 
    } else {
      bool1 = bool2;
    } 
    if (bool1 != bool2)
      playSoundEffect(0); 
    setChecked(bool1);
    cancelSuperTouch(paramMotionEvent);
  }
  
  public void draw(Canvas paramCanvas) {
    Rect rect2 = this.mTempRect;
    int m = this.mSwitchLeft;
    int n = this.mSwitchTop;
    int j = this.mSwitchRight;
    int i1 = this.mSwitchBottom;
    int k = getThumbOffset() + m;
    Rect rect1 = this.mThumbDrawable;
    if (rect1 != null) {
      Rect rect = DrawableUtils.getOpticalBounds(rect1);
    } else {
      rect1 = DrawableUtils.INSETS_NONE;
    } 
    Drawable drawable2 = this.mTrackDrawable;
    int i = k;
    if (drawable2 != null) {
      drawable2.getPadding(rect2);
      int i7 = k + rect2.left;
      k = n;
      int i2 = i1;
      int i3 = m;
      int i4 = k;
      int i5 = j;
      int i6 = i2;
      if (rect1 != null) {
        i = m;
        if (rect1.left > rect2.left)
          i = m + rect1.left - rect2.left; 
        m = k;
        if (rect1.top > rect2.top)
          m = k + rect1.top - rect2.top; 
        k = j;
        if (rect1.right > rect2.right)
          k = j - rect1.right - rect2.right; 
        i3 = i;
        i4 = m;
        i5 = k;
        i6 = i2;
        if (rect1.bottom > rect2.bottom) {
          i6 = i2 - rect1.bottom - rect2.bottom;
          i5 = k;
          i4 = m;
          i3 = i;
        } 
      } 
      this.mTrackDrawable.setBounds(i3, i4, i5, i6);
      i = i7;
    } 
    Drawable drawable1 = this.mThumbDrawable;
    if (drawable1 != null) {
      drawable1.getPadding(rect2);
      j = i - rect2.left;
      i = this.mThumbWidth + i + rect2.right;
      this.mThumbDrawable.setBounds(j, n, i, i1);
      drawable1 = getBackground();
      if (drawable1 != null)
        DrawableCompat.setHotspotBounds(drawable1, j, n, i, i1); 
    } 
    super.draw(paramCanvas);
  }
  
  public void drawableHotspotChanged(float paramFloat1, float paramFloat2) {
    if (Build.VERSION.SDK_INT >= 21)
      super.drawableHotspotChanged(paramFloat1, paramFloat2); 
    Drawable drawable = this.mThumbDrawable;
    if (drawable != null)
      DrawableCompat.setHotspot(drawable, paramFloat1, paramFloat2); 
    drawable = this.mTrackDrawable;
    if (drawable != null)
      DrawableCompat.setHotspot(drawable, paramFloat1, paramFloat2); 
  }
  
  protected void drawableStateChanged() {
    super.drawableStateChanged();
    int[] arrayOfInt = getDrawableState();
    byte b = 0;
    Drawable drawable = this.mThumbDrawable;
    boolean bool1 = b;
    if (drawable != null) {
      bool1 = b;
      if (drawable.isStateful())
        bool1 = false | drawable.setState(arrayOfInt); 
    } 
    drawable = this.mTrackDrawable;
    boolean bool2 = bool1;
    if (drawable != null) {
      bool2 = bool1;
      if (drawable.isStateful())
        bool2 = bool1 | drawable.setState(arrayOfInt); 
    } 
    if (bool2)
      invalidate(); 
  }
  
  public int getCompoundPaddingLeft() {
    if (!ViewUtils.isLayoutRtl(this))
      return super.getCompoundPaddingLeft(); 
    int j = super.getCompoundPaddingLeft() + this.mSwitchWidth;
    int i = j;
    if (!TextUtils.isEmpty(getText()))
      i = j + this.mSwitchPadding; 
    return i;
  }
  
  public int getCompoundPaddingRight() {
    if (ViewUtils.isLayoutRtl(this))
      return super.getCompoundPaddingRight(); 
    int j = super.getCompoundPaddingRight() + this.mSwitchWidth;
    int i = j;
    if (!TextUtils.isEmpty(getText()))
      i = j + this.mSwitchPadding; 
    return i;
  }
  
  public boolean getShowText() { return this.mShowText; }
  
  public boolean getSplitTrack() { return this.mSplitTrack; }
  
  public int getSwitchMinWidth() { return this.mSwitchMinWidth; }
  
  public int getSwitchPadding() { return this.mSwitchPadding; }
  
  public CharSequence getTextOff() { return this.mTextOff; }
  
  public CharSequence getTextOn() { return this.mTextOn; }
  
  public Drawable getThumbDrawable() { return this.mThumbDrawable; }
  
  public int getThumbTextPadding() { return this.mThumbTextPadding; }
  
  @Nullable
  public ColorStateList getThumbTintList() { return this.mThumbTintList; }
  
  @Nullable
  public PorterDuff.Mode getThumbTintMode() { return this.mThumbTintMode; }
  
  public Drawable getTrackDrawable() { return this.mTrackDrawable; }
  
  @Nullable
  public ColorStateList getTrackTintList() { return this.mTrackTintList; }
  
  @Nullable
  public PorterDuff.Mode getTrackTintMode() { return this.mTrackTintMode; }
  
  public void jumpDrawablesToCurrentState() {
    super.jumpDrawablesToCurrentState();
    Drawable drawable = this.mThumbDrawable;
    if (drawable != null)
      drawable.jumpToCurrentState(); 
    drawable = this.mTrackDrawable;
    if (drawable != null)
      drawable.jumpToCurrentState(); 
    ObjectAnimator objectAnimator = this.mPositionAnimator;
    if (objectAnimator != null && objectAnimator.isStarted()) {
      this.mPositionAnimator.end();
      this.mPositionAnimator = null;
    } 
  }
  
  protected int[] onCreateDrawableState(int paramInt) {
    int[] arrayOfInt = super.onCreateDrawableState(paramInt + 1);
    if (isChecked())
      mergeDrawableStates(arrayOfInt, CHECKED_STATE_SET); 
    return arrayOfInt;
  }
  
  protected void onDraw(Canvas paramCanvas) {
    super.onDraw(paramCanvas);
    Layout layout = this.mTempRect;
    Drawable drawable2 = this.mTrackDrawable;
    if (drawable2 != null) {
      drawable2.getPadding(layout);
    } else {
      layout.setEmpty();
    } 
    int j = this.mSwitchTop;
    int k = this.mSwitchBottom;
    int m = layout.top;
    int n = layout.bottom;
    Drawable drawable1 = this.mThumbDrawable;
    if (drawable2 != null)
      if (this.mSplitTrack && drawable1 != null) {
        Rect rect = DrawableUtils.getOpticalBounds(drawable1);
        drawable1.copyBounds(layout);
        layout.left += rect.left;
        layout.right -= rect.right;
        int i1 = paramCanvas.save();
        paramCanvas.clipRect(layout, Region.Op.DIFFERENCE);
        drawable2.draw(paramCanvas);
        paramCanvas.restoreToCount(i1);
      } else {
        drawable2.draw(paramCanvas);
      }  
    int i = paramCanvas.save();
    if (drawable1 != null)
      drawable1.draw(paramCanvas); 
    if (getTargetCheckedState()) {
      Layout layout1 = this.mOnLayout;
    } else {
      layout = this.mOffLayout;
    } 
    if (layout != null) {
      int i1;
      int[] arrayOfInt = getDrawableState();
      ColorStateList colorStateList = this.mTextColors;
      if (colorStateList != null)
        this.mTextPaint.setColor(colorStateList.getColorForState(arrayOfInt, 0)); 
      this.mTextPaint.drawableState = arrayOfInt;
      if (drawable1 != null) {
        Rect rect = drawable1.getBounds();
        i1 = rect.left + rect.right;
      } else {
        i1 = getWidth();
      } 
      i1 /= 2;
      int i2 = layout.getWidth() / 2;
      j = (m + j + k - n) / 2;
      k = layout.getHeight() / 2;
      paramCanvas.translate((i1 - i2), (j - k));
      layout.draw(paramCanvas);
    } 
    paramCanvas.restoreToCount(i);
  }
  
  public void onInitializeAccessibilityEvent(AccessibilityEvent paramAccessibilityEvent) {
    super.onInitializeAccessibilityEvent(paramAccessibilityEvent);
    paramAccessibilityEvent.setClassName("android.widget.Switch");
  }
  
  public void onInitializeAccessibilityNodeInfo(AccessibilityNodeInfo paramAccessibilityNodeInfo) {
    CharSequence charSequence;
    super.onInitializeAccessibilityNodeInfo(paramAccessibilityNodeInfo);
    paramAccessibilityNodeInfo.setClassName("android.widget.Switch");
    if (isChecked()) {
      charSequence = this.mTextOn;
    } else {
      charSequence = this.mTextOff;
    } 
    if (!TextUtils.isEmpty(charSequence)) {
      CharSequence charSequence1 = paramAccessibilityNodeInfo.getText();
      if (TextUtils.isEmpty(charSequence1)) {
        paramAccessibilityNodeInfo.setText(charSequence);
        return;
      } 
      StringBuilder stringBuilder = new StringBuilder();
      stringBuilder.append(charSequence1);
      stringBuilder.append(' ');
      stringBuilder.append(charSequence);
      paramAccessibilityNodeInfo.setText(stringBuilder);
    } 
  }
  
  protected void onLayout(boolean paramBoolean, int paramInt1, int paramInt2, int paramInt3, int paramInt4) {
    super.onLayout(paramBoolean, paramInt1, paramInt2, paramInt3, paramInt4);
    paramInt1 = 0;
    paramInt2 = 0;
    if (this.mThumbDrawable != null) {
      Rect rect1 = this.mTempRect;
      Drawable drawable = this.mTrackDrawable;
      if (drawable != null) {
        drawable.getPadding(rect1);
      } else {
        rect1.setEmpty();
      } 
      Rect rect2 = DrawableUtils.getOpticalBounds(this.mThumbDrawable);
      paramInt1 = Math.max(0, rect2.left - rect1.left);
      paramInt2 = Math.max(0, rect2.right - rect1.right);
    } 
    if (ViewUtils.isLayoutRtl(this)) {
      paramInt3 = getPaddingLeft() + paramInt1;
      paramInt4 = this.mSwitchWidth + paramInt3 - paramInt1 - paramInt2;
    } else {
      paramInt4 = getWidth() - getPaddingRight() - paramInt2;
      paramInt3 = paramInt4 - this.mSwitchWidth + paramInt1 + paramInt2;
    } 
    paramInt1 = getGravity() & 0x70;
    if (paramInt1 != 16) {
      if (paramInt1 != 80) {
        paramInt1 = getPaddingTop();
        int i = this.mSwitchHeight;
        paramInt2 = paramInt1;
        paramInt1 = i + paramInt1;
      } else {
        paramInt1 = getHeight() - getPaddingBottom();
        paramInt2 = paramInt1 - this.mSwitchHeight;
      } 
    } else {
      paramInt2 = (getPaddingTop() + getHeight() - getPaddingBottom()) / 2;
      paramInt1 = this.mSwitchHeight;
      paramInt2 -= paramInt1 / 2;
      paramInt1 = paramInt2 + paramInt1;
    } 
    this.mSwitchLeft = paramInt3;
    this.mSwitchTop = paramInt2;
    this.mSwitchBottom = paramInt1;
    this.mSwitchRight = paramInt4;
  }
  
  public void onMeasure(int paramInt1, int paramInt2) {
    int j;
    if (this.mShowText) {
      if (this.mOnLayout == null)
        this.mOnLayout = makeLayout(this.mTextOn); 
      if (this.mOffLayout == null)
        this.mOffLayout = makeLayout(this.mTextOff); 
    } 
    Rect rect = this.mTempRect;
    Drawable drawable2 = this.mThumbDrawable;
    if (drawable2 != null) {
      drawable2.getPadding(rect);
      j = this.mThumbDrawable.getIntrinsicWidth() - rect.left - rect.right;
      i = this.mThumbDrawable.getIntrinsicHeight();
    } else {
      j = 0;
      i = 0;
    } 
    if (this.mShowText) {
      k = Math.max(this.mOnLayout.getWidth(), this.mOffLayout.getWidth()) + this.mThumbTextPadding * 2;
    } else {
      k = 0;
    } 
    this.mThumbWidth = Math.max(k, j);
    drawable2 = this.mTrackDrawable;
    if (drawable2 != null) {
      drawable2.getPadding(rect);
      j = this.mTrackDrawable.getIntrinsicHeight();
    } else {
      rect.setEmpty();
      j = 0;
    } 
    int i1 = rect.left;
    int n = rect.right;
    Drawable drawable1 = this.mThumbDrawable;
    int m = i1;
    int k = n;
    if (drawable1 != null) {
      Rect rect1 = DrawableUtils.getOpticalBounds(drawable1);
      m = Math.max(i1, rect1.left);
      k = Math.max(n, rect1.right);
    } 
    k = Math.max(this.mSwitchMinWidth, this.mThumbWidth * 2 + m + k);
    int i = Math.max(j, i);
    this.mSwitchWidth = k;
    this.mSwitchHeight = i;
    super.onMeasure(paramInt1, paramInt2);
    if (getMeasuredHeight() < i)
      setMeasuredDimension(getMeasuredWidthAndState(), i); 
  }
  
  public void onPopulateAccessibilityEvent(AccessibilityEvent paramAccessibilityEvent) {
    CharSequence charSequence;
    super.onPopulateAccessibilityEvent(paramAccessibilityEvent);
    if (isChecked()) {
      charSequence = this.mTextOn;
    } else {
      charSequence = this.mTextOff;
    } 
    if (charSequence != null)
      paramAccessibilityEvent.getText().add(charSequence); 
  }
  
  public boolean onTouchEvent(MotionEvent paramMotionEvent) {
    int i;
    float f3;
    this.mVelocityTracker.addMovement(paramMotionEvent);
    switch (paramMotionEvent.getActionMasked()) {
      default:
        return super.onTouchEvent(paramMotionEvent);
      case 2:
        switch (this.mTouchMode) {
          case 2:
            f3 = paramMotionEvent.getX();
            i = getThumbScrollRange();
            f1 = f3 - this.mTouchX;
            if (i != 0) {
              f1 /= i;
            } else if (f1 > 0.0F) {
              f1 = 1.0F;
            } else {
              f1 = -1.0F;
            } 
            f2 = f1;
            if (ViewUtils.isLayoutRtl(this))
              f2 = -f1; 
            f1 = constrain(this.mThumbPosition + f2, 0.0F, 1.0F);
            if (f1 != this.mThumbPosition) {
              this.mTouchX = f3;
              setThumbPosition(f1);
            } 
            return true;
          case 1:
            f1 = paramMotionEvent.getX();
            f2 = paramMotionEvent.getY();
            if (Math.abs(f1 - this.mTouchX) > this.mTouchSlop || Math.abs(f2 - this.mTouchY) > this.mTouchSlop) {
              this.mTouchMode = 2;
              getParent().requestDisallowInterceptTouchEvent(true);
              this.mTouchX = f1;
              this.mTouchY = f2;
              return true;
            } 
            break;
        } 
      case 1:
      case 3:
        if (this.mTouchMode == 2) {
          stopDrag(paramMotionEvent);
          super.onTouchEvent(paramMotionEvent);
          return true;
        } 
        this.mTouchMode = 0;
        this.mVelocityTracker.clear();
      case 0:
        break;
    } 
    float f1 = paramMotionEvent.getX();
    float f2 = paramMotionEvent.getY();
    if (isEnabled() && hitThumb(f1, f2)) {
      this.mTouchMode = 1;
      this.mTouchX = f1;
      this.mTouchY = f2;
    } 
  }
  
  public void setChecked(boolean paramBoolean) {
    float f;
    super.setChecked(paramBoolean);
    paramBoolean = isChecked();
    if (getWindowToken() != null && ViewCompat.isLaidOut(this)) {
      animateThumbToCheckedState(paramBoolean);
      return;
    } 
    cancelPositionAnimator();
    if (paramBoolean) {
      f = 1.0F;
    } else {
      f = 0.0F;
    } 
    setThumbPosition(f);
  }
  
  public void setCustomSelectionActionModeCallback(ActionMode.Callback paramCallback) { super.setCustomSelectionActionModeCallback(TextViewCompat.wrapCustomSelectionActionModeCallback(this, paramCallback)); }
  
  public void setShowText(boolean paramBoolean) {
    if (this.mShowText != paramBoolean) {
      this.mShowText = paramBoolean;
      requestLayout();
    } 
  }
  
  public void setSplitTrack(boolean paramBoolean) {
    this.mSplitTrack = paramBoolean;
    invalidate();
  }
  
  public void setSwitchMinWidth(int paramInt) {
    this.mSwitchMinWidth = paramInt;
    requestLayout();
  }
  
  public void setSwitchPadding(int paramInt) {
    this.mSwitchPadding = paramInt;
    requestLayout();
  }
  
  public void setSwitchTextAppearance(Context paramContext, int paramInt) {
    TintTypedArray tintTypedArray = TintTypedArray.obtainStyledAttributes(paramContext, paramInt, R.styleable.TextAppearance);
    ColorStateList colorStateList = tintTypedArray.getColorStateList(R.styleable.TextAppearance_android_textColor);
    if (colorStateList != null) {
      this.mTextColors = colorStateList;
    } else {
      this.mTextColors = getTextColors();
    } 
    paramInt = tintTypedArray.getDimensionPixelSize(R.styleable.TextAppearance_android_textSize, 0);
    if (paramInt != 0 && paramInt != this.mTextPaint.getTextSize()) {
      this.mTextPaint.setTextSize(paramInt);
      requestLayout();
    } 
    setSwitchTypefaceByIndex(tintTypedArray.getInt(R.styleable.TextAppearance_android_typeface, -1), tintTypedArray.getInt(R.styleable.TextAppearance_android_textStyle, -1));
    if (tintTypedArray.getBoolean(R.styleable.TextAppearance_textAllCaps, false)) {
      this.mSwitchTransformationMethod = new AllCapsTransformationMethod(getContext());
    } else {
      this.mSwitchTransformationMethod = null;
    } 
    tintTypedArray.recycle();
  }
  
  public void setSwitchTypeface(Typeface paramTypeface) {
    if ((this.mTextPaint.getTypeface() != null && !this.mTextPaint.getTypeface().equals(paramTypeface)) || (this.mTextPaint.getTypeface() == null && paramTypeface != null)) {
      this.mTextPaint.setTypeface(paramTypeface);
      requestLayout();
      invalidate();
    } 
  }
  
  public void setSwitchTypeface(Typeface paramTypeface, int paramInt) {
    TextPaint textPaint;
    float f = 0.0F;
    boolean bool = false;
    if (paramInt > 0) {
      boolean bool1;
      if (paramTypeface == null) {
        paramTypeface = Typeface.defaultFromStyle(paramInt);
      } else {
        paramTypeface = Typeface.create(paramTypeface, paramInt);
      } 
      setSwitchTypeface(paramTypeface);
      if (paramTypeface != null) {
        bool1 = paramTypeface.getStyle();
      } else {
        bool1 = false;
      } 
      paramInt = (bool1 ^ 0xFFFFFFFF) & paramInt;
      textPaint = this.mTextPaint;
      if ((paramInt & true) != 0)
        bool = true; 
      textPaint.setFakeBoldText(bool);
      textPaint = this.mTextPaint;
      if ((paramInt & 0x2) != 0)
        f = -0.25F; 
      textPaint.setTextSkewX(f);
      return;
    } 
    this.mTextPaint.setFakeBoldText(false);
    this.mTextPaint.setTextSkewX(0.0F);
    setSwitchTypeface(textPaint);
  }
  
  public void setTextOff(CharSequence paramCharSequence) {
    this.mTextOff = paramCharSequence;
    requestLayout();
  }
  
  public void setTextOn(CharSequence paramCharSequence) {
    this.mTextOn = paramCharSequence;
    requestLayout();
  }
  
  public void setThumbDrawable(Drawable paramDrawable) {
    Drawable drawable = this.mThumbDrawable;
    if (drawable != null)
      drawable.setCallback(null); 
    this.mThumbDrawable = paramDrawable;
    if (paramDrawable != null)
      paramDrawable.setCallback(this); 
    requestLayout();
  }
  
  void setThumbPosition(float paramFloat) {
    this.mThumbPosition = paramFloat;
    invalidate();
  }
  
  public void setThumbResource(int paramInt) { setThumbDrawable(AppCompatResources.getDrawable(getContext(), paramInt)); }
  
  public void setThumbTextPadding(int paramInt) {
    this.mThumbTextPadding = paramInt;
    requestLayout();
  }
  
  public void setThumbTintList(@Nullable ColorStateList paramColorStateList) {
    this.mThumbTintList = paramColorStateList;
    this.mHasThumbTint = true;
    applyThumbTint();
  }
  
  public void setThumbTintMode(@Nullable PorterDuff.Mode paramMode) {
    this.mThumbTintMode = paramMode;
    this.mHasThumbTintMode = true;
    applyThumbTint();
  }
  
  public void setTrackDrawable(Drawable paramDrawable) {
    Drawable drawable = this.mTrackDrawable;
    if (drawable != null)
      drawable.setCallback(null); 
    this.mTrackDrawable = paramDrawable;
    if (paramDrawable != null)
      paramDrawable.setCallback(this); 
    requestLayout();
  }
  
  public void setTrackResource(int paramInt) { setTrackDrawable(AppCompatResources.getDrawable(getContext(), paramInt)); }
  
  public void setTrackTintList(@Nullable ColorStateList paramColorStateList) {
    this.mTrackTintList = paramColorStateList;
    this.mHasTrackTint = true;
    applyTrackTint();
  }
  
  public void setTrackTintMode(@Nullable PorterDuff.Mode paramMode) {
    this.mTrackTintMode = paramMode;
    this.mHasTrackTintMode = true;
    applyTrackTint();
  }
  
  public void toggle() { setChecked(isChecked() ^ true); }
  
  protected boolean verifyDrawable(Drawable paramDrawable) { return (super.verifyDrawable(paramDrawable) || paramDrawable == this.mThumbDrawable || paramDrawable == this.mTrackDrawable); }
}


/* Location:              D:\Apks\New folder\classes-dex2jar.jar!\android\support\v7\widget\SwitchCompat.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.0.7
 */