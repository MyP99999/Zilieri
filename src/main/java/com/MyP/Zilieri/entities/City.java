package com.MyP.Zilieri.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity@Table(name = "orase_table")
public class City {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id; // Assuming you have an auto-incremented ID column

    private Float x;
    private Float y;

    @Column(name = "NUME")
    private String nume;

    @Column(name = "JUDET")
    private String judet;

    @Column(name = "JUDET AUTO")
    private String judetAuto;

    @Column(name = "POPULATIE (in 2002)")
    private Integer populatieIn2002;

    @Column(name = "REGIUNE")
    private String regiune;

}
