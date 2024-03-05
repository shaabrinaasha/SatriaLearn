package propensi.proyek.properly.service.matapelajaran;
import java.util.List;
import java.util.UUID;

import propensi.proyek.properly.model.*;

public interface MataPelajaranService {
    List<MataPelajaran> getListMatpel();
    List<String> getAllMatpelName();
    MataPelajaran getMatpelById(UUID id);
    void addMataPelajaran(MataPelajaran matpel);
    void deleteMatpel(MataPelajaran matpel);


}
