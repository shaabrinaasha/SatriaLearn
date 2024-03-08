package propensi.proyek.properly.service.orangtua;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
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

    @Autowired
    BCryptPasswordEncoder encoder;

    @SuppressWarnings("null")
    @Override
    public void addOrangTua(OrangTua orangTua) {
        var password = encoder.encode(orangTua.getPasswordAwal());
        orangTua.setPassword(password);
        orangTuaDb.save(orangTua);
    }

}
