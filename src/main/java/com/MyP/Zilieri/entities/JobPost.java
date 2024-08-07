package com.MyP.Zilieri.entities;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "job_posts")
public class JobPost {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer jobId;

    @ManyToOne
    @JoinColumn(name = "employer_id", referencedColumnName = "employerId")
    private Employer employer;

    private String title;

    @Lob
    private String description;

    private String location;

    private BigDecimal payment;

    private LocalDateTime datePosted;

    private Date jobDate;

    @Enumerated(EnumType.STRING)
    private JobStatus status;
}
