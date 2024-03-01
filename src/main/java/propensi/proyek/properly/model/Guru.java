package propensi.proyek.properly.model;

import java.util.Set;

import jakarta.persistence.Column;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
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
@DiscriminatorValue("guru")
public class Guru extends Siswa {

    
    @Column(length = 16)
    @NotNull
    @Size(min = 16, max = 16)
    private String nuptk;

    
    @OneToOne(mappedBy = "wali", optional = true)
    private Kelas waliOf;

    @OneToMany(mappedBy = "guru")
    private Set<MataPelajaran> mataPelajarans;
}
