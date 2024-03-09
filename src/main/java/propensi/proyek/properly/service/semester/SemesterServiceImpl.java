package propensi.proyek.properly.service.semester;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import propensi.proyek.properly.Dto.semester.UpdateSemesterDTO;
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
    public long semestersOverlap(LocalDate newTanggalAwal, LocalDate newTanggalAkhir) {
        try {
            var result = semesterDb.isOverlap(newTanggalAwal, newTanggalAkhir);
            return result;
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
            return -1;
        }
    }

    @Override
    public List<Semester> getAllSemesters() {
        return semesterDb.findAll();
    }

    @Override
    public Semester getSemesterById(UUID id) {
        return semesterDb.findById(id).get();
    }

    @Override
    public Semester updateSemester(Semester semesterFromDTO) {
        Semester semester = getSemesterById(semesterFromDTO.getId());
        if (semester != null) {
            semester.setIsGanjil(semesterFromDTO.getIsGanjil());
            semester.setTanggalAwal(semesterFromDTO.getTanggalAwal());
            semester.setTanggalAkhir(semesterFromDTO.getTanggalAkhir());
            semester.setTahunAjaran(semesterFromDTO.getTahunAjaran());
            semesterDb.save(semester);
        }
        return semester;
    }

    @Override
    public void deleteSemester(UUID id) {
        var semester = getSemesterById(id);
        if (semester != null) {
            semesterDb.delete(semester);
        }
        // perlu throw something kayaknya
    }

    @Override
    public String semesterNameGenerator(Boolean isGanjil, String tahunAjaran) {
        var result = "Semester ";
        if (isGanjil) {
            result += "Ganjil ";
        } else {
            result += "Genap ";
        }
        result += tahunAjaran;
        return result;
    }

    @Override
    public long countTahunAjaran(String newTahunAjaran) {
        try {
            var result = semesterDb.countSameTahunAjaran(newTahunAjaran);
            return result;
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
            return -1;
        }
    }

    @Override
    public long countTypeTahunAjaran(Boolean newIsGanjil, String newTahunAjaran) {
        try {
            var result = semesterDb.countSameTypeSameTahunAjaran(newIsGanjil, newTahunAjaran);
            return result;
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
            return -1;
        }
    }

    @Override
    public List<String> whatHasChanged(UpdateSemesterDTO updateSemesterDTO) {
        // get old semester that is going to be updated
        var oldSemester = getSemesterById(updateSemesterDTO.getId());
        List<String> changedFields = new ArrayList<String>();
        if (!oldSemester.getIsGanjil().equals(updateSemesterDTO.getIsGanjil())) {
            changedFields.add("isGanjil");
        } else if (!(oldSemester.getTanggalAwal().equals(updateSemesterDTO.getTanggalAwal())
                || oldSemester.getTanggalAkhir().equals(updateSemesterDTO.getTanggalAkhir()))) {
            changedFields.add("tanggal");
        } else if (!(oldSemester.getTahunAjaran().equals(updateSemesterDTO.getTahunAjaran()))) {
            changedFields.add("tahunAjaran");
        }
        return changedFields;
    }
}