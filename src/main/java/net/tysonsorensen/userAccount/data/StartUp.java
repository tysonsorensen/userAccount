package net.tysonsorensen.userAccount.data;

import lombok.RequiredArgsConstructor;
import net.tysonsorensen.userAccount.data.entities.RoleEntity;
import net.tysonsorensen.userAccount.data.repositories.RoleRepository;
import net.tysonsorensen.userAccount.data.repositories.UserRepository;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
@RequiredArgsConstructor
public class StartUp implements ApplicationListener<ApplicationReadyEvent> {

    private final RoleRepository roleRepository;

    @Override
    public void onApplicationEvent(final ApplicationReadyEvent event) {
        createRole("admin");
        createRole("user");
    }

    private void createRole(final String role) {
        Optional<RoleEntity> roleEntity = roleRepository.findByRoleName(role);
        if(!roleEntity.isPresent()) {
            RoleEntity newRole = new RoleEntity();
            newRole.setRoleName(role);
            roleRepository.save(newRole);
        }
    }
}
