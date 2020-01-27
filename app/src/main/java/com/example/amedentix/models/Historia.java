package com.example.amedentix.models;

public class Historia {

    private String user, hora, id, image, description,likes;

    public Historia() {
    }

    public Historia(String user, String hora, String id, String image, String likes, String description) {
        this.user = user;
        this.hora = hora;
        this.id = id;
        this.image = image;
        this.likes = likes;
        this.description = description;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getHora() {
        return hora;
    }

    public void setHora(String hora) {
        this.hora = hora;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getLikes() {
        return likes;
    }

    public void setLikes(String likes) {
        this.likes = likes;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
