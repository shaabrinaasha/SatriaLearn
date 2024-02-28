package propensi.proyek.properly.repository;

import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import propensi.proyek.properly.model.Kelas;

@Repository
public interface KelasDb extends JpaRepository<Kelas, UUID> {
    
}
