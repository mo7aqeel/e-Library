package com.mo7.library;

public class BookDes {
    String title;
    String desc;
    String img;
    String writer;
    String file;
    String category;
    String price;
    String favNum;

    public BookDes(String title, String desc, String img, String writer, String file, String category, String price, String favNum){
        this.desc = desc;
        this.title = title;
        this.img = img;
        this.writer = writer;
        this.file = file;
        this.category = category;
        this.price = price;
        this.favNum = favNum;
    }

    public BookDes(){}

    public String getTitle() {
        return title;
    }

    public String getFile() {
        return file;
    }

    public String getWriter() {
        return writer;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String des) {
        this.desc = des;
    }

    public String getImg() {
        return img;
    }


    public void setImg(String img) {
        this.img = img;
    }

    public void setWriter(String writer) {
        this.writer = writer;
    }

    public void setFile(String file) {
        this.file = file;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getFavNum() {
        return favNum;
    }

    public void setFavNum(String favNum) {
        this.favNum = favNum;
    }
}
