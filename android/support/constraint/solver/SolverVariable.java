package android.support.constraint.solver;

import java.util.Arrays;

public class SolverVariable {
  private static final boolean INTERNAL_DEBUG = false;
  
  static final int MAX_STRENGTH = 7;
  
  public static final int STRENGTH_BARRIER = 7;
  
  public static final int STRENGTH_EQUALITY = 5;
  
  public static final int STRENGTH_FIXED = 6;
  
  public static final int STRENGTH_HIGH = 3;
  
  public static final int STRENGTH_HIGHEST = 4;
  
  public static final int STRENGTH_LOW = 1;
  
  public static final int STRENGTH_MEDIUM = 2;
  
  public static final int STRENGTH_NONE = 0;
  
  private static int uniqueConstantId;
  
  private static int uniqueErrorId;
  
  private static int uniqueId;
  
  private static int uniqueSlackId = 1;
  
  private static int uniqueUnrestrictedId;
  
  public float computedValue;
  
  int definitionId = -1;
  
  public int id = -1;
  
  ArrayRow[] mClientEquations = new ArrayRow[8];
  
  int mClientEquationsCount = 0;
  
  private String mName;
  
  Type mType;
  
  public int strength = 0;
  
  float[] strengthVector = new float[7];
  
  public int usageInRowCount = 0;
  
  static  {
    uniqueErrorId = 1;
    uniqueUnrestrictedId = 1;
    uniqueConstantId = 1;
    uniqueId = 1;
  }
  
  public SolverVariable(Type paramType, String paramString) { this.mType = paramType; }
  
  public SolverVariable(String paramString, Type paramType) {
    this.mName = paramString;
    this.mType = paramType;
  }
  
  private static String getUniqueName(Type paramType, String paramString) {
    if (paramString != null) {
      stringBuilder = new StringBuilder();
      stringBuilder.append(paramString);
      stringBuilder.append(uniqueErrorId);
      return stringBuilder.toString();
    } 
    switch (stringBuilder) {
      default:
        throw new AssertionError(stringBuilder.name());
      case UNKNOWN:
        stringBuilder = new StringBuilder();
        stringBuilder.append("V");
        i = uniqueId + 1;
        uniqueId = i;
        stringBuilder.append(i);
        return stringBuilder.toString();
      case ERROR:
        stringBuilder = new StringBuilder();
        stringBuilder.append("e");
        i = uniqueErrorId + 1;
        uniqueErrorId = i;
        stringBuilder.append(i);
        return stringBuilder.toString();
      case SLACK:
        stringBuilder = new StringBuilder();
        stringBuilder.append("S");
        i = uniqueSlackId + 1;
        uniqueSlackId = i;
        stringBuilder.append(i);
        return stringBuilder.toString();
      case CONSTANT:
        stringBuilder = new StringBuilder();
        stringBuilder.append("C");
        i = uniqueConstantId + 1;
        uniqueConstantId = i;
        stringBuilder.append(i);
        return stringBuilder.toString();
      case UNRESTRICTED:
        break;
    } 
    StringBuilder stringBuilder = new StringBuilder();
    stringBuilder.append("U");
    int i = uniqueUnrestrictedId + 1;
    uniqueUnrestrictedId = i;
    stringBuilder.append(i);
    return stringBuilder.toString();
  }
  
  static void increaseErrorId() { uniqueErrorId++; }
  
  public final void addToRow(ArrayRow paramArrayRow) {
    int j;
    int i = 0;
    while (true) {
      j = this.mClientEquationsCount;
      if (i < j) {
        if (this.mClientEquations[i] == paramArrayRow)
          return; 
        i++;
        continue;
      } 
      break;
    } 
    ArrayRow[] arrayOfArrayRow = this.mClientEquations;
    if (j >= arrayOfArrayRow.length)
      this.mClientEquations = (ArrayRow[])Arrays.copyOf(arrayOfArrayRow, arrayOfArrayRow.length * 2); 
    arrayOfArrayRow = this.mClientEquations;
    i = this.mClientEquationsCount;
    arrayOfArrayRow[i] = paramArrayRow;
    this.mClientEquationsCount = i + 1;
  }
  
  void clearStrengths() {
    for (byte b = 0; b < 7; b++)
      this.strengthVector[b] = 0.0F; 
  }
  
  public String getName() { return this.mName; }
  
  public final void removeFromRow(ArrayRow paramArrayRow) {
    int j = this.mClientEquationsCount;
    for (int i = 0; i < j; i++) {
      if (this.mClientEquations[i] == paramArrayRow) {
        for (int k = 0; k < j - i - 1; k++) {
          ArrayRow[] arrayOfArrayRow = this.mClientEquations;
          arrayOfArrayRow[i + k] = arrayOfArrayRow[i + k + 1];
        } 
        this.mClientEquationsCount--;
        return;
      } 
    } 
  }
  
  public void reset() {
    this.mName = null;
    this.mType = Type.UNKNOWN;
    this.strength = 0;
    this.id = -1;
    this.definitionId = -1;
    this.computedValue = 0.0F;
    this.mClientEquationsCount = 0;
    this.usageInRowCount = 0;
  }
  
  public void setName(String paramString) { this.mName = paramString; }
  
  public void setType(Type paramType, String paramString) { this.mType = paramType; }
  
  String strengthsToString() {
    StringBuilder stringBuilder = new StringBuilder();
    stringBuilder.append(this);
    stringBuilder.append("[");
    String str1 = stringBuilder.toString();
    boolean bool1 = false;
    boolean bool2 = true;
    for (byte b = 0; b < this.strengthVector.length; b++) {
      StringBuilder stringBuilder1 = new StringBuilder();
      stringBuilder1.append(str1);
      stringBuilder1.append(this.strengthVector[b]);
      str1 = stringBuilder1.toString();
      float[] arrayOfFloat = this.strengthVector;
      if (arrayOfFloat[b] > 0.0F) {
        bool1 = false;
      } else if (arrayOfFloat[b] < 0.0F) {
        bool1 = true;
      } 
      if (this.strengthVector[b] != 0.0F)
        bool2 = false; 
      if (b < this.strengthVector.length - 1) {
        StringBuilder stringBuilder2 = new StringBuilder();
        stringBuilder2.append(str1);
        stringBuilder2.append(", ");
        str1 = stringBuilder2.toString();
      } else {
        StringBuilder stringBuilder2 = new StringBuilder();
        stringBuilder2.append(str1);
        stringBuilder2.append("] ");
        str1 = stringBuilder2.toString();
      } 
    } 
    String str2 = str1;
    if (bool1) {
      StringBuilder stringBuilder1 = new StringBuilder();
      stringBuilder1.append(str1);
      stringBuilder1.append(" (-)");
      str2 = stringBuilder1.toString();
    } 
    str1 = str2;
    if (bool2) {
      StringBuilder stringBuilder1 = new StringBuilder();
      stringBuilder1.append(str2);
      stringBuilder1.append(" (*)");
      str1 = stringBuilder1.toString();
    } 
    return str1;
  }
  
  public String toString() {
    StringBuilder stringBuilder = new StringBuilder();
    stringBuilder.append("");
    stringBuilder.append(this.mName);
    return stringBuilder.toString();
  }
  
  public final void updateReferencesWithNewDefinition(ArrayRow paramArrayRow) {
    int i = this.mClientEquationsCount;
    for (byte b = 0; b < i; b++)
      (this.mClientEquations[b]).variables.updateFromRow(this.mClientEquations[b], paramArrayRow, false); 
    this.mClientEquationsCount = 0;
  }
  
  public enum Type {
    CONSTANT, ERROR, SLACK, UNKNOWN, UNRESTRICTED;
    
    static  {
      CONSTANT = new Type("CONSTANT", 1);
      SLACK = new Type("SLACK", 2);
      ERROR = new Type("ERROR", 3);
      UNKNOWN = new Type("UNKNOWN", 4);
      $VALUES = new Type[] { UNRESTRICTED, CONSTANT, SLACK, ERROR, UNKNOWN };
    }
  }
}


/* Location:              D:\Apks\New folder\classes-dex2jar.jar!\android\support\constraint\solver\SolverVariable.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.0.7
 */