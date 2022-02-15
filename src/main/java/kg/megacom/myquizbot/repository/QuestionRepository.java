package kg.megacom.myquizbot.repository;

import kg.megacom.myquizbot.model.entity.Question;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@Transactional(readOnly = true)
public interface QuestionRepository extends JpaRepository<Question, Long> {
    @Query(nativeQuery = true, value = "SELECT *  FROM tb_quiz ORDER BY random() LIMIT 1")
    Question getRandomQuestion();
}
