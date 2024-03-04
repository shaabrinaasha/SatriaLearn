package propensi.proyek.properly.service;

import propensi.proyek.properly.model.Guru;
import propensi.proyek.properly.repository.GuruDb;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import jakarta.transaction.Transactional;

@Service
@Transactional
public class GuruServiceImpl implements GuruService {

    @Autowired
    private GuruDb guruDb;

    @SuppressWarnings("null")
    @Override
    public void addGuru(Guru guru) {
        guruDb.save(guru);
    }

    @Override
    public List<Guru> getListGuruActive() {
        // List<Guru> ListGuruActive = new ArrayList<>();
        // for (Guru guru : GuruDb.findAll()) {
        //     if (guru.getIsActive()) {
        //         listPengajarActive.add(pengajar);
        //     }
        // }
        // return listPengajarActive;
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getGuruMatpel'");
    }

    @Override
    public String getGuruMatpel(Guru guru) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getGuruMatpel'");
    }

    @Override
    public Guru getGuruById(UUID id) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getGuruById'");
    }
    
}
