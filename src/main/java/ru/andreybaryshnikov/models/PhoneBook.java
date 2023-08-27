package ru.andreybaryshnikov.models;

import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@ToString
@Entity
@Table(name = "phonebook")
public class PhoneBook {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "phonebook_id")
    private int phoneBookID;
    // Фамилия
    @Column(name = "last_name")
    private String lastName;
    // Имя
    @Column(name = "first_name")
    private String firstName;
    // Отчество
    @Column(name = "three_name")
    private String threeName;
    // Номер телефона
    @Column(name = "number_phone")
    private String numberPhone;
    //Адрес
    @Column(name = "address")
    private String address;
    // Описание
    @Column(name = "description")
    private String description;

    public PhoneBook() {
        phoneBookID = 0;
        lastName = "";
        firstName = "";
        threeName = "";
        numberPhone = "";
        address = "";
        description = "";
    }

    public PhoneBook(String lastName, String firstName, String threeName, String numberPhone, String address, String description) {
        this.lastName = lastName;
        this.firstName = firstName;
        this.threeName = threeName;
        this.numberPhone = numberPhone;
        this.address = address;
        this.description = description;
    }
}
