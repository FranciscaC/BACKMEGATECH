package gt.com.megatech.service.implementation;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.auth0.jwt.interfaces.DecodedJWT;
import gt.com.megatech.presentation.dto.AuthCreateUserRequestDTO;
import gt.com.megatech.presentation.dto.AuthLoginRequestDTO;
import gt.com.megatech.presentation.dto.AuthResponseDTO;
import gt.com.megatech.persistence.entity.RoleEntity;
import gt.com.megatech.persistence.entity.UserEntity;
import gt.com.megatech.persistence.repository.IRoleRepository;
import gt.com.megatech.persistence.repository.IUserRepository;
import gt.com.megatech.presentation.dto.UserProfileResponseDTO;
import gt.com.megatech.util.constant.SecurityConstant;
import gt.com.megatech.util.jwt.JwtUtils;
import lombok.RequiredArgsConstructor;

import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserDetailServiceImplementation implements UserDetailsService {

    public static final String DOES_NOT_EXIST = "does not exist.";
    private final IRoleRepository iRoleRepository;
    private final IUserRepository iUserRepository;
    private final JwtUtils jwtUtils;
    private final PasswordEncoder passwordEncoder;

    @Override
    public UserDetails loadUserByUsername(
            String username
    ) throws UsernameNotFoundException {
        UserEntity userEntity = iUserRepository.findUserEntityByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("The user" + username + " " + DOES_NOT_EXIST));
        List<SimpleGrantedAuthority> authorityList = new ArrayList<>();
        userEntity
                .getRoles()
                .forEach(role -> authorityList.add(new SimpleGrantedAuthority(SecurityConstant.ROLE_PREFIX.concat(role.getRoleEnum().name()))));
        userEntity
                .getRoles()
                .stream()
                .flatMap(role -> role.getPermissionList().stream()).forEach(permission -> authorityList.add(new SimpleGrantedAuthority(permission.getName())));
        return new User(
                userEntity.getUsername(),
                userEntity.getPassword(),
                userEntity.isEnabled(),
                userEntity.isAccountNonExpired(),
                userEntity.isAccountNonLocked(),
                userEntity.isCredentialsNonExpired(),
                authorityList
        );
    }

    public AuthResponseDTO loginUser(
            AuthLoginRequestDTO authLoginRequestDTO
    ) {
        String username = authLoginRequestDTO.username();
        String password = authLoginRequestDTO.password();
        Authentication authentication = this.authenticate(username, password);
        SecurityContextHolder.getContext().setAuthentication(authentication);
        String accessToken = jwtUtils.createToken(authentication);
        return new AuthResponseDTO(
                username,
                "User logged successfully",
                accessToken,
                true
        );
    }

    public Authentication authenticate(
            String username,
            String password
    ) {
        UserDetails userDetails = this.loadUserByUsername(username);
        if (userDetails == null) {
            throw new BadCredentialsException("Invalid username or password.");
        }
        if (!passwordEncoder.matches(password, userDetails.getPassword())) {
            throw new BadCredentialsException("Invalid password.");
        }
        return new UsernamePasswordAuthenticationToken(
                username,
                userDetails.getPassword(),
                userDetails.getAuthorities()
        );
    }

    public AuthResponseDTO createUser(
            AuthCreateUserRequestDTO authCreateUserRequestDTO
    ) {
        String username = authCreateUserRequestDTO.username();
        String password = authCreateUserRequestDTO.password();
        List<String> roleRequest = authCreateUserRequestDTO.authCreateRoleRequestDTO().roleListName();
        Set<RoleEntity> roleEntitySet = new HashSet<>(iRoleRepository.findRoleEntitiesByRoleEnumIn(roleRequest));
        if (roleEntitySet.isEmpty()) {
            throw new IllegalArgumentException("The roles specified " + " " + DOES_NOT_EXIST);
        }
        UserEntity userEntity = UserEntity
                .builder()
                .username(username)
                .password(passwordEncoder.encode(password))
                .roles(roleEntitySet)
                .build();
        UserEntity userCreated = iUserRepository.save(userEntity);
        ArrayList<SimpleGrantedAuthority> authorityList = new ArrayList<>();
        userCreated
                .getRoles()
                .forEach(role -> authorityList.add(new SimpleGrantedAuthority(SecurityConstant.ROLE_PREFIX.concat(role.getRoleEnum().name()))));
        userCreated
                .getRoles()
                .stream()
                .flatMap(role -> role.getPermissionList().stream()).forEach(permission -> authorityList.add(new SimpleGrantedAuthority(permission.getName())));
        Authentication authentication = new UsernamePasswordAuthenticationToken(
                userCreated.getUsername(),
                userCreated.getPassword(),
                authorityList
        );
        String accessToken = jwtUtils.createToken(authentication);
        return new AuthResponseDTO(
                userCreated.getUsername(),
                "User created successfully",
                accessToken,
                true
        );
    }

    public AuthResponseDTO updateUser(
            String username,
            AuthCreateUserRequestDTO authCreateUserRequestDTO
    ) {
        UserEntity existingUser = iUserRepository.findUserEntityByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("The user " + username + " " + DOES_NOT_EXIST));
        existingUser.setUsername(authCreateUserRequestDTO.username());
        if (authCreateUserRequestDTO.password() != null && !authCreateUserRequestDTO.password().isEmpty()) {
            existingUser.setPassword(passwordEncoder.encode(authCreateUserRequestDTO.password()));
        }
        UserEntity updatedUser = iUserRepository.save(existingUser);
        ArrayList<SimpleGrantedAuthority> authorityList = new ArrayList<>();
        updatedUser
                .getRoles()
                .forEach(role -> authorityList.add(new SimpleGrantedAuthority(SecurityConstant.ROLE_PREFIX.concat(role.getRoleEnum().name()))));
        updatedUser
                .getRoles()
                .stream()
                .flatMap(role -> role.getPermissionList().stream()).forEach(permission -> authorityList.add(new SimpleGrantedAuthority(permission.getName())));
        Authentication authentication = new UsernamePasswordAuthenticationToken(updatedUser.getUsername(), updatedUser.getPassword(), authorityList);
        String accessToken = jwtUtils.createToken(authentication);
        return new AuthResponseDTO(
                updatedUser.getUsername(),
                "User updated successfully",
                accessToken,
                true
        );
    }

    public void deleteUser(String username) {
        UserEntity existingUser = iUserRepository.findUserEntityByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("The user " + username + " does not exist"));
        existingUser.getRoles().clear();
        iUserRepository.save(existingUser);
        iUserRepository.delete(existingUser);
    }

    public List<UserEntity> findAllUsers() {
        return iUserRepository.findAll();
    }

    public UserProfileResponseDTO getUserProfileFromToken(
            String token
    ) {
        String jwtToken = token.substring(7);
        DecodedJWT decodedJWT = jwtUtils.validatedToken(jwtToken);
        String username = decodedJWT.getSubject();
        return this.getUserProfile(
                username
        );
    }

    public UserProfileResponseDTO getUserProfile(
            String username
    ) {
        UserEntity userEntity = iUserRepository.findUserEntityByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User " + username + " does not exist."));
        List<String> roles = userEntity.getRoles()
                .stream()
                .map(roleEntity -> roleEntity.getRoleEnum().name())
                .toList();
        return new UserProfileResponseDTO
                (userEntity.getUsername(),
                        roles
                );
    }
}
