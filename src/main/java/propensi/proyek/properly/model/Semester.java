package propensi.proyek.properly.model;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.Set;
import java.util.UUID;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import jakarta.persistence.GenerationType;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "semester")
public class Semester implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @NotNull
    @Column(name = "is_ganjil", nullable = false)
    private Boolean isGanjil;

    @NotNull
    @Column(name = "tanggal_awal", nullable = false)
    private LocalDate tanggalAwal;

    @NotNull
    @Column(name="tanggal_akhir", nullable = false)
    private LocalDate tanggalAkhir;

    @NotNull
    @Column(nullable = false, name = "tahun_ajaran")
    private String tahunAjaran;

    @ManyToMany()
    @JoinTable(
        name = "semester_kelas",
        joinColumns = @JoinColumn(name = "id_semester", referencedColumnName = "id"),
        inverseJoinColumns = @JoinColumn(name = "id_kelas", referencedColumnName = "id")
    )
    private Set<Kelas> classes;
}
