package com.allintheloop.Util;

/**
 * Created by nteam on 28/4/16.
 */
public class Appcolor {

    public static String BackColor, topBackColor, topTextColor, iconTextColor, mennuColor, menu_txtColor, menuTxtHovercoolor;

    public Appcolor(String backColor, String topBackColor, String topTextColor, String iconTextColor, String menuColor, String menu_txtColor, String menuTxtHovercoolor) {
        BackColor = backColor;
        this.topBackColor = topBackColor;
        this.topTextColor = topTextColor;
        this.iconTextColor = iconTextColor;
        this.mennuColor = menuColor;
        this.menu_txtColor = menu_txtColor;
        this.menuTxtHovercoolor = menuTxtHovercoolor;
    }

    public String getMennuColor() {
        return mennuColor;
    }

    public void setMennuColor(String mennuColor) {
        this.mennuColor = mennuColor;
    }

    public String getMenu_txtColor() {
        return menu_txtColor;
    }

    public void setMenu_txtColor(String menu_txtColor) {
        this.menu_txtColor = menu_txtColor;
    }

    public String getMenuTxtHovercoolor() {
        return menuTxtHovercoolor;
    }

    public void setMenuTxtHovercoolor(String menuTxtHovercoolor) {
        this.menuTxtHovercoolor = menuTxtHovercoolor;
    }

    public String getBackColor() {
        return BackColor;
    }

    public void setBackColor(String backColor) {
        BackColor = backColor;
    }

    public String getTopBackColor() {
        return topBackColor;
    }

    public void setTopBackColor(String topBackColor) {
        this.topBackColor = topBackColor;
    }

    public String getTopTextColor() {
        return topTextColor;
    }

    public void setTopTextColor(String topTextColor) {
        this.topTextColor = topTextColor;
    }

    public String getIconTextColor() {
        return iconTextColor;
    }

    public void setIconTextColor(String iconTextColor) {
        this.iconTextColor = iconTextColor;
    }
}
