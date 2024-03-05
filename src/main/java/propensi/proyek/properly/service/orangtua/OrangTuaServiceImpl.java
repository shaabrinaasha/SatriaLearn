package propensi.proyek.properly.service.orangtua;

import propensi.proyek.properly.model.OrangTua;
import propensi.proyek.properly.repository.OrangTuaDb;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import jakarta.transaction.Transactional;

@Service
@Transactional
public class OrangTuaServiceImpl implements OrangTuaService {

    @Autowired
    OrangTuaDb orangTuaDb;

    @SuppressWarnings("null")
    @Override
    public void addOrangTua(OrangTua orangTua) {
        orangTuaDb.save(orangTua);
    }

}
