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

    // there should not be >2 semester with the same tahunAjaran
    @Query(value = "SELECT COUNT(*) FROM semester s WHERE s.tahun_ajaran = (?1)", nativeQuery = true)
    long countSameTahunAjaran(String newTahunAjaran);

    // there should only be one ganjil/genap semester in the same tahunAjaran
    @Query(value = "SELECT COUNT(*) FROM semester s WHERE s.is_ganjil = (?1) and s.tahun_ajaran = (?2)", nativeQuery = true)
    long countSameTypeSameTahunAjaran(Boolean newIsGanjil, String newTahunAjaran);
}
