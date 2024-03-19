package propensi.proyek.properly.service.siswaPresensi;

import propensi.proyek.properly.model.SiswaPresensi;
import propensi.proyek.properly.repository.SiswaPresensiDb;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import jakarta.transaction.Transactional;

@Service
@Transactional
public class SiswaPresensiServiceImpl implements SiswaPresensiService {

    @Autowired
    SiswaPresensiDb siswaPresensiDb;

    @SuppressWarnings("null")
    @Override
    public void addSiswaPresensi(SiswaPresensi siswaPresensi) {
        siswaPresensiDb.save(siswaPresensi);
    }

}
