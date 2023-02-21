package com.vshmaliukh.webstore.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "Persistent_tokens")
public class PersistentToken {

    @Id
    @Column(nullable = false)
    private String series;

    private String username;
    private String token;

    @Column(name = "last_used")
    private Date lastUsed;

    public PersistentToken(String username, String series, String token, Date lastUsed) {
        this.series = series;
        this.username = username;
        this.token = token;
        this.lastUsed = lastUsed;
    }

}
