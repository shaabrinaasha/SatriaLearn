package propensi.proyek.properly.controller;

import java.security.Principal;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import propensi.proyek.properly.Dto.akuns.NewUserRequestDto;
import propensi.proyek.properly.Dto.akuns.UserDto;
import propensi.proyek.properly.model.Guru;
import propensi.proyek.properly.model.Kelas;
import propensi.proyek.properly.model.MataPelajaran;
import propensi.proyek.properly.model.OrangTua;
import propensi.proyek.properly.model.Siswa;
import propensi.proyek.properly.model.User;
import propensi.proyek.properly.service.guru.GuruService;
import propensi.proyek.properly.service.orangtua.OrangTuaService;
import propensi.proyek.properly.service.siswa.SiswaService;
import propensi.proyek.properly.service.user.UserService;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class AkunController {

    @Autowired
    UserService userService;

    @Autowired
    SiswaService siswaService;

    @Autowired
    GuruService guruService;

    @Autowired
    OrangTuaService orangTuaService;

    @GetMapping("akuns")
    public ModelAndView manajemenUser(Principal principal, Authentication auth, Model model) {
        userService.addCurrentUserToModel(auth.getName(), auth.getAuthorities(), model);

        var usersInDb = userService.getAllUser();
        var users = new ArrayList<UserDto>();

        for (User user : usersInDb) {
            users.add(UserDto.fromUser(user));
        }
        model.addAttribute("users", users);
        return new ModelAndView("/akuns/index", model.asMap());
    }

    @GetMapping("akuns/create")
    public ModelAndView createNewAkunPage(Model model) {
        model.addAttribute("akun", new NewUserRequestDto());
        model.addAttribute("siswas", siswaService.getAllSiswaWithUndocumentedParent());
        model.addAttribute("selected", "siswa");
        return new ModelAndView("/akuns/create-akun", model.asMap());
    }

    @PostMapping("akuns/create")
    public ModelAndView createNewAkun(@ModelAttribute NewUserRequestDto user, Model model) {
        model.addAttribute("siswas", siswaService.getAllSiswaWithUndocumentedParent());
        model.addAttribute("akun", user);

        switch (user.getPeran()) {
            case "siswa":
                return createNewSiswa(user, model);
            case "guru":
                return createNewGuru(user, model);

            default:
                return createNewOrangTua(user, model);
        }
    }

    private ModelAndView createNewSiswa(NewUserRequestDto user, Model model) {
        model.addAttribute("selected", "siswa");
        if (inputSiswaNotCorrect(user, model))
            return new ModelAndView("/akuns/create-akun", model.asMap());

        var username = userService.generateUsername(user.getNama());
        var password = userService.generatePassword();
        var siswa = user.toSiswa();
        siswa.setUsername(username);
        siswa.setPasswordAwal(password);

        siswaService.addSiswa(siswa);

        model.addAttribute("akun", new NewUserRequestDto());
        model.addAttribute("peran", "Siswa");
        model.addAttribute("nama", siswa.getNama());
        model.addAttribute("siswas", siswaService.getAllSiswaWithUndocumentedParent());
        return new ModelAndView("/akuns/create-akun", model.asMap());
    }

    private Boolean inputSiswaNotCorrect(NewUserRequestDto user, Model model) {
        if (user.getNama().length() > 255) {
            model.addAttribute("error", "Nama terlalu panjang");
            return true;
        }
        if (user.getNipd().length() != 9) {
            model.addAttribute("error", "NIPD harus merupakan 9 digit");
            return true;
        }

        if (user.getNisn().length() != 10) {
            model.addAttribute("error", "NISN harus merupakan 10 digit");
            return true;
        }
        return false;
    }

    private ModelAndView createNewGuru(NewUserRequestDto user, Model model) {
        model.addAttribute("selected", "guru");
        if (inputGuruNotCorrect(user, model))
            return new ModelAndView("/akuns/create-akun", model.asMap());

        var username = userService.generateUsername(user.getNama());
        var password = userService.generatePassword();
        var guru = user.toGuru();
        guru.setUsername(username);
        guru.setPasswordAwal(password);

        guruService.addGuru(guru);

        model.addAttribute("akun", new NewUserRequestDto());
        model.addAttribute("peran", "Guru");
        model.addAttribute("nama", guru.getNama());
        return new ModelAndView("/akuns/create-akun", model.asMap());
    }

    private Boolean inputGuruNotCorrect(NewUserRequestDto user, Model model) {
        if (user.getNama().length() > 255) {
            model.addAttribute("error", "Nama terlalu panjang");
            return true;
        }
        if (user.getNuptk().length() != 16) {
            model.addAttribute("error", "NUPTK harus merupakan 16 digit");
            return true;
        }
        return false;
    }

    private ModelAndView createNewOrangTua(NewUserRequestDto user, Model model) {
        model.addAttribute("selected", "orang tua");
        if (inputOrangTuaNotCorrect(user, model))
            return new ModelAndView("/akuns/create-akun", model.asMap());

        var username = userService.generateUsername(user.getNama());
        var password = userService.generatePassword();

        var siswas = new ArrayList<Siswa>();
        for (UUID id : user.getOrangTuaOf()) {
            siswas.add(siswaService.getSiswaById(id));
        }

        var orangTua = user.toOrangTua(siswas);
        orangTua.setUsername(username);
        orangTua.setPasswordAwal(password);

        orangTuaService.addOrangTua(orangTua);

        model.addAttribute("akun", new NewUserRequestDto());
        model.addAttribute("peran", "Orang Tua");
        model.addAttribute("nama", orangTua.getNama());
        return new ModelAndView("/akuns/create-akun", model.asMap());
    }

    private Boolean inputOrangTuaNotCorrect(NewUserRequestDto user, Model model) {
        if (user.getNama().length() > 255) {
            model.addAttribute("error", "Nama terlalu panjang");
            return true;
        }

        for (UUID id : user.getOrangTuaOf()) {
            try {
                var siswa = siswaService.getSiswaById(id);
                if (siswa.getOrangTua() != null) {
                    model.addAttribute("error",
                            String.format("Orang Tua %s sudah terdaftar di sistem", siswa.getNama()));
                    return true;
                }
            } catch (IllegalArgumentException e) {
                model.addAttribute("error", "Mohon isi setiap field setiap anak atau hapus");
                return true;
            }
        }
        return false;
    }

    @GetMapping("/akuns/update/{id}")
    public String updateAkunFormPage(@PathVariable UUID id, Principal principal, Model model, RedirectAttributes redirectAttrs) {
        User oldUser = userService.getUserById(id);
        // var oldUserDto = UserDto.fromUser(oldUser);
        String userRole = oldUser.getDecriminatorValue();
        var akun = new NewUserRequestDto();
        akun.setNama(oldUser.getNama());
        akun.setPeran(userRole);
        akun.setId(id);
        System.out.println("FORM UPDATE");
        System.out.println(oldUser.getNama());
        System.out.println(userRole);

        if (userRole.contains("siswa")) {
            Siswa oldSiswa = (Siswa) oldUser;
            akun.setNipd(oldSiswa.getNipd());
            akun.setNisn(oldSiswa.getNisn());
            // model.addAttribute("akunParent", newSiswa);
            model.addAttribute("akun", akun);
            model.addAttribute("peran", "Siswa");
            return "akuns/update-akun-siswa";

        } else if (userRole.contains("guru")) {
            Guru oldGuru = (Guru) oldUser;
            akun.setNama(oldGuru.getNama());
            akun.setNuptk(oldGuru.getNuptk());
            model.addAttribute("akun", akun);
            model.addAttribute("peran", "Guru");
            System.out.println(akun.getNama());
            System.out.println(akun.getPeran());
            return "akuns/update-akun-guru";

        } else if (userRole.contains("orang tua") || userRole.contains("Orang Tua")) {
            OrangTua oldOrtu = (OrangTua) oldUser;
            akun.setNama(oldOrtu.getNama());
            // List<UUID> anak = new ArrayList<>();
            // for (Siswa siswa : oldOrtu.getSiswas()) {
            //     anak.add(siswa.getId());
            // }

            akun.setOrangTuaSiswaOf(oldOrtu.getSiswas());
            System.out.println("JUMLAH ANAK");
            System.out.println(oldOrtu.getSiswas().size());
            model.addAttribute("akun", akun);
            model.addAttribute("peran", "Orang Tua");
            model.addAttribute("siswas", siswaService.getAllSiswaWithUndocumentedParent());
            return "akuns/update-akun-ortu";
        }

        redirectAttrs.addFlashAttribute("error", "Akun tidak ditemukan");
        return "redirect:/akuns";
    }

    @PostMapping(value = "/akuns/update", params = { "save" })
    public String updateAkunSubmitPage(@ModelAttribute NewUserRequestDto akun, Model model, RedirectAttributes redirectAttrs) {
        User user = userService.getUserById(akun.getId());
        String userRole = user.getDecriminatorValue();
        System.out.println("SUBMIT UPDATE");
        System.out.println(akun.getNama());
        System.out.println(user.getDecriminatorValue());
        System.out.println(akun.getId());
        if (userRole.contains("siswa")) {
            Siswa oldSiswa = siswaService.getSiswaById(akun.getId());
            String oldNama = oldSiswa.getNama();
            String oldNISN = oldSiswa.getNisn();
            String oldNIPD = oldSiswa.getNipd();

            String newNama = akun.getNama();
            String newNISN = akun.getNisn();
            String newNIPD = akun.getNipd();

            if (!oldNama.equals(newNama)) {
                if (newNama.isEmpty()) {
                    redirectAttrs.addFlashAttribute("error", "Akun Siswa tidak dapat diubah karena field nama kosong");
                    return "redirect:/akuns/update/" + oldSiswa.getId();
                }

                oldSiswa.setNama(newNama);

            } if (!oldNISN.equals(newNISN)) {
                if (newNISN.isEmpty()) {
                    redirectAttrs.addFlashAttribute("error", "Akun Siswa tidak dapat diubah karena field NISN kosong");
                    return "redirect:/akuns/update/" + oldSiswa.getId();
                }

                oldSiswa.setNisn(newNISN);

            } if (!oldNIPD.equals(newNIPD)) {
                if (newNIPD.isEmpty()) {
                    redirectAttrs.addFlashAttribute("error", "Akun Siswa tidak dapat diubah karena field NIPD kosong");
                    return "redirect:/akuns/update/" + oldSiswa.getId();
                }

                oldSiswa.setNipd(newNIPD);
            }

            siswaService.addSiswa(oldSiswa);
            redirectAttrs.addFlashAttribute("success", "Akun Siswa berhasil diubah");
            return "redirect:/akuns";

        } else if (userRole.contains("guru")) {
            Guru oldGuru = guruService.getGuruById(akun.getId());
            String oldNama = oldGuru.getNama();
            String oldNUPTK = oldGuru.getNuptk();

            String newNama = akun.getNama();
            String newNUPTK = akun.getNuptk();

            if (!oldNama.equals(newNama)) {
                if (newNama.isEmpty()) {
                    redirectAttrs.addFlashAttribute("error", "Akun Guru tidak dapat diubah karena field nama kosong");
                    return "redirect:/akuns/update/" + oldGuru.getId();
                }
    
                oldGuru.setNama(newNama);

            } if (!oldNUPTK.equals(newNUPTK)) {
                if (newNUPTK.isEmpty()) {
                    redirectAttrs.addFlashAttribute("error", "Akun Guru tidak dapat diubah karena field NUPTK kosong");
                    return "redirect:/akuns/update/" + oldGuru.getId();
                }
    
                oldGuru.setNuptk(newNUPTK);
            }

            guruService.addGuru(oldGuru);
            redirectAttrs.addFlashAttribute("success", "Akun Guru berhasil diubah");
            return "redirect:/akuns";
    
        } else if (userRole.contains("orang tua")) {
            OrangTua oldOrtu = orangTuaService.getOrangTuaById(akun.getId());
            String oldNama = oldOrtu.getNama();
            List<Siswa> oldAnak = oldOrtu.getSiswas();

            String newNama = akun.getNama();
            // List<Siswa> newAnak = akun.getOrangTuaSiswaOf();
            // System.out.println(newAnak);

            List<UUID> newAnak = akun.getOrangTuaOf(); 
            List<Siswa> newSiswa = new ArrayList<>();
        

            if (!oldNama.equals(newNama)) {
                if (newNama.isEmpty()) {
                    redirectAttrs.addFlashAttribute("error", "Akun Orang Tua tidak dapat diubah karena field nama kosong");
                    return "redirect:/akuns/update/" + oldOrtu.getId();
                }
    
                oldOrtu.setNama(newNama);
            } 

            if (newAnak != null) {
                for (UUID id : newAnak) {
                    newSiswa.add(siswaService.getSiswaById(id));
                }
                System.out.println("Jumlah anak baru");
                System.out.println(newAnak.size());
            }
            
            if (oldAnak.isEmpty() && !newSiswa.isEmpty()){
                oldOrtu.setSiswas(newSiswa);
            } else if (!oldAnak.isEmpty() && newSiswa.isEmpty()){
                // kemungkinan tidak diperlukan
                oldOrtu.setSiswas(null);
            } else if (!oldAnak.isEmpty() && !newSiswa.isEmpty()){
                if (!(oldAnak.containsAll(newSiswa) && newSiswa.containsAll(oldAnak))) {
                    oldOrtu.setSiswas(newSiswa);
                }
            }
            
            orangTuaService.addOrangTua(oldOrtu);
            redirectAttrs.addFlashAttribute("success", "Akun Orang Tua berhasil diubah");
            return "redirect:/akuns";
        }

        redirectAttrs.addFlashAttribute("error", "Akun tidak ditemukan");
        return "redirect:/akuns";
    }

    @GetMapping("detail-akun")
    public String readAkunSaya(Authentication auth, Principal principal, Model model) {
        var username = principal.getName();
        var authorities = auth.getAuthorities();
        userService.addCurrentUserToModel(username, authorities, model);

        return "akuns/detail-akun";
    }

}