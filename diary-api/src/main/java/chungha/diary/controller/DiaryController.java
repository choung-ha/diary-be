package chungha.diary.controller;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.PagedModel;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
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

import chungha.diary.model.request.DiaryCreateReq;
import chungha.diary.model.request.DiaryDeleteReq;
import chungha.diary.model.request.DiaryUpdateReq;
import chungha.diary.model.response.DiaryRes;
import chungha.diary.service.DiaryService;
import chungha.diary.util.validation.annotation.ValidMongoId;
import chungha.diarycommon.entity.Diary;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/diary")
@Validated
public class DiaryController {

	private final DiaryService diaryService;

	@PostMapping()
	@ResponseStatus(HttpStatus.CREATED)
	public void createDiary(@Valid @RequestBody DiaryCreateReq req) {
		diaryService.createDiary(req);
	}

	@GetMapping()
	@ResponseStatus(HttpStatus.OK)
	public PagedModel<EntityModel<DiaryRes>> getAllDiary(
		@RequestParam @ValidMongoId String userId,
		@PageableDefault(sort = "created_at", direction = Sort.Direction.DESC) Pageable pageable,
		PagedResourcesAssembler<DiaryRes> assembler) {
		return assembler.toModel(diaryService.getAllDiary(userId, pageable));
	}

	@GetMapping("/{diaryId}")
	@ResponseStatus(HttpStatus.OK)
	public Diary getDiaryById(@PathVariable @ValidMongoId String diaryId) {
		return diaryService.getDiaryById(diaryId);
	}

	@PatchMapping()
	@ResponseStatus(HttpStatus.OK)
	public Diary updateDiary(@Valid @RequestBody DiaryUpdateReq req) {
		return diaryService.updateDiary(req);
	}

	@DeleteMapping()
	@ResponseStatus(HttpStatus.GONE)
	public void deleteDiaryById(@Valid @RequestBody DiaryDeleteReq req) {
		diaryService.deleteDiary(req);
	}
}
