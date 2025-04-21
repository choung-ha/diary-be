package chungha.diary.repository;

import org.springframework.data.mongodb.repository.MongoRepository;

import chungha.diarycommon.entity.Diary;

public interface DiaryRepository extends MongoRepository<Diary, String>, DiaryCustomRepository {
}
