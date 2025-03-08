package chungha.diary.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.core.FindAndModifyOptions;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.data.support.PageableExecutionUtils;
import org.springframework.stereotype.Repository;

import chungha.diary.model.request.DiaryUpdateReq;
import chungha.diarycommon.entity.Diary;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Repository
public class DiaryRepository {
	private final MongoTemplate mongoTemplate;

	public void saveDiary(Diary diary) {
		mongoTemplate.save(diary);
	}

	public Page<Diary> getAllDiary(Pageable pageable) {
		Query query = new Query().with(pageable);
		List<Diary> diaryList = mongoTemplate.find(query, Diary.class);
		return PageableExecutionUtils.getPage(diaryList, pageable, () -> mongoTemplate.count(query, Diary.class));
	}

	public Optional<Diary> getDiaryById(String diaryId) {
		return Optional.ofNullable(mongoTemplate.findById(diaryId, Diary.class));
	}

	public Diary updateDiary(DiaryUpdateReq req) {
		Query query = new Query(Criteria.where("_id").is(req.diaryId()));
		Update update = new Update();

		if (req.title() != null && !req.title().isEmpty()) {
			update.set("title", req.title());
		}
		if (req.content() != null && !req.content().isEmpty()) {
			update.set("content", req.content());
		}
		if (req.emotion() != null) {
			update.set("emotion", req.emotion());
		}

		FindAndModifyOptions options = new FindAndModifyOptions().returnNew(true); // 업데이트 후 객체 반환
		return mongoTemplate.findAndModify(query, update, options, Diary.class);
	}

	public void deleteDiaryById(String diaryId) {
		mongoTemplate.remove(new Query(Criteria.where("_id").is(diaryId)), Diary.class);
	}

}
