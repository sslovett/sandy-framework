package com.sandy.fw.admin.constant;

public enum MenuType {
    /**
     * 目录
     */
    CATALOG("M"),
    /**
     * 菜单
     */
    MENU("C"),
    /**
     * 按钮
     */
    BUTTON("F");

    private String value;

    MenuType(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
