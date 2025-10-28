CREATE TABLE `notice` (
	`notice_id`	INT	NOT NULL	DEFAULT AUTO_INCREMENT,
	`admin_id`	INT	NOT NULL,
	`notice_title`	VARCHAR(30)	NOT NULL,
	`notice_content`	VARCHAR(100)	NOT NULL,
	`created_at`	DATETIME	NOT NULL	COMMENT 'CURRENT_TIMESTAMP',
	`updated_at`	DATETIME	NULL,
	`is_deleted`	BOOLEAN	NOT NULL	DEFAULT FALSE
);

CREATE TABLE `Ranking` (
	`user_id`	INT	NOT NULL	COMMENT '월간 랭킹으로만 상정',
	`rank`	INT	NOT NULL,
	`year`	YEAR(4)	NOT NULL,
	`month`	TINYINT	NOT NULL
);

CREATE TABLE `point_log` (
	`log_id`	INT	NOT NULL	DEFAULT AUTO_INCREMENT,
	`user_id`	INT	NOT NULL,
	`point_source_id`	INT	NOT NULL,
	`source_type`	ENUM	NOT NULL	COMMENT 'ENUM('CAPTURE','LIKE','SHOP')',
	`point_value`	INT	NOT NULL	DEFAULT 0	COMMENT '부여/차감된 포인트 값',
	`created_at`	DATETIME	NOT NULL	COMMENT 'CURRENT_TIMESTAMP'
);

CREATE TABLE `report` (
	`report_id`	INT	NOT NULL	DEFAULT AUTO_INCREMENT,
	`user_id`	INT	NOT NULL,
	`feed_id`	INT	NOT NULL,
	`admin_id`	INT	NOT NULL,
	`reason`	VARCHAR(30)	NOT NULL,
	`status`	ENUM	NOT NULL	COMMENT 'ENUM('PENDING','IN_PROGRESS','RESOLVED') DEFAULT 'PENDING'',
	`created_at`	DATETIME	NOT NULL	COMMENT 'CURRENT_TIMESTAMP',
	`updated_at`	BOOLEAN	NULL
);

CREATE TABLE `total_point` (
	`user_id`	INT	NOT NULL,
	`total_point`	INT	NOT NULL	DEFAULT 0
);

CREATE TABLE `region` (
	`region_code`	INT	NOT NULL	DEFAULT AUTO_INCREMENT,
	`region_name`	VARCHAR(30)	NOT NULL,
	`region_depth1`	VARCHAR(20)	NOT NULL	COMMENT '시/도',
	`region_depth2`	VARCHAR(20)	NOT NULL	COMMENT '시/군/구',
	`region_depth3`	VARCHAR(20)	NOT NULL	COMMENT '읍/면/동/리'
);

CREATE TABLE `item_storage` (
	`user_id`	INT	NOT NULL	DEFAULT AUTO_INCREMENT,
	`item_id`	INT	NOT NULL,
	`earned_at`	DATETIME	NOT NULL	DEFAULT CURRENT_TIMESTAMP,
	`is_equipped`	BOOLEAN	NOT NULL	DEFAULT TRUE
);

CREATE TABLE `territory` (
	`territory_id`	INT	NOT NULL	DEFAULT AUTO_INCREMENT,
	`user_id`	INT	NOT NULL,
	`capture_start_at`	DATETIME	NOT NULL	COMMENT 'CURRENT_TIEMSTAMP',
	`capture_end_at`	DATETIME	NULL	COMMENT 'CURRENT_TIEMSTAMP',
	`visit_count`	INT	NOT NULL	DEFAULT 0	COMMENT '해당 지역 누적 방문 수',
	`photo_url`	VARCHAR(255)	NULL	COMMENT '해당 지역 인증 사진'
);

CREATE TABLE `feed` (
	`feed_id`	INT	NOT NULL	DEFAULT AUTO_INCREMENT,
	`user_id`	INT	NOT NULL,
	`title`	VARCHAR(50)	NOT NULL,
	`content`	VARCHAR(255)	NOT NULL,
	`image_url`	VARCHAR(255)	NULL,
	`like_count`	INT	NOT NULL	DEFAULT 0,
	`report_count`	INT	NOT NULL	DEFAULT 0,
	`created_at`	DATETIME	NOT NULL	COMMENT 'CURRENT_TIMESTAMP',
	`updated_at`	DATETIME	NOT NULL	COMMENT 'CURRENT_TIMESTAMP'
);

CREATE TABLE `territory_visit_log` (
	`visit_id`	INT	NOT NULL	DEFAULT AUTO_INCREMENT,
	`territory_id`	INT	NOT NULL,
	`duration_minutes`	INT	NOT NULL	DEFAULT 0,
	`is_valid`	BOOLEAN	NOT NULL	DEFAULT FALSE,
	`visited_at`	DATETIME	NOT NULL	COMMENT 'CURRENT_TIEMSTAMP'
);

CREATE TABLE `admin` (
	`admin_id`	INT	NOT NULL	DEFAULT AUTO_INCREMENT,
	`admin_name`	VARCHAR(20)	NOT NULL,
	`admin_pw`	VARCHAR(30)	NOT NULL,
	`status`	ENUM	NOT NULL	COMMENT 'ENUM('ACTIVE','SUSPENDED','DELETED')',
	`created_at`	DATETIME	NOT NULL,
	`updated_at`	DATETIME	NULL
);

CREATE TABLE `alarm_log` (
	`alarm_id`	INT	NOT NULL	DEFAULT AUTO_INCREMENT,
	`user_id`	INT	NOT NULL,
	`alarm_type`	ENUM	NOT NULL	COMMENT 'ENUM('REPORT','POINT','FEED_LIKE')',
	`alarm_context`	VARCHAR(255)	NOT NULL,
	`read`	BOOLEAN	NOT NULL	DEFAULT FALSE,
	`created_at`	DATE	NOT NULL	COMMENT 'CURRENT_TIMESTAMP'
);

CREATE TABLE `user` (
	`user_id`	INT	NOT NULL	DEFAULT AUTO_INCREMENT,
	`login_type`	VARCHAR(10)	NOT NULL	DEFAULT ENUM('GOOGLE','KAKAO'),
	`user_name`	VARCHAR(20)	NOT NULL	COMMENT '한글,영어 이름',
	`user_pw`	VARCHAR(20)	NOT NULL	COMMENT '보류',
	`email`	VARCHAR(20)	NOT NULL	COMMENT '구글 로그인용 이메일 (소셜 ID)',
	`nickname`	VARCHAR(20)	NOT NULL,
	`gender`	CHAR(1)	NOT NULL,
	`profile_image`	BOOLEAN	NULL,
	`status`	ENUM	NOT NULL	DEFAULT ENUM('ACTIVE','SUSPENDED','DELETED'),
	`created_at`	DATETIME	NOT NULL	COMMENT 'CURRENT_TIMESTAMP',
	`updated_at`	DATETIME	NULL,
	`birth_date`	DATE	NOT NULL,
	`preffered_category`	ENUM	NOT NULL	COMMENT 'ENUM('자연',  '체험', '역사' ,'문화')',
	`preffered_season`	ENUM	NOT NULL	COMMENT 'ENUM('봄','여름','가을','겨울')'
);

CREATE TABLE `shop` (
	`item_id`	INT	NOT NULL	DEFAULT AUTO_INCREMENT,
	`region_code`	INT	NULL	COMMENT '해당지역 오픈/비오픈',
	`name`	VARCHAR(20)	NOT NULL	COMMENT '아이템 이름',
	`description`	VARCHAR(50)	NOT NULL	COMMENT '아이템 설명',
	`price`	INT	NOT NULL	DEFAULT 0	COMMENT '포인트 기준 가격',
	`category`	VARCHAR(10)	NOT NULL	COMMENT '(예: ‘배경’, ‘마커’, ‘테두리’)',
	`image_url`	VARCHAR(255)	NOT NULL	COMMENT '아이템 미리보기 이미지 경로',
	`is_active`	BOOLEAN	NOT NULL	DEFAULT TRUE	COMMENT '판매 중 여부'
);

CREATE TABLE `feed_like` (
	`user_id`	INT	NOT NULL,
	`feed_id`	INT	NOT NULL	DEFAULT AUTO_INCREMENT,
	`created_at`	DATETIME	NOT NULL	COMMENT 'CURRENT_TIMESTAMP'
);

CREATE TABLE `recommend` (
	`recommend_id`	INT	NOT NULL	DEFAULT AUTO_INCREMENT,
	`user_id`	INT	NOT NULL,
	`reason`	TEXT	NOT NULL,
	`recommend_at`	DATETIME	NOT NULL	COMMENT 'CURRENT_TIMESTAMP',
	`recommend_spot`	VARCHAR(500)	NOT NULL
);

ALTER TABLE `notice` ADD CONSTRAINT `PK_NOTICE` PRIMARY KEY (
	`notice_id`
);

ALTER TABLE `point_log` ADD CONSTRAINT `PK_POINT_LOG` PRIMARY KEY (
	`log_id`
);

ALTER TABLE `report` ADD CONSTRAINT `PK_REPORT` PRIMARY KEY (
	`report_id`
);

ALTER TABLE `total_point` ADD CONSTRAINT `PK_TOTAL_POINT` PRIMARY KEY (
	`user_id`
);

ALTER TABLE `region` ADD CONSTRAINT `PK_REGION` PRIMARY KEY (
	`region_code`
);

ALTER TABLE `territory` ADD CONSTRAINT `PK_TERRITORY` PRIMARY KEY (
	`territory_id`
);

ALTER TABLE `feed` ADD CONSTRAINT `PK_FEED` PRIMARY KEY (
	`feed_id`
);

ALTER TABLE `territory_visit_log` ADD CONSTRAINT `PK_TERRITORY_VISIT_LOG` PRIMARY KEY (
	`visit_id`
);

ALTER TABLE `admin` ADD CONSTRAINT `PK_ADMIN` PRIMARY KEY (
	`admin_id`
);

ALTER TABLE `alarm_log` ADD CONSTRAINT `PK_ALARM_LOG` PRIMARY KEY (
	`alarm_id`
);

ALTER TABLE `user` ADD CONSTRAINT `PK_USER` PRIMARY KEY (
	`user_id`
);

ALTER TABLE `shop` ADD CONSTRAINT `PK_SHOP` PRIMARY KEY (
	`item_id`
);

ALTER TABLE `recommend` ADD CONSTRAINT `PK_RECOMMEND` PRIMARY KEY (
	`recommend_id`
);

ALTER TABLE `notice` ADD CONSTRAINT `FK_admin_TO_notice_1` FOREIGN KEY (
	`admin_id`
)
REFERENCES `admin` (
	`admin_id`
);

ALTER TABLE `Ranking` ADD CONSTRAINT `FK_user_TO_Ranking_1` FOREIGN KEY (
	`user_id`
)
REFERENCES `user` (
	`user_id`
);

ALTER TABLE `point_log` ADD CONSTRAINT `FK_total_point_TO_point_log_1` FOREIGN KEY (
	`user_id`
)
REFERENCES `total_point` (
	`user_id`
);

ALTER TABLE `report` ADD CONSTRAINT `FK_user_TO_report_1` FOREIGN KEY (
	`user_id`
)
REFERENCES `user` (
	`user_id`
);

ALTER TABLE `report` ADD CONSTRAINT `FK_feed_TO_report_1` FOREIGN KEY (
	`feed_id`
)
REFERENCES `feed` (
	`feed_id`
);

ALTER TABLE `report` ADD CONSTRAINT `FK_admin_TO_report_1` FOREIGN KEY (
	`admin_id`
)
REFERENCES `admin` (
	`admin_id`
);

ALTER TABLE `total_point` ADD CONSTRAINT `FK_user_TO_total_point_1` FOREIGN KEY (
	`user_id`
)
REFERENCES `user` (
	`user_id`
);

ALTER TABLE `item_storage` ADD CONSTRAINT `FK_user_TO_item_storage_1` FOREIGN KEY (
	`user_id`
)
REFERENCES `user` (
	`user_id`
);

ALTER TABLE `item_storage` ADD CONSTRAINT `FK_shop_TO_item_storage_1` FOREIGN KEY (
	`item_id`
)
REFERENCES `shop` (
	`item_id`
);

ALTER TABLE `territory` ADD CONSTRAINT `FK_user_TO_territory_1` FOREIGN KEY (
	`user_id`
)
REFERENCES `user` (
	`user_id`
);

ALTER TABLE `feed` ADD CONSTRAINT `FK_user_TO_feed_1` FOREIGN KEY (
	`user_id`
)
REFERENCES `user` (
	`user_id`
);

ALTER TABLE `territory_visit_log` ADD CONSTRAINT `FK_territory_TO_territory_visit_log_1` FOREIGN KEY (
	`territory_id`
)
REFERENCES `territory` (
	`territory_id`
);

ALTER TABLE `alarm_log` ADD CONSTRAINT `FK_user_TO_alarm_log_1` FOREIGN KEY (
	`user_id`
)
REFERENCES `user` (
	`user_id`
);

ALTER TABLE `shop` ADD CONSTRAINT `FK_region_TO_shop_1` FOREIGN KEY (
	`region_code`
)
REFERENCES `region` (
	`region_code`
);

ALTER TABLE `feed_like` ADD CONSTRAINT `FK_user_TO_feed_like_1` FOREIGN KEY (
	`user_id`
)
REFERENCES `user` (
	`user_id`
);

ALTER TABLE `feed_like` ADD CONSTRAINT `FK_feed_TO_feed_like_1` FOREIGN KEY (
	`feed_id`
)
REFERENCES `feed` (
	`feed_id`
);

ALTER TABLE `recommend` ADD CONSTRAINT `FK_user_TO_recommend_1` FOREIGN KEY (
	`user_id`
)
REFERENCES `user` (
	`user_id`
);

