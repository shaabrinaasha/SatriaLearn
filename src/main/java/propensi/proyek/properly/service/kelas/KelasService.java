package propensi.proyek.properly.service.kelas;

import java.util.List;
import java.util.Set;
import java.util.UUID;

import propensi.proyek.properly.model.Kelas;
import propensi.proyek.properly.model.Semester;
import propensi.proyek.properly.model.Siswa;

public interface KelasService {
    void addKelas(Kelas kelas);
    List<Kelas> getAllKelas();
    void addSemestersToKelas(Kelas kelas, Set<Semester> semesters);
    Kelas getKelasById(UUID id);
    String deleteKelas(Kelas kelas);
    void updateKelasWithSemesters(Kelas kelas, List<Semester> semesters);
    void updateSiswaKelas(Kelas kelas, Set<Siswa> siswas);
}
