package com.qi.model;

public class MenuInfo {
    private Integer id;

    private String menu;

    private String url;

    private String highermenu;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getMenu() {
        return menu;
    }

    public void setMenu(String menu) {
        this.menu = menu == null ? null : menu.trim();
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url == null ? null : url.trim();
    }

    public String getHighermenu() {
        return highermenu;
    }

    public void setHighermenu(String highermenu) {
        this.highermenu = highermenu == null ? null : highermenu.trim();
    }
}