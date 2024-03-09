package propensi.proyek.properly.service.orangtua;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import propensi.proyek.properly.model.OrangTua;
import propensi.proyek.properly.model.Siswa;
import propensi.proyek.properly.repository.OrangTuaDb;

import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import jakarta.transaction.Transactional;
import propensi.proyek.properly.service.siswa.SiswaService;

@Service
@Transactional
public class OrangTuaServiceImpl implements OrangTuaService {

    @Autowired
    OrangTuaDb orangTuaDb;

    @Autowired
    BCryptPasswordEncoder encoder;

    @Autowired
    SiswaService siswaService;

    @SuppressWarnings("null")
    @Override
    public void addOrangTua(OrangTua orangTua) {
        var password = encoder.encode(orangTua.getPasswordAwal());
        orangTua.setPassword(password);
        updateOrangTua(orangTua);
    }

    @Override
    public void updateOrangTua(OrangTua orangTua) {
        if (orangTua.getSiswas() != null) {
            for (Siswa siswa: orangTua.getSiswas()) {
                siswa.setOrangTua(orangTua);
                siswaService.updateSiswa(siswa);
            }
        }
        orangTuaDb.save(orangTua);

    }

    @Override
    public OrangTua getOrangTuaById(UUID id) {
        var optionalOrtu = orangTuaDb.findById(id);
        if (optionalOrtu.isEmpty()) throw new IllegalArgumentException("id not in database");
        return optionalOrtu.get();
    }

}
