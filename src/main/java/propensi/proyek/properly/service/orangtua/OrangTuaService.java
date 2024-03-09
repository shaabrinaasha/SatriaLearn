package propensi.proyek.properly.service.orangtua;

import java.util.UUID;

import propensi.proyek.properly.model.OrangTua;

public interface OrangTuaService {
    void addOrangTua(OrangTua orangTua);

    void updateOrangTua(OrangTua orangTua);
    OrangTua getOrangTuaById(UUID id);
}
