package propensi.proyek.properly.service;

import java.util.List;
import java.util.UUID;

import propensi.proyek.properly.model.Guru;

public interface GuruService {
    void addGuru(Guru guru);
    List<Guru> getListGuruActive();
    String getGuruMatpel(Guru guru);
    Guru getGuruById(UUID id);
}
