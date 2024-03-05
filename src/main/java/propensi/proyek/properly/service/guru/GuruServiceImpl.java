package propensi.proyek.properly.service.guru;

import propensi.proyek.properly.model.Guru;
import propensi.proyek.properly.model.MataPelajaran;
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
        List<Guru> listGuruActive = new ArrayList<>();
        for (Guru guru : guruDb.findAll()) {
            if (guru.getIsActive()) {
                listGuruActive.add(guru);
            }
        }
        return listGuruActive;
    }

    @Override
    public String getGuruMatpel(Guru guru) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getGuruMatpel'");
    }

    @Override
    public Guru getGuruById(UUID id) {
        return guruDb.findById(id).orElse(null);
    }

}
