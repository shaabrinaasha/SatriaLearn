package propensi.proyek.properly.service;

import propensi.proyek.properly.model.Admin;
import propensi.proyek.properly.repository.AdminDb;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import jakarta.transaction.Transactional;

@Service
@Transactional
public class AdminServiceImpl implements AdminService {

    @Autowired
    AdminDb adminDb;

    @SuppressWarnings("null")
    @Override
    public void addAdmin(Admin admin) {
        adminDb.save(admin);
    }

    @Override
    public List<Admin> getAllAdmin() {
        return adminDb.findAll();
    }
    
}
