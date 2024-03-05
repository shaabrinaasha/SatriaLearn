package propensi.proyek.properly.service.matapelajaran;

import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import jakarta.transaction.Transactional;
import propensi.proyek.properly.model.MataPelajaran;
import propensi.proyek.properly.repository.MataPelajaranDb;

@Service
@Transactional
public class MataPelajaranServiceImpl implements MataPelajaranService{

    @Autowired
    private MataPelajaranDb mataPelajaranDb;
    
    @Override
    public List<MataPelajaran> getListMatpel() {
        return mataPelajaranDb.findAll();
    }

    @Override
    public List<String> getAllMatpelName() {
        return mataPelajaranDb.findAllName();
    }

    @SuppressWarnings("null")
    @Override
    public MataPelajaran getMatpelById(UUID id) {
        return mataPelajaranDb.findById(id).orElse(null);
    }

    @SuppressWarnings("null")
    @Override
    public void addMataPelajaran(MataPelajaran matpel) {
        mataPelajaranDb.save(matpel);
    }

    @SuppressWarnings("null")
    @Override
    public void deleteMatpel(MataPelajaran matpel) {
       mataPelajaranDb.delete(matpel);
    }
    
}
