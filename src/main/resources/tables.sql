drop TABLE comment;
drop table chronicle;
CREATE TABLE chronicle (
    id INT NOT NULL GENERATED ALWAYS AS IDENTITY (START WITH 1, INCREMENT BY 1) CONSTRAINT chron_pk PRIMARY KEY,
    subto INT DEFAULT 0,
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
select * from CHRONICLE;
select * from COMMENT;
select * from (select ROW_NUMBER() OVER() as R, chronicle.* from chronicle) AS CR where R <= 2;
select * from (select ROW_NUMBER() OVER() as CNT, chronicle.* from chronicle) AS CR where CNT > (select count (*) from chronicle) - 2;
select * from (select ROW_NUMBER() OVER() as CNT, chronicle.* from chronicle) AS CR where CNT > (select count (*) from chronicle) - 2 order by CNT desc offset 1 rows;
select * from chronicle offset (select count (*) from chronicle)-2 rows;
select count (*) as cnt from chronicle;
select * from (select ROW_NUMBER() OVER() as CNT, chronicle.* from chronicle) AS CR where inserted between '2014-12-01 09:00:52.985' and '2014-12-01 12:09:52.985';
select * from (select ROW_NUMBER() OVER() as CNT, chronicle.* from chronicle where inserted between '2014-12-01 09:00:52.985' and '2014-12-01 12:09:52.985') AS CR;
select * from chronicle where tag!='work' and tag!='break';
select * from (select ROW_NUMBER() OVER() as CNT, chronicle.* from chronicle) AS CR where CNT=1;
select * from (select ROW_NUMBER() OVER() as CNT, chronicle.* from chronicle where tag='work') AS CR where CNT > (select count (*) from chronicle where tag='work')-3 order by CNT desc;
select * from (select ROW_NUMBER() OVER() as CNT, chronicle.* from chronicle where tag!='work' and tag!='break') AS CR where CNT > (select count (*) from chronicle where tag!='work' and tag!='break')-2 order by CNT desc;
select * from chronicle where id in (select p.id from CHRONICLE as p, CHRONICLE as c where p.ID=c.SUBTO or p.ID=302);
select * from chronicle where id in (select p.id from CHRONICLE as p, CHRONICLE as c where p.ID=c.SUBTO);
select * from chronicle where id in (select c.id from CHRONICLE as p, CHRONICLE as c where p.ID=c.SUBTO);
select * from chronicle where id in (select c.id from CHRONICLE as p, CHRONICLE as c where p.ID=c.SUBTO) or id in (select p.id from CHRONICLE as p, CHRONICLE as c where p.ID=c.SUBTO);
select p.id, c.subto from CHRONICLE as p, CHRONICLE as c where p.ID=c.SUBTO;