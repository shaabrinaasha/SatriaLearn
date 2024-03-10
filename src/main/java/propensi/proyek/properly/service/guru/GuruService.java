package propensi.proyek.properly.service.guru;

import java.util.List;
import java.util.UUID;

import propensi.proyek.properly.model.Guru;
import propensi.proyek.properly.model.MataPelajaran;

public interface GuruService {
    void addGuru(Guru guru);

    List<Guru> getAllGuru();

    void updateGuru(Guru guru);

    List<Guru> getListGuruActive();

    String getGuruMatpel(Guru guru);

    Guru getGuruById(UUID id);
}
