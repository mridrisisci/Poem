package app.dtos;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class AccountDTO
{
    private String username;
    private String password;
    private Set<String> roles;

    public AccountDTO(String username, String password)
    {
        this.username = username;
        this.password = password;
    }
    public AccountDTO(String username, Set<String> roles)
    {
        this.username = username;
        this.roles = roles;
    }

}
