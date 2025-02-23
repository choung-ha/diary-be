package diary.controller;

import diary.model.DiaryRequest;
import diary.service.DiaryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/diary")
public class DiaryController {

    private final DiaryService diaryService;

    @PostMapping()
    @ResponseStatus(HttpStatus.CREATED)
    public void createDiary(@RequestBody DiaryRequest req) {
        diaryService.createDiary(req);
    }
}
