package org.scash;

public class NativeBLAKE3Util {

    public static void assertEquals(boolean val, boolean val2, String message) throws AssertFailException {
        if (val != val2)
            throw new AssertFailException("FAIL: " + message);
        else
            System.out.println("PASS: " + message);
    }

    public static void checkState(boolean expression) {
        if (!expression) {
            throw new IllegalStateException();
        }
    }

    public static class AssertFailException extends Exception {
        public AssertFailException(String message) {
            super(message);
        }
    }
}
