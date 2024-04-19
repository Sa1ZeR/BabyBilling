package com.nexign.babybilling.crmservice.service;

import com.nexign.babybilling.CustomerRole;
import com.nexign.babybilling.crmservice.domain.CustomerDetails;
import com.nexign.babybilling.payload.dto.CustomerDto;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import java.util.Collection;

@Service
@RequiredArgsConstructor
public class UserDetailsServiceImpl implements UserDetailsService {

    private final CustomerService customerService;

    /**
     * Создаем UserDetails по переданному номеру абонента
     * @param username the username identifying the user whose data is required.
     * @return UserDetails преобразованный абонент
     * @throws UsernameNotFoundException
     */
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        CustomerDto customer = customerService.findByMsisnd(username);
        if(ObjectUtils.isEmpty(customer))
            throw new UsernameNotFoundException(String.format("Customer with msisnd %s not found", username));
        return CustomerDetails.builder()
                .misidn(customer.msisnd())
                .authorities(getAuthorities(customer.roles()))
                .build();
    }

    /**
     * Преобразование ролей в {@link GrantedAuthority}
     * @param roles роли абонента
     * @return список {@link GrantedAuthority}
     */
    private Collection<? extends GrantedAuthority> getAuthorities(Collection<CustomerRole> roles) {
        return roles.stream().map(r -> new SimpleGrantedAuthority(r.name())).toList();
    }
}
