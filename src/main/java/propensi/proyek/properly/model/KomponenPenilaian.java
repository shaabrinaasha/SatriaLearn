package propensi.proyek.properly.model;

import java.io.Serializable;
import java.util.Set;
import java.util.UUID;

import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
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
@Table(name = "komponen_penilaian")
public class KomponenPenilaian implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @NotNull
    @Column(name = "nama", nullable = false)
    private String nama;

    // Pengetahuan / Keterampilan
    @NotNull
    @Column(name = "klasifikasi", nullable = false)
    private String klasifikasi;

    // Ulangan Harian / Tugas / Ujian
    @NotNull
    @Column(nullable = false, name = "tipe")
    private String tipe;

    @NotNull
    @Column(name="bobot", nullable = false)
    private Integer bobot;

    @ManyToOne
    @JoinColumn(name = "matpel_id", nullable = true)
    @OnDelete(action = OnDeleteAction.CASCADE)
    private MataPelajaran mataPelajaran;

    @ManyToOne
    @JoinColumn(name = "semester_id", nullable = true)
    private Semester semester;

    
    @OneToMany(mappedBy = "komponenPenilaian")
    Set<Nilai> nilais;
}
