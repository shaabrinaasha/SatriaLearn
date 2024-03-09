package propensi.proyek.properly.service.siswa;

import propensi.proyek.properly.model.Kelas;
import propensi.proyek.properly.model.Siswa;

import java.util.List;
import java.util.UUID;

public interface SiswaService {
    void addSiswa(Siswa siswa);

    void updateSiswa(Siswa siswa);

    List<Siswa> getAllSiswa();

    List<Siswa> getSiswasByIds(List<UUID> siswaIds);

    Siswa getSiswaById(UUID id);

    void addKelasToSiswa(Siswa siswa, Kelas kelas);

    void removeKelasFromSiswa(Siswa siswa, Kelas kelas);

    List<Siswa> getAllSiswaWithUndocumentedParent();
}
