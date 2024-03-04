package propensi.proyek.properly.service.siswa;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import jakarta.transaction.Transactional;
import propensi.proyek.properly.model.Siswa;
import propensi.proyek.properly.repository.SiswaDb;

@Service
@Transactional
public class SiswaServiceImpl implements SiswaService {

    @Autowired
    SiswaDb siswaDb;

    @Override
    public List<Siswa> getAllSiswa() {
        return siswaDb.findAll();
    }

    @SuppressWarnings("null")
    @Override
    public void addSiswa(Siswa siswa) {
        siswaDb.save(siswa);
    }

}
