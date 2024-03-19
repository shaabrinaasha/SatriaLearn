package propensi.proyek.properly.service.materi;

import propensi.proyek.properly.model.Materi;
import propensi.proyek.properly.repository.MateriDb;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import jakarta.transaction.Transactional;

@Service
@Transactional
public class MateriServiceImpl implements MateriService {

    @Autowired
    MateriDb materiDb;

    @SuppressWarnings("null")
    @Override
    public void addMateri(Materi materi) {
        materiDb.save(materi);
    }


}
