package net.tysonsorensen.userAccount.data;

import net.tysonsorensen.userAccount.data.entities.RoleEntity;
import net.tysonsorensen.userAccount.data.repositories.RoleRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.Optional;

import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class StartUpTest {

    @Test
    public void testOnApplicationEvent_alreadyCreated() {
        RoleRepository mockRR = mock(RoleRepository.class);
        RoleEntity userEntity = new RoleEntity();
        userEntity.setRoleName("USER");
        RoleEntity adminEntity = new RoleEntity();
        adminEntity.setRoleName("ADMIN");
        doReturn(Optional.of(userEntity)).when(mockRR).findByRoleName("user");
        doReturn(Optional.of(adminEntity)).when(mockRR).findByRoleName("admin");

        StartUp uut = new StartUp(mockRR);
        uut.onApplicationEvent(null);

        verify(mockRR, times(0)).save(any());
    }

    @Test
    public void testOnApplicationEvent() {
        RoleRepository mockRR = mock(RoleRepository.class);
        doReturn(Optional.empty()).when(mockRR).findByRoleName("user");
        doReturn(Optional.empty()).when(mockRR).findByRoleName("admin");

        StartUp uut = new StartUp(mockRR);
        uut.onApplicationEvent(null);

        verify(mockRR, times(2)).save(any());
    }
}