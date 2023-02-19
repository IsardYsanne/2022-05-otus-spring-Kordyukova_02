package ru.otus.library.authentication;

import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.otus.library.model.entity.User;
import ru.otus.library.repository.UserRepository;

import java.util.Objects;

@Service
public class CustomUserDetailService implements UserDetailsService {

    private UserRepository userRepository;

    public CustomUserDetailService(final UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    @Transactional(readOnly=true)
    public UserDetails loadUserByUsername(String userName) {
        User user = userRepository.findUserByUserName(userName);
        if (Objects.isNull(user)) {
            throw new UsernameNotFoundException("Cannot find user: " + userName);
        }
        return new org.springframework.security.core.userdetails.User(
                userName,
                user.getPassword(),
                AuthorityUtils.createAuthorityList(user.getRole()));
    }
}
