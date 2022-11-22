package game;

public class Main {
    public static void main(String[] args) {
        new Window();
    }
    public static boolean sumOfNumberAndReverse(int number) {
        while (number > 0) {
            int pow = (int)(Math.log(number) / Math.log(10));
            int digitNum = (int) Math.pow(10,pow);
            System.out.println("DigitNum: " + digitNum);
            int firstDigit = number/digitNum;
            int lastDigit = number % 10;
            if (firstDigit == 1 && lastDigit != 1) {

            }
            if (lastDigit > firstDigit) return false;
            if (firstDigit - lastDigit > 1) return false;
            number -= digitNum * lastDigit;
            number /= 10;
        }
        return true;
    }
}
