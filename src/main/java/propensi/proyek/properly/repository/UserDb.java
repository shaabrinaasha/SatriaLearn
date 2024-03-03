package propensi.proyek.properly.repository;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import propensi.proyek.properly.model.User;
import java.util.List;


@Repository
public interface UserDb extends JpaRepository<User, UUID> {
    List<User> findByUsername(String username);
}
