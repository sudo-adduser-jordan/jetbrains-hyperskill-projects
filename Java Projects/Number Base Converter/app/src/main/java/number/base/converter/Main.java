package number.base.converter;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.RoundingMode;
import java.util.Scanner;

public class Main {
    private final static Scanner sc = new Scanner(System.in);

    public static void main(String[] args) {
        while (true) {
            System.out.println("Enter two numbers in format: {source base} {target base} " +
                    "(To quit type /exit) ");
            String choice = sc.nextLine();
            int source = 0;
            int target = 0;
            if (choice.equals("/exit")) System.exit(0);
            else {
                String[] bases = choice.split(" ");
                source = Integer.parseInt(bases[0]);
                target = Integer.parseInt(bases[1]);
            }
            while (true) {

                System.out.printf("Enter number in base %d to convert to base %d (To go back type /back)%n", source, target);
                String numbers = sc.nextLine();
                if (numbers.equals("/back")) break;
                if (target == 10) System.out.println("Conversion result: " + toDecimal(numbers, source));
                else {
                    from(numbers, source, target);
                }
            }
        }

    }

    public static void from(String number, int source, int target) {
        String result;
        if (source == 10) {
            result = toSomeBase(number, target);
        } else {
            result = toDecimal(number, source);
            result = toSomeBase(result, target);
        }
        System.out.println("Conversion result: " + result);
    }

    public static String toDecimal(String number, int base) {
        String intPart;
        StringBuilder dPart = new StringBuilder();
        double dResult = 0;
        number = number.replace('.', '-');
        if (number.contains("-")) {
            String[] bases = number.split("-");
            intPart = bases[0];
            dPart.append(bases[1]);
        } else {
            intPart = number;
        }
        long iResult = 0;
        for (int i = 0; i < intPart.length(); i++) {
            int n = intPart.length() - i - 1;
            String c;
            if (Character.isDigit(intPart.charAt(i))) {
                c = String.valueOf(intPart.charAt(i));
            } else {
                int a = Character.toUpperCase(intPart.charAt(i)) - 55;
                c = String.valueOf(a);
            }
            iResult += Integer.parseInt(c) * Math.pow(base, n);
        }
        BigDecimal intResult = new BigDecimal(String.valueOf(iResult));

        if (!dPart.isEmpty()) {
            for (int i = 0; i < dPart.length(); i++) {
                int n = -(i + 1);
                String c;
                if (Character.isDigit(dPart.charAt(i))) {
                    c = String.valueOf(dPart.charAt(i));
                } else {
                    int a = Character.toUpperCase(dPart.charAt(i)) - 55;
                    c = String.valueOf(a);
                }
                dResult += Integer.parseInt(c) * Math.pow(base, n);
            }
        }
        BigDecimal finResult = dPart.isEmpty() ? intResult :
                intResult.setScale(5, RoundingMode.CEILING).add(new BigDecimal(String.valueOf(dResult)));

        return finResult.toString();
    }

    public static String toSomeBase(String lnumber, int target) {
        StringBuilder fin = new StringBuilder();
        lnumber = lnumber.replace('.', '-');
        String intPart;
        StringBuilder dPart = new StringBuilder();
        StringBuilder result = new StringBuilder();
        if (lnumber.contains("-")) {
            String[] bases = lnumber.split("-");
            intPart = bases[0];
            dPart.append("0.");
            dPart.append(bases[1]);
        } else {
            intPart = lnumber;
        }
        BigInteger number = new BigInteger(String.valueOf(intPart));
        do {
            BigInteger answer = number.remainder(BigInteger.valueOf(target));
            if (answer.intValue() > 9) {
                result.append(Character.toChars((answer.intValue() + 55)));
            } else {
                result.append(answer);
            }
            number = number.divide(BigInteger.valueOf(target));
        } while (number.intValue() != 0);

        String numbers = dPart.toString();
        BigDecimal bBase = new BigDecimal(String.valueOf(target));
        while (!dPart.isEmpty()) {
            BigDecimal stop = new BigDecimal(0.0);
            BigDecimal realNo = new BigDecimal(numbers).multiply(bBase);
            BigDecimal quotient = realNo.setScale(0, RoundingMode.DOWN); // 0.33
            BigDecimal stopvalue = realNo.subtract(quotient);

            if (quotient.intValue() > 9) {
                fin.append(Character.toChars((quotient.intValue() + 55)));
            } else {
                fin.append(quotient);
            }
            if (stopvalue.equals(stop)) break;

            numbers = realNo.subtract(quotient).toString();
            if (fin.length() >= 5) break;
        }
        if (!fin.isEmpty()) {
            fin.insert(0, ".");
            return result.reverse() + fin.toString();
        } else {
            return result.reverse().toString();
        }
    }
}
