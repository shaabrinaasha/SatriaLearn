package propensi.proyek.properly.repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import propensi.proyek.properly.model.MataPelajaran;

@Repository
public interface MataPelajaranDb extends JpaRepository<MataPelajaran, UUID> {
    @SuppressWarnings("null")
    Optional<MataPelajaran> findById(@SuppressWarnings("null") UUID id);

    @Query("SELECT nama FROM MataPelajaran")
    List<String> findAllName();
}
