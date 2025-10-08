import java.text.DecimalFormat;

public class Main{
    static public void main(String[] args){
        int[] l = new int[15];
        for (int i = 0; i < 15; i++){
            l[i] = i+1;
        }

        for (int i : l){
            System.out.print(i);
            System.out.print(" ");
        }

        double[] x = new double[11];
        for (int i = 0; i < 11; i++){
            x[i] = randomCountGenerator(-12.0, 11.0);
        }

        System.out.print("\n");
        for (double i : x){
            System.out.print(i);
            System.out.print(" ");
        }

        System.out.print("\n");

        final int[] a = {3, 4, 6, 8, 10, 11, 12};
        final double e = Math.E;

        double[][] s = new double[15][11];
        for (int i = 0; i < 15; i++){
            for (int j = 0; j < 11; j++){
                if (l[i] == 7){
                    double ex = Math.pow(e, x[j]);
                    s[i][j] = Math.pow(e, Math.cos(ex));
                    continue;
                }
                boolean f = isCountInArr(i, a);
                if (f){
                    double sinx = Math.sin(x[j]);
                    s[i][j] = Math.cbrt(Math.cbrt(sinx));
                }
                else{
                    double d = (0.5*(0.5-x[j]))/6;
                    s[i][j] = Math.pow(e, Math.pow(d, 3));
                }
            }
        }

        printArr(s);
    }

    private static void printArr(double[][] s){
        DecimalFormat df = new DecimalFormat(" 0.00 ");
        DecimalFormat dfmin = new DecimalFormat("0.00 ");
        for (double[] i : s){
            for (double j : i){
                if (j > 0){
                    String fj = df.format(j);
                    System.out.print(fj + " ");
                }
                else{
                    String fj = dfmin.format(j);
                    System.out.print(fj + " ");
                }
            }
            System.out.print("\n");
        }
    }

    private static boolean isCountInArr(int i, int[] a) {
        for (int e : a){
            if (e == i){
                return true;
            }
        }
        return false;
    }

    private static double randomCountGenerator(double a, double b){
        return Math.random() * (b-a) + a;
    }
}
