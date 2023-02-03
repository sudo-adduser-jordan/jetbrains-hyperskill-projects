package numeric.matrix.processor;

import java.util.Scanner;

public class Main {

    static Scanner sc = new Scanner(System.in);
    static boolean exit = false;

    static void operate(int choice) {

        int row1;
        int row2;
        int col1;
        int col2;
        double[][] arr;
        double[][] arr1;
        double[][] arr2;

        switch (choice) {
            // add
            case 1:
                System.out.println("Enter size of first matrix: ");
                row1 = sc.nextInt();
                col1 = sc.nextInt();
                System.out.println("Enter first matrix:");
                arr1 = scanMatrix(row1, col1);

                System.out.println("Enter size of second matrix: ");
                row2 = sc.nextInt();
                col2 = sc.nextInt();
                System.out.println("Enter second matrix:");

                if (row1 == row2 && col1 == col2) {
                    arr2 = scanMatrix(row2, col2);
                    printMatrix(addMatrix(arr1, arr2));
                } else {
                    System.out.println("The operation cannot be performed.");
                }
                break;

            // multiply constant
            case 2:
                System.out.println("Enter size of matrix: ");
                row1 = sc.nextInt();
                col1 = sc.nextInt();
                System.out.println("Enter matrix: ");
                arr = scanMatrix(row1, col1);
                System.out.println("Enter constant: ");
                double c = sc.nextDouble();
                printMatrix(mulMatrix(arr, c));
                break;

            //multiply matrix
            case 3:
                System.out.println("Enter size of first matrix: ");
                row1 = sc.nextInt();
                col1 = sc.nextInt();
                double[][] arr3;
                System.out.println("Enter first matrix:");
                arr3 = scanMatrix(row1, col1);

                System.out.println("Enter size of second matrix: ");
                row2 = sc.nextInt();
                col2 = sc.nextInt();
                System.out.println("Enter second matrix:");
                double[][] arr4;
                arr4 = scanMatrix(row2, col2);
                printMatrix(mulMatrix(arr3, arr4));
                break;

            // transpose
            case 4:
                System.out.println("1. Main diagonal");
                System.out.println("2. Side diagonal");
                System.out.println("3. Vertical line");
                System.out.println("4. Horizontal line");
                System.out.println("Your choice: ");
                int line = sc.nextInt();

                System.out.println("Enter matrix size: ");
                row1 = sc.nextInt();
                col1 = sc.nextInt();
                System.out.println("Enter matrix: ");
                arr = scanMatrix(row1, col1);
                printMatrix(transMatrix(arr, line));
                break;

            case 5:
                System.out.println("Enter matrix size: ");
                row1 = sc.nextInt();
                col1 = sc.nextInt();
                System.out.println("Enter matrix: ");
                arr = scanMatrix(row1, col1);
                System.out.println("The result is:");
                System.out.println(det(arr));
                break;

            case 6:
                System.out.println("Enter matrix size:");
                row1 = sc.nextInt();
                col1 = sc.nextInt();
                System.out.println("Enter matrix:");
                arr = scanMatrix(row1, col1);
                if (det(arr) == 0) {
                    System.out.println("This matrix doesn't have an inverse.");
                } else {
                    printMatrix(invMatrix(arr));
                }
                break;

            default:

                break;
        }
    }

    static double[][] scanMatrix(int row, int col) {

        double[][] arr = new double[row][col];

        for (int i = 0; i < row; i++) {
            for (int j = 0; j < col; j++) {
                arr[i][j] = sc.nextDouble();
            }
        }

        return arr;
    }

    static void printMatrix(double[][] arr) {
        System.out.println("The result is: ");
        for (double[] doubles : arr) {
            for (int j = 0; j < arr[0].length; j++) {
                System.out.print(doubles[j] + " ");
            }
            System.out.println();
        }
    }

    static double[][] addMatrix(double[][] arr1, double[][] arr2) {
        double[][] arr = new double[arr1.length][arr1[0].length];
        for (int i = 0; i < arr.length; i++) {
            for (int j = 0; j < arr[0].length; j++) {
                arr[i][j] = arr1[i][j] + arr2[i][j];
            }
        }
        return arr;
    }

    static double[][] mulMatrix(double[][] arr, double n) {
        for (int i = 0; i < arr.length; i++) {
            for (int j = 0; j < arr[0].length; j++) {
                arr[i][j] *= n;
            }
        }
        return arr;
    }

    static double[][] mulMatrix(double[][] arr1, double[][] arr2) {
        double[][] arr = new double[arr1.length][arr2[0].length];
        for (int i = 0; i < arr1.length; i++) {
            for (int j = 0; j < arr2[0].length; j++) {
                arr[i][j] = 0;
                for (int n = 0; n < arr1[0].length; n++) {
                    arr[i][j] += arr1[i][n] * arr2[n][j];
                }
            }
        }
        return arr;
    }

    static double[][] transMatrix(double[][] arr, int line) {
        double[][] transArray = new double[arr.length][arr[0].length];
        switch (line) {
            case 1:
                for (int i = 0; i < arr.length; i++) {
                    for (int j = 0; j < arr[0].length; j++) {
                        transArray[i][j] = i == j ? arr[i][j] : arr[j][i];
                    }
                }
                break;
            case 2:
                for (int i = 0; i < arr.length; i++) {
                    for (int j = 0; j < arr[0].length; j++) {
                        transArray[i][j] = i + j == arr.length - 1 ? arr[i][j] : arr[arr.length - 1 - j][arr.length - 1 - i];
                    }
                }
                break;
            case 3:
                for (int i = 0; i < arr.length; i++) {
                    for (int j = 0; j < arr[0].length; j++) {
                        transArray[i][j] = arr[i][arr[0].length - j - 1];
                    }
                }
                break;
            case 4:
                for (int i = 0; i < arr.length; i++) {
                    System.arraycopy(arr[arr.length - i - 1], 0, transArray[i], 0, arr[0].length);
                }
                break;
            default:
                break;

        }
        return transArray;
    }

    static double[][] minor(double[][] arr, int row, int col) {
        double[][] newArr = new double[arr.length - 1][arr[0].length - 1];
        for (int i = 0; i < newArr.length; i++) {
            for (int j = 0; j < newArr[0].length; j++) {
                if (i < row && j < col) {
                    newArr[i][j] = arr[i][j];
                } else if (i < row) {
                    newArr[i][j] = arr[i][j + 1];
                } else if (j < col) {
                    newArr[i][j] = arr[i + 1][j];
                } else {
                    newArr[i][j] = arr[i + 1][j + 1];
                }
            }
        }
        return newArr;
    }

    static double det(double[][] arr) {

        double d = 0;

        if (arr.length == 1) {
            return arr[0][0];
        } else if (arr.length == 2) {
            return (arr[0][0] * arr[1][1] - arr[0][1] * arr[1][0]);
        } else {
            for (int j = 0; j < arr[0].length; j++) {
                d += Math.pow(-1, j) * arr[0][j] * det(minor(arr, 0, j));
            }
            return d;
        }
    }

    static double[][] adjugate(double[][] arr) {

        double[][] adjArr = new double[arr.length][arr.length];

        for (int i = 0; i < adjArr.length; i++) {
            for (int j = 0; j < adjArr.length; j++) {
                adjArr[j][i] = Math.pow(-1, i + j) * det(minor(arr, i, j));
            }
        }
        return adjArr;
    }

    static double[][] invMatrix(double[][] arr) {

        double[][] invArr = new double[arr.length][arr.length];

        for (int i = 0; i < invArr.length; i++) {
            for (int j = 0; j < invArr[0].length; j++) {
                invArr[i][j] = adjugate(arr)[i][j] / det(arr);
            }
        }
        return invArr;
    }

    public static void main(String[] args) {


        while (!exit) {

            System.out.println("1. Add matrices");
            System.out.println("2. Multiply matrix by a constant");
            System.out.println("3. Multiply matrices");
            System.out.println("4. Transpose matrix");
            System.out.println("5. Calculate a determinant");
            System.out.println("6. Inverse matrix");
            System.out.println("0. Exit");
            System.out.println("Your choice:");

            int choice = sc.nextInt();
            if (choice == 0) {
                exit = true;
            }
            operate(choice);

        }
    }
}