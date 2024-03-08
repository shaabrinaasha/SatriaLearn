package propensi.proyek.properly.service.siswa;

import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import jakarta.transaction.Transactional;
import propensi.proyek.properly.model.Siswa;
import propensi.proyek.properly.repository.SiswaDb;

@Service
@Transactional
public class SiswaServiceImpl implements SiswaService {

    @Autowired
    SiswaDb siswaDb;

    @Autowired
    BCryptPasswordEncoder encoder;

    @Override
    public List<Siswa> getAllSiswa() {
        return siswaDb.findAll();
    }

    @SuppressWarnings("null")
    @Override
    public void addSiswa(Siswa siswa) {
        siswa.setPassword(encoder.encode(siswa.getPasswordAwal()));
        siswaDb.save(siswa);
    }

    @Override
    public Siswa getSiswaById(UUID id) {
        return siswaDb.findById(id).orElse(null);
    }

}
