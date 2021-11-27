package com.example.toyrecommend.Model;

public class Reviews {

    private String rname, rate, review;

    public Reviews() {
    }

    public Reviews(String rname, String rate, String review) {
        this.rname = rname;
        this.rate = rate;
        this.review = review;
    }

    public String getRname() {
        return rname;
    }

    public void setRname(String rname) {
        this.rname = rname;
    }

    public String getRate() {
        return rate;
    }

    public void setRate(String rate) {
        this.rate = rate;
    }

    public String getReview() {
        return review;
    }

    public void setReview(String review) {
        this.review = review;
    }
}
