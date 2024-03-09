package propensi.proyek.properly.Dto.semester;

import java.util.UUID;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class UpdateSemesterDTO extends CreateSemesterDTO {
    private UUID id;
}
