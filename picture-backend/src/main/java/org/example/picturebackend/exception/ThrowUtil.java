package org.example.picturebackend.exception;

public class ThrowUtil {

    /**
     * 条件成立，则抛异常
     *
     * @param condition
     * @param runtimeException
     */
    private static void throwIf(boolean condition, RuntimeException runtimeException) {
        if (condition) {
            throw runtimeException;
        }
    }

    /**
     * 条件成立，则抛异常
     *
     * @param condition
     * @param errorCode
     */
    private static void throwIf(boolean condition, ErrorCode errorCode) {
        if (condition) {
            throw new BusinessException(errorCode);
        }
    }

    /**
     * 条件成立，则抛异常
     *
     * @param condition
     * @param errorCode
     * @param message
     */
    private static void throwIf(boolean condition, ErrorCode errorCode, String message) {
        if (condition) {
            throw new BusinessException(errorCode.getCode(), message);
        }
    }
}
