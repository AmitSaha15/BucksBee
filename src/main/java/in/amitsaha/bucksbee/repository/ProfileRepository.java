package in.amitsaha.bucksbee.repository;

import in.amitsaha.bucksbee.entity.ProfileEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ProfileRepository extends JpaRepository<ProfileEntity, Long> {

//    under the hood it's just a SQL query -->
//    select * from tbl_profiles where email = ?(parameter of the method)
    Optional<ProfileEntity> findByEmail(String email);
//    select * from tbl_profiles where activationToken = ? (method parameter)
    Optional<ProfileEntity> findByActivationToken(String activationToken);
}
