package propensi.proyek.properly.service.presensi;

import propensi.proyek.properly.model.Presensi;
import propensi.proyek.properly.repository.PresensiDb;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import jakarta.transaction.Transactional;

@Service
@Transactional
public class PresensiServiceImpl implements PresensiService {

    @Autowired
    PresensiDb presensiDb;

    @SuppressWarnings("null")
    @Override
    public void addPresensi(Presensi presensi) {
        presensiDb.save(presensi);
    }
}
