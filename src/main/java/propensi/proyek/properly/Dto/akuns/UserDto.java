package propensi.proyek.properly.Dto.akuns;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import propensi.proyek.properly.model.Admin;
import propensi.proyek.properly.model.Guru;
import propensi.proyek.properly.model.OrangTua;
import propensi.proyek.properly.model.Siswa;
import propensi.proyek.properly.model.User;

@Getter
@Setter
@AllArgsConstructor
public class UserDto {
    private String nama;
    private String peran;
    private String nomorIdentifikasi;

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
        return new UserDto(siswa.getNama(), "Siswa", siswa.getNisn());
    }

    private static UserDto fromGuru(Guru guru) {
        return new UserDto(guru.getNama(), "Guru", guru.getNuptk());
    }

    private static UserDto fromOrangTua(OrangTua orangTua) {
        return new UserDto(orangTua.getNama(), "Orang Tua", "-");
    }

    private static UserDto fromAdmin(Admin admin) {
        return new UserDto(admin.getNama(), "Admin", "-");
    }

}
