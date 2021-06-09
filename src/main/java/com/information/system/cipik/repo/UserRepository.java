package com.information.system.cipik.repo;

import com.information.system.cipik.models.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {

    /**
     * Найти пользователя по имени
     *
     * @param username имя пользователя
     * @return пользователь
     */
    User findByUsername(String username);
}
