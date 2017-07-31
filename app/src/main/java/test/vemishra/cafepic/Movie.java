package test.vemishra.cafepic;

import java.util.ArrayList;

/**
 * Created by vemishra on 7/21/2017.
 */

public class Movie {
    private String title, thumbnailUrl,description,category,formattedImage,_id;
    private Boolean isVeg;
    private Boolean servedOnMonday;
    private Boolean servedOnTuesday;
    private Boolean servedOnWednesday;
    private Boolean servedOnThursday;
    private Boolean servedOnFriday;
  //  private int year;
    private double rating;
  //  private ArrayList<String> genre;

    public Movie() {
    }

    public Movie(String id,String name, String thumbnailUrl,String formattedImage,String category, String description,Boolean isVeg,Boolean servedOnMonday,Boolean servedOnTuesday,Boolean servedOnWednesday,Boolean servedOnThursday,Boolean servedOnFriday, double rating ) {
        this.title = name;
        this.thumbnailUrl = thumbnailUrl;
        this.rating = rating;
        this.description = description;
        this.isVeg = isVeg;
        this.servedOnMonday = servedOnMonday;
        this.servedOnTuesday = servedOnTuesday;
        this.servedOnWednesday = servedOnWednesday;
        this.servedOnThursday = servedOnThursday;
        this.servedOnFriday = servedOnFriday;
        this.category = category;
        this.formattedImage = formattedImage;
        this._id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String name) {
        this.title = name;
    }

    public String getThumbnailUrl() {
        return thumbnailUrl;
    }

    public void setThumbnailUrl(String thumbnailUrl) {
        this.thumbnailUrl = thumbnailUrl;
    }

    public double getRating() {
        return rating;
    }

    public void setRating(double rating) {
        this.rating = rating;
    }

    public String getDescription() {return description; }

    public void setDescription(String description) {  this.description = description; }

    public String getCategory() { return category; }

    public void setCategory(String category) {this.category = category;  }

    public Boolean isVeg() { return isVeg; }

    public void setVeg(Boolean veg) {
        isVeg = veg;
    }
    public Boolean isServedOnMonday() {
        return servedOnMonday;
    }

    public void setServedOnMonday(Boolean servedOnMonday) {
        this.servedOnMonday = servedOnMonday;
    }

    public Boolean isServedOnTuesday() {
        return servedOnTuesday;
    }

    public void setServedOnTuesday(Boolean servedOnTuesday) {
        this.servedOnTuesday = servedOnTuesday;
    }

    public Boolean isServedOnWednesday() {
        return servedOnWednesday;
    }

    public void setServedOnWednesday(Boolean servedOnWednesday) {
        this.servedOnWednesday = servedOnWednesday;
    }

    public Boolean isServedOnThursday() {
        return servedOnThursday;
    }

    public String get_id() {
        return _id;
    }

    public void set_id(String _id) {
        this._id = _id;
    }

    public void setServedOnThursday(Boolean servedOnThursday) {
        this.servedOnThursday = servedOnThursday;

    }
    public Boolean isServedOnFriday() { return servedOnFriday; }

    public void setServedOnFriday(Boolean servedOnFriday) {this.servedOnFriday = servedOnFriday; }


    public String getFormattedImage() {
        return formattedImage;
    }

    public void setFormattedImage(String formattedImage) {
        this.formattedImage = formattedImage;
    }
}
