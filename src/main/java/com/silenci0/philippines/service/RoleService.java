package com.silenci0.philippines.service;

import com.silenci0.philippines.domain.entities.Role;
import com.silenci0.philippines.domain.models.service.RoleServiceModel;
import com.silenci0.philippines.domain.models.service.UserServiceModel;

import java.util.Set;

public interface RoleService {
    void seedRolesInDb();

    void assignUserRoles(UserServiceModel userServiceModel, long numberOfUsers);

    Set<RoleServiceModel> findAllRoles();

    RoleServiceModel findByAuthority(String authority);

    Role findByAuthorityReturnRole(String authority);
}
