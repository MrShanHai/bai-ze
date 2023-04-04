package com.shanhai.baize.dto.data;

import com.shanhai.baize.dto.constant.ResultEnums;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ResponseDTO<T> {
    private Integer status;
    private String message;
    private T data;

    public static <T> ResponseDTO<T> success(T data) {
        return new ResponseDTO<>(ResultEnums.SUCCESS.getCode(), ResultEnums.SUCCESS.getMessage(), data);
    }

    public static <T> ResponseDTO<T> success(String message, T data) {
        return new ResponseDTO<>(ResultEnums.SUCCESS.getCode(), message, data);
    }

    public static ResponseDTO<?> failed() {
        return new ResponseDTO<>(ResultEnums.COMMON_FAILED.getCode(), ResultEnums.COMMON_FAILED.getMessage(), null);
    }

    public static ResponseDTO<?> failed(String message) {
        return new ResponseDTO<>(ResultEnums.COMMON_FAILED.getCode(), message, null);
    }


    public static <T> ResponseDTO<T> instance(Integer code, String message, T data) {
        ResponseDTO<T> result = new ResponseDTO<>();
        result.setStatus(code);
        result.setMessage(message);
        result.setData(data);
        return result;
    }
}
