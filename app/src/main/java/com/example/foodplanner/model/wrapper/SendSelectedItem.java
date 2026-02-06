package com.example.foodplanner.model.wrapper;

import java.io.Serializable;

public class SendSelectedItem implements Serializable {

    private int id;
    private String name;
    private String discoveryImageURL;
    private int type;//1 2 3 same sort

    public SendSelectedItem(int id, String name, String discoveryImageURL, int type) {
        this.id = id;
        this.name = name;
        this.discoveryImageURL = discoveryImageURL;
        this.type = type;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDiscoveryImageURL() {
        return discoveryImageURL;
    }

    public void setDiscoveryImageURL(String discoveryImageURL) {
        this.discoveryImageURL = discoveryImageURL;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    @Override
    public String toString() {
        return "SendSelectedItem{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", discoveryImageURL='" + discoveryImageURL + '\'' +
                ", type=" + type +
                '}';
    }
}
