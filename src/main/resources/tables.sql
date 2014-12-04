drop view dependency;
drop table chronicle;

CREATE TABLE chronicle (
    id INT NOT NULL GENERATED ALWAYS AS IDENTITY (START WITH 1, INCREMENT BY 1) CONSTRAINT chron_pk PRIMARY KEY,
    parentId INT DEFAULT 0,
    iscurrent BOOLEAN DEFAULT false,
    tag VARCHAR(50) DEFAULT NULL,
    description VARCHAR(400) DEFAULT NULL,
    inserted TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);
create index parentIdIdx on chronicle(parentId);
create view dependency (p2id, c2id) as select p.id, c.id from CHRONICLE as p, CHRONICLE as c where p.ID=c.parentId;
INSERT INTO APP.CHRONICLE (PARENTID, TAG, DESCRIPTION, INSERTED) 
	VALUES (0, 'esgo4', 'project for banking', '2014-12-03 09:54:57.042');
INSERT INTO APP.CHRONICLE (PARENTID, TAG, DESCRIPTION, INSERTED) 
	VALUES (1, 'story', 'bug fixing', '2014-12-03 09:58:36.365');
INSERT INTO APP.CHRONICLE (PARENTID, TAG, DESCRIPTION, INSERTED) 
	VALUES (10, 'fault123423', 'insert optimisation properties', '2014-12-03 09:59:12.755');
INSERT INTO APP.CHRONICLE (PARENTID, TAG, DESCRIPTION, INSERTED) 
	VALUES (0, 'letsweb', 'own r&D', '2014-12-03 09:59:35.235');
INSERT INTO APP.CHRONICLE (PARENTID, TAG, DESCRIPTION, INSERTED) 
	VALUES (4, 'story - tasker', 'an application for monitoring work time', '2014-12-03 10:00:14.552');
INSERT INTO APP.CHRONICLE (PARENTID, TAG, DESCRIPTION, INSERTED) 
	VALUES (5, 'task improving sqls', 'self-linking table', '2014-12-03 10:00:57.878');
INSERT INTO APP.CHRONICLE (PARENTID, TAG, DESCRIPTION, INSERTED) 
	VALUES (3, 'blocker', 'no internet connection', '2014-12-03 10:01:20.208');
INSERT INTO APP.CHRONICLE (PARENTID, TAG, DESCRIPTION, INSERTED) 
	VALUES (3, 'internat is back', 'intervention of an IT team', '2014-12-03 10:29:44.614');
INSERT INTO APP.CHRONICLE (PARENTID, TAG, DESCRIPTION, INSERTED) 
	VALUES (3, 'internet blocked again', 'no internet connection, can''t work', '2014-12-03 11:52:49.958');
INSERT INTO APP.CHRONICLE (PARENTID, TAG, DESCRIPTION, INSERTED) 
	VALUES (1, 'story1', 'refrev', '2014-12-03 11:54:27.801');
select * from CHRONICLE;



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
select * from chronicle where id in (select p.id from CHRONICLE as p, CHRONICLE as c where p.ID=c.parentId or p.ID=302);
select * from chronicle where id in (select p.id from CHRONICLE as p, CHRONICLE as c where p.ID=c.parentId);
select * from chronicle where id in (select c.id from CHRONICLE as p, CHRONICLE as c where p.ID=c.parentId);
select * from chronicle where id in (select c.id from CHRONICLE as p, CHRONICLE as c where p.ID=c.parentId);
select * from chronicle where id in (select c.id from CHRONICLE as p, CHRONICLE as c where p.ID=c.parentId) or id in (select p.id from CHRONICLE as p, CHRONICLE as c where p.ID=c.parentId);
select * from chronicle where id in (select c.id from CHRONICLE as p, CHRONICLE as c where p.ID in (select p.id from CHRONICLE as p, CHRONICLE as c where p.ID = c.parentId));
select p.parentId, count(*) as "references" from CHRONICLE as p group by p.parentId;
select * from chronicle where id in (select p.ID from CHRONICLE as p group by p.parentId having count(*) > 1);

INSERT INTO CHRONICLE (parentId, TAG, DESCRIPTION, INSERTED) VALUES (0, 'esg04', 'banking', DEFAULT);

select p.id as "parent.id", p.TAG as "parent.tag", c.id as "child.id", c.TAG as "child.tag", c.DESCRIPTION from CHRONICLE as p, CHRONICLE as c where p.ID=c.parentId and (p.ID=311 or c.ID=311);
--find children to ID 
select p.id as "parent.id", p.TAG as "parent.tag", c.id as "child.id", c.TAG as "child.tag", c.DESCRIPTION from CHRONICLE as p, CHRONICLE as c where p.ID=c.parentId and (p.ID=311);
--find parent chain to ID
select p.id as "parent.id", p.TAG as "parent.tag", c.id as "child.id", c.TAG as "child.tag", c.DESCRIPTION from CHRONICLE as p, CHRONICLE as c where p.ID=c.parentId and (c.ID=311 or c.ID = (select p.id from CHRONICLE as p, CHRONICLE as c where p.ID=c.parentId and (c.ID=311)));
select p.id as "parent.id", p.TAG as "parent.tag", p.parentId as "parent.parentId", c.parentId as "child.parentId", c.id as "child.id", c.TAG as "child.tag", c.DESCRIPTION from CHRONICLE as p, CHRONICLE as c where p.ID=c.parentId;
select * from CHRONICLE where ID=304;
create view dependency (p2id, c2id) as select p.id, c.id from CHRONICLE as p, CHRONICLE as c where p.ID=c.parentId;
drop view dependency;
select * from DEPENDENCY;
select cid from DEPENDENCY where pid=304;
SELECT COUNT(p2id) as children FROM DEPENDENCY WHERE (p2id = 3); --if cnt > 0, then it has cnt children
-- get current
select * from CHRONICLE;
select id from CHRONICLE where ISCURRENT=true;
SELECT COUNT(p2id) as children FROM DEPENDENCY WHERE (p2id = (select id from CHRONICLE where ISCURRENT=true)); --if cnt > 0, then it has cnt children
SELECT C2ID FROM DEPENDENCY where P2ID=10;
-- find children to id=10
SELECT * FROM CHRONICLE WHERE ID = (SELECT C2ID FROM DEPENDENCY where P2ID=10);
SELECT * FROM CHRONICLE WHERE PARENTID=10;
-- find parent to id=10
SELECT * FROM CHRONICLE WHERE ID = (SELECT P2ID FROM DEPENDENCY where C2ID=10);
SELECT * FROM CHRONICLE WHERE ID = (select PARENTID from CHRONICLE WHERE ID=10);
-- find parent to id=9
SELECT * FROM CHRONICLE WHERE ID = (SELECT P2ID FROM DEPENDENCY where C2ID=9);