package com.my.model;

/**
 * Created by Marcin on 01.09.2016.
 */
public class Contact {
    private int id;
    private String type;
    private String value;
    private int idPerson;

    public Contact(int id, String type, String value, int idPerson) {
        this.id = id;
        this.type = type;
        this.value = value;
        this.idPerson = idPerson;
    }

    public Contact(String type, String value, int idPerson) {
        this.type = type;
        this.value = value;
        this.idPerson = idPerson;
    }

    public Contact(String type, String value) {
        this.type = type;
        this.value = value;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public int getIdPerson() {
        return idPerson;
    }

    public void setIdPerson(int idPerson) {
        this.idPerson = idPerson;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Contact contact = (Contact) o;

        if (type != null ? !type.equals(contact.type) : contact.type != null) return false;
        return value != null ? value.equals(contact.value) : contact.value == null;

    }

    @Override
    public int hashCode() {
        int result = type != null ? type.hashCode() : 0;
        result = 31 * result + (value != null ? value.hashCode() : 0);
        return result;
    }
}
