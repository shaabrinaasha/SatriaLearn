package propensi.proyek.properly.model;

import java.io.Serializable;
import java.time.LocalDate;
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
@Table(name = "presensi")
public class Presensi implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;
    @NotNull
    @Column(name="tanggal", nullable = false)
    private LocalDate tanggal;

    @ManyToOne
    @JoinColumn(name = "matpel_id", nullable = true)
    @OnDelete(action = OnDeleteAction.CASCADE)
    private MataPelajaran mataPelajaran;

    @OneToMany(mappedBy = "presensi")
    Set<SiswaPresensi> siswaPresensis;
}
