package chungha.diary.service;

import chungha.diary.model.DiaryRequest;
import chungha.diary.repository.DiaryRepository;
import chungha.diarycommon.entity.Diary;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class DiaryService {

    private final Logger logger = LoggerFactory.getLogger(this.getClass().getSimpleName());
    private final DiaryRepository diaryRepository;

    public void createDiary(DiaryRequest req) {
        Diary diary = Diary.builder()
                .title(req.title())
                .content(req.content())
                .emotion(req.emotion())
                .userId(req.userId())
                .build();

        logger.info(diary.toString());
        diaryRepository.insertDiary(diary);
    }

    public List<Diary> getAllDiary() {
        return diaryRepository.getAllDiary();
    }

    public Diary getDiaryById(String diaryId) {
        return diaryRepository.getDiaryById(diaryId);
    }
}
