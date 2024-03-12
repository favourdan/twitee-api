package org.tm30.domain;

import lombok.*;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "m_like")
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Like {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;
    @ManyToOne
    @JoinColumn(name = "twit_id") // twit owner
    private Twit twitId;
    @Column(name = "app_user_id") // the one who liked
    private Long appUserId;
    @Column(name = "is_liked")
    private Boolean isLiked;
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
