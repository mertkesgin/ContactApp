package com.example.contactsapp.Model;

public class Contact implements Comparable<Contact>{

    private int id;
    private String name;
    private String phone;
    private String email;
    private byte[] profile_photo;
    private String favourite;

    public Contact() {

    }

    public Contact(String name, String phone, String email,String favourite) {
        this.name = name;
        this.phone = phone;
        this.email = email;
        this.favourite = favourite;
    }

    public Contact(int id, String name, String phone, String email, byte[] profile_photo) {
        this.id = id;
        this.name = name;
        this.phone = phone;
        this.email = email;
        this.profile_photo = profile_photo;
    }

    public Contact(int id, String name, String phone, String email, byte[] profile_photo, String favourite) {
        this.id = id;
        this.name = name;
        this.phone = phone;
        this.email = email;
        this.profile_photo = profile_photo;
        this.favourite = favourite;
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

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public byte[] getProfile_photo() {
        return profile_photo;
    }

    public void setProfile_photo(byte[] profile_photo) {
        this.profile_photo = profile_photo;
    }

    public String getFavourite() {
        return favourite;
    }

    public void setFavourite(String favourite) {
        this.favourite = favourite;
    }

    @Override
    public int compareTo(Contact c) {
        return new String(this.name).compareTo(c.getName());
    }
}
