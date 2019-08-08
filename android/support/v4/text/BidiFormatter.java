package android.support.v4.text;

import android.text.SpannableStringBuilder;
import java.util.Locale;

public final class BidiFormatter {
  private static final int DEFAULT_FLAGS = 2;
  
  static final BidiFormatter DEFAULT_LTR_INSTANCE;
  
  static final BidiFormatter DEFAULT_RTL_INSTANCE;
  
  static final TextDirectionHeuristicCompat DEFAULT_TEXT_DIRECTION_HEURISTIC = TextDirectionHeuristicsCompat.FIRSTSTRONG_LTR;
  
  private static final int DIR_LTR = -1;
  
  private static final int DIR_RTL = 1;
  
  private static final int DIR_UNKNOWN = 0;
  
  private static final String EMPTY_STRING = "";
  
  private static final int FLAG_STEREO_RESET = 2;
  
  private static final char LRE = '‪';
  
  private static final char LRM = '‎';
  
  private static final String LRM_STRING = Character.toString('‎');
  
  private static final char PDF = '‬';
  
  private static final char RLE = '‫';
  
  private static final char RLM = '‏';
  
  private static final String RLM_STRING = Character.toString('‏');
  
  private final TextDirectionHeuristicCompat mDefaultTextDirectionHeuristicCompat;
  
  private final int mFlags;
  
  private final boolean mIsRtlContext;
  
  static  {
    DEFAULT_LTR_INSTANCE = new BidiFormatter(false, 2, DEFAULT_TEXT_DIRECTION_HEURISTIC);
    DEFAULT_RTL_INSTANCE = new BidiFormatter(true, 2, DEFAULT_TEXT_DIRECTION_HEURISTIC);
  }
  
  BidiFormatter(boolean paramBoolean, int paramInt, TextDirectionHeuristicCompat paramTextDirectionHeuristicCompat) {
    this.mIsRtlContext = paramBoolean;
    this.mFlags = paramInt;
    this.mDefaultTextDirectionHeuristicCompat = paramTextDirectionHeuristicCompat;
  }
  
  private static int getEntryDir(CharSequence paramCharSequence) { return (new DirectionalityEstimator(paramCharSequence, false)).getEntryDir(); }
  
  private static int getExitDir(CharSequence paramCharSequence) { return (new DirectionalityEstimator(paramCharSequence, false)).getExitDir(); }
  
  public static BidiFormatter getInstance() { return (new Builder()).build(); }
  
  public static BidiFormatter getInstance(Locale paramLocale) { return (new Builder(paramLocale)).build(); }
  
  public static BidiFormatter getInstance(boolean paramBoolean) { return (new Builder(paramBoolean)).build(); }
  
  static boolean isRtlLocale(Locale paramLocale) { return (TextUtilsCompat.getLayoutDirectionFromLocale(paramLocale) == 1); }
  
  private String markAfter(CharSequence paramCharSequence, TextDirectionHeuristicCompat paramTextDirectionHeuristicCompat) {
    boolean bool = paramTextDirectionHeuristicCompat.isRtl(paramCharSequence, 0, paramCharSequence.length());
    return (!this.mIsRtlContext && (bool || getExitDir(paramCharSequence) == 1)) ? LRM_STRING : ((this.mIsRtlContext && (!bool || getExitDir(paramCharSequence) == -1)) ? RLM_STRING : "");
  }
  
  private String markBefore(CharSequence paramCharSequence, TextDirectionHeuristicCompat paramTextDirectionHeuristicCompat) {
    boolean bool = paramTextDirectionHeuristicCompat.isRtl(paramCharSequence, 0, paramCharSequence.length());
    return (!this.mIsRtlContext && (bool || getEntryDir(paramCharSequence) == 1)) ? LRM_STRING : ((this.mIsRtlContext && (!bool || getEntryDir(paramCharSequence) == -1)) ? RLM_STRING : "");
  }
  
  public boolean getStereoReset() { return ((this.mFlags & 0x2) != 0); }
  
  public boolean isRtl(CharSequence paramCharSequence) { return this.mDefaultTextDirectionHeuristicCompat.isRtl(paramCharSequence, 0, paramCharSequence.length()); }
  
  public boolean isRtl(String paramString) { return isRtl(paramString); }
  
  public boolean isRtlContext() { return this.mIsRtlContext; }
  
  public CharSequence unicodeWrap(CharSequence paramCharSequence) { return unicodeWrap(paramCharSequence, this.mDefaultTextDirectionHeuristicCompat, true); }
  
  public CharSequence unicodeWrap(CharSequence paramCharSequence, TextDirectionHeuristicCompat paramTextDirectionHeuristicCompat) { return unicodeWrap(paramCharSequence, paramTextDirectionHeuristicCompat, true); }
  
  public CharSequence unicodeWrap(CharSequence paramCharSequence, TextDirectionHeuristicCompat paramTextDirectionHeuristicCompat, boolean paramBoolean) {
    if (paramCharSequence == null)
      return null; 
    boolean bool = paramTextDirectionHeuristicCompat.isRtl(paramCharSequence, 0, paramCharSequence.length());
    SpannableStringBuilder spannableStringBuilder = new SpannableStringBuilder();
    if (getStereoReset() && paramBoolean) {
      if (bool) {
        paramTextDirectionHeuristicCompat = TextDirectionHeuristicsCompat.RTL;
      } else {
        paramTextDirectionHeuristicCompat = TextDirectionHeuristicsCompat.LTR;
      } 
      spannableStringBuilder.append(markBefore(paramCharSequence, paramTextDirectionHeuristicCompat));
    } 
    if (bool != this.mIsRtlContext) {
      char c;
      if (bool) {
        c = '‫';
      } else {
        c = '‪';
      } 
      spannableStringBuilder.append(c);
      spannableStringBuilder.append(paramCharSequence);
      spannableStringBuilder.append('‬');
    } else {
      spannableStringBuilder.append(paramCharSequence);
    } 
    if (paramBoolean) {
      if (bool) {
        paramTextDirectionHeuristicCompat = TextDirectionHeuristicsCompat.RTL;
      } else {
        paramTextDirectionHeuristicCompat = TextDirectionHeuristicsCompat.LTR;
      } 
      spannableStringBuilder.append(markAfter(paramCharSequence, paramTextDirectionHeuristicCompat));
    } 
    return spannableStringBuilder;
  }
  
  public CharSequence unicodeWrap(CharSequence paramCharSequence, boolean paramBoolean) { return unicodeWrap(paramCharSequence, this.mDefaultTextDirectionHeuristicCompat, paramBoolean); }
  
  public String unicodeWrap(String paramString) { return unicodeWrap(paramString, this.mDefaultTextDirectionHeuristicCompat, true); }
  
  public String unicodeWrap(String paramString, TextDirectionHeuristicCompat paramTextDirectionHeuristicCompat) { return unicodeWrap(paramString, paramTextDirectionHeuristicCompat, true); }
  
  public String unicodeWrap(String paramString, TextDirectionHeuristicCompat paramTextDirectionHeuristicCompat, boolean paramBoolean) { return (paramString == null) ? null : unicodeWrap(paramString, paramTextDirectionHeuristicCompat, paramBoolean).toString(); }
  
  public String unicodeWrap(String paramString, boolean paramBoolean) { return unicodeWrap(paramString, this.mDefaultTextDirectionHeuristicCompat, paramBoolean); }
  
  public static final class Builder {
    private int mFlags;
    
    private boolean mIsRtlContext;
    
    private TextDirectionHeuristicCompat mTextDirectionHeuristicCompat;
    
    public Builder() { initialize(BidiFormatter.isRtlLocale(Locale.getDefault())); }
    
    public Builder(Locale param1Locale) { initialize(BidiFormatter.isRtlLocale(param1Locale)); }
    
    public Builder(boolean param1Boolean) { initialize(param1Boolean); }
    
    private static BidiFormatter getDefaultInstanceFromContext(boolean param1Boolean) { return param1Boolean ? BidiFormatter.DEFAULT_RTL_INSTANCE : BidiFormatter.DEFAULT_LTR_INSTANCE; }
    
    private void initialize(boolean param1Boolean) {
      this.mIsRtlContext = param1Boolean;
      this.mTextDirectionHeuristicCompat = BidiFormatter.DEFAULT_TEXT_DIRECTION_HEURISTIC;
      this.mFlags = 2;
    }
    
    public BidiFormatter build() { return (this.mFlags == 2 && this.mTextDirectionHeuristicCompat == BidiFormatter.DEFAULT_TEXT_DIRECTION_HEURISTIC) ? getDefaultInstanceFromContext(this.mIsRtlContext) : new BidiFormatter(this.mIsRtlContext, this.mFlags, this.mTextDirectionHeuristicCompat); }
    
    public Builder setTextDirectionHeuristic(TextDirectionHeuristicCompat param1TextDirectionHeuristicCompat) {
      this.mTextDirectionHeuristicCompat = param1TextDirectionHeuristicCompat;
      return this;
    }
    
    public Builder stereoReset(boolean param1Boolean) {
      if (param1Boolean) {
        this.mFlags |= 0x2;
        return this;
      } 
      this.mFlags &= 0xFFFFFFFD;
      return this;
    }
  }
  
  private static class DirectionalityEstimator {
    private static final byte[] DIR_TYPE_CACHE = new byte[1792];
    
    private static final int DIR_TYPE_CACHE_SIZE = 1792;
    
    private int charIndex;
    
    private final boolean isHtml;
    
    private char lastChar;
    
    private final int length;
    
    private final CharSequence text;
    
    static  {
      for (byte b = 0; b < '܀'; b++)
        DIR_TYPE_CACHE[b] = Character.getDirectionality(b); 
    }
    
    DirectionalityEstimator(CharSequence param1CharSequence, boolean param1Boolean) {
      this.text = param1CharSequence;
      this.isHtml = param1Boolean;
      this.length = param1CharSequence.length();
    }
    
    private static byte getCachedDirectionality(char param1Char) { return (param1Char < '܀') ? DIR_TYPE_CACHE[param1Char] : Character.getDirectionality(param1Char); }
    
    private byte skipEntityBackward() {
      int i = this.charIndex;
      while (true) {
        int j = this.charIndex;
        if (j > 0) {
          CharSequence charSequence = this.text;
          this.charIndex = --j;
          this.lastChar = charSequence.charAt(j);
          j = this.lastChar;
          if (j == 38)
            return 12; 
          if (j == 59)
            break; 
          continue;
        } 
        break;
      } 
      this.charIndex = i;
      this.lastChar = ';';
      return 13;
    }
    
    private byte skipEntityForward() {
      while (true) {
        int i = this.charIndex;
        if (i < this.length) {
          CharSequence charSequence = this.text;
          this.charIndex = i + 1;
          char c = charSequence.charAt(i);
          this.lastChar = c;
          if (c != ';')
            continue; 
        } 
        break;
      } 
      return 12;
    }
    
    private byte skipTagBackward() {
      int i = this.charIndex;
      label22: while (true) {
        int j = this.charIndex;
        if (j > 0) {
          CharSequence charSequence = this.text;
          this.charIndex = --j;
          this.lastChar = charSequence.charAt(j);
          j = this.lastChar;
          if (j == 60)
            return 12; 
          if (j == 62)
            break; 
          if (j == 34 || j == 39) {
            j = this.lastChar;
            while (true) {
              int k = this.charIndex;
              if (k > 0) {
                charSequence = this.text;
                this.charIndex = --k;
                char c = charSequence.charAt(k);
                this.lastChar = c;
                if (c != j)
                  continue; 
                continue label22;
              } 
              continue label22;
            } 
          } 
          continue;
        } 
        break;
      } 
      this.charIndex = i;
      this.lastChar = '>';
      return 13;
    }
    
    private byte skipTagForward() {
      int i = this.charIndex;
      label20: while (true) {
        int j = this.charIndex;
        if (j < this.length) {
          CharSequence charSequence = this.text;
          this.charIndex = j + 1;
          this.lastChar = charSequence.charAt(j);
          j = this.lastChar;
          if (j == 62)
            return 12; 
          if (j == 34 || j == 39) {
            j = this.lastChar;
            while (true) {
              int k = this.charIndex;
              if (k < this.length) {
                charSequence = this.text;
                this.charIndex = k + 1;
                char c = charSequence.charAt(k);
                this.lastChar = c;
                if (c != j)
                  continue; 
                continue label20;
              } 
              continue label20;
            } 
          } 
          continue;
        } 
        break;
      } 
      this.charIndex = i;
      this.lastChar = '<';
      return 13;
    }
    
    byte dirTypeBackward() {
      this.lastChar = this.text.charAt(this.charIndex - 1);
      if (Character.isLowSurrogate(this.lastChar)) {
        int i = Character.codePointBefore(this.text, this.charIndex);
        this.charIndex -= Character.charCount(i);
        return Character.getDirectionality(i);
      } 
      this.charIndex--;
      byte b2 = getCachedDirectionality(this.lastChar);
      byte b1 = b2;
      if (this.isHtml) {
        char c = this.lastChar;
        if (c == '>')
          return skipTagBackward(); 
        b1 = b2;
        if (c == ';')
          b1 = skipEntityBackward(); 
      } 
      return b1;
    }
    
    byte dirTypeForward() {
      this.lastChar = this.text.charAt(this.charIndex);
      if (Character.isHighSurrogate(this.lastChar)) {
        int i = Character.codePointAt(this.text, this.charIndex);
        this.charIndex += Character.charCount(i);
        return Character.getDirectionality(i);
      } 
      this.charIndex++;
      byte b2 = getCachedDirectionality(this.lastChar);
      byte b1 = b2;
      if (this.isHtml) {
        char c = this.lastChar;
        if (c == '<')
          return skipTagForward(); 
        b1 = b2;
        if (c == '&')
          b1 = skipEntityForward(); 
      } 
      return b1;
    }
    
    int getEntryDir() {
      this.charIndex = 0;
      byte b1 = 0;
      byte b = 0;
      byte b2 = 0;
      while (this.charIndex < this.length && !b2) {
        byte b3 = dirTypeForward();
        if (b3 != 9) {
          switch (b3) {
            default:
              switch (b3) {
                default:
                  b2 = b1;
                  continue;
                case 18:
                  b1--;
                  b = 0;
                  continue;
                case 16:
                case 17:
                  b1++;
                  b = 1;
                  continue;
                case 14:
                case 15:
                  break;
              } 
              b1++;
              b = -1;
              continue;
            case 1:
            case 2:
              if (b1 == 0)
                return 1; 
              b2 = b1;
              continue;
            case 0:
              break;
          } 
          if (b1 == 0)
            return -1; 
          b2 = b1;
        } 
      } 
      if (b2 == 0)
        return 0; 
      if (b != 0)
        return b; 
      while (this.charIndex > 0) {
        switch (dirTypeBackward()) {
          default:
            continue;
          case 18:
            b1++;
            continue;
          case 16:
          case 17:
            if (b2 == b1)
              return 1; 
            b1--;
            continue;
          case 14:
          case 15:
            break;
        } 
        if (b2 == b1)
          return -1; 
        b1--;
      } 
      return 0;
    }
    
    int getExitDir() {
      this.charIndex = this.length;
      byte b1 = 0;
      byte b2 = 0;
      while (this.charIndex > 0) {
        byte b = dirTypeBackward();
        if (b != 9) {
          switch (b) {
            default:
              switch (b) {
                default:
                  if (!b2)
                    b2 = b1; 
                  continue;
                case 18:
                  b1++;
                  continue;
                case 16:
                case 17:
                  if (b2 == b1)
                    return 1; 
                  b1--;
                  continue;
                case 14:
                case 15:
                  break;
              } 
              if (b2 == b1)
                return -1; 
              b1--;
              continue;
            case 1:
            case 2:
              if (b1 == 0)
                return 1; 
              if (b2 == 0)
                b2 = b1; 
              continue;
            case 0:
              break;
          } 
          if (b1 == 0)
            return -1; 
          if (b2 == 0)
            b2 = b1; 
        } 
      } 
      return 0;
    }
  }
}


/* Location:              D:\Apks\New folder\classes-dex2jar.jar!\android\support\v4\text\BidiFormatter.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.0.7
 */