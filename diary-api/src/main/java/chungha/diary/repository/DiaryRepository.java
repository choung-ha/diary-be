package chungha.diary.repository;

import chungha.diarycommon.entity.Diary;
import lombok.RequiredArgsConstructor;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;

@RequiredArgsConstructor
@Repository
public class DiaryRepository {
    private final MongoTemplate mongoTemplate;

    public void insertDiary(Diary diary) {
        mongoTemplate.insert(diary);
    }

    public List<Diary> getAllDiary() {
        return mongoTemplate.findAll(Diary.class);
    }

    public Diary getDiaryById(String diaryId) {
        return mongoTemplate.findById(diaryId, Diary.class);
    }
}
