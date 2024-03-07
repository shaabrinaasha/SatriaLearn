package propensi.proyek.properly.Dto.akuns;

import java.util.HashSet;
import java.util.UUID;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import propensi.proyek.properly.model.Siswa;

@Getter
@Setter
@NoArgsConstructor
public class NewUserRequestDto {
    private String peran;
    private String nama;
    private String nipd;
    private String nisn;
    private String nuptk;
    private UUID orangTuaOf;

    public Siswa toSiswa() {
        var siswa = new Siswa();
        siswa.setNama(nama);
        siswa.setNipd(nipd);
        siswa.setNisn(nisn);
        return siswa;
    }
}
