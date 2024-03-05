package propensi.proyek.properly.service.kelas;

import java.util.List;
import java.util.UUID;

import propensi.proyek.properly.model.Kelas;

public interface KelasService {
    void addKelas(Kelas kelas);
    List<Kelas> getAllKelas();
    Kelas getKelasById(UUID id);
}
