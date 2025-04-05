// mongosh < diary_index.js
// 사용할 컬렉션 선택 (예: diary 컬렉션)
use diary // ← 실제 DB 이름으로 변경

// 1. 복합 인덱스: userid + context(text) + title(text)
db.diary.createIndex(
    {
        userid: 1,
        context: "text",
        title: "text"
    },
    {
        name: "idx_userid_content_title"
    }
);

// 2. 단일 인덱스: userid
db.diary.createIndex(
    {
        userid: 1
    },
    {
        name: "idx_userid"
    }
);