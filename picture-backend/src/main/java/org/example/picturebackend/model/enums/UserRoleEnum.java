package org.example.picturebackend.model.enums;

import cn.hutool.core.util.ObjUtil;
import lombok.Getter;

@Getter
public enum UserRoleEnum {
    USER("用户", "user"),
    ADMIN("管理员", "admin");

    private final String text;
    private final String value;

    UserRoleEnum(String text, String value) {
        this.text = text;
        this.value = value;
    }

    //利用值获取枚举
    public static UserRoleEnum getEnumByValue(String value) {
        //入参校验
        if (ObjUtil.isEmpty(value)) {
            return null;
        }

        //遍历枚举值返回枚举
        for (UserRoleEnum anEnum : UserRoleEnum.values()) {
            if (anEnum.value.equals(value)) {
                return anEnum;
            }
        }

        return null;
    }
}
