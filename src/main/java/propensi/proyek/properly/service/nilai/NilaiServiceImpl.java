package propensi.proyek.properly.service.nilai;

import propensi.proyek.properly.model.Nilai;
import propensi.proyek.properly.repository.NilaiDb;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import jakarta.transaction.Transactional;

@Service
@Transactional
public class NilaiServiceImpl implements NilaiService {

    @Autowired
    NilaiDb nilaiDb;

    @SuppressWarnings("null")
    @Override
    public void addNilai(Nilai nilai) {
        nilaiDb.save(nilai);
    }

}