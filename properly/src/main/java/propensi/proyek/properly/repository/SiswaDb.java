package propensi.proyek.properly.repository;

import org.springframework.stereotype.Repository;

import propensi.proyek.properly.model.Siswa;

import org.springframework.data.jpa.repository.JpaRepository;

@Repository
public interface SiswaDb extends JpaRepository<Siswa, String> {
    
}
