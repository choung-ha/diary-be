package diary.service;

import diary.entity.Diary;
import diary.model.DiaryRequest;
import diary.repository.DiaryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class DiaryService {
    private final DiaryRepository diaryRepository;

    public void createDiary(DiaryRequest req) {
        Diary diary = Diary.builder()
                .title(req.title())
                .content(req.content())
                .emotion(req.emotion())
                .feedback(req.feedback())
                .build();
        System.out.println(diary);
        diaryRepository.save(diary);
    }
}
