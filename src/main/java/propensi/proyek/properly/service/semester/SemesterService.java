package propensi.proyek.properly.service.semester;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

import propensi.proyek.properly.Dto.semester.UpdateSemesterDTO;
import propensi.proyek.properly.model.Semester;

public interface SemesterService {
    // save Semester in db via JPA
    void save(Semester semester);

    // check if two Semester's date overlapped
    long semestersOverlap(LocalDate newTanggalAwal, LocalDate newTanggalAkhir);

    // get all Semester via JPA
    List<Semester> getAllSemesters();

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
