package com.codemonkeys.cpp;

public class PaintItem {
    private int id;
    private String paintName;
    private String colourCode = "#FFFFFF";
    private String type;
    private Boolean inventory;
    private Boolean wishlist;

    public PaintItem(int id, String paintName, String colourCode, String type){
        this.id = id;
        this.paintName = paintName;
        this.colourCode = colourCode;
        this.type = type;
    }

    public PaintItem(int id, String paintName, String colourCode, String type, int inventory, int wishlist){
        this.id = id;
        this.paintName = paintName;
        this.colourCode = colourCode;
        this.type = type;
        this.inventory = (inventory != 0);
        this.wishlist = (wishlist != 0);
    }

    public String getPaintName() {
        return paintName;
    }

    public void setPaintName(String paintName) {
        this.paintName = paintName;
    }

    public String getColourCode() {
        return colourCode;
    }

    public void setColourCode(String colourCode) {
        this.colourCode = colourCode;
    }

    public int getId() { return this.id; }

    public String getType() { return type; }

    public void setType(String type) {
        this.type = type;
    }

    public Boolean getInvetory() {
        return inventory;
    }

    public void setInvetory(Boolean invetory) {
        this.inventory = invetory;
    }

    public Boolean getWishList() {
        return this.wishlist;
    }

    public void setWishlist(Boolean wishlist) {
        this.wishlist = wishlist;
    }
}

