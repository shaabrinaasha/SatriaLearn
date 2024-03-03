package propensi.proyek.properly.service;

import propensi.proyek.properly.model.MataPelajaran;
import propensi.proyek.properly.repository.MataPelajaranDb;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import jakarta.transaction.Transactional;

@Service
@Transactional
public class MataPelajaranServiceImpl implements MataPelajaranService {

    @Autowired
    MataPelajaranDb mataPelajaranDb;

    @SuppressWarnings("null")
    @Override
    public void addMataPelajaran(MataPelajaran mataPelajaran) {
        mataPelajaranDb.save(mataPelajaran);
    }
    
}
