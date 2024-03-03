package propensi.proyek.properly.model;

import java.io.Serializable;
import java.util.UUID;

import jakarta.persistence.Column;
import jakarta.persistence.DiscriminatorColumn;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Inheritance;
import jakarta.persistence.InheritanceType;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Inheritance(strategy = InheritanceType.JOINED)
@DiscriminatorColumn(name = "role")
@Table(name="user")
public class User implements Serializable {
    @Id
    // @GeneratedValue(strategy = GenerationType.UUID)
    @GeneratedValue(generator = "uuid2")
    private UUID id;
    
    @NotNull
    @Column(length = 255, nullable = false)
    private String username;

    @NotNull
    @Column(length = 255, nullable = false)
    private String password;

    @NotNull
    @Column(length = 255, nullable = false)
    private String nama;

    @NotNull()
    @Column(name = "is_active", nullable = false)
    private Boolean isActive = true;

    @NotNull
    @Column(name = "password_awal", nullable = false)
    private String passwordAwal;
}
