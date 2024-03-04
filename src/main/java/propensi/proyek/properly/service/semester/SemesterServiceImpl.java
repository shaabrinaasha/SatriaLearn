package propensi.proyek.properly.service.semester;

import java.time.LocalDate;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import propensi.proyek.properly.model.Semester;
import propensi.proyek.properly.repository.SemesterDb;

@Service
public class SemesterServiceImpl implements SemesterService {
    @Autowired
    SemesterDb semesterDb;

    @Override
    public void save(Semester semester) {
        // need validation for the dates
        semesterDb.save(semester);
    }

    @Override
    public Boolean isSemestersOverlap(LocalDate newTanggalAwal, LocalDate newTanggalAkhir) {
        try {
            var resultIsOverlap = semesterDb.isOverlap(newTanggalAwal, newTanggalAkhir);
            if (resultIsOverlap > 0) {
                return true;
            }
            return false;
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
            return true;
        }
    }

}