package propensi.proyek.properly.service.semester;

import java.time.LocalDate;

import propensi.proyek.properly.model.Semester;

public interface SemesterService {
    // save Semester in db via JPA
    void save(Semester semester);

    // check if two Semester's date overlapped
    Boolean isSemestersOverlap(LocalDate newTanggalAwal, LocalDate newTanggalAkhir);
}
