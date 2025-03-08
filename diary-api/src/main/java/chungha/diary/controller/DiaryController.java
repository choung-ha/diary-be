package chungha.diary.controller;

import chungha.diary.model.request.DiaryCreateReq;
import chungha.diary.model.request.DiaryDeleteReq;
import chungha.diary.model.request.DiaryUpdateReq;
import chungha.diary.service.DiaryService;
import chungha.diarycommon.entity.Diary;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/diary")
public class DiaryController {

    private final DiaryService diaryService;

    @PostMapping()
    @ResponseStatus(HttpStatus.CREATED)
    public void createDiary(@RequestBody DiaryCreateReq req) {
        diaryService.createDiary(req);
    }

    @GetMapping()
    @ResponseStatus(HttpStatus.OK)
    public Page<Diary> getAllDiary(@RequestParam(defaultValue = "1") int page,
                                   @RequestParam(defaultValue = "5") int pageSize) {
        return diaryService.getAllDiary(page, pageSize);
    }

    @GetMapping("/{diaryId}")
    @ResponseStatus(HttpStatus.OK)
    public Diary getDiaryById(@PathVariable String diaryId) {
        return diaryService.getDiaryById(diaryId);
    }

    @PatchMapping()
    @ResponseStatus(HttpStatus.OK)
    public Diary updateDiary(@RequestBody DiaryUpdateReq req) {
        return diaryService.updateDiary(req);
    }

    @DeleteMapping()
    @ResponseStatus(HttpStatus.GONE)
    public void deleteDiaryById(@RequestBody DiaryDeleteReq req) {
        diaryService.deleteDiary(req);
    }
}
