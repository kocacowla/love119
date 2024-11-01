package com.smu.love119.domain.user.repository;

import com.smu.love119.domain.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    Boolean existsByUsername(String username);

    @Query("SELECT u FROM User u WHERE u.username = :username AND u.deletedDate IS NULL")
    Optional<User> findByUsername(String username);
    Optional<User> findById(Long id);

    Boolean existsByNickname(String nickname);

    List<User> findByRole(User.RoleType role);

    boolean existsByUsernameAndDeletedDateIsNull(String username);

    boolean existsByNicknameAndDeletedDateIsNull(String nickname);
}
