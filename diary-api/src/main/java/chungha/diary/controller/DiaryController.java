package chungha.diary.controller;

import chungha.diary.model.DiaryRequest;
import chungha.diary.service.DiaryService;
import chungha.diarycommon.entity.Diary;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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

    @GetMapping("/all")
    @ResponseStatus(HttpStatus.OK)
    public List<Diary> getAllDiary() { return diaryService.getAllDiary(); }

    @GetMapping()
    @ResponseStatus(HttpStatus.OK)
    public Diary getDiaryById(@RequestParam String id) {
        return diaryService.getDiaryById(id);
    }
}
