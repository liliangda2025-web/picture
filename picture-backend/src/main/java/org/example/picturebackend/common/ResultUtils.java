package org.example.picturebackend.common;

import org.example.picturebackend.exception.ErrorCode;

public class ResultUtils {
    /**
     * 成功
     *
     * @param data
     * @param <T>
     * @return
     */
    public static <T> BaseResponse<T> success(T data) {
        return new BaseResponse<>(0, data, "ok");
    }

    /**
     * 失败
     *
     * @param errorCode
     * @return
     */
    public static BaseResponse<?> error(ErrorCode errorCode) {
        return new BaseResponse<>(errorCode);
    }

    /**
     * 失败
     *
     * @param errorCode
     * @param message
     * @return
     */
    public static BaseResponse<?> error(int errorCode, String message) {
        return new BaseResponse<>(errorCode, null, message);
    }

    /**
     * 失败
     *
     * @param errorCode
     * @param message
     * @return
     */
    public static BaseResponse<?> error(ErrorCode errorCode, String message) {
        return new BaseResponse<>(errorCode.getCode(), null, message);
    }
}
