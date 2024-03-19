package propensi.proyek.properly.service.komponenPenilaian;

import propensi.proyek.properly.model.KomponenPenilaian;
import propensi.proyek.properly.repository.KomponenPenilaianDb;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import jakarta.transaction.Transactional;

@Service
@Transactional
public class KomponenPenilaianServiceImpl implements KomponenPenilaianService {

    @Autowired
    KomponenPenilaianDb komponenPenilaianDb;

    @SuppressWarnings("null")
    @Override
    public void addKomponenPenilaian(KomponenPenilaian komponenPenilaian) {
        komponenPenilaianDb.save(komponenPenilaian);
    }

}
