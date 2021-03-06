package s.k.c.foagent.test.testfiles;

import java.io.IOException;
import java.util.Random;

public class TryCatchTestObject {
    public String multipleTryCatch() {
        StringBuilder result = new StringBuilder();
        try {
            // 1st try-catch block
            result.append("_1st line in 1st tc");
            String arg = getArgument();
            String key = format(arg);
        } catch (MissingPropertyException e) {
            // MissingPropertyException occured in sourceIndependentTryCatch
            result.append("_mpe in 1st tc");
        } catch (IOException e) {
            result.append("_ioe in 1st tc");
        }

        try {
            // 2nd try-catch block
            result.append("_1st line in 2nd tc");
            String arg = getArgument();
            String key = format(arg);
        } catch (MissingPropertyException e) {
            result.append("_mpe in 2nd tc");
        } catch (IOException e) {
            result.append("_ioe in 2nd tc");
        }

        return result.toString();
    }

    public String sourceIndependentTryCatch() {
        Boolean isCacheActivated = false;
        System.out.println("in try right away!!");
        try {
            System.out.println("first line in try!!");
            String arg = getArgument();
            String key = format(arg);
            return getProperty(key, isCacheActivated);
        } catch (MissingPropertyException e) {
            System.out.println("MissingPropertyException occured in sourceIndependentTryCatch");
            return "missing property";
        } catch (IOException e) {
            System.out.println("IOException occured in sourceIndependentTryCatch");
            return "get argument failed";
        }
    }

    public String sourceDependentTryCatch() {
        Boolean isCacheActivated = true;
        String arg = "str_arg";
        String key = "str_key";
        try {
            isCacheActivated = getCacheAvailability();
            return getProperty(key, isCacheActivated);
        } catch (MissingPropertyException e) {
            System.out.println("MissingPropertyException occured in sourceDependentTryCatch");
            if (isCacheActivated) {
                return "missing property";
            } else {
                throw new CacheDisableException();
            }
        }
    }

    public String purelyResilientTryCatch() {
        String key = "str_key";
        try {
            return getPropertyFromCache(key);
        } catch (MissingPropertyException e) {
            System.out.println("MissingPropertyException occured in purelyResilientTryCatch");
            return getPropertyFromFile(key);
        }
    }

    public String nestedTryCatch() {
        StringBuilder result = new StringBuilder();
        try {
            // 1st try-catch block
            result.append("_1st line in 1st tc");
            String arg = getArgument();

            try {
                // nested try-catch block
                result.append("_1st line in the nested tc");
                String key = format(arg);
            } catch (MissingPropertyException e) {
                result.append("_mpe in the nested tc");
            }
        } catch (IOException e) {
            result.append("_ioe in 1st tc");
        }

        return result.toString();
    }

    public void voidMethodThrowsException() throws NullPointerException {
        System.out.println("I'm a void method.");
        System.out.println("I'll throw an Exception right away!");
        String test = null;
        test.length();
    }

    public void voidMethodThrowsException(String str) throws NullPointerException {
        System.out.println("I'm a void method.");
        System.out.println("I'll throw an Exception right away!");
        String test = null;
        test.length();
    }

    public String returnObjectMethodThrowsException() throws NullPointerException {
        System.out.println("I'm a method which returns an object.");
        System.out.println("I'll throw an Exception right away!");
        String test = null;
        test.length();
        return "This is my original return value.";
    }

    public int returnIntegerMethodThrowsException() throws NullPointerException {
        System.out.println("I'm a method which returns an integer.");
        System.out.println("I'll throw an Exception right away!");
        String test = null;
        test.length();
        return 1;
    }

    public float returnFloatMethodThrowsException() throws NullPointerException {
        System.out.println("I'm a method which returns a float.");
        System.out.println("I'll throw an Exception right away!");
        String test = null;
        test.length();
        return 1.0F;
    }

    public double returnDoubleMethodThrowsException() throws NullPointerException {
        System.out.println("I'm a method which returns a double.");
        System.out.println("I'll throw an Exception right away!");
        String test = null;
        test.length();
        return 1.0D;
    }

    public long returnLongMethodThrowsException() throws NullPointerException {
        System.out.println("I'm a method which returns a long.");
        System.out.println("I'll throw an Exception right away!");
        String test = null;
        test.length();
        return 1L;
    }

    public boolean returnBooleanMethodThrowsException() throws NullPointerException {
        System.out.println("I'm a method which returns a boolean.");
        System.out.println("I'll throw an Exception right away!");
        String test = null;
        test.length();
        return true;
    }

    private String getProperty(String key, Boolean isCacheActivated) throws MissingPropertyException {
        return null;
    }

    private String format(String arg) throws MissingPropertyException {
        return null;
    }

    private String getArgument() throws IOException {
        return null;
    }

    private boolean getCacheAvailability() {
        Random random = new Random();
        return random.nextDouble() < 0.5;
    }

    private String getPropertyFromFile(String key) {
        return "property_from_file";
    }

    private String getPropertyFromCache(String key) throws MissingPropertyException {
        return "property_from_cache";
    }

    public static class CacheDisableException extends RuntimeException {
    }

    public static void main(String[] args) {
        TryCatchTestObject tcTest = new TryCatchTestObject();

        System.out.println(tcTest.multipleTryCatch());
        tcTest.sourceIndependentTryCatch();
        tcTest.sourceDependentTryCatch();
        tcTest.purelyResilientTryCatch();
    }
}
