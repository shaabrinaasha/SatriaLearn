package propensi.proyek.properly.Dto.akuns;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.UUID;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import propensi.proyek.properly.model.Guru;
import propensi.proyek.properly.model.OrangTua;
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
    private List<UUID> orangTuaOf = new ArrayList<>(1);
    private List<Siswa> orangTuaSiswaOf;
    private UUID id;

    public Siswa toSiswa() {
        var siswa = new Siswa();
        siswa.setNama(nama);
        siswa.setNipd(nipd);
        siswa.setNisn(nisn);
        return siswa;
    }

    public Guru toGuru() {
        var guru = new Guru();
        guru.setNama(nama);
        guru.setNuptk(nuptk);
        return guru;
    }

    public OrangTua toOrangTua(List<Siswa> siswas) {
        var orangTua = new OrangTua();
        orangTua.setNama(nama);
        if (siswas == null) {
            orangTua.setSiswas(new ArrayList<>());
        } else {
            orangTua.setSiswas(siswas);
        }
        return orangTua;
    }

}
