package propensi.proyek.properly.service.tugas;

import propensi.proyek.properly.model.PengumumanTugas;
import propensi.proyek.properly.repository.PengumumanTugasDb;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import jakarta.transaction.Transactional;

@Service
@Transactional
public class PengumumanTugasServiceImpl implements PengumumanTugasService {

    @Autowired
    PengumumanTugasDb pengumumanTugasDb;

    @SuppressWarnings("null")
    @Override
    public void addPengumumanTugas(PengumumanTugas pengumumanTugas) {
        pengumumanTugasDb.save(pengumumanTugas);
    }


}
