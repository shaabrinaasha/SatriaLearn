package propensi.proyek.properly.repository;

import org.springframework.stereotype.Repository;

import propensi.proyek.properly.model.Siswa;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

@Repository
public interface SiswaDb extends JpaRepository<Siswa, UUID> {
    
}
