package propensi.proyek.properly.Dto.semester;

import java.time.LocalDate;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class CreateSemesterDTO {
    private Boolean isGanjil;
    private LocalDate tanggalAwal;
    private LocalDate tanggalAkhir;
    private String tahunAjaran;

    @Override
    public String toString() {
        return "CreateSemesterDTO [isGanjil=" + isGanjil + ", tanggalAwal=" + tanggalAwal + ", tanggalAkhir="
                + tanggalAkhir + ", tahunAjaran=" + tahunAjaran + "]";
    }
}
