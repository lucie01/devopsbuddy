package com.devopsbuddy.backend.persistence.domain.backend;

import com.devopsbuddy.backend.persistence.converters.LocalDateTimeAttributeConverter;
import lombok.*;
import lombok.extern.slf4j.Slf4j;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;
@NoArgsConstructor
@Getter
@Setter
@Slf4j
@Entity
public class PasswordResetToken implements Serializable {

    /**
     * The serial version UID for Serializable classes.
    */
    private static final long serialVersionUID = 1L;
    private static final int DEFAULT_TOKEN_LENGTH_IN_MINUTES = 120;


    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @EqualsAndHashCode.Include
    private long id;
    @Column(unique = true)
    private String token;
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id")
    private User user;
    @Column(name = "expiry_date")
    @Convert(converter = LocalDateTimeAttributeConverter.class)
    private LocalDateTime expiryDate;

    /**
     * Full constructor
     * @param token The user token. It must not be null.
     * @param user The user for which the token should be created. It must not be null.
     * @param creationDateTime The date time when this request was created. It must not be null.
     * @param expirationInMinutes The length in minutes , for wich this token will be valid. If less or equal to zero, it will be
     *                            assigned to default value of 120 (2 hours) minutes.
     * @throws IllegalArgumentException If the token, user of creation date time are null.
     */
    public PasswordResetToken(String token, User user, LocalDateTime creationDateTime, int expirationInMinutes) {
        if((null == token) || (null == user) ||(null == creationDateTime)) throw new IllegalArgumentException("token, user and creationTime can't be null");

        if(expirationInMinutes <= 0){
            log.info("The token expiration length in minutes is zero. Assigning the default value {}.", DEFAULT_TOKEN_LENGTH_IN_MINUTES);
            expirationInMinutes = DEFAULT_TOKEN_LENGTH_IN_MINUTES;
        }
        this.token = token;
        this.user = user;
        this.expiryDate = creationDateTime.plusMinutes(expirationInMinutes);
    }
}
