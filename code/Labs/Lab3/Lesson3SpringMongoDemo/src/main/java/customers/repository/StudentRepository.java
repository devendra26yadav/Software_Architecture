package customers.repository;

import customers.domain.Student;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface StudentRepository extends MongoRepository<Student, String> {

    List<Student> findByName(String name);
    Student findByPhoneNumber(String phoneNumber);
    List<Student> findByAddressCity(String city);
}
