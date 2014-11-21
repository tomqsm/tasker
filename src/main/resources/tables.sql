drop TABLE comment;
drop table chronicle;
CREATE TABLE chronicle (
    id INT NOT NULL GENERATED ALWAYS AS IDENTITY (START WITH 1, INCREMENT BY 1) CONSTRAINT chron_pk PRIMARY KEY,
    tag VARCHAR(50) DEFAULT NULL,
    description VARCHAR(400) DEFAULT NULL,
    inserted TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);
CREATE TABLE comment (
    id INT NOT NULL GENERATED ALWAYS AS IDENTITY (START WITH 1, INCREMENT BY 1) CONSTRAINT comment_pk PRIMARY KEY,
    chronicleId INT,
    description VARCHAR(400) DEFAULT NULL,
    inserted TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT chronicleId_fk FOREIGN KEY (chronicleId) REFERENCES chronicle (id)
);

select * from (select ROW_NUMBER() OVER() as CNT, chronicle.* from chronicle) AS CR where CNT > (select count (*) from chronicle) - 1;
select * from TUMCYK.CHRONICLE;
select * from COMMENT;
select * from (select ROW_NUMBER() OVER() as R, chronicle.* from chronicle) AS CR where R <= 2;
select * from (select ROW_NUMBER() OVER() as CNT, chronicle.* from chronicle) AS CR where CNT > (select count (*) from chronicle) - 2;
select * from (select ROW_NUMBER() OVER() as CNT, chronicle.* from chronicle) AS CR where CNT > (select count (*) from chronicle) - 2 order by CNT desc offset 1 rows;
select * from chronicle offset (select count (*) from chronicle)-2 rows;
select count (*) as cnt from chronicle;
select * from (select ROW_NUMBER() OVER() as CNT, chronicle.* from chronicle) AS CR where inserted between '2014-11-20 10:00:52.985' and '2014-11-20 23:09:52.985';
select * from chronicle where tag='work';
select * from (select ROW_NUMBER() OVER() as CNT, chronicle.* from chronicle) AS CR where CNT=1;
