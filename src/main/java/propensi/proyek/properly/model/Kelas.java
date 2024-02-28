package propensi.proyek.properly.model;

import java.io.Serializable;
import java.util.Set;
import java.util.UUID;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
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
@Table(name = "kelas")
public class Kelas implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @NotNull
    @Column(nullable = false)
    private String nama;

    @OneToOne(optional = true)
    @JoinColumn(name = "id_wali", referencedColumnName = "id")
    private Guru wali;

    @ManyToMany
    @JoinTable(name = "siswa_kelas", joinColumns = @JoinColumn(name = "id_kelas", referencedColumnName = "id"), inverseJoinColumns = @JoinColumn(name="id_siswa", referencedColumnName = "id"))
    private Set<Siswa> siswas;

    @ManyToMany(mappedBy = "classes")
    private Set<Semester> semesters;

    @OneToMany(mappedBy = "kelas")
    private Set<MataPelajaran> mataPelajarans;
}
