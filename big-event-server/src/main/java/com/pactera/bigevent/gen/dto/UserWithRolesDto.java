package com.pactera.bigevent.gen.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserWithRolesDto {

    private Long userId;

    private String username;

    private String password;

    private String status;

    private String roleNames;

    public List<GrantedAuthority> roleNamesToList() {
        String[] roles = roleNames.split(",");
        ArrayList<GrantedAuthority> authorities = new ArrayList<>();
        for (String role : roles) {
            authorities.add(new SimpleGrantedAuthority(role.trim()));
        }
        return authorities;
    }

}
