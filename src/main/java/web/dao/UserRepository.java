package web.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import web.model.User;

import javax.transaction.Transactional;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    // Кастомный метод для поиска пользователя по email
    Optional<User> findByEmail(String email);

    // Кастомный метод для сброса идентификаторов при удалении всех записей
    @Modifying
    @Transactional
    @Query(value = "TRUNCATE TABLE users RESTART IDENTITY CASCADE", nativeQuery = true)
    void deleteAllUsersAndResetIds();
}
