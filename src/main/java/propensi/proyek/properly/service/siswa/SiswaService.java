package propensi.proyek.properly.service.siswa;

import propensi.proyek.properly.model.Guru;
import propensi.proyek.properly.model.Siswa;

import java.util.List;
import java.util.UUID;

public interface SiswaService {
    void addSiswa(Siswa siswa);

    List<Siswa> getAllSiswa();
    Siswa getSiswaById(UUID id);
}
