import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;
import java.util.function.Function;

public class main {

    static int n;
    static float[] arrayA;
    static String fileName;



    public static void main(String args[]){

        if (args.length < 1) {
            System.out.println("File not found.");
            return;
        }

        try {
            boolean newt = "-newt".equals(args[0]);
            boolean sec = "-sec".equals(args[0]);
            boolean maxIt;
            int maxItVal = 10000;
            float eps = (float) Math.pow(2, -23);

            fileName = args[args.length-1];
            File file = new File(fileName);
            Scanner scanner = new Scanner(file);
            n = scanner.nextInt();
            int arraySize = n + 1;

            arrayA = new float[arraySize];
            for(int j = 0; j < arraySize; j++)
            {
                arrayA[j] = scanner.nextFloat();
            }

            if (args.length > 5) {
                maxIt = "-maxIt".equals(args[args.length - 5]);
                if (maxIt)
                    maxItVal = Integer.parseInt(args[args.length - 4]);
            }

            if(newt)
            {
                float a = Float.parseFloat(args[args.length-2]);
                float results = newton(main::mathFunction, main::dirFunction, a, maxItVal, eps, eps);
                System.out.println("newt -> " +  results);
            }
            else if(sec) {
                float a = Float.parseFloat(args[args.length - 3]);
                float b = Float.parseFloat(args[args.length - 2]);
                float results = secant(main::mathFunction, a, b, maxItVal, eps);
                System.out.println("sec -> " + results);
            }
            else
            {
                float a = Float.parseFloat(args[args.length - 3]);
                float b = Float.parseFloat(args[args.length - 2]);
                float results = bisection(main::mathFunction, a, b, maxItVal, eps);
                System.out.println("Bi-sectional -> " + results);
            }

        }
        catch (IOException ex){
            System.out.println (ex.toString());
            System.out.println("Could not find file... ");
        }



    }




    //Write to file.
    public static void writeToFile(float x, int y, String z){
        try {
            BufferedWriter outputWriter = new BufferedWriter(new FileWriter(fileName.replace(".pol", ".sol")));
            outputWriter.write(x + " " + y + " " + z);
            outputWriter.flush();
            outputWriter.close();
        }
        catch (IOException e)
        {
            System.out.println("ERRROOOOORRRRR");
        }
    }



    public static float mathFunction(float x)
    {
        float ew = arrayA[n];
        for(int i = 0; i < n;i++)
        {
            ew += arrayA[i] * Math.pow(x, n-i);
        }
        return ew;
    }

    public static float dirFunction(float x)
    {
        float we = 0;
        for(int i = 0; i < n;i++)
        {
            we += arrayA[i]* (n-i) * Math.pow(x, n-i-1);
        }
        return we;
    }




    public static float bisection(Function<Float, Float> function, float a, float b, int maxItr, float eps) {
        float fa = function.apply(a);
        float fb = function.apply(b);

        if (fa * fb >= 0) {
            System.out.println("YO your values for a and b are not valid!!");
            writeToFile(-1, 0,"fail");
            return -1;
        }
        float error = a + b;

        float c = 0;

        for (int i = 0 ; i < maxItr ; i ++) {
            error /= 2;
            c = a + error;
            float fc = function.apply(c);

            if (Math.abs(error) < eps || fc == 0) {
                System.out.println("Algorithm converged after " + i + " iterations");
                writeToFile(c, i,"success");
                return c;
            }

            if (fa * fc < 0) {
                b = c;
                fb = fc;
            } else {
                a = c;
                fa = fc;
            }
        }
        System.out.println("max iterations reached");
        writeToFile(c, maxItr,"success");
        return c; // max itr reached
    }








    public static float newton(Function<Float, Float> function, Function<Float, Float> functionDerivative, float x, int maxItr, float eps, float delta) {
        float fx = function.apply(x);
        for (int i = 0 ; i < maxItr ; i ++) {
            float fd = functionDerivative.apply(x);
            if (Math.abs(fd) < delta) {
                System.out.println("small slope");
                writeToFile(x, 0,"success");
                return x;
            }

            float d = fx / fd;
            x = x - d;
            fx = function.apply(x);

            if (Math.abs(d) < eps) {
                System.out.println("converged after " + i + " iterations");
                writeToFile(x, i,"success");
                return x;
            }
        }
        System.out.println("max iterations reached");
        writeToFile(x, maxItr,"success");
        return x;
    }









    public static float secant(Function<Float, Float> function, float a, float b, int maxItr, float eps) {
        float fa = function.apply(a);
        float fb = function.apply(b);

        for (int i = 0 ; i < maxItr ; i ++) {
            if (Math.abs(fa) > Math.abs(fb)) {
                float tmp = fa;
                fa = fb;
                fb = tmp;
                tmp = a;
                a = b;
                b = tmp;
            }

            float d = (b - a) / (fb - fa);
            b = a;
            fb = fa;
            d = d * fa;

            if (Math.abs(d) < eps) {
                System.out.println("converged after " + i + " iterations");
                writeToFile(a, i,"success");
                return a;
            }

            a = a - d;
            fa = function.apply(a);
        }
        System.out.println("max iterations reached");
        writeToFile(a, maxItr,"success");
        return a;
    }

}





















