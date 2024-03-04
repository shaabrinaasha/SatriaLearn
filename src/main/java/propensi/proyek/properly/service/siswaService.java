package propensi.proyek.properly.service;

import propensi.proyek.properly.model.Siswa;

import java.util.List;

public interface SiswaService {
    void addSiswa(Siswa siswa);
    List<Siswa> getAllSiswa();
}
