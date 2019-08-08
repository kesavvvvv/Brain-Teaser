package android.support.v4.graphics;

import android.graphics.Path;
import android.support.annotation.RestrictTo;
import android.util.Log;
import java.util.ArrayList;

@RestrictTo({RestrictTo.Scope.LIBRARY_GROUP})
public class PathParser {
  private static final String LOGTAG = "PathParser";
  
  private static void addNode(ArrayList<PathDataNode> paramArrayList, char paramChar, float[] paramArrayOfFloat) { paramArrayList.add(new PathDataNode(paramChar, paramArrayOfFloat)); }
  
  public static boolean canMorph(PathDataNode[] paramArrayOfPathDataNode1, PathDataNode[] paramArrayOfPathDataNode2) {
    if (paramArrayOfPathDataNode1 != null) {
      if (paramArrayOfPathDataNode2 == null)
        return false; 
      if (paramArrayOfPathDataNode1.length != paramArrayOfPathDataNode2.length)
        return false; 
      byte b = 0;
      while (b < paramArrayOfPathDataNode1.length) {
        if ((paramArrayOfPathDataNode1[b]).mType == (paramArrayOfPathDataNode2[b]).mType) {
          if ((paramArrayOfPathDataNode1[b]).mParams.length != (paramArrayOfPathDataNode2[b]).mParams.length)
            return false; 
          b++;
          continue;
        } 
        return false;
      } 
      return true;
    } 
    return false;
  }
  
  static float[] copyOfRange(float[] paramArrayOfFloat, int paramInt1, int paramInt2) {
    if (paramInt1 <= paramInt2) {
      int i = paramArrayOfFloat.length;
      if (paramInt1 >= 0 && paramInt1 <= i) {
        paramInt2 -= paramInt1;
        i = Math.min(paramInt2, i - paramInt1);
        float[] arrayOfFloat = new float[paramInt2];
        System.arraycopy(paramArrayOfFloat, paramInt1, arrayOfFloat, 0, i);
        return arrayOfFloat;
      } 
      throw new ArrayIndexOutOfBoundsException();
    } 
    throw new IllegalArgumentException();
  }
  
  public static PathDataNode[] createNodesFromPathData(String paramString) {
    if (paramString == null)
      return null; 
    int i = 0;
    int j = 1;
    ArrayList arrayList = new ArrayList();
    while (j < paramString.length()) {
      j = nextStart(paramString, j);
      String str = paramString.substring(i, j).trim();
      if (str.length() > 0) {
        float[] arrayOfFloat = getFloats(str);
        addNode(arrayList, str.charAt(0), arrayOfFloat);
      } 
      i = j;
      j++;
    } 
    if (j - i == 1 && i < paramString.length())
      addNode(arrayList, paramString.charAt(i), new float[0]); 
    return (PathDataNode[])arrayList.toArray(new PathDataNode[arrayList.size()]);
  }
  
  public static Path createPathFromPathData(String paramString) {
    path = new Path();
    PathDataNode[] arrayOfPathDataNode = createNodesFromPathData(paramString);
    if (arrayOfPathDataNode != null)
      try {
        PathDataNode.nodesToPath(arrayOfPathDataNode, path);
        return path;
      } catch (RuntimeException path) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("Error in parsing ");
        stringBuilder.append(paramString);
        throw new RuntimeException(stringBuilder.toString(), path);
      }  
    return null;
  }
  
  public static PathDataNode[] deepCopyNodes(PathDataNode[] paramArrayOfPathDataNode) {
    if (paramArrayOfPathDataNode == null)
      return null; 
    PathDataNode[] arrayOfPathDataNode = new PathDataNode[paramArrayOfPathDataNode.length];
    for (byte b = 0; b < paramArrayOfPathDataNode.length; b++)
      arrayOfPathDataNode[b] = new PathDataNode(paramArrayOfPathDataNode[b]); 
    return arrayOfPathDataNode;
  }
  
  private static void extract(String paramString, int paramInt, ExtractFloatResult paramExtractFloatResult) {
    int i = paramInt;
    char c = Character.MIN_VALUE;
    paramExtractFloatResult.mEndWithNegOrDot = false;
    byte b1 = 0;
    byte b2 = 0;
    while (i < paramString.length()) {
      byte b = 0;
      char c1 = paramString.charAt(i);
      if (c1 != ' ') {
        byte b4;
        byte b3;
        if (c1 != 'E' && c1 != 'e') {
          switch (c1) {
            default:
              c1 = c;
              b4 = b1;
              b3 = b;
              break;
            case '.':
              if (!b1) {
                b4 = 1;
                c1 = c;
                b3 = b;
                break;
              } 
              c1 = '\001';
              paramExtractFloatResult.mEndWithNegOrDot = true;
              b4 = b1;
              b3 = b;
              break;
            case '-':
              c1 = c;
              b4 = b1;
              b3 = b;
              if (i != paramInt) {
                c1 = c;
                b4 = b1;
                b3 = b;
                if (!b2) {
                  c1 = '\001';
                  paramExtractFloatResult.mEndWithNegOrDot = true;
                  b4 = b1;
                  b3 = b;
                } 
              } 
              break;
            case ',':
              c1 = '\001';
              b3 = b;
              b4 = b1;
              break;
          } 
        } else {
          b3 = 1;
          c1 = c;
          b4 = b1;
        } 
        if (c1 != '\000')
          break; 
        i++;
        c = c1;
        b1 = b4;
        b2 = b3;
      } 
    } 
    paramExtractFloatResult.mEndPosition = i;
  }
  
  private static float[] getFloats(String paramString) {
    if (paramString.charAt(0) == 'z' || paramString.charAt(0) == 'Z')
      return new float[0]; 
    try {
      float[] arrayOfFloat = new float[paramString.length()];
      byte b = 0;
      int i = 1;
      ExtractFloatResult extractFloatResult = new ExtractFloatResult();
      int j = paramString.length();
      while (true) {
        int k;
        byte b1;
        if (i < j) {
          extract(paramString, i, extractFloatResult);
          k = extractFloatResult.mEndPosition;
          b1 = b;
          if (i < k) {
            arrayOfFloat[b] = Float.parseFloat(paramString.substring(i, k));
            b1 = b + true;
          } 
          if (extractFloatResult.mEndWithNegOrDot) {
            i = k;
            b = b1;
            continue;
          } 
        } else {
          return copyOfRange(arrayOfFloat, 0, b);
        } 
        i = k + 1;
        b = b1;
      } 
    } catch (NumberFormatException numberFormatException) {
      StringBuilder stringBuilder = new StringBuilder();
      stringBuilder.append("error in parsing \"");
      stringBuilder.append(paramString);
      stringBuilder.append("\"");
      throw new RuntimeException(stringBuilder.toString(), numberFormatException);
    } 
  }
  
  private static int nextStart(String paramString, int paramInt) {
    while (paramInt < paramString.length()) {
      char c = paramString.charAt(paramInt);
      if (((c - 'A') * (c - 'Z') <= '\000' || (c - 'a') * (c - 'z') <= '\000') && c != 'e' && c != 'E')
        return paramInt; 
      paramInt++;
    } 
    return paramInt;
  }
  
  public static void updateNodes(PathDataNode[] paramArrayOfPathDataNode1, PathDataNode[] paramArrayOfPathDataNode2) {
    for (byte b = 0; b < paramArrayOfPathDataNode2.length; b++) {
      (paramArrayOfPathDataNode1[b]).mType = (paramArrayOfPathDataNode2[b]).mType;
      for (byte b1 = 0; b1 < (paramArrayOfPathDataNode2[b]).mParams.length; b1++)
        (paramArrayOfPathDataNode1[b]).mParams[b1] = (paramArrayOfPathDataNode2[b]).mParams[b1]; 
    } 
  }
  
  private static class ExtractFloatResult {
    int mEndPosition;
    
    boolean mEndWithNegOrDot;
  }
  
  public static class PathDataNode {
    @RestrictTo({RestrictTo.Scope.LIBRARY_GROUP})
    public float[] mParams;
    
    @RestrictTo({RestrictTo.Scope.LIBRARY_GROUP})
    public char mType;
    
    PathDataNode(char param1Char, float[] param1ArrayOfFloat) {
      this.mType = param1Char;
      this.mParams = param1ArrayOfFloat;
    }
    
    PathDataNode(PathDataNode param1PathDataNode) {
      this.mType = param1PathDataNode.mType;
      float[] arrayOfFloat = param1PathDataNode.mParams;
      this.mParams = PathParser.copyOfRange(arrayOfFloat, 0, arrayOfFloat.length);
    }
    
    private static void addCommand(Path param1Path, float[] param1ArrayOfFloat1, char param1Char1, char param1Char2, float[] param1ArrayOfFloat2) {
      char c1;
      float f3 = param1ArrayOfFloat1[0];
      float f4 = param1ArrayOfFloat1[1];
      float f5 = param1ArrayOfFloat1[2];
      float f6 = param1ArrayOfFloat1[3];
      float f2 = param1ArrayOfFloat1[4];
      float f1 = param1ArrayOfFloat1[5];
      switch (param1Char2) {
        default:
          c1 = '\002';
          break;
        case 'Z':
        case 'z':
          param1Path.close();
          f3 = f2;
          f4 = f1;
          f5 = f2;
          f6 = f1;
          param1Path.moveTo(f3, f4);
          c1 = '\002';
          break;
        case 'Q':
        case 'S':
        case 'q':
        case 's':
          c1 = '\004';
          break;
        case 'L':
        case 'M':
        case 'T':
        case 'l':
        case 'm':
        case 't':
          c1 = '\002';
          break;
        case 'H':
        case 'V':
        case 'h':
        case 'v':
          c1 = '\001';
          break;
        case 'C':
        case 'c':
          c1 = '\006';
          break;
        case 'A':
        case 'a':
          c1 = '\007';
          break;
      } 
      char c = Character.MIN_VALUE;
      float f8 = f5;
      float f7 = f6;
      char c2 = param1Char1;
      f6 = f1;
      f5 = f2;
      param1Char1 = c;
      f2 = f3;
      f1 = f4;
      while (param1Char1 < param1ArrayOfFloat2.length) {
        boolean bool2;
        boolean bool1;
        float f;
        switch (param1Char2) {
          default:
            f3 = f8;
            f4 = f7;
            break;
          case 'v':
            param1Path.rLineTo(0.0F, param1ArrayOfFloat2[param1Char1 + Character.MIN_VALUE]);
            f1 += param1ArrayOfFloat2[param1Char1 + Character.MIN_VALUE];
            f3 = f8;
            f4 = f7;
            break;
          case 't':
            f4 = 0.0F;
            f3 = 0.0F;
            if (c2 == 'q' || c2 == 't' || c2 == 'Q' || c2 == 'T') {
              f4 = f2 - f8;
              f3 = f1 - f7;
            } 
            param1Path.rQuadTo(f4, f3, param1ArrayOfFloat2[param1Char1 + Character.MIN_VALUE], param1ArrayOfFloat2[param1Char1 + '\001']);
            f7 = f2 + param1ArrayOfFloat2[param1Char1 + Character.MIN_VALUE];
            f8 = f1 + param1ArrayOfFloat2[param1Char1 + '\001'];
            f4 = f2 + f4;
            f = f1 + f3;
            f1 = f8;
            f2 = f7;
            f3 = f4;
            f4 = f;
            break;
          case 's':
            if (c2 == 'c' || c2 == 's' || c2 == 'C' || c2 == 'S') {
              f3 = f2 - f8;
              f4 = f1 - f7;
            } else {
              f3 = 0.0F;
              f4 = 0.0F;
            } 
            param1Path.rCubicTo(f3, f4, param1ArrayOfFloat2[param1Char1 + Character.MIN_VALUE], param1ArrayOfFloat2[param1Char1 + '\001'], param1ArrayOfFloat2[param1Char1 + '\002'], param1ArrayOfFloat2[param1Char1 + '\003']);
            f7 = param1ArrayOfFloat2[param1Char1 + Character.MIN_VALUE];
            f8 = param1ArrayOfFloat2[param1Char1 + '\001'];
            f3 = f2 + param1ArrayOfFloat2[param1Char1 + '\002'];
            f4 = f1 + param1ArrayOfFloat2[param1Char1 + '\003'];
            f7 += f2;
            f8 += f1;
            f1 = f4;
            f2 = f3;
            f3 = f7;
            f4 = f8;
            break;
          case 'q':
            param1Path.rQuadTo(param1ArrayOfFloat2[param1Char1 + Character.MIN_VALUE], param1ArrayOfFloat2[param1Char1 + '\001'], param1ArrayOfFloat2[param1Char1 + '\002'], param1ArrayOfFloat2[param1Char1 + '\003']);
            f7 = param1ArrayOfFloat2[param1Char1 + Character.MIN_VALUE];
            f8 = param1ArrayOfFloat2[param1Char1 + '\001'];
            f3 = f2 + param1ArrayOfFloat2[param1Char1 + '\002'];
            f4 = f1 + param1ArrayOfFloat2[param1Char1 + '\003'];
            f7 += f2;
            f8 += f1;
            f1 = f4;
            f2 = f3;
            f3 = f7;
            f4 = f8;
            break;
          case 'm':
            f2 += param1ArrayOfFloat2[param1Char1 + Character.MIN_VALUE];
            f1 += param1ArrayOfFloat2[param1Char1 + '\001'];
            if (param1Char1 > '\000') {
              param1Path.rLineTo(param1ArrayOfFloat2[param1Char1 + Character.MIN_VALUE], param1ArrayOfFloat2[param1Char1 + '\001']);
              f3 = f8;
              f4 = f7;
              break;
            } 
            param1Path.rMoveTo(param1ArrayOfFloat2[param1Char1 + Character.MIN_VALUE], param1ArrayOfFloat2[param1Char1 + '\001']);
            f5 = f2;
            f6 = f1;
            f3 = f8;
            f4 = f7;
            break;
          case 'l':
            param1Path.rLineTo(param1ArrayOfFloat2[param1Char1 + Character.MIN_VALUE], param1ArrayOfFloat2[param1Char1 + '\001']);
            f2 += param1ArrayOfFloat2[param1Char1 + Character.MIN_VALUE];
            f1 += param1ArrayOfFloat2[param1Char1 + '\001'];
            f3 = f8;
            f4 = f7;
            break;
          case 'h':
            param1Path.rLineTo(param1ArrayOfFloat2[param1Char1 + Character.MIN_VALUE], 0.0F);
            f2 += param1ArrayOfFloat2[param1Char1 + Character.MIN_VALUE];
            f3 = f8;
            f4 = f7;
            break;
          case 'c':
            param1Path.rCubicTo(param1ArrayOfFloat2[param1Char1 + Character.MIN_VALUE], param1ArrayOfFloat2[param1Char1 + '\001'], param1ArrayOfFloat2[param1Char1 + '\002'], param1ArrayOfFloat2[param1Char1 + '\003'], param1ArrayOfFloat2[param1Char1 + '\004'], param1ArrayOfFloat2[param1Char1 + '\005']);
            f7 = param1ArrayOfFloat2[param1Char1 + '\002'];
            f8 = param1ArrayOfFloat2[param1Char1 + '\003'];
            f3 = f2 + param1ArrayOfFloat2[param1Char1 + '\004'];
            f4 = f1 + param1ArrayOfFloat2[param1Char1 + '\005'];
            f7 += f2;
            f8 += f1;
            f1 = f4;
            f2 = f3;
            f3 = f7;
            f4 = f8;
            break;
          case 'a':
            f3 = param1ArrayOfFloat2[param1Char1 + '\005'];
            f4 = param1ArrayOfFloat2[param1Char1 + '\006'];
            f7 = param1ArrayOfFloat2[param1Char1 + Character.MIN_VALUE];
            f8 = param1ArrayOfFloat2[param1Char1 + '\001'];
            f = param1ArrayOfFloat2[param1Char1 + '\002'];
            if (param1ArrayOfFloat2[param1Char1 + '\003'] != 0.0F) {
              bool1 = true;
            } else {
              bool1 = false;
            } 
            if (param1ArrayOfFloat2[param1Char1 + '\004'] != 0.0F) {
              bool2 = true;
            } else {
              bool2 = false;
            } 
            c2 = param1Char1;
            drawArc(param1Path, f2, f1, f3 + f2, f4 + f1, f7, f8, f, bool1, bool2);
            f2 += param1ArrayOfFloat2[c2 + '\005'];
            f1 += param1ArrayOfFloat2[c2 + '\006'];
            f3 = f2;
            f4 = f1;
            break;
          case 'V':
            c2 = param1Char1;
            param1Path.lineTo(f2, param1ArrayOfFloat2[c2 + Character.MIN_VALUE]);
            f1 = param1ArrayOfFloat2[c2 + Character.MIN_VALUE];
            f3 = f8;
            f4 = f7;
            break;
          case 'T':
            c = param1Char1;
            f4 = f2;
            f3 = f1;
            if (c2 == 'q' || c2 == 't' || c2 == 'Q' || c2 == 'T') {
              f4 = f2 * 2.0F - f8;
              f3 = f1 * 2.0F - f7;
            } 
            param1Path.quadTo(f4, f3, param1ArrayOfFloat2[c + Character.MIN_VALUE], param1ArrayOfFloat2[c + '\001']);
            f2 = param1ArrayOfFloat2[c + Character.MIN_VALUE];
            f1 = param1ArrayOfFloat2[c + '\001'];
            f7 = f3;
            f3 = f4;
            f4 = f7;
            break;
          case 'S':
            c = param1Char1;
            if (c2 == 'c' || c2 == 's' || c2 == 'C' || c2 == 'S') {
              f2 = f2 * 2.0F - f8;
              f1 = f1 * 2.0F - f7;
            } 
            param1Path.cubicTo(f2, f1, param1ArrayOfFloat2[c + Character.MIN_VALUE], param1ArrayOfFloat2[c + '\001'], param1ArrayOfFloat2[c + '\002'], param1ArrayOfFloat2[c + '\003']);
            f3 = param1ArrayOfFloat2[c + Character.MIN_VALUE];
            f4 = param1ArrayOfFloat2[c + '\001'];
            f2 = param1ArrayOfFloat2[c + '\002'];
            f1 = param1ArrayOfFloat2[c + '\003'];
            break;
          case 'Q':
            c2 = param1Char1;
            param1Path.quadTo(param1ArrayOfFloat2[c2 + Character.MIN_VALUE], param1ArrayOfFloat2[c2 + '\001'], param1ArrayOfFloat2[c2 + '\002'], param1ArrayOfFloat2[c2 + '\003']);
            f3 = param1ArrayOfFloat2[c2 + Character.MIN_VALUE];
            f4 = param1ArrayOfFloat2[c2 + '\001'];
            f2 = param1ArrayOfFloat2[c2 + '\002'];
            f1 = param1ArrayOfFloat2[c2 + '\003'];
            break;
          case 'M':
            c2 = param1Char1;
            f2 = param1ArrayOfFloat2[c2 + Character.MIN_VALUE];
            f1 = param1ArrayOfFloat2[c2 + '\001'];
            if (c2 > '\000') {
              param1Path.lineTo(param1ArrayOfFloat2[c2 + Character.MIN_VALUE], param1ArrayOfFloat2[c2 + '\001']);
              f3 = f8;
              f4 = f7;
              break;
            } 
            param1Path.moveTo(param1ArrayOfFloat2[c2 + Character.MIN_VALUE], param1ArrayOfFloat2[c2 + '\001']);
            f5 = f2;
            f6 = f1;
            f3 = f8;
            f4 = f7;
            break;
          case 'L':
            c2 = param1Char1;
            param1Path.lineTo(param1ArrayOfFloat2[c2 + Character.MIN_VALUE], param1ArrayOfFloat2[c2 + '\001']);
            f2 = param1ArrayOfFloat2[c2 + Character.MIN_VALUE];
            f1 = param1ArrayOfFloat2[c2 + '\001'];
            f3 = f8;
            f4 = f7;
            break;
          case 'H':
            c2 = param1Char1;
            param1Path.lineTo(param1ArrayOfFloat2[c2 + Character.MIN_VALUE], f1);
            f2 = param1ArrayOfFloat2[c2 + Character.MIN_VALUE];
            f3 = f8;
            f4 = f7;
            break;
          case 'C':
            c2 = param1Char1;
            param1Path.cubicTo(param1ArrayOfFloat2[c2 + Character.MIN_VALUE], param1ArrayOfFloat2[c2 + '\001'], param1ArrayOfFloat2[c2 + '\002'], param1ArrayOfFloat2[c2 + '\003'], param1ArrayOfFloat2[c2 + '\004'], param1ArrayOfFloat2[c2 + '\005']);
            f2 = param1ArrayOfFloat2[c2 + '\004'];
            f1 = param1ArrayOfFloat2[c2 + '\005'];
            f3 = param1ArrayOfFloat2[c2 + '\002'];
            f4 = param1ArrayOfFloat2[c2 + '\003'];
            break;
          case 'A':
            c2 = param1Char1;
            f3 = param1ArrayOfFloat2[c2 + '\005'];
            f4 = param1ArrayOfFloat2[c2 + '\006'];
            f7 = param1ArrayOfFloat2[c2 + Character.MIN_VALUE];
            f8 = param1ArrayOfFloat2[c2 + '\001'];
            f = param1ArrayOfFloat2[c2 + '\002'];
            if (param1ArrayOfFloat2[c2 + '\003'] != 0.0F) {
              bool1 = true;
            } else {
              bool1 = false;
            } 
            if (param1ArrayOfFloat2[c2 + '\004'] != 0.0F) {
              bool2 = true;
            } else {
              bool2 = false;
            } 
            drawArc(param1Path, f2, f1, f3, f4, f7, f8, f, bool1, bool2);
            f2 = param1ArrayOfFloat2[c2 + '\005'];
            f1 = param1ArrayOfFloat2[c2 + '\006'];
            f3 = f2;
            f4 = f1;
            break;
        } 
        c2 = param1Char2;
        param1Char1 += c1;
        f8 = f3;
        f7 = f4;
      } 
      param1ArrayOfFloat1[0] = f2;
      param1ArrayOfFloat1[1] = f1;
      param1ArrayOfFloat1[2] = f8;
      param1ArrayOfFloat1[3] = f7;
      param1ArrayOfFloat1[4] = f5;
      param1ArrayOfFloat1[5] = f6;
    }
    
    private static void arcToBezier(Path param1Path, double param1Double1, double param1Double2, double param1Double3, double param1Double4, double param1Double5, double param1Double6, double param1Double7, double param1Double8, double param1Double9) {
      double d1 = param1Double3;
      int i = (int)Math.ceil(Math.abs(param1Double9 * 4.0D / Math.PI));
      double d6 = Math.cos(param1Double7);
      double d7 = Math.sin(param1Double7);
      param1Double7 = Math.cos(param1Double8);
      double d9 = Math.sin(param1Double8);
      double d2 = -d1;
      d1 = -d1;
      double d3 = i;
      Double.isNaN(d3);
      double d8 = param1Double9 / d3;
      d3 = d2 * d6 * d9 - param1Double4 * d7 * param1Double7;
      double d4 = d1 * d7 * d9 + param1Double4 * d6 * param1Double7;
      byte b = 0;
      double d5 = param1Double8;
      d2 = param1Double6;
      d1 = param1Double5;
      param1Double9 = d9;
      param1Double5 = d7;
      param1Double6 = d6;
      param1Double8 = d8;
      while (true) {
        d9 = param1Double3;
        if (b < i) {
          double d10 = d5 + param1Double8;
          double d11 = Math.sin(d10);
          double d12 = Math.cos(d10);
          d8 = param1Double1 + d9 * param1Double6 * d12 - param1Double4 * param1Double5 * d11;
          d7 = param1Double2 + d9 * param1Double5 * d12 + param1Double4 * param1Double6 * d11;
          d6 = -d9 * param1Double6 * d11 - param1Double4 * param1Double5 * d12;
          d9 = -d9 * param1Double5 * d11 + param1Double4 * param1Double6 * d12;
          d11 = Math.tan((d10 - d5) / 2.0D);
          d5 = Math.sin(d10 - d5) * (Math.sqrt(d11 * 3.0D * d11 + 4.0D) - 1.0D) / 3.0D;
          param1Path.rLineTo(0.0F, 0.0F);
          param1Path.cubicTo((float)(d1 + d5 * d3), (float)(d2 + d5 * d4), (float)(d8 - d5 * d6), (float)(d7 - d5 * d9), (float)d8, (float)d7);
          d5 = d10;
          d1 = d8;
          d2 = d7;
          d3 = d6;
          d4 = d9;
          b++;
          continue;
        } 
        break;
      } 
    }
    
    private static void drawArc(Path param1Path, float param1Float1, float param1Float2, float param1Float3, float param1Float4, float param1Float5, float param1Float6, float param1Float7, boolean param1Boolean1, boolean param1Boolean2) {
      double d5 = Math.toRadians(param1Float7);
      double d6 = Math.cos(d5);
      double d7 = Math.sin(d5);
      double d1 = param1Float1;
      Double.isNaN(d1);
      double d2 = param1Float2;
      Double.isNaN(d2);
      double d3 = param1Float5;
      Double.isNaN(d3);
      d1 = (d1 * d6 + d2 * d7) / d3;
      d2 = -param1Float1;
      Double.isNaN(d2);
      d3 = param1Float2;
      Double.isNaN(d3);
      double d4 = param1Float6;
      Double.isNaN(d4);
      d4 = (d2 * d7 + d3 * d6) / d4;
      d2 = param1Float3;
      Double.isNaN(d2);
      d3 = param1Float4;
      Double.isNaN(d3);
      double d8 = param1Float5;
      Double.isNaN(d8);
      d8 = (d2 * d6 + d3 * d7) / d8;
      d2 = -param1Float3;
      Double.isNaN(d2);
      d3 = param1Float4;
      Double.isNaN(d3);
      double d9 = param1Float6;
      Double.isNaN(d9);
      d9 = (d2 * d7 + d3 * d6) / d9;
      double d11 = d1 - d8;
      double d10 = d4 - d9;
      d3 = (d1 + d8) / 2.0D;
      d2 = (d4 + d9) / 2.0D;
      double d12 = d11 * d11 + d10 * d10;
      if (d12 == 0.0D) {
        Log.w("PathParser", " Points are coincident");
        return;
      } 
      double d13 = 1.0D / d12 - 0.25D;
      if (d13 < 0.0D) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("Points are too far apart ");
        stringBuilder.append(d12);
        Log.w("PathParser", stringBuilder.toString());
        float f = (float)(Math.sqrt(d12) / 1.99999D);
        drawArc(param1Path, param1Float1, param1Float2, param1Float3, param1Float4, param1Float5 * f, param1Float6 * f, param1Float7, param1Boolean1, param1Boolean2);
        return;
      } 
      d12 = Math.sqrt(d13);
      d11 = d12 * d11;
      d10 = d12 * d10;
      if (param1Boolean1 == param1Boolean2) {
        d3 -= d10;
        d2 += d11;
      } else {
        d3 += d10;
        d2 -= d11;
      } 
      d10 = Math.atan2(d4 - d2, d1 - d3);
      d4 = Math.atan2(d9 - d2, d8 - d3) - d10;
      if (d4 >= 0.0D) {
        param1Boolean1 = true;
      } else {
        param1Boolean1 = false;
      } 
      d1 = d4;
      if (param1Boolean2 != param1Boolean1)
        if (d4 > 0.0D) {
          d1 = d4 - 6.283185307179586D;
        } else {
          d1 = d4 + 6.283185307179586D;
        }  
      d4 = param1Float5;
      Double.isNaN(d4);
      d3 *= d4;
      d4 = param1Float6;
      Double.isNaN(d4);
      d2 = d4 * d2;
      arcToBezier(param1Path, d3 * d6 - d2 * d7, d3 * d7 + d2 * d6, param1Float5, param1Float6, param1Float1, param1Float2, d5, d10, d1);
    }
    
    public static void nodesToPath(PathDataNode[] param1ArrayOfPathDataNode, Path param1Path) {
      float[] arrayOfFloat = new float[6];
      char c = 'm';
      for (byte b = 0; b < param1ArrayOfPathDataNode.length; b++) {
        addCommand(param1Path, arrayOfFloat, c, (param1ArrayOfPathDataNode[b]).mType, (param1ArrayOfPathDataNode[b]).mParams);
        c = (param1ArrayOfPathDataNode[b]).mType;
      } 
    }
    
    public void interpolatePathDataNode(PathDataNode param1PathDataNode1, PathDataNode param1PathDataNode2, float param1Float) {
      byte b = 0;
      while (true) {
        float[] arrayOfFloat = param1PathDataNode1.mParams;
        if (b < arrayOfFloat.length) {
          this.mParams[b] = arrayOfFloat[b] * (1.0F - param1Float) + param1PathDataNode2.mParams[b] * param1Float;
          b++;
          continue;
        } 
        break;
      } 
    }
  }
}


/* Location:              D:\Apks\New folder\classes-dex2jar.jar!\android\support\v4\graphics\PathParser.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.0.7
 */