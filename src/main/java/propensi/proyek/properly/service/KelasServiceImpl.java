package propensi.proyek.properly.service;

import propensi.proyek.properly.model.Kelas;
import propensi.proyek.properly.repository.KelasDb;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import jakarta.transaction.Transactional;

@Service
@Transactional
public class KelasServiceImpl implements KelasService {

    @Autowired
    KelasDb kelasDb;

    @SuppressWarnings("null")
    @Override
    public void addKelas(Kelas kelas) {
        kelasDb.save(kelas);
    }
    
}
