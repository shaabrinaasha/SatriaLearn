package propensi.proyek.properly.model;


import java.util.HashSet;
import java.util.Set;

import jakarta.persistence.Column;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@DiscriminatorValue("siswa")
public class Siswa extends User {
    @NotNull
    @Column(length = 9)
    @Size(min = 9, max = 9)
    private String nipd;

    @NotNull
    @Column(length = 10)
    @Size(min = 10, max = 10)
    private String nisn;

    @ManyToOne
    @JoinColumn(name = "ortu_id", nullable = true)
    private OrangTua orangTua;

    @ManyToMany(mappedBy = "siswas")
    private Set<Kelas> classes = new HashSet<>();

    @OneToMany(mappedBy = "siswa")
    Set<Nilai> nilais;

    @OneToMany(mappedBy = "siswa")
    Set<SiswaPresensi> siswaPresensis;
}
