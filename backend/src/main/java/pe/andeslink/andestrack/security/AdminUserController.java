package pe.andeslink.andestrack.security;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
@RestController @RequestMapping("/api/admin/users")
public class AdminUserController {
    private final AccountService accounts;
    public AdminUserController(AccountService accounts){this.accounts=accounts;}
    @PostMapping @ResponseStatus(HttpStatus.CREATED) public UserView create(@Valid @RequestBody CreateUserRequest request){return accounts.create(request);}
}