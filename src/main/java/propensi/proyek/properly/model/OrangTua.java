package propensi.proyek.properly.model;

import java.util.List;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@DiscriminatorValue("orang tua")
public class OrangTua extends User {
    
    @OneToMany(mappedBy = "orangTua")
    private List<Siswa> siswas;
    
}
