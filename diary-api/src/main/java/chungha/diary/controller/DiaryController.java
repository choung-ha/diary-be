package chungha.diary.controller;

import java.time.LocalDate;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.format.annotation.DateTimeFormat;
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
import chungha.diarycommon.model.Emotion;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/diaries")
@Validated
public class DiaryController {

	private final DiaryService diaryService;

	@PostMapping()
	@ResponseStatus(HttpStatus.CREATED)
	public void createDiary(@Valid @RequestBody DiaryCreateReq req) {
		diaryService.createDiary(req);
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

	@GetMapping()
	@ResponseStatus(HttpStatus.OK)
	public PagedModel<EntityModel<DiaryRes>> searchDiaries(
		@RequestParam @ValidMongoId String userId,
		@PageableDefault(sort = "created_at", direction = Sort.Direction.DESC) Pageable pageable,
		PagedResourcesAssembler<DiaryRes> assembler) {
		return assembler.toModel(diaryService.searchDiaries(userId, pageable));
	}

	@GetMapping("/keyword")
	@ResponseStatus(HttpStatus.OK)
	public PagedModel<EntityModel<DiaryRes>> searchDiaries(
		@RequestParam @ValidMongoId String userId,
		@RequestParam String keyword,
		@PageableDefault(sort = "created_at", direction = Sort.Direction.DESC) Pageable pageable,
		PagedResourcesAssembler<DiaryRes> assembler) {
		return assembler.toModel(diaryService.findByKeyword(userId, pageable, keyword));
	}

	@GetMapping("/date")
	@ResponseStatus(HttpStatus.OK)
	public PagedModel<EntityModel<DiaryRes>> searchDiaries(
		@RequestParam @ValidMongoId String userId,
		@RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate start,
		@RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate end,
		@PageableDefault(sort = "created_at", direction = Sort.Direction.DESC) Pageable pageable,
		PagedResourcesAssembler<DiaryRes> assembler) {
		return assembler.toModel(diaryService.findByCreatedAtBetween(userId, pageable, start, end));
	}

	@GetMapping("/emotion")
	@ResponseStatus(HttpStatus.OK)
	public PagedModel<EntityModel<DiaryRes>> searchDiaries(
		@RequestParam @ValidMongoId String userId,
		@RequestParam Emotion emotion,
		@PageableDefault(sort = "created_at", direction = Sort.Direction.DESC) Pageable pageable,
		PagedResourcesAssembler<DiaryRes> assembler) {
		return assembler.toModel(diaryService.findByEmotion(userId, pageable, emotion));
	}
}
