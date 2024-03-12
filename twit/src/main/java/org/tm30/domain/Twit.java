package org.tm30.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "m_twit")
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Twit {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;
    @Column(name = "app_user_id")
    private Long appUserId;
    @Lob
    @Column(name = "body")
    private String body;
    @Column(name = "title")
    private String title;

    @OneToMany
    private Set<Like> likes = new HashSet<>();

    @Column(name = "created_date")
    @Temporal(TemporalType.TIMESTAMP)
    private Date createdDate = new Date();

    @Column(name = "lastmodified_date")
    @Temporal(TemporalType.TIMESTAMP)
    private Date lastModifiedDate = new Date();


    @PrePersist
    public void onCreationTimestamp() {
        createdDate = new Date();
        lastModifiedDate = new Date();
    }

    @PreUpdate
    public void onUpdateTimestamp() {
        lastModifiedDate = new Date();
    }


}
