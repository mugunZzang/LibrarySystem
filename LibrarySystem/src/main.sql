SHOW user;


SHOW con_name;

-- 사서 테이블 생성
create table tbl_librarian
(lib_seq        number           not null    -- 사서번호
,lib_id         varchar2(30)     not null    -- 아이디
,lib_passwd     varchar2(30)     not null    -- 비밀번호
,lib_name       Nvarchar2(10)    not null    -- 이름
,lib_tel        varchar2(20)     not null    -- 연락처
,lib_email      varchar2(50)                 -- 이메일
,lib_birth      date             not null    -- 생년월일
,constraint PK_tbl_librarian_lib_seq primary key(lib_seq)
,constraint UQ_tbl_librarian_lib_id unique(lib_id)
);

-- Table TBL_LIBRARIAN이(가) 생성되었습니다.

-- 사서 로그인
create table tbl_lib_login
(lib_seq      number             not null    -- 사서번호 
,lib_id       varchar2(30)       not null    -- 사서아이디
,lib_passwd   varchar2(30)       not null    -- 사서비밀번호
,constraint PK_tbl_lib_login_lib_seq primary key(lib_seq)
,constraint UQ_tbl_lib_login_lib_id unique(lib_id)
);

-- Table TBL_LIB_LOGIN이(가) 생성되었습니다.

-- 발주 테이블 생성
create table tbl_order
(orderno      number              not null    -- 발주번호
,lib_seq      number              not null    -- 사서번호
,bookname     Nvarchar2(50)       not null    -- 서명
,author       Nvarchar2(30)       not null    -- 저자
,count        number              default 1   -- 수량
,publisher    Nvarchar2(20)       not null    -- 출판사
,order_date   date                default sysdate    -- 발주일자
,constraint PK_tbl_order_orderno primary key(orderno)
,constraint FK_tbl_order_lib_seq foreign key(lib_seq) references TBL_LIBRARIAN(lib_seq)
);

-- Table TBL_ORDER이(가) 생성되었습니다.

-- 대여 테이블 생성
create table tbl_loan
(loan_no            number              not null           -- 대여번호
,lib_seq            number              not null           -- 사서번호
,user_seq           number              not null           -- 회원번호
,loan_date          date                default sysdate    -- 대여일자
,Return_Due_Date    date                not null           -- 반납기한일
,constraint PK_tbl_loan_loan_no primary key(loan_no)
,constraint FK_tbl_loan_lib_seq foreign key(lib_seq) references TBL_LIBRARIAN(lib_seq)
);

-- Table TBL_LOAN이(가) 생성되었습니다.

-- 대여상세 테이블 생성(물어봐야할것)
create table tbl_loan_detail
(LOAN_DETAIL_NO  number              not null    -- 대여상세번호
,LOAN_NO         number              not null    -- 대여번호
,BOOK_ID         number              not null    -- 도서ID
,RETURN          number(1)           not null    -- 반납
,constraint PK_tbl_loan_detail_LOAN_DETAIL_NO primary key(LOAN_DETAIL_NO)
,constraint FK_tbl_loan_detail_LOAN_NO foreign key(LOAN_NO) references tbl_loan(LOAN_NO)
,constraint FK_tbl_loan_detail_BOOK_ID foreign key(BOOK_ID) references TBL_LOAN_BOOK(BOOK_ID)
,constraint CK_tbl_loan_detail_RETURN CHECK (RETURN IN (0,1))
);

-- Table TBL_LOAN_DETAIL이(가) 생성되었습니다.

-- 대여도서 테이블 생성
create table TBL_LOAN_BOOK
(BOOK_ID            number             not null    -- 도서ID
,ISBN               number             not null    -- ISBN
,LOAN_STATUS        number(1)          not null    -- 대출여부
,BOOK_STATUS        NVARCHAR2(10)      not null    -- 상태
,constraint PK_TBL_LOAN_BOOK_BOOK_ID primary key(BOOK_ID)
,constraint FK_TBL_LOAN_BOOK_ISBN foreign key(ISBN) references TBL_BOOK(ISBN)
,constraint CK_TBL_LOAN_BOOK_LOAN_STATUS CHECK(LOAN_STATUS IN (0,1))
);

-- Table TBL_LOAN_BOOK이(가) 생성되었습니다.

-- 도서 테이블 생성
create table TBL_BOOK
(ISBN            number              not null    -- ISBN
,FK_CATEGORY_ID  NVARCHAR2(10)       not null    -- 카테고리아이디
,BOOK_NAME       NVARCHAR2(50)       not null    -- 도서명
,PUB_YEAR        DATE                not null    -- 발행년도
,CONTENTS        NVARCHAR2(100)      not null    -- 도서내용
,RENTAL_FEE      NUMBER              not null    -- 대여료
,AUTHOR          NVARCHAR2(30)       not null    -- 저자명
,PUBLISHER       NVARCHAR2(10)       not null    -- 출판사
,constraint PK_TBL_BOOK_ISBN primary key(ISBN)
,constraint FK_TBL_BOOK_FK_CATEGORY_ID foreign key(FK_CATEGORY_ID) references TBL_CATEGORY(CATEGORY_ID)
);

-- Table TBL_BOOK이(가) 생성되었습니다.

-- 카테고리 테이블 생성
create table TBL_CATEGORY
(CATEGORY_ID       NVARCHAR2(10)       not null    -- 카테고리아이디
,CATEGORY_NAME     NVARCHAR2(10)       not null    -- 카테고리명
,constraint PK_TBL_CATEGORY_CATEGORY_ID primary key(CATEGORY_ID)
);

-- Table TBL_CATEGORY이(가) 생성되었습니다.

-- 회원 테이블 생성
create table TBL_USER
(USER_SEQ       number          not null    -- 회원번호
,USER_ID        VARCHAR2(30)    not null    -- 아이디
,USER_PW        VARCHAR2(30)    not null    -- 비밀번호
,USER_NAME      NVARCHAR2(10)   not null    -- 이름
,USER_TEL       VARCHAR2(20)    not null    -- 연락처
,USER_EMAIL     VARCHAR2(50)    not null    -- 이메일
,USER_BIRTH     DATE            not null    -- 생년월일
,LOAN_STOP      NUMBER(1)       not null    -- 대출정지여부
,POINT          NUMBER          default 0   -- 포인트
,OVERDUE_FEE    NUMBER          default 0   -- 연체료
,constraint PK_TBL_USER_USER_SEQ primary key(USER_SEQ)
,constraint UQ_TBL_USER_USER_ID unique(USER_ID)
,constraint CK_TBL_USER_LOAN_STOP CHECK (LOAN_STOP IN (0,1))
);

-- Table TBL_USER이(가) 생성되었습니다.

-- 회원 로그인
create table TBL_USER_LOGIN
(USER_SEQ      number             not null    -- 회원번호 
,USER_ID       varchar2(30)       not null    -- 회원아이디
,USER_PW       varchar2(30)       not null    -- 회원비밀번호
,constraint PK_TBL_USER_LOGIN_USER_SEQ primary key(USER_SEQ)
,constraint UQ_TBL_USER_LOGIN_USER_ID unique(USER_ID)
);

-- Table TBL_USER_LOGIN이(가) 생성되었습니다.

-- 희망도서목록 생성
create table TBL_WISH_BOOK
(WISH_BOOK_NO          number          not null    -- 희망도서번호
,USER_SEQ              NUMBER          not null    -- 회원번호
,WISH_BOOK_NAME        NVARCHAR2(50)   not null    -- 도서명
,WISH_BOOK_AUTHOR      NVARCHAR2(30)   not null    -- 저자명
,WISH_BOOK_PUBLISHER   NVARCHAR2(10)   not null    -- 출판사
,REQUEST_DATE          DATE            default sysdate    -- 신청일
,constraint PK_TBL_WISH_BOOK_WISH_BOOK_NO primary key(WISH_BOOK_NO)
,constraint FK_TBL_WISH_BOOK_USER_SEQ foreign key(USER_SEQ) references TBL_USER(USER_SEQ) on delete cascade
);

-- Table TBL_WISH_BOOK이(가) 생성되었습니다.

-- 예약 테이블 생성
create table TBL_RESERVATION
(RESV_ID          number              not null           -- 예약번호
,FK_USER_SEQ       number              not null           -- 회원번호
,RESV_DATE        DATE                default sysdate    -- 예약등록일
,constraint PK_TBL_RESERVATION_RESV_ID primary key(RESV_ID)
,constraint FK_TBL_RESERVATION_FK_USERSEQ foreign key(FK_USER_SEQ) references TBL_USER(USER_SEQ) on delete cascade
);

-- Table TBL_RESERVATION이(가) 생성되었습니다.

-- 예약상세 테이블 생성
create table TBL_RESV_DETAIL
(RESV_DETAIL_ID    number         not null    -- 예약목록번호
,FK_RESV_ID        number         not null    -- 예약번호
,BOOK_ID           number         not null    -- 도서ID
,constraint PK_TBL_RESV_DETAIL_RESV_DETAIL_ID primary key(RESV_DETAIL_ID)
,constraint FK_TBL_RESV_DETAIL_FK_RESV_ID foreign key(FK_RESV_ID) references TBL_RESERVATION(RESV_ID) on delete cascade
,constraint FK_TBL_RESV_DETAIL_BOOK_ID foreign key(BOOK_ID) references TBL_LOAN_BOOK(BOOK_ID)
);

-- Table TBL_RESV_DETAIL이(가) 생성되었습니다.

-- 관심도서 테이블 생성
create table TBL_FAVORITE
(FK_USER_SEQ   number      not null    -- 회원번호
,ISBN         number      not null    -- ISBN
,constraint PK_TBL_FAVORITE_FK_USERSEQ_ISBN primary key(FK_USER_SEQ, ISBN)
,constraint FK_TBL_FAVORITE_FK_USERSEQ foreign key(FK_USER_SEQ) references TBL_USER(USER_SEQ) on delete cascade
,constraint FK_TBL_FAVORITE_ISBN foreign key(ISBN) references TBL_BOOK(ISBN)
);

-- Table TBL_FAVORITE이(가) 생성되었습니다.

CREATE sequence LIB_SEQ;
CREATE sequence USER_SEQ;
CREATE sequence ORDER_SEQ;
CREATE sequence ISBN;
CREATE sequence LOAN_DETAIL_NO;
CREATE sequence BOOK_ID; 
CREATE sequence LOAN_NO;
CREATE sequence RESV_ID;
CREATE sequence RESV_DETAIL_ID;
CREATE sequence WISH_BOOK_NO;

-- Sequence WISH_BOOK_NO이(가) 생성되었습니다.(10개 생성됨)