package com.msalah.contactlist.domain;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;

@Entity
@Table
@NoArgsConstructor
@Data
public class Contact {

    @Id
    @GeneratedValue
    private Long id;

    @NotBlank
    private String name;

    @NotBlank
    private String imageUrl;

    public Contact(String name, String imageUrl) {
        this.name = name;
        this.imageUrl = imageUrl;
    }
}
