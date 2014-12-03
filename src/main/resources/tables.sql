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
select * from chronicle where id in (select c.id from CHRONICLE as p, CHRONICLE as c where p.ID=c.SUBTO);
select * from chronicle where id in (select c.id from CHRONICLE as p, CHRONICLE as c where p.ID=c.SUBTO) or id in (select p.id from CHRONICLE as p, CHRONICLE as c where p.ID=c.SUBTO);
select * from chronicle where id in (select c.id from CHRONICLE as p, CHRONICLE as c where p.ID in (select p.id from CHRONICLE as p, CHRONICLE as c where p.ID = c.SUBTO));
select p.SUBTO, count(*) as "references" from CHRONICLE as p group by p.SUBTO;
select * from chronicle where id in (select p.ID from CHRONICLE as p group by p.SUBTO having count(*) > 1);

select * from CHRONICLE;
INSERT INTO CHRONICLE (SUBTO, TAG, DESCRIPTION, INSERTED) VALUES (0, 'esg04', 'banking', DEFAULT);

select p.id as "parent.id", p.TAG as "parent.tag", c.id as "child.id", c.TAG as "child.tag", c.DESCRIPTION from CHRONICLE as p, CHRONICLE as c where p.ID=c.SUBTO and (p.ID=311 or c.ID=311);
--find children to ID 
select p.id as "parent.id", p.TAG as "parent.tag", c.id as "child.id", c.TAG as "child.tag", c.DESCRIPTION from CHRONICLE as p, CHRONICLE as c where p.ID=c.SUBTO and (p.ID=311);
--find parent chain to ID
select p.id as "parent.id", p.TAG as "parent.tag", c.id as "child.id", c.TAG as "child.tag", c.DESCRIPTION from CHRONICLE as p, CHRONICLE as c where p.ID=c.SUBTO and (c.ID=311 or c.ID = (select p.id from CHRONICLE as p, CHRONICLE as c where p.ID=c.SUBTO and (c.ID=311)));
select p.id as "parent.id", p.TAG as "parent.tag", p.SUBTO as "parent.subto", c.SUBTO as "child.subto", c.id as "child.id", c.TAG as "child.tag", c.DESCRIPTION from CHRONICLE as p, CHRONICLE as c where p.ID=c.SUBTO;
select * from CHRONICLE where SUBTO=311;

--is parent (has child/children - hasn't got a child) hence is parent by default
-- is not parent (doesn't exist)
-- is not parent (is a stage)
--is child (has a parent)