package com.example.entities;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "appliances")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Appliance {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private int amount;
    private String brand;
    private String equipment;
}
