package mmosii.bookstore.repository.user;

import mmosii.bookstore.model.Role;
import mmosii.bookstore.model.RoleName;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoleRepository extends JpaRepository<Role, Long> {
    Role findAllByName(RoleName name);
}
