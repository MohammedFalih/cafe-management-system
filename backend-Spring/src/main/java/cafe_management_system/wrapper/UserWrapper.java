package cafe_management_system.wrapper;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserWrapper {

    private Integer Id;

    private String name;

    private String email;
     
    private String contactNumber;

    private String password;

    private String status;
}
