package org.tm30.domain;

import lombok.*;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "m_comment")
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Comment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;
    @Column(name = "commenter_id")
    private Long commenterId;

    @Column(name = "twit_id")
    private Long twitId;
    @Lob
    @Column(name = "body")
    private String body;

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
