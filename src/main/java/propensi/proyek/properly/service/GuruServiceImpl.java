package propensi.proyek.properly.service;

import propensi.proyek.properly.model.Guru;
import propensi.proyek.properly.repository.GuruDb;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import jakarta.transaction.Transactional;

@Service
@Transactional
public class GuruServiceImpl implements GuruService {

    @Autowired
    GuruDb guruDb;

    @SuppressWarnings("null")
    @Override
    public void addGuru(Guru guru) {
        guruDb.save(guru);
    }
    
}
