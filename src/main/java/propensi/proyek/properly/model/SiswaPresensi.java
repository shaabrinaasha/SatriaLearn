package propensi.proyek.properly.model;

import java.io.Serializable;
import java.util.UUID;

import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
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
@Table(name = "siswa_presensi")
public class SiswaPresensi implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne
    @JoinColumn(name = "id_siswa")
    Siswa siswa;

    @ManyToOne
    @JoinColumn(name = "id_presensi")
    @OnDelete(action = OnDeleteAction.SET_NULL)
    Presensi presensi;
    
    // Alpa / Hadir / Sakit / Izin
    @NotNull
    @Column(name = "status", nullable = false)
    String status;
}
