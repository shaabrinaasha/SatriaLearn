package propensi.proyek.properly.service.siswa;

import java.util.List;
import java.util.Set;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import jakarta.transaction.Transactional;
import propensi.proyek.properly.model.Kelas;
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

    @Override
    public Siswa getSiswaById(UUID id) {
        var optionalSiswa = siswaDb.findById(id);
        if (optionalSiswa.isEmpty()) throw new IllegalArgumentException("id not in database");
        return optionalSiswa.get();
    }

    @Override
    public List<Siswa> getAllSiswaWithUndocumentedParent() {
        return siswaDb.findAllByOrangTuaNull();
    }

    @SuppressWarnings("null")
    @Override
    public void addSiswa(Siswa siswa) {
        siswa.setPassword(encoder.encode(siswa.getPasswordAwal()));
        updateSiswa(siswa);
    }

    @Override
    public void updateSiswa(Siswa siswa) {
        siswaDb.save(siswa);
    }

    @SuppressWarnings("null")
    @Override
    public List<Siswa> getSiswasByIds(List<UUID> siswaIds) {
        return siswaDb.findAllById(siswaIds);
    }

    @Override
    public void addKelasToSiswa(Siswa siswa, Kelas kelas) {
        Set<Kelas> kelasSet = siswa.getClasses();
        kelasSet.add(kelas);
        siswa.setClasses(kelasSet);
        siswaDb.save(siswa);
    }

    @Override
    public void removeKelasFromSiswa(Siswa siswa, Kelas kelas) {
        Set<Kelas> kelasSet = siswa.getClasses();
        for (Kelas k : kelasSet) {
            if (k.getId().equals(kelas.getId())) {
                kelasSet.remove(k);
                break;
            }
        }
        siswa.setClasses(kelasSet);
        siswaDb.save(siswa);
    }

}
