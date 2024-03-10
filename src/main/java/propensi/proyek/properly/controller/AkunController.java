package propensi.proyek.properly.controller;

import java.security.Principal;
import java.util.*;
import java.util.ArrayList;
import java.util.UUID;
import java.util.Collection;
import java.util.List;
import java.util.*;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.InvalidDataAccessApiUsageException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.parameters.P;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import propensi.proyek.properly.Dto.akuns.NewUserRequestDto;
import propensi.proyek.properly.Dto.akuns.UserDto;
import propensi.proyek.properly.model.*;
import propensi.proyek.properly.model.Siswa;
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
            if (user.getIsActive() && !Objects.equals(user.getDecriminatorValue(), "admin")){
                users.add(UserDto.fromUser(user));
            }
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

    @PostMapping(value = "/akuns/create", params = { "add=add" })
    public ModelAndView addAnak(@ModelAttribute NewUserRequestDto user, Model model) {
        model.addAttribute("siswas", siswaService.getAllSiswaWithUndocumentedParent());
        model.addAttribute("selected", "orang tua");

        var orangTuaOfs = user.getOrangTuaOf();
        orangTuaOfs.add(null);
        user.setOrangTuaOf(orangTuaOfs);
        model.addAttribute("akun", user);

        return new ModelAndView("/akuns/create-akun", model.asMap());
    }

    @PostMapping(value = "/akuns/create", params = { "remove" })
    public ModelAndView removeAnak(@RequestParam(value = "remove", defaultValue = "-1") Integer remove,
            @ModelAttribute NewUserRequestDto user, Model model) {
        model.addAttribute("siswas", siswaService.getAllSiswaWithUndocumentedParent());
        model.addAttribute("selected", "orang tua");

        var orangTuaOfs = user.getOrangTuaOf();
        UUID removed = orangTuaOfs.remove(remove.intValue());
        user.setOrangTuaOf(orangTuaOfs);
        model.addAttribute("akun", user);

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

        if (user.getNama().isEmpty()) {
            model.addAttribute("error", "Mohon berikan nama");
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
            } catch (InvalidDataAccessApiUsageException e) {
                if (user.getOrangTuaOf().size() == 1) {
                    model.addAttribute("error", "Setiap Orang Tua harus memiliki 1 atau lebih anak");
                }
                model.addAttribute("error", "ID yang diberikan tidak valid");
                return true;
            }
        }
        return false;
    }

    @GetMapping("/akuns/update/{id}")
    public String updateAkunFormPage(@PathVariable UUID id, Principal principal, Model model,
            RedirectAttributes redirectAttrs) {
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
    
            List<UUID> anak = new ArrayList<>();
            for (Siswa siswa : oldOrtu.getSiswas()) {
                anak.add(siswa.getId());
            }

            var siswa = siswaService.getAllSiswaWithUndocumentedParent();
            for (Siswa s : oldOrtu.getSiswas()) {
                siswa.add(s);
            }

            akun.setOrangTuaOf(anak);
            System.out.println("JUMLAH ANAK");
            System.out.println(oldOrtu.getSiswas().size());
            model.addAttribute("akun", akun);
            model.addAttribute("peran", "Orang Tua");
            model.addAttribute("siswas", siswa);
            return "akuns/update-akun-ortu";
        }

        redirectAttrs.addFlashAttribute("error", "Akun tidak ditemukan");
        return "redirect:/akuns";
    }

    @PostMapping(value = "/akuns/update", params = { "save" })
    public String updateAkunSubmitPage(@ModelAttribute NewUserRequestDto akun, Model model,
            RedirectAttributes redirectAttrs) {
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

            }
            if (!oldNISN.equals(newNISN)) {
                if (newNISN.isEmpty()) {
                    redirectAttrs.addFlashAttribute("error", "Akun Siswa tidak dapat diubah karena field NISN kosong");
                    return "redirect:/akuns/update/" + oldSiswa.getId();
                }

                oldSiswa.setNisn(newNISN);

            }
            if (!oldNIPD.equals(newNIPD)) {
                if (newNIPD.isEmpty()) {
                    redirectAttrs.addFlashAttribute("error", "Akun Siswa tidak dapat diubah karena field NIPD kosong");
                    return "redirect:/akuns/update/" + oldSiswa.getId();
                }

                oldSiswa.setNipd(newNIPD);
            }

            siswaService.updateSiswa(oldSiswa);
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

            }
            if (!oldNUPTK.equals(newNUPTK)) {
                if (newNUPTK.isEmpty()) {
                    redirectAttrs.addFlashAttribute("error", "Akun Guru tidak dapat diubah karena field NUPTK kosong");
                    return "redirect:/akuns/update/" + oldGuru.getId();
                }

                oldGuru.setNuptk(newNUPTK);
            }

            guruService.updateGuru(oldGuru);
            redirectAttrs.addFlashAttribute("success", "Akun Guru berhasil diubah");
            return "redirect:/akuns";

        } else if (userRole.contains("orang tua")) {
            OrangTua oldOrtu = orangTuaService.getOrangTuaById(akun.getId());
            String oldNama = oldOrtu.getNama();
            List<Siswa> oldAnak = oldOrtu.getSiswas();

            String newNama = akun.getNama();        

            if (!oldNama.equals(newNama)) {
                if (newNama.isEmpty()) {
                    redirectAttrs.addFlashAttribute("error",
                            "Akun Orang Tua tidak dapat diubah karena field nama kosong");
                    return "redirect:/akuns/update/" + oldOrtu.getId();
                }

                oldOrtu.setNama(newNama);
            } 
            
            List<UUID> newAnak = akun.getOrangTuaOf(); 

            for (UUID id : newAnak) {
                try {
                    var siswa = siswaService.getSiswaById(id);
                    if (siswa.getOrangTua() != null && !siswa.getOrangTua().getId().equals(oldOrtu.getId())) {
                        redirectAttrs.addFlashAttribute("error", "Orang Tua sudah terdaftar di sistem");
                        return "redirect:/akuns/update/" + oldOrtu.getId();

                    }
                } catch (IllegalArgumentException e) {
                    redirectAttrs.addFlashAttribute("error", "Mohon isi setiap field setiap anak");
                    return "redirect:/akuns/update/" + oldOrtu.getId();
                } catch (InvalidDataAccessApiUsageException e) {
                    redirectAttrs.addFlashAttribute("error", "Id anak tidak ditemukan");
                    return "redirect:/akuns/update/" + oldOrtu.getId();
                }
            }
            
            for (Siswa siswa: oldOrtu.getSiswas()) {
                siswa.setOrangTua(null);
                siswaService.updateSiswa(siswa);
            }

            if (oldAnak.size() == 0 && newAnak.size() > 0){
                var newSiswa = new ArrayList<Siswa>();
                for (UUID id : newAnak) {
                    newSiswa.add(siswaService.getSiswaById(id));
                }
        
                oldOrtu.setSiswas(newSiswa);
                System.out.println(oldOrtu.getSiswas().size());
                System.out.println("dari tidak ada menjadi ada anak");

            } else if (oldAnak.size() > 0 && newAnak.size() == 0 ){
                redirectAttrs.addFlashAttribute("error", "Akun Orang Tua tidak dapat diubah karena field anak harus diisi");
                return "redirect:/akuns/update/" + oldOrtu.getId();

            } else if (oldAnak.size() > 0 && newAnak.size() > 0){

                if (!(oldAnak.containsAll(newAnak) && newAnak.containsAll(oldAnak))) {
                    var newSiswa = new ArrayList<Siswa>();
                    for (UUID id : newAnak) {
                        newSiswa.add(siswaService.getSiswaById(id));
                    }
                    oldOrtu.setSiswas(newSiswa);
                    System.out.println(oldOrtu.getSiswas().size());
                    System.out.println("dari ada menjadi ada anak");
                }
            }
            
            System.out.println("FINAL ANAK");

            orangTuaService.updateOrangTua(oldOrtu);
            System.out.println(oldOrtu.getSiswas().size());
            System.out.println(oldOrtu);
            redirectAttrs.addFlashAttribute("success", "Akun Orang Tua berhasil diubah");
            return "redirect:/akuns";
        }

        redirectAttrs.addFlashAttribute("error", "Akun tidak ditemukan");
        return "redirect:/akuns";
    }

    @GetMapping("akun-saya")
    public String readAkunSaya(Authentication auth, Principal principal, Model model) {
        var username = principal.getName();
        var authorities = auth.getAuthorities();
        userService.addCurrentUserToModel(username, authorities, model);

        Collection<? extends GrantedAuthority> authorities2 = auth.getAuthorities();

        List<String> roles = authorities2.stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.toList());

        String userRole = roles.get(0);

        if (userRole.contains("Guru") || userRole.contains("guru")) {
            User user = userService.getUserByUsername(username);
            Guru guru = guruService.getGuruById(user.getId());

            List<MataPelajaran> listMatpel = new ArrayList<>(guru.getMataPelajarans());

            model.addAttribute("listMatpel", listMatpel);
        }

        if (userRole.contains("Siswa") || userRole.contains("siswa")) {
            User user = userService.getUserByUsername(username);
            Siswa siswa = siswaService.getSiswaById(user.getId());

            Set<Kelas> kelasSiswa = siswa.getClasses();
            List<Kelas> listKelas = new ArrayList<>(kelasSiswa);

            List<MataPelajaran> listMatpel = new ArrayList<>();

            for (Kelas kelas : listKelas) {
                listMatpel.addAll(kelas.getMataPelajarans());
            }

            model.addAttribute("listKelas", listKelas);
            model.addAttribute("listMatpel", listMatpel);
        }

        return "akuns/akun-saya";
    }

    @GetMapping("/akuns/{id}/details")
    public String detailAkun(@PathVariable UUID id, Authentication auth, Principal principal, Model model, RedirectAttributes redirectAttrs) {
        var username = principal.getName();
        var authorities = auth.getAuthorities();
        userService.addCurrentUserToModel(username, authorities, model);

        User user = userService.getUserById(id);
        String userRole = user.getDecriminatorValue();

        if (userRole.contains("siswa") || userRole.contains("Siswa")) {
            Siswa siswa = siswaService.getSiswaById(user.getId());

            Set<Kelas> kelasSiswa = siswa.getClasses();
            List<Kelas> listKelas = new ArrayList<>(kelasSiswa);

            List<MataPelajaran> listMatpel = new ArrayList<>();

            for (Kelas kelas : listKelas) {
                listMatpel.addAll(kelas.getMataPelajarans());
            }

            model.addAttribute("user", user);
            model.addAttribute("peran", "Siswa");
            model.addAttribute("listKelas", listKelas);
            model.addAttribute("listMatpel", listMatpel);

            return "akuns/detail-akun-siswa";

        } else if (userRole.contains("guru") || userRole.contains("Guru")) {
            Guru guru = guruService.getGuruById(user.getId());

            List<MataPelajaran> listMatpel = new ArrayList<>(guru.getMataPelajarans());

            model.addAttribute("user", user);
            model.addAttribute("peran", "Guru");
            model.addAttribute("listMatpel", listMatpel);

            return "akuns/detail-akun-guru";

        } else if (userRole.contains("orang tua") || userRole.contains("Orang Tua")) {
            model.addAttribute("user", user);
            model.addAttribute("peran", "Orang Tua");

            return "akuns/detail-akun-ortu";
        } else if (userRole.contains("admin") || userRole.contains("Admin")) {
            model.addAttribute("user", user);
            model.addAttribute("peran", "Admin");

            return "akuns/detail-akun-admin";
        }

        redirectAttrs.addFlashAttribute("error", "Akun tidak ditemukan");
        return "redirect:/akuns";
    }

    @GetMapping("/akun-saya/update-password")
    public String updatePasswordFormPage(Model model, Principal principal) {
        User user = userService.getUserByUsername(principal.getName());
        UpdatePassword updatePassword = new UpdatePassword();

        model.addAttribute("updatePassword", updatePassword);

        return "akuns/update-password";
    }

    @PostMapping("/akun-saya/update-password")
    public String updatePasswordSubmitPage(@Validated @ModelAttribute UpdatePassword updatePassword, BindingResult bindingResult, Principal principal, Model model, RedirectAttributes redirectAttrs) {
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        User user = userService.getUserByUsername(principal.getName());

        String currentPassword = user.getPassword();
        String oldPassword = updatePassword.getOldPassword(); // user input
        String newPassword = updatePassword.getNewPassword(); // user input
        String confirmationPass = updatePassword.getConfirmNewPassword(); // user input

        // Checking if old password and current password match
        boolean checkMatch = passwordEncoder.matches(oldPassword, currentPassword);

        // Checking if new password and confirmation new password match
        boolean checkConfirmation = newPassword.equals(confirmationPass);

        // Checking if password is valid (min. 8 chars with upper case, lower case, and digit)
        boolean checkValidation = false;

        int passwordLength = newPassword.length();
        int upChars = 0, lowChars = 0, digits = 0;

        if (passwordLength < 8) {
            checkValidation = false;
        } else {
            for (int i = 0; i < passwordLength; i++) {
                char ch = newPassword.charAt(i);
                if (Character.isUpperCase(ch))
                    upChars = 1;
                else if (Character.isLowerCase(ch))
                    lowChars = 1;
                else if (Character.isDigit(ch))
                    digits = 1;
            }

            if (upChars == 1 && lowChars == 1 && digits == 1) {
                checkValidation = true;
            }
        }

        if (checkMatch && checkConfirmation && checkValidation) {
            user.setPassword(passwordEncoder.encode(updatePassword.getNewPassword()));
            userService.updateUser(user);
            redirectAttrs.addFlashAttribute("success", "Password akun berhasil diubah.");
            return "redirect:/akun-saya/update-password";
        } else if (!checkMatch) {
            redirectAttrs.addFlashAttribute("error", "Password lama tidak sesuai.");
            return "redirect:/akun-saya/update-password";
        } else if (!checkConfirmation) {
            redirectAttrs.addFlashAttribute("error", "Password baru dan konfirmasi password baru tidak sesuai.");
            return "redirect:/akun-saya/update-password";
        } else if (!checkValidation) {
            redirectAttrs.addFlashAttribute("error", "Password tidak valid. Password minimal 8 karakter yang terdiri dari karakter kecil, besar, dan numerik.");
            return "redirect:/akun-saya/update-password";
        } else {
            return "redirect:/akun-saya/update-password";
        }
    }

    @GetMapping("/akuns/{id}/delete")
    public String deleteAkun(@PathVariable UUID id, RedirectAttributes redirectAttrs) {
        User user = userService.getUserById(id);
        String userRole = user.getDecriminatorValue();

        if (userRole.contains("admin") || userRole.contains("Admin")) {
            redirectAttrs.addFlashAttribute("error", "Akun admin tidak dapat dihapus.");
            return "redirect:/akuns";
        } else {
            user.setIsActive(false);
            userService.updateUser(user);

            redirectAttrs.addFlashAttribute("success", "Akun berhasil dihapus.");
            return "redirect:/akuns";
        }
    }

    @PostMapping(value = "/akuns/update", params = {"add=add"})
    public ModelAndView addAnakUpdate(@ModelAttribute NewUserRequestDto user, Model model) {
        // model.addAttribute("siswas", siswaService.getAllSiswaWithUndocumentedParent());
        // model.addAttribute("selected", "orang tua");

        // List<Siswa> orangTuaOfs = akun.getOrangTuaSiswaOf();
        // orangTuaOfs.add(null);
        // akun.setOrangTuaSiswaOf(orangTuaOfs);
        // System.out.println("JUMLAH ANAK AFTER ADD");
        // System.out.println(akun.getOrangTuaSiswaOf().size());
        // model.addAttribute("akun", akun);

        var orangTuaOfs = user.getOrangTuaOf();
        orangTuaOfs.add(null);

        var siswa = siswaService.getAllSiswaWithUndocumentedParent();
        var oldOrtu = orangTuaService.getOrangTuaById(user.getId());
        for (Siswa s : oldOrtu.getSiswas()) {
            siswa.add(s);
        }
        user.setOrangTuaOf(orangTuaOfs);

        model.addAttribute("akun", user);
        model.addAttribute("siswas", siswa);
        model.addAttribute("selected", "orang tua");

        return new ModelAndView("akuns/update-akun-ortu", model.asMap());
    }

    @PostMapping(value = "/akuns/update", params = {"remove"})
    public ModelAndView removeAnakUpdate(@RequestParam(value = "remove", defaultValue = "-1") Integer remove, @ModelAttribute NewUserRequestDto user, Model model) {
        // model.addAttribute("siswas", siswaService.getAllSiswaWithUndocumentedParent());
        // model.addAttribute("selected", "orang tua");

        // var orangTuaOfs = akun.getOrangTuaSiswaOf();
        // Siswa removed = orangTuaOfs.remove(remove.intValue());
        // akun.setOrangTuaSiswaOf(orangTuaOfs);
        // model.addAttribute("akun", akun);

        var orangTuaOfs = user.getOrangTuaOf();
        UUID removed = orangTuaOfs.remove(remove.intValue());

        var siswa = siswaService.getAllSiswaWithUndocumentedParent();
        var oldOrtu = orangTuaService.getOrangTuaById(user.getId());
        for (Siswa s : oldOrtu.getSiswas()) {
            siswa.add(s);
        }

        user.setOrangTuaOf(orangTuaOfs);
        model.addAttribute("akun", user);
        model.addAttribute("siswas", siswa);
        model.addAttribute("selected", "orang tua");


        return new ModelAndView("akuns/update-akun-ortu", model.asMap());
    }

}