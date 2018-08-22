package cn.wizzer.bugwk.commons.enums;

/**
 * 角色的枚举类
 * Created by wizzer on 2018.08
 */
public enum Role {
    ADMIN("ADMIN", "管理员"),
    USER("USER", "普通用户");

    private final String value;
    private final String text;

    Role(String value, String text) {
        this.value = value;
        this.text = text;
    }

    public String getValue() {
        return value;
    }

    public String getText() {
        return text;
    }

    public String value() {
        return value;
    }

    public String text() {
        return text;
    }

    public static Role from(String value) {
        for (Role t : values()) {
            if (t.value.equals(value)) {
                return t;
            }
        }
        throw new IllegalArgumentException("unknown Role: " + value);
    }
}
