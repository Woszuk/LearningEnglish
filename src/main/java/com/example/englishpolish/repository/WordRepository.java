package com.example.englishpolish.repository;

import com.example.englishpolish.entity.Word;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface WordRepository extends JpaRepository<Word, Long> {
    @Query(value = "SELECT * FROM word ORDER BY english_word ASC LIMIT :from, :to", nativeQuery = true)
    List<Word> allWordOrderByEnglishWord(@Param("from") int from, @Param("to") int to);

    @Query("SELECT w.englishWord FROM Word w WHERE w.englishWord=?1")
    String ifExistEnglishWord(String word);

    @Query("SELECT w.polishWord FROM Word w WHERE w.polishWord=?1")
    String ifExistPolishWord(String word);

    @Query("SELECT w.id FROM Word w WHERE w.englishWord NOT LIKE '%,%'")
    List<Long> selectWordWhichPolishWordHasOneMeaning();
}
