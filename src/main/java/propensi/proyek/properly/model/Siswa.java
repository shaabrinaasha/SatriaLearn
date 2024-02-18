package propensi.proyek.properly.model;

import java.io.Serializable;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
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
@Table(name="siswa")
public class Siswa implements Serializable {
    @Id
    @Column(name = "id")
    private String id;

    @NotNull
    @Column(name = "nama", nullable = false)
    private String nama;
}
