package propensi.proyek.properly.service.kelas;

import propensi.proyek.properly.model.Kelas;
import propensi.proyek.properly.model.Semester;
import propensi.proyek.properly.model.Siswa;
import propensi.proyek.properly.repository.SiswaDb;
import propensi.proyek.properly.service.semester.SemesterService;
import propensi.proyek.properly.repository.KelasDb;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import jakarta.transaction.Transactional;

@Service
@Transactional
public class KelasServiceImpl implements KelasService {

    @Autowired
    KelasDb kelasDb;

    @Autowired
    SiswaDb siswaDb;

    @Autowired
    SemesterService semesterService;

    @SuppressWarnings("null")
    @Override
    public void addKelas(Kelas kelas) {
        kelasDb.save(kelas);
    }

    @Override
    public List<Kelas> getAllKelas() {
        return kelasDb.findAll();
    }

    @Override
    public void addSemestersToKelas(Kelas kelas, Set<Semester> semesters) {
        kelas.setSemesters(semesters);
        kelasDb.save(kelas);
    }


    @SuppressWarnings("null")
    @Override
    public String deleteKelas(Kelas kelas) {
        // check if kelas still has any siswa
        if (kelas.getSiswas().size() > 0) {
            return "Gagal menghapus kelas! Kelas masih memiliki siswa";
        }

        // check if kelas still has any semester
        if (kelas.getMataPelajarans().size() > 0) {
            return "Gagal menghapus kelas! Kelas masih memiliki mata pelajaran";
        }

        // check if kelas is still in between the semester
        if (kelas.getSemesters().size() > 0) {
            LocalDate latestTime = null;
            for (Semester semester : kelas.getSemesters()) {
                if (latestTime == null || semester.getTanggalAkhir().isAfter(latestTime)) {
                    latestTime = semester.getTanggalAkhir();
                }
            }

            if (latestTime.isAfter(LocalDate.now())) {
                return "Gagal menghapus kelas! Kelas masih berlangsung di semester yang belum berakhir";
            }
        }

        for (Semester semester : kelas.getSemesters()) {
            Set<Kelas> kelasSet = semester.getClasses();
            kelasSet.remove(kelas);
            semester.setClasses(kelasSet);
        }

        kelasDb.delete(kelas);
        return null;
    }

    @Override
    public void updateKelasWithSemesters(Kelas kelas, List<Semester> newSemesters) {
        kelas.setSemesters(new HashSet<>(newSemesters));
        kelasDb.save(kelas);
    }

    @Override
    public void updateSiswaKelas(Kelas kelas, Set<Siswa> siswas) {
        kelas.setSiswas(siswas);
        kelasDb.save(kelas);
    }
    public Kelas getKelasById(UUID id) {
        return kelasDb.findById(id).orElse(null);
    }

}
