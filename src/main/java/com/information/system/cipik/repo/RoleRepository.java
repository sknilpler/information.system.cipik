package com.information.system.cipik.repo;

import com.information.system.cipik.models.Role;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoleRepository extends JpaRepository<Role, Long> {

    /**
     * Найти роль пользователя по имени
     *
     * @param name имя роли
     * @return роль
     */
    Role findByName(String name);
}
