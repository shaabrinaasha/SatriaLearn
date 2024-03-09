package propensi.proyek.properly.service.semester;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

import propensi.proyek.properly.model.Kelas;
import propensi.proyek.properly.Dto.semester.UpdateSemesterDTO;
import propensi.proyek.properly.model.Semester;

public interface SemesterService {
    // save Semester in db via JPA
    void save(Semester semester);

    List<Semester> findSemestersById(List<UUID> semesterIds);
    List<Semester> getAllSemesterByTahunAjaran(String tahunAjaran);
    List<Semester> getAllSemester();
    void addKelasToSemester(Semester semester, Kelas kelas);
    void removeKelasFromSemester(Semester semester, Kelas kelas);
    long semestersOverlap(LocalDate newTanggalAwal, LocalDate newTanggalAkhir);

    // find semester by id via JPA
    Semester getSemesterById(UUID id);

    // update semester via JPA
    Semester updateSemester(Semester semesterFromDTO);

    // delete semester by id via JPA
    void deleteSemester(UUID id);

    // semester name generator
    String semesterNameGenerator(Boolean isGanjil, String tahunAjaran);

    // count the same tahunAjaran
    long countTahunAjaran(String newTahunAjaran);

    // count the same type with the same tahunAjaran
    long countTypeTahunAjaran(Boolean newIsGanjil, String newTahunAjaran);

    // check what data changed in updateSemesterDTO compared to the semester in db
    List<String> whatHasChanged(UpdateSemesterDTO updateSemesterDTO);
}
