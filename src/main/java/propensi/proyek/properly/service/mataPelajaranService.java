package propensi.proyek.properly.service;
import java.util.List;
import java.util.UUID;

import propensi.proyek.properly.model.*;

public interface mataPelajaranService {
    List<MataPelajaran> getListMatpel();
    List<String> getAllMatpelName();
    MataPelajaran getMatpelById(UUID id);
    void addMatpel(MataPelajaran matpel);
    void deleteMatpel(MataPelajaran matpel);

}
