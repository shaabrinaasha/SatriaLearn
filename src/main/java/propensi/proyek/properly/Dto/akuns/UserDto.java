package propensi.proyek.properly.Dto.akuns;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import propensi.proyek.properly.model.*;

import java.util.Set;
import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
public class UserDto {
    private UUID id;
    private String nama;
    private String peran;
    private String nomorIdentifikasi;
    private String kelas;

    public static UserDto fromUser(User user) {
        switch (user.getDecriminatorValue()) {
            case "siswa":
                return fromSiswa((Siswa) user);
            case "guru":
                return fromGuru((Guru) user);
            case "orang tua":
                return fromOrangTua((OrangTua) user);
            default:
                return fromAdmin((Admin) user);
        }
    }

    private static UserDto fromSiswa(Siswa siswa) {
        return new UserDto(siswa.getId(), siswa.getNama(), "Siswa", siswa.getNisn(), getIndexableClassFromClasses(siswa.getClasses()));
    }

    private static UserDto fromGuru(Guru guru) {
        if (guru.getWaliOf() == null)  return new UserDto(guru.getId(), guru.getNama(), "Guru", guru.getNuptk(), null);
        return new UserDto(guru.getId(), guru.getNama(), "Guru", guru.getNuptk(), guru.getWaliOf().getNama());
    }

    private static UserDto fromOrangTua(OrangTua orangTua) {
        return new UserDto(orangTua.getId(), orangTua.getNama(), "Orang Tua", "-", null);
    }

    private static UserDto fromAdmin(Admin admin) {
        return new UserDto(admin.getId(), admin.getNama(), "Admin", "-", null);
    }

    private static String getIndexableClassFromClasses(Set<Kelas> classes) {
        var sb = new StringBuilder();
        for (Kelas kelas : classes) {
            sb.append(kelas.getNama());
            sb.append(" ");
        }
        return sb.toString();
    }
}
