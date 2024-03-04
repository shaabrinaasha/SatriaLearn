package propensi.proyek.properly;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import com.github.javafaker.Faker;

import propensi.proyek.properly.service.SiswaService;
import propensi.proyek.properly.service.AdminService;
import propensi.proyek.properly.service.GuruService;
import propensi.proyek.properly.service.KelasService;
import propensi.proyek.properly.service.OrangTuaService;
import propensi.proyek.properly.service.SemesterService;
import propensi.proyek.properly.service.MataPelajaranService;
import jakarta.transaction.Transactional;
import propensi.proyek.properly.model.Siswa;
import propensi.proyek.properly.model.OrangTua;
import propensi.proyek.properly.model.Admin;
import propensi.proyek.properly.model.Kelas;
import propensi.proyek.properly.model.Guru;
import propensi.proyek.properly.model.Semester;
import propensi.proyek.properly.model.MataPelajaran;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class ProperlyApplication {

	public static void main(String[] args) {
		SpringApplication.run(ProperlyApplication.class, args);
	}

	@Bean
	@Transactional
	CommandLineRunner run(
			SiswaService siswaService,
			OrangTuaService orangTuaService,
			AdminService adminService,
			GuruService guruService,
			KelasService kelasService,
			SemesterService semesterService,
			MataPelajaranService mataPelajaranService) {
		return args -> {
			var faker = new Faker();
			List<OrangTua> orangTuaList = new ArrayList<>();
			List<Guru> guruList = new ArrayList<>();
			List<Kelas> kelasList = new ArrayList<>();
			List<Siswa> siswaList = new ArrayList<>();

			// Add admin
			Admin admin = new Admin();
			admin.setNama("admin");
			admin.setUsername("admin");
			admin.setPassword("admin");
			admin.setPasswordAwal("admin");
			adminService.addAdmin(admin);

			// Add Orang Tua
			for (int i = 0; i < 10; i++) {
				OrangTua orangTua = new OrangTua();
				orangTua.setNama(faker.name().fullName());
				orangTua.setPassword(faker.internet().password());
				orangTua.setUsername(faker.name().username());
				orangTua.setPasswordAwal(faker.internet().password());
				orangTuaList.add(orangTua);
				orangTuaService.addOrangTua(orangTua);
			}

			// Add Siswa
			for (int i = 0; i < 30; i++) {

				Siswa siswa = new Siswa();
				siswa.setNama(faker.name().fullName());
				siswa.setUsername(faker.name().username());
				siswa.setPassword(faker.internet().password());
				siswa.setPasswordAwal(faker.internet().password());

				siswa.setNisn(faker.number().digits(10));
				siswa.setNipd(faker.number().digits(9));
				siswa.setOrangTua(orangTuaList.get(faker.number().numberBetween(0, 9)));
				siswaList.add(siswa);

			}

			// Add Guru
			for (int i = 0; i < 20; i++) {
				Guru guru = new Guru();
				guru.setNama(faker.name().fullName());
				guru.setUsername(faker.name().username());
				guru.setPassword(faker.internet().password());
				guru.setPasswordAwal(faker.internet().password());
				guru.setNuptk(faker.number().digits(16));
				guruList.add(guru);
				guruService.addGuru(guru);
			}

			// Add Kelas
			for (int i = 0; i < 12; i++) {
				Kelas kelas = new Kelas();
				// for i = 0 to 3, kelas 10A, 10B, 10C, 10D
				// for i = 4 to 7, kelas 11A, 11B, 11C, 11D
				// for i = 8 to 11, kelas 12A, 12B, 12C, 12D
				kelas.setNama((i / 4 + 10) + "" + (char) ('A' + i % 4));
				
				kelas.setWali(guruList.get(i));

				kelasList.add(kelas);
				kelas.setSiswas(new HashSet<>(siswaList));
			}

			// Add Siswa to Kelas
			for (int i = 0; i < 30; i++) {
				siswaList.get(i).setClasses(new HashSet<>(kelasList));
				siswaService.addSiswa(siswaList.get(i));
			}

			for (int i = 0; i < 12; i++) {
				kelasService.addKelas(kelasList.get(i));
			}

			// Add Semester
			for (int i = 0; i < 6; i++) {
				Semester semester = new Semester();
				semester.setIsGanjil(i % 2 == 0);
				semester.setTahunAjaran("202" + i + "/202" + (i + 1));
				semester.setTanggalAwal(
						faker.date().birthday().toInstant().atZone(java.time.ZoneId.systemDefault()).toLocalDate());
				semester.setTanggalAkhir(
						faker.date().birthday().toInstant().atZone(java.time.ZoneId.systemDefault()).toLocalDate());
				semester.setClasses(new HashSet<>(kelasList));
				semesterService.addSemester(semester);
			}

			// Add Mata Pelajaran
			for (int i = 0; i < 12; i++) {
				MataPelajaran mataPelajaran = new MataPelajaran();
				mataPelajaran.setNama(faker.educator().course());
				mataPelajaran.setGuru(guruList.get(i));
				mataPelajaran.setKelas(kelasList.get(i));
				mataPelajaranService.addMatpel(mataPelajaran);
			}

		};
	}
}
