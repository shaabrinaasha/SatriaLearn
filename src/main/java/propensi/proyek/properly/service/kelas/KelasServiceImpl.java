package propensi.proyek.properly.service.kelas;

import propensi.proyek.properly.model.Kelas;
import propensi.proyek.properly.repository.KelasDb;

import java.util.List;
import java.util.UUID;

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

    @Override
    public List<Kelas> getAllKelas() {
        return kelasDb.findAll();
    }

    @Override
    public Kelas getKelasById(UUID id) {
        return kelasDb.findById(id).orElse(null);
    }

}
