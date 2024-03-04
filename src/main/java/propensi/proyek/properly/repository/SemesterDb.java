package propensi.proyek.properly.repository;

import java.time.LocalDate;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import propensi.proyek.properly.model.Semester;

@Repository
public interface SemesterDb extends JpaRepository<Semester, UUID> {
    // overlap if >0
    @Query(value = "SELECT COUNT(*) FROM semester s WHERE (?1) <= s.tanggal_akhir AND (?2) >= s.tanggal_awal", nativeQuery = true)
    long isOverlap(LocalDate newTanggalAwal, LocalDate newTanggalAkhir);
}
