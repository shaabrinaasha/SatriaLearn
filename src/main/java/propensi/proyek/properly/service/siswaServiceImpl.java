package propensi.proyek.properly.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import jakarta.transaction.Transactional;
import propensi.proyek.properly.model.Siswa;
import propensi.proyek.properly.repository.SiswaDb;

@Service
@Transactional
public class siswaServiceImpl implements siswaService {

    @Autowired
    SiswaDb siswaDb;

    @Override
    public List<Siswa> getAllSiswa() {
        return siswaDb.findAll();
    }
    
}
