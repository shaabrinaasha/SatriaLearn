package propensi.proyek.properly.service;

import propensi.proyek.properly.model.Semester;
import propensi.proyek.properly.repository.SemesterDb;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import jakarta.transaction.Transactional;

@Service
@Transactional
public class SemesterServiceImpl implements SemesterService {

    @Autowired
    SemesterDb semesterDb;

    @SuppressWarnings("null")
    @Override
    public void addSemester(Semester semester) {
        semesterDb.save(semester);
    }
    
}
