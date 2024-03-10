package propensi.proyek.properly.model;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class UpdatePassword {
    private String oldPassword;
    private String newPassword;
    private String confirmNewPassword;
}
