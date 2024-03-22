package propensi.proyek.properly;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.core.env.Environment;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import propensi.proyek.properly.model.Admin;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import com.github.javafaker.Faker;

import propensi.proyek.properly.service.admin.AdminService;
import propensi.proyek.properly.service.guru.GuruService;
import propensi.proyek.properly.service.kelas.KelasService;
import propensi.proyek.properly.service.matapelajaran.MataPelajaranService;
import propensi.proyek.properly.service.materi.MateriService;
import propensi.proyek.properly.service.orangtua.OrangTuaService;
import propensi.proyek.properly.service.semester.SemesterService;
import propensi.proyek.properly.service.siswa.SiswaService;
import propensi.proyek.properly.service.tugas.PengumumanTugasService;
import propensi.proyek.properly.service.presensi.PresensiService;
import propensi.proyek.properly.service.siswaPresensi.SiswaPresensiService;
import propensi.proyek.properly.service.komponenPenilaian.KomponenPenilaianService;
import propensi.proyek.properly.service.nilai.NilaiService;
import jakarta.transaction.Transactional;
import propensi.proyek.properly.model.Siswa;
import propensi.proyek.properly.model.OrangTua;
import propensi.proyek.properly.model.Kelas;
import propensi.proyek.properly.model.Guru;
import propensi.proyek.properly.model.Semester;
import propensi.proyek.properly.model.MataPelajaran;
import propensi.proyek.properly.model.Materi;
import propensi.proyek.properly.model.PengumumanTugas;
import propensi.proyek.properly.model.Presensi;
import propensi.proyek.properly.model.SiswaPresensi;
import propensi.proyek.properly.model.KomponenPenilaian;
import propensi.proyek.properly.model.Nilai;
import java.time.LocalDate;

@SpringBootApplication
public class ProperlyApplication {

	@Autowired
	private Environment env;

	public static void main(String[] args) {
		SpringApplication.run(ProperlyApplication.class, args);
	}

	// @SuppressWarnings("deprecation")
	// @Bean
	// @Transactional
	// CommandLineRunner run(
	// 		SiswaService siswaService,
	// 		OrangTuaService orangTuaService,
	// 		AdminService adminService,
	// 		GuruService guruService,
	// 		KelasService kelasService,
	// 		SemesterService semesterService,
	// 		MataPelajaranService mataPelajaranService,
	// 		MateriService materiService,
	// 		PengumumanTugasService pengumumanTugasService,
	// 		PresensiService presensiService,
	// 		SiswaPresensiService siswaPresensiService,
	// 		KomponenPenilaianService komponenPenilaianService,
	// 		NilaiService nilaiService,
	// 		BCryptPasswordEncoder encoder) {
	// 	return args -> {
	// 		var faker = new Faker();
	// 		List<OrangTua> orangTuaList = new ArrayList<>();
	// 		List<Guru> guruList = new ArrayList<>();
	// 		List<Kelas> kelasList = new ArrayList<>();
	// 		List<Siswa> siswaList = new ArrayList<>();
	// 		List<MataPelajaran> mataPelajaranList = new ArrayList<>();
	// 		List<Presensi> presensiList = new ArrayList<>();
	// 		List<Semester> semesterList = new ArrayList<>();
	// 		List<KomponenPenilaian> komponenPenilaianList = new ArrayList<>();

	// 		// Add admin
	// 		addAdminIfEmpty(adminService, encoder);

	// 		if (env.acceptsProfiles("dev") || env.acceptsProfiles("test")) {
	// 			// Add Orang Tua
	// 			for (int i = 0; i < 10; i++) {
	// 				var password = faker.internet().password();
	// 				OrangTua orangTua = new OrangTua();
	// 				orangTua.setNama(faker.name().fullName());
	// 				orangTua.setPassword(encoder.encode(password));
	// 				orangTua.setUsername(faker.name().username());
	// 				orangTua.setPasswordAwal(password);
	// 				orangTuaList.add(orangTua);
	// 				orangTuaService.addOrangTua(orangTua);
	// 			}

	// 			// Add Siswa
	// 			for (int i = 0; i < 10; i++) {
	// 				var password = faker.internet().password();
	// 				Siswa siswa = new Siswa();
	// 				siswa.setNama(faker.name().fullName());
	// 				siswa.setUsername(faker.name().username());
	// 				siswa.setPassword(encoder.encode(password));
	// 				siswa.setPasswordAwal(password);

	// 				siswa.setNisn(faker.number().digits(10));
	// 				siswa.setNipd(faker.number().digits(9));
	// 				siswa.setOrangTua(orangTuaList.get(faker.number().numberBetween(0, 9)));
	// 				siswaList.add(siswa);

	// 			}

	// 			// Add Guru
	// 			for (int i = 0; i < 20; i++) {
	// 				var password = faker.internet().password();

	// 				Guru guru = new Guru();
	// 				guru.setNama(faker.name().fullName());
	// 				guru.setUsername(faker.name().username());
	// 				guru.setPassword(encoder.encode(password));
	// 				guru.setPasswordAwal(password);
	// 				guru.setNuptk(faker.number().digits(16));
	// 				guruList.add(guru);
	// 				guruService.addGuru(guru);
	// 			}

	// 			// Add Kelas
	// 			for (int i = 0; i < 12; i++) {
	// 				Kelas kelas = new Kelas();
	// 				// for i = 0 to 3, kelas 10A, 10B, 10C, 10D
	// 				// for i = 4 to 7, kelas 11A, 11B, 11C, 11D
	// 				// for i = 8 to 11, kelas 12A, 12B, 12C, 12D
	// 				kelas.setNama((i / 4 + 10) + "" + (char) ('A' + i % 4));

	// 				kelas.setWali(guruList.get(i));

	// 				kelasList.add(kelas);
	// 				kelas.setSiswas(new HashSet<>(siswaList));
	// 			}

	// 			// Add Siswa to Kelas
	// 			for (int i = 0; i < 10; i++) {
	// 				siswaList.get(i).setClasses(new HashSet<>(kelasList));
	// 				siswaService.addSiswa(siswaList.get(i));
	// 			}

	// 			for (int i = 0; i < 12; i++) {
	// 				kelasService.addKelas(kelasList.get(i));
	// 			}

	// 			// Add Semester
	// 			for (int i = 0; i < 6; i++) {
	// 				Semester semester = new Semester();
	// 				semester.setIsGanjil(i % 2 == 0);
	// 				semester.setTahunAjaran(
	// 						"202" + (int) (Math.floor(i / 2)) + "/202" + ((int) (Math.floor(i / 2)) + 1));
	// 				// if i is ganjil then set tanggal awal to 1st of July, else set to 1st of
	// 				// January. the year is 2020 + i/2
	// 				semester.setTanggalAwal(
	// 						(i % 2 == 0) ? LocalDate.of(2020 + (int) (Math.floor(i / 2)), 1, 1)
	// 								: LocalDate.of(2020 + (int) (Math.floor(i / 2)), 7, 1));
	// 				semester.setClasses(new HashSet<>(kelasList));
	// 				// set tanggal akhir to 6 months after tanggal awal
	// 				semester.setTanggalAkhir(semester.getTanggalAwal().plusMonths(6));
	// 				semesterService.save(semester);
	// 				semesterList.add(semester);
	// 			}

	// 			// Add Mata Pelajaran
	// 			for (int i = 0; i < 6; i++) {
	// 				MataPelajaran mataPelajaran = new MataPelajaran();
	// 				mataPelajaran.setNama(faker.educator().course());
	// 				mataPelajaran.setGuru(guruList.get(i));
	// 				mataPelajaran.setKelas(kelasList.get(i));
	// 				mataPelajaranService.addMataPelajaran(mataPelajaran);
	// 				mataPelajaranList.add(mataPelajaran);
	// 			}

	// 			// Add Materi
	// 			for (int i = 0; i < 6; i++) {
	// 				for (int j = 0; j < 5; j++) {
	// 					var materi = new Materi();
	// 					materi.setJudul(faker.educator().course());
	// 					materi.setDeskripsi(faker.lorem().sentence());
	// 					materi.setFile(faker.internet().url());
	// 					materi.setTanggalUpload(LocalDate.now());
	// 					materi.setMataPelajaran(mataPelajaranList.get(i));
	// 					materiService.addMateri(materi);
	// 				}
	// 			}

	// 			// Add Pengumuman Tugas
	// 			for (int i = 0; i < 6; i++) {
	// 				for (int j = 0; j < 5; j++) {
	// 					var pengumumanTugas = new PengumumanTugas();
	// 					pengumumanTugas.setJudul(faker.educator().course());
	// 					pengumumanTugas.setDeskripsi(faker.lorem().paragraph());
	// 					pengumumanTugas.setFile(faker.internet().url());
	// 					pengumumanTugas.setTanggalUpload(LocalDate.now());
	// 					pengumumanTugas.setDeadline(LocalDate.now().plusDays(7));
	// 					pengumumanTugas.setMataPelajaran(mataPelajaranList.get(i));
	// 					pengumumanTugasService.addPengumumanTugas(pengumumanTugas);
	// 				}
	// 			}

	// 			// Add Presensi
	// 			for (int i = 0; i < 6; i++) {
	// 				for (int j = 0; j < 10; j++) {
	// 					var presensi = new Presensi();
	// 					presensi.setTanggal(LocalDate.now());
	// 					presensi.setMataPelajaran(mataPelajaranList.get(i));
	// 					presensiService.addPresensi(presensi);
	// 					presensiList.add(presensi);
	// 				}
	// 			}

	// 			// Add SiswaPresensi
	// 			for (int i = 0; i < 6; i++) {
	// 				for (int j = 0; j < 10; j++) {
	// 					for (int k = 0; k < 10; k++) {
	// 						var siswaPresensi = new SiswaPresensi();
	// 						siswaPresensi.setPresensi(presensiList.get(i * 10 + j));
	// 						siswaPresensi.setSiswa(siswaList.get(k));
	// 						// 25% to be Alpa, 25% to be Sakit, 25% to be Izin, 25% to be Hadir
	// 						siswaPresensi.setStatus(faker.number().numberBetween(0, 4) == 0 ? "Alpa"
	// 								: faker.number().numberBetween(0, 4) == 1 ? "Sakit"
	// 										: faker.number().numberBetween(0, 4) == 2 ? "Izin" : "Hadir");
	// 						siswaPresensiService.addSiswaPresensi(siswaPresensi);
	// 					}
	// 				}
	// 			}

	// 			// Add Komponen Penilaian
	// 			for (int i = 0; i < 6; i++) {
	// 				for (int j = 0; j < 6; j++) {
	// 					var komponenPenilaian = new KomponenPenilaian();
	// 					komponenPenilaian.setNama(faker.educator().course());
	// 					// 50% to be Pengetahuan, 50% to be Keterampilan
	// 					komponenPenilaian.setKlasifikasi(faker.number().numberBetween(0, 2) == 0 ? "Pengetahuan" : "Keterampilan");
	// 					// 33% to be Ulangan Harian, 33% to be Tugas, 33% to be Ujian
	// 					komponenPenilaian.setTipe(faker.number().numberBetween(0, 3) == 0 ? "Ulangan Harian"
	// 							: faker.number().numberBetween(0, 3) == 1 ? "Tugas" : "Ujian");
	// 					komponenPenilaian.setBobot(faker.number().numberBetween(1, 50));
	// 					komponenPenilaian.setMataPelajaran(mataPelajaranList.get(i));
	// 					komponenPenilaian.setSemester(semesterList.get(j));
	// 					komponenPenilaianService.addKomponenPenilaian(komponenPenilaian);
	// 					komponenPenilaianList.add(komponenPenilaian);
	// 				}
	// 			}

	// 			// Add Nilai
	// 			for (int i = 0; i < 6; i++) {
	// 				for (int j = 0; j < 6; j++) {
	// 					for (int k = 0; k < 10; k++) {
	// 						var nilai = new Nilai();
	// 						nilai.setKomponenPenilaian(komponenPenilaianList.get(i * 6 + j));
	// 						nilai.setSiswa(siswaList.get(k));
	// 						nilai.setNilai((long) faker.number().numberBetween(60, 101));
	// 						nilaiService.addNilai(nilai);
	// 					}
	// 				}
	// 			}
	// 		}

	// 	};
	// }

	private void addAdminIfEmpty(AdminService adminService, BCryptPasswordEncoder encoder) {
		var admins = adminService.getAllAdmin();

		if (admins.size() != 0)
			return;

		var admin = new Admin();
		admin.setNama("admin");
		admin.setUsername("admin");
		admin.setPasswordAwal("password.");
		admin.setPassword(encoder.encode(admin.getPasswordAwal()));

		adminService.addAdmin(admin);
	}

}
