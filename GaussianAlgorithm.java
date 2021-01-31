/*
x1  x2  x3     r4
 2   2  -4  |  12
 1   3   1  |   4
-1  -1   3  |  -8

x1 = 0; x2 = 2; x3 = -2;
*/

import java.util.List;
import java.util.ArrayList;
import java.util.Scanner;

class GaussianUtilities {
    static void clearConsole() {
        /*
         * try { final String os = System.getProperty("os.name");
         * 
         * if (os.contains("Windows")) { // TODO: check compatibility on windows
         * Runtime.getRuntime().exec("cls"); } else { System.out.print("\033\143"); } }
         * catch (final Exception e) { }
         */
        System.out.print("\n\nDEBUG CLEAR CONSOLE\n\n");
    }

    static void printToDifference(int diff, String val) {
        for (int i = 0; i < diff; i++) {
            System.out.print(val);
        }
    }

    static void printToDifference(int diff) {
        printToDifference(diff, " ");
    }
}

class GaussianSolvedItemList {
    int index = 0;
    List<GaussianSolvedItem> rawList = new ArrayList<GaussianSolvedItem>();

    GaussianLine visualStep(GaussianLine comparativeGaussianLine) {
        // TODO: implement interactive fill-in of variable values
        return new GaussianLine();
    }

    double calcStep(GaussianLine comparativeGaussianLine) {
        double toBeShifted = 0;
        for (int i = 0; i < comparativeGaussianLine.values.length; i++) {
            // TODO: check for existing value in item list to set values
            if (this.hasSolution(i)) {
                // factor_z * value_z
                double t = this.getSolution(i) * comparativeGaussianLine.values[i];
                toBeShifted += t;
            }
        }
        return toBeShifted;
    }

    boolean hasSolution(int i) {
        boolean _result = false;
        for (int _i = 0; _i < this.rawList.size(); _i++) {
            if (rawList.get(_i).index == i)
                return true;
        }
        return _result;
    }

    double getSolution(int i) {
        for (int _i = 0; _i < this.rawList.size(); _i++) {
            if (rawList.get(_i).index == i)
                return rawList.get(_i).value;
        }
        return 0.0;
    }

    void addSolvedItem(GaussianSolvedItem item) {
        rawList.add(item);
    }

    public void setIndex(int index) {
        this.index = index;
    }

    public void setRawList(List<GaussianSolvedItem> rawList) {
        this.rawList = rawList;
    }
}

// pure object-class
class GaussianSolvedItem {
    int index;
    double value;

    static GaussianSolvedItem build(int i, double v) {
        GaussianSolvedItem t = new GaussianSolvedItem();
        t.index = i;
        t.value = v;
        return t;
    }
}

// handling last part of simple linear equation solving: ?xC * x = ?xV
class GaussianAlignment {
    double xC = 0.0;
    double xV = 0.0;

    void setParticipants(double count, double value) {
        this.xC = count;
        this.xV = value;
    }

    double solve() {
        this.xV /= xC;
        return this.xV;
    }
}

// handling "higher" steps in linear equation solving
class GaussianAlignmentBuilder {
    GaussianLine preparableAlignmentLine;
    GaussianSolvedItemList preparedSolvedItemList;

    boolean fast = true;

    void setPreparableAlignmentLine(GaussianLine preparableAlignmentLine) {
        this.preparableAlignmentLine = preparableAlignmentLine;
    }

    double purgeZeros() {
        for (int i = 0; i < this.preparableAlignmentLine.values.length; i++) {
            if (this.preparableAlignmentLine.values[i] != 0) {
                return this.preparableAlignmentLine.values[i];
            }
        }
        return 0.0;
    }

    public void setPreparedSolvedItemList(GaussianSolvedItemList preparedSolvedItemList) {
        this.preparedSolvedItemList = preparedSolvedItemList;
    }

    double fillKnown() {
        return this.preparedSolvedItemList.calcStep(preparableAlignmentLine);
    }

    GaussianAlignment build() {
        GaussianAlignment newAlignment = new GaussianAlignment();
        double leftSideV, rightSideV = this.preparableAlignmentLine.result;
        if (this.fast == true) {
            leftSideV = this.purgeZeros();
        } else {
            double filled = this.fillKnown();
            leftSideV = this.preparableAlignmentLine.values[preparedSolvedItemList.index];
            rightSideV -= filled;
        }
        newAlignment.setParticipants(leftSideV, rightSideV);
        return newAlignment;
    }
}

class GaussianLine {
    double[] values;
    double result;

    int loadedLen = 0;

    void setValues(double[] newValues) {
        this.values = newValues;
        loadedLen = this.values.length + 1;
    }

    void setResult(double res) {
        this.result = res;
    }

    public void setLoadedLen(int loadedLen) {
        this.loadedLen = loadedLen;
    }

    GaussianLine multiplyBy(double multiplier) {
        GaussianLine newLine = new GaussianLine();
        double[] newValues = new double[this.values.length];
        for (int i = 0; i < this.values.length; i++) {
            newValues[i] = this.values[i] * multiplier;
        }
        newLine.setValues(newValues);
        double newResult = this.result * multiplier;
        newLine.setResult(newResult);
        return newLine;
    }

    GaussianLine sub(GaussianLine subtrahendLine) {
        GaussianLine newLine = new GaussianLine();
        double[] newValues = new double[this.values.length];
        for (int i = 0; i < this.values.length; i++) {
            newValues[i] = this.values[i] - subtrahendLine.values[i];
        }
        newLine.setValues(newValues);
        double newResult = this.result - subtrahendLine.result;
        newLine.setResult(newResult);
        return newLine;
    }

    void print() {
        for (int i = 0; i < this.values.length; i++) {
            System.out.print("x" + i + "=" + this.values[i] + ";");
        }
        System.out.print("r=" + this.result + "\n");
    }

    void prettyPrint(int biggestLen) {
        boolean firstVar = true;
        String[] prettyItems = new String[this.values.length];
        for (int i = 0; i < this.values.length; i++) {
            String strVal = "";
            if (i > this.loadedLen) {
                if (firstVar) {
                    firstVar = false;
                    strVal = " x ";
                } else {
                    strVal = " ? ";
                }
            } else {
                strVal = String.valueOf(this.values[i]);
            }
            prettyItems[i] = strVal;
            if (strVal.length() > biggestLen)
                biggestLen = strVal.length();
        }
        for (int i = 0; i < prettyItems.length; i++) {
            GaussianUtilities.printToDifference(biggestLen - prettyItems[i].length());
            System.out.print(prettyItems[i]);
            if (i != prettyItems.length - 1) {
                System.out.print("  ");
            }
        }
        String resultStr = String.valueOf(this.result);
        if (this.values.length == this.loadedLen) {
            resultStr = "x";
        }
        System.out.print(" | " + resultStr + "\n");
    }

    void prettyPrint() {
        prettyPrint(0);
    }

    boolean isLast() {
        boolean _result = true;
        for (int i = 1; i < this.values.length; i++) {
            if (this.values[i] == 0) {
                _result = false;
                break;
            }
        }
        return _result;
    }
}

class GaussianSystem {
    boolean hold = false;

    GaussianLine[] lines;
    GaussianSystemInput input;

    int printIndex = 0;

    void init() {
        input = new GaussianSystemInput();
        input.setParent(this);
    }

    void setLineCount(int i) {
        lines = new GaussianLine[i];
        //System.out.print("i=" + i + ";len=" + lines.length);
    }

    void setCoords(int x, int y, double v) {
        if (existsCoords(x, y)) {
            this.lines[y].values[x] = v;
        } else {
            GaussianUtilities.clearConsole();
            System.out.println("Error: No item at x=" + x + ";y=" + y);
        }
    }

    boolean existsCoords(int x, int y) {
        return y < this.lines.length && x < this.lines[y].values.length;
    }

    double getCoords(int x, int y) {
        if (existsCoords(x, y)) {
            return this.lines[y].values[x];
        } else {
            GaussianUtilities.clearConsole();
            System.out.println("Error: No item at x=" + x + ";y=" + y);
            return Math.E;
        }
    }

    void prettyPrint() {
        for (int i = 0; i < this.lines.length && i <= this.printIndex; i++) {
            this.lines[i].prettyPrint();
        }
    }

    void print() {
        for (int i = 0; i < this.lines.length && i <= this.printIndex; i++) {
            this.lines[i].print();
        }
    }
}

class GaussianSystemInput {
    int lineCount = 0;
    int varCount = 0;
    Scanner scanner;

    GaussianSystem parent;

    public void setParent(GaussianSystem parent) {
        this.parent = parent;
    }

    public void setScanner(Scanner scanner) {
        this.scanner = scanner;
    }

    void receiveStandardKnowledge() {
        System.out.print("Geben Sie die Anzahl der Zeilen/Variablen der Gauss-Matrix an: ");
        this.lineCount = scanner.nextInt();
        this.varCount = this.lineCount;
        GaussianUtilities.clearConsole();
    }

    void receiveLine() {
        for (int x = 0; x < this.varCount; x++) {
        }
    }

    void fakePrint() {
        parent.prettyPrint();
    }

    void start() {
        this.receiveStandardKnowledge();
        parent.setLineCount(this.lineCount);
        this.initEmpty();
        GaussianLine testLine = new GaussianLine();
        double[] vals = { 2, 2, -4 };
        testLine.setValues(vals);
        testLine.setResult(12);
        parent.lines[0] = testLine;
        parent.prettyPrint();
    }

    void initEmpty() {
        for (int y = 0; y < this.lineCount; y++) {
            parent.lines[y] = new GaussianLine();
            double[] vals = new double[this.varCount];
            for (int x = 0; x < this.varCount; x++) {
                vals[x] = 0;
            }
            parent.lines[y].setValues(vals);
            parent.lines[y].setResult(0);
        }
    }
}

public class GaussianAlgorithm {

    public static void main(String[] args) {

        // GaussianUtilities.clearConsole();
        System.out.println("GAUSS-ALGORITHMUS IN JAVA");
        System.out.println("-------------------------");
        System.out.println("Es wird eine lösbare und saubere Gauss-Matrix vorrausgesetzt.");

        Scanner scanner = new Scanner(System.in);
        GaussianSystem system = new GaussianSystem();
        system.init();
        system.input.setScanner(scanner);
        system.input.start();

        System.out.print("\n");
        scanner.close();

        /*
         * GaussianLine testLine1 = new GaussianLine(); double[] testLine1Values = {
         * 2.0, 2.0, -4.0 }; testLine1.setValues(testLine1Values);
         * testLine1.setResult(12.0);
         * 
         * testLine1.prettyPrint();
         * 
         * GaussianLine testLine2 = new GaussianLine(); double[] testLine2Values = {
         * 1.0, 3.0, 1.0 }; testLine2.setValues(testLine2Values);
         * testLine2.setResult(4.0);
         * 
         * testLine2.prettyPrint();
         * 
         * GaussianLine subLine = testLine2.multiplyBy(2.0 / 1.0); GaussianLine
         * resultLine = testLine1.sub(subLine);
         * 
         * resultLine.prettyPrint();
         */
    }

}