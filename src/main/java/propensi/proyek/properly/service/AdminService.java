package propensi.proyek.properly.service;

import java.util.List;

import propensi.proyek.properly.model.Admin;

public interface AdminService {
    void addAdmin(Admin admin);
    List<Admin> getAllAdmin();
}
