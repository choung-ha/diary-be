package diary.service;

import chungha.diarycommon.entity.Diary;
import diary.model.DiaryRequest;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class DiaryService {

    private final Logger logger = (Logger) LoggerFactory.getLogger(this.getClass().getSimpleName());
    private final MongoTemplate mongoTemplate;

    public void createDiary(DiaryRequest req) {
        Diary diary = Diary.builder()
                .title(req.title())
                .content(req.content())
                .emotion(req.emotion())
                .feedback(req.feedback())
                .build();

        logger.info(diary.toString());
        diaryRepository.save(diary);
    }
}
