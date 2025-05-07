-- 1) 회원 정보
CREATE TABLE MEMBER (
  PART_ID    VARCHAR2(20) NOT NULL,
  PART_PW    VARCHAR2(20) NOT NULL,
  PART_NAME  VARCHAR2(20) NOT NULL,
  EMAIL      VARCHAR2(30) NOT NULL,
  PHONE      VARCHAR2(20) NOT NULL,
  CONSTRAINT PK_MEMBER PRIMARY KEY (PART_ID)
);

-- 2) 여행 패키지
CREATE TABLE TOURS (
  TOUR_ID          NUMBER        NOT NULL,
  TOUR_NAME        VARCHAR2(20)  NOT NULL,
  START_DATE       DATE          NOT NULL,
  END_DATE         DATE          NOT NULL,
  TOUR_PRICE       NUMBER        NOT NULL,
  MAX_PARTICIPANTS NUMBER        NOT NULL,
  TOUR_INFO        VARCHAR2(200),
  CONSTRAINT PK_TOURS PRIMARY KEY (TOUR_ID)
);

-- 3) 예약 신청
CREATE TABLE BOOKING (
  BOOKING_NO     NUMBER        NOT NULL,
  TOUR_ID        NUMBER        NOT NULL,
  PART_ID        VARCHAR2(20)  NOT NULL,
  BOOKING_DATE   DATE          NOT NULL,
  BOOKING_STATUS VARCHAR2(10),
  PAID_AMOUNT    NUMBER        NOT NULL,
  CONSTRAINT PK_BOOKING PRIMARY KEY (BOOKING_NO),
  CONSTRAINT FK_BOOKING_TOURS
    FOREIGN KEY (TOUR_ID) REFERENCES TOURS (TOUR_ID),
  CONSTRAINT FK_BOOKING_MEMBER
    FOREIGN KEY (PART_ID) REFERENCES MEMBER (PART_ID)
);
ALTER TABLE booking
  MODIFY booking_status VARCHAR2(20);

ALTER TABLE booking
ADD CONSTRAINT unq_booking_part_tour
  UNIQUE (part_id, tour_id);

commit;

ALTER TABLE BOOKING
  MODIFY BOOKING_STATUS DEFAULT 'BOOKED';

-- 4) 여행 일정
CREATE TABLE SCHEDULE (
  TOUR_DAY   DATE         NOT NULL,
  TOUR_ID    NUMBER       NOT NULL,
  ACTIVITY   VARCHAR2(50) NOT NULL,
  TOUR_TIME  VARCHAR2(10) NOT NULL,
  CONSTRAINT PK_SCHEDULE PRIMARY KEY (TOUR_DAY, TOUR_ID),
  CONSTRAINT FK_SCHEDULE_TOURS
    FOREIGN KEY (TOUR_ID) REFERENCES TOURS (TOUR_ID)
);
ALTER TABLE SCHEDULE
  MODIFY ACTIVITY VARCHAR2(200);


ALTER TABLE schedule DROP CONSTRAINT pk_schedule;
ALTER TABLE schedule ADD CONSTRAINT pk_schedule
  PRIMARY KEY(tour_day, tour_id, tour_time);

create table admin(
    admin_id    VARCHAR2(20) NOT NULL,
    admin_PW    VARCHAR2(20) NOT NULL,
    CONSTRAINT PK_ADMIN PRIMARY KEY (admin_id)
);

insert into admin values('admin', 'admin');
select * from admin;

select * from tours;


-- 1) 예약 등록 전, tours.max_participants 검사 및 감소 트리거
create or replace TRIGGER trg_before_insert_booking
BEFORE INSERT ON booking
FOR EACH ROW
DECLARE
  v_max NUMBER;
BEGIN
  -- 1) 현재 남은 정원 조회 (FOR UPDATE 로 행 잠금)
    SELECT max_participants INTO v_max
    FROM tours
    WHERE tour_id = :NEW.tour_id
    FOR UPDATE;

  -- 2) 정원 부족 시 예외 발생
  IF v_max <= 0 THEN
    dbms_output.put_line( '예약 불가: 정원(남은좌석) 초과');
  END IF;
  
  -- 3) 예약 성공 상태로 변경
--  :NEW.booking_status := 'BOOKED'; -- default로 변경
  
  -- 4) 결제금액을 해당 투어 가격으로 설정
  SELECT tour_price
    INTO :NEW.paid_amount
    FROM tours
   WHERE tour_id = :NEW.tour_id;

  -- 5) 정원 1 감소
  UPDATE tours
     SET max_participants = max_participants - 1
   WHERE tour_id = :NEW.tour_id;
   
   --6) 예약 번호 갱신
   :new.booking_no := seq_booking.nextval;
END;
/

-- 시퀀스 넘버
create sequence seq_booking START WITH 10000;

-- 예약이 삭제(취소)되면 tours.max_participants를 +1 해 주는 트리거
CREATE OR REPLACE TRIGGER trg_after_delete_booking
AFTER DELETE ON booking
FOR EACH ROW
BEGIN
  UPDATE tours
     SET max_participants = max_participants + 1
   WHERE tour_id = :OLD.tour_id;
END;
/
commit;


select * from schedule where tour_id =9105;

select * from tours;
update tours set tours.max_participants = 1 where tour_id = 9105;
commit;
select * from booking;
select * from member;

SELECT part_id FROM member WHERE part_id = 'codnjs' AND part_pw = '1234';
