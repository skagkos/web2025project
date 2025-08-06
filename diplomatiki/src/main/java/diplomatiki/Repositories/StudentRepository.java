package diplomatiki.Repositories;

import diplomatiki.entity.Student;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StudentRepository extends JpaRepository<Student, Integer> {
    Student findByAm(String am);
}
